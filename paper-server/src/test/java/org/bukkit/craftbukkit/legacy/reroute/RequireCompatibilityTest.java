package org.bukkit.craftbukkit.legacy.reroute;

import com.google.common.base.Predicates;
import java.util.List;
import java.util.Map;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class RequireCompatibilityTest extends AbstractRerouteTest {

    @Test
    public void testRequireCompatibility() {
        this.test(RequireCompatibilityTestData.class, Map.of(
                        "()Ljava/util/List;getList", this.create(
                                "()Ljava/util/List;getList",
                                "()Ljava/util/List;",
                                "java/lang/Object",
                                "getList",
                                false,
                                "(Ljava/lang/Object;)Ljava/util/List;",
                                "org/bukkit/craftbukkit/legacy/reroute/RequireCompatibilityTest$RequireCompatibilityTestData",
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

    @Test
    public void testRequireCompatibilityNotPresent() {
        this.test(RequireCompatibilityTestData.class, Predicates.alwaysFalse(), Map.of());
    }

    public static class RequireCompatibilityTestData {

        @RequireCompatibility("test-value")
        public static List<String> getList(Object o) {
            return null;
        }
    }
}
