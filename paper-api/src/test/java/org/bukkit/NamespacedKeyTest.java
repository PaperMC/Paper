package org.bukkit;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class NamespacedKeyTest {

    @Test
    public void testValid() {
        assertEquals("minecraft:foo", new NamespacedKey("minecraft", "foo").toString());
        assertEquals("minecraft:foo/bar", new NamespacedKey("minecraft", "foo/bar").toString());
        assertEquals("minecraft:foo/bar_baz", new NamespacedKey("minecraft", "foo/bar_baz").toString());
        assertEquals("minecraft:foo/bar_baz-qux", new NamespacedKey("minecraft", "foo/bar_baz-qux").toString());
        assertEquals("minecraft:foo/bar_baz-qux.quux", new NamespacedKey("minecraft", "foo/bar_baz-qux.quux").toString());
    }

    @Test
    public void testValidFromString() {
        NamespacedKey expected = NamespacedKey.minecraft("foo");
        assertEquals(expected, NamespacedKey.fromString("foo"));
        assertEquals(expected, NamespacedKey.fromString(":foo"));
        assertEquals(expected, NamespacedKey.fromString("minecraft:foo"));
        assertEquals(new NamespacedKey("foo", "bar"), NamespacedKey.fromString("foo:bar"));

        assertNull(NamespacedKey.fromString("fOO"));
        assertNull(NamespacedKey.fromString(":Foo"));
        assertNull(NamespacedKey.fromString("fOO:bar"));
        assertNull(NamespacedKey.fromString("minecraft:fOO"));
        assertNull(NamespacedKey.fromString("foo:bar:bazz"));
    }

    @Test
    public void testFromStringNullInput() {
        assertThrows(IllegalArgumentException.class, () -> NamespacedKey.fromString(null));
    }

    @Test
    public void testInvalidNamespace() {
        assertThrows(IllegalArgumentException.class, () -> new NamespacedKey("minecraft/test", "foo").toString());
    }

    @Test
    public void testInvalidNamespaceCasing() {
        assertThrows(IllegalArgumentException.class, () -> new NamespacedKey("Minecraft", "foo").toString());
    }

    @Test
    public void testInvalidKeyCasing() {
        assertThrows(IllegalArgumentException.class, () -> new NamespacedKey("minecraft", "Foo").toString());
    }

    @Test
    public void testInvalidKey() {
        assertThrows(IllegalArgumentException.class, () -> new NamespacedKey("minecraft", "foo!").toString());
    }

    @Test
    public void testBelowLength() {
        new NamespacedKey("loremipsumdolorsitametconsecteturadipiscingelitduisvolutpatvelitsitametmaximusscelerisquemorbiullamcorperexacconsequategestas",
                "loremipsumdolorsitametconsecteturadipiscingelitduisvolutpatvelitsitametmaximusscelerisquemorbiullamcorperexacconsequategestas").toString();
    }
}
