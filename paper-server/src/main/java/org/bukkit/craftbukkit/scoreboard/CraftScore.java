package org.bukkit.craftbukkit.scoreboard;

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
        Scoreboard board = this.objective.checkState().board;

        ReadOnlyScoreInfo score = board.getPlayerScoreInfo(this.entry, this.objective.getHandle());
        if (score != null) { // Lazy
            return score.value();
        }

        return 0; // Lazy
    }

    @Override
    public void setScore(int score) {
        this.objective.checkState().board.getOrCreatePlayerScore(this.entry, this.objective.getHandle()).set(score);
    }

    @Override
    public boolean isScoreSet() {
        Scoreboard board = this.objective.checkState().board;

        return board.getPlayerScoreInfo(this.entry, this.objective.getHandle()) != null;
    }

    @Override
    public CraftScoreboard getScoreboard() {
        return this.objective.getScoreboard();
    }

    // Paper start
    @Override
    public void resetScore() {
        Scoreboard board = this.objective.checkState().board;
        board.resetSinglePlayerScore(entry, this.objective.getHandle());
    }
    // Paper end

    // Paper start - add more score API
    @Override
    public boolean isTriggerable() {
        if (this.objective.getTrackedCriteria() != org.bukkit.scoreboard.Criteria.TRIGGER) {
            return false;
        }
        final Scoreboard board = this.objective.checkState().board;
        final ReadOnlyScoreInfo scoreInfo = board.getPlayerScoreInfo(this.entry, this.objective.getHandle());
        return scoreInfo != null && !scoreInfo.isLocked();
    }

    @Override
    public void setTriggerable(final boolean triggerable) {
        com.google.common.base.Preconditions.checkArgument(this.objective.getTrackedCriteria() == org.bukkit.scoreboard.Criteria.TRIGGER, "the criteria isn't 'trigger'");
        final Scoreboard board = this.objective.checkState().board;
        if (triggerable) {
            board.getOrCreatePlayerScore(this.entry, this.objective.getHandle()).unlock();
        } else {
            board.getOrCreatePlayerScore(this.entry, this.objective.getHandle()).lock();
        }
    }

    @Override
    public net.kyori.adventure.text.Component customName() {
        final Scoreboard board = this.objective.checkState().board;
        final ReadOnlyScoreInfo scoreInfo = board.getPlayerScoreInfo(this.entry, this.objective.getHandle());
        if (scoreInfo == null) {
            return null; // If score doesn't exist, don't create one
        }
        final net.minecraft.network.chat.Component display = board.getOrCreatePlayerScore(this.entry, this.objective.getHandle()).display();
        return display == null ? null : io.papermc.paper.adventure.PaperAdventure.asAdventure(display);
    }

    @Override
    public void customName(final net.kyori.adventure.text.Component customName) {
        final Scoreboard board = this.objective.checkState().board;
        board.getOrCreatePlayerScore(this.entry, this.objective.getHandle()).display(io.papermc.paper.adventure.PaperAdventure.asVanilla(customName));
    }
    // Paper end - add more score API
}
