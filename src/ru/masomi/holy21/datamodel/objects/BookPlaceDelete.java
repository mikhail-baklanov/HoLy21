package ru.masomi.holy21.datamodel.objects;

import ru.masomi.holy21.datamodel.ModelCommand;
import ru.masomi.holy21.datamodel.ModelRelation;

public class BookPlaceDelete extends ModelRelation {
	public static final String OBJECT_STORAGE_NAME = "book-place-delete";

	public BookPlaceDelete(int id1, int id2) {
		super(id1, id2);
	}

	@Override
	public void apply() {
		((Book) dm.getRegisterModelObject(Book.class, getId1()))
		.setPlace(null);
	}

	@Override
	public ModelCommand getRevertCommand() {
		return new BookPlace(getId1(), getId2());
	}
}
