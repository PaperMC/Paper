package io.papermc.paper.statistic;

import com.google.common.base.Preconditions;
import java.util.HashSet;
import java.util.Set;
import net.kyori.adventure.key.Key;
import net.minecraft.util.Util;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stat;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Registry;
import org.jspecify.annotations.Nullable;

public final class PaperStatistics {

    public static final Set<Key> IGNORED_STATS_FOR_EVENT = Util.make(new HashSet<>(), set -> {
        set.add(CustomStatistics.TIME_SINCE_DEATH.key());
        set.add(CustomStatistics.TIME_SINCE_REST.key());
        set.add(CustomStatistics.SNEAK_TIME.key());
        set.add(CustomStatistics.TOTAL_WORLD_TIME.key());
        set.add(CustomStatistics.PLAY_TIME.key());

        Registry.CUSTOM_STAT.keyStream().forEach(key -> {
            if (key.key().value().endsWith("_one_cm")) {
                set.add(key);
            }
        });
    });

    private PaperStatistics() {
    }

    public static void changeStatistic(final ServerStatsCounter manager, final Statistic<?> statistic, final int delta, final @Nullable Player player) {
        if (delta == 0) return;
        Preconditions.checkArgument(statistic != null, "statistic cannot be null");
        final Stat<?> stat = getNMSStatistic(statistic);
        final int newAmount = manager.getValue(stat) + delta;
        Preconditions.checkArgument(newAmount >= 0, "New amount must be greater than or equal to 0");
        //noinspection ConstantConditions
        manager.setValue(player, stat, newAmount);
        if (player != null) {
            player.level().getCraftServer().getScoreboardManager().forAllObjectives(stat, player, score -> {
                score.set(newAmount);
            });
        }
    }

    public static void setStatistic(final ServerStatsCounter manager, final Statistic<?> statistic, final int newAmount, final @Nullable Player player) {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(newAmount >= 0, "New amount must be greater than or equal to 0");
        final Stat<?> stat = getNMSStatistic(statistic);
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

    @SuppressWarnings("unchecked")
    public static <M> Statistic<?> getPaperStatistic(final Stat<M> nmsStat) {
        final PaperStatisticType<?, M> type = (PaperStatisticType<?, M>) PaperStatisticType.minecraftToBukkit(nmsStat.getType());
        return type.convertStat(nmsStat);
    }

    public static Stat<?> getNMSStatistic(final Statistic<?> paperStat) {
        return ((PaperStatistic<?, ?>) paperStat).handle();
    }
}
