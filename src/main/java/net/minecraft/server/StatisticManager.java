package net.minecraft.server;
 
import java.util.HashMap;
import java.util.Map;
 
public class StatisticManager {

    protected final Map a = new HashMap();

    public StatisticManager() {
    }

    public boolean a(Achievement achievement) {
        return this.a((Statistic) achievement) > 0;
    }

    public boolean b(Achievement achievement) {
        return achievement.c == null || this.a(achievement.c);
    }

    public void b(EntityHuman entityhuman, Statistic statistic, int i) {
        if (!statistic.d() || this.b((Achievement) statistic)) {
            // CraftBukkit start
            org.bukkit.event.Cancellable cancellable = org.bukkit.craftbukkit.event.CraftEventFactory.handleStatisticsIncrease(entityhuman, statistic, a(statistic), i);
            if (cancellable != null && cancellable.isCancelled()) {
                return;
            }
            // CraftBukkit end
            this.a(entityhuman, statistic, this.a(statistic) + i);
        }
    }

    public void a(EntityHuman entityhuman, Statistic statistic, int i) {
        StatisticWrapper statisticwrapper = (StatisticWrapper) this.a.get(statistic);

        if (statisticwrapper == null) {
            statisticwrapper = new StatisticWrapper();
            this.a.put(statistic, statisticwrapper);
        }

        statisticwrapper.a(i);
    }

    public int a(Statistic statistic) {
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
