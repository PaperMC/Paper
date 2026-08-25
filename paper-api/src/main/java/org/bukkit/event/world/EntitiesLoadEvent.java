package org.bukkit.event.world;

import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Called when entities are loaded.
 * <br>
 * The provided chunk may or may not be loaded.
 */
public interface EntitiesLoadEvent extends ChunkEvent {

    /**
     * Get the entities which are being loaded.
     *
     * @return unmodifiable list of loaded entities.
     */
    @Unmodifiable List<Entity> getEntities();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
