package io.papermc.paper.registry.entry;

import com.mojang.datafixers.util.Either;
import io.papermc.paper.registry.RegistryKey;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;

public class RegistryEntryBuilder<M, A extends Keyed> { // TODO remove Keyed

    public static <M, A extends Keyed> RegistryEntryBuilder<M, A> start( // TODO remove Keyed
        final ResourceKey<? extends Registry<M>> mcKey,
        final RegistryKey<A> apiKey
    ) {
        return new RegistryEntryBuilder<>(mcKey, apiKey);
    }

    protected final ResourceKey<? extends Registry<M>> mcKey;
    protected final RegistryKey<A> apiKey;

    private RegistryEntryBuilder(final ResourceKey<? extends Registry<M>> mcKey, RegistryKey<A> apiKey) {
        this.mcKey = mcKey;
        this.apiKey = apiKey;
    }

    public RegistryEntry<M, A> apiOnly(final Supplier<org.bukkit.Registry<A>> apiRegistrySupplier) {
        return new ApiRegistryEntry<>(this.mcKey, this.apiKey, apiRegistrySupplier);
    }

    public CraftStage<M, A> craft(final Class<?> classToPreload, final BiFunction<? super NamespacedKey, M, ? extends A> minecraftToBukkit) {
        return new CraftStage<>(this.mcKey, this.apiKey, classToPreload, new RegistryTypeMapper<>(minecraftToBukkit));
    }

    public CraftStage<M, A> craft(final Class<?> classToPreload, final Function<Holder<M>, ? extends A> minecraftToBukkit) {
        return new CraftStage<>(this.mcKey, this.apiKey, classToPreload, new RegistryTypeMapper<>(minecraftToBukkit));
    }

    public static final class CraftStage<M, A extends Keyed> extends RegistryEntryBuilder<M, A> { // TODO remove Keyed

        private final Class<?> classToPreload;
        private final RegistryTypeMapper<M, A> minecraftToBukkit;

        private CraftStage(
            final ResourceKey<? extends Registry<M>> mcKey,
            final RegistryKey<A> apiKey,
            final Class<?> classToPreload,
            final RegistryTypeMapper<M, A> minecraftToBukkit
        ) {
            super(mcKey, apiKey);
            this.classToPreload = classToPreload;
            this.minecraftToBukkit = minecraftToBukkit;
        }

        public RegistryEntry<M, A> build() {
            return new CraftRegistryEntry<>(this.mcKey, this.apiKey, this.classToPreload, this.minecraftToBukkit);
        }
    }
}
