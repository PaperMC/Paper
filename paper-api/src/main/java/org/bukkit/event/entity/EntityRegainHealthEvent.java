package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Stores data for health-regain events
 */
public class EntityRegainHealthEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private double amount;
    private final RegainReason regainReason;

    public EntityRegainHealthEvent(@NotNull final Entity entity, final double amount, @NotNull final RegainReason regainReason) {
        super(entity);
        this.amount = amount;
        this.regainReason = regainReason;
    }

    /**
     * Gets the amount of regained health
     *
     * @return The amount of health regained
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of regained health
     *
     * @param amount the amount of health the entity will regain
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    /**
     * Gets the reason for why the entity is regaining health
     *
     * @return A RegainReason detailing the reason for the entity regaining
     *     health
     */
    @NotNull
    public RegainReason getRegainReason() {
        return regainReason;
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
         * When a player regains health from eating consumables
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
