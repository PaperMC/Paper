package org.bukkit;

import static org.hamcrest.Matchers.*;
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
import org.junit.Assert;
import org.junit.Test;

public class BlockDataTest extends AbstractTestingBase {

    @Test
    public void testParsing() {
        BlockData cakeTest = CraftBlockData.fromData(Blocks.CAKE.defaultBlockState().setValue(BlockCake.BITES, 3));

        BlockData materialString = CraftBlockData.newData(Material.CAKE, "[bites=3]");
        Assert.assertThat(materialString, is(cakeTest));

        BlockData combined = CraftBlockData.newData(null, "cake[bites=3]");
        Assert.assertThat(combined, is(cakeTest));

        BlockData combinedMinecraft = CraftBlockData.newData(null, "minecraft:cake[bites=3]");
        Assert.assertThat(combinedMinecraft, is(cakeTest));

        BlockData inverted = CraftBlockData.newData(null, cakeTest.getAsString());
        Assert.assertThat(inverted, is(cakeTest));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadMaterial() {
        CraftBlockData.newData(null, "invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadSyntax() {
        CraftBlockData.newData(null, "minecraft:cake[bites=3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoubleMaterial() {
        CraftBlockData.newData(Material.CAKE, "minecraft:cake[bites=3]");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMistake() {
        BlockData cakeTest = CraftBlockData.fromData(Blocks.CAKE.defaultBlockState().setValue(BlockCake.BITES, 3));

        CraftBlockData.newData(Material.CAKE, cakeTest.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testItem() {
        CraftBlockData.newData(Material.DIAMOND_AXE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testItemParse() {
        CraftBlockData.newData(null, "minecraft:diamond_axe");
    }

    @Test
    public void testClone() {
        Cake cakeTest = (Cake) CraftBlockData.fromData(Blocks.CAKE.defaultBlockState().setValue(BlockCake.BITES, 3));
        Cake clone = (Cake) cakeTest.clone();

        Assert.assertFalse("Clone did not return new object", cakeTest == clone);
        Assert.assertThat("Clone is not equal", clone, is(cakeTest));

        clone.setBites(1);
        Assert.assertThat("Clone is not actually clone", clone, is(not(cakeTest)));
    }

    @Test
    public void testMerge() {
        Chest trueTarget = (Chest) CraftBlockData.newData(null, "minecraft:chest[facing=east,waterlogged=true]");
        Chest falseTarget = (Chest) CraftBlockData.newData(null, "minecraft:chest[facing=east,waterlogged=false]");
        Chest waterlogged = (Chest) CraftBlockData.newData(null, "minecraft:chest[waterlogged=true]");

        BlockData candidate;

        Assert.assertFalse("Target and match are not yet equal", trueTarget.equals(waterlogged));
        candidate = trueTarget.merge(waterlogged);
        Assert.assertTrue("Target and candidate are now equal", trueTarget.equals(candidate));

        Assert.assertFalse("Target and match are not yet equal", falseTarget.equals(waterlogged));
        candidate = falseTarget.merge(waterlogged);
        Assert.assertFalse("Target and candidate are still not equal", falseTarget.equals(candidate));
    }

    @Test
    public void testMergeAny() {
        Chest trueTarget = (Chest) CraftBlockData.newData(null, "minecraft:chest[facing=east,waterlogged=true]");
        Chest falseTarget = (Chest) CraftBlockData.newData(null, "minecraft:chest[facing=east,waterlogged=false]");
        Chest any = (Chest) CraftBlockData.newData(null, "minecraft:chest");

        BlockData candidate;

        Assert.assertFalse("Target and match are not yet equal", trueTarget.equals(any));
        candidate = trueTarget.merge(any);
        Assert.assertTrue("Target and candidate are now equal", trueTarget.equals(candidate));

        Assert.assertFalse("Target and match are not yet equal", falseTarget.equals(any));
        candidate = falseTarget.merge(any);
        Assert.assertTrue("Target and candidate are now equal", falseTarget.equals(candidate));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotMerge1() {
        Chest one = (Chest) CraftBlockData.newData(null, "minecraft:chest[facing=east,waterlogged=true]");
        Chest two = (Chest) CraftBlockData.fromData(Blocks.CHEST.defaultBlockState());

        one.merge(two);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotMerge2() {
        Chest one = (Chest) CraftBlockData.newData(null, "minecraft:chest[waterlogged=true]");
        Chest two = (Chest) CraftBlockData.newData(null, "minecraft:chest[waterlogged=true]");

        one.merge(two);

        two.setFacing(BlockFace.NORTH);
        one.merge(two);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotMerge3() {
        Chest one = (Chest) CraftBlockData.newData(null, "minecraft:chest[waterlogged=true]");
        Chest two = (Chest) CraftBlockData.newData(null, "minecraft:trapped_chest[waterlogged=true]");

        one.merge(two);
    }

    @Test
    public void testMatch() {
        Assert.assertTrue(CraftBlockData.newData(null, "minecraft:chest[facing=east,waterlogged=true]").matches(CraftBlockData.newData(null, "minecraft:chest[waterlogged=true]")));
        Assert.assertFalse(CraftBlockData.newData(null, "minecraft:chest[facing=east,waterlogged=false]").matches(CraftBlockData.newData(null, "minecraft:chest[waterlogged=true]")));
        Assert.assertTrue(CraftBlockData.newData(null, "minecraft:chest[facing=east,waterlogged=true]").matches(CraftBlockData.newData(null, "minecraft:chest")));
        Assert.assertFalse(CraftBlockData.newData(null, "minecraft:trapped_chest[facing=east,waterlogged=false]").matches(CraftBlockData.newData(null, "minecraft:chest[waterlogged=true]")));
        Assert.assertTrue(CraftBlockData.newData(null, "minecraft:chest[facing=east,waterlogged=true]").matches(CraftBlockData.newData(null, "minecraft:chest[waterlogged=true,facing=east]")));

        Chest one = (Chest) CraftBlockData.fromData(Blocks.CHEST.defaultBlockState().setValue(BlockChest.FACING, EnumDirection.EAST));
        Chest two = (Chest) CraftBlockData.newData(null, "minecraft:chest[waterlogged=false]");

        Assert.assertTrue(one.matches(two));
        Assert.assertFalse(two.matches(one));
    }

    @Test
    public void testGetAsString() {
        String dataString = "minecraft:chest[facing=east,waterlogged=true]";
        BlockData data = CraftBlockData.newData(null, dataString);

        Assert.assertThat(data.getAsString(true), is(dataString));
        Assert.assertThat(data.getAsString(false), is("minecraft:chest[facing=east,type=single,waterlogged=true]"));
    }

    @Test
    public void testGetAsString2() {
        Chest data = (Chest) CraftBlockData.fromData(Blocks.CHEST.defaultBlockState().setValue(BlockChest.FACING, EnumDirection.EAST));

        Assert.assertThat(data.getAsString(true), is("minecraft:chest[facing=east,type=single,waterlogged=false]"));
        Assert.assertThat(data.getAsString(false), is("minecraft:chest[facing=east,type=single,waterlogged=false]"));
    }
}
