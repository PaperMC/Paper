package org.bukkit.metadata;

import static org.junit.Assert.assertEquals;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.TestPlugin;
import org.junit.Test;

public class MetadataValueAdapterTest {
    private TestPlugin plugin = new TestPlugin("x");

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
        assertEquals(false, simpleValue(null).asBoolean());

        // String to boolean.
        assertEquals(true, simpleValue("True").asBoolean());
        assertEquals(true, simpleValue("TRUE").asBoolean());
        assertEquals(false, simpleValue("false").asBoolean());

        // Number to boolean.
        assertEquals(true, simpleValue(1).asBoolean());
        assertEquals(true, simpleValue(5.0).asBoolean());
        assertEquals(false, simpleValue(0).asBoolean());
        assertEquals(false, simpleValue(0.1).asBoolean());

        // Boolean as boolean, of course.
        assertEquals(true, simpleValue(Boolean.TRUE).asBoolean());
        assertEquals(false, simpleValue(Boolean.FALSE).asBoolean());

        // any object that is not null and not a Boolean, String, or Number is true.
        assertEquals(true, simpleValue(new Object()).asBoolean());
    }

    /** Test String conversions return an empty string when given null. */
    @Test
    public void testStringConversionNull() {
        assertEquals("", simpleValue(null).asString());
    }

    /** Get a fixed value MetadataValue. */
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

        public Object value() {
            return ++internalValue;
        }

        public void invalidate() {
            internalValue = 0;
        }
    }
}
