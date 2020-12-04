package de.games.engine.graphics;

public enum ViewType {

	DEFAULT(0), TWO_D(1), THREE_D(2);

	private int intVal;

	private ViewType(final int intVal) {
		this.intVal = intVal;
	}

	public int getIntVal() {
		return intVal;
	}

}
