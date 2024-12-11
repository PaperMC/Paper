package org.bukkit.craftbukkit.legacy.reroute;

import java.util.List;
import java.util.Map;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class NotInBukkitTest extends AbstractRerouteTest {

    @Test
    public void testNotInBukkit() {
        this.test(NotInBukkitTestData.class, Map.of(
                        "()Ljava/util/List;getList", this.create(
                                "()Ljava/util/List;getList",
                                "()Ljava/util/List;",
                                "java/lang/Object",
                                "getList",
                                false,
                                "(Ljava/lang/Object;)Ljava/util/List;",
                                "org/bukkit/craftbukkit/legacy/reroute/NotInBukkitTest$NotInBukkitTestData",
                                "getList",
                                this.create(
                                        this.create("Ljava/lang/Object;", "Ljava/lang/Object;", false, false, null)
                                ),
                                "Ljava/util/List;",
                                false,
                                null
                        )
                )
        );
    }

    public static class NotInBukkitTestData {

        @NotInBukkit
        public static List<String> getList(Object o) {
            return null;
        }
    }
}
