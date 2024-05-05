package org.bukkit.craftbukkit.entity;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectList;
import net.minecraft.world.entity.projectile.EntityTippedArrow;
import net.minecraft.world.item.alchemy.PotionContents;
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

public class CraftArrow extends CraftAbstractArrow implements Arrow {

    public CraftArrow(CraftServer server, EntityTippedArrow entity) {
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
        if (hasCustomEffect(effect.getType())) {
            if (!override) {
                return false;
            }
            removeCustomEffect(effect.getType());
        }
        getHandle().addEffect(CraftPotionUtil.fromBukkit(effect));
        getHandle().updateColor();
        return true;
    }

    @Override
    public void clearCustomEffects() {
        PotionContents old = getHandle().getPotionContents();
        getHandle().setPotionContents(new PotionContents(old.potion(), old.customColor(), List.of()));
        getHandle().updateColor();
    }

    @Override
    public List<PotionEffect> getCustomEffects() {
        ImmutableList.Builder<PotionEffect> builder = ImmutableList.builder();
        for (MobEffect effect : getHandle().getPotionContents().customEffects()) {
            builder.add(CraftPotionUtil.toBukkit(effect));
        }
        return builder.build();
    }

    @Override
    public boolean hasCustomEffect(PotionEffectType type) {
        for (MobEffect effect : getHandle().getPotionContents().customEffects()) {
            if (CraftPotionUtil.equals(effect.getEffect(), type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasCustomEffects() {
        return !getHandle().getPotionContents().customEffects().isEmpty();
    }

    @Override
    public boolean removeCustomEffect(PotionEffectType effect) {
        if (!hasCustomEffect(effect)) {
            return false;
        }
        Holder<MobEffectList> minecraft = CraftPotionEffectType.bukkitToMinecraftHolder(effect);

        PotionContents old = getHandle().getPotionContents();
        getHandle().setPotionContents(new PotionContents(old.potion(), old.customColor(), old.customEffects().stream().filter((mobEffect) -> !mobEffect.getEffect().equals(minecraft)).toList()));
        return true;
    }

    @Override
    public void setBasePotionData(PotionData data) {
        setBasePotionType(CraftPotionUtil.fromBukkit(data));
    }

    @Override
    public PotionData getBasePotionData() {
        return CraftPotionUtil.toBukkit(getBasePotionType());
    }

    @Override
    public void setBasePotionType(PotionType potionType) {
        if (potionType != null) {
            getHandle().setPotionContents(getHandle().getPotionContents().withPotion(CraftPotionType.bukkitToMinecraftHolder(potionType)));
        } else {
            PotionContents old = getHandle().getPotionContents();
            getHandle().setPotionContents(new PotionContents(Optional.empty(), old.customColor(), old.customEffects()));
        }
    }

    @Override
    public PotionType getBasePotionType() {
        return getHandle().getPotionContents().potion().map(CraftPotionType::minecraftHolderToBukkit).orElse(null);
    }

    @Override
    public void setColor(Color color) {
        int colorRGB = (color == null) ? -1 : color.asRGB();
        PotionContents old = getHandle().getPotionContents();
        getHandle().setPotionContents(new PotionContents(old.potion(), Optional.of(colorRGB), old.customEffects()));
    }

    @Override
    public Color getColor() {
        if (getHandle().getColor() <= -1) {
            return null;
        }
        return Color.fromRGB(getHandle().getColor());
    }
}
