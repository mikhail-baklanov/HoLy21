package ru.masomi.holy21.datamodel;

public class ModelObject extends ModelCommand {
	protected int id;

	public ModelObject() {
	}
	
	public ModelObject(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void apply() {
		dm.registerObjectAppend(this);
	}

	@Override
	public void revert() {
		dm.deregisterObjectAppend(this);
	}

}
