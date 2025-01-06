package io.papermc.generator.types.craftblockdata.property.holder.appender;

import io.papermc.generator.types.craftblockdata.property.holder.DataHolderType;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DataAppenders {

    private static final Map<DataHolderType, DataAppender> APPENDERS = Stream.of(
        new ArrayAppender(),
        new ListAppender(),
        new MapAppender()
    ).collect(Collectors.toUnmodifiableMap(DataAppender::getType, key -> key));

    public static void ifPresent(DataHolderType type, Consumer<DataAppender> callback) {
        if (APPENDERS.containsKey(type)) {
            callback.accept(APPENDERS.get(type));
        }
    }
}
