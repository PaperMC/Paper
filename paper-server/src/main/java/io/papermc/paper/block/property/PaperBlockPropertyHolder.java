package io.papermc.paper.block.property;

import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jspecify.annotations.Nullable;

public interface PaperBlockPropertyHolder<O, S extends StateHolder<O, S>> extends BlockPropertyHolder {

    StateHolder<O, S> getState();

    StateDefinition<O, S> getStateDefinition();

    <T extends Comparable<T>> T get(Property<T> property);

    <A extends Enum<A>> A get(EnumProperty<?> property, Class<A> bukkitClass);

    @SuppressWarnings("unchecked")
    @Override
    default <T extends Comparable<T>> @Nullable BlockProperty<T> getProperty(final String propertyName) {
        final Property<?> property = this.getStateDefinition().getProperty(propertyName);
        if (property != null) {
            return (BlockProperty<T>) PaperBlockProperties.convertToPaperProperty(property);
        }
        return null;
    }

    @Override
    default <T extends Comparable<T>> boolean hasProperty(final BlockProperty<T> property) {
        return this.getState().hasProperty(PaperBlockProperties.convertTointernalProperty(property));
    }

    @SuppressWarnings("unchecked")
    @Override
    default <T extends Comparable<T>> T getValue(final BlockProperty<T> property) {
        final Property<?> internalProperty = PaperBlockProperties.convertTointernalProperty(property);
        Preconditions.checkArgument(this.getState().hasProperty(internalProperty), property.name() + " is not present on " + this);
        switch (internalProperty) {
            case final IntegerProperty intProperty -> {
                final T value;
                if (property instanceof final AsIntegerProperty<T> intRepresented) {
                    value = intRepresented.fromIntValue(this.get(intProperty));
                } else {
                    value = this.get((Property<T>) intProperty);
                }
                return value;
            }
            case final BooleanProperty _ -> {
                return this.get((Property<T>) internalProperty);
            }
            case final EnumProperty<?> enumProperty when property instanceof final EnumBlockProperty<?> enumDataProperty -> {
                return (T) this.get(enumProperty, enumDataProperty.type());
            }
            default -> throw new IllegalArgumentException("Did not recognize " + property + " and " + internalProperty);
        }
    }

    @Override
    default <T extends Comparable<T>> Optional<T> getOptionalValue(final BlockProperty<T> property) {
        if (!this.hasProperty(property)) {
            return Optional.empty();
        } else {
            return Optional.of(this.getValue(property));
        }
    }

    @Override
    default Collection<BlockProperty<?>> getProperties() {
        return Collections.unmodifiableCollection(Collections2.transform(this.getState().getProperties(), PaperBlockProperties::convertToPaperProperty));
    }

    interface PaperMutable<O, S extends StateHolder<O, S>> extends PaperBlockPropertyHolder<O, S>, BlockPropertyHolder.Mutable {

        <T extends Comparable<T>, V extends T> void set(Property<T> property, V value);

        <M extends Enum<M> & StringRepresentable> void set(EnumProperty<M> property, Enum<?> bukkit);

        @Override
        default <T extends Comparable<T>> void setValue(final BlockProperty<T> property, final T value) {
            Preconditions.checkNotNull(value, "Cannot set a data property to null");
            final Property<?> internalProperty = PaperBlockProperties.convertTointernalProperty(property);
            Preconditions.checkArgument(this.getState().hasProperty(internalProperty), property.name() + " is not present on " + this);
            switch (internalProperty) {
                case final IntegerProperty intProperty -> {
                    final int intValue;
                    if (property instanceof final AsIntegerProperty<T> intRepresented) {
                        intValue = intRepresented.toIntValue(value);
                    } else {
                        intValue = (Integer) value;
                    }
                    this.set(intProperty, intValue);
                }
                case final BooleanProperty booleanProperty -> this.set(booleanProperty, (Boolean) value);
                case final EnumProperty<?> enumProperty when value instanceof final Enum<?> enumValue -> this.set(enumProperty, enumValue);
                default ->
                    throw new IllegalArgumentException("Did not recognize " + property + " with value " + value + " (" + value.getClass().getSimpleName() + ") for " + internalProperty);
            }
        }
    }
}
