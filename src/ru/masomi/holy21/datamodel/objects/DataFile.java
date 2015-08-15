package ru.masomi.holy21.datamodel.objects;

import ru.masomi.holy21.datamodel.ModelObject;

public class DataFile extends ModelObject {
	public static final String OBJECT_STORAGE_NAME = "dataFile"; 
	private String name;

	public DataFile() {
	}
	
	public DataFile(int id, String name) {
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

	@Override
	public String toString() {
		return "DataFile [id=" + id + ", name="
				+ name + "]";
	}

}
