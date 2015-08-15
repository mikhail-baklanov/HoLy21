package ru.masomi.holy21.datamodel.objects;

import ru.masomi.holy21.datamodel.ModelObject;

public class BookUpdate extends ModelObject {
	public static final String OBJECT_STORAGE_NAME = "book-update";
	private String name;
	private transient Place place;
	private transient DataFile image;

	public BookUpdate() {
	}

	public BookUpdate(int id, String name, Place place, DataFile image) {
		super();
		this.id = id;
		this.name = name;
		this.place = place;
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public DataFile getImage() {
		return image;
	}

	public void setImage(DataFile image) {
		this.image = image;
	}

	@Override
	public void apply() {
		Book book = (Book) dm.getRegisterModelObject(Book.class,
				getId());
		if (book != null) {
			book.setImage(getImage());
			book.setName(getName());
			book.setPlace(getPlace());
		}
	}
}
