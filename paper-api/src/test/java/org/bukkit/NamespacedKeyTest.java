package org.bukkit;

import org.junit.Assert;
import org.junit.Test;

public class NamespacedKeyTest {

    @Test
    public void testValid() {
        Assert.assertEquals("minecraft:foo", new NamespacedKey("minecraft", "foo").toString());
        Assert.assertEquals("minecraft:foo/bar", new NamespacedKey("minecraft", "foo/bar").toString());
        Assert.assertEquals("minecraft:foo/bar_baz", new NamespacedKey("minecraft", "foo/bar_baz").toString());
        Assert.assertEquals("minecraft:foo/bar_baz-qux", new NamespacedKey("minecraft", "foo/bar_baz-qux").toString());
        Assert.assertEquals("minecraft:foo/bar_baz-qux.quux", new NamespacedKey("minecraft", "foo/bar_baz-qux.quux").toString());
    }

    @Test
    public void testValidFromString() {
        NamespacedKey expected = NamespacedKey.minecraft("foo");
        Assert.assertEquals(expected, NamespacedKey.fromString("foo"));
        Assert.assertEquals(expected, NamespacedKey.fromString(":foo"));
        Assert.assertEquals(expected, NamespacedKey.fromString("minecraft:foo"));
        Assert.assertEquals(new NamespacedKey("foo", "bar"), NamespacedKey.fromString("foo:bar"));

        Assert.assertNull(NamespacedKey.fromString("fOO"));
        Assert.assertNull(NamespacedKey.fromString(":Foo"));
        Assert.assertNull(NamespacedKey.fromString("fOO:bar"));
        Assert.assertNull(NamespacedKey.fromString("minecraft:fOO"));
        Assert.assertNull(NamespacedKey.fromString("foo:bar:bazz"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromStringEmptyInput() {
        NamespacedKey.fromString("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromStringNullInput() {
        NamespacedKey.fromString(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyNamespace() {
        new NamespacedKey("", "foo").toString();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyKey() {
        new NamespacedKey("minecraft", "").toString();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNamespace() {
        new NamespacedKey("minecraft/test", "foo").toString();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNamespaceCasing() {
        new NamespacedKey("Minecraft", "foo").toString();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidKeyCasing() {
        new NamespacedKey("minecraft", "Foo").toString();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidKey() {
        new NamespacedKey("minecraft", "foo!").toString();
    }

    @Test
    public void testBelowLength() {
        new NamespacedKey("loremipsumdolorsitametconsecteturadipiscingelitduisvolutpatvelitsitametmaximusscelerisquemorbiullamcorperexacconsequategestas",
                "loremipsumdolorsitametconsecteturadipiscingelitduisvolutpatvelitsitametmaximusscelerisquemorbiullamcorperexacconsequategestas").toString();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAboveLength() {
        new NamespacedKey("loremipsumdolorsitametconsecteturadipiscingelitduisvolutpatvelitsitametmaximusscelerisquemorbiullamcorperexacconsequategestas",
                "loremipsumdolorsitametconsecteturadipiscingelitduisvolutpatvelitsitametmaximusscelerisquemorbiullamcorperexacconsequategestas/"
                + "loremipsumdolorsitametconsecteturadipiscingelitduisvolutpatvelitsitametmaximusscelerisquemorbiullamcorperexacconsequategestas").toString();
    }
}
