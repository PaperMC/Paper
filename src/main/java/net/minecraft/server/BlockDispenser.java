package net.minecraft.server;

import java.util.Random;

public class BlockDispenser extends BlockContainer {

    public static final IRegistry a = new RegistryDefault(new DispenseBehaviorItem());
    private Random b = new Random();
    public static boolean eventFired = false; // CraftBukkit

    protected BlockDispenser(int i) {
        super(i, Material.STONE);
        this.textureId = 45;
        this.a(CreativeModeTab.d);
    }

    public int r_() {
        return 4;
    }

    public int getDropType(int i, Random random, int j) {
        return Block.DISPENSER.id;
    }

    public void onPlace(World world, int i, int j, int k) {
        super.onPlace(world, i, j, k);
        this.l(world, i, j, k);
    }

    private void l(World world, int i, int j, int k) {
        if (!world.isStatic) {
            int l = world.getTypeId(i, j, k - 1);
            int i1 = world.getTypeId(i, j, k + 1);
            int j1 = world.getTypeId(i - 1, j, k);
            int k1 = world.getTypeId(i + 1, j, k);
            byte b0 = 3;

            if (Block.q[l] && !Block.q[i1]) {
                b0 = 3;
            }

            if (Block.q[i1] && !Block.q[l]) {
                b0 = 2;
            }

            if (Block.q[j1] && !Block.q[k1]) {
                b0 = 5;
            }

            if (Block.q[k1] && !Block.q[j1]) {
                b0 = 4;
            }

            world.setData(i, j, k, b0);
        }
    }

    public int a(int i) {
        return i == 1 ? this.textureId + 17 : (i == 0 ? this.textureId + 17 : (i == 3 ? this.textureId + 1 : this.textureId));
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman, int l, float f, float f1, float f2) {
        if (world.isStatic) {
            return true;
        } else {
            TileEntityDispenser tileentitydispenser = (TileEntityDispenser) world.getTileEntity(i, j, k);

            if (tileentitydispenser != null) {
                entityhuman.openDispenser(tileentitydispenser);
            }

            return true;
        }
    }

    // CraftBukkit - private -> public
    public void dispense(World world, int i, int j, int k) {
        SourceBlock sourceblock = new SourceBlock(world, i, j, k);
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser) sourceblock.getTileEntity();

        if (tileentitydispenser != null) {
            int l = tileentitydispenser.i();

            if (l < 0) {
                world.triggerEffect(1001, i, j, k, 0);
            } else {
                ItemStack itemstack = tileentitydispenser.getItem(l);
                IDispenseBehavior idispensebehavior = (IDispenseBehavior) a.a(itemstack.getItem());

                if (idispensebehavior != IDispenseBehavior.a) {
                    ItemStack itemstack1 = idispensebehavior.a(sourceblock, itemstack);
                    eventFired = false; // CraftBukkit - reset event status

                    tileentitydispenser.setItem(l, itemstack1.count == 0 ? null : itemstack1);
                }
            }
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (l > 0 && Block.byId[l].isPowerSource()) {
            boolean flag = world.isBlockIndirectlyPowered(i, j, k) || world.isBlockIndirectlyPowered(i, j + 1, k);

            if (flag) {
                world.a(i, j, k, this.id, this.r_());
            }
        }
    }

    public void b(World world, int i, int j, int k, Random random) {
        if (!world.isStatic && (world.isBlockIndirectlyPowered(i, j, k) || world.isBlockIndirectlyPowered(i, j + 1, k))) {
            this.dispense(world, i, j, k);
        }
    }

    public TileEntity a(World world) {
        return new TileEntityDispenser();
    }

    public void postPlace(World world, int i, int j, int k, EntityLiving entityliving) {
        int l = MathHelper.floor((double) (entityliving.yaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0) {
            world.setData(i, j, k, 2);
        }

        if (l == 1) {
            world.setData(i, j, k, 5);
        }

        if (l == 2) {
            world.setData(i, j, k, 3);
        }

        if (l == 3) {
            world.setData(i, j, k, 4);
        }
    }

    public void remove(World world, int i, int j, int k, int l, int i1) {
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser) world.getTileEntity(i, j, k);

        if (tileentitydispenser != null) {
            for (int j1 = 0; j1 < tileentitydispenser.getSize(); ++j1) {
                ItemStack itemstack = tileentitydispenser.getItem(j1);

                if (itemstack != null) {
                    float f = this.b.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.b.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.b.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.count > 0) {
                        int k1 = this.b.nextInt(21) + 10;

                        if (k1 > itemstack.count) {
                            k1 = itemstack.count;
                        }

                        itemstack.count -= k1;
                        EntityItem entityitem = new EntityItem(world, (double) ((float) i + f), (double) ((float) j + f1), (double) ((float) k + f2), new ItemStack(itemstack.id, k1, itemstack.getData()));

                        if (itemstack.hasTag()) {
                            entityitem.itemStack.setTag((NBTTagCompound) itemstack.getTag().clone());
                        }

                        float f3 = 0.05F;

                        entityitem.motX = (double) ((float) this.b.nextGaussian() * f3);
                        entityitem.motY = (double) ((float) this.b.nextGaussian() * f3 + 0.2F);
                        entityitem.motZ = (double) ((float) this.b.nextGaussian() * f3);
                        world.addEntity(entityitem);
                    }
                }
            }
        }

        super.remove(world, i, j, k, l, i1);
    }

    public static IPosition a(ISourceBlock isourceblock) {
        EnumFacing enumfacing = EnumFacing.a(isourceblock.h());
        double d0 = isourceblock.getX() + 0.6D * (double) enumfacing.c();
        double d1 = isourceblock.getY();
        double d2 = isourceblock.getZ() + 0.6D * (double) enumfacing.e();

        return new Position(d0, d1, d2);
    }
}
