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
        this.entitySource = entitySource;
        this.cause = cause;
        this.action = action;
        this.override = override;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) super.getEntity();
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
     * Gets the entity which caused the effect to change
     * (Not applicable for {@link Action#REMOVED}).
     *
     * @return The entity which caused the effect to change or {@code null}
     */
    public @Nullable Entity getSource() {
        return this.entitySource;
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
         * When the potion effect is added because an entity didn't have its
         * type.
         */
        ADDED,
        /**
         * When an entity already had the potion effect type, but the effect is
         * changed.
         */
        CHANGED,
        /**
         * When the potion effect is removed as part of the removal of all effects.
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
         * When an entity stands inside an effect cloud.
         */
        EFFECT_CLOUD,
        /**
         * When an entity is hit by a spectral or tipped arrow.
         */
        ARROW,
        /**
         * When an entity is inflicted with a potion effect due to an entity
         * attack (e.g. a cave spider or a shulker bullet).
         */
        ATTACK,
        /**
         * When an entity gets a positive effect or a negative effect is removed as a gift made by another entity (like a dolphin or an axolotl).
         */
        ENTITY_GIFT,
        /**
         * When an entity gets the effect from an axolotl.
         *
         * @deprecated use {@link #ENTITY_GIFT} and check the source with {@link #getSource()}
         */
        @Deprecated(since = "26.2")
        AXOLOTL,
        /**
         * When an entity plays dead (currently only for the axolotl).
         */
        FAKE_DEATH,
        /**
         * When an entity self consume an item.
         */
        SELF_CONSUME,
        /**
         * When an entity consume an item from another entity.
         */
        CONSUME_OTHER,
        /**
         * When beacon effects get applied due to an entity being nearby.
         */
        BEACON,
        /**
         * When an entity gets revealed because of a bell.
         */
        BELL,
        /**
         * When an entity comes in contact with a wither rose.
         */
        WITHER_ROSE,
        /**
         * When an entity comes in contact with an eye blossom.
         */
        EYE_BLOSSOM,
        /**
         * When an entity gets the effect from a conduit.
         */
        CONDUIT,
        /**
         * When a conversion started or finished (e.g. a villager zombie to a villager) or a cube mob
         * is split.
         */
        CONVERSION,
        /**
         * When all effects are removed due to death.
         */
        DEATH,
        /**
         * When an entity gets the effect from a dolphin.
         *
         * @deprecated use {@link #ENTITY_GIFT} and check the source with {@link #getSource()}
         */
        @Deprecated(since = "26.2")
        DOLPHIN,
        /**
         * When the effect was removed due to expiration.
         */
        EXPIRATION,
        /**
         * When an effect is inflicted due to food (e.g. when a player eats or a
         * cookie is given to a parrot).
         *
         * @deprecated use {@link #SELF_CONSUME} and check the item in hand if needed or {@link #CONSUME_OTHER}
         */
        @Deprecated(since = "26.2")
        FOOD,
        /**
         * When an illusion illager makes himself disappear.
         */
        ILLUSION,
        /**
         * When all effects are removed due to a bucket of milk.
         *
         * @deprecated use {@link #SELF_CONSUME} and check the item in hand if needed
         */
        @Deprecated(since = "26.2")
        MILK,
        /**
         * When an entity gets the effect from a nautilus.
         */
        NAUTILUS, // TODO: should be ENTITY_GIFT but for now the source is not set (see MC-309103)
        /**
         * When a player gets bad omen after killing a patrol captain.
         *
         * @deprecated no longer used, player now gets an ominous bottle instead
         */
        @Deprecated(since = "1.21", forRemoval = true)
        PATROL_CAPTAIN,
        /**
         * When an entity drinks a potion.
         *
         * @deprecated use {@link #SELF_CONSUME} and check the item in hand if needed
         */
        @Deprecated(since = "26.2")
        POTION_DRINK,
        /**
         * When an entity is inflicted with an effect due to a splash potion.
         */
        POTION_SPLASH,
        /**
         * When the effect is caused by a raid.
         * For example when a player gets close to a village with {@link PotionEffectType#BAD_OMEN} and gets {@link PotionEffectType#RAID_OMEN}
         * or the raid start or finish with a hero.
         */
        RAID,
        /**
         * When a player gets close to a trial spawner with {@link PotionEffectType#BAD_OMEN} and gets {@link PotionEffectType#TRIAL_OMEN}.
         */
        TRIAL_STARTED,
        /**
         * When a spider gets effects when spawning on hard difficulty.
         */
        SPIDER_SPAWN, // possibly move to a more generic ENTITY_SPAWN later
        /**
         * When an entity gets effects from a totem item saving its life.
         *
         * @deprecated use {@link #SELF_CONSUME} and check the used item in {@link EntityResurrectEvent}
         */
        @Deprecated(since = "26.2")
        TOTEM,
        /**
         * When an entity gets water breathing by wearing a turtle helmet.
         */
        TURTLE_HELMET,
        /**
         * When a villager gets regeneration after a trade.
         */
        VILLAGER_TRADE,
        /**
         * When an entity gets the effect from a warden.
         */
        WARDEN,
        /**
         * When a potion effect is changed due to the /effect or /raid command.
         */
        COMMAND,
        /**
         * When a potion effect is modified through the plugin methods.
         */
        PLUGIN,
        /**
         * When the Cause is missing.
         */
        UNKNOWN;

        /**
         * When an entity stands inside an area effect cloud.
         *
         * @deprecated use {@link #EFFECT_CLOUD} and check the source with {@link #getSource()}
         */
        @Deprecated(since = "26.2")
        public static final Cause AREA_EFFECT_CLOUD = EFFECT_CLOUD;
    }
}
