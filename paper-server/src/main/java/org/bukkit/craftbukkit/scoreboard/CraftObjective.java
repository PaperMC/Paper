package org.bukkit.craftbukkit.scoreboard;

import com.google.common.base.Preconditions;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.ScoreboardObjective;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;

final class CraftObjective extends CraftScoreboardComponent implements Objective {
    private final ScoreboardObjective objective;
    private final CraftCriteria criteria;

    CraftObjective(CraftScoreboard scoreboard, ScoreboardObjective objective) {
        super(scoreboard);
        this.objective = objective;
        this.criteria = CraftCriteria.getFromNMS(objective);
    }

    ScoreboardObjective getHandle() {
        return objective;
    }

    @Override
    public String getName() throws IllegalStateException {
        checkState();

        return objective.getName();
    }

    @Override
    public String getDisplayName() throws IllegalStateException {
        checkState();

        return CraftChatMessage.fromComponent(objective.getDisplayName());
    }

    @Override
    public void setDisplayName(String displayName) throws IllegalStateException, IllegalArgumentException {
        Preconditions.checkArgument(displayName != null, "Display name cannot be null");
        Preconditions.checkArgument(displayName.length() <= 128, "Display name '" + displayName + "' is longer than the limit of 128 characters");
        checkState();

        objective.setDisplayName(CraftChatMessage.fromString(displayName)[0]); // SPIGOT-4112: not nullable
    }

    @Override
    public String getCriteria() throws IllegalStateException {
        checkState();

        return criteria.bukkitName;
    }

    @Override
    public Criteria getTrackedCriteria() throws IllegalStateException {
        checkState();

        return criteria;
    }

    @Override
    public boolean isModifiable() throws IllegalStateException {
        checkState();

        return !criteria.criteria.isReadOnly();
    }

    @Override
    public void setDisplaySlot(DisplaySlot slot) throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();
        Scoreboard board = scoreboard.board;
        ScoreboardObjective objective = this.objective;

        for (int i = 0; i < CraftScoreboardTranslations.MAX_DISPLAY_SLOT; i++) {
            if (board.getDisplayObjective(i) == objective) {
                board.setDisplayObjective(i, null);
            }
        }
        if (slot != null) {
            int slotNumber = CraftScoreboardTranslations.fromBukkitSlot(slot);
            board.setDisplayObjective(slotNumber, getHandle());
        }
    }

    @Override
    public DisplaySlot getDisplaySlot() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();
        Scoreboard board = scoreboard.board;
        ScoreboardObjective objective = this.objective;

        for (int i = 0; i < CraftScoreboardTranslations.MAX_DISPLAY_SLOT; i++) {
            if (board.getDisplayObjective(i) == objective) {
                return CraftScoreboardTranslations.toBukkitSlot(i);
            }
        }
        return null;
    }

    @Override
    public void setRenderType(RenderType renderType) throws IllegalStateException {
        Preconditions.checkArgument(renderType != null, "RenderType cannot be null");
        checkState();

        this.objective.setRenderType(CraftScoreboardTranslations.fromBukkitRender(renderType));
    }

    @Override
    public RenderType getRenderType() throws IllegalStateException {
        checkState();

        return CraftScoreboardTranslations.toBukkitRender(this.objective.getRenderType());
    }

    @Override
    public Score getScore(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException {
        Preconditions.checkArgument(player != null, "Player cannot be null");
        checkState();

        return new CraftScore(this, player.getName());
    }

    @Override
    public Score getScore(String entry) throws IllegalArgumentException, IllegalStateException {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");
        Preconditions.checkArgument(entry.length() <= Short.MAX_VALUE, "Score '" + entry + "' is longer than the limit of 32767 characters");
        checkState();

        return new CraftScore(this, entry);
    }

    @Override
    public void unregister() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();

        scoreboard.board.removeObjective(objective);
    }

    @Override
    CraftScoreboard checkState() throws IllegalStateException {
        Preconditions.checkState(getScoreboard().board.getObjective(objective.getName()) != null, "Unregistered scoreboard component");

        return getScoreboard();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.objective != null ? this.objective.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CraftObjective other = (CraftObjective) obj;
        return !(this.objective != other.objective && (this.objective == null || !this.objective.equals(other.objective)));
    }


}
