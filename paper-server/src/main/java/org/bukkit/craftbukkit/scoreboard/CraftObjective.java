package org.bukkit.craftbukkit.scoreboard;

import net.minecraft.server.Scoreboard;
import net.minecraft.server.ScoreboardObjective;

import org.apache.commons.lang.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

final class CraftObjective extends CraftScoreboardComponent implements Objective {
    private final ScoreboardObjective objective;
    private final CraftCriteria criteria;

    CraftObjective(CraftScoreboard scoreboard, ScoreboardObjective objective) {
        super(scoreboard);
        this.objective = objective;
        this.criteria = CraftCriteria.getFromNMS(objective);

        scoreboard.objectives.put(objective.getName(), this);
    }

    ScoreboardObjective getHandle() {
        return objective;
    }

    public String getName() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();

        return objective.getName();
    }

    public String getDisplayName() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();

        return objective.getDisplayName();
    }

    public void setDisplayName(String displayName) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull(displayName, "Display name cannot be null");
        Validate.isTrue(displayName.length() <= 32, "Display name '" + displayName + "' is longer than the limit of 32 characters");
        CraftScoreboard scoreboard = checkState();

        objective.setDisplayName(displayName);
    }

    public String getCriteria() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();

        return criteria.bukkitName;
    }

    public boolean isModifiable() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();

        return !criteria.criteria.isReadOnly();
    }

    public void setDisplaySlot(DisplaySlot slot) throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();
        Scoreboard board = scoreboard.board;
        ScoreboardObjective objective = this.objective;

        for (int i = 0; i < CraftScoreboardTranslations.MAX_DISPLAY_SLOT; i++) {
            if (board.getObjectiveForSlot(i) == objective) {
                board.setDisplaySlot(i, null);
            }
        }
        if (slot != null) {
            int slotNumber = CraftScoreboardTranslations.fromBukkitSlot(slot);
            board.setDisplaySlot(slotNumber, getHandle());
        }
    }

    public DisplaySlot getDisplaySlot() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();
        Scoreboard board = scoreboard.board;
        ScoreboardObjective objective = this.objective;

        for (int i = 0; i < CraftScoreboardTranslations.MAX_DISPLAY_SLOT; i++) {
            if (board.getObjectiveForSlot(i) == objective) {
                return CraftScoreboardTranslations.toBukkitSlot(i);
            }
        }
        return null;
    }

    public Score getScore(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException {
        Validate.notNull(player, "Player cannot be null");
        CraftScoreboard scoreboard = checkState();

        return new CraftScore(this, player.getName());
    }

    @Override
    public void unregister() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();

        scoreboard.objectives.remove(this.getName());
        scoreboard.board.unregisterObjective(objective);
        setUnregistered();
    }
}
