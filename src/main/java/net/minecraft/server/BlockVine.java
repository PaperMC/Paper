package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public class BlockVine extends Block {

    public BlockVine() {
        super(Material.REPLACEABLE_PLANT);
        this.a(true);
        this.a(CreativeModeTab.c);
    }

    public void g() {
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public int b() {
        return 20;
    }

    public boolean c() {
        return false;
    }

    public boolean d() {
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

        if (!flag && this.a(iblockaccess.getType(i, j + 1, k))) {
            f2 = Math.min(f2, 0.9375F);
            f5 = 1.0F;
            f1 = 0.0F;
            f4 = 1.0F;
            f3 = 0.0F;
            f6 = 1.0F;
        }

        this.a(f1, f2, f3, f4, f5, f6);
    }

    public AxisAlignedBB a(World world, int i, int j, int k) {
        return null;
    }

    public boolean canPlace(World world, int i, int j, int k, int l) {
        switch (l) {
        case 1:
            return this.a(world.getType(i, j + 1, k));

        case 2:
            return this.a(world.getType(i, j, k + 1));

        case 3:
            return this.a(world.getType(i, j, k - 1));

        case 4:
            return this.a(world.getType(i + 1, j, k));

        case 5:
            return this.a(world.getType(i - 1, j, k));

        default:
            return false;
        }
    }

    private boolean a(Block block) {
        return block.d() && block.material.isSolid();
    }

    private boolean e(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);
        int i1 = l;

        if (l > 0) {
            for (int j1 = 0; j1 <= 3; ++j1) {
                int k1 = 1 << j1;

                if ((l & k1) != 0 && !this.a(world.getType(i + Direction.a[j1], j, k + Direction.b[j1])) && (world.getType(i, j + 1, k) != this || (world.getData(i, j + 1, k) & k1) == 0)) {
                    i1 &= ~k1;
                }
            }
        }

        if (i1 == 0 && !this.a(world.getType(i, j + 1, k))) {
            return false;
        } else {
            if (i1 != l) {
                world.setData(i, j, k, i1, 2);
            }

            return true;
        }
    }

    public void doPhysics(World world, int i, int j, int k, Block block) {
        if (!world.isStatic && !this.e(world, i, j, k)) {
            this.b(world, i, j, k, world.getData(i, j, k), 0);
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

            label134:
            for (i1 = i - b0; i1 <= i + b0; ++i1) {
                for (j1 = k - b0; j1 <= k + b0; ++j1) {
                    for (k1 = j - 1; k1 <= j + 1; ++k1) {
                        if (world.getType(i1, k1, j1) == this) {
                            --l;
                            if (l <= 0) {
                                flag = true;
                                break label134;
                            }
                        }
                    }
                }
            }

            i1 = world.getData(i, j, k);
            j1 = world.random.nextInt(6);
            k1 = Direction.e[j1];
            int l1;

            if (j1 == 1 && j < 255 && world.isEmpty(i, j + 1, k)) {
                if (flag) {
                    return;
                }

                int i2 = world.random.nextInt(16) & i1;

                if (i2 > 0) {
                    for (l1 = 0; l1 <= 3; ++l1) {
                        if (!this.a(world.getType(i + Direction.a[l1], j + 1, k + Direction.b[l1]))) {
                            i2 &= ~(1 << l1);
                        }
                    }

                    if (i2 > 0) {
                        // CraftBukkit start - Call BlockSpreadEvent
                        org.bukkit.block.Block source = world.getWorld().getBlockAt(i, j, k);
                        org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j + 1, k);
                        CraftEventFactory.handleBlockSpreadEvent(block, source, this, l1);
                        // CraftBukkit end
                    }
                }
            } else {
                Block block;
                int j2;

                if (j1 >= 2 && j1 <= 5 && (i1 & 1 << k1) == 0) {
                    if (flag) {
                        return;
                    }

                    block = world.getType(i + Direction.a[k1], j, k + Direction.b[k1]);
                    if (block.material == Material.AIR) {
                        l1 = k1 + 1 & 3;
                        j2 = k1 + 3 & 3;

                        // CraftBukkit start - Call BlockSpreadEvent
                        org.bukkit.block.Block source = world.getWorld().getBlockAt(i, j, k);
                        org.bukkit.block.Block bukkitBlock = world.getWorld().getBlockAt(i + Direction.a[k1], j, k + Direction.b[k1]);
                        if ((i1 & 1 << l1) != 0 && this.a(world.getType(i + Direction.a[k1] + Direction.a[l1], j, k + Direction.b[k1] + Direction.b[l1]))) {
                            CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, 1 << l1);
                        } else if ((i1 & 1 << j2) != 0 && this.a(world.getType(i + Direction.a[k1] + Direction.a[j2], j, k + Direction.b[k1] + Direction.b[j2]))) {
                            CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, 1 << j2);
                        } else if ((i1 & 1 << l1) != 0 && world.isEmpty(i + Direction.a[k1] + Direction.a[l1], j, k + Direction.b[k1] + Direction.b[l1]) && this.a(world.getType(i + Direction.a[l1], j, k + Direction.b[l1]))) {
                            bukkitBlock = world.getWorld().getBlockAt(i + Direction.a[k1] + Direction.a[l1], j, k + Direction.b[k1] + Direction.b[l1]);
                            CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, 1 << (k1 + 2 & 3));
                        } else if ((i1 & 1 << j2) != 0 && world.isEmpty(i + Direction.a[k1] + Direction.a[j2], j, k + Direction.b[k1] + Direction.b[j2]) && this.a(world.getType(i + Direction.a[j2], j, k + Direction.b[j2]))) {
                            bukkitBlock = world.getWorld().getBlockAt(i + Direction.a[k1] + Direction.a[j2], j, k + Direction.b[k1] + Direction.b[j2]);
                            CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, 1 << (k1 + 2 & 3));
                        } else if (this.a(world.getType(i + Direction.a[k1], j + 1, k + Direction.b[k1]))) {
                            CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, 0);
                        }
                        // CraftBukkit end
                    } else if (block.material.k() && block.d()) {
                        world.setData(i, j, k, i1 | 1 << k1, 2);
                    }
                } else if (j > 1) {
                    block = world.getType(i, j - 1, k);
                    if (block.material == Material.AIR) {
                        l1 = world.random.nextInt(16) & i1;
                        if (l1 > 0) {
                            // CraftBukkit start - Call BlockSpreadEvent
                            org.bukkit.block.Block source = world.getWorld().getBlockAt(i, j, k);
                            org.bukkit.block.Block bukkitBlock = world.getWorld().getBlockAt(i, j - 1, k);
                            CraftEventFactory.handleBlockSpreadEvent(bukkitBlock, source, this, l1);
                            // CraftBukkit end
                        }
                    } else if (block == this) {
                        l1 = world.random.nextInt(16) & i1;
                        j2 = world.getData(i, j - 1, k);
                        if (j2 != (j2 | l1)) {
                            world.setData(i, j - 1, k, j2 | l1, 2);
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

    public Item getDropType(int i, Random random, int j) {
        return null;
    }

    public int a(Random random) {
        return 0;
    }

    public void a(World world, EntityHuman entityhuman, int i, int j, int k, int l) {
        if (!world.isStatic && entityhuman.bD() != null && entityhuman.bD().getItem() == Items.SHEARS) {
            entityhuman.a(StatisticList.C[Block.b((Block) this)], 1);
            this.a(world, i, j, k, new ItemStack(Blocks.VINE, 1, 0));
        } else {
            super.a(world, entityhuman, i, j, k, l);
        }
    }
}
