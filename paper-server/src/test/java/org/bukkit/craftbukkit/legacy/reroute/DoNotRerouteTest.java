package org.bukkit.craftbukkit.legacy.reroute;

import java.util.List;
import java.util.Map;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class DoNotRerouteTest extends AbstractRerouteTest {

    @Test
    public void testDoNotReroute() {
        this.test(DoNotRerouteTestData.class, Map.of());
    }

    public static class DoNotRerouteTestData {

        @DoNotReroute
        public static List<String> getList(Object o) {
            return null;
        }
    }
}
