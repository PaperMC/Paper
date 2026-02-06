package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PaperPlayerReadyArrowEvent extends CraftPlayerEvent implements PlayerReadyArrowEvent {

    private final ItemStack bow;
    private final ItemStack arrow;

    private boolean cancelled;

    public PaperPlayerReadyArrowEvent(final Player player, final ItemStack bow, final ItemStack arrow) {
        super(player);
        this.bow = bow;
        this.arrow = arrow;
    }

    @Override
    public ItemStack getBow() {
        return this.bow;
    }

    @Override
    public ItemStack getArrow() {
        return this.arrow;
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
        return PlayerReadyArrowEvent.getHandlerList();
    }
}
