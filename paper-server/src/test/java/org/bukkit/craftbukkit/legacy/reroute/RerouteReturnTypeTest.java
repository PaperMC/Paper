package org.bukkit.craftbukkit.legacy.reroute;

import java.util.List;
import java.util.Map;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class RerouteReturnTypeTest extends AbstractRerouteTest {

    @Test
    public void testRerouteReturnType() {
        this.test(RerouteReturnTypeTestData.class, Map.of(
                        "()Ljava/util/Map;getList", this.create(
                                "()Ljava/util/Map;getList",
                                "()Ljava/util/Map;",
                                "java/lang/Object",
                                "getList",
                                false,
                                "(Ljava/lang/Object;)Ljava/util/List;",
                                "org/bukkit/craftbukkit/legacy/reroute/RerouteReturnTypeTest$RerouteReturnTypeTestData",
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

    public static class RerouteReturnTypeTestData {

        @RerouteReturnType("java/util/Map")
        public static List<String> getList(Object o) {
            return null;
        }
    }
}
