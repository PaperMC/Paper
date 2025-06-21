package io.papermc.paper.statistic;

import net.minecraft.stats.Stat;
import org.bukkit.craftbukkit.scoreboard.CraftScoreboardTranslations;
import org.bukkit.scoreboard.RenderType;

public record PaperStatistic<S, M>(Stat<M> handle, S owner, M nmsValue, StatisticType<S> type) implements Statistic<S> {

    @Override
    public String getName() {
        return Stat.buildName(this.handle.getType(),this.nmsValue);
    }

    @Override
    public boolean isReadOnly() {
        return this.handle.isReadOnly();
    }

    @Override
    public RenderType getDefaultRenderType() {
        return CraftScoreboardTranslations.toBukkitRender(this.handle.getDefaultRenderType());
    }
}
