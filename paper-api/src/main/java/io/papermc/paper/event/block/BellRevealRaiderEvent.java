package io.papermc.paper.event.block;

import org.bukkit.entity.Raider;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEventNew;

/**
 * Called when a {@link Raider} is revealed by a bell.
 *
 * @deprecated use {@link org.bukkit.event.block.BellResonateEvent}
 */
@Deprecated(since = "1.19.4")
public interface BellRevealRaiderEvent extends BlockEventNew, Cancellable {

    /**
     * Gets the raider that the bell revealed.
     *
     * @return The raider
     */
    Raider getEntity();

    /**
     * {@inheritDoc}
     * <p>
     * This does not cancel the particle effects shown on the bell, only the entity.
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
