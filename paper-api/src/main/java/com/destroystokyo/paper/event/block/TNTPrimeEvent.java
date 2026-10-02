package com.destroystokyo.paper.event.block;

import org.bukkit.Material;
import org.bukkit.Warning;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/// Called when TNT block is about to turn into [TNTPrimed]
///
/// Cancelling it won't turn TNT into [TNTPrimed] and leaves
/// the TNT block as-is
///
/// @author Mark Vainomaa
/// @deprecated use [org.bukkit.event.block.TNTPrimeEvent]
@Deprecated(forRemoval = true, since = "1.19.4")
@Warning(propagate = false)
public class TNTPrimeEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @NotNull private final PrimeReason reason;
    @Nullable private final Entity primerEntity;

    private boolean cancelled;

    @ApiStatus.Internal
    public TNTPrimeEvent(@NotNull Block block, @NotNull PrimeReason reason, @Nullable Entity primerEntity) {
        super(block);
        this.reason = reason;
        this.primerEntity = primerEntity;
    }

    /// Gets the TNT prime reason
    ///
    /// @return Prime reason
    @NotNull
    public PrimeReason getReason() {
        return this.reason;
    }

    /// Gets the TNT primer [Entity].
    ///
    /// It's `null` if [#getReason()] is [PrimeReason#REDSTONE] or [PrimeReason#FIRE].
    /// It's not `null` if [#getReason()] is [PrimeReason#ITEM] or [PrimeReason#PROJECTILE]
    /// It might be `null` if [#getReason()] is [PrimeReason#EXPLOSION]
    ///
    /// @return The [Entity] who primed the TNT
    @Nullable
    public Entity getPrimerEntity() {
        return this.primerEntity;
    }

    /// Gets whether spawning [TNTPrimed] should be cancelled or not
    ///
    /// @return Whether spawning [TNTPrimed] should be cancelled or not
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /// Sets whether to cancel spawning [TNTPrimed] or not
    ///
    /// @param cancel whether spawning [TNTPrimed] should be cancelled or not
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public enum PrimeReason {
        /// When TNT prime was caused by other explosion (chain reaction)
        EXPLOSION,

        /// When TNT prime was caused by fire
        FIRE,

        /// When [Player] used [Material#FLINT_AND_STEEL] or
        /// [Material#FIRE_CHARGE] on given TNT block
        ITEM,

        /// When TNT prime was caused by an [Entity] shooting TNT
        /// using a bow with [Enchantment#FLAME] enchantment
        PROJECTILE,

        /// When redstone power triggered the TNT prime
        REDSTONE
    }
}
