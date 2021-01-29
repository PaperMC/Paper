package io.papermc.paper.adventure;

import java.util.HashSet;
import java.util.Set;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KeyTest {

    @Test
    public void equalsTest() {
        Key key = new NamespacedKey("test", "key");
        Key key1 = Key.key("test", "key");
        assertEquals(key, key1);
        assertEquals(key1, key);
    }

    @Test
    public void setTest() {
        Key key = new NamespacedKey("test", "key");
        Key key1 = Key.key("test", "key");
        Set<Key> set = new HashSet<>(Set.of(key));
        Set<Key> set1 = new HashSet<>(Set.of(key1));
        assertTrue(set.contains(key1));
        assertTrue(set1.contains(key));
    }
}
