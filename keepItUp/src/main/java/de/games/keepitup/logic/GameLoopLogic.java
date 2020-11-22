package de.games.keepitup.logic;

import android.games.AbstractGameActivity;
import android.games.datamanagers.Scene;
import android.games.datamanagers.SoundManager;
import android.games.graphics.AbstractBound;
import android.games.graphics.AbstractBound.DetectionMethod;
import android.games.graphics.SphereBound;
import android.games.graphics.Sprite;
import android.games.graphics.Vector;
import android.games.levels.AbstractLevelFactory;
import android.games.logic.AbstractGameLogic;
import android.games.objects.AbstractGameObject;
import android.games.objects.Asteroid;
import android.games.objects.GameObjectChain;
import android.games.objects.Player;
import android.games.objects.TunnelPart;
import de.games.keepitup.R;
import de.games.keepitup.datamanagers.KeepItUpSaveGame;
import de.games.keepitup.datamanagers.KeepItUpSettingsManager;

public class GameLoopLogic extends AbstractGameLogic {
	/** control flags **/
	private boolean isPaused = true;
	private boolean isDownPressed = false;

	//	private final List<Block[]> blockArray;
	/** quick references **/
	private final GameObjectChain<Asteroid> asteroidBelt;
	private final GameObjectChain<TunnelPart> tunnel;
	private final Player player;
	private final Sprite explosion;
	private final int flipVal;

	@SuppressWarnings("unchecked")
	// generic type erasure
	public GameLoopLogic(final AbstractGameActivity activity,
			final Scene scene, final AbstractLevelFactory levelFactory) {
		super(activity, scene, levelFactory);
		this.flipVal = KeepItUpSettingsManager.getInstance().isControlFlipped() ? -1
				: 1;
		this.player = scene.getPlayer();
		this.explosion = scene.getSprite(activity
				.getString(R.string.label_explosion));
		this.asteroidBelt = (GameObjectChain<Asteroid>) scene
				.getGameObjectChains().get("asteroids");
		this.tunnel = (GameObjectChain<TunnelPart>) scene.getGameObjectChains()
				.get("tunnel");

		//		this.blockArray = 
	}

	@Override
	public void update(final float deltaTime) {
		//		this.isDownPressed = isDownPressed;
		if (!isPaused) {
			movePlayer(deltaTime, 0f, 0f); // TODO noch in engine ne alternativ methode anbieten
			collisionHandling(deltaTime);
			scene.update(deltaTime);
		}
	}

	@Override
	public boolean isPaused() {
		return isPaused;
	}

	@Override
	public void setPaused(final boolean isPaused) {
		this.isPaused = isPaused;
	}

	@Override
	public void storeSaveGame() {
		KeepItUpSaveGame.getInstance().storeSaveGame(player);
	}

	@Override
	public boolean isGameDone() {
		if (player.lives == 0 && !explosion.isPlaying) { // in case of game over
			return true;
		}
		return false;
	}

	private void collisionHandling(final float delta) {
		if (player.lives == 0) {
			return;
		}
		if (!player.isImmortal()) {
			if (isCollidingWith(player, DetectionMethod.OUTER,
					asteroidBelt.getFirst(), DetectionMethod.OUTER)) {
				explosion.setPosition(new Vector(player.getPos().x, player
						.getPos().y, player.getPos().z + 1f));
				explosion.drawSprite(1000);
				player.lives--;
				SoundManager.getInstance().playSound(R.raw.explosion, 1);
				if (player.lives == 0) {
					player.setHidden(true);
					SoundManager.getInstance().pauseMusic();
				} else {
					player.startImmortality();
				}
			}
		} else {
			player.updateImmortalityStatus(delta);
		}
	}

	@Override
	protected void movePlayer(final float delta, final float xChange,
			final float yChange) {
		TunnelPart tunnelPart = tunnel.getFirst();
		SphereBound bound = (SphereBound) tunnelPart.getBounds().get(0); // FIXME get(0) ist statisch und h�ngt von der box datei ab
		//			moveWithinYAxisLimit(player, delta, bound.getRadius(tunnelPart),
		//					yChange * flipVal);
		//		System.out.println(yChange);
		moveWithinYAxisLimit(player, delta, bound.getRadius(tunnelPart),
				(isDownPressed ? -0.3f : 0.3f) * flipVal);
	}

	private void moveWithinYAxisLimit(final AbstractGameObject o,
			final float delta, final float radius, final float yChange) {
		moveY(o, delta, yChange * player.scale2dMovement);
		SphereBound bound = (SphereBound) o.getBounds().get(3); // FIXME get(3) ist statisch und h�ngt von der box datei ab
		Vector pos = o.getPos();
		float r = radius - bound.getRadius(o);
		if (pos.y > r) {
			pos.y = r;
		} else if (pos.y < -r) {
			pos.y = -r;
		}
	}

	private void moveWithin2DRadius(final AbstractGameObject o,
			final float delta, final float radius, final float xChange,
			final float yChange) {
		float prevX = o.getPos().x;
		float prevY = o.getPos().y;

		Vector pos = o.getPos();

		moveX(o, delta, xChange);
		moveY(o, delta, yChange);

		// Sonderfall weil Bound von TunnelPart nicht korrekt ist.
		AbstractGameObject tunnelPart = tunnel.getFirst();
		for (AbstractBound tunnelBound : tunnelPart.getBounds()) {
			tunnelBound.getOffset().z = 0 - tunnelPart.getPos().z;
		}

		// Player hat noch SphereBound damit wir den Radius haben, welchen wir
		// nur f�r das movement handling weiter unten ben�tigen
		if (isCollidingWith(o, DetectionMethod.OUTER, tunnelPart,
				DetectionMethod.INNER)) { // tunnel ber�hrt

			float diff;
			SphereBound bound = (SphereBound) o.getBounds().get(3); // FIXME get(3) ist statisch und h�ngt von der box datei ab
			float r = radius - bound.getRadius(o);

			if (pos.x >= 0 && pos.y >= 0) { // Quadrant 1
				if (xChange >= yChange) {
					diff = xChange;
					pos.x = prevX + (delta * player.getVelocity().x * diff);
					if (pos.x <= r) {
						pos.y = (float) Math.sqrt(r * r - pos.x * pos.x);
					} else {
						pos.x = r;
						pos.y = 0.f;
					}
				} else {
					diff = yChange;
					pos.y = prevY + (delta * player.getVelocity().y * diff);
					if (pos.y <= r) {
						pos.x = (float) Math.sqrt(r * r - pos.y * pos.y);
					} else {
						pos.y = r;
						pos.x = 0.f;// -0.01f;
					}
				}
			} else if (pos.x < 0 && pos.y >= 0) { // Quadrant 2
				float xAbs = Math.abs(xChange);
				if (xAbs >= yChange) {
					diff = xAbs;
					pos.x = prevX - (delta * player.getVelocity().x * diff);
					if (pos.x > -r) {
						pos.y = (float) Math.sqrt(r * r - pos.x * pos.x);
					} else {
						pos.x = -r;
						pos.y = 0.f;// -0.01f;
					}
				} else {
					diff = yChange;
					pos.y = prevY + (delta * player.getVelocity().y * diff);
					if (pos.y <= r) {
						pos.x = -(float) Math.sqrt(r * r - pos.y * pos.y);
					} else {
						pos.y = r;
						pos.x = -Float.MIN_VALUE;// 0.01f;
					}
				}
			} else if (pos.x < 0 && pos.y < 0) { // Quadrant 3
				if (xChange <= yChange) {
					diff = xChange;
					pos.x = prevX + (delta * player.getVelocity().x * diff);
					if (pos.x > -r) {
						pos.y = -(float) Math.sqrt(r * r - pos.x * pos.x);
					} else {
						pos.x = -r;
						pos.y = -Float.MIN_VALUE;// 0.01f;
					}
				} else {
					diff = yChange;
					pos.y = prevY + (delta * player.getVelocity().y * diff);
					if (pos.y > -r) {
						pos.x = -(float) Math.sqrt(r * r - pos.y * pos.y);
					} else {
						pos.y = -r;
						pos.x = -Float.MIN_VALUE;// 0.01f;
					}
				}
			} else if (pos.x >= 0 && pos.y < 0) { // Quadrant 4
				float yAbs = Math.abs(yChange);
				if (xChange >= yAbs) {
					diff = xChange;
					pos.x = prevX + (delta * player.getVelocity().x * diff);
					if (pos.x <= r) {
						pos.y = -(float) Math.sqrt(r * r - pos.x * pos.x);
					} else {
						pos.x = r;
						pos.y = -Float.MIN_VALUE;// 0.01f;
					}
				} else {
					diff = yAbs;
					pos.y = prevY - (delta * player.getVelocity().y * diff);
					if (pos.y > -r) {
						pos.x = (float) Math.sqrt(r * r - pos.y * pos.y);
					} else {
						pos.y = -r;
						pos.x = Float.MIN_VALUE;// 0.f;//-0.01f;
					}
				}
			} else {
				pos.x = prevX;
				pos.y = prevY;
			}
		}
	}

	private void moveX(final AbstractGameObject o, final float delta,
			final float xChange) {
		o.getPos().x += (delta * player.getVelocity().x * xChange);
	}

	private void moveY(final AbstractGameObject o, final float delta,
			final float yChange) {
		o.getPos().y += (delta * player.getVelocity().y * yChange);
	}

	public boolean isDownPressed() {
		return isDownPressed;
	}

	public void setDownPressed(final boolean isDownPressed) {
		this.isDownPressed = isDownPressed;
	}

}
