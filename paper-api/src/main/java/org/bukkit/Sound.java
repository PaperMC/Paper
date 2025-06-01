package org.bukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.papermc.paper.registry.RegistryKey;
import java.util.Locale;
import org.bukkit.packs.DataPack;
import org.bukkit.util.OldEnum;
import org.jetbrains.annotations.NotNull;

/**
 * An interface of Sounds the server is able to send to players.
 * <p>
 * The sounds listed in this interface are present in the default server
 * or can be enabled via a {@link FeatureFlag}.
 * There may be additional sounds present in the server, for example from a {@link DataPack}
 * which can be accessed via {@link Registry#SOUNDS}.
 * <p>
 * <b>WARNING:</b> At any time, sounds may be added/removed from this interface or even
 * Minecraft itself! There is no guarantee the sounds will play. There is no
 * guarantee values will not be removed from this interface. As such, you should not
 * depend on the ordinal values of this class.
 */
public interface Sound extends OldEnum<Sound>, Keyed, net.kyori.adventure.sound.Sound.Type { // Paper - implement Sound.Type

    // Start generate - Sound
    // @GeneratedFrom 1.21.6-pre1
    Sound AMBIENT_BASALT_DELTAS_ADDITIONS = getSound("ambient.basalt_deltas.additions");

    Sound AMBIENT_BASALT_DELTAS_LOOP = getSound("ambient.basalt_deltas.loop");

    Sound AMBIENT_BASALT_DELTAS_MOOD = getSound("ambient.basalt_deltas.mood");

    Sound AMBIENT_CAVE = getSound("ambient.cave");

    Sound AMBIENT_CRIMSON_FOREST_ADDITIONS = getSound("ambient.crimson_forest.additions");

    Sound AMBIENT_CRIMSON_FOREST_LOOP = getSound("ambient.crimson_forest.loop");

    Sound AMBIENT_CRIMSON_FOREST_MOOD = getSound("ambient.crimson_forest.mood");

    Sound AMBIENT_NETHER_WASTES_ADDITIONS = getSound("ambient.nether_wastes.additions");

    Sound AMBIENT_NETHER_WASTES_LOOP = getSound("ambient.nether_wastes.loop");

    Sound AMBIENT_NETHER_WASTES_MOOD = getSound("ambient.nether_wastes.mood");

    Sound AMBIENT_SOUL_SAND_VALLEY_ADDITIONS = getSound("ambient.soul_sand_valley.additions");

    Sound AMBIENT_SOUL_SAND_VALLEY_LOOP = getSound("ambient.soul_sand_valley.loop");

    Sound AMBIENT_SOUL_SAND_VALLEY_MOOD = getSound("ambient.soul_sand_valley.mood");

    Sound AMBIENT_UNDERWATER_ENTER = getSound("ambient.underwater.enter");

    Sound AMBIENT_UNDERWATER_EXIT = getSound("ambient.underwater.exit");

    Sound AMBIENT_UNDERWATER_LOOP = getSound("ambient.underwater.loop");

    Sound AMBIENT_UNDERWATER_LOOP_ADDITIONS = getSound("ambient.underwater.loop.additions");

    Sound AMBIENT_UNDERWATER_LOOP_ADDITIONS_RARE = getSound("ambient.underwater.loop.additions.rare");

    Sound AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRA_RARE = getSound("ambient.underwater.loop.additions.ultra_rare");

    Sound AMBIENT_WARPED_FOREST_ADDITIONS = getSound("ambient.warped_forest.additions");

    Sound AMBIENT_WARPED_FOREST_LOOP = getSound("ambient.warped_forest.loop");

    Sound AMBIENT_WARPED_FOREST_MOOD = getSound("ambient.warped_forest.mood");

    Sound BLOCK_AMETHYST_BLOCK_BREAK = getSound("block.amethyst_block.break");

    Sound BLOCK_AMETHYST_BLOCK_CHIME = getSound("block.amethyst_block.chime");

    Sound BLOCK_AMETHYST_BLOCK_FALL = getSound("block.amethyst_block.fall");

    Sound BLOCK_AMETHYST_BLOCK_HIT = getSound("block.amethyst_block.hit");

    Sound BLOCK_AMETHYST_BLOCK_PLACE = getSound("block.amethyst_block.place");

    Sound BLOCK_AMETHYST_BLOCK_RESONATE = getSound("block.amethyst_block.resonate");

    Sound BLOCK_AMETHYST_BLOCK_STEP = getSound("block.amethyst_block.step");

    Sound BLOCK_AMETHYST_CLUSTER_BREAK = getSound("block.amethyst_cluster.break");

    Sound BLOCK_AMETHYST_CLUSTER_FALL = getSound("block.amethyst_cluster.fall");

    Sound BLOCK_AMETHYST_CLUSTER_HIT = getSound("block.amethyst_cluster.hit");

    Sound BLOCK_AMETHYST_CLUSTER_PLACE = getSound("block.amethyst_cluster.place");

    Sound BLOCK_AMETHYST_CLUSTER_STEP = getSound("block.amethyst_cluster.step");

    Sound BLOCK_ANCIENT_DEBRIS_BREAK = getSound("block.ancient_debris.break");

    Sound BLOCK_ANCIENT_DEBRIS_FALL = getSound("block.ancient_debris.fall");

    Sound BLOCK_ANCIENT_DEBRIS_HIT = getSound("block.ancient_debris.hit");

    Sound BLOCK_ANCIENT_DEBRIS_PLACE = getSound("block.ancient_debris.place");

    Sound BLOCK_ANCIENT_DEBRIS_STEP = getSound("block.ancient_debris.step");

    Sound BLOCK_ANVIL_BREAK = getSound("block.anvil.break");

    Sound BLOCK_ANVIL_DESTROY = getSound("block.anvil.destroy");

    Sound BLOCK_ANVIL_FALL = getSound("block.anvil.fall");

    Sound BLOCK_ANVIL_HIT = getSound("block.anvil.hit");

    Sound BLOCK_ANVIL_LAND = getSound("block.anvil.land");

    Sound BLOCK_ANVIL_PLACE = getSound("block.anvil.place");

    Sound BLOCK_ANVIL_STEP = getSound("block.anvil.step");

    Sound BLOCK_ANVIL_USE = getSound("block.anvil.use");

    Sound BLOCK_AZALEA_BREAK = getSound("block.azalea.break");

    Sound BLOCK_AZALEA_FALL = getSound("block.azalea.fall");

    Sound BLOCK_AZALEA_HIT = getSound("block.azalea.hit");

    Sound BLOCK_AZALEA_PLACE = getSound("block.azalea.place");

    Sound BLOCK_AZALEA_STEP = getSound("block.azalea.step");

    Sound BLOCK_AZALEA_LEAVES_BREAK = getSound("block.azalea_leaves.break");

    Sound BLOCK_AZALEA_LEAVES_FALL = getSound("block.azalea_leaves.fall");

    Sound BLOCK_AZALEA_LEAVES_HIT = getSound("block.azalea_leaves.hit");

    Sound BLOCK_AZALEA_LEAVES_PLACE = getSound("block.azalea_leaves.place");

    Sound BLOCK_AZALEA_LEAVES_STEP = getSound("block.azalea_leaves.step");

    Sound BLOCK_BAMBOO_BREAK = getSound("block.bamboo.break");

    Sound BLOCK_BAMBOO_FALL = getSound("block.bamboo.fall");

    Sound BLOCK_BAMBOO_HIT = getSound("block.bamboo.hit");

    Sound BLOCK_BAMBOO_PLACE = getSound("block.bamboo.place");

    Sound BLOCK_BAMBOO_STEP = getSound("block.bamboo.step");

    Sound BLOCK_BAMBOO_SAPLING_BREAK = getSound("block.bamboo_sapling.break");

    Sound BLOCK_BAMBOO_SAPLING_HIT = getSound("block.bamboo_sapling.hit");

    Sound BLOCK_BAMBOO_SAPLING_PLACE = getSound("block.bamboo_sapling.place");

    Sound BLOCK_BAMBOO_WOOD_BREAK = getSound("block.bamboo_wood.break");

    Sound BLOCK_BAMBOO_WOOD_FALL = getSound("block.bamboo_wood.fall");

    Sound BLOCK_BAMBOO_WOOD_HIT = getSound("block.bamboo_wood.hit");

    Sound BLOCK_BAMBOO_WOOD_PLACE = getSound("block.bamboo_wood.place");

    Sound BLOCK_BAMBOO_WOOD_STEP = getSound("block.bamboo_wood.step");

    Sound BLOCK_BAMBOO_WOOD_BUTTON_CLICK_OFF = getSound("block.bamboo_wood_button.click_off");

    Sound BLOCK_BAMBOO_WOOD_BUTTON_CLICK_ON = getSound("block.bamboo_wood_button.click_on");

    Sound BLOCK_BAMBOO_WOOD_DOOR_CLOSE = getSound("block.bamboo_wood_door.close");

    Sound BLOCK_BAMBOO_WOOD_DOOR_OPEN = getSound("block.bamboo_wood_door.open");

    Sound BLOCK_BAMBOO_WOOD_FENCE_GATE_CLOSE = getSound("block.bamboo_wood_fence_gate.close");

    Sound BLOCK_BAMBOO_WOOD_FENCE_GATE_OPEN = getSound("block.bamboo_wood_fence_gate.open");

    Sound BLOCK_BAMBOO_WOOD_HANGING_SIGN_BREAK = getSound("block.bamboo_wood_hanging_sign.break");

    Sound BLOCK_BAMBOO_WOOD_HANGING_SIGN_FALL = getSound("block.bamboo_wood_hanging_sign.fall");

    Sound BLOCK_BAMBOO_WOOD_HANGING_SIGN_HIT = getSound("block.bamboo_wood_hanging_sign.hit");

    Sound BLOCK_BAMBOO_WOOD_HANGING_SIGN_PLACE = getSound("block.bamboo_wood_hanging_sign.place");

    Sound BLOCK_BAMBOO_WOOD_HANGING_SIGN_STEP = getSound("block.bamboo_wood_hanging_sign.step");

    Sound BLOCK_BAMBOO_WOOD_PRESSURE_PLATE_CLICK_OFF = getSound("block.bamboo_wood_pressure_plate.click_off");

    Sound BLOCK_BAMBOO_WOOD_PRESSURE_PLATE_CLICK_ON = getSound("block.bamboo_wood_pressure_plate.click_on");

    Sound BLOCK_BAMBOO_WOOD_TRAPDOOR_CLOSE = getSound("block.bamboo_wood_trapdoor.close");

    Sound BLOCK_BAMBOO_WOOD_TRAPDOOR_OPEN = getSound("block.bamboo_wood_trapdoor.open");

    Sound BLOCK_BARREL_CLOSE = getSound("block.barrel.close");

    Sound BLOCK_BARREL_OPEN = getSound("block.barrel.open");

    Sound BLOCK_BASALT_BREAK = getSound("block.basalt.break");

    Sound BLOCK_BASALT_FALL = getSound("block.basalt.fall");

    Sound BLOCK_BASALT_HIT = getSound("block.basalt.hit");

    Sound BLOCK_BASALT_PLACE = getSound("block.basalt.place");

    Sound BLOCK_BASALT_STEP = getSound("block.basalt.step");

    Sound BLOCK_BEACON_ACTIVATE = getSound("block.beacon.activate");

    Sound BLOCK_BEACON_AMBIENT = getSound("block.beacon.ambient");

    Sound BLOCK_BEACON_DEACTIVATE = getSound("block.beacon.deactivate");

    Sound BLOCK_BEACON_POWER_SELECT = getSound("block.beacon.power_select");

    Sound BLOCK_BEEHIVE_DRIP = getSound("block.beehive.drip");

    Sound BLOCK_BEEHIVE_ENTER = getSound("block.beehive.enter");

    Sound BLOCK_BEEHIVE_EXIT = getSound("block.beehive.exit");

    Sound BLOCK_BEEHIVE_SHEAR = getSound("block.beehive.shear");

    Sound BLOCK_BEEHIVE_WORK = getSound("block.beehive.work");

    Sound BLOCK_BELL_RESONATE = getSound("block.bell.resonate");

    Sound BLOCK_BELL_USE = getSound("block.bell.use");

    Sound BLOCK_BIG_DRIPLEAF_BREAK = getSound("block.big_dripleaf.break");

    Sound BLOCK_BIG_DRIPLEAF_FALL = getSound("block.big_dripleaf.fall");

    Sound BLOCK_BIG_DRIPLEAF_HIT = getSound("block.big_dripleaf.hit");

    Sound BLOCK_BIG_DRIPLEAF_PLACE = getSound("block.big_dripleaf.place");

    Sound BLOCK_BIG_DRIPLEAF_STEP = getSound("block.big_dripleaf.step");

    Sound BLOCK_BIG_DRIPLEAF_TILT_DOWN = getSound("block.big_dripleaf.tilt_down");

    Sound BLOCK_BIG_DRIPLEAF_TILT_UP = getSound("block.big_dripleaf.tilt_up");

    Sound BLOCK_BLASTFURNACE_FIRE_CRACKLE = getSound("block.blastfurnace.fire_crackle");

    Sound BLOCK_BONE_BLOCK_BREAK = getSound("block.bone_block.break");

    Sound BLOCK_BONE_BLOCK_FALL = getSound("block.bone_block.fall");

    Sound BLOCK_BONE_BLOCK_HIT = getSound("block.bone_block.hit");

    Sound BLOCK_BONE_BLOCK_PLACE = getSound("block.bone_block.place");

    Sound BLOCK_BONE_BLOCK_STEP = getSound("block.bone_block.step");

    Sound BLOCK_BREWING_STAND_BREW = getSound("block.brewing_stand.brew");

    Sound BLOCK_BUBBLE_COLUMN_BUBBLE_POP = getSound("block.bubble_column.bubble_pop");

    Sound BLOCK_BUBBLE_COLUMN_UPWARDS_AMBIENT = getSound("block.bubble_column.upwards_ambient");

    Sound BLOCK_BUBBLE_COLUMN_UPWARDS_INSIDE = getSound("block.bubble_column.upwards_inside");

    Sound BLOCK_BUBBLE_COLUMN_WHIRLPOOL_AMBIENT = getSound("block.bubble_column.whirlpool_ambient");

    Sound BLOCK_BUBBLE_COLUMN_WHIRLPOOL_INSIDE = getSound("block.bubble_column.whirlpool_inside");

    Sound BLOCK_CACTUS_FLOWER_BREAK = getSound("block.cactus_flower.break");

    Sound BLOCK_CACTUS_FLOWER_PLACE = getSound("block.cactus_flower.place");

    Sound BLOCK_CAKE_ADD_CANDLE = getSound("block.cake.add_candle");

    Sound BLOCK_CALCITE_BREAK = getSound("block.calcite.break");

    Sound BLOCK_CALCITE_FALL = getSound("block.calcite.fall");

    Sound BLOCK_CALCITE_HIT = getSound("block.calcite.hit");

    Sound BLOCK_CALCITE_PLACE = getSound("block.calcite.place");

    Sound BLOCK_CALCITE_STEP = getSound("block.calcite.step");

    Sound BLOCK_CAMPFIRE_CRACKLE = getSound("block.campfire.crackle");

    Sound BLOCK_CANDLE_AMBIENT = getSound("block.candle.ambient");

    Sound BLOCK_CANDLE_BREAK = getSound("block.candle.break");

    Sound BLOCK_CANDLE_EXTINGUISH = getSound("block.candle.extinguish");

    Sound BLOCK_CANDLE_FALL = getSound("block.candle.fall");

    Sound BLOCK_CANDLE_HIT = getSound("block.candle.hit");

    Sound BLOCK_CANDLE_PLACE = getSound("block.candle.place");

    Sound BLOCK_CANDLE_STEP = getSound("block.candle.step");

    Sound BLOCK_CAVE_VINES_BREAK = getSound("block.cave_vines.break");

    Sound BLOCK_CAVE_VINES_FALL = getSound("block.cave_vines.fall");

    Sound BLOCK_CAVE_VINES_HIT = getSound("block.cave_vines.hit");

    Sound BLOCK_CAVE_VINES_PICK_BERRIES = getSound("block.cave_vines.pick_berries");

    Sound BLOCK_CAVE_VINES_PLACE = getSound("block.cave_vines.place");

    Sound BLOCK_CAVE_VINES_STEP = getSound("block.cave_vines.step");

    Sound BLOCK_CHAIN_BREAK = getSound("block.chain.break");

    Sound BLOCK_CHAIN_FALL = getSound("block.chain.fall");

    Sound BLOCK_CHAIN_HIT = getSound("block.chain.hit");

    Sound BLOCK_CHAIN_PLACE = getSound("block.chain.place");

    Sound BLOCK_CHAIN_STEP = getSound("block.chain.step");

    Sound BLOCK_CHERRY_LEAVES_BREAK = getSound("block.cherry_leaves.break");

    Sound BLOCK_CHERRY_LEAVES_FALL = getSound("block.cherry_leaves.fall");

    Sound BLOCK_CHERRY_LEAVES_HIT = getSound("block.cherry_leaves.hit");

    Sound BLOCK_CHERRY_LEAVES_PLACE = getSound("block.cherry_leaves.place");

    Sound BLOCK_CHERRY_LEAVES_STEP = getSound("block.cherry_leaves.step");

    Sound BLOCK_CHERRY_SAPLING_BREAK = getSound("block.cherry_sapling.break");

    Sound BLOCK_CHERRY_SAPLING_FALL = getSound("block.cherry_sapling.fall");

    Sound BLOCK_CHERRY_SAPLING_HIT = getSound("block.cherry_sapling.hit");

    Sound BLOCK_CHERRY_SAPLING_PLACE = getSound("block.cherry_sapling.place");

    Sound BLOCK_CHERRY_SAPLING_STEP = getSound("block.cherry_sapling.step");

    Sound BLOCK_CHERRY_WOOD_BREAK = getSound("block.cherry_wood.break");

    Sound BLOCK_CHERRY_WOOD_FALL = getSound("block.cherry_wood.fall");

    Sound BLOCK_CHERRY_WOOD_HIT = getSound("block.cherry_wood.hit");

    Sound BLOCK_CHERRY_WOOD_PLACE = getSound("block.cherry_wood.place");

    Sound BLOCK_CHERRY_WOOD_STEP = getSound("block.cherry_wood.step");

    Sound BLOCK_CHERRY_WOOD_BUTTON_CLICK_OFF = getSound("block.cherry_wood_button.click_off");

    Sound BLOCK_CHERRY_WOOD_BUTTON_CLICK_ON = getSound("block.cherry_wood_button.click_on");

    Sound BLOCK_CHERRY_WOOD_DOOR_CLOSE = getSound("block.cherry_wood_door.close");

    Sound BLOCK_CHERRY_WOOD_DOOR_OPEN = getSound("block.cherry_wood_door.open");

    Sound BLOCK_CHERRY_WOOD_FENCE_GATE_CLOSE = getSound("block.cherry_wood_fence_gate.close");

    Sound BLOCK_CHERRY_WOOD_FENCE_GATE_OPEN = getSound("block.cherry_wood_fence_gate.open");

    Sound BLOCK_CHERRY_WOOD_HANGING_SIGN_BREAK = getSound("block.cherry_wood_hanging_sign.break");

    Sound BLOCK_CHERRY_WOOD_HANGING_SIGN_FALL = getSound("block.cherry_wood_hanging_sign.fall");

    Sound BLOCK_CHERRY_WOOD_HANGING_SIGN_HIT = getSound("block.cherry_wood_hanging_sign.hit");

    Sound BLOCK_CHERRY_WOOD_HANGING_SIGN_PLACE = getSound("block.cherry_wood_hanging_sign.place");

    Sound BLOCK_CHERRY_WOOD_HANGING_SIGN_STEP = getSound("block.cherry_wood_hanging_sign.step");

    Sound BLOCK_CHERRY_WOOD_PRESSURE_PLATE_CLICK_OFF = getSound("block.cherry_wood_pressure_plate.click_off");

    Sound BLOCK_CHERRY_WOOD_PRESSURE_PLATE_CLICK_ON = getSound("block.cherry_wood_pressure_plate.click_on");

    Sound BLOCK_CHERRY_WOOD_TRAPDOOR_CLOSE = getSound("block.cherry_wood_trapdoor.close");

    Sound BLOCK_CHERRY_WOOD_TRAPDOOR_OPEN = getSound("block.cherry_wood_trapdoor.open");

    Sound BLOCK_CHEST_CLOSE = getSound("block.chest.close");

    Sound BLOCK_CHEST_LOCKED = getSound("block.chest.locked");

    Sound BLOCK_CHEST_OPEN = getSound("block.chest.open");

    Sound BLOCK_CHISELED_BOOKSHELF_BREAK = getSound("block.chiseled_bookshelf.break");

    Sound BLOCK_CHISELED_BOOKSHELF_FALL = getSound("block.chiseled_bookshelf.fall");

    Sound BLOCK_CHISELED_BOOKSHELF_HIT = getSound("block.chiseled_bookshelf.hit");

    Sound BLOCK_CHISELED_BOOKSHELF_INSERT = getSound("block.chiseled_bookshelf.insert");

    Sound BLOCK_CHISELED_BOOKSHELF_INSERT_ENCHANTED = getSound("block.chiseled_bookshelf.insert.enchanted");

    Sound BLOCK_CHISELED_BOOKSHELF_PICKUP = getSound("block.chiseled_bookshelf.pickup");

    Sound BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED = getSound("block.chiseled_bookshelf.pickup.enchanted");

    Sound BLOCK_CHISELED_BOOKSHELF_PLACE = getSound("block.chiseled_bookshelf.place");

    Sound BLOCK_CHISELED_BOOKSHELF_STEP = getSound("block.chiseled_bookshelf.step");

    Sound BLOCK_CHORUS_FLOWER_DEATH = getSound("block.chorus_flower.death");

    Sound BLOCK_CHORUS_FLOWER_GROW = getSound("block.chorus_flower.grow");

    Sound BLOCK_COBWEB_BREAK = getSound("block.cobweb.break");

    Sound BLOCK_COBWEB_FALL = getSound("block.cobweb.fall");

    Sound BLOCK_COBWEB_HIT = getSound("block.cobweb.hit");

    Sound BLOCK_COBWEB_PLACE = getSound("block.cobweb.place");

    Sound BLOCK_COBWEB_STEP = getSound("block.cobweb.step");

    Sound BLOCK_COMPARATOR_CLICK = getSound("block.comparator.click");

    Sound BLOCK_COMPOSTER_EMPTY = getSound("block.composter.empty");

    Sound BLOCK_COMPOSTER_FILL = getSound("block.composter.fill");

    Sound BLOCK_COMPOSTER_FILL_SUCCESS = getSound("block.composter.fill_success");

    Sound BLOCK_COMPOSTER_READY = getSound("block.composter.ready");

    Sound BLOCK_CONDUIT_ACTIVATE = getSound("block.conduit.activate");

    Sound BLOCK_CONDUIT_AMBIENT = getSound("block.conduit.ambient");

    Sound BLOCK_CONDUIT_AMBIENT_SHORT = getSound("block.conduit.ambient.short");

    Sound BLOCK_CONDUIT_ATTACK_TARGET = getSound("block.conduit.attack.target");

    Sound BLOCK_CONDUIT_DEACTIVATE = getSound("block.conduit.deactivate");

    Sound BLOCK_COPPER_BREAK = getSound("block.copper.break");

    Sound BLOCK_COPPER_FALL = getSound("block.copper.fall");

    Sound BLOCK_COPPER_HIT = getSound("block.copper.hit");

    Sound BLOCK_COPPER_PLACE = getSound("block.copper.place");

    Sound BLOCK_COPPER_STEP = getSound("block.copper.step");

    Sound BLOCK_COPPER_BULB_BREAK = getSound("block.copper_bulb.break");

    Sound BLOCK_COPPER_BULB_FALL = getSound("block.copper_bulb.fall");

    Sound BLOCK_COPPER_BULB_HIT = getSound("block.copper_bulb.hit");

    Sound BLOCK_COPPER_BULB_PLACE = getSound("block.copper_bulb.place");

    Sound BLOCK_COPPER_BULB_STEP = getSound("block.copper_bulb.step");

    Sound BLOCK_COPPER_BULB_TURN_OFF = getSound("block.copper_bulb.turn_off");

    Sound BLOCK_COPPER_BULB_TURN_ON = getSound("block.copper_bulb.turn_on");

    Sound BLOCK_COPPER_DOOR_CLOSE = getSound("block.copper_door.close");

    Sound BLOCK_COPPER_DOOR_OPEN = getSound("block.copper_door.open");

    Sound BLOCK_COPPER_GRATE_BREAK = getSound("block.copper_grate.break");

    Sound BLOCK_COPPER_GRATE_FALL = getSound("block.copper_grate.fall");

    Sound BLOCK_COPPER_GRATE_HIT = getSound("block.copper_grate.hit");

    Sound BLOCK_COPPER_GRATE_PLACE = getSound("block.copper_grate.place");

    Sound BLOCK_COPPER_GRATE_STEP = getSound("block.copper_grate.step");

    Sound BLOCK_COPPER_TRAPDOOR_CLOSE = getSound("block.copper_trapdoor.close");

    Sound BLOCK_COPPER_TRAPDOOR_OPEN = getSound("block.copper_trapdoor.open");

    Sound BLOCK_CORAL_BLOCK_BREAK = getSound("block.coral_block.break");

    Sound BLOCK_CORAL_BLOCK_FALL = getSound("block.coral_block.fall");

    Sound BLOCK_CORAL_BLOCK_HIT = getSound("block.coral_block.hit");

    Sound BLOCK_CORAL_BLOCK_PLACE = getSound("block.coral_block.place");

    Sound BLOCK_CORAL_BLOCK_STEP = getSound("block.coral_block.step");

    Sound BLOCK_CRAFTER_CRAFT = getSound("block.crafter.craft");

    Sound BLOCK_CRAFTER_FAIL = getSound("block.crafter.fail");

    Sound BLOCK_CREAKING_HEART_BREAK = getSound("block.creaking_heart.break");

    Sound BLOCK_CREAKING_HEART_FALL = getSound("block.creaking_heart.fall");

    Sound BLOCK_CREAKING_HEART_HIT = getSound("block.creaking_heart.hit");

    Sound BLOCK_CREAKING_HEART_HURT = getSound("block.creaking_heart.hurt");

    Sound BLOCK_CREAKING_HEART_IDLE = getSound("block.creaking_heart.idle");

    Sound BLOCK_CREAKING_HEART_PLACE = getSound("block.creaking_heart.place");

    Sound BLOCK_CREAKING_HEART_SPAWN = getSound("block.creaking_heart.spawn");

    Sound BLOCK_CREAKING_HEART_STEP = getSound("block.creaking_heart.step");

    Sound BLOCK_CROP_BREAK = getSound("block.crop.break");

    Sound BLOCK_DEADBUSH_IDLE = getSound("block.deadbush.idle");

    Sound BLOCK_DECORATED_POT_BREAK = getSound("block.decorated_pot.break");

    Sound BLOCK_DECORATED_POT_FALL = getSound("block.decorated_pot.fall");

    Sound BLOCK_DECORATED_POT_HIT = getSound("block.decorated_pot.hit");

    Sound BLOCK_DECORATED_POT_INSERT = getSound("block.decorated_pot.insert");

    Sound BLOCK_DECORATED_POT_INSERT_FAIL = getSound("block.decorated_pot.insert_fail");

    Sound BLOCK_DECORATED_POT_PLACE = getSound("block.decorated_pot.place");

    Sound BLOCK_DECORATED_POT_SHATTER = getSound("block.decorated_pot.shatter");

    Sound BLOCK_DECORATED_POT_STEP = getSound("block.decorated_pot.step");

    Sound BLOCK_DEEPSLATE_BREAK = getSound("block.deepslate.break");

    Sound BLOCK_DEEPSLATE_FALL = getSound("block.deepslate.fall");

    Sound BLOCK_DEEPSLATE_HIT = getSound("block.deepslate.hit");

    Sound BLOCK_DEEPSLATE_PLACE = getSound("block.deepslate.place");

    Sound BLOCK_DEEPSLATE_STEP = getSound("block.deepslate.step");

    Sound BLOCK_DEEPSLATE_BRICKS_BREAK = getSound("block.deepslate_bricks.break");

    Sound BLOCK_DEEPSLATE_BRICKS_FALL = getSound("block.deepslate_bricks.fall");

    Sound BLOCK_DEEPSLATE_BRICKS_HIT = getSound("block.deepslate_bricks.hit");

    Sound BLOCK_DEEPSLATE_BRICKS_PLACE = getSound("block.deepslate_bricks.place");

    Sound BLOCK_DEEPSLATE_BRICKS_STEP = getSound("block.deepslate_bricks.step");

    Sound BLOCK_DEEPSLATE_TILES_BREAK = getSound("block.deepslate_tiles.break");

    Sound BLOCK_DEEPSLATE_TILES_FALL = getSound("block.deepslate_tiles.fall");

    Sound BLOCK_DEEPSLATE_TILES_HIT = getSound("block.deepslate_tiles.hit");

    Sound BLOCK_DEEPSLATE_TILES_PLACE = getSound("block.deepslate_tiles.place");

    Sound BLOCK_DEEPSLATE_TILES_STEP = getSound("block.deepslate_tiles.step");

    Sound BLOCK_DISPENSER_DISPENSE = getSound("block.dispenser.dispense");

    Sound BLOCK_DISPENSER_FAIL = getSound("block.dispenser.fail");

    Sound BLOCK_DISPENSER_LAUNCH = getSound("block.dispenser.launch");

    Sound BLOCK_DRIED_GHAST_AMBIENT = getSound("block.dried_ghast.ambient");

    Sound BLOCK_DRIED_GHAST_AMBIENT_WATER = getSound("block.dried_ghast.ambient_water");

    Sound BLOCK_DRIED_GHAST_BREAK = getSound("block.dried_ghast.break");

    Sound BLOCK_DRIED_GHAST_FALL = getSound("block.dried_ghast.fall");

    Sound BLOCK_DRIED_GHAST_PLACE = getSound("block.dried_ghast.place");

    Sound BLOCK_DRIED_GHAST_PLACE_IN_WATER = getSound("block.dried_ghast.place_in_water");

    Sound BLOCK_DRIED_GHAST_STEP = getSound("block.dried_ghast.step");

    Sound BLOCK_DRIED_GHAST_TRANSITION = getSound("block.dried_ghast.transition");

    Sound BLOCK_DRIPSTONE_BLOCK_BREAK = getSound("block.dripstone_block.break");

    Sound BLOCK_DRIPSTONE_BLOCK_FALL = getSound("block.dripstone_block.fall");

    Sound BLOCK_DRIPSTONE_BLOCK_HIT = getSound("block.dripstone_block.hit");

    Sound BLOCK_DRIPSTONE_BLOCK_PLACE = getSound("block.dripstone_block.place");

    Sound BLOCK_DRIPSTONE_BLOCK_STEP = getSound("block.dripstone_block.step");

    Sound BLOCK_DRY_GRASS_AMBIENT = getSound("block.dry_grass.ambient");

    Sound BLOCK_ENCHANTMENT_TABLE_USE = getSound("block.enchantment_table.use");

    Sound BLOCK_END_GATEWAY_SPAWN = getSound("block.end_gateway.spawn");

    Sound BLOCK_END_PORTAL_SPAWN = getSound("block.end_portal.spawn");

    Sound BLOCK_END_PORTAL_FRAME_FILL = getSound("block.end_portal_frame.fill");

    Sound BLOCK_ENDER_CHEST_CLOSE = getSound("block.ender_chest.close");

    Sound BLOCK_ENDER_CHEST_OPEN = getSound("block.ender_chest.open");

    Sound BLOCK_EYEBLOSSOM_CLOSE = getSound("block.eyeblossom.close");

    Sound BLOCK_EYEBLOSSOM_CLOSE_LONG = getSound("block.eyeblossom.close_long");

    Sound BLOCK_EYEBLOSSOM_IDLE = getSound("block.eyeblossom.idle");

    Sound BLOCK_EYEBLOSSOM_OPEN = getSound("block.eyeblossom.open");

    Sound BLOCK_EYEBLOSSOM_OPEN_LONG = getSound("block.eyeblossom.open_long");

    Sound BLOCK_FENCE_GATE_CLOSE = getSound("block.fence_gate.close");

    Sound BLOCK_FENCE_GATE_OPEN = getSound("block.fence_gate.open");

    Sound BLOCK_FIRE_AMBIENT = getSound("block.fire.ambient");

    Sound BLOCK_FIRE_EXTINGUISH = getSound("block.fire.extinguish");

    Sound BLOCK_FIREFLY_BUSH_IDLE = getSound("block.firefly_bush.idle");

    Sound BLOCK_FLOWERING_AZALEA_BREAK = getSound("block.flowering_azalea.break");

    Sound BLOCK_FLOWERING_AZALEA_FALL = getSound("block.flowering_azalea.fall");

    Sound BLOCK_FLOWERING_AZALEA_HIT = getSound("block.flowering_azalea.hit");

    Sound BLOCK_FLOWERING_AZALEA_PLACE = getSound("block.flowering_azalea.place");

    Sound BLOCK_FLOWERING_AZALEA_STEP = getSound("block.flowering_azalea.step");

    Sound BLOCK_FROGLIGHT_BREAK = getSound("block.froglight.break");

    Sound BLOCK_FROGLIGHT_FALL = getSound("block.froglight.fall");

    Sound BLOCK_FROGLIGHT_HIT = getSound("block.froglight.hit");

    Sound BLOCK_FROGLIGHT_PLACE = getSound("block.froglight.place");

    Sound BLOCK_FROGLIGHT_STEP = getSound("block.froglight.step");

    Sound BLOCK_FROGSPAWN_BREAK = getSound("block.frogspawn.break");

    Sound BLOCK_FROGSPAWN_FALL = getSound("block.frogspawn.fall");

    Sound BLOCK_FROGSPAWN_HATCH = getSound("block.frogspawn.hatch");

    Sound BLOCK_FROGSPAWN_HIT = getSound("block.frogspawn.hit");

    Sound BLOCK_FROGSPAWN_PLACE = getSound("block.frogspawn.place");

    Sound BLOCK_FROGSPAWN_STEP = getSound("block.frogspawn.step");

    Sound BLOCK_FUNGUS_BREAK = getSound("block.fungus.break");

    Sound BLOCK_FUNGUS_FALL = getSound("block.fungus.fall");

    Sound BLOCK_FUNGUS_HIT = getSound("block.fungus.hit");

    Sound BLOCK_FUNGUS_PLACE = getSound("block.fungus.place");

    Sound BLOCK_FUNGUS_STEP = getSound("block.fungus.step");

    Sound BLOCK_FURNACE_FIRE_CRACKLE = getSound("block.furnace.fire_crackle");

    Sound BLOCK_GILDED_BLACKSTONE_BREAK = getSound("block.gilded_blackstone.break");

    Sound BLOCK_GILDED_BLACKSTONE_FALL = getSound("block.gilded_blackstone.fall");

    Sound BLOCK_GILDED_BLACKSTONE_HIT = getSound("block.gilded_blackstone.hit");

    Sound BLOCK_GILDED_BLACKSTONE_PLACE = getSound("block.gilded_blackstone.place");

    Sound BLOCK_GILDED_BLACKSTONE_STEP = getSound("block.gilded_blackstone.step");

    Sound BLOCK_GLASS_BREAK = getSound("block.glass.break");

    Sound BLOCK_GLASS_FALL = getSound("block.glass.fall");

    Sound BLOCK_GLASS_HIT = getSound("block.glass.hit");

    Sound BLOCK_GLASS_PLACE = getSound("block.glass.place");

    Sound BLOCK_GLASS_STEP = getSound("block.glass.step");

    Sound BLOCK_GRASS_BREAK = getSound("block.grass.break");

    Sound BLOCK_GRASS_FALL = getSound("block.grass.fall");

    Sound BLOCK_GRASS_HIT = getSound("block.grass.hit");

    Sound BLOCK_GRASS_PLACE = getSound("block.grass.place");

    Sound BLOCK_GRASS_STEP = getSound("block.grass.step");

    Sound BLOCK_GRAVEL_BREAK = getSound("block.gravel.break");

    Sound BLOCK_GRAVEL_FALL = getSound("block.gravel.fall");

    Sound BLOCK_GRAVEL_HIT = getSound("block.gravel.hit");

    Sound BLOCK_GRAVEL_PLACE = getSound("block.gravel.place");

    Sound BLOCK_GRAVEL_STEP = getSound("block.gravel.step");

    Sound BLOCK_GRINDSTONE_USE = getSound("block.grindstone.use");

    Sound BLOCK_GROWING_PLANT_CROP = getSound("block.growing_plant.crop");

    Sound BLOCK_HANGING_ROOTS_BREAK = getSound("block.hanging_roots.break");

    Sound BLOCK_HANGING_ROOTS_FALL = getSound("block.hanging_roots.fall");

    Sound BLOCK_HANGING_ROOTS_HIT = getSound("block.hanging_roots.hit");

    Sound BLOCK_HANGING_ROOTS_PLACE = getSound("block.hanging_roots.place");

    Sound BLOCK_HANGING_ROOTS_STEP = getSound("block.hanging_roots.step");

    Sound BLOCK_HANGING_SIGN_BREAK = getSound("block.hanging_sign.break");

    Sound BLOCK_HANGING_SIGN_FALL = getSound("block.hanging_sign.fall");

    Sound BLOCK_HANGING_SIGN_HIT = getSound("block.hanging_sign.hit");

    Sound BLOCK_HANGING_SIGN_PLACE = getSound("block.hanging_sign.place");

    Sound BLOCK_HANGING_SIGN_STEP = getSound("block.hanging_sign.step");

    Sound BLOCK_HANGING_SIGN_WAXED_INTERACT_FAIL = getSound("block.hanging_sign.waxed_interact_fail");

    Sound BLOCK_HEAVY_CORE_BREAK = getSound("block.heavy_core.break");

    Sound BLOCK_HEAVY_CORE_FALL = getSound("block.heavy_core.fall");

    Sound BLOCK_HEAVY_CORE_HIT = getSound("block.heavy_core.hit");

    Sound BLOCK_HEAVY_CORE_PLACE = getSound("block.heavy_core.place");

    Sound BLOCK_HEAVY_CORE_STEP = getSound("block.heavy_core.step");

    Sound BLOCK_HONEY_BLOCK_BREAK = getSound("block.honey_block.break");

    Sound BLOCK_HONEY_BLOCK_FALL = getSound("block.honey_block.fall");

    Sound BLOCK_HONEY_BLOCK_HIT = getSound("block.honey_block.hit");

    Sound BLOCK_HONEY_BLOCK_PLACE = getSound("block.honey_block.place");

    Sound BLOCK_HONEY_BLOCK_SLIDE = getSound("block.honey_block.slide");

    Sound BLOCK_HONEY_BLOCK_STEP = getSound("block.honey_block.step");

    Sound BLOCK_IRON_BREAK = getSound("block.iron.break");

    Sound BLOCK_IRON_FALL = getSound("block.iron.fall");

    Sound BLOCK_IRON_HIT = getSound("block.iron.hit");

    Sound BLOCK_IRON_PLACE = getSound("block.iron.place");

    Sound BLOCK_IRON_STEP = getSound("block.iron.step");

    Sound BLOCK_IRON_DOOR_CLOSE = getSound("block.iron_door.close");

    Sound BLOCK_IRON_DOOR_OPEN = getSound("block.iron_door.open");

    Sound BLOCK_IRON_TRAPDOOR_CLOSE = getSound("block.iron_trapdoor.close");

    Sound BLOCK_IRON_TRAPDOOR_OPEN = getSound("block.iron_trapdoor.open");

    Sound BLOCK_LADDER_BREAK = getSound("block.ladder.break");

    Sound BLOCK_LADDER_FALL = getSound("block.ladder.fall");

    Sound BLOCK_LADDER_HIT = getSound("block.ladder.hit");

    Sound BLOCK_LADDER_PLACE = getSound("block.ladder.place");

    Sound BLOCK_LADDER_STEP = getSound("block.ladder.step");

    Sound BLOCK_LANTERN_BREAK = getSound("block.lantern.break");

    Sound BLOCK_LANTERN_FALL = getSound("block.lantern.fall");

    Sound BLOCK_LANTERN_HIT = getSound("block.lantern.hit");

    Sound BLOCK_LANTERN_PLACE = getSound("block.lantern.place");

    Sound BLOCK_LANTERN_STEP = getSound("block.lantern.step");

    Sound BLOCK_LARGE_AMETHYST_BUD_BREAK = getSound("block.large_amethyst_bud.break");

    Sound BLOCK_LARGE_AMETHYST_BUD_PLACE = getSound("block.large_amethyst_bud.place");

    Sound BLOCK_LAVA_AMBIENT = getSound("block.lava.ambient");

    Sound BLOCK_LAVA_EXTINGUISH = getSound("block.lava.extinguish");

    Sound BLOCK_LAVA_POP = getSound("block.lava.pop");

    Sound BLOCK_LEAF_LITTER_BREAK = getSound("block.leaf_litter.break");

    Sound BLOCK_LEAF_LITTER_FALL = getSound("block.leaf_litter.fall");

    Sound BLOCK_LEAF_LITTER_HIT = getSound("block.leaf_litter.hit");

    Sound BLOCK_LEAF_LITTER_PLACE = getSound("block.leaf_litter.place");

    Sound BLOCK_LEAF_LITTER_STEP = getSound("block.leaf_litter.step");

    Sound BLOCK_LEVER_CLICK = getSound("block.lever.click");

    Sound BLOCK_LILY_PAD_PLACE = getSound("block.lily_pad.place");

    Sound BLOCK_LODESTONE_BREAK = getSound("block.lodestone.break");

    Sound BLOCK_LODESTONE_FALL = getSound("block.lodestone.fall");

    Sound BLOCK_LODESTONE_HIT = getSound("block.lodestone.hit");

    Sound BLOCK_LODESTONE_PLACE = getSound("block.lodestone.place");

    Sound BLOCK_LODESTONE_STEP = getSound("block.lodestone.step");

    Sound BLOCK_MANGROVE_ROOTS_BREAK = getSound("block.mangrove_roots.break");

    Sound BLOCK_MANGROVE_ROOTS_FALL = getSound("block.mangrove_roots.fall");

    Sound BLOCK_MANGROVE_ROOTS_HIT = getSound("block.mangrove_roots.hit");

    Sound BLOCK_MANGROVE_ROOTS_PLACE = getSound("block.mangrove_roots.place");

    Sound BLOCK_MANGROVE_ROOTS_STEP = getSound("block.mangrove_roots.step");

    Sound BLOCK_MEDIUM_AMETHYST_BUD_BREAK = getSound("block.medium_amethyst_bud.break");

    Sound BLOCK_MEDIUM_AMETHYST_BUD_PLACE = getSound("block.medium_amethyst_bud.place");

    Sound BLOCK_METAL_BREAK = getSound("block.metal.break");

    Sound BLOCK_METAL_FALL = getSound("block.metal.fall");

    Sound BLOCK_METAL_HIT = getSound("block.metal.hit");

    Sound BLOCK_METAL_PLACE = getSound("block.metal.place");

    Sound BLOCK_METAL_STEP = getSound("block.metal.step");

    Sound BLOCK_METAL_PRESSURE_PLATE_CLICK_OFF = getSound("block.metal_pressure_plate.click_off");

    Sound BLOCK_METAL_PRESSURE_PLATE_CLICK_ON = getSound("block.metal_pressure_plate.click_on");

    Sound BLOCK_MOSS_BREAK = getSound("block.moss.break");

    Sound BLOCK_MOSS_FALL = getSound("block.moss.fall");

    Sound BLOCK_MOSS_HIT = getSound("block.moss.hit");

    Sound BLOCK_MOSS_PLACE = getSound("block.moss.place");

    Sound BLOCK_MOSS_STEP = getSound("block.moss.step");

    Sound BLOCK_MOSS_CARPET_BREAK = getSound("block.moss_carpet.break");

    Sound BLOCK_MOSS_CARPET_FALL = getSound("block.moss_carpet.fall");

    Sound BLOCK_MOSS_CARPET_HIT = getSound("block.moss_carpet.hit");

    Sound BLOCK_MOSS_CARPET_PLACE = getSound("block.moss_carpet.place");

    Sound BLOCK_MOSS_CARPET_STEP = getSound("block.moss_carpet.step");

    Sound BLOCK_MUD_BREAK = getSound("block.mud.break");

    Sound BLOCK_MUD_FALL = getSound("block.mud.fall");

    Sound BLOCK_MUD_HIT = getSound("block.mud.hit");

    Sound BLOCK_MUD_PLACE = getSound("block.mud.place");

    Sound BLOCK_MUD_STEP = getSound("block.mud.step");

    Sound BLOCK_MUD_BRICKS_BREAK = getSound("block.mud_bricks.break");

    Sound BLOCK_MUD_BRICKS_FALL = getSound("block.mud_bricks.fall");

    Sound BLOCK_MUD_BRICKS_HIT = getSound("block.mud_bricks.hit");

    Sound BLOCK_MUD_BRICKS_PLACE = getSound("block.mud_bricks.place");

    Sound BLOCK_MUD_BRICKS_STEP = getSound("block.mud_bricks.step");

    Sound BLOCK_MUDDY_MANGROVE_ROOTS_BREAK = getSound("block.muddy_mangrove_roots.break");

    Sound BLOCK_MUDDY_MANGROVE_ROOTS_FALL = getSound("block.muddy_mangrove_roots.fall");

    Sound BLOCK_MUDDY_MANGROVE_ROOTS_HIT = getSound("block.muddy_mangrove_roots.hit");

    Sound BLOCK_MUDDY_MANGROVE_ROOTS_PLACE = getSound("block.muddy_mangrove_roots.place");

    Sound BLOCK_MUDDY_MANGROVE_ROOTS_STEP = getSound("block.muddy_mangrove_roots.step");

    Sound BLOCK_NETHER_BRICKS_BREAK = getSound("block.nether_bricks.break");

    Sound BLOCK_NETHER_BRICKS_FALL = getSound("block.nether_bricks.fall");

    Sound BLOCK_NETHER_BRICKS_HIT = getSound("block.nether_bricks.hit");

    Sound BLOCK_NETHER_BRICKS_PLACE = getSound("block.nether_bricks.place");

    Sound BLOCK_NETHER_BRICKS_STEP = getSound("block.nether_bricks.step");

    Sound BLOCK_NETHER_GOLD_ORE_BREAK = getSound("block.nether_gold_ore.break");

    Sound BLOCK_NETHER_GOLD_ORE_FALL = getSound("block.nether_gold_ore.fall");

    Sound BLOCK_NETHER_GOLD_ORE_HIT = getSound("block.nether_gold_ore.hit");

    Sound BLOCK_NETHER_GOLD_ORE_PLACE = getSound("block.nether_gold_ore.place");

    Sound BLOCK_NETHER_GOLD_ORE_STEP = getSound("block.nether_gold_ore.step");

    Sound BLOCK_NETHER_ORE_BREAK = getSound("block.nether_ore.break");

    Sound BLOCK_NETHER_ORE_FALL = getSound("block.nether_ore.fall");

    Sound BLOCK_NETHER_ORE_HIT = getSound("block.nether_ore.hit");

    Sound BLOCK_NETHER_ORE_PLACE = getSound("block.nether_ore.place");

    Sound BLOCK_NETHER_ORE_STEP = getSound("block.nether_ore.step");

    Sound BLOCK_NETHER_SPROUTS_BREAK = getSound("block.nether_sprouts.break");

    Sound BLOCK_NETHER_SPROUTS_FALL = getSound("block.nether_sprouts.fall");

    Sound BLOCK_NETHER_SPROUTS_HIT = getSound("block.nether_sprouts.hit");

    Sound BLOCK_NETHER_SPROUTS_PLACE = getSound("block.nether_sprouts.place");

    Sound BLOCK_NETHER_SPROUTS_STEP = getSound("block.nether_sprouts.step");

    Sound BLOCK_NETHER_WART_BREAK = getSound("block.nether_wart.break");

    Sound BLOCK_NETHER_WOOD_BREAK = getSound("block.nether_wood.break");

    Sound BLOCK_NETHER_WOOD_FALL = getSound("block.nether_wood.fall");

    Sound BLOCK_NETHER_WOOD_HIT = getSound("block.nether_wood.hit");

    Sound BLOCK_NETHER_WOOD_PLACE = getSound("block.nether_wood.place");

    Sound BLOCK_NETHER_WOOD_STEP = getSound("block.nether_wood.step");

    Sound BLOCK_NETHER_WOOD_BUTTON_CLICK_OFF = getSound("block.nether_wood_button.click_off");

    Sound BLOCK_NETHER_WOOD_BUTTON_CLICK_ON = getSound("block.nether_wood_button.click_on");

    Sound BLOCK_NETHER_WOOD_DOOR_CLOSE = getSound("block.nether_wood_door.close");

    Sound BLOCK_NETHER_WOOD_DOOR_OPEN = getSound("block.nether_wood_door.open");

    Sound BLOCK_NETHER_WOOD_FENCE_GATE_CLOSE = getSound("block.nether_wood_fence_gate.close");

    Sound BLOCK_NETHER_WOOD_FENCE_GATE_OPEN = getSound("block.nether_wood_fence_gate.open");

    Sound BLOCK_NETHER_WOOD_HANGING_SIGN_BREAK = getSound("block.nether_wood_hanging_sign.break");

    Sound BLOCK_NETHER_WOOD_HANGING_SIGN_FALL = getSound("block.nether_wood_hanging_sign.fall");

    Sound BLOCK_NETHER_WOOD_HANGING_SIGN_HIT = getSound("block.nether_wood_hanging_sign.hit");

    Sound BLOCK_NETHER_WOOD_HANGING_SIGN_PLACE = getSound("block.nether_wood_hanging_sign.place");

    Sound BLOCK_NETHER_WOOD_HANGING_SIGN_STEP = getSound("block.nether_wood_hanging_sign.step");

    Sound BLOCK_NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF = getSound("block.nether_wood_pressure_plate.click_off");

    Sound BLOCK_NETHER_WOOD_PRESSURE_PLATE_CLICK_ON = getSound("block.nether_wood_pressure_plate.click_on");

    Sound BLOCK_NETHER_WOOD_TRAPDOOR_CLOSE = getSound("block.nether_wood_trapdoor.close");

    Sound BLOCK_NETHER_WOOD_TRAPDOOR_OPEN = getSound("block.nether_wood_trapdoor.open");

    Sound BLOCK_NETHERITE_BLOCK_BREAK = getSound("block.netherite_block.break");

    Sound BLOCK_NETHERITE_BLOCK_FALL = getSound("block.netherite_block.fall");

    Sound BLOCK_NETHERITE_BLOCK_HIT = getSound("block.netherite_block.hit");

    Sound BLOCK_NETHERITE_BLOCK_PLACE = getSound("block.netherite_block.place");

    Sound BLOCK_NETHERITE_BLOCK_STEP = getSound("block.netherite_block.step");

    Sound BLOCK_NETHERRACK_BREAK = getSound("block.netherrack.break");

    Sound BLOCK_NETHERRACK_FALL = getSound("block.netherrack.fall");

    Sound BLOCK_NETHERRACK_HIT = getSound("block.netherrack.hit");

    Sound BLOCK_NETHERRACK_PLACE = getSound("block.netherrack.place");

    Sound BLOCK_NETHERRACK_STEP = getSound("block.netherrack.step");

    Sound BLOCK_NOTE_BLOCK_BANJO = getSound("block.note_block.banjo");

    Sound BLOCK_NOTE_BLOCK_BASEDRUM = getSound("block.note_block.basedrum");

    Sound BLOCK_NOTE_BLOCK_BASS = getSound("block.note_block.bass");

    Sound BLOCK_NOTE_BLOCK_BELL = getSound("block.note_block.bell");

    Sound BLOCK_NOTE_BLOCK_BIT = getSound("block.note_block.bit");

    Sound BLOCK_NOTE_BLOCK_CHIME = getSound("block.note_block.chime");

    Sound BLOCK_NOTE_BLOCK_COW_BELL = getSound("block.note_block.cow_bell");

    Sound BLOCK_NOTE_BLOCK_DIDGERIDOO = getSound("block.note_block.didgeridoo");

    Sound BLOCK_NOTE_BLOCK_FLUTE = getSound("block.note_block.flute");

    Sound BLOCK_NOTE_BLOCK_GUITAR = getSound("block.note_block.guitar");

    Sound BLOCK_NOTE_BLOCK_HARP = getSound("block.note_block.harp");

    Sound BLOCK_NOTE_BLOCK_HAT = getSound("block.note_block.hat");

    Sound BLOCK_NOTE_BLOCK_IMITATE_CREEPER = getSound("block.note_block.imitate.creeper");

    Sound BLOCK_NOTE_BLOCK_IMITATE_ENDER_DRAGON = getSound("block.note_block.imitate.ender_dragon");

    Sound BLOCK_NOTE_BLOCK_IMITATE_PIGLIN = getSound("block.note_block.imitate.piglin");

    Sound BLOCK_NOTE_BLOCK_IMITATE_SKELETON = getSound("block.note_block.imitate.skeleton");

    Sound BLOCK_NOTE_BLOCK_IMITATE_WITHER_SKELETON = getSound("block.note_block.imitate.wither_skeleton");

    Sound BLOCK_NOTE_BLOCK_IMITATE_ZOMBIE = getSound("block.note_block.imitate.zombie");

    Sound BLOCK_NOTE_BLOCK_IRON_XYLOPHONE = getSound("block.note_block.iron_xylophone");

    Sound BLOCK_NOTE_BLOCK_PLING = getSound("block.note_block.pling");

    Sound BLOCK_NOTE_BLOCK_SNARE = getSound("block.note_block.snare");

    Sound BLOCK_NOTE_BLOCK_XYLOPHONE = getSound("block.note_block.xylophone");

    Sound BLOCK_NYLIUM_BREAK = getSound("block.nylium.break");

    Sound BLOCK_NYLIUM_FALL = getSound("block.nylium.fall");

    Sound BLOCK_NYLIUM_HIT = getSound("block.nylium.hit");

    Sound BLOCK_NYLIUM_PLACE = getSound("block.nylium.place");

    Sound BLOCK_NYLIUM_STEP = getSound("block.nylium.step");

    Sound BLOCK_PACKED_MUD_BREAK = getSound("block.packed_mud.break");

    Sound BLOCK_PACKED_MUD_FALL = getSound("block.packed_mud.fall");

    Sound BLOCK_PACKED_MUD_HIT = getSound("block.packed_mud.hit");

    Sound BLOCK_PACKED_MUD_PLACE = getSound("block.packed_mud.place");

    Sound BLOCK_PACKED_MUD_STEP = getSound("block.packed_mud.step");

    Sound BLOCK_PALE_HANGING_MOSS_IDLE = getSound("block.pale_hanging_moss.idle");

    Sound BLOCK_PINK_PETALS_BREAK = getSound("block.pink_petals.break");

    Sound BLOCK_PINK_PETALS_FALL = getSound("block.pink_petals.fall");

    Sound BLOCK_PINK_PETALS_HIT = getSound("block.pink_petals.hit");

    Sound BLOCK_PINK_PETALS_PLACE = getSound("block.pink_petals.place");

    Sound BLOCK_PINK_PETALS_STEP = getSound("block.pink_petals.step");

    Sound BLOCK_PISTON_CONTRACT = getSound("block.piston.contract");

    Sound BLOCK_PISTON_EXTEND = getSound("block.piston.extend");

    Sound BLOCK_POINTED_DRIPSTONE_BREAK = getSound("block.pointed_dripstone.break");

    Sound BLOCK_POINTED_DRIPSTONE_DRIP_LAVA = getSound("block.pointed_dripstone.drip_lava");

    Sound BLOCK_POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON = getSound("block.pointed_dripstone.drip_lava_into_cauldron");

    Sound BLOCK_POINTED_DRIPSTONE_DRIP_WATER = getSound("block.pointed_dripstone.drip_water");

    Sound BLOCK_POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON = getSound("block.pointed_dripstone.drip_water_into_cauldron");

    Sound BLOCK_POINTED_DRIPSTONE_FALL = getSound("block.pointed_dripstone.fall");

    Sound BLOCK_POINTED_DRIPSTONE_HIT = getSound("block.pointed_dripstone.hit");

    Sound BLOCK_POINTED_DRIPSTONE_LAND = getSound("block.pointed_dripstone.land");

    Sound BLOCK_POINTED_DRIPSTONE_PLACE = getSound("block.pointed_dripstone.place");

    Sound BLOCK_POINTED_DRIPSTONE_STEP = getSound("block.pointed_dripstone.step");

    Sound BLOCK_POLISHED_DEEPSLATE_BREAK = getSound("block.polished_deepslate.break");

    Sound BLOCK_POLISHED_DEEPSLATE_FALL = getSound("block.polished_deepslate.fall");

    Sound BLOCK_POLISHED_DEEPSLATE_HIT = getSound("block.polished_deepslate.hit");

    Sound BLOCK_POLISHED_DEEPSLATE_PLACE = getSound("block.polished_deepslate.place");

    Sound BLOCK_POLISHED_DEEPSLATE_STEP = getSound("block.polished_deepslate.step");

    Sound BLOCK_POLISHED_TUFF_BREAK = getSound("block.polished_tuff.break");

    Sound BLOCK_POLISHED_TUFF_FALL = getSound("block.polished_tuff.fall");

    Sound BLOCK_POLISHED_TUFF_HIT = getSound("block.polished_tuff.hit");

    Sound BLOCK_POLISHED_TUFF_PLACE = getSound("block.polished_tuff.place");

    Sound BLOCK_POLISHED_TUFF_STEP = getSound("block.polished_tuff.step");

    Sound BLOCK_PORTAL_AMBIENT = getSound("block.portal.ambient");

    Sound BLOCK_PORTAL_TRAVEL = getSound("block.portal.travel");

    Sound BLOCK_PORTAL_TRIGGER = getSound("block.portal.trigger");

    Sound BLOCK_POWDER_SNOW_BREAK = getSound("block.powder_snow.break");

    Sound BLOCK_POWDER_SNOW_FALL = getSound("block.powder_snow.fall");

    Sound BLOCK_POWDER_SNOW_HIT = getSound("block.powder_snow.hit");

    Sound BLOCK_POWDER_SNOW_PLACE = getSound("block.powder_snow.place");

    Sound BLOCK_POWDER_SNOW_STEP = getSound("block.powder_snow.step");

    Sound BLOCK_PUMPKIN_CARVE = getSound("block.pumpkin.carve");

    Sound BLOCK_REDSTONE_TORCH_BURNOUT = getSound("block.redstone_torch.burnout");

    Sound BLOCK_RESIN_BREAK = getSound("block.resin.break");

    Sound BLOCK_RESIN_FALL = getSound("block.resin.fall");

    Sound BLOCK_RESIN_PLACE = getSound("block.resin.place");

    Sound BLOCK_RESIN_STEP = getSound("block.resin.step");

    Sound BLOCK_RESIN_BRICKS_BREAK = getSound("block.resin_bricks.break");

    Sound BLOCK_RESIN_BRICKS_FALL = getSound("block.resin_bricks.fall");

    Sound BLOCK_RESIN_BRICKS_HIT = getSound("block.resin_bricks.hit");

    Sound BLOCK_RESIN_BRICKS_PLACE = getSound("block.resin_bricks.place");

    Sound BLOCK_RESIN_BRICKS_STEP = getSound("block.resin_bricks.step");

    Sound BLOCK_RESPAWN_ANCHOR_AMBIENT = getSound("block.respawn_anchor.ambient");

    Sound BLOCK_RESPAWN_ANCHOR_CHARGE = getSound("block.respawn_anchor.charge");

    Sound BLOCK_RESPAWN_ANCHOR_DEPLETE = getSound("block.respawn_anchor.deplete");

    Sound BLOCK_RESPAWN_ANCHOR_SET_SPAWN = getSound("block.respawn_anchor.set_spawn");

    Sound BLOCK_ROOTED_DIRT_BREAK = getSound("block.rooted_dirt.break");

    Sound BLOCK_ROOTED_DIRT_FALL = getSound("block.rooted_dirt.fall");

    Sound BLOCK_ROOTED_DIRT_HIT = getSound("block.rooted_dirt.hit");

    Sound BLOCK_ROOTED_DIRT_PLACE = getSound("block.rooted_dirt.place");

    Sound BLOCK_ROOTED_DIRT_STEP = getSound("block.rooted_dirt.step");

    Sound BLOCK_ROOTS_BREAK = getSound("block.roots.break");

    Sound BLOCK_ROOTS_FALL = getSound("block.roots.fall");

    Sound BLOCK_ROOTS_HIT = getSound("block.roots.hit");

    Sound BLOCK_ROOTS_PLACE = getSound("block.roots.place");

    Sound BLOCK_ROOTS_STEP = getSound("block.roots.step");

    Sound BLOCK_SAND_BREAK = getSound("block.sand.break");

    Sound BLOCK_SAND_FALL = getSound("block.sand.fall");

    Sound BLOCK_SAND_HIT = getSound("block.sand.hit");

    Sound BLOCK_SAND_IDLE = getSound("block.sand.idle");

    Sound BLOCK_SAND_PLACE = getSound("block.sand.place");

    Sound BLOCK_SAND_STEP = getSound("block.sand.step");

    Sound BLOCK_SCAFFOLDING_BREAK = getSound("block.scaffolding.break");

    Sound BLOCK_SCAFFOLDING_FALL = getSound("block.scaffolding.fall");

    Sound BLOCK_SCAFFOLDING_HIT = getSound("block.scaffolding.hit");

    Sound BLOCK_SCAFFOLDING_PLACE = getSound("block.scaffolding.place");

    Sound BLOCK_SCAFFOLDING_STEP = getSound("block.scaffolding.step");

    Sound BLOCK_SCULK_BREAK = getSound("block.sculk.break");

    Sound BLOCK_SCULK_CHARGE = getSound("block.sculk.charge");

    Sound BLOCK_SCULK_FALL = getSound("block.sculk.fall");

    Sound BLOCK_SCULK_HIT = getSound("block.sculk.hit");

    Sound BLOCK_SCULK_PLACE = getSound("block.sculk.place");

    Sound BLOCK_SCULK_SPREAD = getSound("block.sculk.spread");

    Sound BLOCK_SCULK_STEP = getSound("block.sculk.step");

    Sound BLOCK_SCULK_CATALYST_BLOOM = getSound("block.sculk_catalyst.bloom");

    Sound BLOCK_SCULK_CATALYST_BREAK = getSound("block.sculk_catalyst.break");

    Sound BLOCK_SCULK_CATALYST_FALL = getSound("block.sculk_catalyst.fall");

    Sound BLOCK_SCULK_CATALYST_HIT = getSound("block.sculk_catalyst.hit");

    Sound BLOCK_SCULK_CATALYST_PLACE = getSound("block.sculk_catalyst.place");

    Sound BLOCK_SCULK_CATALYST_STEP = getSound("block.sculk_catalyst.step");

    Sound BLOCK_SCULK_SENSOR_BREAK = getSound("block.sculk_sensor.break");

    Sound BLOCK_SCULK_SENSOR_CLICKING = getSound("block.sculk_sensor.clicking");

    Sound BLOCK_SCULK_SENSOR_CLICKING_STOP = getSound("block.sculk_sensor.clicking_stop");

    Sound BLOCK_SCULK_SENSOR_FALL = getSound("block.sculk_sensor.fall");

    Sound BLOCK_SCULK_SENSOR_HIT = getSound("block.sculk_sensor.hit");

    Sound BLOCK_SCULK_SENSOR_PLACE = getSound("block.sculk_sensor.place");

    Sound BLOCK_SCULK_SENSOR_STEP = getSound("block.sculk_sensor.step");

    Sound BLOCK_SCULK_SHRIEKER_BREAK = getSound("block.sculk_shrieker.break");

    Sound BLOCK_SCULK_SHRIEKER_FALL = getSound("block.sculk_shrieker.fall");

    Sound BLOCK_SCULK_SHRIEKER_HIT = getSound("block.sculk_shrieker.hit");

    Sound BLOCK_SCULK_SHRIEKER_PLACE = getSound("block.sculk_shrieker.place");

    Sound BLOCK_SCULK_SHRIEKER_SHRIEK = getSound("block.sculk_shrieker.shriek");

    Sound BLOCK_SCULK_SHRIEKER_STEP = getSound("block.sculk_shrieker.step");

    Sound BLOCK_SCULK_VEIN_BREAK = getSound("block.sculk_vein.break");

    Sound BLOCK_SCULK_VEIN_FALL = getSound("block.sculk_vein.fall");

    Sound BLOCK_SCULK_VEIN_HIT = getSound("block.sculk_vein.hit");

    Sound BLOCK_SCULK_VEIN_PLACE = getSound("block.sculk_vein.place");

    Sound BLOCK_SCULK_VEIN_STEP = getSound("block.sculk_vein.step");

    Sound BLOCK_SHROOMLIGHT_BREAK = getSound("block.shroomlight.break");

    Sound BLOCK_SHROOMLIGHT_FALL = getSound("block.shroomlight.fall");

    Sound BLOCK_SHROOMLIGHT_HIT = getSound("block.shroomlight.hit");

    Sound BLOCK_SHROOMLIGHT_PLACE = getSound("block.shroomlight.place");

    Sound BLOCK_SHROOMLIGHT_STEP = getSound("block.shroomlight.step");

    Sound BLOCK_SHULKER_BOX_CLOSE = getSound("block.shulker_box.close");

    Sound BLOCK_SHULKER_BOX_OPEN = getSound("block.shulker_box.open");

    Sound BLOCK_SIGN_WAXED_INTERACT_FAIL = getSound("block.sign.waxed_interact_fail");

    Sound BLOCK_SLIME_BLOCK_BREAK = getSound("block.slime_block.break");

    Sound BLOCK_SLIME_BLOCK_FALL = getSound("block.slime_block.fall");

    Sound BLOCK_SLIME_BLOCK_HIT = getSound("block.slime_block.hit");

    Sound BLOCK_SLIME_BLOCK_PLACE = getSound("block.slime_block.place");

    Sound BLOCK_SLIME_BLOCK_STEP = getSound("block.slime_block.step");

    Sound BLOCK_SMALL_AMETHYST_BUD_BREAK = getSound("block.small_amethyst_bud.break");

    Sound BLOCK_SMALL_AMETHYST_BUD_PLACE = getSound("block.small_amethyst_bud.place");

    Sound BLOCK_SMALL_DRIPLEAF_BREAK = getSound("block.small_dripleaf.break");

    Sound BLOCK_SMALL_DRIPLEAF_FALL = getSound("block.small_dripleaf.fall");

    Sound BLOCK_SMALL_DRIPLEAF_HIT = getSound("block.small_dripleaf.hit");

    Sound BLOCK_SMALL_DRIPLEAF_PLACE = getSound("block.small_dripleaf.place");

    Sound BLOCK_SMALL_DRIPLEAF_STEP = getSound("block.small_dripleaf.step");

    Sound BLOCK_SMITHING_TABLE_USE = getSound("block.smithing_table.use");

    Sound BLOCK_SMOKER_SMOKE = getSound("block.smoker.smoke");

    Sound BLOCK_SNIFFER_EGG_CRACK = getSound("block.sniffer_egg.crack");

    Sound BLOCK_SNIFFER_EGG_HATCH = getSound("block.sniffer_egg.hatch");

    Sound BLOCK_SNIFFER_EGG_PLOP = getSound("block.sniffer_egg.plop");

    Sound BLOCK_SNOW_BREAK = getSound("block.snow.break");

    Sound BLOCK_SNOW_FALL = getSound("block.snow.fall");

    Sound BLOCK_SNOW_HIT = getSound("block.snow.hit");

    Sound BLOCK_SNOW_PLACE = getSound("block.snow.place");

    Sound BLOCK_SNOW_STEP = getSound("block.snow.step");

    Sound BLOCK_SOUL_SAND_BREAK = getSound("block.soul_sand.break");

    Sound BLOCK_SOUL_SAND_FALL = getSound("block.soul_sand.fall");

    Sound BLOCK_SOUL_SAND_HIT = getSound("block.soul_sand.hit");

    Sound BLOCK_SOUL_SAND_PLACE = getSound("block.soul_sand.place");

    Sound BLOCK_SOUL_SAND_STEP = getSound("block.soul_sand.step");

    Sound BLOCK_SOUL_SOIL_BREAK = getSound("block.soul_soil.break");

    Sound BLOCK_SOUL_SOIL_FALL = getSound("block.soul_soil.fall");

    Sound BLOCK_SOUL_SOIL_HIT = getSound("block.soul_soil.hit");

    Sound BLOCK_SOUL_SOIL_PLACE = getSound("block.soul_soil.place");

    Sound BLOCK_SOUL_SOIL_STEP = getSound("block.soul_soil.step");

    Sound BLOCK_SPAWNER_BREAK = getSound("block.spawner.break");

    Sound BLOCK_SPAWNER_FALL = getSound("block.spawner.fall");

    Sound BLOCK_SPAWNER_HIT = getSound("block.spawner.hit");

    Sound BLOCK_SPAWNER_PLACE = getSound("block.spawner.place");

    Sound BLOCK_SPAWNER_STEP = getSound("block.spawner.step");

    Sound BLOCK_SPONGE_ABSORB = getSound("block.sponge.absorb");

    Sound BLOCK_SPONGE_BREAK = getSound("block.sponge.break");

    Sound BLOCK_SPONGE_FALL = getSound("block.sponge.fall");

    Sound BLOCK_SPONGE_HIT = getSound("block.sponge.hit");

    Sound BLOCK_SPONGE_PLACE = getSound("block.sponge.place");

    Sound BLOCK_SPONGE_STEP = getSound("block.sponge.step");

    Sound BLOCK_SPORE_BLOSSOM_BREAK = getSound("block.spore_blossom.break");

    Sound BLOCK_SPORE_BLOSSOM_FALL = getSound("block.spore_blossom.fall");

    Sound BLOCK_SPORE_BLOSSOM_HIT = getSound("block.spore_blossom.hit");

    Sound BLOCK_SPORE_BLOSSOM_PLACE = getSound("block.spore_blossom.place");

    Sound BLOCK_SPORE_BLOSSOM_STEP = getSound("block.spore_blossom.step");

    Sound BLOCK_STEM_BREAK = getSound("block.stem.break");

    Sound BLOCK_STEM_FALL = getSound("block.stem.fall");

    Sound BLOCK_STEM_HIT = getSound("block.stem.hit");

    Sound BLOCK_STEM_PLACE = getSound("block.stem.place");

    Sound BLOCK_STEM_STEP = getSound("block.stem.step");

    Sound BLOCK_STONE_BREAK = getSound("block.stone.break");

    Sound BLOCK_STONE_FALL = getSound("block.stone.fall");

    Sound BLOCK_STONE_HIT = getSound("block.stone.hit");

    Sound BLOCK_STONE_PLACE = getSound("block.stone.place");

    Sound BLOCK_STONE_STEP = getSound("block.stone.step");

    Sound BLOCK_STONE_BUTTON_CLICK_OFF = getSound("block.stone_button.click_off");

    Sound BLOCK_STONE_BUTTON_CLICK_ON = getSound("block.stone_button.click_on");

    Sound BLOCK_STONE_PRESSURE_PLATE_CLICK_OFF = getSound("block.stone_pressure_plate.click_off");

    Sound BLOCK_STONE_PRESSURE_PLATE_CLICK_ON = getSound("block.stone_pressure_plate.click_on");

    Sound BLOCK_SUSPICIOUS_GRAVEL_BREAK = getSound("block.suspicious_gravel.break");

    Sound BLOCK_SUSPICIOUS_GRAVEL_FALL = getSound("block.suspicious_gravel.fall");

    Sound BLOCK_SUSPICIOUS_GRAVEL_HIT = getSound("block.suspicious_gravel.hit");

    Sound BLOCK_SUSPICIOUS_GRAVEL_PLACE = getSound("block.suspicious_gravel.place");

    Sound BLOCK_SUSPICIOUS_GRAVEL_STEP = getSound("block.suspicious_gravel.step");

    Sound BLOCK_SUSPICIOUS_SAND_BREAK = getSound("block.suspicious_sand.break");

    Sound BLOCK_SUSPICIOUS_SAND_FALL = getSound("block.suspicious_sand.fall");

    Sound BLOCK_SUSPICIOUS_SAND_HIT = getSound("block.suspicious_sand.hit");

    Sound BLOCK_SUSPICIOUS_SAND_PLACE = getSound("block.suspicious_sand.place");

    Sound BLOCK_SUSPICIOUS_SAND_STEP = getSound("block.suspicious_sand.step");

    Sound BLOCK_SWEET_BERRY_BUSH_BREAK = getSound("block.sweet_berry_bush.break");

    Sound BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES = getSound("block.sweet_berry_bush.pick_berries");

    Sound BLOCK_SWEET_BERRY_BUSH_PLACE = getSound("block.sweet_berry_bush.place");

    Sound BLOCK_TRIAL_SPAWNER_ABOUT_TO_SPAWN_ITEM = getSound("block.trial_spawner.about_to_spawn_item");

    Sound BLOCK_TRIAL_SPAWNER_AMBIENT = getSound("block.trial_spawner.ambient");

    Sound BLOCK_TRIAL_SPAWNER_AMBIENT_OMINOUS = getSound("block.trial_spawner.ambient_ominous");

    Sound BLOCK_TRIAL_SPAWNER_BREAK = getSound("block.trial_spawner.break");

    Sound BLOCK_TRIAL_SPAWNER_CLOSE_SHUTTER = getSound("block.trial_spawner.close_shutter");

    Sound BLOCK_TRIAL_SPAWNER_DETECT_PLAYER = getSound("block.trial_spawner.detect_player");

    Sound BLOCK_TRIAL_SPAWNER_EJECT_ITEM = getSound("block.trial_spawner.eject_item");

    Sound BLOCK_TRIAL_SPAWNER_FALL = getSound("block.trial_spawner.fall");

    Sound BLOCK_TRIAL_SPAWNER_HIT = getSound("block.trial_spawner.hit");

    Sound BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE = getSound("block.trial_spawner.ominous_activate");

    Sound BLOCK_TRIAL_SPAWNER_OPEN_SHUTTER = getSound("block.trial_spawner.open_shutter");

    Sound BLOCK_TRIAL_SPAWNER_PLACE = getSound("block.trial_spawner.place");

    Sound BLOCK_TRIAL_SPAWNER_SPAWN_ITEM = getSound("block.trial_spawner.spawn_item");

    Sound BLOCK_TRIAL_SPAWNER_SPAWN_ITEM_BEGIN = getSound("block.trial_spawner.spawn_item_begin");

    Sound BLOCK_TRIAL_SPAWNER_SPAWN_MOB = getSound("block.trial_spawner.spawn_mob");

    Sound BLOCK_TRIAL_SPAWNER_STEP = getSound("block.trial_spawner.step");

    Sound BLOCK_TRIPWIRE_ATTACH = getSound("block.tripwire.attach");

    Sound BLOCK_TRIPWIRE_CLICK_OFF = getSound("block.tripwire.click_off");

    Sound BLOCK_TRIPWIRE_CLICK_ON = getSound("block.tripwire.click_on");

    Sound BLOCK_TRIPWIRE_DETACH = getSound("block.tripwire.detach");

    Sound BLOCK_TUFF_BREAK = getSound("block.tuff.break");

    Sound BLOCK_TUFF_FALL = getSound("block.tuff.fall");

    Sound BLOCK_TUFF_HIT = getSound("block.tuff.hit");

    Sound BLOCK_TUFF_PLACE = getSound("block.tuff.place");

    Sound BLOCK_TUFF_STEP = getSound("block.tuff.step");

    Sound BLOCK_TUFF_BRICKS_BREAK = getSound("block.tuff_bricks.break");

    Sound BLOCK_TUFF_BRICKS_FALL = getSound("block.tuff_bricks.fall");

    Sound BLOCK_TUFF_BRICKS_HIT = getSound("block.tuff_bricks.hit");

    Sound BLOCK_TUFF_BRICKS_PLACE = getSound("block.tuff_bricks.place");

    Sound BLOCK_TUFF_BRICKS_STEP = getSound("block.tuff_bricks.step");

    Sound BLOCK_VAULT_ACTIVATE = getSound("block.vault.activate");

    Sound BLOCK_VAULT_AMBIENT = getSound("block.vault.ambient");

    Sound BLOCK_VAULT_BREAK = getSound("block.vault.break");

    Sound BLOCK_VAULT_CLOSE_SHUTTER = getSound("block.vault.close_shutter");

    Sound BLOCK_VAULT_DEACTIVATE = getSound("block.vault.deactivate");

    Sound BLOCK_VAULT_EJECT_ITEM = getSound("block.vault.eject_item");

    Sound BLOCK_VAULT_FALL = getSound("block.vault.fall");

    Sound BLOCK_VAULT_HIT = getSound("block.vault.hit");

    Sound BLOCK_VAULT_INSERT_ITEM = getSound("block.vault.insert_item");

    Sound BLOCK_VAULT_INSERT_ITEM_FAIL = getSound("block.vault.insert_item_fail");

    Sound BLOCK_VAULT_OPEN_SHUTTER = getSound("block.vault.open_shutter");

    Sound BLOCK_VAULT_PLACE = getSound("block.vault.place");

    Sound BLOCK_VAULT_REJECT_REWARDED_PLAYER = getSound("block.vault.reject_rewarded_player");

    Sound BLOCK_VAULT_STEP = getSound("block.vault.step");

    Sound BLOCK_VINE_BREAK = getSound("block.vine.break");

    Sound BLOCK_VINE_FALL = getSound("block.vine.fall");

    Sound BLOCK_VINE_HIT = getSound("block.vine.hit");

    Sound BLOCK_VINE_PLACE = getSound("block.vine.place");

    Sound BLOCK_VINE_STEP = getSound("block.vine.step");

    Sound BLOCK_WART_BLOCK_BREAK = getSound("block.wart_block.break");

    Sound BLOCK_WART_BLOCK_FALL = getSound("block.wart_block.fall");

    Sound BLOCK_WART_BLOCK_HIT = getSound("block.wart_block.hit");

    Sound BLOCK_WART_BLOCK_PLACE = getSound("block.wart_block.place");

    Sound BLOCK_WART_BLOCK_STEP = getSound("block.wart_block.step");

    Sound BLOCK_WATER_AMBIENT = getSound("block.water.ambient");

    Sound BLOCK_WEEPING_VINES_BREAK = getSound("block.weeping_vines.break");

    Sound BLOCK_WEEPING_VINES_FALL = getSound("block.weeping_vines.fall");

    Sound BLOCK_WEEPING_VINES_HIT = getSound("block.weeping_vines.hit");

    Sound BLOCK_WEEPING_VINES_PLACE = getSound("block.weeping_vines.place");

    Sound BLOCK_WEEPING_VINES_STEP = getSound("block.weeping_vines.step");

    Sound BLOCK_WET_GRASS_BREAK = getSound("block.wet_grass.break");

    Sound BLOCK_WET_GRASS_FALL = getSound("block.wet_grass.fall");

    Sound BLOCK_WET_GRASS_HIT = getSound("block.wet_grass.hit");

    Sound BLOCK_WET_GRASS_PLACE = getSound("block.wet_grass.place");

    Sound BLOCK_WET_GRASS_STEP = getSound("block.wet_grass.step");

    Sound BLOCK_WET_SPONGE_BREAK = getSound("block.wet_sponge.break");

    Sound BLOCK_WET_SPONGE_DRIES = getSound("block.wet_sponge.dries");

    Sound BLOCK_WET_SPONGE_FALL = getSound("block.wet_sponge.fall");

    Sound BLOCK_WET_SPONGE_HIT = getSound("block.wet_sponge.hit");

    Sound BLOCK_WET_SPONGE_PLACE = getSound("block.wet_sponge.place");

    Sound BLOCK_WET_SPONGE_STEP = getSound("block.wet_sponge.step");

    Sound BLOCK_WOOD_BREAK = getSound("block.wood.break");

    Sound BLOCK_WOOD_FALL = getSound("block.wood.fall");

    Sound BLOCK_WOOD_HIT = getSound("block.wood.hit");

    Sound BLOCK_WOOD_PLACE = getSound("block.wood.place");

    Sound BLOCK_WOOD_STEP = getSound("block.wood.step");

    Sound BLOCK_WOODEN_BUTTON_CLICK_OFF = getSound("block.wooden_button.click_off");

    Sound BLOCK_WOODEN_BUTTON_CLICK_ON = getSound("block.wooden_button.click_on");

    Sound BLOCK_WOODEN_DOOR_CLOSE = getSound("block.wooden_door.close");

    Sound BLOCK_WOODEN_DOOR_OPEN = getSound("block.wooden_door.open");

    Sound BLOCK_WOODEN_PRESSURE_PLATE_CLICK_OFF = getSound("block.wooden_pressure_plate.click_off");

    Sound BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON = getSound("block.wooden_pressure_plate.click_on");

    Sound BLOCK_WOODEN_TRAPDOOR_CLOSE = getSound("block.wooden_trapdoor.close");

    Sound BLOCK_WOODEN_TRAPDOOR_OPEN = getSound("block.wooden_trapdoor.open");

    Sound BLOCK_WOOL_BREAK = getSound("block.wool.break");

    Sound BLOCK_WOOL_FALL = getSound("block.wool.fall");

    Sound BLOCK_WOOL_HIT = getSound("block.wool.hit");

    Sound BLOCK_WOOL_PLACE = getSound("block.wool.place");

    Sound BLOCK_WOOL_STEP = getSound("block.wool.step");

    Sound ENCHANT_THORNS_HIT = getSound("enchant.thorns.hit");

    Sound ENTITY_ALLAY_AMBIENT_WITH_ITEM = getSound("entity.allay.ambient_with_item");

    Sound ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM = getSound("entity.allay.ambient_without_item");

    Sound ENTITY_ALLAY_DEATH = getSound("entity.allay.death");

    Sound ENTITY_ALLAY_HURT = getSound("entity.allay.hurt");

    Sound ENTITY_ALLAY_ITEM_GIVEN = getSound("entity.allay.item_given");

    Sound ENTITY_ALLAY_ITEM_TAKEN = getSound("entity.allay.item_taken");

    Sound ENTITY_ALLAY_ITEM_THROWN = getSound("entity.allay.item_thrown");

    Sound ENTITY_ARMADILLO_AMBIENT = getSound("entity.armadillo.ambient");

    Sound ENTITY_ARMADILLO_BRUSH = getSound("entity.armadillo.brush");

    Sound ENTITY_ARMADILLO_DEATH = getSound("entity.armadillo.death");

    Sound ENTITY_ARMADILLO_EAT = getSound("entity.armadillo.eat");

    Sound ENTITY_ARMADILLO_HURT = getSound("entity.armadillo.hurt");

    Sound ENTITY_ARMADILLO_HURT_REDUCED = getSound("entity.armadillo.hurt_reduced");

    Sound ENTITY_ARMADILLO_LAND = getSound("entity.armadillo.land");

    Sound ENTITY_ARMADILLO_PEEK = getSound("entity.armadillo.peek");

    Sound ENTITY_ARMADILLO_ROLL = getSound("entity.armadillo.roll");

    Sound ENTITY_ARMADILLO_SCUTE_DROP = getSound("entity.armadillo.scute_drop");

    Sound ENTITY_ARMADILLO_STEP = getSound("entity.armadillo.step");

    Sound ENTITY_ARMADILLO_UNROLL_FINISH = getSound("entity.armadillo.unroll_finish");

    Sound ENTITY_ARMADILLO_UNROLL_START = getSound("entity.armadillo.unroll_start");

    Sound ENTITY_ARMOR_STAND_BREAK = getSound("entity.armor_stand.break");

    Sound ENTITY_ARMOR_STAND_FALL = getSound("entity.armor_stand.fall");

    Sound ENTITY_ARMOR_STAND_HIT = getSound("entity.armor_stand.hit");

    Sound ENTITY_ARMOR_STAND_PLACE = getSound("entity.armor_stand.place");

    Sound ENTITY_ARROW_HIT = getSound("entity.arrow.hit");

    Sound ENTITY_ARROW_HIT_PLAYER = getSound("entity.arrow.hit_player");

    Sound ENTITY_ARROW_SHOOT = getSound("entity.arrow.shoot");

    Sound ENTITY_AXOLOTL_ATTACK = getSound("entity.axolotl.attack");

    Sound ENTITY_AXOLOTL_DEATH = getSound("entity.axolotl.death");

    Sound ENTITY_AXOLOTL_HURT = getSound("entity.axolotl.hurt");

    Sound ENTITY_AXOLOTL_IDLE_AIR = getSound("entity.axolotl.idle_air");

    Sound ENTITY_AXOLOTL_IDLE_WATER = getSound("entity.axolotl.idle_water");

    Sound ENTITY_AXOLOTL_SPLASH = getSound("entity.axolotl.splash");

    Sound ENTITY_AXOLOTL_SWIM = getSound("entity.axolotl.swim");

    Sound ENTITY_BAT_AMBIENT = getSound("entity.bat.ambient");

    Sound ENTITY_BAT_DEATH = getSound("entity.bat.death");

    Sound ENTITY_BAT_HURT = getSound("entity.bat.hurt");

    Sound ENTITY_BAT_LOOP = getSound("entity.bat.loop");

    Sound ENTITY_BAT_TAKEOFF = getSound("entity.bat.takeoff");

    Sound ENTITY_BEE_DEATH = getSound("entity.bee.death");

    Sound ENTITY_BEE_HURT = getSound("entity.bee.hurt");

    Sound ENTITY_BEE_LOOP = getSound("entity.bee.loop");

    Sound ENTITY_BEE_LOOP_AGGRESSIVE = getSound("entity.bee.loop_aggressive");

    Sound ENTITY_BEE_POLLINATE = getSound("entity.bee.pollinate");

    Sound ENTITY_BEE_STING = getSound("entity.bee.sting");

    Sound ENTITY_BLAZE_AMBIENT = getSound("entity.blaze.ambient");

    Sound ENTITY_BLAZE_BURN = getSound("entity.blaze.burn");

    Sound ENTITY_BLAZE_DEATH = getSound("entity.blaze.death");

    Sound ENTITY_BLAZE_HURT = getSound("entity.blaze.hurt");

    Sound ENTITY_BLAZE_SHOOT = getSound("entity.blaze.shoot");

    Sound ENTITY_BOAT_PADDLE_LAND = getSound("entity.boat.paddle_land");

    Sound ENTITY_BOAT_PADDLE_WATER = getSound("entity.boat.paddle_water");

    Sound ENTITY_BOGGED_AMBIENT = getSound("entity.bogged.ambient");

    Sound ENTITY_BOGGED_DEATH = getSound("entity.bogged.death");

    Sound ENTITY_BOGGED_HURT = getSound("entity.bogged.hurt");

    Sound ENTITY_BOGGED_SHEAR = getSound("entity.bogged.shear");

    Sound ENTITY_BOGGED_STEP = getSound("entity.bogged.step");

    Sound ENTITY_BREEZE_CHARGE = getSound("entity.breeze.charge");

    Sound ENTITY_BREEZE_DEATH = getSound("entity.breeze.death");

    Sound ENTITY_BREEZE_DEFLECT = getSound("entity.breeze.deflect");

    Sound ENTITY_BREEZE_HURT = getSound("entity.breeze.hurt");

    Sound ENTITY_BREEZE_IDLE_AIR = getSound("entity.breeze.idle_air");

    Sound ENTITY_BREEZE_IDLE_GROUND = getSound("entity.breeze.idle_ground");

    Sound ENTITY_BREEZE_INHALE = getSound("entity.breeze.inhale");

    Sound ENTITY_BREEZE_JUMP = getSound("entity.breeze.jump");

    Sound ENTITY_BREEZE_LAND = getSound("entity.breeze.land");

    Sound ENTITY_BREEZE_SHOOT = getSound("entity.breeze.shoot");

    Sound ENTITY_BREEZE_SLIDE = getSound("entity.breeze.slide");

    Sound ENTITY_BREEZE_WHIRL = getSound("entity.breeze.whirl");

    Sound ENTITY_BREEZE_WIND_BURST = getSound("entity.breeze.wind_burst");

    Sound ENTITY_CAMEL_AMBIENT = getSound("entity.camel.ambient");

    Sound ENTITY_CAMEL_DASH = getSound("entity.camel.dash");

    Sound ENTITY_CAMEL_DASH_READY = getSound("entity.camel.dash_ready");

    Sound ENTITY_CAMEL_DEATH = getSound("entity.camel.death");

    Sound ENTITY_CAMEL_EAT = getSound("entity.camel.eat");

    Sound ENTITY_CAMEL_HURT = getSound("entity.camel.hurt");

    Sound ENTITY_CAMEL_SADDLE = getSound("entity.camel.saddle");

    Sound ENTITY_CAMEL_SIT = getSound("entity.camel.sit");

    Sound ENTITY_CAMEL_STAND = getSound("entity.camel.stand");

    Sound ENTITY_CAMEL_STEP = getSound("entity.camel.step");

    Sound ENTITY_CAMEL_STEP_SAND = getSound("entity.camel.step_sand");

    Sound ENTITY_CAT_AMBIENT = getSound("entity.cat.ambient");

    Sound ENTITY_CAT_BEG_FOR_FOOD = getSound("entity.cat.beg_for_food");

    Sound ENTITY_CAT_DEATH = getSound("entity.cat.death");

    Sound ENTITY_CAT_EAT = getSound("entity.cat.eat");

    Sound ENTITY_CAT_HISS = getSound("entity.cat.hiss");

    Sound ENTITY_CAT_HURT = getSound("entity.cat.hurt");

    Sound ENTITY_CAT_PURR = getSound("entity.cat.purr");

    Sound ENTITY_CAT_PURREOW = getSound("entity.cat.purreow");

    Sound ENTITY_CAT_STRAY_AMBIENT = getSound("entity.cat.stray_ambient");

    Sound ENTITY_CHICKEN_AMBIENT = getSound("entity.chicken.ambient");

    Sound ENTITY_CHICKEN_DEATH = getSound("entity.chicken.death");

    Sound ENTITY_CHICKEN_EGG = getSound("entity.chicken.egg");

    Sound ENTITY_CHICKEN_HURT = getSound("entity.chicken.hurt");

    Sound ENTITY_CHICKEN_STEP = getSound("entity.chicken.step");

    Sound ENTITY_COD_AMBIENT = getSound("entity.cod.ambient");

    Sound ENTITY_COD_DEATH = getSound("entity.cod.death");

    Sound ENTITY_COD_FLOP = getSound("entity.cod.flop");

    Sound ENTITY_COD_HURT = getSound("entity.cod.hurt");

    Sound ENTITY_COW_AMBIENT = getSound("entity.cow.ambient");

    Sound ENTITY_COW_DEATH = getSound("entity.cow.death");

    Sound ENTITY_COW_HURT = getSound("entity.cow.hurt");

    Sound ENTITY_COW_MILK = getSound("entity.cow.milk");

    Sound ENTITY_COW_STEP = getSound("entity.cow.step");

    Sound ENTITY_CREAKING_ACTIVATE = getSound("entity.creaking.activate");

    Sound ENTITY_CREAKING_AMBIENT = getSound("entity.creaking.ambient");

    Sound ENTITY_CREAKING_ATTACK = getSound("entity.creaking.attack");

    Sound ENTITY_CREAKING_DEACTIVATE = getSound("entity.creaking.deactivate");

    Sound ENTITY_CREAKING_DEATH = getSound("entity.creaking.death");

    Sound ENTITY_CREAKING_FREEZE = getSound("entity.creaking.freeze");

    Sound ENTITY_CREAKING_SPAWN = getSound("entity.creaking.spawn");

    Sound ENTITY_CREAKING_STEP = getSound("entity.creaking.step");

    Sound ENTITY_CREAKING_SWAY = getSound("entity.creaking.sway");

    Sound ENTITY_CREAKING_TWITCH = getSound("entity.creaking.twitch");

    Sound ENTITY_CREAKING_UNFREEZE = getSound("entity.creaking.unfreeze");

    Sound ENTITY_CREEPER_DEATH = getSound("entity.creeper.death");

    Sound ENTITY_CREEPER_HURT = getSound("entity.creeper.hurt");

    Sound ENTITY_CREEPER_PRIMED = getSound("entity.creeper.primed");

    Sound ENTITY_DOLPHIN_AMBIENT = getSound("entity.dolphin.ambient");

    Sound ENTITY_DOLPHIN_AMBIENT_WATER = getSound("entity.dolphin.ambient_water");

    Sound ENTITY_DOLPHIN_ATTACK = getSound("entity.dolphin.attack");

    Sound ENTITY_DOLPHIN_DEATH = getSound("entity.dolphin.death");

    Sound ENTITY_DOLPHIN_EAT = getSound("entity.dolphin.eat");

    Sound ENTITY_DOLPHIN_HURT = getSound("entity.dolphin.hurt");

    Sound ENTITY_DOLPHIN_JUMP = getSound("entity.dolphin.jump");

    Sound ENTITY_DOLPHIN_PLAY = getSound("entity.dolphin.play");

    Sound ENTITY_DOLPHIN_SPLASH = getSound("entity.dolphin.splash");

    Sound ENTITY_DOLPHIN_SWIM = getSound("entity.dolphin.swim");

    Sound ENTITY_DONKEY_AMBIENT = getSound("entity.donkey.ambient");

    Sound ENTITY_DONKEY_ANGRY = getSound("entity.donkey.angry");

    Sound ENTITY_DONKEY_CHEST = getSound("entity.donkey.chest");

    Sound ENTITY_DONKEY_DEATH = getSound("entity.donkey.death");

    Sound ENTITY_DONKEY_EAT = getSound("entity.donkey.eat");

    Sound ENTITY_DONKEY_HURT = getSound("entity.donkey.hurt");

    Sound ENTITY_DONKEY_JUMP = getSound("entity.donkey.jump");

    Sound ENTITY_DRAGON_FIREBALL_EXPLODE = getSound("entity.dragon_fireball.explode");

    Sound ENTITY_DROWNED_AMBIENT = getSound("entity.drowned.ambient");

    Sound ENTITY_DROWNED_AMBIENT_WATER = getSound("entity.drowned.ambient_water");

    Sound ENTITY_DROWNED_DEATH = getSound("entity.drowned.death");

    Sound ENTITY_DROWNED_DEATH_WATER = getSound("entity.drowned.death_water");

    Sound ENTITY_DROWNED_HURT = getSound("entity.drowned.hurt");

    Sound ENTITY_DROWNED_HURT_WATER = getSound("entity.drowned.hurt_water");

    Sound ENTITY_DROWNED_SHOOT = getSound("entity.drowned.shoot");

    Sound ENTITY_DROWNED_STEP = getSound("entity.drowned.step");

    Sound ENTITY_DROWNED_SWIM = getSound("entity.drowned.swim");

    Sound ENTITY_EGG_THROW = getSound("entity.egg.throw");

    Sound ENTITY_ELDER_GUARDIAN_AMBIENT = getSound("entity.elder_guardian.ambient");

    Sound ENTITY_ELDER_GUARDIAN_AMBIENT_LAND = getSound("entity.elder_guardian.ambient_land");

    Sound ENTITY_ELDER_GUARDIAN_CURSE = getSound("entity.elder_guardian.curse");

    Sound ENTITY_ELDER_GUARDIAN_DEATH = getSound("entity.elder_guardian.death");

    Sound ENTITY_ELDER_GUARDIAN_DEATH_LAND = getSound("entity.elder_guardian.death_land");

    Sound ENTITY_ELDER_GUARDIAN_FLOP = getSound("entity.elder_guardian.flop");

    Sound ENTITY_ELDER_GUARDIAN_HURT = getSound("entity.elder_guardian.hurt");

    Sound ENTITY_ELDER_GUARDIAN_HURT_LAND = getSound("entity.elder_guardian.hurt_land");

    Sound ENTITY_ENDER_DRAGON_AMBIENT = getSound("entity.ender_dragon.ambient");

    Sound ENTITY_ENDER_DRAGON_DEATH = getSound("entity.ender_dragon.death");

    Sound ENTITY_ENDER_DRAGON_FLAP = getSound("entity.ender_dragon.flap");

    Sound ENTITY_ENDER_DRAGON_GROWL = getSound("entity.ender_dragon.growl");

    Sound ENTITY_ENDER_DRAGON_HURT = getSound("entity.ender_dragon.hurt");

    Sound ENTITY_ENDER_DRAGON_SHOOT = getSound("entity.ender_dragon.shoot");

    Sound ENTITY_ENDER_EYE_DEATH = getSound("entity.ender_eye.death");

    Sound ENTITY_ENDER_EYE_LAUNCH = getSound("entity.ender_eye.launch");

    Sound ENTITY_ENDER_PEARL_THROW = getSound("entity.ender_pearl.throw");

    Sound ENTITY_ENDERMAN_AMBIENT = getSound("entity.enderman.ambient");

    Sound ENTITY_ENDERMAN_DEATH = getSound("entity.enderman.death");

    Sound ENTITY_ENDERMAN_HURT = getSound("entity.enderman.hurt");

    Sound ENTITY_ENDERMAN_SCREAM = getSound("entity.enderman.scream");

    Sound ENTITY_ENDERMAN_STARE = getSound("entity.enderman.stare");

    Sound ENTITY_ENDERMAN_TELEPORT = getSound("entity.enderman.teleport");

    Sound ENTITY_ENDERMITE_AMBIENT = getSound("entity.endermite.ambient");

    Sound ENTITY_ENDERMITE_DEATH = getSound("entity.endermite.death");

    Sound ENTITY_ENDERMITE_HURT = getSound("entity.endermite.hurt");

    Sound ENTITY_ENDERMITE_STEP = getSound("entity.endermite.step");

    Sound ENTITY_EVOKER_AMBIENT = getSound("entity.evoker.ambient");

    Sound ENTITY_EVOKER_CAST_SPELL = getSound("entity.evoker.cast_spell");

    Sound ENTITY_EVOKER_CELEBRATE = getSound("entity.evoker.celebrate");

    Sound ENTITY_EVOKER_DEATH = getSound("entity.evoker.death");

    Sound ENTITY_EVOKER_HURT = getSound("entity.evoker.hurt");

    Sound ENTITY_EVOKER_PREPARE_ATTACK = getSound("entity.evoker.prepare_attack");

    Sound ENTITY_EVOKER_PREPARE_SUMMON = getSound("entity.evoker.prepare_summon");

    Sound ENTITY_EVOKER_PREPARE_WOLOLO = getSound("entity.evoker.prepare_wololo");

    Sound ENTITY_EVOKER_FANGS_ATTACK = getSound("entity.evoker_fangs.attack");

    Sound ENTITY_EXPERIENCE_BOTTLE_THROW = getSound("entity.experience_bottle.throw");

    Sound ENTITY_EXPERIENCE_ORB_PICKUP = getSound("entity.experience_orb.pickup");

    Sound ENTITY_FIREWORK_ROCKET_BLAST = getSound("entity.firework_rocket.blast");

    Sound ENTITY_FIREWORK_ROCKET_BLAST_FAR = getSound("entity.firework_rocket.blast_far");

    Sound ENTITY_FIREWORK_ROCKET_LARGE_BLAST = getSound("entity.firework_rocket.large_blast");

    Sound ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR = getSound("entity.firework_rocket.large_blast_far");

    Sound ENTITY_FIREWORK_ROCKET_LAUNCH = getSound("entity.firework_rocket.launch");

    Sound ENTITY_FIREWORK_ROCKET_SHOOT = getSound("entity.firework_rocket.shoot");

    Sound ENTITY_FIREWORK_ROCKET_TWINKLE = getSound("entity.firework_rocket.twinkle");

    Sound ENTITY_FIREWORK_ROCKET_TWINKLE_FAR = getSound("entity.firework_rocket.twinkle_far");

    Sound ENTITY_FISH_SWIM = getSound("entity.fish.swim");

    Sound ENTITY_FISHING_BOBBER_RETRIEVE = getSound("entity.fishing_bobber.retrieve");

    Sound ENTITY_FISHING_BOBBER_SPLASH = getSound("entity.fishing_bobber.splash");

    Sound ENTITY_FISHING_BOBBER_THROW = getSound("entity.fishing_bobber.throw");

    Sound ENTITY_FOX_AGGRO = getSound("entity.fox.aggro");

    Sound ENTITY_FOX_AMBIENT = getSound("entity.fox.ambient");

    Sound ENTITY_FOX_BITE = getSound("entity.fox.bite");

    Sound ENTITY_FOX_DEATH = getSound("entity.fox.death");

    Sound ENTITY_FOX_EAT = getSound("entity.fox.eat");

    Sound ENTITY_FOX_HURT = getSound("entity.fox.hurt");

    Sound ENTITY_FOX_SCREECH = getSound("entity.fox.screech");

    Sound ENTITY_FOX_SLEEP = getSound("entity.fox.sleep");

    Sound ENTITY_FOX_SNIFF = getSound("entity.fox.sniff");

    Sound ENTITY_FOX_SPIT = getSound("entity.fox.spit");

    Sound ENTITY_FOX_TELEPORT = getSound("entity.fox.teleport");

    Sound ENTITY_FROG_AMBIENT = getSound("entity.frog.ambient");

    Sound ENTITY_FROG_DEATH = getSound("entity.frog.death");

    Sound ENTITY_FROG_EAT = getSound("entity.frog.eat");

    Sound ENTITY_FROG_HURT = getSound("entity.frog.hurt");

    Sound ENTITY_FROG_LAY_SPAWN = getSound("entity.frog.lay_spawn");

    Sound ENTITY_FROG_LONG_JUMP = getSound("entity.frog.long_jump");

    Sound ENTITY_FROG_STEP = getSound("entity.frog.step");

    Sound ENTITY_FROG_TONGUE = getSound("entity.frog.tongue");

    Sound ENTITY_GENERIC_BIG_FALL = getSound("entity.generic.big_fall");

    Sound ENTITY_GENERIC_BURN = getSound("entity.generic.burn");

    Sound ENTITY_GENERIC_DEATH = getSound("entity.generic.death");

    Sound ENTITY_GENERIC_DRINK = getSound("entity.generic.drink");

    Sound ENTITY_GENERIC_EAT = getSound("entity.generic.eat");

    Sound ENTITY_GENERIC_EXPLODE = getSound("entity.generic.explode");

    Sound ENTITY_GENERIC_EXTINGUISH_FIRE = getSound("entity.generic.extinguish_fire");

    Sound ENTITY_GENERIC_HURT = getSound("entity.generic.hurt");

    Sound ENTITY_GENERIC_SMALL_FALL = getSound("entity.generic.small_fall");

    Sound ENTITY_GENERIC_SPLASH = getSound("entity.generic.splash");

    Sound ENTITY_GENERIC_SWIM = getSound("entity.generic.swim");

    Sound ENTITY_GHAST_AMBIENT = getSound("entity.ghast.ambient");

    Sound ENTITY_GHAST_DEATH = getSound("entity.ghast.death");

    Sound ENTITY_GHAST_HURT = getSound("entity.ghast.hurt");

    Sound ENTITY_GHAST_SCREAM = getSound("entity.ghast.scream");

    Sound ENTITY_GHAST_SHOOT = getSound("entity.ghast.shoot");

    Sound ENTITY_GHAST_WARN = getSound("entity.ghast.warn");

    Sound ENTITY_GHASTLING_AMBIENT = getSound("entity.ghastling.ambient");

    Sound ENTITY_GHASTLING_DEATH = getSound("entity.ghastling.death");

    Sound ENTITY_GHASTLING_HURT = getSound("entity.ghastling.hurt");

    Sound ENTITY_GHASTLING_SPAWN = getSound("entity.ghastling.spawn");

    Sound ENTITY_GLOW_ITEM_FRAME_ADD_ITEM = getSound("entity.glow_item_frame.add_item");

    Sound ENTITY_GLOW_ITEM_FRAME_BREAK = getSound("entity.glow_item_frame.break");

    Sound ENTITY_GLOW_ITEM_FRAME_PLACE = getSound("entity.glow_item_frame.place");

    Sound ENTITY_GLOW_ITEM_FRAME_REMOVE_ITEM = getSound("entity.glow_item_frame.remove_item");

    Sound ENTITY_GLOW_ITEM_FRAME_ROTATE_ITEM = getSound("entity.glow_item_frame.rotate_item");

    Sound ENTITY_GLOW_SQUID_AMBIENT = getSound("entity.glow_squid.ambient");

    Sound ENTITY_GLOW_SQUID_DEATH = getSound("entity.glow_squid.death");

    Sound ENTITY_GLOW_SQUID_HURT = getSound("entity.glow_squid.hurt");

    Sound ENTITY_GLOW_SQUID_SQUIRT = getSound("entity.glow_squid.squirt");

    Sound ENTITY_GOAT_AMBIENT = getSound("entity.goat.ambient");

    Sound ENTITY_GOAT_DEATH = getSound("entity.goat.death");

    Sound ENTITY_GOAT_EAT = getSound("entity.goat.eat");

    Sound ENTITY_GOAT_HORN_BREAK = getSound("entity.goat.horn_break");

    Sound ENTITY_GOAT_HURT = getSound("entity.goat.hurt");

    Sound ENTITY_GOAT_LONG_JUMP = getSound("entity.goat.long_jump");

    Sound ENTITY_GOAT_MILK = getSound("entity.goat.milk");

    Sound ENTITY_GOAT_PREPARE_RAM = getSound("entity.goat.prepare_ram");

    Sound ENTITY_GOAT_RAM_IMPACT = getSound("entity.goat.ram_impact");

    Sound ENTITY_GOAT_SCREAMING_AMBIENT = getSound("entity.goat.screaming.ambient");

    Sound ENTITY_GOAT_SCREAMING_DEATH = getSound("entity.goat.screaming.death");

    Sound ENTITY_GOAT_SCREAMING_EAT = getSound("entity.goat.screaming.eat");

    Sound ENTITY_GOAT_SCREAMING_HURT = getSound("entity.goat.screaming.hurt");

    Sound ENTITY_GOAT_SCREAMING_LONG_JUMP = getSound("entity.goat.screaming.long_jump");

    Sound ENTITY_GOAT_SCREAMING_MILK = getSound("entity.goat.screaming.milk");

    Sound ENTITY_GOAT_SCREAMING_PREPARE_RAM = getSound("entity.goat.screaming.prepare_ram");

    Sound ENTITY_GOAT_SCREAMING_RAM_IMPACT = getSound("entity.goat.screaming.ram_impact");

    Sound ENTITY_GOAT_STEP = getSound("entity.goat.step");

    Sound ENTITY_GUARDIAN_AMBIENT = getSound("entity.guardian.ambient");

    Sound ENTITY_GUARDIAN_AMBIENT_LAND = getSound("entity.guardian.ambient_land");

    Sound ENTITY_GUARDIAN_ATTACK = getSound("entity.guardian.attack");

    Sound ENTITY_GUARDIAN_DEATH = getSound("entity.guardian.death");

    Sound ENTITY_GUARDIAN_DEATH_LAND = getSound("entity.guardian.death_land");

    Sound ENTITY_GUARDIAN_FLOP = getSound("entity.guardian.flop");

    Sound ENTITY_GUARDIAN_HURT = getSound("entity.guardian.hurt");

    Sound ENTITY_GUARDIAN_HURT_LAND = getSound("entity.guardian.hurt_land");

    Sound ENTITY_HAPPY_GHAST_AMBIENT = getSound("entity.happy_ghast.ambient");

    Sound ENTITY_HAPPY_GHAST_DEATH = getSound("entity.happy_ghast.death");

    Sound ENTITY_HAPPY_GHAST_EQUIP = getSound("entity.happy_ghast.equip");

    Sound ENTITY_HAPPY_GHAST_HARNESS_GOGGLES_DOWN = getSound("entity.happy_ghast.harness_goggles_down");

    Sound ENTITY_HAPPY_GHAST_HARNESS_GOGGLES_UP = getSound("entity.happy_ghast.harness_goggles_up");

    Sound ENTITY_HAPPY_GHAST_HURT = getSound("entity.happy_ghast.hurt");

    Sound ENTITY_HAPPY_GHAST_RIDING = getSound("entity.happy_ghast.riding");

    Sound ENTITY_HAPPY_GHAST_UNEQUIP = getSound("entity.happy_ghast.unequip");

    Sound ENTITY_HOGLIN_AMBIENT = getSound("entity.hoglin.ambient");

    Sound ENTITY_HOGLIN_ANGRY = getSound("entity.hoglin.angry");

    Sound ENTITY_HOGLIN_ATTACK = getSound("entity.hoglin.attack");

    Sound ENTITY_HOGLIN_CONVERTED_TO_ZOMBIFIED = getSound("entity.hoglin.converted_to_zombified");

    Sound ENTITY_HOGLIN_DEATH = getSound("entity.hoglin.death");

    Sound ENTITY_HOGLIN_HURT = getSound("entity.hoglin.hurt");

    Sound ENTITY_HOGLIN_RETREAT = getSound("entity.hoglin.retreat");

    Sound ENTITY_HOGLIN_STEP = getSound("entity.hoglin.step");

    Sound ENTITY_HORSE_AMBIENT = getSound("entity.horse.ambient");

    Sound ENTITY_HORSE_ANGRY = getSound("entity.horse.angry");

    Sound ENTITY_HORSE_ARMOR = getSound("entity.horse.armor");

    Sound ENTITY_HORSE_BREATHE = getSound("entity.horse.breathe");

    Sound ENTITY_HORSE_DEATH = getSound("entity.horse.death");

    Sound ENTITY_HORSE_EAT = getSound("entity.horse.eat");

    Sound ENTITY_HORSE_GALLOP = getSound("entity.horse.gallop");

    Sound ENTITY_HORSE_HURT = getSound("entity.horse.hurt");

    Sound ENTITY_HORSE_JUMP = getSound("entity.horse.jump");

    Sound ENTITY_HORSE_LAND = getSound("entity.horse.land");

    Sound ENTITY_HORSE_SADDLE = getSound("entity.horse.saddle");

    Sound ENTITY_HORSE_STEP = getSound("entity.horse.step");

    Sound ENTITY_HORSE_STEP_WOOD = getSound("entity.horse.step_wood");

    Sound ENTITY_HOSTILE_BIG_FALL = getSound("entity.hostile.big_fall");

    Sound ENTITY_HOSTILE_DEATH = getSound("entity.hostile.death");

    Sound ENTITY_HOSTILE_HURT = getSound("entity.hostile.hurt");

    Sound ENTITY_HOSTILE_SMALL_FALL = getSound("entity.hostile.small_fall");

    Sound ENTITY_HOSTILE_SPLASH = getSound("entity.hostile.splash");

    Sound ENTITY_HOSTILE_SWIM = getSound("entity.hostile.swim");

    Sound ENTITY_HUSK_AMBIENT = getSound("entity.husk.ambient");

    Sound ENTITY_HUSK_CONVERTED_TO_ZOMBIE = getSound("entity.husk.converted_to_zombie");

    Sound ENTITY_HUSK_DEATH = getSound("entity.husk.death");

    Sound ENTITY_HUSK_HURT = getSound("entity.husk.hurt");

    Sound ENTITY_HUSK_STEP = getSound("entity.husk.step");

    Sound ENTITY_ILLUSIONER_AMBIENT = getSound("entity.illusioner.ambient");

    Sound ENTITY_ILLUSIONER_CAST_SPELL = getSound("entity.illusioner.cast_spell");

    Sound ENTITY_ILLUSIONER_DEATH = getSound("entity.illusioner.death");

    Sound ENTITY_ILLUSIONER_HURT = getSound("entity.illusioner.hurt");

    Sound ENTITY_ILLUSIONER_MIRROR_MOVE = getSound("entity.illusioner.mirror_move");

    Sound ENTITY_ILLUSIONER_PREPARE_BLINDNESS = getSound("entity.illusioner.prepare_blindness");

    Sound ENTITY_ILLUSIONER_PREPARE_MIRROR = getSound("entity.illusioner.prepare_mirror");

    Sound ENTITY_IRON_GOLEM_ATTACK = getSound("entity.iron_golem.attack");

    Sound ENTITY_IRON_GOLEM_DAMAGE = getSound("entity.iron_golem.damage");

    Sound ENTITY_IRON_GOLEM_DEATH = getSound("entity.iron_golem.death");

    Sound ENTITY_IRON_GOLEM_HURT = getSound("entity.iron_golem.hurt");

    Sound ENTITY_IRON_GOLEM_REPAIR = getSound("entity.iron_golem.repair");

    Sound ENTITY_IRON_GOLEM_STEP = getSound("entity.iron_golem.step");

    Sound ENTITY_ITEM_BREAK = getSound("entity.item.break");

    Sound ENTITY_ITEM_PICKUP = getSound("entity.item.pickup");

    Sound ENTITY_ITEM_FRAME_ADD_ITEM = getSound("entity.item_frame.add_item");

    Sound ENTITY_ITEM_FRAME_BREAK = getSound("entity.item_frame.break");

    Sound ENTITY_ITEM_FRAME_PLACE = getSound("entity.item_frame.place");

    Sound ENTITY_ITEM_FRAME_REMOVE_ITEM = getSound("entity.item_frame.remove_item");

    Sound ENTITY_ITEM_FRAME_ROTATE_ITEM = getSound("entity.item_frame.rotate_item");

    Sound ENTITY_LIGHTNING_BOLT_IMPACT = getSound("entity.lightning_bolt.impact");

    Sound ENTITY_LIGHTNING_BOLT_THUNDER = getSound("entity.lightning_bolt.thunder");

    Sound ENTITY_LINGERING_POTION_THROW = getSound("entity.lingering_potion.throw");

    Sound ENTITY_LLAMA_AMBIENT = getSound("entity.llama.ambient");

    Sound ENTITY_LLAMA_ANGRY = getSound("entity.llama.angry");

    Sound ENTITY_LLAMA_CHEST = getSound("entity.llama.chest");

    Sound ENTITY_LLAMA_DEATH = getSound("entity.llama.death");

    Sound ENTITY_LLAMA_EAT = getSound("entity.llama.eat");

    Sound ENTITY_LLAMA_HURT = getSound("entity.llama.hurt");

    Sound ENTITY_LLAMA_SPIT = getSound("entity.llama.spit");

    Sound ENTITY_LLAMA_STEP = getSound("entity.llama.step");

    Sound ENTITY_LLAMA_SWAG = getSound("entity.llama.swag");

    Sound ENTITY_MAGMA_CUBE_DEATH = getSound("entity.magma_cube.death");

    Sound ENTITY_MAGMA_CUBE_DEATH_SMALL = getSound("entity.magma_cube.death_small");

    Sound ENTITY_MAGMA_CUBE_HURT = getSound("entity.magma_cube.hurt");

    Sound ENTITY_MAGMA_CUBE_HURT_SMALL = getSound("entity.magma_cube.hurt_small");

    Sound ENTITY_MAGMA_CUBE_JUMP = getSound("entity.magma_cube.jump");

    Sound ENTITY_MAGMA_CUBE_SQUISH = getSound("entity.magma_cube.squish");

    Sound ENTITY_MAGMA_CUBE_SQUISH_SMALL = getSound("entity.magma_cube.squish_small");

    Sound ENTITY_MINECART_INSIDE = getSound("entity.minecart.inside");

    Sound ENTITY_MINECART_INSIDE_UNDERWATER = getSound("entity.minecart.inside.underwater");

    Sound ENTITY_MINECART_RIDING = getSound("entity.minecart.riding");

    Sound ENTITY_MOOSHROOM_CONVERT = getSound("entity.mooshroom.convert");

    Sound ENTITY_MOOSHROOM_EAT = getSound("entity.mooshroom.eat");

    Sound ENTITY_MOOSHROOM_MILK = getSound("entity.mooshroom.milk");

    Sound ENTITY_MOOSHROOM_SHEAR = getSound("entity.mooshroom.shear");

    Sound ENTITY_MOOSHROOM_SUSPICIOUS_MILK = getSound("entity.mooshroom.suspicious_milk");

    Sound ENTITY_MULE_AMBIENT = getSound("entity.mule.ambient");

    Sound ENTITY_MULE_ANGRY = getSound("entity.mule.angry");

    Sound ENTITY_MULE_CHEST = getSound("entity.mule.chest");

    Sound ENTITY_MULE_DEATH = getSound("entity.mule.death");

    Sound ENTITY_MULE_EAT = getSound("entity.mule.eat");

    Sound ENTITY_MULE_HURT = getSound("entity.mule.hurt");

    Sound ENTITY_MULE_JUMP = getSound("entity.mule.jump");

    Sound ENTITY_OCELOT_AMBIENT = getSound("entity.ocelot.ambient");

    Sound ENTITY_OCELOT_DEATH = getSound("entity.ocelot.death");

    Sound ENTITY_OCELOT_HURT = getSound("entity.ocelot.hurt");

    Sound ENTITY_PAINTING_BREAK = getSound("entity.painting.break");

    Sound ENTITY_PAINTING_PLACE = getSound("entity.painting.place");

    Sound ENTITY_PANDA_AGGRESSIVE_AMBIENT = getSound("entity.panda.aggressive_ambient");

    Sound ENTITY_PANDA_AMBIENT = getSound("entity.panda.ambient");

    Sound ENTITY_PANDA_BITE = getSound("entity.panda.bite");

    Sound ENTITY_PANDA_CANT_BREED = getSound("entity.panda.cant_breed");

    Sound ENTITY_PANDA_DEATH = getSound("entity.panda.death");

    Sound ENTITY_PANDA_EAT = getSound("entity.panda.eat");

    Sound ENTITY_PANDA_HURT = getSound("entity.panda.hurt");

    Sound ENTITY_PANDA_PRE_SNEEZE = getSound("entity.panda.pre_sneeze");

    Sound ENTITY_PANDA_SNEEZE = getSound("entity.panda.sneeze");

    Sound ENTITY_PANDA_STEP = getSound("entity.panda.step");

    Sound ENTITY_PANDA_WORRIED_AMBIENT = getSound("entity.panda.worried_ambient");

    Sound ENTITY_PARROT_AMBIENT = getSound("entity.parrot.ambient");

    Sound ENTITY_PARROT_DEATH = getSound("entity.parrot.death");

    Sound ENTITY_PARROT_EAT = getSound("entity.parrot.eat");

    Sound ENTITY_PARROT_FLY = getSound("entity.parrot.fly");

    Sound ENTITY_PARROT_HURT = getSound("entity.parrot.hurt");

    Sound ENTITY_PARROT_IMITATE_BLAZE = getSound("entity.parrot.imitate.blaze");

    Sound ENTITY_PARROT_IMITATE_BOGGED = getSound("entity.parrot.imitate.bogged");

    Sound ENTITY_PARROT_IMITATE_BREEZE = getSound("entity.parrot.imitate.breeze");

    Sound ENTITY_PARROT_IMITATE_CREAKING = getSound("entity.parrot.imitate.creaking");

    Sound ENTITY_PARROT_IMITATE_CREEPER = getSound("entity.parrot.imitate.creeper");

    Sound ENTITY_PARROT_IMITATE_DROWNED = getSound("entity.parrot.imitate.drowned");

    Sound ENTITY_PARROT_IMITATE_ELDER_GUARDIAN = getSound("entity.parrot.imitate.elder_guardian");

    Sound ENTITY_PARROT_IMITATE_ENDER_DRAGON = getSound("entity.parrot.imitate.ender_dragon");

    Sound ENTITY_PARROT_IMITATE_ENDERMITE = getSound("entity.parrot.imitate.endermite");

    Sound ENTITY_PARROT_IMITATE_EVOKER = getSound("entity.parrot.imitate.evoker");

    Sound ENTITY_PARROT_IMITATE_GHAST = getSound("entity.parrot.imitate.ghast");

    Sound ENTITY_PARROT_IMITATE_GUARDIAN = getSound("entity.parrot.imitate.guardian");

    Sound ENTITY_PARROT_IMITATE_HOGLIN = getSound("entity.parrot.imitate.hoglin");

    Sound ENTITY_PARROT_IMITATE_HUSK = getSound("entity.parrot.imitate.husk");

    Sound ENTITY_PARROT_IMITATE_ILLUSIONER = getSound("entity.parrot.imitate.illusioner");

    Sound ENTITY_PARROT_IMITATE_MAGMA_CUBE = getSound("entity.parrot.imitate.magma_cube");

    Sound ENTITY_PARROT_IMITATE_PHANTOM = getSound("entity.parrot.imitate.phantom");

    Sound ENTITY_PARROT_IMITATE_PIGLIN = getSound("entity.parrot.imitate.piglin");

    Sound ENTITY_PARROT_IMITATE_PIGLIN_BRUTE = getSound("entity.parrot.imitate.piglin_brute");

    Sound ENTITY_PARROT_IMITATE_PILLAGER = getSound("entity.parrot.imitate.pillager");

    Sound ENTITY_PARROT_IMITATE_RAVAGER = getSound("entity.parrot.imitate.ravager");

    Sound ENTITY_PARROT_IMITATE_SHULKER = getSound("entity.parrot.imitate.shulker");

    Sound ENTITY_PARROT_IMITATE_SILVERFISH = getSound("entity.parrot.imitate.silverfish");

    Sound ENTITY_PARROT_IMITATE_SKELETON = getSound("entity.parrot.imitate.skeleton");

    Sound ENTITY_PARROT_IMITATE_SLIME = getSound("entity.parrot.imitate.slime");

    Sound ENTITY_PARROT_IMITATE_SPIDER = getSound("entity.parrot.imitate.spider");

    Sound ENTITY_PARROT_IMITATE_STRAY = getSound("entity.parrot.imitate.stray");

    Sound ENTITY_PARROT_IMITATE_VEX = getSound("entity.parrot.imitate.vex");

    Sound ENTITY_PARROT_IMITATE_VINDICATOR = getSound("entity.parrot.imitate.vindicator");

    Sound ENTITY_PARROT_IMITATE_WARDEN = getSound("entity.parrot.imitate.warden");

    Sound ENTITY_PARROT_IMITATE_WITCH = getSound("entity.parrot.imitate.witch");

    Sound ENTITY_PARROT_IMITATE_WITHER = getSound("entity.parrot.imitate.wither");

    Sound ENTITY_PARROT_IMITATE_WITHER_SKELETON = getSound("entity.parrot.imitate.wither_skeleton");

    Sound ENTITY_PARROT_IMITATE_ZOGLIN = getSound("entity.parrot.imitate.zoglin");

    Sound ENTITY_PARROT_IMITATE_ZOMBIE = getSound("entity.parrot.imitate.zombie");

    Sound ENTITY_PARROT_IMITATE_ZOMBIE_VILLAGER = getSound("entity.parrot.imitate.zombie_villager");

    Sound ENTITY_PARROT_STEP = getSound("entity.parrot.step");

    Sound ENTITY_PHANTOM_AMBIENT = getSound("entity.phantom.ambient");

    Sound ENTITY_PHANTOM_BITE = getSound("entity.phantom.bite");

    Sound ENTITY_PHANTOM_DEATH = getSound("entity.phantom.death");

    Sound ENTITY_PHANTOM_FLAP = getSound("entity.phantom.flap");

    Sound ENTITY_PHANTOM_HURT = getSound("entity.phantom.hurt");

    Sound ENTITY_PHANTOM_SWOOP = getSound("entity.phantom.swoop");

    Sound ENTITY_PIG_AMBIENT = getSound("entity.pig.ambient");

    Sound ENTITY_PIG_DEATH = getSound("entity.pig.death");

    Sound ENTITY_PIG_HURT = getSound("entity.pig.hurt");

    Sound ENTITY_PIG_SADDLE = getSound("entity.pig.saddle");

    Sound ENTITY_PIG_STEP = getSound("entity.pig.step");

    Sound ENTITY_PIGLIN_ADMIRING_ITEM = getSound("entity.piglin.admiring_item");

    Sound ENTITY_PIGLIN_AMBIENT = getSound("entity.piglin.ambient");

    Sound ENTITY_PIGLIN_ANGRY = getSound("entity.piglin.angry");

    Sound ENTITY_PIGLIN_CELEBRATE = getSound("entity.piglin.celebrate");

    Sound ENTITY_PIGLIN_CONVERTED_TO_ZOMBIFIED = getSound("entity.piglin.converted_to_zombified");

    Sound ENTITY_PIGLIN_DEATH = getSound("entity.piglin.death");

    Sound ENTITY_PIGLIN_HURT = getSound("entity.piglin.hurt");

    Sound ENTITY_PIGLIN_JEALOUS = getSound("entity.piglin.jealous");

    Sound ENTITY_PIGLIN_RETREAT = getSound("entity.piglin.retreat");

    Sound ENTITY_PIGLIN_STEP = getSound("entity.piglin.step");

    Sound ENTITY_PIGLIN_BRUTE_AMBIENT = getSound("entity.piglin_brute.ambient");

    Sound ENTITY_PIGLIN_BRUTE_ANGRY = getSound("entity.piglin_brute.angry");

    Sound ENTITY_PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED = getSound("entity.piglin_brute.converted_to_zombified");

    Sound ENTITY_PIGLIN_BRUTE_DEATH = getSound("entity.piglin_brute.death");

    Sound ENTITY_PIGLIN_BRUTE_HURT = getSound("entity.piglin_brute.hurt");

    Sound ENTITY_PIGLIN_BRUTE_STEP = getSound("entity.piglin_brute.step");

    Sound ENTITY_PILLAGER_AMBIENT = getSound("entity.pillager.ambient");

    Sound ENTITY_PILLAGER_CELEBRATE = getSound("entity.pillager.celebrate");

    Sound ENTITY_PILLAGER_DEATH = getSound("entity.pillager.death");

    Sound ENTITY_PILLAGER_HURT = getSound("entity.pillager.hurt");

    Sound ENTITY_PLAYER_ATTACK_CRIT = getSound("entity.player.attack.crit");

    Sound ENTITY_PLAYER_ATTACK_KNOCKBACK = getSound("entity.player.attack.knockback");

    Sound ENTITY_PLAYER_ATTACK_NODAMAGE = getSound("entity.player.attack.nodamage");

    Sound ENTITY_PLAYER_ATTACK_STRONG = getSound("entity.player.attack.strong");

    Sound ENTITY_PLAYER_ATTACK_SWEEP = getSound("entity.player.attack.sweep");

    Sound ENTITY_PLAYER_ATTACK_WEAK = getSound("entity.player.attack.weak");

    Sound ENTITY_PLAYER_BIG_FALL = getSound("entity.player.big_fall");

    Sound ENTITY_PLAYER_BREATH = getSound("entity.player.breath");

    Sound ENTITY_PLAYER_BURP = getSound("entity.player.burp");

    Sound ENTITY_PLAYER_DEATH = getSound("entity.player.death");

    Sound ENTITY_PLAYER_HURT = getSound("entity.player.hurt");

    Sound ENTITY_PLAYER_HURT_DROWN = getSound("entity.player.hurt_drown");

    Sound ENTITY_PLAYER_HURT_FREEZE = getSound("entity.player.hurt_freeze");

    Sound ENTITY_PLAYER_HURT_ON_FIRE = getSound("entity.player.hurt_on_fire");

    Sound ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH = getSound("entity.player.hurt_sweet_berry_bush");

    Sound ENTITY_PLAYER_LEVELUP = getSound("entity.player.levelup");

    Sound ENTITY_PLAYER_SMALL_FALL = getSound("entity.player.small_fall");

    Sound ENTITY_PLAYER_SPLASH = getSound("entity.player.splash");

    Sound ENTITY_PLAYER_SPLASH_HIGH_SPEED = getSound("entity.player.splash.high_speed");

    Sound ENTITY_PLAYER_SWIM = getSound("entity.player.swim");

    Sound ENTITY_PLAYER_TELEPORT = getSound("entity.player.teleport");

    Sound ENTITY_POLAR_BEAR_AMBIENT = getSound("entity.polar_bear.ambient");

    Sound ENTITY_POLAR_BEAR_AMBIENT_BABY = getSound("entity.polar_bear.ambient_baby");

    Sound ENTITY_POLAR_BEAR_DEATH = getSound("entity.polar_bear.death");

    Sound ENTITY_POLAR_BEAR_HURT = getSound("entity.polar_bear.hurt");

    Sound ENTITY_POLAR_BEAR_STEP = getSound("entity.polar_bear.step");

    Sound ENTITY_POLAR_BEAR_WARNING = getSound("entity.polar_bear.warning");

    Sound ENTITY_PUFFER_FISH_BLOW_OUT = getSound("entity.puffer_fish.blow_out");

    Sound ENTITY_PUFFER_FISH_BLOW_UP = getSound("entity.puffer_fish.blow_up");

    Sound ENTITY_PUFFER_FISH_DEATH = getSound("entity.puffer_fish.death");

    Sound ENTITY_PUFFER_FISH_FLOP = getSound("entity.puffer_fish.flop");

    Sound ENTITY_PUFFER_FISH_HURT = getSound("entity.puffer_fish.hurt");

    Sound ENTITY_PUFFER_FISH_STING = getSound("entity.puffer_fish.sting");

    Sound ENTITY_RABBIT_AMBIENT = getSound("entity.rabbit.ambient");

    Sound ENTITY_RABBIT_ATTACK = getSound("entity.rabbit.attack");

    Sound ENTITY_RABBIT_DEATH = getSound("entity.rabbit.death");

    Sound ENTITY_RABBIT_HURT = getSound("entity.rabbit.hurt");

    Sound ENTITY_RABBIT_JUMP = getSound("entity.rabbit.jump");

    Sound ENTITY_RAVAGER_AMBIENT = getSound("entity.ravager.ambient");

    Sound ENTITY_RAVAGER_ATTACK = getSound("entity.ravager.attack");

    Sound ENTITY_RAVAGER_CELEBRATE = getSound("entity.ravager.celebrate");

    Sound ENTITY_RAVAGER_DEATH = getSound("entity.ravager.death");

    Sound ENTITY_RAVAGER_HURT = getSound("entity.ravager.hurt");

    Sound ENTITY_RAVAGER_ROAR = getSound("entity.ravager.roar");

    Sound ENTITY_RAVAGER_STEP = getSound("entity.ravager.step");

    Sound ENTITY_RAVAGER_STUNNED = getSound("entity.ravager.stunned");

    Sound ENTITY_SALMON_AMBIENT = getSound("entity.salmon.ambient");

    Sound ENTITY_SALMON_DEATH = getSound("entity.salmon.death");

    Sound ENTITY_SALMON_FLOP = getSound("entity.salmon.flop");

    Sound ENTITY_SALMON_HURT = getSound("entity.salmon.hurt");

    Sound ENTITY_SHEEP_AMBIENT = getSound("entity.sheep.ambient");

    Sound ENTITY_SHEEP_DEATH = getSound("entity.sheep.death");

    Sound ENTITY_SHEEP_HURT = getSound("entity.sheep.hurt");

    Sound ENTITY_SHEEP_SHEAR = getSound("entity.sheep.shear");

    Sound ENTITY_SHEEP_STEP = getSound("entity.sheep.step");

    Sound ENTITY_SHULKER_AMBIENT = getSound("entity.shulker.ambient");

    Sound ENTITY_SHULKER_CLOSE = getSound("entity.shulker.close");

    Sound ENTITY_SHULKER_DEATH = getSound("entity.shulker.death");

    Sound ENTITY_SHULKER_HURT = getSound("entity.shulker.hurt");

    Sound ENTITY_SHULKER_HURT_CLOSED = getSound("entity.shulker.hurt_closed");

    Sound ENTITY_SHULKER_OPEN = getSound("entity.shulker.open");

    Sound ENTITY_SHULKER_SHOOT = getSound("entity.shulker.shoot");

    Sound ENTITY_SHULKER_TELEPORT = getSound("entity.shulker.teleport");

    Sound ENTITY_SHULKER_BULLET_HIT = getSound("entity.shulker_bullet.hit");

    Sound ENTITY_SHULKER_BULLET_HURT = getSound("entity.shulker_bullet.hurt");

    Sound ENTITY_SILVERFISH_AMBIENT = getSound("entity.silverfish.ambient");

    Sound ENTITY_SILVERFISH_DEATH = getSound("entity.silverfish.death");

    Sound ENTITY_SILVERFISH_HURT = getSound("entity.silverfish.hurt");

    Sound ENTITY_SILVERFISH_STEP = getSound("entity.silverfish.step");

    Sound ENTITY_SKELETON_AMBIENT = getSound("entity.skeleton.ambient");

    Sound ENTITY_SKELETON_CONVERTED_TO_STRAY = getSound("entity.skeleton.converted_to_stray");

    Sound ENTITY_SKELETON_DEATH = getSound("entity.skeleton.death");

    Sound ENTITY_SKELETON_HURT = getSound("entity.skeleton.hurt");

    Sound ENTITY_SKELETON_SHOOT = getSound("entity.skeleton.shoot");

    Sound ENTITY_SKELETON_STEP = getSound("entity.skeleton.step");

    Sound ENTITY_SKELETON_HORSE_AMBIENT = getSound("entity.skeleton_horse.ambient");

    Sound ENTITY_SKELETON_HORSE_AMBIENT_WATER = getSound("entity.skeleton_horse.ambient_water");

    Sound ENTITY_SKELETON_HORSE_DEATH = getSound("entity.skeleton_horse.death");

    Sound ENTITY_SKELETON_HORSE_GALLOP_WATER = getSound("entity.skeleton_horse.gallop_water");

    Sound ENTITY_SKELETON_HORSE_HURT = getSound("entity.skeleton_horse.hurt");

    Sound ENTITY_SKELETON_HORSE_JUMP_WATER = getSound("entity.skeleton_horse.jump_water");

    Sound ENTITY_SKELETON_HORSE_STEP_WATER = getSound("entity.skeleton_horse.step_water");

    Sound ENTITY_SKELETON_HORSE_SWIM = getSound("entity.skeleton_horse.swim");

    Sound ENTITY_SLIME_ATTACK = getSound("entity.slime.attack");

    Sound ENTITY_SLIME_DEATH = getSound("entity.slime.death");

    Sound ENTITY_SLIME_DEATH_SMALL = getSound("entity.slime.death_small");

    Sound ENTITY_SLIME_HURT = getSound("entity.slime.hurt");

    Sound ENTITY_SLIME_HURT_SMALL = getSound("entity.slime.hurt_small");

    Sound ENTITY_SLIME_JUMP = getSound("entity.slime.jump");

    Sound ENTITY_SLIME_JUMP_SMALL = getSound("entity.slime.jump_small");

    Sound ENTITY_SLIME_SQUISH = getSound("entity.slime.squish");

    Sound ENTITY_SLIME_SQUISH_SMALL = getSound("entity.slime.squish_small");

    Sound ENTITY_SNIFFER_DEATH = getSound("entity.sniffer.death");

    Sound ENTITY_SNIFFER_DIGGING = getSound("entity.sniffer.digging");

    Sound ENTITY_SNIFFER_DIGGING_STOP = getSound("entity.sniffer.digging_stop");

    Sound ENTITY_SNIFFER_DROP_SEED = getSound("entity.sniffer.drop_seed");

    Sound ENTITY_SNIFFER_EAT = getSound("entity.sniffer.eat");

    Sound ENTITY_SNIFFER_HAPPY = getSound("entity.sniffer.happy");

    Sound ENTITY_SNIFFER_HURT = getSound("entity.sniffer.hurt");

    Sound ENTITY_SNIFFER_IDLE = getSound("entity.sniffer.idle");

    Sound ENTITY_SNIFFER_SCENTING = getSound("entity.sniffer.scenting");

    Sound ENTITY_SNIFFER_SEARCHING = getSound("entity.sniffer.searching");

    Sound ENTITY_SNIFFER_SNIFFING = getSound("entity.sniffer.sniffing");

    Sound ENTITY_SNIFFER_STEP = getSound("entity.sniffer.step");

    Sound ENTITY_SNOW_GOLEM_AMBIENT = getSound("entity.snow_golem.ambient");

    Sound ENTITY_SNOW_GOLEM_DEATH = getSound("entity.snow_golem.death");

    Sound ENTITY_SNOW_GOLEM_HURT = getSound("entity.snow_golem.hurt");

    Sound ENTITY_SNOW_GOLEM_SHEAR = getSound("entity.snow_golem.shear");

    Sound ENTITY_SNOW_GOLEM_SHOOT = getSound("entity.snow_golem.shoot");

    Sound ENTITY_SNOWBALL_THROW = getSound("entity.snowball.throw");

    Sound ENTITY_SPIDER_AMBIENT = getSound("entity.spider.ambient");

    Sound ENTITY_SPIDER_DEATH = getSound("entity.spider.death");

    Sound ENTITY_SPIDER_HURT = getSound("entity.spider.hurt");

    Sound ENTITY_SPIDER_STEP = getSound("entity.spider.step");

    Sound ENTITY_SPLASH_POTION_BREAK = getSound("entity.splash_potion.break");

    Sound ENTITY_SPLASH_POTION_THROW = getSound("entity.splash_potion.throw");

    Sound ENTITY_SQUID_AMBIENT = getSound("entity.squid.ambient");

    Sound ENTITY_SQUID_DEATH = getSound("entity.squid.death");

    Sound ENTITY_SQUID_HURT = getSound("entity.squid.hurt");

    Sound ENTITY_SQUID_SQUIRT = getSound("entity.squid.squirt");

    Sound ENTITY_STRAY_AMBIENT = getSound("entity.stray.ambient");

    Sound ENTITY_STRAY_DEATH = getSound("entity.stray.death");

    Sound ENTITY_STRAY_HURT = getSound("entity.stray.hurt");

    Sound ENTITY_STRAY_STEP = getSound("entity.stray.step");

    Sound ENTITY_STRIDER_AMBIENT = getSound("entity.strider.ambient");

    Sound ENTITY_STRIDER_DEATH = getSound("entity.strider.death");

    Sound ENTITY_STRIDER_EAT = getSound("entity.strider.eat");

    Sound ENTITY_STRIDER_HAPPY = getSound("entity.strider.happy");

    Sound ENTITY_STRIDER_HURT = getSound("entity.strider.hurt");

    Sound ENTITY_STRIDER_RETREAT = getSound("entity.strider.retreat");

    Sound ENTITY_STRIDER_SADDLE = getSound("entity.strider.saddle");

    Sound ENTITY_STRIDER_STEP = getSound("entity.strider.step");

    Sound ENTITY_STRIDER_STEP_LAVA = getSound("entity.strider.step_lava");

    Sound ENTITY_TADPOLE_DEATH = getSound("entity.tadpole.death");

    Sound ENTITY_TADPOLE_FLOP = getSound("entity.tadpole.flop");

    Sound ENTITY_TADPOLE_GROW_UP = getSound("entity.tadpole.grow_up");

    Sound ENTITY_TADPOLE_HURT = getSound("entity.tadpole.hurt");

    Sound ENTITY_TNT_PRIMED = getSound("entity.tnt.primed");

    Sound ENTITY_TROPICAL_FISH_AMBIENT = getSound("entity.tropical_fish.ambient");

    Sound ENTITY_TROPICAL_FISH_DEATH = getSound("entity.tropical_fish.death");

    Sound ENTITY_TROPICAL_FISH_FLOP = getSound("entity.tropical_fish.flop");

    Sound ENTITY_TROPICAL_FISH_HURT = getSound("entity.tropical_fish.hurt");

    Sound ENTITY_TURTLE_AMBIENT_LAND = getSound("entity.turtle.ambient_land");

    Sound ENTITY_TURTLE_DEATH = getSound("entity.turtle.death");

    Sound ENTITY_TURTLE_DEATH_BABY = getSound("entity.turtle.death_baby");

    Sound ENTITY_TURTLE_EGG_BREAK = getSound("entity.turtle.egg_break");

    Sound ENTITY_TURTLE_EGG_CRACK = getSound("entity.turtle.egg_crack");

    Sound ENTITY_TURTLE_EGG_HATCH = getSound("entity.turtle.egg_hatch");

    Sound ENTITY_TURTLE_HURT = getSound("entity.turtle.hurt");

    Sound ENTITY_TURTLE_HURT_BABY = getSound("entity.turtle.hurt_baby");

    Sound ENTITY_TURTLE_LAY_EGG = getSound("entity.turtle.lay_egg");

    Sound ENTITY_TURTLE_SHAMBLE = getSound("entity.turtle.shamble");

    Sound ENTITY_TURTLE_SHAMBLE_BABY = getSound("entity.turtle.shamble_baby");

    Sound ENTITY_TURTLE_SWIM = getSound("entity.turtle.swim");

    Sound ENTITY_VEX_AMBIENT = getSound("entity.vex.ambient");

    Sound ENTITY_VEX_CHARGE = getSound("entity.vex.charge");

    Sound ENTITY_VEX_DEATH = getSound("entity.vex.death");

    Sound ENTITY_VEX_HURT = getSound("entity.vex.hurt");

    Sound ENTITY_VILLAGER_AMBIENT = getSound("entity.villager.ambient");

    Sound ENTITY_VILLAGER_CELEBRATE = getSound("entity.villager.celebrate");

    Sound ENTITY_VILLAGER_DEATH = getSound("entity.villager.death");

    Sound ENTITY_VILLAGER_HURT = getSound("entity.villager.hurt");

    Sound ENTITY_VILLAGER_NO = getSound("entity.villager.no");

    Sound ENTITY_VILLAGER_TRADE = getSound("entity.villager.trade");

    Sound ENTITY_VILLAGER_WORK_ARMORER = getSound("entity.villager.work_armorer");

    Sound ENTITY_VILLAGER_WORK_BUTCHER = getSound("entity.villager.work_butcher");

    Sound ENTITY_VILLAGER_WORK_CARTOGRAPHER = getSound("entity.villager.work_cartographer");

    Sound ENTITY_VILLAGER_WORK_CLERIC = getSound("entity.villager.work_cleric");

    Sound ENTITY_VILLAGER_WORK_FARMER = getSound("entity.villager.work_farmer");

    Sound ENTITY_VILLAGER_WORK_FISHERMAN = getSound("entity.villager.work_fisherman");

    Sound ENTITY_VILLAGER_WORK_FLETCHER = getSound("entity.villager.work_fletcher");

    Sound ENTITY_VILLAGER_WORK_LEATHERWORKER = getSound("entity.villager.work_leatherworker");

    Sound ENTITY_VILLAGER_WORK_LIBRARIAN = getSound("entity.villager.work_librarian");

    Sound ENTITY_VILLAGER_WORK_MASON = getSound("entity.villager.work_mason");

    Sound ENTITY_VILLAGER_WORK_SHEPHERD = getSound("entity.villager.work_shepherd");

    Sound ENTITY_VILLAGER_WORK_TOOLSMITH = getSound("entity.villager.work_toolsmith");

    Sound ENTITY_VILLAGER_WORK_WEAPONSMITH = getSound("entity.villager.work_weaponsmith");

    Sound ENTITY_VILLAGER_YES = getSound("entity.villager.yes");

    Sound ENTITY_VINDICATOR_AMBIENT = getSound("entity.vindicator.ambient");

    Sound ENTITY_VINDICATOR_CELEBRATE = getSound("entity.vindicator.celebrate");

    Sound ENTITY_VINDICATOR_DEATH = getSound("entity.vindicator.death");

    Sound ENTITY_VINDICATOR_HURT = getSound("entity.vindicator.hurt");

    Sound ENTITY_WANDERING_TRADER_AMBIENT = getSound("entity.wandering_trader.ambient");

    Sound ENTITY_WANDERING_TRADER_DEATH = getSound("entity.wandering_trader.death");

    Sound ENTITY_WANDERING_TRADER_DISAPPEARED = getSound("entity.wandering_trader.disappeared");

    Sound ENTITY_WANDERING_TRADER_DRINK_MILK = getSound("entity.wandering_trader.drink_milk");

    Sound ENTITY_WANDERING_TRADER_DRINK_POTION = getSound("entity.wandering_trader.drink_potion");

    Sound ENTITY_WANDERING_TRADER_HURT = getSound("entity.wandering_trader.hurt");

    Sound ENTITY_WANDERING_TRADER_NO = getSound("entity.wandering_trader.no");

    Sound ENTITY_WANDERING_TRADER_REAPPEARED = getSound("entity.wandering_trader.reappeared");

    Sound ENTITY_WANDERING_TRADER_TRADE = getSound("entity.wandering_trader.trade");

    Sound ENTITY_WANDERING_TRADER_YES = getSound("entity.wandering_trader.yes");

    Sound ENTITY_WARDEN_AGITATED = getSound("entity.warden.agitated");

    Sound ENTITY_WARDEN_AMBIENT = getSound("entity.warden.ambient");

    Sound ENTITY_WARDEN_ANGRY = getSound("entity.warden.angry");

    Sound ENTITY_WARDEN_ATTACK_IMPACT = getSound("entity.warden.attack_impact");

    Sound ENTITY_WARDEN_DEATH = getSound("entity.warden.death");

    Sound ENTITY_WARDEN_DIG = getSound("entity.warden.dig");

    Sound ENTITY_WARDEN_EMERGE = getSound("entity.warden.emerge");

    Sound ENTITY_WARDEN_HEARTBEAT = getSound("entity.warden.heartbeat");

    Sound ENTITY_WARDEN_HURT = getSound("entity.warden.hurt");

    Sound ENTITY_WARDEN_LISTENING = getSound("entity.warden.listening");

    Sound ENTITY_WARDEN_LISTENING_ANGRY = getSound("entity.warden.listening_angry");

    Sound ENTITY_WARDEN_NEARBY_CLOSE = getSound("entity.warden.nearby_close");

    Sound ENTITY_WARDEN_NEARBY_CLOSER = getSound("entity.warden.nearby_closer");

    Sound ENTITY_WARDEN_NEARBY_CLOSEST = getSound("entity.warden.nearby_closest");

    Sound ENTITY_WARDEN_ROAR = getSound("entity.warden.roar");

    Sound ENTITY_WARDEN_SNIFF = getSound("entity.warden.sniff");

    Sound ENTITY_WARDEN_SONIC_BOOM = getSound("entity.warden.sonic_boom");

    Sound ENTITY_WARDEN_SONIC_CHARGE = getSound("entity.warden.sonic_charge");

    Sound ENTITY_WARDEN_STEP = getSound("entity.warden.step");

    Sound ENTITY_WARDEN_TENDRIL_CLICKS = getSound("entity.warden.tendril_clicks");

    Sound ENTITY_WIND_CHARGE_THROW = getSound("entity.wind_charge.throw");

    Sound ENTITY_WIND_CHARGE_WIND_BURST = getSound("entity.wind_charge.wind_burst");

    Sound ENTITY_WITCH_AMBIENT = getSound("entity.witch.ambient");

    Sound ENTITY_WITCH_CELEBRATE = getSound("entity.witch.celebrate");

    Sound ENTITY_WITCH_DEATH = getSound("entity.witch.death");

    Sound ENTITY_WITCH_DRINK = getSound("entity.witch.drink");

    Sound ENTITY_WITCH_HURT = getSound("entity.witch.hurt");

    Sound ENTITY_WITCH_THROW = getSound("entity.witch.throw");

    Sound ENTITY_WITHER_AMBIENT = getSound("entity.wither.ambient");

    Sound ENTITY_WITHER_BREAK_BLOCK = getSound("entity.wither.break_block");

    Sound ENTITY_WITHER_DEATH = getSound("entity.wither.death");

    Sound ENTITY_WITHER_HURT = getSound("entity.wither.hurt");

    Sound ENTITY_WITHER_SHOOT = getSound("entity.wither.shoot");

    Sound ENTITY_WITHER_SPAWN = getSound("entity.wither.spawn");

    Sound ENTITY_WITHER_SKELETON_AMBIENT = getSound("entity.wither_skeleton.ambient");

    Sound ENTITY_WITHER_SKELETON_DEATH = getSound("entity.wither_skeleton.death");

    Sound ENTITY_WITHER_SKELETON_HURT = getSound("entity.wither_skeleton.hurt");

    Sound ENTITY_WITHER_SKELETON_STEP = getSound("entity.wither_skeleton.step");

    Sound ENTITY_WOLF_AMBIENT = getSound("entity.wolf.ambient");

    Sound ENTITY_WOLF_DEATH = getSound("entity.wolf.death");

    Sound ENTITY_WOLF_GROWL = getSound("entity.wolf.growl");

    Sound ENTITY_WOLF_HURT = getSound("entity.wolf.hurt");

    Sound ENTITY_WOLF_PANT = getSound("entity.wolf.pant");

    Sound ENTITY_WOLF_SHAKE = getSound("entity.wolf.shake");

    Sound ENTITY_WOLF_STEP = getSound("entity.wolf.step");

    Sound ENTITY_WOLF_WHINE = getSound("entity.wolf.whine");

    Sound ENTITY_WOLF_ANGRY_AMBIENT = getSound("entity.wolf_angry.ambient");

    Sound ENTITY_WOLF_ANGRY_DEATH = getSound("entity.wolf_angry.death");

    Sound ENTITY_WOLF_ANGRY_GROWL = getSound("entity.wolf_angry.growl");

    Sound ENTITY_WOLF_ANGRY_HURT = getSound("entity.wolf_angry.hurt");

    Sound ENTITY_WOLF_ANGRY_PANT = getSound("entity.wolf_angry.pant");

    Sound ENTITY_WOLF_ANGRY_WHINE = getSound("entity.wolf_angry.whine");

    Sound ENTITY_WOLF_BIG_AMBIENT = getSound("entity.wolf_big.ambient");

    Sound ENTITY_WOLF_BIG_DEATH = getSound("entity.wolf_big.death");

    Sound ENTITY_WOLF_BIG_GROWL = getSound("entity.wolf_big.growl");

    Sound ENTITY_WOLF_BIG_HURT = getSound("entity.wolf_big.hurt");

    Sound ENTITY_WOLF_BIG_PANT = getSound("entity.wolf_big.pant");

    Sound ENTITY_WOLF_BIG_WHINE = getSound("entity.wolf_big.whine");

    Sound ENTITY_WOLF_CUTE_AMBIENT = getSound("entity.wolf_cute.ambient");

    Sound ENTITY_WOLF_CUTE_DEATH = getSound("entity.wolf_cute.death");

    Sound ENTITY_WOLF_CUTE_GROWL = getSound("entity.wolf_cute.growl");

    Sound ENTITY_WOLF_CUTE_HURT = getSound("entity.wolf_cute.hurt");

    Sound ENTITY_WOLF_CUTE_PANT = getSound("entity.wolf_cute.pant");

    Sound ENTITY_WOLF_CUTE_WHINE = getSound("entity.wolf_cute.whine");

    Sound ENTITY_WOLF_GRUMPY_AMBIENT = getSound("entity.wolf_grumpy.ambient");

    Sound ENTITY_WOLF_GRUMPY_DEATH = getSound("entity.wolf_grumpy.death");

    Sound ENTITY_WOLF_GRUMPY_GROWL = getSound("entity.wolf_grumpy.growl");

    Sound ENTITY_WOLF_GRUMPY_HURT = getSound("entity.wolf_grumpy.hurt");

    Sound ENTITY_WOLF_GRUMPY_PANT = getSound("entity.wolf_grumpy.pant");

    Sound ENTITY_WOLF_GRUMPY_WHINE = getSound("entity.wolf_grumpy.whine");

    Sound ENTITY_WOLF_PUGLIN_AMBIENT = getSound("entity.wolf_puglin.ambient");

    Sound ENTITY_WOLF_PUGLIN_DEATH = getSound("entity.wolf_puglin.death");

    Sound ENTITY_WOLF_PUGLIN_GROWL = getSound("entity.wolf_puglin.growl");

    Sound ENTITY_WOLF_PUGLIN_HURT = getSound("entity.wolf_puglin.hurt");

    Sound ENTITY_WOLF_PUGLIN_PANT = getSound("entity.wolf_puglin.pant");

    Sound ENTITY_WOLF_PUGLIN_WHINE = getSound("entity.wolf_puglin.whine");

    Sound ENTITY_WOLF_SAD_AMBIENT = getSound("entity.wolf_sad.ambient");

    Sound ENTITY_WOLF_SAD_DEATH = getSound("entity.wolf_sad.death");

    Sound ENTITY_WOLF_SAD_GROWL = getSound("entity.wolf_sad.growl");

    Sound ENTITY_WOLF_SAD_HURT = getSound("entity.wolf_sad.hurt");

    Sound ENTITY_WOLF_SAD_PANT = getSound("entity.wolf_sad.pant");

    Sound ENTITY_WOLF_SAD_WHINE = getSound("entity.wolf_sad.whine");

    Sound ENTITY_ZOGLIN_AMBIENT = getSound("entity.zoglin.ambient");

    Sound ENTITY_ZOGLIN_ANGRY = getSound("entity.zoglin.angry");

    Sound ENTITY_ZOGLIN_ATTACK = getSound("entity.zoglin.attack");

    Sound ENTITY_ZOGLIN_DEATH = getSound("entity.zoglin.death");

    Sound ENTITY_ZOGLIN_HURT = getSound("entity.zoglin.hurt");

    Sound ENTITY_ZOGLIN_STEP = getSound("entity.zoglin.step");

    Sound ENTITY_ZOMBIE_AMBIENT = getSound("entity.zombie.ambient");

    Sound ENTITY_ZOMBIE_ATTACK_IRON_DOOR = getSound("entity.zombie.attack_iron_door");

    Sound ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR = getSound("entity.zombie.attack_wooden_door");

    Sound ENTITY_ZOMBIE_BREAK_WOODEN_DOOR = getSound("entity.zombie.break_wooden_door");

    Sound ENTITY_ZOMBIE_CONVERTED_TO_DROWNED = getSound("entity.zombie.converted_to_drowned");

    Sound ENTITY_ZOMBIE_DEATH = getSound("entity.zombie.death");

    Sound ENTITY_ZOMBIE_DESTROY_EGG = getSound("entity.zombie.destroy_egg");

    Sound ENTITY_ZOMBIE_HURT = getSound("entity.zombie.hurt");

    Sound ENTITY_ZOMBIE_INFECT = getSound("entity.zombie.infect");

    Sound ENTITY_ZOMBIE_STEP = getSound("entity.zombie.step");

    Sound ENTITY_ZOMBIE_HORSE_AMBIENT = getSound("entity.zombie_horse.ambient");

    Sound ENTITY_ZOMBIE_HORSE_DEATH = getSound("entity.zombie_horse.death");

    Sound ENTITY_ZOMBIE_HORSE_HURT = getSound("entity.zombie_horse.hurt");

    Sound ENTITY_ZOMBIE_VILLAGER_AMBIENT = getSound("entity.zombie_villager.ambient");

    Sound ENTITY_ZOMBIE_VILLAGER_CONVERTED = getSound("entity.zombie_villager.converted");

    Sound ENTITY_ZOMBIE_VILLAGER_CURE = getSound("entity.zombie_villager.cure");

    Sound ENTITY_ZOMBIE_VILLAGER_DEATH = getSound("entity.zombie_villager.death");

    Sound ENTITY_ZOMBIE_VILLAGER_HURT = getSound("entity.zombie_villager.hurt");

    Sound ENTITY_ZOMBIE_VILLAGER_STEP = getSound("entity.zombie_villager.step");

    Sound ENTITY_ZOMBIFIED_PIGLIN_AMBIENT = getSound("entity.zombified_piglin.ambient");

    Sound ENTITY_ZOMBIFIED_PIGLIN_ANGRY = getSound("entity.zombified_piglin.angry");

    Sound ENTITY_ZOMBIFIED_PIGLIN_DEATH = getSound("entity.zombified_piglin.death");

    Sound ENTITY_ZOMBIFIED_PIGLIN_HURT = getSound("entity.zombified_piglin.hurt");

    Sound EVENT_MOB_EFFECT_BAD_OMEN = getSound("event.mob_effect.bad_omen");

    Sound EVENT_MOB_EFFECT_RAID_OMEN = getSound("event.mob_effect.raid_omen");

    Sound EVENT_MOB_EFFECT_TRIAL_OMEN = getSound("event.mob_effect.trial_omen");

    Sound EVENT_RAID_HORN = getSound("event.raid.horn");

    Sound INTENTIONALLY_EMPTY = getSound("intentionally_empty");

    Sound ITEM_ARMOR_EQUIP_CHAIN = getSound("item.armor.equip_chain");

    Sound ITEM_ARMOR_EQUIP_DIAMOND = getSound("item.armor.equip_diamond");

    Sound ITEM_ARMOR_EQUIP_ELYTRA = getSound("item.armor.equip_elytra");

    Sound ITEM_ARMOR_EQUIP_GENERIC = getSound("item.armor.equip_generic");

    Sound ITEM_ARMOR_EQUIP_GOLD = getSound("item.armor.equip_gold");

    Sound ITEM_ARMOR_EQUIP_IRON = getSound("item.armor.equip_iron");

    Sound ITEM_ARMOR_EQUIP_LEATHER = getSound("item.armor.equip_leather");

    Sound ITEM_ARMOR_EQUIP_NETHERITE = getSound("item.armor.equip_netherite");

    Sound ITEM_ARMOR_EQUIP_TURTLE = getSound("item.armor.equip_turtle");

    Sound ITEM_ARMOR_EQUIP_WOLF = getSound("item.armor.equip_wolf");

    Sound ITEM_ARMOR_UNEQUIP_WOLF = getSound("item.armor.unequip_wolf");

    Sound ITEM_AXE_SCRAPE = getSound("item.axe.scrape");

    Sound ITEM_AXE_STRIP = getSound("item.axe.strip");

    Sound ITEM_AXE_WAX_OFF = getSound("item.axe.wax_off");

    Sound ITEM_BONE_MEAL_USE = getSound("item.bone_meal.use");

    Sound ITEM_BOOK_PAGE_TURN = getSound("item.book.page_turn");

    Sound ITEM_BOOK_PUT = getSound("item.book.put");

    Sound ITEM_BOTTLE_EMPTY = getSound("item.bottle.empty");

    Sound ITEM_BOTTLE_FILL = getSound("item.bottle.fill");

    Sound ITEM_BOTTLE_FILL_DRAGONBREATH = getSound("item.bottle.fill_dragonbreath");

    Sound ITEM_BRUSH_BRUSHING_GENERIC = getSound("item.brush.brushing.generic");

    Sound ITEM_BRUSH_BRUSHING_GRAVEL = getSound("item.brush.brushing.gravel");

    Sound ITEM_BRUSH_BRUSHING_GRAVEL_COMPLETE = getSound("item.brush.brushing.gravel.complete");

    Sound ITEM_BRUSH_BRUSHING_SAND = getSound("item.brush.brushing.sand");

    Sound ITEM_BRUSH_BRUSHING_SAND_COMPLETE = getSound("item.brush.brushing.sand.complete");

    Sound ITEM_BUCKET_EMPTY = getSound("item.bucket.empty");

    Sound ITEM_BUCKET_EMPTY_AXOLOTL = getSound("item.bucket.empty_axolotl");

    Sound ITEM_BUCKET_EMPTY_FISH = getSound("item.bucket.empty_fish");

    Sound ITEM_BUCKET_EMPTY_LAVA = getSound("item.bucket.empty_lava");

    Sound ITEM_BUCKET_EMPTY_POWDER_SNOW = getSound("item.bucket.empty_powder_snow");

    Sound ITEM_BUCKET_EMPTY_TADPOLE = getSound("item.bucket.empty_tadpole");

    Sound ITEM_BUCKET_FILL = getSound("item.bucket.fill");

    Sound ITEM_BUCKET_FILL_AXOLOTL = getSound("item.bucket.fill_axolotl");

    Sound ITEM_BUCKET_FILL_FISH = getSound("item.bucket.fill_fish");

    Sound ITEM_BUCKET_FILL_LAVA = getSound("item.bucket.fill_lava");

    Sound ITEM_BUCKET_FILL_POWDER_SNOW = getSound("item.bucket.fill_powder_snow");

    Sound ITEM_BUCKET_FILL_TADPOLE = getSound("item.bucket.fill_tadpole");

    Sound ITEM_BUNDLE_DROP_CONTENTS = getSound("item.bundle.drop_contents");

    Sound ITEM_BUNDLE_INSERT = getSound("item.bundle.insert");

    Sound ITEM_BUNDLE_INSERT_FAIL = getSound("item.bundle.insert_fail");

    Sound ITEM_BUNDLE_REMOVE_ONE = getSound("item.bundle.remove_one");

    Sound ITEM_CHORUS_FRUIT_TELEPORT = getSound("item.chorus_fruit.teleport");

    Sound ITEM_CROP_PLANT = getSound("item.crop.plant");

    Sound ITEM_CROSSBOW_HIT = getSound("item.crossbow.hit");

    Sound ITEM_CROSSBOW_LOADING_END = getSound("item.crossbow.loading_end");

    Sound ITEM_CROSSBOW_LOADING_MIDDLE = getSound("item.crossbow.loading_middle");

    Sound ITEM_CROSSBOW_LOADING_START = getSound("item.crossbow.loading_start");

    Sound ITEM_CROSSBOW_QUICK_CHARGE_1 = getSound("item.crossbow.quick_charge_1");

    Sound ITEM_CROSSBOW_QUICK_CHARGE_2 = getSound("item.crossbow.quick_charge_2");

    Sound ITEM_CROSSBOW_QUICK_CHARGE_3 = getSound("item.crossbow.quick_charge_3");

    Sound ITEM_CROSSBOW_SHOOT = getSound("item.crossbow.shoot");

    Sound ITEM_DYE_USE = getSound("item.dye.use");

    Sound ITEM_ELYTRA_FLYING = getSound("item.elytra.flying");

    Sound ITEM_FIRECHARGE_USE = getSound("item.firecharge.use");

    Sound ITEM_FLINTANDSTEEL_USE = getSound("item.flintandsteel.use");

    Sound ITEM_GLOW_INK_SAC_USE = getSound("item.glow_ink_sac.use");

    Sound ITEM_GOAT_HORN_SOUND_0 = getSound("item.goat_horn.sound.0");

    Sound ITEM_GOAT_HORN_SOUND_1 = getSound("item.goat_horn.sound.1");

    Sound ITEM_GOAT_HORN_SOUND_2 = getSound("item.goat_horn.sound.2");

    Sound ITEM_GOAT_HORN_SOUND_3 = getSound("item.goat_horn.sound.3");

    Sound ITEM_GOAT_HORN_SOUND_4 = getSound("item.goat_horn.sound.4");

    Sound ITEM_GOAT_HORN_SOUND_5 = getSound("item.goat_horn.sound.5");

    Sound ITEM_GOAT_HORN_SOUND_6 = getSound("item.goat_horn.sound.6");

    Sound ITEM_GOAT_HORN_SOUND_7 = getSound("item.goat_horn.sound.7");

    Sound ITEM_HOE_TILL = getSound("item.hoe.till");

    Sound ITEM_HONEY_BOTTLE_DRINK = getSound("item.honey_bottle.drink");

    Sound ITEM_HONEYCOMB_WAX_ON = getSound("item.honeycomb.wax_on");

    Sound ITEM_HORSE_ARMOR_UNEQUIP = getSound("item.horse_armor.unequip");

    Sound ITEM_INK_SAC_USE = getSound("item.ink_sac.use");

    Sound ITEM_LEAD_BREAK = getSound("item.lead.break");

    Sound ITEM_LEAD_TIED = getSound("item.lead.tied");

    Sound ITEM_LEAD_UNTIED = getSound("item.lead.untied");

    Sound ITEM_LLAMA_CARPET_UNEQUIP = getSound("item.llama_carpet.unequip");

    Sound ITEM_LODESTONE_COMPASS_LOCK = getSound("item.lodestone_compass.lock");

    Sound ITEM_MACE_SMASH_AIR = getSound("item.mace.smash_air");

    Sound ITEM_MACE_SMASH_GROUND = getSound("item.mace.smash_ground");

    Sound ITEM_MACE_SMASH_GROUND_HEAVY = getSound("item.mace.smash_ground_heavy");

    Sound ITEM_NETHER_WART_PLANT = getSound("item.nether_wart.plant");

    Sound ITEM_OMINOUS_BOTTLE_DISPOSE = getSound("item.ominous_bottle.dispose");

    Sound ITEM_SADDLE_UNEQUIP = getSound("item.saddle.unequip");

    Sound ITEM_SHEARS_SNIP = getSound("item.shears.snip");

    Sound ITEM_SHIELD_BLOCK = getSound("item.shield.block");

    Sound ITEM_SHIELD_BREAK = getSound("item.shield.break");

    Sound ITEM_SHOVEL_FLATTEN = getSound("item.shovel.flatten");

    Sound ITEM_SPYGLASS_STOP_USING = getSound("item.spyglass.stop_using");

    Sound ITEM_SPYGLASS_USE = getSound("item.spyglass.use");

    Sound ITEM_TOTEM_USE = getSound("item.totem.use");

    Sound ITEM_TRIDENT_HIT = getSound("item.trident.hit");

    Sound ITEM_TRIDENT_HIT_GROUND = getSound("item.trident.hit_ground");

    Sound ITEM_TRIDENT_RETURN = getSound("item.trident.return");

    Sound ITEM_TRIDENT_RIPTIDE_1 = getSound("item.trident.riptide_1");

    Sound ITEM_TRIDENT_RIPTIDE_2 = getSound("item.trident.riptide_2");

    Sound ITEM_TRIDENT_RIPTIDE_3 = getSound("item.trident.riptide_3");

    Sound ITEM_TRIDENT_THROW = getSound("item.trident.throw");

    Sound ITEM_TRIDENT_THUNDER = getSound("item.trident.thunder");

    Sound ITEM_WOLF_ARMOR_BREAK = getSound("item.wolf_armor.break");

    Sound ITEM_WOLF_ARMOR_CRACK = getSound("item.wolf_armor.crack");

    Sound ITEM_WOLF_ARMOR_DAMAGE = getSound("item.wolf_armor.damage");

    Sound ITEM_WOLF_ARMOR_REPAIR = getSound("item.wolf_armor.repair");

    Sound MUSIC_CREATIVE = getSound("music.creative");

    Sound MUSIC_CREDITS = getSound("music.credits");

    Sound MUSIC_DRAGON = getSound("music.dragon");

    Sound MUSIC_END = getSound("music.end");

    Sound MUSIC_GAME = getSound("music.game");

    Sound MUSIC_MENU = getSound("music.menu");

    Sound MUSIC_NETHER_BASALT_DELTAS = getSound("music.nether.basalt_deltas");

    Sound MUSIC_NETHER_CRIMSON_FOREST = getSound("music.nether.crimson_forest");

    Sound MUSIC_NETHER_NETHER_WASTES = getSound("music.nether.nether_wastes");

    Sound MUSIC_NETHER_SOUL_SAND_VALLEY = getSound("music.nether.soul_sand_valley");

    Sound MUSIC_NETHER_WARPED_FOREST = getSound("music.nether.warped_forest");

    Sound MUSIC_OVERWORLD_BADLANDS = getSound("music.overworld.badlands");

    Sound MUSIC_OVERWORLD_BAMBOO_JUNGLE = getSound("music.overworld.bamboo_jungle");

    Sound MUSIC_OVERWORLD_CHERRY_GROVE = getSound("music.overworld.cherry_grove");

    Sound MUSIC_OVERWORLD_DEEP_DARK = getSound("music.overworld.deep_dark");

    Sound MUSIC_OVERWORLD_DESERT = getSound("music.overworld.desert");

    Sound MUSIC_OVERWORLD_DRIPSTONE_CAVES = getSound("music.overworld.dripstone_caves");

    Sound MUSIC_OVERWORLD_FLOWER_FOREST = getSound("music.overworld.flower_forest");

    Sound MUSIC_OVERWORLD_FOREST = getSound("music.overworld.forest");

    Sound MUSIC_OVERWORLD_FROZEN_PEAKS = getSound("music.overworld.frozen_peaks");

    Sound MUSIC_OVERWORLD_GROVE = getSound("music.overworld.grove");

    Sound MUSIC_OVERWORLD_JAGGED_PEAKS = getSound("music.overworld.jagged_peaks");

    Sound MUSIC_OVERWORLD_JUNGLE = getSound("music.overworld.jungle");

    Sound MUSIC_OVERWORLD_LUSH_CAVES = getSound("music.overworld.lush_caves");

    Sound MUSIC_OVERWORLD_MEADOW = getSound("music.overworld.meadow");

    Sound MUSIC_OVERWORLD_OLD_GROWTH_TAIGA = getSound("music.overworld.old_growth_taiga");

    Sound MUSIC_OVERWORLD_SNOWY_SLOPES = getSound("music.overworld.snowy_slopes");

    Sound MUSIC_OVERWORLD_SPARSE_JUNGLE = getSound("music.overworld.sparse_jungle");

    Sound MUSIC_OVERWORLD_STONY_PEAKS = getSound("music.overworld.stony_peaks");

    Sound MUSIC_OVERWORLD_SWAMP = getSound("music.overworld.swamp");

    Sound MUSIC_UNDER_WATER = getSound("music.under_water");

    Sound MUSIC_DISC_11 = getSound("music_disc.11");

    Sound MUSIC_DISC_13 = getSound("music_disc.13");

    Sound MUSIC_DISC_5 = getSound("music_disc.5");

    Sound MUSIC_DISC_BLOCKS = getSound("music_disc.blocks");

    Sound MUSIC_DISC_CAT = getSound("music_disc.cat");

    Sound MUSIC_DISC_CHIRP = getSound("music_disc.chirp");

    Sound MUSIC_DISC_CREATOR = getSound("music_disc.creator");

    Sound MUSIC_DISC_CREATOR_MUSIC_BOX = getSound("music_disc.creator_music_box");

    Sound MUSIC_DISC_FAR = getSound("music_disc.far");

    Sound MUSIC_DISC_MALL = getSound("music_disc.mall");

    Sound MUSIC_DISC_MELLOHI = getSound("music_disc.mellohi");

    Sound MUSIC_DISC_OTHERSIDE = getSound("music_disc.otherside");

    Sound MUSIC_DISC_PIGSTEP = getSound("music_disc.pigstep");

    Sound MUSIC_DISC_PRECIPICE = getSound("music_disc.precipice");

    Sound MUSIC_DISC_RELIC = getSound("music_disc.relic");

    Sound MUSIC_DISC_STAL = getSound("music_disc.stal");

    Sound MUSIC_DISC_STRAD = getSound("music_disc.strad");

    Sound MUSIC_DISC_TEARS = getSound("music_disc.tears");

    Sound MUSIC_DISC_WAIT = getSound("music_disc.wait");

    Sound MUSIC_DISC_WARD = getSound("music_disc.ward");

    Sound PARTICLE_SOUL_ESCAPE = getSound("particle.soul_escape");

    Sound UI_BUTTON_CLICK = getSound("ui.button.click");

    Sound UI_CARTOGRAPHY_TABLE_TAKE_RESULT = getSound("ui.cartography_table.take_result");

    Sound UI_HUD_BUBBLE_POP = getSound("ui.hud.bubble_pop");

    Sound UI_LOOM_SELECT_PATTERN = getSound("ui.loom.select_pattern");

    Sound UI_LOOM_TAKE_RESULT = getSound("ui.loom.take_result");

    Sound UI_STONECUTTER_SELECT_RECIPE = getSound("ui.stonecutter.select_recipe");

    Sound UI_STONECUTTER_TAKE_RESULT = getSound("ui.stonecutter.take_result");

    Sound UI_TOAST_CHALLENGE_COMPLETE = getSound("ui.toast.challenge_complete");

    Sound UI_TOAST_IN = getSound("ui.toast.in");

    Sound UI_TOAST_OUT = getSound("ui.toast.out");

    Sound WEATHER_RAIN = getSound("weather.rain");

    Sound WEATHER_RAIN_ABOVE = getSound("weather.rain.above");
    // End generate - Sound

    @NotNull
    private static Sound getSound(@NotNull String key) {
        return Registry.SOUNDS.getOrThrow(NamespacedKey.minecraft(key));
    }

    /**
     * @param name of the sound.
     * @return the sound with the given name.
     * @deprecated only for backwards compatibility, use {@link Registry#get(NamespacedKey)} instead.
     */
    @NotNull
    @Deprecated(since = "1.21.3", forRemoval = true) @org.jetbrains.annotations.ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
    static Sound valueOf(@NotNull String name) {
        Sound sound = Bukkit.getUnsafe().get(RegistryKey.SOUND_EVENT, NamespacedKey.fromString(name.toLowerCase(Locale.ROOT)));
        if (sound != null) {
            return sound;
        }

        // Sound keys can have dots in them which where converted to _. Since converting
        // the _ back to a dot would be to complex (since not all _ need to be converted back) we use the field name.
        try {
            sound = (Sound) Sound.class.getField(name).get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            sound = null;
        }

        Preconditions.checkArgument(sound != null, "No sound found with the name %s", name);
        return sound;
    }

    // Paper start - deprecate getKey
    /**
     * @deprecated use {@link Registry#getKey(Keyed)} and {@link Registry#SOUNDS}. Sounds
     * can exist without a key.
     */
    @Deprecated(since = "1.20.5", forRemoval = true)
    @Override
    @NotNull NamespacedKey getKey();
    // Paper end - deprecate getKey

    /**
     * @return an array of all known sounds.
     * @deprecated use {@link Registry#iterator()}.
     */
    @NotNull
    @Deprecated(since = "1.21.3", forRemoval = true) @org.jetbrains.annotations.ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
    static Sound[] values() {
        return Lists.newArrayList(Registry.SOUNDS).toArray(new Sound[0]);
    }

    // Paper start
    /**
     * @deprecated use {@link Registry#getKey(Keyed)} and {@link Registry#SOUNDS}. Sounds
     * can exist without a key.
     */
    @Deprecated(since = "1.20.5", forRemoval = true)
    @Override
    default net.kyori.adventure.key.@NotNull Key key() {
        return this.getKey();
    }
    // Paper end
}
