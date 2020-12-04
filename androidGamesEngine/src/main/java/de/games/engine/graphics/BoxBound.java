package de.games.engine.graphics;

public class BoxBound extends AbstractBound {
	private final Vector min; // nicht verwenden, nur getMin();
	private final Vector max; // nicht verwenden, nur getMax();

	public BoxBound(final Vector min, final Vector max, final Vector offset,
			final Mesh mesh) {
		super(offset, mesh);
		this.min = min;
		this.max = max;
	}

	public Vector getMin(final Node node) {
		return new Vector(min.x * node.getScale().x, min.y * node.getScale().y,
				min.z * node.getScale().z);
	}

	public Vector getMax(final Node node) {
		return new Vector(max.x * node.getScale().x, max.y * node.getScale().y,
				max.z * node.getScale().z);
	}

	public boolean isCollidingWith(final Node myNode, final Node otherNode,
			final BoxBound otherBound, final DetectionMethod myType,
			final DetectionMethod otherType) {
		switch (myType) {
		case INNER:
			switch (otherType) {
			case INNER: // pruefen ob beide ineinander sind (geht nicht)
				return false;
			case OUTER: // pruefen ob otherNode in myNode drin ist
				Vector myMin = Vector.add(getMin(myNode),
						Vector.add(myNode.getPos(), getOffset()));
				Vector myMax = Vector.add(getMax(myNode),
						Vector.add(myNode.getPos(), getOffset()));
				Vector otherMin = Vector.add(otherBound.getMin(otherNode),
						Vector.add(otherNode.getPos(), otherBound.getOffset()));
				Vector otherMax = Vector.add(otherBound.getMax(otherNode),
						Vector.add(otherNode.getPos(), otherBound.getOffset()));

				return (otherMin.x > myMax.x || otherMax.x < myMin.x
						|| otherMin.y > myMax.y || otherMax.y < myMin.y
						|| otherMin.z > myMax.z || otherMax.z < myMin.z);
			}
			break;
		case OUTER:
			switch (otherType) {
			case INNER: // pruefen ob myNode in otherNode ist
				Vector myMin = Vector.add(getMin(myNode),
						Vector.add(myNode.getPos(), getOffset()));
				Vector myMax = Vector.add(getMax(myNode),
						Vector.add(myNode.getPos(), getOffset()));
				Vector otherMin = Vector.add(otherBound.getMin(otherNode),
						Vector.add(otherNode.getPos(), otherBound.getOffset()));
				Vector otherMax = Vector.add(otherBound.getMax(otherNode),
						Vector.add(otherNode.getPos(), otherBound.getOffset()));

				return (myMin.x > otherMax.x || myMax.x < otherMin.x
						|| myMin.y > otherMax.y || myMax.y < otherMin.y
						|| myMin.z > otherMax.z || myMax.z < otherMin.z);

			case OUTER: // pruefen ob sie weg von einander sind
				Vector myHalfWidths = Vector.mul(
						Vector.add(getMin(myNode), getMax(myNode)), 0.5f);
				Vector otherHalfWidths = Vector.mul(
						Vector.add(otherBound.getMin(otherNode),
								otherBound.getMax(otherNode)), 0.5f);

				Vector myPos = Vector.add(myNode.getPos(), getOffset());
				Vector otherPos = Vector.add(otherNode.getPos(),
						otherBound.getOffset());

				if (Math.abs(myPos.x - otherPos.x) <= myHalfWidths.x
						+ otherHalfWidths.x) {
					if (Math.abs(myPos.y - otherPos.y) <= myHalfWidths.y
							+ otherHalfWidths.y) {
						if (Math.abs(myPos.z - otherPos.z) <= myHalfWidths.z
								+ otherHalfWidths.z) {
							return true;
						}
					}
				}
				return false;
			}
			break;
		}
		throw new RuntimeException("Impossible comparison");
	}

	public boolean isCollidingWith(final Node myNode, final Node otherNode,
			final SphereBound otherBound, final DetectionMethod myType,
			final DetectionMethod otherType) {
		switch (myType) {
		case INNER:
			switch (otherType) {
			case INNER: // pruefen ob beide ineinander sind (geht nicht)
				return false;
			case OUTER: // Ich bin Kreis und in mir muss ein Wuerfel sein
				return this.isCubeInSphere(otherNode, otherBound, myNode, this);
			}
			break;
		case OUTER:
			switch (otherType) {
			case INNER: // Ich bin Kreis und muss in einem Wuerfel sein.
				return this.isSphereInCube(otherNode, otherBound, myNode, this);
			case OUTER: // Ich bin Kreis und darf einen Wuerfel nicht beruehren.
				return this.isSphereOutsideCube(otherNode, otherBound, myNode,
						this);
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
