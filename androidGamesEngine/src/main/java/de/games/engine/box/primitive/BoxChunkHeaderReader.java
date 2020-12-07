package de.games.engine.box.primitive;

import de.games.engine.box.format.BoxFormat;
import de.games.engine.box.io.BinaryReader;
import java.io.IOException;

public class BoxChunkHeaderReader {
    public BoxChunkHeader read(BinaryReader reader) throws IOException {
        BoxFormat.Identifier id = BoxFormat.Identifier.getFromFlag(reader.readInt());
        BoxStringReader signatureReader = new BoxStringReader();
        BoxVersionReader versionReader = new BoxVersionReader();
        BoxChunkHeader blockHeader =
                new BoxChunkHeader(id, signatureReader.read(reader), versionReader.read(reader));
        blockHeader.setLength(reader.readInt());
        return blockHeader;
    }
}
