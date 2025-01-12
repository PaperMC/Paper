package io.papermc.paper.block.property;

import com.google.common.collect.ImmutableMap;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
import net.minecraft.world.level.block.entity.vault.VaultState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import net.minecraft.world.level.block.state.properties.CreakingHeartState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
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
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Bell;
import org.bukkit.block.data.type.BigDripleaf;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.block.data.type.CreakingHeart;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Jigsaw;
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
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@AllFeatures
public class BlockPropertyTest {

    private static final Map<Class<? extends Enum<? extends StringRepresentable>>, Class<? extends Enum<?>>> ENUM_MAPPING = ImmutableMap.<Class<? extends Enum<? extends StringRepresentable>>, Class<? extends Enum<?>>>builder()
        .put(DoorHingeSide.class, Door.Hinge.class)
        .put(SlabType.class, Slab.Type.class)
        .put(StructureMode.class, StructureBlock.Mode.class)
        .put(FrontAndTop.class, Orientation.class)
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
        .put(VaultState.class, Vault.State.class)
        .put(CreakingHeartState.class, CreakingHeart.State.class)
        .put(TestBlockMode.class, TestBlock.Mode.class)
        .build();

    private PrintStream old;
    @BeforeEach
    public void beforeEach() {
        old = System.out;
    }

    @AfterEach
    public void afterEach() {
        System.setOut(this.old);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testEnumMapping() throws IllegalAccessException {
        final Map<String, EnumProperty> nmsPropertyMap = collectProperties(BlockStateProperties.class, EnumProperty.class);
        for (final EnumProperty value : nmsPropertyMap.values()) {
            assertNotNull(ENUM_MAPPING.get(value.getValueClass()), "Missing enum mapping for " + value.getValueClass());
        }
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void validateProperties() throws IllegalAccessException {
        final Map<String, Property> nmsPropertyMap = collectProperties(BlockStateProperties.class, Property.class);
        final Map<String, BlockProperty> paperPropertyMap = collectProperties(BlockProperties.class, BlockProperty.class);
        final List<String> missing = new ArrayList<>();
        final List<String> invalid = new ArrayList<>();
        nmsPropertyMap.forEach((name, prop) -> {
            if (paperPropertyMap.containsKey(name)) {
                if (!isEqual(prop, paperPropertyMap.get(name))) {
                    invalid.add(name + ": \n\t" + paperPropertyMap.get(name) + "\n\t" + prop);
                }
                paperPropertyMap.remove(name);
            } else {
                missing.add(stringifyPropertyField(name, prop));
            }
        });

        assertEquals(0, invalid.size(), "Invalid Property: \n" + String.join("\n", invalid) + "\n");
        assertEquals(0, paperPropertyMap.size(), "Extra Property: \n" + String.join("\n", paperPropertyMap.keySet()) + "\n");
        assertEquals(0, missing.size(), "Missing Property: \n" + String.join("\n", missing) + "\n");
    }


    @Test
    public void testToNmsPropertyConversion() {
        assertFalse(BlockProperties.PROPERTIES.isEmpty(), "no paper properties found");
        for (final BlockProperty<?> property : BlockProperties.PROPERTIES.values()) {
            final Property<?> nmsProperty = PaperBlockProperties.convertToNmsProperty(property);
            assertNotNull(nmsProperty, "Could not convert " + property + " to its nms counterpart");
            assertTrue(isEqual(nmsProperty, property), property.name() + " is not equal to " + nmsProperty.getName());
        }
    }

    @Test
    public void testToPaperPropertyConversion() {
        assertFalse(Property.PROPERTY_MULTIMAP.isEmpty(), "no nms properties found");
        for (final Property<?> nmsProp : Property.PROPERTY_MULTIMAP.values()) {
            final BlockProperty<?> paperProp = PaperBlockProperties.convertToPaperProperty(nmsProp);
            assertNotNull(paperProp, "Could not convert " + nmsProp + " to its paper counterpart");
            assertTrue(isEqual(nmsProp, paperProp), nmsProp.getName() + " is not equal to " + paperProp.name());
        }
    }

    private static boolean isEqual(final Property<?> nmsProperty, final BlockProperty<?> property) {
        return switch (property) {
            // special cases
            case final RotationBlockProperty rotation when nmsProperty instanceof IntegerProperty && "rotation".equals(nmsProperty.getName()) ->
                nmsProperty.getPossibleValues().size() == rotation.values().size();
            case final NoteBlockProperty note when nmsProperty instanceof IntegerProperty && "note".equals(nmsProperty.getName()) ->
                nmsProperty.getPossibleValues().size() == note.values().size();
            // end special cases
            case final BooleanBlockProperty ignored when nmsProperty instanceof BooleanProperty -> true;
            case final IntegerBlockProperty apiIntProp when nmsProperty instanceof final IntegerProperty nmsIntProp ->
                nmsIntProp.min == apiIntProp.min() && nmsIntProp.max == apiIntProp.max() && nmsIntProp.getPossibleValues().size() == apiIntProp.values().size();
            case final EnumBlockProperty<?> apiEnumProp when nmsProperty instanceof final EnumProperty<?> nmsEnumProp ->
                ENUM_MAPPING.get(nmsEnumProp.getValueClass()) == apiEnumProp.type() && nmsEnumProp.getPossibleValues().size() == apiEnumProp.values().size();
            default -> false;
        };
    }

    private static String stringifyPropertyField(final String fieldName, final Property<?> property) {
        switch (property) {
            case final IntegerProperty intProp -> {
                // special cases
                if (property == BlockStateProperties.ROTATION_16) { /** see {@link RotationBlockProperty} */
                    return "public static final EnumBlockProperty<BlockFace> " + fieldName + " = new RotationBlockProperty(\"" + property.getName() + "\");";
                } else if (property == BlockStateProperties.NOTE) { /** see {@link NoteBlockProperty} */
                    return "public static final BlockProperty<Note> " + fieldName + " = new NoteBlockProperty(\"" + property.getName() + "\");";
                }
                // end special cases
                return "public static final IntegerBlockProperty " + fieldName + " = integer(\"" + property.getName() + "\", " + intProp.min + ", " + intProp.max + ");";
                // end special cases
            }
            case final BooleanProperty ignored -> {
                return "public static final BooleanBlockProperty " + fieldName + " = bool(\"" + property.getName() + "\");";
            }
            case final EnumProperty<?> enumProp -> {
                final String value;
                assumeTrue(ENUM_MAPPING.containsKey(enumProp.getValueClass()), "Missing enum mappings!");
                final Class<? extends Enum<?>> bukkitEnum = ENUM_MAPPING.get(enumProp.getValueClass());
                final String name = (bukkitEnum.isMemberClass() ? bukkitEnum.getEnclosingClass().getSimpleName() + "." : "") + bukkitEnum.getSimpleName();
                value = "public static final EnumBlockProperty<" + name + "> " + fieldName + " = enumeration(\"" + property.getName() + "\", " + name + ".class);";
                return value;
            }
            default -> {
            }
        }
        fail(property + " is not a recognized property type");
        return "";
    }

    private static <P> Map<String, P> collectProperties(final Class<?> containerClass, final Class<P> propertyClass) throws IllegalAccessException {
        final Map<String, P> propertyMap = new LinkedHashMap<>();
        for (final Field field : containerClass.getFields()) {
            if (!Modifier.isStatic(field.getModifiers()) || !propertyClass.isAssignableFrom(field.getType())) {
                continue;
            }
            propertyMap.put(field.getName(), propertyClass.cast(field.get(null)));
        }
        return propertyMap;
    }
}
