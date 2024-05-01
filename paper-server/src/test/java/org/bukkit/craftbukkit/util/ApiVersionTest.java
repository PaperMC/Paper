package org.bukkit.craftbukkit.util;

import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.Stream;
import net.minecraft.SharedConstants;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ApiVersionTest extends AbstractTestingBase {

    public static Stream<Arguments> parseData() {
        return Stream.of(
                Arguments.of(null, "none"),
                Arguments.of("", "none"),
                Arguments.of("none", "none"),
                Arguments.of("1.12", "1.12.0"),
                Arguments.of("1.13.3", "1.13.3"),
                Arguments.of("1.+20.3", "1.20.3")
        );
    }

    public static Stream<Arguments> compareData() {
        return Stream.of(
                Arguments.of("none", "none", CompareResult.SAME),
                Arguments.of("none", "1.20", CompareResult.SMALLER),
                Arguments.of("2.20.3", "1.30.4", CompareResult.BIGGER),
                Arguments.of("1.13", "1.12", CompareResult.BIGGER),
                Arguments.of("1.13.2", "1.13.3", CompareResult.SMALLER)
        );
    }

    public static Stream<Arguments> newerData() {
        return Stream.of(
                Arguments.of("1.12", "1.12", false),
                Arguments.of("1.12", "1.12.2", false),
                Arguments.of("1.12.2", "1.12", true)
        );
    }

    public static Stream<Arguments> olderData() {
        return Stream.of(
                Arguments.of("1.12", "1.12", false),
                Arguments.of("1.12", "1.12.2", true),
                Arguments.of("1.12.2", "1.12", false)
        );
    }

    public static Stream<Arguments> newerOrSameData() {
        return Stream.of(
                Arguments.of("1.12", "1.12", true),
                Arguments.of("1.12", "1.12.2", false),
                Arguments.of("1.12.2", "1.12", true)
        );
    }

    public static Stream<Arguments> olderOrSameData() {
        return Stream.of(
                Arguments.of("1.12", "1.12", true),
                Arguments.of("1.12", "1.12.2", true),
                Arguments.of("1.12.2", "1.12", false)
        );
    }

    @Test
    public void testCurrentVersionUpdated() {
        ApiVersion apiVersionOne = ApiVersion.getOrCreateVersion(SharedConstants.getCurrentVersion().getName());
        ApiVersion apiVersionTwo = ApiVersion.CURRENT;

        assertEquals(apiVersionOne, apiVersionTwo, "The current version in ApiVersion not match current minecraft version");
    }

    @ParameterizedTest
    @MethodSource("parseData")
    public void testParsing(String parse, String expected) {
        ApiVersion apiVersion = ApiVersion.getOrCreateVersion(parse);

        assertEquals(expected, apiVersion.getVersionString());
    }

    @Test
    public void testSameInstance() {
        ApiVersion one = ApiVersion.getOrCreateVersion("1.23.3");
        ApiVersion second = ApiVersion.getOrCreateVersion("1.+23.3");

        assertSame(one, second);
    }

    @ParameterizedTest
    @MethodSource("compareData")
    public void testCompareTo(String first, String second, CompareResult compareResult) {
        ApiVersion firstApi = ApiVersion.getOrCreateVersion(first);
        ApiVersion secondApi = ApiVersion.getOrCreateVersion(second);


        int result = firstApi.compareTo(secondApi);

        assertSame(compareResult, CompareResult.toCompareResult(result));
    }

    @ParameterizedTest
    @MethodSource("newerData")
    public void testNewerThan(String first, String second, boolean newer) {
        ApiVersion firstApi = ApiVersion.getOrCreateVersion(first);
        ApiVersion secondApi = ApiVersion.getOrCreateVersion(second);


        boolean result = firstApi.isNewerThan(secondApi);

        assertSame(newer, result);
    }

    @ParameterizedTest
    @MethodSource("olderData")
    public void testOlderThan(String first, String second, boolean older) {
        ApiVersion firstApi = ApiVersion.getOrCreateVersion(first);
        ApiVersion secondApi = ApiVersion.getOrCreateVersion(second);


        boolean result = firstApi.isOlderThan(secondApi);

        assertSame(older, result);
    }

    @ParameterizedTest
    @MethodSource("newerOrSameData")
    public void testNewerOrSame(String first, String second, boolean newerOrSame) {
        ApiVersion firstApi = ApiVersion.getOrCreateVersion(first);
        ApiVersion secondApi = ApiVersion.getOrCreateVersion(second);


        boolean result = firstApi.isNewerThanOrSameAs(secondApi);

        assertSame(newerOrSame, result);
    }

    @ParameterizedTest
    @MethodSource("olderOrSameData")
    public void testOlderOrSame(String first, String second, boolean olderOrSame) {
        ApiVersion firstApi = ApiVersion.getOrCreateVersion(first);
        ApiVersion secondApi = ApiVersion.getOrCreateVersion(second);


        boolean result = firstApi.isOlderThanOrSameAs(secondApi);

        assertSame(olderOrSame, result);
    }

    public enum CompareResult {
        SMALLER,
        BIGGER,
        SAME;

        public static CompareResult toCompareResult(int i) {
            if (i == 0) {
                return CompareResult.SAME;
            }

            if (i < 0) {
                return CompareResult.SMALLER;
            }

            return CompareResult.BIGGER;
        }
    }
}
