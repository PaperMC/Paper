package org.bukkit.craftbukkit.event.entity;

import io.papermc.paper.util.MCUtil;
import java.util.List;
import java.util.function.Function;
import net.minecraft.Optionull;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.craftbukkit.damage.CraftDamageSource;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.util.BoundChecker.requirePositive;

public class CraftEntityDeathEvent extends CraftEntityEvent implements EntityDeathEvent {

    private static final Function<@Nullable ItemStack, Entity.@Nullable DefaultDrop> FROM_FUNCTION = item -> {
        if (item == null) return null;
        return new Entity.DefaultDrop(CraftItemType.bukkitToMinecraft(item.getType()), item, null);
    };

    private final DamageSource damageSource;
    private final List<ItemStack> drops;
    private int droppedExp = 0;

    private double reviveHealth = 0;
    private boolean shouldPlayDeathSound;

    private @Nullable Sound deathSound;
    private @Nullable SoundCategory deathSoundCategory;
    private float deathSoundVolume;
    private float deathSoundPitch;

    private boolean cancelled;

    public CraftEntityDeathEvent(
        final LivingEntity livingEntity,
        final DamageSource damageSource,
        final List<ItemStack> drops,
        final int droppedExp,
        final double reviveHealth,
        final boolean shouldPlayDeathSound,
        final @Nullable Sound deathSound,
        final SoundCategory deathSoundCategory,
        final float deathSoundVolume,
        final float deathSoundPitch
    ) {
        super(livingEntity);
        this.damageSource = damageSource;
        this.drops = drops;
        this.droppedExp = droppedExp;
        this.reviveHealth = reviveHealth;
        this.shouldPlayDeathSound = shouldPlayDeathSound;
        this.deathSound = deathSound;
        this.deathSoundCategory = deathSoundCategory;
        this.deathSoundVolume = deathSoundVolume;
        this.deathSoundPitch = deathSoundPitch;
    }

    public CraftEntityDeathEvent(
        final net.minecraft.world.entity.LivingEntity livingEntity,
        final net.minecraft.world.damagesource.DamageSource damageSource,
        final List<Entity.DefaultDrop> drops,
        final int droppedExp,
        final double reviveHealth,
        final boolean shouldPlayDeathSound,
        final @Nullable SoundEvent deathSound,
        final SoundSource deathSoundSource,
        final float deathSoundVolume,
        final float deathSoundPitch
    ) {
        this(
            livingEntity.getBukkitEntity(),
            new CraftDamageSource(damageSource),
            MCUtil.mutableTransform(drops, Entity.DefaultDrop::stack, FROM_FUNCTION),
            droppedExp,
            reviveHealth,
            shouldPlayDeathSound,
            Optionull.map(deathSound, CraftSound::minecraftToBukkit),
            SoundCategory.valueOf(deathSoundSource.name()),
            deathSoundVolume,
            deathSoundPitch
        );
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
        return this.droppedExp;
    }

    @Override
    public void setDroppedExp(final int exp) {
        this.droppedExp = exp;
    }

    @Override
    public List<ItemStack> getDrops() {
        return this.drops;
    }

    @Override
    public double getReviveHealth() {
        return Math.min(this.reviveHealth, ((CraftLivingEntity) this.entity).getHandle().getAttributeValue(Attributes.MAX_HEALTH));
    }

    @Override
    public void setReviveHealth(final double reviveHealth) throws IllegalArgumentException {
        this.reviveHealth = requirePositive(reviveHealth, "reviveHealth");
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

    // todo this feels overblown better to just provide a way to cancel the sound and plugins play their own sound
    public void playDeathSound(final net.minecraft.world.entity.LivingEntity victim, final net.minecraft.world.damagesource.DamageSource damageSource) {
        if (!this.shouldPlayDeathSound || this.deathSound == null || this.deathSoundCategory == null) {
            return;
        }

        final net.minecraft.world.entity.player.Player source = victim instanceof final net.minecraft.world.entity.player.Player player ? player : null;
        final SoundEvent soundEvent = CraftSound.bukkitToMinecraft(this.deathSound);
        final SoundSource soundSource = SoundSource.valueOf(this.deathSoundCategory.name());
        victim.level().playSound(source, victim.getX(), victim.getY(), victim.getZ(), soundEvent, soundSource, this.deathSoundVolume, this.deathSoundPitch);
        victim.playSecondaryHurtSound(damageSource);
    }
}
