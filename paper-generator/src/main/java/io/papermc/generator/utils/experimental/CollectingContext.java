package io.papermc.generator.utils.experimental;

import com.mojang.serialization.Lifecycle;
import io.papermc.generator.Main;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record CollectingContext<T>(Set<ResourceKey<T>> registered,
                                   Registry<T> registry) implements BootstrapContext<T> {

    @Override
    public Holder.Reference<T> register(ResourceKey<T> key, T value, Lifecycle lifecycle) {
        this.registered.add(key);
        return Holder.Reference.createStandAlone(this.registry, key);
    }

    @Override
    public <S> HolderGetter<S> lookup(ResourceKey<? extends Registry<? extends S>> key) {
        return Main.REGISTRY_ACCESS.lookupOrThrow(key);
    }
}
