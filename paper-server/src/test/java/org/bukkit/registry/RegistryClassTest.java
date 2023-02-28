package org.bukkit.registry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.google.common.base.Joiner;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import java.util.stream.Stream;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlags;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.MinecraftExperimental;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.AllFeatures;
import org.bukkit.support.extension.AllFeaturesExtension;
import org.bukkit.support.provider.RegistriesArgumentProvider;
import org.bukkit.support.test.RegistriesTest;
import org.jetbrains.annotations.ApiStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.internal.util.MockUtil;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Note: This test class assumes that feature flags only enable more features and do not disable vanilla ones.
 */
@AllFeatures
@org.junit.jupiter.api.Disabled // Paper - disabled for now as it constructs a second root registry, which is not supported on paper
public class RegistryClassTest {

    private static final Map<Class<? extends Keyed>, Data> INIT_DATA = new HashMap<>();
    private static final List<Arguments> FIELD_DATA_CACHE = new ArrayList<>();
    private static RegistryAccess.Frozen vanilla_registry;

    public static Stream<Arguments> fieldData() {
        return RegistryClassTest.FIELD_DATA_CACHE.stream();
    }

    @BeforeAll
    public static void init() {
        RegistryClassTest.initValueClasses();
        RegistryClassTest.initFieldDataCache();
    }

    @AfterAll
    public static void cleanUp() {
        RegistryClassTest.INIT_DATA.clear();
        RegistryClassTest.FIELD_DATA_CACHE.clear();
    }

    private static void initValueClasses() {
        RegistryClassTest.vanilla_registry = RegistryHelper.createRegistry(FeatureFlags.VANILLA_SET);

        Map<Class<? extends Keyed>, List<NamespacedKey>> outsideRequest = new LinkedHashMap<>();

        // First init listening for outside requests
        RegistriesArgumentProvider.getData()
                .map(Arguments::get).map(args -> args[0])
                .map(type -> (Class<? extends Keyed>) type)
                .forEach(type -> {
                    Registry<?> spyRegistry = Bukkit.getRegistry(type);
                    spyOutsideRequests(outsideRequest, type, spyRegistry);
                });

        // Init all registries and recorde the outcome
        RegistriesArgumentProvider.getData()
                .map(Arguments::get)
                .map(args -> args[0])
                .map(type -> (Class<? extends Keyed>) type)
                .forEachOrdered(type -> {
                    try {
                        Registry<?> spyRegistry = Bukkit.getRegistry(type);
                        Registry<?> realRegistry = AllFeaturesExtension.getRealRegistry(type);

                        Set<NamespacedKey> nullAble = new HashSet<>();
                        Set<NamespacedKey> notNullAble = new HashSet<>();
                        Set<NamespacedKey> nullValue = new HashSet<>();

                        outsideRequest.clear();
                        MockUtil.resetMock(spyRegistry);
                        doAnswer(invocation -> {
                            Keyed item = realRegistry.get((NamespacedKey) invocation.getArgument(0)); // Paper - registry modification api - specifically call namespaced key overload

                            if (item == null) {
                                nullValue.add(invocation.getArgument(0));
                            }

                            nullAble.add(invocation.getArgument(0));

                            return item;
                        }).when(spyRegistry).get((NamespacedKey) any()); // Paper - registry modification api - specifically call namespaced key overload

                        doAnswer(invocation -> {
                            Keyed item = realRegistry.get((NamespacedKey) invocation.getArgument(0)); // Paper - registry modification api - specifically call namespaced key overload

                            if (item == null) {
                                nullValue.add(invocation.getArgument(0));

                                // We return a mock here so that it (hopefully) does not error out immediately.
                                // It may cause errors in other tests, but this test class should always pass before other tests can run safely.
                                // This is not necessary in the normal get method since the API contract states that the method can return null,
                                // whereas in this method, it is guaranteed to be non-null.
                                item = mock(type);
                            }

                            notNullAble.add(invocation.getArgument(0));

                            return item;
                        }).when(spyRegistry).getOrThrow((NamespacedKey) any()); // Paper - registry modification api - specifically call namespaced key overload

                        // Load class
                        try {
                            Class.forName(type.getName());
                        } catch (Throwable e) {
                            e.printStackTrace(); // Print stacktrace, since JUnit eats the error in BeforeAll
                            fail(e);
                        } finally {
                            MockUtil.resetMock(spyRegistry);
                            spyOutsideRequests(outsideRequest, type, spyRegistry);
                        }

                        INIT_DATA.put(type, new Data(nullAble, notNullAble, nullValue, new LinkedHashMap<>(outsideRequest)));
                    } catch (Throwable e) {
                        e.printStackTrace(); // Print stacktrace, since JUnit eats the error in BeforeAll
                        fail(e);
                    }
                });

        // Cleanup
        RegistriesArgumentProvider.getData()
                .map(Arguments::get)
                .map(args -> args[0])
                .map(type -> (Class<? extends Keyed>) type)
                .forEach(type -> MockUtil.resetMock(Bukkit.getRegistry(type)));
    }

    private static void spyOutsideRequests(Map<Class<? extends Keyed>, List<NamespacedKey>> outsideRequest, Class<? extends Keyed> type, Registry<?> spyRegistry) {
        doAnswer(invocation -> {
            outsideRequest
                    .computeIfAbsent(type, ty -> new ArrayList<>()).add(invocation.getArgument(0));
            return mock(type);
        }).when(spyRegistry).get((NamespacedKey) any()); // Paper - registry modification api - specifically call namespaced key overload

        doAnswer(invocation -> {
            outsideRequest
                    .computeIfAbsent(type, ty -> new ArrayList<>()).add(invocation.getArgument(0));
            return mock(type);
        }).when(spyRegistry).getOrThrow((NamespacedKey) any()); // Paper - registry modification api - specifically call namespaced key overload
    }

    private static void initFieldDataCache() {
        RegistriesArgumentProvider.getData().map(arguments -> {
            Class<? extends Keyed> type = (Class<? extends Keyed>) arguments.get()[0];
            Map<String, List<Class<? extends Annotation>>> annotations = RegistryClassTest.getFieldAnnotations(type);

            List<FieldData> fields = new ArrayList<>();
            for (Field field : type.getFields()) {
                // We ignore each field that does not have the class itself as its type,
                // is not static, public, or is deprecated.
                if (!RegistryClassTest.isValidField(type, field)) {
                    continue;
                }

                Keyed keyed;
                try {
                    keyed = (Keyed) field.get(null);
                } catch (IllegalAccessException e) {
                    Logger.getGlobal().warning(String.format("Something went wrong while trying to read the field %s from class %s.\n"
                            + "Please see the stack trace below for more information.", field, type));
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

                if (keyed == null) {
                    // We have a test case for those scenarios,
                    // so we ignore them here to ensure that at least the other fields are being tested.
                    continue;
                }

                if (MockUtil.isMock(keyed)) {
                    // If it is a mock, it means that there was no Minecraft registry item for that key.
                    // In this case, ignore it, as we already test and inform about this in another test.
                    continue;
                }

                fields.add(new FieldData(field, annotations.computeIfAbsent(field.getName(), k -> new ArrayList<>())));
            }

            return Arguments.arguments(arguments.get()[0], arguments.get()[1], fields);
        }).forEach(FIELD_DATA_CACHE::add);
    }

    private static Map<String, List<Class<? extends Annotation>>> getFieldAnnotations(Class<?> aClass) {
        Map<String, List<Class<? extends Annotation>>> annotation = new HashMap<>();

        try (JarFile jarFile = new JarFile(new File(aClass.getProtectionDomain().getCodeSource().getLocation().toURI()))) {
            JarEntry jarEntry = jarFile.getJarEntry(aClass.getName().replace('.', '/') + ".class");

            try (InputStream inputStream = jarFile.getInputStream(jarEntry)) {
                ClassReader classReader = new ClassReader(inputStream);
                classReader.accept(new ClassVisitor(Opcodes.ASM9) {
                    @Override
                    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                        return new FieldVisitor(Opcodes.ASM9) {
                            @Override
                            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                                try {
                                    annotation.computeIfAbsent(name, k -> new ArrayList<>()).add((Class<? extends Annotation>) Class.forName(Type.getType(descriptor).getClassName()));
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                    throw new RuntimeException(e);
                                }
                                return null;
                            }
                        };
                    }
                }, Opcodes.ASM9);
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return annotation;
    }

    @RegistriesTest
    public <T extends Keyed> void testOutsideRequests(Class<T> type) {
        Data data = RegistryClassTest.INIT_DATA.get(type);

        assertNotNull(data, String.format("No data present for %s. This should never happen since the same data source is used.\n"
                + "Something has gone horribly wrong.", type));

        assertTrue(data.outsideRequests.isEmpty(), String.format("""
                There were outside registry accesses while loading registry items for class %s.
                This can happen if you try to read any registry item in the constructor.
                For ease of testing, please remove this from the constructor.
                Subsequent tests may fail because of this.

                You can use a Supplier instead. For easy caching, you can use com.google.common.base.Suppliers#memoize(Supplier)
                Example:

                    private final Supplier<RegisterItem> otherRegisterItem;

                    public CraftRegisterItem([...]) {
                        [...]
                        this.otherRegisterItem = Suppliers.memoize(() -> CraftRegisterItem.getSome("other_key"));
                    }

                    public RegisterItem getOtherRegisterItem() {
                        return otherRegisterItem.get();
                    }
                The following outside requests were registered:
                %s""", type.getName(), Joiner.on('\n').withKeyValueSeparator(" <-> ").join(data.outsideRequests)));
    }

    @RegistriesTest
    public <T extends Keyed> void testNoNullValuePresent(Class<T> type) {
        Data data = RegistryClassTest.INIT_DATA.get(type);

        assertNotNull(data, String.format("No data present for %s. This should never happen since the same data source is used.\n"
                + "Something has gone horribly wrong.", type));

        assertTrue(data.nullValue.isEmpty(), String.format("""
                %s tried to get registry items that are not present.
                This can be caused if the affected registry item was removed, renamed, or if you mistyped the name.
                Alternatively, there maybe was an attempt to read another registry item in the constructor of a registry item.
                Subsequent tests may fail because of this.
                The following registry items were requested, but there is no registry item for them:
                %s""", type, Joiner.on('\n').join(data.nullValue)));
    }

    @RegistriesTest
    public <T extends Keyed> void testForNullValues(Class<T> type) throws IllegalAccessException {
        List<Field> nullFields = new ArrayList<>();

        for (Field field : type.getFields()) {
            // We ignore each field that does not have the class itself as its type, is not static, public, or is deprecated.
            if (!RegistryClassTest.isValidField(type, field)) {
                continue;
            }

            Keyed keyed = (Keyed) field.get(null);

            if (keyed == null) {
                // This should not happen since, even if the item is not present in the Minecraft registry, we would use a mock.
                nullFields.add(field);
            }
        }

        assertTrue(nullFields.isEmpty(), String.format("""
                The class %s has fields with no value assigned to them.
                This should not normally happen, since all feature flags are set,
                and a dummy is returned in cases where the registry item does not exist.
                Please make sure the following fields are not null:
                %s""", type, Joiner.on('\n').join(nullFields)));
    }

    @ParameterizedTest
    @MethodSource("fieldData")
    public <T extends Keyed> void testExcessExperimentalAnnotation(Class<T> type, ResourceKey<net.minecraft.core.Registry<?>> registryKey, List<FieldData> fieldDataList) throws IllegalAccessException {
        List<Field> excess = new ArrayList<>();

        for (FieldData fieldData : fieldDataList) {
            if (!fieldData.annotations().contains(MinecraftExperimental.class) && !fieldData.annotations().contains(ApiStatus.Experimental.class)) {
                // No annotation -> no problem
                continue;
            }

            net.minecraft.core.Registry<?> registry = RegistryClassTest.vanilla_registry.lookupOrThrow(registryKey);

            Optional<?> optionalValue = registry.getOptional(CraftNamespacedKey.toMinecraft(((Keyed) fieldData.field().get(null)).getKey()));

            if (optionalValue.isEmpty()) {
                // The value is not present, which means it comes from a feature flag.
                continue;
            }

            Object value = optionalValue.get();

            if (value instanceof FeatureElement featureElement && !featureElement.isEnabled(FeatureFlags.VANILLA_SET)) {
                // It is a FeatureElement that is not enabled for vanilla FeatureSet so ignore it.
                continue;
            }

            excess.add(fieldData.field());
        }

        assertTrue(excess.isEmpty(), String.format("""
                The class %s has fields with the MinecraftExperimental and/or ApiStatus.Experimental annotation.
                However, the value is present and active with the vanilla feature flags.
                The annotation should be removed from the following fields:
                %s""", type, Joiner.on('\n').join(excess)));
    }

    @ParameterizedTest
    @MethodSource("fieldData")
    public <T extends Keyed> void testMissingExperimentalAnnotation(Class<T> type, ResourceKey<net.minecraft.core.Registry<?>> registryKey, List<FieldData> fieldDataList) throws IllegalAccessException {
        net.minecraft.core.Registry<?> registry = RegistryClassTest.vanilla_registry.lookupOrThrow(registryKey);
        List<Field> missing = new ArrayList<>();

        for (FieldData fieldData : fieldDataList) {
            if (fieldData.annotations().contains(MinecraftExperimental.class) && fieldData.annotations().contains(ApiStatus.Experimental.class)) {
                // Annotation present -> no problem
                continue;
            }

            Optional<?> optionalValue = registry.getOptional(CraftNamespacedKey.toMinecraft(((Keyed) fieldData.field().get(null)).getKey()));

            if (optionalValue.isEmpty()) {
                // The value is not present, which means it comes from a feature flag.
                missing.add(fieldData.field());
                continue;
            }

            Object value = optionalValue.get();

            if (value instanceof FeatureElement featureElement && !featureElement.isEnabled(FeatureFlags.VANILLA_SET)) {
                // It is a FeatureElement that is not enabled for vanilla FeatureSet.
                missing.add(fieldData.field());
            }
        }

        assertTrue(missing.isEmpty(), String.format("""
                The class %s has fields that don't have the MinecraftExperimental and/or ApiStatus.Experimental annotation.
                However, the value is not present or active with the vanilla feature flags.
                The annotation should be added to the following fields: %s
                """, type, Joiner.on('\n').join(missing)));
    }

    @ParameterizedTest
    @MethodSource("fieldData")
    public <T extends Keyed> void testExcessNullCheck(Class<T> type, ResourceKey<net.minecraft.core.Registry<?>> registryKey, List<FieldData> fieldDataList) throws IllegalAccessException {
        net.minecraft.core.Registry<?> registry = RegistryClassTest.vanilla_registry.lookupOrThrow(registryKey);
        List<Field> excess = new ArrayList<>();
        Data data = RegistryClassTest.INIT_DATA.get(type);

        for (FieldData fieldData : fieldDataList) {
            NamespacedKey key = ((Keyed) fieldData.field().get(null)).getKey();

            Optional<?> optionalValue = registry.getOptional(CraftNamespacedKey.toMinecraft(key));

            if (optionalValue.isPresent()) {
                // The value is present, which means it cannot have an unnecessary null check, so ignore it.
                continue;
            }

            if (data.notNullAble.contains(key)) {
                // The value is null, but there was a null check, which it should not have.
                excess.add(fieldData.field());
            }
        }

        assertTrue(excess.isEmpty(), String.format("""
                The class %s has fields that do have a null check.
                However, the value can be null, and there should not be a null check, so that it won't error out, and an IDE can handle it accordingly.
                If there is no null check, make sure that org.bukkit.Registry#get(NamespaceKey) is used.
                The following fields do have a null check:
                %s""", type, Joiner.on('\n').join(excess)));
    }

    @ParameterizedTest
    @MethodSource("fieldData")
    public <T extends Keyed> void testMissingNullCheck(Class<T> type, ResourceKey<net.minecraft.core.Registry<?>> registryKey, List<FieldData> fieldDataList) throws IllegalAccessException {
        net.minecraft.core.Registry<?> registry = RegistryClassTest.vanilla_registry.lookupOrThrow(registryKey);
        List<Field> missing = new ArrayList<>();
        Data data = RegistryClassTest.INIT_DATA.get(type);

        for (FieldData fieldData : fieldDataList) {
            NamespacedKey key = ((Keyed) fieldData.field().get(null)).getKey();

            Optional<?> optionalValue = registry.getOptional(CraftNamespacedKey.toMinecraft(key));

            if (optionalValue.isEmpty()) {
                // The value is not present, which means there is no need for a null check, so ignore it.
                continue;
            }

            if (data.nullAble.contains(key)) {
                // The value is not null, and there was no null check, which means it is missing.
                missing.add(fieldData.field());
            }
        }

        assertTrue(missing.isEmpty(), String.format("""
                The class %s has fields that don't have a null check.
                However, the value cannot be null, and there should be a null check so that an IDE can handle it accordingly.
                If there is a null check, make sure that org.bukkit.Registry#getOrThrow(NamespaceKey) is used.
                The following fields don't have a null check:
                %s""", type, Joiner.on('\n').join(missing)));
    }

    @ParameterizedTest
    @MethodSource("fieldData")
    public <T extends Keyed> void testMatchingFieldNames(Class<T> type, ResourceKey<net.minecraft.core.Registry<?>> registryKey, List<FieldData> fieldDataList) throws IllegalAccessException {
        Map<Field, NamespacedKey> notMatching = new LinkedHashMap<>();

        for (FieldData fieldData : fieldDataList) {
            NamespacedKey key = ((Keyed) fieldData.field().get(null)).getKey();

            if (fieldData.field().getName().equals(this.convertToFieldName(key.getKey()))) {
                continue;
            }

            // Field names to not match.
            notMatching.put(fieldData.field(), key);
        }

        assertTrue(notMatching.isEmpty(), String.format("""
                The class %s has fields whose keys don't match up.
                The names of each field should match up with their key.
                If there is a '.' in the key, it should be replaced with '_' in the field name.
                If the registry item no longer exists and is replaced by a new one,
                add the @Deprecated annotation to the field and create a new field for the new registry item.
                The following fields have mismatching keys:
                %s""", type, Joiner.on('\n').withKeyValueSeparator(" <-> ").join(notMatching)));
    }

    @ParameterizedTest
    @MethodSource("fieldData")
    public <T extends Keyed> void testMissingConstants(Class<T> type, ResourceKey<net.minecraft.core.Registry<?>> registryKey) throws IllegalAccessException {
        net.minecraft.core.Registry<Object> registry = RegistryHelper.getRegistry().lookupOrThrow(registryKey);
        List<ResourceLocation> missingKeys = new ArrayList<>();

        for (Object nmsObject : registry) {
            ResourceLocation minecraftKey = registry.getKey(nmsObject);

            try {
                Field field = type.getField(this.convertToFieldName(minecraftKey.getPath()));

                // Only fields which are not Deprecated
                // and have the right registry item associated with the field count.
                if (!RegistryClassTest.isValidField(type, field)) {
                    missingKeys.add(minecraftKey);
                    continue;
                }

                T keyed = (T) field.get(null);

                if (keyed == null) {
                    missingKeys.add(minecraftKey);
                    continue;
                }

                if (!keyed.getKey().equals(CraftNamespacedKey.fromMinecraft(minecraftKey))) {
                    missingKeys.add(minecraftKey);
                }
            } catch (NoSuchFieldException e) {
                missingKeys.add(minecraftKey);
            }
        }

        assertTrue(missingKeys.isEmpty(), String.format("""
                The class %s has missing fields for some registry items.
                There should be a field for each registry item.
                In case there is a field with the correct name (not misspelled), make sure that:
                The field is not null, the field is not annotated with @Deprecated, the field has the right registry item key.
                The following registry items don't have a field:
                %s""", type, Joiner.on('\n').join(missingKeys)));
    }

    private String convertToFieldName(String value) {
        return switch (value) {
            // JukeboxSong does have keys which start with a number, which is not a valid field name
            case "5" -> "FIVE";
            case "11" -> "ELEVEN";
            case "13" -> "THIRTEEN";
            default -> value.toUpperCase(Locale.ROOT).replace('.', '_');
        };
    }

    private static boolean isValidField(Class<? extends Keyed> type, Field field) {
        return type.isAssignableFrom(field.getType()) && Modifier.isStatic(field.getModifiers())
                && Modifier.isPublic(field.getModifiers()) && !field.isAnnotationPresent(Deprecated.class);
    }

    private record Data(Set<NamespacedKey> nullAble, Set<NamespacedKey> notNullAble,
                        Set<NamespacedKey> nullValue,
                        Map<Class<? extends Keyed>, List<NamespacedKey>> outsideRequests) {
    }

    private record FieldData(Field field, List<Class<? extends Annotation>> annotations) {
    }
}
