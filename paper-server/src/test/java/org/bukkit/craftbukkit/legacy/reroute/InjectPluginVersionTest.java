package org.bukkit.craftbukkit.legacy.reroute;

import static org.junit.jupiter.api.Assertions.*;
import com.google.common.base.Predicates;
import java.util.List;
import java.util.Map;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class InjectPluginVersionTest extends AbstractRerouteTest {

    @Test
    public void testInjectPluginVersion() {
        this.test(InjectPluginVersionTestData.class, Map.of(
                        "()Ljava/util/List;getList", this.create(
                                "()Ljava/util/List;getList",
                                "()Ljava/util/List;",
                                "java/lang/Object",
                                "getList",
                                false,
                                "(Ljava/lang/Object;Lorg/bukkit/craftbukkit/util/ApiVersion;)Ljava/util/List;",
                                "org/bukkit/craftbukkit/legacy/reroute/InjectPluginVersionTest$InjectPluginVersionTestData",
                                "getList",
                                this.create(
                                        this.create("Ljava/lang/Object;", "Ljava/lang/Object;", false, false, null),
                                        this.create("Lorg/bukkit/craftbukkit/util/ApiVersion;", "Lorg/bukkit/craftbukkit/util/ApiVersion;", false, true, null)
                                ),
                                "Ljava/util/List;",
                                true,
                                null
                        )
                )
        );
    }

    @Test
    public void testInjectPluginVersionIncorrectType() {
        assertThrows(RuntimeException.class, () -> RerouteBuilder.create(Predicates.alwaysTrue()).forClass(InjectPluginVersionIncorrectTypeTestData.class).build());
    }

    public static class InjectPluginVersionTestData {

        public static List<String> getList(Object o, @InjectPluginVersion ApiVersion value) {
            return null;
        }
    }

    public static class InjectPluginVersionIncorrectTypeTestData {

        public static List<String> getList(Object o, @InjectPluginVersion String value) {
            return null;
        }
    }
}
