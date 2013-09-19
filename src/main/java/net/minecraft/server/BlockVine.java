package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public class BlockVine extends Block {

    public BlockVine(int i) {
        super(i, Material.REPLACEABLE_PLANT);
        this.b(true);
        this.a(CreativeModeTab.c);
    }

    public void g() {
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public int d() {
        return 20;
    }

    public boolean c() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
        float f = 0.0625F;
        int l = iblockaccess.getData(i, j, k);
        float f1 = 1.0F;
        float f2 = 1.0F;
        float f3 = 1.0F;
        float f4 = 0.0F;
        float f5 = 0.0F;
        float f6 = 0.0F;
        boolean flag = l > 0;

        if ((l & 2) != 0) {
            f4 = Math.max(f4, 0.0625F);
            f1 = 0.0F;
            f2 = 0.0F;
            f5 = 1.0F;
            f3 = 0.0F;
            f6 = 1.0F;
            flag = true;
        }

        if ((l & 8) != 0) {
            f1 = Math.min(f1, 0.9375F);
            f4 = 1.0F;
            f2 = 0.0F;
            f5 = 1.0F;
            f3 = 0.0F;
            f6 = 1.0F;
            flag = true;
        }

        if ((l & 4) != 0) {
            f6 = Math.max(f6, 0.0625F);
            f3 = 0.0F;
            f1 = 0.0F;
            f4 = 1.0F;
            f2 = 0.0F;
            f5 = 1.0F;
            flag = true;
        }

        if ((l & 1) != 0) {
            f3 = Math.min(f3, 0.9375F);
            f6 = 1.0F;
            f1 = 0.0F;
            f4 = 1.0F;
            f2 = 0.0F;
            f5 = 1.0F;
            flag = true;
        }

        if (!flag && this.d(iblockaccess.getTypeId(i, j + 1, k))) {
            f2 = Math.min(f2, 0.9375F);
            f5 = 1.0F;
            f1 = 0.0F;
            f4 = 1.0F;
            f3 = 0.0F;
            f6 = 1.0F;
        }

        this.a(f1, f2, f3, f4, f5, f6);
    }

    public AxisAlignedBB b(World world, int i, int j, int k) {
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

    private boolean k(World world, int i, int j, int k) {
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
                world.setData(i, j, k, i1, 2);
            }

            return true;
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (!world.isStatic && !this.k(world, i, j, k)) {
            this.c(world, i, j, k, world.getData(i, j, k), 0);
            world.setAir(i, j, k);
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
            k1 = Direction.e[j1];
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
                        // CraftBukkit start - Call BlockSpreadEvent
                        org.bukkit.block.Block source = world.getWorld().getBlockAt(i, j, k);
                        org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j + 1, k);
                        CraftEventFactory.handleBlockSpreadEvent(block, source, this.id, l1);
                        // CraftBukkit end
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
                        if (Block.byId[l1].material.k() && Block.byId[l1].b()) {
                            world.setData(i, j, k, i1 | 1 << k1, 2);
                        }
                    } else {
                        i2 = k1 + 1 & 3;
                        j2 = k1 + 3 & 3;

                        // CraftBukkit start - Call BlockSpreadEvent
                        org.bukkit.block.Block source = world.getWorld().getBlockAt(i, j, k);
                        org.bukkit.block.Block block = world.getWorld().getBlockAt(i + Direction.a[k1], j, k + Direction.b[k1]);
                        if ((i1 & 1 << i2) != 0 && this.d(world.getTypeId(i + Direction.a[k1] + Direction.a[i2], j, k + Direction.b[k1] + Direction.b[i2]))) {
                            CraftEventFactory.handleBlockSpreadEvent(block, source, this.id, 1 << i2);
                        } else if ((i1 & 1 << j2) != 0 && this.d(world.getTypeId(i + Direction.a[k1] + Direction.a[j2], j, k + Direction.b[k1] + Direction.b[j2]))) {
                            CraftEventFactory.handleBlockSpreadEvent(block, source, this.id, 1 << j2);
                        } else if ((i1 & 1 << i2) != 0 && world.isEmpty(i + Direction.a[k1] + Direction.a[i2], j, k + Direction.b[k1] + Direction.b[i2]) && this.d(world.getTypeId(i + Direction.a[i2], j, k + Direction.b[i2]))) {
                            block = world.getWorld().getBlockAt(i + Direction.a[k1] + Direction.a[i2], j, k + Direction.b[k1] + Direction.b[i2]);
                            CraftEventFactory.handleBlockSpreadEvent(block, source, this.id, 1 << (k1 + 2 & 3));
                        } else if ((i1 & 1 << j2) != 0 && world.isEmpty(i + Direction.a[k1] + Direction.a[j2], j, k + Direction.b[k1] + Direction.b[j2]) && this.d(world.getTypeId(i + Direction.a[j2], j, k + Direction.b[j2]))) {
                            block = world.getWorld().getBlockAt(i + Direction.a[k1] + Direction.a[j2], j, k + Direction.b[k1] + Direction.b[j2]);
                            CraftEventFactory.handleBlockSpreadEvent(block, source, this.id, 1 << (k1 + 2 & 3));
                        } else if (this.d(world.getTypeId(i + Direction.a[k1], j + 1, k + Direction.b[k1]))) {
                            CraftEventFactory.handleBlockSpreadEvent(block, source, this.id, 0);
                        }
                        // CraftBukkit end
                    }
                } else if (j > 1) {
                    l1 = world.getTypeId(i, j - 1, k);
                    if (l1 == 0) {
                        i2 = world.random.nextInt(16) & i1;
                        if (i2 > 0) {
                            // CraftBukkit start - Call BlockSpreadEvent
                            org.bukkit.block.Block source = world.getWorld().getBlockAt(i, j, k);
                            org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j - 1, k);
                            CraftEventFactory.handleBlockSpreadEvent(block, source, this.id, i2);
                            // CraftBukkit end
                        }
                    } else if (l1 == this.id) {
                        i2 = world.random.nextInt(16) & i1;
                        j2 = world.getData(i, j - 1, k);
                        if (j2 != (j2 | i2)) {
                            world.setData(i, j - 1, k, j2 | i2, 2);
                        }
                    }
                }
            }
        }
    }

    public int getPlacedData(World world, int i, int j, int k, int l, float f, float f1, float f2, int i1) {
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

        return b0 != 0 ? b0 : i1;
    }

    public int getDropType(int i, Random random, int j) {
        return 0;
    }

    public int a(Random random) {
        return 0;
    }

    public void a(World world, EntityHuman entityhuman, int i, int j, int k, int l) {
        if (!world.isStatic && entityhuman.by() != null && entityhuman.by().id == Item.SHEARS.id) {
            entityhuman.a(StatisticList.C[this.id], 1);
            this.b(world, i, j, k, new ItemStack(Block.VINE, 1, 0));
        } else {
            super.a(world, entityhuman, i, j, k, l);
        }
    }
}
