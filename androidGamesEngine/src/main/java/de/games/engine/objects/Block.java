package de.games.engine.objects;

import de.games.engine.datamanagers.Scene;
import de.games.engine.graphics.Mesh;
import de.games.engine.graphics.RotationSettings;
import de.games.engine.graphics.Texture;
import de.games.engine.graphics.Vector;
import java.util.HashMap;
import javax.microedition.khronos.opengles.GL11;

public class Block extends AbstractGameObject {

    public Block(
            final Scene scene,
            final HashMap<Mesh, RotationSettings> meshIds,
            final Texture texture,
            final Vector velocity,
            final Vector startPosition) {
        super(scene, meshIds, texture, velocity, startPosition);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void update(final float delta) {
        // TODO Auto-generated method stub

    }

    @Override
    public void render(final GL11 gl, final Mesh.RenderType type) {
        // TODO Auto-generated method stub

    }
}
