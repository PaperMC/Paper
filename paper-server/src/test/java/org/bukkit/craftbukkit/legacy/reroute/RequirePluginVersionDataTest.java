package org.bukkit.craftbukkit.legacy.reroute;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.annotation.Annotation;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class RequirePluginVersionDataTest {

    @Test
    public void testSame() {
        RequirePluginVersionData data = RequirePluginVersionData.create(new RequirePluginVersionImpl("", "1.21", "1.21"));

        assertEquals(ApiVersion.getOrCreateVersion("1.21"), data.minInclusive());
        assertEquals(ApiVersion.getOrCreateVersion("1.21"), data.maxInclusive());
    }

    @Test
    public void testDifferent() {
        RequirePluginVersionData data = RequirePluginVersionData.create(new RequirePluginVersionImpl("", "1.21", "1.23"));

        assertEquals(ApiVersion.getOrCreateVersion("1.21"), data.minInclusive());
        assertEquals(ApiVersion.getOrCreateVersion("1.23"), data.maxInclusive());
    }

    @Test
    public void testValue() {
        RequirePluginVersionData data = RequirePluginVersionData.create(new RequirePluginVersionImpl("1.42", "", ""));

        assertEquals(ApiVersion.getOrCreateVersion("1.42"), data.minInclusive());
        assertEquals(ApiVersion.getOrCreateVersion("1.42"), data.maxInclusive());
    }

    @Test
    public void testValueAndMin() {
        assertThrows(IllegalArgumentException.class, () -> RequirePluginVersionData.create(new RequirePluginVersionImpl("1.42", "1.52", "")));
    }

    @Test
    public void testValueAndMax() {
        assertThrows(IllegalArgumentException.class, () -> RequirePluginVersionData.create(new RequirePluginVersionImpl("1.42", "", "1.53")));
    }


    @Test
    public void testValueAndMinMax() {
        assertThrows(IllegalArgumentException.class, () -> RequirePluginVersionData.create(new RequirePluginVersionImpl("1.42", "1.51", "1.53")));
    }

    @Test
    public void testMinNewerThanMax() {
        assertThrows(IllegalArgumentException.class, () -> RequirePluginVersionData.create(new RequirePluginVersionImpl("", "1.59", "1.57")));
    }

    @Test
    public void testOnlyMin() {
        RequirePluginVersionData data = RequirePluginVersionData.create(new RequirePluginVersionImpl("", "1.44", ""));

        assertEquals(ApiVersion.getOrCreateVersion("1.44"), data.minInclusive());
        assertNull(data.maxInclusive());
    }


    @Test
    public void testOnlyMax() {
        RequirePluginVersionData data = RequirePluginVersionData.create(new RequirePluginVersionImpl("", "", "1.96"));

        assertNull(data.minInclusive());
        assertEquals(ApiVersion.getOrCreateVersion("1.96"), data.maxInclusive());
    }

    @Test
    public void testExactData() {
        RequirePluginVersionData data = new RequirePluginVersionData(ApiVersion.getOrCreateVersion("1.12"), ApiVersion.getOrCreateVersion("1.12"));

        assertTrue(data.test(ApiVersion.getOrCreateVersion("1.12")));
        assertFalse(data.test(ApiVersion.getOrCreateVersion("1.11")));
        assertFalse(data.test(ApiVersion.getOrCreateVersion("1.13")));
    }

    @Test
    public void testRangeData() {
        RequirePluginVersionData data = new RequirePluginVersionData(ApiVersion.getOrCreateVersion("1.12"), ApiVersion.getOrCreateVersion("1.13"));

        assertTrue(data.test(ApiVersion.getOrCreateVersion("1.12")));
        assertTrue(data.test(ApiVersion.getOrCreateVersion("1.12.5")));
        assertTrue(data.test(ApiVersion.getOrCreateVersion("1.13")));

        assertFalse(data.test(ApiVersion.getOrCreateVersion("1.11")));
        assertFalse(data.test(ApiVersion.getOrCreateVersion("1.14")));
    }

    @Test
    public void testOlderOpenRangeData() {
        RequirePluginVersionData data = new RequirePluginVersionData(null, ApiVersion.getOrCreateVersion("1.13"));

        assertTrue(data.test(ApiVersion.getOrCreateVersion("1.1")));
        assertTrue(data.test(ApiVersion.getOrCreateVersion("1.13")));

        assertFalse(data.test(ApiVersion.getOrCreateVersion("1.14")));
    }

    @Test
    public void testNewerOpenRangeData() {
        RequirePluginVersionData data = new RequirePluginVersionData(ApiVersion.getOrCreateVersion("1.12"), null);

        assertTrue(data.test(ApiVersion.getOrCreateVersion("1.12")));
        assertTrue(data.test(ApiVersion.getOrCreateVersion("1.14")));

        assertFalse(data.test(ApiVersion.getOrCreateVersion("1.11")));
    }

    private record RequirePluginVersionImpl(String value, String minInclusive, String maxInclusive) implements RequirePluginVersion {
        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }
    }
}
