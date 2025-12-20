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

                // check block entity type
                BlockEntity blockEntity = ((EntityBlock) block).newBlockEntity(BlockPos.ZERO, block.defaultBlockState());
                BlockEntity materialBlockEntity = CraftBlockStates.createNewBlockEntity(material);

                if (blockEntity == null) {
                    if (CraftBlockStates.isBlockEntityOptional(material)) {
                        continue;
                    }
                    fail(material + " has no block entity, it be added to CraftBlockStates#isBlockEntityOptional");
                }

                assertNotNull(materialBlockEntity, material + " has no block entity expected block entity of type " + blockEntity.getClass());
                assertSame(materialBlockEntity.getClass(), blockEntity.getClass(), material + " has unexpected block entity type, expected " + blockEntity.getClass() + " but got " + blockEntity.getClass());
            } else {
                assertFalse(isCraftBlockEntityState, material + " has unexpected CraftBlockEntityState subtype " + blockStateType.getName() + " (but is not a block entity)");
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
