package de.games.engine.box.format.chunk;

import de.games.engine.box.format.BoxChunkInterface;
import de.games.engine.box.format.chunk.meshDodgeItStuff.Material;
import de.games.engine.box.primitive.BoxString;
import java.util.ArrayList;

public class BoxMeshDodgeIt implements BoxChunkInterface {
    protected float[] faces;
    protected float[] normals;
    protected float[] uvs;
    protected final ArrayList<BoxMeshBound> bounds = new ArrayList<BoxMeshBound>();

    protected boolean hasMaterial = false;

    protected BoxString id;

    protected float[] ambient;

    protected float[] diffuse;
    protected float[] specular;
    protected float[] emissive;
    protected float shininess;
    protected float opacity = 1.0F;

    protected Material mat;

    public BoxMeshDodgeIt(float[] faces, float[] normals, float[] uvs) {
        this(faces, normals, uvs, null);
    }

    public BoxMeshDodgeIt(float[] faces, float[] normals, float[] uvs, Material mat) {
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

    public void setNormals(float[] normals) {
        this.normals = normals;
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

    public void setHasMaterial(boolean hasMaterial) {
        this.hasMaterial = hasMaterial;
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

    public void setAmbient(float[] ambient) {
        this.ambient = ambient;
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

    public void setSpecular(float[] specular) {
        this.specular = specular;
    }

    public float[] getEmissive() {
        return this.emissive;
    }

    public void setEmissive(float[] emissive) {
        this.emissive = emissive;
    }

    public float getShininess() {
        return this.shininess;
    }

    public void setShininess(float shininess) {
        this.shininess = shininess;
    }

    public float getOpacity() {
        return this.opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public Material getMat() {
        return this.mat;
    }

    public void setMat(Material mat) {
        this.mat = mat;
    }
}
