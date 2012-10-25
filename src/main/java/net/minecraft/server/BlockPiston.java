package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
// CraftBukkit end

public class BlockPiston extends Block {

    private boolean a;

    public BlockPiston(int i, int j, boolean flag) {
        super(i, j, Material.PISTON);
        this.a = flag;
        this.a(h);
        this.c(0.5F);
        this.a(CreativeModeTab.d);
    }

    public int a(int i, int j) {
        int k = e(j);

        return k > 5 ? this.textureId : (i == k ? (!f(j) && this.minX <= 0.0D && this.minY <= 0.0D && this.minZ <= 0.0D && this.maxX >= 1.0D && this.maxY >= 1.0D && this.maxZ >= 1.0D ? this.textureId : 110) : (i == Facing.OPPOSITE_FACING[k] ? 109 : 108));
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

    public void postPlace(World world, int i, int j, int k, EntityLiving entityliving) {
        int l = b(world, i, j, k, (EntityHuman) entityliving);

        world.setData(i, j, k, l);
        if (!world.isStatic) {
            this.l(world, i, j, k);
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (!world.isStatic) {
            this.l(world, i, j, k);
        }
    }

    public void onPlace(World world, int i, int j, int k) {
        if (!world.isStatic && world.getTileEntity(i, j, k) == null) {
            this.l(world, i, j, k);
        }
    }

    private void l(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);
        int i1 = e(l);

        if (i1 != 7) {
            boolean flag = this.d(world, i, j, k, i1);

            if (flag && !f(l)) {
                // CraftBukkit start
                int length = h(world, i, j, k, i1);
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
            } else if (!flag && f(l)) {
                // CraftBukkit start
                org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
                BlockPistonRetractEvent event = new BlockPistonRetractEvent(block, CraftBlock.notchToBlockFace(i1));
                world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }
                // CraftBukkit end

                world.playNote(i, j, k, this.id, 1, i1);
            }
        }
    }

    private boolean d(World world, int i, int j, int k, int l) {
        return l != 0 && world.isBlockFaceIndirectlyPowered(i, j - 1, k, 0) ? true : (l != 1 && world.isBlockFaceIndirectlyPowered(i, j + 1, k, 1) ? true : (l != 2 && world.isBlockFaceIndirectlyPowered(i, j, k - 1, 2) ? true : (l != 3 && world.isBlockFaceIndirectlyPowered(i, j, k + 1, 3) ? true : (l != 5 && world.isBlockFaceIndirectlyPowered(i + 1, j, k, 5) ? true : (l != 4 && world.isBlockFaceIndirectlyPowered(i - 1, j, k, 4) ? true : (world.isBlockFaceIndirectlyPowered(i, j, k, 0) ? true : (world.isBlockFaceIndirectlyPowered(i, j + 2, k, 1) ? true : (world.isBlockFaceIndirectlyPowered(i, j + 1, k - 1, 2) ? true : (world.isBlockFaceIndirectlyPowered(i, j + 1, k + 1, 3) ? true : (world.isBlockFaceIndirectlyPowered(i - 1, j + 1, k, 4) ? true : world.isBlockFaceIndirectlyPowered(i + 1, j + 1, k, 5)))))))))));
    }

    public void b(World world, int i, int j, int k, int l, int i1) {
        if (l == 0) {
            world.setRawData(i, j, k, i1 | 8);
        } else {
            world.setRawData(i, j, k, i1);
        }

        if (l == 0) {
            if (this.i(world, i, j, k, i1)) {
                world.setData(i, j, k, i1 | 8);
                world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "tile.piston.out", 0.5F, world.random.nextFloat() * 0.25F + 0.6F);
            } else {
                world.setRawData(i, j, k, i1);
            }
        } else if (l == 1) {
            TileEntity tileentity = world.getTileEntity(i + Facing.b[i1], j + Facing.c[i1], k + Facing.d[i1]);

            if (tileentity instanceof TileEntityPiston) {
                ((TileEntityPiston) tileentity).f();
            }

            world.setRawTypeIdAndData(i, j, k, Block.PISTON_MOVING.id, i1);
            world.setTileEntity(i, j, k, BlockPistonMoving.a(this.id, i1, i1, false, true));
            if (this.a) {
                int j1 = i + Facing.b[i1] * 2;
                int k1 = j + Facing.c[i1] * 2;
                int l1 = k + Facing.d[i1] * 2;
                int i2 = world.getTypeId(j1, k1, l1);
                int j2 = world.getData(j1, k1, l1);
                boolean flag = false;

                if (i2 == Block.PISTON_MOVING.id) {
                    TileEntity tileentity1 = world.getTileEntity(j1, k1, l1);

                    if (tileentity1 instanceof TileEntityPiston) {
                        TileEntityPiston tileentitypiston = (TileEntityPiston) tileentity1;

                        if (tileentitypiston.c() == i1 && tileentitypiston.b()) {
                            tileentitypiston.f();
                            i2 = tileentitypiston.a();
                            j2 = tileentitypiston.p();
                            flag = true;
                        }
                    }
                }

                if (!flag && i2 > 0 && a(i2, world, j1, k1, l1, false) && (Block.byId[i2].q_() == 0 || i2 == Block.PISTON.id || i2 == Block.PISTON_STICKY.id)) {
                    i += Facing.b[i1];
                    j += Facing.c[i1];
                    k += Facing.d[i1];
                    world.setRawTypeIdAndData(i, j, k, Block.PISTON_MOVING.id, j2);
                    world.setTileEntity(i, j, k, BlockPistonMoving.a(i2, j2, i1, false, false));
                    world.setTypeId(j1, k1, l1, 0);
                } else if (!flag) {
                    world.setTypeId(i + Facing.b[i1], j + Facing.c[i1], k + Facing.d[i1], 0);
                }
            } else {
                world.setTypeId(i + Facing.b[i1], j + Facing.c[i1], k + Facing.d[i1], 0);
            }

            world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "tile.piston.in", 0.5F, world.random.nextFloat() * 0.15F + 0.6F);
        }
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
        int l = iblockaccess.getData(i, j, k);

        if (f(l)) {
            switch (e(l)) {
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

    public void f() {
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void a(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List list, Entity entity) {
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.a(world, i, j, k, axisalignedbb, list, entity);
    }

    public AxisAlignedBB e(World world, int i, int j, int k) {
        this.updateShape(world, i, j, k);
        return super.e(world, i, j, k);
    }

    public boolean b() {
        return false;
    }

    public static int e(int i) {
        if ((i & 7) >= Facing.OPPOSITE_FACING.length) return 0; // CraftBukkit - check for AIOOB on piston data
        return i & 7;
    }

    public static boolean f(int i) {
        return (i & 8) != 0;
    }

    public static int b(World world, int i, int j, int k, EntityHuman entityhuman) {
        if (MathHelper.abs((float) entityhuman.locX - (float) i) < 2.0F && MathHelper.abs((float) entityhuman.locZ - (float) k) < 2.0F) {
            double d0 = entityhuman.locY + 1.82D - (double) entityhuman.height;

            if (d0 - (double) j > 2.0D) {
                return 1;
            }

            if ((double) j - d0 > 0.0D) {
                return 0;
            }
        }

        int l = MathHelper.floor((double) (entityhuman.yaw * 4.0F / 360.0F) + 0.5D) & 3;

        return l == 0 ? 2 : (l == 1 ? 5 : (l == 2 ? 3 : (l == 3 ? 4 : 0)));
    }

    private static boolean a(int i, World world, int j, int k, int l, boolean flag) {
        if (i == Block.OBSIDIAN.id) {
            return false;
        } else {
            if (i != Block.PISTON.id && i != Block.PISTON_STICKY.id) {
                if (Block.byId[i].m(world, j, k, l) == -1.0F) {
                    return false;
                }

                if (Block.byId[i].q_() == 2) {
                    return false;
                }

                if (!flag && Block.byId[i].q_() == 1) {
                    return false;
                }
            } else if (f(world.getData(j, k, l))) {
                return false;
            }

            return !(Block.byId[i] instanceof BlockContainer);
        }
    }

    // CraftBukkit - boolean -> int return
    private static int h(World world, int i, int j, int k, int l) {
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

                    if (Block.byId[i2].q_() != 1) {
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

    private boolean i(World world, int i, int j, int k, int l) {
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

                    if (Block.byId[i2].q_() != 1) {
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
                    world.setTypeId(i1, j1, k1, 0);
                }
            }

            while (i1 != i || j1 != j || k1 != k) {
                l1 = i1 - Facing.b[l];
                i2 = j1 - Facing.c[l];
                int j2 = k1 - Facing.d[l];
                int k2 = world.getTypeId(l1, i2, j2);
                int l2 = world.getData(l1, i2, j2);

                if (k2 == this.id && l1 == i && i2 == j && j2 == k) {
                    world.setRawTypeIdAndData(i1, j1, k1, Block.PISTON_MOVING.id, l | (this.a ? 8 : 0), false);
                    world.setTileEntity(i1, j1, k1, BlockPistonMoving.a(Block.PISTON_EXTENSION.id, l | (this.a ? 8 : 0), l, true, false));
                } else {
                    world.setRawTypeIdAndData(i1, j1, k1, Block.PISTON_MOVING.id, l2, false);
                    world.setTileEntity(i1, j1, k1, BlockPistonMoving.a(k2, l2, l, true, false));
                }

                i1 = l1;
                j1 = i2;
                k1 = j2;
            }

            return true;
        }
    }
}
