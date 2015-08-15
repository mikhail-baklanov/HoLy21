package ru.masomi.holy21.datamodel.objects;

import ru.masomi.holy21.datamodel.ModelObject;

public class Work extends ModelObject {
	public static final String OBJECT_STORAGE_NAME = "work"; 
	private String name;
	private String author;

	public Work() {
	}
	
	public Work(int id, String name, String author) {
		this.id = id;
		this.author = author;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}
