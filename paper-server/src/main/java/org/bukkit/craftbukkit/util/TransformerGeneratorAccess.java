package org.bukkit.craftbukkit.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.material.FluidState;
import org.bukkit.craftbukkit.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class TransformerGeneratorAccess extends DelegatedGeneratorAccess {

    private CraftStructureTransformer structureTransformer;

    public void setStructureTransformer(CraftStructureTransformer structureTransformer) {
        this.structureTransformer = structureTransformer;
    }

    public CraftStructureTransformer getStructureTransformer() {
        return this.structureTransformer;
    }

    @Override
    public boolean addFreshEntity(Entity entity) {
        if (this.structureTransformer != null && !this.structureTransformer.transformEntity(entity)) {
            return false;
        }
        return super.addFreshEntity(entity);
    }

    @Override
    public boolean addFreshEntity(Entity arg0, SpawnReason arg1) {
        if (this.structureTransformer != null && !this.structureTransformer.transformEntity(arg0)) {
            return false;
        }
        return super.addFreshEntity(arg0, arg1);
    }

    @Override
    public void addFreshEntityWithPassengers(Entity entity) {
        if (this.structureTransformer != null && !this.structureTransformer.transformEntity(entity)) {
            return;
        }
        super.addFreshEntityWithPassengers(entity);
    }

    @Override
    public void addFreshEntityWithPassengers(Entity arg0, SpawnReason arg1) {
        if (this.structureTransformer != null && !this.structureTransformer.transformEntity(arg0)) {
            return;
        }
        super.addFreshEntityWithPassengers(arg0, arg1);
    }

    public boolean setCraftBlock(BlockPos position, CraftBlockState craftBlockState, int i, int j) {
        if (this.structureTransformer != null) {
            craftBlockState = this.structureTransformer.transformCraftState(craftBlockState);
        }
        // This code is based on the method 'net.minecraft.world.level.levelgen.structure.StructurePiece#placeBlock'
        // It ensures that any kind of block is updated correctly upon placing it
        BlockState iblockdata = craftBlockState.getHandle();
        boolean result = super.setBlock(position, iblockdata, i, j);
        FluidState fluid = this.getFluidState(position);
        if (!fluid.isEmpty()) {
            this.scheduleTick(position, fluid.getType(), 0);
        }
        if (StructurePiece.SHAPE_CHECK_BLOCKS.contains(iblockdata.getBlock())) {
            this.getChunk(position).markPosForPostprocessing(position);
        }
        BlockEntity tileEntity = this.getBlockEntity(position);
        if (tileEntity != null && craftBlockState instanceof CraftBlockEntityState<?> craftEntityState) {
            tileEntity.loadWithComponents(craftEntityState.getSnapshotNBT(), this.registryAccess());
        }
        return result;
    }

    public boolean setCraftBlock(BlockPos position, CraftBlockState craftBlockState, int i) {
        return this.setCraftBlock(position, craftBlockState, i, 512);
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState state, int flags, int maxUpdateDepth) {
        if (this.structureTransformer == null || !this.structureTransformer.canTransformBlocks()) {
            return super.setBlock(pos, state, flags, maxUpdateDepth);
        }
        return this.setCraftBlock(pos, (CraftBlockState) CraftBlockStates.getBlockState(this, pos, state, null), flags, maxUpdateDepth);
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState state, int flags) {
        return this.setBlock(pos, state, flags, 512);
    }
}
