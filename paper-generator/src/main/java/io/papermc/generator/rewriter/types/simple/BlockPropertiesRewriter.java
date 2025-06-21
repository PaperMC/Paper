package io.papermc.generator.rewriter.types.simple;

import io.papermc.generator.rewriter.types.Types;
import io.papermc.generator.utils.BlockStateMapping;
import io.papermc.paper.block.property.BlockProperty;
import io.papermc.paper.block.property.BooleanBlockProperty;
import io.papermc.paper.block.property.EnumBlockProperty;
import io.papermc.paper.block.property.IntegerBlockProperty;
import io.papermc.typewriter.ClassNamed;
import io.papermc.typewriter.replace.SearchMetadata;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import io.papermc.typewriter.util.ClassHelper;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.bukkit.Axis;
import org.bukkit.Note;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rail;
import org.jspecify.annotations.Nullable;

import static io.papermc.generator.utils.Formatting.quoted;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

public class BlockPropertiesRewriter extends SearchReplaceRewriter {

    private record BlockPropertyData(Class<? extends BlockProperty> propertyClass, String accessorName) {
    }

    private static final Map<Class<? extends Property>, BlockPropertyData> DATA = Map.of(
        BooleanProperty.class, new BlockPropertyData(BooleanBlockProperty.class, "bool"),
        IntegerProperty.class, new BlockPropertyData(IntegerBlockProperty.class, "integer"),
        EnumProperty.class, new BlockPropertyData(EnumBlockProperty.class, "enumeration")
    );

    // propertyClass is Internal and not exposed to this module
    private record PropertyType(Class<? extends BlockProperty> typeClass, ClassNamed propertyClass, @Nullable Class<?> valueClass) {
    }

    private static final Map<Property<?>, PropertyType> MODIFIED_TYPES = Map.of(
        BlockStateProperties.NOTE, new PropertyType(BlockProperty.class, Types.NOTE_BLOCK_PROPERTY, Note.class),
        BlockStateProperties.ROTATION_16, new PropertyType(EnumBlockProperty.class, Types.ROTATION_BLOCK_PROPERTY, BlockFace.class)
    );

    private interface CodeContext {
        void write(StringBuilder builder, Function<Class<?>, String> imported);
    }

    private static final Map<Property<?>, CodeContext> PREDICATES = Map.of(
        BlockStateProperties.FACING, (builder, imported) -> {
            builder.append("%s::isCartesian".formatted(imported.apply(BlockFace.class)));
        },
        BlockStateProperties.HORIZONTAL_FACING, (builder, imported) -> {
            builder.append("%s::isCardinal".formatted(imported.apply(BlockFace.class)));
        },
        BlockStateProperties.FACING_HOPPER, (builder, imported) -> {
            builder.append("((%1$s<%2$s>) %2$s::isCartesian).and(face -> face != %2$s.UP)".formatted(
                Predicate.class.getSimpleName(),
                imported.apply(BlockFace.class)
            ));
        },
        BlockStateProperties.VERTICAL_DIRECTION, (builder, $) -> {
            builder.append("%1$s -> %1$s.getModY() != 0".formatted("face"));
        },
        BlockStateProperties.HORIZONTAL_AXIS, (builder, imported) -> {
            builder.append("%s::isHorizontal".formatted(
                imported.apply(Axis.class)
            ));
        },
        BlockStateProperties.RAIL_SHAPE_STRAIGHT, (builder, imported) -> {
            builder.append("%s::isStraight".formatted(
                imported.apply(Rail.Shape.class)
            ));
        }
    );

    private static final Comparator<? super Map.Entry<Property<?>, Field>> FIELD_ORDER =
        Comparator.<Map.Entry<Property<?>, Field>, String>comparing(entry -> entry.getKey().getClass().getSimpleName())
        .thenComparing(entry -> entry.getValue().getName());

    @Override
    protected void insert(SearchMetadata metadata, StringBuilder builder) {
        for (Map.Entry<Property<?>, Field> entry : BlockStateMapping.FALLBACK_GENERIC_FIELDS.entrySet().stream().sorted(FIELD_ORDER).toList()) {
            Property<?> property = entry.getKey();
            builder.append(metadata.indent());
            builder.append("%s %s %s ".formatted(PUBLIC, STATIC, FINAL));
            BlockPropertyData data = DATA.get(property.getClass());

            if (MODIFIED_TYPES.containsKey(property)) {
                PropertyType type = MODIFIED_TYPES.get(property);
                builder.append("%s<%s> %s = register(new %s(%s));".formatted(
                    type.typeClass().getSimpleName(),
                    this.importCollector.getShortName(type.valueClass()),
                    entry.getValue().getName(),
                    type.propertyClass().simpleName(),
                    quoted(entry.getKey().getName())
                ));
            } else {
                builder.append(data.propertyClass().getSimpleName());
                Class<?> enumType = null;
                if (property.getClass().equals(EnumProperty.class)) {
                    builder.append('<');
                    enumType = BlockStateMapping.ENUM_BRIDGE.get(property.getValueClass());
                    if (enumType == null) {
                        throw new IllegalStateException("Unknown enum type for " + property);
                    }
                    builder.append(this.importCollector.getShortName(enumType));
                    builder.append('>');
                }

                builder.append(' ');
                builder.append(entry.getValue().getName());
                builder.append(" = ");
                builder.append(data.accessorName());
                builder.append('(');
                builder.append(quoted(entry.getKey().getName()));
                if (property instanceof IntegerProperty intProperty) {
                    builder.append(", ");
                    List<Integer> values = intProperty.getPossibleValues();
                    builder.append("%d, %d".formatted(values.getFirst(), values.getLast()));
                } else if (enumType != null) {
                    builder.append(", ");
                    builder.append("%s.class".formatted(ClassHelper.retrieveFullNestedName(enumType))); // already imported
                    CodeContext predicate = PREDICATES.get(property);
                    if (predicate != null) {
                        builder.append(", ");
                        predicate.write(builder, name -> this.importCollector.getShortName(name));
                    }
                }
                builder.append(");");
            }

            builder.append('\n');
        }
    }
}
