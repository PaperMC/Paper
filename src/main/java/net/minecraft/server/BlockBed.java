package net.minecraft.server;

import java.util.Iterator;
import java.util.Random;

public class BlockBed extends BlockDirectional {

    public static final int[][] a = new int[][] { { 0, 1}, { -1, 0}, { 0, -1}, { 1, 0}};

    public BlockBed(int i) {
        super(i, 134, Material.CLOTH);
        this.s();
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman) {
        if (world.isStatic) {
            return true;
        } else {
            int l = world.getData(i, j, k);

            if (!d(l)) {
                int i1 = b(l);

                i += a[i1][0];
                k += a[i1][1];
                if (world.getTypeId(i, j, k) != this.id) {
                    return true;
                }

                l = world.getData(i, j, k);
            }

            if (!world.worldProvider.c()) {
                double d0 = (double) i + 0.5D;
                double d1 = (double) j + 0.5D;
                double d2 = (double) k + 0.5D;

                world.setTypeId(i, j, k, 0);
                int j1 = b(l);

                i += a[j1][0];
                k += a[j1][1];
                if (world.getTypeId(i, j, k) == this.id) {
                    world.setTypeId(i, j, k, 0);
                    d0 = (d0 + (double) i + 0.5D) / 2.0D;
                    d1 = (d1 + (double) j + 0.5D) / 2.0D;
                    d2 = (d2 + (double) k + 0.5D) / 2.0D;
                }

                world.createExplosion((Entity) null, (double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), 5.0F, true);
                return true;
            } else {
                if (e(l)) {
                    EntityHuman entityhuman1 = null;
                    Iterator iterator = world.players.iterator();

                    while (iterator.hasNext()) {
                        EntityHuman entityhuman2 = (EntityHuman) iterator.next();

                        if (entityhuman2.isSleeping()) {
                            ChunkCoordinates chunkcoordinates = entityhuman2.F;

                            if (chunkcoordinates.x == i && chunkcoordinates.y == j && chunkcoordinates.z == k) {
                                entityhuman1 = entityhuman2;
                            }
                        }
                    }

                    if (entityhuman1 != null) {
                        entityhuman.a("tile.bed.occupied");
                        return true;
                    }

                    a(world, i, j, k, false);
                }

                EnumBedResult enumbedresult = entityhuman.a(i, j, k);

                if (enumbedresult == EnumBedResult.OK) {
                    a(world, i, j, k, true);
                    return true;
                } else {
                    if (enumbedresult == EnumBedResult.NOT_POSSIBLE_NOW) {
                        entityhuman.a("tile.bed.noSleep");
                    } else if (enumbedresult == EnumBedResult.NOT_SAFE) {
                        entityhuman.a("tile.bed.notSafe");
                    }

                    return true;
                }
            }
        }
    }

    public int a(int i, int j) {
        if (i == 0) {
            return Block.WOOD.textureId;
        } else {
            int k = b(j);
            int l = Direction.h[k][i];

            return d(j) ? (l == 2 ? this.textureId + 2 + 16 : (l != 5 && l != 4 ? this.textureId + 1 : this.textureId + 1 + 16)) : (l == 3 ? this.textureId - 1 + 16 : (l != 5 && l != 4 ? this.textureId : this.textureId + 16));
        }
    }

    public int c() {
        return 14;
    }

    public boolean b() {
        return false;
    }

    public boolean a() {
        return false;
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
        this.s();
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        int i1 = world.getData(i, j, k);
        int j1 = b(i1);

        if (d(i1)) {
            if (world.getTypeId(i - a[j1][0], j, k - a[j1][1]) != this.id) {
                world.setTypeId(i, j, k, 0);
            }
        } else if (world.getTypeId(i + a[j1][0], j, k + a[j1][1]) != this.id) {
            world.setTypeId(i, j, k, 0);
            /* CraftBukkit - Move this to on break instead of on physics
            if (!world.isStatic) {
                this.b(world, i, j, k, i1, 0);
            }
            // */
        }
    }

    public int getDropType(int i, Random random, int j) {
        return /* d(i) ? 0 CraftBukkit : */ Item.BED.id;
    }

    private void s() {
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.5625F, 1.0F);
    }

    public static boolean d(int i) {
        return (i & 8) != 0;
    }

    public static boolean e(int i) {
        return (i & 4) != 0;
    }

    public static void a(World world, int i, int j, int k, boolean flag) {
        int l = world.getData(i, j, k);

        if (flag) {
            l |= 4;
        } else {
            l &= -5;
        }

        world.setData(i, j, k, l);
    }

    public static ChunkCoordinates f(World world, int i, int j, int k, int l) {
        int i1 = world.getData(i, j, k);
        int j1 = BlockDirectional.b(i1);

        for (int k1 = 0; k1 <= 1; ++k1) {
            int l1 = i - a[j1][0] * k1 - 1;
            int i2 = k - a[j1][1] * k1 - 1;
            int j2 = l1 + 2;
            int k2 = i2 + 2;

            for (int l2 = l1; l2 <= j2; ++l2) {
                for (int i3 = i2; i3 <= k2; ++i3) {
                    if (world.e(l2, j - 1, i3) && world.isEmpty(l2, j, i3) && world.isEmpty(l2, j + 1, i3)) {
                        if (l <= 0) {
                            return new ChunkCoordinates(l2, j, i3);
                        }

                        --l;
                    }
                }
            }
        }

        return null;
    }

    public void dropNaturally(World world, int i, int j, int k, int l, float f, int i1) {
        //if (!d(l)) { // CraftBukkit
            super.dropNaturally(world, i, j, k, l, f, 0);
        // } // CraftBukkit
    }

    public int g() {
        return 1;
    }
}
