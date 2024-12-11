package org.bukkit.craftbukkit.util;

import static org.junit.jupiter.api.Assertions.*;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.constant.Constable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Normal
public class ClassTraverserTest {

    public static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of(FirstTest.class, List.of(
                        FirstTest.class,
                        Object.class
                )),
                Arguments.of(SecondTest.class, List.of(
                        SecondTest.class,
                        SecondTestClass.class,
                        SecondTestInterface.class,
                        SecondTestInterface2.class,
                        Object.class
                )),
                Arguments.of(ThirdTestInterface.class, List.of(
                        ThirdTestInterface.class,
                        ThirdTestInterface2.class
                )),
                Arguments.of(FourthTest.class, List.of(
                        FourthTest.class,
                        FourthTestInterface.class,
                        Record.class,
                        Object.class
                )),
                Arguments.of(FifthTest.class, List.of(
                        FifthTest.class,
                        Enum.class,
                        Constable.class,
                        Comparable.class,
                        Serializable.class,
                        Object.class
                )),
                Arguments.of(SixthTest.class, List.of(
                        SixthTest.class,
                        Annotation.class
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    public void test(Class<?> clazz, List<Class<?>> expected) {
        ClassTraverser classTraverser = new ClassTraverser(clazz);
        List<Class<?>> result = new ArrayList<>();

        while (classTraverser.hasNext()) {
            result.add(classTraverser.next());
        }

        assertEquals(expected.size(), result.size(), String.format("""
                Expected classes:
                %s

                Result:
                %s
                """, expected, result));

        for (Class<?> got : expected) {
            assertTrue(result.contains(got), String.format("""
                    Result classes:
                    %s

                    Does not contain class:
                    %s
                    """, result, got));
        }
    }

    // First Test
    private class FirstTest {
    }

    // Second Test
    private class SecondTest extends SecondTestClass implements SecondTestInterface {
    }

    private class SecondTestClass implements SecondTestInterface, SecondTestInterface2 {
    }

    private interface SecondTestInterface extends SecondTestInterface2 {
    }

    private interface SecondTestInterface2 {
    }

    // Third Test
    private interface ThirdTestInterface extends ThirdTestInterface2 {
    }

    private interface ThirdTestInterface2 {
    }

    // Fourth Test
    private record FourthTest() implements FourthTestInterface {
    }

    private interface FourthTestInterface {
    }

    // Fifth Test
    private enum FifthTest {
    }

    // Sixth Test
    private @interface SixthTest {
    }
}
