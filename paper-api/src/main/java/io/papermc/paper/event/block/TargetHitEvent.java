package io.papermc.paper.event.block;

import com.google.common.base.Preconditions;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Projectile;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a Target Block is hit by a projectile.
 * <p>
 * Cancelling this event will stop the Target from emitting a redstone signal,
 * and in the case that the shooter is a player, will stop them from receiving
 * advancement criteria.
 */
@NullMarked
public class TargetHitEvent extends ProjectileHitEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private int signalStrength;

    @ApiStatus.Internal
    public TargetHitEvent(final Projectile projectile, final Block block, final BlockFace blockFace, final int signalStrength) {
        super(projectile, null, block, blockFace);
        this.signalStrength = signalStrength;
    }

    /**
     * Gets the strength of the redstone signal to be emitted by the Target block
     *
     * @return the strength of the redstone signal to be emitted
     */
    public @Range(from = 0, to = 15) int getSignalStrength() {
        return this.signalStrength;
    }

    /**
     * Sets the strength of the redstone signal to be emitted by the Target block
     *
     * @param signalStrength the strength of the redstone signal to be emitted
     */
    public void setSignalStrength(final @Range(from = 0, to = 15) int signalStrength) {
        Preconditions.checkArgument(signalStrength >= 0 && signalStrength <= 15, "Signal strength out of range (%s), must be in range [0,15]", signalStrength);
        this.signalStrength = signalStrength;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
