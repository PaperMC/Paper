package io.papermc.paper.registry;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Lifecycle;
import io.papermc.paper.adventure.PaperAdventure;
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
import org.jspecify.annotations.Nullable;

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

    public void register(final TypedKey<T> key, final @Nullable TypedKey<T> copyFrom, final Consumer<? super B> value, final Conversions conversions) {
        final ResourceKey<M> resourceKey = PaperRegistries.toNms(key);
        this.registry.validateWrite(resourceKey);
        final B builder;
        if (copyFrom != null) {
            final M existing = this.registry.temporaryUnfrozenMap.get(PaperAdventure.asVanilla(copyFrom));
            Preconditions.checkArgument(existing != null, "Cannot copy from unregistered key: %s", copyFrom);
            builder = this.meta.builderFiller().fill(conversions, existing);
        } else {
            builder = this.meta.builderFiller().create(conversions);
        }
        value.accept(builder);
        PaperRegistryListenerManager.INSTANCE.registerWithListeners(
            this.registry,
            this.meta,
            resourceKey,
            builder,
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
        public void register(final TypedKey<T> key, final Consumer<? super B> value) {
            WritableCraftRegistry.this.register(key, null, value, this.conversions);
        }

        @Override
        public void register(final TypedKey<T> key, final TypedKey<T> copyFrom, final Consumer<? super B> value) {
            WritableCraftRegistry.this.register(key, copyFrom, value, this.conversions);
        }
    }
}
