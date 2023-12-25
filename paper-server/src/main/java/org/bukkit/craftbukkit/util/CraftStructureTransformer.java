package org.bukkit.craftbukkit.util;

import java.util.Objects;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureBoundingBox;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.generator.CraftLimitedRegion;
import org.bukkit.craftbukkit.generator.structure.CraftStructure;
import org.bukkit.event.world.AsyncStructureGenerateEvent;
import org.bukkit.event.world.AsyncStructureGenerateEvent.Cause;
import org.bukkit.util.BlockTransformer;
import org.bukkit.util.BlockTransformer.TransformationState;
import org.bukkit.util.EntityTransformer;

public class CraftStructureTransformer {

    private static class CraftTransformationState implements TransformationState {

        private final BlockState original;
        private final BlockState world;

        private BlockState originalCopy;
        private BlockState worldCopy;

        private CraftTransformationState(BlockState original, BlockState world) {
            this.original = original;
            this.world = world;
        }

        @Override
        public BlockState getOriginal() {
            if (originalCopy != null) {
                return originalCopy;
            }
            return originalCopy = original.copy();
        }

        @Override
        public BlockState getWorld() {
            if (worldCopy != null) {
                return worldCopy;
            }
            return worldCopy = world.copy();
        }

        private void destroyCopies() {
            originalCopy = null;
            worldCopy = null;
        }

    }

    private CraftLimitedRegion limitedRegion;
    private BlockTransformer[] blockTransformers;
    private EntityTransformer[] entityTransformers;

    public CraftStructureTransformer(Cause cause, GeneratorAccessSeed generatoraccessseed, StructureManager structuremanager, Structure structure, StructureBoundingBox structureboundingbox, ChunkCoordIntPair chunkcoordintpair) {
        AsyncStructureGenerateEvent event = new AsyncStructureGenerateEvent(structuremanager.level.getMinecraftWorld().getWorld(), !Bukkit.isPrimaryThread(), cause, CraftStructure.minecraftToBukkit(structure), new org.bukkit.util.BoundingBox(structureboundingbox.minX(), structureboundingbox.minY(), structureboundingbox.minZ(), structureboundingbox.maxX(), structureboundingbox.maxY(), structureboundingbox.maxZ()), chunkcoordintpair.x, chunkcoordintpair.z);
        Bukkit.getPluginManager().callEvent(event);
        this.blockTransformers = event.getBlockTransformers().values().toArray(BlockTransformer[]::new);
        this.entityTransformers = event.getEntityTransformers().values().toArray(EntityTransformer[]::new);
        this.limitedRegion = new CraftLimitedRegion(generatoraccessseed, chunkcoordintpair);
    }

    public boolean transformEntity(Entity entity) {
        EntityTransformer[] transformers = entityTransformers;
        if (transformers == null || transformers.length == 0) {
            return true;
        }
        CraftLimitedRegion region = limitedRegion;
        if (region == null) {
            return true;
        }
        entity.generation = true;
        CraftEntity craftEntity = entity.getBukkitEntity();
        int x = entity.getBlockX();
        int y = entity.getBlockY();
        int z = entity.getBlockZ();
        boolean allowedToSpawn = true;
        for (EntityTransformer transformer : transformers) {
            allowedToSpawn = transformer.transform(region, x, y, z, craftEntity, allowedToSpawn);
        }
        return allowedToSpawn;
    }

    public boolean canTransformBlocks() {
        return blockTransformers != null && blockTransformers.length != 0 && limitedRegion != null;
    }

    public CraftBlockState transformCraftState(CraftBlockState originalState) {
        BlockTransformer[] transformers = blockTransformers;
        if (transformers == null || transformers.length == 0) {
            return originalState;
        }
        CraftLimitedRegion region = limitedRegion;
        if (region == null) {
            return originalState;
        }
        originalState.setWorldHandle(region.getHandle());
        BlockPosition position = originalState.getPosition();
        BlockState blockState = originalState.copy();
        CraftTransformationState transformationState = new CraftTransformationState(originalState, region.getBlockState(position.getX(), position.getY(), position.getZ()));
        for (BlockTransformer transformer : transformers) {
            blockState = Objects.requireNonNull(transformer.transform(region, position.getX(), position.getY(), position.getZ(), blockState, transformationState), "BlockState can't be null");
            transformationState.destroyCopies();
        }
        return (CraftBlockState) blockState;
    }

    public void discard() {
        limitedRegion.saveEntities();
        limitedRegion.breakLink();
        this.limitedRegion = null;
        this.blockTransformers = null;
        this.entityTransformers = null;
    }
}
