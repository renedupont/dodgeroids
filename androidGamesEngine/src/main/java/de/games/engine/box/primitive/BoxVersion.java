package de.games.engine.box.primitive;

public final class BoxVersion {
    protected short major;
    protected short minor;

    public BoxVersion(short major, short minor) {
        this.major = major;
        this.minor = minor;
    }

    public short getMajor() {
        return this.major;
    }

    public void setMajor(short major) {
        this.major = major;
    }

    public short getMinor() {
        return this.minor;
    }

    public void setMinor(short minor) {
        this.minor = minor;
    }

    public BoxVersion clone() {
        return new BoxVersion(this.major, this.minor);
    }

    public boolean equals(BoxVersion v) {
        if (this.major != v.getMajor()) return false;
        if (this.minor != v.getMinor()) return false;
        return true;
    }
}
