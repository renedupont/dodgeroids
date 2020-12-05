package de.games.engine.box.format.chunk;

import de.games.engine.box.primitive.BoxString;

public class BoxMeshBound {
    protected BoxString identifier;
    protected Shape shape;
    protected float[] minValues;
    protected float[] maxValues;
    protected float radius;
    protected BoxMeshDodgeIt mesh;

    public enum Shape {
        Circle,
        Cube;
    }

    public BoxMeshBound(BoxString identifier, Shape shape, BoxMeshDodgeIt mesh) {
        float x, y, z, a, b, c;
        int i;
        this.minValues = new float[3];
        this.maxValues = new float[3];
        this.identifier = identifier;
        this.shape = shape;
        this.mesh = mesh;
        float[] vertices = mesh.getFaces();
        switch (shape) {
            case Circle:
                x = 0.0F;
                y = 0.0F;
                z = 0.0F;
                a = x - vertices[0];
                b = y - vertices[1];
                c = z - vertices[2];
                this.radius = (float) Math.sqrt((a * a + b * b + c * c));
                break;

            case Cube:
                this.minValues[0] = Float.POSITIVE_INFINITY;
                this.minValues[1] = Float.POSITIVE_INFINITY;
                this.minValues[2] = Float.POSITIVE_INFINITY;
                this.maxValues[0] = Float.NEGATIVE_INFINITY;
                this.maxValues[1] = Float.NEGATIVE_INFINITY;
                this.maxValues[2] = Float.NEGATIVE_INFINITY;
                for (i = 0; i < vertices.length; i += 3) {
                    if (vertices[i] < this.minValues[0]) {
                        this.minValues[0] = vertices[i];
                    } else if (vertices[i] > this.maxValues[0]) {
                        this.maxValues[0] = vertices[i];
                    }
                    if (vertices[i + 1] < this.minValues[1]) {
                        this.minValues[1] = vertices[i + 1];
                    } else if (vertices[i + 1] > this.maxValues[1]) {
                        this.maxValues[1] = vertices[i + 1];
                    }
                    if (vertices[i + 2] < this.minValues[2]) {
                        this.minValues[2] = vertices[i + 2];
                    } else if (vertices[i + 2] > this.maxValues[2]) {
                        this.maxValues[2] = vertices[i + 2];
                    }
                }
                break;
        }
    }

    public Shape getShape() {
        return this.shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public float[] getMinValues() {
        return this.minValues;
    }

    public void setMinValues(float[] minValues) {
        this.minValues = minValues;
    }

    public float[] getMaxValues() {
        return this.maxValues;
    }

    public void setMaxValues(float[] maxValues) {
        this.maxValues = maxValues;
    }

    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public BoxMeshDodgeIt getMesh() {
        return this.mesh;
    }

    public void setMesh(BoxMeshDodgeIt mesh) {
        this.mesh = mesh;
    }

    public BoxString getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(BoxString identifier) {
        this.identifier = identifier;
    }
}
