package io.papermc.paper.registry.entry;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.RegistryHolder;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.WritableCraftRegistry;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.event.RegistryEntryAddEventImpl;
import io.papermc.paper.registry.event.RegistryFreezeEventImpl;
import io.papermc.paper.registry.legacy.DelayedRegistryEntry;
import java.util.function.BiFunction;
import net.minecraft.core.Registry;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.jspecify.annotations.Nullable;

public interface RegistryEntry<M, B extends Keyed> extends RegistryEntryInfo<M, B> { // TODO remove Keyed

    RegistryHolder<B> createRegistryHolder(Registry<M> nmsRegistry);

    default RegistryEntry<M, B> withSerializationUpdater(final BiFunction<NamespacedKey, ApiVersion, NamespacedKey> updater) {
        return this;
    }

    /**
     * This should only be used if the registry instance needs to exist early due to the need
     * to populate a field in {@link org.bukkit.Registry}. Data-driven registries shouldn't exist
     * as fields, but instead be obtained via {@link io.papermc.paper.registry.RegistryAccess#getRegistry(RegistryKey)}
     */
    @Deprecated
    default RegistryEntry<M, B> delayed() {
        return new DelayedRegistryEntry<>(this);
    }

    interface BuilderHolder<M, T, B extends PaperRegistryBuilder<M, T>> extends RegistryEntryInfo<M, T> {

        B fillBuilder(Conversions conversions, M nms);
    }

    /**
     * Can mutate values being added to the registry
     */
    interface Modifiable<M, T, B extends PaperRegistryBuilder<M, T>> extends BuilderHolder<M, T, B> {

        static boolean isModifiable(final @Nullable RegistryEntryInfo<?, ?> entry) {
            return entry instanceof RegistryEntry.Modifiable<?, ?, ?> || (entry instanceof final DelayedRegistryEntry<?, ?> delayed && delayed.delegate() instanceof RegistryEntry.Modifiable<?, ?, ?>);
        }

        static <M, T extends Keyed, B extends PaperRegistryBuilder<M, T>> Modifiable<M, T, B> asModifiable(final RegistryEntryInfo<M, T> entry) { // TODO remove Keyed
            return (Modifiable<M, T, B>) possiblyUnwrap(entry);
        }

        default RegistryEntryAddEventImpl<T, B> createEntryAddEvent(final TypedKey<T> key, final B initialBuilder, final Conversions conversions) {
            return new RegistryEntryAddEventImpl<>(key, initialBuilder, this.apiKey(), conversions);
        }
    }

    /**
     * Can only add new values to the registry, not modify any values.
     */
    interface Addable<M, T extends Keyed, B extends PaperRegistryBuilder<M, T>> extends BuilderHolder<M, T, B> { // TODO remove Keyed

        default RegistryFreezeEventImpl<T, B> createFreezeEvent(final WritableCraftRegistry<M, T, B> writableRegistry, final Conversions conversions) {
            return new RegistryFreezeEventImpl<>(this.apiKey(), writableRegistry.createApiWritableRegistry(conversions), conversions);
        }

        static boolean isAddable(final @Nullable RegistryEntryInfo<?, ?> entry) {
            return entry instanceof RegistryEntry.Addable<?, ?, ?> || (entry instanceof final DelayedRegistryEntry<?, ?> delayed && delayed.delegate() instanceof RegistryEntry.Addable<?, ?, ?>);
        }

        static <M, T extends Keyed, B extends PaperRegistryBuilder<M, T>> Addable<M, T, B> asAddable(final RegistryEntryInfo<M, T> entry) {
            return (Addable<M, T, B>) possiblyUnwrap(entry);
        }
    }

    /**
     * Can mutate values and add new values.
     */
    interface Writable<M, T extends Keyed, B extends PaperRegistryBuilder<M, T>> extends Modifiable<M, T, B>, Addable<M, T, B> { // TODO remove Keyed

        static boolean isWritable(final @Nullable RegistryEntryInfo<?, ?> entry) {
            return entry instanceof RegistryEntry.Writable<?, ?, ?> || (entry instanceof final DelayedRegistryEntry<?, ?> delayed && delayed.delegate() instanceof RegistryEntry.Writable<?, ?, ?>);
        }

        static <M, T extends Keyed, B extends PaperRegistryBuilder<M, T>> Writable<M, T, B> asWritable(final RegistryEntryInfo<M, T> entry) { // TODO remove Keyed
            return (Writable<M, T, B>) possiblyUnwrap(entry);
        }
    }

    private static <M, B extends Keyed> RegistryEntryInfo<M, B> possiblyUnwrap(final RegistryEntryInfo<M, B> entry) {
        return entry instanceof final DelayedRegistryEntry<M, B> delayed ? delayed.delegate() : entry;
    }
}
