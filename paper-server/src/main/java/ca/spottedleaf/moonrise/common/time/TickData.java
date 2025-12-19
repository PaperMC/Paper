package ca.spottedleaf.moonrise.common.time;

import ca.spottedleaf.concurrentutil.util.TimeUtil;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TickData {

    private final long interval; // ns

    private final ArrayDeque<TickTime> timeData = new ArrayDeque<>();

    public TickData(final long intervalNS) {
        this.interval = intervalNS;
    }

    public void addDataFrom(final TickTime time) {
        final long start = time.tickStart();

        TickTime first;
        while ((first = this.timeData.peekFirst()) != null) {
            // only remove data completely out of window
            if ((start - first.tickEnd()) <= this.interval) {
                break;
            }
            this.timeData.pollFirst();
        }

        this.timeData.add(time);
    }

    // fromIndex inclusive, toIndex exclusive
    // will throw if arr.length == 0
    private static double median(final long[] arr, final int fromIndex, final int toIndex) {
        final int len = toIndex - fromIndex;
        final int middle = fromIndex + (len >>> 1);
        if ((len & 1) == 0) {
            // even, average the two middle points
            return (double)(arr[middle - 1] + arr[middle]) / 2.0;
        } else {
            // odd, just grab the middle
            return (double)arr[middle];
        }
    }

    // will throw if arr.length == 0
    private static SegmentData computeSegmentData(final long[] arr, final int fromIndex, final int toIndex,
                                                  final boolean inverse) {
        final int len = toIndex - fromIndex;
        long sum = 0L;
        final double median = median(arr, fromIndex, toIndex);
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;

        for (int i = fromIndex; i < toIndex; ++i) {
            final long val = arr[i];
            sum += val;
            min = Math.min(min, val);
            max = Math.max(max, val);
        }

        if (inverse) {
            // for positive a,b we have that a >= b if and only if 1/a <= 1/b
            return new SegmentData(
                len,
                (double)len / ((double)sum / 1.0E9),
                1.0E9 / median,
                1.0E9 / (double)max,
                1.0E9 / (double)min
            );
        } else {
            return new SegmentData(
                len,
                (double)sum / (double)len,
                median,
                (double)min,
                (double)max
            );
        }
    }

    private static SegmentedAverage computeSegmentedAverage(final long[] data, final int allStart, final int allEnd,
                                                            final int percent99BestStart, final int percent99BestEnd,
                                                            final int percent95BestStart, final int percent95BestEnd,
                                                            final int percent1WorstStart, final int percent1WorstEnd,
                                                            final int percent5WorstStart, final int percent5WorstEnd,
                                                            final boolean inverse) {
        return new SegmentedAverage(
            computeSegmentData(data, allStart, allEnd, inverse),
            computeSegmentData(data, percent99BestStart, percent99BestEnd, inverse),
            computeSegmentData(data, percent95BestStart, percent95BestEnd, inverse),
            computeSegmentData(data, percent1WorstStart, percent1WorstEnd, inverse),
            computeSegmentData(data, percent5WorstStart, percent5WorstEnd, inverse),
            data
        );
    }

    public Double getTPSAverage(final TickTime inProgress, final long tickInterval) {
        if (this.timeData.isEmpty() && inProgress == null) {
            return null;
        }

        long totalTimeBetweenTicks = 0L;
        int collectedTicks = this.timeData.size();

        if (inProgress != null) {
            ++collectedTicks;
            totalTimeBetweenTicks += inProgress.differenceFromLastTick(tickInterval);
        }

        for (final TickTime time : this.timeData) {
            totalTimeBetweenTicks += time.differenceFromLastTick(tickInterval);
        }
        return Double.valueOf((double)collectedTicks / ((double)totalTimeBetweenTicks / 1.0E9));
    }

    public record MSPTData(double avg, long[] rawData) {}

    public MSPTData getMSPTData(final TickTime inProgress, final long tickInterval) {
        if (this.timeData.isEmpty() && inProgress == null) {
            return null;
        }

        long totalTimeTicking = 0L;
        final long[] timePerTickDataRaw = new long[this.timeData.size() + (inProgress != null ? 1 : 0)];
        int index = 0;
        for (TickTime time : this.timeData) {
            final long totalLength = time.tickLength() + time.intermediateTaskExecutionTime();
            totalTimeTicking += totalLength;
            timePerTickDataRaw[index++] = totalLength;
        }

        if (inProgress != null) {
            final long totalLength = inProgress.tickLength() + inProgress.intermediateTaskExecutionTime();
            totalTimeTicking += totalLength;
            timePerTickDataRaw[index++] = totalLength;
        }

        return new MSPTData((double)totalTimeTicking / (double)timePerTickDataRaw.length * 1.0E-6, timePerTickDataRaw);
    }

    // rets null if there is no data
    public TickReportData generateTickReport(final TickTime inProgress, final long endTime, final long tickInterval) {
        if (this.timeData.isEmpty() && inProgress == null) {
            return null;
        }

        final List<TickTime> allData = new ArrayList<>(this.timeData);
        if (inProgress != null) {
            allData.add(inProgress);
        }

        final long intervalStart = allData.get(0).tickStart();
        final long intervalEnd = allData.get(allData.size() - 1).tickEnd();

        // to make utilisation accurate, we need to take the total time used over the last interval period -
        // this means if a tick start before the measurement interval, but ends within the interval, then we
        // only consider the time it spent ticking inside the interval
        long totalTimeOverInterval = 0L;
        long measureStart = endTime - this.interval;

        for (int i = 0, len = allData.size(); i < len; ++i) {
            final TickTime time = allData.get(i);
            if (TimeUtil.compareTimes(time.tickStart(), measureStart) < 0) {
                final long diff = time.tickEnd() - measureStart;
                if (diff > 0L) {
                    totalTimeOverInterval += diff;
                } // else: the time is entirely out of interval
            } else {
                totalTimeOverInterval += time.tickLength();
            }
        }

        final int collectedTicks = allData.size();
        final long[] tickStartToStartDifferences = new long[collectedTicks];
        final long[] timePerTickDataRaw = new long[collectedTicks];
        final long[] missingCPUTimeDataRaw = new long[collectedTicks];

        long totalTimeTicking = 0L;

        int i = 0;
        for (final TickTime time : allData) {
            tickStartToStartDifferences[i] = time.differenceFromLastTick(tickInterval);
            final long timePerTick = timePerTickDataRaw[i] = time.tickLength() + time.intermediateTaskExecutionTime();
            if (time.supportCPUTime()) {
                missingCPUTimeDataRaw[i] = Math.max(0L, timePerTick - (time.tickCpuTime() + time.intermediateTaskExecutionTimeCPU()));
            }

            ++i;

            totalTimeTicking += timePerTick;
        }

        Arrays.sort(tickStartToStartDifferences);
        Arrays.sort(timePerTickDataRaw);
        Arrays.sort(missingCPUTimeDataRaw);

        // Note: computeSegmentData cannot take start == end
        final int allStart = 0;
        final int allEnd = collectedTicks;
        final int percent95BestStart = 0;
        final int percent95BestEnd = collectedTicks == 1 ? 1 : (int)(0.95 * collectedTicks);
        final int percent99BestStart = 0;
        // (int)(0.99 * collectedTicks) == 0 if collectedTicks = 1, so we need to use 1 to avoid start == end
        final int percent99BestEnd = collectedTicks == 1 ? 1 : (int)(0.99 * collectedTicks);
        final int percent1WorstStart = (int)(0.99 * collectedTicks);
        final int percent1WorstEnd = collectedTicks;
        final int percent5WorstStart = (int)(0.95 * collectedTicks);
        final int percent5WorstEnd = collectedTicks;

        final SegmentedAverage tpsData = computeSegmentedAverage(
            tickStartToStartDifferences,
            allStart, allEnd,
            percent99BestStart, percent99BestEnd,
            percent95BestStart, percent95BestEnd,
            percent1WorstStart, percent1WorstEnd,
            percent5WorstStart, percent5WorstEnd,
            true
        );

        final SegmentedAverage timePerTickData = computeSegmentedAverage(
            timePerTickDataRaw,
            allStart, allEnd,
            percent99BestStart, percent99BestEnd,
            percent95BestStart, percent95BestEnd,
            percent1WorstStart, percent1WorstEnd,
            percent5WorstStart, percent5WorstEnd,
            false
        );

        final SegmentedAverage missingCPUTimeData = computeSegmentedAverage(
            missingCPUTimeDataRaw,
            allStart, allEnd,
            percent99BestStart, percent99BestEnd,
            percent95BestStart, percent95BestEnd,
            percent1WorstStart, percent1WorstEnd,
            percent5WorstStart, percent5WorstEnd,
            false
        );

        final double utilisation = (double)totalTimeOverInterval / (double)this.interval;

        return new TickReportData(
            collectedTicks,
            intervalStart,
            intervalEnd,
            totalTimeTicking,
            utilisation,

            tpsData,
            timePerTickData,
            missingCPUTimeData
        );
    }

    public static final record TickReportData(
        int collectedTicks,
        long collectedTickIntervalStart,
        long collectedTickIntervalEnd,
        long totalTimeTicking,
        double utilisation,

        SegmentedAverage tpsData,
        // in ns
        SegmentedAverage timePerTickData,
        // in ns
        SegmentedAverage missingCPUTimeData
    ) {}

    public static final record SegmentedAverage(
        SegmentData segmentAll,
        SegmentData segment99PercentBest,
        SegmentData segment95PercentBest,
        SegmentData segment5PercentWorst,
        SegmentData segment1PercentWorst,
        long[] rawData
    ) {}

    public static final record SegmentData(
        int count,
        double average,
        double median,
        double least,
        double greatest
    ) {}
}
