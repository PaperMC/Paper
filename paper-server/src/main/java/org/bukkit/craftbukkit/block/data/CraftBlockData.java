package org.bukkit.craftbukkit.block.data;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundGroup;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockSupport;
import org.bukkit.block.BlockType;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftSoundGroup;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.block.CraftBlockSupport;
import org.bukkit.craftbukkit.block.CraftBlockType;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.craftbukkit.util.CraftVoxelShape;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class CraftBlockData implements BlockData {

    private BlockState state;
    private List<Property.Value<?>> parsedStates;

    protected CraftBlockData(BlockState state) {
        this.state = state;
    }

    @Override
    public Material getMaterial() {
        return this.state.getBukkitMaterial();
    }

    public BlockState getState() {
        return this.state;
    }

    @Override
    public BlockData merge(BlockData data) {
        CraftBlockData craft = (CraftBlockData) data;
        Preconditions.checkArgument(craft.parsedStates != null, "Block data not created via string parsing");
        Preconditions.checkArgument(this.state.getBlock() == craft.state.getBlock(), "States have different types (got %s, expected %s)", this.state.getBlock(), craft.state.getBlock());

        CraftBlockData clone = (CraftBlockData) this.clone();
        clone.parsedStates = null;

        for (Property.Value<?> parsed : craft.parsedStates) {
            clone.state = setValue(clone.state, parsed);
        }

        return clone;
    }

    private static <T extends Comparable<T>> BlockState setValue(final BlockState state, final Property.Value<T> propertyValue) {
        return state.setValue(propertyValue.property(), propertyValue.value());
    }

    @Override
    public boolean matches(BlockData data) {
        if (!(data instanceof final CraftBlockData craft)) {
            return false;
        }
        if (this.state.getBlock() != craft.state.getBlock()) {
            return false;
        }

        // Fastpath an exact match
        if (this.equals(data)) {
            return true;
        }

        // If that failed, do a merge and check
        if (craft.parsedStates != null) {
            return this.merge(data).equals(this);
        }
        return false;
    }

    private static final ClassValue<Enum<?>[]> ENUM_VALUES = new ClassValue<>() {
        @Override
        protected Enum<?>[] computeValue(@NonNull final Class<?> klass) {
            return (Enum<?>[]) klass.getEnumConstants();
        }
    };

    public static <B extends Enum<B>> B fromVanilla(Enum<?> internal, Class<B> bukkit) {
        final Object rawValue;
        if (internal instanceof Direction direction) {
            rawValue = CraftBlock.notchToBlockFace(direction);
        } else {
            rawValue = ENUM_VALUES.get(bukkit)[internal.ordinal()];
        }
        return bukkit.cast(rawValue);
    }

    public static <M extends Enum<M> & StringRepresentable> M toVanilla(Enum<?> bukkit, Class<M> internalClass) {
        final Object rawValue;
        if (bukkit instanceof BlockFace face) {
            rawValue = CraftBlock.blockFaceToNotch(face);
        } else {
            rawValue = ENUM_VALUES.get(internalClass)[bukkit.ordinal()];
        }
        return internalClass.cast(rawValue);
    }

    protected <A extends Enum<A>> A get(EnumProperty<?> property, Class<A> bukkitClass) {
        return fromVanilla(this.state.getValue(property), bukkitClass);
    }

    protected <M extends Enum<M> & StringRepresentable, A extends Enum<A>> @Unmodifiable Set<A> getValues(EnumProperty<M> property, Class<A> bukkitClass) {
        List<M> values = property.getPossibleValues();
        ImmutableSet.Builder<A> result = ImmutableSet.builderWithExpectedSize(values.size());

        for (M e : values) {
            result.add(fromVanilla(e, bukkitClass));
        }

        return result.build();
    }

    protected <A extends Enum<A>, M extends Enum<M> & StringRepresentable> void set(EnumProperty<M> property, A bukkit) {
        this.parsedStates = null;
        this.state = this.state.setValue(property, toVanilla(bukkit, property.getValueClass()));
    }

    // Straight integer or boolean getter
    protected <T extends Comparable<T>> T get(Property<T> property) {
        return this.state.getValue(property);
    }

    // Straight integer or boolean setter
    public <T extends Comparable<T>, V extends T> void set(Property<T> property, V value) {
        this.parsedStates = null;
        this.state = this.state.setValue(property, value);
    }

    @Override
    public String getAsString() {
        return this.toString(this.state.getValues(), this.state.isSingletonState());
    }

    @Override
    public String getAsString(boolean hideUnspecified) {
        return (hideUnspecified && this.parsedStates != null) ? this.toString(this.parsedStates.stream(), this.parsedStates.isEmpty()) : this.getAsString();
    }

    @Override
    public BlockData clone() {
        try {
            return (BlockData) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError("Clone not supported", ex);
        }
    }

    @Override
    public String toString() {
        return "CraftBlockData{" + this.getAsString() + "}";
    }

    // Mimicked from StateHolder#toString() prefixed by just the key instead of Block{key}
    public String toString(Stream<Property.Value<?>> states, boolean singleton) {
        StringBuilder stateString = new StringBuilder(BuiltInRegistries.BLOCK.getKey(this.state.getBlock()).toString());
        if (!singleton) {
            stateString.append('[');
            stateString.append(states.map(Property.Value::toString).collect(Collectors.joining(",")));
            stateString.append(']');
        }
        return stateString.toString();
    }

    public Map<String, String> toStates(boolean hideUnspecified) {
        Map<String, String> serializedStates = new HashMap<>();
        if (hideUnspecified && this.parsedStates != null) {
            this.parsedStates.forEach(state -> serializedStates.put(state.property().getName(), state.valueName()));
        } else {
            this.state.getValues().forEach(state -> serializedStates.put(state.property().getName(), state.valueName()));
        }
        return serializedStates;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CraftBlockData && this.state.equals(((CraftBlockData) obj).state);
    }

    @Override
    public int hashCode() {
        return this.state.hashCode();
    }

    public static final BlockFace[] ROTATION_CYCLE = {
        BlockFace.SOUTH, BlockFace.SOUTH_SOUTH_WEST, BlockFace.SOUTH_WEST, BlockFace.WEST_SOUTH_WEST,
        BlockFace.WEST, BlockFace.WEST_NORTH_WEST, BlockFace.NORTH_WEST, BlockFace.NORTH_NORTH_WEST,
        BlockFace.NORTH, BlockFace.NORTH_NORTH_EAST, BlockFace.NORTH_EAST, BlockFace.EAST_NORTH_EAST,
        BlockFace.EAST, BlockFace.EAST_SOUTH_EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_SOUTH_EAST
    };

    private static final Map<Class<? extends Block>, Function<BlockState, CraftBlockData>> INSTANCE_CREATOR = new HashMap<>();

    static {
        //<editor-fold desc="CraftBlockData Registration" defaultstate="collapsed">
        // Start generate - CraftBlockData#INSTANCE_CREATOR
        register(net.minecraft.world.level.block.AmethystClusterBlock.class, org.bukkit.craftbukkit.block.impl.CraftAmethystCluster::new);
        register(net.minecraft.world.level.block.AnvilBlock.class, org.bukkit.craftbukkit.block.impl.CraftAnvil::new);
        register(net.minecraft.world.level.block.AttachedStemBlock.class, org.bukkit.craftbukkit.block.impl.CraftAttachedStem::new);
        register(net.minecraft.world.level.block.BambooStalkBlock.class, org.bukkit.craftbukkit.block.impl.CraftBambooStalk::new);
        register(net.minecraft.world.level.block.BannerBlock.class, org.bukkit.craftbukkit.block.impl.CraftBanner::new);
        register(net.minecraft.world.level.block.BarrelBlock.class, org.bukkit.craftbukkit.block.impl.CraftBarrel::new);
        register(net.minecraft.world.level.block.BarrierBlock.class, org.bukkit.craftbukkit.block.impl.CraftBarrier::new);
        register(net.minecraft.world.level.block.BaseCoralFanBlock.class, org.bukkit.craftbukkit.block.impl.CraftBaseCoralFan::new);
        register(net.minecraft.world.level.block.BaseCoralPlantBlock.class, org.bukkit.craftbukkit.block.impl.CraftBaseCoralPlant::new);
        register(net.minecraft.world.level.block.BaseCoralWallFanBlock.class, org.bukkit.craftbukkit.block.impl.CraftBaseCoralWallFan::new);
        register(net.minecraft.world.level.block.BedBlock.class, org.bukkit.craftbukkit.block.impl.CraftBed::new);
        register(net.minecraft.world.level.block.BeehiveBlock.class, org.bukkit.craftbukkit.block.impl.CraftBeehive::new);
        register(net.minecraft.world.level.block.BeetrootBlock.class, org.bukkit.craftbukkit.block.impl.CraftBeetroot::new);
        register(net.minecraft.world.level.block.BellBlock.class, org.bukkit.craftbukkit.block.impl.CraftBell::new);
        register(net.minecraft.world.level.block.BigDripleafBlock.class, org.bukkit.craftbukkit.block.impl.CraftBigDripleaf::new);
        register(net.minecraft.world.level.block.BigDripleafStemBlock.class, org.bukkit.craftbukkit.block.impl.CraftBigDripleafStem::new);
        register(net.minecraft.world.level.block.BlastFurnaceBlock.class, org.bukkit.craftbukkit.block.impl.CraftBlastFurnace::new);
        register(net.minecraft.world.level.block.BrewingStandBlock.class, org.bukkit.craftbukkit.block.impl.CraftBrewingStand::new);
        register(net.minecraft.world.level.block.BrushableBlock.class, org.bukkit.craftbukkit.block.impl.CraftBrushable::new);
        register(net.minecraft.world.level.block.BubbleColumnBlock.class, org.bukkit.craftbukkit.block.impl.CraftBubbleColumn::new);
        register(net.minecraft.world.level.block.ButtonBlock.class, org.bukkit.craftbukkit.block.impl.CraftButton::new);
        register(net.minecraft.world.level.block.CactusBlock.class, org.bukkit.craftbukkit.block.impl.CraftCactus::new);
        register(net.minecraft.world.level.block.CakeBlock.class, org.bukkit.craftbukkit.block.impl.CraftCake::new);
        register(net.minecraft.world.level.block.CalibratedSculkSensorBlock.class, org.bukkit.craftbukkit.block.impl.CraftCalibratedSculkSensor::new);
        register(net.minecraft.world.level.block.CampfireBlock.class, org.bukkit.craftbukkit.block.impl.CraftCampfire::new);
        register(net.minecraft.world.level.block.CandleBlock.class, org.bukkit.craftbukkit.block.impl.CraftCandle::new);
        register(net.minecraft.world.level.block.CandleCakeBlock.class, org.bukkit.craftbukkit.block.impl.CraftCandleCake::new);
        register(net.minecraft.world.level.block.CarrotBlock.class, org.bukkit.craftbukkit.block.impl.CraftCarrot::new);
        register(net.minecraft.world.level.block.CarvedPumpkinBlock.class, org.bukkit.craftbukkit.block.impl.CraftCarvedPumpkin::new);
        register(net.minecraft.world.level.block.CaveVinesBlock.class, org.bukkit.craftbukkit.block.impl.CraftCaveVines::new);
        register(net.minecraft.world.level.block.CaveVinesPlantBlock.class, org.bukkit.craftbukkit.block.impl.CraftCaveVinesPlant::new);
        register(net.minecraft.world.level.block.CeilingHangingSignBlock.class, org.bukkit.craftbukkit.block.impl.CraftCeilingHangingSign::new);
        register(net.minecraft.world.level.block.ChainBlock.class, org.bukkit.craftbukkit.block.impl.CraftChain::new);
        register(net.minecraft.world.level.block.ChestBlock.class, org.bukkit.craftbukkit.block.impl.CraftChest::new);
        register(net.minecraft.world.level.block.ChiseledBookShelfBlock.class, org.bukkit.craftbukkit.block.impl.CraftChiseledBookShelf::new);
        register(net.minecraft.world.level.block.ChorusFlowerBlock.class, org.bukkit.craftbukkit.block.impl.CraftChorusFlower::new);
        register(net.minecraft.world.level.block.ChorusPlantBlock.class, org.bukkit.craftbukkit.block.impl.CraftChorusPlant::new);
        register(net.minecraft.world.level.block.CocoaBlock.class, org.bukkit.craftbukkit.block.impl.CraftCocoa::new);
        register(net.minecraft.world.level.block.CommandBlock.class, org.bukkit.craftbukkit.block.impl.CraftCommandBlock::new);
        register(net.minecraft.world.level.block.ComparatorBlock.class, org.bukkit.craftbukkit.block.impl.CraftComparator::new);
        register(net.minecraft.world.level.block.ComposterBlock.class, org.bukkit.craftbukkit.block.impl.CraftComposter::new);
        register(net.minecraft.world.level.block.ConduitBlock.class, org.bukkit.craftbukkit.block.impl.CraftConduit::new);
        register(net.minecraft.world.level.block.CopperBulbBlock.class, org.bukkit.craftbukkit.block.impl.CraftCopperBulb::new);
        register(net.minecraft.world.level.block.CopperChestBlock.class, org.bukkit.craftbukkit.block.impl.CraftCopperChest::new);
        register(net.minecraft.world.level.block.CopperGolemStatueBlock.class, org.bukkit.craftbukkit.block.impl.CraftCopperGolemStatue::new);
        register(net.minecraft.world.level.block.CoralFanBlock.class, org.bukkit.craftbukkit.block.impl.CraftCoralFan::new);
        register(net.minecraft.world.level.block.CoralPlantBlock.class, org.bukkit.craftbukkit.block.impl.CraftCoralPlant::new);
        register(net.minecraft.world.level.block.CoralWallFanBlock.class, org.bukkit.craftbukkit.block.impl.CraftCoralWallFan::new);
        register(net.minecraft.world.level.block.CrafterBlock.class, org.bukkit.craftbukkit.block.impl.CraftCrafter::new);
        register(net.minecraft.world.level.block.CreakingHeartBlock.class, org.bukkit.craftbukkit.block.impl.CraftCreakingHeart::new);
        register(net.minecraft.world.level.block.CropBlock.class, org.bukkit.craftbukkit.block.impl.CraftCrop::new);
        register(net.minecraft.world.level.block.DaylightDetectorBlock.class, org.bukkit.craftbukkit.block.impl.CraftDaylightDetector::new);
        register(net.minecraft.world.level.block.DecoratedPotBlock.class, org.bukkit.craftbukkit.block.impl.CraftDecoratedPot::new);
        register(net.minecraft.world.level.block.DetectorRailBlock.class, org.bukkit.craftbukkit.block.impl.CraftDetectorRail::new);
        register(net.minecraft.world.level.block.DispenserBlock.class, org.bukkit.craftbukkit.block.impl.CraftDispenser::new);
        register(net.minecraft.world.level.block.DoorBlock.class, org.bukkit.craftbukkit.block.impl.CraftDoor::new);
        register(net.minecraft.world.level.block.DoublePlantBlock.class, org.bukkit.craftbukkit.block.impl.CraftDoublePlant::new);
        register(net.minecraft.world.level.block.DriedGhastBlock.class, org.bukkit.craftbukkit.block.impl.CraftDriedGhast::new);
        register(net.minecraft.world.level.block.DropperBlock.class, org.bukkit.craftbukkit.block.impl.CraftDropper::new);
        register(net.minecraft.world.level.block.EndPortalFrameBlock.class, org.bukkit.craftbukkit.block.impl.CraftEndPortalFrame::new);
        register(net.minecraft.world.level.block.EndRodBlock.class, org.bukkit.craftbukkit.block.impl.CraftEndRod::new);
        register(net.minecraft.world.level.block.EnderChestBlock.class, org.bukkit.craftbukkit.block.impl.CraftEnderChest::new);
        register(net.minecraft.world.level.block.FarmlandBlock.class, org.bukkit.craftbukkit.block.impl.CraftFarmland::new);
        register(net.minecraft.world.level.block.FenceBlock.class, org.bukkit.craftbukkit.block.impl.CraftFence::new);
        register(net.minecraft.world.level.block.FenceGateBlock.class, org.bukkit.craftbukkit.block.impl.CraftFenceGate::new);
        register(net.minecraft.world.level.block.FireBlock.class, org.bukkit.craftbukkit.block.impl.CraftFire::new);
        register(net.minecraft.world.level.block.FlowerBedBlock.class, org.bukkit.craftbukkit.block.impl.CraftFlowerBed::new);
        register(net.minecraft.world.level.block.FrostedIceBlock.class, org.bukkit.craftbukkit.block.impl.CraftFrostedIce::new);
        register(net.minecraft.world.level.block.FurnaceBlock.class, org.bukkit.craftbukkit.block.impl.CraftFurnace::new);
        register(net.minecraft.world.level.block.GlazedTerracottaBlock.class, org.bukkit.craftbukkit.block.impl.CraftGlazedTerracotta::new);
        register(net.minecraft.world.level.block.GlowLichenBlock.class, org.bukkit.craftbukkit.block.impl.CraftGlowLichen::new);
        register(net.minecraft.world.level.block.GrassBlock.class, org.bukkit.craftbukkit.block.impl.CraftGrass::new);
        register(net.minecraft.world.level.block.GrindstoneBlock.class, org.bukkit.craftbukkit.block.impl.CraftGrindstone::new);
        register(net.minecraft.world.level.block.HangingMossBlock.class, org.bukkit.craftbukkit.block.impl.CraftHangingMoss::new);
        register(net.minecraft.world.level.block.HangingRootsBlock.class, org.bukkit.craftbukkit.block.impl.CraftHangingRoots::new);
        register(net.minecraft.world.level.block.HayBlock.class, org.bukkit.craftbukkit.block.impl.CraftHay::new);
        register(net.minecraft.world.level.block.HeavyCoreBlock.class, org.bukkit.craftbukkit.block.impl.CraftHeavyCore::new);
        register(net.minecraft.world.level.block.HopperBlock.class, org.bukkit.craftbukkit.block.impl.CraftHopper::new);
        register(net.minecraft.world.level.block.HugeMushroomBlock.class, org.bukkit.craftbukkit.block.impl.CraftHugeMushroom::new);
        register(net.minecraft.world.level.block.InfestedRotatedPillarBlock.class, org.bukkit.craftbukkit.block.impl.CraftInfestedRotatedPillar::new);
        register(net.minecraft.world.level.block.IronBarsBlock.class, org.bukkit.craftbukkit.block.impl.CraftIronBars::new);
        register(net.minecraft.world.level.block.JigsawBlock.class, org.bukkit.craftbukkit.block.impl.CraftJigsaw::new);
        register(net.minecraft.world.level.block.JukeboxBlock.class, org.bukkit.craftbukkit.block.impl.CraftJukebox::new);
        register(net.minecraft.world.level.block.KelpBlock.class, org.bukkit.craftbukkit.block.impl.CraftKelp::new);
        register(net.minecraft.world.level.block.LadderBlock.class, org.bukkit.craftbukkit.block.impl.CraftLadder::new);
        register(net.minecraft.world.level.block.LanternBlock.class, org.bukkit.craftbukkit.block.impl.CraftLantern::new);
        register(net.minecraft.world.level.block.LayeredCauldronBlock.class, org.bukkit.craftbukkit.block.impl.CraftLayeredCauldron::new);
        register(net.minecraft.world.level.block.LeafLitterBlock.class, org.bukkit.craftbukkit.block.impl.CraftLeafLitter::new);
        register(net.minecraft.world.level.block.LecternBlock.class, org.bukkit.craftbukkit.block.impl.CraftLectern::new);
        register(net.minecraft.world.level.block.LeverBlock.class, org.bukkit.craftbukkit.block.impl.CraftLever::new);
        register(net.minecraft.world.level.block.LightBlock.class, org.bukkit.craftbukkit.block.impl.CraftLight::new);
        register(net.minecraft.world.level.block.LightningRodBlock.class, org.bukkit.craftbukkit.block.impl.CraftLightningRod::new);
        register(net.minecraft.world.level.block.LiquidBlock.class, org.bukkit.craftbukkit.block.impl.CraftLiquid::new);
        register(net.minecraft.world.level.block.LoomBlock.class, org.bukkit.craftbukkit.block.impl.CraftLoom::new);
        register(net.minecraft.world.level.block.MangroveLeavesBlock.class, org.bukkit.craftbukkit.block.impl.CraftMangroveLeaves::new);
        register(net.minecraft.world.level.block.MangrovePropaguleBlock.class, org.bukkit.craftbukkit.block.impl.CraftMangrovePropagule::new);
        register(net.minecraft.world.level.block.MangroveRootsBlock.class, org.bukkit.craftbukkit.block.impl.CraftMangroveRoots::new);
        register(net.minecraft.world.level.block.MossyCarpetBlock.class, org.bukkit.craftbukkit.block.impl.CraftMossyCarpet::new);
        register(net.minecraft.world.level.block.MultifaceBlock.class, org.bukkit.craftbukkit.block.impl.CraftMultiface::new);
        register(net.minecraft.world.level.block.MyceliumBlock.class, org.bukkit.craftbukkit.block.impl.CraftMycelium::new);
        register(net.minecraft.world.level.block.NetherPortalBlock.class, org.bukkit.craftbukkit.block.impl.CraftNetherPortal::new);
        register(net.minecraft.world.level.block.NetherWartBlock.class, org.bukkit.craftbukkit.block.impl.CraftNetherWart::new);
        register(net.minecraft.world.level.block.NoteBlock.class, org.bukkit.craftbukkit.block.impl.CraftNoteBlock::new);
        register(net.minecraft.world.level.block.ObserverBlock.class, org.bukkit.craftbukkit.block.impl.CraftObserver::new);
        register(net.minecraft.world.level.block.PiglinWallSkullBlock.class, org.bukkit.craftbukkit.block.impl.CraftPiglinWallSkull::new);
        register(net.minecraft.world.level.block.PitcherCropBlock.class, org.bukkit.craftbukkit.block.impl.CraftPitcherCrop::new);
        register(net.minecraft.world.level.block.PlayerHeadBlock.class, org.bukkit.craftbukkit.block.impl.CraftPlayerHead::new);
        register(net.minecraft.world.level.block.PlayerWallHeadBlock.class, org.bukkit.craftbukkit.block.impl.CraftPlayerWallHead::new);
        register(net.minecraft.world.level.block.PointedDripstoneBlock.class, org.bukkit.craftbukkit.block.impl.CraftPointedDripstone::new);
        register(net.minecraft.world.level.block.PotatoBlock.class, org.bukkit.craftbukkit.block.impl.CraftPotato::new);
        register(net.minecraft.world.level.block.PoweredRailBlock.class, org.bukkit.craftbukkit.block.impl.CraftPoweredRail::new);
        register(net.minecraft.world.level.block.PressurePlateBlock.class, org.bukkit.craftbukkit.block.impl.CraftPressurePlate::new);
        register(net.minecraft.world.level.block.RailBlock.class, org.bukkit.craftbukkit.block.impl.CraftRail::new);
        register(net.minecraft.world.level.block.RedStoneOreBlock.class, org.bukkit.craftbukkit.block.impl.CraftRedStoneOre::new);
        register(net.minecraft.world.level.block.RedStoneWireBlock.class, org.bukkit.craftbukkit.block.impl.CraftRedStoneWire::new);
        register(net.minecraft.world.level.block.RedstoneLampBlock.class, org.bukkit.craftbukkit.block.impl.CraftRedstoneLamp::new);
        register(net.minecraft.world.level.block.RedstoneTorchBlock.class, org.bukkit.craftbukkit.block.impl.CraftRedstoneTorch::new);
        register(net.minecraft.world.level.block.RedstoneWallTorchBlock.class, org.bukkit.craftbukkit.block.impl.CraftRedstoneWallTorch::new);
        register(net.minecraft.world.level.block.RepeaterBlock.class, org.bukkit.craftbukkit.block.impl.CraftRepeater::new);
        register(net.minecraft.world.level.block.RespawnAnchorBlock.class, org.bukkit.craftbukkit.block.impl.CraftRespawnAnchor::new);
        register(net.minecraft.world.level.block.RotatedPillarBlock.class, org.bukkit.craftbukkit.block.impl.CraftRotatedPillar::new);
        register(net.minecraft.world.level.block.SaplingBlock.class, org.bukkit.craftbukkit.block.impl.CraftSapling::new);
        register(net.minecraft.world.level.block.ScaffoldingBlock.class, org.bukkit.craftbukkit.block.impl.CraftScaffolding::new);
        register(net.minecraft.world.level.block.SculkCatalystBlock.class, org.bukkit.craftbukkit.block.impl.CraftSculkCatalyst::new);
        register(net.minecraft.world.level.block.SculkSensorBlock.class, org.bukkit.craftbukkit.block.impl.CraftSculkSensor::new);
        register(net.minecraft.world.level.block.SculkShriekerBlock.class, org.bukkit.craftbukkit.block.impl.CraftSculkShrieker::new);
        register(net.minecraft.world.level.block.SculkVeinBlock.class, org.bukkit.craftbukkit.block.impl.CraftSculkVein::new);
        register(net.minecraft.world.level.block.SeaPickleBlock.class, org.bukkit.craftbukkit.block.impl.CraftSeaPickle::new);
        register(net.minecraft.world.level.block.ShelfBlock.class, org.bukkit.craftbukkit.block.impl.CraftShelf::new);
        register(net.minecraft.world.level.block.ShulkerBoxBlock.class, org.bukkit.craftbukkit.block.impl.CraftShulkerBox::new);
        register(net.minecraft.world.level.block.SkullBlock.class, org.bukkit.craftbukkit.block.impl.CraftSkull::new);
        register(net.minecraft.world.level.block.SlabBlock.class, org.bukkit.craftbukkit.block.impl.CraftSlab::new);
        register(net.minecraft.world.level.block.SmallDripleafBlock.class, org.bukkit.craftbukkit.block.impl.CraftSmallDripleaf::new);
        register(net.minecraft.world.level.block.SmokerBlock.class, org.bukkit.craftbukkit.block.impl.CraftSmoker::new);
        register(net.minecraft.world.level.block.SnifferEggBlock.class, org.bukkit.craftbukkit.block.impl.CraftSnifferEgg::new);
        register(net.minecraft.world.level.block.SnowLayerBlock.class, org.bukkit.craftbukkit.block.impl.CraftSnowLayer::new);
        register(net.minecraft.world.level.block.SnowyBlock.class, org.bukkit.craftbukkit.block.impl.CraftSnowy::new);
        register(net.minecraft.world.level.block.StainedGlassPaneBlock.class, org.bukkit.craftbukkit.block.impl.CraftStainedGlassPane::new);
        register(net.minecraft.world.level.block.StairBlock.class, org.bukkit.craftbukkit.block.impl.CraftStair::new);
        register(net.minecraft.world.level.block.StandingSignBlock.class, org.bukkit.craftbukkit.block.impl.CraftStandingSign::new);
        register(net.minecraft.world.level.block.StemBlock.class, org.bukkit.craftbukkit.block.impl.CraftStem::new);
        register(net.minecraft.world.level.block.StonecutterBlock.class, org.bukkit.craftbukkit.block.impl.CraftStonecutter::new);
        register(net.minecraft.world.level.block.StructureBlock.class, org.bukkit.craftbukkit.block.impl.CraftStructureBlock::new);
        register(net.minecraft.world.level.block.SugarCaneBlock.class, org.bukkit.craftbukkit.block.impl.CraftSugarCane::new);
        register(net.minecraft.world.level.block.SweetBerryBushBlock.class, org.bukkit.craftbukkit.block.impl.CraftSweetBerryBush::new);
        register(net.minecraft.world.level.block.TallFlowerBlock.class, org.bukkit.craftbukkit.block.impl.CraftTallFlower::new);
        register(net.minecraft.world.level.block.TallSeagrassBlock.class, org.bukkit.craftbukkit.block.impl.CraftTallSeagrass::new);
        register(net.minecraft.world.level.block.TargetBlock.class, org.bukkit.craftbukkit.block.impl.CraftTarget::new);
        register(net.minecraft.world.level.block.TestBlock.class, org.bukkit.craftbukkit.block.impl.CraftTestBlock::new);
        register(net.minecraft.world.level.block.TintedParticleLeavesBlock.class, org.bukkit.craftbukkit.block.impl.CraftTintedParticleLeaves::new);
        register(net.minecraft.world.level.block.TntBlock.class, org.bukkit.craftbukkit.block.impl.CraftTnt::new);
        register(net.minecraft.world.level.block.TorchflowerCropBlock.class, org.bukkit.craftbukkit.block.impl.CraftTorchflowerCrop::new);
        register(net.minecraft.world.level.block.TrapDoorBlock.class, org.bukkit.craftbukkit.block.impl.CraftTrapDoor::new);
        register(net.minecraft.world.level.block.TrappedChestBlock.class, org.bukkit.craftbukkit.block.impl.CraftTrappedChest::new);
        register(net.minecraft.world.level.block.TrialSpawnerBlock.class, org.bukkit.craftbukkit.block.impl.CraftTrialSpawner::new);
        register(net.minecraft.world.level.block.TripWireBlock.class, org.bukkit.craftbukkit.block.impl.CraftTripWire::new);
        register(net.minecraft.world.level.block.TripWireHookBlock.class, org.bukkit.craftbukkit.block.impl.CraftTripWireHook::new);
        register(net.minecraft.world.level.block.TurtleEggBlock.class, org.bukkit.craftbukkit.block.impl.CraftTurtleEgg::new);
        register(net.minecraft.world.level.block.TwistingVinesBlock.class, org.bukkit.craftbukkit.block.impl.CraftTwistingVines::new);
        register(net.minecraft.world.level.block.UntintedParticleLeavesBlock.class, org.bukkit.craftbukkit.block.impl.CraftUntintedParticleLeaves::new);
        register(net.minecraft.world.level.block.VaultBlock.class, org.bukkit.craftbukkit.block.impl.CraftVault::new);
        register(net.minecraft.world.level.block.VineBlock.class, org.bukkit.craftbukkit.block.impl.CraftVine::new);
        register(net.minecraft.world.level.block.WallBannerBlock.class, org.bukkit.craftbukkit.block.impl.CraftWallBanner::new);
        register(net.minecraft.world.level.block.WallBlock.class, org.bukkit.craftbukkit.block.impl.CraftWall::new);
        register(net.minecraft.world.level.block.WallHangingSignBlock.class, org.bukkit.craftbukkit.block.impl.CraftWallHangingSign::new);
        register(net.minecraft.world.level.block.WallSignBlock.class, org.bukkit.craftbukkit.block.impl.CraftWallSign::new);
        register(net.minecraft.world.level.block.WallSkullBlock.class, org.bukkit.craftbukkit.block.impl.CraftWallSkull::new);
        register(net.minecraft.world.level.block.WallTorchBlock.class, org.bukkit.craftbukkit.block.impl.CraftWallTorch::new);
        register(net.minecraft.world.level.block.WaterloggedTransparentBlock.class, org.bukkit.craftbukkit.block.impl.CraftWaterloggedTransparent::new);
        register(net.minecraft.world.level.block.WeatheringCopperBarsBlock.class, org.bukkit.craftbukkit.block.impl.CraftWeatheringCopperBars::new);
        register(net.minecraft.world.level.block.WeatheringCopperBulbBlock.class, org.bukkit.craftbukkit.block.impl.CraftWeatheringCopperBulb::new);
        register(net.minecraft.world.level.block.WeatheringCopperChainBlock.class, org.bukkit.craftbukkit.block.impl.CraftWeatheringCopperChain::new);
        register(net.minecraft.world.level.block.WeatheringCopperChestBlock.class, org.bukkit.craftbukkit.block.impl.CraftWeatheringCopperChest::new);
        register(net.minecraft.world.level.block.WeatheringCopperDoorBlock.class, org.bukkit.craftbukkit.block.impl.CraftWeatheringCopperDoor::new);
        register(net.minecraft.world.level.block.WeatheringCopperGolemStatueBlock.class, org.bukkit.craftbukkit.block.impl.CraftWeatheringCopperGolemStatue::new);
        register(net.minecraft.world.level.block.WeatheringCopperGrateBlock.class, org.bukkit.craftbukkit.block.impl.CraftWeatheringCopperGrate::new);
        register(net.minecraft.world.level.block.WeatheringCopperSlabBlock.class, org.bukkit.craftbukkit.block.impl.CraftWeatheringCopperSlab::new);
        register(net.minecraft.world.level.block.WeatheringCopperStairBlock.class, org.bukkit.craftbukkit.block.impl.CraftWeatheringCopperStair::new);
        register(net.minecraft.world.level.block.WeatheringCopperTrapDoorBlock.class, org.bukkit.craftbukkit.block.impl.CraftWeatheringCopperTrapDoor::new);
        register(net.minecraft.world.level.block.WeatheringLanternBlock.class, org.bukkit.craftbukkit.block.impl.CraftWeatheringLantern::new);
        register(net.minecraft.world.level.block.WeatheringLightningRodBlock.class, org.bukkit.craftbukkit.block.impl.CraftWeatheringLightningRod::new);
        register(net.minecraft.world.level.block.WeepingVinesBlock.class, org.bukkit.craftbukkit.block.impl.CraftWeepingVines::new);
        register(net.minecraft.world.level.block.WeightedPressurePlateBlock.class, org.bukkit.craftbukkit.block.impl.CraftWeightedPressurePlate::new);
        register(net.minecraft.world.level.block.WitherSkullBlock.class, org.bukkit.craftbukkit.block.impl.CraftWitherSkull::new);
        register(net.minecraft.world.level.block.WitherWallSkullBlock.class, org.bukkit.craftbukkit.block.impl.CraftWitherWallSkull::new);
        register(net.minecraft.world.level.block.piston.MovingPistonBlock.class, org.bukkit.craftbukkit.block.impl.CraftMovingPiston::new);
        register(net.minecraft.world.level.block.piston.PistonBaseBlock.class, org.bukkit.craftbukkit.block.impl.CraftPistonBase::new);
        register(net.minecraft.world.level.block.piston.PistonHeadBlock.class, org.bukkit.craftbukkit.block.impl.CraftPistonHead::new);
        // End generate - CraftBlockData#INSTANCE_CREATOR
        //</editor-fold>
    }

    private static void register(Class<? extends Block> blockClass, Function<BlockState, CraftBlockData> newInstance) {
        Preconditions.checkState(INSTANCE_CREATOR.put(blockClass, newInstance) == null, "Duplicate mapping %s->%s", blockClass, newInstance);
    }

    private static final Map<String, CraftBlockData> STRING_DATA_CACHE = new ConcurrentHashMap<>();

    static {
        // cache all the default states at startup, will not cache ones with the custom states inside of the
        // brackets in a different order, though
        reloadCache();
    }

    public static void reloadCache() {
        STRING_DATA_CACHE.clear();
        Block.BLOCK_STATE_REGISTRY.forEach(state -> {
            CraftBlockData data = state.asBlockData();
            STRING_DATA_CACHE.put(data.getAsString(), data);
        });
    }

    public static CraftBlockData newData(@Nullable BlockType blockType, @Nullable String data) {
        StringBuilder prefixedData = new StringBuilder();
        if (blockType != null) {
            prefixedData.append(blockType.key().asString());
        }

        if (data != null) {
            prefixedData.append(data);
        }

        CraftBlockData cached = STRING_DATA_CACHE.computeIfAbsent(prefixedData.toString(), d -> createNewData(null, d));
        return (CraftBlockData) cached.clone();
    }

    private static CraftBlockData createNewData(@Nullable BlockType blockType, @Nullable String serializedData) {
        if (serializedData == null) {
            assert blockType != null; // blockType is expected to be not null if data is not provided
            return CraftBlockType.bukkitToMinecraftNew(blockType).defaultBlockState().asBlockData();
        }

        // blockType provided, force that type in
        if (blockType != null) {
            serializedData = blockType.key().asString() + serializedData;
        }

        final BlockStateParser.BlockResult result;
        try {
            StringReader reader = new StringReader(serializedData);
            result = BlockStateParser.parseForBlock(CraftRegistry.getMinecraftRegistry(Registries.BLOCK), reader, false);
            if (reader.canRead()) {
                throw new IllegalArgumentException("Spurious trailing data: " + reader.getRemaining());
            }
        } catch (CommandSyntaxException ex) {
            throw new IllegalArgumentException("Could not parse data: " + serializedData, ex);
        }

        CraftBlockData data = result.blockState().asBlockData();
        List<Property.Value<?>> parsed = new ArrayList<>(result.properties().size());
        result.properties().forEach((property, value) -> {
            parsed.add(StateHolder.createValue(property, value));
        });

        data.parsedStates = parsed;
        return data;
    }

    static {
        // Initialize cached data for all BlockState instances after registration
        Block.BLOCK_STATE_REGISTRY.forEach(BlockState::asBlockData);
    }

    public static CraftBlockData createData(BlockState state) {
        return INSTANCE_CREATOR.getOrDefault(state.getBlock().getClass(), CraftBlockData::new).apply(state);
    }

    @Override
    public SoundGroup getSoundGroup() {
        return CraftSoundGroup.getSoundGroup(this.state.getSoundType());
    }

    @Override
    public int getLightEmission() {
        return this.state.getLightEmission();
    }

    @Override
    public boolean isOccluding() {
        return this.state.canOcclude();
    }

    @Override
    public boolean requiresCorrectToolForDrops() {
        return this.state.requiresCorrectToolForDrops();
    }

    @Override
    public boolean isPreferredTool(ItemStack tool) {
        Preconditions.checkArgument(tool != null, "tool must not be null");
        return isPreferredTool(this.state, CraftItemStack.asNMSCopy(tool));
    }

    public static boolean isPreferredTool(BlockState state, net.minecraft.world.item.ItemStack item) {
        return !state.requiresCorrectToolForDrops() || item.isCorrectToolForDrops(state);
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.getById(this.state.getPistonPushReaction().ordinal());
    }

    @Override
    public boolean isSupported(org.bukkit.block.Block block) {
        Preconditions.checkArgument(block != null, "block must not be null");

        CraftBlock craftBlock = (CraftBlock) block;
        return this.state.canSurvive(craftBlock.getCraftWorld().getHandle(), craftBlock.getPosition());
    }

    @Override
    public boolean isSupported(Location location) {
        Preconditions.checkArgument(location != null, "location must not be null");

        CraftWorld world = (CraftWorld) location.getWorld();
        Preconditions.checkArgument(world != null, "location must not have a null world");

        BlockPos position = CraftLocation.toBlockPosition(location);
        return this.state.canSurvive(world.getHandle(), position);
    }

    @Override
    public boolean isFaceSturdy(BlockFace face, BlockSupport support) {
        Preconditions.checkArgument(face != null, "face must not be null");
        Preconditions.checkArgument(support != null, "support must not be null");

        return this.state.isFaceSturdy(EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CraftBlock.blockFaceToNotch(face), CraftBlockSupport.toMinecraft(support));
    }

    @Override
    public org.bukkit.util.VoxelShape getCollisionShape(Location location) {
        Preconditions.checkArgument(location != null, "location must not be null");

        CraftWorld world = (CraftWorld) location.getWorld();
        Preconditions.checkArgument(world != null, "location must not have a null world");

        BlockPos position = CraftLocation.toBlockPosition(location);
        VoxelShape shape = this.state.getCollisionShape(world.getHandle(), position);
        return new CraftVoxelShape(shape);
    }

    @Override
    public Color getMapColor() {
        return Color.fromRGB(this.state.getMapColor(null, null).col);
    }

    @Override
    public Material getPlacementMaterial() {
        return CraftItemType.minecraftToBukkit(this.state.getBlock().asItem());
    }

    @Override
    public void rotate(StructureRotation rotation) {
        this.state = this.state.rotate(Rotation.valueOf(rotation.name()));
    }

    @Override
    public void mirror(Mirror mirror) {
        this.state = this.state.mirror(net.minecraft.world.level.block.Mirror.valueOf(mirror.name()));
    }

    @Override
    public void copyTo(BlockData blockData) {
        CraftBlockData other = (CraftBlockData) blockData;
        BlockState otherState = other.state;
        for (Property<?> property : this.state.getBlock().getStateDefinition().getProperties()) {
            if (otherState.hasProperty(property)) {
                otherState = this.copyProperty(this.state, otherState, property);
            }
        }

        other.state = otherState;
    }

    private <T extends Comparable<T>> BlockState copyProperty(BlockState source, BlockState target, Property<T> property) {
        return target.setValue(property, source.getValue(property));
    }

    @NotNull
    @Override
    public org.bukkit.block.BlockState createBlockState() {
        return CraftBlockStates.getBlockState(this.state, null);
    }

    @Override
    public float getDestroySpeed(final ItemStack item, final boolean considerEnchants) {
        net.minecraft.world.item.ItemStack itemStack = CraftItemStack.unwrap(item);
        float speed = itemStack.getDestroySpeed(this.state);
        if (speed > 1.0F && considerEnchants) {
            final Holder<Attribute> attribute = Attributes.MINING_EFFICIENCY;
            // Logic sourced from AttributeInstance#calculateValue
            final double initialBaseValue = attribute.value().getDefaultValue();
            final MutableDouble modifiedBaseValue = new MutableDouble(initialBaseValue);
            final MutableDouble baseValMul = new MutableDouble(1);
            final MutableDouble totalValMul = new MutableDouble(1);

            EnchantmentHelper.forEachModifier(
                itemStack, EquipmentSlot.MAINHAND, (_, attributeModifier) -> {
                    switch (attributeModifier.operation()) {
                        case ADD_VALUE -> modifiedBaseValue.add(attributeModifier.amount());
                        case ADD_MULTIPLIED_BASE -> baseValMul.add(attributeModifier.amount());
                        case ADD_MULTIPLIED_TOTAL -> totalValMul.setValue(totalValMul.doubleValue() * (1.0D + attributeModifier.amount()));
                    }
                }
            );

            final double actualModifier = modifiedBaseValue.doubleValue() * baseValMul.doubleValue() * totalValMul.doubleValue();

            speed += (float) attribute.value().sanitizeValue(actualModifier);
        }
        return speed;
    }

    @Override
    public boolean isRandomlyTicked() {
        return this.state.isRandomlyTicking();
    }

    @Override
    public boolean isReplaceable() {
        return this.state.canBeReplaced();
    }
}
