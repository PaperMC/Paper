package io.papermc.paper.statistic;

import net.minecraft.stats.Stat;

public record PaperStatistic<S, M>(Stat<M> handle, S value, M nmsValue, StatisticType<S> type) implements Statistic<S> {

    @Override
    public String getName() {
        return Stat.buildName(this.handle.getType(),this.nmsValue);
    }
}
