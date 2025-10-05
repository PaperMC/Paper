package ca.spottedleaf.moonrise.common.time;


/**
 * A Schedule is an object that can be used to maintain a periodic schedule for an event of interest.
 */
public final class Schedule {

    private long lastPeriod;

    /**
     * Initialises a schedule with the provided period.
     * @param firstPeriod The last time an event of interest occurred.
     * @see #setLastPeriod(long)
     */
    public Schedule(final long firstPeriod) {
        this.lastPeriod = firstPeriod;
    }

    /**
     * Updates the last period to the specified value. This call sets the last "time" the event
     * of interest took place at. Thus, the value returned by {@link #getDeadline(long)} is
     * the provided time plus the period length provided to {@code getDeadline}.
     * @param value The value to set the last period to.
     */
    public void setLastPeriod(final long value) {
        this.lastPeriod = value;
    }

    /**
     * Returns the last time the event of interest should have taken place.
     */
    public long getLastPeriod() {
        return this.lastPeriod;
    }

    /**
     * Returns the number of times the event of interest should have taken place between the last
     * period and the provided time given the period between each event.
     * @param periodLength The length of the period between events in ns.
     * @param time The provided time.
     */
    public long getPeriodsAhead(final long periodLength, final long time) {
        final long difference = time - this.lastPeriod;
        final long ret = Math.abs(difference) / periodLength;
        return difference >= 0 ? ret : -ret;
    }

    /**
     * Returns the next starting deadline for the event of interest to take place,
     * given the provided period length.
     * @param periodLength The provided period length.
     */
    public long getDeadline(final long periodLength) {
        return this.lastPeriod + periodLength;
    }

    /**
     * Adjusts the last period so that the next starting deadline returned is the next period specified,
     * given the provided period length.
     * @param nextPeriod The specified next starting deadline.
     * @param periodLength The specified period length.
     */
    public void setNextPeriod(final long nextPeriod, final long periodLength) {
        this.lastPeriod = nextPeriod - periodLength;
    }

    /**
     * Increases the last period by the specified number of periods and period length.
     * The specified number of periods may be < 0, in which case the last period
     * will decrease.
     * @param periods The specified number of periods.
     * @param periodLength The specified period length.
     */
    public void advanceBy(final long periods, final long periodLength) {
        this.lastPeriod += periods * periodLength;
    }

    /**
     * Sets the last period so that it is the specified number of periods ahead
     * given the specified time and period length.
     * @param periodsToBeAhead Specified number of periods to be ahead by.
     * @param periodLength The specified period length.
     * @param time The specified time.
     */
    public void setPeriodsAhead(final long periodsToBeAhead, final long periodLength, final long time) {
        final long periodsAhead = this.getPeriodsAhead(periodLength, time);
        final long periodsToAdd = periodsToBeAhead - periodsAhead;

        this.lastPeriod -= periodsToAdd * periodLength;
    }
}
