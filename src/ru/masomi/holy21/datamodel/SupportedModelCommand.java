package ru.masomi.holy21.datamodel;


public class SupportedModelCommand {
	private Class<? extends ModelCommand> clazz;
	private String name;

	public SupportedModelCommand(Class<? extends ModelCommand> clazz,
			String name) {
		super();
		this.clazz = clazz;
		this.name = name;
	}

	public Class<? extends ModelCommand> getClazz() {
		return clazz;
	}

	public String getName() {
		return name;
	}

}
