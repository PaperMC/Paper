package io.papermc.paper.registry.entry;

import com.mojang.datafixers.util.Either;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.RegistryKey;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;

public class WritableRegistryEntry<M, T extends Keyed, B extends PaperRegistryBuilder<M, T>> extends AddableRegistryEntry<M, T, B> implements RegistryEntry.Writable<M, T, B> { // TODO remove Keyed

    protected WritableRegistryEntry(
        final ResourceKey<? extends Registry<M>> mcKey,
        final RegistryKey<T> apiKey,
        final Class<?> classToPreload,
        final RegistryTypeMapper<M, T> minecraftToBukkit,
        final PaperRegistryBuilder.Filler<M, T, B> builderFiller
    ) {
        super(mcKey, apiKey, classToPreload, minecraftToBukkit, builderFiller);
    }
}
