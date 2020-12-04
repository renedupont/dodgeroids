package de.games.engine.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * A simple Mesh class that wraps OpenGL ES Vertex Arrays. Just instantiate it
 * with the proper parameters then fill it with the color, texCoord, normal and
 * vertex method.
 * 
 * @author mzechner
 * 
 */
public final class Mesh {

	public enum RenderType {
		POINTS(GL11.GL_POINTS), LINES(GL11.GL_LINES), LINE_LOOP(
				GL11.GL_LINE_LOOP), LINE_STRIP(GL11.GL_LINE_STRIP), TRIANGLES(
				GL11.GL_TRIANGLES), TRIANGLE_STRIP(GL11.GL_TRIANGLE_STRIP), TRIANGLE_FAN(
				GL11.GL_TRIANGLE_FAN);

		private final int value;

		private RenderType(final int $value) {
			this.value = $value;
		}

		public int getValue() {
			return value;
		}
	}

	/** The gl instance **/
	private final GL11 gl;

	/** vertex position buffer and array **/
	private float vertices[];
	private int vertexHandle = -1;
	private FloatBuffer vertexBuffer;

	/** color buffer and array **/
	private float colors[];
	private int colorHandle = -1;
	private FloatBuffer colorBuffer;

	/** texture coordinate buffer and array **/
	private float texCoords[];
	private int texHandle = -1;
	private FloatBuffer texCoordBuffer;

	/** normal buffer and array **/
	private float normals[];
	private int normalHandle = -1;
	private FloatBuffer normalBuffer;

	/** vertex index at which the next vertex gets inserted **/
	private int index = 0;

	/** number of vertices defined for the mesh **/
	private int numVertices = 0;

	/** is the mesh dirty? **/
	private boolean dirty = true;

	/** last mesh **/
	private static Mesh lastMesh;

	/** renderer supports vbos **/
	public static boolean globalVBO = true;

	/** mesh count **/
	public static int meshes = 0;

	private final ArrayList<AbstractBound> bounds = new ArrayList<AbstractBound>();

	// private Vector min;
	// private Vector max;
	// private float maxRadius = 0; // in regard of max and min values...

	public Mesh(final GL11 gl, final int numVertices, final boolean hasColors,
			final boolean hasTextureCoordinates, final boolean hasNormals) {
		this.gl = gl;
		vertices = new float[numVertices * 3];
		// min = new Vector();
		// max = new Vector();
		int[] buffer = new int[1];

		if (!globalVBO) {
			vertexBuffer = allocateBuffer(numVertices * 3);
		} else {
			gl.glGenBuffers(1, buffer, 0);
			vertexHandle = buffer[0];
			vertexBuffer = FloatBuffer.wrap(vertices);
		}

		if (hasColors) {
			colors = new float[numVertices * 4];
			if (!globalVBO) {
				colorBuffer = allocateBuffer(numVertices * 4);
			} else {
				gl.glGenBuffers(1, buffer, 0);
				colorHandle = buffer[0];
				colorBuffer = FloatBuffer.wrap(colors);
			}
		}

		if (hasTextureCoordinates) {
			texCoords = new float[numVertices * 2];
			if (!globalVBO) {
				texCoordBuffer = allocateBuffer(numVertices * 2);
			} else {
				gl.glGenBuffers(1, buffer, 0);
				texHandle = buffer[0];
				texCoordBuffer = FloatBuffer.wrap(texCoords);
			}
		}

		if (hasNormals) {
			normals = new float[numVertices * 3];
			if (!globalVBO) {
				normalBuffer = allocateBuffer(numVertices * 3);
			} else {
				gl.glGenBuffers(1, buffer, 0);
				normalHandle = buffer[0];
				normalBuffer = FloatBuffer.wrap(normals);
			}
		}
	}

	// public ArrayList<AbstractBound> getBounds(CollisionShape shape) {
	// ArrayList<Bound> r = new ArrayList<Bound>();
	// for (AbstractBound bound:bounds) {
	// if (bound.getShape() == shape) {
	// r.add(bound);
	// }
	// }
	// return r;
	// }

	// public Bound getBound(String name) {
	// for (Bound bound:bounds) {
	// if (bound.getName().equals(name)) {
	// return bound;
	// }
	// }
	// return null;
	// }

	public ArrayList<AbstractBound> getBounds() {
		return bounds;
	}

	public boolean addBound(final AbstractBound bnd) {
		return bounds.add(bnd);
	}

	// public void setMinMaxVerticeValues(float min[], float max[]) { //float
	// minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
	// this.min.set(min[0], min[1], max[2]);
	// this.max.set(max[0], max[1], max[2]);
	// }

	public static float calculateMaxRadius(final Vector min, final Vector max) {
		return calculateMaxRadius(min, max, false);
	}

	public static float calculateMaxRadius(final Vector min, final Vector max,
			final boolean onlyXY) {
		// considers only x and y axis regarding the calculation if onlyXY is
		// set to true
		float maxRadius = 0f;
		// TODO: Exceptions schmei�en
		if (min != null && max != null && min.x < Float.POSITIVE_INFINITY
				&& min.y < Float.POSITIVE_INFINITY
				&& min.z < Float.POSITIVE_INFINITY
				&& max.x > Float.NEGATIVE_INFINITY
				&& max.y > Float.NEGATIVE_INFINITY
				&& max.z > Float.NEGATIVE_INFINITY) {
			float distX = max.x - min.x;
			float distY = max.y - min.y;
			float distZ = max.z - min.z;
			maxRadius = maxRadius < distX ? distX : maxRadius;
			maxRadius = maxRadius < distY ? distY : maxRadius;
			if (!onlyXY) {
				maxRadius = maxRadius < distZ ? distZ : maxRadius;
			}
			maxRadius /= 2; // setzt voraus das min und max jeweils gleiche
							// distanz zum nullpunkt haben...
		}
		return maxRadius;
	}

	// public void setMaxRadius(float maxRadius) {
	// this.maxRadius = maxRadius;
	// }
	//
	// public float getMaxRadius() {
	// return maxRadius;
	// }
	//
	// public void setMin(Vector min) {
	// this.min = min;
	// }
	//
	// public void setMax(Vector max) {
	// this.max = max;
	// }
	//
	// public Vector getMin() {
	// return min;
	// }
	//
	// public Vector getMax() {
	// return max;
	// }

	/**
	 * Allocates a direct FloatBuffer of the given size. Sets order to native
	 * 
	 * @param size
	 *            The size in number of floats
	 * @return The FloatBuffer
	 */
	private FloatBuffer allocateBuffer(final int size) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(size * 4);
		buffer.order(ByteOrder.nativeOrder());
		return buffer.asFloatBuffer();
	}

	/**
	 * updates the direct buffers in case the user
	 */
	private void update() {
		if (!globalVBO) {
			vertexBuffer.put(vertices);
			vertexBuffer.position(0);

			if (colors != null) {
				colorBuffer.put(colors);
				colorBuffer.position(0);
			}

			if (texCoords != null) {
				texCoordBuffer.put(texCoords);
				texCoordBuffer.position(0);
			}

			if (normals != null) {
				normalBuffer.put(normals);
				normalBuffer.position(0);
			}
		} else {
			GL11 gl = this.gl;

			gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertexHandle);
			gl.glBufferData(GL11.GL_ARRAY_BUFFER, vertices.length * 4,
					vertexBuffer, GL11.GL_DYNAMIC_DRAW);

			if (colors != null) {
				gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, colorHandle);
				gl.glBufferData(GL11.GL_ARRAY_BUFFER, colors.length * 4,
						colorBuffer, GL11.GL_DYNAMIC_DRAW);
			}

			if (normals != null) {
				gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, normalHandle);
				gl.glBufferData(GL11.GL_ARRAY_BUFFER, normals.length * 4,
						normalBuffer, GL11.GL_DYNAMIC_DRAW);
			}

			if (texCoords != null) {
				gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, texHandle);
				gl.glBufferData(GL11.GL_ARRAY_BUFFER, texCoords.length * 4,
						texCoordBuffer, GL11.GL_DYNAMIC_DRAW);
			}

			gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
		}

		numVertices = index;
		index = 0;
		dirty = false;
	}

	/**
	 * Renders the mesh as the given type, starting at offset using numVertices
	 * vertices.
	 * 
	 * @param type
	 *            the type
	 * @param offset
	 *            the offset, in number of vertices
	 * @param numVertices
	 *            the number of vertices to use
	 */
	public void render(final RenderType type, final int offset,
			final int numVertices) {
		boolean wasDirty = dirty;
		if (dirty) {
			update();
		}

		if (this == lastMesh && !wasDirty) {
			gl.glDrawArrays(type.getValue(), offset, numVertices);
			return;
		} else {
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		}

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		if (globalVBO) {
			gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertexHandle);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);
		} else {
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		}

		if (colors != null) {
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			if (globalVBO) {
				gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, colorHandle);
				gl.glColorPointer(4, GL10.GL_FLOAT, 0, 0);
			} else {
				gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
			}
		}

		if (texCoords != null) {
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			if (globalVBO) {
				gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, texHandle);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0);
			} else {
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer);
			}
		}

		if (normals != null) {
			gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
			if (globalVBO) {
				gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, normalHandle);
				gl.glNormalPointer(GL10.GL_FLOAT, 0, 0);
			} else {
				gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
			}
		}

		gl.glDrawArrays(type.getValue(), offset, numVertices);
		lastMesh = this;
	}

	/**
	 * Renders the mesh as the given type using as many vertices as have been
	 * defined by calling vertex().
	 * 
	 * @param type
	 *            the type
	 */
	public void render(final RenderType type) {
		render(type, 0, numVertices);
	}

	/**
	 * Defines the position of the current vertex. Before you call this you have
	 * to call any other method like color, normal and texCoord for the current
	 * vertex!
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param z
	 *            the z coordinate
	 */
	public void vertex(final float x, final float y, final float z) {
		dirty = true;
		int offset = index * 3;
		vertices[offset] = x;
		vertices[offset + 1] = y;
		vertices[offset + 2] = z;
		index++;
	}

	public void vertex(final Vector p) {
		vertex(p.x, p.y, p.z);
	}

	public void color(final Color c) {
		color(c.r, c.g, c.b, c.a);
	}

	public void color(final float r, final float g, final float b, final float a) {
		dirty = true;
		int offset = index * 4;
		colors[offset] = r;
		colors[offset + 1] = g;
		colors[offset + 2] = b;
		colors[offset + 3] = a;
	}

	/**
	 * Sets the normal of the current vertex
	 * 
	 * @param x
	 *            the x component
	 * @param y
	 *            the y component
	 * @param z
	 *            the z component
	 */
	public void normal(final float x, final float y, final float z) {
		dirty = true;
		int offset = index * 3;
		normals[offset] = x;
		normals[offset + 1] = y;
		normals[offset + 2] = z;
	}

	/**
	 * Sets the texture coordinates of the current vertex
	 * 
	 * @param s
	 *            the s coordinate
	 * @param t
	 *            the t coordinate
	 */
	public void texCoord(final float s, final float t) {
		dirty = true;
		int offset = index * 2;
		texCoords[offset] = s;
		texCoords[offset + 1] = t;
	}

	public int getMaximumVertices() {
		return vertices.length / 3;
	}

	public void reset() {
		dirty = true;
		index = 0;
	}

	public void dispose() {
		if (globalVBO) {
			GL11 gl = this.gl;
			if (vertexHandle != -1) {
				gl.glDeleteBuffers(1, new int[] { vertexHandle }, 0);
			}
			if (colorHandle != -1) {
				gl.glDeleteBuffers(1, new int[] { colorHandle }, 0);
			}
			if (normalHandle != -1) {
				gl.glDeleteBuffers(1, new int[] { normalHandle }, 0);
			}
			if (texHandle != -1) {
				gl.glDeleteBuffers(1, new int[] { texHandle }, 0);
			}
		}

		vertices = null;
		vertexBuffer = null;
		colors = null;
		colorBuffer = null;
		normals = null;
		normalBuffer = null;
		texCoords = null;
		texCoordBuffer = null;
		meshes--;
	}

}
