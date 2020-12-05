package de.games.engine.box.primitive;

import de.games.engine.box.format.BoxWriterInterface;
import de.games.engine.box.format.chunk.BoxMeshDodgeItWriter;
import de.games.engine.box.format.chunk.BoxMeshWriter;
import de.games.engine.box.io.BinaryWriter;
import java.io.IOException;

public class BoxBlockWriter implements BoxWriterInterface {
    private final BoxBlock block;

    public BoxBlockWriter(BoxBlock block) {
        this.block = block;
    }

    public void write(BinaryWriter writer) throws IOException {
        BoxMeshWriter meshWriter;
        BoxMeshDodgeItWriter mw;
        BoxChunkHeader blockHeader = this.block.getHeader();
        BoxStringWriter signatureWriter = new BoxStringWriter(blockHeader.getSignature());
        BoxVersionWriter versionWriter = new BoxVersionWriter(blockHeader.getVersion());
        writer.writeInt(blockHeader.getId().getFlag());
        signatureWriter.write(writer);
        versionWriter.write(writer);
        switch (this.block.getHeader().getId()) {
            case MESH:
                meshWriter = new BoxMeshWriter(this.block.getChunk());
                writer.writeInt(meshWriter.getLength());
                meshWriter.write(writer);
            case MESHDODGEIT:
                mw = new BoxMeshDodgeItWriter(this.block.getChunk());
                writer.writeInt(mw.getLength());
                mw.write(writer);
                break;
        }
    }
}
