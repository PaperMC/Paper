package net.minecraft.server;

import java.util.ArrayList; // CraftBukkit
import java.util.Random;

public class BlockVine extends Block {

    public BlockVine(int i) {
        super(i, 143, Material.REPLACEABLE_PLANT);
        this.a(true);
    }

    public void f() {
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public int c() {
        return 20;
    }

    public boolean a() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
        int l = iblockaccess.getData(i, j, k);
        float f = 1.0F;
        float f1 = 1.0F;
        float f2 = 1.0F;
        float f3 = 0.0F;
        float f4 = 0.0F;
        float f5 = 0.0F;
        boolean flag = l > 0;

        if ((l & 2) != 0) {
            f3 = Math.max(f3, 0.0625F);
            f = 0.0F;
            f1 = 0.0F;
            f4 = 1.0F;
            f2 = 0.0F;
            f5 = 1.0F;
            flag = true;
        }

        if ((l & 8) != 0) {
            f = Math.min(f, 0.9375F);
            f3 = 1.0F;
            f1 = 0.0F;
            f4 = 1.0F;
            f2 = 0.0F;
            f5 = 1.0F;
            flag = true;
        }

        if ((l & 4) != 0) {
            f5 = Math.max(f5, 0.0625F);
            f2 = 0.0F;
            f = 0.0F;
            f3 = 1.0F;
            f1 = 0.0F;
            f4 = 1.0F;
            flag = true;
        }

        if ((l & 1) != 0) {
            f2 = Math.min(f2, 0.9375F);
            f5 = 1.0F;
            f = 0.0F;
            f3 = 1.0F;
            f1 = 0.0F;
            f4 = 1.0F;
            flag = true;
        }

        if (!flag && this.d(iblockaccess.getTypeId(i, j + 1, k))) {
            f1 = Math.min(f1, 0.9375F);
            f4 = 1.0F;
            f = 0.0F;
            f3 = 1.0F;
            f2 = 0.0F;
            f5 = 1.0F;
        }

        this.a(f, f1, f2, f3, f4, f5);
    }

    public AxisAlignedBB e(World world, int i, int j, int k) {
        return null;
    }

    public boolean canPlace(World world, int i, int j, int k, int l) {
        switch (l) {
        case 1:
            return this.d(world.getTypeId(i, j + 1, k));

        case 2:
            return this.d(world.getTypeId(i, j, k + 1));

        case 3:
            return this.d(world.getTypeId(i, j, k - 1));

        case 4:
            return this.d(world.getTypeId(i + 1, j, k));

        case 5:
            return this.d(world.getTypeId(i - 1, j, k));

        default:
            return false;
        }
    }

    private boolean d(int i) {
        if (i == 0) {
            return false;
        } else {
            Block block = Block.byId[i];

            return block.b() && block.material.isSolid();
        }
    }

    private boolean g(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);
        int i1 = l;

        if (l > 0) {
            for (int j1 = 0; j1 <= 3; ++j1) {
                int k1 = 1 << j1;

                if ((l & k1) != 0 && !this.d(world.getTypeId(i + Direction.a[j1], j, k + Direction.b[j1])) && (world.getTypeId(i, j + 1, k) != this.id || (world.getData(i, j + 1, k) & k1) == 0)) {
                    i1 &= ~k1;
                }
            }
        }

        if (i1 == 0 && !this.d(world.getTypeId(i, j + 1, k))) {
            return false;
        } else {
            if (i1 != l) {
                world.setData(i, j, k, i1);
            }

            return true;
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (!world.isStatic && !this.g(world, i, j, k)) {
            this.b(world, i, j, k, world.getData(i, j, k), 0);
            world.setTypeId(i, j, k, 0);
        }
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (!world.isStatic && world.random.nextInt(4) == 0) {
            byte b0 = 4;
            int l = 5;
            boolean flag = false;

            int i1;
            int j1;
            int k1;

            label138:
            for (i1 = i - b0; i1 <= i + b0; ++i1) {
                for (j1 = k - b0; j1 <= k + b0; ++j1) {
                    for (k1 = j - 1; k1 <= j + 1; ++k1) {
                        if (world.getTypeId(i1, k1, j1) == this.id) {
                            --l;
                            if (l <= 0) {
                                flag = true;
                                break label138;
                            }
                        }
                    }
                }
            }

            i1 = world.getData(i, j, k);
            j1 = world.random.nextInt(6);
            k1 = Direction.d[j1];
            int l1;
            int i2;

            if (j1 == 1 && j < 255 && world.isEmpty(i, j + 1, k)) {
                if (flag) {
                    return;
                }

                l1 = world.random.nextInt(16) & i1;
                if (l1 > 0) {
                    for (i2 = 0; i2 <= 3; ++i2) {
                        if (!this.d(world.getTypeId(i + Direction.a[i2], j + 1, k + Direction.b[i2]))) {
                            l1 &= ~(1 << i2);
                        }
                    }

                    if (l1 > 0) {
                        world.setTypeIdAndData(i, j + 1, k, this.id, l1);
                    }
                }
            } else {
                int j2;

                if (j1 >= 2 && j1 <= 5 && (i1 & 1 << k1) == 0) {
                    if (flag) {
                        return;
                    }

                    l1 = world.getTypeId(i + Direction.a[k1], j, k + Direction.b[k1]);
                    if (l1 != 0 && Block.byId[l1] != null) {
                        if (Block.byId[l1].material.j() && Block.byId[l1].b()) {
                            world.setData(i, j, k, i1 | 1 << k1);
                        }
                    } else {
                        i2 = k1 + 1 & 3;
                        j2 = k1 + 3 & 3;
                        if ((i1 & 1 << i2) != 0 && this.d(world.getTypeId(i + Direction.a[k1] + Direction.a[i2], j, k + Direction.b[k1] + Direction.b[i2]))) {
                            world.setTypeIdAndData(i + Direction.a[k1], j, k + Direction.b[k1], this.id, 1 << i2);
                        } else if ((i1 & 1 << j2) != 0 && this.d(world.getTypeId(i + Direction.a[k1] + Direction.a[j2], j, k + Direction.b[k1] + Direction.b[j2]))) {
                            world.setTypeIdAndData(i + Direction.a[k1], j, k + Direction.b[k1], this.id, 1 << j2);
                        } else if ((i1 & 1 << i2) != 0 && world.isEmpty(i + Direction.a[k1] + Direction.a[i2], j, k + Direction.b[k1] + Direction.b[i2]) && this.d(world.getTypeId(i + Direction.a[i2], j, k + Direction.b[i2]))) {
                            world.setTypeIdAndData(i + Direction.a[k1] + Direction.a[i2], j, k + Direction.b[k1] + Direction.b[i2], this.id, 1 << (k1 + 2 & 3));
                        } else if ((i1 & 1 << j2) != 0 && world.isEmpty(i + Direction.a[k1] + Direction.a[j2], j, k + Direction.b[k1] + Direction.b[j2]) && this.d(world.getTypeId(i + Direction.a[j2], j, k + Direction.b[j2]))) {
                            world.setTypeIdAndData(i + Direction.a[k1] + Direction.a[j2], j, k + Direction.b[k1] + Direction.b[j2], this.id, 1 << (k1 + 2 & 3));
                        } else if (this.d(world.getTypeId(i + Direction.a[k1], j + 1, k + Direction.b[k1]))) {
                            world.setTypeIdAndData(i + Direction.a[k1], j, k + Direction.b[k1], this.id, 0);
                        }
                    }
                } else if (j > 1) {
                    l1 = world.getTypeId(i, j - 1, k);
                    if (l1 == 0) {
                        i2 = world.random.nextInt(16) & i1;
                        if (i2 > 0) {
                            world.setTypeIdAndData(i, j - 1, k, this.id, i2);
                        }
                    } else if (l1 == this.id) {
                        i2 = world.random.nextInt(16) & i1;
                        j2 = world.getData(i, j - 1, k);
                        if (j2 != (j2 | i2)) {
                            world.setData(i, j - 1, k, j2 | i2);
                        }
                    }
                }
            }
        }
    }

    public void postPlace(World world, int i, int j, int k, int l) {
        byte b0 = 0;

        switch (l) {
        case 2:
            b0 = 1;
            break;

        case 3:
            b0 = 4;
            break;

        case 4:
            b0 = 8;
            break;

        case 5:
            b0 = 2;
        }

        if (b0 != 0) {
            world.setData(i, j, k, b0);
        }
    }

    public int getDropType(int i, Random random, int j) {
        return 0;
    }

    public int a(Random random) {
        return 0;
    }

    public void a(World world, EntityHuman entityhuman, int i, int j, int k, int l) {
        if (!world.isStatic && entityhuman.U() != null && entityhuman.U().id == Item.SHEARS.id) {
            entityhuman.a(StatisticList.C[this.id], 1);
            /* CraftBukkit start - moved this line into calculateDrops
            this.a(world, i, j, k, new ItemStack(Block.VINE, 1, 0));
            */
            this.doActualDrop(world, i, j, k);
            // CraftBukkit end
        } else {
            super.a(world, entityhuman, i, j, k, l);
        }
    }

    // CraftBukkit start - Calculate drops
    public ArrayList<ItemStack> calculateDrops(World world, EntityHuman entityhuman, int i, int j, int k, int l) {
        if (!world.isStatic && entityhuman.U() != null && entityhuman.U().id == Item.SHEARS.id) {
            this.a(world, i, j, k, new ItemStack(Block.VINE, 1, 0));
            return this.dropList;
        } else {
            return super.calculateDrops(world, entityhuman, i, j, k, l);
        }
    }
    // CraftBukkit end
}
