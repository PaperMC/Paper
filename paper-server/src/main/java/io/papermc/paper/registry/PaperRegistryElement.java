package io.papermc.paper.registry;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Holder;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PaperRegistryElement<M, A> extends RegistryElement<A> {

    Holder<M> getHolder();

    @Override
    default boolean is(final Key type) {
        return this.getHolder().is(PaperAdventure.asVanilla(type));
    }

    @Override
    default boolean is(final TagKey<A> type) {
        return this.getHolder().is(PaperRegistries.toNms(type));
    }
}
