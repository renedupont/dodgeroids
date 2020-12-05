package de.games.engine.objects;

import de.games.engine.datamanagers.Scene;
import de.games.engine.graphics.Vector;
import de.games.engine.levels.AbstractLevelFactory;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class GameObjectChain<T extends AbstractGameObject> {

    private final AbstractLevelFactory levelFactory;
    private final Scene scene;
    private final Class<?> genericType;
    private final float minRadius;
    private final List<T> gameObjects;
    private final float zPosOfFirstObject;
    private final float distanceToNextObject;
    private final Vector deathThresholdMin;
    private final Vector deathThresholdMax;
    // private float deathThresholdRadius;

    private int removedCounter;

    @SuppressWarnings("unchecked")
    // generic type erasure
    public GameObjectChain(
            final Class<? extends AbstractGameObject> genericType,
            final float minRadius,
            final Scene scene,
            final AbstractLevelFactory levelFactory,
            final int amountOfObjects,
            final float zPosOfFirstObject,
            final float distanceToNextObject,
            final Vector deathThresholdMin,
            final Vector deathThresholdMax,
            final float deathThresholdRadius) {
        this.genericType = genericType;
        this.minRadius = minRadius;
        this.scene = scene;
        this.levelFactory = levelFactory;
        this.gameObjects = new LinkedList<T>();
        this.zPosOfFirstObject = zPosOfFirstObject;
        this.distanceToNextObject = distanceToNextObject;
        this.deathThresholdMin = deathThresholdMin;
        this.deathThresholdMax = deathThresholdMax;
        // this.deathThresholdRadius = deathThresholdRadius;

        for (int i = 0; i < amountOfObjects; ++i) {
            addGameObjectToChain(
                    (T)
                            levelFactory.createGameObject(
                                    genericType, createStartPosition(), minRadius));
        }
    }

    public List<T> getGameObjects() {
        return gameObjects;
    }

    public void addGameObjectToChain(final T gameObject) {
        gameObjects.add(gameObject);
        scene.addGameObject(gameObject);
    }

    @SuppressWarnings("unchecked")
    // generic type erasure
    public int update(final float delta) {
        removedCounter = 0;
        // Iterator was used due to the need of removing objects while iterating
        Iterator<T> it = gameObjects.iterator();
        T gameObject;
        while (it.hasNext()) {
            gameObject = it.next();
            if (!gameObject.isWithinPositionThreshold(deathThresholdMin, deathThresholdMax)) {
                // &&
                // !gameObject.isWithinPositionThreshold(deathThresholdRadius))
                // { // TODO bei Bedarf freischalten
                scene.removeGameObject(gameObject);
                it.remove();
                removedCounter++;
            }
        }
        for (int i = 0; i < removedCounter; i++) {
            gameObject =
                    (T)
                            levelFactory.createGameObject(
                                    genericType, createStartPosition(), minRadius);
            addGameObjectToChain(gameObject);
        }
        return removedCounter;
    }

    public Vector createStartPosition() {
        // Diese Methode gibt die default start position an.
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

    public float getMinRadius() {
        return minRadius;
    }
}
