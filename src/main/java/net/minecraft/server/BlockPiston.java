package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
// CraftBukkit end

public class BlockPiston extends Block {

    private final boolean a;

    public BlockPiston(int i, boolean flag) {
        super(i, Material.PISTON);
        this.a = flag;
        this.a(k);
        this.c(0.5F);
        this.a(CreativeModeTab.d);
    }

    public int d() {
        return 16;
    }

    public boolean c() {
        return false;
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman, int l, float f, float f1, float f2) {
        return false;
    }

    public void postPlace(World world, int i, int j, int k, EntityLiving entityliving, ItemStack itemstack) {
        int l = a(world, i, j, k, entityliving);

        world.setData(i, j, k, l, 2);
        if (!world.isStatic) {
            this.k(world, i, j, k);
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (!world.isStatic) {
            this.k(world, i, j, k);
        }
    }

    public void onPlace(World world, int i, int j, int k) {
        if (!world.isStatic && world.getTileEntity(i, j, k) == null) {
            this.k(world, i, j, k);
        }
    }

    private void k(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);
        int i1 = d(l);

        if (i1 != 7) {
            boolean flag = this.d(world, i, j, k, i1);

            if (flag && !e(l)) {
                // CraftBukkit start
                int length = e(world, i, j, k, i1);
                if (length >= 0) {
                    org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
                    BlockPistonExtendEvent event = new BlockPistonExtendEvent(block, length, CraftBlock.notchToBlockFace(i1));
                    world.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return;
                    }
                    // CraftBukkit end

                    world.playNote(i, j, k, this.id, 0, i1);
                }
            } else if (!flag && e(l)) {
                // CraftBukkit start
                org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
                BlockPistonRetractEvent event = new BlockPistonRetractEvent(block, CraftBlock.notchToBlockFace(i1));
                world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }
                // CraftBukkit end

                world.setData(i, j, k, i1, 2);
                world.playNote(i, j, k, this.id, 1, i1);
            }
        }
    }

    private boolean d(World world, int i, int j, int k, int l) {
        return l != 0 && world.isBlockFacePowered(i, j - 1, k, 0) ? true : (l != 1 && world.isBlockFacePowered(i, j + 1, k, 1) ? true : (l != 2 && world.isBlockFacePowered(i, j, k - 1, 2) ? true : (l != 3 && world.isBlockFacePowered(i, j, k + 1, 3) ? true : (l != 5 && world.isBlockFacePowered(i + 1, j, k, 5) ? true : (l != 4 && world.isBlockFacePowered(i - 1, j, k, 4) ? true : (world.isBlockFacePowered(i, j, k, 0) ? true : (world.isBlockFacePowered(i, j + 2, k, 1) ? true : (world.isBlockFacePowered(i, j + 1, k - 1, 2) ? true : (world.isBlockFacePowered(i, j + 1, k + 1, 3) ? true : (world.isBlockFacePowered(i - 1, j + 1, k, 4) ? true : world.isBlockFacePowered(i + 1, j + 1, k, 5)))))))))));
    }

    public boolean b(World world, int i, int j, int k, int l, int i1) {
        if (!world.isStatic) {
            boolean flag = this.d(world, i, j, k, i1);

            if (flag && l == 1) {
                world.setData(i, j, k, i1 | 8, 2);
                return false;
            }

            if (!flag && l == 0) {
                return false;
            }
        }

        if (l == 0) {
            if (!this.f(world, i, j, k, i1)) {
                return false;
            }

            world.setData(i, j, k, i1 | 8, 2);
            world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "tile.piston.out", 0.5F, world.random.nextFloat() * 0.25F + 0.6F);
        } else if (l == 1) {
            TileEntity tileentity = world.getTileEntity(i + Facing.b[i1], j + Facing.c[i1], k + Facing.d[i1]);

            if (tileentity instanceof TileEntityPiston) {
                ((TileEntityPiston) tileentity).f();
            }

            world.setTypeIdAndData(i, j, k, Block.PISTON_MOVING.id, i1, 3);
            world.setTileEntity(i, j, k, BlockPistonMoving.a(this.id, i1, i1, false, true));
            if (this.a) {
                int j1 = i + Facing.b[i1] * 2;
                int k1 = j + Facing.c[i1] * 2;
                int l1 = k + Facing.d[i1] * 2;
                int i2 = world.getTypeId(j1, k1, l1);
                int j2 = world.getData(j1, k1, l1);
                boolean flag1 = false;

                if (i2 == Block.PISTON_MOVING.id) {
                    TileEntity tileentity1 = world.getTileEntity(j1, k1, l1);

                    if (tileentity1 instanceof TileEntityPiston) {
                        TileEntityPiston tileentitypiston = (TileEntityPiston) tileentity1;

                        if (tileentitypiston.c() == i1 && tileentitypiston.b()) {
                            tileentitypiston.f();
                            i2 = tileentitypiston.a();
                            j2 = tileentitypiston.p();
                            flag1 = true;
                        }
                    }
                }

                if (!flag1 && i2 > 0 && a(i2, world, j1, k1, l1, false) && (Block.byId[i2].h() == 0 || i2 == Block.PISTON.id || i2 == Block.PISTON_STICKY.id)) {
                    i += Facing.b[i1];
                    j += Facing.c[i1];
                    k += Facing.d[i1];
                    world.setTypeIdAndData(i, j, k, Block.PISTON_MOVING.id, j2, 3);
                    world.setTileEntity(i, j, k, BlockPistonMoving.a(i2, j2, i1, false, false));
                    world.setAir(j1, k1, l1);
                } else if (!flag1) {
                    world.setAir(i + Facing.b[i1], j + Facing.c[i1], k + Facing.d[i1]);
                }
            } else {
                world.setAir(i + Facing.b[i1], j + Facing.c[i1], k + Facing.d[i1]);
            }

            world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "tile.piston.in", 0.5F, world.random.nextFloat() * 0.15F + 0.6F);
        }

        return true;
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
        int l = iblockaccess.getData(i, j, k);

        if (e(l)) {
            float f = 0.25F;

            switch (d(l)) {
            case 0:
                this.a(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
                break;

            case 1:
                this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
                break;

            case 2:
                this.a(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
                break;

            case 3:
                this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
                break;

            case 4:
                this.a(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                break;

            case 5:
                this.a(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
            }
        } else {
            this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public void g() {
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void a(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List list, Entity entity) {
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.a(world, i, j, k, axisalignedbb, list, entity);
    }

    public AxisAlignedBB b(World world, int i, int j, int k) {
        this.updateShape(world, i, j, k);
        return super.b(world, i, j, k);
    }

    public boolean b() {
        return false;
    }

    public static int d(int i) {
        if ((i & 7) >= Facing.OPPOSITE_FACING.length) return 7; // CraftBukkit - check for AIOOB on piston data
        return i & 7;
    }

    public static boolean e(int i) {
        return (i & 8) != 0;
    }

    public static int a(World world, int i, int j, int k, EntityLiving entityliving) {
        if (MathHelper.abs((float) entityliving.locX - (float) i) < 2.0F && MathHelper.abs((float) entityliving.locZ - (float) k) < 2.0F) {
            double d0 = entityliving.locY + 1.82D - (double) entityliving.height;

            if (d0 - (double) j > 2.0D) {
                return 1;
            }

            if ((double) j - d0 > 0.0D) {
                return 0;
            }
        }

        int l = MathHelper.floor((double) (entityliving.yaw * 4.0F / 360.0F) + 0.5D) & 3;

        return l == 0 ? 2 : (l == 1 ? 5 : (l == 2 ? 3 : (l == 3 ? 4 : 0)));
    }

    private static boolean a(int i, World world, int j, int k, int l, boolean flag) {
        if (i == Block.OBSIDIAN.id) {
            return false;
        } else {
            if (i != Block.PISTON.id && i != Block.PISTON_STICKY.id) {
                if (Block.byId[i].l(world, j, k, l) == -1.0F) {
                    return false;
                }

                if (Block.byId[i].h() == 2) {
                    return false;
                }

                if (Block.byId[i].h() == 1) {
                    if (!flag) {
                        return false;
                    }

                    return true;
                }
            } else if (e(world.getData(j, k, l))) {
                return false;
            }

            return !(Block.byId[i] instanceof IContainer);
        }
    }

    // CraftBukkit - boolean -> int return
    private static int e(World world, int i, int j, int k, int l) {
        int i1 = i + Facing.b[l];
        int j1 = j + Facing.c[l];
        int k1 = k + Facing.d[l];
        int l1 = 0;

        while (true) {
            if (l1 < 13) {
                if (j1 <= 0 || j1 >= 255) {
                    return -1; // CraftBukkit
                }

                int i2 = world.getTypeId(i1, j1, k1);

                if (i2 != 0) {
                    if (!a(i2, world, i1, j1, k1, true)) {
                        return -1; // CraftBukkit
                    }

                    if (Block.byId[i2].h() != 1) {
                        if (l1 == 12) {
                            return -1; // CraftBukkit
                        }

                        i1 += Facing.b[l];
                        j1 += Facing.c[l];
                        k1 += Facing.d[l];
                        ++l1;
                        continue;
                    }
                }
            }

            return l1; // CraftBukkit
        }
    }

    private boolean f(World world, int i, int j, int k, int l) {
        int i1 = i + Facing.b[l];
        int j1 = j + Facing.c[l];
        int k1 = k + Facing.d[l];
        int l1 = 0;

        while (true) {
            int i2;

            if (l1 < 13) {
                if (j1 <= 0 || j1 >= 255) {
                    return false;
                }

                i2 = world.getTypeId(i1, j1, k1);
                if (i2 != 0) {
                    if (!a(i2, world, i1, j1, k1, true)) {
                        return false;
                    }

                    if (Block.byId[i2].h() != 1) {
                        if (l1 == 12) {
                            return false;
                        }

                        i1 += Facing.b[l];
                        j1 += Facing.c[l];
                        k1 += Facing.d[l];
                        ++l1;
                        continue;
                    }

                    Block.byId[i2].c(world, i1, j1, k1, world.getData(i1, j1, k1), 0);
                    world.setAir(i1, j1, k1);
                }
            }

            l1 = i1;
            i2 = j1;
            int j2 = k1;
            int k2 = 0;

            int[] aint;
            int l2;
            int i3;
            int j3;

            for (aint = new int[13]; i1 != i || j1 != j || k1 != k; k1 = j3) {
                l2 = i1 - Facing.b[l];
                i3 = j1 - Facing.c[l];
                j3 = k1 - Facing.d[l];
                int k3 = world.getTypeId(l2, i3, j3);
                int l3 = world.getData(l2, i3, j3);

                if (k3 == this.id && l2 == i && i3 == j && j3 == k) {
                    world.setTypeIdAndData(i1, j1, k1, Block.PISTON_MOVING.id, l | (this.a ? 8 : 0), 4);
                    world.setTileEntity(i1, j1, k1, BlockPistonMoving.a(Block.PISTON_EXTENSION.id, l | (this.a ? 8 : 0), l, true, false));
                } else {
                    world.setTypeIdAndData(i1, j1, k1, Block.PISTON_MOVING.id, l3, 4);
                    world.setTileEntity(i1, j1, k1, BlockPistonMoving.a(k3, l3, l, true, false));
                }

                aint[k2++] = k3;
                i1 = l2;
                j1 = i3;
            }

            i1 = l1;
            j1 = i2;
            k1 = j2;

            for (k2 = 0; i1 != i || j1 != j || k1 != k; k1 = j3) {
                l2 = i1 - Facing.b[l];
                i3 = j1 - Facing.c[l];
                j3 = k1 - Facing.d[l];
                world.applyPhysics(l2, i3, j3, aint[k2++]);
                i1 = l2;
                j1 = i3;
            }

            return true;
        }
    }
}
