package io.papermc.paper.event.player;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called when a player sets the effect for a beacon
 */
@NullMarked
public interface PlayerChangeBeaconEffectEvent extends PlayerEventNew, Cancellable {

    /**
     * @return the primary effect
     */
    @Nullable PotionEffectType getPrimary();

    /**
     * Sets the primary effect
     * <p>
     * NOTE: The primary effect still has to be one of the valid effects for a beacon.
     *
     * @param primary the primary effect
     */
    void setPrimary(@Nullable PotionEffectType primary);

    /**
     * @return the secondary effect
     */
    @Nullable PotionEffectType getSecondary();

    /**
     * Sets the secondary effect
     * <p>
     * This only has an effect when the beacon is able to accept a secondary effect.
     * NOTE: The secondary effect still has to be a valid effect for a beacon.
     *
     * @param secondary the secondary effect
     */
    void setSecondary(@Nullable PotionEffectType secondary);

    /**
     * @return the beacon block associated with this event
     */
    Block getBeacon();

    /**
     * Gets if the item used to change the beacon will be consumed.
     * <p>
     * Independent of {@link #isCancelled()}. If the event is cancelled
     * the item will <b>NOT</b> be consumed.
     *
     * @return {@code true} if item will be consumed
     */
    boolean willConsumeItem();

    /**
     * Sets if the item used to change the beacon should be consumed.
     * <p>
     * Independent of {@link #isCancelled()}. If the event is cancelled
     * the item will <b>NOT</b> be consumed.
     *
     * @param consumeItem {@code true} if item should be consumed
     */
    void setConsumeItem(boolean consumeItem);

    /**
     * {@inheritDoc}
     * <p>
     * If a {@link PlayerChangeBeaconEffectEvent} is cancelled, the changes will
     * not take effect
     */
    @Override
    boolean isCancelled();

    /**
     * {@inheritDoc}
     * <p>
     * If cancelled, the item will <b>NOT</b> be consumed regardless of what {@link #willConsumeItem()} says
     * <p>
     * If a {@link PlayerChangeBeaconEffectEvent} is cancelled, the changes will not be applied
     * or saved.
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
