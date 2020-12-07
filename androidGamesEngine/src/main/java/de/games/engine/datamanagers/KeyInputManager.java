package de.games.engine.datamanagers;

import android.view.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class KeyInputManager extends Thread {
    private static final int THREAD_WAIT_TIME = 30;
    private final Stack<KeyEvent> eventsBuffer = new Stack<>();
    private final List<KeyEventListener> listeners = new ArrayList<>();
    private final KeyEvent[] keysCache = new KeyEvent[210];
    private static KeyInputManager instance = new KeyInputManager();

    private KeyInputManager() {
        start();
    }

    public static KeyInputManager getInstance() {
        return instance;
    }


    public void registerKeyEvent(KeyEvent event) {
        synchronized (eventsBuffer) {
            // TODO ? alternative zu synchronized, evtl schneller: http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/CopyOnWriteArrayList.html
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
                        default:
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
