package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * Thrown whenever a living entity dies
 */
public interface EntityDeathEvent extends EntityEventNew, Cancellable {

    @Override
    LivingEntity getEntity();

    /**
     * Gets the source of damage which caused the death.
     *
     * @return a DamageSource detailing the source of the damage for the death.
     */
    DamageSource getDamageSource();

    /**
     * Gets how much EXP should be dropped from this death.
     * <p>
     * This does not indicate how much EXP should be taken from the entity in
     * question, merely how much should be created after its death.
     *
     * @return Amount of EXP to drop.
     */
    int getDroppedExp();

    /**
     * Sets how much EXP should be dropped from this death.
     * <p>
     * This does not indicate how much EXP should be taken from the entity in
     * question, merely how much should be created after its death.
     *
     * @param exp Amount of EXP to drop.
     */
    void setDroppedExp(int exp);

    /**
     * Gets all the items which will drop when the entity dies
     *
     * @return Items to drop when the entity dies
     */
    List<ItemStack> getDrops();

    /**
     * Get the amount of health that the entity should revive with after cancelling the event.
     * Set to the entity's max health by default.
     *
     * @return The amount of health
     */
    double getReviveHealth();

    /**
     * Set the amount of health that the entity should revive with after cancelling the event.
     * Revive health value must be between 0 (exclusive) and the entity's max health (inclusive).
     *
     * @param reviveHealth The amount of health
     * @throws IllegalArgumentException Thrown if the health is {@literal <= 0 or >} max health
     */
    void setReviveHealth(double reviveHealth);

    /**
     * Whether the death sound should play when the entity dies. If the event is cancelled it does not play!
     *
     * @return Whether the death sound should play. Event is called with this set to {@code false} if the entity is silent.
     */
    boolean shouldPlayDeathSound();

    /**
     * Set whether the death sound should play when the entity dies. If the event is cancelled it does not play!
     *
     * @param playDeathSound Enable or disable the death sound
     */
    void setShouldPlayDeathSound(boolean playDeathSound);

    /**
     * Get the sound that the entity makes when dying
     *
     * @return The sound that the entity makes
     */
    @Nullable Sound getDeathSound();

    /**
     * Set the sound that the entity makes when dying
     *
     * @param sound The sound that the entity should make when dying
     */
    void setDeathSound(@Nullable Sound sound);
    /**
     * Get the sound category that the death sound should play in
     *
     * @return The sound category
     */
    @Nullable SoundCategory getDeathSoundCategory();

    /**
     * Set the sound category that the death sound should play in.
     *
     * @param soundCategory The sound category
     */
    void setDeathSoundCategory(@Nullable SoundCategory soundCategory);

    /**
     * Get the volume that the death sound will play at.
     *
     * @return The volume the death sound will play at
     */
    float getDeathSoundVolume();

    /**
     * Set the volume the death sound should play at. If the event is cancelled this will not play the sound!
     *
     * @param volume The volume the death sound should play at
     */
    void setDeathSoundVolume(float volume);

    /**
     * Get the pitch that the death sound will play with.
     *
     * @return The pitch the death sound will play with
     */
    float getDeathSoundPitch();

    /**
     * Set the pitch that the death sound should play with.
     *
     * @param pitch The pitch the death sound should play with
     */
    void setDeathSoundPitch(float pitch);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
