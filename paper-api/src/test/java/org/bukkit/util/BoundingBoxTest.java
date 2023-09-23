package org.bukkit.util;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.junit.jupiter.api.Test;

public class BoundingBoxTest {

    private static final double delta = 1.0 / 1000000;

    @Test
    public void testConstruction() {
        BoundingBox expected = new BoundingBox(-1, -1, -1, 1, 2, 3);
        assertThat(expected.getMin(), is(new Vector(-1, -1, -1)));
        assertThat(expected.getMax(), is(new Vector(1, 2, 3)));
        assertThat(expected.getCenter(), is(new Vector(0.0D, 0.5D, 1.0D)));
        assertThat(expected.getWidthX(), is(2.0D));
        assertThat(expected.getHeight(), is(3.0D));
        assertThat(expected.getWidthZ(), is(4.0D));
        assertThat(expected.getVolume(), is(24.0D));

        assertThat(BoundingBox.of(new Vector(-1, -1, -1), new Vector(1, 2, 3)), is(expected));
        assertThat(BoundingBox.of(new Vector(1, 2, 3), new Vector(-1, -1, -1)), is(expected));
        assertThat(BoundingBox.of(new Location(null, -1, -1, -1), new Location(null, 1, 2, 3)), is(expected));
        assertThat(BoundingBox.of(new Vector(0.0D, 0.5D, 1.0D), 1.0D, 1.5D, 2.0D), is(expected));
        assertThat(BoundingBox.of(new Location(null, 0.0D, 0.5D, 1.0D), 1.0D, 1.5D, 2.0D), is(expected));
    }

    @Test
    public void testContains() {
        BoundingBox aabb = new BoundingBox(-1, -1, -1, 1, 2, 3);
        assertThat(aabb.contains(-0.5D, 0.0D, 0.5D), is(true));
        assertThat(aabb.contains(-1.0D, -1.0D, -1.0D), is(true));
        assertThat(aabb.contains(1.0D, 2.0D, 3.0D), is(false));
        assertThat(aabb.contains(-1.0D, 1.0D, 4.0D), is(false));
        assertThat(aabb.contains(new Vector(-0.5D, 0.0D, 0.5D)), is(true));

        assertThat(aabb.contains(new BoundingBox(-0.5D, -0.5D, -0.5D, 0.5D, 1.0D, 2.0D)), is(true));
        assertThat(aabb.contains(aabb), is(true));
        assertThat(aabb.contains(new BoundingBox(-1, -1, -1, 1, 1, 3)), is(true));
        assertThat(aabb.contains(new BoundingBox(-2, -1, -1, 1, 2, 3)), is(false));
        assertThat(aabb.contains(new Vector(-0.5D, -0.5D, -0.5D), new Vector(0.5D, 1.0D, 2.0D)), is(true));
    }

    @Test
    public void testOverlaps() {
        BoundingBox aabb = new BoundingBox(-1, -1, -1, 1, 2, 3);
        assertThat(aabb.contains(aabb), is(true));
        assertThat(aabb.overlaps(new BoundingBox(-2, -2, -2, 0, 0, 0)), is(true));
        assertThat(aabb.overlaps(new BoundingBox(0.5D, 1.5D, 2.5D, 1, 2, 3)), is(true));
        assertThat(aabb.overlaps(new BoundingBox(0.5D, 1.5D, 2.5D, 2, 3, 4)), is(true));
        assertThat(aabb.overlaps(new BoundingBox(-2, -2, -2, -1, -1, -1)), is(false));
        assertThat(aabb.overlaps(new BoundingBox(1, 2, 3, 2, 3, 4)), is(false));
        assertThat(aabb.overlaps(new Vector(0.5D, 1.5D, 2.5D), new Vector(1, 2, 3)), is(true));
    }

    @Test
    public void testDegenerate() {
        BoundingBox aabb = new BoundingBox(0, 0, 0, 0, 0, 0);
        assertThat(aabb.getWidthX(), is(0.0D));
        assertThat(aabb.getHeight(), is(0.0D));
        assertThat(aabb.getWidthZ(), is(0.0D));
        assertThat(aabb.getVolume(), is(0.0D));
    }

    @Test
    public void testShift() {
        BoundingBox aabb = new BoundingBox(0, 0, 0, 1, 1, 1);
        assertThat(aabb.clone().shift(1, 2, 3), is(new BoundingBox(1, 2, 3, 2, 3, 4)));
        assertThat(aabb.clone().shift(-1, -2, -3), is(new BoundingBox(-1, -2, -3, 0, -1, -2)));
        assertThat(aabb.clone().shift(new Vector(1, 2, 3)), is(new BoundingBox(1, 2, 3, 2, 3, 4)));
        assertThat(aabb.clone().shift(new Location(null, 1, 2, 3)), is(new BoundingBox(1, 2, 3, 2, 3, 4)));
    }

    @Test
    public void testUnion() {
        BoundingBox aabb1 = new BoundingBox(0, 0, 0, 1, 1, 1);
        assertThat(aabb1.clone().union(new BoundingBox(-2, -2, -2, -1, -1, -1)), is(new BoundingBox(-2, -2, -2, 1, 1, 1)));
        assertThat(aabb1.clone().union(1, 2, 3), is(new BoundingBox(0, 0, 0, 1, 2, 3)));
        assertThat(aabb1.clone().union(new Vector(1, 2, 3)), is(new BoundingBox(0, 0, 0, 1, 2, 3)));
        assertThat(aabb1.clone().union(new Location(null, 1, 2, 3)), is(new BoundingBox(0, 0, 0, 1, 2, 3)));
    }

    @Test
    public void testIntersection() {
        BoundingBox aabb = new BoundingBox(-1, -1, -1, 1, 2, 3);
        assertThat(aabb.clone().intersection(new BoundingBox(-2, -2, -2, 4, 4, 4)), is(aabb));
        assertThat(aabb.clone().intersection(new BoundingBox(-2, -2, -2, 1, 1, 1)), is(new BoundingBox(-1, -1, -1, 1, 1, 1)));
    }

    @Test
    public void testExpansion() {
        BoundingBox aabb = new BoundingBox(0, 0, 0, 2, 2, 2);
        assertThat(aabb.clone().expand(1, 2, 3, 1, 2, 3), is(new BoundingBox(-1, -2, -3, 3, 4, 5)));
        assertThat(aabb.clone().expand(-1, -2, -3, 1, 2, 3), is(new BoundingBox(1, 2, 3, 3, 4, 5)));
        assertThat(aabb.clone().expand(1, 2, 3, -1, -2, -3), is(new BoundingBox(-1, -2, -3, 1, 0, -1)));
        assertThat(aabb.clone().expand(-1, -2, -3, -0.5D, -0.5, -3), is(new BoundingBox(1, 1.5D, 1, 1.5D, 1.5D, 1)));

        assertThat(aabb.clone().expand(1, 2, 3), is(new BoundingBox(-1, -2, -3, 3, 4, 5)));
        assertThat(aabb.clone().expand(-0.1, -0.5, -2), is(new BoundingBox(0.1D, 0.5D, 1, 1.9D, 1.5D, 1)));
        assertThat(aabb.clone().expand(new Vector(1, 2, 3)), is(new BoundingBox(-1, -2, -3, 3, 4, 5)));

        assertThat(aabb.clone().expand(1), is(new BoundingBox(-1, -1, -1, 3, 3, 3)));
        assertThat(aabb.clone().expand(-0.5D), is(new BoundingBox(0.5D, 0.5D, 0.5D, 1.5D, 1.5D, 1.5D)));

        assertThat(aabb.clone().expand(1, 0, 0, 0.5D), is(new BoundingBox(0, 0, 0, 2.5D, 2, 2)));
        assertThat(aabb.clone().expand(1, 0, 0, -0.5D), is(new BoundingBox(0, 0, 0, 1.5D, 2, 2)));
        assertThat(aabb.clone().expand(-1, 0, 0, 0.5D), is(new BoundingBox(-0.5D, 0, 0, 2, 2, 2)));
        assertThat(aabb.clone().expand(-1, 0, 0, -0.5D), is(new BoundingBox(0.5D, 0, 0, 2, 2, 2)));

        assertThat(aabb.clone().expand(0, 1, 0, 0.5D), is(new BoundingBox(0, 0, 0, 2, 2.5D, 2)));
        assertThat(aabb.clone().expand(0, 1, 0, -0.5D), is(new BoundingBox(0, 0, 0, 2, 1.5D, 2)));
        assertThat(aabb.clone().expand(0, -1, 0, 0.5D), is(new BoundingBox(0, -0.5D, 0, 2, 2, 2)));
        assertThat(aabb.clone().expand(0, -1, 0, -0.5D), is(new BoundingBox(0, 0.5D, 0, 2, 2, 2)));

        assertThat(aabb.clone().expand(0, 0, 1, 0.5D), is(new BoundingBox(0, 0, 0, 2, 2, 2.5D)));
        assertThat(aabb.clone().expand(0, 0, 1, -0.5D), is(new BoundingBox(0, 0, 0, 2, 2, 1.5D)));
        assertThat(aabb.clone().expand(0, 0, -1, 0.5D), is(new BoundingBox(0, 0, -0.5D, 2, 2, 2)));
        assertThat(aabb.clone().expand(0, 0, -1, -0.5D), is(new BoundingBox(0, 0, 0.5D, 2, 2, 2)));

        assertThat(aabb.clone().expand(new Vector(1, 0, 0), 0.5D), is(new BoundingBox(0, 0, 0, 2.5D, 2, 2)));
        assertThat(aabb.clone().expand(BlockFace.EAST, 0.5D), is(new BoundingBox(0, 0, 0, 2.5D, 2, 2)));
        assertThat(aabb.clone().expand(BlockFace.NORTH_NORTH_WEST, 1.0D), is(aabb.clone().expand(BlockFace.NORTH_NORTH_WEST.getDirection(), 1.0D)));
        assertThat(aabb.clone().expand(BlockFace.SELF, 1.0D), is(aabb));

        BoundingBox expanded = aabb.clone().expand(BlockFace.NORTH_WEST, 1.0D);
        assertThat(expanded.getWidthX(), is(closeTo(aabb.getWidthX() + Math.sqrt(0.5D), delta)));
        assertThat(expanded.getWidthZ(), is(closeTo(aabb.getWidthZ() + Math.sqrt(0.5D), delta)));
        assertThat(expanded.getHeight(), is(aabb.getHeight()));

        assertThat(aabb.clone().expandDirectional(1, 2, 3), is(new BoundingBox(0, 0, 0, 3, 4, 5)));
        assertThat(aabb.clone().expandDirectional(-1, -2, -3), is(new BoundingBox(-1, -2, -3, 2, 2, 2)));
        assertThat(aabb.clone().expandDirectional(new Vector(1, 2, 3)), is(new BoundingBox(0, 0, 0, 3, 4, 5)));
    }

    @Test
    public void testRayTrace() {
        BoundingBox aabb = new BoundingBox(-1, -1, -1, 1, 1, 1);

        assertThat(aabb.rayTrace(new Vector(-2, 0, 0), new Vector(1, 0, 0), 10),
                is(new RayTraceResult(new Vector(-1, 0, 0), BlockFace.WEST)));
        assertThat(aabb.rayTrace(new Vector(2, 0, 0), new Vector(-1, 0, 0), 10),
                is(new RayTraceResult(new Vector(1, 0, 0), BlockFace.EAST)));

        assertThat(aabb.rayTrace(new Vector(0, -2, 0), new Vector(0, 1, 0), 10),
                is(new RayTraceResult(new Vector(0, -1, 0), BlockFace.DOWN)));
        assertThat(aabb.rayTrace(new Vector(0, 2, 0), new Vector(0, -1, 0), 10),
                is(new RayTraceResult(new Vector(0, 1, 0), BlockFace.UP)));

        assertThat(aabb.rayTrace(new Vector(0, 0, -2), new Vector(0, 0, 1), 10),
                is(new RayTraceResult(new Vector(0, 0, -1), BlockFace.NORTH)));
        assertThat(aabb.rayTrace(new Vector(0, 0, 2), new Vector(0, 0, -1), 10),
                is(new RayTraceResult(new Vector(0, 0, 1), BlockFace.SOUTH)));

        assertThat(aabb.rayTrace(new Vector(0, 0, 0), new Vector(1, 0, 0), 10),
                is(new RayTraceResult(new Vector(1, 0, 0), BlockFace.EAST)));
        assertThat(aabb.rayTrace(new Vector(0, 0, 0), new Vector(-1, 0, 0), 10),
                is(new RayTraceResult(new Vector(-1, 0, 0), BlockFace.WEST)));

        assertThat(aabb.rayTrace(new Vector(0, 0, 0), new Vector(0, 1, 0), 10),
                is(new RayTraceResult(new Vector(0, 1, 0), BlockFace.UP)));
        assertThat(aabb.rayTrace(new Vector(0, 0, 0), new Vector(0, -1, 0), 10),
                is(new RayTraceResult(new Vector(0, -1, 0), BlockFace.DOWN)));

        assertThat(aabb.rayTrace(new Vector(0, 0, 0), new Vector(0, 0, 1), 10),
                is(new RayTraceResult(new Vector(0, 0, 1), BlockFace.SOUTH)));
        assertThat(aabb.rayTrace(new Vector(0, 0, 0), new Vector(0, 0, -1), 10),
                is(new RayTraceResult(new Vector(0, 0, -1), BlockFace.NORTH)));

        assertThat(aabb.rayTrace(new Vector(-2, -2, -2), new Vector(1, 0, 0), 10), is(nullValue()));
        assertThat(aabb.rayTrace(new Vector(-2, -2, -2), new Vector(0, 1, 0), 10), is(nullValue()));
        assertThat(aabb.rayTrace(new Vector(-2, -2, -2), new Vector(0, 0, 1), 10), is(nullValue()));

        assertThat(aabb.rayTrace(new Vector(0, 0, -3), new Vector(1, 0, 1), 10), is(nullValue()));
        assertThat(aabb.rayTrace(new Vector(0, 0, -2), new Vector(1, 0, 2), 10),
                is(new RayTraceResult(new Vector(0.5D, 0, -1), BlockFace.NORTH)));

        // corner/edge hits yield unspecified block face:
        assertThat(aabb.rayTrace(new Vector(2, 2, 2), new Vector(-1, -1, -1), 10),
                anyOf(is(new RayTraceResult(new Vector(1, 1, 1), BlockFace.EAST)),
                        is(new RayTraceResult(new Vector(1, 1, 1), BlockFace.UP)),
                        is(new RayTraceResult(new Vector(1, 1, 1), BlockFace.SOUTH))));

        assertThat(aabb.rayTrace(new Vector(-2, -2, -2), new Vector(1, 1, 1), 10),
                anyOf(is(new RayTraceResult(new Vector(-1, -1, -1), BlockFace.WEST)),
                        is(new RayTraceResult(new Vector(-1, -1, -1), BlockFace.DOWN)),
                        is(new RayTraceResult(new Vector(-1, -1, -1), BlockFace.NORTH))));

        assertThat(aabb.rayTrace(new Vector(0, 0, -2), new Vector(1, 0, 1), 10),
                anyOf(is(new RayTraceResult(new Vector(1, 0, -1), BlockFace.NORTH)),
                        is(new RayTraceResult(new Vector(1, 0, -1), BlockFace.EAST))));
    }

    @Test
    public void testSerialization() {
        BoundingBox aabb = new BoundingBox(-1, -1, -1, 1, 1, 1);
        Map<String, Object> serialized = aabb.serialize();
        BoundingBox deserialized = BoundingBox.deserialize(serialized);
        assertThat(deserialized, is(aabb));
    }
}
