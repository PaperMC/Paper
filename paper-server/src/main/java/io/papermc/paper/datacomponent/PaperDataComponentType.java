package io.papermc.paper.datacomponent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.Handleable;
import org.jspecify.annotations.Nullable;

public abstract class PaperDataComponentType<T, NMS> implements DataComponentType, Handleable<net.minecraft.core.component.DataComponentType<NMS>> {

    static {
        DataComponentAdapters.bootstrap();
    }

    public static <T> net.minecraft.core.component.DataComponentType<T> bukkitToMinecraft(final DataComponentType type) {
        return CraftRegistry.bukkitToMinecraft(type);
    }

    public static DataComponentType minecraftToBukkit(final net.minecraft.core.component.DataComponentType<?> type) {
        return CraftRegistry.minecraftToBukkit(type, Registries.DATA_COMPONENT_TYPE);
    }

    public static Set<DataComponentType> minecraftToBukkit(final Set<net.minecraft.core.component.DataComponentType<?>> nmsTypes) {
        final Set<DataComponentType> types = new HashSet<>(nmsTypes.size());
        for (final net.minecraft.core.component.DataComponentType<?> nmsType : nmsTypes) {
            types.add(PaperDataComponentType.minecraftToBukkit(nmsType));
        }
        return Collections.unmodifiableSet(types);
    }

    public static <B, M> @Nullable B convertDataComponentValue(final DataComponentMap map, final PaperDataComponentType.ValuedImpl<B, M> type) {
        final net.minecraft.core.component.DataComponentType<M> nms = bukkitToMinecraft(type);
        final M nmsValue = map.get(nms);
        if (nmsValue == null) {
            return null;
        }
        return type.getAdapter().fromVanilla(nmsValue);
    }

    private final NamespacedKey key;
    private final net.minecraft.core.component.DataComponentType<NMS> type;
    private final DataComponentAdapter<NMS, T> adapter;

    public PaperDataComponentType(final NamespacedKey key, final net.minecraft.core.component.DataComponentType<NMS> type, final DataComponentAdapter<NMS, T> adapter) {
        this.key = key;
        this.type = type;
        this.adapter = adapter;
    }

    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public boolean isPersistent() {
        return !this.type.isTransient();
    }

    public DataComponentAdapter<NMS, T> getAdapter() {
        return this.adapter;
    }

    @Override
    public net.minecraft.core.component.DataComponentType<NMS> getHandle() {
        return this.type;
    }

    @SuppressWarnings("unchecked")
    public static <NMS> DataComponentType of(final NamespacedKey key, final net.minecraft.core.component.DataComponentType<NMS> type) {
        final DataComponentAdapter<NMS, ?> adapter = (DataComponentAdapter<NMS, ?>) DataComponentAdapters.ADAPTERS.get(BuiltInRegistries.DATA_COMPONENT_TYPE.getResourceKey(type).orElseThrow());
        if (adapter == null) {
            throw new IllegalArgumentException("No adapter found for " + key);
        }
        if (adapter.isUnimplemented()) {
            return new Unimplemented<>(key, type, adapter);
        } else if (adapter.isValued()) {
            return new ValuedImpl<>(key, type, adapter);
        } else {
            return new NonValuedImpl<>(key, type, adapter);
        }
    }

    public static final class NonValuedImpl<T, NMS> extends PaperDataComponentType<T, NMS> implements NonValued {

        NonValuedImpl(
            final NamespacedKey key,
            final net.minecraft.core.component.DataComponentType<NMS> type,
            final DataComponentAdapter<NMS, T> adapter
        ) {
            super(key, type, adapter);
        }
    }

    public static final class ValuedImpl<T, NMS> extends PaperDataComponentType<T, NMS> implements Valued<T> {

        ValuedImpl(
            final NamespacedKey key,
            final net.minecraft.core.component.DataComponentType<NMS> type,
            final DataComponentAdapter<NMS, T> adapter
        ) {
            super(key, type, adapter);
        }
    }

    public static final class Unimplemented<T, NMS> extends PaperDataComponentType<T, NMS> {

        public Unimplemented(
            final NamespacedKey key,
            final net.minecraft.core.component.DataComponentType<NMS> type,
            final DataComponentAdapter<NMS, T> adapter
        ) {
            super(key, type, adapter);
        }
    }
}
