package de.games.engine.box.format.chunk;

import de.games.engine.box.format.chunk.meshDodgeItStuff.Color;
import de.games.engine.box.format.chunk.meshDodgeItStuff.Material;
import de.games.engine.box.io.BinaryReader;
import de.games.engine.box.primitive.BoxString;
import de.games.engine.box.primitive.BoxStringReader;
import java.io.IOException;

public class BoxMeshDodgeItReader {
    public BoxMeshDodgeIt read(BinaryReader reader) throws IOException {
        int numFaces = reader.readInt();
        float[] faces = reader.readFloat(numFaces);
        int numNormals = reader.readInt();
        float[] normals = reader.readFloat(numNormals);
        int numUvs = reader.readInt();
        float[] uvs = reader.readFloat(numUvs);
        int numBounds = reader.readInt();
        BoxMeshBound[] bounds = new BoxMeshBound[numBounds];
        for (int i = 0; i < numBounds; i++) {
            BoxMeshBoundReader boundReader = new BoxMeshBoundReader();
            bounds[i] = boundReader.read(reader);
        }
        byte hasMaterials = reader.readByte();
        if (hasMaterials == 1) {
            Material mat = new Material();
            BoxStringReader idReader = new BoxStringReader();
            BoxString id = idReader.read(reader);
            mat.id = id.getString();
            float[] ambient = reader.readFloat(4);
            mat.ambient = new Color(ambient[0], ambient[2], ambient[3], ambient[4]);
            float[] diffuse = reader.readFloat(4);
            mat.diffuse = new Color(diffuse[0], diffuse[2], diffuse[3], diffuse[4]);
            float[] specular = reader.readFloat(4);
            mat.specular = new Color(specular[0], specular[2], specular[3], specular[4]);
            float[] emissive = reader.readFloat(4);
            mat.emissive = new Color(emissive[0], emissive[2], emissive[3], emissive[4]);
            mat.shininess = reader.readFloat();
            mat.opacity = reader.readFloat();
            BoxMeshDodgeIt boxMeshDodgeIt = new BoxMeshDodgeIt(faces, normals, uvs, mat);
        }
        BoxMeshDodgeIt r = new BoxMeshDodgeIt(faces, normals, uvs);
        byte b;
        int j;
        BoxMeshBound[] arrayOfBoxMeshBound1;
        for (j = (arrayOfBoxMeshBound1 = bounds).length, b = 0; b < j; ) {
            BoxMeshBound bound = arrayOfBoxMeshBound1[b];
            r.addBound(bound);
            b++;
        }
        return r;
    }
}
