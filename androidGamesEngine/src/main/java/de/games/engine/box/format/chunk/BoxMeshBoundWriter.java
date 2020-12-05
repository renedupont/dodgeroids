package de.games.engine.box.format.chunk;

import de.games.engine.box.format.BoxChunkWriter;
import de.games.engine.box.io.ByteConversion;
import de.games.engine.box.primitive.BoxStringWriter;
import java.io.IOException;

public class BoxMeshBoundWriter extends BoxChunkWriter {
    public BoxMeshBoundWriter(BoxMeshBound bound) throws IOException {
        byte[][] raw = new byte[(bound.getShape() == BoxMeshBound.Shape.Circle) ? 4 : 5][];
        int offset = 0;
        BoxStringWriter stringWriter = new BoxStringWriter(bound.getIdentifier());
        raw[offset] = stringWriter.getRawBytes();
        raw[offset] = new byte[] {(byte) (bound.getShape() == BoxMeshBound.Shape.Circle ? 1 : 0)};
        switch (bound.getShape()) {
            case Circle:
                raw[++offset] = ByteConversion.floatToBytes(bound.getRadius());
                break;
            case Cube:
                raw[++offset] = ByteConversion.floatArrayToBytes(bound.getMinValues());
                raw[++offset] = ByteConversion.floatArrayToBytes(bound.getMaxValues());
                break;
        }
        BoxMeshDodgeItWriter meshWriter = new BoxMeshDodgeItWriter(bound.getMesh());
        raw[++offset] = meshWriter.getRawBytes();
        setRawBytes(concat(raw));
    }
}
