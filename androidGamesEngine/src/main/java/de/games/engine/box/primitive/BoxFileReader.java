package de.games.engine.box.primitive;

import de.games.engine.box.format.BoxChunkInterface;
import de.games.engine.box.format.BoxFormat;
import de.games.engine.box.format.chunk.BoxMeshDodgeroidsReader;
import de.games.engine.box.format.chunk.BoxMeshReader;
import de.games.engine.box.io.BinaryReader;
import de.games.engine.box.io.ByteConversion;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class BoxFileReader {
    protected BinaryReader reader;
    protected RandomAccessFile file;
    protected ByteConversion.ByteOrder endianness;
    protected final File fileHandle;
    protected boolean isClosed = false;

    public BoxFileReader(File fileHandle) throws IOException {
        this(fileHandle, ByteConversion.ByteOrder.BigEndian);
    }

    public BoxFileReader(File fileHandle, ByteConversion.ByteOrder byteOrder) throws IOException {
        this.fileHandle = fileHandle;
        this.endianness = byteOrder;
        this.file = new RandomAccessFile(fileHandle, "r");
        this.reader = new BinaryReader(this.file, byteOrder);
    }

    public void close() throws IOException {
        this.isClosed = true;
        this.file.close();
    }

    private boolean isValid() throws IOException {
        this.file.seek(0L);
        byte[] first_four_bytes = new byte[4];
        this.file.read(first_four_bytes);
        if (Arrays.equals(first_four_bytes, BoxFormat.MAGIC_BYTES)) {
            this.endianness = this.reader.getByteOrder();
            return true;
        }
        return false;
    }

    public BoxFile read() throws IOException {
        if (this.isClosed) {
            this.file = new RandomAccessFile(this.fileHandle, "r");
            this.reader = new BinaryReader(this.file, this.endianness);
            this.isClosed = false;
        }
        if (!isValid()) {
            throw new IOException("Invalid File");
        }
        BoxVersionReader versionReader = new BoxVersionReader();
        BoxVersion version = versionReader.read(this.reader);
        BoxFile theFile = new BoxFile(version);
        while (this.file.getFilePointer() < this.file.length()) {
            BoxBlockReader blockReader = new BoxBlockReader();
            BoxBlock block = blockReader.read(this.reader);
            theFile.addBlock(block);
            this.file.seek(this.file.getFilePointer() + block.getHeader().getLength());
        }
        return theFile;
    }

    public BoxChunkInterface readChunk(BoxBlock block) throws IOException {
        BoxMeshReader mr;
        BoxMeshDodgeroidsReader mrd;
        if (block == null) {
            return null;
        }
        if (this.isClosed) {
            this.file = new RandomAccessFile(this.fileHandle, "r");
            this.reader = new BinaryReader(this.file, this.endianness);
            this.isClosed = false;
        }
        if (this.endianness == null && !isValid()) {
            throw new IOException("Invalid File");
        }
        if (this.reader.getByteOrder() != this.endianness) {
            this.reader.setByteOrder(this.endianness);
        }
        this.file.seek(block.getOffset());
        switch (block.getHeader().getId()) {
            case MESH:
                mr = new BoxMeshReader();
                block.setChunk((BoxChunkInterface) mr.read(this.reader));
                return block.getChunk();
            case MESH_DODGEROIDS:
                mrd = new BoxMeshDodgeroidsReader();
                block.setChunk((BoxChunkInterface) mrd.read(this.reader));
                return block.getChunk();
        }
        return null;
    }
}
