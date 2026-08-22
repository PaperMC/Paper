package org.bukkit.craftbukkit.event.entity;

import java.util.List;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class CraftEntityDeathEvent extends CraftEntityEvent implements EntityDeathEvent {

    private final DamageSource damageSource;
    private final List<ItemStack> drops;
    private int dropExp = 0;

    private double reviveHealth = 0;
    private boolean shouldPlayDeathSound;

    private Sound deathSound;
    private SoundCategory deathSoundCategory;
    private float deathSoundVolume;
    private float deathSoundPitch;

    private boolean cancelled;

    public CraftEntityDeathEvent(final LivingEntity livingEntity, final DamageSource damageSource, final List<ItemStack> drops, final int droppedExp) {
        super(livingEntity);
        this.damageSource = damageSource;
        this.drops = drops;
        this.dropExp = droppedExp;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    @Override
    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    @Override
    public int getDroppedExp() {
        return this.dropExp;
    }

    @Override
    public void setDroppedExp(final int exp) {
        this.dropExp = exp;
    }

    @Override
    public List<ItemStack> getDrops() {
        return this.drops;
    }

    @Override
    public double getReviveHealth() {
        return this.reviveHealth;
    }

    @Override
    public void setReviveHealth(final double reviveHealth) throws IllegalArgumentException {
        final double maxHealth = ((LivingEntity) this.entity).getAttribute(Attribute.MAX_HEALTH).getValue();
        if ((maxHealth != 0 && reviveHealth <= 0) || (reviveHealth > maxHealth)) {
            throw new IllegalArgumentException("Health must be between 0 (exclusive) and " + maxHealth + " (inclusive), but was " + reviveHealth);
        }
        this.reviveHealth = reviveHealth;
    }

    @Override
    public boolean shouldPlayDeathSound() {
        return this.shouldPlayDeathSound;
    }

    @Override
    public void setShouldPlayDeathSound(final boolean playDeathSound) {
        this.shouldPlayDeathSound = playDeathSound;
    }

    @Override
    public @Nullable Sound getDeathSound() {
        return this.deathSound;
    }

    @Override
    public void setDeathSound(final @Nullable Sound sound) {
        this.deathSound = sound;
    }

    @Override
    public @Nullable SoundCategory getDeathSoundCategory() {
        return this.deathSoundCategory;
    }

    @Override
    public void setDeathSoundCategory(final @Nullable SoundCategory soundCategory) {
        this.deathSoundCategory = soundCategory;
    }

    @Override
    public float getDeathSoundVolume() {
        return this.deathSoundVolume;
    }

    @Override
    public void setDeathSoundVolume(final float volume) {
        this.deathSoundVolume = volume;
    }

    @Override
    public float getDeathSoundPitch() {
        return this.deathSoundPitch;
    }

    @Override
    public void setDeathSoundPitch(final float pitch) {
        this.deathSoundPitch = pitch;
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
    public HandlerList getHandlers() {
        return EntityDeathEvent.getHandlerList();
    }
}
