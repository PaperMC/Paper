package io.papermc.paper.configuration.serializer;

import io.papermc.paper.configuration.type.EngineMode;
import java.lang.reflect.Type;
import java.util.function.Predicate;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

public final class EngineModeSerializer extends ScalarSerializer<EngineMode> {

    public EngineModeSerializer() {
        super(EngineMode.class);
    }

    @Override
    public EngineMode deserialize(final Type type, final Object obj) throws SerializationException {
        if (obj instanceof final Integer id) {
            try {
                return EngineMode.valueOf(id);
            } catch (final IllegalArgumentException e) {
                throw new SerializationException(id + " is not a valid id for type " + type + " for this node");
            }
        }

        throw new SerializationException(obj + " is not of a valid type " + type + " for this node");
    }

    @Override
    protected Object serialize(final EngineMode item, final Predicate<Class<?>> typeSupported) {
        return item.getId();
    }
}
