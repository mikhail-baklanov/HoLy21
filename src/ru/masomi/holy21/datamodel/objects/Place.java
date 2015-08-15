package ru.masomi.holy21.datamodel.objects;

import ru.masomi.holy21.datamodel.ModelObject;

public class Place extends ModelObject {
	public static final String OBJECT_STORAGE_NAME = "place"; 
	private String name;

	public Place() {
	}
	
	public Place(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
