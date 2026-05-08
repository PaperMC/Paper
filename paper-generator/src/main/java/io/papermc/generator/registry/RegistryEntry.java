package io.papermc.generator.registry;

import com.google.common.base.Preconditions;
import io.papermc.generator.Main;
import io.papermc.generator.utils.ClassHelper;
import java.lang.constant.ConstantDescs;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import javax.lang.model.SourceVersion;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class RegistryEntry<T> {

    private final ResourceKey<? extends Registry<T>> registryKey;
    private final RegistryKeyField<T> registryKeyField;
    private final Class<T> elementClass;
    private final Class<?> holderElementsClass;
    private boolean allowDirect;

    private final Class<? extends Keyed> apiClass; // TODO remove Keyed
    private Class<?> preloadClass;
    private final String implClass;

    private @Nullable RegistryModificationApiSupport modificationApiSupport;
    private @Nullable Class<?> apiRegistryBuilder;
    private @Nullable String apiRegistryBuilderImpl;

    private @Nullable String fieldRename;
    private boolean delayed;
    private String apiAccessName = ConstantDescs.INIT_NAME;
    private Optional<String> apiRegistryField = Optional.empty();
    private int genericArgCount = 0;

    private @Nullable Map<ResourceKey<T>, String> fieldNames;

    public RegistryEntry(ResourceKey<? extends Registry<T>> registryKey, RegistryKeyField<T> registryKeyField, Class<?> holderElementsClass, Class<? extends Keyed> apiClass, String implClass) {
        this.registryKey = registryKey;
        this.registryKeyField = registryKeyField;
        this.elementClass = registryKeyField.elementClass();
        this.holderElementsClass = holderElementsClass;
        this.apiClass = apiClass;
        this.preloadClass = apiClass;
        this.implClass = implClass;
    }

    public ResourceKey<? extends Registry<T>> registryKey() {
        return this.registryKey;
    }

    public Registry<T> registry() {
        return Main.REGISTRY_ACCESS.lookupOrThrow(this.registryKey);
    }

    public String registryKeyField() {
        return this.registryKeyField.name();
    }

    public Class<T> elementClass() {
        return this.elementClass;
    }

    public Class<? extends Keyed> apiClass() {
        return this.apiClass;
    }

    public String implClass() {
        return this.implClass;
    }

    public RegistryEntry<T> allowDirect() {
        this.allowDirect = true;
        return this;
    }

    public RegistryEntry<T> delayed() {
        this.delayed = true;
        return this;
    }

    public RegistryEntry<T> preload(Class<?> klass) {
        this.preloadClass = klass;
        return this;
    }

    public RegistryEntry<T> genericArgCount(int count) {
        Preconditions.checkArgument(count >= 0, "Generic argument count must be non-negative");
        this.genericArgCount = count;
        return this;
    }

    public RegistryEntry<T> apiAccessName(String name) {
        Preconditions.checkArgument(SourceVersion.isIdentifier(name) && !SourceVersion.isKeyword(name), "Invalid accessor name");
        this.apiAccessName = name;
        return this;
    }

    public RegistryEntry<T> serializationUpdater(String fieldName) {
        this.fieldRename = fieldName;
        return this;
    }

    public boolean canAllowDirect() {
        return this.allowDirect;
    }

    public boolean isDelayed() {
        return this.delayed;
    }

    public String apiAccessName() {
        return this.apiAccessName;
    }

    public Class<?> preloadClass() {
        return this.preloadClass;
    }

    public @Nullable String fieldRename() {
        return this.fieldRename;
    }

    public @Nullable Class<?> apiRegistryBuilder() {
        return this.apiRegistryBuilder;
    }

    public @Nullable String apiRegistryBuilderImpl() {
        return this.apiRegistryBuilderImpl;
    }

    public int genericArgCount() {
        return this.genericArgCount;
    }

    public @Nullable RegistryModificationApiSupport modificationApiSupport() {
        return this.modificationApiSupport;
    }

    public RegistryEntry<T> writableApiRegistryBuilder(Class<?> builderClass, String builderImplClass) {
       return this.apiRegistryBuilder(builderClass, builderImplClass, RegistryModificationApiSupport.WRITABLE);
    }

    public RegistryEntry<T> apiRegistryBuilder(Class<?> builderClass, String builderImplClass, RegistryModificationApiSupport modificationApiSupport) {
        this.apiRegistryBuilder = builderClass;
        this.apiRegistryBuilderImpl = builderImplClass;
        this.modificationApiSupport = modificationApiSupport;
        return this;
    }

    public Optional<String> apiRegistryField() {
        return this.apiRegistryField;
    }

    public RegistryEntry<T> apiRegistryField(String registryField) {
        this.apiRegistryField = Optional.of(registryField);
        return this;
    }

    public String keyClassName() {
        if (RegistryEntries.REGISTRY_CLASS_NAME_BASED_ON_API.contains(this.apiClass)) {
            return this.apiClass.getSimpleName();
        }

        return this.elementClass.getSimpleName();
    }

    public boolean allowCustomKeys() {
        return (this.apiRegistryBuilder != null && this.modificationApiSupport.canAdd()) || RegistryEntries.DATA_DRIVEN.contains(this);
    }

    private <TO> Map<ResourceKey<T>, TO> getFields(Map<ResourceKey<T>, TO> map, Function<Field, @Nullable TO> transform) {
        Registry<T> registry = this.registry();
        try {
            for (Field field : this.holderElementsClass.getDeclaredFields()) {
                if (!ResourceKey.class.isAssignableFrom(field.getType()) && !Holder.Reference.class.isAssignableFrom(field.getType()) && !this.elementClass.isAssignableFrom(field.getType())) {
                    continue;
                }

                if (ClassHelper.isStaticConstant(field, Modifier.PUBLIC)) {
                    ResourceKey<T> key = null;
                    if (this.elementClass.isAssignableFrom(field.getType())) {
                        key = registry.getResourceKey(this.elementClass.cast(field.get(null))).orElseThrow();
                    } else {
                        if (field.getGenericType() instanceof ParameterizedType complexType && complexType.getActualTypeArguments().length == 1 &&
                            complexType.getActualTypeArguments()[0] == this.elementClass) {

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
            "apiClass=" + this.apiClass + ", " +
            "implClass=" + this.implClass + ", " +
            ']';
    }

    public enum RegistryModificationApiSupport {
        NONE,
        ADDABLE,
        MODIFIABLE,
        WRITABLE;

        public boolean canAdd() {
            return this != MODIFIABLE && this != NONE;
        }
    }
}
