package io.papermc.paper.event.entity;

import io.papermc.paper.event.ReceiveGameEvent;
import org.bukkit.GameEvent;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called when an entity receives a game event and hence might take some action.
 * <p>
 * Will be called cancelled if the entity's default behavior is to ignore the
 * event.
 */
@NullMarked
public class EntityReceiveGameEvent extends EntityEvent implements ReceiveGameEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final GameEvent event;
    private final @Nullable Entity triggerEntity;
    private final @Nullable BlockData triggerBlockData;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityReceiveGameEvent(final Entity target, final GameEvent event, final @Nullable Entity triggerEntity, final @Nullable BlockData triggerBlockData) {
        super(target);
        this.event = event;
        this.triggerEntity = triggerEntity;
        this.triggerBlockData = triggerBlockData;
    }

    /**
     * Gets the entity which received the game event, not to be
     * confused with the entity which may have caused the game event.
     *
     * @see #getTriggerEntity()
     */
    @Override
    public Entity getEntity() {
        return super.getEntity();
    }

    @Override
    public GameEvent getEvent() {
        return this.event;
    }

    @Override
    public @Nullable Entity getTriggerEntity() {
        return this.triggerEntity;
    }

    @Override
    public @Nullable BlockData getTriggerBlockData() {
        return this.triggerBlockData;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
