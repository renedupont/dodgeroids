package android.games.engine.objects;

import java.util.HashMap;

import javax.microedition.khronos.opengles.GL11;

import android.games.engine.datamanagers.Scene;
import android.games.engine.graphics.Mesh;
import android.games.engine.graphics.Mesh.RenderType;
import android.games.engine.graphics.RotationSettings;
import android.games.engine.graphics.Texture;
import android.games.engine.graphics.Vector;

public class Block extends AbstractGameObject {

	public Block(final Scene scene,
			final HashMap<Mesh, RotationSettings> meshIds,
			final Texture texture, final Vector velocity,
			final Vector startPosition) {
		super(scene, meshIds, texture, velocity, startPosition);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(final float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(final GL11 gl, final RenderType type) {
		// TODO Auto-generated method stub

	}

}
