package org.spigotmc;

public class TickLimiter {

    private final int maxTime;
    private long startTime;

    public TickLimiter(int maxtime) {
        this.maxTime = maxtime;
    }

    public void initTick() {
        this.startTime = System.currentTimeMillis();
    }

    public boolean shouldContinue() {
        long remaining = System.currentTimeMillis() - this.startTime;
        return remaining < this.maxTime;
    }
}
