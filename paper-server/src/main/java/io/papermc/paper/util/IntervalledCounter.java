package io.papermc.paper.util;

public final class IntervalledCounter {

    private static final int INITIAL_SIZE = 8;

    protected long[] times;
    protected long[] counts;
    protected final long interval;
    protected long minTime;
    protected long sum;
    protected int head; // inclusive
    protected int tail; // exclusive

    public IntervalledCounter(final long interval) {
        this.times = new long[INITIAL_SIZE];
        this.counts = new long[INITIAL_SIZE];
        this.interval = interval;
    }

    public void updateCurrentTime() {
        this.updateCurrentTime(System.nanoTime());
    }

    public void updateCurrentTime(final long currentTime) {
        long sum = this.sum;
        int head = this.head;
        final int tail = this.tail;
        final long minTime = currentTime - this.interval;

        final int arrayLen = this.times.length;

        // guard against overflow by using subtraction
        while (head != tail && this.times[head] - minTime < 0) {
            sum -= this.counts[head];
            // there are two ways we can do this:
            // 1. free the count when adding
            // 2. free it now
            // option #2
            this.counts[head] = 0;
            if (++head >= arrayLen) {
                head = 0;
            }
        }

        this.sum = sum;
        this.head = head;
        this.minTime = minTime;
    }

    public void addTime(final long currTime) {
        this.addTime(currTime, 1L);
    }

    public void addTime(final long currTime, final long count) {
        // guard against overflow by using subtraction
        if (currTime - this.minTime < 0) {
            return;
        }
        int nextTail = (this.tail + 1) % this.times.length;
        if (nextTail == this.head) {
            this.resize();
            nextTail = (this.tail + 1) % this.times.length;
        }

        this.times[this.tail] = currTime;
        this.counts[this.tail] += count;
        this.sum += count;
        this.tail = nextTail;
    }

    public void updateAndAdd(final long count) {
        final long currTime = System.nanoTime();
        this.updateCurrentTime(currTime);
        this.addTime(currTime, count);
    }

    public void updateAndAdd(final long count, final long currTime) {
        this.updateCurrentTime(currTime);
        this.addTime(currTime, count);
    }

    private void resize() {
        final int oldLength = this.times.length;
        final int newLength = Math.min(Integer.MAX_VALUE >>> 1, oldLength << 1); // Safe doubling with overflow check

        if (newLength <= oldLength) {
            throw new OutOfMemoryError("Cannot resize array beyond maximum size");
        }

        final long[] newTimes = new long[newLength];
        final long[] newCounts = new long[newLength];

        final int head = this.head;
        final int tail = this.tail;
        final int size = tail >= head ? (tail - head) : (tail + (oldLength - head));

        if (tail >= head) {
            // Sequential case: [head, tail)
            System.arraycopy(this.times, head, newTimes, 0, size);
            System.arraycopy(this.counts, head, newCounts, 0, size);
        } else {
            // Wrapped case: [head, oldLength) + [0, tail)
            int firstPartLength = oldLength - head;
            System.arraycopy(this.times, head, newTimes, 0, firstPartLength);
            System.arraycopy(this.times, 0, newTimes, firstPartLength, tail);

            System.arraycopy(this.counts, head, newCounts, 0, firstPartLength);
            System.arraycopy(this.counts, 0, newCounts, firstPartLength, tail);
        }

        this.times = newTimes;
        this.counts = newCounts;
        this.head = 0;
        this.tail = size;
    }

    // returns in units per second
    public double getRate() {
        return (double)this.sum / ((double)this.interval * 1.0E-9);
    }

    public long getInterval() {
        return this.interval;
    }

    public long getSum() {
        return this.sum;
    }

    public int totalDataPoints() {
        return this.tail >= this.head ? (this.tail - this.head) : (this.tail + (this.counts.length - this.head));
    }
}
