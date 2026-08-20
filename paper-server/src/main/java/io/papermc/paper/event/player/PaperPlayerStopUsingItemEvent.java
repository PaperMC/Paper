package io.papermc.paper.event.player;

import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PaperPlayerStopUsingItemEvent extends CraftPlayerEvent implements PlayerStopUsingItemEvent {

    private final ItemStack item;
    private final int ticksHeldFor;

    public PaperPlayerStopUsingItemEvent(final Player player, final ItemStack item, final int ticksHeldFor) {
        super(player);
        this.item = item;
        this.ticksHeldFor = ticksHeldFor;
    }

    @Override
    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public int getTicksHeldFor() {
        return this.ticksHeldFor;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerStopUsingItemEvent.getHandlerList();
    }
}
