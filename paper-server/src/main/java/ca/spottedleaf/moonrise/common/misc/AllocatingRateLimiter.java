package ca.spottedleaf.moonrise.common.misc;

final class AllocatingRateLimiter {

    private double rate;
    private long intervalNS;
    private double maxAllocation;

    // the allocation that has been taken
    private double takeCarry = 0.0;
    // the allocation that has yet to be taken
    private double allocation = 0.0;
    private long lastAllocationUpdate;

    // maxGranularity in nanoseconds, rate in units/second, maxAllocation in units, allocation in units, time in nanoseconds
    public AllocatingRateLimiter(final double rate, final long intervalNS,
                          final double allocation, final long time) {
        this.reset(rate, intervalNS, allocation, time);
    }

    public double getRate() {
        return this.rate;
    }

    public long getIntervalNS() {
        return this.intervalNS;
    }

    public double getAllocation() {
        return this.allocation;
    }

    public long getLastAllocationUpdate() {
        return this.lastAllocationUpdate;
    }

    public static double getMaxAllocation(final double rate, final long intervalNS) {
        return rate * ((double)intervalNS / 1.0E9);
    }

    // maxGranularity in nanoseconds, rate in units/second, maxAllocation in units, allocation in units, time in nanoseconds
    public void reset(final double rate, final long intervalNS, final double allocation, final long time) {
        this.rate = rate;
        this.intervalNS = intervalNS;
        this.maxAllocation = getMaxAllocation(rate, intervalNS);

        this.takeCarry = 0.0;
        this.allocation = Math.min(allocation, this.maxAllocation);
        this.lastAllocationUpdate = time;
    }

    // time in ns
    public void tickAllocation(final long time) {
        final long diff = Math.max(0L, time - this.lastAllocationUpdate);
        this.lastAllocationUpdate = time;

        this.allocation = Math.min(this.maxAllocation, this.allocation + this.rate * ((double)diff / 1.0E9));
    }

    public long previewAllocation(final long maxTake) {
        if (maxTake < 0L) {
            return 0L;
        }
        return (long)Math.floor(
                Math.min((double)maxTake, this.allocation + this.takeCarry)
        );
    }

    public long takeAllocation(final long maxTake) {
        if (maxTake < 0L) {
            return 0L;
        }

        // start with the amount we have already taken
        double ret = this.takeCarry;

        // attempt to subtract the rest from the current allocation
        final double takeFromAllocation  = Math.min((double)maxTake - ret, this.allocation);
        this.allocation -= takeFromAllocation;
        ret += takeFromAllocation;

        // leave the fraction we could not take for next call
        final long retInteger = (long)Math.floor(ret);
        this.takeCarry = ret - (double)retInteger;

        return retInteger;
    }

    public void returnUnused(final long unused) {
        if (unused <= 0L) {
            return;
        }
        // note: expect allocation + take carry + value < maxAllocation + 1
        final double newAllocation = Math.min(this.maxAllocation + 1.0, (this.allocation + this.takeCarry) + (double)unused);
        this.allocation = Math.min(newAllocation - 1.0, this.maxAllocation); // allocation > 0.0 as unused >= 1
        this.takeCarry = 0.9999999999999; // close enough really...
    }

    @Override
    public String toString() {
        return "AllocatingRateLimiter{" +
            "rate=" + this.rate +
            ", intervalNS=" + this.intervalNS +
            ", maxAllocation=" + this.maxAllocation +
            ", takeCarry=" + this.takeCarry +
            ", allocation=" + this.allocation +
            ", lastAllocationUpdate=" + this.lastAllocationUpdate +
            '}';
    }
}
