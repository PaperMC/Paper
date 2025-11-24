package io.papermc.paper.event.entity;

import io.papermc.paper.world.WeatheringCopperState;
import org.bukkit.entity.CopperGolem;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a {@link CopperGolem} transitions from one {@link WeatheringCopperState} to another.
 */
@NullMarked
public class CopperGolemWeatheringEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final WeatheringCopperState currentWeatherState;
    private final WeatheringCopperState nextWeatherState;

    private boolean cancelled;

    @ApiStatus.Internal
    public CopperGolemWeatheringEvent(final CopperGolem golem, final WeatheringCopperState currentWeatherState, final WeatheringCopperState nextWeatherState) {
        super(golem);
        this.currentWeatherState = currentWeatherState;
        this.nextWeatherState = nextWeatherState;
    }

    /**
     * Gets the copper golem entity that is about to weather.
     *
     * @return The copper golem entity about to weather.
     */
    @Override
    public CopperGolem getEntity() {
        return (CopperGolem) super.getEntity();
    }

    /**
     * Gets the current weathering state of the copper golem.
     *
     * @return The current weathering state.
     */
    public WeatheringCopperState getCurrentWeatheringState() {
        return currentWeatherState;
    }

    /**
     * Gets the next weathering state the copper golem will transition to.
     *
     * @return The next weathering state.
     */
    public WeatheringCopperState getNextWeatheringState() {
        return nextWeatherState;
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled;
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
