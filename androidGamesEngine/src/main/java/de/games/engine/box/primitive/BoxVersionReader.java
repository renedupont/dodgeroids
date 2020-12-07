package de.games.engine.box.primitive;

import de.games.engine.box.io.BinaryReader;
import java.io.IOException;

public class BoxVersionReader {
    public BoxVersion read(BinaryReader reader) throws IOException {
        short major = reader.readShort();
        short minor = reader.readShort();
        BoxVersion version = new BoxVersion(major, minor);
        return version;
    }
}
