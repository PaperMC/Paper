package io.papermc.paper.configuration.serializer;

import com.mojang.logging.LogUtils;
import io.leangen.geantyref.TypeToken;
import java.lang.reflect.AnnotatedType;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.util.EnumLookup;

import static io.leangen.geantyref.GenericTypeReflector.erase;

/**
 * Enum serializer that lists options if fails and accepts `-` as `_`.
 */
public class EnumValueSerializer extends ScalarSerializer.Annotated<Enum<?>> {

    private static final Logger LOGGER = LogUtils.getClassLogger();

    public EnumValueSerializer() {
        super(new TypeToken<Enum<?>>() {});
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public @Nullable Enum<?> deserialize(final AnnotatedType annotatedType, final Object obj) throws SerializationException {
        final String enumConstant = obj.toString();
        final Class<? extends Enum> typeClass = erase(annotatedType.getType()).asSubclass(Enum.class);
        Enum<?> ret = EnumLookup.lookupEnum(typeClass, enumConstant);
        if (ret == null) {
            ret = EnumLookup.lookupEnum(typeClass, enumConstant.replace("-", "_"));
        }
        if (ret == null) {
            final boolean longer = typeClass.getEnumConstants().length > 10;
            final List<String> options = Arrays.stream(typeClass.getEnumConstants()).limit(10L).map(Enum::name).toList();
            LOGGER.error("Invalid enum constant provided, expected one of [{}{}], but got {}", String.join(", ", options), longer ? ", ..." : "", enumConstant);
        }
        return ret;
    }

    @Override
    public Object serialize(final AnnotatedType type, final Enum<?> item, final Predicate<Class<?>> typeSupported) {
        return item.name();
    }
}
