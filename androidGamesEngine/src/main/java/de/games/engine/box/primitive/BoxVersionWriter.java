package de.games.engine.box.primitive;

import de.games.engine.box.format.BoxWriterInterface;
import de.games.engine.box.io.BinaryWriter;
import java.io.IOException;

public class BoxVersionWriter implements BoxWriterInterface {
    protected BoxVersion version;

    public BoxVersionWriter(BoxVersion version) {
        this.version = version;
    }

    public void write(BinaryWriter writer) throws IOException {
        writer.writeShort(this.version.getMajor());
        writer.writeShort(this.version.getMinor());
    }
}
