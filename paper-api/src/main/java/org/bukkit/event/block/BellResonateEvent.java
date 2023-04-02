package org.bukkit.event.block;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a bell resonated after being rung and highlights nearby raiders.
 * A bell will only resonate if raiders are in the vicinity of the bell.
 */
public class BellResonateEvent extends BlockEvent {

    private static final HandlerList handlers = new HandlerList();
    private final List<LivingEntity> resonatedEntities;

    public BellResonateEvent(@NotNull Block theBlock, @NotNull List<LivingEntity> resonatedEntities) {
        super(theBlock);
        this.resonatedEntities = resonatedEntities;
    }

    /**
     * Get a mutable list of all {@link LivingEntity LivingEntities} to be
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
    @NotNull
    public List<LivingEntity> getResonatedEntities() {
        return resonatedEntities;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
