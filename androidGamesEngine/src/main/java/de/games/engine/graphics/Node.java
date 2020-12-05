package de.games.engine.graphics;

public class Node {

    protected final Vector pos = new Vector();
    protected final Vector rot = new Vector();
    protected final Vector scale = new Vector(1.0f, 1.0f, 1.0f);
    protected final Vector left = new Vector(1f, 0f, 0f);
    protected final Vector up = new Vector(0f, 1f, 0f);
    protected final Vector forward = new Vector(0f, 0f, 1f);

    /*************************
     * GLOBAL TRANSFOMATIONS *
     *************************/

    public Vector getPos() {
        return pos;
    }

    public Vector getRot() {
        return rot;
    }

    public Vector getScale() {
        return scale;
    }

    public Vector getPosition() {
        return pos;
    }

    public Vector getRotation() {
        return rot;
    }

    public void setPos(final float x, final float y, final float z) {
        pos.set(x, y, z);
    }

    public void setPos(final Vector v) {
        pos.set(v);
    }

    public void setPosition(final Vector v) {
        pos.set(v);
    }

    public void setScale(final Vector v) {
        scale.set(v);
    }

    public void setPosition(final float x, final float y, final float z) {
        pos.set(x, y, z);
    }

    public void setScale(final float x, final float y, final float z) {
        scale.set(x, y, z);
    }

    public void setRot(final Vector v) {
        rot.x = v.x % 360.0f;
        rot.y = v.y % 360.0f;
        rot.z = v.z % 360.0f;
    }

    public void setRot(final float x, final float y, final float z) {
        rot.x = x % 360.0f;
        rot.y = y % 360.0f;
        rot.z = z % 360.0f;
    }

    public void setRotation(final Vector v) {
        rot.x = v.x % 360.0f;
        rot.y = v.y % 360.0f;
        rot.z = v.z % 360.0f;
    }

    public void setRotation(final float x, final float y, final float z) {
        rot.x = x % 360.0f;
        rot.y = y % 360.0f;
        rot.z = z % 360.0f;
    }

    public void rotate(final Vector v) {
        rot.x = (rot.x + v.x) % 360.0f;
        rot.y = (rot.y + v.y) % 360.0f;
        rot.z = (rot.z + v.z) % 360.0f;
    }

    public void rotate(final float x, final float y, final float z) {
        rot.x = (rot.x + x) % 360.0f;
        rot.y = (rot.y + y) % 360.0f;
        rot.z = (rot.z + z) % 360.0f;
    }

    public void setPosition(final Node n, final float x, final float y, final float z) {
        pos.set(toLocal(n, x, y, z));
    }

    public Vector getPosition(final Node n) {
        return toLocal(n, pos.x, pos.y, pos.z);
    }

    // public Vector getRotation(Node n) {
    // return toLocal(n, pos.x, pos.y, pos.z);
    // }

    /************************
     * LOCAL TRANSFOMATIONS *
     ************************/

    // public void rotateLocal(Vector amount) {
    // rotate(0f, (float)amount, 0f);
    // anglesToAxes(getRot());
    // }

    public void setPosition(final Node n, final Vector v) {
        // pos.set(toLocal(n, v.x, v.y, v.z));
        anglesToAxes(n.rot);
        pos.set(n.pos);
        getPos().add(Vector.mul(forward, Math.abs(v.z)));
        getPos().add(Vector.mul(left, Math.abs(v.x)));
        getPos().add(Vector.mul(up, Math.abs(v.y)));
    }

    protected void anglesToAxes(final Vector angles) {
        float DEG2RAD = 3.141593f / 180;
        float sx, sy, sz, cx, cy, cz, theta;

        // rotation angle about X-axis (pitch)
        theta = angles.x * DEG2RAD;
        sx = (float) Math.sin(theta);
        cx = (float) Math.cos(theta);

        // rotation angle about Y-axis (yaw)
        theta = angles.y * DEG2RAD;
        sy = (float) Math.sin(theta);
        cy = (float) Math.cos(theta);

        // rotation angle about Z-axis (roll)
        theta = angles.z * DEG2RAD;
        sz = (float) Math.sin(theta);
        cz = (float) Math.cos(theta);

        // determine left axis
        left.x = cy * cz;
        left.y = sx * sy * cz + cx * sz;
        left.z = -cx * sy * cz + sx * sz;

        // determine up axis
        up.x = -cy * sz;
        up.y = -sx * sy * sz + cx * cz;
        up.z = cx * sy * sz + sx * cz;

        // determine forward axis
        forward.x = sy;
        forward.y = -sx * cy;
        forward.z = cx * cy;
    }

    protected Vector toLocal(final float dx, final float dy, final float dz) {
        return toLocal(this, dx, dy, dz);
    }

    protected Vector toLocal(final Node n, final float dx, final float dy, final float dz) {
        // Don't calculate for nothing ...
        if (dx == 0.0f & dy == 0.0f && dz == 0.0f) {
            return new Vector();
        }

        // Convert to Radian : 360 degree = 2PI
        double xRot = Math.toRadians(n.getRot().x); // Math.toRadians is
        // toRadians in Java 1.5
        // (static import)
        double yRot = Math.toRadians(n.getRot().y);

        // Calculate the formula
        float x =
                (float)
                        (dx * Math.cos(yRot)
                                + dy * Math.sin(xRot) * Math.sin(yRot)
                                - dz * Math.cos(xRot) * Math.sin(yRot));
        float y = (float) (+dy * Math.cos(xRot) + dz * Math.sin(xRot));
        float z =
                (float)
                        (dx * Math.sin(yRot)
                                - dy * Math.sin(xRot) * Math.cos(yRot)
                                + dz * Math.cos(xRot) * Math.cos(yRot));

        // Return the vector expressed in the global axis system
        return new Vector(x, y, z);
    }
}
