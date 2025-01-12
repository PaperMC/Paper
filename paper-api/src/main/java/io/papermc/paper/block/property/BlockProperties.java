package io.papermc.paper.block.property;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.bukkit.Axis;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Orientation;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.SideChaining;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Bell;
import org.bukkit.block.data.type.BigDripleaf;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.block.data.type.CopperGolemStatue;
import org.bukkit.block.data.type.CreakingHeart;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.PointedDripstone;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.block.data.type.SculkSensor;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.StructureBlock;
import org.bukkit.block.data.type.TechnicalPiston;
import org.bukkit.block.data.type.TestBlock;
import org.bukkit.block.data.type.TrialSpawner;
import org.bukkit.block.data.type.Vault;
import org.bukkit.block.data.type.Wall;

/**
 * All block properties applicable to {@link BlockPropertyHolder}s.
 */
public final class BlockProperties {

    static final Multimap<String, BlockProperty<?>> PROPERTIES = HashMultimap.create();

    // Start generate - BlockProperties
    public static final BooleanBlockProperty ATTACHED = bool("attached");
    public static final BooleanBlockProperty BERRIES = bool("berries");
    public static final BooleanBlockProperty BLOOM = bool("bloom");
    public static final BooleanBlockProperty BOTTOM = bool("bottom");
    public static final BooleanBlockProperty CAN_SUMMON = bool("can_summon");
    public static final BooleanBlockProperty CONDITIONAL = bool("conditional");
    public static final BooleanBlockProperty CRACKED = bool("cracked");
    public static final BooleanBlockProperty CRAFTING = bool("crafting");
    public static final BooleanBlockProperty DISARMED = bool("disarmed");
    public static final BooleanBlockProperty DOWN = bool("down");
    public static final BooleanBlockProperty DRAG = bool("drag");
    public static final BooleanBlockProperty EAST = bool("east");
    public static final BooleanBlockProperty ENABLED = bool("enabled");
    public static final BooleanBlockProperty EXTENDED = bool("extended");
    public static final BooleanBlockProperty EYE = bool("eye");
    public static final BooleanBlockProperty FALLING = bool("falling");
    public static final BooleanBlockProperty HANGING = bool("hanging");
    public static final BooleanBlockProperty HAS_BOOK = bool("has_book");
    public static final BooleanBlockProperty HAS_BOTTLE_0 = bool("has_bottle_0");
    public static final BooleanBlockProperty HAS_BOTTLE_1 = bool("has_bottle_1");
    public static final BooleanBlockProperty HAS_BOTTLE_2 = bool("has_bottle_2");
    public static final BooleanBlockProperty HAS_RECORD = bool("has_record");
    public static final BooleanBlockProperty INVERTED = bool("inverted");
    public static final BooleanBlockProperty IN_WALL = bool("in_wall");
    public static final BooleanBlockProperty LIT = bool("lit");
    public static final BooleanBlockProperty LOCKED = bool("locked");
    public static final BooleanBlockProperty MAP = bool("map");
    public static final BooleanBlockProperty NATURAL = bool("natural");
    public static final BooleanBlockProperty NORTH = bool("north");
    public static final BooleanBlockProperty OCCUPIED = bool("occupied");
    public static final BooleanBlockProperty OMINOUS = bool("ominous");
    public static final BooleanBlockProperty OPEN = bool("open");
    public static final BooleanBlockProperty PERSISTENT = bool("persistent");
    public static final BooleanBlockProperty POWERED = bool("powered");
    public static final BooleanBlockProperty SHORT = bool("short");
    public static final BooleanBlockProperty SHRIEKING = bool("shrieking");
    public static final BooleanBlockProperty SIGNAL_FIRE = bool("signal_fire");
    public static final BooleanBlockProperty SLOT_0_OCCUPIED = bool("slot_0_occupied");
    public static final BooleanBlockProperty SLOT_1_OCCUPIED = bool("slot_1_occupied");
    public static final BooleanBlockProperty SLOT_2_OCCUPIED = bool("slot_2_occupied");
    public static final BooleanBlockProperty SLOT_3_OCCUPIED = bool("slot_3_occupied");
    public static final BooleanBlockProperty SLOT_4_OCCUPIED = bool("slot_4_occupied");
    public static final BooleanBlockProperty SLOT_5_OCCUPIED = bool("slot_5_occupied");
    public static final BooleanBlockProperty SNOWY = bool("snowy");
    public static final BooleanBlockProperty SOUTH = bool("south");
    public static final BooleanBlockProperty TIP = bool("tip");
    public static final BooleanBlockProperty TRIGGERED = bool("triggered");
    public static final BooleanBlockProperty UNSTABLE = bool("unstable");
    public static final BooleanBlockProperty UP = bool("up");
    public static final BooleanBlockProperty WATERLOGGED = bool("waterlogged");
    public static final BooleanBlockProperty WEST = bool("west");
    public static final EnumBlockProperty<FaceAttachable.AttachedFace> ATTACH_FACE = enumeration("face", FaceAttachable.AttachedFace.class);
    public static final EnumBlockProperty<Axis> AXIS = enumeration("axis", Axis.class);
    public static final EnumBlockProperty<Bamboo.Leaves> BAMBOO_LEAVES = enumeration("leaves", Bamboo.Leaves.class);
    public static final EnumBlockProperty<Bed.Part> BED_PART = enumeration("part", Bed.Part.class);
    public static final EnumBlockProperty<Bell.Attachment> BELL_ATTACHMENT = enumeration("attachment", Bell.Attachment.class);
    public static final EnumBlockProperty<Chest.Type> CHEST_TYPE = enumeration("type", Chest.Type.class);
    public static final EnumBlockProperty<CopperGolemStatue.Pose> COPPER_GOLEM_POSE = enumeration("copper_golem_pose", CopperGolemStatue.Pose.class);
    public static final EnumBlockProperty<CreakingHeart.State> CREAKING_HEART_STATE = enumeration("creaking_heart_state", CreakingHeart.State.class);
    public static final EnumBlockProperty<Door.Hinge> DOOR_HINGE = enumeration("hinge", Door.Hinge.class);
    public static final EnumBlockProperty<Bisected.Half> DOUBLE_BLOCK_HALF = enumeration("half", Bisected.Half.class);
    public static final EnumBlockProperty<PointedDripstone.Thickness> DRIPSTONE_THICKNESS = enumeration("thickness", PointedDripstone.Thickness.class);
    public static final EnumBlockProperty<RedstoneWire.Connection> EAST_REDSTONE = enumeration("east", RedstoneWire.Connection.class);
    public static final EnumBlockProperty<Wall.Height> EAST_WALL = enumeration("east", Wall.Height.class);
    public static final EnumBlockProperty<BlockFace> FACING = enumeration("facing", BlockFace.class, BlockFace::isCartesian);
    public static final EnumBlockProperty<BlockFace> FACING_HOPPER = enumeration("facing", BlockFace.class, ((Predicate<BlockFace>) BlockFace::isCartesian).and(face -> face != BlockFace.UP));
    public static final EnumBlockProperty<Bisected.Half> HALF = enumeration("half", Bisected.Half.class);
    public static final EnumBlockProperty<Axis> HORIZONTAL_AXIS = enumeration("axis", Axis.class, Axis::isHorizontal);
    public static final EnumBlockProperty<BlockFace> HORIZONTAL_FACING = enumeration("facing", BlockFace.class, BlockFace::isCardinal);
    public static final EnumBlockProperty<Comparator.Mode> MODE_COMPARATOR = enumeration("mode", Comparator.Mode.class);
    public static final EnumBlockProperty<RedstoneWire.Connection> NORTH_REDSTONE = enumeration("north", RedstoneWire.Connection.class);
    public static final EnumBlockProperty<Wall.Height> NORTH_WALL = enumeration("north", Wall.Height.class);
    public static final EnumBlockProperty<Instrument> NOTEBLOCK_INSTRUMENT = enumeration("instrument", Instrument.class);
    public static final EnumBlockProperty<Orientation> ORIENTATION = enumeration("orientation", Orientation.class);
    public static final EnumBlockProperty<TechnicalPiston.Type> PISTON_TYPE = enumeration("type", TechnicalPiston.Type.class);
    public static final EnumBlockProperty<Rail.Shape> RAIL_SHAPE = enumeration("shape", Rail.Shape.class);
    public static final EnumBlockProperty<Rail.Shape> RAIL_SHAPE_STRAIGHT = enumeration("shape", Rail.Shape.class, Rail.Shape::isStraight);
    public static final EnumBlockProperty<SculkSensor.Phase> SCULK_SENSOR_PHASE = enumeration("sculk_sensor_phase", SculkSensor.Phase.class);
    public static final EnumBlockProperty<SideChaining.ChainPart> SIDE_CHAIN_PART = enumeration("side_chain", SideChaining.ChainPart.class);
    public static final EnumBlockProperty<Slab.Type> SLAB_TYPE = enumeration("type", Slab.Type.class);
    public static final EnumBlockProperty<RedstoneWire.Connection> SOUTH_REDSTONE = enumeration("south", RedstoneWire.Connection.class);
    public static final EnumBlockProperty<Wall.Height> SOUTH_WALL = enumeration("south", Wall.Height.class);
    public static final EnumBlockProperty<Stairs.Shape> STAIRS_SHAPE = enumeration("shape", Stairs.Shape.class);
    public static final EnumBlockProperty<StructureBlock.Mode> STRUCTUREBLOCK_MODE = enumeration("mode", StructureBlock.Mode.class);
    public static final EnumBlockProperty<TestBlock.Mode> TEST_BLOCK_MODE = enumeration("mode", TestBlock.Mode.class);
    public static final EnumBlockProperty<BigDripleaf.Tilt> TILT = enumeration("tilt", BigDripleaf.Tilt.class);
    public static final EnumBlockProperty<TrialSpawner.State> TRIAL_SPAWNER_STATE = enumeration("trial_spawner_state", TrialSpawner.State.class);
    public static final EnumBlockProperty<Vault.State> VAULT_STATE = enumeration("vault_state", Vault.State.class);
    public static final EnumBlockProperty<BlockFace> VERTICAL_DIRECTION = enumeration("vertical_direction", BlockFace.class, face -> face.getModY() != 0);
    public static final EnumBlockProperty<RedstoneWire.Connection> WEST_REDSTONE = enumeration("west", RedstoneWire.Connection.class);
    public static final EnumBlockProperty<Wall.Height> WEST_WALL = enumeration("west", Wall.Height.class);
    public static final IntegerBlockProperty AGE_1 = integer("age", 0, 1);
    public static final IntegerBlockProperty AGE_15 = integer("age", 0, 15);
    public static final IntegerBlockProperty AGE_2 = integer("age", 0, 2);
    public static final IntegerBlockProperty AGE_25 = integer("age", 0, 25);
    public static final IntegerBlockProperty AGE_3 = integer("age", 0, 3);
    public static final IntegerBlockProperty AGE_4 = integer("age", 0, 4);
    public static final IntegerBlockProperty AGE_5 = integer("age", 0, 5);
    public static final IntegerBlockProperty AGE_7 = integer("age", 0, 7);
    public static final IntegerBlockProperty BITES = integer("bites", 0, 6);
    public static final IntegerBlockProperty CANDLES = integer("candles", 1, 4);
    public static final IntegerBlockProperty DELAY = integer("delay", 1, 4);
    public static final IntegerBlockProperty DISTANCE = integer("distance", 1, 7);
    public static final IntegerBlockProperty DRIED_GHAST_HYDRATION_LEVELS = integer("hydration", 0, 3);
    public static final IntegerBlockProperty DUSTED = integer("dusted", 0, 3);
    public static final IntegerBlockProperty EGGS = integer("eggs", 1, 4);
    public static final IntegerBlockProperty FLOWER_AMOUNT = integer("flower_amount", 1, 4);
    public static final IntegerBlockProperty HATCH = integer("hatch", 0, 2);
    public static final IntegerBlockProperty LAYERS = integer("layers", 1, 8);
    public static final IntegerBlockProperty LEVEL = integer("level", 0, 15);
    public static final IntegerBlockProperty LEVEL_CAULDRON = integer("level", 1, 3);
    public static final IntegerBlockProperty LEVEL_COMPOSTER = integer("level", 0, 8);
    public static final IntegerBlockProperty LEVEL_FLOWING = integer("level", 1, 8);
    public static final IntegerBlockProperty LEVEL_HONEY = integer("honey_level", 0, 5);
    public static final IntegerBlockProperty MOISTURE = integer("moisture", 0, 7);
    public static final BlockProperty<Note> NOTE = register(new NoteBlockProperty("note"));
    public static final IntegerBlockProperty PICKLES = integer("pickles", 1, 4);
    public static final IntegerBlockProperty POWER = integer("power", 0, 15);
    public static final IntegerBlockProperty RESPAWN_ANCHOR_CHARGES = integer("charges", 0, 4);
    public static final EnumBlockProperty<BlockFace> ROTATION_16 = register(new RotationBlockProperty("rotation"));
    public static final IntegerBlockProperty SEGMENT_AMOUNT = integer("segment_amount", 1, 4);
    public static final IntegerBlockProperty STABILITY_DISTANCE = integer("distance", 0, 7);
    public static final IntegerBlockProperty STAGE = integer("stage", 0, 1);
    // End generate - BlockProperties

    private BlockProperties() {
    }

    //<editor-fold defaultstate="collapsed" desc="static factory methods">
    private static IntegerBlockProperty integer(final String name, final int min, final int max) {
        return register(new IntegerBlockPropertyImpl(name, min, max));
    }

    private static BooleanBlockProperty bool(final String name) {
        return register(new BooleanBlockPropertyImpl(name));
    }

    private static <E extends Enum<E>> EnumBlockProperty<E> enumeration(final String name, final Class<E> enumClass) {
        return enumeration(name, enumClass, enumClass.getEnumConstants());
    }

    @SuppressWarnings("SameParameterValue")
    @SafeVarargs
    private static <E extends Enum<E>> EnumBlockProperty<E> enumeration(final String name, final Class<E> enumClass, final E... values) {
        return register(new EnumBlockPropertyImpl<>(name, enumClass, Set.of(values)));
    }

    private static <E extends Enum<E>> EnumBlockProperty<E> enumeration(final String name, final Class<E> enumClass, final Predicate<E> test) {
        return register(new EnumBlockPropertyImpl<>(name, enumClass, Arrays.stream(enumClass.getEnumConstants()).filter(test).collect(Collectors.toSet())));
    }

    private static <V extends Comparable<V>, P extends BlockProperty<V>> P register(final P property) {
        PROPERTIES.put(property.name(), property);
        return property;
    }
    //</editor-fold>
}
