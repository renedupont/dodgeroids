package android.games.datamanagers;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.util.Log;
import android.view.KeyEvent;

public class KeyInputManager extends Thread {
	private static final int THREAD_WAIT_TIME = 30;
	private final Stack<KeyEvent> eventsBuffer = new Stack<KeyEvent>();
	private final List<KeyEventListener> listeners = new ArrayList<KeyEventListener>();
	private final KeyEvent[] keysCache = new KeyEvent[210];
	private static KeyInputManager instance = new KeyInputManager();

	private KeyInputManager() {
		start();
	}

	public static KeyInputManager getInstance() {
		return instance;
	}

	public boolean addListener(final KeyEventListener listener) {
		synchronized (listeners) {
			if (!listeners.contains(listener)) {
				return listeners.add(listener);
			}
		}
		return false;
	}

	public boolean removeListener(final KeyEventListener listener) {
		synchronized (listeners) {
			return listeners.remove(listener);
		}
	}

	public void registerKeyEvent(final KeyEvent event, final boolean status) {
		synchronized (eventsBuffer) {
			/**
			 * alternative zu synchronized, evtl schneller:
			 * http://docs.oracle.com
			 * /javase/7/docs/api/java/util/concurrent/CopyOnWriteArrayList.html
			 **/
			if (event != null) {
				eventsBuffer.add(event);
			}
		}
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			synchronized (eventsBuffer) {
				for (KeyEvent event : eventsBuffer) {
					switch (event.getAction()) {
					case (KeyEvent.ACTION_DOWN):
						if (keysCache[event.getKeyCode()] == null) {
							keysCache[event.getKeyCode()] = event;
						}
						synchronized (listeners) {
							for (KeyEventListener listener : listeners) {
								listener.keyPressed(event);
							}
						}
						break;
					case (KeyEvent.ACTION_UP):
						keysCache[event.getKeyCode()] = null;
						synchronized (listeners) {
							for (KeyEventListener listener : listeners) {
								listener.keyReleased(event);
							}
						}
						break;
					case (KeyEvent.ACTION_MULTIPLE):
						break;
					}
				}
				eventsBuffer.clear();
			}
			for (KeyEvent event : keysCache) {
				if (event != null) {
					synchronized (listeners) {
						for (KeyEventListener listener : listeners) {
							listener.keyHeld(event);
						}
					}
				}
			}

			try {
				Thread.sleep(THREAD_WAIT_TIME);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
	}

	public void dispose() {
		interrupt();
	}
}
