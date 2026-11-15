package io.papermc.paper.datacomponent;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.holder.PaperRegistryHolders;
import io.papermc.paper.registry.holder.RegistryHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.Removed;
import org.jspecify.annotations.Nullable;

// only reads the patch itself, as the prototype components of an item aren't bound while registries are loading
public record PaperDataComponentPatchView(DataComponentPatch patch) implements DataComponentPatchView {

    public static <NMS> void checkNotHolderValued(final PaperDataComponentType<?, NMS> type, final String alternative) {
        Preconditions.checkArgument(type.getAdapter().holderValue() == null, "%s holds a value of a data-driven registry, use %s instead", type.getHolder().getRegisteredName(), alternative);
    }

    public static <NMS> DataComponentAdapter.HolderValue<NMS, ?> requireHolderValue(final PaperDataComponentType<?, NMS> type) {
        final DataComponentAdapter.@Nullable HolderValue<NMS, ?> holderValue = type.getAdapter().holderValue();
        Preconditions.checkArgument(holderValue != null, "%s doesn't hold a value of a data-driven registry", type.getHolder().getRegisteredName());
        return holderValue;
    }

    @SuppressWarnings("unchecked")
    public <NMS> @Nullable NMS getValue(final net.minecraft.core.component.DataComponentType<NMS> type) {
        final Object value = this.patch.map.get(type);
        return value == null ? null : (NMS) Removed.removedToNull(value);
    }

    @Override
    public <T> @Nullable T getData(final DataComponentType.Valued<T> type) {
        return this.getData((PaperDataComponentType.ValuedImpl<T, ?>) type);
    }

    private <T, NMS> @Nullable T getData(final PaperDataComponentType.ValuedImpl<T, NMS> type) {
        checkNotHolderValued(type, "getHolder");
        final NMS value = this.getValue(type.getHandle());
        return value == null ? null : type.getAdapter().fromVanilla(value);
    }

    @Override
    public <T extends RegistryElement.Buildable<T, E, ?>, E> @Nullable RegistryHolder<T, E> getHolder(final DataComponentType.Valued<T> type) {
        return this.getHolder((PaperDataComponentType.ValuedImpl<T, ?>) type);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private <T extends RegistryElement.Buildable<T, E, ?>, E, NMS> @Nullable RegistryHolder<T, E> getHolder(final PaperDataComponentType.ValuedImpl<T, NMS> type) {
        final DataComponentAdapter.HolderValue<NMS, ?> holderValue = requireHolderValue(type);
        final NMS value = this.getValue(type.getHandle());
        if (value == null) {
            return null;
        }
        final Holder<?> holder = holderValue.unwrap().apply(value);
        return (RegistryHolder<T, E>) PaperRegistryHolders.create((RegistryKey) holderValue.registryKey(), holder, Conversions.current());
    }

    @Override
    public boolean hasData(final DataComponentType type) {
        return this.getValue(PaperDataComponentType.bukkitToMinecraft(type)) != null;
    }

    @Override
    public boolean isDataRemoved(final DataComponentType type) {
        final Object value = this.patch.map.get(PaperDataComponentType.bukkitToMinecraft(type));
        return value != null && Removed.isRemoved(value);
    }
}
