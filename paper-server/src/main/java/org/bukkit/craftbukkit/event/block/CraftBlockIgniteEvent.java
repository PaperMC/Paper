package org.bukkit.craftbukkit.event.block;

import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockIgniteEvent;
import org.jspecify.annotations.Nullable;

public class CraftBlockIgniteEvent extends CraftBlockEvent implements BlockIgniteEvent {

    private final IgniteCause cause;
    private final @Nullable Entity ignitingEntity;
    private final @Nullable Block ignitingBlock;

    private boolean cancelled;

    public CraftBlockIgniteEvent(final Block block, final IgniteCause cause, final @Nullable Entity ignitingEntity, final @Nullable Block ignitingBlock) {
        super(block);
        this.cause = cause;
        this.ignitingEntity = ignitingEntity;
        this.ignitingBlock = ignitingBlock;
    }

    public CraftBlockIgniteEvent(
        final Level level, final BlockPos pos, final IgniteCause cause, final net.minecraft.world.entity.@Nullable Entity ignitingEntity, final @Nullable Block ignitingBlock
    ) {
        this(CraftBlock.at(level, pos), cause, Optionull.map(ignitingEntity, net.minecraft.world.entity.Entity::getBukkitEntity), ignitingBlock);
    }

    public CraftBlockIgniteEvent(
        final Level level, final BlockPos pos, final IgniteCause cause, final net.minecraft.world.entity.@Nullable Entity ignitingEntity
    ) {
        this(level, pos, cause, ignitingEntity, null);
    }

    public CraftBlockIgniteEvent(
        final Level level, final BlockPos pos, net.minecraft.world.entity.Entity ignitingEntity
    ) {
        final IgniteCause cause = switch (ignitingEntity) {
            case EndCrystal _ -> IgniteCause.ENDER_CRYSTAL;
            case LightningBolt _ -> IgniteCause.LIGHTNING;
            case Fireball _ -> IgniteCause.FIREBALL;
            case Arrow _ -> IgniteCause.ARROW;
            default -> IgniteCause.FLINT_AND_STEEL;
        };

        if (ignitingEntity instanceof final net.minecraft.world.entity.projectile.Projectile projectile) {
            ignitingEntity = projectile.getOwner();
        }
        this(level, pos, cause, ignitingEntity);
    }

    public CraftBlockIgniteEvent(final Level level, final BlockPos pos, final BlockPos sourcePos) {
        final Block initingBlock = CraftBlock.at(level, sourcePos);
        final IgniteCause cause = switch (initingBlock.getType()) {
            case LAVA -> IgniteCause.LAVA;
            case DISPENSER -> IgniteCause.FLINT_AND_STEEL; // Fire or any other unknown block counts as SPREAD.
            default -> IgniteCause.SPREAD;
        };
        this(level, pos, cause, null, initingBlock);
    }

    public CraftBlockIgniteEvent(final Level level, final BlockPos pos, final Explosion explosion) {
        this(level, pos, IgniteCause.EXPLOSION, explosion.getDirectSourceEntity());
    }

    @Override
    public IgniteCause getCause() {
        return this.cause;
    }

    @Override
    public @Nullable Player getPlayer() {
        if (this.ignitingEntity instanceof final Player ignitingPlayer) {
            return ignitingPlayer;
        }

        return null;
    }

    @Override
    public @Nullable Entity getIgnitingEntity() {
        return this.ignitingEntity;
    }

    @Override
    public @Nullable Block getIgnitingBlock() {
        return this.ignitingBlock;
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
        return BlockIgniteEvent.getHandlerList();
    }
}
