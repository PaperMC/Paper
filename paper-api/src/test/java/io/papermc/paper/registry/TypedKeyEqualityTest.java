package io.papermc.paper.registry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemType;
import org.junit.jupiter.api.Test;

class TypedKeyEqualityTest {

    @Test
    void typedKeyIsSymmetricWithPlainKey() {
        final Key key = Key.key("minecraft:stone");
        final TypedKey<ItemType> typedKey = TypedKey.create(RegistryKey.ITEM, key);

        // equals must be symmetric across the Key hierarchy
        assertTrue(key.equals(typedKey), "plain Key should equal an equal TypedKey");
        assertTrue(typedKey.equals(key), "TypedKey should equal an equal plain Key");
        assertEquals(key.equals(typedKey), typedKey.equals(key), "equals must be symmetric");

        // equal keys must share a hash code
        assertEquals(key.hashCode(), typedKey.hashCode());
    }

    @Test
    void typedKeysDifferByRegistry() {
        final Key key = Key.key("minecraft:stone");
        final TypedKey<ItemType> itemKey = TypedKey.create(RegistryKey.ITEM, key);
        final TypedKey<?> blockKey = TypedKey.create(RegistryKey.BLOCK, key);

        // same underlying key but different registries are not equal
        assertNotEquals(itemKey, blockKey);
    }
}
