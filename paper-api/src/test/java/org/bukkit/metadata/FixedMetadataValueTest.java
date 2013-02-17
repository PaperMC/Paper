package org.bukkit.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.TestPlugin;
import org.junit.Test;

public class FixedMetadataValueTest {
    private Plugin plugin = new TestPlugin("X");
    private FixedMetadataValue subject;

    @Test
    public void testBasic() {
        subject = new FixedMetadataValue(plugin, new Integer(50));
        assertSame(plugin, subject.getOwningPlugin());
        assertEquals(new Integer(50), subject.value());
    }

    @Test
    public void testNumberTypes() {
        subject = new FixedMetadataValue(plugin, new Integer(5));
        assertEquals(new Integer(5), subject.value());
        assertEquals(5, subject.asInt());
        assertEquals(true, subject.asBoolean());
        assertEquals(5, subject.asByte());
        assertEquals(5.0, subject.asFloat(), 0.1e-8);
        assertEquals(5.0D, subject.asDouble(), 0.1e-8D);
        assertEquals(5L, subject.asLong());
        assertEquals(5, subject.asShort());
        assertEquals("5", subject.asString());
    }

    @Test
    public void testInvalidateDoesNothing() {
        Object o = new Object();
        subject = new FixedMetadataValue(plugin, o);
        subject.invalidate();
        assertSame(o, subject.value());
    }
}
