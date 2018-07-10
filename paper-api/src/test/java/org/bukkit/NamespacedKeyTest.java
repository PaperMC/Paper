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
