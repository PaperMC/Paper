package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectList;
import net.minecraft.world.entity.projectile.EntityTippedArrow;
import org.bukkit.Color;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.potion.CraftPotionType;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.Arrow;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

public class CraftTippedArrow extends CraftArrow implements Arrow {

    public CraftTippedArrow(CraftServer server, EntityTippedArrow entity) {
        super(server, entity);
    }

    @Override
    public EntityTippedArrow getHandle() {
        return (EntityTippedArrow) entity;
    }

    @Override
    public String toString() {
        return "CraftTippedArrow";
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
        this.getHandle().potion = CraftPotionType.bukkitToMinecraft(CraftPotionUtil.fromBukkit(data));
    }

    @Override
    public PotionData getBasePotionData() {
        return CraftPotionUtil.toBukkit(CraftPotionType.minecraftToBukkit(getHandle().potion));
    }

    @Override
    public void setBasePotionType(@NotNull PotionType potionType) {
        // TODO: 10/6/23 Change PotionType.UNCRAFTABLE to PotionType.EMPTY in error message
        Preconditions.checkArgument(potionType != null, "PotionType cannot be null use PotionType.UNCRAFTABLE to represent no effect instead.");

        getHandle().potion = CraftPotionType.bukkitToMinecraft(potionType);
    }

    @NotNull
    @Override
    public PotionType getBasePotionType() {
        return CraftPotionType.minecraftToBukkit(getHandle().potion);
    }

    @Override
    public void setColor(Color color) {
        int colorRGB = (color == null) ? -1 : color.asRGB();
        getHandle().setFixedColor(colorRGB);
    }

    @Override
    public Color getColor() {
        if (getHandle().getColor() <= -1) {
            return null;
        }
        return Color.fromRGB(getHandle().getColor());
    }
}
