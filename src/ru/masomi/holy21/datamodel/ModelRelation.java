package ru.masomi.holy21.datamodel;


abstract public class ModelRelation extends ModelCommand {
	private int id1;
	private int id2;

	public ModelRelation() {
	}

	public ModelRelation(int id1, int id2) {
		super();
		this.id1 = id1;
		this.id2 = id2;
	}

	public int getId2() {
		return id2;
	}

	public int getId1() {
		return id1;
	}

	abstract public ModelCommand getRevertCommand();

	@Override
	public void revert() {
		ModelCommand c = getRevertCommand();
		c.setDm(dm);
		c.apply();
	}

}
