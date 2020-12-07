package de.games.engine.box.format;

import de.games.engine.box.io.BinaryWriter;
import java.io.IOException;

public interface BoxWriterInterface {
    void write(BinaryWriter paramBinaryWriter) throws IOException;
}
