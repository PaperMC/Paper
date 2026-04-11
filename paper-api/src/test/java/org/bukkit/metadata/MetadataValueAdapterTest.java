package org.bukkit.metadata;

import static org.junit.jupiter.api.Assertions.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.BukkitTestPlugin;
import org.junit.jupiter.api.Test;

public class MetadataValueAdapterTest {
    private BukkitTestPlugin plugin = new BukkitTestPlugin("x");

    @Test
    public void testAdapterBasics() {
        IncrementingMetaValue mv = new IncrementingMetaValue(plugin);
        // check getOwningPlugin
        assertEquals(mv.getOwningPlugin(), this.plugin);

        // Check value-getting and invalidation.
        assertEquals(new Integer(1), mv.value());
        assertEquals(new Integer(2), mv.value());
        mv.invalidate();
        assertEquals(new Integer(1), mv.value());
    }

    @Test
    public void testAdapterConversions() {
        IncrementingMetaValue mv = new IncrementingMetaValue(plugin);

        assertEquals(1, mv.asInt());
        assertEquals(2L, mv.asLong());
        assertEquals(3.0, mv.asFloat(), 0.001);
        assertEquals(4, mv.asByte());
        assertEquals(5.0, mv.asDouble(), 0.001);
        assertEquals(6, mv.asShort());
        assertEquals("7", mv.asString());
    }

    /** Boolean conversion is non-trivial, we want to test it thoroughly. */
    @Test
    public void testBooleanConversion() {
        // null is False.
        assertFalse(simpleValue(null).asBoolean());

        // String to boolean.
        assertTrue(simpleValue("True").asBoolean());
        assertTrue(simpleValue("TRUE").asBoolean());
        assertFalse(simpleValue("false").asBoolean());

        // Number to boolean.
        assertTrue(simpleValue(1).asBoolean());
        assertTrue(simpleValue(5.0).asBoolean());
        assertFalse(simpleValue(0).asBoolean());
        assertFalse(simpleValue(0.1).asBoolean());

        // Boolean as boolean, of course.
        assertTrue(simpleValue(Boolean.TRUE).asBoolean());
        assertFalse(simpleValue(Boolean.FALSE).asBoolean());

        // any object that is not null and not a Boolean, String, or Number is true.
        assertTrue(simpleValue(new Object()).asBoolean());
    }

    /** Test String conversions return an empty string when given null. */
    @Test
    public void testStringConversionNull() {
        assertEquals("", simpleValue(null).asString());
    }

    /**
     * Get a fixed value MetadataValue.
     *
     * @param value the value to wrap
     * @return the fixed value
     */
    private MetadataValue simpleValue(Object value) {
        return new FixedMetadataValue(plugin, value);
    }

    /**
     * A sample non-trivial MetadataValueAdapter implementation.
     *
     * The rationale for implementing an incrementing value is to have a value
     * which changes with every call to value(). This is important for testing
     * because we want to make sure all the tested conversions are calling the
     * value() method exactly once and no caching is going on.
     */
    class IncrementingMetaValue extends MetadataValueAdapter {
        private int internalValue = 0;

        protected IncrementingMetaValue(Plugin owningPlugin) {
            super(owningPlugin);
        }

        @Override
        public Object value() {
            return ++internalValue;
        }

        @Override
        public void invalidate() {
            internalValue = 0;
        }
    }
}
