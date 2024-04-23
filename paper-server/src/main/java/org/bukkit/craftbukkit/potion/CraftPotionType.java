package org.bukkit.craftbukkit.potion;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.alchemy.PotionRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class CraftPotionType implements PotionType.InternalPotionData {

    public static PotionType minecraftHolderToBukkit(Holder<PotionRegistry> minecraft) {
        return minecraftToBukkit(minecraft.value());
    }

    public static PotionType minecraftToBukkit(PotionRegistry minecraft) {
        Preconditions.checkArgument(minecraft != null);

        IRegistry<PotionRegistry> registry = CraftRegistry.getMinecraftRegistry(Registries.POTION);
        PotionType bukkit = Registry.POTION.get(CraftNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    public static PotionRegistry bukkitToMinecraft(PotionType bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return CraftRegistry.getMinecraftRegistry(Registries.POTION)
                .getOptional(CraftNamespacedKey.toMinecraft(bukkit.getKey())).orElseThrow();
    }

    public static Holder<PotionRegistry> bukkitToMinecraftHolder(PotionType bukkit) {
        Preconditions.checkArgument(bukkit != null);

        IRegistry<PotionRegistry> registry = CraftRegistry.getMinecraftRegistry(Registries.POTION);

        if (registry.wrapAsHolder(bukkitToMinecraft(bukkit)) instanceof Holder.c<PotionRegistry> holder) {
            return holder;
        }

        throw new IllegalArgumentException("No Reference holder found for " + bukkit
                + ", this can happen if a plugin creates its own sound effect with out properly registering it.");
    }

    public static String bukkitToString(PotionType potionType) {
        Preconditions.checkArgument(potionType != null);

        return potionType.getKey().toString();
    }

    public static PotionType stringToBukkit(String string) {
        Preconditions.checkArgument(string != null);

        return Registry.POTION.get(NamespacedKey.fromString(string));
    }

    private final NamespacedKey key;
    private final PotionRegistry potion;
    private final Supplier<List<PotionEffect>> potionEffects;
    private final Supplier<Boolean> upgradeable;
    private final Supplier<Boolean> extendable;
    private final Supplier<Integer> maxLevel;

    public CraftPotionType(NamespacedKey key, PotionRegistry potion) {
        this.key = key;
        this.potion = potion;
        this.potionEffects = Suppliers.memoize(() -> potion.getEffects().stream().map(CraftPotionUtil::toBukkit).toList());
        this.upgradeable = Suppliers.memoize(() -> Registry.POTION.get(new NamespacedKey(key.getNamespace(), "strong_" + key.getKey())) != null);
        this.extendable = Suppliers.memoize(() -> Registry.POTION.get(new NamespacedKey(key.getNamespace(), "long_" + key.getKey())) != null);
        this.maxLevel = Suppliers.memoize(() -> isUpgradeable() ? 2 : 1);
    }

    @Override
    public PotionEffectType getEffectType() {
        return getPotionEffects().isEmpty() ? null : getPotionEffects().get(0).getType();
    }

    @Override
    public List<PotionEffect> getPotionEffects() {
        return potionEffects.get();
    }

    @Override
    public boolean isInstant() {
        return potion.hasInstantEffects();
    }

    @Override
    public boolean isUpgradeable() {
        return upgradeable.get();
    }

    @Override
    public boolean isExtendable() {
        return extendable.get();
    }

    @Override
    public int getMaxLevel() {
        return maxLevel.get();
    }
}
