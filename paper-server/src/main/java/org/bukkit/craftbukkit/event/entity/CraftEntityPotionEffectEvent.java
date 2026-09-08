package org.bukkit.craftbukkit.event.entity;

import net.minecraft.Optionull;
import net.minecraft.world.effect.MobEffectInstance;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.Nullable;

public class CraftEntityPotionEffectEvent extends CraftEntityEvent implements EntityPotionEffectEvent {

    private final @Nullable PotionEffect oldEffect;
    private final @Nullable PotionEffect newEffect;
    private final @Nullable Entity entitySource;
    private final Cause cause;
    private final Action action;
    private boolean override;

    private boolean cancelled;

    public CraftEntityPotionEffectEvent(
        final LivingEntity livingEntity,
        final @Nullable PotionEffect oldEffect,
        final @Nullable PotionEffect newEffect,
        final @Nullable Entity entitySource,
        final Cause cause,
        final Action action,
        final boolean override
    ) {
        assert oldEffect != null || newEffect != null;
        super(livingEntity);
        this.oldEffect = oldEffect;
        this.newEffect = newEffect;
        this.entitySource = entitySource;
        this.cause = cause;
        this.action = action;
        this.override = override;
    }

    public CraftEntityPotionEffectEvent(
        final net.minecraft.world.entity.LivingEntity livingEntity,
        final @Nullable MobEffectInstance oldEffect,
        final @Nullable MobEffectInstance newEffect,
        final Cause cause,
        final @Nullable Action knownAction
    ) {
        this(livingEntity, oldEffect, newEffect, null, cause, knownAction, true);
    }

    public CraftEntityPotionEffectEvent(
        final net.minecraft.world.entity.LivingEntity livingEntity,
        final @Nullable MobEffectInstance oldEffect,
        final @Nullable MobEffectInstance newEffect,
        final net.minecraft.world.entity.@Nullable Entity entitySource,
        final Cause cause,
        final @Nullable Action knownAction,
        final boolean override
    ) {
        assert oldEffect != null || newEffect != null;
        this(
            livingEntity.getBukkitEntity(),
            Optionull.map(oldEffect, CraftPotionUtil::toBukkit),
            Optionull.map(newEffect, CraftPotionUtil::toBukkit),
            Optionull.map(entitySource, net.minecraft.world.entity.Entity::getBukkitEntity),
            cause,
            knownAction == null ? computeEffectAction(oldEffect, newEffect) : knownAction,
            override
        );
    }

    private static EntityPotionEffectEvent.Action computeEffectAction(final @Nullable MobEffectInstance oldEffect, final @Nullable MobEffectInstance newEffect) {
        if (oldEffect == null) {
            return EntityPotionEffectEvent.Action.ADDED;
        }
        if (newEffect == null) {
            return EntityPotionEffectEvent.Action.REMOVED;
        }
        return EntityPotionEffectEvent.Action.CHANGED;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    @Override
    public @Nullable PotionEffect getOldEffect() {
        return this.oldEffect;
    }

    @Override
    public @Nullable PotionEffect getNewEffect() {
        return this.newEffect;
    }

    @Override
    public @Nullable Entity getSource() {
        return this.entitySource;
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

    @Override
    public Action getAction() {
        return this.action;
    }

    @Override
    public PotionEffectType getModifiedType() {
        return this.oldEffect == null ? this.newEffect.getType() : this.oldEffect.getType();
    }

    @Override
    public boolean isOverride() {
        return this.override;
    }

    @Override
    public void setOverride(final boolean override) {
        this.override = override;
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
        return EntityPotionEffectEvent.getHandlerList();
    }
}
