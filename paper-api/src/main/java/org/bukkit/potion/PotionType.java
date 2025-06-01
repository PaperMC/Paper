package org.bukkit.potion;

import com.google.common.base.Suppliers;
import java.util.List;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This enum reflects and matches each potion state that can be obtained from
 * the Creative mode inventory
 */
public enum PotionType implements Keyed, io.papermc.paper.world.flag.FeatureDependant { // Paper - feature flag API
    // Start generate - PotionType
    // @GeneratedFrom 1.21.6-pre1
    AWKWARD("awkward"),
    FIRE_RESISTANCE("fire_resistance"),
    HARMING("harming"),
    HEALING("healing"),
    INFESTED("infested"),
    INVISIBILITY("invisibility"),
    LEAPING("leaping"),
    LONG_FIRE_RESISTANCE("long_fire_resistance"),
    LONG_INVISIBILITY("long_invisibility"),
    LONG_LEAPING("long_leaping"),
    LONG_NIGHT_VISION("long_night_vision"),
    LONG_POISON("long_poison"),
    LONG_REGENERATION("long_regeneration"),
    LONG_SLOW_FALLING("long_slow_falling"),
    LONG_SLOWNESS("long_slowness"),
    LONG_STRENGTH("long_strength"),
    LONG_SWIFTNESS("long_swiftness"),
    LONG_TURTLE_MASTER("long_turtle_master"),
    LONG_WATER_BREATHING("long_water_breathing"),
    LONG_WEAKNESS("long_weakness"),
    LUCK("luck"),
    MUNDANE("mundane"),
    NIGHT_VISION("night_vision"),
    OOZING("oozing"),
    POISON("poison"),
    REGENERATION("regeneration"),
    SLOW_FALLING("slow_falling"),
    SLOWNESS("slowness"),
    STRENGTH("strength"),
    STRONG_HARMING("strong_harming"),
    STRONG_HEALING("strong_healing"),
    STRONG_LEAPING("strong_leaping"),
    STRONG_POISON("strong_poison"),
    STRONG_REGENERATION("strong_regeneration"),
    STRONG_SLOWNESS("strong_slowness"),
    STRONG_STRENGTH("strong_strength"),
    STRONG_SWIFTNESS("strong_swiftness"),
    STRONG_TURTLE_MASTER("strong_turtle_master"),
    SWIFTNESS("swiftness"),
    THICK("thick"),
    TURTLE_MASTER("turtle_master"),
    WATER("water"),
    WATER_BREATHING("water_breathing"),
    WEAKNESS("weakness"),
    WEAVING("weaving"),
    WIND_CHARGED("wind_charged");
    // End generate - PotionType

    private final NamespacedKey key;
    private final Supplier<InternalPotionData> internalPotionDataSupplier;

    PotionType(String key) {
        this.key = NamespacedKey.minecraft(key);
        this.internalPotionDataSupplier = Suppliers.memoize(() -> Bukkit.getUnsafe().getInternalPotionData(this.key));
    }

    /**
     * @return the potion effect type of this potion type
     * @deprecated Potions can have multiple effects use {@link #getPotionEffects()}
     */
    @Nullable
    @Deprecated(since = "1.20.2")
    public PotionEffectType getEffectType() {
        return internalPotionDataSupplier.get().getEffectType();
    }

    /**
     * @return a list of all effects this potion type has
     */
    @NotNull
    public List<PotionEffect> getPotionEffects() {
        return internalPotionDataSupplier.get().getPotionEffects();
    }

    /**
     * @return if this potion type is instant
     * @deprecated PotionType can have multiple effects, some of which can be instant and others not.
     * Use {@link PotionEffectType#isInstant()} in combination with {@link #getPotionEffects()} and {@link PotionEffect#getType()}
     */
    @Deprecated(since = "1.20.2")
    public boolean isInstant() {
        return internalPotionDataSupplier.get().isInstant();
    }

    /**
     * Checks if the potion type has an upgraded state.
     * This refers to whether or not the potion type can be Tier 2,
     * such as Potion of Fire Resistance II.
     *
     * @return true if the potion type can be upgraded;
     */
    public boolean isUpgradeable() {
        return internalPotionDataSupplier.get().isUpgradeable();
    }

    /**
     * Checks if the potion type has an extended state.
     * This refers to the extended duration potions
     *
     * @return true if the potion type can be extended
     */
    public boolean isExtendable() {
        return internalPotionDataSupplier.get().isExtendable();
    }

    public int getMaxLevel() {
        return internalPotionDataSupplier.get().getMaxLevel();
    }

    /**
     * @param effectType the effect to get by
     * @return the matching potion type
     * @deprecated Misleading
     */
    @Deprecated(since = "1.9")
    @Nullable
    public static PotionType getByEffect(@Nullable PotionEffectType effectType) {
        if (effectType == null)
            return WATER;
        for (PotionType type : PotionType.values()) {
            if (effectType.equals(type.getEffectType()))
                return type;
        }
        return null;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    /**
     * @deprecated Do not use, interface will get removed, and the plugin won't run
     */
    @Deprecated(since = "1.20.2", forRemoval = true)
    @ApiStatus.Internal
    public interface InternalPotionData {

        PotionEffectType getEffectType();

        List<PotionEffect> getPotionEffects();

        boolean isInstant();

        boolean isUpgradeable();

        boolean isExtendable();

        int getMaxLevel();
    }
}
