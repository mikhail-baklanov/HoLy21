package ru.masomi.holy21.datamodel.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.masomi.holy21.datamodel.ModelObject;

public class Book extends ModelObject {
	public static final String OBJECT_STORAGE_NAME = "book";
	private String name;
	private transient Place place;
	private transient List<Work> works = new ArrayList<Work>();
	private transient List<Reading> readings = new ArrayList<Reading>();
	private transient DataFile image;

	public Book() {
	}

	public Book(int id, String name, Place place, DataFile image) {
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

	public List<Work> getWorks() {
		return works;
	}

	public void setWorks(List<Work> works) {
		if (works == null) {
			if (this.works == null)
				this.works = new ArrayList<Work>();
			else
				this.works.clear();
		} else
			this.works = works;
	}

	public DataFile getImage() {
		return image;
	}

	public void setImage(DataFile image) {
		this.image = image;
	}

	public List<Reading> getReadings() {
		return readings;
	}

	public void setReadings(List<Reading> readings) {
		this.readings = readings;
	}

	public void takeMe(Date date) {
		assert !isOnSite() : "Нельзя выдать невозвращенную книгу";
		if (readings == null)
			readings = new ArrayList<Reading>();
		Reading reading = new Reading(0, date, null);
		dm.applyCommand(reading);
		dm.applyCommand(new BookReading(id, reading.getId()));
	}

	public void returnMe(Date date) {
		assert isOnSite() : "Нельзя вернуть невыданную книгу";
		readings.get(readings.size() - 1).setReturnDate(date);
	}

	public boolean isOnSite() {
		return readings == null || readings.size() == 0
				|| readings.get(readings.size() - 1).getReturnDate() == null;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + ", place=" + place
				+ ", works=" + works + ", readings=" + readings + ", image="
				+ image + "]";
	}

}
