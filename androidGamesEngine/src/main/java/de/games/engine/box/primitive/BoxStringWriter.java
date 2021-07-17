package de.games.engine.box.primitive;

import de.games.engine.box.format.BoxWriterInterface;
import de.games.engine.box.io.BinaryWriter;
import de.games.engine.box.io.ByteConversion;
import java.io.IOException;

public class BoxStringWriter implements BoxWriterInterface {
    protected short length;
    protected char[] data;

    public BoxStringWriter(BoxString string) {
        this.data = string.getString().toCharArray();
        this.length = (short) this.data.length;
    }

    public byte[] getRawBytes() {
        byte[] l = ByteConversion.shortToBytes(this.length);
        byte[] d = ByteConversion.charArrayToBytes(this.data);
        byte[] r = new byte[l.length + d.length];
        int pos = 0;
        byte b;
        int i;
        byte[] arrayOfByte1;
        for (i = (arrayOfByte1 = l).length, b = 0; b < i; ) {
            byte x = arrayOfByte1[b];
            r[pos] = x;
            pos++;
            b++;
        }
        for (i = (arrayOfByte1 = d).length, b = 0; b < i; ) {
            byte x = arrayOfByte1[b];
            r[pos] = x;
            pos++;
            b++;
        }
        return r;
    }

    public void write(BinaryWriter writer) throws IOException {
        writer.writeShort(this.length);
        writer.writeChar(this.data);
    }
}
