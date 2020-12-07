package de.games.engine.box.format;

import de.games.engine.box.io.BinaryWriter;
import java.io.IOException;

public class BoxChunkWriter implements BoxWriterInterface {
    protected byte[] rawBytes;

    protected void setRawBytes(byte[] bytes) {
        this.rawBytes = bytes;
    }

    public byte[] getRawBytes() {
        return this.rawBytes;
    }

    public int getLength() {
        return this.rawBytes.length;
    }

    protected byte[] concat(byte[][] arrays) {
        int length = 0;
        byte b1;
        int i;
        byte[][] arrayOfByte1;
        for (i = (arrayOfByte1 = arrays).length, b1 = 0; b1 < i; ) {
            byte[] arr = arrayOfByte1[b1];
            length += arr.length;
            b1++;
        }
        byte[] result = new byte[length];
        int pos = 0;
        byte b2;
        int j;
        byte[][] arrayOfByte2;
        for (j = (arrayOfByte2 = arrays).length, b2 = 0; b2 < j; ) {
            byte[] arr = arrayOfByte2[b2];
            byte b;
            int k;
            byte[] arrayOfByte3;
            for (k = (arrayOfByte3 = arr).length, b = 0; b < k; ) {
                byte b3 = arrayOfByte3[b];
                result[pos] = b3;
                pos++;
                b++;
            }
            b2++;
        }
        return result;
    }

    public void write(BinaryWriter writer) throws IOException {
        writer.writeByte(this.rawBytes);
    }
}
