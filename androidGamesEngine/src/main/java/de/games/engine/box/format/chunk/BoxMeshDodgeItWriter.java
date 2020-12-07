package de.games.engine.box.format.chunk;

import de.games.engine.box.format.BoxChunkInterface;
import de.games.engine.box.format.BoxChunkWriter;
import de.games.engine.box.io.ByteConversion;
import de.games.engine.box.primitive.BoxStringWriter;
import java.io.IOException;

public class BoxMeshDodgeItWriter extends BoxChunkWriter {
    protected BoxMeshDodgeIt mesh;
    protected byte[] rawBytes;

    public BoxMeshDodgeItWriter(BoxChunkInterface mesh) throws IOException {
        this.mesh = (BoxMeshDodgeIt) mesh;
        float[] faces = this.mesh.getFaces();
        float[] normals = this.mesh.getNormals();
        float[] uvs = this.mesh.getUvs();
        byte[][] raw =
                new byte[(this.mesh.hasMaterial() ? 15 : 8) + (this.mesh.getBounds()).length][];
        raw[0] = ByteConversion.intToBytes(faces.length);
        raw[1] = ByteConversion.floatArrayToBytes(faces);
        raw[2] = ByteConversion.intToBytes(normals.length);
        raw[3] = ByteConversion.floatArrayToBytes(normals);
        raw[4] = ByteConversion.intToBytes(uvs.length);
        raw[5] = ByteConversion.floatArrayToBytes(uvs);
        raw[6] = ByteConversion.intToBytes((this.mesh.getBounds()).length);
        int offset = 7;
        byte b;
        int i;
        BoxMeshBound[] arrayOfBoxMeshBound;
        for (i = (arrayOfBoxMeshBound = this.mesh.getBounds()).length, b = 0; b < i; ) {
            BoxMeshBound bound = arrayOfBoxMeshBound[b];
            BoxMeshBoundWriter boundWriter = new BoxMeshBoundWriter(bound);
            raw[offset++] = boundWriter.getRawBytes();
            b++;
        }
        raw[offset++] = new byte[] {(byte) (this.mesh.hasMaterial() ? 1 : 0)};
        if (this.mesh.hasMaterial()) {
            BoxStringWriter id = new BoxStringWriter(this.mesh.getId());
            raw[offset++] = id.getRawBytes();
            raw[offset++] = ByteConversion.floatArrayToBytes(this.mesh.getAmbient());
            raw[offset++] = ByteConversion.floatArrayToBytes(this.mesh.getDiffuse());
            raw[offset++] = ByteConversion.floatArrayToBytes(this.mesh.getSpecular());
            raw[offset++] = ByteConversion.floatArrayToBytes(this.mesh.getEmissive());
            raw[offset++] = ByteConversion.floatToBytes(this.mesh.getShininess());
            raw[offset++] = ByteConversion.floatToBytes(this.mesh.getOpacity());
        }
        setRawBytes(concat(raw));
    }
}
