package org.bukkit.loot;

import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * This enum holds a list of all known {@link LootTable}s offered by Mojang.
 * This list is not guaranteed to be accurate in future versions.
 *
 * See the
 * <a href="https://minecraft.gamepedia.com/Loot_table#List_of_loot_tables">
 * Minecraft Wiki</a> for more information on loot tables.
 */
public enum LootTables implements Keyed {

    EMPTY("empty"),
    // Chests/Dispensers - treasure chests
    ABANDONED_MINESHAFT("chests/abandoned_mineshaft"),
    BURIED_TREASURE("chests/buried_treasure"),
    DESERT_PYRAMID("chests/desert_pyramid"),
    END_CITY_TREASURE("chests/end_city_treasure"),
    IGLOO_CHEST("chests/igloo_chest"),
    JUNGLE_TEMPLE("chests/jungle_temple"),
    JUNGLE_TEMPLE_DISPENSER("chests/jungle_temple_dispenser"),
    NETHER_BRIDGE("chests/nether_bridge"),
    PILLAGER_OUTPOST("chests/pillager_outpost"),
    BASTION_TREASURE("chests/bastion_treasure"),
    BASTION_OTHER("chests/bastion_other"),
    BASTION_BRIDGE("chests/bastion_bridge"),
    BASTION_HOGLIN_STABLE("chests/bastion_hoglin_stable"),
    ANCIENT_CITY("chests/ancient_city"),
    ANCIENT_CITY_ICE_BOX("chests/ancient_city_ice_box"),
    RUINED_PORTAL("chests/ruined_portal"),
    SHIPWRECK_MAP("chests/shipwreck_map"),
    SHIPWRECK_SUPPLY("chests/shipwreck_supply"),
    SHIPWRECK_TREASURE("chests/shipwreck_treasure"),
    SIMPLE_DUNGEON("chests/simple_dungeon"),
    SPAWN_BONUS_CHEST("chests/spawn_bonus_chest"),
    STRONGHOLD_CORRIDOR("chests/stronghold_corridor"),
    STRONGHOLD_CROSSING("chests/stronghold_crossing"),
    STRONGHOLD_LIBRARY("chests/stronghold_library"),
    UNDERWATER_RUIN_BIG("chests/underwater_ruin_big"),
    UNDERWATER_RUIN_SMALL("chests/underwater_ruin_small"),
    VILLAGE_ARMORER("chests/village/village_armorer"),
    VILLAGE_BUTCHER("chests/village/village_butcher"),
    VILLAGE_CARTOGRAPHER("chests/village/village_cartographer"),
    VILLAGE_DESERT_HOUSE("chests/village/village_desert_house"),
    VILLAGE_FISHER("chests/village/village_fisher"),
    VILLAGE_FLETCHER("chests/village/village_fletcher"),
    VILLAGE_MASON("chests/village/village_mason"),
    VILLAGE_PLAINS_HOUSE("chests/village/village_plains_house"),
    VILLAGE_SAVANNA_HOUSE("chests/village/village_savanna_house"),
    VILLAGE_SHEPHERD("chests/village/village_shepherd"),
    VILLAGE_SNOWY_HOUSE("chests/village/village_snowy_house"),
    VILLAGE_TAIGA_HOUSE("chests/village/village_taiga_house"),
    VILLAGE_TANNERY("chests/village/village_tannery"),
    VILLAGE_TEMPLE("chests/village/village_temple"),
    VILLAGE_TOOLSMITH("chests/village/village_toolsmith"),
    VILLAGE_WEAPONSMITH("chests/village/village_weaponsmith"),
    WOODLAND_MANSION("chests/woodland_mansion"),
    // Entities
    ARMOR_STAND("entities/armor_stand"),
    AXOLOTL("entities/axolotl"),
    BAT("entities/bat"),
    BEE("entities/bee"),
    BLAZE("entities/blaze"),
    CAT("entities/cat"),
    CAVE_SPIDER("entities/cave_spider"),
    CHICKEN("entities/chicken"),
    COD("entities/cod"),
    COW("entities/cow"),
    CREEPER("entities/creeper"),
    DOLPHIN("entities/dolphin"),
    DONKEY("entities/donkey"),
    DROWNED("entities/drowned"),
    ELDER_GUARDIAN("entities/elder_guardian"),
    ENDER_DRAGON("entities/ender_dragon"),
    ENDERMAN("entities/enderman"),
    ENDERMITE("entities/endermite"),
    EVOKER("entities/evoker"),
    FOX("entities/fox"),
    GHAST("entities/ghast"),
    GIANT("entities/giant"),
    GLOW_SQUID("entities/glow_squid"),
    GOAT("entities/goat"),
    GUARDIAN("entities/guardian"),
    HOGLIN("entities/hoglin"),
    HORSE("entities/horse"),
    HUSK("entities/husk"),
    ILLUSIONER("entities/illusioner"),
    IRON_GOLEM("entities/iron_golem"),
    LLAMA("entities/llama"),
    MAGMA_CUBE("entities/magma_cube"),
    MOOSHROOM("entities/mooshroom"),
    MULE("entities/mule"),
    OCELOT("entities/ocelot"),
    PANDA("entities/panda"),
    PARROT("entities/parrot"),
    PHANTOM("entities/phantom"),
    PIG("entities/pig"),
    PIGLIN("entities/piglin"),
    PIGLIN_BRUTE("entities/piglin_brute"),
    PILLAGER("entities/pillager"),
    PLAYER("entities/player"),
    POLAR_BEAR("entities/polar_bear"),
    PUFFERFISH("entities/pufferfish"),
    RABBIT("entities/rabbit"),
    RAVAGER("entities/ravager"),
    SALMON("entities/salmon"),
    // Sheep entry here, moved below for organizational purposes
    SHULKER("entities/shulker"),
    SILVERFISH("entities/silverfish"),
    SKELETON("entities/skeleton"),
    SKELETON_HORSE("entities/skeleton_horse"),
    SLIME("entities/slime"),
    SNOW_GOLEM("entities/snow_golem"),
    SPIDER("entities/spider"),
    SQUID("entities/squid"),
    STRAY("entities/stray"),
    STRIDER("entities/strider"),
    TRADER_LLAMA("entities/trader_llama"),
    TROPICAL_FISH("entities/tropical_fish"),
    TURTLE("entities/turtle"),
    VEX("entities/vex"),
    VILLAGER("entities/villager"),
    VINDICATOR("entities/vindicator"),
    WANDERING_TRADER("entities/wandering_trader"),
    WITCH("entities/witch"),
    WITHER("entities/wither"),
    WITHER_SKELETON("entities/wither_skeleton"),
    WOLF("entities/wolf"),
    ZOGLIN("entities/zoglin"),
    ZOMBIE("entities/zombie"),
    ZOMBIE_HORSE("entities/zombie_horse"),
    ZOMBIE_VILLAGER("entities/zombie_villager"),
    ZOMBIFIED_PIGLIN("entities/zombified_piglin"),
    // Gameplay
    ARMORER_GIFT("gameplay/hero_of_the_village/armorer_gift"),
    BUTCHER_GIFT("gameplay/hero_of_the_village/butcher_gift"),
    CARTOGRAPHER_GIFT("gameplay/hero_of_the_village/cartographer_gift"),
    CAT_MORNING_GIFT("gameplay/cat_morning_gift"),
    CLERIC_GIFT("gameplay/hero_of_the_village/cleric_gift"),
    FARMER_GIFT("gameplay/hero_of_the_village/farmer_gift"),
    FISHERMAN_GIFT("gameplay/hero_of_the_village/fisherman_gift"),
    FISHING("gameplay/fishing"),
    FISHING_FISH("gameplay/fishing/fish"),
    FISHING_JUNK("gameplay/fishing/junk"),
    FISHING_TREASURE("gameplay/fishing/treasure"),
    FLETCHER_GIFT("gameplay/hero_of_the_village/fletcher_gift"),
    LEATHERWORKER_GIFT("gameplay/hero_of_the_village/leatherworker_gift"),
    LIBRARIAN_GIFT("gameplay/hero_of_the_village/librarian_gift"),
    MASON_GIFT("gameplay/hero_of_the_village/mason_gift"),
    SHEPHERD_GIFT("gameplay/hero_of_the_village/shepherd_gift"),
    TOOLSMITH_GIFT("gameplay/hero_of_the_village/toolsmith_gift"),
    WEAPONSMITH_GIFT("gameplay/hero_of_the_village/weaponsmith_gift"),
    SNIFFER_DIGGING("gameplay/sniffer_digging"),
    PIGLIN_BARTERING("gameplay/piglin_bartering"),
    // Archaeology
    DESERT_WELL_ARCHAEOLOGY("archaeology/desert_well"),
    DESERT_PYRAMID_ARCHAEOLOGY("archaeology/desert_pyramid"),
    TRAIL_RUINS_ARCHAEOLOGY_COMMON("archaeology/trail_ruins_common"),
    TRAIL_RUINS_ARCHAEOLOGY_RARE("archaeology/trail_ruins_rare"),
    OCEAN_RUIN_WARM_ARCHAEOLOGY("archaeology/ocean_ruin_warm"),
    OCEAN_RUIN_COLD_ARCHAEOLOGY("archaeology/ocean_ruin_cold"),
    // Sheep
    SHEEP("entities/sheep"),
    SHEEP_BLACK("entities/sheep/black"),
    SHEEP_BLUE("entities/sheep/blue"),
    SHEEP_BROWN("entities/sheep/brown"),
    SHEEP_CYAN("entities/sheep/cyan"),
    SHEEP_GRAY("entities/sheep/gray"),
    SHEEP_GREEN("entities/sheep/green"),
    SHEEP_LIGHT_BLUE("entities/sheep/light_blue"),
    SHEEP_LIGHT_GRAY("entities/sheep/light_gray"),
    SHEEP_LIME("entities/sheep/lime"),
    SHEEP_MAGENTA("entities/sheep/magenta"),
    SHEEP_ORANGE("entities/sheep/orange"),
    SHEEP_PINK("entities/sheep/pink"),
    SHEEP_PURPLE("entities/sheep/purple"),
    SHEEP_RED("entities/sheep/red"),
    SHEEP_WHITE("entities/sheep/white"),
    SHEEP_YELLOW("entities/sheep/yellow"),
    ;

    private final String location;

    private LootTables(/*@NotNull*/ String location) {
        this.location = location;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return NamespacedKey.minecraft(location);
    }

    /**
     * Get the {@link LootTable} corresponding to this constant. This is
     * equivalent to calling {@code Bukkit.getLootTable(this.getKey());}.
     *
     * @return the associated LootTable
     */
    @NotNull
    public LootTable getLootTable() {
        return Bukkit.getLootTable(getKey());
    }
}
