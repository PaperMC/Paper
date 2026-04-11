package ca.spottedleaf.moonrise.common.misc;

import ca.spottedleaf.concurrentutil.util.TimeUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class StaggeredRateLimiter {

    private final Limiter[] limiters;
    private double rate;

    private StaggeredRateLimiter(final Limiter[] limiters, final double rate) {
        this.limiters = Objects.requireNonNull(limiters);

        if (limiters.length == 0) {
            throw new IllegalArgumentException("Must define at least one limiter");
        }
        if (rate < 0.0) {
            throw new IllegalArgumentException("Initial rate must be >= 0.0");
        }

        for (final Limiter limiter : limiters) {
            if (limiter.rateFactor <= 0.0) {
                throw new IllegalArgumentException("Rate factor must be > 0.0");
            }
        }

        this.reset(rate);
    }

    public static StaggeredRateLimiter.Builder builder() {
        return new Builder();
    }

    public void tick(final long timeNow) {
        this.tick(timeNow, this.rate, 0.0);
    }

    public void tick(final long timeNow, final double newRate, final double allocFactor) {
        if (newRate != this.rate) {
            this.reset(newRate, allocFactor);
        }

        for (final Limiter limiter : this.limiters) {
            limiter.tick(timeNow);
        }
    }

    private long previewAllocation(final long maxTake) {
        long min = Long.MAX_VALUE;

        for (final Limiter limiter : this.limiters) {
            min = Math.min(min, limiter.limiter.previewAllocation(maxTake));
        }

        return min;
    }

    public long takeAllocation(final long maxTake) {
        final long take = this.previewAllocation(maxTake);

        for (final Limiter limiter : this.limiters) {
            // do we need to attempt take + 1 to fill carry?
            final long toTake;
            if (take == limiter.limiter.previewAllocation(take + 1)) {
                // we must attempt to take + 1 to fill carry
                toTake = take + 1;
            } else {
                // no: we are limited by another limiter
                // (i.e takeAllocation(take + 1) == take + 1
                toTake = take;
            }

            if (take != limiter.limiter.takeAllocation(toTake)) {
                throw new IllegalStateException();
            }
        }

        return take;
    }

    public void returnUnused(final long unused) {
        for (final Limiter limiter : this.limiters) {
            limiter.limiter.returnUnused(unused);
        }
    }

    public void reset(final double allocFactor) {
        this.reset(this.rate, allocFactor);
    }

    public void reset(final double rate, final double allocFactor) {
        this.rate = rate;

        for (final Limiter limiter : this.limiters) {
            limiter.adjustRate(rate, allocFactor);
        }
    }

    private static record Limiter(double rateFactor, AllocatingRateLimiter limiter) {
        public void tick(final long timeNow) {
            this.limiter.tickAllocation(timeNow);
        }

        public void adjustRate(final double rate, final double allocFactor) {
            final double adjustedRate = this.rateFactor * rate;

            final double allocation = allocFactor * AllocatingRateLimiter.getMaxAllocation(adjustedRate, this.limiter.getIntervalNS());

            this.limiter.reset(
                adjustedRate, this.limiter.getIntervalNS(),
                allocation, this.limiter.getLastAllocationUpdate()
            );
        }
    }

    public static final class Builder {

        private Builder() {}

        private final long timeNow = System.nanoTime();
        private List<Limiter> limiters = new ArrayList<>();
        private double initialRate;

        public Builder setRate(final double rate) {
            this.initialRate = rate;
            return this;
        }

        public Builder add(final long interval, final TimeUnit intervalUnit,
                           final double rateFactor) {
            this.limiters.add(
                new Limiter(
                    rateFactor,
                    new AllocatingRateLimiter(0.0, intervalUnit.toNanos(interval), 0.0, this.timeNow)
                )
            );
            return this;
        }

        public StaggeredRateLimiter build() {
            // want lower intervals first
            this.limiters.sort((final Limiter l1, final Limiter l2) -> {
                return TimeUtil.compareTimes(l1.limiter.getIntervalNS(), l2.limiter.getIntervalNS());
            });

            return new StaggeredRateLimiter(this.limiters.toArray(new Limiter[0]), this.initialRate);
        }
    }
}
