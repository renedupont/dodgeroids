package de.games.engine.box.format.chunk;

import de.games.engine.box.format.BoxChunkInterface;

public class BoxMesh implements BoxChunkInterface {
    protected float[] faces;
    protected float[] normals;
    protected float[] uvs;

    public BoxMesh(float[] faces, float[] normals, float[] uvs) {
        this.faces = faces;
        this.normals = normals;
        this.uvs = uvs;
    }

    public float[] getFaces() {
        return this.faces;
    }

    public void setFaces(float[] faces) {
        this.faces = faces;
    }

    public float[] getNormals() {
        return this.normals;
    }

    public void setNormals(float[] normals) {
        this.normals = normals;
    }

    public float[] getUvs() {
        return this.uvs;
    }

    public void setUvs(float[] uvs) {
        this.uvs = uvs;
    }
}
