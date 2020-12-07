package de.games.dodgeroids.logic;

import de.games.dodgeroids.R;
import de.games.dodgeroids.datamanagers.DodgeroidsSaveGame;
import de.games.dodgeroids.datamanagers.DodgeroidsSettingsManager;
import de.games.engine.AbstractGameActivity;
import de.games.engine.datamanagers.Scene;
import de.games.engine.datamanagers.SoundManager;
import de.games.engine.graphics.AbstractBound.DetectionMethod;
import de.games.engine.graphics.SphereBound;
import de.games.engine.graphics.Sprite;
import de.games.engine.graphics.Vector;
import de.games.engine.levels.AbstractLevelFactory;
import de.games.engine.logic.AbstractGameLogic;
import de.games.engine.objects.AbstractGameObject;
import de.games.engine.objects.Asteroid;
import de.games.engine.objects.GameObjectChain;
import de.games.engine.objects.Player;
import de.games.engine.objects.TunnelPart;

public class GameLoopLogic extends AbstractGameLogic {

    /** control flags * */
    private boolean isPaused = true;

    private boolean isDownPressed = false;

    /** quick references * */
    private final GameObjectChain<Asteroid> asteroidBelt;

    private final GameObjectChain<TunnelPart> tunnel;
    private final Player player;
    private final Sprite explosion;
    private final int flipVal;

    @SuppressWarnings("unchecked")
    public GameLoopLogic(
            final AbstractGameActivity activity,
            final Scene scene,
            final AbstractLevelFactory levelFactory) {
        super(activity, scene, levelFactory);
        this.flipVal = DodgeroidsSettingsManager.getInstance().isControlFlipped() ? -1 : 1;
        this.player = scene.getPlayer();
        this.explosion = scene.getSprite(activity.getString(R.string.label_explosion));
        this.asteroidBelt =
                (GameObjectChain<Asteroid>) scene.getGameObjectChains().get("asteroids");
        this.tunnel = (GameObjectChain<TunnelPart>) scene.getGameObjectChains().get("tunnel");
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
        TunnelPart tunnelPart = tunnel.getFirst();
        SphereBound bound =
                (SphereBound)
                        tunnelPart
                                .getBounds()
                                .get(0); // FIXME get(0) ist statisch und h�ngt von der box datei ab
        moveWithinYAxisLimit(
                player,
                delta,
                bound.getRadius(tunnelPart),
                (isDownPressed ? -0.3f : 0.3f) * flipVal);
    }

    private void moveWithinYAxisLimit(
            final AbstractGameObject o,
            final float delta,
            final float radius,
            final float yChange) {
        moveY(o, delta, yChange * player.scale2dMovement);
        SphereBound bound =
                (SphereBound)
                        o.getBounds()
                                .get(3); // FIXME get(3) ist statisch und h�ngt von der box datei ab
        Vector pos = o.getPos();
        float r = radius - bound.getRadius(o);
        if (pos.y > r) {
            pos.y = r;
        } else if (pos.y < -r) {
            pos.y = -r;
        }
    }

    private void moveY(final AbstractGameObject o, final float delta, final float yChange) {
        o.getPos().y += (delta * player.getVelocity().y * yChange);
    }

    public void setDownPressed(final boolean isDownPressed) {
        this.isDownPressed = isDownPressed;
    }
}
