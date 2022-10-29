package io.papermc.paper.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import net.neoforged.srgutils.IMappingFile;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public enum ObfHelper {
    INSTANCE;

    private final @Nullable Map<String, ClassMapping> mappingsByObfName;
    private final @Nullable Map<String, ClassMapping> mappingsByMojangName;

    ObfHelper() {
        final @Nullable Set<ClassMapping> maps = loadMappingsIfPresent();
        if (maps != null) {
            this.mappingsByObfName = maps.stream().collect(Collectors.toUnmodifiableMap(ClassMapping::obfName, map -> map));
            this.mappingsByMojangName = maps.stream().collect(Collectors.toUnmodifiableMap(ClassMapping::mojangName, map -> map));
        } else {
            this.mappingsByObfName = null;
            this.mappingsByMojangName = null;
        }
    }

    public @Nullable Map<String, ClassMapping> mappingsByObfName() {
        return this.mappingsByObfName;
    }

    public @Nullable Map<String, ClassMapping> mappingsByMojangName() {
        return this.mappingsByMojangName;
    }

    /**
     * Attempts to get the obf name for a given class by its Mojang name. Will
     * return the input string if mappings are not present.
     *
     * @param fullyQualifiedMojangName fully qualified class name (dotted)
     * @return mapped or original fully qualified (dotted) class name
     */
    public String reobfClassName(final String fullyQualifiedMojangName) {
        if (this.mappingsByMojangName == null) {
            return fullyQualifiedMojangName;
        }

        final ClassMapping map = this.mappingsByMojangName.get(fullyQualifiedMojangName);
        if (map == null) {
            return fullyQualifiedMojangName;
        }

        return map.obfName();
    }

    /**
     * Attempts to get the Mojang name for a given class by its obf name. Will
     * return the input string if mappings are not present.
     *
     * @param fullyQualifiedObfName fully qualified class name (dotted)
     * @return mapped or original fully qualified (dotted) class name
     */
    public String deobfClassName(final String fullyQualifiedObfName) {
        if (this.mappingsByObfName == null) {
            return fullyQualifiedObfName;
        }

        final ClassMapping map = this.mappingsByObfName.get(fullyQualifiedObfName);
        if (map == null) {
            return fullyQualifiedObfName;
        }

        return map.mojangName();
    }

    private static @Nullable Set<ClassMapping> loadMappingsIfPresent() {
        if (!MappingEnvironment.hasMappings()) {
            return null;
        }
        try (final InputStream mappingsInputStream = MappingEnvironment.mappingsStream()) {
            final IMappingFile mappings = IMappingFile.load(mappingsInputStream); // Mappings are mojang->spigot
            final Set<ClassMapping> classes = new HashSet<>();

            final StringPool pool = new StringPool();
            for (final IMappingFile.IClass cls : mappings.getClasses()) {
                final Map<String, String> methods = new HashMap<>();
                final Map<String, String> fields = new HashMap<>();
                final Map<String, String> strippedMethods = new HashMap<>();

                for (final IMappingFile.IMethod methodMapping : cls.getMethods()) {
                    methods.put(
                            pool.string(methodKey(
                                    Objects.requireNonNull(methodMapping.getMapped()),
                                    Objects.requireNonNull(methodMapping.getMappedDescriptor())
                            )),
                            pool.string(Objects.requireNonNull(methodMapping.getOriginal()))
                    );

                    strippedMethods.put(
                            pool.string(pool.string(strippedMethodKey(
                                    methodMapping.getMapped(),
                                    methodMapping.getDescriptor()
                            ))),
                            pool.string(methodMapping.getOriginal())
                    );
                }
                for (final IMappingFile.IField field : cls.getFields()) {
                    fields.put(
                            pool.string(field.getMapped()),
                            pool.string(field.getOriginal())
                    );
                }

                final ClassMapping map = new ClassMapping(
                        Objects.requireNonNull(cls.getMapped()).replace('/', '.'),
                        Objects.requireNonNull(cls.getOriginal()).replace('/', '.'),
                        Map.copyOf(methods),
                        Map.copyOf(fields),
                        Map.copyOf(strippedMethods)
                );
                classes.add(map);
            }

            return Set.copyOf(classes);
        } catch (final IOException ex) {
            System.err.println("Failed to load mappings.");
            ex.printStackTrace();
            return null;
        }
    }

    public static String strippedMethodKey(final String methodName, final String methodDescriptor) {
        final String methodKey = methodKey(methodName, methodDescriptor);
        final int returnDescriptorEnd = methodKey.indexOf(')');
        return methodKey.substring(0, returnDescriptorEnd + 1);
    }

    public static String methodKey(final String methodName, final String methodDescriptor) {
        return methodName + methodDescriptor;
    }

    public record ClassMapping(
            String obfName,
            String mojangName,
            Map<String, String> methodsByObf,
            Map<String, String> fieldsByObf,
            // obf name with mapped desc to mapped name. return value is excluded from desc as reflection doesn't use it
            Map<String, String> strippedMethods
    ) {}
}
