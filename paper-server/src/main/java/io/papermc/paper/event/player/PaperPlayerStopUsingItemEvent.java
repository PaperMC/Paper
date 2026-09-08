package io.papermc.paper.event.player;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
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

    public PaperPlayerStopUsingItemEvent(final ServerPlayer player, final net.minecraft.world.item.ItemStack item, final int ticksHeldFor) {
        this(player.getBukkitEntity(), CraftItemStack.asCraftMirror(item), ticksHeldFor);
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
