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
        return Bukkit.getOfflinePlayer(entry.getScoreboardName());
    }

    @Override
    public String getEntry() {
        return entry.getScoreboardName();
    }

    @Override
    public Objective getObjective() {
        return objective;
    }

    @Override
    public int getScore() {
        Scoreboard board = objective.checkState().board;

        ReadOnlyScoreInfo score = board.getPlayerScoreInfo(entry, objective.getHandle());
        if (score != null) { // Lazy
            return score.value();
        }

        return 0; // Lazy
    }

    @Override
    public void setScore(int score) {
        objective.checkState().board.getOrCreatePlayerScore(entry, objective.getHandle()).set(score);
    }

    @Override
    public boolean isScoreSet() {
        Scoreboard board = objective.checkState().board;

        return board.getPlayerScoreInfo(entry, objective.getHandle()) != null;
    }

    @Override
    public CraftScoreboard getScoreboard() {
        return objective.getScoreboard();
    }
}
