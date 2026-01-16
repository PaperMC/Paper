package io.papermc.generator.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Either;
import io.papermc.generator.types.craftblockdata.property.holder.VirtualField;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.BigDripleafStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.CopperGolemStatueBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.TestBlock;
import net.minecraft.world.level.block.TestInstanceBlock;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
import net.minecraft.world.level.block.entity.vault.VaultState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import net.minecraft.world.level.block.state.properties.CreakingHeartState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.level.block.state.properties.SideChainPart;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.block.state.properties.TestBlockMode;
import net.minecraft.world.level.block.state.properties.Tilt;
import net.minecraft.world.level.block.state.properties.WallSide;
import org.bukkit.Axis;
import org.bukkit.Instrument;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Orientation;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Brushable;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.Hangable;
import org.bukkit.block.data.Hatchable;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.Segmentable;
import org.bukkit.block.data.SideChaining;
import org.bukkit.block.data.Snowable;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Bell;
import org.bukkit.block.data.type.BigDripleaf;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.block.data.type.CopperGolemStatue;
import org.bukkit.block.data.type.CreakingHeart;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Dripleaf;
import org.bukkit.block.data.type.Fence;
import org.bukkit.block.data.type.Furnace;
import org.bukkit.block.data.type.PointedDripstone;
import org.bukkit.block.data.type.RedstoneRail;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.block.data.type.ResinClump;
import org.bukkit.block.data.type.SculkSensor;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.Switch;
import org.bukkit.block.data.type.TechnicalPiston;
import org.bukkit.block.data.type.TrialSpawner;
import org.bukkit.block.data.type.Vault;
import org.bukkit.block.data.type.Wall;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class BlockStateMapping {

    public record BlockData(String implName, @Nullable Class<? extends org.bukkit.block.data.BlockData> api,
                            Collection<? extends Property<?>> properties, Map<Property<?>, Field> propertyFields,
                            Multimap<Either<Field, VirtualField>, Property<?>> complexPropertyFields) {
    }

    private static final Map<String, String> API_RENAMES = ImmutableMap.<String, String>builder()
        .put("SnowLayer", "Snow")
        .put("StainedGlassPane", "GlassPane") // weird that this one implements glass pane but not the regular glass pane
        .put("CeilingHangingSign", "HangingSign")
        .put("RedStoneWire", "RedstoneWire")
        .put("TripWire", "Tripwire")
        .put("TripWireHook", "TripwireHook")
        .put("Tnt", "TNT")
        .put("BambooStalk", "Bamboo")
        .put("Farm", "Farmland")
        .put("ChiseledBookShelf", "ChiseledBookshelf")
        .put("UntintedParticleLeaves", "Leaves")
        .put("TintedParticleLeaves", "Leaves")
        .put("StandingSign", "Sign")
        .put("FenceGate", "Gate")
        .buildOrThrow();

    private static final Set<Class<? extends Block>> BLOCK_SUFFIX_INTENDED = Set.of(
        CommandBlock.class,
        StructureBlock.class,
        NoteBlock.class,
        TestBlock.class,
        TestInstanceBlock.class
    );

    // virtual data that doesn't exist as constant in the source but still organized this way in the api
    public static final ImmutableMultimap<Class<?>, VirtualField> VIRTUAL_NODES = ImmutableMultimap.<Class<?>, VirtualField>builder()
        .build();

    public static final Map<Property<?>, Field> FALLBACK_GENERIC_FIELDS;

    static {
        Map<Property<?>, Field> fallbackGenericFields = new HashMap<>();
        fetchProperties(BlockStateProperties.class, (field, property) -> fallbackGenericFields.put(property, field), null);
        FALLBACK_GENERIC_FIELDS = Collections.unmodifiableMap(fallbackGenericFields);
    }

    public static final Map<Class<? extends Block>, BlockData> MAPPING;

    static {
        Map<Class<? extends Block>, Collection<Property<?>>> specialBlocks = new IdentityHashMap<>();
        for (Block block : BuiltInRegistries.BLOCK) {
            if (!block.getStateDefinition().getProperties().isEmpty()) {
                specialBlocks.put(block.getClass(), block.getStateDefinition().getProperties());
            }
        }

        Map<Class<? extends Block>, BlockData> map = new IdentityHashMap<>();
        for (Map.Entry<Class<? extends Block>, Collection<Property<?>>> entry : specialBlocks.entrySet()) {
            Class<? extends Block> specialBlock = entry.getKey();

            Collection<Property<?>> properties = new ArrayList<>(entry.getValue());

            Map<Property<?>, Field> propertyFields = new HashMap<>(properties.size());
            Multimap<Either<Field, VirtualField>, Property<?>> complexPropertyFields = ArrayListMultimap.create();

            fetchProperties(specialBlock, (field, property) -> {
                if (properties.contains(property)) {
                    propertyFields.put(property, field);
                }
            }, (field, property) -> {
                if (properties.remove(property)) { // handle those separately and only count if the property was in the state definition
                    complexPropertyFields.put(Either.left(field), property);
                }
            });

            // virtual nodes
            if (VIRTUAL_NODES.containsKey(specialBlock)) {
                for (VirtualField virtualField : VIRTUAL_NODES.get(specialBlock)) {
                    for (Property<?> property : virtualField.values()) {
                        if (properties.remove(property)) {
                            complexPropertyFields.put(Either.right(virtualField), property);
                        } else {
                            throw new IllegalStateException("Unhandled virtual node " + virtualField.name() + " for " + property + " in " + specialBlock.getCanonicalName());
                        }
                    }
                }
            }

            String apiName = formatApiName(specialBlock);
            String implName = "Craft".concat(apiName); // before renames

            apiName = Formatting.stripWordOfCamelCaseName(apiName, "Base", true);
            apiName = API_RENAMES.getOrDefault(apiName, apiName);

            Class<? extends org.bukkit.block.data.BlockData> api = ClassHelper.classOr("org.bukkit.block.data.type." + apiName, null);
            if (api == null) {
                Class<?> parent = specialBlock.getSuperclass();
                while (parent != Object.class && api == null) {
                    if (specialBlocks.containsKey(parent)) {
                        // Consider any parents with matching properties
                        if (specialBlocks.get(parent).equals(entry.getValue())) {
                            String parentApiName = formatApiName(parent);
                            parentApiName = Formatting.stripWordOfCamelCaseName(parentApiName, "Base", true);
                            parentApiName = API_RENAMES.getOrDefault(parentApiName, parentApiName);
                            api = ClassHelper.classOr("org.bukkit.block.data.type." + parentApiName, api);
                        }
                    }
                    parent = parent.getSuperclass();
                }
            }
            if (api == null) { // todo remove this part
                if (AbstractFurnaceBlock.class.isAssignableFrom(specialBlock)) {
                    api = Furnace.class; // for smoker and blast furnace
                } else if (specialBlock == BigDripleafStemBlock.class) {
                    api = Dripleaf.class;
                } else if (IronBarsBlock.class.isAssignableFrom(specialBlock)) {
                    api = Fence.class; // for glass pane (regular) and iron/copper bars
                } else if (specialBlock == MultifaceBlock.class) {
                    api = ResinClump.class;
                }
            }

            map.put(specialBlock, new BlockData(implName, api, properties, propertyFields, complexPropertyFields));
        }
        MAPPING = Collections.unmodifiableMap(map);
    }

    private static final Map<String, Class<? extends org.bukkit.block.data.BlockData>> NAME_TO_DATA = Map.of(
        BlockStateProperties.AGE_1.getName(), Ageable.class,
        BlockStateProperties.LEVEL.getName(), Levelled.class
    );

    private static final Map<Property<?>, Class<? extends org.bukkit.block.data.BlockData>> PROPERTY_TO_DATA = ImmutableMap.<Property<?>, Class<? extends org.bukkit.block.data.BlockData>>builder()
        // levelled and ageable are done using the property name
        .put(BlockStateProperties.POWER, AnaloguePowerable.class)
        .put(BlockStateProperties.HALF, Bisected.class)
        .put(BlockStateProperties.DOUBLE_BLOCK_HALF, Bisected.class)
        .put(BlockStateProperties.DUSTED, Brushable.class)
        .put(BlockStateProperties.FACING, Directional.class)
        .put(BlockStateProperties.HORIZONTAL_FACING, Directional.class)
        .put(BlockStateProperties.ATTACH_FACE, FaceAttachable.class)
        .put(BlockStateProperties.HANGING, Hangable.class)
        .put(BlockStateProperties.HATCH, Hatchable.class)
        .put(BlockStateProperties.LIT, Lightable.class)
        // multiple facing is done by matching two or more pipe block properties
        .put(BlockStateProperties.OPEN, Openable.class)
        .put(BlockStateProperties.HORIZONTAL_AXIS, Orientable.class)
        .put(BlockStateProperties.AXIS, Orientable.class)
        .put(BlockStateProperties.POWERED, Powerable.class)
        .put(BlockStateProperties.RAIL_SHAPE, Rail.class)
        .put(BlockStateProperties.RAIL_SHAPE_STRAIGHT, Rail.class)
        .put(BlockStateProperties.ROTATION_16, Rotatable.class)
        .put(BlockStateProperties.SNOWY, Snowable.class)
        .put(BlockStateProperties.WATERLOGGED, Waterlogged.class)
        .put(BlockStateProperties.SEGMENT_AMOUNT, Segmentable.class)
        .put(BlockStateProperties.SIDE_CHAIN_PART, SideChaining.class)
        .buildOrThrow();

    private static final Map<Property<?>, Class<? extends org.bukkit.block.data.BlockData>> MAIN_PROPERTY_TO_DATA = Map.of(
        BlockStateProperties.PISTON_TYPE, TechnicalPiston.class,
        BlockStateProperties.STAIRS_SHAPE, Stairs.class
    );

    public static final Map<Class<? extends Enum<? extends StringRepresentable>>, Class<? extends Enum<?>>> ENUM_BRIDGE = ImmutableMap.<Class<? extends Enum<? extends StringRepresentable>>, Class<? extends Enum<?>>>builder()
        .put(DoorHingeSide.class, Door.Hinge.class)
        .put(SlabType.class, Slab.Type.class)
        .put(StructureMode.class, org.bukkit.block.data.type.StructureBlock.Mode.class)
        .put(DripstoneThickness.class, PointedDripstone.Thickness.class)
        .put(WallSide.class, Wall.Height.class)
        .put(BellAttachType.class, Bell.Attachment.class)
        .put(NoteBlockInstrument.class, Instrument.class)
        .put(StairsShape.class, Stairs.Shape.class)
        .put(Direction.class, BlockFace.class)
        .put(ComparatorMode.class, Comparator.Mode.class)
        .put(PistonType.class, TechnicalPiston.Type.class)
        .put(BedPart.class, Bed.Part.class)
        .put(Half.class, Bisected.Half.class)
        .put(AttachFace.class, FaceAttachable.AttachedFace.class)
        .put(RailShape.class, Rail.Shape.class)
        .put(SculkSensorPhase.class, SculkSensor.Phase.class)
        .put(DoubleBlockHalf.class, Bisected.Half.class)
        .put(Tilt.class, BigDripleaf.Tilt.class)
        .put(ChestType.class, Chest.Type.class)
        .put(RedstoneSide.class, RedstoneWire.Connection.class)
        .put(Direction.Axis.class, Axis.class)
        .put(BambooLeaves.class, Bamboo.Leaves.class)
        .put(TrialSpawnerState.class, TrialSpawner.State.class)
        .put(FrontAndTop.class, Orientation.class)
        .put(VaultState.class, Vault.State.class)
        .put(CreakingHeartState.class, CreakingHeart.State.class)
        .put(TestBlockMode.class, org.bukkit.block.data.type.TestBlock.Mode.class)
        .put(CopperGolemStatueBlock.Pose.class, CopperGolemStatue.Pose.class)
        .put(SideChainPart.class, SideChaining.ChainPart.class)
        .buildOrThrow();

    public static @Nullable Class<? extends org.bukkit.block.data.BlockData> getBestSuitedApiClass(Class<?> block) {
        if (!MAPPING.containsKey(block)) {
            return null;
        }

        return getBestSuitedApiClass(MAPPING.get(block));
    }

    public static @Nullable Class<? extends org.bukkit.block.data.BlockData> getBestSuitedApiClass(BlockData data) {
        if (data.api() != null) {
            return data.api();
        }

        int pipeProps = 0;
        Set<Class<? extends org.bukkit.block.data.BlockData>> extensions = new LinkedHashSet<>();
        for (Property<?> property : data.properties()) {
            if (MAIN_PROPERTY_TO_DATA.containsKey(property)) {
                return MAIN_PROPERTY_TO_DATA.get(property);
            }

            if (NAME_TO_DATA.containsKey(property.getName())) {
                extensions.add(NAME_TO_DATA.get(property.getName()));
                continue;
            }

            if (PROPERTY_TO_DATA.containsKey(property)) {
                extensions.add(PROPERTY_TO_DATA.get(property));
                continue;
            }

            if (PipeBlock.PROPERTY_BY_DIRECTION.containsValue(property)) {
                pipeProps++;
            }
        }

        if (!extensions.isEmpty()) {
            if (isExactly(extensions, Switch.class)) {
                return Switch.class;
            }
            if (isExactly(extensions, RedstoneRail.class)) {
                return RedstoneRail.class;
            }

            return extensions.iterator().next();
        }

        for (Property<?> property : data.complexPropertyFields().values()) {
            if (PipeBlock.PROPERTY_BY_DIRECTION.containsValue(property)) {
                pipeProps++;
            }
        }

        if (pipeProps >= 2) {
            return MultipleFacing.class;
        }
        return null;
    }

    private static boolean isExactly(Set<Class<? extends org.bukkit.block.data.BlockData>> extensions, Class<? extends org.bukkit.block.data.BlockData> globClass) {
        return extensions.equals(ClassHelper.getAllInterfaces(globClass, org.bukkit.block.data.BlockData.class, new HashSet<>()));
    }

    private static String formatApiName(Class<?> specialBlock) {
        String apiName = specialBlock.getSimpleName();
        if (!BLOCK_SUFFIX_INTENDED.contains(specialBlock)) {
            return apiName.substring(0, apiName.length() - "Block".length());
        }
        return apiName;
    }

    private static boolean handleComplexType(Field field, BiConsumer<Field, Property<?>> complexCallback) throws IllegalAccessException {
        if (field.getType().isArray() && Property.class.isAssignableFrom(field.getType().getComponentType())) {
            if (!field.trySetAccessible()) {
                return true;
            }

            for (Property<?> property : (Property<?>[]) field.get(null)) {
                complexCallback.accept(field, property);
            }
            return true;
        }
        if (Iterable.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType complexType) {
            Type[] args = complexType.getActualTypeArguments();
            if (args.length == 1 && Property.class.isAssignableFrom(ClassHelper.eraseType(args[0]))) {
                if (!field.trySetAccessible()) {
                    return true;
                }

                for (Property<?> property : (Iterable<Property<?>>) field.get(null)) {
                    complexCallback.accept(field, property);
                }
            }
            return true;
        }
        if (Map.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType complexType) {
            if (!field.trySetAccessible()) {
                return true;
            }

            Type[] args = complexType.getActualTypeArguments();
            if (args.length == 2 && Property.class.isAssignableFrom(ClassHelper.eraseType(args[1]))) {
                for (Property<?> property : ((Map<?, Property<?>>) field.get(null)).values()) {
                    complexCallback.accept(field, property);
                }
                return true;
            }
        }
        return false;
    }

    private static void fetchProperties(Class<?> block, BiConsumer<Field, Property<?>> simpleCallback, @Nullable BiConsumer<Field, Property<?>> complexCallback) {
        try {
            for (Field field : block.getDeclaredFields()) {
                if (ClassHelper.isStaticConstant(field, 0)) {
                    if (complexCallback != null && handleComplexType(field, complexCallback)) {
                        continue;
                    }

                    if (!Property.class.isAssignableFrom(field.getType())) {
                        continue;
                    }

                    if (field.trySetAccessible()) {
                        Property<?> property = ((Property<?>) field.get(null));
                        simpleCallback.accept(field, property);
                    }
                }
            }
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }

        if (block.isInterface()) {
            return;
        }

        // look deeper
        if (block.getSuperclass() != null && block.getSuperclass() != Block.class) {
            fetchProperties(block.getSuperclass(), simpleCallback, complexCallback);
        }
        for (Class<?> ext : block.getInterfaces()) {
            fetchProperties(ext, simpleCallback, complexCallback);
        }
    }
}
