package org.bukkit.craftbukkit.legacy.reroute;

import java.util.List;
import java.util.Map;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class RequirePluginVersionTest extends AbstractRerouteTest {

    @Test
    public void testRequirePluginVersion() {
        this.test(RequirePluginVersionTestData.class, Map.of(
                        "()Ljava/util/List;getList", this.create(
                                "()Ljava/util/List;getList",
                                "()Ljava/util/List;",
                                "java/lang/Object",
                                "getList",
                                false,
                                "(Ljava/lang/Object;)Ljava/util/List;",
                                "org/bukkit/craftbukkit/legacy/reroute/RequirePluginVersionTest$RequirePluginVersionTestData",
                                "getList",
                                this.create(
                                        this.create("Ljava/lang/Object;", "Ljava/lang/Object;", false, false, null)
                                ),
                                "Ljava/util/List;",
                                true,
                                new RequirePluginVersionData(ApiVersion.getOrCreateVersion("1.42"), ApiVersion.getOrCreateVersion("1.42"))
                        )
                )
        );
    }

    public static class RequirePluginVersionTestData {

        @RequirePluginVersion("1.42")
        public static List<String> getList(Object o) {
            return null;
        }
    }
}
