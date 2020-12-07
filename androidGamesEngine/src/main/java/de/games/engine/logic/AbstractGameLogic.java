package de.games.engine.logic;

import de.games.engine.AbstractGameActivity;
import de.games.engine.datamanagers.Scene;
import de.games.engine.graphics.AbstractBound;
import de.games.engine.levels.AbstractLevelFactory;
import de.games.engine.objects.AbstractGameObject;

public abstract class AbstractGameLogic {

    protected final AbstractGameActivity activity;

    protected AbstractLevelFactory levelFactory;
    protected Scene scene;

    public AbstractGameLogic(
            final AbstractGameActivity activity,
            final Scene scene,
            final AbstractLevelFactory levelFactory) {
        this.activity = activity;
        this.levelFactory = levelFactory;
        this.scene = scene;
    }

    public abstract boolean isPaused();

    public abstract void setPaused(boolean isPaused);

    public abstract void storeSaveGame();

    public abstract void update(float deltaTime);

    public abstract boolean isGameDone();

    /** unimplemented methods as suggestions * */
    public void dispose() {}

    protected void movePlayer(final float delta) {}

    /** implemented methods which are abstract enough to be valid for every subclass */
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
}
