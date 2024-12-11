package org.bukkit.craftbukkit.legacy.reroute;

import java.util.List;
import java.util.Map;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class RerouteArgumentTypeTest extends AbstractRerouteTest {

    @Test
    public void testRerouteArgumentType() {
        this.test(RerouteArgumentTypeTestData.class, Map.of(
                        "()Ljava/util/List;getList", this.create(
                                "()Ljava/util/List;getList",
                                "()Ljava/util/List;",
                                "java/util/Map",
                                "getList",
                                false,
                                "(Ljava/lang/Object;)Ljava/util/List;",
                                "org/bukkit/craftbukkit/legacy/reroute/RerouteArgumentTypeTest$RerouteArgumentTypeTestData",
                                "getList",
                                this.create(
                                        this.create("Ljava/lang/Object;", "Ljava/util/Map;", false, false, null)
                                ),
                                "Ljava/util/List;",
                                true,
                                null
                        )
                )
        );
    }

    @Test
    public void testRerouteArgumentTypeSecond() {
        this.test(RerouteArgumentTypeSecondTestData.class, Map.of(
                        "(Ljava/lang/String;)Ljava/util/List;getList", this.create(
                                "(Ljava/lang/String;)Ljava/util/List;getList",
                                "(Ljava/lang/String;)Ljava/util/List;",
                                "java/lang/Object",
                                "getList",
                                false,
                                "(Ljava/lang/Object;Ljava/util/Map;)Ljava/util/List;",
                                "org/bukkit/craftbukkit/legacy/reroute/RerouteArgumentTypeTest$RerouteArgumentTypeSecondTestData",
                                "getList",
                                this.create(
                                        this.create("Ljava/lang/Object;", "Ljava/lang/Object;", false, false, null),
                                        this.create("Ljava/util/Map;", "Ljava/lang/String;", false, false, null)
                                ),
                                "Ljava/util/List;",
                                true,
                                null
                        )
                )
        );
    }

    public static class RerouteArgumentTypeTestData {

        public static List<String> getList(@RerouteArgumentType("java/util/Map") Object o) {
            return null;
        }
    }

    public static class RerouteArgumentTypeSecondTestData {

        public static List<String> getList(Object o, @RerouteArgumentType("java/lang/String") Map<String, Object> map) {
            return null;
        }
    }
}
