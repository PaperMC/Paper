package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

public class BlockRedstoneWire extends Block {

    private boolean a = true;
    private Set b = new java.util.LinkedHashSet(); // CraftBukkit - HashSet -> LinkedHashSet

    public BlockRedstoneWire(int i, int j) {
        super(i, j, Material.ORIENTABLE);
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
    }

    public int a(int i, int j) {
        return this.textureId;
    }

    public AxisAlignedBB e(World world, int i, int j, int k) {
        return null;
    }

    public boolean c() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public int d() {
        return 5;
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return world.t(i, j - 1, k) || world.getTypeId(i, j - 1, k) == Block.GLOWSTONE.id;
    }

    private void l(World world, int i, int j, int k) {
        this.a(world, i, j, k, i, j, k);
        ArrayList arraylist = new ArrayList(this.b);

        this.b.clear();
        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            ChunkPosition chunkposition = (ChunkPosition) iterator.next();

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

                if (world.s(j2, j, k2) && !world.s(i, j + 1, k)) {
                    if (j2 != l || j + 1 != i1 || k2 != j1) {
                        l1 = this.getPower(world, j2, j + 1, k2, l1);
                    }
                } else if (!world.s(j2, j, k2) && (j2 != l || j - 1 != i1 || k2 != j1)) {
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
            BlockRedstoneEvent event = new BlockRedstoneEvent(world.getWorld().getBlockAt(i, j, k), k1, l1);
            world.getServer().getPluginManager().callEvent(event);

            l1 = event.getNewCurrent();
        }
        // CraftBukkit end

        if (k1 != l1) {
            world.suppressPhysics = true;
            world.setData(i, j, k, l1);
            world.e(i, j, k, i, j, k);
            world.suppressPhysics = false;

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

                if (world.s(j2, j, k2)) {
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

            if (k1 < l1 || l1 == 0) {
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

    private void n(World world, int i, int j, int k) {
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

    public void onPlace(World world, int i, int j, int k) {
        if (world.suppressPhysics) return; // CraftBukkit
        super.onPlace(world, i, j, k);
        if (!world.isStatic) {
            this.l(world, i, j, k);
            world.applyPhysics(i, j + 1, k, this.id);
            world.applyPhysics(i, j - 1, k, this.id);
            this.n(world, i - 1, j, k);
            this.n(world, i + 1, j, k);
            this.n(world, i, j, k - 1);
            this.n(world, i, j, k + 1);
            if (world.s(i - 1, j, k)) {
                this.n(world, i - 1, j + 1, k);
            } else {
                this.n(world, i - 1, j - 1, k);
            }

            if (world.s(i + 1, j, k)) {
                this.n(world, i + 1, j + 1, k);
            } else {
                this.n(world, i + 1, j - 1, k);
            }

            if (world.s(i, j, k - 1)) {
                this.n(world, i, j + 1, k - 1);
            } else {
                this.n(world, i, j - 1, k - 1);
            }

            if (world.s(i, j, k + 1)) {
                this.n(world, i, j + 1, k + 1);
            } else {
                this.n(world, i, j - 1, k + 1);
            }
        }
    }

    public void remove(World world, int i, int j, int k, int l, int i1) {
        super.remove(world, i, j, k, l, i1);
        if (!world.isStatic) {
            world.applyPhysics(i, j + 1, k, this.id);
            world.applyPhysics(i, j - 1, k, this.id);
            world.applyPhysics(i + 1, j, k, this.id);
            world.applyPhysics(i - 1, j, k, this.id);
            world.applyPhysics(i, j, k + 1, this.id);
            world.applyPhysics(i, j, k - 1, this.id);
            this.l(world, i, j, k);
            this.n(world, i - 1, j, k);
            this.n(world, i + 1, j, k);
            this.n(world, i, j, k - 1);
            this.n(world, i, j, k + 1);
            if (world.s(i - 1, j, k)) {
                this.n(world, i - 1, j + 1, k);
            } else {
                this.n(world, i - 1, j - 1, k);
            }

            if (world.s(i + 1, j, k)) {
                this.n(world, i + 1, j + 1, k);
            } else {
                this.n(world, i + 1, j - 1, k);
            }

            if (world.s(i, j, k - 1)) {
                this.n(world, i, j + 1, k - 1);
            } else {
                this.n(world, i, j - 1, k - 1);
            }

            if (world.s(i, j, k + 1)) {
                this.n(world, i, j + 1, k + 1);
            } else {
                this.n(world, i, j - 1, k + 1);
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

            if (flag) {
                this.l(world, i, j, k);
            } else {
                this.c(world, i, j, k, i1, 0);
                world.setTypeId(i, j, k, 0);
            }

            super.doPhysics(world, i, j, k, l);
        }
    }

    public int getDropType(int i, Random random, int j) {
        return Item.REDSTONE.id;
    }

    public boolean c(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return !this.a ? false : this.b(iblockaccess, i, j, k, l);
    }

    public boolean b(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        if (!this.a) {
            return false;
        } else if (iblockaccess.getData(i, j, k) == 0) {
            return false;
        } else if (l == 1) {
            return true;
        } else {
            boolean flag = g(iblockaccess, i - 1, j, k, 1) || !iblockaccess.s(i - 1, j, k) && g(iblockaccess, i - 1, j - 1, k, -1);
            boolean flag1 = g(iblockaccess, i + 1, j, k, 3) || !iblockaccess.s(i + 1, j, k) && g(iblockaccess, i + 1, j - 1, k, -1);
            boolean flag2 = g(iblockaccess, i, j, k - 1, 2) || !iblockaccess.s(i, j, k - 1) && g(iblockaccess, i, j - 1, k - 1, -1);
            boolean flag3 = g(iblockaccess, i, j, k + 1, 0) || !iblockaccess.s(i, j, k + 1) && g(iblockaccess, i, j - 1, k + 1, -1);

            if (!iblockaccess.s(i, j + 1, k)) {
                if (iblockaccess.s(i - 1, j, k) && g(iblockaccess, i - 1, j + 1, k, -1)) {
                    flag = true;
                }

                if (iblockaccess.s(i + 1, j, k) && g(iblockaccess, i + 1, j + 1, k, -1)) {
                    flag1 = true;
                }

                if (iblockaccess.s(i, j, k - 1) && g(iblockaccess, i, j + 1, k - 1, -1)) {
                    flag2 = true;
                }

                if (iblockaccess.s(i, j, k + 1) && g(iblockaccess, i, j + 1, k + 1, -1)) {
                    flag3 = true;
                }
            }

            return !flag2 && !flag1 && !flag && !flag3 && l >= 2 && l <= 5 ? true : (l == 2 && flag2 && !flag && !flag1 ? true : (l == 3 && flag3 && !flag && !flag1 ? true : (l == 4 && flag && !flag2 && !flag3 ? true : l == 5 && flag1 && !flag2 && !flag3)));
        }
    }

    public boolean isPowerSource() {
        return this.a;
    }

    public static boolean f(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        int i1 = iblockaccess.getTypeId(i, j, k);

        if (i1 == Block.REDSTONE_WIRE.id) {
            return true;
        } else if (i1 == 0) {
            return false;
        } else if (i1 != Block.DIODE_OFF.id && i1 != Block.DIODE_ON.id) {
            return Block.byId[i1].isPowerSource() && l != -1;
        } else {
            int j1 = iblockaccess.getData(i, j, k);

            return l == (j1 & 3) || l == Direction.f[j1 & 3];
        }
    }

    public static boolean g(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        if (f(iblockaccess, i, j, k, l)) {
            return true;
        } else {
            int i1 = iblockaccess.getTypeId(i, j, k);

            if (i1 == Block.DIODE_ON.id) {
                int j1 = iblockaccess.getData(i, j, k);

                return l == (j1 & 3);
            } else {
                return false;
            }
        }
    }
}
