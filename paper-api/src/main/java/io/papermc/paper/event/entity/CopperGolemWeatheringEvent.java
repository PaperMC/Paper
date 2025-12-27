package io.papermc.paper.event.entity;

import io.papermc.paper.world.WeatheringCopperState;
import org.bukkit.entity.CopperGolem;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a Copper Golem's Weathering State changes
 * <p>
 * If the event is cancelled, the copper golem weathering state will not change.
 */
public class CopperGolemWeatheringEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final WeatheringCopperState weatheringCopperState;

    private boolean cancelled;

    public CopperGolemWeatheringEvent(final @NotNull Entity entity, final WeatheringCopperState weatheringCopperState) {
        super(entity);
        this.entity = entity;
        this.weatheringCopperState = weatheringCopperState;
    }

    /**
     * Gets the new copper golem's weathering state
     *
     * @return new weathering state
     * @see CopperGolem#getWeatheringState()
     */
    @NotNull
    public WeatheringCopperState getWeatheringCopperState() {
        return weatheringCopperState;
    }

    @Override
    public CopperGolem getEntity() {
        return (CopperGolem) super.getEntity();
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
