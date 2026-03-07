package io.papermc.paper.registry.typed.converter;

import io.papermc.paper.util.MCUtil;
import java.util.Collection;

public record CollectionConverter<M, A>(
    Converter<M, A> converter
) implements Converter<Collection<? extends M>, Collection<? extends A>> {

    @Override
    public Collection<? extends M> toVanilla(final Collection<? extends A> value) {
        return MCUtil.transformUnmodifiable(value, this.converter::toVanilla);
    }

    @Override
    public Collection<? extends A> fromVanilla(final Collection<? extends M> value) {
        return MCUtil.transformUnmodifiable(value, this.converter::fromVanilla);
    }
}
