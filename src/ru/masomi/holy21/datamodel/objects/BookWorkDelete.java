package ru.masomi.holy21.datamodel.objects;

import java.util.List;

import ru.masomi.holy21.datamodel.ModelCommand;
import ru.masomi.holy21.datamodel.ModelRelation;

public class BookWorkDelete extends ModelRelation {
	public static final String OBJECT_STORAGE_NAME = "book-work-delete";

	public BookWorkDelete(int id1, int id2) {
		super(id1, id2);
	}

	@Override
	public void apply() {
		List<Work> works = ((Book) dm.getRegisterModelObject(Book.class,
				getId1())).getWorks();
		Work work = (Work) dm.getRegisterModelObject(
						Work.class, getId2());
		works.remove(work);
	}

	@Override
	public ModelCommand getRevertCommand() {
		return new BookWork(getId1(), getId2());
	}
}
