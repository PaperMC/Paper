package org.bukkit.enchantments;

import com.google.common.collect.Lists;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import java.util.Locale;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Translatable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The various type of enchantments that may be added to armour or weapons
 */
public abstract class Enchantment implements Keyed, Translatable, net.kyori.adventure.translation.Translatable { // Paper - Adventure translations
    /**
     * Provides protection against environmental damage
     */
    public static final Enchantment PROTECTION = getEnchantment("protection");

    /**
     * Provides protection against fire damage
     */
    public static final Enchantment FIRE_PROTECTION = getEnchantment("fire_protection");

    /**
     * Provides protection against fall damage
     */
    public static final Enchantment FEATHER_FALLING = getEnchantment("feather_falling");

    /**
     * Provides protection against explosive damage
     */
    public static final Enchantment BLAST_PROTECTION = getEnchantment("blast_protection");

    /**
     * Provides protection against projectile damage
     */
    public static final Enchantment PROJECTILE_PROTECTION = getEnchantment("projectile_protection");

    /**
     * Decreases the rate of air loss whilst underwater
     */
    public static final Enchantment RESPIRATION = getEnchantment("respiration");

    /**
     * Increases the speed at which a player may mine underwater
     */
    public static final Enchantment AQUA_AFFINITY = getEnchantment("aqua_affinity");

    /**
     * Damages the attacker
     */
    public static final Enchantment THORNS = getEnchantment("thorns");

    /**
     * Increases walking speed while in water
     */
    public static final Enchantment DEPTH_STRIDER = getEnchantment("depth_strider");

    /**
     * Freezes any still water adjacent to ice / frost which player is walking on
     */
    public static final Enchantment FROST_WALKER = getEnchantment("frost_walker");

    /**
     * Item cannot be removed
     */
    public static final Enchantment BINDING_CURSE = getEnchantment("binding_curse");

    /**
     * Increases damage against all targets
     */
    public static final Enchantment SHARPNESS = getEnchantment("sharpness");

    /**
     * Increases damage against undead targets
     */
    public static final Enchantment SMITE = getEnchantment("smite");

    /**
     * Increases damage against arthropod targets
     */
    public static final Enchantment BANE_OF_ARTHROPODS = getEnchantment("bane_of_arthropods");

    /**
     * All damage to other targets will knock them back when hit
     */
    public static final Enchantment KNOCKBACK = getEnchantment("knockback");

    /**
     * When attacking a target, has a chance to set them on fire
     */
    public static final Enchantment FIRE_ASPECT = getEnchantment("fire_aspect");

    /**
     * Provides a chance of gaining extra loot when killing monsters
     */
    public static final Enchantment LOOTING = getEnchantment("looting");

    /**
     * Increases damage against targets when using a sweep attack
     */
    public static final Enchantment SWEEPING_EDGE = getEnchantment("sweeping_edge");

    /**
     * Increases the rate at which you mine/dig
     */
    public static final Enchantment EFFICIENCY = getEnchantment("efficiency");

    /**
     * Allows blocks to drop themselves instead of fragments (for example,
     * stone instead of cobblestone)
     */
    public static final Enchantment SILK_TOUCH = getEnchantment("silk_touch");

    /**
     * Decreases the rate at which a tool looses durability
     */
    public static final Enchantment UNBREAKING = getEnchantment("unbreaking");

    /**
     * Provides a chance of gaining extra loot when destroying blocks
     */
    public static final Enchantment FORTUNE = getEnchantment("fortune");

    /**
     * Provides extra damage when shooting arrows from bows
     */
    public static final Enchantment POWER = getEnchantment("power");

    /**
     * Provides a knockback when an entity is hit by an arrow from a bow
     */
    public static final Enchantment PUNCH = getEnchantment("punch");

    /**
     * Sets entities on fire when hit by arrows shot from a bow
     */
    public static final Enchantment FLAME = getEnchantment("flame");

    /**
     * Provides infinite arrows when shooting a bow
     */
    public static final Enchantment INFINITY = getEnchantment("infinity");

    /**
     * Decreases odds of catching worthless junk
     */
    public static final Enchantment LUCK_OF_THE_SEA = getEnchantment("luck_of_the_sea");

    /**
     * Increases rate of fish biting your hook
     */
    public static final Enchantment LURE = getEnchantment("lure");

    /**
     * Causes a thrown trident to return to the player who threw it
     */
    public static final Enchantment LOYALTY = getEnchantment("loyalty");

    /**
     * Deals more damage to mobs that live in the ocean
     */
    public static final Enchantment IMPALING = getEnchantment("impaling");

    /**
     * When it is rainy, launches the player in the direction their trident is thrown
     */
    public static final Enchantment RIPTIDE = getEnchantment("riptide");

    /**
     * Strikes lightning when a mob is hit with a trident if conditions are
     * stormy
     */
    public static final Enchantment CHANNELING = getEnchantment("channeling");

    /**
     * Shoot multiple arrows from crossbows
     */
    public static final Enchantment MULTISHOT = getEnchantment("multishot");

    /**
     * Charges crossbows quickly
     */
    public static final Enchantment QUICK_CHARGE = getEnchantment("quick_charge");

    /**
     * Crossbow projectiles pierce entities
     */
    public static final Enchantment PIERCING = getEnchantment("piercing");

    /**
     * Increases fall damage of maces
     */
    public static final Enchantment DENSITY = getEnchantment("density");

    /**
     * Reduces armor effectiveness against maces
     */
    public static final Enchantment BREACH = getEnchantment("breach");

    /**
     * Emits wind burst upon hitting enemy
     */
    public static final Enchantment WIND_BURST = getEnchantment("wind_burst");

    /**
     * Allows mending the item using experience orbs
     */
    public static final Enchantment MENDING = getEnchantment("mending");

    /**
     * Item disappears instead of dropping
     */
    public static final Enchantment VANISHING_CURSE = getEnchantment("vanishing_curse");

    /**
     * Walk quicker on soul blocks
     */
    public static final Enchantment SOUL_SPEED = getEnchantment("soul_speed");

    /**
     * Walk quicker while sneaking
     */
    public static final Enchantment SWIFT_SNEAK = getEnchantment("swift_sneak");

    @NotNull
    private static Enchantment getEnchantment(@NotNull String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).getOrThrow(NamespacedKey.minecraft(key));
    }

    /**
     * Gets the unique name of this enchantment
     *
     * @return Unique name
     * @deprecated enchantments are badly named, use {@link #getKey()}.
     */
    @NotNull
    @Deprecated(since = "1.13", forRemoval = true)
    public abstract String getName();

    /**
     * Gets the maximum level that this Enchantment may become.
     *
     * @return Maximum level of the Enchantment
     */
    public abstract int getMaxLevel();

    /**
     * Gets the level that this Enchantment should start at
     *
     * @return Starting level of the Enchantment
     */
    public abstract int getStartLevel();

    /**
     * Gets the type of {@link ItemStack} that may fit this Enchantment.
     *
     * @return Target type of the Enchantment
     * @deprecated enchantment groupings are now managed by tags, not categories
     */
    @NotNull
    @Deprecated(since = "1.20.5", forRemoval = true) @org.jetbrains.annotations.Contract("-> fail") // Paper
    public abstract EnchantmentTarget getItemTarget();

    /**
     * Checks if this enchantment is a treasure enchantment.
     * <br>
     * Treasure enchantments can only be received via looting, trading, or
     * fishing.
     *
     * @return true if the enchantment is a treasure enchantment
     * @deprecated enchantment types are now managed by tags
     */
    @Deprecated(since = "1.21")
    public abstract boolean isTreasure();

    /**
     * Checks if this enchantment is a cursed enchantment
     * <br>
     * Cursed enchantments are found the same way treasure enchantments are
     *
     * @return true if the enchantment is cursed
     */
    public abstract boolean isCursed();

    /**
     * Check if this enchantment conflicts with another enchantment.
     *
     * @param other The enchantment to check against
     * @return True if there is a conflict.
     */
    public abstract boolean conflictsWith(@NotNull Enchantment other);

    /**
     * Checks if this Enchantment may be applied to the given {@link
     * ItemStack}.
     * <p>
     * This does not check if it conflicts with any enchantments already
     * applied to the item.
     *
     * @param item Item to test
     * @return True if the enchantment may be applied, otherwise False
     */
    public abstract boolean canEnchantItem(@NotNull ItemStack item);
    // Paper start
    /**
     * Get the name of the enchantment with its applied level.
     * <p>
     * If the given {@code level} is either less than the {@link #getStartLevel()} or greater than the {@link #getMaxLevel()},
     * the level may not be shown in the numeral format one may otherwise expect.
     * </p>
     *
     * @param level the level of the enchantment to show
     * @return the name of the enchantment with {@code level} applied
     */
    public abstract net.kyori.adventure.text.@NotNull Component displayName(int level);
    // Paper end

    // Paper start - more Enchantment API
    /**
     * Checks if this enchantment can be found in villager trades.
     *
     * @return true if the enchantment can be found in trades
     */
    public abstract boolean isTradeable();

    /**
     * Checks if this enchantment can be found in an enchanting table
     * or use to enchant items generated by loot tables.
     *
     * @return true if the enchantment can be found in a table or by loot tables
     */
    public abstract boolean isDiscoverable();

    /**
     * Gets the minimum modified cost of this enchantment at a specific level.
     * <p>
     * Note this is not the number of experience levels needed, and does not directly translate to the levels shown in an enchanting table.
     * This value is used in combination with factors such as tool enchantability to determine a final cost.
     * See <a href="https://minecraft.wiki/w/Enchanting/Levels">https://minecraft.wiki/w/Enchanting/Levels</a> for more information.
     * </p>
     * @param level The level of the enchantment
     * @return The modified cost of this enchantment
     */
    public abstract int getMinModifiedCost(int level);

    /**
     * Gets the maximum modified cost of this enchantment at a specific level.
     * <p>
     * Note this is not the number of experience levels needed, and does not directly translate to the levels shown in an enchanting table.
     * This value is used in combination with factors such as tool enchantability to determine a final cost.
     * See <a href="https://minecraft.wiki/w/Enchanting/Levels">https://minecraft.wiki/w/Enchanting/Levels</a> for more information.
     * </p>
     * @param level The level of the enchantment
     * @return The modified cost of this enchantment
     */
    public abstract int getMaxModifiedCost(int level);

    /**
     * Gets cost of applying this enchantment using an anvil.
     * <p>
     * Note that this is halved when using an enchantment book, and is multiplied by the level of the enchantment.
     * See <a href="https://minecraft.wiki/w/Anvil_mechanics">https://minecraft.wiki/w/Anvil_mechanics</a> for more information.
     * </p>
     * @return The anvil cost of this enchantment
     */
    public abstract int getAnvilCost();

    /**
     * Gets the rarity of this enchantment.
     *
     * @return the rarity
     * @deprecated As of 1.20.5 enchantments do not have a rarity.
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "1.20.5")
    @Contract("-> fail")
    public abstract io.papermc.paper.enchantments.EnchantmentRarity getRarity();

    /**
     * Gets the damage increase as a result of the level and entity category specified
     *
     * @param level the level of enchantment
     * @param entityCategory the category of entity
     * @return the damage increase
     * @deprecated Enchantments now have a complex effect systems that cannot be reduced to a simple damage increase.
     */
    @Contract("_, _ -> fail")
    @Deprecated(forRemoval = true, since = "1.20.5")
    public abstract float getDamageIncrease(int level, @NotNull org.bukkit.entity.EntityCategory entityCategory);

    /**
     * Gets the damage increase as a result of the level and entity type specified
     *
     * @param level the level of enchantment
     * @param entityType the type of entity.
     * @return the damage increase
     * @deprecated Enchantments now have a complex effect systems that cannot be reduced to a simple damage increase.
     */
    @Contract("_, _ -> fail")
    @Deprecated(forRemoval = true, since = "1.21")
    public abstract float getDamageIncrease(int level, @NotNull org.bukkit.entity.EntityType entityType);

    /**
     * Gets the equipment slots where this enchantment is considered "active".
     *
     * @return the equipment slots
     * @deprecated Use {@link #getActiveSlotGroups()} instead as enchantments are now applicable to a group of equipment slots.
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "1.21")
    public java.util.Set<org.bukkit.inventory.EquipmentSlot> getActiveSlots() {
        final java.util.Set<org.bukkit.inventory.EquipmentSlotGroup> slots = this.getActiveSlotGroups();
        return java.util.Arrays.stream(org.bukkit.inventory.EquipmentSlot.values()).filter(e -> {
            for (final org.bukkit.inventory.EquipmentSlotGroup group : slots) {
                if (group.test(e)) return true;
            }
            return false;
        }).collect(java.util.stream.Collectors.toSet());
    }

    /**
     * Gets the equipment slots where this enchantment is considered "active".
     *
     * @return the equipment slots
     */
    @NotNull
    public abstract java.util.Set<org.bukkit.inventory.EquipmentSlotGroup> getActiveSlotGroups();
    // Paper end - more Enchantment API

    // Paper start - even more Enchantment API
    /**
     * Provides the description of this enchantment entry as displayed to the client, e.g. "Sharpness" for the sharpness
     * enchantment.
     *
     * @return the description component.
     */
    public abstract net.kyori.adventure.text.@NotNull Component description();

    /**
     * Provides the registry key set referencing the items this enchantment is supported on.
     *
     * @return the registry key set.
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    public abstract io.papermc.paper.registry.set.@NotNull RegistryKeySet<org.bukkit.inventory.ItemType> getSupportedItems();

    /**
     * Provides the registry key set referencing the item types this enchantment can be applied to when
     * enchanting in an enchantment table.
     * <p>
     * If this value is {@code null}, {@link #getSupportedItems()} will be sourced instead in the context of an enchantment table.
     * Additionally, the tag {@link io.papermc.paper.registry.keys.tags.EnchantmentTagKeys#IN_ENCHANTING_TABLE} defines
     * which enchantments can even show up in an enchantment table.
     *
     * @return the registry key set.
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    public abstract io.papermc.paper.registry.set.@Nullable RegistryKeySet<org.bukkit.inventory.ItemType> getPrimaryItems();

    /**
     * Provides the weight of this enchantment used by the weighted random when selecting enchantments.
     *
     * @return the weight value.
     * @see <a href="https://minecraft.wiki/w/Enchanting">https://minecraft.wiki/w/Enchanting</a> for examplary weights.
     */
    public abstract int getWeight();

    /**
     * Provides the registry key set of enchantments that this enchantment is exclusive with.
     * <p>
     * Exclusive enchantments prohibit the application of this enchantment to an item if they are already present on
     * said item.
     *
     * @return a registry set of enchantments exclusive to this one.
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    public abstract io.papermc.paper.registry.set.@NotNull RegistryKeySet<Enchantment> getExclusiveWith();
    // Paper end - even more Enchantment API

    // Paper start - mark translation key as deprecated
    /**
     * @deprecated this method assumes that the enchantments description
     * always be a translatable component which is not guaranteed.
     */
    @Override
    @Deprecated(forRemoval = true)
    public abstract @NotNull String translationKey();
    // Paper end - mark translation key as deprecated

    /**
     * Gets the Enchantment at the specified key
     *
     * @param key key to fetch
     * @return Resulting Enchantment, or null if not found
     * @deprecated only for backwards compatibility, use {@link Registry#get(NamespacedKey)} instead
     */
    @Contract("null -> null")
    @Nullable
    @Deprecated(since = "1.20.3")
    public static Enchantment getByKey(@Nullable NamespacedKey key) {
        if (key == null) {
            return null;
        }
        return Registry.ENCHANTMENT.get(key);
    }

    /**
     * Gets the Enchantment at the specified name
     *
     * @param name Name to fetch
     * @return Resulting Enchantment, or null if not found
     * @deprecated enchantments are badly named, use {@link #getByKey(org.bukkit.NamespacedKey)}.
     */
    @Deprecated(since = "1.13")
    @Contract("null -> null")
    @Nullable
    public static Enchantment getByName(@Nullable String name) {
        if (name == null) {
            return null;
        }

        return getByKey(NamespacedKey.fromString(name.toLowerCase(Locale.ROOT)));
    }

    /**
     * Gets an array of all the registered {@link Enchantment}s
     *
     * @return Array of enchantments
     * @deprecated use {@link Registry#iterator() Registry.ENCHANTMENT.iterator()}
     */
    @NotNull
    @Deprecated(since = "1.20.3")
    public static Enchantment[] values() {
        return Lists.newArrayList(Registry.ENCHANTMENT).toArray(new Enchantment[0]);
    }
}
