package net.minecraft.server;

import java.util.Random;

public class WorldGenWitchHut extends WorldGenScatteredPiece {

    private boolean e;

    public WorldGenWitchHut() {}

    public WorldGenWitchHut(Random random, int i, int j) {
        super(random, i, 64, j, 7, 5, 9);
    }

    protected void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        nbttagcompound.setBoolean("Witch", this.e);
    }

    protected void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        this.e = nbttagcompound.getBoolean("Witch");
    }

    public boolean a(World world, Random random, StructureBoundingBox structureboundingbox) {
        if (!this.a(world, structureboundingbox, 0)) {
            return false;
        } else {
            this.a(world, structureboundingbox, 1, 1, 1, 5, 1, 7, Blocks.WOOD, 1, Blocks.WOOD, 1, false);
            this.a(world, structureboundingbox, 1, 4, 2, 5, 4, 7, Blocks.WOOD, 1, Blocks.WOOD, 1, false);
            this.a(world, structureboundingbox, 2, 1, 0, 4, 1, 0, Blocks.WOOD, 1, Blocks.WOOD, 1, false);
            this.a(world, structureboundingbox, 2, 2, 2, 3, 3, 2, Blocks.WOOD, 1, Blocks.WOOD, 1, false);
            this.a(world, structureboundingbox, 1, 2, 3, 1, 3, 6, Blocks.WOOD, 1, Blocks.WOOD, 1, false);
            this.a(world, structureboundingbox, 5, 2, 3, 5, 3, 6, Blocks.WOOD, 1, Blocks.WOOD, 1, false);
            this.a(world, structureboundingbox, 2, 2, 7, 4, 3, 7, Blocks.WOOD, 1, Blocks.WOOD, 1, false);
            this.a(world, structureboundingbox, 1, 0, 2, 1, 3, 2, Blocks.LOG, Blocks.LOG, false);
            this.a(world, structureboundingbox, 5, 0, 2, 5, 3, 2, Blocks.LOG, Blocks.LOG, false);
            this.a(world, structureboundingbox, 1, 0, 7, 1, 3, 7, Blocks.LOG, Blocks.LOG, false);
            this.a(world, structureboundingbox, 5, 0, 7, 5, 3, 7, Blocks.LOG, Blocks.LOG, false);
            this.a(world, Blocks.FENCE, 0, 2, 3, 2, structureboundingbox);
            this.a(world, Blocks.FENCE, 0, 3, 3, 7, structureboundingbox);
            this.a(world, Blocks.AIR, 0, 1, 3, 4, structureboundingbox);
            this.a(world, Blocks.AIR, 0, 5, 3, 4, structureboundingbox);
            this.a(world, Blocks.AIR, 0, 5, 3, 5, structureboundingbox);
            this.a(world, Blocks.FLOWER_POT, 7, 1, 3, 5, structureboundingbox);
            this.a(world, Blocks.WORKBENCH, 0, 3, 2, 6, structureboundingbox);
            this.a(world, Blocks.CAULDRON, 0, 4, 2, 6, structureboundingbox);
            this.a(world, Blocks.FENCE, 0, 1, 2, 1, structureboundingbox);
            this.a(world, Blocks.FENCE, 0, 5, 2, 1, structureboundingbox);
            int i = this.a(Blocks.WOOD_STAIRS, 3);
            int j = this.a(Blocks.WOOD_STAIRS, 1);
            int k = this.a(Blocks.WOOD_STAIRS, 0);
            int l = this.a(Blocks.WOOD_STAIRS, 2);

            this.a(world, structureboundingbox, 0, 4, 1, 6, 4, 1, Blocks.SPRUCE_WOOD_STAIRS, i, Blocks.SPRUCE_WOOD_STAIRS, i, false);
            this.a(world, structureboundingbox, 0, 4, 2, 0, 4, 7, Blocks.SPRUCE_WOOD_STAIRS, k, Blocks.SPRUCE_WOOD_STAIRS, k, false);
            this.a(world, structureboundingbox, 6, 4, 2, 6, 4, 7, Blocks.SPRUCE_WOOD_STAIRS, j, Blocks.SPRUCE_WOOD_STAIRS, j, false);
            this.a(world, structureboundingbox, 0, 4, 8, 6, 4, 8, Blocks.SPRUCE_WOOD_STAIRS, l, Blocks.SPRUCE_WOOD_STAIRS, l, false);

            int i1;
            int j1;

            for (i1 = 2; i1 <= 7; i1 += 5) {
                for (j1 = 1; j1 <= 5; j1 += 4) {
                    this.b(world, Blocks.LOG, 0, j1, -1, i1, structureboundingbox);
                }
            }

            if (!this.e) {
                i1 = this.a(2, 5);
                j1 = this.a(2);
                int k1 = this.b(2, 5);

                if (structureboundingbox.b(i1, j1, k1)) {
                    this.e = true;
                    EntityWitch entitywitch = new EntityWitch(world);

                    entitywitch.setPositionRotation((double) i1 + 0.5D, (double) j1, (double) k1 + 0.5D, 0.0F, 0.0F);
                    entitywitch.prepare((GroupDataEntity) null);
                    world.addEntity(entitywitch, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.CHUNK_GEN); // CraftBukkit - add SpawnReason
                }
            }

            return true;
        }
    }
}
