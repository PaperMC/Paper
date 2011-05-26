package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

// CraftBukkit start
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.event.block.BlockRedstoneEvent;
// CraftBukkit end

public class BlockRedstoneWire extends Block {

    private boolean a = true;
    private Set b = new HashSet();

    public BlockRedstoneWire(int i, int j) {
        super(i, j, Material.ORIENTABLE);
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
    }

    public int a(int i, int j) {
        return this.textureId;
    }

    public AxisAlignedBB d(World world, int i, int j, int k) {
        return null;
    }

    public boolean a() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return world.d(i, j - 1, k);
    }

    private void g(World world, int i, int j, int k) {
        this.a(world, i, j, k, i, j, k);
        ArrayList arraylist = new ArrayList(this.b);

        this.b.clear();

        for (int l = 0; l < arraylist.size(); ++l) {
            ChunkPosition chunkposition = (ChunkPosition) arraylist.get(l);

            world.applyPhysics(chunkposition.x, chunkposition.y, chunkposition.z, this.id);
        }
    }

    private void a(World world, int i, int j, int k, int l, int i1, int j1) {
        int k1 = world.getData(i, j, k);
        int l1 = 0;

        this.a = false;
        boolean flag = world.isBlockIndirectlyPowered(i, j, k);

        this.a = true;
        int i2;
        int j2;
        int k2;

        if (flag) {
            l1 = 15;
        } else {
            for (i2 = 0; i2 < 4; ++i2) {
                j2 = i;
                k2 = k;
                if (i2 == 0) {
                    j2 = i - 1;
                }

                if (i2 == 1) {
                    ++j2;
                }

                if (i2 == 2) {
                    k2 = k - 1;
                }

                if (i2 == 3) {
                    ++k2;
                }

                if (j2 != l || j != i1 || k2 != j1) {
                    l1 = this.getPower(world, j2, j, k2, l1);
                }

                if (world.d(j2, j, k2) && !world.d(i, j + 1, k)) {
                    if (j2 != l || j + 1 != i1 || k2 != j1) {
                        l1 = this.getPower(world, j2, j + 1, k2, l1);
                    }
                } else if (!world.d(j2, j, k2) && (j2 != l || j - 1 != i1 || k2 != j1)) {
                    l1 = this.getPower(world, j2, j - 1, k2, l1);
                }
            }

            if (l1 > 0) {
                --l1;
            } else {
                l1 = 0;
            }
        }

        // CraftBukkit start
        if (k1 != l1) {
            CraftBlock block = (CraftBlock) ((WorldServer) world).getWorld().getBlockAt(i, j, k);
            BlockRedstoneEvent event = new BlockRedstoneEvent(block, k1, l1);
            ((WorldServer) world).getServer().getPluginManager().callEvent(event);
            l1 = event.getNewCurrent();
        }
        // CraftBukkit end

        if (k1 != l1) {
            world.o = true;
            world.setData(i, j, k, l1);
            world.b(i, j, k, i, j, k);
            world.o = false;

            for (i2 = 0; i2 < 4; ++i2) {
                j2 = i;
                k2 = k;
                int l2 = j - 1;

                if (i2 == 0) {
                    j2 = i - 1;
                }

                if (i2 == 1) {
                    ++j2;
                }

                if (i2 == 2) {
                    k2 = k - 1;
                }

                if (i2 == 3) {
                    ++k2;
                }

                if (world.d(j2, j, k2)) {
                    l2 += 2;
                }

                boolean flag1 = false;
                int i3 = this.getPower(world, j2, j, k2, -1);

                l1 = world.getData(i, j, k);
                if (l1 > 0) {
                    --l1;
                }

                if (i3 >= 0 && i3 != l1) {
                    this.a(world, j2, j, k2, i, j, k);
                }

                i3 = this.getPower(world, j2, l2, k2, -1);
                l1 = world.getData(i, j, k);
                if (l1 > 0) {
                    --l1;
                }

                if (i3 >= 0 && i3 != l1) {
                    this.a(world, j2, l2, k2, i, j, k);
                }
            }

            if (k1 == 0 || l1 == 0) {
                this.b.add(new ChunkPosition(i, j, k));
                this.b.add(new ChunkPosition(i - 1, j, k));
                this.b.add(new ChunkPosition(i + 1, j, k));
                this.b.add(new ChunkPosition(i, j - 1, k));
                this.b.add(new ChunkPosition(i, j + 1, k));
                this.b.add(new ChunkPosition(i, j, k - 1));
                this.b.add(new ChunkPosition(i, j, k + 1));
            }
        }
    }

    private void h(World world, int i, int j, int k) {
        if (world.getTypeId(i, j, k) == this.id) {
            world.applyPhysics(i, j, k, this.id);
            world.applyPhysics(i - 1, j, k, this.id);
            world.applyPhysics(i + 1, j, k, this.id);
            world.applyPhysics(i, j, k - 1, this.id);
            world.applyPhysics(i, j, k + 1, this.id);
            world.applyPhysics(i, j - 1, k, this.id);
            world.applyPhysics(i, j + 1, k, this.id);
        }
    }

    public void e(World world, int i, int j, int k) {
        super.e(world, i, j, k);
        if (!world.isStatic) {
            this.g(world, i, j, k);
            world.applyPhysics(i, j + 1, k, this.id);
            world.applyPhysics(i, j - 1, k, this.id);
            this.h(world, i - 1, j, k);
            this.h(world, i + 1, j, k);
            this.h(world, i, j, k - 1);
            this.h(world, i, j, k + 1);
            if (world.d(i - 1, j, k)) {
                this.h(world, i - 1, j + 1, k);
            } else {
                this.h(world, i - 1, j - 1, k);
            }

            if (world.d(i + 1, j, k)) {
                this.h(world, i + 1, j + 1, k);
            } else {
                this.h(world, i + 1, j - 1, k);
            }

            if (world.d(i, j, k - 1)) {
                this.h(world, i, j + 1, k - 1);
            } else {
                this.h(world, i, j - 1, k - 1);
            }

            if (world.d(i, j, k + 1)) {
                this.h(world, i, j + 1, k + 1);
            } else {
                this.h(world, i, j - 1, k + 1);
            }
        }
    }

    public void remove(World world, int i, int j, int k) {
        super.remove(world, i, j, k);
        if (!world.isStatic) {
            world.applyPhysics(i, j + 1, k, this.id);
            world.applyPhysics(i, j - 1, k, this.id);
            this.g(world, i, j, k);
            this.h(world, i - 1, j, k);
            this.h(world, i + 1, j, k);
            this.h(world, i, j, k - 1);
            this.h(world, i, j, k + 1);
            if (world.d(i - 1, j, k)) {
                this.h(world, i - 1, j + 1, k);
            } else {
                this.h(world, i - 1, j - 1, k);
            }

            if (world.d(i + 1, j, k)) {
                this.h(world, i + 1, j + 1, k);
            } else {
                this.h(world, i + 1, j - 1, k);
            }

            if (world.d(i, j, k - 1)) {
                this.h(world, i, j + 1, k - 1);
            } else {
                this.h(world, i, j - 1, k - 1);
            }

            if (world.d(i, j, k + 1)) {
                this.h(world, i, j + 1, k + 1);
            } else {
                this.h(world, i, j - 1, k + 1);
            }
        }
    }

    // CraftBukkit - private -> public
    public int getPower(World world, int i, int j, int k, int l) {
        if (world.getTypeId(i, j, k) != this.id) {
            return l;
        } else {
            int i1 = world.getData(i, j, k);

            return i1 > l ? i1 : l;
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (!world.isStatic) {
            int i1 = world.getData(i, j, k);
            boolean flag = this.canPlace(world, i, j, k);

            if (!flag) {
                this.b_(world, i, j, k, i1);
                world.setTypeId(i, j, k, 0);
            } else {
                this.g(world, i, j, k);
            }

            super.doPhysics(world, i, j, k, l);
        }
    }

    public int a(int i, Random random) {
        return Item.REDSTONE.id;
    }

    public boolean c(World world, int i, int j, int k, int l) {
        return !this.a ? false : this.a(world, i, j, k, l);
    }

    public boolean a(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        if (!this.a) {
            return false;
        } else if (iblockaccess.getData(i, j, k) == 0) {
            return false;
        } else if (l == 1) {
            return true;
        } else {
            boolean flag = b(iblockaccess, i - 1, j, k) || !iblockaccess.d(i - 1, j, k) && b(iblockaccess, i - 1, j - 1, k);
            boolean flag1 = b(iblockaccess, i + 1, j, k) || !iblockaccess.d(i + 1, j, k) && b(iblockaccess, i + 1, j - 1, k);
            boolean flag2 = b(iblockaccess, i, j, k - 1) || !iblockaccess.d(i, j, k - 1) && b(iblockaccess, i, j - 1, k - 1);
            boolean flag3 = b(iblockaccess, i, j, k + 1) || !iblockaccess.d(i, j, k + 1) && b(iblockaccess, i, j - 1, k + 1);

            if (!iblockaccess.d(i, j + 1, k)) {
                if (iblockaccess.d(i - 1, j, k) && b(iblockaccess, i - 1, j + 1, k)) {
                    flag = true;
                }

                if (iblockaccess.d(i + 1, j, k) && b(iblockaccess, i + 1, j + 1, k)) {
                    flag1 = true;
                }

                if (iblockaccess.d(i, j, k - 1) && b(iblockaccess, i, j + 1, k - 1)) {
                    flag2 = true;
                }

                if (iblockaccess.d(i, j, k + 1) && b(iblockaccess, i, j + 1, k + 1)) {
                    flag3 = true;
                }
            }

            return !flag2 && !flag1 && !flag && !flag3 && l >= 2 && l <= 5 ? true : (l == 2 && flag2 && !flag && !flag1 ? true : (l == 3 && flag3 && !flag && !flag1 ? true : (l == 4 && flag && !flag2 && !flag3 ? true : l == 5 && flag1 && !flag2 && !flag3)));
        }
    }

    public boolean isPowerSource() {
        return this.a;
    }

    public static boolean b(IBlockAccess iblockaccess, int i, int j, int k) {
        int l = iblockaccess.getTypeId(i, j, k);

        return l == Block.REDSTONE_WIRE.id ? true : (l == 0 ? false : Block.byId[l].isPowerSource());
    }
}
