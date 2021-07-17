package de.games.engine.box.primitive;

import java.util.ArrayList;

public class BoxFile {
    protected BoxVersion version;
    protected final ArrayList<BoxBlock> blocks;

    public BoxFile(BoxVersion version) {
        this.version = version;
        this.blocks = new ArrayList<>();
    }

    public void addBlock(BoxBlock block) {
        this.blocks.add(block);
    }

    public boolean removeBlock(BoxBlock block) {
        return this.blocks.remove(block);
    }

    public BoxBlock getBlock(BoxChunkHeader header) {
        for (BoxBlock block : this.blocks) {
            if (block.getHeader() == header) {
                return block;
            }
        }
        return null;
    }

    public ArrayList<BoxBlock> getBlocks() {
        return this.blocks;
    }

    public ArrayList<BoxChunkHeader> getHeaders() {
        ArrayList<BoxChunkHeader> l = new ArrayList<>();
        for (BoxBlock block : this.blocks) {
            l.add(block.getHeader());
        }
        return l;
    }

    public BoxChunkHeader findBySignature(String signature) {
        for (BoxChunkHeader header : getHeaders()) {
            if (header.getSignature().getString().equals(signature)) {
                return header;
            }
        }
        return null;
    }

    public BoxVersion getVersion() {
        return this.version;
    }

    public void setVersion(BoxVersion version) {
        this.version = version;
    }
}
