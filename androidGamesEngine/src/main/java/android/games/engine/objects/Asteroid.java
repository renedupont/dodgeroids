package android.games.engine.objects;

import java.util.HashMap;
import java.util.Map.Entry;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.games.engine.datamanagers.Scene;
import android.games.engine.graphics.Mesh;
import android.games.engine.graphics.Mesh.RenderType;
import android.games.engine.graphics.RotationSettings;
import android.games.engine.graphics.Texture;
import android.games.engine.graphics.Vector;

public class Asteroid extends AbstractGameObject {

	private float[][] transparencyZPosAndValue;

	// private int asteroidType;

	public Asteroid(final HashMap<Mesh, RotationSettings> meshIds,
			final Texture textureId, final float scaleFactor,
			final Vector velocity, final Vector startPosition,
			final float[][] transparencyZPosAndValue) {
		this(null, meshIds, textureId, scaleFactor, velocity, startPosition);
		this.transparencyZPosAndValue = transparencyZPosAndValue;
		this.transparent = true;
	}

	public Asteroid(final Scene scene,
			final HashMap<Mesh, RotationSettings> meshIds,
			final Texture textureId, final float scaleFactor,
			final Vector velocity, final Vector startPosition) {
		super(scene, meshIds, textureId, velocity, startPosition);
		setScale(scaleFactor, scaleFactor, scaleFactor);
		// this.asteroidType = new Random().nextInt(4);
		// WHATSE FACK WAR DAS NOCHMAL FUER? und wieso so kompliziert? war das
		// dafuer das steine am anfang nicht alle von selber rotation starten?
		rot.x = 2.0f * ((float) Math.random() / 1.0f) - 1.0f;
		rot.y = 2.0f * ((float) Math.random() / 1.0f) - 1.0f;
		rot.z = 2.0f * ((float) Math.random() / 1.0f) - 1.0f;
	}

	@Override
	public void update(final float delta) {
		updateAllMeshRotations(delta);
		pos.z += (velocity.z * delta);
	}

	@Override
	public void render(final GL11 gl, final RenderType type) {
		if (transparent) {
			// TODO: noch so optimieren das dies nur bei den vordersten
			// asteroiden abgefragt wird?
			for (int i = 0; i < transparencyZPosAndValue.length; i++) {
				if (pos.z > transparencyZPosAndValue[i][0]) {
					gl.glDisable(GL10.GL_LIGHTING);
					gl.glBlendFunc(GL10.GL_SRC_ALPHA,
							GL10.GL_ONE_MINUS_SRC_ALPHA);
					gl.glColor4f(1, 1, 1, transparencyZPosAndValue[i][1]);
					gl.glEnable(GL10.GL_BLEND);
					for (Entry<Mesh, RotationSettings> entry : meshes
							.entrySet()) {
						entry.getKey().render(type);
					}
					gl.glColor4f(1, 1, 1, 1);
					gl.glDisable(GL10.GL_BLEND);
					gl.glEnable(GL10.GL_LIGHTING);
					return;
				}
			}
		}

		Vector rotation;
		for (Entry<Mesh, RotationSettings> entry : meshes.entrySet()) {
			gl.glPushMatrix();
			rotation = entry.getValue().getCurrentRotation();
			gl.glRotatef(rotation.x, 1, 0, 0);
			gl.glRotatef(rotation.y, 0, 1, 0);
			gl.glRotatef(rotation.z, 0, 0, 1);
			entry.getKey().render(type);
			gl.glPopMatrix();
		}
	}

}
