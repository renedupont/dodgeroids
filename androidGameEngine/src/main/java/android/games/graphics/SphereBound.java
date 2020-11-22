package android.games.graphics;

public class SphereBound extends AbstractBound {
	private final float radius; // nicht verwenden, nur getRadius()

	public SphereBound(final float radius, final Vector offset, final Mesh mesh) {
		super(offset, mesh);
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}

	public float getRadius(final Node node) {
		return radius * node.getScale().x;
	}

	public boolean isCollidingWith(final Node myNode, final Node otherNode,
			final SphereBound otherBound, final DetectionMethod myType,
			final DetectionMethod otherType) {
		switch (myType) {
		case INNER:
			switch (otherType) {
			case INNER: // pruefen ob beide ineinander sind (geht nicht)
				return false;
			case OUTER: // pruefen ob otherNode in myNode drin ist
				// return (Vector.add(myNode.getPos(),
				// getOffset()).distance(Vector.add(otherNode.getPos(),
				// otherBound.getOffset())) + this.radius >=
				// otherBound.getRadius());
				return (Vector.add(myNode.getPos(), getOffset()).distance(
						Vector.add(otherNode.getPos(), otherBound.getOffset()))
						+ otherBound.getRadius(otherNode) >= getRadius(myNode));
			}
		case OUTER:
			switch (otherType) {
			case INNER: // pruefen ob myNode in otherNode ist
				// return (Vector.add(myNode.getPos(),
				// getOffset()).distance(Vector.add(otherNode.getPos(),
				// otherBound.getOffset())) + otherBound.getRadius() >=
				// this.radius);
				return (Vector.add(myNode.getPos(), getOffset()).distance(
						Vector.add(otherNode.getPos(), otherBound.getOffset()))
						+ getRadius(myNode) >= otherBound.getRadius(otherNode));
			case OUTER: // pruefen ob zwei Kugeln nicht ueberlappen
				return (Vector.add(myNode.getPos(), getOffset()).distance(
						Vector.add(otherNode.getPos(), otherBound.getOffset())) <= (otherBound
						.getRadius(otherNode) + getRadius(myNode)));
			}
		}
		throw new RuntimeException("Impossible comparison");
	}

	public boolean isCollidingWith(final Node myNode, final Node otherNode,
			final BoxBound otherBound, final DetectionMethod myType,
			final DetectionMethod otherType) {
		switch (myType) {
		case INNER:
			switch (otherType) {
			case INNER: // pr�fen ob beide ineinander sind (geht nicht)
				return false;
			case OUTER: // Ich bin Kreis und in mir muss ein W�rfel sein
				return isCubeInSphere(myNode, this, otherNode, otherBound);
			}
			break;
		case OUTER:
			switch (otherType) {
			case INNER: // Ich bin Kreis und in einem W�rfel sein.
				return isSphereInCube(myNode, this, otherNode, otherBound);
			case OUTER: // Ich bin Kreis und darf einen W�rfel nicht ber�hren.
				return isSphereOutsideCube(myNode, this, otherNode, otherBound);
			}
			break;
		}
		throw new RuntimeException("Impossible comparison");
	}

	@Override
	public boolean isCollidingWith(final Node myNode,
			final DetectionMethod myType, final Node otherNode,
			final DetectionMethod otherType, final AbstractBound otherBound) {
		if (otherBound instanceof SphereBound) {
			return isCollidingWith(myNode, otherNode, (SphereBound) otherBound,
					myType, otherType);
		}
		if (otherBound instanceof BoxBound) {
			return isCollidingWith(myNode, otherNode, (BoxBound) otherBound,
					myType, otherType);
		}
		throw new RuntimeException("Bound of type <"
				+ otherBound.getClass().getName() + "> not supported!");
	}
}
