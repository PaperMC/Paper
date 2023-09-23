package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import net.minecraft.core.EnumDirection;
import net.minecraft.world.level.block.BlockCake;
import net.minecraft.world.level.block.BlockChest;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Cake;
import org.bukkit.block.data.type.Chest;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

public class BlockDataTest extends AbstractTestingBase {

    @Test
    public void testParsing() {
        BlockData cakeTest = CraftBlockData.fromData(Blocks.CAKE.defaultBlockState().setValue(BlockCake.BITES, 3));

        BlockData materialString = CraftBlockData.newData(Material.CAKE, "[bites=3]");
        assertThat(materialString, is(cakeTest));

        BlockData combined = CraftBlockData.newData(null, "cake[bites=3]");
        assertThat(combined, is(cakeTest));

        BlockData combinedMinecraft = CraftBlockData.newData(null, "minecraft:cake[bites=3]");
        assertThat(combinedMinecraft, is(cakeTest));

        BlockData inverted = CraftBlockData.newData(null, cakeTest.getAsString());
        assertThat(inverted, is(cakeTest));
    }

    @Test
    public void testBadMaterial() {
        assertThrows(IllegalArgumentException.class, () -> CraftBlockData.newData(null, "invalid"));
    }

    @Test
    public void testBadSyntax() {
        assertThrows(IllegalArgumentException.class, () -> CraftBlockData.newData(null, "minecraft:cake[bites=3"));
    }

    @Test
    public void testDoubleMaterial() {
        assertThrows(IllegalArgumentException.class, () -> CraftBlockData.newData(Material.CAKE, "minecraft:cake[bites=3]"));
    }

    @Test
    public void testMistake() {
        BlockData cakeTest = CraftBlockData.fromData(Blocks.CAKE.defaultBlockState().setValue(BlockCake.BITES, 3));

        assertThrows(IllegalArgumentException.class, () -> CraftBlockData.newData(Material.CAKE, cakeTest.toString()));
    }

    @Test
    public void testItem() {
        assertThrows(IllegalArgumentException.class, () -> CraftBlockData.newData(Material.DIAMOND_AXE, null));
    }

    @Test
    public void testItemParse() {
        assertThrows(IllegalArgumentException.class, () -> CraftBlockData.newData(null, "minecraft:diamond_axe"));
    }

    @Test
    public void testClone() {
        Cake cakeTest = (Cake) CraftBlockData.fromData(Blocks.CAKE.defaultBlockState().setValue(BlockCake.BITES, 3));
        Cake clone = (Cake) cakeTest.clone();

        assertNotSame(cakeTest, clone, "Clone did not return new object");
        assertThat(clone, is(cakeTest), "Clone is not equal");

        clone.setBites(1);
        assertThat(clone, is(not(cakeTest)), "Clone is not actually clone");
    }

    @Test
    public void testMerge() {
        Chest trueTarget = (Chest) CraftBlockData.newData(null, "minecraft:chest[facing=east,waterlogged=true]");
        Chest falseTarget = (Chest) CraftBlockData.newData(null, "minecraft:chest[facing=east,waterlogged=false]");
        Chest waterlogged = (Chest) CraftBlockData.newData(null, "minecraft:chest[waterlogged=true]");

        BlockData candidate;

        assertNotEquals(trueTarget, waterlogged, "Target and match are not yet equal");
        candidate = trueTarget.merge(waterlogged);
        assertEquals(trueTarget, candidate, "Target and candidate are now equal");

        assertNotEquals(falseTarget, waterlogged, "Target and match are not yet equal");
        candidate = falseTarget.merge(waterlogged);
        assertNotEquals(falseTarget, candidate, "Target and candidate are still not equal");
    }

    @Test
    public void testMergeAny() {
        Chest trueTarget = (Chest) CraftBlockData.newData(null, "minecraft:chest[facing=east,waterlogged=true]");
        Chest falseTarget = (Chest) CraftBlockData.newData(null, "minecraft:chest[facing=east,waterlogged=false]");
        Chest any = (Chest) CraftBlockData.newData(null, "minecraft:chest");

        BlockData candidate;

        assertNotEquals(trueTarget, any, "Target and match are not yet equal");
        candidate = trueTarget.merge(any);
        assertEquals(trueTarget, candidate, "Target and candidate are now equal");

        assertNotEquals(falseTarget, any, "Target and match are not yet equal");
        candidate = falseTarget.merge(any);
        assertEquals(falseTarget, candidate, "Target and candidate are now equal");
    }

    @Test
    public void testCannotMerge1() {
        Chest one = (Chest) CraftBlockData.newData(null, "minecraft:chest[facing=east,waterlogged=true]");
        Chest two = (Chest) CraftBlockData.fromData(Blocks.CHEST.defaultBlockState());

        assertThrows(IllegalArgumentException.class, () -> one.merge(two));
    }

    @Test
    public void testCannotMerge2() {
        Chest one = (Chest) CraftBlockData.newData(null, "minecraft:chest[waterlogged=true]");
        Chest two = (Chest) CraftBlockData.newData(null, "minecraft:chest[waterlogged=true]");

        one.merge(two);

        two.setFacing(BlockFace.NORTH);
        assertThrows(IllegalArgumentException.class, () -> one.merge(two));
    }

    @Test
    public void testCannotMerge3() {
        Chest one = (Chest) CraftBlockData.newData(null, "minecraft:chest[waterlogged=true]");
        Chest two = (Chest) CraftBlockData.newData(null, "minecraft:trapped_chest[waterlogged=true]");

        assertThrows(IllegalArgumentException.class, () -> one.merge(two));
    }

    @Test
    public void testMatch() {
        assertTrue(CraftBlockData.newData(null, "minecraft:chest[facing=east,waterlogged=true]").matches(CraftBlockData.newData(null, "minecraft:chest[waterlogged=true]")));
        assertFalse(CraftBlockData.newData(null, "minecraft:chest[facing=east,waterlogged=false]").matches(CraftBlockData.newData(null, "minecraft:chest[waterlogged=true]")));
        assertTrue(CraftBlockData.newData(null, "minecraft:chest[facing=east,waterlogged=true]").matches(CraftBlockData.newData(null, "minecraft:chest")));
        assertFalse(CraftBlockData.newData(null, "minecraft:trapped_chest[facing=east,waterlogged=false]").matches(CraftBlockData.newData(null, "minecraft:chest[waterlogged=true]")));
        assertTrue(CraftBlockData.newData(null, "minecraft:chest[facing=east,waterlogged=true]").matches(CraftBlockData.newData(null, "minecraft:chest[waterlogged=true,facing=east]")));

        Chest one = (Chest) CraftBlockData.fromData(Blocks.CHEST.defaultBlockState().setValue(BlockChest.FACING, EnumDirection.EAST));
        Chest two = (Chest) CraftBlockData.newData(null, "minecraft:chest[waterlogged=false]");

        assertTrue(one.matches(two));
        assertFalse(two.matches(one));
    }

    @Test
    public void testGetAsString() {
        String dataString = "minecraft:chest[facing=east,waterlogged=true]";
        BlockData data = CraftBlockData.newData(null, dataString);

        assertThat(data.getAsString(true), is(dataString));
        assertThat(data.getAsString(false), is("minecraft:chest[facing=east,type=single,waterlogged=true]"));
    }

    @Test
    public void testGetAsString2() {
        Chest data = (Chest) CraftBlockData.fromData(Blocks.CHEST.defaultBlockState().setValue(BlockChest.FACING, EnumDirection.EAST));

        assertThat(data.getAsString(true), is("minecraft:chest[facing=east,type=single,waterlogged=false]"));
        assertThat(data.getAsString(false), is("minecraft:chest[facing=east,type=single,waterlogged=false]"));
    }
}
