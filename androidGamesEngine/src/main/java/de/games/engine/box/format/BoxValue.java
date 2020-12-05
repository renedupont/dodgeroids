package de.games.engine.box.format;

public abstract class BoxValue {
    protected int length;

    protected void setLength(int length) {
        this.length = length;
    }

    protected int getLength() {
        return this.length;
    }
}
