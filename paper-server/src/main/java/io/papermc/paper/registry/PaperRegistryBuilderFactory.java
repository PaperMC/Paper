package io.papermc.paper.registry;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.data.util.Conversions;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.jspecify.annotations.Nullable;

public class PaperRegistryBuilderFactory<M, A extends Keyed, B extends PaperRegistryBuilder<M, A>> implements RegistryBuilderFactory<A, B> { // TODO remove Keyed

    private final ResourceKey<? extends Registry<M>> registryKey;
    private final Conversions conversions;
    private final PaperRegistryBuilder.Filler<M, A, B> builderFiller;
    private final Function<ResourceKey<M>, Optional<M>> existingValueGetter;
    private @Nullable B builder;

    public PaperRegistryBuilderFactory(
        final ResourceKey<? extends Registry<M>> registryKey,
        final Conversions conversions,
        final PaperRegistryBuilder.Filler<M, A, B> builderFiller,
        final Function<ResourceKey<M>, Optional<M>> existingValueGetter
    ) {
        this.registryKey = registryKey;
        this.conversions = conversions;
        this.builderFiller = builderFiller;
        this.existingValueGetter = existingValueGetter;
    }

    private void validate() {
        if (this.builder != null) {
            throw new IllegalStateException("Already created a builder");
        }
    }

    public B requireBuilder() {
        if (this.builder == null) {
            throw new IllegalStateException("Builder not created yet");
        }
        return this.builder;
    }

    @Override
    public B empty() {
        this.validate();
        return this.builder = this.builderFiller.create(this.conversions);
    }

    @Override
    public B copyFrom(final TypedKey<A> key) {
        this.validate();
        final Optional<M> existing = this.existingValueGetter.apply(PaperAdventure.asVanilla(this.registryKey, key));
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Key " + key + " doesn't exist");
        }
        return this.builder = this.builderFiller.fill(this.conversions, existing.get());
    }
}
