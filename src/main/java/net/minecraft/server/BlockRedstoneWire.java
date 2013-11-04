package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

public class BlockRedstoneWire extends Block {

    private boolean a = true;
    private Set b = new HashSet();

    public BlockRedstoneWire() {
        super(Material.ORIENTABLE);
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
    }

    public AxisAlignedBB a(World world, int i, int j, int k) {
        return null;
    }

    public boolean c() {
        return false;
    }

    public boolean d() {
        return false;
    }

    public int b() {
        return 5;
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return World.a((IBlockAccess) world, i, j - 1, k) || world.getType(i, j - 1, k) == Blocks.GLOWSTONE;
    }

    private void e(World world, int i, int j, int k) {
        this.a(world, i, j, k, i, j, k);
        ArrayList arraylist = new ArrayList(this.b);

        this.b.clear();

        for (int l = 0; l < arraylist.size(); ++l) {
            ChunkPosition chunkposition = (ChunkPosition) arraylist.get(l);

            world.applyPhysics(chunkposition.x, chunkposition.y, chunkposition.z, this);
        }
    }

    private void a(World world, int i, int j, int k, int l, int i1, int j1) {
        int k1 = world.getData(i, j, k);
        byte b0 = 0;
        int l1 = this.getPower(world, l, i1, j1, b0);

        this.a = false;
        int i2 = world.getHighestNeighborSignal(i, j, k);

        this.a = true;
        if (i2 > 0 && i2 > l1 - 1) {
            l1 = i2;
        }

        int j2 = 0;

        for (int k2 = 0; k2 < 4; ++k2) {
            int l2 = i;
            int i3 = k;

            if (k2 == 0) {
                l2 = i - 1;
            }

            if (k2 == 1) {
                ++l2;
            }

            if (k2 == 2) {
                i3 = k - 1;
            }

            if (k2 == 3) {
                ++i3;
            }

            if (l2 != l || i3 != j1) {
                j2 = this.getPower(world, l2, j, i3, j2);
            }

            if (world.getType(l2, j, i3).r() && !world.getType(i, j + 1, k).r()) {
                if ((l2 != l || i3 != j1) && j >= i1) {
                    j2 = this.getPower(world, l2, j + 1, i3, j2);
                }
            } else if (!world.getType(l2, j, i3).r() && (l2 != l || i3 != j1) && j <= i1) {
                j2 = this.getPower(world, l2, j - 1, i3, j2);
            }
        }

        if (j2 > l1) {
            l1 = j2 - 1;
        } else if (l1 > 0) {
            --l1;
        } else {
            l1 = 0;
        }

        if (i2 > l1 - 1) {
            l1 = i2;
        }

        // CraftBukkit start
        if (k1 != l1) {
            BlockRedstoneEvent event = new BlockRedstoneEvent(world.getWorld().getBlockAt(i, j, k), k1, l1);
            world.getServer().getPluginManager().callEvent(event);

            l1 = event.getNewCurrent();
        }
        // CraftBukkit end
        if (k1 != l1) {
            world.setData(i, j, k, l1, 2);
            this.b.add(new ChunkPosition(i, j, k));
            this.b.add(new ChunkPosition(i - 1, j, k));
            this.b.add(new ChunkPosition(i + 1, j, k));
            this.b.add(new ChunkPosition(i, j - 1, k));
            this.b.add(new ChunkPosition(i, j + 1, k));
            this.b.add(new ChunkPosition(i, j, k - 1));
            this.b.add(new ChunkPosition(i, j, k + 1));
        }
    }

    private void m(World world, int i, int j, int k) {
        if (world.getType(i, j, k) == this) {
            world.applyPhysics(i, j, k, this);
            world.applyPhysics(i - 1, j, k, this);
            world.applyPhysics(i + 1, j, k, this);
            world.applyPhysics(i, j, k - 1, this);
            world.applyPhysics(i, j, k + 1, this);
            world.applyPhysics(i, j - 1, k, this);
            world.applyPhysics(i, j + 1, k, this);
        }
    }

    public void onPlace(World world, int i, int j, int k) {
        super.onPlace(world, i, j, k);
        if (!world.isStatic) {
            this.e(world, i, j, k);
            world.applyPhysics(i, j + 1, k, this);
            world.applyPhysics(i, j - 1, k, this);
            this.m(world, i - 1, j, k);
            this.m(world, i + 1, j, k);
            this.m(world, i, j, k - 1);
            this.m(world, i, j, k + 1);
            if (world.getType(i - 1, j, k).r()) {
                this.m(world, i - 1, j + 1, k);
            } else {
                this.m(world, i - 1, j - 1, k);
            }

            if (world.getType(i + 1, j, k).r()) {
                this.m(world, i + 1, j + 1, k);
            } else {
                this.m(world, i + 1, j - 1, k);
            }

            if (world.getType(i, j, k - 1).r()) {
                this.m(world, i, j + 1, k - 1);
            } else {
                this.m(world, i, j - 1, k - 1);
            }

            if (world.getType(i, j, k + 1).r()) {
                this.m(world, i, j + 1, k + 1);
            } else {
                this.m(world, i, j - 1, k + 1);
            }
        }
    }

    public void remove(World world, int i, int j, int k, Block block, int l) {
        super.remove(world, i, j, k, block, l);
        if (!world.isStatic) {
            world.applyPhysics(i, j + 1, k, this);
            world.applyPhysics(i, j - 1, k, this);
            world.applyPhysics(i + 1, j, k, this);
            world.applyPhysics(i - 1, j, k, this);
            world.applyPhysics(i, j, k + 1, this);
            world.applyPhysics(i, j, k - 1, this);
            this.e(world, i, j, k);
            this.m(world, i - 1, j, k);
            this.m(world, i + 1, j, k);
            this.m(world, i, j, k - 1);
            this.m(world, i, j, k + 1);
            if (world.getType(i - 1, j, k).r()) {
                this.m(world, i - 1, j + 1, k);
            } else {
                this.m(world, i - 1, j - 1, k);
            }

            if (world.getType(i + 1, j, k).r()) {
                this.m(world, i + 1, j + 1, k);
            } else {
                this.m(world, i + 1, j - 1, k);
            }

            if (world.getType(i, j, k - 1).r()) {
                this.m(world, i, j + 1, k - 1);
            } else {
                this.m(world, i, j - 1, k - 1);
            }

            if (world.getType(i, j, k + 1).r()) {
                this.m(world, i, j + 1, k + 1);
            } else {
                this.m(world, i, j - 1, k + 1);
            }
        }
    }

    // CraftBukkit - private -> public
    public int getPower(World world, int i, int j, int k, int l) {
        if (world.getType(i, j, k) != this) {
            return l;
        } else {
            int i1 = world.getData(i, j, k);

            return i1 > l ? i1 : l;
        }
    }

    public void doPhysics(World world, int i, int j, int k, Block block) {
        if (!world.isStatic) {
            boolean flag = this.canPlace(world, i, j, k);

            if (flag) {
                this.e(world, i, j, k);
            } else {
                this.b(world, i, j, k, 0, 0);
                world.setAir(i, j, k);
            }

            super.doPhysics(world, i, j, k, block);
        }
    }

    public Item getDropType(int i, Random random, int j) {
        return Items.REDSTONE;
    }

    public int c(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return !this.a ? 0 : this.b(iblockaccess, i, j, k, l);
    }

    public int b(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        if (!this.a) {
            return 0;
        } else {
            int i1 = iblockaccess.getData(i, j, k);

            if (i1 == 0) {
                return 0;
            } else if (l == 1) {
                return i1;
            } else {
                boolean flag = g(iblockaccess, i - 1, j, k, 1) || !iblockaccess.getType(i - 1, j, k).r() && g(iblockaccess, i - 1, j - 1, k, -1);
                boolean flag1 = g(iblockaccess, i + 1, j, k, 3) || !iblockaccess.getType(i + 1, j, k).r() && g(iblockaccess, i + 1, j - 1, k, -1);
                boolean flag2 = g(iblockaccess, i, j, k - 1, 2) || !iblockaccess.getType(i, j, k - 1).r() && g(iblockaccess, i, j - 1, k - 1, -1);
                boolean flag3 = g(iblockaccess, i, j, k + 1, 0) || !iblockaccess.getType(i, j, k + 1).r() && g(iblockaccess, i, j - 1, k + 1, -1);

                if (!iblockaccess.getType(i, j + 1, k).r()) {
                    if (iblockaccess.getType(i - 1, j, k).r() && g(iblockaccess, i - 1, j + 1, k, -1)) {
                        flag = true;
                    }

                    if (iblockaccess.getType(i + 1, j, k).r() && g(iblockaccess, i + 1, j + 1, k, -1)) {
                        flag1 = true;
                    }

                    if (iblockaccess.getType(i, j, k - 1).r() && g(iblockaccess, i, j + 1, k - 1, -1)) {
                        flag2 = true;
                    }

                    if (iblockaccess.getType(i, j, k + 1).r() && g(iblockaccess, i, j + 1, k + 1, -1)) {
                        flag3 = true;
                    }
                }

                return !flag2 && !flag1 && !flag && !flag3 && l >= 2 && l <= 5 ? i1 : (l == 2 && flag2 && !flag && !flag1 ? i1 : (l == 3 && flag3 && !flag && !flag1 ? i1 : (l == 4 && flag && !flag2 && !flag3 ? i1 : (l == 5 && flag1 && !flag2 && !flag3 ? i1 : 0))));
            }
        }
    }

    public boolean isPowerSource() {
        return this.a;
    }

    public static boolean f(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        Block block = iblockaccess.getType(i, j, k);

        if (block == Blocks.REDSTONE_WIRE) {
            return true;
        } else if (!Blocks.DIODE_OFF.e(block)) {
            return block.isPowerSource() && l != -1;
        } else {
            int i1 = iblockaccess.getData(i, j, k);

            return l == (i1 & 3) || l == Direction.f[i1 & 3];
        }
    }

    public static boolean g(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        if (f(iblockaccess, i, j, k, l)) {
            return true;
        } else if (iblockaccess.getType(i, j, k) == Blocks.DIODE_ON) {
            int i1 = iblockaccess.getData(i, j, k);

            return l == (i1 & 3);
        } else {
            return false;
        }
    }
}
