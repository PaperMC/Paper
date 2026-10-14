package io.papermc.paper.registry;

import com.mojang.serialization.Lifecycle;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.entry.RegistryEntryMeta;
import io.papermc.paper.registry.event.WritableRegistry;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.bukkit.craftbukkit.CraftRegistry;

public class WritableCraftRegistry<M, T extends Keyed, B extends PaperRegistryBuilder<M, T>> extends CraftRegistry<T, M> {

    private static final RegistrationInfo FROM_PLUGIN = new RegistrationInfo(Optional.empty(), Lifecycle.experimental());

    private final RegistryEntryMeta.Buildable<M, T, B> meta;
    private final MappedRegistry<M> registry;

    public WritableCraftRegistry(
        final MappedRegistry<M> registry,
        final RegistryEntryMeta.Buildable<M, T, B> meta
    ) {
        super(meta, registry);
        this.registry = registry;
        this.meta = meta;
    }

    public void register(final TypedKey<T> key, final Consumer<RegistryBuilderFactory<T, B>> value, final Conversions conversions) {
        final ResourceKey<M> resourceKey = PaperRegistries.toNms(key);
        this.registry.validateWrite(resourceKey);
        final PaperRegistryBuilderFactory<M, T, B> builderFactory = new PaperRegistryBuilderFactory<>(this.registry.key(), conversions, this.meta.builderFiller(), this.registry::getValueForCopying);
        value.accept(builderFactory);
        PaperRegistryListenerManager.INSTANCE.registerWithListeners(
            this.registry,
            this.meta,
            resourceKey,
            builderFactory.requireBuilder(),
            FROM_PLUGIN,
            conversions
        );
    }

    public WritableRegistry<T, B> createApiWritableRegistry(final Conversions conversions) {
        return new ApiWritableRegistry(conversions);
    }

    public class ApiWritableRegistry implements WritableRegistry<T, B> {

        private final Conversions conversions;

        public ApiWritableRegistry(final Conversions conversions) {
            this.conversions = conversions;
        }

        @Override
        public void registerWith(final TypedKey<T> key, final Consumer<RegistryBuilderFactory<T, B>> value) {
            WritableCraftRegistry.this.register(key, value, this.conversions);
        }
    }
}
