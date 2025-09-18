package org.bukkit.craftbukkit.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.bukkit.Material;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@AllFeatures
public class BlockStateTest {

    @Test
    public void testBlockEntityBlockStates() {
        for (Block block : BuiltInRegistries.BLOCK) {
            Material material = CraftBlockType.minecraftToBukkit(block);
            Class<?> blockStateType = CraftBlockStates.getBlockStateType(material);
            boolean isCraftBlockEntityState = CraftBlockEntityState.class.isAssignableFrom(blockStateType);

            if (block instanceof EntityBlock) {
                assertTrue(isCraftBlockEntityState, material + " has BlockState of type " + blockStateType.getName() + ", but expected subtype of CraftBlockEntityState");

                // check tile entity type
                BlockEntity blockEntity = ((EntityBlock) block).newBlockEntity(BlockPos.ZERO, block.defaultBlockState());
                BlockEntity materialTileEntity = CraftBlockStates.createNewBlockEntity(material);

                if (blockEntity == null) {
                    if (CraftBlockStates.isBlockEntityOptional(material)) {
                        continue;
                    }
                    fail(material + " has no tile entity, it be added to CraftBlockStates#isTileEntityOptional");
                }

                assertNotNull(materialTileEntity, material + " has no tile entity expected tile entity of type " + blockEntity.getClass());
                assertSame(materialTileEntity.getClass(), blockEntity.getClass(), material + " has unexpected tile entity type, expected " + blockEntity.getClass() + " but got " + blockEntity.getClass());
            } else {
                assertFalse(isCraftBlockEntityState, material + " has unexpected CraftBlockEntityState subytype " + blockStateType.getName() + " (but is not a tile)");
            }
        }
    }

    @Test
    public void testBlockEntityTypes() {
        for (BlockEntityType<?> blockEntityType : BuiltInRegistries.BLOCK_ENTITY_TYPE) {
            assertNotNull(CraftBlockStates.getBlockStateType(blockEntityType));
        }
    }
}
