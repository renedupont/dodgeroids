package de.games.engine.box.io;

import java.io.IOException;
import java.io.RandomAccessFile;

public class BinaryReader {
    protected ByteConversion.ByteOrder byteOrder;
    protected RandomAccessFile input;

    public BinaryReader(RandomAccessFile file, ByteConversion.ByteOrder byteOrder) {
        this.input = file;
        this.byteOrder = byteOrder;
    }

    public final void setByteOrder(ByteConversion.ByteOrder bo) {
        this.byteOrder = bo;
    }

    public final ByteConversion.ByteOrder getByteOrder() {
        return this.byteOrder;
    }

    public RandomAccessFile getFile() {
        return this.input;
    }

    public byte readByte() throws IOException {
        byte[] value = new byte[1];
        this.input.read(value);
        return value[0];
    }

    public char[] readChar(int arraySize) throws IOException {
        byte[] data = new byte[2 * arraySize];
        this.input.read(data);
        return ByteConversion.charArrayFromBytes(data);
    }

    public short readShort() throws IOException {
        byte[] data = new byte[2];
        this.input.read(data);
        return ByteConversion.shortFromBytes(data);
    }

    public short[] readShort(int arraySize) throws IOException {
        byte[] data = new byte[2 * arraySize];
        this.input.read(data);
        return ByteConversion.shortArrayFromBytes(data);
    }

    public int readInt() throws IOException {
        byte[] data = new byte[4];
        this.input.read(data);
        return ByteConversion.intFromBytes(data);
    }

    public float readFloat() throws IOException {
        byte[] value = new byte[4];
        this.input.read(value);
        return ByteConversion.floatFromBytes(value);
    }

    public float[] readFloat(int arraySize) throws IOException {
        byte[] value = new byte[4 * arraySize];
        this.input.read(value);
        return ByteConversion.floatArrayFromBytes(value);
    }
}
