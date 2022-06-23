package org.bukkit.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a potion effect is modified on an entity.
 * <p>
 * If the event is cancelled, no change will be made on the entity.
 */
public class EntityPotionEffectEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private final PotionEffect oldEffect;
    private final PotionEffect newEffect;
    private final Cause cause;
    private final Action action;
    private boolean override;

    @Contract("_, null, null, _, _, _ -> fail")
    public EntityPotionEffectEvent(@NotNull LivingEntity livingEntity, @Nullable PotionEffect oldEffect, @Nullable PotionEffect newEffect, @NotNull Cause cause, @NotNull Action action, boolean override) {
        super(livingEntity);
        this.oldEffect = oldEffect;
        this.newEffect = newEffect;
        this.cause = cause;
        this.action = action;
        this.override = override;
    }

    /**
     * Gets the old potion effect of the changed type, which will be removed.
     *
     * @return The old potion effect or null if the entity did not have the
     * changed effect type.
     */
    @Nullable
    public PotionEffect getOldEffect() {
        return oldEffect;
    }

    /**
     * Gets new potion effect of the changed type to be applied.
     *
     * @return The new potion effect or null if the effect of the changed type
     * will be removed.
     */
    @Nullable
    public PotionEffect getNewEffect() {
        return newEffect;
    }

    /**
     * Gets the cause why the effect has changed.
     *
     * @return A Cause value why the effect has changed.
     */
    @NotNull
    public Cause getCause() {
        return cause;
    }

    /**
     * Gets the action which will be performed on the potion effect type.
     *
     * @return An action to be performed on the potion effect type.
     */
    @NotNull
    public Action getAction() {
        return action;
    }

    /**
     * Gets the modified potion effect type.
     *
     * @return The effect type which will be modified on the entity.
     */
    @NotNull
    public PotionEffectType getModifiedType() {
        return (oldEffect == null) ? ((newEffect == null) ? null : newEffect.getType()) : oldEffect.getType();
    }

    /**
     * Returns if the new potion effect will override the old potion effect
     * (Only applicable for the CHANGED Action).
     *
     * @return If the new effect will override the old one.
     */
    public boolean isOverride() {
        return override;
    }

    /**
     * Sets if the new potion effect will override the old potion effect (Only
     * applicable for the CHANGED action).
     *
     * @param override If the new effect will override the old one.
     */
    public void setOverride(boolean override) {
        this.override = override;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
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
     * An enum to specify the action to be performed.
     */
    public enum Action {

        /**
         * When the potion effect is added because the entity didn't have it's
         * type.
         */
        ADDED,
        /**
         * When the entity already had the potion effect type, but the effect is
         * changed.
         */
        CHANGED,
        /**
         * When the effect is removed due to all effects being removed.
         */
        CLEARED,
        /**
         * When the potion effect type is completely removed.
         */
        REMOVED
    }

    /**
     * An enum to specify the cause why an effect was changed.
     */
    public enum Cause {

        /**
         * When the entity stands inside an area effect cloud.
         */
        AREA_EFFECT_CLOUD,
        /**
         * When the entity is hit by an spectral or tipped arrow.
         */
        ARROW,
        /**
         * When the entity is inflicted with a potion effect due to an entity
         * attack (e.g. a cave spider or a shulker bullet).
         */
        ATTACK,
        /**
         * When an entity gets the effect from an axolotl.
         */
        AXOLOTL,
        /**
         * When beacon effects get applied due to the entity being nearby.
         */
        BEACON,
        /**
         * When a potion effect is changed due to the /effect command.
         */
        COMMAND,
        /**
         * When the entity gets the effect from a conduit.
         */
        CONDUIT,
        /**
         * When a conversion from a villager zombie to a villager is started or
         * finished.
         */
        CONVERSION,
        /**
         * When all effects are removed due to death (Note: This is called on
         * respawn, so it's player only!)
         */
        DEATH,
        /**
         * When the entity gets the effect from a dolphin.
         */
        DOLPHIN,
        /**
         * When the effect was removed due to expiration.
         */
        EXPIRATION,
        /**
         * When an effect is inflicted due to food (e.g. when a player eats or a
         * cookie is given to a parrot).
         */
        FOOD,
        /**
         * When an illusion illager makes himself disappear.
         */
        ILLUSION,
        /**
         * When all effects are removed due to a bucket of milk.
         */
        MILK,
        /**
         * When a player gets bad omen after killing a patrol captain.
         */
        PATROL_CAPTAIN,
        /**
         * When a potion effect is modified through the plugin methods.
         */
        PLUGIN,
        /**
         * When the entity drinks a potion.
         */
        POTION_DRINK,
        /**
         * When the entity is inflicted with an effect due to a splash potion.
         */
        POTION_SPLASH,
        /**
         * When a spider gets effects when spawning on hard difficulty.
         */
        SPIDER_SPAWN,
        /**
         * When the entity gets effects from a totem item saving it's life.
         */
        TOTEM,
        /**
         * When the entity gets water breathing by wearing a turtle helmet.
         */
        TURTLE_HELMET,
        /**
         * When the Cause is missing.
         */
        UNKNOWN,
        /**
         * When a villager gets regeneration after a trade.
         */
        VILLAGER_TRADE,
        /**
         * When an entity gets the effect from a warden.
         */
        WARDEN,
        /**
         * When an entity comes in contact with a wither rose.
         */
        WITHER_ROSE
    }
}
