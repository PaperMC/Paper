package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectList;
import net.minecraft.world.entity.animal.EntityMushroomCow;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.MushroomCow;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CraftMushroomCow extends CraftCow implements MushroomCow {
    public CraftMushroomCow(CraftServer server, EntityMushroomCow entity) {
        super(server, entity);
    }

    @Override
    public boolean hasEffectsForNextStew() {
        SuspiciousStewEffects stewEffects = this.getHandle().stewEffects;
        return stewEffects != null && !stewEffects.effects().isEmpty();
    }

    @Override
    public List<PotionEffect> getEffectsForNextStew() {
        SuspiciousStewEffects stewEffects = this.getHandle().stewEffects;
        if (stewEffects != null) {
            return stewEffects.effects().stream().map(recordSuspiciousEffect -> CraftPotionUtil.toBukkit(recordSuspiciousEffect.createEffectInstance())).toList();
        }
        return ImmutableList.of();
    }

    @Override
    public boolean addEffectToNextStew(PotionEffect potionEffect, boolean overwrite) {
        Preconditions.checkArgument(potionEffect != null, "PotionEffect cannot be null");
        MobEffect minecraftPotionEffect = CraftPotionUtil.fromBukkit(potionEffect);
        if (!overwrite && this.hasEffectForNextStew(potionEffect.getType())) {
            return false;
        }
        SuspiciousStewEffects stewEffects = this.getHandle().stewEffects;
        if (stewEffects == null) {
            stewEffects = SuspiciousStewEffects.EMPTY;
        }
        SuspiciousStewEffects.a recordSuspiciousEffect = new SuspiciousStewEffects.a(minecraftPotionEffect.getEffect(), minecraftPotionEffect.getDuration());
        this.removeEffectFromNextStew(potionEffect.getType()); // Avoid duplicates of effects
        getHandle().stewEffects = stewEffects.withEffectAdded(recordSuspiciousEffect);
        return true;
    }

    @Override
    public boolean removeEffectFromNextStew(PotionEffectType potionEffectType) {
        Preconditions.checkArgument(potionEffectType != null, "potionEffectType cannot be null");
        if (!hasEffectForNextStew(potionEffectType)) {
            return false;
        }

        SuspiciousStewEffects stewEffects = this.getHandle().stewEffects;
        if (stewEffects == null) {
            return false;
        }

        Holder<MobEffectList> minecraftPotionEffectType = CraftPotionEffectType.bukkitToMinecraftHolder(potionEffectType);
        getHandle().stewEffects = new SuspiciousStewEffects(stewEffects.effects().stream().filter((effect) -> !effect.effect().equals(minecraftPotionEffectType)).toList());
        return true;
    }

    @Override
    public boolean hasEffectForNextStew(PotionEffectType potionEffectType) {
        Preconditions.checkArgument(potionEffectType != null, "potionEffectType cannot be null");
        SuspiciousStewEffects stewEffects = this.getHandle().stewEffects;
        if (stewEffects == null) {
            return false;
        }
        Holder<MobEffectList> minecraftPotionEffectType = CraftPotionEffectType.bukkitToMinecraftHolder(potionEffectType);
        return stewEffects.effects().stream().anyMatch(recordSuspiciousEffect -> recordSuspiciousEffect.effect().equals(minecraftPotionEffectType));
    }

    @Override
    public void clearEffectsForNextStew() {
        this.getHandle().stewEffects = null;
    }

    @Override
    public EntityMushroomCow getHandle() {
        return (EntityMushroomCow) entity;
    }

    @Override
    public Variant getVariant() {
        return Variant.values()[getHandle().getVariant().ordinal()];
    }

    @Override
    public void setVariant(Variant variant) {
        Preconditions.checkArgument(variant != null, "Variant cannot be null");

        getHandle().setVariant(EntityMushroomCow.Type.values()[variant.ordinal()]);
    }

    @Override
    public String toString() {
        return "CraftMushroomCow";
    }
}
