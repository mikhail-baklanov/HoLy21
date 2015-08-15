package ru.masomi.holy21.datamodel.objects;

import ru.masomi.holy21.datamodel.ModelCommand;
import ru.masomi.holy21.datamodel.ModelRelation;

public class BookWork extends ModelRelation {
	public static final String OBJECT_STORAGE_NAME = "book-work";

	public BookWork() {
	}

	public BookWork(int id1, int id2) {
		super(id1, id2);
	}

	@Override
	public void apply() {
		((Book) dm.getRegisterModelObject(Book.class,
				getId1())).getWorks().add(
				(Work) dm.getRegisterModelObject(
						Work.class, getId2()));
	}

	@Override
	public ModelCommand getRevertCommand() {
		return new BookWorkDelete(getId1(), getId2());
	}

}
