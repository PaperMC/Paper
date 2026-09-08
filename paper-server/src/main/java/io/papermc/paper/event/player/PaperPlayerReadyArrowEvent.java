package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
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

    public PaperPlayerReadyArrowEvent(final ServerPlayer player, final net.minecraft.world.item.ItemStack bow, final net.minecraft.world.item.ItemStack arrow) {
        this(player.getBukkitEntity(), CraftItemStack.asCraftMirror(bow), CraftItemStack.asCraftMirror(arrow));
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
