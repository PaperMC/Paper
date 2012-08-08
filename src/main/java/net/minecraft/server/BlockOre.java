package net.minecraft.server;

import java.util.Random;

public class BlockOre extends Block {

    public BlockOre(int i, int j) {
        super(i, j, Material.STONE);
        this.a(CreativeModeTab.b);
    }

    public int getDropType(int i, Random random, int j) {
        return this.id == Block.COAL_ORE.id ? Item.COAL.id : (this.id == Block.DIAMOND_ORE.id ? Item.DIAMOND.id : (this.id == Block.LAPIS_ORE.id ? Item.INK_SACK.id : (this.id == Block.EMERALD_ORE.id ? Item.EMERALD.id : this.id)));
    }

    public int a(Random random) {
        return this.id == Block.LAPIS_ORE.id ? 4 + random.nextInt(5) : 1;
    }

    public int getDropCount(int i, Random random) {
        if (i > 0 && this.id != this.getDropType(0, random, i)) {
            int j = random.nextInt(i + 2) - 1;

            if (j < 0) {
                j = 0;
            }

            return this.a(random) * (j + 1);
        } else {
            return this.a(random);
        }
    }

    public void dropNaturally(World world, int i, int j, int k, int l, float f, int i1) {
        super.dropNaturally(world, i, j, k, l, f, i1);
        if (this.getDropType(l, world.random, i1) != this.id) {
            int j1 = 0;

            if (this.id == Block.COAL_ORE.id) {
                j1 = MathHelper.a(world.random, 0, 2);
            } else if (this.id == Block.DIAMOND_ORE.id) {
                j1 = MathHelper.a(world.random, 3, 7);
            } else if (this.id == Block.EMERALD_ORE.id) {
                j1 = MathHelper.a(world.random, 3, 7);
            } else if (this.id == Block.LAPIS_ORE.id) {
                j1 = MathHelper.a(world.random, 2, 5);
            }

            this.g(world, i, j, k, j1);
        }
    }

    protected int getDropData(int i) {
        return this.id == Block.LAPIS_ORE.id ? 4 : 0;
    }
}
