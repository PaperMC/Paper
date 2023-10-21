package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectList;
import net.minecraft.world.entity.EntityAreaEffectCloud;
import net.minecraft.world.entity.EntityLiving;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.CraftParticle;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.potion.CraftPotionType;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;

public class CraftAreaEffectCloud extends CraftEntity implements AreaEffectCloud {

    public CraftAreaEffectCloud(CraftServer server, EntityAreaEffectCloud entity) {
        super(server, entity);
    }

    @Override
    public EntityAreaEffectCloud getHandle() {
        return (EntityAreaEffectCloud) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftAreaEffectCloud";
    }

    @Override
    public int getDuration() {
        return getHandle().getDuration();
    }

    @Override
    public void setDuration(int duration) {
        getHandle().setDuration(duration);
    }

    @Override
    public int getWaitTime() {
        return getHandle().waitTime;
    }

    @Override
    public void setWaitTime(int waitTime) {
        getHandle().setWaitTime(waitTime);
    }

    @Override
    public int getReapplicationDelay() {
        return getHandle().reapplicationDelay;
    }

    @Override
    public void setReapplicationDelay(int delay) {
        getHandle().reapplicationDelay = delay;
    }

    @Override
    public int getDurationOnUse() {
        return getHandle().durationOnUse;
    }

    @Override
    public void setDurationOnUse(int duration) {
        getHandle().durationOnUse = duration;
    }

    @Override
    public float getRadius() {
        return getHandle().getRadius();
    }

    @Override
    public void setRadius(float radius) {
        getHandle().setRadius(radius);
    }

    @Override
    public float getRadiusOnUse() {
        return getHandle().radiusOnUse;
    }

    @Override
    public void setRadiusOnUse(float radius) {
        getHandle().setRadiusOnUse(radius);
    }

    @Override
    public float getRadiusPerTick() {
        return getHandle().radiusPerTick;
    }

    @Override
    public void setRadiusPerTick(float radius) {
        getHandle().setRadiusPerTick(radius);
    }

    @Override
    public Particle getParticle() {
        return CraftParticle.minecraftToBukkit(getHandle().getParticle().getType());
    }

    @Override
    public void setParticle(Particle particle) {
        setParticle(particle, null);
    }

    @Override
    public <T> void setParticle(Particle particle, T data) {
        particle = CraftParticle.convertLegacy(particle);
        data = CraftParticle.convertLegacy(data);
        if (data != null) {
            Preconditions.checkArgument(particle.getDataType().isInstance(data), "data (%s) should be %s", data.getClass(), particle.getDataType());
        }
        getHandle().setParticle(CraftParticle.createParticleParam(particle, data));
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(getHandle().getColor());
    }

    @Override
    public void setColor(Color color) {
        getHandle().setFixedColor(color.asRGB());
    }

    @Override
    public boolean addCustomEffect(PotionEffect effect, boolean override) {
        MobEffectList minecraft = CraftPotionEffectType.bukkitToMinecraft(effect.getType());
        MobEffect existing = null;
        for (MobEffect mobEffect : getHandle().effects) {
            if (mobEffect.getEffect() == minecraft) {
                existing = mobEffect;
            }
        }
        if (existing != null) {
            if (!override) {
                return false;
            }
            getHandle().effects.remove(existing);
        }
        getHandle().addEffect(CraftPotionUtil.fromBukkit(effect));
        getHandle().updateColor();
        return true;
    }

    @Override
    public void clearCustomEffects() {
        getHandle().effects.clear();
        getHandle().updateColor();
    }

    @Override
    public List<PotionEffect> getCustomEffects() {
        ImmutableList.Builder<PotionEffect> builder = ImmutableList.builder();
        for (MobEffect effect : getHandle().effects) {
            builder.add(CraftPotionUtil.toBukkit(effect));
        }
        return builder.build();
    }

    @Override
    public boolean hasCustomEffect(PotionEffectType type) {
        for (MobEffect effect : getHandle().effects) {
            if (CraftPotionUtil.equals(effect.getEffect(), type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasCustomEffects() {
        return !getHandle().effects.isEmpty();
    }

    @Override
    public boolean removeCustomEffect(PotionEffectType effect) {
        MobEffectList minecraft = CraftPotionEffectType.bukkitToMinecraft(effect);
        MobEffect existing = null;
        for (MobEffect mobEffect : getHandle().effects) {
            if (mobEffect.getEffect() == minecraft) {
                existing = mobEffect;
            }
        }
        if (existing == null) {
            return false;
        }
        getHandle().effects.remove(existing);
        getHandle().updateColor();
        return true;
    }

    @Override
    public void setBasePotionData(PotionData data) {
        Preconditions.checkArgument(data != null, "PotionData cannot be null");

        getHandle().setPotion(CraftPotionType.bukkitToMinecraft(CraftPotionUtil.fromBukkit(data)));
    }

    @Override
    public PotionData getBasePotionData() {
        return CraftPotionUtil.toBukkit(CraftPotionType.minecraftToBukkit(getHandle().getPotion()));
    }

    @Override
    public void setBasePotionType(@NotNull PotionType potionType) {
        // TODO: 10/6/23 Change PotionType.UNCRAFTABLE to PotionType.EMPTY in error message
        Preconditions.checkArgument(potionType != null, "PotionType cannot be null use PotionType.UNCRAFTABLE to represent no effect instead.");

        getHandle().setPotion(CraftPotionType.bukkitToMinecraft(potionType));
    }

    @NotNull
    @Override
    public PotionType getBasePotionType() {
        return CraftPotionType.minecraftToBukkit(getHandle().getPotion());
    }

    @Override
    public ProjectileSource getSource() {
        EntityLiving source = getHandle().getOwner();
        return (source == null) ? null : (LivingEntity) source.getBukkitEntity();
    }

    @Override
    public void setSource(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity craftLivingEntity) {
            getHandle().setOwner(craftLivingEntity.getHandle());
        } else {
            getHandle().setOwner(null);
        }
    }
}
