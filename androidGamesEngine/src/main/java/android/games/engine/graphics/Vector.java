package android.games.engine.graphics;

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

	public void set(final float f) {
		this.x = f;
		this.y = f;
		this.z = f;
	}

	public Vector cross(final Vector v) {
		set(Vector.cross(this, v));
		return this;
	}

	public static Vector cross(final Vector a, final Vector b) {
		Vector t = new Vector(
		// a.y * b.z - b.y * a.z,
		// a.z * b.x - b.z * a.x,
		// a.x * b.y - b.x * a.y);

				a.y * b.z - a.z * b.y, b.x * a.z - b.z * a.x, a.x * b.y - a.y
						* b.x);

		// x = v1.y*v2.z - v1.z*v2.y;
		// y = v2.x*v1.z - v2.z*v1.x;
		// z = v1.x*v2.y - v1.y*v2.x;

		// float xh = a.y * b.z - b.y * a.z;
		// float yh = a.z * b.x - b.z * a.x;
		// float zh = a.x * b.y - b.x * a.y;
		// return new Vector(xh, yh, zh);
		return t;
	}

	public Vector normalize() {
		// Vector v2 = new Vector();
		// float length = (float)Math.sqrt( x*x + y*y + z*z );
		// if (length != 0) {
		// v2.x = x/length;
		// v2.y = y/length;
		// v2.z = z/length;
		// }
		// return v2;

		float norm;

		norm = (float) (1.0 / Math.sqrt(x * x + y * y + z * z));
		this.x *= norm;
		this.y *= norm;
		this.z *= norm;
		return this;
	}

	public static Vector normalize(Vector v) {
		v = v.clone();
		return v.normalize();
	}

	public float max() {
		return Math.max(x, Math.max(y, z));
	}

	public float min() {
		return Math.min(x, Math.min(y, z));
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

	public Vector sub(final Vector v) {
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
		return this;
	}

	public static Vector sub(final Vector v1, final Vector v2) {
		Vector t = new Vector(v1.x, v1.y, v1.z);
		t.x -= v2.x;
		t.y -= v2.y;
		t.z -= v2.z;
		return t;
	}

	public static Vector abs(final Vector v) {
		return new Vector(v).abs();
	}

	public Vector abs() {
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);
		return this;
	}

	public Vector mul(final double d) {
		this.x *= d;
		this.y *= d;
		this.z *= d;
		return this;
	}

	public static Vector mul(final Vector v, final float d) {
		return new Vector(v.x * d, v.y * d, v.z * d);
	}

	public float dot(final Vector v) {
		return (x * v.x + y * v.y + z * v.z);
	}

	public static float dot(final Vector v1, final Vector v2) {
		return (v1.x * v2.x + v1.y * v2.y + v1.z * v2.z);
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
		return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z
				* this.z);
	}

	public final float angle(final Vector v1) {
		double vDot = this.dot(v1) / (this.length() * v1.length());
		if (vDot < -1.0) {
			vDot = -1.0;
		}
		if (vDot > 1.0) {
			vDot = 1.0;
		}
		return ((float) (Math.acos(vDot)));
	}

	public float Xangle(final Vector v) {
		Vector t = Vector.normalize(v);
		Vector s = Vector.normalize(this);
		return (float) Math.acos(s.dot(t));
	}

	/*
	 * public Vector rotate(double angle, Vector axis) { // Quaternion p = new
	 * Quaternion(0,this.x, this.y, this.z); Quaternion p = new
	 * Quaternion(this.x, this.y, this.z, 0); Quaternion rot = new
	 * Quaternion(angle, axis); Quaternion c = rot.mul(p.mul(rot.inverse()));
	 * return c.getVector(); }
	 */

	public Vector rotate(final double angle, final Vector axis) {
		/**
		 * See Hearn and Baker p.419
		 * 
		 */
		Quaternion p = new Quaternion(0, this.x, this.y, this.z);
		Quaternion rot = new Quaternion(angle, axis);
		Quaternion c = rot.mul(p.mul(rot.inverse()));
		return c.getVector();
	}

	public Vector orthogonalProjection3D(final Vector v1, final Vector v2) {
		return Vector.orthogonalProjection3D(v1, v1, this);
	}

	public static Vector orthogonalProjection3D(final Vector v1,
			final Vector v2, final Vector x) {
		Vector vGreater;
		Vector vSmaller;
		if (v1.length() <= v2.length()) {
			vSmaller = v1;
			vGreater = v2;
		} else {
			vSmaller = v2;
			vGreater = v1;
		}

		Vector directionVector = new Vector(vGreater.x - vSmaller.x, vGreater.y
				- vSmaller.y, vGreater.z - vSmaller.z);
		float scalar = Vector.dot(x, directionVector)
				/ Vector.dot(directionVector, directionVector);

		return new Vector(scalar * directionVector.x + vSmaller.x, scalar
				* directionVector.y + vSmaller.y, scalar * directionVector.z
				+ vSmaller.z);
	}

	public double angleWith(final Vector v) {
		// if ( Math.abs(x-other.x)<1e-3 && Math.abs(y-other.y)<1e-3 &&
		// Math.abs(z-other.z)<1e-3)
		// return 0;
		// angle = atan2(norm(cross(a,b)),dot(a,b));

		// double angle = Math.atan2(this.cross(v).getMag(), this.dot(v));
		double angle = Math.atan2(cross(this, v).getMag(), this.dot(v));
		return angle;

		// Vector dis = normalize();
		// v.normalize();
		// double cos = dis.dot(v);
		// return Math.acos(cos);
	}

	public boolean isZero() {
		if (x == 0.0 && y == 0.0 && z == 0.0) {
			return true;
		} else {
			return false;
		}
	}

	public double getMag() {
		double mag = x * x + y * y + z * z;
		mag = Math.sqrt(mag);
		return mag;
	}

	@Override
	public String toString() {
		String s = "[" + x + ", " + y + ", " + z + "]";
		return s;
	}

	@Override
	public Vector clone() {
		Vector t = new Vector(x, y, z);
		return t;
	}
}
