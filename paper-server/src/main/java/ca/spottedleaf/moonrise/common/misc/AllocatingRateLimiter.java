package ca.spottedleaf.moonrise.common.misc;

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

    public AllocatingRateLimiter(final long maxGranularity) {
        this.maxGranularity = maxGranularity;
    }

    public void reset(final long time) {
        this.allocation = 0.0;
        this.lastAllocationUpdate = time;
        this.takeCarry = 0.0;
        this.lastTakeUpdate = time;
    }

    // rate in units/s, and time in ns
    public void tickAllocation(final long time, final double rate, final double maxAllocation) {
        final long diff = Math.min(this.maxGranularity, time - this.lastAllocationUpdate);
        this.lastAllocationUpdate = time;

        this.allocation = Math.min(maxAllocation - this.takeCarry, this.allocation + rate * (diff*1.0E-9D));
    }

    public long previewAllocation(final long time, final double rate, final long maxTake) {
        if (maxTake < 1L) {
            return 0L;
        }

        final long diff = Math.min(this.maxGranularity, time - this.lastTakeUpdate);

        // note: abs(takeCarry) <= 1.0
        final double take = Math.min(
            Math.min((double)maxTake - this.takeCarry, this.allocation),
            rate * (diff*1.0E-9)
        );

        return (long)Math.floor(this.takeCarry + take);
    }

    // rate in units/s, and time in ns
    public long takeAllocation(final long time, final double rate, final long maxTake) {
        if (maxTake < 1L) {
            return 0L;
        }

        double ret = this.takeCarry;
        final long diff = Math.min(this.maxGranularity, time - this.lastTakeUpdate);
        this.lastTakeUpdate = time;

        // note: abs(takeCarry) <= 1.0
        final double take = Math.min(
            Math.min((double)maxTake - this.takeCarry, this.allocation),
            rate * (diff*1.0E-9)
        );

        ret += take;
        this.allocation -= take;

        final long retInteger = (long)Math.floor(ret);
        this.takeCarry = ret - (double)retInteger;

        return retInteger;
    }
}
