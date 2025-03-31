package io.papermc.generator.types.craftblockdata.property.converter;

import com.squareup.javapoet.TypeName;
import net.minecraft.world.level.block.state.properties.Property;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Converter<T extends Comparable<T>> extends ConverterBase {

    Property<T> getProperty();

    @Override
    TypeName getApiType();
}
