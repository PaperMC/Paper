package io.papermc.paper.block.property;

import com.google.common.base.Suppliers;
import com.google.common.collect.Sets;
import io.papermc.paper.InternalAPIBridge;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;

@ApiStatus.Internal
sealed class EnumBlockPropertyImpl<E extends Enum<E>> implements EnumBlockProperty<E> permits RotationBlockProperty {

    private final String name;
    private final Class<E> type;
    private final Set<E> values;
    private final Supplier<Index<String, E>> byNameIndex;

    EnumBlockPropertyImpl(final String name, final Class<E> type, final Collection<E> values) {
        this.name = name;
        this.type = type;
        this.values = Sets.immutableEnumSet(values);
        this.byNameIndex = Suppliers.memoize(this::createByNameIndex);
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Class<E> type() {
        return this.type;
    }

    private Index<String, E> byNameIndex() {
        return this.byNameIndex.get();
    }

    private Index<String, E> createByNameIndex() {
        return Index.create(e -> InternalAPIBridge.get().getPropertyEnumName(this, e), List.copyOf(this.values));
    }

    @Override
    public String name(final E value) {
        final String valueName = this.byNameIndex().key(value);
        if (valueName == null) {
            throw ExceptionCreator.INSTANCE.create(value, ExceptionCreator.Type.VALUE, this);
        }
        return valueName;
    }

    @Override
    public boolean isValidName(final String name) {
        return this.byNameIndex().value(name) != null;
    }

    @Override
    public E value(final String name) {
        final E value = this.byNameIndex().value(name);
        if (value == null) {
            throw ExceptionCreator.INSTANCE.create(name, ExceptionCreator.Type.NAME, this);
        }
        return value;
    }

    @Override
    public boolean isValidValue(final E enumValue) {
        return this.values.contains(enumValue);
    }

    @Override
    public @Unmodifiable Set<E> values() {
        return this.values;
    }
}
