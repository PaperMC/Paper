package io.papermc.paper.loot;

import io.papermc.paper.math.Position;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.loot.LootContext;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@VanillaFeature
class LootTableValidationTest {

    private static final World WORLD = mock(World.class);

    private static CraftLootTable table(final net.minecraft.util.context.ContextKeySet paramSet) {
        return new CraftLootTable(NamespacedKey.minecraft("test"), LootTable.lootTable().setParamSet(paramSet).build());
    }

    @Test
    void testMissingRequiredKey() {
        final CraftLootTable chest = table(LootContextParamSets.CHEST);
        assertFalse(chest.isValidContext(LootContext.builder(WORLD).build()));
        assertFalse(chest.isValidContext(LootContext.builder(WORLD)));
    }

    @Test
    void testRequiredKeysPresent() {
        final CraftLootTable chest = table(LootContextParamSets.CHEST);
        final LootContext.Builder builder = LootContext.builder(WORLD).with(LootContextKeys.ORIGIN, Position.block(0, 0, 0));
        assertTrue(chest.isValidContext(builder));
        assertTrue(chest.isValidContext(builder.build()));
    }

    @Test
    void testDisallowedKeysAreIgnored() {
        // tool is not allowed by the chest param set, it is dropped when converting to vanilla
        final CraftLootTable chest = table(LootContextParamSets.CHEST);
        final LootContext.Builder builder = LootContext.builder(WORLD)
            .with(LootContextKeys.ORIGIN, Position.block(0, 0, 0))
            .with(LootContextKeys.EXPLOSION_RADIUS, 1F);
        assertTrue(chest.isValidContext(builder));
    }

    @Test
    void testInternalRequiredKey() {
        // the generic param set (the default for loot tables) requires the container key, which is not exposed in the api
        final CraftLootTable generic = table(LootContextParamSets.ALL_PARAMS);
        assertTrue(LootContextParamSets.ALL_PARAMS.required().contains(LootContextParams.CONTAINER));
        assertDoesNotThrow(generic::requiredContextKeys);
        assertDoesNotThrow(generic::allowedContextKeys);

        final LootContext.Builder builder = LootContext.builder(WORLD);
        for (final LootContextKey key : generic.requiredContextKeys()) {
            if (key instanceof final LootContextKey.NonValued nonValued) {
                builder.with(nonValued);
            }
        }
        assertFalse(generic.isValidContext(builder), "a context can never supply the container key");
    }
}
