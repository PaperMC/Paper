package org.bukkit.craftbukkit.entity;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
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

    public CraftArrow(CraftServer server, net.minecraft.world.entity.projectile.Arrow entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.Arrow getHandle() {
        return (net.minecraft.world.entity.projectile.Arrow) this.entity;
    }

    @Override
    public String toString() {
        return "CraftTippedArrow";
    }

    @Override
    public boolean addCustomEffect(PotionEffect effect, boolean override) {
        if (this.hasCustomEffect(effect.getType())) {
            if (!override) {
                return false;
            }
            this.removeCustomEffect(effect.getType());
        }
        this.getHandle().addEffect(CraftPotionUtil.fromBukkit(effect));
        this.getHandle().updateColor();
        return true;
    }

    @Override
    public void clearCustomEffects() {
        PotionContents old = this.getHandle().getPotionContents();
        this.getHandle().setPotionContents(new PotionContents(old.potion(), old.customColor(), List.of(), old.customName()));
        this.getHandle().updateColor();
    }

    @Override
    public List<PotionEffect> getCustomEffects() {
        ImmutableList.Builder<PotionEffect> builder = ImmutableList.builder();
        for (MobEffectInstance effect : this.getHandle().getPotionContents().customEffects()) {
            builder.add(CraftPotionUtil.toBukkit(effect));
        }
        return builder.build();
    }

    @Override
    public boolean hasCustomEffect(PotionEffectType type) {
        for (MobEffectInstance effect : this.getHandle().getPotionContents().customEffects()) {
            if (CraftPotionUtil.equals(effect.getEffect(), type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasCustomEffects() {
        return !this.getHandle().getPotionContents().customEffects().isEmpty();
    }

    @Override
    public boolean removeCustomEffect(PotionEffectType effect) {
        if (!this.hasCustomEffect(effect)) {
            return false;
        }
        Holder<MobEffect> minecraft = CraftPotionEffectType.bukkitToMinecraftHolder(effect);

        PotionContents old = this.getHandle().getPotionContents();
        this.getHandle().setPotionContents(new PotionContents(old.potion(), old.customColor(), old.customEffects().stream().filter((mobEffect) -> !mobEffect.getEffect().equals(minecraft)).toList(), old.customName()));
        return true;
    }

    @Override
    public void setBasePotionData(PotionData data) {
        this.setBasePotionType(CraftPotionUtil.fromBukkit(data));
    }

    @Override
    public PotionData getBasePotionData() {
        return CraftPotionUtil.toBukkit(this.getBasePotionType());
    }

    @Override
    public void setBasePotionType(PotionType potionType) {
        if (potionType != null) {
            this.getHandle().setPotionContents(this.getHandle().getPotionContents().withPotion(CraftPotionType.bukkitToMinecraftHolder(potionType)));
        } else {
            PotionContents old = this.getHandle().getPotionContents();
            this.getHandle().setPotionContents(new PotionContents(Optional.empty(), old.customColor(), old.customEffects(), old.customName()));
        }
    }

    @Override
    public PotionType getBasePotionType() {
        return this.getHandle().getPotionContents().potion().map(CraftPotionType::minecraftHolderToBukkit).orElse(null);
    }

    @Override
    public void setColor(Color color) {
        int colorRGB = (color == null) ? -1 : color.asRGB();
        PotionContents old = this.getHandle().getPotionContents();
        this.getHandle().setPotionContents(new PotionContents(old.potion(), Optional.of(colorRGB), old.customEffects(), old.customName()));
    }

    @Override
    public Color getColor() {
        if (this.getHandle().getColor() <= -1) {
            return null;
        }
        return Color.fromRGB(this.getHandle().getColor());
    }
}
