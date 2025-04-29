package io.papermc.generator.types.craftblockdata.property.converter;

import io.papermc.generator.types.craftblockdata.property.PropertyMaker;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.world.level.block.state.properties.Property;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Converters {

    private static final Map<Property<?>, ConverterBase> CONVERTERS = Stream.of(
        new RotationConverter(),
        new NoteConverter()
    ).collect(Collectors.toUnmodifiableMap(Converter::getProperty, key -> key));

    public static ConverterBase getOrDefault(Property<?> property, PropertyMaker maker) {
        return CONVERTERS.getOrDefault(property, maker);
    }

    public static boolean has(Property<?> property) {
        return CONVERTERS.containsKey(property);
    }
}
