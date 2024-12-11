package io.papermc.paper.configuration.serializer;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.MobCategory;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

public final class StringRepresentableSerializer extends ScalarSerializer<StringRepresentable> {
    private static final Map<Type, Function<String, StringRepresentable>> TYPES = Collections.synchronizedMap(Map.ofEntries(
        createEntry(MobCategory.class)
    ));

    public StringRepresentableSerializer() {
        super(StringRepresentable.class);
    }

    public static boolean isValidFor(final Type type) {
        return TYPES.containsKey(type);
    }

    private static <E extends Enum<E> & StringRepresentable> Map.Entry<Type, Function<String, @Nullable StringRepresentable>> createEntry(Class<E> type) {
        return Map.entry(type, s -> {
            for (E value : type.getEnumConstants()) {
                if (value.getSerializedName().equals(s)) {
                    return value;
                }
            }
            return null;
        });
    }

    @Override
    public StringRepresentable deserialize(Type type, Object obj) throws SerializationException {
        Function<String, StringRepresentable> function = TYPES.get(type);
        if (function == null) {
            throw new SerializationException(type + " isn't registered");
        }
        return function.apply(obj.toString());
    }

    @Override
    protected Object serialize(StringRepresentable item, Predicate<Class<?>> typeSupported) {
        return item.getSerializedName();
    }
}
