package net.minecraft.server;

import java.util.Random;

public class WorldGenWitchHut extends WorldGenScatteredPiece {

    private boolean e;
    private boolean f;

    public WorldGenWitchHut(Random random, int i, int j) {
        super(WorldGenFeatureStructurePieceType.K, random, i, 64, j, 7, 7, 9);
    }

    public WorldGenWitchHut(DefinedStructureManager definedstructuremanager, NBTTagCompound nbttagcompound) {
        super(WorldGenFeatureStructurePieceType.K, nbttagcompound);
        this.e = nbttagcompound.getBoolean("Witch");
        this.f = nbttagcompound.getBoolean("Cat");
    }

    @Override
    protected void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        nbttagcompound.setBoolean("Witch", this.e);
        nbttagcompound.setBoolean("Cat", this.f);
    }

    @Override
    public boolean a(GeneratorAccessSeed generatoraccessseed, StructureManager structuremanager, ChunkGenerator chunkgenerator, Random random, StructureBoundingBox structureboundingbox, ChunkCoordIntPair chunkcoordintpair, BlockPosition blockposition) {
        if (!this.a(generatoraccessseed, structureboundingbox, 0)) {
            return false;
        } else {
            this.a(generatoraccessseed, structureboundingbox, 1, 1, 1, 5, 1, 7, Blocks.SPRUCE_PLANKS.getBlockData(), Blocks.SPRUCE_PLANKS.getBlockData(), false);
            this.a(generatoraccessseed, structureboundingbox, 1, 4, 2, 5, 4, 7, Blocks.SPRUCE_PLANKS.getBlockData(), Blocks.SPRUCE_PLANKS.getBlockData(), false);
            this.a(generatoraccessseed, structureboundingbox, 2, 1, 0, 4, 1, 0, Blocks.SPRUCE_PLANKS.getBlockData(), Blocks.SPRUCE_PLANKS.getBlockData(), false);
            this.a(generatoraccessseed, structureboundingbox, 2, 2, 2, 3, 3, 2, Blocks.SPRUCE_PLANKS.getBlockData(), Blocks.SPRUCE_PLANKS.getBlockData(), false);
            this.a(generatoraccessseed, structureboundingbox, 1, 2, 3, 1, 3, 6, Blocks.SPRUCE_PLANKS.getBlockData(), Blocks.SPRUCE_PLANKS.getBlockData(), false);
            this.a(generatoraccessseed, structureboundingbox, 5, 2, 3, 5, 3, 6, Blocks.SPRUCE_PLANKS.getBlockData(), Blocks.SPRUCE_PLANKS.getBlockData(), false);
            this.a(generatoraccessseed, structureboundingbox, 2, 2, 7, 4, 3, 7, Blocks.SPRUCE_PLANKS.getBlockData(), Blocks.SPRUCE_PLANKS.getBlockData(), false);
            this.a(generatoraccessseed, structureboundingbox, 1, 0, 2, 1, 3, 2, Blocks.OAK_LOG.getBlockData(), Blocks.OAK_LOG.getBlockData(), false);
            this.a(generatoraccessseed, structureboundingbox, 5, 0, 2, 5, 3, 2, Blocks.OAK_LOG.getBlockData(), Blocks.OAK_LOG.getBlockData(), false);
            this.a(generatoraccessseed, structureboundingbox, 1, 0, 7, 1, 3, 7, Blocks.OAK_LOG.getBlockData(), Blocks.OAK_LOG.getBlockData(), false);
            this.a(generatoraccessseed, structureboundingbox, 5, 0, 7, 5, 3, 7, Blocks.OAK_LOG.getBlockData(), Blocks.OAK_LOG.getBlockData(), false);
            this.a(generatoraccessseed, Blocks.OAK_FENCE.getBlockData(), 2, 3, 2, structureboundingbox);
            this.a(generatoraccessseed, Blocks.OAK_FENCE.getBlockData(), 3, 3, 7, structureboundingbox);
            this.a(generatoraccessseed, Blocks.AIR.getBlockData(), 1, 3, 4, structureboundingbox);
            this.a(generatoraccessseed, Blocks.AIR.getBlockData(), 5, 3, 4, structureboundingbox);
            this.a(generatoraccessseed, Blocks.AIR.getBlockData(), 5, 3, 5, structureboundingbox);
            this.a(generatoraccessseed, Blocks.POTTED_RED_MUSHROOM.getBlockData(), 1, 3, 5, structureboundingbox);
            this.a(generatoraccessseed, Blocks.CRAFTING_TABLE.getBlockData(), 3, 2, 6, structureboundingbox);
            this.a(generatoraccessseed, Blocks.CAULDRON.getBlockData(), 4, 2, 6, structureboundingbox);
            this.a(generatoraccessseed, Blocks.OAK_FENCE.getBlockData(), 1, 2, 1, structureboundingbox);
            this.a(generatoraccessseed, Blocks.OAK_FENCE.getBlockData(), 5, 2, 1, structureboundingbox);
            IBlockData iblockdata = (IBlockData) Blocks.SPRUCE_STAIRS.getBlockData().set(BlockStairs.FACING, EnumDirection.NORTH);
            IBlockData iblockdata1 = (IBlockData) Blocks.SPRUCE_STAIRS.getBlockData().set(BlockStairs.FACING, EnumDirection.EAST);
            IBlockData iblockdata2 = (IBlockData) Blocks.SPRUCE_STAIRS.getBlockData().set(BlockStairs.FACING, EnumDirection.WEST);
            IBlockData iblockdata3 = (IBlockData) Blocks.SPRUCE_STAIRS.getBlockData().set(BlockStairs.FACING, EnumDirection.SOUTH);

            this.a(generatoraccessseed, structureboundingbox, 0, 4, 1, 6, 4, 1, iblockdata, iblockdata, false);
            this.a(generatoraccessseed, structureboundingbox, 0, 4, 2, 0, 4, 7, iblockdata1, iblockdata1, false);
            this.a(generatoraccessseed, structureboundingbox, 6, 4, 2, 6, 4, 7, iblockdata2, iblockdata2, false);
            this.a(generatoraccessseed, structureboundingbox, 0, 4, 8, 6, 4, 8, iblockdata3, iblockdata3, false);
            this.a(generatoraccessseed, (IBlockData) iblockdata.set(BlockStairs.SHAPE, BlockPropertyStairsShape.OUTER_RIGHT), 0, 4, 1, structureboundingbox);
            this.a(generatoraccessseed, (IBlockData) iblockdata.set(BlockStairs.SHAPE, BlockPropertyStairsShape.OUTER_LEFT), 6, 4, 1, structureboundingbox);
            this.a(generatoraccessseed, (IBlockData) iblockdata3.set(BlockStairs.SHAPE, BlockPropertyStairsShape.OUTER_LEFT), 0, 4, 8, structureboundingbox);
            this.a(generatoraccessseed, (IBlockData) iblockdata3.set(BlockStairs.SHAPE, BlockPropertyStairsShape.OUTER_RIGHT), 6, 4, 8, structureboundingbox);

            int i;
            int j;

            for (j = 2; j <= 7; j += 5) {
                for (i = 1; i <= 5; i += 4) {
                    this.b(generatoraccessseed, Blocks.OAK_LOG.getBlockData(), i, -1, j, structureboundingbox);
                }
            }

            if (!this.e) {
                j = this.a(2, 5);
                i = this.d(2);
                int k = this.b(2, 5);

                if (structureboundingbox.b((BaseBlockPosition) (new BlockPosition(j, i, k)))) {
                    this.e = true;
                    EntityWitch entitywitch = (EntityWitch) EntityTypes.WITCH.a((World) generatoraccessseed.getMinecraftWorld());

                    entitywitch.setPersistent();
                    entitywitch.setPositionRotation((double) j + 0.5D, (double) i, (double) k + 0.5D, 0.0F, 0.0F);
                    entitywitch.prepare(generatoraccessseed, generatoraccessseed.getDamageScaler(new BlockPosition(j, i, k)), EnumMobSpawn.STRUCTURE, (GroupDataEntity) null, (NBTTagCompound) null);
                    generatoraccessseed.addAllEntities(entitywitch, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.CHUNK_GEN); // CraftBukkit - add SpawnReason
                }
            }

            this.a((WorldAccess) generatoraccessseed, structureboundingbox);
            return true;
        }
    }

    private void a(WorldAccess worldaccess, StructureBoundingBox structureboundingbox) {
        if (!this.f) {
            int i = this.a(2, 5);
            int j = this.d(2);
            int k = this.b(2, 5);

            if (structureboundingbox.b((BaseBlockPosition) (new BlockPosition(i, j, k)))) {
                this.f = true;
                EntityCat entitycat = (EntityCat) EntityTypes.CAT.a((World) worldaccess.getMinecraftWorld());

                entitycat.setPersistent();
                entitycat.setPositionRotation((double) i + 0.5D, (double) j, (double) k + 0.5D, 0.0F, 0.0F);
                entitycat.prepare(worldaccess, worldaccess.getDamageScaler(new BlockPosition(i, j, k)), EnumMobSpawn.STRUCTURE, (GroupDataEntity) null, (NBTTagCompound) null);
                worldaccess.addAllEntities(entitycat, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.CHUNK_GEN); // CraftBukkit - add SpawnReason
            }
        }

    }
}
