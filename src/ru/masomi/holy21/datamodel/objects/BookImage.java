package ru.masomi.holy21.datamodel.objects;

import ru.masomi.holy21.datamodel.ModelCommand;
import ru.masomi.holy21.datamodel.ModelRelation;

public class BookImage extends ModelRelation {
	public static final String OBJECT_STORAGE_NAME = "book-image";

	public BookImage() {
	}
	public BookImage(int id1, int id2) {
		super(id1, id2);
	}

	@Override
	public void apply() {
		((Book) dm.getRegisterModelObject(Book.class,
				getId1())).setImage((DataFile) dm
				.getRegisterModelObject(DataFile.class,
						getId2()));
	}
	@Override
	public ModelCommand getRevertCommand() {
		return new BookImageDelete(getId1(), getId2());
	}

}
