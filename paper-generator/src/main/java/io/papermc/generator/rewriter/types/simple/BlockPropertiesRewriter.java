package io.papermc.generator.rewriter.types.simple;

import com.squareup.javapoet.ClassName;
import io.papermc.generator.resources.DataFileLoader;
import io.papermc.generator.resources.DataFiles;
import io.papermc.generator.rewriter.types.Types;
import io.papermc.generator.utils.BlockStateMapping;
import io.papermc.typewriter.ClassNamed;
import io.papermc.typewriter.replace.SearchMetadata;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jspecify.annotations.Nullable;

import static io.papermc.generator.utils.Formatting.quoted;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

public class BlockPropertiesRewriter extends SearchReplaceRewriter {

    private record BlockPropertyData(ClassNamed propertyClass, String accessorName) {
    }

    private static final Map<Class<?>, BlockPropertyData> DATA = Map.of(
        BooleanProperty.class, new BlockPropertyData(Types.BOOLEAN_BLOCK_PROPERTY, "bool"),
        IntegerProperty.class, new BlockPropertyData(Types.INTEGER_BLOCK_PROPERTY, "integer"),
        EnumProperty.class, new BlockPropertyData(Types.ENUM_BLOCK_PROPERTY, "enumeration")
    );

    private record PropertyType(ClassNamed typeClass, ClassNamed propertyClass, @Nullable ClassNamed valueClass) {
    }

    private static final Map<Property<?>, PropertyType> MODIFIED_TYPES = Map.of(
        BlockStateProperties.NOTE, new PropertyType(Types.BLOCK_PROPERTY, Types.NOTE_BLOCK_PROPERTY, Types.NOTE),
        BlockStateProperties.ROTATION_16, new PropertyType(Types.ENUM_BLOCK_PROPERTY, Types.ROTATION_BLOCK_PROPERTY, Types.BLOCK_FACE)
    );

    private interface CodeContext {
        void write(StringBuilder builder, Function<ClassNamed, String> imported);
    }

    private static final Map<Property<?>, CodeContext> PREDICATES = Map.of(
        BlockStateProperties.FACING, (builder, imported) -> {
            builder.append("%s::isCartesian".formatted(imported.apply(Types.BLOCK_FACE)));
        },
        BlockStateProperties.HORIZONTAL_FACING, (builder, imported) -> {
            builder.append("%s::isCardinal".formatted(imported.apply(Types.BLOCK_FACE)));
        },
        BlockStateProperties.FACING_HOPPER, (builder, imported) -> {
            builder.append("((%1$s<%2$s>) %2$s::isCartesian).and(face -> face != %2$s.UP)".formatted(
                Predicate.class.getSimpleName(),
                imported.apply(Types.BLOCK_FACE)
            ));
        },
        BlockStateProperties.VERTICAL_DIRECTION, (builder, $) -> {
            builder.append("%1$s -> %1$s.getModY() != 0".formatted("face"));
        },
        BlockStateProperties.HORIZONTAL_AXIS, (builder, imported) -> {
            builder.append("%s::isHorizontal".formatted(
                imported.apply(Types.AXIS)
            ));
        },
        BlockStateProperties.RAIL_SHAPE_STRAIGHT, (builder, imported) -> {
            builder.append("%s::isStraight".formatted(
                imported.apply(Types.BLOCK_DATA_RAIL_SHAPE)
            ));
        }
    );

    private static final Comparator<? super Map.Entry<Property<?>, Field>> FIELD_ORDER =
        Comparator.<Map.Entry<Property<?>, Field>, String>comparing(entry -> entry.getKey().getClass().getSimpleName())
            .thenComparing(entry -> entry.getValue().getName());

    @Override
    protected void insert(SearchMetadata metadata, StringBuilder builder) {
        Map<Class<? extends Enum<? extends StringRepresentable>>, ClassName> enumTypes = DataFileLoader.get(DataFiles.BLOCK_STATE_ENUM_PROPERTY_TYPES);
        for (Map.Entry<Property<?>, Field> entry : BlockStateMapping.GENERIC_FIELDS.entrySet().stream().sorted(FIELD_ORDER).toList()) {
            Property<?> property = entry.getKey();
            builder.append(metadata.indent());
            builder.append("%s %s %s ".formatted(PUBLIC, STATIC, FINAL));
            BlockPropertyData data = DATA.get(property.getClass());

            if (MODIFIED_TYPES.containsKey(property)) {
                PropertyType type = MODIFIED_TYPES.get(property);
                builder.append("%s<%s> %s = register(new %s(%s));".formatted(
                    type.typeClass().simpleName(),
                    this.importCollector.getShortName(type.valueClass()),
                    entry.getValue().getName(),
                    type.propertyClass().simpleName(),
                    quoted(entry.getKey().getName())
                ));
            } else {
                builder.append(data.propertyClass().simpleName());
                ClassNamed enumType = null;
                if (property.getClass().equals(EnumProperty.class)) {
                    builder.append('<');
                    if (!enumTypes.containsKey(property.getValueClass())) {
                        throw new IllegalStateException("Unknown enum type for " + property);
                    }
                    enumType = Types.typed(enumTypes.get(property.getValueClass()));
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
                    builder.append("%s.class".formatted(enumType.dottedNestedName())); // already imported
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
