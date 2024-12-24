package io.papermc.paper.registry;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.entry.RegistryEntryMeta;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.Keyed;
import org.jspecify.annotations.Nullable;

public class PaperRegistryBuilderFactory<M, A extends Keyed, B extends PaperRegistryBuilder<M, A>> implements RegistryBuilderFactory<A, B> { // TODO remove Keyed

    private final Conversions conversions;
    private final RegistryEntryMeta.Buildable<M, A, B> meta;
    private final Function<? super ResourceLocation, ? extends @Nullable M> existingValueGetter;
    private @Nullable B builder;

    public PaperRegistryBuilderFactory(final Conversions conversions, final RegistryEntryMeta.Buildable<M, A, B> meta, final Function<? super ResourceLocation, ? extends @Nullable M> existingValueGetter) {
        this.conversions = conversions;
        this.meta = meta;
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
        return this.builder = this.meta.builderFiller().create(this.conversions);
    }

    @Override
    public B copyOf(final TypedKey<A> key) {
        this.validate();
        final M existing = this.existingValueGetter.apply(PaperAdventure.asVanilla(key));
        if (existing == null) {
            throw new IllegalArgumentException("Key " + key + " doesn't exist");
        }
        return this.builder = this.meta.builderFiller().fill(this.conversions, existing);
    }
}
