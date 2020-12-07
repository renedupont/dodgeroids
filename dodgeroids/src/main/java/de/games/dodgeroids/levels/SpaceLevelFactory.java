package de.games.dodgeroids.levels;

import de.games.dodgeroids.R;
import de.games.engine.datamanagers.Scene;
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
import de.games.engine.objects.Asteroid;
import de.games.engine.objects.GameObjectChain;
import de.games.engine.objects.Player;
import de.games.engine.objects.TunnelPart;
import de.games.engine.objects.TunnelRing;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SpaceLevelFactory extends AbstractLevelFactory {

    @Override
    protected int getBoxId() {
        //		return R.raw.data;
        return R.raw.data_new;
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
        meshIds.add("tunnelPart");
        meshIds.add("tunnelRing");
        meshIds.add("TerrainBox");
        return meshIds;
    }

    @Override
    public List<String> createTextureIdList() {
        List<String> textureIds = new ArrayList<>();
        textureIds.add("ufo_diffuse.jpg");
        textureIds.add("asteroid.jpg");
        textureIds.add("cloud2.png");
        textureIds.add("tunnelring.png");
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

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AbstractGameObject> T createGameObject(
            final Class<?> type, final Vector startPosition) {
        if (type.equals(Player.class)) {
            return (T) createPlayer(startPosition);
        } else if (type.equals(Asteroid.class)) {
            return (T) createAsteroid(startPosition);
        } else if (type.equals(TunnelPart.class)) {
            return (T) createTunnelPart(startPosition);
        } else if (type.equals(TunnelRing.class)) {
            return (T) createTunnelRing(startPosition);
        }
        return null; // TODO rather throw exception here
    }

    @Override
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

    @Override
    public AbstractGameObject createAsteroid(final Vector startPosition) {
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
        float maxRadius = ((SphereBound) meshes.get("tunnelPart").getBounds().get(0)).getRadius();
        float asteroidRadius =
                ((SphereBound) meshes.get("asteroid_" + (asteroidType + 1)).getBounds().get(0))
                                .getRadius()
                        * scaleFactor;
        setRandomY(startPosition, asteroidRadius, maxRadius);
        return new Asteroid(
                asteroidMeshes,
                textures.get("asteroid.jpg"),
                scaleFactor,
                velocity,
                startPosition);
    }

    @Override
    public AbstractGameObject createTunnelPart(final Vector startPosition) {
        HashMap<Mesh, RotationSettings> tunnelPartMeshes = new HashMap<>();
        tunnelPartMeshes.put(
                meshes.get("tunnelPart"),
                new RotationSettings(new Vector(0.0f, 0.0f, 0.0f), new Vector(0.0f, 0.0f, 0.0f)));
        Vector velocity = new Vector(0.0f, 0.0f, 20.0f);
        TunnelPart tunnelPart =
                new TunnelPart(
                        tunnelPartMeshes, textures.get("cloud2.png"), velocity, startPosition);
        tunnelPart.setHidden(true);
        return tunnelPart;
    }

    @Override
    public AbstractGameObject createTunnelRing(final Vector startPosition) {
        HashMap<Mesh, RotationSettings> tunnelRingMeshes = new HashMap<>();
        tunnelRingMeshes.put(
                meshes.get("tunnelRing"),
                new RotationSettings(new Vector(0.0f, 0.0f, 0.0f), new Vector(0.0f, 0.0f, 0.0f)));
        Vector velocity = new Vector(0.0f, 0.0f, 20.0f);
        return new TunnelRing(
                tunnelRingMeshes, textures.get("tunnelring.png"), velocity, startPosition);
    }

    @Override
    public <T extends AbstractGameObject> GameObjectChain<T> createGameObjectChain(
            final Class<T> type, final Scene scene, final Vector range, final float minRadius) {
        float distanceToNextObject;
        int amountOfObjects;
        if (type.equals(Asteroid.class)) {
            distanceToNextObject = 20.0f;
            amountOfObjects = (int) (range.z / distanceToNextObject) + 1;
            return new GameObjectChain<>(
                    Asteroid.class,
                    minRadius,
                    scene,
                    this,
                    amountOfObjects,
                    -50.0f,
                    distanceToNextObject,
                    new Vector(
                            Float.NEGATIVE_INFINITY,
                            Float.NEGATIVE_INFINITY,
                            Float.NEGATIVE_INFINITY),
                    new Vector(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, 0.5f));
        }
        if (type.equals(TunnelPart.class)) {
            distanceToNextObject = 20.0f;
            amountOfObjects = (int) (range.z / distanceToNextObject) + 1;
            return new GameObjectChain<>(
                    TunnelPart.class,
                    0,
                    scene,
                    this,
                    amountOfObjects,
                    10.0f,
                    distanceToNextObject,
                    new Vector(
                            Float.NEGATIVE_INFINITY,
                            Float.NEGATIVE_INFINITY,
                            Float.NEGATIVE_INFINITY),
                    new Vector(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, 35.0f));
        }
        if (type.equals(TunnelRing.class)) {
            distanceToNextObject = 20.0f;
            amountOfObjects = (int) (range.z / distanceToNextObject) + 1;
            return new GameObjectChain<>(
                    TunnelRing.class,
                    0,
                    scene,
                    this,
                    amountOfObjects,
                    10.0f,
                    distanceToNextObject,
                    new Vector(
                            Float.NEGATIVE_INFINITY,
                            Float.NEGATIVE_INFINITY,
                            Float.NEGATIVE_INFINITY),
                    new Vector(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, 35.0f));
        }
        return null; // TODO rather throw exception here
    }

    @Override
    public HashMap<String, GameObjectChain<? extends AbstractGameObject>> createGameObjectChains(
            final Scene scene, final Vector range) {
        HashMap<String, GameObjectChain<? extends AbstractGameObject>> chains = new HashMap<>();
        GameObjectChain<TunnelPart> tunnel =
                createGameObjectChain(TunnelPart.class, scene, range, 0);
        // TODO move strings into strings.xml?
        chains.put("tunnel", tunnel);
        GameObjectChain<Asteroid> asteroidBelt =
                createGameObjectChain(Asteroid.class, scene, range, 0);
        chains.put("asteroids", asteroidBelt);
        return chains;
    }
}
