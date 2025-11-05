package io.papermc.paper.datacomponent;

import com.google.common.collect.Collections2;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@AllFeatures
public class DataComponentTypesTest {

    private static final Set<Identifier> NOT_IN_API = Set.of(
        Identifier.parse("custom_data"),
        Identifier.parse("entity_data"),
        Identifier.parse("bees"),
        Identifier.parse("debug_stick_state"),
        Identifier.parse("block_entity_data"),
        Identifier.parse("bucket_entity_data"),
        Identifier.parse("lock"),
        Identifier.parse("creative_slot_lock")
    );

    @Test
    public void testAllDataComponentsAreMapped() throws IllegalAccessException {
        final Set<Identifier> vanillaDataComponentTypes = new ObjectOpenHashSet<>(
            RegistryHelper.getRegistry()
                .lookupOrThrow(Registries.DATA_COMPONENT_TYPE)
                .keySet()
        );

        for (final Field declaredField : DataComponentTypes.class.getDeclaredFields()) {
            if (!DataComponentType.class.isAssignableFrom(declaredField.getType())) continue;

            final DataComponentType dataComponentType = (DataComponentType) declaredField.get(null);
            if (!vanillaDataComponentTypes.remove(CraftNamespacedKey.toMinecraft(dataComponentType.getKey()))) {
                Assertions.fail("API defined component type " + dataComponentType.key().asMinimalString() + " is unknown to vanilla registry");
            }
        }

        if (!vanillaDataComponentTypes.containsAll(NOT_IN_API)) {
            Assertions.fail("API defined data components that were marked as not-yet-implemented: " + NOT_IN_API.stream().filter(Predicate.not(vanillaDataComponentTypes::contains)).map(Identifier::toString).collect(Collectors.joining(", ")));
        }

        vanillaDataComponentTypes.removeAll(NOT_IN_API);
        if (!vanillaDataComponentTypes.isEmpty()) {
            Assertions.fail("API did not define following vanilla data component types: " + String.join(", ", Collections2.transform(vanillaDataComponentTypes, Identifier::toString)));
        }
    }

}
