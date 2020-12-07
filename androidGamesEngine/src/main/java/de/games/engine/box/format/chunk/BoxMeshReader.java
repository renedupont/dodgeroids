package de.games.engine.box.format.chunk;

import de.games.engine.box.io.BinaryReader;
import java.io.IOException;

public class BoxMeshReader {
    public BoxMesh read(BinaryReader reader) throws IOException {
        int numFaces = reader.readInt();
        float[] faces = reader.readFloat(numFaces);
        int numNormals = reader.readInt();
        float[] normals = reader.readFloat(numNormals);
        int numUvs = reader.readInt();
        float[] uvs = reader.readFloat(numUvs);
        return new BoxMesh(faces, normals, uvs);
    }
}
