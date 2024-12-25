package org.bukkit.craftbukkit.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CraftSyncDailyTask extends CraftTask {

    private final LocalTime scheduleTime;
    private final ZoneId zoneId;
    private ZonedDateTime nextRun;

    public CraftSyncDailyTask(Plugin plugin, Object task, int id, LocalTime scheduleTime) {
        super(plugin, task, id, -1);
        this.scheduleTime = scheduleTime;
        this.zoneId = ZoneId.systemDefault();
        this.nextRun = calculateNextRun(scheduleTime, zoneId);
    }

    @Override
    public void run() {
        try {
            super.run();
        } finally {
            // After running, calculate the next run time
            ZonedDateTime now = ZonedDateTime.now(zoneId);
            ZonedDateTime next = calculateNextRun(scheduleTime, zoneId);
            nextRun = next;

            setNextRun(Bukkit.getCurrentTick() + calculateDelayTicks(now, next));
        }
    }

    static ZonedDateTime calculateNextRun(LocalTime scheduleTime, ZoneId zoneId) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime next = now.with(scheduleTime);

        // If time already passed today, schedule to tomorrow
        if (!now.toLocalTime().isBefore(scheduleTime)) {
            next = next.plusDays(1);
        }

        // Ensure we're using the correct offset for the target time
        return next.withZoneSameInstant(zoneId);
    }

    static long calculateDelayTicks(ZonedDateTime from, ZonedDateTime to) {
        return Math.max(0, Duration.between(from, to).toMillis() / CraftScheduler.MILLISECONDS_PER_TICK);
    }

}
