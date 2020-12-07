package de.games.engine.box.primitive;

import de.games.engine.box.format.BoxChunkInterface;

public class BoxBlock {
    private final BoxChunkHeader header;
    private BoxChunkInterface chunk;
    private long chunkPosition;

    public BoxBlock(BoxChunkHeader header) {
        this.header = header;
    }

    public BoxChunkHeader getHeader() {
        return this.header;
    }

    public BoxChunkInterface getChunk() {
        return this.chunk;
    }

    public boolean hasChunk() {
        return (this.chunk != null);
    }

    public void setChunk(BoxChunkInterface chunk) {
        this.chunk = chunk;
    }

    public long getOffset() {
        return this.chunkPosition;
    }

    public void setOffset(long offset) {
        this.chunkPosition = offset;
    }
}
