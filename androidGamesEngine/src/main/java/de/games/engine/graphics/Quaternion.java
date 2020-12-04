package de.games.engine.graphics;

public class Quaternion {
	protected float x, y, z, w;

	public Quaternion(final float x, final float y, final float z, final float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Quaternion(final double angle, final Vector v) {
		this.w = (float) Math.cos(angle / 2);
		this.x = (float) Math.sin(angle / 2) * v.x;
		this.y = (float) Math.sin(angle / 2) * v.y;
		this.z = (float) Math.sin(angle / 2) * v.z;
	}

	public Quaternion mul(final Quaternion other) {
		float scalarbit = this.w * other.w - this.x * other.x - this.y
				* other.y - this.z * other.z;
		float newx = this.y * other.z - this.z * other.y + this.w * other.x
				+ other.w * this.x;
		float newy = -this.x * other.z + other.x * this.z + this.w * other.y
				+ other.w * this.y;
		float newz = this.x * other.y - other.x * this.y + this.w * other.z
				+ other.w * this.z;
		return new Quaternion(newx, newy, newz, scalarbit);
	}

	public Quaternion inverse() {
		float i = x * x + y * y + z * z + w * w;
		i = 1.0f / i;
		return new Quaternion(w * i, -x * i, -y * i, -z * i);
	}

	public Vector getVector() {
		return new Vector(x, y, z);
	}
}
