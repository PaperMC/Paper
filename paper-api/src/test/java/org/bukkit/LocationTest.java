package org.bukkit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import org.bukkit.util.Vector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.ImmutableList;

@RunWith(Parameterized.class)
public class LocationTest {
    private static final double δ = 1.0 / 1000000;
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

    @Parameters(name= "{index}: {0}")
    public static List<Object[]> data() {
        Random RANDOM = new Random(1l); // Test is deterministic
        int r = 0;
        return ImmutableList.<Object[]>of(
            new Object[] { "X",
                1, 0, 0,
                270, 0
            },
            new Object[] { "-X",
                -1, 0, 0,
                90, 0
            },
            new Object[] { "Z",
                0, 0, 1,
                0, 0
            },
            new Object[] { "-Z",
                0, 0, -1,
                180, 0
            },
            new Object[] { "Y",
                0, 1, 0,
                0, -90 // Zero is here as a "default" value
            },
            new Object[] { "-Y",
                0, -1, 0,
                0, 90 // Zero is here as a "default" value
            },
            new Object[] { "X Z",
                HALF_UNIT, 0, HALF_UNIT,
                (270 + 360) / 2, 0
            },
            new Object[] { "X -Z",
                HALF_UNIT, 0, -HALF_UNIT,
                (270 + 180) / 2, 0
            },
            new Object[] { "-X -Z",
                -HALF_UNIT, 0, -HALF_UNIT,
                (90 + 180) / 2, 0
            },
            new Object[] { "-X Z",
                -HALF_UNIT, 0, HALF_UNIT,
                (90 + 0) / 2, 0
            },
            new Object[] { "X Y Z",
                HALF_HALF_UNIT, HALF_UNIT, HALF_HALF_UNIT,
                (270 + 360) / 2, -45
            },
            new Object[] { "-X -Y -Z",
                -HALF_HALF_UNIT, -HALF_UNIT, -HALF_HALF_UNIT,
                (90 + 180) / 2, 45
            },
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

    private static Object[] getRandom(Random random, int index) {
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

        return new Object[] { "R" + index,
            vector.getX(), vector.getY(), vector.getZ(),
            location.getYaw(), location.getPitch()
        };
    }

    @Parameter(0)
    public String nane;
    @Parameter(1)
    public double x;
    @Parameter(2)
    public double y;
    @Parameter(3)
    public double z;
    @Parameter(4)
    public float yaw;
    @Parameter(5)
    public float pitch;

    @Test
    public void testExpectedPitchYaw() {
        Location location = getEmptyLocation().setDirection(getVector());

        assertThat((double) location.getYaw(), is(closeTo(yaw, δ)));
        assertThat((double) location.getPitch(), is(closeTo(pitch, δ)));
    }

    @Test
    public void testExpectedXYZ() {
        Vector vector = getLocation().getDirection();

        assertThat(vector.getX(), is(closeTo(x, δ)));
        assertThat(vector.getY(), is(closeTo(y, δ)));
        assertThat(vector.getZ(), is(closeTo(z, δ)));
    }

    private Vector getVector() {
        return new Vector(x, y, z);
    }

    private static Location getEmptyLocation() {
        return new Location(null, 0, 0, 0);
    }

    private Location getLocation() {
        Location location = getEmptyLocation();
        location.setYaw(yaw);
        location.setPitch(pitch);
        return location;
    }
}
