package org.bukkit.craftbukkit.legacy.reroute;

import java.util.List;
import java.util.Map;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class RerouteStaticTest extends AbstractRerouteTest {

    @Test
    public void testStaticReroute() {
        this.test(RerouteStaticTestData.class, Map.of(
                        "(Ljava/lang/Object;)Ljava/util/List;getList", this.create(
                                "(Ljava/lang/Object;)Ljava/util/List;getList",
                                "(Ljava/lang/Object;)Ljava/util/List;",
                                "java/util/Map",
                                "getList",
                                true,
                                "(Ljava/lang/Object;)Ljava/util/List;",
                                "org/bukkit/craftbukkit/legacy/reroute/RerouteStaticTest$RerouteStaticTestData",
                                "getList",
                                this.create(
                                        this.create("Ljava/lang/Object;", "Ljava/lang/Object;", false, false, null)
                                ),
                                "Ljava/util/List;",
                                true,
                                null
                        )
                )
        );
    }

    public static class RerouteStaticTestData {

        @RerouteStatic("java/util/Map")
        public static List<String> getList(Object o) {
            return null;
        }
    }
}
