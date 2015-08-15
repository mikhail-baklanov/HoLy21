package ru.masomi.holy21;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import ru.masomi.holy21.datamodel.DataModel;
import ru.masomi.holy21.datamodel.FileAccessor;
import ru.masomi.holy21.datamodel.ModelSaver;
import ru.masomi.holy21.datamodel.objects.SupportedModelObjects;
import ru.masomi.holy21.datamodel.service.Service;
import android.app.Application;
import android.content.res.AssetManager;
import android.os.Environment;

public class HoLyApplication extends Application {

	private static final String FILE_NAME_PATTERN = "delta-{0}.txt";
	private DataModel dataModel;
	private Service service;
	private ModelSaver modelSaver;

	@Override
	public void onCreate() {
		super.onCreate();
		String baseDir = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + "HoLy" + File.separator;

		dataModel = new DataModel(new FileAccessor() {
			
			@Override
			public InputStream getFileInputStream(String fileName) throws IOException {
				if (fileName.startsWith("file:///android_asset/")) {
					fileName = fileName.substring("file:///android_asset/".length());
					AssetManager am = HoLyApplication.this.getAssets();
					return am.open(fileName);
				} else {
					return new FileInputStream(fileName);
				}
			}
		}, baseDir);
		dataModel.setSupportedObjects(SupportedModelObjects.getObjects());
		
//		dataModel.startTransaction();
//		Book b = new Book();
//		dataModel.applyCommand(b);
//		Work w = new Work();
//		dataModel.applyCommand(w);
//		dataModel.applyCommand(new BookWork(b.getId(), w.getId()));
//		dataModel.applyCommand(new BookWorkDelete(b.getId(), w.getId()));
//		dataModel.rollbackTransaction();
//		dataModel.commitTransaction();
		
		service = new Service(dataModel);
		int lastDeltaFileNumber = loadModel(baseDir);
		modelSaver = new ModelSaver(dataModel, FILE_NAME_PATTERN,
				lastDeltaFileNumber);
	}

	public DataModel getDataModel() {
		return dataModel;
	}

	private int loadModel(String baseDir) {
		dataModel.applyFile("data-json.txt");
		int lastDeltaFileNumber = 0;
		while (true) {
			String fileName = FILE_NAME_PATTERN.replace("{0}", ""
					+ (++lastDeltaFileNumber));
			if (new File(baseDir + fileName).exists()) {
				dataModel.applyFile(fileName);
			} else
				break;
		}
		dataModel.clearChanges();
		return lastDeltaFileNumber;
	}

	public Service getService() {
		return service;
	}

}
