package org.bukkit.event.block;

import java.util.List;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;

/**
 * Called when a bell resonated after being rung and highlights nearby raiders.
 * A bell will only resonate if raiders are in the vicinity of the bell.
 */
public interface BellResonateEvent extends BlockEventNew {

    /**
     * Get a mutable list of all {@link LivingEntity entities} to be
     * highlighted by the bell's resonating. This list can be added to or
     * removed from to change which entities are highlighted, and may be empty
     * if no entities were resonated as a result of this event.
     * <p>
     * While the highlighted entities will change, the particles that display
     * over a resonated entity and their colors will not. This is handled by the
     * client and cannot be controlled by the server.
     *
     * @return a list of resonated entities
     */
    List<LivingEntity> getResonatedEntities();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
