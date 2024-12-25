package org.bukkit;

import java.util.Set;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a tag that may be defined by the server or a resource pack to
 * group like things together.
 *
 * Note that whilst all tags defined within this interface must be present in
 * implementations, their existence is not guaranteed across future versions.
 *
 * <p>Custom tags defined by Paper are not present (as constants) in this class.
 * To access them please refer to {@link com.destroystokyo.paper.MaterialTags}
 * and {@link io.papermc.paper.tag.EntityTags}.</p>
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
     * Vanilla block tag representing all stone buttons.
     */
    Tag<Material> STONE_BUTTONS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("stone_buttons"), Material.class);
    /**
     * Vanilla block tag representing all buttons (inherits from
     * {@link #WOODEN_BUTTONS}.
     */
    Tag<Material> BUTTONS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("buttons"), Material.class);
    /**
     * Vanilla block tag representing all colors of carpet.
     */
    Tag<Material> WOOL_CARPETS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wool_carpets"), Material.class);
    /**
     * @deprecated {@link #WOOL_CARPETS}.
     */
    @Deprecated(since = "1.19")
    Tag<Material> CARPETS = WOOL_CARPETS;
    /**
     * Vanilla block tag representing all wooden doors.
     */
    Tag<Material> WOODEN_DOORS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wooden_doors"), Material.class);
    /**
     * Vanilla block tag representing all doors which can be opened by mobs.
     */
    Tag<Material> MOB_INTERACTABLE_DOORS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("mob_interactable_doors"), Material.class);
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
     * Vanilla block tag representing all pale oak log and bark variants.
     */
    Tag<Material> PALE_OAK_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("pale_oak_logs"), Material.class);
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
     * Vanilla block tag representing all cherry log and bark variants.
     */
    Tag<Material> CHERRY_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("cherry_logs"), Material.class);
    /**
     * Vanilla block tag representing all jungle log and bark variants.
     */
    Tag<Material> JUNGLE_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("jungle_logs"), Material.class);
    /**
     * Vanilla block tag representing all spruce log and bark variants.
     */
    Tag<Material> SPRUCE_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("spruce_logs"), Material.class);
    /**
     * Vanilla block tag representing all mangrove log and bark variants.
     */
    Tag<Material> MANGROVE_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("mangrove_logs"), Material.class);
    /**
     * Vanilla block tag representing all crimson stems.
     */
    Tag<Material> CRIMSON_STEMS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("crimson_stems"), Material.class);
    /**
     * Vanilla block tag representing all warped stems.
     */
    Tag<Material> WARPED_STEMS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("warped_stems"), Material.class);
    /**
     * Vanilla block tag representing all bamboo blocks.
     */
    Tag<Material> BAMBOO_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("bamboo_blocks"), Material.class);
    /**
     * Vanilla block tag representing all banner blocks.
     */
    Tag<Material> BANNERS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("banners"), Material.class);
    /**
     * Vanilla block tag representing all sand blocks.
     */
    Tag<Material> SAND = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("sand"), Material.class);
    /**
     * Vanilla block tag representing all blocks which smelt to glass in a furnace.
     */
    Tag<Material> SMELTS_TO_GLASS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("smelts_to_glass"), Material.class);
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
     * Vanilla block tag representing all flowers.
     */
    Tag<Material> FLOWERS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("flowers"), Material.class);
    /**
     * Vanilla block tag representing all blocks which attract bees.
     */
    Tag<Material> BEE_ATTRACTIVE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("bee_attractive"), Material.class);
    /**
     * Vanilla block tag representing all piglin repellents.
     */
    Tag<Material> PIGLIN_REPELLENTS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("piglin_repellents"), Material.class);
    /**
     * Vanilla block tag representing all gold ores.
     */
    Tag<Material> GOLD_ORES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("gold_ores"), Material.class);
    /**
     * Vanilla block tag representing all iron ores.
     */
    Tag<Material> IRON_ORES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("iron_ores"), Material.class);
    /**
     * Vanilla block tag representing all diamond ores.
     */
    Tag<Material> DIAMOND_ORES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("diamond_ores"), Material.class);
    /**
     * Vanilla block tag representing all redstone ores.
     */
    Tag<Material> REDSTONE_ORES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("redstone_ores"), Material.class);
    /**
     * Vanilla block tag representing all lapis ores.
     */
    Tag<Material> LAPIS_ORES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("lapis_ores"), Material.class);
    /**
     * Vanilla block tag representing all coal ores.
     */
    Tag<Material> COAL_ORES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("coal_ores"), Material.class);
    /**
     * Vanilla block tag representing all emerald ores.
     */
    Tag<Material> EMERALD_ORES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("emerald_ores"), Material.class);
    /**
     * Vanilla block tag representing all copper ores.
     */
    Tag<Material> COPPER_ORES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("copper_ores"), Material.class);
    /**
     * Vanilla block tag representing all candles.
     */
    Tag<Material> CANDLES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("candles"), Material.class);
    /**
     * Vanilla block tag representing all dirt.
     */
    Tag<Material> DIRT = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("dirt"), Material.class);
    /**
     * Vanilla block tag representing all terracotta.
     */
    Tag<Material> TERRACOTTA = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("terracotta"), Material.class);
    /**
     * Vanilla block tag representing all badlands terracotta.
     */
    Tag<Material> BADLANDS_TERRACOTTA = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("badlands_terracotta"), Material.class);
    /**
     * Vanilla block tag representing all concrete powder.
     */
    Tag<Material> CONCRETE_POWDER = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("concrete_powder"), Material.class);
    /**
     * Vanilla block tag representing all blocks which complete the find tree
     * tutorial.
     */
    Tag<Material> COMPLETES_FIND_TREE_TUTORIAL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("completes_find_tree_tutorial"), Material.class);
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
     * Vanilla block tag representing all regular signs.
     */
    Tag<Material> SIGNS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("signs"), Material.class);
    /**
     * Vanilla block tag representing all ceiling signs.
     */
    Tag<Material> CEILING_HANGING_SIGNS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("ceiling_hanging_signs"), Material.class);
    /**
     * Vanilla block tag representing all wall hanging signs.
     */
    Tag<Material> WALL_HANGING_SIGNS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wall_hanging_signs"), Material.class);
    /**
     * Vanilla block tag representing all hanging signs.
     */
    Tag<Material> ALL_HANGING_SIGNS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("all_hanging_signs"), Material.class);
    /**
     * Vanilla block tag representing all signs, regardless of type.
     */
    Tag<Material> ALL_SIGNS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("all_signs"), Material.class);
    /**
     * Vanilla block tag representing all blocks immune to dragons.
     */
    Tag<Material> DRAGON_IMMUNE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("dragon_immune"), Material.class);
    /**
     * Vanilla block tag representing all blocks transparent to the ender
     * dragon.
     */
    Tag<Material> DRAGON_TRANSPARENT = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("dragon_transparent"), Material.class);
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
     * Vanilla block tag representing all blocks affected by the soul speed
     * enchantment.
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
     * Vanilla block tag representing all blocks which reset fall damage.
     */
    Tag<Material> FALL_DAMAGE_RESETTING = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("fall_damage_resetting"), Material.class);
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
    Tag<Material> MUSHROOM_GROW_BLOCK = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("mushroom_grow_block"), Material.class);

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
     * Vanilla block tag representing the overworld base material.
     */
    Tag<Material> BASE_STONE_OVERWORLD = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("base_stone_overworld"), Material.class);
    /**
     * Vanilla block tag representing all blocks that may be replaced by ores.
     */
    Tag<Material> STONE_ORE_REPLACEABLES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("stone_ore_replaceables"), Material.class);
    /**
     * Vanilla block tag representing all blocks that may be replaced by
     * deepslate ores.
     */
    Tag<Material> DEEPSLATE_ORE_REPLACEABLES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("deepslate_ore_replaceables"), Material.class);
    /**
     * Vanilla block tag representing the nether base material.
     */
    Tag<Material> BASE_STONE_NETHER = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("base_stone_nether"), Material.class);
    /**
     * Vanilla block tag representing all blocks replaceable by the overworld
     * carver.
     */
    Tag<Material> OVERWORLD_CARVER_REPLACEABLES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("overworld_carver_replaceables"), Material.class);
    /**
     * Vanilla block tag representing all blocks replaceable by the nether
     * carver.
     */
    Tag<Material> NETHER_CARVER_REPLACEABLES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("nether_carver_replaceables"), Material.class);
    /**
     * Vanilla block tag representing all candle cakes.
     */
    Tag<Material> CANDLE_CAKES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("candle_cakes"), Material.class);
    /**
     * Vanilla block tag representing all cauldrons.
     */
    Tag<Material> CAULDRONS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("cauldrons"), Material.class);
    /**
     * Vanilla block tag representing all blocks that make crystal sounds.
     */
    Tag<Material> CRYSTAL_SOUND_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("crystal_sound_blocks"), Material.class);
    /**
     * Vanilla block tag representing all blocks that play muffled step sounds.
     */
    Tag<Material> INSIDE_STEP_SOUND_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("inside_step_sound_blocks"), Material.class);
    /**
     * Vanilla block tag representing all blocks that play combination step sounds.
     */
    Tag<Material> COMBINATION_STEP_SOUND_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("combination_step_sound_blocks"), Material.class);
    /**
     * Vanilla block tag representing all blocks that play step sounds with camels on sand.
     */
    Tag<Material> CAMEL_SAND_STEP_SOUND_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("camel_sand_step_sound_blocks"), Material.class);
    /**
     * Vanilla block tag representing all blocks that block vibration signals.
     */
    Tag<Material> OCCLUDES_VIBRATION_SIGNALS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("occludes_vibration_signals"), Material.class);
    /**
     * Vanilla block tag representing all blocks that dampen the propagation of
     * vibration signals.
     */
    Tag<Material> DAMPENS_VIBRATIONS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("dampens_vibrations"), Material.class);
    /**
     * Vanilla block tag representing all blocks that are replaceable by
     * dripstone.
     */
    Tag<Material> DRIPSTONE_REPLACEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("dripstone_replaceable_blocks"), Material.class);
    /**
     * Vanilla block tag representing all cave vines.
     */
    Tag<Material> CAVE_VINES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("cave_vines"), Material.class);
    /**
     * Vanilla block tag representing all blocks replaceable by moss.
     */
    Tag<Material> MOSS_REPLACEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("moss_replaceable"), Material.class);
    /**
     * Vanilla block tag representing all blocks replaceable by lush ground.
     */
    Tag<Material> LUSH_GROUND_REPLACEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("lush_ground_replaceable"), Material.class);
    /**
     * Vanilla block tag representing all blocks replaceable by azalea root.
     */
    Tag<Material> AZALEA_ROOT_REPLACEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("azalea_root_replaceable"), Material.class);
    /**
     * Vanilla block tag representing all blocks which small dripleaf can be
     * placed on.
     */
    Tag<Material> SMALL_DRIPLEAF_PLACEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("small_dripleaf_placeable"), Material.class);
    /**
     * Vanilla block tag representing all blocks which big dripleaf can be
     * placed on.
     */
    Tag<Material> BIG_DRIPLEAF_PLACEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("big_dripleaf_placeable"), Material.class);
    /**
     * Vanilla block tag representing all snow blocks.
     */
    Tag<Material> SNOW = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("snow"), Material.class);
    /**
     * Vanilla block tag representing all blocks mineable with an axe.
     */
    Tag<Material> MINEABLE_AXE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("mineable/axe"), Material.class);
    /**
     * Vanilla block tag representing all blocks mineable with a hoe.
     */
    Tag<Material> MINEABLE_HOE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("mineable/hoe"), Material.class);
    /**
     * Vanilla block tag representing all blocks mineable with a pickaxe.
     */
    Tag<Material> MINEABLE_PICKAXE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("mineable/pickaxe"), Material.class);
    /**
     * Vanilla block tag representing all blocks mineable with a shovel.
     */
    Tag<Material> MINEABLE_SHOVEL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("mineable/shovel"), Material.class);
    /**
     * Vanilla block tag representing all blocks that can be efficiently mined with a sword.
     */
    Tag<Material> SWORD_EFFICIENT = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("sword_efficient"), Material.class);
    /**
     * Vanilla block tag representing all blocks which require a diamond tool.
     */
    Tag<Material> NEEDS_DIAMOND_TOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("needs_diamond_tool"), Material.class);
    /**
     * Vanilla block tag representing all blocks which require an iron tool.
     */
    Tag<Material> NEEDS_IRON_TOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("needs_iron_tool"), Material.class);
    /**
     * Vanilla block tag representing all blocks which require a stone tool.
     */
    Tag<Material> NEEDS_STONE_TOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("needs_stone_tool"), Material.class);
    /**
     * Vanilla block tag representing all blocks which will not drop items with a netherite tool.
     */
    Tag<Material> INCORRECT_FOR_NETHERITE_TOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("incorrect_for_netherite_tool"), Material.class);
    /**
     * Vanilla block tag representing all blocks which will not drop items with a diamond tool.
     */
    Tag<Material> INCORRECT_FOR_DIAMOND_TOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("incorrect_for_diamond_tool"), Material.class);
    /**
     * Vanilla block tag representing all blocks which will not drop items with a iron tool.
     */
    Tag<Material> INCORRECT_FOR_IRON_TOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("incorrect_for_iron_tool"), Material.class);
    /**
     * Vanilla block tag representing all blocks which will not drop items with a stone tool.
     */
    Tag<Material> INCORRECT_FOR_STONE_TOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("incorrect_for_stone_tool"), Material.class);
    /**
     * Vanilla block tag representing all blocks which will not drop items with a gold tool.
     */
    Tag<Material> INCORRECT_FOR_GOLD_TOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("incorrect_for_gold_tool"), Material.class);
    /**
     * Vanilla block tag representing all blocks which will not drop items with a wooden tool.
     */
    Tag<Material> INCORRECT_FOR_WOODEN_TOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("incorrect_for_wooden_tool"), Material.class);
    /**
     * Vanilla block tag representing all blocks which will not be replaced by
     * world generation features.
     */
    Tag<Material> FEATURES_CANNOT_REPLACE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("features_cannot_replace"), Material.class);
    /**
     * Vanilla block tag representing all blocks which lava pools will not
     * replace.
     */
    Tag<Material> LAVA_POOL_STONE_CANNOT_REPLACE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("lava_pool_stone_cannot_replace"), Material.class);
    /**
     * Vanilla block tag representing all blocks which geodes will not spawn in.
     */
    Tag<Material> GEODE_INVALID_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("geode_invalid_blocks"), Material.class);
    /**
     * Vanilla block tag representing all blocks which frogs prefer to jump to.
     */
    Tag<Material> FROG_PREFER_JUMP_TO = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("frog_prefer_jump_to"), Material.class);
    /**
     * Vanilla block tag representing all blocks which can be replaced by skulk.
     */
    Tag<Material> SCULK_REPLACEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("sculk_replaceable"), Material.class);
    /**
     * Vanilla block tag representing all blocks which can be replaced by skulk
     * during world generation.
     */
    Tag<Material> SCULK_REPLACEABLE_WORLD_GEN = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("sculk_replaceable_world_gen"), Material.class);
    /**
     * Vanilla block tag representing all blocks which can be replaced by
     * ancient cities.
     */
    Tag<Material> ANCIENT_CITY_REPLACEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("ancient_city_replaceable"), Material.class);
    /**
     * Vanilla block tag representing all blocks which resonate vibrations.
     */
    Tag<Material> VIBRATION_RESONATORS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("vibration_resonators"), Material.class);
    /**
     * Vanilla block tag representing all blocks which animals will spawn on.
     */
    Tag<Material> ANIMALS_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("animals_spawnable_on"), Material.class);
    /**
     * Vanilla block tag representing all blocks which armadillos will spawn on.
     */
    Tag<Material> ARMADILLO_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("armadillo_spawnable_on"), Material.class);
    /**
     * Vanilla block tag representing all blocks which axolotls will spawn on.
     */
    Tag<Material> AXOLOTLS_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("axolotls_spawnable_on"), Material.class);
    /**
     * Vanilla block tag representing all blocks which goats will spawn on.
     */
    Tag<Material> GOATS_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("goats_spawnable_on"), Material.class);
    /**
     * Vanilla block tag representing all blocks which mooshrooms will spawn on.
     */
    Tag<Material> MOOSHROOMS_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("mooshrooms_spawnable_on"), Material.class);
    /**
     * Vanilla block tag representing all blocks which parrots will spawn on.
     */
    Tag<Material> PARROTS_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("parrots_spawnable_on"), Material.class);
    /**
     * Vanilla block tag representing all blocks which polar bears will spawn
     * on.
     */
    Tag<Material> POLAR_BEARS_SPAWNABLE_ON_ALTERNATE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("polar_bears_spawnable_on_alternate"), Material.class);
    /**
     * Vanilla block tag representing all blocks which rabbits will spawn on.
     */
    Tag<Material> RABBITS_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("rabbits_spawnable_on"), Material.class);
    /**
     * Vanilla block tag representing all blocks which foxes will spawn on.
     */
    Tag<Material> FOXES_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("foxes_spawnable_on"), Material.class);
    /**
     * Vanilla block tag representing all blocks which wolves will spawn on.
     */
    Tag<Material> WOLVES_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wolves_spawnable_on"), Material.class);
    /**
     * Vanilla block tag representing all blocks which frogs will spawn on.
     */
    Tag<Material> FROGS_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("frogs_spawnable_on"), Material.class);
    /**
     * Vanilla block tag representing all blocks which bats will spawn on.
     */
    Tag<Material> BATS_SPAWNABLE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("bats_spawnable_on"), Material.class);
    /**
     * Vanilla block tag representing all blocks which azaleas will grow on.
     */
    Tag<Material> AZALEA_GROWS_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("azalea_grows_on"), Material.class);
    /**
     * Vanilla block tag representing all blocks which may be converted to mud.
     */
    Tag<Material> CONVERTABLE_TO_MUD = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("convertable_to_mud"), Material.class);
    /**
     * Vanilla block tag representing all blocks which mangrove logs can grow
     * through.
     */
    Tag<Material> MANGROVE_LOGS_CAN_GROW_THROUGH = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("mangrove_logs_can_grow_through"), Material.class);
    /**
     * Vanilla block tag representing all blocks which mangrove roots can grow
     * through.
     */
    Tag<Material> MANGROVE_ROOTS_CAN_GROW_THROUGH = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("mangrove_roots_can_grow_through"), Material.class);
    /**
     * Vanilla block tag representing all blocks which dead bushes may be placed
     * on.
     */
    Tag<Material> DEAD_BUSH_MAY_PLACE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("dead_bush_may_place_on"), Material.class);
    /**
     * Vanilla block tag representing all blocks which snap dropped goat horns.
     */
    Tag<Material> SNAPS_GOAT_HORN = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("snaps_goat_horn"), Material.class);
    /**
     * Vanilla block tag representing all blocks replaceable by growing trees.
     */
    Tag<Material> REPLACEABLE_BY_TREES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("replaceable_by_trees"), Material.class);
    /**
     * Vanilla block tag representing blocks which snow cannot survive on.
     */
    Tag<Material> SNOW_LAYER_CANNOT_SURVIVE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("snow_layer_cannot_survive_on"), Material.class);
    /**
     * Vanilla block tag representing blocks which snow can survive on.
     */
    Tag<Material> SNOW_LAYER_CAN_SURVIVE_ON = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("snow_layer_can_survive_on"), Material.class);
    /**
     * Vanilla block tag representing blocks which cannot be dismounted into.
     */
    Tag<Material> INVALID_SPAWN_INSIDE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("invalid_spawn_inside"), Material.class);
    /**
     * Vanilla block tag representing blocks which can be dug by sniffers.
     */
    Tag<Material> SNIFFER_DIGGABLE_BLOCK = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("sniffer_diggable_block"), Material.class);
    /**
     * Vanilla block tag representing all blocks which booster sniffer egg hatching.
     */
    Tag<Material> SNIFFER_EGG_HATCH_BOOST = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("sniffer_egg_hatch_boost"), Material.class);
    /**
     * Vanilla block tag representing all blocks which can be replaced by trail ruins.
     */
    Tag<Material> TRAIL_RUINS_REPLACEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("trail_ruins_replaceable"), Material.class);
    /**
     * Vanilla block tag representing all blocks which are replaceable.
     */
    Tag<Material> REPLACEABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("replaceable"), Material.class);
    /**
     * Vanilla block tag representing all blocks which provide enchantment power.
     */
    Tag<Material> ENCHANTMENT_POWER_PROVIDER = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("enchantment_power_provider"), Material.class);
    /**
     * Vanilla block tag representing all blocks which transmit enchantment power.
     */
    Tag<Material> ENCHANTMENT_POWER_TRANSMITTER = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("enchantment_power_transmitter"), Material.class);
    /**
     * Vanilla block tag representing all blocks which do not destroy farmland when placed.
     */
    Tag<Material> MAINTAINS_FARMLAND = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("maintains_farmland"), Material.class);
    /**
     * Vanilla block tag representing all blocks which block wind charge explosions.
     */
    Tag<Material> BLOCKS_WIND_CHARGE_EXPLOSIONS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("blocks_wind_charge_explosions"), Material.class);
    /**
     * Vanilla block tag representing solid blocks which do not block hopper operation.
     */
    Tag<Material> DOES_NOT_BLOCK_HOPPERS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("does_not_block_hoppers"), Material.class);
    /**
     * Vanilla block tag representing all blocks that resemble air.
     */
    Tag<Material> AIR = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("air"), Material.class);
    /**
     * Key for the built in item registry.
     */
    String REGISTRY_ITEMS = "items";
    /**
     * Vanilla item tag representing all items loved by piglins.
     */
    Tag<Material> ITEMS_PIGLIN_LOVED = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("piglin_loved"), Material.class);
    /**
     * Vanilla item tag representing all items ignored by piglin babies.
     */
    Tag<Material> IGNORED_BY_PIGLIN_BABIES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("ignored_by_piglin_babies"), Material.class);
    /**
     * Vanilla item tag representing all items which will prevent piglins from being angered.
     */
    Tag<Material> ITEMS_PIGLIN_SAFE_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("piglin_safe_armor"), Material.class);
    /**
     * Vanilla item tag representing all items which can be used to duplicate Allays when they are dancing.
     */
    Tag<Material> ITEMS_DUPLICATES_ALLAYS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("duplicates_allays"), Material.class);
    /**
     * Vanilla item tag representing all brewing stand fuel items.
     */
    Tag<Material> ITEMS_BREWING_FUEL = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("brewing_fuel"), Material.class);
    /**
     * Vanilla item tag representing all meat.
     */
    Tag<Material> ITEMS_MEAT = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("meat"), Material.class);
    /**
     * Vanilla item tag representing all sniffer food.
     */
    Tag<Material> ITEMS_SNIFFER_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("sniffer_food"), Material.class);
    /**
     * Vanilla item tag representing all piglin food.
     */
    Tag<Material> ITEMS_PIGLIN_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("piglin_food"), Material.class);
    /**
     * Vanilla item tag representing all piglin food.
     *
     * @deprecated use {@link #ITEMS_PIGLIN_FOOD}
     */
    @Deprecated(since = "1.20.5")
    Tag<Material> PIGLIN_FOOD = ITEMS_PIGLIN_FOOD;
    /**
     * Vanilla item tag representing all fox food.
     */
    Tag<Material> ITEMS_FOX_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("fox_food"), Material.class);
    /**
     * Vanilla item tag representing all fox food.
     *
     * @deprecated use {@link #ITEMS_FOX_FOOD}
     */
    @Deprecated(since = "1.20.5")
    Tag<Material> FOX_FOOD = ITEMS_FOX_FOOD;
    /**
     * Vanilla item tag representing all cow food.
     */
    Tag<Material> ITEMS_COW_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("cow_food"), Material.class);
    /**
     * Vanilla item tag representing all goat food.
     */
    Tag<Material> ITEMS_GOAT_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("goat_food"), Material.class);
    /**
     * Vanilla item tag representing all sheep food.
     */
    Tag<Material> ITEMS_SHEEP_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("sheep_food"), Material.class);
    /**
     * Vanilla item tag representing all wolf food.
     */
    Tag<Material> ITEMS_WOLF_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("wolf_food"), Material.class);
    /**
     * Vanilla item tag representing all cat food.
     */
    Tag<Material> ITEMS_CAT_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("cat_food"), Material.class);
    /**
     * Vanilla item tag representing all horse food.
     */
    Tag<Material> ITEMS_HORSE_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("horse_food"), Material.class);
    /**
     * Vanilla item tag representing all horse tempt items.
     */
    Tag<Material> ITEMS_HORSE_TEMPT_ITEMS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("horse_tempt_items"), Material.class);
    /**
     * Vanilla item tag representing all camel food.
     */
    Tag<Material> ITEMS_CAMEL_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("camel_food"), Material.class);
    /**
     * Vanilla item tag representing all armadillo food.
     */
    Tag<Material> ITEMS_ARMADILLO_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("armadillo_food"), Material.class);
    /**
     * Vanilla item tag representing all bee food.
     */
    Tag<Material> ITEMS_BEE_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("bee_food"), Material.class);
    /**
     * Vanilla item tag representing all chicken food.
     */
    Tag<Material> ITEMS_CHICKEN_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("chicken_food"), Material.class);
    /**
     * Vanilla item tag representing all frog food.
     */
    Tag<Material> ITEMS_FROG_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("frog_food"), Material.class);
    /**
     * Vanilla item tag representing all hoglin food.
     */
    Tag<Material> ITEMS_HOGLIN_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("hoglin_food"), Material.class);
    /**
     * Vanilla item tag representing all llama food.
     */
    Tag<Material> ITEMS_LLAMA_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("llama_food"), Material.class);
    /**
     * Vanilla item tag representing all llama tempt items.
     */
    Tag<Material> ITEMS_LLAMA_TEMPT_ITEMS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("llama_tempt_items"), Material.class);
    /**
     * Vanilla item tag representing all ocelot food.
     */
    Tag<Material> ITEMS_OCELOT_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("ocelot_food"), Material.class);
    /**
     * Vanilla item tag representing all panda food.
     */
    Tag<Material> ITEMS_PANDA_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("panda_food"), Material.class);
    /**
     * Vanilla item tag representing all items that a panda will pick up and eat from the ground.
     */
    Tag<Material> ITEMS_PANDA_EATS_FROM_GROUND = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("panda_eats_from_ground"), Material.class);
    /**
     * Vanilla item tag representing all pig food.
     */
    Tag<Material> ITEMS_PIG_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("pig_food"), Material.class);
    /**
     * Vanilla item tag representing all rabbit food.
     */
    Tag<Material> ITEMS_RABBIT_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("rabbit_food"), Material.class);
    /**
     * Vanilla item tag representing all strider food.
     */
    Tag<Material> ITEMS_STRIDER_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("strider_food"), Material.class);
    /**
     * Vanilla item tag representing all strider tempt items.
     */
    Tag<Material> ITEMS_STRIDER_TEMPT_ITEMS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("strider_tempt_items"), Material.class);
    /**
     * Vanilla item tag representing all turtle food.
     */
    Tag<Material> ITEMS_TURTLE_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("turtle_food"), Material.class);
    /**
     * Vanilla item tag representing all parrot food.
     */
    Tag<Material> ITEMS_PARROT_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("parrot_food"), Material.class);
    /**
     * Vanilla item tag representing all parrot poisonous food.
     */
    Tag<Material> ITEMS_PARROT_POISONOUS_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("parrot_poisonous_food"), Material.class);
    /**
     * Vanilla item tag representing all axolotl food.
     */
    Tag<Material> ITEMS_AXOLOTL_FOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("axolotl_food"), Material.class);
    /**
     * Vanilla item tag representing all banner items.
     */
    Tag<Material> ITEMS_BANNERS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("banners"), Material.class);
    /**
     * Vanilla item tag representing all non flammable wood items.
     */
    Tag<Material> ITEMS_NON_FLAMMABLE_WOOD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("non_flammable_wood"), Material.class);
    /**
     * Vanilla item tag representing all boat items.
     */
    Tag<Material> ITEMS_BOATS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("boats"), Material.class);
    /**
     * Vanilla item tag representing all chest boat items.
     */
    Tag<Material> ITEMS_CHEST_BOATS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("chest_boats"), Material.class);
    /**
     * Vanilla item tag representing all fish items.
     */
    Tag<Material> ITEMS_FISHES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("fishes"), Material.class);
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
     * Vanilla item tag representing all books that may be placed on bookshelves.
     */
    Tag<Material> ITEMS_BOOKSHELF_BOOKS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("bookshelf_books"), Material.class);
    /**
     * Vanilla item tag representing all items that may be placed in beacons.
     */
    Tag<Material> ITEMS_BEACON_PAYMENT_ITEMS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("beacon_payment_items"), Material.class);
    /**
     * Vanilla item tag representing all wooden tool materials.
     */
    Tag<Material> ITEMS_WOODEN_TOOL_MATERIALS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("wooden_tool_materials"), Material.class);
    /**
     * Vanilla item tag representing all stone tool materials.
     */
    Tag<Material> ITEMS_STONE_TOOL_MATERIALS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("stone_tool_materials"), Material.class);
    /**
     * Vanilla item tag representing all iron tool materials.
     */
    Tag<Material> ITEMS_IRON_TOOL_MATERIALS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("iron_tool_materials"), Material.class);
    /**
     * Vanilla item tag representing all gold tool materials.
     */
    Tag<Material> ITEMS_GOLD_TOOL_MATERIALS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("gold_tool_materials"), Material.class);
    /**
     * Vanilla item tag representing all diamond tool materials.
     */
    Tag<Material> ITEMS_DIAMOND_TOOL_MATERIALS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("diamond_tool_materials"), Material.class);
    /**
     * Vanilla item tag representing all netherite tool materials.
     */
    Tag<Material> ITEMS_NETHERITE_TOOL_MATERIALS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("netherite_tool_materials"), Material.class);
    /**
     * Vanilla item tag representing all items which repair leather armor.
     */
    Tag<Material> ITEMS_REPAIRS_LEATHER_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("repairs_leather_armor"), Material.class);
    /**
     * Vanilla item tag representing all items which repair chain armor.
     */
    Tag<Material> ITEMS_REPAIRS_CHAIN_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("repairs_chain_armor"), Material.class);
    /**
     * Vanilla item tag representing all items which repair iron armor.
     */
    Tag<Material> ITEMS_REPAIRS_IRON_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("repairs_iron_armor"), Material.class);
    /**
     * Vanilla item tag representing all items which repair gold armor.
     */
    Tag<Material> ITEMS_REPAIRS_GOLD_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("repairs_gold_armor"), Material.class);
    /**
     * Vanilla item tag representing all items which repair diamond armor.
     */
    Tag<Material> ITEMS_REPAIRS_DIAMOND_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("repairs_diamond_armor"), Material.class);
    /**
     * Vanilla item tag representing all items which repair netherite armor.
     */
    Tag<Material> ITEMS_REPAIRS_NETHERITE_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("repairs_netherite_armor"), Material.class);
    /**
     * Vanilla item tag representing all items which repair turtle helmets.
     */
    Tag<Material> ITEMS_REPAIRS_TURTLE_HELMET = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("repairs_turtle_helmet"), Material.class);
    /**
     * Vanilla item tag representing all items which repair wolf armor.
     */
    Tag<Material> ITEMS_REPAIRS_WOLF_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("repairs_wolf_armor"), Material.class);
    /**
     * Vanilla item tag representing all stone based materials for crafting.
     */
    Tag<Material> ITEMS_STONE_CRAFTING_MATERIALS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("stone_crafting_materials"), Material.class);
    /**
     * Vanilla item tag representing all furnace materials.
     *
     * @deprecated partially replaced by {@link #ITEMS_STONE_CRAFTING_MATERIALS}
     */
    @Deprecated(since = "1.16.2", forRemoval = true)
    Tag<Material> ITEMS_FURNACE_MATERIALS = ITEMS_STONE_CRAFTING_MATERIALS;
    /**
     * Vanilla item tag representing all compasses.
     */
    Tag<Material> ITEMS_COMPASSES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("compasses"), Material.class);
    /**
     * Vanilla item tag representing all hanging signs.
     */
    Tag<Material> ITEMS_HANGING_SIGNS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("hanging_signs"), Material.class);
    /**
     * Vanilla item tag representing all items which will ignite creepers when
     * interacted with.
     */
    Tag<Material> ITEMS_CREEPER_IGNITERS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("creeper_igniters"), Material.class);
    /**
     * Vanilla item tag representing all items which modify note block sounds when placed on top.
     */
    Tag<Material> ITEMS_NOTE_BLOCK_TOP_INSTRUMENTS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("noteblock_top_instruments"), Material.class);
    /**
     * Vanilla item tag representing all foot armor.
     */
    Tag<Material> ITEMS_FOOT_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("foot_armor"), Material.class);
    /**
     * Vanilla item tag representing all leg armor.
     */
    Tag<Material> ITEMS_LEG_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("leg_armor"), Material.class);
    /**
     * Vanilla item tag representing all chest armor.
     */
    Tag<Material> ITEMS_CHEST_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("chest_armor"), Material.class);
    /**
     * Vanilla item tag representing all head armor.
     */
    Tag<Material> ITEMS_HEAD_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("head_armor"), Material.class);
    /**
     * Vanilla item tag representing all skulls.
     */
    Tag<Material> ITEMS_SKULLS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("skulls"), Material.class);
    /**
     * Vanilla item tag representing all trimmable armor items.
     */
    Tag<Material> ITEMS_TRIMMABLE_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("trimmable_armor"), Material.class);
    /**
     * Vanilla item tag representing all materials which can be used for trimming armor.
     */
    Tag<Material> ITEMS_TRIM_MATERIALS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("trim_materials"), Material.class);
    /**
     * Vanilla item tag representing all decorated pot sherds.
     */
    Tag<Material> ITEMS_DECORATED_POT_SHERDS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("decorated_pot_sherds"), Material.class);
    /**
     * Vanilla item tag representing all decorated pot ingredients.
     */
    Tag<Material> ITEMS_DECORATED_POT_INGREDIENTS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("decorated_pot_ingredients"), Material.class);
    /**
     * Vanilla item tag representing all swords.
     */
    Tag<Material> ITEMS_SWORDS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("swords"), Material.class);
    /**
     * Vanilla item tag representing all axes.
     */
    Tag<Material> ITEMS_AXES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("axes"), Material.class);
    /**
     * Vanilla item tag representing all hoes.
     */
    Tag<Material> ITEMS_HOES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("hoes"), Material.class);
    /**
     * Vanilla item tag representing all pickaxes.
     */
    Tag<Material> ITEMS_PICKAXES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("pickaxes"), Material.class);
    /**
     * Vanilla item tag representing all shovels.
     */
    Tag<Material> ITEMS_SHOVELS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("shovels"), Material.class);
    /**
     * Vanilla item tag representing all items which break decorated pots.
     */
    Tag<Material> ITEMS_BREAKS_DECORATED_POTS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("breaks_decorated_pots"), Material.class);
    /**
     * Vanilla item tag representing all tools.
     *
     * @deprecated removed in Minecraft 1.20.5. Do not use. Will be removed at a later date. Until then,
     * this constant now acts as a reference to {@link #ITEMS_BREAKS_DECORATED_POTS} which largely shares
     * the same contents of the old "minecraft:tools" tag.
     */
    @Deprecated(since = "1.20.6", forRemoval = true)
    Tag<Material> ITEMS_TOOLS = ITEMS_BREAKS_DECORATED_POTS;
    /**
     * Vanilla item tag representing all seeds plantable by villagers.
     */
    Tag<Material> ITEMS_VILLAGER_PLANTABLE_SEEDS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("villager_plantable_seeds"), Material.class);
    /**
     * Vanilla item tag representing all items which villagers pick up.
     */
    Tag<Material> ITEMS_VILLAGER_PICKS_UP = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("villager_picks_up"), Material.class);
    /**
     * Vanilla item tag representing all dyeable items.
     */
    Tag<Material> ITEMS_DYEABLE = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("dyeable"), Material.class);
    /**
     * Vanilla item tag representing all furnace minecart fuel.
     */
    Tag<Material> ITEMS_FURNACE_MINECART_FUEL = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("furnace_minecart_fuel"), Material.class);
    /**
     * Vanilla item tag representing all bundle items.
     */
    Tag<Material> ITEMS_BUNDLES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("bundles"), Material.class);
    /**
     * Vanilla item tag representing all skeleton preferred weapons.
     */
    Tag<Material> ITEMS_SKELETON_PREFERRED_WEAPONS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("skeleton_preferred_weapons"), Material.class);
    /**
     * Vanilla item tag representing all drowned preferred weapons.
     */
    Tag<Material> ITEMS_DROWNED_PREFERRED_WEAPONS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("drowned_preferred_weapons"), Material.class);
    /**
     * Vanilla item tag representing all piglin preferred weapons.
     */
    Tag<Material> ITEMS_PIGLIN_PREFERRED_WEAPONS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("piglin_preferred_weapons"), Material.class);
    /**
     * Vanilla item tag representing all pillager preferred weapons.
     */
    Tag<Material> ITEMS_PILLAGER_PREFERRED_WEAPONS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("pillager_preferred_weapons"), Material.class);
    /**
     * Vanilla item tag representing all wither skeleton disliked weapons.
     */
    Tag<Material> ITEMS_WITHER_SKELETON_DISLIKED_WEAPONS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("wither_skeleton_disliked_weapons"), Material.class);
    /**
     * Vanilla item tag representing all enchantable foot armor.
     */
    Tag<Material> ITEMS_ENCHANTABLE_FOOT_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/foot_armor"), Material.class);
    /**
     * Vanilla item tag representing all enchantable leg armor.
     */
    Tag<Material> ITEMS_ENCHANTABLE_LEG_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/leg_armor"), Material.class);
    /**
     * Vanilla item tag representing all enchantable chest armor.
     */
    Tag<Material> ITEMS_ENCHANTABLE_CHEST_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/chest_armor"), Material.class);
    /**
     * Vanilla item tag representing all enchantable head armor.
     */
    Tag<Material> ITEMS_ENCHANTABLE_HEAD_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/head_armor"), Material.class);
    /**
     * Vanilla item tag representing all enchantable armor.
     */
    Tag<Material> ITEMS_ENCHANTABLE_ARMOR = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/armor"), Material.class);
    /**
     * Vanilla item tag representing all enchantable swords.
     */
    Tag<Material> ITEMS_ENCHANTABLE_SWORD = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/sword"), Material.class);
    /**
     * Vanilla item tag representing all items enchantable with the fire aspect enchantment.
     */
    Tag<Material> ITEMS_ENCHANTABLE_FIRE_ASPECT = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/fire_aspect"), Material.class);
    /**
     * Vanilla item tag representing all items enchantable with the sharpness enchantment.
     */
    Tag<Material> ITEMS_ENCHANTABLE_SHARP_WEAPON = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/sharp_weapon"), Material.class);
    /**
     * Vanilla item tag representing all enchantable weapons.
     */
    Tag<Material> ITEMS_ENCHANTABLE_WEAPON = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/weapon"), Material.class);
    /**
     * Vanilla item tag representing all enchantable mining tools.
     */
    Tag<Material> ITEMS_ENCHANTABLE_MINING = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/mining"), Material.class);
    /**
     * Vanilla item tag representing all items enchantable with the mining_loot enchantment.
     */
    Tag<Material> ITEMS_ENCHANTABLE_MINING_LOOT = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/mining_loot"), Material.class);
    /**
     * Vanilla item tag representing all items enchantable with fishing enchantments.
     */
    Tag<Material> ITEMS_ENCHANTABLE_FISHING = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/fishing"), Material.class);
    /**
     * Vanilla item tag representing all items enchantable with trident enchantments.
     */
    Tag<Material> ITEMS_ENCHANTABLE_TRIDENT = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/trident"), Material.class);
    /**
     * Vanilla item tag representing all items enchantable with durability enchantments.
     */
    Tag<Material> ITEMS_ENCHANTABLE_DURABILITY = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/durability"), Material.class);
    /**
     * Vanilla item tag representing all items enchantable with bow enchantments.
     */
    Tag<Material> ITEMS_ENCHANTABLE_BOW = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/bow"), Material.class);
    /**
     * Vanilla item tag representing all items enchantable with the binding enchantment.
     */
    Tag<Material> ITEMS_ENCHANTABLE_EQUIPPABLE = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/equippable"), Material.class);
    /**
     * Vanilla item tag representing all items enchantable with crossbow enchantments.
     */
    Tag<Material> ITEMS_ENCHANTABLE_CROSSBOW = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/crossbow"), Material.class);
    /**
     * Vanilla item tag representing all items enchantable with the vanishing enchantment.
     */
    Tag<Material> ITEMS_ENCHANTABLE_VANISHING = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/vanishing"), Material.class);
    /**
     * Vanilla item tag representing all items enchantable with mace enchantments.
     */
    Tag<Material> ITEMS_ENCHANTABLE_MACE = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("enchantable/mace"), Material.class);
        /**
     * Vanilla item tag representing all items which when equipped will hide the entity from maps.
     */
    Tag<Material> ITEMS_MAP_INVISIBILITY_EQUIPMENT = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("map_invisibility_equipment"), Material.class);
        /**
     * Vanilla item tag representing all items which disguise the wearer's gaze from other entities.
     */
    Tag<Material> ITEMS_GAZE_DISGUISE_EQUIPMENT = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("gaze_disguise_equipment"), Material.class);
    /**
     * Vanilla item tag representing all items that confer freeze immunity on
     * the wearer.
     */
    Tag<Material> FREEZE_IMMUNE_WEARABLES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("freeze_immune_wearables"), Material.class);
    /**
     * Vanilla item tag representing all items which tempt axolotls.
     *
     * @deprecated use {@link #ITEMS_AXOLOTL_FOOD}
     */
    @Deprecated(since = "1.20.5")
    Tag<Material> AXOLOTL_TEMPT_ITEMS = ITEMS_AXOLOTL_FOOD;
    /**
     * Vanilla item tag representing all items which are preferred for
     * harvesting clusters (unused).
     */
    Tag<Material> CLUSTER_MAX_HARVESTABLES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("cluster_max_harvestables"), Material.class);
    /**
     * Key for the built in fluid registry.
     */
    String REGISTRY_FLUIDS = "fluids";
    /**
     * Vanilla fluid tag representing lava and flowing lava.
     */
    Tag<Fluid> FLUIDS_LAVA = Bukkit.getTag(REGISTRY_FLUIDS, NamespacedKey.minecraft("lava"), Fluid.class);
    /**
     * Vanilla fluid tag representing water and flowing water.
     */
    Tag<Fluid> FLUIDS_WATER = Bukkit.getTag(REGISTRY_FLUIDS, NamespacedKey.minecraft("water"), Fluid.class);
    /**
     * Key for the built in entity registry.
     */
    String REGISTRY_ENTITY_TYPES = "entity_types";
    /**
     * Vanilla tag representing skeletons.
     */
    Tag<EntityType> ENTITY_TYPES_SKELETONS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("skeletons"), EntityType.class);
    /**
     * Vanilla tag representing raiders.
     */
    Tag<EntityType> ENTITY_TYPES_RAIDERS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("raiders"), EntityType.class);
    /**
     * Vanilla tag representing entities which can live in beehives.
     */
    Tag<EntityType> ENTITY_TYPES_BEEHIVE_INHABITORS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("beehive_inhabitors"), EntityType.class);
    /**
     * Vanilla tag representing arrows.
     */
    Tag<EntityType> ENTITY_TYPES_ARROWS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("arrows"), EntityType.class);
    /**
     * Vanilla tag representing projectiles.
     */
    Tag<EntityType> ENTITY_TYPES_IMPACT_PROJECTILES = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("impact_projectiles"), EntityType.class);
    /**
     * Vanilla tag representing mobs which can walk on powder snow.
     */
    Tag<EntityType> ENTITY_TYPES_POWDER_SNOW_WALKABLE_MOBS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("powder_snow_walkable_mobs"), EntityType.class);
    /**
     * Vanilla tag representing which entities axolotls are always hostile to.
     */
    Tag<EntityType> ENTITY_TYPES_AXOLOTL_ALWAYS_HOSTILES = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("axolotl_always_hostiles"), EntityType.class);
    /**
     * Vanilla tag representing axolotl targets.
     */
    Tag<EntityType> ENTITY_TYPES_AXOLOTL_HUNT_TARGETS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("axolotl_hunt_targets"), EntityType.class);
    /**
     * Vanilla tag representing entities immune from freezing.
     */
    Tag<EntityType> ENTITY_TYPES_FREEZE_IMMUNE_ENTITY_TYPES = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("freeze_immune_entity_types"), EntityType.class);
    /**
     * Vanilla tag representing entities extra susceptible to freezing.
     */
    Tag<EntityType> ENTITY_TYPES_FREEZE_HURTS_EXTRA_TYPES = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("freeze_hurts_extra_types"), EntityType.class);
    /**
     * Vanilla tag representing entities which can be eaten by frogs.
     */
    Tag<EntityType> ENTITY_TYPES_FROG_FOOD = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("frog_food"), EntityType.class);
    /**
     * Vanilla tag representing entities which are immune from fall damage.
     */
    Tag<EntityType> ENTITY_TYPES_FALL_DAMAGE_IMMUNE = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("fall_damage_immune"), EntityType.class);
    /**
     * Vanilla tag representing entities which are dismounted when underwater.
     */
    Tag<EntityType> ENTITY_TYPES_DISMOUNTS_UNDERWATER = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("dismounts_underwater"), EntityType.class);
    /**
     * Vanilla tag representing entities which are not controlled by their mount.
     */
    Tag<EntityType> ENTITY_TYPES_NON_CONTROLLING_RIDER = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("non_controlling_rider"), EntityType.class);
    /**
     * Vanilla tag representing entities which deflect projectiles.
     */
    Tag<EntityType> ENTITY_TYPES_DEFLECTS_PROJECTILES = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("deflects_projectiles"), EntityType.class);
    /**
     * Vanilla tag representing entities which deflect arrows.
     * @deprecated use {@link #ENTITY_TYPES_DEFLECTS_PROJECTILES}
     */
    @Deprecated(since = "1.20.5")
    Tag<EntityType> ENTITY_TYPES_DEFLECTS_ARROWS = ENTITY_TYPES_DEFLECTS_PROJECTILES;
    /**
     * Vanilla tag representing entities which can turn in boats.
     */
    Tag<EntityType> ENTITY_TYPES_CAN_TURN_IN_BOATS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("can_turn_in_boats"), EntityType.class);
    /**
     * Vanilla tag representing all entities sensitive to illager enchantments.
     */
    Tag<EntityType> ENTITY_TYPES_ILLAGER = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("illager"), EntityType.class);
    /**
     * Vanilla tag representing all entities sensitive to aquatic enchantments..
     */
    Tag<EntityType> ENTITY_TYPES_AQUATIC = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("aquatic"), EntityType.class);
    /**
     * Vanilla tag representing all entities sensitive to arthropod enchantments..
     */
    Tag<EntityType> ENTITY_TYPES_ARTHROPOD = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("arthropod"), EntityType.class);
    /**
     * Vanilla tag representing all entities which ignores poison and regeneration effects.
     */
    Tag<EntityType> ENTITY_TYPES_IGNORES_POISON_AND_REGEN = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("ignores_poison_and_regen"), EntityType.class);
    /**
     * Vanilla tag representing all entities which are sensitive to inverted healing and harm potion effects.
     */
    Tag<EntityType> ENTITY_TYPES_INVERTED_HEALING_AND_HARM = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("inverted_healing_and_harm"), EntityType.class);
    /**
     * Vanilla tag representing all entities which are friendly with withers.
     */
    Tag<EntityType> ENTITY_TYPES_WITHER_FRIENDS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("wither_friends"), EntityType.class);
    /**
     * Vanilla tag representing all entities which are friendly with illagers.
     */
    Tag<EntityType> ENTITY_TYPES_ILLAGER_FRIENDS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("illager_friends"), EntityType.class);
    /**
     * Vanilla tag representing all entities which are not scary for pufferfish.
     */
    Tag<EntityType> ENTITY_TYPES_NOT_SCARY_FOR_PUFFERFISH = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("not_scary_for_pufferfish"), EntityType.class);
    /**
     * Vanilla tag representing all entities which are sensitive to impaling.
     */
    Tag<EntityType> ENTITY_TYPES_SENSITIVE_TO_IMPALING = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("sensitive_to_impaling"), EntityType.class);
    /**
     * Vanilla tag representing all entities which are sensitive to the bane_of_arthropods enchantment.
     */
    Tag<EntityType> ENTITY_TYPES_SENSITIVE_TO_BANE_OF_ARTHROPODS = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("sensitive_to_bane_of_arthropods"), EntityType.class);
    /**
     * Vanilla tag representing all entities which are sensitive to the smite enchantment.
     */
    Tag<EntityType> ENTITY_TYPES_SENSITIVE_TO_SMITE = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("sensitive_to_smite"), EntityType.class);
    /**
     * Vanilla tag representing all entities which do not receive anger from wind charges.
     */
    Tag<EntityType> ENTITY_TYPES_NO_ANGER_FROM_WIND_CHARGE = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("no_anger_from_wind_charge"), EntityType.class);
    /**
     * Vanilla tag representing all entities which are immune from the oozing effect.
     */
    Tag<EntityType> ENTITY_TYPES_IMMUNE_TO_OOZING = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("immune_to_oozing"), EntityType.class);
    /**
     * Vanilla tag representing all entities which are immune from the infested effect.
     */
    Tag<EntityType> ENTITY_TYPES_IMMUNE_TO_INFESTED = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("immune_to_infested"), EntityType.class);
    /**
     * Vanilla tag representing all projectiles which can be punched back.
     */
    Tag<EntityType> ENTITY_TYPES_REDIRECTABLE_PROJECTILE = Bukkit.getTag(REGISTRY_ENTITY_TYPES, NamespacedKey.minecraft("redirectable_projectile"), EntityType.class);

    // Paper start
    String REGISTRY_GAME_EVENTS = "game_events";

    /**
     * Tag for game events that trigger sculk sensors
     */
    Tag<GameEvent> GAME_EVENT_VIBRATIONS = Bukkit.getTag(REGISTRY_GAME_EVENTS, NamespacedKey.minecraft("vibrations"), GameEvent.class);

    /**
     * Tag for game events that are ignored if the entity is sneaking
     */
    Tag<GameEvent> GAME_EVENT_IGNORE_VIBRATIONS_SNEAKING = Bukkit.getTag(REGISTRY_GAME_EVENTS, NamespacedKey.minecraft("ignore_vibrations_sneaking"), GameEvent.class);

    /**
     * Tag for game events that an allay can listen to
     */
    Tag<GameEvent> GAME_EVENT_ALLAY_CAN_LISTEN = Bukkit.getTag(REGISTRY_GAME_EVENTS, NamespacedKey.minecraft("allay_can_listen"), GameEvent.class);
    // Paper end

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
