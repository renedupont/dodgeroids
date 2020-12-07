package de.games.engine.box.primitive;

import de.games.engine.box.format.BoxFormat;

public final class BoxString {
    protected String string;
    protected String encoding;

    public BoxString(String string) {
        this.string = string;
        this.encoding = BoxFormat.ENCODING;
    }

    public String getString() {
        return this.string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String toString() {
        return this.string;
    }

    public BoxString clone() {
        return new BoxString(this.string);
    }

    public int getByteLength() {
        return (this.string.toCharArray()).length * 2 + 2;
    }

    public boolean equals(BoxString s) {
        if (!s.getString().equals(this.string)) return false;
        if (!s.getEncoding().equals(this.encoding)) return false;
        return true;
    }
}
