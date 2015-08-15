package ru.masomi.holy21.datamodel.objects;

import ru.masomi.holy21.datamodel.ModelCommand;
import ru.masomi.holy21.datamodel.ModelRelation;

public class BookPlace extends ModelRelation {
	public static final String OBJECT_STORAGE_NAME = "book-place";

	public BookPlace() {
	}

	public BookPlace(int id1, int id2) {
		super(id1, id2);
	}

	@Override
	public void apply() {
		((Book) dm.getRegisterModelObject(Book.class, getId1()))
				.setPlace((Place) dm.getRegisterModelObject(Place.class,
						getId2()));

	}

	@Override
	public ModelCommand getRevertCommand() {
		return new BookWorkDelete(getId1(), getId2());
	}

}
