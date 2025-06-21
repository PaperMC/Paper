package io.papermc.paper.registry.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.EnchantmentTagKeys;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import org.bukkit.MinecraftExperimental;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla tags for {@link RegistryKey#ENCHANTMENT}.
 *
 * @apiNote The fields provided here are a direct representation of
 * what is available from the vanilla game source. They may be
 * changed (including removals) on any Minecraft version
 * bump, so cross-version compatibility is not provided on the
 * same level as it is on most of the other API.
 */
@SuppressWarnings({
        "unused",
        "SpellCheckingInspection"
})
@NullMarked
@GeneratedFrom("1.21.6")
public final class EnchantmentTags {
    /**
     * {@code #minecraft:curse}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> CURSE = fetch(EnchantmentTagKeys.CURSE);

    /**
     * {@code #minecraft:double_trade_price}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> DOUBLE_TRADE_PRICE = fetch(EnchantmentTagKeys.DOUBLE_TRADE_PRICE);

    /**
     * {@code #minecraft:exclusive_set/armor}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> EXCLUSIVE_SET_ARMOR = fetch(EnchantmentTagKeys.EXCLUSIVE_SET_ARMOR);

    /**
     * {@code #minecraft:exclusive_set/boots}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> EXCLUSIVE_SET_BOOTS = fetch(EnchantmentTagKeys.EXCLUSIVE_SET_BOOTS);

    /**
     * {@code #minecraft:exclusive_set/bow}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> EXCLUSIVE_SET_BOW = fetch(EnchantmentTagKeys.EXCLUSIVE_SET_BOW);

    /**
     * {@code #minecraft:exclusive_set/crossbow}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> EXCLUSIVE_SET_CROSSBOW = fetch(EnchantmentTagKeys.EXCLUSIVE_SET_CROSSBOW);

    /**
     * {@code #minecraft:exclusive_set/damage}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> EXCLUSIVE_SET_DAMAGE = fetch(EnchantmentTagKeys.EXCLUSIVE_SET_DAMAGE);

    /**
     * {@code #minecraft:exclusive_set/mining}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> EXCLUSIVE_SET_MINING = fetch(EnchantmentTagKeys.EXCLUSIVE_SET_MINING);

    /**
     * {@code #minecraft:exclusive_set/riptide}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> EXCLUSIVE_SET_RIPTIDE = fetch(EnchantmentTagKeys.EXCLUSIVE_SET_RIPTIDE);

    /**
     * {@code #minecraft:in_enchanting_table}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> IN_ENCHANTING_TABLE = fetch(EnchantmentTagKeys.IN_ENCHANTING_TABLE);

    /**
     * {@code #minecraft:non_treasure}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> NON_TREASURE = fetch(EnchantmentTagKeys.NON_TREASURE);

    /**
     * {@code #minecraft:on_mob_spawn_equipment}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> ON_MOB_SPAWN_EQUIPMENT = fetch(EnchantmentTagKeys.ON_MOB_SPAWN_EQUIPMENT);

    /**
     * {@code #minecraft:on_random_loot}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> ON_RANDOM_LOOT = fetch(EnchantmentTagKeys.ON_RANDOM_LOOT);

    /**
     * {@code #minecraft:on_traded_equipment}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> ON_TRADED_EQUIPMENT = fetch(EnchantmentTagKeys.ON_TRADED_EQUIPMENT);

    /**
     * {@code #minecraft:prevents_bee_spawns_when_mining}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> PREVENTS_BEE_SPAWNS_WHEN_MINING = fetch(EnchantmentTagKeys.PREVENTS_BEE_SPAWNS_WHEN_MINING);

    /**
     * {@code #minecraft:prevents_decorated_pot_shattering}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> PREVENTS_DECORATED_POT_SHATTERING = fetch(EnchantmentTagKeys.PREVENTS_DECORATED_POT_SHATTERING);

    /**
     * {@code #minecraft:prevents_ice_melting}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> PREVENTS_ICE_MELTING = fetch(EnchantmentTagKeys.PREVENTS_ICE_MELTING);

    /**
     * {@code #minecraft:prevents_infested_spawns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> PREVENTS_INFESTED_SPAWNS = fetch(EnchantmentTagKeys.PREVENTS_INFESTED_SPAWNS);

    /**
     * {@code #minecraft:smelts_loot}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> SMELTS_LOOT = fetch(EnchantmentTagKeys.SMELTS_LOOT);

    /**
     * {@code #minecraft:tooltip_order}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> TOOLTIP_ORDER = fetch(EnchantmentTagKeys.TOOLTIP_ORDER);

    /**
     * {@code #minecraft:tradeable}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> TRADEABLE = fetch(EnchantmentTagKeys.TRADEABLE);

    /**
     * {@code #minecraft:trades/desert_common}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final Tag<Enchantment> TRADES_DESERT_COMMON = fetch(EnchantmentTagKeys.TRADES_DESERT_COMMON);

    /**
     * {@code #minecraft:trades/desert_special}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final Tag<Enchantment> TRADES_DESERT_SPECIAL = fetch(EnchantmentTagKeys.TRADES_DESERT_SPECIAL);

    /**
     * {@code #minecraft:trades/jungle_common}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final Tag<Enchantment> TRADES_JUNGLE_COMMON = fetch(EnchantmentTagKeys.TRADES_JUNGLE_COMMON);

    /**
     * {@code #minecraft:trades/jungle_special}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final Tag<Enchantment> TRADES_JUNGLE_SPECIAL = fetch(EnchantmentTagKeys.TRADES_JUNGLE_SPECIAL);

    /**
     * {@code #minecraft:trades/plains_common}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final Tag<Enchantment> TRADES_PLAINS_COMMON = fetch(EnchantmentTagKeys.TRADES_PLAINS_COMMON);

    /**
     * {@code #minecraft:trades/plains_special}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final Tag<Enchantment> TRADES_PLAINS_SPECIAL = fetch(EnchantmentTagKeys.TRADES_PLAINS_SPECIAL);

    /**
     * {@code #minecraft:trades/savanna_common}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final Tag<Enchantment> TRADES_SAVANNA_COMMON = fetch(EnchantmentTagKeys.TRADES_SAVANNA_COMMON);

    /**
     * {@code #minecraft:trades/savanna_special}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final Tag<Enchantment> TRADES_SAVANNA_SPECIAL = fetch(EnchantmentTagKeys.TRADES_SAVANNA_SPECIAL);

    /**
     * {@code #minecraft:trades/snow_common}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final Tag<Enchantment> TRADES_SNOW_COMMON = fetch(EnchantmentTagKeys.TRADES_SNOW_COMMON);

    /**
     * {@code #minecraft:trades/snow_special}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final Tag<Enchantment> TRADES_SNOW_SPECIAL = fetch(EnchantmentTagKeys.TRADES_SNOW_SPECIAL);

    /**
     * {@code #minecraft:trades/swamp_common}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final Tag<Enchantment> TRADES_SWAMP_COMMON = fetch(EnchantmentTagKeys.TRADES_SWAMP_COMMON);

    /**
     * {@code #minecraft:trades/swamp_special}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final Tag<Enchantment> TRADES_SWAMP_SPECIAL = fetch(EnchantmentTagKeys.TRADES_SWAMP_SPECIAL);

    /**
     * {@code #minecraft:trades/taiga_common}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final Tag<Enchantment> TRADES_TAIGA_COMMON = fetch(EnchantmentTagKeys.TRADES_TAIGA_COMMON);

    /**
     * {@code #minecraft:trades/taiga_special}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final Tag<Enchantment> TRADES_TAIGA_SPECIAL = fetch(EnchantmentTagKeys.TRADES_TAIGA_SPECIAL);

    /**
     * {@code #minecraft:treasure}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Enchantment> TREASURE = fetch(EnchantmentTagKeys.TREASURE);

    private EnchantmentTags() {
    }

    private static Tag<Enchantment> fetch(final TagKey<Enchantment> tagKey) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).getTag(tagKey);
    }
}
