package net.minecraft.server;

import java.util.Random;

public class BlockTNT extends Block {

    public BlockTNT(int i, int j) {
        super(i, j, Material.TNT);
        this.a(CreativeModeTab.d);
    }

    public int a(int i) {
        return i == 0 ? this.textureId + 2 : (i == 1 ? this.textureId + 1 : this.textureId);
    }

    public void onPlace(World world, int i, int j, int k) {
        super.onPlace(world, i, j, k);
        if (!world.suppressPhysics && world.isBlockIndirectlyPowered(i, j, k)) { // CraftBukkit
            this.postBreak(world, i, j, k, 1);
            world.setTypeId(i, j, k, 0);
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (l > 0 && Block.byId[l].isPowerSource() && world.isBlockIndirectlyPowered(i, j, k)) {
            this.postBreak(world, i, j, k, 1);
            world.setTypeId(i, j, k, 0);
        }
    }

    public int a(Random random) {
        return 1;
    }

    public void wasExploded(World world, int i, int j, int k) {
        if (!world.isStatic) {
            EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, (double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F));

            entitytntprimed.fuseTicks = world.random.nextInt(entitytntprimed.fuseTicks / 4) + entitytntprimed.fuseTicks / 8;
            world.addEntity(entitytntprimed);
        }
    }

    public void postBreak(World world, int i, int j, int k, int l) {
        if (!world.isStatic) {
            if ((l & 1) == 1) {
                EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, (double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F));

                world.addEntity(entitytntprimed);
                world.makeSound(entitytntprimed, "random.fuse", 1.0F, 1.0F);
            }
        }
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman, int l, float f, float f1, float f2) {
        if (entityhuman.bP() != null && entityhuman.bP().id == Item.FLINT_AND_STEEL.id) {
            this.postBreak(world, i, j, k, 1);
            world.setTypeId(i, j, k, 0);
            return true;
        } else {
            return super.interact(world, i, j, k, entityhuman, l, f, f1, f2);
        }
    }

    public void a(World world, int i, int j, int k, Entity entity) {
        if (entity instanceof EntityArrow && !world.isStatic) {
            EntityArrow entityarrow = (EntityArrow) entity;

            if (entityarrow.isBurning()) {
                this.postBreak(world, i, j, k, 1);
                world.setTypeId(i, j, k, 0);
            }
        }
    }
}
