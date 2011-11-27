package net.minecraft.server;

import java.util.Random;

public class BlockGravel extends BlockSand {

    public BlockGravel(int i, int j) {
        super(i, j);
    }

    public int a(int i, Random random, int j) {
        j = Math.max(j, 3); // CraftBukkit - added to fix crash when j > 3
        return random.nextInt(10 - j * 3) == 0 ? Item.FLINT.id : this.id;
    }
}
