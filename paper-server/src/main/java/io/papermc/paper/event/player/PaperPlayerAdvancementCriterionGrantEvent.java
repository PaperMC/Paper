package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPlayerAdvancementCriterionGrantEvent extends CraftPlayerEvent implements PlayerAdvancementCriterionGrantEvent {

    private final Advancement advancement;
    private final String criterion;
    private final AdvancementProgress advancementProgress;

    private boolean cancelled;

    public PaperPlayerAdvancementCriterionGrantEvent(final Player player, final Advancement advancement, final String criterion) {
        super(player);
        this.advancement = advancement;
        this.criterion = criterion;
        this.advancementProgress = player.getAdvancementProgress(advancement);
    }

    @Override
    public Advancement getAdvancement() {
        return this.advancement;
    }

    @Override
    public String getCriterion() {
        return this.criterion;
    }

    @Override
    public AdvancementProgress getAdvancementProgress() {
        return this.advancementProgress;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerAdvancementCriterionGrantEvent.getHandlerList();
    }
}
