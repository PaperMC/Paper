package org.bukkit.craftbukkit.legacy.reroute;

import static org.junit.jupiter.api.Assertions.*;
import com.google.common.base.Predicates;
import java.util.List;
import java.util.Map;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class InjectPluginNameTest extends AbstractRerouteTest {

    @Test
    public void testInjectPluginName() {
        this.test(InjectPluginNameTestData.class, Map.of(
                        "()Ljava/util/List;getList", this.create(
                                "()Ljava/util/List;getList",
                                "()Ljava/util/List;",
                                "java/lang/Object",
                                "getList",
                                false,
                                "(Ljava/lang/Object;Ljava/lang/String;)Ljava/util/List;",
                                "org/bukkit/craftbukkit/legacy/reroute/InjectPluginNameTest$InjectPluginNameTestData",
                                "getList",
                                this.create(
                                        this.create("Ljava/lang/Object;", "Ljava/lang/Object;", false, false, null),
                                        this.create("Ljava/lang/String;", "Ljava/lang/String;", true, false, null)
                                ),
                                "Ljava/util/List;",
                                true,
                                null
                        )
                )
        );
    }

    @Test
    public void testInjectPluginNameIncorrectType() {
        assertThrows(RuntimeException.class, () -> RerouteBuilder.create(Predicates.alwaysTrue()).forClass(InjectPluginNameIncorrectTypeTestData.class).build());
    }

    public static class InjectPluginNameTestData {

        public static List<String> getList(Object o, @InjectPluginName String value) {
            return null;
        }
    }

    public static class InjectPluginNameIncorrectTypeTestData {

        public static List<String> getList(Object o, @InjectPluginName Object value) {
            return null;
        }
    }
}
