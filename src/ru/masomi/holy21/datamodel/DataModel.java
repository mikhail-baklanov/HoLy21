package ru.masomi.holy21.datamodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.masomi.holy21.datamodel.objects.DataFile;
import android.util.Log;
import android.util.SparseArray;

import com.google.gson.Gson;

public class DataModel {

	private static int maxId = 0;
	private String baseDir;
	private FileAccessor fileAccessor;
	private Map<Class<? extends ModelCommand>, SparseArray<ModelCommand>> registeredObjects = new HashMap<Class<? extends ModelCommand>, SparseArray<ModelCommand>>();

	private Map<Class<? extends ModelCommand>, SupportedModelCommand> class2descriptor = new HashMap<Class<? extends ModelCommand>, SupportedModelCommand>();
	private Map<String, SupportedModelCommand> name2descriptor = new HashMap<String, SupportedModelCommand>();

	private List<ModelCommand> changeCommands = new ArrayList<ModelCommand>();
	private int commitedChanges = 0;
	private boolean isTransactionStarted = false;

	Gson gson = new Gson();

	public enum SearchCriteria {
		AUTHOR, BOOK_NAME, WORK_NAME, PLACE;
	}

	public enum SearchObject {
		BOOK;
	}

	public DataModel(FileAccessor fileAccessor, String baseDir) {
		this.fileAccessor = fileAccessor;
		this.baseDir = baseDir;
	}

	private String getFilesDir() {
		return baseDir + "files" + File.separator;
	}

	public DataFile importDataFile(String fileName) throws IOException {
		Pattern ext = Pattern.compile("(?<=\\.)[^\\.]*$");
		Matcher m = ext.matcher(fileName);
		String extension = "";
		if (m.find()) {
			extension = "." + m.group();
		}
		String destFileName = generateFileName(extension);
		String destFullFileName = getFilesDir() + destFileName;
		File destFile = new File(destFullFileName);
		Log.d(getClass().getName(), "copy file from " + fileName + " to "
				+ destFullFileName);
		copyFile(new File(fileName), destFile);
		DataFile df = new DataFile(0, destFileName);
		applyCommand(df);
		return df;
	}

	private String generateFileName(String extension) {
		String name;
		while (true) {
			name = ((int) (Math.random() * Integer.MAX_VALUE)) + extension;
			if (!new File(getFilesDir() + name).exists())
				break;
		}
		return name;
	}

	public void startTransaction() {
		isTransactionStarted = true;
	}

	public void commitTransaction() {
		isTransactionStarted = false;
		commitedChanges = changeCommands.size();
	}

	public void rollbackTransaction() {
		isTransactionStarted = false;
		while (changeCommands.size() != commitedChanges) {
			ModelCommand mc = changeCommands.remove(changeCommands.size() - 1);
			revertCommand(mc);
		}
	}

	public void registerObjectAppend(ModelObject object) {
		object.setDm(this);
		Class<? extends ModelObject> clazz = object.getClass();
		SparseArray<ModelCommand> map = registeredObjects.get(clazz);
		if (map == null) {
			map = new SparseArray<ModelCommand>();
			registeredObjects.put(clazz, map);
		}
		if (object.getId() == 0) {
			object.setId(getNextId(clazz));
		}
		map.put(object.getId(), object);
		updateMaxId(object);
	}

	public void deregisterObjectAppend(ModelObject object) {
		object.setDm(this);
		Class<? extends ModelObject> clazz = object.getClass();
		SparseArray<ModelCommand> map = registeredObjects.get(clazz);
		if (map != null) {
			map.delete(object.getId());
		}
	}

	private void registerModelChanges(ModelCommand object) {
		synchronized (changeCommands) {
			changeCommands.add(object);
			if (!isTransactionStarted)
				commitedChanges++;
		}
	}

	public void clearChanges() {
		synchronized (changeCommands) {
			for (int i = 0; i < commitedChanges; i++)
				changeCommands.remove(0);
			commitedChanges = 0;
		}
	}

	public boolean hasChanges() {
		boolean result;
		synchronized (changeCommands) {
			result = commitedChanges > 0;
		}
		return result;
	}

	private void updateMaxId(ModelObject object) {
		int objectId = object.getId();
		if (objectId > maxId)
			maxId = objectId;
	}

	public int getNextId(Class<? extends ModelObject> clazz) {
		return ++maxId;
	}

	public ModelCommand getRegisterModelObject(
			Class<? extends ModelCommand> clazz, int id) {
		SparseArray<ModelCommand> map = registeredObjects.get(clazz);
		if (map == null) {
			return null;
		}
		return map.get(id);
	}

	public InputStream getFileInputStream(String fileName) throws IOException {
		return fileAccessor.getFileInputStream(fileName);
	}

	public void initModel() {
		registeredObjects.clear();
		maxId = 0;
		clearChanges();
	}

	public void applyFile(String fileName) {
		List<String> lines = readFile(fileName);
		applyChanges(lines);
	}

	private void applyChanges(List<String> lines) {
		for (String line : lines) {
			if (line.trim().length() == 0)
				continue;
			int i = line.indexOf("{");
			String type = line.substring(0, i).trim();
			String value = line.substring(i);
			SupportedModelCommand c = name2descriptor.get(type);
			if (c != null) {
				Class<? extends ModelCommand> objClazz = c.getClazz();
				if (objClazz != null) {
					applyCommand(gson.fromJson(value, objClazz));
				} else
					Log.w(getClass().getName(),
							"Не указан класс для типа объекта '" + type + "'");

			} else {
				Log.w(getClass().getName(), "Неизвестный тип объекта '" + type
						+ "'");
			}
		}
	}

	public void applyCommand(ModelCommand object) {
		object.setDm(this);
		object.apply();
		Log.d(getClass().getName(),
				"applyCommand(): "
						+ class2descriptor.get(object.getClass()).getName()
						+ " " + gson.toJson(object));
		registerModelChanges(object);
	}

	/*
	 * private void resizeImage() { BitmapFactory.Options options = new
	 * BitmapFactory.Options(); InputStream is = null; is = new
	 * FileInputStream(path_to_file);
	 * BitmapFactory.decodeStream(is,null,options); is.close(); is = new
	 * FileInputStream(path_to_file); // here w and h are the desired width and
	 * height options.inSampleSize = Math.max(options.outWidth/w,
	 * options.outHeight/h); // bitmap is the resized bitmap Bitmap bitmap =
	 * BitmapFactory.decodeStream(is,null,options); }
	 */
	private List<String> readFile(String fileName) {

		List<String> lines = new ArrayList<String>();
		try {
			InputStreamReader reader = new InputStreamReader(
					getFileInputStream(baseDir + fileName), "UTF-8");
			lines = readLines(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}

	private List<String> readLines(Reader reader) {
		List<String> lines = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(reader);
			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return lines;
	}

	public void saveChanges(String fileName, boolean resetChanges) {
		synchronized (changeCommands) {
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new OutputStreamWriter(
						new FileOutputStream(baseDir + fileName), "UTF-8"));
				for (int i = 0; i < commitedChanges; i++) {
					ModelCommand mc = changeCommands.get(i);
					pw.write(class2descriptor.get(mc.getClass()).getName()
							+ " " + gson.toJson(mc) + "\n");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (pw != null)
					pw.close();
			}
			if (resetChanges)
				clearChanges();
		}
	}

	public String getFullFileName(String fileName) {
		return getFilesDir() + fileName;
	}

	public static void copyFile(File sourceFile, File destFile)
			throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;

		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}

	public void setSupportedObjects(List<SupportedModelCommand> list) {
		class2descriptor.clear();
		name2descriptor.clear();
		for (SupportedModelCommand mo : list) {
			class2descriptor.put(mo.getClazz(), mo);
			name2descriptor.put(mo.getName(), mo);
		}
	}

	public Map<Class<? extends ModelCommand>, SparseArray<ModelCommand>> getRegisteredObjects() {
		return registeredObjects;
	}

	public void revertCommand(ModelCommand command) {
		command.setDm(this);
		command.revert();
		Log.d(getClass().getName(),
				"revertCommand(): "
						+ class2descriptor.get(command.getClass()).getName()
						+ " " + gson.toJson(command));
	}
}
