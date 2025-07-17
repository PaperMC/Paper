package io.papermc.paper.datacomponent;

import io.papermc.paper.registry.HolderableBase;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import org.bukkit.craftbukkit.CraftRegistry;
import org.jspecify.annotations.Nullable;

public abstract class PaperDataComponentType<T, NMS> extends HolderableBase<net.minecraft.core.component.DataComponentType<NMS>> implements DataComponentType {

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

    private final DataComponentAdapter<NMS, T> adapter;

    private PaperDataComponentType(final Holder<net.minecraft.core.component.DataComponentType<NMS>> holder, final DataComponentAdapter<NMS, T> adapter) {
        super(holder);
        this.adapter = adapter;
    }

    @Override
    public boolean isPersistent() {
        return !this.getHandle().isTransient();
    }

    public DataComponentAdapter<NMS, T> getAdapter() {
        return this.adapter;
    }

    @SuppressWarnings({"unchecked"})
    public static <NMS> DataComponentType of(final Holder<?> holder) {
        final DataComponentAdapter<NMS, ?> adapter = (DataComponentAdapter<NMS, ?>) DataComponentAdapters.ADAPTERS.get(holder.unwrapKey().orElseThrow());
        if (adapter == null) {
            throw new IllegalArgumentException("No adapter found for " + holder);
        }
        if (adapter.isUnimplemented()) {
            return new Unimplemented<>((Holder<net.minecraft.core.component.DataComponentType<NMS>>) holder, adapter);
        } else if (adapter.isValued()) {
            return new ValuedImpl<>((Holder<net.minecraft.core.component.DataComponentType<NMS>>) holder, adapter);
        } else {
            return new NonValuedImpl<>((Holder<net.minecraft.core.component.DataComponentType<NMS>>) holder, adapter);
        }
    }

    public static final class NonValuedImpl<T, NMS> extends PaperDataComponentType<T, NMS> implements NonValued {

        NonValuedImpl(
            final Holder<net.minecraft.core.component.DataComponentType<NMS>> holder,
            final DataComponentAdapter<NMS, T> adapter
        ) {
            super(holder, adapter);
        }
    }

    public static final class ValuedImpl<T, NMS> extends PaperDataComponentType<T, NMS> implements Valued<T> {

        ValuedImpl(
            final Holder<net.minecraft.core.component.DataComponentType<NMS>> holder,
            final DataComponentAdapter<NMS, T> adapter
        ) {
            super(holder, adapter);
        }
    }

    public static final class Unimplemented<T, NMS> extends PaperDataComponentType<T, NMS> {

        public Unimplemented(
            final Holder<net.minecraft.core.component.DataComponentType<NMS>> holder,
            final DataComponentAdapter<NMS, T> adapter
        ) {
            super(holder, adapter);
        }
    }
}
