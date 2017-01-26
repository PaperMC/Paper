package org.spigotmc;

/**
 * Keeps track of the time spent doing main thread activities that can be spread across ticks,
 * so that such work doesn't exceed the current tick's estimated available slack time. Each
 * activity is allotted a proportion of the expected slack time according to its weight, versus the
 * estimated total weight of all activities.
 */
public class SlackActivityAccountant {
    private double prevTickSlackWeightReciprocal = 1 / MIN_SLACK_WEIGHT;
    private static final double MIN_SLACK_WEIGHT = 1 / 65536.0;
    private double averageTickNonSlackNanos = 0;
    private static final double AVERAGING_FACTOR = 0.375;

    private long currentActivityStartNanos;
    private static final long OFF = -1;
    private long currentActivityEndNanos;
    private double tickSlackWeight;
    private long tickSlackNanos;

    private double getSlackFraction(double slackWeight) {
        return Math.min(slackWeight * this.prevTickSlackWeightReciprocal, 1);
    }

    private int getEstimatedSlackNanos() {
        return (int) Math.max(net.minecraft.server.MinecraftServer.TICK_TIME - (long) this.averageTickNonSlackNanos, 0);
    }

    public void tickStarted() {
        this.currentActivityStartNanos = OFF;
        this.tickSlackWeight = 0;
        this.tickSlackNanos = 0;
    }

    public void startActivity(double slackWeight) {
        double slackFraction0 = getSlackFraction(this.tickSlackWeight);
        this.tickSlackWeight += slackWeight;
        double slackFraction1 = getSlackFraction(this.tickSlackWeight);

        long t = System.nanoTime();
        this.currentActivityStartNanos = t;
        this.currentActivityEndNanos = t + ((int) ((slackFraction1 - slackFraction0) * this.getEstimatedSlackNanos()));
    }

    private void endActivity(long endNanos) {
        this.tickSlackNanos += endNanos - this.currentActivityStartNanos;
        this.currentActivityStartNanos = OFF;
    }

    public boolean activityTimeIsExhausted() {
        if (this.currentActivityStartNanos == OFF) {
            return true;
        }

        long t = System.nanoTime();
        if (t <= this.currentActivityEndNanos) {
            return false;
        } else {
            this.endActivity(this.currentActivityEndNanos);
            return true;
        }
    }

    public void endActivity() {
        if (this.currentActivityStartNanos == OFF) {
            return;
        }

        this.endActivity(Math.min(System.nanoTime(), this.currentActivityEndNanos));
    }

    public void tickEnded(long tickNanos) {
        this.prevTickSlackWeightReciprocal = 1 / Math.max(this.tickSlackWeight, MIN_SLACK_WEIGHT);

        long tickNonSlackNanos = tickNanos - this.tickSlackNanos;
        this.averageTickNonSlackNanos = this.averageTickNonSlackNanos * (1 - AVERAGING_FACTOR) + tickNonSlackNanos * AVERAGING_FACTOR;
    }
}
