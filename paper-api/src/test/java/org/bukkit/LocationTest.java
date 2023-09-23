package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import java.util.Random;
import java.util.stream.Stream;
import org.bukkit.util.Vector;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LocationTest {
    private static final double delta = 1.0 / 1000000;
    /**
     * <pre>
     * a² + b² = c², a = b
     * => 2∙(a²) = 2∙(b²) = c², c = 1
     * => 2∙(a²) = 1
     * => a² = 1/2
     * => a = √(1/2) ∎
     * </pre>
     */
    private static final double HALF_UNIT = Math.sqrt(1 / 2f);
    /**
     * <pre>
     * a² + b² = c², c = √(1/2)
     * => a² + b² = √(1/2)², a = b
     * => 2∙(a²) = 2∙(b²) = 1/2
     * => a² = 1/4
     * => a = √(1/4) ∎
     * </pre>
     */
    private static final double HALF_HALF_UNIT = Math.sqrt(1 / 4f);

    public static Stream<Arguments> data() {
        Random RANDOM = new Random(1L); // Test is deterministic
        int r = 0;
        return Stream.of(
            Arguments.of("X",
                1, 0, 0,
                270, 0
            ),
            Arguments.of("-X",
                -1, 0, 0,
                90, 0
            ),
            Arguments.of("Z",
                0, 0, 1,
                0, 0
            ),
            Arguments.of("-Z",
                0, 0, -1,
                180, 0
            ),
            Arguments.of("Y",
                0, 1, 0,
                0, -90 // Zero is here as a "default" value
            ),
            Arguments.of("-Y",
                0, -1, 0,
                0, 90 // Zero is here as a "default" value
            ),
            Arguments.of("X Z",
                HALF_UNIT, 0, HALF_UNIT,
                (270 + 360) / 2, 0
            ),
            Arguments.of("X -Z",
                HALF_UNIT, 0, -HALF_UNIT,
                (270 + 180) / 2, 0
            ),
            Arguments.of("-X -Z",
                -HALF_UNIT, 0, -HALF_UNIT,
                (90 + 180) / 2, 0
            ),
            Arguments.of("-X Z",
                -HALF_UNIT, 0, HALF_UNIT,
                (90 + 0) / 2, 0
            ),
            Arguments.of("X Y Z",
                HALF_HALF_UNIT, HALF_UNIT, HALF_HALF_UNIT,
                (270 + 360) / 2, -45
            ),
            Arguments.of("-X -Y -Z",
                -HALF_HALF_UNIT, -HALF_UNIT, -HALF_HALF_UNIT,
                (90 + 180) / 2, 45
            ),
            getRandom(RANDOM, r++),
            getRandom(RANDOM, r++),
            getRandom(RANDOM, r++),
            getRandom(RANDOM, r++),
            getRandom(RANDOM, r++),
            getRandom(RANDOM, r++),
            getRandom(RANDOM, r++),
            getRandom(RANDOM, r++),
            getRandom(RANDOM, r++),
            getRandom(RANDOM, r++),
            getRandom(RANDOM, r++),
            getRandom(RANDOM, r++),
            getRandom(RANDOM, r++),
            getRandom(RANDOM, r++),
            getRandom(RANDOM, r++),
            getRandom(RANDOM, r++),
            getRandom(RANDOM, r++),
            getRandom(RANDOM, r++)
        );
    }

    private static Arguments getRandom(Random random, int index) {
        final double YAW_FACTOR = 360;
        final double YAW_OFFSET = 0;
        final double PITCH_FACTOR = 180;
        final double PITCH_OFFSET = -90;
        final double CARTESIAN_FACTOR = 256;
        final double CARTESIAN_OFFSET = -128;

        Vector vector;
        Location location;
        if (random.nextBoolean()) {
            float pitch = (float) (random.nextDouble() * PITCH_FACTOR + PITCH_OFFSET);
            float yaw = (float) (random.nextDouble() * YAW_FACTOR + YAW_OFFSET);

            location = getEmptyLocation();
            location.setPitch(pitch);
            location.setYaw(yaw);

            vector = location.getDirection();
        } else {
            double x = random.nextDouble() * CARTESIAN_FACTOR + CARTESIAN_OFFSET;
            double y = random.nextDouble() * CARTESIAN_FACTOR + CARTESIAN_OFFSET;
            double z = random.nextDouble() * CARTESIAN_FACTOR + CARTESIAN_OFFSET;

            location = getEmptyLocation();
            vector = new Vector(x, y, z).normalize();

            location.setDirection(vector);
        }

        return Arguments.of("R" + index,
            vector.getX(), vector.getY(), vector.getZ(),
            location.getYaw(), location.getPitch()
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testExpectedPitchYaw(String name, double x, double y, double z, float yaw, float pitch) {
        Location location = getEmptyLocation().setDirection(getVector(x, y, z));

        assertThat((double) location.getYaw(), is(closeTo(yaw, delta)));
        assertThat((double) location.getPitch(), is(closeTo(pitch, delta)));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testExpectedXYZ(String name, double x, double y, double z, float yaw, float pitch) {
        Vector vector = getLocation(yaw, pitch).getDirection();

        assertThat(vector.getX(), is(closeTo(x, delta)));
        assertThat(vector.getY(), is(closeTo(y, delta)));
        assertThat(vector.getZ(), is(closeTo(z, delta)));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testEquals(String name, double x, double y, double z, float yaw, float pitch) {
        Location first = getLocation(yaw, pitch).add(getVector(x, y, z));
        Location second = getLocation(yaw, pitch).add(getVector(x, y, z));

        assertThat(first.hashCode(), is(second.hashCode()));
        assertThat(first, is(second));
    }

    private Vector getVector(double x, double y, double z) {
        return new Vector(x, y, z);
    }

    private static final World TEST_WORLD = mock();

    private static Location getEmptyLocation() {
        return new Location(TEST_WORLD, 0, 0, 0);
    }

    private Location getLocation(float yaw, float pitch) {
        Location location = getEmptyLocation();
        location.setYaw(yaw);
        location.setPitch(pitch);
        return location;
    }
}
