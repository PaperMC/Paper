package net.minecraft.server;

import java.util.Map;

import net.minecraft.util.com.google.common.collect.Maps;

public class StatisticManager {

    protected final Map a = Maps.newConcurrentMap();

    public StatisticManager() {
    }

    public boolean a(Achievement achievement) {
        return this.getStatisticValue((Statistic) achievement) > 0;
    }

    public boolean b(Achievement achievement) {
        return achievement.c == null || this.a(achievement.c);
    }

    public void b(EntityHuman entityhuman, Statistic statistic, int i) {
        if (!statistic.d() || this.b((Achievement) statistic)) {
            // CraftBukkit start - fire Statistic events
            org.bukkit.event.Cancellable cancellable = org.bukkit.craftbukkit.event.CraftEventFactory.handleStatisticsIncrease(entityhuman, statistic, this.getStatisticValue(statistic), i);
            if (cancellable != null && cancellable.isCancelled()) {
                return;
            }
            // CraftBukkit end
            this.setStatistic(entityhuman, statistic, this.getStatisticValue(statistic) + i);
        }
    }

    public void setStatistic(EntityHuman entityhuman, Statistic statistic, int i) {
        StatisticWrapper statisticwrapper = (StatisticWrapper) this.a.get(statistic);

        if (statisticwrapper == null) {
            statisticwrapper = new StatisticWrapper();
            this.a.put(statistic, statisticwrapper);
        }

        statisticwrapper.a(i);
    }

    public int getStatisticValue(Statistic statistic) {
        StatisticWrapper statisticwrapper = (StatisticWrapper) this.a.get(statistic);

        return statisticwrapper == null ? 0 : statisticwrapper.a();
    }

    public IJsonStatistic b(Statistic statistic) {
        StatisticWrapper statisticwrapper = (StatisticWrapper) this.a.get(statistic);

        return statisticwrapper != null ? statisticwrapper.b() : null;
    }

    public IJsonStatistic a(Statistic statistic, IJsonStatistic ijsonstatistic) {
        StatisticWrapper statisticwrapper = (StatisticWrapper) this.a.get(statistic);

        if (statisticwrapper == null) {
            statisticwrapper = new StatisticWrapper();
            this.a.put(statistic, statisticwrapper);
        }

        statisticwrapper.a(ijsonstatistic);
        return ijsonstatistic;
    }
}
