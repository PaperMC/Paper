package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Stores data for health-regain events
 */
public class EntityRegainHealthEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private double amount;
    private final RegainReason regainReason;
    private final boolean isFastRegen;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityRegainHealthEvent(@NotNull final Entity entity, final double amount, @NotNull final RegainReason regainReason) {
        this(entity, amount, regainReason, false);
    }

    @ApiStatus.Internal
    public EntityRegainHealthEvent(@NotNull final Entity entity, final double amount, @NotNull final RegainReason regainReason, boolean isFastRegen) {
        super(entity);
        this.amount = amount;
        this.regainReason = regainReason;
        this.isFastRegen = isFastRegen;
    }

    /**
     * Gets the amount of regained health
     *
     * @return The amount of health regained
     */
    public double getAmount() {
        return this.amount;
    }

    /**
     * Sets the amount of regained health
     *
     * @param amount the amount of health the entity will regain
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Gets the reason for why the entity is regaining health
     *
     * @return A RegainReason detailing the reason for the entity regaining
     *     health
     */
    @NotNull
    public RegainReason getRegainReason() {
        return this.regainReason;
    }

    /**
     * Is this event a result of the fast regeneration mechanic
     *
     * @return Whether the event is the result of a fast regeneration mechanic
     */
    public boolean isFastRegen() {
        return this.isFastRegen;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * An enum to specify the type of health regaining that is occurring
     */
    public enum RegainReason {

        /**
         * When a player regains health from regenerating due to Peaceful mode
         * (difficulty=0)
         */
        REGEN,
        /**
         * When a player regains health from regenerating due to their hunger
         * being satisfied
         */
        SATIATED,
        /**
         * When an animal regains health from eating consumables
         */
        EATING,
        /**
         * When an ender dragon regains health from an ender crystal
         */
        ENDER_CRYSTAL,
        /**
         * When a player is healed by a potion or spell
         */
        MAGIC,
        /**
         * When a player is healed over time by a potion or spell
         */
        MAGIC_REGEN,
        /**
         * When a wither is filling its health during spawning
         */
        WITHER_SPAWN,
        /**
         * When an entity is damaged by the Wither potion effect
         */
        WITHER,
        /**
         * Any other reason not covered by the reasons above
         */
        CUSTOM
    }
}
