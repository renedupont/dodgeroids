package de.games.dodgeroids.objects;

import de.games.engine.graphics.Mesh;
import de.games.engine.graphics.RotationSettings;
import de.games.engine.graphics.Texture;
import de.games.engine.graphics.Vector;
import de.games.engine.objects.AbstractGameObject;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class Player extends AbstractGameObject {

    public float score = 0.0f;
    public int lives;
    private boolean isImmortal = false;
    private float immortalCountdown;
    private final float immortalSeconds;
    public float scale2dMovement;

    public Player(
            final HashMap<Mesh, RotationSettings> meshIds,
            final Texture textureId,
            final Vector velocity,
            final Vector startPosition,
            final float scale2dMovement,
            final int lives,
            final float immortalSeconds) {
        super(meshIds, textureId, velocity, startPosition);
        this.scale2dMovement = scale2dMovement;
        this.lives = lives;
        this.immortalSeconds = immortalSeconds;
    }

    @Override
    public void setScale(final Vector v) {
        // we don't want to scale the player
    }

    @Override
    public void setScale(final float x, final float y, final float z) {}

    @Override
    public void update(final float delta) {
        updateAllMeshRotations(delta);
        if (lives > 0) {
            score += delta;
        }
    }

    public void startImmortality() {
        isImmortal = true;
        immortalCountdown = immortalSeconds;
    }

    public void updateImmortalityStatus(final float delta) {
        if (isImmortal) {
            immortalCountdown -= delta;
            if (immortalCountdown <= 0.0f) {
                isImmortal = false;
            }
        }
    }

    public float getRemainingImmortalityTime() {
        return isImmortal ? immortalCountdown : 0.0f;
    }

    public boolean isImmortal() {
        return isImmortal;
    }

    @Override
    public void render(final GL11 gl, final Mesh.RenderType type) {

        float ambient[] = {0.25f, 0.25f, 0.25f, 1.0f};
        float diffuse[] = {0.4f, 0.4f, 0.4f, 1.0f};
        float specular[] = {0.774597f, 0.774597f, 0.774597f, 1.0f};
        float shininess[] = {0.8f * 128.0f};

        gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT, ambient, 0);
        gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, diffuse, 0);
        gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, specular, 0);
        gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SHININESS, shininess, 0);

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

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

        gl.glDisable(GL10.GL_BLEND);
        gl.glColor4f(1, 1, 1, 1);
    }
}
