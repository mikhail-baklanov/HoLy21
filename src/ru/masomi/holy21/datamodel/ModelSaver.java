package ru.masomi.holy21.datamodel;

import java.util.Timer;
import java.util.TimerTask;

public class ModelSaver {
	private Timer timer;
	private DataModel dataModel;
	private String fileNamePattern;
	private int startNumber;

	public ModelSaver(DataModel dataModel, String fileNamePattern,
			int startNumber) {
		this.dataModel = dataModel;
		this.fileNamePattern = fileNamePattern;
		this.startNumber = startNumber;
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				checkAndSave();
			}

		}, 0, 5000);
	}

	public void cancel() {
		timer.cancel();
	}

	private void checkAndSave() {
		if (dataModel.hasChanges()) {
			String fileName = fileNamePattern.replace("{0}", ""
					+ (startNumber++));
			dataModel.saveChanges(fileName, true);
		}
	}
}
