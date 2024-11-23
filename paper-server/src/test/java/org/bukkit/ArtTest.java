package org.bukkit;

import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.Stream;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@VanillaFeature
public class ArtTest {

    public static Stream<Arguments> widthData() {
        return Stream.of(Arguments.of(Art.KEBAB, 1),
                         Arguments.of(Art.WANDERER, 1),
                         Arguments.of(Art.POOL, 2),
                         Arguments.of(Art.MATCH, 2),
                         Arguments.of(Art.BOUQUET, 3),
                         Arguments.of(Art.BACKYARD, 3),
                         Arguments.of(Art.FIGHTERS, 4),
                         Arguments.of(Art.SKELETON, 4),
                         Arguments.of(Art.POINTER, 4));
    }

    public static Stream<Arguments> heightData() {
        return Stream.of(Arguments.of(Art.KEBAB, 1),
                         Arguments.of(Art.WANDERER, 2),
                         Arguments.of(Art.POOL, 1),
                         Arguments.of(Art.MATCH, 2),
                         Arguments.of(Art.BOUQUET, 3),
                         Arguments.of(Art.BACKYARD, 4),
                         Arguments.of(Art.FIGHTERS, 2),
                         Arguments.of(Art.SKELETON, 3),
                         Arguments.of(Art.POINTER, 4));
    }

    @ParameterizedTest
    @MethodSource("widthData")
    public void testWidth(Art art, int expected) {
        assertEquals(expected, art.getBlockWidth(), """
                Art '%s' does not have the correct width.
                This can be caused by either a change in the Implementation.
                Or the width for this specific art was changed in which case the test needs to be updated.
                """.formatted(art.getKey()));
    }

    @ParameterizedTest
    @MethodSource("heightData")
    public void testHeight(Art art, int expected) {
        assertEquals(expected, art.getBlockHeight(), """
                Art '%s' does not have the correct height.
                This can be caused by either a change in the Implementation.
                Or the height for this specific art was changed in which case the test needs to be updated.
                """.formatted(art.getKey()));
    }
}
