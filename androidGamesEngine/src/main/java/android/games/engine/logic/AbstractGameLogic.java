package android.games.engine.logic;

import android.games.engine.AbstractGameActivity;
import android.games.engine.datamanagers.Scene;
import android.games.engine.graphics.AbstractBound;
import android.games.engine.graphics.AbstractBound.DetectionMethod;
import android.games.engine.levels.AbstractLevelFactory;
import android.games.engine.objects.AbstractGameObject;

public abstract class AbstractGameLogic {

	/** class elements **/
	protected final AbstractGameActivity activity;
	protected AbstractLevelFactory levelFactory;
	protected Scene scene;

	/** ctors **/
	public AbstractGameLogic(final AbstractGameActivity activity,
			final Scene scene, final AbstractLevelFactory levelFactory) {
		this.activity = activity;
		this.levelFactory = levelFactory;
		this.scene = scene;
	}

	/** abstract methods **/
	public abstract boolean isPaused();

	public abstract void setPaused(boolean isPaused);

	public abstract void storeSaveGame();

	public abstract void update(float deltaTime);

	public abstract boolean isGameDone();

	/** unimplemented methods as suggestions **/
	public void dispose() {
	}

	protected void movePlayer(final float delta, final float xChange,
			final float yChange) {
	};

	protected void moveGameObject(final AbstractGameLogic o, final float delta,
			final float xChange, final float yChange) {
	};

	/**
	 * implemented methods which are abstract enough to be valid for every
	 * subclass
	 **/
	public boolean isCollidingWith(final AbstractGameObject obj1,
			final DetectionMethod type1, final AbstractGameObject obj2,
			final DetectionMethod type2) {
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
