package org.bukkit.craftbukkit.block;

import static org.junit.jupiter.api.Assertions.*;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ITileEntity;
import net.minecraft.world.level.block.entity.TileEntity;
import org.bukkit.Material;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

public class BlockStateTest extends AbstractTestingBase {

    @Test
    public void testTileEntityBlockStates() {
        for (Block block : BuiltInRegistries.BLOCK) {
            Material material = CraftBlockType.minecraftToBukkit(block);
            Class<?> blockStateType = CraftBlockStates.getBlockStateType(material);
            boolean isCraftBlockEntityState = CraftBlockEntityState.class.isAssignableFrom(blockStateType);

            if (block instanceof ITileEntity) {
                assertTrue(isCraftBlockEntityState, material + " has BlockState of type " + blockStateType.getName() + ", but expected subtype of CraftBlockEntityState");

                // check tile entity type
                TileEntity tileEntity = ((ITileEntity) block).newBlockEntity(BlockPosition.ZERO, block.defaultBlockState());
                TileEntity materialTileEntity = CraftBlockStates.createNewTileEntity(material);

                if (tileEntity == null) {
                    if (CraftBlockStates.isTileEntityOptional(material)) {
                        continue;
                    }
                    fail(material + " has no tile entity, it be added to CraftBlockStates#isTileEntityOptional");
                }

                assertNotNull(materialTileEntity, material + " has no tile entity expected tile entity of type " + tileEntity.getClass());
                assertSame(materialTileEntity.getClass(), tileEntity.getClass(), material + " has unexpected tile entity type, expected " + tileEntity.getClass() + " but got " + tileEntity.getClass());
            } else {
                assertFalse(isCraftBlockEntityState, material + " has unexpected CraftBlockEntityState subytype " + blockStateType.getName() + " (but is not a tile)");
            }
        }
    }
}
