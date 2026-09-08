package org.bukkit.craftbukkit.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

public class CraftBlockDamageEvent extends CraftBlockEvent implements BlockDamageEvent {

    private final Player player;
    private final BlockFace face;
    private final ItemStack itemInHand;
    private boolean instaBreak;

    private boolean cancelled;

    public CraftBlockDamageEvent(final Player player, final Block block, final BlockFace face, final ItemStack itemInHand, final boolean instaBreak) {
        super(block);
        this.player = player;
        this.face = face;
        this.itemInHand = itemInHand;
        this.instaBreak = instaBreak;
    }

    public CraftBlockDamageEvent(final ServerPlayer player, final BlockPos pos, final Direction face, final net.minecraft.world.item.ItemStack itemInHand, final boolean instaBreak) {
        this(
            player.getBukkitEntity(),
            CraftBlock.at(player.level(), pos),
            CraftBlock.notchToBlockFace(face),
            CraftItemStack.asCraftMirror(itemInHand),
            instaBreak
        );
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
    public BlockFace getBlockFace() {
        return this.face;
    }

    @Override
    public boolean getInstaBreak() {
        return this.instaBreak;
    }

    @Override
    public void setInstaBreak(final boolean instaBreak) {
        this.instaBreak = instaBreak;
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
        return BlockDamageEvent.getHandlerList();
    }
}
