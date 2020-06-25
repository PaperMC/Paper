package org.bukkit;

import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a tag that may be defined by the server or a resource pack to
 * group like things together.
 *
 * Note that whilst all tags defined within this interface must be present in
 * implementations, their existence is not guaranteed across future versions.
 *
 * @param <T> the type of things grouped by this tag
 */
public interface Tag<T extends Keyed> extends Keyed {

    /**
     * Key for the built in block registry.
     */
    String REGISTRY_BLOCKS = "blocks";
    /**
     * Vanilla block tag representing all colors of wool.
     */
    Tag<Material> WOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wool"), Material.class);
    /**
     * Vanilla block tag representing all plank variants.
     */
    Tag<Material> PLANKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("planks"), Material.class);
    /**
     * Vanilla block tag representing all regular/mossy/cracked/chiseled stone
     * bricks.
     */
    Tag<Material> STONE_BRICKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("stone_bricks"), Material.class);
    /**
     * Vanilla block tag representing all wooden buttons.
     */
    Tag<Material> WOODEN_BUTTONS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wooden_buttons"), Material.class);
    /**
     * Vanilla block tag representing all buttons (inherits from
     * {@link #WOODEN_BUTTONS}.
     */
    Tag<Material> BUTTONS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("buttons"), Material.class);
    /**
     * Vanilla block tag representing all colors of carpet.
     */
    Tag<Material> CARPETS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("carpets"), Material.class);
    /**
     * Vanilla block tag representing all wooden doors.
     */
    Tag<Material> WOODEN_DOORS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wooden_doors"), Material.class);
    /**
     * Vanilla block tag representing all wooden stairs.
     */
    Tag<Material> WOODEN_STAIRS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wooden_stairs"), Material.class);
    /**
     * Vanilla block tag representing all wooden slabs.
     */
    Tag<Material> WOODEN_SLABS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wooden_slabs"), Material.class);
    /**
     * Vanilla block tag representing all wooden fences.
     */
    Tag<Material> WOODEN_FENCES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wooden_fences"), Material.class);
    /**
     * Vanilla block tag representing all pressure plates.
     */
    Tag<Material> PRESSURE_PLATES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("pressure_plates"), Material.class);
    /**
     * Vanilla block tag representing all wooden pressure plates.
     */
    Tag<Material> WOODEN_PRESSURE_PLATES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wooden_pressure_plates"), Material.class);
    /**
     * Vanilla block tag representing all stone pressure plates.
     */
    Tag<Material> STONE_PRESSURE_PLATES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("stone_pressure_plates"), Material.class);
    /**
     * Vanilla block tag representing all wooden trapdoors.
     */
    Tag<Material> WOODEN_TRAPDOORS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wooden_trapdoors"), Material.class);
    /**
     * Vanilla block tag representing all doors (inherits from
     * {@link #WOODEN_DOORS}.
     */
    Tag<Material> DOORS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("doors"), Material.class);
    /**
     * Vanilla block tag representing all sapling variants.
     */
    Tag<Material> SAPLINGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("saplings"), Material.class);
    /**
     * Vanilla block tag representing all log and bark variants that burn.
     */
    Tag<Material> LOGS_THAT_BURN = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("logs_that_burn"), Material.class);
    /**
     * Vanilla block tag representing all log and bark variants.
     */
    Tag<Material> LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("logs"), Material.class);
    /**
     * Vanilla block tag representing all dark oak log and bark variants.
     */
    Tag<Material> DARK_OAK_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("dark_oak_logs"), Material.class);
    /**
     * Vanilla block tag representing all oak log and bark variants.
     */
    Tag<Material> OAK_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("oak_logs"), Material.class);
    /**
     * Vanilla block tag representing all birch log and bark variants.
     */
    Tag<Material> BIRCH_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("birch_logs"), Material.class);
    /**
     * Vanilla block tag representing all acacia log and bark variants.
     */
    Tag<Material> ACACIA_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("acacia_logs"), Material.class);
    /**
     * Vanilla block tag representing all jungle log and bark variants.
     */
    Tag<Material> JUNGLE_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("jungle_logs"), Material.class);
    /**
     * Vanilla block tag representing all spruce log and bark variants.
     */
    Tag<Material> SPRUCE_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("spruce_logs"), Material.class);
    /**
     * Vanilla block tag representing all crimson stems.
     */
    Tag<Material> CRIMSON_STEMS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("crimson_stems"), Material.class);
    /**
     * Vanilla block tag representing all warped stems.
     */
    Tag<Material> WARPED_STEMS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("warped_stems"), Material.class);
    /**
     * Vanilla block tag representing all banner blocks.
     */
    Tag<Material> BANNERS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("banners"), Material.class);
    /**
     * Vanilla block tag representing all sand blocks.
     */
    Tag<Material> SAND = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("sand"), Material.class);
    /**
     * Vanilla block tag representing all stairs.
     */
    Tag<Material> STAIRS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("stairs"), Material.class);
    /**
     * Vanilla block tag representing all slabs.
     */
    Tag<Material> SLABS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("slabs"), Material.class);
    /**
     * Vanilla block tag representing all walls.
     */
    Tag<Material> WALLS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("walls"), Material.class);
    /**
     * Vanilla block tag representing all damaged and undamaged anvils.
     */
    Tag<Material> ANVIL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("anvil"), Material.class);
    /**
     * Vanilla block tag representing all Minecart rails.
     */
    Tag<Material> RAILS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("rails"), Material.class);
    /**
     * Vanilla block tag representing all leaves fans.
     */
    Tag<Material> LEAVES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("leaves"), Material.class);
    /**
     * Vanilla block tag representing all trapdoors (inherits from
     * {@link #WOODEN_TRAPDOORS}.
     */
    Tag<Material> TRAPDOORS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("trapdoors"), Material.class);
    /**
     * Vanilla block tag representing all empty and filled flower pots.
     */
    Tag<Material> FLOWER_POTS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("flower_pots"), Material.class);
    /**
     * Vanilla block tag representing all small flowers.
     */
    Tag<Material> SMALL_FLOWERS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("small_flowers"), Material.class);
    /**
     * Vanilla block tag representing all beds.
     */
    Tag<Material> BEDS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("beds"), Material.class);
    /**
     * Vanilla block tag representing all fences.
     */
    Tag<Material> FENCES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("fences"), Material.class);
    /**
     * Vanilla block tag representing all tall flowers.
     */
    Tag<Material> TALL_FLOWERS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("tall_flowers"), Material.class);
    /**
     * Vanilla block tag representing all flowers.
     */
    Tag<Material> FLOWERS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("flowers"), Material.class);
    /**
     * Vanilla block tag representing all piglin repellents.
     */
    Tag<Material> PIGLIN_REPELLENTS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("piglin_repellents"), Material.class);
    /**
     * Vanilla block tag representing all gold ores.
     */
    Tag<Material> GOLD_ORES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("gold_ores"), Material.class);
    /**
     * Vanilla block tag representing all non flammable wood.
     */
    Tag<Material> NON_FLAMMABLE_WOOD = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("non_flammable_wood"), Material.class);
    /**
     * Vanilla block tag denoting blocks that enderman may pick up and hold.
     */
    Tag<Material> ENDERMAN_HOLDABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("enderman_holdable"), Material.class);
    /**
     * Vanilla block tag denoting ice blocks.
     */
    Tag<Material> ICE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("ice"), Material.class);
    /**
     * Vanilla block tag denoting all valid mob spawn positions.
     */
    Tag<Material> VALID_SPAWN = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("valid_spawn"), Material.class);
    /**
     * Vanilla block tag denoting impermeable blocks which do not drip fluids.
     */
    Tag<Material> IMPERMEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("impermeable"), Material.class);
    /**
     * Vanilla block tag denoting all underwater blocks which may be bonemealed.
     */
    Tag<Material> UNDERWATER_BONEMEALS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("underwater_bonemeals"), Material.class);
    /**
     * Vanilla block tag representing all coral blocks.
     */
    Tag<Material> CORAL_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("coral_blocks"), Material.class);
    /**
     * Vanilla block tag representing all wall corals.
     */
    Tag<Material> WALL_CORALS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wall_corals"), Material.class);
    /**
     * Vanilla block tag representing all coral plants.
     */
    Tag<Material> CORAL_PLANTS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("coral_plants"), Material.class);
    /**
     * Vanilla block tag representing all coral.
     */
    Tag<Material> CORALS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("corals"), Material.class);
    /**
     * Vanilla block tag denoting all blocks bamboo may be planted on.
     */
    Tag<Material> BAMBOO_PLANTABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("bamboo_plantable_on"), Material.class);
    /**
     * Vanilla block tag representing all standing signs.
     */
    Tag<Material> STANDING_SIGNS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("standing_signs"), Material.class);
    /**
     * Vanilla block tag representing all wall signs.
     */
    Tag<Material> WALL_SIGNS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wall_signs"), Material.class);
    /**
     * Vanilla block tag representing all signs.
     */
    Tag<Material> SIGNS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("signs"), Material.class);
    /**
     * Vanilla block tag representing all blocks immune to dragons.
     */
    Tag<Material> DRAGON_IMMUNE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("dragon_immune"), Material.class);
    /**
     * Vanilla block tag representing all blocks immune to withers.
     */
    Tag<Material> WITHER_IMMUNE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wither_immune"), Material.class);
    /**
     * Vanilla block tag representing all base blocks used for wither summoning.
     */
    Tag<Material> WITHER_SUMMON_BASE_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wither_summon_base_blocks"), Material.class);
    /**
     * Vanilla block tag representing all beehives.
     */
    Tag<Material> BEEHIVES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("beehives"), Material.class);
    /**
     * Vanilla block tag representing all crops.
     */
    Tag<Material> CROPS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("crops"), Material.class);
    /**
     * Vanilla block tag representing all bee growables.
     */
    Tag<Material> BEE_GROWABLES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("bee_growables"), Material.class);
    /**
     * Vanilla block tag representing all portals.
     */
    Tag<Material> PORTALS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("portals"), Material.class);
    /**
     * Vanilla block tag representing all fire blocks.
     */
    Tag<Material> FIRE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("fire"), Material.class);
    /**
     * Vanilla block tag representing all nylium blocks.
     */
    Tag<Material> NYLIUM = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("nylium"), Material.class);
    /**
     * Vanilla block tag representing all wart blocks.
     */
    Tag<Material> WART_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wart_blocks"), Material.class);
    /**
     * Vanilla block tag representing all beacon base blocks.
     */
    Tag<Material> BEACON_BASE_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("beacon_base_blocks"), Material.class);
    /**
     * Vanilla block tag representing all blocks affected by the soul speed enchantment.
     */
    Tag<Material> SOUL_SPEED_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("soul_speed_blocks"), Material.class);
    /**
     * Vanilla block tag representing all wall post overrides.
     */
    Tag<Material> WALL_POST_OVERRIDE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wall_post_override"), Material.class);
    /**
     * Vanilla block tag representing all climbable blocks.
     */
    Tag<Material> CLIMBABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("climbable"), Material.class);
    /**
     * Vanilla block tag representing all shulker boxes.
     */
    Tag<Material> SHULKER_BOXES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("shulker_boxes"), Material.class);
    /**
     * Vanilla block tag representing all hoglin repellents.
     */
    Tag<Material> HOGLIN_REPELLENTS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("hoglin_repellents"), Material.class);
    /**
     * Vanilla block tag representing all soul fire base blocks.
     */
    Tag<Material> SOUL_FIRE_BASE_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("soul_fire_base_blocks"), Material.class);
    /**
     * Vanilla block tag representing all warm strider blocks.
     */
    Tag<Material> STRIDER_WARM_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("strider_warm_blocks"), Material.class);
    /**
     * Vanilla block tag representing all campfires.
     */
    Tag<Material> CAMPFIRES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("campfires"), Material.class);
    /**
     * Vanilla block tag representing all blocks guarded by piglins.
     */
    Tag<Material> GUARDED_BY_PIGLINS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("guarded_by_piglins"), Material.class);
    /**
     * Vanilla block tag representing all blocks that prevent inside mob
     * spawning.
     */
    Tag<Material> PREVENT_MOB_SPAWNING_INSIDE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("prevent_mob_spawning_inside"), Material.class);
    /**
     * Vanilla block tag representing all fence gates.
     */
    Tag<Material> FENCE_GATES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("fence_gates"), Material.class);
    /**
     * Vanilla block tag representing all unstable bottom center blocks.
     */
    Tag<Material> UNSTABLE_BOTTOM_CENTER = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("unstable_bottom_center"), Material.class);
    /**
     * Vanilla block tag representing all blocks that burn forever in the
     * overworld.
     */
    Tag<Material> INFINIBURN_OVERWORLD = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("infiniburn_overworld"), Material.class);
    /**
     * Vanilla block tag representing all blocks that burn forever in the
     * nether.
     */
    Tag<Material> INFINIBURN_NETHER = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("infiniburn_nether"), Material.class);
    /**
     * Vanilla block tag representing all blocks that burn forever in the end.
     */
    Tag<Material> INFINIBURN_END = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("infiniburn_end"), Material.class);
    /**
     * Key for the built in item registry.
     */
    String REGISTRY_ITEMS = "items";
    /**
     * Vanilla item tag representing all items loved by piglins.
     */
    Tag<Material> ITEMS_PIGLIN_LOVED = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("piglin_loved"), Material.class);
    /**
     * Vanilla item tag representing all banner items.
     */
    Tag<Material> ITEMS_BANNERS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("banners"), Material.class);
    /**
     * Vanilla item tag representing all boat items.
     */
    Tag<Material> ITEMS_BOATS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("boats"), Material.class);
    /**
     * Vanilla item tag representing all fish items.
     */
    Tag<Material> ITEMS_FISHES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("fishes"), Material.class);
    /**
     * Vanilla item tag representing all music disc items.
     */
    Tag<Material> ITEMS_MUSIC_DISCS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("music_discs"), Material.class);
    /**
     * Vanilla item tag representing all music disc items dropped by creepers.
     */
    Tag<Material> ITEMS_CREEPER_DROP_MUSIC_DISCS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("creeper_drop_music_discs"), Material.class);
    /**
     * Vanilla item tag representing all coal items.
     */
    Tag<Material> ITEMS_COALS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("coals"), Material.class);
    /**
     * Vanilla item tag representing all arrow items.
     */
    Tag<Material> ITEMS_ARROWS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("arrows"), Material.class);
    /**
     * Vanilla item tag representing all books that may be placed on lecterns.
     */
    Tag<Material> ITEMS_LECTERN_BOOKS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("lectern_books"), Material.class);
    /**
     * Vanilla item tag representing all items that may be placed in beacons.
     */
    Tag<Material> ITEMS_BEACON_PAYMENT_ITEMS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("beacon_payment_items"), Material.class);
    /**
     * Vanilla item tag representing all stone tool materials.
     */
    Tag<Material> ITEMS_STONE_TOOL_MATERIALS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("stone_tool_materials"), Material.class);
    /**
     * Vanilla item tag representing all furnace materials.
     */
    Tag<Material> ITEMS_FURNACE_MATERIALS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("furnace_materials"), Material.class);

    /**
     * Returns whether or not this tag has an entry for the specified item.
     *
     * @param item to check
     * @return if it is tagged
     */
    boolean isTagged(@NotNull T item);

    /**
     * Gets an immutable set of all tagged items.
     *
     * @return set of tagged items
     */
    @NotNull
    Set<T> getValues();

}
