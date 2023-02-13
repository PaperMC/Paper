package org.bukkit.registry;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.damage.DamageType;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

@AllFeatures
public class RegistryConstantsTest {

    @Test
    public void testDamageType() {
        this.testExcessConstants(DamageType.class, Registry.DAMAGE_TYPE);
        this.testMissingConstants(DamageType.class, Registries.DAMAGE_TYPE); // Paper - re-enable this one
    }

    @Test
    public void testTrimMaterial() {
        this.testExcessConstants(TrimMaterial.class, org.bukkit.Registry.TRIM_MATERIAL); // Paper - remap fix
        this.testMissingConstants(TrimMaterial.class, Registries.TRIM_MATERIAL);
    }

    @Test
    public void testTrimPattern() {
        this.testExcessConstants(TrimPattern.class, org.bukkit.Registry.TRIM_PATTERN); // Paper - remap fix
        this.testMissingConstants(TrimPattern.class, Registries.TRIM_PATTERN);
    }

    private <T extends Keyed> void testExcessConstants(Class<T> clazz, org.bukkit.Registry<T> registry) { // Paper - remap fix
        List<NamespacedKey> excessKeys = new ArrayList<>();

        for (Field field : clazz.getFields()) {
            if (field.getType() != clazz || !Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            String name = field.getName();
            NamespacedKey key = NamespacedKey.fromString(name.toLowerCase(Locale.ROOT));
            if (registry.get(key) == null) {
                excessKeys.add(key);
            }

        }

        assertTrue(excessKeys.isEmpty(), excessKeys.size() + " excess constants(s) in " + clazz.getSimpleName() + " that do not exist: " + excessKeys);
    }

    private <T extends Keyed, M> void testMissingConstants(Class<T> clazz, ResourceKey<net.minecraft.core.Registry<M>> nmsRegistryKey) {
        List<ResourceLocation> missingKeys = new ArrayList<>();

        net.minecraft.core.Registry<M> nmsRegistry = RegistryHelper.getRegistry().lookupOrThrow(nmsRegistryKey);
        for (M nmsObject : nmsRegistry) {
            ResourceLocation minecraftKey = nmsRegistry.getKey(nmsObject);

            try {
                @SuppressWarnings("unchecked")
                T bukkitObject = (T) clazz.getField(minecraftKey.getPath().toUpperCase(Locale.ROOT)).get(null);

                assertEquals(minecraftKey, CraftNamespacedKey.toMinecraft(bukkitObject.getKey()), "Keys are not the same for " + minecraftKey);
            } catch (NoSuchFieldException e) {
                missingKeys.add(minecraftKey);
            } catch (Exception e) {
                fail(e.getMessage());
            }
        }

        assertTrue(missingKeys.isEmpty(), "Missing (" + missingKeys.size() + ") constants in " + clazz.getSimpleName() + ": " + missingKeys);
    }
}
