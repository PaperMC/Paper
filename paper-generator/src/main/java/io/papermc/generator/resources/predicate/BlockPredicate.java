package io.papermc.generator.resources.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.papermc.generator.utils.SourceCodecs;
import java.util.List;
import java.util.Set;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import org.jspecify.annotations.NullMarked;

@NullMarked
public sealed interface BlockPredicate permits BlockPredicate.ContainsPropertyPredicate, BlockPredicate.InstanceOfPredicate, BlockPredicate.IsClassPredicate {

    Codec<BlockPredicate> CODEC = Type.CODEC.dispatch("type", BlockPredicate::type, type -> type.codec);

    Type type();

    enum Type implements StringRepresentable {
        INSTANCE_OF("instance_of", InstanceOfPredicate.CODEC),
        IS_CLASS("is_class", IsClassPredicate.CODEC),
        HAS_PROPERTY("has_property", ContainsPropertyPredicate.SINGLE_CODEC),
        CONTAINS_PROPERTY("contains_property", ContainsPropertyPredicate.CODEC);

        public static final Codec<Type> CODEC = StringRepresentable.fromValues(Type::values);
        private final String name;
        final MapCodec<? extends BlockPredicate> codec;

        Type(final String name, final MapCodec<? extends BlockPredicate> codec) {
            this.name = name;
            this.codec = codec;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    boolean matches(Class<? extends Block> block, Set<Property<?>> properties);

    record IsClassPredicate(Class<? extends Block> value) implements BlockPredicate {

        public static final MapCodec<IsClassPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SourceCodecs.classCodec(Block.class).fieldOf("value").forGetter(IsClassPredicate::value)
        ).apply(instance, IsClassPredicate::new));

        @Override
        public Type type() {
            return Type.IS_CLASS;
        }

        @Override
        public boolean matches(Class<? extends Block> block, Set<Property<?>> properties) {
            return this.value.equals(block);
        }
    }

    record InstanceOfPredicate(Class<? extends Block> value, List<BlockPropertyPredicate> propertyPredicates) implements BlockPredicate {

        public static final MapCodec<InstanceOfPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SourceCodecs.classCodec(Block.class).fieldOf("value").forGetter(InstanceOfPredicate::value),
            ExtraCodecs.compactListCodec(BlockPropertyPredicate.CODEC).optionalFieldOf("has_property", List.of()).forGetter(InstanceOfPredicate::propertyPredicates)
        ).apply(instance, InstanceOfPredicate::new));

        @Override
        public Type type() {
            return Type.INSTANCE_OF;
        }

        @Override
        public boolean matches(Class<? extends Block> block, Set<Property<?>> properties) {
            if (!this.value.isAssignableFrom(block)) {
                return false;
            }

            if (this.propertyPredicates.isEmpty()) {
                return true;
            }

            for (BlockPropertyPredicate predicate : this.propertyPredicates) {
                for (Property<?> property : properties) {
                    if (predicate.matches(property)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    record ContainsPropertyPredicate(List<BlockPropertyPredicate> value, int count, Strategy strategy) implements BlockPredicate {

        public static final MapCodec<ContainsPropertyPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.nonEmptyList(BlockPropertyPredicate.CODEC.listOf()).fieldOf("value").forGetter(ContainsPropertyPredicate::value),
            ExtraCodecs.POSITIVE_INT.fieldOf("count").forGetter(ContainsPropertyPredicate::count),
            Strategy.CODEC.fieldOf("strategy").forGetter(ContainsPropertyPredicate::strategy)
        ).apply(instance, ContainsPropertyPredicate::new));

        public static final MapCodec<ContainsPropertyPredicate> SINGLE_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.compactListCodec(BlockPropertyPredicate.CODEC, ExtraCodecs.nonEmptyList(BlockPropertyPredicate.CODEC.listOf())).fieldOf("value").forGetter(ContainsPropertyPredicate::value)
        ).apply(instance, value -> new ContainsPropertyPredicate(value, 1, Strategy.AT_LEAST)));

        @Override
        public Type type() {
            return Type.CONTAINS_PROPERTY;
        }

        @Override
        public boolean matches(Class<? extends Block> block, Set<Property<?>> properties) {
            int found = 0;
            for (BlockPropertyPredicate predicate : this.value) {
                for (Property<?> property : properties) {
                    if (predicate.matches(property)) {
                        found++;
                        if (this.strategy == Strategy.AT_LEAST && found == this.count) {
                            return true;
                        }
                    }
                }
            }

            return this.strategy == Strategy.EXACT && found == this.count;
        }

        public enum Strategy implements StringRepresentable {
            EXACT("exact"),
            AT_LEAST("at_least");

            private final String name;
            static final Codec<Strategy> CODEC = StringRepresentable.fromEnum(Strategy::values);

            Strategy(String name) {
                this.name = name;
            }

            @Override
            public String getSerializedName() {
                return this.name;
            }
        }
    }
}
