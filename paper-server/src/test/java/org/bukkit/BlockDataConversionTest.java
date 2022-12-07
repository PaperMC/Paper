package org.bukkit;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * This test class ensures that all Blocks (as registered in BuiltInRegistries.BLOCK)
 * can be converted into their CraftBlockData equivalent.
 */
@RunWith(Parameterized.class)
public class BlockDataConversionTest extends AbstractTestingBase {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static List<Object[]> args() {
        List<Object[]> list = new ArrayList<>();
        for (Block block : (Iterable<Block>) BuiltInRegistries.BLOCK) {
            list.add(new Object[]{block.defaultBlockState()});
        }
        return list;
    }

    @Parameterized.Parameter(0) public IBlockData data;

    @Test
    public void testNotNull() {
        Assert.assertNotNull(data);
        Assert.assertNotNull(CraftBlockData.fromData(data));
    }
}
