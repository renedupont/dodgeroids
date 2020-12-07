package de.games.engine.datamanagers;

import de.games.engine.AbstractGameActivity;
import de.games.engine.graphics.Background;
import de.games.engine.graphics.Camera;
import de.games.engine.graphics.Font.Text;
import de.games.engine.graphics.Light;
import de.games.engine.graphics.Sprite;
import de.games.engine.graphics.Vector;
import de.games.engine.levels.AbstractLevelFactory;
import de.games.engine.objects.AbstractGameObject;
import de.games.engine.objects.GameObjectChain;
import de.games.engine.objects.Player;
import de.games.engine.screens.AbstractScreenFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.microedition.khronos.opengles.GL11;

public class Scene {

    /** class elements * */
    private final LinkedList<AbstractGameObject> gObjects;

    private final HashMap<String, GameObjectChain<? extends AbstractGameObject>> gameObjectChains;
    private final HashMap<String, Text> texts;
    private final HashMap<String, Sprite> sprites;
    private final List<Light> lights;
    private final Background background;
    private final Camera camera;
    private Player player;

    // TODO combine constructors? or create different scenes?
    public Scene(
            final AbstractGameActivity activity,
            final GL11 gl,
            final AbstractScreenFactory screenFactory) { // currently intended for start and game over screen
        this.gObjects = new LinkedList<>();
        this.lights = new ArrayList<>();
        // load screen objects
        this.texts = screenFactory.createTexts(activity, gl);
        this.sprites = screenFactory.createSprites(activity, gl);
        this.background = screenFactory.createDefaultBackground(activity, gl);
        this.camera =
                screenFactory.createCamera(
                        activity.getViewportWidth(), activity.getViewportHeight());
        this.gameObjectChains =
                new HashMap<>();
    }

    public Scene(
            final AbstractGameActivity activity,
            final GL11 gl,
            final AbstractScreenFactory screenFactory,
            final AbstractLevelFactory levelFactory) {
        this.gObjects = new LinkedList<>();
        // load screen objects
        this.texts = screenFactory.createTexts(activity, gl);
        this.sprites = screenFactory.createSprites(activity, gl);
        this.background = screenFactory.createDefaultBackground(activity, gl);
        // load level objects
        levelFactory.loadResources(gl, activity);
        this.camera =
                screenFactory.createCamera(
                        activity.getViewportWidth(), activity.getViewportHeight());
        this.lights = levelFactory.createLights();
        this.player = levelFactory.createPlayer(new Vector(0f, 0f, -3.85f));
        addGameObject(player);
        this.gameObjectChains =
                levelFactory.createGameObjectChains(
                        this, new Vector(0.0f, 0.0f, camera.getzFar() - camera.getzNear()));
    }

    public Player getPlayer() {
        return player;
    }

    public Camera getCamera() {
        return camera;
    }

    public List<AbstractGameObject> getGameObjects() {
        return gObjects;
    }

    public List<Light> getLights() {
        return lights;
    }

    public Collection<Text> getTexts() {
        return texts.values();
    }

    public Text getText(final String label) {
        return texts.get(label);
    }

    public Collection<Sprite> getSprites() {
        return sprites.values();
    }

    public Sprite getSprite(final String label) {
        return sprites.get(label);
    }

    public void addGameObject(final AbstractGameObject o) {
        if (o.isTransparent()) {
            gObjects.addLast(o);
        } else {
            gObjects.addFirst(o);
        }
    }

    public boolean removeGameObject(final AbstractGameObject o) {
        return gObjects.remove(o);
    }

    public void update(final float delta) {
        for (AbstractGameObject o : gObjects) {
            o.update(delta);
        }
        camera.update(delta);
        for (Sprite sprite : sprites.values()) {
            sprite.update(delta);
        }
        for (GameObjectChain<? extends AbstractGameObject> chain : gameObjectChains.values()) {
            chain.update();
        }
    }

    public Background getBackground() {
        return background;
    }

    public HashMap<String, GameObjectChain<? extends AbstractGameObject>> getGameObjectChains() {
        return gameObjectChains;
    }

    public void dispose() {
        for (AbstractGameObject gObject : gObjects) {
            gObject.dispose();
        }
        for (Text text : texts.values()) {
            text.dispose();
        }
        for (Sprite sprite : sprites.values()) {
            sprite.dispose();
        }
        background.dispose();
    }
}
