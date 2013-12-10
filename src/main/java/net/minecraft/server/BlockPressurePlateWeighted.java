package net.minecraft.server;

import java.util.List;

public class BlockPressurePlateWeighted extends BlockPressurePlateAbstract {
    private final int a;

    protected BlockPressurePlateWeighted(String s, Material material, int i) {
        super(s, material);
        this.a = i;
    }

    protected int e(World world, int i, int j, int k) {
        int l = Math.min(world.a(Entity.class, this.a(i, j, k)).size(), this.a);

        if (l <= 0) {
            return 0;
        }

        float f = (float) Math.min(this.a, l) / (float) this.a;
        return MathHelper.f(f * 15.0F);
    }

    protected int c(int i) {
        return i;
    }

    protected int d(int i) {
        return i;
    }

    public int a(World world) {
        return 10;
    }
}
