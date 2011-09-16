package net.minecraft.server;

import java.util.Random;

public class BlockChest extends BlockContainer {

    private Random a = new Random();

    protected BlockChest(int i) {
        super(i, Material.WOOD);
        this.textureId = 26;
    }

    public boolean a() {
        return false;
    }

    public void a(World world, int i, int j, int k) {
        super.a(world, i, j, k);
        this.b(world, i, j, k);
        int l = world.getTypeId(i, j, k - 1);
        int i1 = world.getTypeId(i, j, k + 1);
        int j1 = world.getTypeId(i - 1, j, k);
        int k1 = world.getTypeId(i + 1, j, k);

        if (l == this.id) {
            this.b(world, i, j, k - 1);
        }

        if (i1 == this.id) {
            this.b(world, i, j, k + 1);
        }

        if (j1 == this.id) {
            this.b(world, i - 1, j, k);
        }

        if (k1 == this.id) {
            this.b(world, i + 1, j, k);
        }
    }

    public void postPlace(World world, int i, int j, int k, EntityLiving entityliving) {
        if (world.getData(i, j, k) != 6) return;
        int l = world.getTypeId(i, j, k - 1);
        int i1 = world.getTypeId(i, j, k + 1);
        int j1 = world.getTypeId(i - 1, j, k);
        int k1 = world.getTypeId(i + 1, j, k);
        byte b0 = 0;
        int l1 = MathHelper.floor((double) (entityliving.yaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l1 == 0) {
            b0 = 2;
        }

        if (l1 == 1) {
            b0 = 5;
        }

        if (l1 == 2) {
            b0 = 3;
        }

        if (l1 == 3) {
            b0 = 4;
        }

        if (l != this.id && i1 != this.id && j1 != this.id && k1 != this.id) {
            world.setData(i, j, k, b0);
        } else {
            if ((l == this.id || i1 == this.id) && (b0 == 4 || b0 == 5)) {
                if (l == this.id) {
                    world.setData(i, j, k - 1, b0);
                } else {
                    world.setData(i, j, k + 1, b0);
                }

                world.setData(i, j, k, b0);
            }

            if ((j1 == this.id || k1 == this.id) && (b0 == 2 || b0 == 3)) {
                if (j1 == this.id) {
                    world.setData(i - 1, j, k, b0);
                } else {
                    world.setData(i + 1, j, k, b0);
                }

                world.setData(i, j, k, b0);
            }
        }
    }

    public void b(World world, int i, int j, int k) {
        if (!world.isStatic) {
            int l = world.getTypeId(i, j, k - 1);
            int i1 = world.getTypeId(i, j, k + 1);
            int j1 = world.getTypeId(i - 1, j, k);
            int k1 = world.getTypeId(i + 1, j, k);
            boolean flag = true;
            int l1;
            int i2;
            boolean flag1;
            byte b0;
            int j2;

            if (l != this.id && i1 != this.id) {
                if (j1 != this.id && k1 != this.id) {
                    b0 = 3;
                    if (Block.o[l] && !Block.o[i1]) {
                        b0 = 3;
                    }

                    if (Block.o[i1] && !Block.o[l]) {
                        b0 = 2;
                    }

                    if (Block.o[j1] && !Block.o[k1]) {
                        b0 = 5;
                    }

                    if (Block.o[k1] && !Block.o[j1]) {
                        b0 = 4;
                    }
                } else {
                    l1 = world.getTypeId(j1 == this.id ? i - 1 : i + 1, j, k - 1);
                    i2 = world.getTypeId(j1 == this.id ? i - 1 : i + 1, j, k + 1);
                    b0 = 3;
                    flag1 = true;
                    if (j1 == this.id) {
                        j2 = world.getData(i - 1, j, k);
                    } else {
                        j2 = world.getData(i + 1, j, k);
                    }

                    if (j2 == 2) {
                        b0 = 2;
                    }

                    if ((Block.o[l] || Block.o[l1]) && !Block.o[i1] && !Block.o[i2]) {
                        b0 = 3;
                    }

                    if ((Block.o[i1] || Block.o[i2]) && !Block.o[l] && !Block.o[l1]) {
                        b0 = 2;
                    }
                }
            } else {
                l1 = world.getTypeId(i - 1, j, l == this.id ? k - 1 : k + 1);
                i2 = world.getTypeId(i + 1, j, l == this.id ? k - 1 : k + 1);
                b0 = 5;
                flag1 = true;
                if (l == this.id) {
                    j2 = world.getData(i, j, k - 1);
                } else {
                    j2 = world.getData(i, j, k + 1);
                }

                if (j2 == 4) {
                    b0 = 4;
                }

                if ((Block.o[j1] || Block.o[l1]) && !Block.o[k1] && !Block.o[i2]) {
                    b0 = 5;
                }

                if ((Block.o[k1] || Block.o[i2]) && !Block.o[j1] && !Block.o[l1]) {
                    b0 = 4;
                }
            }

            world.setData(i, j, k, b0);
        }
    }

    public int a(int i) {
        return i == 1 ? this.textureId - 1 : (i == 0 ? this.textureId - 1 : (i == 3 ? this.textureId + 1 : this.textureId));
    }

    public boolean canPlace(World world, int i, int j, int k) {
        int l = 0;

        if (world.getTypeId(i - 1, j, k) == this.id) {
            ++l;
        }

        if (world.getTypeId(i + 1, j, k) == this.id) {
            ++l;
        }

        if (world.getTypeId(i, j, k - 1) == this.id) {
            ++l;
        }

        if (world.getTypeId(i, j, k + 1) == this.id) {
            ++l;
        }

        return l > 1 ? false : (this.g(world, i - 1, j, k) ? false : (this.g(world, i + 1, j, k) ? false : (this.g(world, i, j, k - 1) ? false : !this.g(world, i, j, k + 1))));
    }

    private boolean g(World world, int i, int j, int k) {
        return world.getTypeId(i, j, k) != this.id ? false : (world.getTypeId(i - 1, j, k) == this.id ? true : (world.getTypeId(i + 1, j, k) == this.id ? true : (world.getTypeId(i, j, k - 1) == this.id ? true : world.getTypeId(i, j, k + 1) == this.id)));
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        super.doPhysics(world, i, j, k, l);
        TileEntityChest tileentitychest = (TileEntityChest) world.getTileEntity(i, j, k);

        if (tileentitychest != null) {
            tileentitychest.g();
        }
    }

    public void remove(World world, int i, int j, int k) {
        TileEntityChest tileentitychest = (TileEntityChest) world.getTileEntity(i, j, k);

        if (tileentitychest != null) {
            for (int l = 0; l < tileentitychest.getSize(); ++l) {
                ItemStack itemstack = tileentitychest.getItem(l);

                if (itemstack != null) {
                    float f = this.a.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.a.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.a.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.count > 0) {
                        int i1 = this.a.nextInt(21) + 10;

                        if (i1 > itemstack.count) {
                            i1 = itemstack.count;
                        }

                        itemstack.count -= i1;
                        EntityItem entityitem = new EntityItem(world, (double) ((float) i + f), (double) ((float) j + f1), (double) ((float) k + f2), new ItemStack(itemstack.id, i1, itemstack.getData()));
                        float f3 = 0.05F;

                        entityitem.motX = (double) ((float) this.a.nextGaussian() * f3);
                        entityitem.motY = (double) ((float) this.a.nextGaussian() * f3 + 0.2F);
                        entityitem.motZ = (double) ((float) this.a.nextGaussian() * f3);
                        world.addEntity(entityitem);
                    }
                }
            }
        }

        super.remove(world, i, j, k);
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman) {
        b(world, i, j, k);
        Object object = (TileEntityChest) world.getTileEntity(i, j, k);

        if (object == null) {
            return true;
        } else if (world.e(i, j + 1, k)) {
            return true;
        } else if (world.getTypeId(i - 1, j, k) == this.id && world.e(i - 1, j + 1, k)) {
            return true;
        } else if (world.getTypeId(i + 1, j, k) == this.id && world.e(i + 1, j + 1, k)) {
            return true;
        } else if (world.getTypeId(i, j, k - 1) == this.id && world.e(i, j + 1, k - 1)) {
            return true;
        } else if (world.getTypeId(i, j, k + 1) == this.id && world.e(i, j + 1, k + 1)) {
            return true;
        } else {
            if (world.getTypeId(i - 1, j, k) == this.id) {
                object = new InventoryLargeChest("Large chest", (TileEntityChest) world.getTileEntity(i - 1, j, k), (IInventory) object);
            }

            if (world.getTypeId(i + 1, j, k) == this.id) {
                object = new InventoryLargeChest("Large chest", (IInventory) object, (TileEntityChest) world.getTileEntity(i + 1, j, k));
            }

            if (world.getTypeId(i, j, k - 1) == this.id) {
                object = new InventoryLargeChest("Large chest", (TileEntityChest) world.getTileEntity(i, j, k - 1), (IInventory) object);
            }

            if (world.getTypeId(i, j, k + 1) == this.id) {
                object = new InventoryLargeChest("Large chest", (IInventory) object, (TileEntityChest) world.getTileEntity(i, j, k + 1));
            }

            if (world.isStatic) {
                return true;
            } else {
                entityhuman.a((IInventory) object);
                return true;
            }
        }
    }

    public TileEntity a_() {
        return new TileEntityChest();
    }
}
