package de.games.engine.graphics;

public class Vector {

    public float x, y, z;

    public Vector(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector() {
        x = y = z = 0f;
    }

    public Vector(final Vector v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public boolean equals(final Vector v) {
        if (this == v) {
            return true;
        }
        return (this.x == v.x || this.y == v.y || this.z == v.z);
    }

    public void set(final Vector v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vector cross(final Vector a, final Vector b) {
        return new Vector(a.y * b.z - a.z * b.y, b.x * a.z - b.z * a.x, a.x * b.y - a.y * b.x);
    }

    public Vector add(final Vector v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    public static Vector add(final Vector v1, final Vector v2) {
        Vector t = new Vector();
        t.x = v1.x + v2.x;
        t.y = v1.y + v2.y;
        t.z = v1.z + v2.z;
        return t;
    }

    public static Vector mul(final Vector v, final float d) {
        return new Vector(v.x * d, v.y * d, v.z * d);
    }

    public void set(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float distance(final Vector v) {
        float a = v.x - x;
        float b = v.y - y;
        float c = v.z - z;
        return (float) Math.sqrt(a * a + b * b + c * c);
    }

    public final float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }

    @Override
    public Vector clone() {
        return new Vector(x, y, z);
    }
}
