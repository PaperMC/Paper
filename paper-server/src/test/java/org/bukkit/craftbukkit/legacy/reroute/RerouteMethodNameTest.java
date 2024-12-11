package org.bukkit.craftbukkit.legacy.reroute;

import java.util.List;
import java.util.Map;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class RerouteMethodNameTest extends AbstractRerouteTest {

    @Test
    public void testRerouteMethodName() {
        this.test(RerouteMethodNameTestData.class, Map.of(
                        "()Ljava/util/List;getMap", this.create(
                                "()Ljava/util/List;getMap",
                                "()Ljava/util/List;",
                                "java/lang/Object",
                                "getMap",
                                false,
                                "(Ljava/lang/Object;)Ljava/util/List;",
                                "org/bukkit/craftbukkit/legacy/reroute/RerouteMethodNameTest$RerouteMethodNameTestData",
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

    public static class RerouteMethodNameTestData {

        @RerouteMethodName("getMap")
        public static List<String> getList(Object o) {
            return null;
        }
    }
}
