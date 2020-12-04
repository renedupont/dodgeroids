package de.games.engine.graphics;

public class Material {

	public enum MaterialType {
		Lambert, Phong
	}

	public String id;

	public MaterialType type;

	public Color ambient;
	public Color diffuse;
	public Color specular;
	public Color emissive;

	public float shininess;
	public float opacity = 1.f;

	// public Array<ModelTexture> textures;
}
