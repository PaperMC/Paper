package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.PortalType;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.PortalCreateEvent;

/**
 * Thrown when a Living Entity creates a portal in a world.
 *
 * @deprecated Use {@link PortalCreateEvent}
 */
@Deprecated(since = "1.14.1")
public interface EntityCreatePortalEvent extends EntityEventNew, Cancellable {

    @Override
    LivingEntity getEntity();

    List<BlockState> getBlocks();

    PortalType getPortalType();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
