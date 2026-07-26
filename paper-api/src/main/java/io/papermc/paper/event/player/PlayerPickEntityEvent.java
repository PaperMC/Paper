package io.papermc.paper.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Event that is fired when a player uses the pick item functionality on an entity
 * (middle-clicking an entity to get the appropriate item).
 * After the handling of this event, the contents of the source and the target slot will be swapped,
 * and the currently selected hotbar slot of the player will be set to the target slot.
 */
@NullMarked
public class PlayerPickEntityEvent extends PlayerPickItemEvent {

    private final Entity entity;

    @ApiStatus.Internal
    public PlayerPickEntityEvent(final Player player, final Entity entity, final boolean includeData, final int targetSlot, final int sourceSlot) {
        super(player, includeData, targetSlot, sourceSlot);
        this.entity = entity;
    }

    /**
     * Retrieves the entity associated with this event.
     *
     * @return the entity involved in the event
     */
    public Entity getEntity() {
        return this.entity;
    }
}
