package org.bukkit.craftbukkit.legacy.reroute;

import static org.junit.jupiter.api.Assertions.*;
import com.google.common.base.Predicates;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class RerouteBuilderTest extends AbstractRerouteTest {

    @Test
    public void testReroute() {
        test(FirstTest.class, Map.of(
                "()VtestReroute", create(
                        "()VtestReroute",
                        "()V",
                        "org/bukkit/craftbukkit/legacy/reroute/RerouteBuilderTest",
                        "testReroute",
                        false,
                        "(Lorg/bukkit/craftbukkit/legacy/reroute/RerouteBuilderTest;)V",
                        "org/bukkit/craftbukkit/legacy/reroute/RerouteBuilderTest$FirstTest",
                        "testReroute",
                        create(
                                create("Lorg/bukkit/craftbukkit/legacy/reroute/RerouteBuilderTest;", "Lorg/bukkit/craftbukkit/legacy/reroute/RerouteBuilderTest;", false, false, null)
                        ),
                        "V",
                        true,
                        null
                )
        ));
    }

    @Test
    public void testInvalidMethods() {
        test(InvalidMethodTest.class, Map.of());
    }

    @Test
    public void testMultipleMethods() {
        test(MultipleMethodTest.class, Map.of(
                        "()Ljava/util/List;getList", create(
                                "()Ljava/util/List;getList",
                                "()Ljava/util/List;",
                                "java/lang/Object",
                                "getList",
                                false,
                                "(Ljava/lang/Object;)Ljava/util/List;",
                                "org/bukkit/craftbukkit/legacy/reroute/RerouteBuilderTest$MultipleMethodTest",
                                "getList",
                                create(
                                        create("Ljava/lang/Object;", "Ljava/lang/Object;", false, false, null)
                                ),
                                "Ljava/util/List;",
                                true,
                                null
                        ),
                        "(Ljava/util/Map;)Ljava/util/Map;getMap", create(
                                "(Ljava/util/Map;)Ljava/util/Map;getMap",
                                "(Ljava/util/Map;)Ljava/util/Map;",
                                "java/lang/String",
                                "getMap",
                                false,
                                "(Ljava/lang/String;Ljava/util/Map;)Ljava/util/Map;",
                                "org/bukkit/craftbukkit/legacy/reroute/RerouteBuilderTest$MultipleMethodTest",
                                "getMap",
                                create(
                                        create("Ljava/lang/String;", "Ljava/lang/String;", false, false, null),
                                        create("Ljava/util/Map;", "Ljava/util/Map;", false, false, null)
                                ),
                                "Ljava/util/Map;",
                                true,
                                null
                        ),
                        "(ZS)IgetInt", create(
                                "(ZS)IgetInt",
                                "(ZS)I",
                                "java/util/logging/Logger",
                                "getInt",
                                false,
                                "(Ljava/util/logging/Logger;ZS)I",
                                "org/bukkit/craftbukkit/legacy/reroute/RerouteBuilderTest$MultipleMethodTest",
                                "getInt",
                                create(
                                        create("Ljava/util/logging/Logger;", "Ljava/util/logging/Logger;", false, false, null),
                                        create("Z", "Z", false, false, null),
                                        create("S", "S", false, false, null)
                                ),
                                "I",
                                true,
                                null
                        )
                )
        );
    }

    @Test
    public void testInterfaceRerouteClass() {
        assertThrows(IllegalArgumentException.class, () -> RerouteBuilder.create(Predicates.alwaysTrue()).forClass(InterfaceTest.class).build());
    }

    @Test
    public void testAnnotationRerouteClass() {
        assertThrows(IllegalArgumentException.class, () -> RerouteBuilder.create(Predicates.alwaysTrue()).forClass(AnnotationTest.class).build());
    }

    @Test
    public void testMissingOwner() {
        assertThrows(RuntimeException.class, () -> RerouteBuilder.create(Predicates.alwaysTrue()).forClass(MissingOwnerTest.class).build());
    }

    @Test
    public void testSameKey() {
        test(SameKeyTest.class, Predicates.alwaysTrue(), List.of(
                new TestDataHolder("()Ljava/util/List;getList", List.of(create(
                                "()Ljava/util/List;getList",
                                "()Ljava/util/List;",
                                "java/lang/Object",
                                "getList",
                                false,
                                "(Ljava/lang/Object;)Ljava/util/List;",
                                "org/bukkit/craftbukkit/legacy/reroute/RerouteBuilderTest$SameKeyTest",
                                "getList",
                                create(
                                        create("Ljava/lang/Object;", "Ljava/lang/Object;", false, false, null)
                                ),
                                "Ljava/util/List;",
                                true,
                                null
                        ),
                        create(
                                "()Ljava/util/List;getList",
                                "()Ljava/util/List;",
                                "java/lang/String",
                                "getList",
                                false,
                                "(Ljava/lang/String;Ljava/lang/String;)Ljava/util/List;",
                                "org/bukkit/craftbukkit/legacy/reroute/RerouteBuilderTest$SameKeyTest",
                                "getList",
                                create(
                                        create("Ljava/lang/String;", "Ljava/lang/String;", false, false, null),
                                        create("Ljava/lang/String;", "Ljava/lang/String;", true, false, null)
                                ),
                                "Ljava/util/List;",
                                true,
                                null
                        )
                ))
        ));
    }

    public static class FirstTest {

        public static void testReroute(RerouteBuilderTest rerouteBuilderTest) {
        }
    }

    public static class InvalidMethodTest {

        public void testReroute(RerouteBuilderTest rerouteBuilderTest) {
        }

        static void testReroute2(RerouteBuilderTest rerouteBuilderTest) {
        }

        void testReroute3(RerouteBuilderTest rerouteBuilderTest) {
        }

        private static void testReroute4(RerouteBuilderTest rerouteBuilderTest) {
        }

        protected static void testReroute5(RerouteBuilderTest rerouteBuilderTest) {
        }
    }

    public static class MultipleMethodTest {

        public static List<String> getList(Object o) {
            return null;
        }

        public static Map<String, String> getMap(String value, Map<String, String> map) {
            return null;
        }

        public static int getInt(Logger logger, boolean bool, short s) {
            return 0;
        }
    }

    public interface InterfaceTest {

        static List<String> getList(Object o) {
            return null;
        }
    }

    public @interface AnnotationTest {

    }

    public static class MissingOwnerTest {

        public static List<String> getList() {
            return null;
        }
    }

    public class SameKeyTest {

        public static List<String> getList(Object o) {
            return null;
        }

        public static List<String> getList(String s, @InjectPluginName String name) {
            return null;
        }
    }
}
