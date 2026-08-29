package io.papermc.paper.statistic;

import net.minecraft.stats.Stat;
import org.bukkit.craftbukkit.scoreboard.CraftScoreboardTranslations;
import org.bukkit.scoreboard.RenderType;

public record PaperStatistic<S, M>(Stat<M> handle, S owner, StatisticType<S> type) implements Statistic<S> {

    @Override
    public String getName() {
        return this.handle.getName();
    }

    @Override
    public boolean isReadOnly() {
        return this.handle.isReadOnly();
    }

    @Override
    public RenderType getDefaultRenderType() {
        return CraftScoreboardTranslations.toBukkitRender(this.handle.getDefaultRenderType());
    }

    public static <M> Statistic<?> getPaperStatistic(final Stat<M> stat) {
        return PaperStatisticType.minecraftToBukkit(stat.getType()).convertStat(stat);
    }

    public static Stat<?> getNMSStatistic(final Statistic<?> statistic) {
        return ((PaperStatistic<?, ?>) statistic).handle();
    }
}
