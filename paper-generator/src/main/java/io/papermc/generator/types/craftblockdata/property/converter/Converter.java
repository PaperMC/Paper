package io.papermc.generator.types.craftblockdata.property.converter;

import net.minecraft.world.level.block.state.properties.Property;

public interface Converter<T extends Comparable<T>, A> extends ConverterBase {

    Property<T> getProperty();

    @Override
    Class<A> getApiType();
}
