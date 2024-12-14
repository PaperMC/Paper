package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Thrown whenever a LivingEntity dies
 *
 * @since 1.0.0 R1
 */
public class EntityDeathEvent extends EntityEvent implements org.bukkit.event.Cancellable {  // Paper - make cancellable
    private static final HandlerList handlers = new HandlerList();
    private final DamageSource damageSource;
    private final List<ItemStack> drops;
    private int dropExp = 0;
    // Paper start - make cancellable
    private boolean cancelled;
    private double reviveHealth = 0;
    private boolean shouldPlayDeathSound;
    @Nullable private org.bukkit.Sound deathSound;
    @Nullable private org.bukkit.SoundCategory deathSoundCategory;
    private float deathSoundVolume;
    private float deathSoundPitch;
    // Paper end

    public EntityDeathEvent(@NotNull final LivingEntity entity, @NotNull DamageSource damageSource, @NotNull final List<ItemStack> drops) {
        this(entity, damageSource, drops, 0);
    }

    public EntityDeathEvent(@NotNull final LivingEntity what, @NotNull DamageSource damageSource, @NotNull final List<ItemStack> drops, final int droppedExp) {
        super(what);
        this.damageSource = damageSource;
        this.drops = drops;
        this.dropExp = droppedExp;
    }

    /**
     * @since 1.1.0 R5
     */
    @NotNull
    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) entity;
    }

    /**
     * Gets the source of damage which caused the death.
     *
     * @return a DamageSource detailing the source of the damage for the death.
     * @since 1.20.6
     */
    @NotNull
    public DamageSource getDamageSource() {
        return damageSource;
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
        return dropExp;
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
        return drops;
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @since 1.12.2
     */
    // Paper start - make cancellable
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * @since 1.12.2
     */
    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    /**
     * Get the amount of health that the entity should revive with after cancelling the event.
     * Set to the entity's max health by default.
     *
     * @return The amount of health
     * @since 1.12.2
     */
    public double getReviveHealth() {
        return reviveHealth;
    }

    /**
     * Set the amount of health that the entity should revive with after cancelling the event.
     * Revive health value must be between 0 (exclusive) and the entity's max health (inclusive).
     *
     * @param reviveHealth The amount of health
     * @throws IllegalArgumentException Thrown if the health is {@literal <= 0 or >} max health
     * @since 1.12.2
     */
    public void setReviveHealth(double reviveHealth) throws IllegalArgumentException {
        double maxHealth = ((LivingEntity) entity).getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue();
        if ((maxHealth != 0 && reviveHealth <= 0) || (reviveHealth > maxHealth)) {
            throw new IllegalArgumentException("Health must be between 0 (exclusive) and " + maxHealth + " (inclusive), but was " + reviveHealth);
        }
        this.reviveHealth = reviveHealth;
    }

    /**
     * Whether or not the death sound should play when the entity dies. If the event is cancelled it does not play!
     *
     * @return Whether or not the death sound should play. Event is called with this set to false if the entity is silent.
     * @since 1.12.2
     */
    public boolean shouldPlayDeathSound() {
        return shouldPlayDeathSound;
    }

    /**
     * Set whether or not the death sound should play when the entity dies. If the event is cancelled it does not play!
     *
     * @param playDeathSound Enable or disable the death sound
     * @since 1.12.2
     */
    public void setShouldPlayDeathSound(boolean playDeathSound) {
        this.shouldPlayDeathSound = playDeathSound;
    }

    /**
     * Get the sound that the entity makes when dying
     *
     * @return The sound that the entity makes
     * @since 1.12.2
     */
    @Nullable
    public org.bukkit.Sound getDeathSound() {
        return deathSound;
    }

    /**
     * Set the sound that the entity makes when dying
     *
     * @param sound The sound that the entity should make when dying
     * @since 1.12.2
     */
    public void setDeathSound(@Nullable org.bukkit.Sound sound) {
        deathSound = sound;
    }

    /**
     * Get the sound category that the death sound should play in
     *
     * @return The sound category
     * @since 1.12.2
     */
    @Nullable
    public org.bukkit.SoundCategory getDeathSoundCategory() {
        return deathSoundCategory;
    }

    /**
     * Set the sound category that the death sound should play in.
     *
     * @param soundCategory The sound category
     * @since 1.12.2
     */
    public void setDeathSoundCategory(@Nullable org.bukkit.SoundCategory soundCategory) {
        this.deathSoundCategory = soundCategory;
    }

    /**
     * Get the volume that the death sound will play at.
     *
     * @return The volume the death sound will play at
     * @since 1.12.2
     */
    public float getDeathSoundVolume() {
        return deathSoundVolume;
    }

    /**
     * Set the volume the death sound should play at. If the event is cancelled this will not play the sound!
     *
     * @param volume The volume the death sound should play at
     * @since 1.12.2
     */
    public void setDeathSoundVolume(float volume) {
        this.deathSoundVolume = volume;
    }

    /**
     * Get the pitch that the death sound will play with.
     *
     * @return The pitch the death sound will play with
     * @since 1.12.2
     */
    public float getDeathSoundPitch() {
        return deathSoundPitch;
    }

    /**
     * Set the pitch that the death sound should play with.
     *
     * @param pitch The pitch the death sound should play with
     * @since 1.12.2
     */
    public void setDeathSoundPitch(float pitch) {
        this.deathSoundPitch = pitch;
    }
    // Paper end
}
