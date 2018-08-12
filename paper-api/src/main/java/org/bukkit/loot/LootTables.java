package org.bukkit.loot;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;

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
    VILLAGE_BLACKSMITH("chests/village_blacksmith"),
    WOODLAND_MANSION("chests/woodland_mansion"),
    // Entities
    BAT("entities/bat"),
    BLAZE("entities/blaze"),
    CAVE_SPIDER("entities/cave_spider"),
    CHICKEN("entities/chicken"),
    COD("entities/cod"),
    COW("entities/cow"),
    CREEPER("entities/creeper"),
    DOLPHIN("entities/dolphin"),
    DONKEY("entities/donkey"),
    DROWNED("entities/drowned"),
    ELDER_GUARDIAN("entities/elder_guardian"),
    ENDERMAN("entities/enderman"),
    ENDERMITE("entities/endermite"),
    ENDER_DRAGON("entities/ender_dragon"),
    EVOKER("entities/evoker"),
    GHAST("entities/ghast"),
    GIANT("entities/giant"),
    GUARDIAN("entities/guardian"),
    HORSE("entities/horse"),
    HUSK("entities/husk"),
    IRON_GOLEM("entities/iron_golem"),
    LLAMA("entities/llama"),
    MAGMA_CUBE("entities/magma_cube"),
    MULE("entities/mule"),
    MUSHROOM_COW("entities/mushroom_cow"),
    OCELOT("entities/ocelot"),
    PARROT("entities/parrot"),
    PHANTOM("entities/phantom"),
    PIG("entities/pig"),
    POLAR_BEAR("entities/polar_bear"),
    PUFFERFISH("entities/pufferfish"),
    RABBIT("entities/rabbit"),
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
    TROPICAL_FISH("entities/tropical_fish"),
    TURTLE("entities/turtle"),
    VEX("entities/vex"),
    VILLAGER("entities/villager"),
    VINDICATOR("entities/vindicator"),
    WITCH("entities/witch"),
    WITHER_SKELETON("entities/wither_skeleton"),
    WOLF("entities/wolf"),
    ZOMBIE("entities/zombie"),
    ZOMBIE_HORSE("entities/zombie_horse"),
    ZOMBIE_PIGMAN("entities/zombie_pigman"),
    ZOMBIE_VILLAGER("entities/zombie_villager"),
    // Gameplay
    FISHING("gameplay/fishing"),
    FISHING_FISH("gameplay/fishing/fish"),
    FISHING_JUNK("gameplay/fishing/junk"),
    FISHING_TREASURE("gameplay/fishing/treasure"),
    // Sheep
    SHEEP("entities/sheep"),
    SHEEP_BLACK("entities/sheep/black"),
    SHEEP_BLUE("entities/sheep/blue"),
    SHEEP_BROWN("entities/sheep/brown"),
    SHEEP_CYAN("entities/sheep/cyan"),
    SHEEP_GRAY("entities/sheep/gray"),
    SHEEP_GREEN("entities/sheep/green"),
    SHEEP_LIGHT_BLUE("entities/sheep/light_blue"),
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

    private LootTables(String location) {
        this.location = location;
    }

    @Override
    public NamespacedKey getKey() {
        return NamespacedKey.minecraft(location);
    }
}
