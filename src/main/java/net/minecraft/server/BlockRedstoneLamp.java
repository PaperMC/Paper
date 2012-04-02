package net.minecraft.server;

import java.util.Random;

public class BlockRedstoneLamp extends Block {

    private final boolean a;

    public BlockRedstoneLamp(int i, boolean flag) {
        super(i, 211, Material.BUILDABLE_GLASS);
        this.a = flag;
        if (flag) {
            this.a(1.0F);
            ++this.textureId;
        }
    }

    public void onPlace(World world, int i, int j, int k) {
        if (!world.isStatic) {
            if (this.a && !world.isBlockIndirectlyPowered(i, j, k)) {
                world.c(i, j, k, this.id, 4);
            } else if (!this.a && world.isBlockIndirectlyPowered(i, j, k)) {
                world.setTypeId(i, j, k, Block.REDSTONE_LAMP_ON.id);
            }
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (!world.isStatic) {
            if (this.a && !world.isBlockIndirectlyPowered(i, j, k)) {
                world.c(i, j, k, this.id, 4);
            } else if (!this.a && world.isBlockIndirectlyPowered(i, j, k)) {
                world.setTypeId(i, j, k, Block.REDSTONE_LAMP_ON.id);
            }
        }
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (!world.isStatic && this.a && !world.isBlockIndirectlyPowered(i, j, k)) {
            world.setTypeId(i, j, k, Block.REDSTONE_LAMP_OFF.id);
        }
    }

    public int getDropType(int i, Random random, int j) {
        return Block.REDSTONE_LAMP_OFF.id;
    }
}
