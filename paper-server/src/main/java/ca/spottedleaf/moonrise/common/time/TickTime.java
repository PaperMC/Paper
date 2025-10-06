package ca.spottedleaf.moonrise.common.time;

import ca.spottedleaf.concurrentutil.scheduler.SchedulerThreadPool;

// All time units are in nanoseconds.
public final record TickTime(
    long previousTickStart,
    long scheduledTickStart,
    long tickStart,
    long tickStartCPU,
    long tickEnd,
    long tickEndCPU,
    boolean supportCPUTime,
    boolean isTickExecution
) {
    /**
     * The difference between the start tick time and the scheduled start tick time. This value is
     * < 0 if the tick started before the scheduled tick time.
     * Only valid when {@link #isTickExecution()} is {@code true}.
     */
    public final long startOvershoot() {
        return this.tickStart - this.scheduledTickStart;
    }

    /**
     * The difference from the end tick time and the start tick time. Always >= 0 (unless nanoTime is just wrong).
     */
    public final long tickLength() {
        return this.tickEnd - this.tickStart;
    }

    /**
     * The total CPU time from the start tick time to the end tick time. Generally should be equal to the tickLength,
     * unless there is CPU starvation or the tick thread was blocked by I/O or other tasks. Returns Long.MIN_VALUE
     * if CPU time measurement is not supported.
     */
    public final long tickCpuTime() {
        if (!this.supportCPUTime()) {
            return Long.MIN_VALUE;
        }
        return this.tickEndCPU - this.tickStartCPU;
    }

    /**
     * The difference in time from the start of the last tick to the start of the current tick. If there is no
     * last tick, then this value is max(tickInterval, tickLength).
     * @param tickInterval The expected interval between ticks.
     * Only valid when {@link #isTickExecution()} is {@code true}.
     */
    public final long differenceFromLastTick(final long tickInterval) {
        if (this.hasLastTick()) {
            return this.tickStart - this.previousTickStart;
        }
        return Math.max(tickInterval, this.tickLength());
    }

    /**
     * Returns whether there was a tick that occurred before this one.
     * Only valid when {@link #isTickExecution()} is {@code true}.
     */
    public boolean hasLastTick() {
        return this.previousTickStart != SchedulerThreadPool.DEADLINE_NOT_SET;
    }

    /*
     * Remember, this is the expected behavior of the following:
     *
     * MSPT: Time per tick. This does not include overshoot time, just the tickLength().
     *
     * TPS: The number of ticks per second. It should be ticks / (sum of differenceFromLastTick).
     */
}
