package io.papermc.paper.pluginremap.reflect;

import com.mojang.logging.LogUtils;
import io.papermc.paper.util.MappingEnvironment;
import io.papermc.paper.util.ObfHelper;
import io.papermc.reflectionrewriter.runtime.AbstractDefaultRulesReflectionProxy;
import io.papermc.reflectionrewriter.runtime.DefineClassReflectionProxy;
import java.lang.invoke.MethodHandles;
import java.nio.ByteBuffer;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.slf4j.Logger;

// todo proper inheritance handling
@SuppressWarnings("unused")
@DefaultQualifier(NonNull.class)
public final class PaperReflection extends AbstractDefaultRulesReflectionProxy implements DefineClassReflectionProxy {
    // concat to avoid being rewritten by shadow
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String CB_PACKAGE_PREFIX = "org.bukkit.".concat("craftbukkit.");
    private static final String LEGACY_CB_PACKAGE_PREFIX = "org.bukkit.".concat("craftbukkit.") + MappingEnvironment.LEGACY_CB_VERSION + ".";

    private final DefineClassReflectionProxy defineClassProxy;
    private final Map<String, ObfHelper.ClassMapping> mappingsByMojangName;
    private final Map<String, ObfHelper.ClassMapping> mappingsByObfName;
    // Reflection does not care about method return values, so this map removes the return value descriptor from the key
    private final Map<String, Map<String, String>> strippedMethodMappings;

    PaperReflection() {
        this.defineClassProxy = DefineClassReflectionProxy.create(PaperReflection::processClass);
        if (!MappingEnvironment.hasMappings()) {
            this.mappingsByMojangName = Map.of();
            this.mappingsByObfName = Map.of();
            this.strippedMethodMappings = Map.of();
            return;
        }
        final ObfHelper obfHelper = ObfHelper.INSTANCE;
        this.mappingsByMojangName = Objects.requireNonNull(obfHelper.mappingsByMojangName(), "mappingsByMojangName");
        this.mappingsByObfName = Objects.requireNonNull(obfHelper.mappingsByObfName(), "mappingsByObfName");
        this.strippedMethodMappings = this.mappingsByMojangName.entrySet().stream().collect(Collectors.toUnmodifiableMap(
            Map.Entry::getKey,
            entry -> entry.getValue().strippedMethods()
        ));
    }

    @Override
    protected String mapClassName(final String name) {
        final ObfHelper.@Nullable ClassMapping mapping = this.mappingsByObfName.get(name);
        return mapping != null ? mapping.mojangName() : removeCraftBukkitRelocation(name);
    }

    @Override
    protected String mapDeclaredMethodName(final Class<?> clazz, final String name, final Class<?> @Nullable ... parameterTypes) {
        final @Nullable Map<String, String> mapping = this.strippedMethodMappings.get(clazz.getName());
        if (mapping == null) {
            return name;
        }
        return mapping.getOrDefault(strippedMethodKey(name, parameterTypes), name);
    }

    @Override
    protected String mapMethodName(final Class<?> clazz, final String name, final Class<?> @Nullable ... parameterTypes) {
        final @Nullable String mapped = this.findMappedMethodName(clazz, name, parameterTypes);
        return mapped != null ? mapped : name;
    }

    @Override
    protected String mapDeclaredFieldName(final Class<?> clazz, final String name) {
        final ObfHelper.@Nullable ClassMapping mapping = this.mappingsByMojangName.get(clazz.getName());
        if (mapping == null) {
            return name;
        }
        return mapping.fieldsByObf().getOrDefault(name, name);
    }

    @Override
    protected String mapFieldName(final Class<?> clazz, final String name) {
        final @Nullable String mapped = this.findMappedFieldName(clazz, name);
        return mapped != null ? mapped : name;
    }

    private @Nullable String findMappedMethodName(final Class<?> clazz, final String name, final Class<?> @Nullable ... parameterTypes) {
        final Map<String, String> map = this.strippedMethodMappings.get(clazz.getName());
        @Nullable String mapped = null;
        if (map != null) {
            mapped = map.get(strippedMethodKey(name, parameterTypes));
            if (mapped != null) {
                return mapped;
            }
        }
        // JVM checks super before interfaces
        final Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            mapped = this.findMappedMethodName(superClass, name, parameterTypes);
        }
        if (mapped == null) {
            for (final Class<?> i : clazz.getInterfaces()) {
                mapped = this.findMappedMethodName(i, name, parameterTypes);
                if (mapped != null) {
                    break;
                }
            }
        }
        return mapped;
    }

    private @Nullable String findMappedFieldName(final Class<?> clazz, final String name) {
        final ObfHelper.ClassMapping mapping = this.mappingsByMojangName.get(clazz.getName());
        @Nullable String mapped = null;
        if (mapping != null) {
            mapped = mapping.fieldsByObf().get(name);
            if (mapped != null) {
                return mapped;
            }
        }
        // The JVM checks super before interfaces
        final Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            mapped = this.findMappedFieldName(superClass, name);
        }
        if (mapped == null) {
            for (final Class<?> i : clazz.getInterfaces()) {
                mapped = this.findMappedFieldName(i, name);
                if (mapped != null) {
                    break;
                }
            }
        }
        return mapped;
    }

    private static String strippedMethodKey(final String methodName, final Class<?> @Nullable ... parameterTypes) {
        return methodName + parameterDescriptor(parameterTypes);
    }

    private static String parameterDescriptor(final Class<?> @Nullable ... parameterTypes) {
        if (parameterTypes == null) {
            // Null parameterTypes is treated as an empty array
            return "()";
        }
        final StringBuilder builder = new StringBuilder();
        builder.append('(');
        for (final Class<?> parameterType : parameterTypes) {
            builder.append(parameterType.descriptorString());
        }
        builder.append(')');
        return builder.toString();
    }

    private static String removeCraftBukkitRelocation(final String name) {
        if (MappingEnvironment.hasMappings()) {
            // Relocation is applied in reobf, and when mappings are present they handle the relocation
            return name;
        }
        if (name.startsWith(LEGACY_CB_PACKAGE_PREFIX)) {
            return CB_PACKAGE_PREFIX + name.substring(LEGACY_CB_PACKAGE_PREFIX.length());
        }
        return name;
    }

    @Override
    public Class<?> defineClass(final Object loader, final byte[] b, final int off, final int len) throws ClassFormatError {
        return this.defineClassProxy.defineClass(loader, b, off, len);
    }

    @Override
    public Class<?> defineClass(final Object loader, final String name, final byte[] b, final int off, final int len) throws ClassFormatError {
        return this.defineClassProxy.defineClass(loader, name, b, off, len);
    }

    @Override
    public Class<?> defineClass(final Object loader, final @Nullable String name, final byte[] b, final int off, final int len, final @Nullable ProtectionDomain protectionDomain) throws ClassFormatError {
        return this.defineClassProxy.defineClass(loader, name, b, off, len, protectionDomain);
    }

    @Override
    public Class<?> defineClass(final Object loader, final String name, final ByteBuffer b, final ProtectionDomain protectionDomain) throws ClassFormatError {
        return this.defineClassProxy.defineClass(loader, name, b, protectionDomain);
    }

    @Override
    public Class<?> defineClass(final Object secureLoader, final String name, final byte[] b, final int off, final int len, final CodeSource cs) {
        return this.defineClassProxy.defineClass(secureLoader, name, b, off, len, cs);
    }

    @Override
    public Class<?> defineClass(final Object secureLoader, final String name, final ByteBuffer b, final CodeSource cs) {
        return this.defineClassProxy.defineClass(secureLoader, name, b, cs);
    }

    @Override
    public Class<?> defineClass(final MethodHandles.Lookup lookup, final byte[] bytes) throws IllegalAccessException {
        return this.defineClassProxy.defineClass(lookup, bytes);
    }

    // todo apply bytecode remap here as well
    private static byte[] processClass(final byte[] bytes) {
        try {
            return ReflectionRemapper.processClass(bytes);
        } catch (final Exception ex) {
            LOGGER.warn("Failed to process class bytes", ex);
            return bytes;
        }
    }
}
