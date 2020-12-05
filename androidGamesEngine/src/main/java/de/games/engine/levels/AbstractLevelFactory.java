package de.games.engine.levels;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.box.format.BoxFormat.Identifier;
import com.box.format.chunk.BoxMeshBound;
import com.box.format.chunk.BoxMeshDodgeIt;
import com.box.primitive.BoxBlock;
import com.box.primitive.BoxFile;
import com.box.primitive.BoxFileReader;
import de.games.engine.datamanagers.Scene;
import de.games.engine.graphics.BoxBound;
import de.games.engine.graphics.Light;
import de.games.engine.graphics.Mesh;
import de.games.engine.graphics.SphereBound;
import de.games.engine.graphics.Texture;
import de.games.engine.graphics.Vector;
import de.games.engine.objects.AbstractGameObject;
import de.games.engine.objects.GameObjectChain;
import de.games.engine.objects.Player;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import javax.microedition.khronos.opengles.GL11;

public abstract class AbstractLevelFactory {

    private BoxFile data;
    private BoxFileReader fileReader;
    protected final HashMap<String, Mesh> meshes;
    protected final HashMap<String, Texture> textures;
    private final HashMap<String, Bitmap> bitmapCache;

    public AbstractLevelFactory() {
        this.meshes = new HashMap<String, Mesh>();
        this.textures = new HashMap<String, Texture>();
        bitmapCache = new HashMap<String, Bitmap>();
        // loadResources();
    }

    protected abstract int getBoxId();

    public abstract List<String> createMeshIdList();

    public abstract List<String> createTextureIdList();

    public abstract List<Light> createLights();

    public abstract <T extends AbstractGameObject> T createGameObject(
            Class<?> type, Vector startPosition, float minRadius);

    public abstract Player createPlayer(Vector startPosition);

    protected abstract AbstractGameObject createAsteroid(Vector startPosition, float minRadius);

    protected abstract AbstractGameObject createTunnelPart(Vector startPosition);

    protected abstract AbstractGameObject createTunnelRing(Vector startPosition);

    public abstract <T extends AbstractGameObject> GameObjectChain<T> createGameObjectChain(
            Class<T> type, Scene scene, Vector range, float minRadius);

    public abstract HashMap<String, GameObjectChain<? extends AbstractGameObject>>
            createGameObjectChains(Scene scene, Vector range);

    @SuppressWarnings("unchecked")
    // generic type erasure
    public <T extends AbstractGameObject>
            GameObjectChain<AbstractGameObject> createAbstractGameObjectChain(
                    final Class<T> type,
                    final Scene scene,
                    final Vector range,
                    final float minRadius) {
        return (GameObjectChain<AbstractGameObject>)
                createGameObjectChain(type, scene, range, minRadius);
    }

    // private float durationInSeconds;

    protected void setRandomXY(
            final Vector startPosition,
            final float objectRadius,
            final float minRadius,
            final float maxRadius) {
        double theta = Math.random() * 360;
        float M = minRadius + ((float) Math.random() * (maxRadius - objectRadius));
        startPosition.x = (float) (M * Math.cos(theta));
        startPosition.y = (float) (M * Math.sin(theta));
    }

    protected void setRandomY(
            final Vector startPosition, final float objectRadius, final float maxRadius) {
        startPosition.y =
                (float)
                        (-maxRadius
                                + objectRadius
                                + (Math.random() * 2 * (maxRadius - objectRadius)));
    }

    private void loadMesh(final GL11 gl, final String id) {
        // if (!meshes.containsKey(id)) {
        BoxBlock block = data.getBlock(data.findBySignature(id));
        if (block != null && block.getHeader().getId() == Identifier.MESHDODGEIT) {
            try {
                fileReader.readChunk(block);

                BoxMeshDodgeIt chunk = (BoxMeshDodgeIt) block.getChunk();
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
                    BoxMeshDodgeIt boundMesh = bound.getMesh();
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

                    switch (bound.getShape()) { // TODO ist das folgende final
                            // so?
                        case Circle:
                            SphereBound bnd = new SphereBound(bound.getRadius(), centroid, theMesh);
                            mesh.addBound(bnd);
                            break;
                        case Cube:
                            BoxBound bnd2 =
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
                                            theMesh);
                            mesh.addBound(bnd2);
                            break;
                    }
                }
                meshes.put(id, mesh);
            } catch (IOException e) {
                // Log.d("Dodge It!", "Scene: Could not load mesh <" + id + ">");
            }
        }
        // }
    }

    private void loadTexture(final GL11 gl, final Activity activity, final String id) {
        // if (id != null && !textures.containsKey(id)) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(activity.getAssets().open(id));
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
            // //Log.d("Dodge It!", "Scene: Loading texture "+id);
        } catch (IOException e) {
            // Log.d("Dodge It!", "Unable to load bitmap <" + id + ">");
        }
        // }
    }

    public void loadResources(final GL11 gl, final Activity activity) {
        Resources resources = activity.getResources();
        File cacheFile = new File(activity.getCacheDir(), "tmp.box");

        // if (!cacheFile.exists()) {
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
        // }
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
            loadTexture(gl, activity, textureId);
        }

        try {
            fileReader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
