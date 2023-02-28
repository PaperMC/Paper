package io.papermc.paper.registry;

import com.google.common.base.Suppliers;
import io.papermc.paper.registry.legacy.DelayedRegistry;
import io.papermc.paper.registry.legacy.DelayedRegistryEntry;
import java.util.function.Supplier;
import org.bukkit.Keyed;
import org.bukkit.Registry;

public interface RegistryHolder<B extends Keyed> {

    Registry<B> get();

    final class Memoized<B extends Keyed, R extends Registry<B>> implements RegistryHolder<B> {

        private final Supplier<R> memoizedSupplier;

        public Memoized(final Supplier<? extends R> supplier) {
            this.memoizedSupplier = Suppliers.memoize(supplier::get);
        }

        public Registry<B> get() {
            return this.memoizedSupplier.get();
        }
    }

    final class Delayed<B extends Keyed, R extends Registry<B>> implements RegistryHolder<B> {

        private final DelayedRegistry<B, R> delayedRegistry = new DelayedRegistry<>();

        @Override
        public DelayedRegistry<B, R> get() {
            return this.delayedRegistry;
        }

        <M> void loadFrom(final DelayedRegistryEntry<M, B> delayedEntry, final net.minecraft.core.Registry<M> registry) {
            final RegistryHolder<B> delegateHolder = delayedEntry.delegate().createRegistryHolder(registry);
            if (!(delegateHolder instanceof RegistryHolder.Memoized<B, ?>)) {
                throw new IllegalArgumentException(delegateHolder + " must be a memoized holder");
            }
            this.delayedRegistry.load(((Memoized<B, R>) delegateHolder).memoizedSupplier);
        }
    }
}
