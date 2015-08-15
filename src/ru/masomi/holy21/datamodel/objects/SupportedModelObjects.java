package ru.masomi.holy21.datamodel.objects;

import java.util.Arrays;
import java.util.List;

import ru.masomi.holy21.datamodel.SupportedModelCommand;

public class SupportedModelObjects {
	private static final List<SupportedModelCommand> objects = Arrays
			.asList(new SupportedModelCommand[] {
					new SupportedModelCommand(Book.class,
							Book.OBJECT_STORAGE_NAME),
					new SupportedModelCommand(BookUpdate.class,
							BookUpdate.OBJECT_STORAGE_NAME),
					new SupportedModelCommand(Work.class,
							Work.OBJECT_STORAGE_NAME),
					new SupportedModelCommand(DataFile.class,
							DataFile.OBJECT_STORAGE_NAME),
					new SupportedModelCommand(Place.class,
							Place.OBJECT_STORAGE_NAME),
					new SupportedModelCommand(BookPlace.class,
							BookPlace.OBJECT_STORAGE_NAME),
					new SupportedModelCommand(BookWork.class,
							BookWork.OBJECT_STORAGE_NAME),
					new SupportedModelCommand(BookWorkDelete.class,
							BookWorkDelete.OBJECT_STORAGE_NAME),
					new SupportedModelCommand(BookReading.class,
							BookReading.OBJECT_STORAGE_NAME),
					new SupportedModelCommand(BookImage.class,
							BookImage.OBJECT_STORAGE_NAME) });

	public static List<SupportedModelCommand> getObjects() {
		return objects;
	}

}
