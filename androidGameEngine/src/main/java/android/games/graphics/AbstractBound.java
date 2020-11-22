package android.games.graphics;

public abstract class AbstractBound {
	public enum DetectionMethod {
		INNER, OUTER
	}

	private final Mesh mesh;
	private Vector offset;

	public AbstractBound(final Vector offset, final Mesh mesh) {
		this.offset = offset;
		this.mesh = mesh;
	}

	public Vector getOffset() {
		return offset;
	}

	public void setOffset(final Vector offset) {
		this.offset = offset;
	}

	public Mesh getMesh() {
		return mesh;
	}

	protected boolean isCubeInSphere(final Node sphereNode,
			final SphereBound sphereBound, final Node boxNode,
			final BoxBound boxBound) {
		Vector min = Vector.add(boxBound.getMin(boxNode),
				Vector.add(boxNode.getPos(), boxBound.getOffset()));
		Vector max = Vector.add(boxBound.getMax(boxNode),
				Vector.add(boxNode.getPos(), boxBound.getOffset()));
		float radius = sphereBound.getRadius(sphereNode);
		return (new Vector(min.x, min.y, min.z).distance(sphereNode.getPos()) < radius
				&& new Vector(max.x, min.y, min.z)
						.distance(sphereNode.getPos()) < radius
				&& new Vector(min.x, max.y, min.z)
						.distance(sphereNode.getPos()) < radius
				&& new Vector(max.x, max.y, min.z)
						.distance(sphereNode.getPos()) < radius
				&& new Vector(min.x, min.y, max.z)
						.distance(sphereNode.getPos()) < radius
				&& new Vector(max.x, min.y, max.z)
						.distance(sphereNode.getPos()) < radius
				&& new Vector(min.x, max.y, max.z)
						.distance(sphereNode.getPos()) < radius && new Vector(
				max.x, max.y, max.z).distance(sphereNode.getPos()) < radius);
	}

	protected boolean isSphereInCube(final Node sphereNode,
			final SphereBound sphereBound, final Node boxNode,
			final BoxBound boxBound) {
		return false; // TODO: implementieren
	}

	private final float squared(final float v) {
		return v * v;
	}

	/** TODO: ungetestet **/
	protected boolean isSphereOutsideCube(final Node sphereNode,
			final SphereBound sphereBound, final Node boxNode,
			final BoxBound boxBound) {
		Vector posSphere = Vector.add(sphereNode.getPos(),
				sphereBound.getOffset());
		Vector minBox = Vector.add(boxBound.getMin(boxNode),
				Vector.add(boxNode.getPos(), boxBound.getOffset()));
		Vector maxBox = Vector.add(boxBound.getMax(boxNode),
				Vector.add(boxNode.getPos(), boxBound.getOffset()));
		float radiusSquared = sphereBound.getRadius(sphereNode)
				* sphereBound.getRadius(sphereNode);

		if (posSphere.x < minBox.x) {
			radiusSquared -= squared(posSphere.x - minBox.x);
		} else if (posSphere.x > maxBox.x) {
			radiusSquared -= squared(posSphere.x - maxBox.x);
		}
		if (posSphere.y < minBox.y) {
			radiusSquared -= squared(posSphere.y - minBox.y);
		} else if (posSphere.y > maxBox.y) {
			radiusSquared -= squared(posSphere.y - maxBox.y);
		}
		if (posSphere.z < minBox.z) {
			radiusSquared -= squared(posSphere.z - minBox.z);
		} else if (posSphere.z > maxBox.z) {
			radiusSquared -= squared(posSphere.z - maxBox.z);
		}

		return radiusSquared > 0;
	}

	public abstract boolean isCollidingWith(Node myNode,
			DetectionMethod myType, Node otherNode, DetectionMethod otherType,
			AbstractBound otherBound);
}
