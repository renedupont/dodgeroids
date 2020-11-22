package android.games.objects;

import java.util.HashMap;
import java.util.Map.Entry;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.games.datamanagers.Scene;
import android.games.graphics.Mesh;
import android.games.graphics.Mesh.RenderType;
import android.games.graphics.RotationSettings;
import android.games.graphics.Texture;
import android.games.graphics.Vector;

public class TunnelPart extends AbstractGameObject {

	public TunnelPart(final HashMap<Mesh, RotationSettings> meshIds,
			final Texture textureId, final Vector velocity,
			final Vector startPosition) {
		this(null, meshIds, textureId, velocity, startPosition);
	}

	public TunnelPart(final Scene scene,
			final HashMap<Mesh, RotationSettings> meshIds,
			final Texture textureId, final Vector velocity,
			final Vector startPosition) {
		super(scene, meshIds, textureId, velocity, startPosition);
		this.transparent = true;
	}

	@Override
	public void render(final GL11 gl, final RenderType type) {
		gl.glDisable(GL10.GL_LIGHTING);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4f(1, 1, 1, 0.35f);
		gl.glEnable(GL10.GL_BLEND);
		// scene.getMesh("tunnelPart").render(type);
		for (Entry<Mesh, RotationSettings> entry : meshes.entrySet()) {
			entry.getKey().render(type);
		}
		gl.glColor4f(1, 1, 1, 1);
		gl.glDisable(GL10.GL_BLEND);
		gl.glEnable(GL10.GL_LIGHTING);
	}

	@Override
	public void update(final float delta) {
		pos.z += velocity.z * delta;
	}
}
