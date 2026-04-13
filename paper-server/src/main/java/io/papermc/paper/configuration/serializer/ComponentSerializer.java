package io.papermc.paper.configuration.serializer;

import java.lang.reflect.AnnotatedType;
import java.util.function.Predicate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

public class ComponentSerializer extends ScalarSerializer.Annotated<Component> {

    public ComponentSerializer() {
        super(Component.class);
    }

    @Override
    public Component deserialize(final AnnotatedType type, final Object obj) throws SerializationException {
        return MiniMessage.miniMessage().deserialize(obj.toString());
    }

    @Override
    protected Object serialize(final AnnotatedType type, final Component item, final Predicate<Class<?>> typeSupported) {
        return MiniMessage.miniMessage().serialize(item);
    }
}
