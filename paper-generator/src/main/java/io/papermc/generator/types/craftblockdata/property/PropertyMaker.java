package io.papermc.generator.types.craftblockdata.property;

import com.squareup.javapoet.TypeName;
import io.papermc.generator.types.craftblockdata.property.appender.AppenderBase;
import io.papermc.generator.types.craftblockdata.property.converter.ConverterBase;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PropertyMaker extends ConverterBase, AppenderBase {

    TypeName getPropertyType();

    static <T extends Comparable<T>> PropertyMaker make(Property<T> property) {
        if (property instanceof IntegerProperty intProperty) {
            return new IntegerPropertyWriter(intProperty);
        }
        if (property instanceof EnumProperty<?> enumProperty) {
            return new EnumPropertyWriter<>(enumProperty);
        }
        return new PropertyWriter<>(property);
    }
}
