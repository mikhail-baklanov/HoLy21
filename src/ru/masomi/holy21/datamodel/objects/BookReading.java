package ru.masomi.holy21.datamodel.objects;

import ru.masomi.holy21.datamodel.ModelCommand;
import ru.masomi.holy21.datamodel.ModelRelation;

public class BookReading extends ModelRelation {
	public static final String OBJECT_STORAGE_NAME = "book-reading";

	public BookReading() {
	}

	public BookReading(int id1, int id2) {
		super(id1, id2);
	}

	@Override
	public void apply() {
		((Book) dm.getRegisterModelObject(Book.class, getId1())).getReadings()
				.add((Reading) dm.getRegisterModelObject(Reading.class,
						getId2()));

	}

	@Override
	public ModelCommand getRevertCommand() {
		return new BookReadingDelete(getId1(), getId2());
	}

}
