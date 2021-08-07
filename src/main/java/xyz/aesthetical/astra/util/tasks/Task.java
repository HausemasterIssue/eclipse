package xyz.aesthetical.astra.util.tasks;

import xyz.aesthetical.astra.util.Timer;

import java.util.concurrent.TimeUnit;

public class Task {
    private final String name;
    private final Timer timer = new Timer().reset();
    private final Runnable runnable;

    private final int time;
    private final TimeUnit unit;

    public Task(int time, TimeUnit unit, Runnable runnable) {
        this("Task", time, unit, runnable);
    }

    public Task(String name, int time, TimeUnit unit, Runnable runnable) {
        this.name = name;
        this.runnable = runnable;
        this.time = time;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public Timer getTimer() {
        return timer;
    }

    public boolean passed() {
        return timer.passedMs(unit.toMillis(time));
    }

    public void run() {
        this.runnable.run();
    }
}
