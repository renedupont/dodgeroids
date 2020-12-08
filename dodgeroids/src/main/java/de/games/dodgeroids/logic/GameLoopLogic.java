package de.games.dodgeroids.logic;

import de.games.dodgeroids.R;
import de.games.dodgeroids.datamanagers.DodgeroidsSaveGame;
import de.games.dodgeroids.datamanagers.DodgeroidsSettingsManager;
import de.games.engine.AbstractGameActivity;
import de.games.engine.datamanagers.Scene;
import de.games.engine.datamanagers.SoundManager;
import de.games.engine.graphics.AbstractBound.DetectionMethod;
import de.games.engine.graphics.Sprite;
import de.games.engine.graphics.Vector;
import de.games.engine.levels.AbstractLevelFactory;
import de.games.engine.logic.AbstractGameLogic;
import de.games.engine.objects.AbstractGameObject;
import de.games.engine.objects.Asteroid;
import de.games.engine.objects.GameObjectChain;
import de.games.engine.objects.Player;

public class GameLoopLogic extends AbstractGameLogic {

    /** control flags * */
    private boolean isPaused = true;

    private boolean isDownPressed = false;

    /** quick references * */
    private final GameObjectChain<Asteroid> asteroidBelt;

    private final Player player;
    private final Sprite explosion;
    private final int flipVal;
    private float maxLevelHeight;

    @SuppressWarnings("unchecked")
    public GameLoopLogic(
            final AbstractGameActivity activity,
            final Scene scene,
            final AbstractLevelFactory levelFactory) {
        super(activity, scene, levelFactory);
        this.flipVal = DodgeroidsSettingsManager.getInstance().isControlFlipped() ? -1 : 1;
        this.maxLevelHeight = levelFactory.getMaxHeight();
        this.player = scene.getPlayer();
        this.explosion = scene.getSprite(activity.getString(R.string.label_explosion));
        this.asteroidBelt =
                (GameObjectChain<Asteroid>) scene.getGameObjectChains().get("asteroids");
    }

    @Override
    public void update(final float deltaTime) {
        if (!isPaused) {
            movePlayer(deltaTime); // TODO noch in engine ne alternativ methode anbieten
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
        DodgeroidsSaveGame.getInstance().storeSaveGame(player);
    }

    @Override
    public boolean isGameDone() {
        return player.lives == 0 && !explosion.isPlaying;
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

    @Override
    protected void movePlayer(float delta) {
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

    public void setDownPressed(final boolean isDownPressed) {
        this.isDownPressed = isDownPressed;
    }
}
