package de.games.engine.objects;

import de.games.engine.graphics.AbstractBound;
import de.games.engine.graphics.Mesh;
import de.games.engine.graphics.Node;
import de.games.engine.graphics.RotationSettings;
import de.games.engine.graphics.Texture;
import de.games.engine.graphics.Vector;
import java.util.ArrayList;
import java.util.HashMap;
import javax.microedition.khronos.opengles.GL11;

public abstract class AbstractGameObject extends Node {

    protected boolean hidden = false;
    protected boolean transparent = false;
    public Texture texture;
    public HashMap<Mesh, RotationSettings> meshes;
    protected Vector velocity;
    public Mesh.RenderType renderType = Mesh.RenderType.TRIANGLES;

    public AbstractGameObject(
            final HashMap<Mesh, RotationSettings> meshIds,
            final Texture texture,
            final Vector velocity,
            final Vector startPosition) {
        this.meshes = new HashMap<>();
        this.meshes = meshIds;
        this.texture = texture;
        this.velocity = velocity;
        this.setPosition(startPosition);
    }

    public boolean isWithinPositionThreshold(final Vector min, final Vector max) {
        // TODO eigentlich waere eine Cube klasse cool die nen raum aufspannt und
        // man hier anstatt der min und max vals uebergeben koennte. vll dann
        // auch fuer die Boxbound interessant
        return (getPos().x > min.x
                && getPos().x < max.x
                && getPos().y > min.y
                && getPos().y < max.y
                && getPos().z > min.z
                && getPos().z < max.z);
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

    public abstract void render(GL11 gl, Mesh.RenderType type);

    public ArrayList<AbstractBound> getBounds() {
        ArrayList<AbstractBound> list = new ArrayList<>();
        for (Mesh mesh : meshes.keySet()) {
            for (AbstractBound bound : mesh.getBounds()) {
                list.add(bound);
            }
        }
        return list;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void dispose() {
        texture.dispose();
        for (Mesh mesh : meshes.keySet()) {
            mesh.dispose();
        }
    }
}
