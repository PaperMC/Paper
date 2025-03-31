package io.papermc.generator.resources.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.papermc.generator.utils.BlockStateMapping;
import io.papermc.generator.utils.SourceCodecs;
import java.util.function.Predicate;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.jspecify.annotations.NullMarked;

@NullMarked
public sealed interface BlockPropertyPredicate permits BlockPropertyPredicate.IsFieldPredicate, BlockPropertyPredicate.IsNamePredicate {

    Codec<BlockPropertyPredicate> DIRECT_CODEC = Type.CODEC.dispatch("type", BlockPropertyPredicate::type, type -> type.codec);
    Codec<BlockPropertyPredicate> COMPACT_CODEC = Codec.withAlternative(IsFieldPredicate.COMPACT_CODEC, IsNamePredicate.COMPACT_CODEC);
    Codec<BlockPropertyPredicate> CODEC = Codec.withAlternative(DIRECT_CODEC, COMPACT_CODEC);

    String value();

    Type type();

    enum Type implements StringRepresentable {
        IS_FIELD("is_field", IsFieldPredicate.CODEC),
        IS_NAME("is_name", IsNamePredicate.CODEC);

        public static final Codec<Type> CODEC = StringRepresentable.fromValues(Type::values);
        private final String name;
        final MapCodec<? extends BlockPropertyPredicate> codec;

        Type(final String name, final MapCodec<? extends BlockPropertyPredicate> codec) {
            this.name = name;
            this.codec = codec;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    boolean matches(Property<?> property);

    static boolean testProperty(Predicate<String> predicate, String id, boolean supportInversion) {
        // expand if needed with proper support + AND/OR-ed
        if (supportInversion && id.charAt(0) == '!') {
            return !predicate.test(id.substring(1));
        }

        return predicate.test(id);
    }

    record IsNamePredicate(String value) implements BlockPropertyPredicate {

        private static final Codec<String> PROPERTY_NAME = ExtraCodecs.RESOURCE_PATH_CODEC
            .comapFlatMap(name -> {
                if (!BlockStateMapping.PROPERTY_NAMES.contains(name)) {
                    return DataResult.error(() -> "Invalid property name: '%s'".formatted(name));
                }
                return DataResult.success(name);
            }, name -> name);

        public static final MapCodec<IsNamePredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            PROPERTY_NAME.fieldOf("value").forGetter(IsNamePredicate::value)
        ).apply(instance, IsNamePredicate::new));

        public static final Codec<BlockPropertyPredicate> COMPACT_CODEC = PROPERTY_NAME.xmap(IsNamePredicate::new, BlockPropertyPredicate::value);

        @Override
        public Type type() {
            return Type.IS_NAME;
        }

        @Override
        public boolean matches(Property<?> property) {
            return BlockPropertyPredicate.testProperty(
                name -> name.equals(property.getName()),
                this.value,
                false
            );
        }
    }

    record IsFieldPredicate(String value) implements BlockPropertyPredicate {

        public static final MapCodec<IsFieldPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SourceCodecs.fieldNameCodec(BlockStateProperties.class, BlockStateMapping.GENERIC_FIELD_NAMES::containsValue).fieldOf("value").forGetter(IsFieldPredicate::value)
        ).apply(instance, IsFieldPredicate::new));

        public static final Codec<BlockPropertyPredicate> COMPACT_CODEC = SourceCodecs.fieldCodec(
            BlockStateProperties.class, BlockStateMapping.GENERIC_FIELD_NAMES::containsValue
        ).xmap(IsFieldPredicate::new, BlockPropertyPredicate::value);

        @Override
        public Type type() {
            return Type.IS_FIELD;
        }

        @Override
        public boolean matches(Property<?> property) {
            return BlockPropertyPredicate.testProperty(
                field -> field.equals(BlockStateMapping.GENERIC_FIELD_NAMES.get(property)),
                this.value,
                false
            );
        }
    }
}
