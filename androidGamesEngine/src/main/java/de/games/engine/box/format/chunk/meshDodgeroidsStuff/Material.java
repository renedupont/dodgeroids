package de.games.engine.box.format.chunk.meshDodgeroidsStuff;

public class Material {

    public String id;
    public Color ambient;
    public Color diffuse;
    public Color specular;
    public Color emissive;
    public float shininess;
    public float opacity;

    public Material() {
        this.opacity = 1.0F;
    }
}
