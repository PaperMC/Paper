package org.bukkit.craftbukkit.event.entity;

import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.Nullable;

public class CraftEntityPlaceEvent extends CraftEntityEvent implements EntityPlaceEvent {

    private final @Nullable Player player;
    private final Block block;
    private final BlockFace blockFace;
    private final EquipmentSlot hand;

    private boolean cancelled;

    public CraftEntityPlaceEvent(final Entity entity, final @Nullable Player player, final Block block, final BlockFace blockFace, final EquipmentSlot hand) {
        super(entity);
        this.player = player;
        this.block = block;
        this.blockFace = blockFace;
        this.hand = hand;
    }

    public CraftEntityPlaceEvent(
        final net.minecraft.world.entity.Entity entity,
        final net.minecraft.world.entity.player.@Nullable Player player,
        final Level level,
        final BlockPos clickedPos,
        final Direction clickedFace,
        final InteractionHand hand
    ) {
        this(
            entity.getBukkitEntity(),
            (Player) Optionull.map(player, net.minecraft.world.entity.player.Player::getBukkitEntity),
            CraftBlock.at(level, clickedPos),
            CraftBlock.notchToBlockFace(clickedFace),
            CraftEquipmentSlot.getHand(hand)
        );
    }

    public CraftEntityPlaceEvent(final net.minecraft.world.entity.Entity entity, final UseOnContext context) {
        this(entity, context.getPlayer(), context.getLevel(), context.getClickedPos(), context.getClickedFace(), context.getHand());
    }

    @Override
    public @Nullable Player getPlayer() {
        return this.player;
    }

    @Override
    public Block getBlock() {
        return this.block;
    }

    @Override
    public BlockFace getBlockFace() {
        return this.blockFace;
    }

    @Override
    public EquipmentSlot getHand() {
        return this.hand;
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
        return EntityPlaceEvent.getHandlerList();
    }
}
