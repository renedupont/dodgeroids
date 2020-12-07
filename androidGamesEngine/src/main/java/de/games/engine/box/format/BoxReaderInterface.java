package de.games.engine.box.format;

import de.games.engine.box.io.BinaryReader;
import java.io.IOException;

public interface BoxReaderInterface {
    int getLength();

    void readIndex(BinaryReader paramBinaryReader) throws IOException;

    void readValue(BinaryReader paramBinaryReader) throws IOException;
}
