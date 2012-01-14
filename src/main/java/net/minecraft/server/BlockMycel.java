package net.minecraft.server;

import java.util.Random;

public class BlockMycel extends Block {

    protected BlockMycel(int i) {
        super(i, Material.GRASS);
        this.textureId = 77;
        this.a(true);
    }

    public int a(int i, int j) {
        return i == 1 ? 78 : (i == 0 ? 2 : 77);
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (!world.isStatic) {
            if (world.getLightLevel(i, j + 1, k) < 4 && Block.q[world.getTypeId(i, j + 1, k)] > 2) {
                world.setTypeId(i, j, k, Block.DIRT.id);
            } else if (world.getLightLevel(i, j + 1, k) >= 9) {
                for (int l = 0; l < 4; ++l) {
                    int i1 = i + random.nextInt(3) - 1;
                    int j1 = j + random.nextInt(5) - 3;
                    int k1 = k + random.nextInt(3) - 1;
                    int l1 = world.getTypeId(i1, j1 + 1, k1);

                    if (world.getTypeId(i1, j1, k1) == Block.DIRT.id && world.getLightLevel(i1, j1 + 1, k1) >= 4 && Block.q[l1] <= 2) {
                        world.setTypeId(i1, j1, k1, this.id);
                    }
                }
            }
        }
    }

    public int getDropType(int i, Random random, int j) {
        return Block.DIRT.getDropType(0, random, j);
    }
}
