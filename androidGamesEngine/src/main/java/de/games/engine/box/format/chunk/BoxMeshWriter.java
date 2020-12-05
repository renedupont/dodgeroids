package de.games.engine.box.format.chunk;

import de.games.engine.box.format.BoxChunkInterface;
import de.games.engine.box.format.BoxChunkWriter;
import de.games.engine.box.io.ByteConversion;

public class BoxMeshWriter extends BoxChunkWriter {
    protected BoxMesh mesh;
    protected byte[] rawBytes;

    public BoxMeshWriter(BoxChunkInterface mesh) {
        this.mesh = (BoxMesh) mesh;
        float[] faces = this.mesh.getFaces();
        float[] normals = this.mesh.getNormals();
        float[] uvs = this.mesh.getUvs();
        byte[][] raw = new byte[6][];
        raw[0] = ByteConversion.intToBytes(faces.length);
        raw[1] = ByteConversion.floatArrayToBytes(faces);
        raw[2] = ByteConversion.intToBytes(normals.length);
        raw[3] = ByteConversion.floatArrayToBytes(normals);
        raw[4] = ByteConversion.intToBytes(uvs.length);
        raw[5] = ByteConversion.floatArrayToBytes(uvs);
        setRawBytes(concat(raw));
    }
}
