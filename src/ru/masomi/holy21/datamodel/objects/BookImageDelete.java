package ru.masomi.holy21.datamodel.objects;

import ru.masomi.holy21.datamodel.ModelCommand;
import ru.masomi.holy21.datamodel.ModelRelation;

public class BookImageDelete extends ModelRelation {
	public static final String OBJECT_STORAGE_NAME = "book-image-delete";

	public BookImageDelete(int id1, int id2) {
		super(id1, id2);
	}

	@Override
	public void apply() {
		((Book) dm.getRegisterModelObject(Book.class,
				getId1())).setImage(null);
	}

	@Override
	public ModelCommand getRevertCommand() {
		return new BookImage(getId1(), getId2());
	}
}
