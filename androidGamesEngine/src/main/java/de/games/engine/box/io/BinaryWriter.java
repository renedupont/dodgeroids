package de.games.engine.box.io;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class BinaryWriter {
    protected ByteConversion.ByteOrder byteOrder;
    protected final BufferedOutputStream output;

    public BinaryWriter(BufferedOutputStream output, ByteConversion.ByteOrder byteOrder) {
        this.output = output;
        this.byteOrder = byteOrder;
    }

    public final void setByteOrder(ByteConversion.ByteOrder bo) {
        this.byteOrder = bo;
    }

    public final ByteConversion.ByteOrder getByteOrder() {
        return this.byteOrder;
    }

    public final BufferedOutputStream getStream() {
        return this.output;
    }

    public int writeByte(byte[] data) throws IOException {
        this.output.write(data);
        return data.length;
    }

    public int writeChar(char[] data) throws IOException {
        byte[] d = ByteConversion.charArrayToBytes(data);
        this.output.write(d);
        return d.length;
    }

    public int writeShort(short data) throws IOException {
        byte[] d = ByteConversion.shortToBytes(data);
        this.output.write(d);
        return d.length;
    }

    public int writeInt(int data) throws IOException {
        byte[] d = ByteConversion.intToBytes(data);
        this.output.write(d);
        return d.length;
    }
}
