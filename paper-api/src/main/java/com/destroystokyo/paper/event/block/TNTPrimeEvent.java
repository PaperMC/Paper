package com.destroystokyo.paper.event.block;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEventNew;
import org.jspecify.annotations.Nullable;

/**
 * Called when TNT block is about to turn into {@link TNTPrimed}
 * <p>
 * Cancelling it won't turn TNT into {@link TNTPrimed} and leaves
 * the TNT block as-is
 *
 * @author Mark Vainomaa
 * @deprecated use {@link org.bukkit.event.block.TNTPrimeEvent}
 */
@Deprecated(forRemoval = true, since = "1.19.4")
public interface TNTPrimeEvent extends BlockEventNew, Cancellable {

    /**
     * Gets the TNT prime reason
     *
     * @return Prime reason
     */
    PrimeReason getReason();

    /**
     * Gets the TNT primer {@link Entity}.
     * <p>
     * It's {@code null} if {@link #getReason()} is {@link PrimeReason#REDSTONE} or {@link PrimeReason#FIRE}.
     * It's not {@code null} if {@link #getReason()} is {@link PrimeReason#ITEM} or {@link PrimeReason#PROJECTILE}
     * It might be {@code null} if {@link #getReason()} is {@link PrimeReason#EXPLOSION}
     *
     * @return The {@link Entity} who primed the TNT
     */
    @Nullable Entity getPrimerEntity();

    /**
     * Gets whether spawning {@link TNTPrimed} should be cancelled or not
     *
     * @return Whether spawning {@link TNTPrimed} should be cancelled or not
     */
    @Override
    boolean isCancelled();

    /**
     * Sets whether to cancel spawning {@link TNTPrimed} or not
     *
     * @param cancel whether spawning {@link TNTPrimed} should be cancelled or not
     */
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum PrimeReason {
        /**
         * When TNT prime was caused by other explosion (chain reaction)
         */
        EXPLOSION,

        /**
         * When TNT prime was caused by fire
         */
        FIRE,

        /**
         * When {@link Player} used {@link Material#FLINT_AND_STEEL} or
         * {@link Material#FIRE_CHARGE} on given TNT block
         */
        ITEM,

        /**
         * When TNT prime was caused by an {@link Entity} shooting TNT
         * using a bow with {@link Enchantment#FLAME} enchantment
         */
        PROJECTILE,

        /**
         * When redstone power triggered the TNT prime
         */
        REDSTONE
    }
}
