package io.papermc.paper.loot;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@VanillaFeature
class LootContextKeyTest {

    @BeforeAll
    static void setup() throws ClassNotFoundException {
        Class.forName(LootContextKeys.class.getName()); // force-load class
    }

    static Set<ContextKey<?>> contextParams() throws ReflectiveOperationException {
        final Set<ContextKey<?>> keys = new HashSet<>();
        for (final Field field : LootContextParams.class.getDeclaredFields()) { // SlotDisplayContext keys are only used by the client
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && field.getType().equals(ContextKey.class)) {
                keys.add((ContextKey<?>) field.get(null));
            }
        }

        assertFalse(keys.isEmpty());
        return keys;
    }

    static Collection<LootContextKey> contextKeys() {
        assertFalse(LootContextKeyImpl.KEYS.isEmpty());
        return LootContextKeyImpl.KEYS.values();
    }

    @ParameterizedTest
    @MethodSource("contextParams")
    void testVanillaToApi(final ContextKey<?> key) {
        assertNotNull(PaperLootContext.Key.CONVERTER.fromVanilla(key), "Did not find api equivalent for context key " + key.name());
    }

    @ParameterizedTest
    @MethodSource("contextKeys")
    void testApiToVanilla(final LootContextKey key) {
        assertNotNull(PaperLootContext.Key.CONVERTER.toVanilla(key), "Did not find internal equivalent for context key " + key.key().asString());
    }
}
