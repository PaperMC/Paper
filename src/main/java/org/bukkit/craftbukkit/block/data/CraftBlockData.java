package org.bukkit.craftbukkit.block.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.ArgumentBlock;
import net.minecraft.server.Block;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateInteger;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class CraftBlockData implements BlockData {

    private IBlockData state;

    protected CraftBlockData() {
        throw new AssertionError("Template Constructor");
    }

    protected CraftBlockData(IBlockData state) {
        this.state = state;
    }

    @Override
    public Material getMaterial() {
        return CraftMagicNumbers.getMaterial(state.getBlock());
    }

    public IBlockData getState() {
        return state;
    }

    /**
     * Get a given BlockStateEnum's value as its Bukkit counterpart.
     *
     * @param nms the NMS state to convert
     * @param bukkit the Bukkit class
     * @param <B> the type
     * @return the matching Bukkit type
     */
    protected <B extends Enum<B>> B get(BlockStateEnum<?> nms, Class<B> bukkit) {
        return toBukkit(state.get(nms), bukkit);
    }

    /**
     * Convert all values from the given BlockStateEnum to their appropriate
     * Bukkit counterpart.
     *
     * @param nms the NMS state to get values from
     * @param bukkit the bukkit class to convert the values to
     * @param <B> the bukkit class type
     * @return an immutable Set of values in their appropriate Bukkit type
     */
    @SuppressWarnings("unchecked")
    protected <B extends Enum<B>> Set<B> getValues(BlockStateEnum<?> nms, Class<B> bukkit) {
        ImmutableSet.Builder<B> values = ImmutableSet.builder();

        for (Enum<?> e : nms.d()) {
            values.add(toBukkit(e, bukkit));
        }

        return values.build();
    }

    /**
     * Set a given {@link BlockStateEnum} with the matching enum from Bukkit.
     *
     * @param nms the NMS BlockStateEnum to set
     * @param bukkit the matching Bukkit Enum
     * @param <B> the Bukkit type
     * @param <N> the NMS type
     */
    protected <B extends Enum<B>, N extends Enum<N> & INamable> void set(BlockStateEnum<N> nms, Enum<B> bukkit) {
        this.state = this.state.set(nms, toNMS(bukkit, nms.b()));
    }

    private static final Map<Class, BiMap<Enum<?>, Enum<?>>> classMappings = new HashMap<>();

    /**
     * Convert an NMS Enum (usually a BlockStateEnum) to its appropriate Bukkit
     * enum from the given class.
     *
     * @throws IllegalStateException if the Enum could not be converted
     */
    @SuppressWarnings("unchecked")
    private static <B extends Enum<B>> B toBukkit(Enum<?> nms, Class<B> bukkit) {
        Enum<?> converted;
        BiMap<Enum<?>, Enum<?>> nmsToBukkit = classMappings.get(nms.getClass());

        if (nmsToBukkit != null) {
            converted = nmsToBukkit.get(nms);
            if (converted != null) {
                return (B) converted;
            }
        }

        if (nms instanceof EnumDirection) {
            converted = CraftBlock.notchToBlockFace((EnumDirection) nms);
        } else {
            converted = bukkit.getEnumConstants()[nms.ordinal()];
        }

        Preconditions.checkState(converted != null, "Could not convert enum %s->%s", nms, bukkit);

        if (nmsToBukkit == null) {
            nmsToBukkit = HashBiMap.create();
            classMappings.put(nms.getClass(), nmsToBukkit);
        }

        nmsToBukkit.put(nms, converted);

        return (B) converted;
    }

    /**
     * Convert a given Bukkit enum to its matching NMS enum type.
     *
     * @param bukkit the Bukkit enum to convert
     * @param nms the NMS class
     * @return the matching NMS type
     * @throws IllegalStateException if the Enum could not be converted
     */
    @SuppressWarnings("unchecked")
    private static <N extends Enum<N> & INamable> N toNMS(Enum<?> bukkit, Class<N> nms) {
        Enum<?> converted;
        BiMap<Enum<?>, Enum<?>> nmsToBukkit = classMappings.get(nms);

        if (nmsToBukkit != null) {
            converted = nmsToBukkit.inverse().get(bukkit);
            if (converted != null) {
                return (N) converted;
            }
        }

        if (bukkit instanceof BlockFace) {
            converted = CraftBlock.blockFaceToNotch((BlockFace) bukkit);
        } else {
            converted = nms.getEnumConstants()[bukkit.ordinal()];
        }

        Preconditions.checkState(converted != null, "Could not convert enum %s->%s", nms, bukkit);

        if (nmsToBukkit == null) {
            nmsToBukkit = HashBiMap.create();
            classMappings.put(nms, nmsToBukkit);
        }

        nmsToBukkit.put(converted, bukkit);

        return (N) converted;
    }

    /**
     * Get the current value of a given state.
     *
     * @param ibs the state to check
     * @param <T> the type
     * @return the current value of the given state
     */
    protected <T extends Comparable<T>> T get(IBlockState<T> ibs) {
        // Straight integer or boolean getter
        return this.state.get(ibs);
    }

    /**
     * Set the specified state's value.
     *
     * @param ibs the state to set
     * @param v the new value
     * @param <T> the state's type
     * @param <V> the value's type. Must match the state's type.
     */
    public <T extends Comparable<T>, V extends T> void set(IBlockState<T> ibs, V v) {
        // Straight integer or boolean setter
        this.state = this.state.set(ibs, v);
    }

    @Override
    public String getAsString() {
        return state.toString();
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
        return "CraftBlockData{" + state.toString() + "}";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CraftBlockData && state.equals(((CraftBlockData) obj).state);
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    protected static BlockStateBoolean getBoolean(String name) {
        throw new AssertionError("Template Method");
    }

    protected static BlockStateBoolean getBoolean(String name, boolean optional) {
        throw new AssertionError("Template Method");
    }

    protected static BlockStateEnum<?> getEnum(String name) {
        throw new AssertionError("Template Method");
    }

    protected static BlockStateInteger getInteger(String name) {
        throw new AssertionError("Template Method");
    }

    protected static BlockStateBoolean getBoolean(Class<? extends Block> block, String name) {
        return (BlockStateBoolean) getState(block, name, false);
    }

    protected static BlockStateBoolean getBoolean(Class<? extends Block> block, String name, boolean optional) {
        return (BlockStateBoolean) getState(block, name, optional);
    }

    protected static BlockStateEnum<?> getEnum(Class<? extends Block> block, String name) {
        return (BlockStateEnum<?>) getState(block, name, false);
    }

    protected static BlockStateInteger getInteger(Class<? extends Block> block, String name) {
        return (BlockStateInteger) getState(block, name, false);
    }

    /**
     * Get a specified {@link IBlockState} from a given block's class with a
     * given name
     *
     * @param block the class to retrieve the state from
     * @param name the name of the state to retrieve
     * @param optional if the state can be null
     * @return the specified state or null
     * @throws IllegalStateException if the state is null and {@code optional}
     * is false.
     */
    private static IBlockState<?> getState(Class<? extends Block> block, String name, boolean optional) {
        IBlockState<?> state = null;

        for (Block instance : (Iterable<Block>) Block.REGISTRY) { // Eclipse fail
            if (instance.getClass() == block) {
                if (state == null) {
                    state = instance.getStates().a(name);
                } else {
                    IBlockState<?> newState = instance.getStates().a(name);

                    Preconditions.checkState(state == newState, "State mistmatch %s,%s", state, newState);
                }
            }
        }

        Preconditions.checkState(optional || state != null, "Null state for %s,%s", block, name);

        return state;
    }

    /**
     * Get the minimum value allowed by the BlockStateInteger.
     *
     * @param state the state to check
     * @return the minimum value allowed
     */
    protected static int getMin(BlockStateInteger state) {
        return state.min;
    }

    /**
     * Get the maximum value allowed by the BlockStateInteger.
     *
     * @param state the state to check
     * @return the maximum value allowed
     */
    protected static int getMax(BlockStateInteger state) {
        return state.max;
    }

    //
    private static final Map<Class<? extends Block>, Class<? extends CraftBlockData>> MAP = new HashMap<>();

    static {
        register(net.minecraft.server.BlockAnvil.class, org.bukkit.craftbukkit.block.impl.CraftAnvil.class);
        register(net.minecraft.server.BlockBanner.class, org.bukkit.craftbukkit.block.impl.CraftBanner.class);
        register(net.minecraft.server.BlockBannerWall.class, org.bukkit.craftbukkit.block.impl.CraftBannerWall.class);
        register(net.minecraft.server.BlockBed.class, org.bukkit.craftbukkit.block.impl.CraftBed.class);
        register(net.minecraft.server.BlockBeetroot.class, org.bukkit.craftbukkit.block.impl.CraftBeetroot.class);
        register(net.minecraft.server.BlockBrewingStand.class, org.bukkit.craftbukkit.block.impl.CraftBrewingStand.class);
        register(net.minecraft.server.BlockBubbleColumn.class, org.bukkit.craftbukkit.block.impl.CraftBubbleColumn.class);
        register(net.minecraft.server.BlockCactus.class, org.bukkit.craftbukkit.block.impl.CraftCactus.class);
        register(net.minecraft.server.BlockCake.class, org.bukkit.craftbukkit.block.impl.CraftCake.class);
        register(net.minecraft.server.BlockCarrots.class, org.bukkit.craftbukkit.block.impl.CraftCarrots.class);
        register(net.minecraft.server.BlockCauldron.class, org.bukkit.craftbukkit.block.impl.CraftCauldron.class);
        register(net.minecraft.server.BlockChest.class, org.bukkit.craftbukkit.block.impl.CraftChest.class);
        register(net.minecraft.server.BlockChestTrapped.class, org.bukkit.craftbukkit.block.impl.CraftChestTrapped.class);
        register(net.minecraft.server.BlockChorusFlower.class, org.bukkit.craftbukkit.block.impl.CraftChorusFlower.class);
        register(net.minecraft.server.BlockChorusFruit.class, org.bukkit.craftbukkit.block.impl.CraftChorusFruit.class);
        register(net.minecraft.server.BlockCobbleWall.class, org.bukkit.craftbukkit.block.impl.CraftCobbleWall.class);
        register(net.minecraft.server.BlockCocoa.class, org.bukkit.craftbukkit.block.impl.CraftCocoa.class);
        register(net.minecraft.server.BlockCommand.class, org.bukkit.craftbukkit.block.impl.CraftCommand.class);
        register(net.minecraft.server.BlockCoralFan.class, org.bukkit.craftbukkit.block.impl.CraftCoralFan.class);
        register(net.minecraft.server.BlockCoralFanAbstract.class, org.bukkit.craftbukkit.block.impl.CraftCoralFanAbstract.class);
        register(net.minecraft.server.BlockCoralFanWall.class, org.bukkit.craftbukkit.block.impl.CraftCoralFanWall.class);
        register(net.minecraft.server.BlockCoralFanWallAbstract.class, org.bukkit.craftbukkit.block.impl.CraftCoralFanWallAbstract.class);
        register(net.minecraft.server.BlockCrops.class, org.bukkit.craftbukkit.block.impl.CraftCrops.class);
        register(net.minecraft.server.BlockDaylightDetector.class, org.bukkit.craftbukkit.block.impl.CraftDaylightDetector.class);
        register(net.minecraft.server.BlockDirtSnow.class, org.bukkit.craftbukkit.block.impl.CraftDirtSnow.class);
        register(net.minecraft.server.BlockDispenser.class, org.bukkit.craftbukkit.block.impl.CraftDispenser.class);
        register(net.minecraft.server.BlockDoor.class, org.bukkit.craftbukkit.block.impl.CraftDoor.class);
        register(net.minecraft.server.BlockDropper.class, org.bukkit.craftbukkit.block.impl.CraftDropper.class);
        register(net.minecraft.server.BlockEndRod.class, org.bukkit.craftbukkit.block.impl.CraftEndRod.class);
        register(net.minecraft.server.BlockEnderChest.class, org.bukkit.craftbukkit.block.impl.CraftEnderChest.class);
        register(net.minecraft.server.BlockEnderPortalFrame.class, org.bukkit.craftbukkit.block.impl.CraftEnderPortalFrame.class);
        register(net.minecraft.server.BlockFence.class, org.bukkit.craftbukkit.block.impl.CraftFence.class);
        register(net.minecraft.server.BlockFenceGate.class, org.bukkit.craftbukkit.block.impl.CraftFenceGate.class);
        register(net.minecraft.server.BlockFire.class, org.bukkit.craftbukkit.block.impl.CraftFire.class);
        register(net.minecraft.server.BlockFloorSign.class, org.bukkit.craftbukkit.block.impl.CraftFloorSign.class);
        register(net.minecraft.server.BlockFluids.class, org.bukkit.craftbukkit.block.impl.CraftFluids.class);
        register(net.minecraft.server.BlockFurnace.class, org.bukkit.craftbukkit.block.impl.CraftFurnace.class);
        register(net.minecraft.server.BlockGlassPane.class, org.bukkit.craftbukkit.block.impl.CraftGlassPane.class);
        register(net.minecraft.server.BlockGlazedTerracotta.class, org.bukkit.craftbukkit.block.impl.CraftGlazedTerracotta.class);
        register(net.minecraft.server.BlockGrass.class, org.bukkit.craftbukkit.block.impl.CraftGrass.class);
        register(net.minecraft.server.BlockHay.class, org.bukkit.craftbukkit.block.impl.CraftHay.class);
        register(net.minecraft.server.BlockHopper.class, org.bukkit.craftbukkit.block.impl.CraftHopper.class);
        register(net.minecraft.server.BlockHugeMushroom.class, org.bukkit.craftbukkit.block.impl.CraftHugeMushroom.class);
        register(net.minecraft.server.BlockIceFrost.class, org.bukkit.craftbukkit.block.impl.CraftIceFrost.class);
        register(net.minecraft.server.BlockIronBars.class, org.bukkit.craftbukkit.block.impl.CraftIronBars.class);
        register(net.minecraft.server.BlockJukeBox.class, org.bukkit.craftbukkit.block.impl.CraftJukeBox.class);
        register(net.minecraft.server.BlockKelp.class, org.bukkit.craftbukkit.block.impl.CraftKelp.class);
        register(net.minecraft.server.BlockLadder.class, org.bukkit.craftbukkit.block.impl.CraftLadder.class);
        register(net.minecraft.server.BlockLeaves.class, org.bukkit.craftbukkit.block.impl.CraftLeaves.class);
        register(net.minecraft.server.BlockLever.class, org.bukkit.craftbukkit.block.impl.CraftLever.class);
        register(net.minecraft.server.BlockLogAbstract.class, org.bukkit.craftbukkit.block.impl.CraftLogAbstract.class);
        register(net.minecraft.server.BlockMinecartDetector.class, org.bukkit.craftbukkit.block.impl.CraftMinecartDetector.class);
        register(net.minecraft.server.BlockMinecartTrack.class, org.bukkit.craftbukkit.block.impl.CraftMinecartTrack.class);
        register(net.minecraft.server.BlockMycel.class, org.bukkit.craftbukkit.block.impl.CraftMycel.class);
        register(net.minecraft.server.BlockNetherWart.class, org.bukkit.craftbukkit.block.impl.CraftNetherWart.class);
        register(net.minecraft.server.BlockNote.class, org.bukkit.craftbukkit.block.impl.CraftNote.class);
        register(net.minecraft.server.BlockObserver.class, org.bukkit.craftbukkit.block.impl.CraftObserver.class);
        register(net.minecraft.server.BlockPiston.class, org.bukkit.craftbukkit.block.impl.CraftPiston.class);
        register(net.minecraft.server.BlockPistonExtension.class, org.bukkit.craftbukkit.block.impl.CraftPistonExtension.class);
        register(net.minecraft.server.BlockPistonMoving.class, org.bukkit.craftbukkit.block.impl.CraftPistonMoving.class);
        register(net.minecraft.server.BlockPortal.class, org.bukkit.craftbukkit.block.impl.CraftPortal.class);
        register(net.minecraft.server.BlockPotatoes.class, org.bukkit.craftbukkit.block.impl.CraftPotatoes.class);
        register(net.minecraft.server.BlockPoweredRail.class, org.bukkit.craftbukkit.block.impl.CraftPoweredRail.class);
        register(net.minecraft.server.BlockPressurePlateBinary.class, org.bukkit.craftbukkit.block.impl.CraftPressurePlateBinary.class);
        register(net.minecraft.server.BlockPressurePlateWeighted.class, org.bukkit.craftbukkit.block.impl.CraftPressurePlateWeighted.class);
        register(net.minecraft.server.BlockPumpkinCarved.class, org.bukkit.craftbukkit.block.impl.CraftPumpkinCarved.class);
        register(net.minecraft.server.BlockRedstoneComparator.class, org.bukkit.craftbukkit.block.impl.CraftRedstoneComparator.class);
        register(net.minecraft.server.BlockRedstoneLamp.class, org.bukkit.craftbukkit.block.impl.CraftRedstoneLamp.class);
        register(net.minecraft.server.BlockRedstoneOre.class, org.bukkit.craftbukkit.block.impl.CraftRedstoneOre.class);
        register(net.minecraft.server.BlockRedstoneTorch.class, org.bukkit.craftbukkit.block.impl.CraftRedstoneTorch.class);
        register(net.minecraft.server.BlockRedstoneTorchWall.class, org.bukkit.craftbukkit.block.impl.CraftRedstoneTorchWall.class);
        register(net.minecraft.server.BlockRedstoneWire.class, org.bukkit.craftbukkit.block.impl.CraftRedstoneWire.class);
        register(net.minecraft.server.BlockReed.class, org.bukkit.craftbukkit.block.impl.CraftReed.class);
        register(net.minecraft.server.BlockRepeater.class, org.bukkit.craftbukkit.block.impl.CraftRepeater.class);
        register(net.minecraft.server.BlockRotatable.class, org.bukkit.craftbukkit.block.impl.CraftRotatable.class);
        register(net.minecraft.server.BlockSapling.class, org.bukkit.craftbukkit.block.impl.CraftSapling.class);
        register(net.minecraft.server.BlockSeaPickle.class, org.bukkit.craftbukkit.block.impl.CraftSeaPickle.class);
        register(net.minecraft.server.BlockShulkerBox.class, org.bukkit.craftbukkit.block.impl.CraftShulkerBox.class);
        register(net.minecraft.server.BlockSkull.class, org.bukkit.craftbukkit.block.impl.CraftSkull.class);
        register(net.minecraft.server.BlockSkullPlayer.class, org.bukkit.craftbukkit.block.impl.CraftSkullPlayer.class);
        register(net.minecraft.server.BlockSkullPlayerWall.class, org.bukkit.craftbukkit.block.impl.CraftSkullPlayerWall.class);
        register(net.minecraft.server.BlockSkullWall.class, org.bukkit.craftbukkit.block.impl.CraftSkullWall.class);
        register(net.minecraft.server.BlockSnow.class, org.bukkit.craftbukkit.block.impl.CraftSnow.class);
        register(net.minecraft.server.BlockSoil.class, org.bukkit.craftbukkit.block.impl.CraftSoil.class);
        register(net.minecraft.server.BlockStainedGlassPane.class, org.bukkit.craftbukkit.block.impl.CraftStainedGlassPane.class);
        register(net.minecraft.server.BlockStairs.class, org.bukkit.craftbukkit.block.impl.CraftStairs.class);
        register(net.minecraft.server.BlockStem.class, org.bukkit.craftbukkit.block.impl.CraftStem.class);
        register(net.minecraft.server.BlockStemAttached.class, org.bukkit.craftbukkit.block.impl.CraftStemAttached.class);
        register(net.minecraft.server.BlockStepAbstract.class, org.bukkit.craftbukkit.block.impl.CraftStepAbstract.class);
        register(net.minecraft.server.BlockStoneButton.class, org.bukkit.craftbukkit.block.impl.CraftStoneButton.class);
        register(net.minecraft.server.BlockStructure.class, org.bukkit.craftbukkit.block.impl.CraftStructure.class);
        register(net.minecraft.server.BlockTallPlantFlower.class, org.bukkit.craftbukkit.block.impl.CraftTallPlantFlower.class);
        register(net.minecraft.server.BlockTallPlantShearable.class, org.bukkit.craftbukkit.block.impl.CraftTallPlantShearable.class);
        register(net.minecraft.server.BlockTallSeaGrass.class, org.bukkit.craftbukkit.block.impl.CraftTallSeaGrass.class);
        register(net.minecraft.server.BlockTorchWall.class, org.bukkit.craftbukkit.block.impl.CraftTorchWall.class);
        register(net.minecraft.server.BlockTrapdoor.class, org.bukkit.craftbukkit.block.impl.CraftTrapdoor.class);
        register(net.minecraft.server.BlockTripwire.class, org.bukkit.craftbukkit.block.impl.CraftTripwire.class);
        register(net.minecraft.server.BlockTripwireHook.class, org.bukkit.craftbukkit.block.impl.CraftTripwireHook.class);
        register(net.minecraft.server.BlockTurtleEgg.class, org.bukkit.craftbukkit.block.impl.CraftTurtleEgg.class);
        register(net.minecraft.server.BlockVine.class, org.bukkit.craftbukkit.block.impl.CraftVine.class);
        register(net.minecraft.server.BlockWallSign.class, org.bukkit.craftbukkit.block.impl.CraftWallSign.class);
        register(net.minecraft.server.BlockWitherSkull.class, org.bukkit.craftbukkit.block.impl.CraftWitherSkull.class);
        register(net.minecraft.server.BlockWitherSkullWall.class, org.bukkit.craftbukkit.block.impl.CraftWitherSkullWall.class);
        register(net.minecraft.server.BlockWoodButton.class, org.bukkit.craftbukkit.block.impl.CraftWoodButton.class);
    }

    private static void register(Class<? extends Block> nms, Class<? extends CraftBlockData> bukkit) {
        Preconditions.checkState(MAP.put(nms, bukkit) == null, "Duplicate mapping %s->%s", nms, bukkit);
    }

    public static CraftBlockData newData(Material material, String data) {
        IBlockData blockData;
        Block block = CraftMagicNumbers.getBlock(material);

        // Data provided, use it
        if (data != null) {
            try {
                // Material provided, force that material in
                if (block != null) {
                    data = Block.REGISTRY.b(block) + data;
                }

                StringReader reader = new StringReader(data);
                ArgumentBlock arg = new ArgumentBlock(reader, false).a(false);
                Preconditions.checkArgument(!reader.canRead(), "Spurious trailing data");

                blockData = arg.b();
            } catch (CommandSyntaxException ex) {
                throw new IllegalArgumentException("Could not parse data: " + data, ex);
            }
        } else {
            blockData = block.getBlockData();
        }

        return fromData(blockData);
    }

    public static CraftBlockData fromData(IBlockData data) {
        Class<? extends CraftBlockData> craft = MAP.get(data.getBlock().getClass());
        if (craft == null) {
            craft = CraftBlockData.class;
        }

        CraftBlockData ret;
        try {
            ret = craft.getDeclaredConstructor(IBlockData.class).newInstance(data);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }

        return ret;
    }
}
