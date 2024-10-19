package de.games.engine.objects;

import de.games.engine.graphics.Vector;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

// TODO: make iterable or collection (and rename) and get rid of getGameObjects() + getFirst()
public class GameObjectChain<T extends AbstractGameObject> {

    private final Function<Vector, T> gameObjectCreator;
    private final float zPosOfFirstObject;
    private final float distanceToNextObject;
    private final Vector deathThresholdMin;
    private final Vector deathThresholdMax;
    private final List<T> gameObjects;

    public GameObjectChain(
            Function<Vector, T> gameObjectCreator,
            int gameObjectsCount,
            float zPosOfFirstObject,
            float distanceToNextObject,
            Vector deathThresholdMin,
            Vector deathThresholdMax) {
        this.gameObjectCreator = gameObjectCreator;
        this.zPosOfFirstObject = zPosOfFirstObject;
        this.distanceToNextObject = distanceToNextObject;
        this.deathThresholdMin = deathThresholdMin;
        this.deathThresholdMax = deathThresholdMax;

        this.gameObjects = new LinkedList<>();

        for (int i = 0; i < gameObjectsCount; i++) {
            addGameObjectToChain(gameObjectCreator.apply(determineStartPosition()));
        }
    }

    public void addGameObjectToChain(T gameObject) {
        gameObjects.add(gameObject);
    }

    public void update(LinkedList<AbstractGameObject> allObjects) {
        int removedCounter = 0;
        // Iterator was used due to the need of removing objects while iterating
        // TODO: improve this...
        Iterator<T> it = gameObjects.iterator();
        T gameObject;
        while (it.hasNext()) {
            gameObject = it.next();
            if (!gameObject.isWithinPositionThreshold(deathThresholdMin, deathThresholdMax)) {
                it.remove();
                allObjects.remove(gameObject);
                removedCounter++;
            }
        }
        for (int i = 0; i < removedCounter; i++) {
            T newObject = gameObjectCreator.apply(determineStartPosition());
            addGameObjectToChain(newObject);
            allObjects.add(newObject);
        }
    }

    // calculates the start position of a next game object in the chain
    public Vector determineStartPosition() {
        return new Vector(
                0.0f,
                0.0f,
                (gameObjects.isEmpty()
                        ? zPosOfFirstObject
                        : (gameObjects.get(gameObjects.size() - 1).getPosition().z
                                - distanceToNextObject)));
    }

    public T getFirst() {
        return gameObjects.get(0);
    }

    public List<T> getGameObjects() {
        return gameObjects;
    }
}
