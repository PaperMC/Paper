package io.papermc.paper.configuration.serializer;

import java.lang.reflect.Type;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

public class ResourceLocationSerializer extends ScalarSerializer<ResourceLocation> {

    public static final ScalarSerializer<ResourceLocation> INSTANCE = new ResourceLocationSerializer();

    private ResourceLocationSerializer() {
        super(ResourceLocation.class);
    }

    @Override
    public ResourceLocation deserialize(final Type type, final Object obj) throws SerializationException {
        return ResourceLocation.read(obj.toString()).getOrThrow(s -> new SerializationException(ResourceLocation.class, s));
    }

    @Override
    protected Object serialize(final ResourceLocation item, final Predicate<Class<?>> typeSupported) {
        return item.toString();
    }
}
