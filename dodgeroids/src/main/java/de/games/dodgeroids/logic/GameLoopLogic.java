package de.games.dodgeroids.logic;

import de.games.dodgeroids.R;
import de.games.dodgeroids.datamanagers.DodgeroidsSaveGame;
import de.games.dodgeroids.datamanagers.DodgeroidsSettingsManager;
import de.games.dodgeroids.datamanagers.SoundManager;
import de.games.dodgeroids.objects.Asteroid;
import de.games.dodgeroids.objects.Player;
import de.games.engine.graphics.AbstractBound;
import de.games.engine.graphics.AbstractBound.DetectionMethod;
import de.games.engine.graphics.Sprite;
import de.games.engine.graphics.Vector;
import de.games.engine.objects.AbstractGameObject;
import de.games.engine.objects.GameObjectChain;
import de.games.engine.scenes.Scene;

public class GameLoopLogic {

    private Scene scene;

    private GameObjectChain<Asteroid> asteroidBelt;
    private Player player;
    private Sprite explosion;
    private int flipVal;
    private float maxLevelHeight;

    private boolean isPaused = true;
    private boolean isDownPressed = false;

    @SuppressWarnings("unchecked")
    public GameLoopLogic(Scene scene, float maxLevelHeight, Player player, String explosionLabel) {
        this.scene = scene;
        this.flipVal = DodgeroidsSettingsManager.getInstance().isControlFlipped() ? -1 : 1;
        this.maxLevelHeight = maxLevelHeight;
        this.player = player;
        this.explosion = scene.getSprite(explosionLabel);
        this.asteroidBelt =
                (GameObjectChain<Asteroid>) scene.getGameObjectChains().get("asteroids");
    }

    public void update(final float deltaTime) {
        if (!isPaused) {
            movePlayer(deltaTime); // TODO noch in engine ne alternativ methode anbieten
            collisionHandling(deltaTime);
            scene.update(deltaTime);
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(final boolean isPaused) {
        this.isPaused = isPaused;
    }

    public void storeSaveGame() {
        DodgeroidsSaveGame.getInstance().storeSaveGame(player);
    }

    public boolean isGameDone() {
        return player.lives == 0 && !explosion.isPlaying;
    }

    public boolean isCollidingWith(
            final AbstractGameObject obj1,
            final AbstractBound.DetectionMethod type1,
            final AbstractGameObject obj2,
            final AbstractBound.DetectionMethod type2) {
        for (AbstractBound bound1 : obj1.getBounds()) {
            for (AbstractBound bound2 : obj2.getBounds()) {
                if (bound1.isCollidingWith(obj1, type1, obj2, type2, bound2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void collisionHandling(final float delta) {
        if (player.lives == 0) {
            return;
        }
        if (!player.isImmortal()) {
            if (isCollidingWith(
                    player,
                    DetectionMethod.OUTER,
                    asteroidBelt.getFirst(),
                    DetectionMethod.OUTER)) {
                explosion.setPosition(
                        new Vector(player.getPos().x, player.getPos().y, player.getPos().z + 1f));
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

    public void setDownPressed(final boolean isDownPressed) {
        this.isDownPressed = isDownPressed;
    }

    public void dispose() {}

    private void movePlayer(float delta) {
        float yChange = (isDownPressed ? -0.3f : 0.3f) * flipVal;
        moveY(player, delta, yChange * player.scale2dMovement);
        Vector pos = player.getPos();
        float yMax = maxLevelHeight;
        if (pos.y > yMax) {
            pos.y = yMax;
        } else if (pos.y < -yMax) {
            pos.y = -yMax;
        }
    }

    private void moveY(final AbstractGameObject o, final float delta, final float yChange) {
        o.getPos().y += (delta * o.getVelocity().y * yChange);
    }
}
