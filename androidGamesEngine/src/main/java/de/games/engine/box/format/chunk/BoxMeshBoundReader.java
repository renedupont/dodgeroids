package de.games.engine.box.format.chunk;

import de.games.engine.box.io.BinaryReader;
import de.games.engine.box.primitive.BoxString;
import de.games.engine.box.primitive.BoxStringReader;
import java.io.IOException;

public class BoxMeshBoundReader {
    public BoxMeshBound read(BinaryReader reader) throws IOException {
        BoxMeshBound.Shape theShape;
        BoxStringReader stringReader = new BoxStringReader();
        BoxString identifier = stringReader.read(reader);
        byte shape = reader.readByte();
        float[] minValues = new float[3];
        float[] maxValues = new float[3];
        float radius = 0.0F;
        switch (shape) {
            case 0:
                theShape = BoxMeshBound.Shape.Cube;
                minValues = reader.readFloat(3);
                maxValues = reader.readFloat(3);
                break;
            case 1:
                theShape = BoxMeshBound.Shape.Circle;
                radius = reader.readFloat();
                break;
            default:
                theShape = BoxMeshBound.Shape.Circle;
                break;
        }
        BoxMeshDodgeroidsReader meshReader = new BoxMeshDodgeroidsReader();
        BoxMeshDodgeroids mesh = meshReader.read(reader);
        BoxMeshBound bound = new BoxMeshBound(identifier, theShape, mesh);
        switch (theShape) {
            case Circle:
                bound.setRadius(radius);
                break;
            case Cube:
                bound.setMinValues(minValues);
                bound.setMaxValues(maxValues);
                break;
        }
        return bound;
    }
}
