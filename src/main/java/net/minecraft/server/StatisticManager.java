package net.minecraft.server;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class StatisticManager {

    protected final Object2IntMap<Statistic<?>> a = Object2IntMaps.synchronize(new Object2IntOpenHashMap());

    public StatisticManager() {
        this.a.defaultReturnValue(0);
    }

    public void b(EntityHuman entityhuman, Statistic<?> statistic, int i) {
        int j = (int) Math.min((long) this.getStatisticValue(statistic) + (long) i, 2147483647L);

        this.setStatistic(entityhuman, statistic, j);
    }

    public void setStatistic(EntityHuman entityhuman, Statistic<?> statistic, int i) {
        this.a.put(statistic, i);
    }

    public int getStatisticValue(Statistic<?> statistic) {
        return this.a.getInt(statistic);
    }
}
