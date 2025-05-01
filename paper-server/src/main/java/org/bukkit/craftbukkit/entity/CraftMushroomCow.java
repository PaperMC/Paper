package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.MushroomCow;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CraftMushroomCow extends CraftAbstractCow implements MushroomCow, io.papermc.paper.entity.PaperShearable { // Paper

    public CraftMushroomCow(CraftServer server, net.minecraft.world.entity.animal.MushroomCow entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.MushroomCow getHandle() {
        return (net.minecraft.world.entity.animal.MushroomCow) this.entity;
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
        // Paper start - add overloads to use suspicious effect entry to mushroom cow and suspicious stew meta
        return this.addEffectToNextStew(io.papermc.paper.potion.SuspiciousEffectEntry.create(potionEffect.getType(), potionEffect.getDuration()), overwrite);
    }

    @Override
    public boolean addEffectToNextStew(io.papermc.paper.potion.SuspiciousEffectEntry suspiciousEffectEntry, boolean overwrite) {
        Preconditions.checkArgument(suspiciousEffectEntry != null, "SuspiciousEffectEntry cannot be null");
        Holder<MobEffect> minecraftPotionEffect = CraftPotionEffectType.bukkitToMinecraftHolder(suspiciousEffectEntry.effect());
        // Paper end - add overloads to use suspicious effect entry to mushroom cow and suspicious stew meta
        if (!overwrite && this.hasEffectForNextStew(suspiciousEffectEntry.effect())) {
            return false;
        }
        SuspiciousStewEffects stewEffects = this.getHandle().stewEffects;
        if (stewEffects == null) {
            stewEffects = SuspiciousStewEffects.EMPTY;
        }
        SuspiciousStewEffects.Entry recordSuspiciousEffect = new SuspiciousStewEffects.Entry(minecraftPotionEffect, suspiciousEffectEntry.duration()); // Paper - sus effect entry API
        this.removeEffectFromNextStew(suspiciousEffectEntry.effect()); // Avoid duplicates of effects // Paper - sus effect entry API
        this.getHandle().stewEffects = stewEffects.withEffectAdded(recordSuspiciousEffect);
        return true;
    }

    @Override
    public boolean removeEffectFromNextStew(PotionEffectType potionEffectType) {
        Preconditions.checkArgument(potionEffectType != null, "potionEffectType cannot be null");
        if (!this.hasEffectForNextStew(potionEffectType)) {
            return false;
        }

        SuspiciousStewEffects stewEffects = this.getHandle().stewEffects;
        if (stewEffects == null) {
            return false;
        }

        Holder<MobEffect> minecraftPotionEffectType = CraftPotionEffectType.bukkitToMinecraftHolder(potionEffectType);
        this.getHandle().stewEffects = new SuspiciousStewEffects(stewEffects.effects().stream().filter((effect) -> !effect.effect().equals(minecraftPotionEffectType)).toList());
        return true;
    }

    @Override
    public boolean hasEffectForNextStew(PotionEffectType potionEffectType) {
        Preconditions.checkArgument(potionEffectType != null, "potionEffectType cannot be null");
        SuspiciousStewEffects stewEffects = this.getHandle().stewEffects;
        if (stewEffects == null) {
            return false;
        }
        Holder<MobEffect> minecraftPotionEffectType = CraftPotionEffectType.bukkitToMinecraftHolder(potionEffectType);
        return stewEffects.effects().stream().anyMatch(recordSuspiciousEffect -> recordSuspiciousEffect.effect().equals(minecraftPotionEffectType));
    }

    @Override
    public void clearEffectsForNextStew() {
        this.getHandle().stewEffects = null;
    }

    @Override
    public Variant getVariant() {
        return Variant.values()[this.getHandle().getVariant().ordinal()];
    }

    @Override
    public void setVariant(Variant variant) {
        Preconditions.checkArgument(variant != null, "Variant cannot be null");

        this.getHandle().setVariant(net.minecraft.world.entity.animal.MushroomCow.Variant.values()[variant.ordinal()]);
    }

    // Paper start
    @Override
    public List<io.papermc.paper.potion.SuspiciousEffectEntry> getStewEffects() {
        if (this.getHandle().stewEffects == null) {
            return List.of();
        }

        final List<io.papermc.paper.potion.SuspiciousEffectEntry> effectEntries = new java.util.ArrayList<>(this.getHandle().stewEffects.effects().size());
        for (final SuspiciousStewEffects.Entry effect : this.getHandle().stewEffects.effects()) {
            effectEntries.add(io.papermc.paper.potion.SuspiciousEffectEntry.create(
                org.bukkit.craftbukkit.potion.CraftPotionEffectType.minecraftHolderToBukkit(effect.effect()),
                effect.duration()
            ));
        }

        return java.util.Collections.unmodifiableList(effectEntries);
    }

    @Override
    public void setStewEffects(final List<io.papermc.paper.potion.SuspiciousEffectEntry> effects) {
        if (effects.isEmpty()) {
            this.getHandle().stewEffects = null;
            return;
        }

        List<SuspiciousStewEffects.Entry> nmsPairs = new java.util.ArrayList<>(effects.size());
        for (final io.papermc.paper.potion.SuspiciousEffectEntry effect : effects) {
            nmsPairs.add(new SuspiciousStewEffects.Entry(
                org.bukkit.craftbukkit.potion.CraftPotionEffectType.bukkitToMinecraftHolder(effect.effect()),
                effect.duration()
            ));
        }

        this.getHandle().stewEffects = new SuspiciousStewEffects(nmsPairs);
    }
    // Paper end
}
