package org.bukkit.metadata;

import static org.junit.Assert.assertEquals;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.TestPlugin;
import org.junit.Test;

public class FixedMetadataValueTest {
    private Plugin plugin = new TestPlugin("X");
    private FixedMetadataValue subject;

    private void valueEquals(Object value) {
        subject = new FixedMetadataValue(plugin, value);
        assertEquals(value, subject.value());
    }

    @Test
    public void testTypes() {
        valueEquals(10);
        valueEquals(0.1);
        valueEquals("TEN");
        valueEquals(true);
        valueEquals(null);
        valueEquals((float) 10.5);
        valueEquals((long) 10);
        valueEquals((short) 10);
        valueEquals((byte) 10);
    }
}
