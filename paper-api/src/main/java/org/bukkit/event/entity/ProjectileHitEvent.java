package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Called when a projectile hits an object
 */
public interface ProjectileHitEvent extends EntityEventNew, Cancellable {

    @Override
    Projectile getEntity();

    @Nullable Entity getHitEntity();

    /**
     * Gets the block that was hit, if it was a block that was hit.
     *
     * @return hit block or else {@code null}
     */
    @Nullable Block getHitBlock();

    /**
     * Gets the block face that was hit, if it was a block that was hit and the
     * face was provided in the event.
     *
     * @return hit face or else {@code null}
     */
    @Nullable BlockFace getHitBlockFace();

    /**
     * Whether to cancel the action that occurs when the projectile hits.
     * <p>
     * In the case of an entity, it will not collide (unless it's a firework,
     * then use {@link FireworkExplodeEvent}).
     * <br>
     * In the case of a block, some blocks (e.g. target block, bell) will not
     * perform the action associated.
     * <p>
     * This does NOT prevent block collisions, and explosions will still occur
     * unless their respective events are cancelled.
     */
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
