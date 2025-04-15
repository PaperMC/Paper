package ca.spottedleaf.moonrise.common.misc;

/**
 * Rate limiter that allocates and controls resource consumption over time.
 * Uses a token bucket algorithm to manage allocated resources with time-based replenishment.
 */
public final class AllocatingRateLimiter {

    // max difference granularity in ns
    private final long maxGranularity;

    private double allocation = 0.0;
    private long lastAllocationUpdate;
    // the carry is used to store the remainder of the last take, so that the take amount remains the same (minus floating point error)
    // over any time period using take regardless of the number of take calls or the intervals between the take calls
    // i.e. take obtains 3.5 elements, stores 0.5 to this field for the next take() call to use and returns 3
    private double takeCarry = 0.0;
    private long lastTakeUpdate;

    /**
     * Creates a new rate limiter with specified maximum time granularity.
     *
     * @param maxGranularity Maximum time difference to consider in nanoseconds
     */
    public AllocatingRateLimiter(final long maxGranularity) {
        this.maxGranularity = maxGranularity;
    }

    /**
     * Resets the rate limiter state to initial values.
     *
     * @param time Current time in nanoseconds
     */
    public void reset(final long time) {
        this.allocation = 0.0;
        this.lastAllocationUpdate = time;
        this.takeCarry = 0.0;
        this.lastTakeUpdate = time;
    }

    /**
     * Updates the allocation based on elapsed time and rate.
     * Skips updates for very small or negative time differences to prevent numerical instability.
     *
     * @param time Current time in nanoseconds
     * @param rate Rate of allocation replenishment in units per second
     * @param maxAllocation Maximum allocation allowed
     */
    public void tickAllocation(final long time, final double rate, final double maxAllocation) {
        final long timeDiff = time - this.lastAllocationUpdate;

        // Skip updates for very small or negative time differences to prevent numerical instability
        if (timeDiff <= 0) {
            return;
        }

        final long diff = Math.min(this.maxGranularity, timeDiff);
        this.lastAllocationUpdate = time;

        this.allocation = Math.min(maxAllocation - this.takeCarry, this.allocation + rate * (diff * 1.0E-9D));
    }

    /**
     * Estimates how much allocation can be taken without actually taking it.
     *
     * @param time Current time in nanoseconds
     * @param rate Rate of allocation replenishment in units per second
     * @param maxTake Maximum amount to take
     * @return Estimated amount that could be taken
     */
    public long previewAllocation(final long time, final double rate, final long maxTake) {
        if (maxTake < 1L) {
            return 0L;
        }

        final long timeDiff = time - this.lastTakeUpdate;
        if (timeDiff <= 0) {
            // For negative or zero time differences, only use existing allocation
            return (long)Math.floor(this.takeCarry + Math.min(Math.min((double)maxTake - this.takeCarry, this.allocation), 0.0));
        }

        final long diff = Math.min(this.maxGranularity, timeDiff);

        // note: abs(takeCarry) <= 1.0
        final double take = Math.min(
            Math.min((double)maxTake - this.takeCarry, this.allocation),
            rate * (diff * 1.0E-9)
        );

        return (long)Math.floor(this.takeCarry + take);
    }

    /**
     * Takes allocation based on elapsed time and rate.
     *
     * @param time Current time in nanoseconds
     * @param rate Rate of allocation replenishment in units per second
     * @param maxTake Maximum amount to take
     * @return Amount actually taken, as a whole number
     */
    public long takeAllocation(final long time, final double rate, final long maxTake) {
        if (maxTake < 1L) {
            return 0L;
        }

        double ret = this.takeCarry;
        final long timeDiff = time - this.lastTakeUpdate;

        // For negative time differences, only use existing allocation without time-based replenishment
        final long diff = timeDiff <= 0 ? 0 : Math.min(this.maxGranularity, timeDiff);
        this.lastTakeUpdate = time;

        // note: abs(takeCarry) <= 1.0
        final double take = Math.min(
            Math.min((double)maxTake - this.takeCarry, this.allocation),
            rate * (diff * 1.0E-9)
        );

        ret += take;
        this.allocation -= take;

        final long retInteger = (long)Math.floor(ret);
        this.takeCarry = ret - (double)retInteger;

        return retInteger;
    }
}
