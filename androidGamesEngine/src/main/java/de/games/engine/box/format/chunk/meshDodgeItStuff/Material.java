package de.games.engine.box.format.chunk.meshDodgeItStuff;

public class Material {
    public String id;
    public MaterialType type;
    public Color ambient;
    public Color diffuse;
    public Color specular;
    public Color emissive;
    public float shininess;
    public float opacity;

    public Material() {
        this.opacity = 1.0F;
    }

    public enum MaterialType {
        Lambert,
        Phong;
    }
}
