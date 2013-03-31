package net.minecraft.server;

import java.util.List;
import java.util.Random;

public class BlockHopper extends BlockContainer {

    private final Random a = new Random();

    public BlockHopper(int i) {
        super(i, Material.ORE);
        this.a(CreativeModeTab.d);
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void a(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List list, Entity entity) {
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        super.a(world, i, j, k, axisalignedbb, list, entity);
        float f = 0.125F;

        this.a(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        super.a(world, i, j, k, axisalignedbb, list, entity);
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        super.a(world, i, j, k, axisalignedbb, list, entity);
        this.a(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.a(world, i, j, k, axisalignedbb, list, entity);
        this.a(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        super.a(world, i, j, k, axisalignedbb, list, entity);
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public int getPlacedData(World world, int i, int j, int k, int l, float f, float f1, float f2, int i1) {
        int j1 = Facing.OPPOSITE_FACING[l];

        if (j1 == 1) {
            j1 = 0;
        }

        return j1;
    }

    public TileEntity b(World world) {
        return new TileEntityHopper();
    }

    public void postPlace(World world, int i, int j, int k, EntityLiving entityliving, ItemStack itemstack) {
        super.postPlace(world, i, j, k, entityliving, itemstack);
        if (itemstack.hasName()) {
            TileEntityHopper tileentityhopper = d(world, i, j, k);

            tileentityhopper.a(itemstack.getName());
        }
    }

    public void onPlace(World world, int i, int j, int k) {
        super.onPlace(world, i, j, k);
        this.k(world, i, j, k);
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman, int l, float f, float f1, float f2) {
        if (world.isStatic) {
            return true;
        } else {
            TileEntityHopper tileentityhopper = d(world, i, j, k);

            if (tileentityhopper != null) {
                entityhuman.openHopper(tileentityhopper);
            }

            return true;
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        this.k(world, i, j, k);
    }

    private void k(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);
        int i1 = c(l);
        boolean flag = !world.isBlockIndirectlyPowered(i, j, k);
        boolean flag1 = d(l);

        if (flag != flag1) {
            world.setData(i, j, k, i1 | (flag ? 0 : 8), 4);
        }
    }

    public void remove(World world, int i, int j, int k, int l, int i1) {
        TileEntityHopper tileentityhopper = (TileEntityHopper) world.getTileEntity(i, j, k);

        if (tileentityhopper != null) {
            for (int j1 = 0; j1 < tileentityhopper.getSize(); ++j1) {
                ItemStack itemstack = tileentityhopper.getItem(j1);

                if (itemstack != null) {
                    float f = this.a.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.a.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.a.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.count > 0) {
                        int k1 = this.a.nextInt(21) + 10;

                        if (k1 > itemstack.count) {
                            k1 = itemstack.count;
                        }

                        itemstack.count -= k1;
                        EntityItem entityitem = new EntityItem(world, (double) ((float) i + f), (double) ((float) j + f1), (double) ((float) k + f2), new ItemStack(itemstack.id, k1, itemstack.getData()));

                        if (itemstack.hasTag()) {
                            entityitem.getItemStack().setTag((NBTTagCompound) itemstack.getTag().clone());
                        }

                        float f3 = 0.05F;

                        entityitem.motX = (double) ((float) this.a.nextGaussian() * f3);
                        entityitem.motY = (double) ((float) this.a.nextGaussian() * f3 + 0.2F);
                        entityitem.motZ = (double) ((float) this.a.nextGaussian() * f3);
                        world.addEntity(entityitem);
                    }
                }
            }

            world.m(i, j, k, l);
        }

        super.remove(world, i, j, k, l, i1);
    }

    public int d() {
        return 38;
    }

    public boolean b() {
        return false;
    }

    public boolean c() {
        return false;
    }

    public static int c(int i) {
        return Math.min(i & 7, 5); // CraftBukkit - Fix AIOOBE in callers
    }

    public static boolean d(int i) {
        return (i & 8) != 8;
    }

    public boolean q_() {
        return true;
    }

    public int b_(World world, int i, int j, int k, int l) {
        return Container.b((IInventory) d(world, i, j, k));
    }

    public static TileEntityHopper d(IBlockAccess iblockaccess, int i, int j, int k) {
        return (TileEntityHopper) iblockaccess.getTileEntity(i, j, k);
    }
}
