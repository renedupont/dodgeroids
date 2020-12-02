package android.games.engine.objects;

import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL11;

import android.games.engine.datamanagers.Scene;
import android.games.engine.graphics.AbstractBound;
import android.games.engine.graphics.Material;
import android.games.engine.graphics.Mesh;
import android.games.engine.graphics.Mesh.RenderType;
import android.games.engine.graphics.Node;
import android.games.engine.graphics.RotationSettings;
import android.games.engine.graphics.Texture;
import android.games.engine.graphics.Vector;

public abstract class AbstractGameObject extends Node {

	protected boolean hidden = false;
	protected boolean transparent = false;
	public Texture texture;
	public HashMap<Mesh, RotationSettings> meshes;
	protected Vector velocity;
	public RenderType renderType = RenderType.TRIANGLES;
	protected Material material = null;

	// TODO wenn man noch ne arraylist mit den Meshes haette koennte man hier
	// Scene rauskriegen... und der code waere besser und an vielen stellen
	// besser wo getbounds aufgerufen wird
	// vll extra sogar ne liste fuer die bounds da getbounds glaub ich bei der
	// collision detection immer aufgerufen wird...
	// public HashMap<String, RotationSettings> meshIds; wird dann zu public
	// HashMap<Mesh, RotationSettings> meshIds;

	public AbstractGameObject(final Scene scene,
			final HashMap<Mesh, RotationSettings> meshIds,
			final Texture texture, final Vector velocity,
			final Vector startPosition) {
		this.meshes = new HashMap<Mesh, RotationSettings>();
		this.meshes = meshIds;
		this.texture = texture;
		this.velocity = velocity;
		this.setPosition(startPosition);
	}

	public boolean isWithinPositionThreshold(final float thresholdRadius) {
		return (getPosition().distance(new Vector()) < thresholdRadius);
	}

	public boolean isWithinPositionThreshold(final Vector min, final Vector max) { // TODO eigentlich waere eine Cube klasse cool die nen raum aufspannt und man hier anstatt der min und max vals uebergeben koennte. vll dann auch fuer die Boxbound interessant
		return (getPos().x > min.x && getPos().x < max.x && getPos().y > min.y
				&& getPos().y < max.y && getPos().z > min.z && getPos().z < max.z);
	}

	public abstract void update(float delta);

	public void updateAllMeshRotations(final float delta) {
		Vector v;
		for (RotationSettings rotationSettings : meshes.values()) {
			v = Vector.mul(rotationSettings.getRotationSpeed(), delta);
			v.set(v.x % 360, v.y % 360, v.z % 360);
			v = rotationSettings.getCurrentRotation().add(v);
			rotationSettings.setCurrentRotation(v);
		}
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(final boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public abstract void render(GL11 gl, RenderType type);

	public ArrayList<AbstractBound> getBounds() {
		ArrayList<AbstractBound> list = new ArrayList<AbstractBound>();
		for (Mesh mesh : meshes.keySet()) {
			for (AbstractBound bound : mesh.getBounds()) {
				list.add(bound);
			}
		}
		return list;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(final Material material) {
		this.material = material;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public void setVelocity(final Vector velocity) {
		this.velocity = velocity;
	}

	public void dispose() {
		texture.dispose();
		for (Mesh mesh : meshes.keySet()) {
			mesh.dispose();
		}
	}

}
