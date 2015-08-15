package ru.masomi.holy21.datamodel.objects;

import java.util.List;

import ru.masomi.holy21.datamodel.ModelCommand;
import ru.masomi.holy21.datamodel.ModelRelation;

public class BookReadingDelete extends ModelRelation {
	public static final String OBJECT_STORAGE_NAME = "book-reading-delete";

	public BookReadingDelete(int id1, int id2) {
		super(id1, id2);
	}

	@Override
	public void apply() {
		List<Reading> readings = ((Book) dm.getRegisterModelObject(Book.class,
				getId1())).getReadings();
		Reading reading = (Reading) dm.getRegisterModelObject(
						Reading.class, getId2());
		readings.remove(reading);
	}

	@Override
	public ModelCommand getRevertCommand() {
		return new BookReading(getId1(), getId2());
	}
}
