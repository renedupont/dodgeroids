package de.games.dodgeroids.levels;

import de.games.dodgeroids.R;
import de.games.dodgeroids.objects.Asteroid;
import de.games.dodgeroids.objects.Player;
import de.games.engine.graphics.Color;
import de.games.engine.graphics.Light;
import de.games.engine.graphics.Light.Id;
import de.games.engine.graphics.Light.Type;
import de.games.engine.graphics.Mesh;
import de.games.engine.graphics.RotationSettings;
import de.games.engine.graphics.SphereBound;
import de.games.engine.graphics.Vector;
import de.games.engine.levels.AbstractLevelFactory;
import de.games.engine.objects.AbstractGameObject;
import de.games.engine.objects.GameObjectChain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class SpaceLevelFactory extends AbstractLevelFactory {

    @Override
    protected int getBoxId() {
        //		return R.raw.data;
        return R.raw.data_new;
    }

    public float getMaxHeight() {
        return 8f;
    }

    @Override
    public List<String> createMeshIdList() {
        List<String> meshIds = new ArrayList<>();
        meshIds.add("ufo_body");
        meshIds.add("ufo_ring");
        meshIds.add("asteroid_1");
        meshIds.add("asteroid_2");
        meshIds.add("asteroid_3");
        meshIds.add("asteroid_4");
        meshIds.add("TerrainBox");
        return meshIds;
    }

    @Override
    public List<String> createTextureIdList() {
        List<String> textureIds = new ArrayList<>();
        textureIds.add("ufo_diffuse.jpg");
        textureIds.add("asteroid.jpg");
        return textureIds;
    }

    @Override
    public List<Light> createLights() {
        List<Light> lights = new ArrayList<>();

        Light light1 = new Light(Type.POINT, Id.ONE);
        light1.setAmbient(Color.WHITE);
        light1.setDiffuse(new Color(0.7f, 0.7f, 0.7f, 1.0f));
        light1.setSpecular(Color.WHITE);
        light1.setPosition(new Vector(0.0f, 0.0f, 0.2f));
        lights.add(light1);

        Light light2 = new Light(Type.DIRECTIONAL, Id.THREE);
        light2.setAmbient(new Color(0.3f, 0.3f, 0.3f, 0.1f));
        light2.setDiffuse(new Color(0.5f, 0.5f, 0.5f, 0.1f));
        light2.setSpecular(Color.CLEAR);
        light2.setPosition(new Vector(0.0f, 0.0f, 1.0f));
        light2.setDirection(new Vector(0.0f, 0.0f, 0.0f));
        lights.add(light2);

        return lights;
    }

    public Player createPlayer(final Vector startPosition) {
        HashMap<Mesh, RotationSettings> playerMeshes = new HashMap<>();
        playerMeshes.put(
                meshes.get("ufo_body"),
                new RotationSettings(new Vector(0.0f, 0.0f, 0.0f), new Vector(0.0f, 0.0f, 0.0f)));
        playerMeshes.put(
                meshes.get("ufo_ring"),
                new RotationSettings(new Vector(0.0f, 0.0f, 0.0f), new Vector(0.0f, 25.0f, 0.0f)));
        Vector velocity = new Vector(25.0f, 25.0f, 0.0f);
        return new Player(
                playerMeshes,
                textures.get("ufo_diffuse.jpg"),
                velocity,
                startPosition,
                1.3f,
                3,
                3.0f);
    }

    public Function<Vector, Asteroid> createAsteroid() {
        return (startPosition) -> {
            HashMap<Mesh, RotationSettings> asteroidMeshes = new HashMap<>();
            int asteroidType = new Random().nextInt(4); // choose randomly one of the four meshes
            asteroidMeshes.put(
                    meshes.get("asteroid_" + (asteroidType + 1)),
                    new RotationSettings(
                            new Vector(0.0f, 0.0f, 0.0f),
                            new Vector(
                                    (0.5f - (float) Math.random()) * 80.0f,
                                    (0.5f - (float) Math.random()) * 80.0f,
                                    (0.5f - (float) Math.random()) * 80.0f)));
            Vector velocity = new Vector(0.0f, 0.0f, 24.0f);
            float scaleFactor = 0.8f;
            float asteroidRadius =
                    ((SphereBound) meshes.get("asteroid_" + (asteroidType + 1)).getBounds().get(0))
                                    .getRadius()
                            * scaleFactor;
            setRandomY(startPosition, asteroidRadius, getMaxHeight());
            return new Asteroid(
                    asteroidMeshes,
                    textures.get("asteroid.jpg"),
                    scaleFactor,
                    velocity,
                    startPosition);
        };
    }

    public GameObjectChain<Asteroid> createAsteroidBelt(Vector range) {
        float distanceToNextObject = 20.0f;
        int amountOfObjects = (int) (range.z / distanceToNextObject) + 1;
        return new GameObjectChain<>(
                createAsteroid(),
                amountOfObjects,
                -50.0f,
                distanceToNextObject,
                new Vector(
                        Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY),
                new Vector(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, 0.5f));
    }

    @Override
    public HashMap<String, GameObjectChain<? extends AbstractGameObject>> createGameObjectChains(
            Vector range) {
        HashMap<String, GameObjectChain<? extends AbstractGameObject>> chains = new HashMap<>();
        // TODO move string into strings.xml?
        GameObjectChain<Asteroid> asteroidBelt = createAsteroidBelt(range);
        chains.put("asteroids", asteroidBelt);
        return chains;
    }
}
