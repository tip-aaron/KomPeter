/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.lib.helper;

import javax.swing.Timer;

public class Debouncer {
    private final int delayMillis;
    private Timer timer;

    /**
     * Creates a new Debouncer with the specified delay.
     *
     * @param delayMillis
     *            Delay in milliseconds before the action is triggered.
     */
    public Debouncer(final int delayMillis) {
        this.delayMillis = delayMillis;
    }

    /**
     * Schedules the action to run after the debounce delay. If called again before
     * the delay elapses, the timer is reset.
     *
     * @param action
     *            Action to run after the delay.
     */
    public void call(final Runnable action) {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        timer = new Timer(delayMillis, v -> {
            timer = null;
            action.run();
        });

        timer.setRepeats(false);
        timer.start();
    }

    /** Cancels any pending debounced action. */
    public void cancel() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
            timer = null;
        }
    }
}
