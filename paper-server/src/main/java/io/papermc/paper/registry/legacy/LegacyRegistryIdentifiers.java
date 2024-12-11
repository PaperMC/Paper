package io.papermc.paper.registry.legacy;

import com.google.common.collect.ImmutableMap;
import io.leangen.geantyref.GenericTypeReflector;
import io.papermc.paper.registry.RegistryKey;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

@Deprecated
public final class LegacyRegistryIdentifiers {

    public static final Map<Class<?>, RegistryKey<?>> CLASS_TO_KEY_MAP;

    static {
        final ImmutableMap.Builder<Class<?>, RegistryKey<?>> builder = ImmutableMap.builder();
        try {
            for (final Field field : RegistryKey.class.getFields()) {
                if (field.getType() == RegistryKey.class) {
                    // get the legacy type from the RegistryKey generic parameter on the field
                    final Class<?> legacyType = GenericTypeReflector.erase(((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]);
                    builder.put(legacyType, (RegistryKey<?>) field.get(null));
                }
            }
        } catch (final ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
        CLASS_TO_KEY_MAP = builder.build();
    }

    private LegacyRegistryIdentifiers() {
    }
}
