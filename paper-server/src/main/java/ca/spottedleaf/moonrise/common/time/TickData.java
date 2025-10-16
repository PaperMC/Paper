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

    private static record TickInformation(
        long differenceFromLastTick,
        long tickTime,
        long tickTimeCPU
    ) {}

    public Double getTPSAverage(final TickTime inProgress, final long tickInterval) {
        return this.getTPSAverage(inProgress, tickInterval, false);
    }

    public Double getTPSAverage(final TickTime inProgress, final long tickInterval, final boolean createFakeTick) {
        if (this.timeData.isEmpty() && inProgress == null) {
            return null;
        }

        final List<TickTime> allData = new ArrayList<>(this.timeData);
        if (inProgress != null) {
            allData.add(inProgress);
        }

        final List<TickInformation> collapsedData = collapseData(allData, tickInterval, createFakeTick);
        if (collapsedData.isEmpty()) {
            return null;
        }

        long totalTimeBetweenTicks = 0L;
        int collectedTicks = 0;
        for (final TickInformation time : collapsedData) {
            totalTimeBetweenTicks += time.differenceFromLastTick();
            ++collectedTicks;
        }
        return (double)collectedTicks / ((double)totalTimeBetweenTicks / 1.0E9);
    }

    public record MSPTData(double avg, long[] rawData) {}

    public MSPTData getMSPTData(final TickTime inProgress, final long tickInterval) {
        return this.getMSPTData(inProgress, tickInterval, false);
    }

    public MSPTData getMSPTData(final TickTime inProgress, final long tickInterval, final boolean createFakeTick) {
        if (this.timeData.isEmpty() && inProgress == null) {
            return null;
        }

        final List<TickTime> allData = new ArrayList<>(this.timeData);
        if (inProgress != null) {
            allData.add(inProgress);
        }

        final List<TickInformation> collapsedData = collapseData(allData, tickInterval, createFakeTick);
        if (collapsedData.isEmpty()) {
            return null;
        }

        long totalTimeTicking = 0L;
        int collectedTicks = 0;
        final long[] timePerTickDataRaw = new long[collapsedData.size()];
        for (int i = 0; i < collapsedData.size(); ++i) {
            final TickInformation time = collapsedData.get(i);
            totalTimeTicking += time.tickTime();
            timePerTickDataRaw[i] = time.tickTime();
            ++collectedTicks;
        }
        return new MSPTData((double)totalTimeTicking / (double)collectedTicks * 1.0E-6, timePerTickDataRaw);
    }

    public TickReportData generateTickReport(final TickTime inProgress, final long endTime, final long tickInterval) {
        return this.generateTickReport(inProgress, endTime, tickInterval, false);
    }

    // rets null if there is no data
    public TickReportData generateTickReport(final TickTime inProgress, final long endTime, final long tickInterval, final boolean createFakeTick) {
        if (this.timeData.isEmpty() && inProgress == null) {
            return null;
        }

        final List<TickTime> allData = new ArrayList<>(this.timeData);
        if (inProgress != null) {
            allData.add(inProgress);
        }

        final List<TickInformation> collapsedData = collapseData(allData, tickInterval, createFakeTick);
        if (collapsedData.isEmpty()) {
            return null;
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

        final int collectedTicks = collapsedData.size();
        final long[] tickStartToStartDifferences = new long[collectedTicks];
        final long[] timePerTickDataRaw = new long[collectedTicks];
        final long[] missingCPUTimeDataRaw = new long[collectedTicks];

        long totalTimeTicking = 0L;

        int i = 0;
        for (final TickInformation time : collapsedData) {
            tickStartToStartDifferences[i] = time.differenceFromLastTick();
            final long timePerTick = timePerTickDataRaw[i] = time.tickTime();
            missingCPUTimeDataRaw[i] = Math.max(0L, timePerTick - time.tickTimeCPU());

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

    private static List<TickInformation> collapseData(final List<TickTime> allData, final long tickInterval, final boolean createFakeTick) {
        // we only care about ticks, but because of inbetween tick task execution
        // there will be data in allData that isn't ticks. But, that data cannot
        // be ignored since it contributes to utilisation.
        // So, we will "compact" the data by merging any inbetween tick times
        // the next tick.
        // If there is no "next tick", then we will create one.
        final List<TickInformation> collapsedData = new ArrayList<>();
        for (int i = 0, len = allData.size(); i < len; ++i) {
            final List<TickTime> toCollapse = new ArrayList<>();
            TickTime lastTick = null;
            for (;i < len; ++i) {
                final TickTime time = allData.get(i);
                if (!time.isTickExecution()) {
                    toCollapse.add(time);
                    continue;
                }
                lastTick = time;
                break;
            }

            if (toCollapse.isEmpty()) {
                // nothing to collapse
                final TickTime last = allData.get(i);
                collapsedData.add(
                    new TickInformation(
                        last.differenceFromLastTick(tickInterval),
                        last.tickLength(),
                        last.supportCPUTime() ? last.tickCpuTime() : 0L
                    )
                );
            } else {
                long totalTickTime = 0L;
                long totalCpuTime = 0L;
                for (int k = 0, len2 = toCollapse.size(); k < len2; ++k) {
                    final TickTime time = toCollapse.get(k);
                    totalTickTime += time.tickLength();
                    totalCpuTime += time.supportCPUTime() ? time.tickCpuTime() : 0L;
                }
                if (i < len) {
                    // we know there is a tick to collapse into
                    final TickTime last = allData.get(i);
                    collapsedData.add(
                        new TickInformation(
                            last.differenceFromLastTick(tickInterval),
                            last.tickLength() + totalTickTime,
                            (last.supportCPUTime() ? last.tickCpuTime() : 0L) + totalCpuTime
                        )
                    );
                } else if (createFakeTick) {
                    // we do not have a tick to collapse into, so we must make one up
                    // we will assume that the tick is "starting now" and ongoing

                    // compute difference between imaginary tick and last tick
                    final long differenceBetweenTicks;
                    if (lastTick != null) {
                        // we have a last tick, use it
                        differenceBetweenTicks = lastTick.tickStart();
                    } else {
                        // we don't have a last tick, so we must make one up that makes sense
                        // if the current interval exceeds the max tick time, then use it

                        // Otherwise use the interval length.
                        // This is how differenceFromLastTick() works on TickTime when there is no previous interval.
                        differenceBetweenTicks = Math.max(
                            tickInterval, totalTickTime
                        );
                    }

                    collapsedData.add(
                        new TickInformation(
                            differenceBetweenTicks,
                            totalTickTime,
                            totalCpuTime
                        )
                    );
                }
            }
        }
        return collapsedData;
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
