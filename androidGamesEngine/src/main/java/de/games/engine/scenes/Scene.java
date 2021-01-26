package de.games.engine.scenes;

import de.games.engine.graphics.Background;
import de.games.engine.graphics.Camera;
import de.games.engine.graphics.Font.Text;
import de.games.engine.graphics.Light;
import de.games.engine.graphics.Sprite;
import de.games.engine.objects.AbstractGameObject;
import de.games.engine.objects.GameObjectChain;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Scene {

    private HashMap<String, Text> texts;
    private HashMap<String, Sprite> sprites;
    private List<Light> lights;
    private Background background;
    private Camera camera;
    private LinkedList<AbstractGameObject> gameObjects;
    private Map<String, GameObjectChain<? extends AbstractGameObject>> gameObjectChains;

    /** Constructor for a scene without any ingame objects, use-case could be a menu. * */
    public Scene(
            HashMap<String, Text> texts,
            HashMap<String, Sprite> sprites,
            Background background,
            Camera camera) {
        this.texts = texts;
        this.sprites = sprites;
        this.background = background;
        this.camera = camera;
        this.lights = new LinkedList<>();
        this.gameObjects = new LinkedList<>();
        this.gameObjectChains = new HashMap<>();
    }

    /** Constructor for a scene with ingame objects, use-case could be the actual game loop. * */
    public Scene(
            HashMap<String, Text> texts,
            HashMap<String, Sprite> sprites,
            Background background,
            List<Light> lights,
            Camera camera,
            LinkedList<AbstractGameObject> gameObjects,
            HashMap<String, GameObjectChain<? extends AbstractGameObject>> gameObjectChains) {
        this.texts = texts;
        this.sprites = sprites;
        this.background = background;
        this.lights = lights;
        this.camera = camera;
        this.gameObjects = gameObjects;
        this.gameObjectChains = gameObjectChains;
        gameObjectChains
                .values()
                .forEach(
                        chain ->
                                chain.getGameObjects()
                                        .forEach(gameObject -> addGameObject(gameObject)));
    }

    public void update(float delta) {
        camera.update(delta);
        for (Sprite sprite : sprites.values()) {
            sprite.update(delta);
        }
        for (AbstractGameObject o : gameObjects) {
            o.update(delta);
        }
        for (GameObjectChain<? extends AbstractGameObject> chain : gameObjectChains.values()) {
            chain.update(gameObjects);
        }
    }

    public void dispose() {
        for (Text text : texts.values()) {
            text.dispose();
        }
        for (Sprite sprite : sprites.values()) {
            sprite.dispose();
        }
        background.dispose();
        for (AbstractGameObject o : gameObjects) {
            o.dispose();
        }
    }

    public void addGameObject(AbstractGameObject o) {
        if (o.isTransparent()) {
            gameObjects.addLast(o);
        } else {
            gameObjects.addFirst(o);
        }
    }

    public boolean removeGameObject(AbstractGameObject o) {
        return gameObjects.remove(o);
    }

    public Camera getCamera() {
        return camera;
    }

    public Collection<Text> getTexts() {
        return texts.values();
    }

    public Text getText(String label) {
        return texts.get(label);
    }

    public Collection<Sprite> getSprites() {
        return sprites.values();
    }

    public Sprite getSprite(String label) {
        return sprites.get(label);
    }

    public Background getBackground() {
        return background;
    }

    public List<Light> getLights() {
        return lights;
    }

    public List<AbstractGameObject> getGameObjects() {
        return gameObjects;
    }

    public Map<String, GameObjectChain<? extends AbstractGameObject>> getGameObjectChains() {
        return gameObjectChains;
    }
}
