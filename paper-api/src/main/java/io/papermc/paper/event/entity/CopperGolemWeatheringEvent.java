package io.papermc.paper.event.entity;

import io.papermc.paper.world.WeatheringCopperState;
import org.bukkit.entity.CopperGolem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a Copper Golem's Weathering State changes.
 */
public class CopperGolemWeatheringEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final WeatheringCopperState weatheringCopperState;
    private final WeatheringCopperState previousWeatheringCopperState;
    private final Reason reason;
    private final Player player;
    private boolean cancelled;

    public CopperGolemWeatheringEvent(final @NotNull Entity entity, final @NotNull WeatheringCopperState weatheringCopperState, final @NotNull WeatheringCopperState previousWeatheringCopperState, final @NotNull Reason reason, final @Nullable Player player) {
        super(entity);
        this.weatheringCopperState = weatheringCopperState;
        this.previousWeatheringCopperState = previousWeatheringCopperState;
        this.reason = reason;
        this.player = player;
    }

    @NotNull
    public WeatheringCopperState getWeatheringCopperState() {
        return this.weatheringCopperState;
    }

    @NotNull
    public WeatheringCopperState getPreviousWeatheringCopperState() {
        return this.previousWeatheringCopperState;
    }

    @NotNull
    public Reason getReason() {
        return this.reason;
    }

    /**
     * Gets the player involved in the weathering change, if applicable.
     *
     * @return the player or null
     */
    @Nullable
    public Player getPlayer() {
        return this.player;
    }

    @Override
    @NotNull
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
    @NotNull
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public enum Reason {
        NATURAL,
        AXE,
        LIGHTNING,
        OTHER
    }
}
