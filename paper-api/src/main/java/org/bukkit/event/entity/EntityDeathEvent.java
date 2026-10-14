package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Thrown whenever a LivingEntity dies
 */
public class EntityDeathEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final DamageSource damageSource;
    private final List<ItemStack> drops;
    private int dropExp = 0;

    private double reviveHealth = 0;
    private boolean shouldPlayDeathSound;
    @Nullable private org.bukkit.Sound deathSound;
    @Nullable private org.bukkit.SoundCategory deathSoundCategory;
    private float deathSoundVolume;
    private float deathSoundPitch;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityDeathEvent(@NotNull final LivingEntity livingEntity, @NotNull DamageSource damageSource, @NotNull final List<ItemStack> drops) {
        this(livingEntity, damageSource, drops, 0);
    }

    @ApiStatus.Internal
    public EntityDeathEvent(@NotNull final LivingEntity livingEntity, @NotNull DamageSource damageSource, @NotNull final List<ItemStack> drops, final int droppedExp) {
        super(livingEntity);
        this.damageSource = damageSource;
        this.drops = drops;
        this.dropExp = droppedExp;
    }

    @NotNull
    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    /**
     * Gets the source of damage which caused the death.
     *
     * @return a DamageSource detailing the source of the damage for the death.
     */
    @NotNull
    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    /**
     * Gets how much EXP should be dropped from this death.
     * <p>
     * This does not indicate how much EXP should be taken from the entity in
     * question, merely how much should be created after its death.
     *
     * @return Amount of EXP to drop.
     */
    public int getDroppedExp() {
        return this.dropExp;
    }

    /**
     * Sets how much EXP should be dropped from this death.
     * <p>
     * This does not indicate how much EXP should be taken from the entity in
     * question, merely how much should be created after its death.
     *
     * @param exp Amount of EXP to drop.
     */
    public void setDroppedExp(int exp) {
        this.dropExp = exp;
    }

    /**
     * Gets all the items which will drop when the entity dies
     *
     * @return Items to drop when the entity dies
     */
    @NotNull
    public List<ItemStack> getDrops() {
        return this.drops;
    }

    /**
     * Get the amount of health that the entity should revive with after cancelling the event.
     * Set to the entity's max health by default.
     *
     * @return The amount of health
     */
    public double getReviveHealth() {
        return this.reviveHealth;
    }

    /**
     * Set the amount of health that the entity should revive with after cancelling the event.
     * Revive health value must be between 0 (exclusive) and the entity's max health (inclusive).
     *
     * @param reviveHealth The amount of health
     * @throws IllegalArgumentException Thrown if the health is {@literal <= 0 or >} max health
     */
    public void setReviveHealth(double reviveHealth) throws IllegalArgumentException {
        double maxHealth = ((LivingEntity) this.entity).getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue();
        if ((maxHealth != 0 && reviveHealth <= 0) || (reviveHealth > maxHealth)) {
            throw new IllegalArgumentException("Health must be between 0 (exclusive) and " + maxHealth + " (inclusive), but was " + reviveHealth);
        }
        this.reviveHealth = reviveHealth;
    }

    /**
     * Whether the death sound should play when the entity dies. If the event is cancelled it does not play!
     *
     * @return Whether the death sound should play. Event is called with this set to {@code false} if the entity is silent.
     */
    public boolean shouldPlayDeathSound() {
        return this.shouldPlayDeathSound;
    }

    /**
     * Set whether the death sound should play when the entity dies. If the event is cancelled it does not play!
     *
     * @param playDeathSound Enable or disable the death sound
     */
    public void setShouldPlayDeathSound(boolean playDeathSound) {
        this.shouldPlayDeathSound = playDeathSound;
    }

    /**
     * Get the sound that the entity makes when dying
     *
     * @return The sound that the entity makes
     */
    @Nullable
    public org.bukkit.Sound getDeathSound() {
        return this.deathSound;
    }

    /**
     * Set the sound that the entity makes when dying
     *
     * @param sound The sound that the entity should make when dying
     */
    public void setDeathSound(@Nullable org.bukkit.Sound sound) {
        this.deathSound = sound;
    }

    /**
     * Get the sound category that the death sound should play in
     *
     * @return The sound category
     */
    @Nullable
    public org.bukkit.SoundCategory getDeathSoundCategory() {
        return this.deathSoundCategory;
    }

    /**
     * Set the sound category that the death sound should play in.
     *
     * @param soundCategory The sound category
     */
    public void setDeathSoundCategory(@Nullable org.bukkit.SoundCategory soundCategory) {
        this.deathSoundCategory = soundCategory;
    }

    /**
     * Get the volume that the death sound will play at.
     *
     * @return The volume the death sound will play at
     */
    public float getDeathSoundVolume() {
        return this.deathSoundVolume;
    }

    /**
     * Set the volume the death sound should play at. If the event is cancelled this will not play the sound!
     *
     * @param volume The volume the death sound should play at
     */
    public void setDeathSoundVolume(float volume) {
        this.deathSoundVolume = volume;
    }

    /**
     * Get the pitch that the death sound will play with.
     *
     * @return The pitch the death sound will play with
     */
    public float getDeathSoundPitch() {
        return this.deathSoundPitch;
    }

    /**
     * Set the pitch that the death sound should play with.
     *
     * @param pitch The pitch the death sound should play with
     */
    public void setDeathSoundPitch(float pitch) {
        this.deathSoundPitch = pitch;
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
}
