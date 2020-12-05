package de.games.engine.box.primitive;

import de.games.engine.box.format.BoxFormat;

public class BoxChunkHeader {
    protected BoxFormat.Identifier id;
    protected BoxString signature;
    protected BoxVersion version;
    protected int length;

    public BoxChunkHeader(BoxFormat.Identifier id, BoxString signature, BoxVersion version) {
        this.id = id;
        this.signature = signature;
        this.version = version;
    }

    public BoxFormat.Identifier getId() {
        return this.id;
    }

    public void setId(BoxFormat.Identifier id) {
        this.id = id;
    }

    public BoxString getSignature() {
        return this.signature;
    }

    public void setSignature(BoxString signature) {
        this.signature = signature;
    }

    public BoxVersion getVersion() {
        return this.version;
    }

    public void setVersion(BoxVersion version) {
        this.version = version;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public BoxChunkHeader clone() {
        return new BoxChunkHeader(this.id, this.signature.clone(), this.version.clone());
    }

    public boolean equals(BoxChunkHeader h) {
        if (!this.id.equals(h.getId())) return false;
        if (!this.signature.equals(h.getSignature())) return false;
        if (!this.version.equals(h.getVersion())) return false;
        if (this.length != h.getLength()) {
            return false;
        }
        return true;
    }
}
