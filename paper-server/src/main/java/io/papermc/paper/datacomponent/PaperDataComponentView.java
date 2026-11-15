package io.papermc.paper.datacomponent;

import net.minecraft.core.component.DataComponentGetter;
import org.jspecify.annotations.Nullable;

public record PaperDataComponentView(DataComponentGetter getter) implements DataComponentView {

    @Override
    public <T> @Nullable T getData(final DataComponentType.Valued<T> type) {
        return PaperDataComponentType.convertDataComponentValue(this.getter, (PaperDataComponentType.ValuedImpl<T, ?>) type);
    }

    @Override
    public <T> @Nullable T getDataOrDefault(final DataComponentType.Valued<? extends T> type, final @Nullable T fallback) {
        final T value = this.getData(type);
        return value != null ? value : fallback;
    }

    @Override
    public boolean hasData(final DataComponentType type) {
        return this.getter.get(PaperDataComponentType.bukkitToMinecraft(type)) != null;
    }
}
