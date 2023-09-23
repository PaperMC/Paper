package org.bukkit.util;

import static org.junit.jupiter.api.Assertions.*;
import org.bukkit.block.BlockFace;
import org.junit.jupiter.api.Test;

public class VectorTest {

    @Test
    public void testNormalisedVectors() {
        assertFalse(new Vector(1, 0, 0).multiply(1.1).isNormalized());

        assertTrue(new Vector(1, 1, 1).normalize().isNormalized());
        assertTrue(new Vector(1, 0, 0).isNormalized());
    }

    @Test
    public void testNullVectorAxis() {
        assertThrows(IllegalArgumentException.class, () -> new Vector(0, 1, 0).rotateAroundAxis(null, Math.PI));
    }

    @Test
    public void testBypassingAxisVector() {
        new Vector(0, 1, 0).rotateAroundNonUnitAxis(new Vector(1, 1, 1), Math.PI); // This will result some weird result, but there may be some use for it for some people
    }

    @Test
    public void testResizeAxis() {
        Vector axis = new Vector(0, 10, 0);
        assertEquals(BlockFace.EAST.getDirection().rotateAroundAxis(axis, Math.PI * 0.5), BlockFace.NORTH.getDirection());
    }

    /**
     * As west to east are the x axis in Minecraft, rotating around it from up
     * should lead to up -> south -> down -> north.
     */
    @Test
    public void testRotationAroundX() {
        Vector vector = BlockFace.UP.getDirection();
        assertEquals(BlockFace.SOUTH.getDirection(), vector.clone().rotateAroundX(Math.PI * 0.5)); // Should rotate around x axis for 1/4 of a circle.
        assertEquals(BlockFace.DOWN.getDirection(), vector.clone().rotateAroundX(Math.PI * 1.0)); // Should rotate around x axis for 2/4 of a circle.
        assertEquals(BlockFace.NORTH.getDirection(), vector.clone().rotateAroundX(Math.PI * 1.5)); // Should rotate around x axis for 3/4 of a circle.
        assertEquals(BlockFace.UP.getDirection(), vector.clone().rotateAroundX(Math.PI * 2.0)); // Should rotate around x axis for 4/4 of a circle.
    }

    /**
     * As up to down are the y axis in Minecraft, rotating around it from up
     * should lead to east (positive x) -> south -> west -> north.
     */
    @Test
    public void testRotationAroundY() {
        Vector vector = BlockFace.EAST.getDirection();
        assertEquals(BlockFace.NORTH.getDirection(), vector.clone().rotateAroundY(Math.PI * 0.5)); // Should rotate around x axis for 1/4 of a circle.
        assertEquals(BlockFace.WEST.getDirection(), vector.clone().rotateAroundY(Math.PI * 1.0)); // Should rotate around x axis for 2/4 of a circle.
        assertEquals(BlockFace.SOUTH.getDirection(), vector.clone().rotateAroundY(Math.PI * 1.5)); // Should rotate around x axis for 3/4 of a circle.
        assertEquals(BlockFace.EAST.getDirection(), vector.clone().rotateAroundY(Math.PI * 2.0)); // Should rotate around x axis for 4/4 of a circle.
    }

    /**
     * As up to down are the y axis in Minecraft, rotating around it from up
     * should lead to east (positive x) -> south -> west -> north.
     */
    @Test
    public void testRotationAroundYUsingCustomAxis() {
        Vector vector = BlockFace.EAST.getDirection();
        Vector axis = BlockFace.UP.getDirection();
        assertEquals(BlockFace.NORTH.getDirection(), vector.clone().rotateAroundAxis(axis, Math.PI * 0.5)); // Should rotate around x axis for 1/4 of a circle.
        assertEquals(BlockFace.WEST.getDirection(), vector.clone().rotateAroundAxis(axis, Math.PI * 1.0)); // Should rotate around x axis for 2/4 of a circle.
        assertEquals(BlockFace.SOUTH.getDirection(), vector.clone().rotateAroundAxis(axis, Math.PI * 1.5)); // Should rotate around x axis for 3/4 of a circle.
        assertEquals(BlockFace.EAST.getDirection(), vector.clone().rotateAroundAxis(axis, Math.PI * 2.0)); // Should rotate around x axis for 4/4 of a circle.
    }

    /**
     * As south to north are the z axis in Minecraft, rotating around it from up
     * should lead to up (positive y) -> west -> down -> east.
     */
    @Test
    public void testRotationAroundZ() {
        Vector vector = BlockFace.UP.getDirection();
        assertEquals(BlockFace.WEST.getDirection(), vector.clone().rotateAroundZ(Math.PI * 0.5)); // Should rotate around x axis for 1/4 of a circle.
        assertEquals(BlockFace.DOWN.getDirection(), vector.clone().rotateAroundZ(Math.PI * 1.0)); // Should rotate around x axis for 2/4 of a circle.
        assertEquals(BlockFace.EAST.getDirection(), vector.clone().rotateAroundZ(Math.PI * 1.5)); // Should rotate around x axis for 3/4 of a circle.
        assertEquals(BlockFace.UP.getDirection(), vector.clone().rotateAroundZ(Math.PI * 2.0)); // Should rotate around x axis for 4/4 of a circle.
    }

    @Test
    public void testRotationAroundAxis() {
        Vector axis = new Vector(1, 0, 1);
        assertEquals(new Vector(0, 1, 0).rotateAroundNonUnitAxis(axis, Math.PI * 0.5), new Vector(-1, 0, 1));
    }

    @Test
    public void testRotationAroundAxisNonUnit() {
        Vector axis = new Vector(0, 2, 0);
        Vector v = BlockFace.EAST.getDirection();

        assertEquals(v.rotateAroundNonUnitAxis(axis, Math.PI * 0.5), BlockFace.NORTH.getDirection().multiply(2));
    }

    /**
     * This will be a bit tricky to prove so we will try to simply see if the
     * vectors have correct angle to each other This will work with any two
     * vectors, as the rotation will keep the angle the same.
     */
    @Test
    public void testRotationAroundCustomAngle() {
        Vector axis = new Vector(-30, 1, 2000).normalize();
        Vector v = new Vector(53, 12, 98);

        float a = v.angle(axis);
        double stepSize = Math.PI / 21;
        for (int i = 0; i < 42; i++) {
            v.rotateAroundAxis(axis, stepSize);
            assertEquals(a, v.angle(axis), Vector.getEpsilon());
        }
    }

    @Test
    public void testSmallAngle() {
        Vector a = new Vector(-0.13154885489775203, 0.0, 0.12210868381700482);
        Vector b = new Vector(-0.7329152226448059, -0.0, 0.6803199648857117);

        assertTrue(Double.isFinite(a.angle(b)));
    }

    @Test
    public void testIsZero() {
        assertTrue(new Vector().isZero());
        assertTrue(new Vector(0, 0, 0).isZero());

        Vector vector = new Vector(1, 2, 3);
        vector.zero();
        assertTrue(vector.isZero());
    }
}
