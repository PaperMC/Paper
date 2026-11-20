package io.papermc.generator.types.craftblockdata.property.appender;

import net.minecraft.world.level.block.state.properties.Property;

public interface PropertyAppender<T extends Comparable<T>, A> extends AppenderBase {

    Property<T> getProperty();

    Class<A> getApiType();
}
