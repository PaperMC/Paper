package io.papermc.paper.statistic;

import com.google.common.base.Preconditions;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stat;

public final class PaperStatistics {

    public static final Set<CustomStatistic> IGNORED_STATS_FOR_EVENT = Set.of(
        CustomStatistic.FALL_ONE_CM,
        CustomStatistic.BOAT_ONE_CM,
        CustomStatistic.CLIMB_ONE_CM,
        CustomStatistic.WALK_ON_WATER_ONE_CM,
        CustomStatistic.WALK_UNDER_WATER_ONE_CM,
        CustomStatistic.FLY_ONE_CM,
        CustomStatistic.HORSE_ONE_CM,
        CustomStatistic.MINECART_ONE_CM,
        CustomStatistic.PIG_ONE_CM,
        CustomStatistic.PLAY_TIME,
        CustomStatistic.SWIM_ONE_CM,
        CustomStatistic.WALK_ONE_CM,
        CustomStatistic.SPRINT_ONE_CM,
        CustomStatistic.CROUCH_ONE_CM,
        CustomStatistic.TIME_SINCE_DEATH,
        CustomStatistic.SNEAK_TIME,
        CustomStatistic.TOTAL_WORLD_TIME,
        CustomStatistic.TIME_SINCE_REST,
        CustomStatistic.AVIATE_ONE_CM,
        CustomStatistic.STRIDER_ONE_CM
    );

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
