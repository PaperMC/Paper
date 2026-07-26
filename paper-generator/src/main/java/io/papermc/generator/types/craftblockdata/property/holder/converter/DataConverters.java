package io.papermc.generator.types.craftblockdata.property.holder.converter;

import io.papermc.generator.types.craftblockdata.property.holder.DataHolderType;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DataConverters {

    private static final Map<DataHolderType, DataConverter> CONVERTERS = Stream.of(
        new ArrayConverter(),
        new ListConverter(),
        new MapConverter()
    ).collect(Collectors.toUnmodifiableMap(DataConverter::getType, key -> key));

    public static DataConverter getOrThrow(DataHolderType type) {
        DataConverter converter = CONVERTERS.get(type);
        if (converter == null) {
            throw new IllegalStateException("Cannot handle data holder type: " + type);
        }
        return converter;
    }
}
