package de.games.engine.box.primitive;

import de.games.engine.box.io.BinaryReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BoxBlockReader {
    public BoxBlock read(BinaryReader reader) throws IOException {
        BoxChunkHeaderReader blockHeaderReader = new BoxChunkHeaderReader();
        BoxChunkHeader blockHeader = blockHeaderReader.read(reader);
        BoxBlock block = new BoxBlock(blockHeader);
        RandomAccessFile file = reader.getFile();
        block.setOffset(file.getFilePointer());
        return block;
    }
}
