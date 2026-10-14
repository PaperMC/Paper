package org.bukkit.craftbukkit.scoreboard;

import com.google.common.base.Preconditions;
import net.minecraft.world.scores.ReadOnlyScoreInfo;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

/**
 * TL;DR: This class is special and lazily grabs a handle...
 * ...because a handle is a full fledged (I think permanent) hashMap for the associated name.
 * <p>
 * Also, as an added perk, a CraftScore will (intentionally) stay a valid reference so long as objective is valid.
 */
final class CraftScore implements Score {
    private final ScoreHolder entry;
    private final CraftObjective objective;

    CraftScore(CraftObjective objective, ScoreHolder entry) {
        this.objective = objective;
        this.entry = entry;
    }

    @Override
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(this.entry.getScoreboardName());
    }

    @Override
    public String getEntry() {
        return this.entry.getScoreboardName();
    }

    @Override
    public Objective getObjective() {
        return this.objective;
    }

    @Override
    public int getScore() {
        this.objective.checkState();

        Scoreboard board = this.objective.getScoreboard().getHandle();
        ReadOnlyScoreInfo score = board.getPlayerScoreInfo(this.entry, this.objective.getHandle());
        if (score != null) {
            return score.value();
        }

        return 0;
    }

    @Override
    public void setScore(int score) {
        this.objective.checkState();
        this.objective.getScoreboard().getHandle().getOrCreatePlayerScore(this.entry, this.objective.getHandle()).set(score);
    }

    @Override
    public io.papermc.paper.scoreboard.numbers.NumberFormat numberFormat() {
        this.objective.checkState();

        ReadOnlyScoreInfo scoreInfo = this.objective.getScoreboard().getHandle()
            .getPlayerScoreInfo(this.entry, this.objective.getHandle());
        if (scoreInfo == null) {
            return null;
        }

        net.minecraft.network.chat.numbers.NumberFormat vanilla = scoreInfo.numberFormat();
        if (vanilla == null) {
            return null;
        }

        return io.papermc.paper.util.PaperScoreboardFormat.asPaper(vanilla);
    }


    @Override
    public void numberFormat(io.papermc.paper.scoreboard.numbers.NumberFormat format) {
        this.objective.checkState();

        final net.minecraft.world.scores.ScoreAccess access = this.objective.getScoreboard()
            .getHandle().getOrCreatePlayerScore(this.entry, this.objective.getHandle());
        if (format == null) {
            access.numberFormatOverride(null);
            return;
        }

        access.numberFormatOverride(io.papermc.paper.util.PaperScoreboardFormat.asVanilla(format));
    }

    @Override
    public boolean isScoreSet() {
        this.objective.checkState();

        Scoreboard board = this.objective.getScoreboard().getHandle();
        return board.getPlayerScoreInfo(this.entry, this.objective.getHandle()) != null;
    }

    @Override
    public CraftScoreboard getScoreboard() {
        return this.objective.getScoreboard();
    }

    @Override
    public void resetScore() {
        this.objective.checkState();

        Scoreboard board = this.objective.getScoreboard().getHandle();
        board.resetSinglePlayerScore(entry, this.objective.getHandle());
    }

    @Override
    public boolean isTriggerable() {
        this.objective.checkState();

        if (this.objective.getTrackedCriteria() != org.bukkit.scoreboard.Criteria.TRIGGER) {
            return false;
        }
        final Scoreboard board = this.objective.getScoreboard().getHandle();
        final ReadOnlyScoreInfo scoreInfo = board.getPlayerScoreInfo(this.entry, this.objective.getHandle());
        return scoreInfo != null && !scoreInfo.isLocked();
    }

    @Override
    public void setTriggerable(final boolean triggerable) {
        Preconditions.checkArgument(this.objective.getTrackedCriteria() == org.bukkit.scoreboard.Criteria.TRIGGER, "the criteria isn't 'trigger'");
        this.objective.checkState();

        final Scoreboard board = this.objective.getScoreboard().getHandle();
        if (triggerable) {
            board.getOrCreatePlayerScore(this.entry, this.objective.getHandle()).unlock();
        } else {
            board.getOrCreatePlayerScore(this.entry, this.objective.getHandle()).lock();
        }
    }

    @Override
    public net.kyori.adventure.text.Component customName() {
        this.objective.checkState();

        final Scoreboard board = this.objective.getScoreboard().getHandle();
        final ReadOnlyScoreInfo scoreInfo = board.getPlayerScoreInfo(this.entry, this.objective.getHandle());
        if (scoreInfo == null) {
            return null; // If score doesn't exist, don't create one
        }
        final net.minecraft.network.chat.Component display = board.getOrCreatePlayerScore(this.entry, this.objective.getHandle()).display();
        return display == null ? null : io.papermc.paper.adventure.PaperAdventure.asAdventure(display);
    }

    @Override
    public void customName(final net.kyori.adventure.text.Component customName) {
        this.objective.checkState();

        final Scoreboard board = this.objective.getScoreboard().getHandle();
        board.getOrCreatePlayerScore(this.entry, this.objective.getHandle()).display(io.papermc.paper.adventure.PaperAdventure.asVanilla(customName));
    }
}
