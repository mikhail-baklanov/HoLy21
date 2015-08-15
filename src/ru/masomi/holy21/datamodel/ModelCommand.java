package ru.masomi.holy21.datamodel;

abstract public class ModelCommand {
	protected transient DataModel dm;

	public DataModel getDm() {
		return dm;
	}

	public void setDm(DataModel dm) {
		this.dm = dm;
	}
	
	public abstract void apply();
	
	public abstract void revert();

}
