package io.papermc.paper.loot;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@VanillaFeature
class LootContextKeyTest { // todo do not fail on first error

    static Map<String, ContextKey<?>> vanillaParams = new HashMap<>();

    @BeforeAll
    static void collectVanillaContextParams() throws ReflectiveOperationException {
        Class.forName(LootContextKey.class.getName()); // force-load class
        for (final Field field : LootContextParams.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && field.getType().equals(ContextKey.class)) {
                vanillaParams.put(field.getName(), (ContextKey<?>) field.get(null));
            }
        }
    }

    @Test
    void testMinecraftToApi() {
        assertFalse(vanillaParams.isEmpty());
        vanillaParams.forEach((fieldName, lootContextParam) -> {
            final List<LootContextKey<?>> matching = LootContextKeyImpl.KEYS.stream().filter(k -> k.key().asString().equals(lootContextParam.name().toString())).toList();
            assertEquals(1, matching.size(), "Did not find 1 matching context key for " + lootContextParam.name());
        });
    }

    @Test
    void testApiToMinecraft() {
        assertNotEquals(0, LootContextKeyImpl.KEYS.size());
        LootContextKeyImpl.KEYS.forEach(lootContextKey -> {
            final List<ContextKey<?>> matching = vanillaParams.values().stream().filter(p -> p.name().toString().equals(lootContextKey.key().asString())).toList();
            assertEquals(1, matching.size(), "Did not find 1 matching loot param for " + lootContextKey.key());
        });
    }
}
