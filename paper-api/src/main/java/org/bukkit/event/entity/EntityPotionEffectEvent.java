package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called when a potion effect is modified on an entity.
 * <p>
 * If the event is cancelled, no change will be made on the entity.
 */
@NullMarked
public class EntityPotionEffectEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final @Nullable PotionEffect oldEffect;
    private final @Nullable PotionEffect newEffect;
    private final @Nullable Entity entitySource;
    private final Cause cause;
    private final Action action;
    private boolean override;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityPotionEffectEvent(final LivingEntity livingEntity, final @Nullable PotionEffect oldEffect, final @Nullable PotionEffect newEffect, final @Nullable Entity entitySource, final Cause cause, final Action action, final boolean override) {
        super(livingEntity);
        this.oldEffect = oldEffect;
        this.newEffect = newEffect;
        this.cause = cause;
        this.action = action;
        this.override = override;
        this.entitySource = entitySource;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) super.getEntity();
    }

    /**
     * Gets the entity which caused the effect change
     * (Not applicable for {@link Action#REMOVED}).
     *
     * @return The entity which caused the effect change or {@code null}
     */
    public @Nullable Entity getSource() {
        return this.entitySource;
    }

    /**
     * Gets the old potion effect of the changed type, which will be removed.
     *
     * @return The old potion effect or {@code null} if the entity did not have the
     * changed effect type.
     */
    public @Nullable PotionEffect getOldEffect() {
        return this.oldEffect;
    }

    /**
     * Gets new potion effect of the changed type to be applied.
     *
     * @return The new potion effect or {@code null} if the effect of the changed type
     * will be removed.
     */
    public @Nullable PotionEffect getNewEffect() {
        return this.newEffect;
    }

    /**
     * Gets the cause why the effect has changed.
     *
     * @return A Cause value why the effect has changed.
     */
    public Cause getCause() {
        return this.cause;
    }

    /**
     * Gets the action which will be performed on the potion effect type.
     *
     * @return An action to be performed on the potion effect type.
     */
    public Action getAction() {
        return this.action;
    }

    /**
     * Gets the modified potion effect type.
     *
     * @return The effect type which will be modified on the entity.
     */
    public PotionEffectType getModifiedType() {
        return this.oldEffect == null ? this.newEffect.getType() : this.oldEffect.getType();
    }

    /**
     * Returns if the new potion effect will override the old potion effect
     * (Only applicable for {@link Action#CHANGED}).
     *
     * @return If the new effect will override the old one.
     */
    public boolean isOverride() {
        return this.override;
    }

    /**
     * Sets if the new potion effect will override the old potion effect
     * (Only applicable for {@link Action#CHANGED}).
     *
     * @param override If the new effect will override the old one.
     */
    public void setOverride(boolean override) {
        this.override = override;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * An enum to specify the action to be performed.
     */
    public enum Action {

        /**
         * When the potion effect is added because the entity didn't have its
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
         * When the entity is hit by a spectral or tipped arrow.
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
         * When all effects are removed due to death.
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
         * When the entity gets the effect from a nautilus.
         */
        NAUTILUS,
        /**
         * When a player gets bad omen after killing a patrol captain.
         *
         * @deprecated no longer used, player now gets an ominous bottle instead
         */
        @Deprecated(since = "1.21", forRemoval = true)
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
         * When the entity gets effects from a totem item saving its life.
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
