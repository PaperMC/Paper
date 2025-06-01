package io.papermc.paper.registry.keys.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.bukkit.MinecraftExperimental;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla tag keys for {@link RegistryKey#ENCHANTMENT}.
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
@GeneratedFrom("1.21.6-pre1")
@ApiStatus.Experimental
public final class EnchantmentTagKeys {
    /**
     * {@code #minecraft:curse}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> CURSE = create(key("curse"));

    /**
     * {@code #minecraft:double_trade_price}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> DOUBLE_TRADE_PRICE = create(key("double_trade_price"));

    /**
     * {@code #minecraft:exclusive_set/armor}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> EXCLUSIVE_SET_ARMOR = create(key("exclusive_set/armor"));

    /**
     * {@code #minecraft:exclusive_set/boots}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> EXCLUSIVE_SET_BOOTS = create(key("exclusive_set/boots"));

    /**
     * {@code #minecraft:exclusive_set/bow}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> EXCLUSIVE_SET_BOW = create(key("exclusive_set/bow"));

    /**
     * {@code #minecraft:exclusive_set/crossbow}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> EXCLUSIVE_SET_CROSSBOW = create(key("exclusive_set/crossbow"));

    /**
     * {@code #minecraft:exclusive_set/damage}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> EXCLUSIVE_SET_DAMAGE = create(key("exclusive_set/damage"));

    /**
     * {@code #minecraft:exclusive_set/mining}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> EXCLUSIVE_SET_MINING = create(key("exclusive_set/mining"));

    /**
     * {@code #minecraft:exclusive_set/riptide}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> EXCLUSIVE_SET_RIPTIDE = create(key("exclusive_set/riptide"));

    /**
     * {@code #minecraft:in_enchanting_table}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> IN_ENCHANTING_TABLE = create(key("in_enchanting_table"));

    /**
     * {@code #minecraft:non_treasure}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> NON_TREASURE = create(key("non_treasure"));

    /**
     * {@code #minecraft:on_mob_spawn_equipment}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> ON_MOB_SPAWN_EQUIPMENT = create(key("on_mob_spawn_equipment"));

    /**
     * {@code #minecraft:on_random_loot}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> ON_RANDOM_LOOT = create(key("on_random_loot"));

    /**
     * {@code #minecraft:on_traded_equipment}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> ON_TRADED_EQUIPMENT = create(key("on_traded_equipment"));

    /**
     * {@code #minecraft:prevents_bee_spawns_when_mining}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> PREVENTS_BEE_SPAWNS_WHEN_MINING = create(key("prevents_bee_spawns_when_mining"));

    /**
     * {@code #minecraft:prevents_decorated_pot_shattering}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> PREVENTS_DECORATED_POT_SHATTERING = create(key("prevents_decorated_pot_shattering"));

    /**
     * {@code #minecraft:prevents_ice_melting}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> PREVENTS_ICE_MELTING = create(key("prevents_ice_melting"));

    /**
     * {@code #minecraft:prevents_infested_spawns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> PREVENTS_INFESTED_SPAWNS = create(key("prevents_infested_spawns"));

    /**
     * {@code #minecraft:smelts_loot}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> SMELTS_LOOT = create(key("smelts_loot"));

    /**
     * {@code #minecraft:tooltip_order}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> TOOLTIP_ORDER = create(key("tooltip_order"));

    /**
     * {@code #minecraft:tradeable}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> TRADEABLE = create(key("tradeable"));

    /**
     * {@code #minecraft:trades/desert_common}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final TagKey<Enchantment> TRADES_DESERT_COMMON = create(key("trades/desert_common"));

    /**
     * {@code #minecraft:trades/desert_special}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final TagKey<Enchantment> TRADES_DESERT_SPECIAL = create(key("trades/desert_special"));

    /**
     * {@code #minecraft:trades/jungle_common}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final TagKey<Enchantment> TRADES_JUNGLE_COMMON = create(key("trades/jungle_common"));

    /**
     * {@code #minecraft:trades/jungle_special}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final TagKey<Enchantment> TRADES_JUNGLE_SPECIAL = create(key("trades/jungle_special"));

    /**
     * {@code #minecraft:trades/plains_common}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final TagKey<Enchantment> TRADES_PLAINS_COMMON = create(key("trades/plains_common"));

    /**
     * {@code #minecraft:trades/plains_special}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final TagKey<Enchantment> TRADES_PLAINS_SPECIAL = create(key("trades/plains_special"));

    /**
     * {@code #minecraft:trades/savanna_common}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final TagKey<Enchantment> TRADES_SAVANNA_COMMON = create(key("trades/savanna_common"));

    /**
     * {@code #minecraft:trades/savanna_special}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final TagKey<Enchantment> TRADES_SAVANNA_SPECIAL = create(key("trades/savanna_special"));

    /**
     * {@code #minecraft:trades/snow_common}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final TagKey<Enchantment> TRADES_SNOW_COMMON = create(key("trades/snow_common"));

    /**
     * {@code #minecraft:trades/snow_special}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final TagKey<Enchantment> TRADES_SNOW_SPECIAL = create(key("trades/snow_special"));

    /**
     * {@code #minecraft:trades/swamp_common}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final TagKey<Enchantment> TRADES_SWAMP_COMMON = create(key("trades/swamp_common"));

    /**
     * {@code #minecraft:trades/swamp_special}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final TagKey<Enchantment> TRADES_SWAMP_SPECIAL = create(key("trades/swamp_special"));

    /**
     * {@code #minecraft:trades/taiga_common}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final TagKey<Enchantment> TRADES_TAIGA_COMMON = create(key("trades/taiga_common"));

    /**
     * {@code #minecraft:trades/taiga_special}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.TRADE_REBALANCE)
    public static final TagKey<Enchantment> TRADES_TAIGA_SPECIAL = create(key("trades/taiga_special"));

    /**
     * {@code #minecraft:treasure}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Enchantment> TREASURE = create(key("treasure"));

    private EnchantmentTagKeys() {
    }

    /**
     * Creates a tag key for {@link Enchantment} in the registry {@code minecraft:enchantment}.
     *
     * @param key the tag key's key
     * @return a new tag key
     */
    @ApiStatus.Experimental
    public static TagKey<Enchantment> create(final Key key) {
        return TagKey.create(RegistryKey.ENCHANTMENT, key);
    }
}
