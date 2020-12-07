package de.games.engine.box.primitive;

import de.games.engine.box.io.BinaryReader;
import java.io.IOException;

public class BoxStringReader {
    protected short length;
    protected char[] data;

    public BoxString read(BinaryReader reader) throws IOException {
        int length = reader.readShort();
        char[] data = new char[length];
        data = reader.readChar(length);
        return new BoxString(new String(data));
    }
}
