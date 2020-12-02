package de.games.keepitup.levels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.games.engine.datamanagers.Scene;
import android.games.engine.graphics.Color;
import android.games.engine.graphics.Light;
import android.games.engine.graphics.Light.Id;
import android.games.engine.graphics.Light.Type;
import android.games.engine.graphics.Mesh;
import android.games.engine.graphics.RotationSettings;
import android.games.engine.graphics.SphereBound;
import android.games.engine.graphics.Vector;
import android.games.engine.levels.AbstractLevelFactory;
import android.games.engine.objects.AbstractGameObject;
import android.games.engine.objects.Asteroid;
import android.games.engine.objects.Block;
import android.games.engine.objects.GameObjectChain;
import android.games.engine.objects.Player;
import android.games.engine.objects.TunnelPart;
import android.games.engine.objects.TunnelRing;
import de.games.keepitup.R;

public class SpaceLevelFactory extends AbstractLevelFactory {

	@Override
	protected int getBoxId() {
		//		return R.raw.data;

		return R.raw.data_new;
	}

	@Override
	public List<String> createMeshIdList() {
		List<String> meshIds = new ArrayList<String>();
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
		List<String> textureIds = new ArrayList<String>();
		textureIds.add("ufo_diffuse.jpg");
		textureIds.add("asteroid.jpg");
		textureIds.add("cloud2.png");
		textureIds.add("tunnelring.png");
		return textureIds;
	}

	@Override
	public List<Light> createLights() {
		List<Light> lights = new ArrayList<Light>();

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
	// generic type erasure
	public <T extends AbstractGameObject> T createGameObject(
			final Class<?> type, final Vector startPosition,
			final float minRadius) {
		if (type.equals(Player.class)) {
			return (T) createPlayer(startPosition);
		} else if (type.equals(Asteroid.class)) {
			return (T) createAsteroid(startPosition, minRadius);
		} else if (type.equals(TunnelPart.class)) {
			return (T) createTunnelPart(startPosition);
		} else if (type.equals(TunnelRing.class)) {
			return (T) createTunnelRing(startPosition);
		}
		return null; // TODO exception schmei�en bei null
	}

	@Override
	public Player createPlayer(final Vector startPosition) {
		HashMap<Mesh, RotationSettings> playerMeshes = new HashMap<Mesh, RotationSettings>();
		playerMeshes.put(meshes.get("ufo_body"), new RotationSettings(
				new Vector(0.0f, 0.0f, 0.0f), new Vector(0.0f, 0.0f, 0.0f)));
		playerMeshes.put(meshes.get("ufo_ring"), new RotationSettings(
				new Vector(0.0f, 0.0f, 0.0f), new Vector(0.0f, 25.0f, 0.0f)));
		Vector velocity = new Vector(25.0f, 25.0f, 0.0f);
		return new Player(playerMeshes, textures.get("ufo_diffuse.jpg"),
				velocity, startPosition, 1.3f, 3, 3.0f);
	}

	@Override
	public AbstractGameObject createAsteroid(final Vector startPosition,
			final float minRadius) {
		HashMap<Mesh, RotationSettings> asteroidMeshes = new HashMap<Mesh, RotationSettings>();
		int asteroidType = new Random().nextInt(4); // choose randomly one of
													// the four meshes
		asteroidMeshes.put(meshes.get("asteroid_"
				+ Integer.toString(asteroidType + 1)),
				new RotationSettings(new Vector(0.0f, 0.0f, 0.0f), new Vector(
						(0.5f - (float) Math.random()) * 80.0f,
						(0.5f - (float) Math.random()) * 80.0f,
						(0.5f - (float) Math.random()) * 80.0f)));
		Vector velocity = new Vector(0.0f, 0.0f, 24.0f);
		float scaleFactor = 0.8f;
		float maxRadius = ((SphereBound) meshes.get("tunnelPart").getBounds()
				.get(0)).getRadius();
		float asteroidRadius = ((SphereBound) meshes
				.get("asteroid_" + Integer.toString(asteroidType + 1))
				.getBounds().get(0)).getRadius()
				* scaleFactor;
		setRandomY(startPosition, asteroidRadius, maxRadius);
		return new Asteroid(null, asteroidMeshes, textures.get("asteroid.jpg"),
				scaleFactor, velocity, startPosition);
	}

	@Override
	public AbstractGameObject createTunnelPart(final Vector startPosition) {
		HashMap<Mesh, RotationSettings> tunnelPartMeshes = new HashMap<Mesh, RotationSettings>();
		tunnelPartMeshes.put(meshes.get("tunnelPart"), new RotationSettings(
				new Vector(0.0f, 0.0f, 0.0f), new Vector(0.0f, 0.0f, 0.0f)));
		Vector velocity = new Vector(0.0f, 0.0f, 20.0f);
		TunnelPart tunnelPart = new TunnelPart(tunnelPartMeshes, textures.get("cloud2.png"),
				velocity, startPosition);
        tunnelPart.setHidden(true);
        return tunnelPart;
	}

	@Override
	public AbstractGameObject createTunnelRing(final Vector startPosition) {
		HashMap<Mesh, RotationSettings> tunnelRingMeshes = new HashMap<Mesh, RotationSettings>();
		tunnelRingMeshes.put(meshes.get("tunnelRing"), new RotationSettings(
				new Vector(0.0f, 0.0f, 0.0f), new Vector(0.0f, 0.0f, 0.0f)));
		Vector velocity = new Vector(0.0f, 0.0f, 20.0f);
		return new TunnelRing(tunnelRingMeshes, textures.get("tunnelring.png"),
				velocity, startPosition);
	}

	//	public LinkedList<Block[]> createStartBlockArray() {
	//		this.amountOfBlockRows = 60;
	//		this.amountOfBlockSlotsInRow = 30;
	//		this.amountOfFilledBlockSlotsInRow = 10;
	//		this.currentObstacleIndex = 0;
	//		this.newObstacleIndex = 30;
	//		LinkedList<Block[]> blockArray = new LinkedList<Block[]>();
	//		Block[] lastBlockRow = null;
	//		for (int i = 0; i < amountOfBlockRows; i++) {
	//			blockArray.add(calculateNewBlockRow(lastBlockRow));
	//			lastBlockRow = blockArray.getLast();
	//		}
	//		return blockArray;
	//	}

	public AbstractGameObject createBlock(final Vector startPosition) {
		HashMap<Mesh, RotationSettings> blockMeshes = new HashMap<Mesh, RotationSettings>();
		blockMeshes.put(meshes.get("block"), new RotationSettings( // TODO: hier den tats�chlichen namen des meshes
				new Vector(0.0f, 0.0f, 0.0f), new Vector(0.0f, 0.0f, 0.0f)));
		Vector velocity = new Vector(0.0f, 0.0f, 20.0f);
		return new Block(null, blockMeshes, textures.get("block.png"),
				velocity, startPosition);
	}

	@Override
	public <T extends AbstractGameObject> GameObjectChain<T> createGameObjectChain(
			final Class<T> type, final Scene scene, final Vector range,
			final float minRadius) {
		float distanceToNextObject;
		int amountOfObjects;
		if (type.equals(Asteroid.class)) {
			distanceToNextObject = 20.0f;
			amountOfObjects = (int) (range.z / distanceToNextObject) + 1;
			return new GameObjectChain<T>(Asteroid.class, minRadius, scene,
					this, amountOfObjects, -50.0f, distanceToNextObject,
					new Vector(Float.NEGATIVE_INFINITY,
							Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY),
					new Vector(Float.POSITIVE_INFINITY,
							Float.POSITIVE_INFINITY, 0.5f),
					((SphereBound) meshes.get("tunnelPart").getBounds().get(0))
							.getRadius());
		}
		if (type.equals(TunnelPart.class)) {
			distanceToNextObject = 20.0f;
			amountOfObjects = (int) (range.z / distanceToNextObject) + 1;
			return new GameObjectChain<T>(TunnelPart.class, 0, scene, this,
					amountOfObjects, 10.0f, distanceToNextObject, new Vector(
							Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY,
							Float.NEGATIVE_INFINITY), new Vector(
							Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY,
							35.0f), Float.POSITIVE_INFINITY);
		}
		if (type.equals(TunnelRing.class)) {
			distanceToNextObject = 20.0f;
			amountOfObjects = (int) (range.z / distanceToNextObject) + 1;
			return new GameObjectChain<T>(TunnelRing.class, 0, scene, this,
					amountOfObjects, 10.0f, distanceToNextObject, new Vector(
							Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY,
							Float.NEGATIVE_INFINITY), new Vector(
							Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY,
							35.0f), Float.POSITIVE_INFINITY);
		}
		return null; // TODO Exception schmei�en
	}

	@Override
	public HashMap<String, GameObjectChain<? extends AbstractGameObject>> createGameObjectChains(
			final Scene scene, final Vector range) {
		HashMap<String, GameObjectChain<? extends AbstractGameObject>> chains = new HashMap<String, GameObjectChain<? extends AbstractGameObject>>();
/*		GameObjectChain<TunnelRing> tunnelRings = createGameObjectChain(
				TunnelRing.class, scene, range, 0);
		chains.put("tunnelRings", tunnelRings); // TODO strings noch in
												// strings.xml bringen*/
		GameObjectChain<TunnelPart> tunnel = createGameObjectChain(
				TunnelPart.class, scene, range, 0);
		chains.put("tunnel", tunnel);
		GameObjectChain<Asteroid> asteroidBelt = createGameObjectChain(
				Asteroid.class, scene, range, 0);
		chains.put("asteroids", asteroidBelt);
		return chains;
	}

}
