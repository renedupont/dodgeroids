package de.games.engine.levels;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import de.games.engine.box.format.BoxFormat.Identifier;
import de.games.engine.box.format.chunk.BoxMeshBound;
import de.games.engine.box.format.chunk.BoxMeshDodgeroids;
import de.games.engine.box.primitive.BoxBlock;
import de.games.engine.box.primitive.BoxFile;
import de.games.engine.box.primitive.BoxFileReader;
import de.games.engine.graphics.BoxBound;
import de.games.engine.graphics.Light;
import de.games.engine.graphics.Mesh;
import de.games.engine.graphics.SphereBound;
import de.games.engine.graphics.Texture;
import de.games.engine.graphics.Vector;
import de.games.engine.objects.AbstractGameObject;
import de.games.engine.objects.GameObjectChain;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import javax.microedition.khronos.opengles.GL11;

public abstract class AbstractLevelFactory {

    protected BoxFile data;
    protected BoxFileReader fileReader;
    protected HashMap<String, Mesh> meshes;
    protected HashMap<String, Texture> textures;
    protected HashMap<String, Bitmap> bitmapCache;

    public AbstractLevelFactory() {
        this.meshes = new HashMap<>();
        this.textures = new HashMap<>();
        this.bitmapCache = new HashMap<>();
    }

    protected abstract int getBoxId();

    public abstract float getMaxHeight();

    public abstract List<String> createMeshIdList();

    public abstract List<String> createTextureIdList();

    public abstract List<Light> createLights();

    public abstract HashMap<String, GameObjectChain<? extends AbstractGameObject>>
            createGameObjectChains(Vector range);

    protected void setRandomY(
            final Vector startPosition, final float objectRadius, final float maxRadius) {
        startPosition.y =
                (float)
                        (-maxRadius
                                + objectRadius
                                + (Math.random() * 2 * (maxRadius - objectRadius)));
    }

    private void loadMesh(final GL11 gl, final String id) {
        BoxBlock block = data.getBlock(data.findBySignature(id));
        if (block != null && block.getHeader().getId() == Identifier.MESH_DODGEROIDS) {
            try {
                fileReader.readChunk(block);

                BoxMeshDodgeroids chunk = (BoxMeshDodgeroids) block.getChunk();
                Mesh mesh = new Mesh(gl, chunk.getFaces().length, false, true, true);

                for (int i = 0; i < chunk.getFaces().length / 3; i++) {
                    mesh.normal(
                            chunk.getNormals()[i * 3],
                            chunk.getNormals()[i * 3 + 1],
                            chunk.getNormals()[i * 3 + 2]);
                    mesh.texCoord(chunk.getUvs()[i * 2], chunk.getUvs()[i * 2 + 1]);
                    mesh.vertex(
                            chunk.getFaces()[i * 3],
                            chunk.getFaces()[i * 3 + 1],
                            chunk.getFaces()[i * 3 + 2]);
                }

                for (BoxMeshBound bound : chunk.getBounds()) {
                    BoxMeshDodgeroids boundMesh = bound.getMesh();
                    Mesh theMesh = new Mesh(gl, boundMesh.getFaces().length, false, true, true);
                    for (int i = 0; i < boundMesh.getFaces().length / 3; i++) {
                        theMesh.normal(
                                boundMesh.getNormals()[i * 3],
                                boundMesh.getNormals()[i * 3 + 1],
                                boundMesh.getNormals()[i * 3 + 2]);
                        theMesh.texCoord(boundMesh.getUvs()[i * 2], boundMesh.getUvs()[i * 2 + 1]);
                        theMesh.vertex(
                                boundMesh.getFaces()[i * 3],
                                boundMesh.getFaces()[i * 3 + 1],
                                boundMesh.getFaces()[i * 3 + 2]);
                    }

                    Vector centroid = new Vector();
                    float[] verts = boundMesh.getFaces();
                    for (int i = 0; i < verts.length; i += 3) {
                        centroid.add(new Vector(verts[i], verts[i + 1], verts[i + 2]));
                    }
                    centroid.x /= verts.length;
                    centroid.y /= verts.length;
                    centroid.z /= verts.length;

                    switch (bound.getShape()) {
                        case Circle:
                            mesh.addBound(new SphereBound(bound.getRadius(), centroid, theMesh));
                            break;
                        case Cube:
                            mesh.addBound(
                                    new BoxBound(
                                            new Vector(
                                                    bound.getMinValues()[0],
                                                    bound.getMinValues()[1],
                                                    bound.getMinValues()[2]),
                                            new Vector(
                                                    bound.getMaxValues()[0],
                                                    bound.getMaxValues()[1],
                                                    bound.getMaxValues()[2]),
                                            centroid,
                                            theMesh));
                            break;
                        default:
                            break;
                    }
                }
                meshes.put(id, mesh);
            } catch (IOException e) {
                // TODO proper exeption handling...
                // Log.d("Dodge It!", "Scene: Could not load mesh <" + id + ">");
            }
        }
    }

    private void loadTexture(final GL11 gl, final AssetManager assetManager, final String id) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(assetManager.open(id));
            Texture tex =
                    new Texture(
                            gl,
                            bitmap,
                            Texture.TextureFilter.MipMap,
                            Texture.TextureFilter.Linear,
                            Texture.TextureWrap.ClampToEdge,
                            Texture.TextureWrap.ClampToEdge);
            textures.put(id, tex);
            bitmapCache.put(id, bitmap);
            bitmap.recycle();
        } catch (IOException e) {
            // TODO proper exeption handling...
            // Log.d("Dodge It!", "Unable to load bitmap <" + id + ">");
        }
    }

    public void loadResources(
            GL11 gl, Resources resources, File cacheDir, AssetManager assetManager) {
        File cacheFile = new File(cacheDir, "tmp.box");

        try {
            InputStream is = resources.openRawResource(getBoxId());
            FileOutputStream os = new FileOutputStream(cacheFile);
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.close();
            is.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            fileReader = new BoxFileReader(cacheFile);
            data = fileReader.read();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (String meshId : createMeshIdList()) {
            loadMesh(gl, meshId);
        }
        for (String textureId : createTextureIdList()) {
            loadTexture(gl, assetManager, textureId);
        }

        try {
            fileReader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
