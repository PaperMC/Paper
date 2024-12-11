package org.bukkit.craftbukkit.scoreboard;

import com.google.common.base.Preconditions;
import net.minecraft.world.scores.Scoreboard;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;

final class CraftObjective extends CraftScoreboardComponent implements Objective {
    private final net.minecraft.world.scores.Objective objective;
    private final CraftCriteria criteria;

    CraftObjective(CraftScoreboard scoreboard, net.minecraft.world.scores.Objective objective) {
        super(scoreboard);
        this.objective = objective;
        this.criteria = CraftCriteria.getFromNMS(objective);
    }

    net.minecraft.world.scores.Objective getHandle() {
        return this.objective;
    }

    @Override
    public String getName() {
        this.checkState();

        return this.objective.getName();
    }

    @Override
    public String getDisplayName() {
        this.checkState();

        return CraftChatMessage.fromComponent(this.objective.getDisplayName());
    }

    @Override
    public void setDisplayName(String displayName) {
        Preconditions.checkArgument(displayName != null, "Display name cannot be null");
        this.checkState();

        this.objective.setDisplayName(CraftChatMessage.fromString(displayName)[0]); // SPIGOT-4112: not nullable
    }

    @Override
    public String getCriteria() {
        this.checkState();

        return this.criteria.bukkitName;
    }

    @Override
    public Criteria getTrackedCriteria() {
        this.checkState();

        return this.criteria;
    }

    @Override
    public boolean isModifiable() {
        this.checkState();

        return !this.criteria.criteria.isReadOnly();
    }

    @Override
    public void setDisplaySlot(DisplaySlot slot) {
        CraftScoreboard scoreboard = this.checkState();
        Scoreboard board = scoreboard.board;
        net.minecraft.world.scores.Objective objective = this.objective;

        for (net.minecraft.world.scores.DisplaySlot i : net.minecraft.world.scores.DisplaySlot.values()) {
            if (board.getDisplayObjective(i) == objective) {
                board.setDisplayObjective(i, null);
            }
        }
        if (slot != null) {
            net.minecraft.world.scores.DisplaySlot slotNumber = CraftScoreboardTranslations.fromBukkitSlot(slot);
            board.setDisplayObjective(slotNumber, this.getHandle());
        }
    }

    @Override
    public DisplaySlot getDisplaySlot() {
        CraftScoreboard scoreboard = this.checkState();
        Scoreboard board = scoreboard.board;
        net.minecraft.world.scores.Objective objective = this.objective;

        for (net.minecraft.world.scores.DisplaySlot i : net.minecraft.world.scores.DisplaySlot.values()) {
            if (board.getDisplayObjective(i) == objective) {
                return CraftScoreboardTranslations.toBukkitSlot(i);
            }
        }
        return null;
    }

    @Override
    public void setRenderType(RenderType renderType) {
        Preconditions.checkArgument(renderType != null, "RenderType cannot be null");
        this.checkState();

        this.objective.setRenderType(CraftScoreboardTranslations.fromBukkitRender(renderType));
    }

    @Override
    public RenderType getRenderType() {
        this.checkState();

        return CraftScoreboardTranslations.toBukkitRender(this.objective.getRenderType());
    }

    @Override
    public Score getScore(OfflinePlayer player) {
        this.checkState();

        return new CraftScore(this, CraftScoreboard.getScoreHolder(player));
    }

    @Override
    public Score getScore(String entry) {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");
        Preconditions.checkArgument(entry.length() <= Short.MAX_VALUE, "Score '" + entry + "' is longer than the limit of 32767 characters");
        this.checkState();

        return new CraftScore(this, CraftScoreboard.getScoreHolder(entry));
    }

    @Override
    public void unregister() {
        CraftScoreboard scoreboard = this.checkState();

        scoreboard.board.removeObjective(this.objective);
    }

    @Override
    CraftScoreboard checkState() {
        Preconditions.checkState(this.getScoreboard().board.getObjective(this.objective.getName()) != null, "Unregistered scoreboard component");

        return this.getScoreboard();
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
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final CraftObjective other = (CraftObjective) obj;
        return !(this.objective != other.objective && (this.objective == null || !this.objective.equals(other.objective)));
    }


}
