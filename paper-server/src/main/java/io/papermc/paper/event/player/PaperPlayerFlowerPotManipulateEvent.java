package io.papermc.paper.event.player;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PaperPlayerFlowerPotManipulateEvent extends CraftPlayerEvent implements PlayerFlowerPotManipulateEvent {

    private final Block flowerpot;
    private final ItemStack item;
    private final boolean placing;

    private boolean cancelled;

    public PaperPlayerFlowerPotManipulateEvent(final Player player, final Block flowerpot, final ItemStack item, final boolean placing) {
        super(player);
        this.flowerpot = flowerpot;
        this.item = item;
        this.placing = placing;
    }

    public PaperPlayerFlowerPotManipulateEvent(
        final net.minecraft.world.entity.player.Player player,
        final Level level,
        final BlockPos pos,
        final net.minecraft.world.item.ItemStack item,
        final boolean placing
    ) {
        this((Player) player.getBukkitEntity(), CraftBlock.at(level, pos), CraftItemStack.asBukkitCopy(item), placing);
    }

    @Override
    public Block getBlock() {
        return this.flowerpot;
    }

    @Override
    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public boolean isPlacing() {
        return this.placing;
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
        return PlayerFlowerPotManipulateEvent.getHandlerList();
    }
}
