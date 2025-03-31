package io.papermc.generator.registry;

import com.google.common.base.Preconditions;
import io.papermc.generator.Main;
import io.papermc.generator.resources.data.RegistryData;
import io.papermc.generator.utils.ClassHelper;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class RegistryEntry<T> implements RegistryIdentifiable<T> {

    private final ResourceKey<? extends Registry<T>> registryKey;
    private final RegistryKeyField<T> registryKeyField;
    private final Class<?> holderElementsClass;
    private final RegistryData data;

    private @Nullable Map<ResourceKey<T>, String> fieldNames;

    protected RegistryEntry(ResourceKey<? extends Registry<T>> registryKey, RegistryKeyField<T> registryKeyField, Class<?> holderElementsClass, RegistryData data) {
        this.registryKey = registryKey;
        this.registryKeyField = registryKeyField;
        this.holderElementsClass = holderElementsClass;
        this.data = data;
    }

    @Override
    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return this.registryKey;
    }

    public Registry<T> registry() {
        return Main.REGISTRY_ACCESS.lookupOrThrow(this.registryKey);
    }

    public Class<T> elementClass() {
        return this.registryKeyField.elementClass();
    }

    public String registryKeyField() {
        return this.registryKeyField.name();
    }

    public RegistryData data() {
        return this.data;
    }

    protected void validate(Type type) {
        boolean isBuiltIn = type == Type.BUILT_IN;
        Type realType = type == Type.BUILT_IN ? Type.DATA_DRIVEN : Type.BUILT_IN;
        Preconditions.checkState(isBuiltIn == BuiltInRegistries.REGISTRY.containsKey(this.registryKey.location()), // type is checked at runtime and not guessed in case api/impl change is needed
            "Mismatch type, registry '%s' is %s but was in registry/%s.json", this.registryKey.location(), realType.getSerializedName(), type.getSerializedName()
        );
        Preconditions.checkState(type != Type.BUILT_IN || !this.data.impl().delayed(), "Built-in registry '%s' cannot be delayed!", this.registryKey.location());
    }

    public String keyClassName() {
        if (this.data.api().keyClassNameRelate()) {
            return this.data.api().klass().name().simpleName();
        }

        return this.elementClass().getSimpleName();
    }

    public boolean allowCustomKeys() {
        return (this.data.builder().isPresent() && this.data.builder().get().capability().canAdd()) || RegistryEntries.byType(Type.DATA_DRIVEN).contains(this);
    }

    private <TO> Map<ResourceKey<T>, TO> getFields(Map<ResourceKey<T>, TO> map, Function<Field, @Nullable TO> transform) {
        Registry<T> registry = this.registry();
        Class<T> elementClass = this.elementClass();
        try {
            for (Field field : this.holderElementsClass.getDeclaredFields()) {
                if (!ResourceKey.class.isAssignableFrom(field.getType()) && !Holder.Reference.class.isAssignableFrom(field.getType()) && !elementClass.isAssignableFrom(field.getType())) {
                    continue;
                }

                if (ClassHelper.isStaticConstant(field, Modifier.PUBLIC)) {
                    ResourceKey<T> key = null;
                    if (elementClass.isAssignableFrom(field.getType())) {
                        key = registry.getResourceKey(elementClass.cast(field.get(null))).orElseThrow();
                    } else {
                        if (field.getGenericType() instanceof ParameterizedType complexType && complexType.getActualTypeArguments().length == 1 &&
                            complexType.getActualTypeArguments()[0] == elementClass) {

                            if (Holder.Reference.class.isAssignableFrom(field.getType())) {
                                key = ((Holder.Reference<T>) field.get(null)).key();
                            } else {
                                key = (ResourceKey<T>) field.get(null);
                            }
                        }
                    }
                    if (key != null) {
                        TO value = transform.apply(field);
                        if (value != null) {
                            map.put(key, value);
                        }
                    }
                }
            }
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
        return map;
    }

    public Map<ResourceKey<T>, String> getFieldNames() {
        if (this.fieldNames == null) {
            this.fieldNames = this.getFields(Field::getName);
        }
        return this.fieldNames;
    }

    public <TO> Map<ResourceKey<T>, TO> getFields(Function<Field, @Nullable TO> transform) {
        return Collections.unmodifiableMap(this.getFields(new IdentityHashMap<>(), transform));
    }

    @Override
    public String toString() {
        return "RegistryEntry[" +
            "registryKey=" + this.registryKey + ", " +
            "registryKeyField=" + this.registryKeyField + ", " +
            "data=" + this.data +
            ']';
    }

    public enum Type implements StringRepresentable {

        BUILT_IN("built_in"),
        DATA_DRIVEN("data_driven");

        private final String name;
        Type(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
