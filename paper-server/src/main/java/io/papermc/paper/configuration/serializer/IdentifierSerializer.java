package io.papermc.paper.configuration.serializer;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.function.Predicate;
import net.minecraft.resources.Identifier;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

public class IdentifierSerializer extends ScalarSerializer.Annotated<Identifier> {

    public static final ScalarSerializer<Identifier> INSTANCE = new IdentifierSerializer();

    private IdentifierSerializer() {
        super(Identifier.class);
    }

    @Override
    public Identifier deserialize(final AnnotatedType annotatedType, final Object obj) throws SerializationException {
        return Identifier.read(obj.toString()).getOrThrow(s -> new SerializationException(Identifier.class, s));
    }

    @Override
    protected Object serialize(final AnnotatedType annotatedType, final Identifier item, final Predicate<Class<?>> typeSupported) {
        return item.toString();
    }
}
