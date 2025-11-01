package org.bukkit.registry;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.lang.model.SourceVersion;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.Keyed;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.AllFeatures;
import org.bukkit.support.provider.RegistriesArgumentProvider;
import org.junit.jupiter.params.ArgumentCountValidationMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@AllFeatures
public class RegistryConstantsTest {

    public static Stream<Arguments> registries() {
        return RegistriesArgumentProvider.getData()
            .filter(args -> args.registryKey() != Registries.DATA_COMPONENT_TYPE) // already has its own test
            .map(args -> Arguments.of(args.api(), args.apiHolder(), args.registryKey()));
    }

    @MethodSource("registries")
    @ParameterizedTest(argumentCountValidation = ArgumentCountValidationMode.NONE)
    public <T extends Keyed> void testConstantNames(Class<T> api, Class<?> apiHolder) throws IllegalAccessException {
        Set<String> badNames = new HashSet<>();

        for (Field field : apiHolder.getDeclaredFields()) {
            if (!field.getType().isAssignableFrom(api) || !Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            if (field.isAnnotationPresent(Deprecated.class)) {
                continue;
            }

            String name = field.getName();
            String expectedName = formatKeyAsField(((T) field.get(null)).key().value());
            if (!SourceVersion.isIdentifier(expectedName)) {
                continue; // skip invalid names for now (for JukeboxSong)
            }
            if (!name.equals(expectedName)) {
                badNames.add(name);
            }
        }

        assertTrue(badNames.isEmpty(), badNames.size() + " bad constants(s) in " + apiHolder.getSimpleName() + " that do not match their expected names: " + badNames);
    }

    @MethodSource("registries")
    @ParameterizedTest(argumentCountValidation = ArgumentCountValidationMode.NONE)
    public <T extends Keyed, M> void testMissingConstants(Class<T> api, Class<?> apiHolder, ResourceKey<net.minecraft.core.Registry<M>> registryKey) throws IllegalAccessException {
        Set<ResourceLocation> missing = new HashSet<>();

        for (Holder.Reference<M> reference : RegistryHelper.getRegistry().lookupOrThrow(registryKey).listElements().toList()) {
            ResourceLocation key = reference.key().location();
            String name = formatKeyAsField(key.getPath());
            if (!SourceVersion.isIdentifier(name)) {
                continue; // skip invalid names for now (for JukeboxSong)
            }
            try {
                apiHolder.getField(name).get(null);
            } catch (NoSuchFieldException e) {
                missing.add(key);
            }
        }

        assertTrue(missing.isEmpty(), "Missing (" + missing.size() + ") constants in " + apiHolder.getSimpleName() + ": " + missing);
    }

    private static final Pattern ILLEGAL_FIELD_CHARACTERS = Pattern.compile("[.-/]");

    private static String formatKeyAsField(String path) {
        return ILLEGAL_FIELD_CHARACTERS.matcher(path.toUpperCase(Locale.ENGLISH)).replaceAll("_");
    }
}
