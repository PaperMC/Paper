package org.bukkit.craftbukkit.legacy.reroute;

import static org.junit.jupiter.api.Assertions.*;
import com.google.common.base.Predicates;
import java.util.List;
import java.util.Map;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class InjectCompatibilityTest extends AbstractRerouteTest {

    @Test
    public void testInjectCompatibility() {
        this.test(InjectCompatibilityTestData.class, Map.of(
                        "()Ljava/util/List;getList", this.create(
                                "()Ljava/util/List;getList",
                                "()Ljava/util/List;",
                                "java/lang/Object",
                                "getList",
                                false,
                                "(Ljava/lang/Object;Z)Ljava/util/List;",
                                "org/bukkit/craftbukkit/legacy/reroute/InjectCompatibilityTest$InjectCompatibilityTestData",
                                "getList",
                                this.create(
                                        this.create("Ljava/lang/Object;", "Ljava/lang/Object;", false, false, null),
                                        this.create("Z", "Z", false, false, "test-value")
                                ),
                                "Ljava/util/List;",
                                true,
                                null
                        )
                )
        );
    }

    @Test
    public void testInjectCompatibilityIncorrectType() {
        assertThrows(RuntimeException.class, () -> RerouteBuilder.create(Predicates.alwaysTrue()).forClass(InjectCompatibilityIncorrectTypeTestData.class).build());
    }

    public static class InjectCompatibilityTestData {

        public static List<String> getList(Object o, @InjectCompatibility("test-value") boolean value) {
            return null;
        }
    }

    public static class InjectCompatibilityIncorrectTypeTestData {

        public static List<String> getList(Object o, @InjectCompatibility("test-value") String value) {
            return null;
        }
    }
}
