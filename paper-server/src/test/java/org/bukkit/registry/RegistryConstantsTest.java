package org.bukkit.registry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.AllFeatures;
import org.bukkit.support.provider.RegistriesArgumentProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@AllFeatures
public class RegistryConstantsTest {

    private static final Multimap<ResourceKey<? extends Registry<?>>, Identifier> IGNORED_KEYS = HashMultimap.create();

    private static <M> void ignore(ResourceKey<? extends Registry<M>> registryKey, Set<M> elements) {
        Registry<M> registry = RegistryHelper.registryAccess().lookupOrThrow(registryKey);
        IGNORED_KEYS.putAll(registryKey, elements.stream().map(registry::getKey).toList());
    }

    private static <M> void ignoreKeys(ResourceKey<? extends Registry<M>> registryKey, Set<ResourceKey<? extends M>> keys) {
        IGNORED_KEYS.putAll(registryKey, keys.stream().map(ResourceKey::identifier).toList());
    }

    private static <M> void ignoreReferences(ResourceKey<? extends Registry<M>> registryKey, Set<Holder.Reference<? extends M>> references) {
        IGNORED_KEYS.putAll(registryKey, references.stream().map(holder -> holder.key().identifier()).toList());
    }

    @BeforeAll
    public static void populateIgnored() {
        ignore(Registries.DATA_COMPONENT_TYPE, Set.of(
            DataComponents.CUSTOM_DATA,
            DataComponents.ENTITY_DATA,
            DataComponents.BEES,
            DataComponents.DEBUG_STICK_STATE,
            DataComponents.BLOCK_ENTITY_DATA,
            DataComponents.BUCKET_ENTITY_DATA,
            DataComponents.LOCK,
            DataComponents.CREATIVE_SLOT_LOCK
        ));
    }

    public static Stream<Arguments> registries() {
        return RegistriesArgumentProvider.getData()
            .map(args -> Arguments.of(args.api(), args.apiHolder(), args.registryKey()));
    }

    @MethodSource("registries")
    @ParameterizedTest
    public <B extends Keyed, M> void testConstants(Class<B> api, Class<?> apiHolder, ResourceKey<? extends Registry<M>> registryKey) throws IllegalAccessException {
        final Set<Identifier> keys = new ObjectOpenHashSet<>(
            RegistryHelper.registryAccess().lookupOrThrow(registryKey).keySet()
        );

        for (final Field field : apiHolder.getDeclaredFields()) {
            if (!api.isAssignableFrom(field.getType())) continue;
            if (field.isAnnotationPresent(Deprecated.class)) continue;

            final B element = api.cast(field.get(null));
            if (!keys.remove(CraftNamespacedKey.toMinecraft(element.getKey()))) {
                fail("Constant " + apiHolder.getSimpleName() + " / " + field.getName() + " is unknown to vanilla registry");
            }
        }

        final Collection<Identifier> ignoredKeys = IGNORED_KEYS.get(registryKey);
        if (!ignoredKeys.isEmpty()) {
            if (!keys.containsAll(ignoredKeys)) {
                Set<Identifier> extraKeys = ignoredKeys.stream().filter(Predicate.not(keys::contains)).collect(Collectors.toCollection(ObjectOpenHashSet::new));
                fail("Constants that should be ignored in " + apiHolder.getSimpleName() + ": " + extraKeys);
            }
            keys.removeAll(ignoredKeys);
        }

        assertTrue(keys.isEmpty(), "Missing (" + keys.size() + ") constants in " + apiHolder.getSimpleName() + ": " +  keys);
    }
}
