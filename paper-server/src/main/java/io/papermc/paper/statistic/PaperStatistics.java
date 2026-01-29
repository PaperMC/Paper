package io.papermc.paper.statistic;

import com.google.common.base.Preconditions;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

public final class PaperStatistics {

    public static final Set<Identifier> IGNORED_STATS_FOR_EVENT = Util.make(new HashSet<>(), set -> {
        set.add(Stats.TIME_SINCE_DEATH);
        set.add(Stats.TIME_SINCE_REST);
        set.add(Stats.CROUCH_TIME);
        set.add(Stats.TOTAL_WORLD_TIME);
        set.add(Stats.PLAY_TIME);

        BuiltInRegistries.CUSTOM_STAT.stream().forEach(key -> {
            if (key.getPath().endsWith("_one_cm")) {
                set.add(key);
            }
        });
    });

    private PaperStatistics() {
    }

    public static void changeStatistic(final ServerStatsCounter manager, final Statistic<?> statistic, final int delta, final @Nullable Player player) {
        Preconditions.checkArgument(statistic != null, "statistic cannot be null");
        final Stat<?> stat = getNMSStatistic(statistic);
        final int newAmount = (int) Math.min((long) manager.getValue(stat) + delta, Integer.MAX_VALUE); // clamp from StatsCounter
        setStatistic0(manager, stat, newAmount, player);
    }

    public static void setStatistic(final ServerStatsCounter manager, final Statistic<?> statistic, final int newAmount, final @Nullable Player player) {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        final Stat<?> stat = getNMSStatistic(statistic);
        setStatistic0(manager, stat, newAmount, player);
    }

    private static void setStatistic0(final ServerStatsCounter manager, final Stat<?> stat, final int newAmount, final @Nullable Player player) {
        Preconditions.checkArgument(newAmount >= 0, "New amount must be greater than or equal to 0");
        //noinspection ConstantConditions
        manager.setValue(player, stat, newAmount);
        if (player != null) {
            player.level().getCraftServer().getScoreboardManager().forAllObjectives(stat, player, score -> {
                score.set(newAmount);
            });
        }
    }

    public static int getStatistic(final ServerStatsCounter manager, final Statistic<?> statistic) {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        return manager.getValue(getNMSStatistic(statistic));
    }

    public static String getFormattedValue(final ServerStatsCounter manager, final Statistic<?> statistic) {
        final Stat<?> nmsStat = getNMSStatistic(statistic);
        return nmsStat.format(manager.getValue(nmsStat));
    }

    public static <M> Statistic<?> getPaperStatistic(final Stat<M> stat) {
        return PaperStatisticType.minecraftToBukkit(stat.getType()).convertStat(stat);
    }

    public static Stat<?> getNMSStatistic(final Statistic<?> statistic) {
        return ((PaperStatistic<?, ?>) statistic).handle();
    }
}
