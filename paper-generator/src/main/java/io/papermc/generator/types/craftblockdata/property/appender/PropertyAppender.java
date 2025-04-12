package io.papermc.generator.types.craftblockdata.property.appender;

import net.minecraft.world.level.block.state.properties.Property;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PropertyAppender<T extends Comparable<T>, A> extends AppenderBase {

    Property<T> getProperty();

    Class<A> getApiType();
}
