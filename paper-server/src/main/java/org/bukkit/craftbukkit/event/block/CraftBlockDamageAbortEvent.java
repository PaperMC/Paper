package org.bukkit.craftbukkit.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.inventory.ItemStack;

public class CraftBlockDamageAbortEvent extends CraftBlockEvent implements BlockDamageAbortEvent {

    private final Player player;
    private final ItemStack itemInHand;

    public CraftBlockDamageAbortEvent(final Player player, final Block block, final ItemStack itemInHand) {
        super(block);
        this.player = player;
        this.itemInHand = itemInHand;
    }

    public CraftBlockDamageAbortEvent(final ServerPlayer player, final BlockPos pos, final net.minecraft.world.item.ItemStack itemInHand) {
        this(player.getBukkitEntity(), CraftBlock.at(player.level(), pos), CraftItemStack.asCraftMirror(itemInHand));
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public ItemStack getItemInHand() {
        return this.itemInHand;
    }

    @Override
    public HandlerList getHandlers() {
        return BlockDamageAbortEvent.getHandlerList();
    }
}
