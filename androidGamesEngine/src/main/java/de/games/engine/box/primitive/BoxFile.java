package de.games.engine.box.primitive;

import de.games.engine.box.format.BoxChunkInterface;
import java.util.ArrayList;

public class BoxFile {
    protected BoxVersion version;
    protected final ArrayList<BoxBlock> blocks;

    public BoxFile(BoxVersion version) {
        this.version = version;
        this.blocks = new ArrayList<BoxBlock>();
    }

    public void addBlock(BoxChunkHeader header, BoxChunkInterface boxChunk) {
        BoxBlock block = new BoxBlock(header);
        block.setChunk(boxChunk);
        this.blocks.add(block);
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
        ArrayList<BoxChunkHeader> l = new ArrayList<BoxChunkHeader>();
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

    public BoxChunkHeader findBySignature(BoxString signature) {
        for (BoxChunkHeader header : getHeaders()) {
            if (header.getSignature().equals(signature)) {
                return header;
            }
        }
        return null;
    }

    public ArrayList<BoxChunkHeader> findBySignatureMulti(BoxString signature) {
        ArrayList<BoxChunkHeader> l = new ArrayList<BoxChunkHeader>();
        for (BoxChunkHeader header : getHeaders()) {
            if (header.getSignature().equals(signature)) {
                l.add(header);
            }
        }
        return l;
    }

    public BoxChunkHeader findByVersion(BoxVersion version) {
        for (BoxChunkHeader header : getHeaders()) {
            if (header.getVersion().equals(version)) {
                return header;
            }
        }
        return null;
    }

    public ArrayList<BoxChunkHeader> findByVersionMulti(BoxVersion version) {
        ArrayList<BoxChunkHeader> l = new ArrayList<BoxChunkHeader>();
        for (BoxChunkHeader header : getHeaders()) {
            if (header.getVersion().equals(version)) {
                l.add(header);
            }
        }
        return l;
    }

    public BoxVersion getVersion() {
        return this.version;
    }

    public void setVersion(BoxVersion version) {
        this.version = version;
    }
}
