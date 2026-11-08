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
        final Key key = new NamespacedKey("test", "key");
        final Key key1 = Key.key("test", "key");
        assertEquals(key, key1);
        assertEquals(key1, key);
    }

    @Test
    public void setTest() {
        final Key key = new NamespacedKey("test", "key");
        final Key key1 = Key.key("test", "key");
        final Set<Key> set = new HashSet<>(Set.of(key));
        final Set<Key> set1 = new HashSet<>(Set.of(key1));
        assertTrue(set.contains(key1));
        assertTrue(set1.contains(key));
    }
}
