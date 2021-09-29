package org.bukkit.craftbukkit.block;

import static org.junit.Assert.assertTrue;
import net.minecraft.core.IRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ITileEntity;
import org.bukkit.Material;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

public class BlockStateTest extends AbstractTestingBase {

    @Test
    public void testTileEntityBlockStates() {
        for (Block block : IRegistry.BLOCK) {
            Material material = CraftMagicNumbers.getMaterial(block);
            Class<?> blockStateType = CraftBlockStates.getBlockStateType(material);
            boolean isCraftBlockEntityState = CraftBlockEntityState.class.isAssignableFrom(blockStateType);

            if (block instanceof ITileEntity) {
                assertTrue(material + " has BlockState of type " + blockStateType.getName() + ", but expected subtype of CraftBlockEntityState", isCraftBlockEntityState);
            } else {
                assertTrue(material + " has unexpected CraftBlockEntityState subytype " + blockStateType.getName() + " (but is not a tile)", !isCraftBlockEntityState);
            }
        }
    }
}
