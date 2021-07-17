package de.games.engine.box.format.chunk;

import de.games.engine.box.format.BoxChunkInterface;
import de.games.engine.box.format.chunk.meshDodgeroidsStuff.Material;
import de.games.engine.box.primitive.BoxString;
import java.util.ArrayList;

public class BoxMeshDodgeroids implements BoxChunkInterface {

    protected float[] faces;
    protected float[] normals;
    protected float[] uvs;
    protected final ArrayList<BoxMeshBound> bounds = new ArrayList<>();

    protected boolean hasMaterial = false;

    protected BoxString id;

    protected float[] ambient;

    protected float[] diffuse;
    protected float[] specular;
    protected float[] emissive;
    protected float shininess;
    protected float opacity = 1.0F;

    protected Material mat;

    public BoxMeshDodgeroids(float[] faces, float[] normals, float[] uvs) {
        this(faces, normals, uvs, null);
    }

    public BoxMeshDodgeroids(float[] faces, float[] normals, float[] uvs, Material mat) {
        this.faces = faces;
        this.normals = normals;
        this.uvs = uvs;
        if (mat != null) {
            this.hasMaterial = true;
            this.mat = mat;
            this.id = new BoxString(mat.id);
            this.ambient = mat.ambient.getRGBA();
            this.diffuse = mat.diffuse.getRGBA();
            this.specular = mat.specular.getRGBA();
            this.emissive = mat.emissive.getRGBA();
            this.shininess = mat.shininess;
            this.opacity = mat.opacity;
        }
    }

    public boolean addBound(BoxMeshBound bound) {
        return this.bounds.add(bound);
    }

    public BoxMeshBound[] getBounds() {
        BoxMeshBound[] r = new BoxMeshBound[this.bounds.size()];
        for (int i = 0; i < this.bounds.size(); i++) {
            r[i] = this.bounds.get(i);
        }
        return r;
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

    public float[] getUvs() {
        return this.uvs;
    }

    public void setUvs(float[] uvs) {
        this.uvs = uvs;
    }

    public boolean hasMaterial() {
        return this.hasMaterial;
    }

    public BoxString getId() {
        return this.id;
    }

    public void setId(BoxString id) {
        this.id = id;
    }

    public float[] getAmbient() {
        return this.ambient;
    }

    public float[] getDiffuse() {
        return this.diffuse;
    }

    public void setDiffuse(float[] diffuse) {
        this.diffuse = diffuse;
    }

    public float[] getSpecular() {
        return this.specular;
    }

    public float[] getEmissive() {
        return this.emissive;
    }

    public float getShininess() {
        return this.shininess;
    }

    public float getOpacity() {
        return this.opacity;
    }
}
