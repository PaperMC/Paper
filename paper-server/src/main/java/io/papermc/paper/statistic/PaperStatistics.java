package io.papermc.paper.statistic;

import com.google.common.base.Preconditions;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stat;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;

public final class PaperStatistics {

    public static final Set<NamespacedKey> IGNORED_STATS_FOR_EVENT = Util.make(new HashSet<>(), set -> {
        set.add(CustomStatistic.TIME_SINCE_DEATH.getKey());
        set.add(CustomStatistic.TIME_SINCE_REST.getKey());
        set.add(CustomStatistic.SNEAK_TIME.getKey());
        set.add(CustomStatistic.TOTAL_WORLD_TIME.getKey());
        set.add(CustomStatistic.PLAY_TIME.getKey());

        Registry.CUSTOM_STAT.keyStream().forEach(key -> {
            if (key.getKey().endsWith("_one_cm")) {
                set.add(key);
            }
        });
    });

    private PaperStatistics() {
    }

    public static void changeStatistic(final ServerStatsCounter manager, final Statistic<?> statistic, final int delta) {
        if (delta == 0) return;
        Preconditions.checkNotNull(statistic, "statistic cannot be null");
        final Stat<?> stat = getNMSStatistic(statistic);
        //noinspection ConstantConditions
        manager.setValue(null, stat, manager.getValue(stat) + delta);
    }

    public static void setStatistic(final ServerStatsCounter manager, final Statistic<?> statistic, final int newAmount) {
        Preconditions.checkNotNull(statistic, "Statistic cannot be null");
        Preconditions.checkArgument(newAmount >= 0, "New amount must be greater than or equal to 0");
        //noinspection ConstantConditions
        manager.setValue(null, getNMSStatistic(statistic), newAmount);
    }

    public static int getStatistic(final ServerStatsCounter manager, final Statistic<?> statistic) {
        Preconditions.checkNotNull(statistic, "Statistic cannot be null");
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
