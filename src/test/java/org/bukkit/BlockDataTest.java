package org.bukkit;

import net.minecraft.server.BlockCake;
import net.minecraft.server.Blocks;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Cake;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.support.AbstractTestingBase;
import static org.hamcrest.Matchers.*;
import org.junit.Assert;
import org.junit.Test;

public class BlockDataTest extends AbstractTestingBase {

    @Test
    public void testParsing() {
        BlockData cakeTest = CraftBlockData.fromData(Blocks.CAKE.getBlockData().set(BlockCake.BITES, 3));

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

    @Test
    public void testClone() {
        Cake cakeTest = (Cake) CraftBlockData.fromData(Blocks.CAKE.getBlockData().set(BlockCake.BITES, 3));
        Cake clone = (Cake) cakeTest.clone();

        Assert.assertFalse("Clone did not return new object", cakeTest == clone);
        Assert.assertThat("Clone is not equal", clone, is(cakeTest));

        clone.setBites(1);
        Assert.assertThat("Clone is not actually clone", clone, is(not(cakeTest)));
    }
}
