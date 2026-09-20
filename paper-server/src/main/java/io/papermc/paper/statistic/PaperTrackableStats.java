package io.papermc.paper.statistic;

import com.google.common.base.Preconditions;
import java.util.function.IntUnaryOperator;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stat;
import net.minecraft.world.scores.ScoreHolder;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.scoreboard.CraftScoreboardManager;
import org.jspecify.annotations.Nullable;

public interface PaperTrackableStats {

    ServerStatsCounter getStatCounter();

    @Nullable ScoreHolder getScoreHolder();

    private ServerStatsCounter getUsableCounter() {
        return this.getUsableCounter(this.getScoreHolder());
    }

    private ServerStatsCounter getUsableCounter(final @Nullable ScoreHolder from) {
        return from instanceof ServerPlayer player ? player.getStats() : this.getStatCounter();
    }

    default void decrementStatistic(final Statistic<?> statistic, final int amount) {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(amount > 0, "Amount must be greater than 0");

        this.updateStatistic(PaperStatistic.getNMSStatistic(statistic), originalAmount -> {
            final int newAmount = originalAmount - amount;
            Preconditions.checkArgument(newAmount >= 0, "New amount must be greater than or equal to 0");
            return newAmount;
        });
    }

    default void incrementStatistic(final Statistic<?> statistic, final int amount) {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(amount > 0, "Amount must be greater than 0");

        this.updateStatistic(PaperStatistic.getNMSStatistic(statistic), originalAmount -> (int) Math.min((long) originalAmount + amount, Integer.MAX_VALUE)); // clamp from StatsCounter
    }

    default void setStatistic(final Statistic<?> statistic, final int newAmount) {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(newAmount >= 0, "New amount must be greater than or equal to 0");

        this.setStatistic(PaperStatistic.getNMSStatistic(statistic), newAmount);
    }

    private void updateStatistic(final Stat<?> stat, final IntUnaryOperator operation) {
        final ScoreHolder holder = this.getScoreHolder();
        final ServerStatsCounter counter = this.getUsableCounter(holder);
        final int newAmount = operation.applyAsInt(counter.getValue(stat));
        setStatistic(counter, stat, newAmount, holder);
    }

    private void setStatistic(final Stat<?> stat, final int newAmount) {
        final ScoreHolder holder = this.getScoreHolder();
        setStatistic(this.getUsableCounter(holder), stat, newAmount, holder);
    }

    @SuppressWarnings("ConstantConditions")
    private static void setStatistic(final ServerStatsCounter counter, final Stat<?> stat, final int newAmount, final @Nullable ScoreHolder holder) {
        final ServerPlayer player = holder instanceof ServerPlayer p ? p : null;
        counter.setValue(player, stat, newAmount);
        if (holder != null) {
            ((CraftScoreboardManager) Bukkit.getScoreboardManager()).forAllObjectives(stat, holder, score -> {
                score.set(newAmount);
            });
        }
        if (player == null) { // offline
            counter.save();
        }
    }

    default int getStatistic(final Statistic<?> statistic) {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        return this.getUsableCounter().getValue(PaperStatistic.getNMSStatistic(statistic));
    }

    default String getFormattedValue(final Statistic<?> statistic) {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        final Stat<?> stat = PaperStatistic.getNMSStatistic(statistic);
        return stat.format(this.getUsableCounter().getValue(stat));
    }
}
