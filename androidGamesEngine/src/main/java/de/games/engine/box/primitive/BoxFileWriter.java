package de.games.engine.box.primitive;

import de.games.engine.box.format.BoxFormat;
import de.games.engine.box.format.BoxWriterInterface;
import de.games.engine.box.io.BinaryWriter;
import java.io.BufferedOutputStream;
import java.io.IOException;

public class BoxFileWriter implements BoxWriterInterface {
    protected BoxFile file;

    public BoxFileWriter(BoxFile file) {
        this.file = file;
    }

    public void write(BinaryWriter writer) throws IOException {
        BufferedOutputStream stream = writer.getStream();
        stream.write(BoxFormat.MAGIC_BYTES);
        BoxVersionWriter version = new BoxVersionWriter(this.file.getVersion());
        version.write(writer);
        for (BoxBlock block : this.file.getBlocks()) {
            BoxBlockWriter blockWriter = new BoxBlockWriter(block);
            blockWriter.write(writer);
        }
    }
}
