package io.papermc.paper.event.entity;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.common.value.qual.IntRange;

public class PaperEntityEffectTickEvent extends CraftEntityEvent implements EntityEffectTickEvent {

    private final PotionEffectType type;
    private final int amplifier;
    private boolean cancelled;

    public PaperEntityEffectTickEvent(final LivingEntity entity, final PotionEffectType type, final int amplifier) {
        super(entity);
        this.type = type;
        this.amplifier = amplifier;
    }

    public PaperEntityEffectTickEvent(final net.minecraft.world.entity.LivingEntity entity, final Holder<MobEffect> effect, final int amplifier) {
        this(entity.getBukkitEntity(), CraftPotionEffectType.minecraftHolderToBukkit(effect), amplifier);
    }

    public PaperEntityEffectTickEvent(final net.minecraft.world.entity.LivingEntity entity, final MobEffect effect, final int amplifier) {
        this(entity.getBukkitEntity(), CraftPotionEffectType.minecraftToBukkit(effect), amplifier);
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    @Override
    public PotionEffectType getType() {
        return this.type;
    }

    @Override
    public @IntRange(from = MobEffectInstance.MIN_AMPLIFIER, to = MobEffectInstance.MAX_AMPLIFIER) int getAmplifier() {
        return this.amplifier;
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
        return EntityEffectTickEvent.getHandlerList();
    }
}
