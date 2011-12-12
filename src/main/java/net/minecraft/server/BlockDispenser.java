package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.util.Vector;
// CraftBukkit end

public class BlockDispenser extends BlockContainer {

    private Random a = new Random();

    protected BlockDispenser(int i) {
        super(i, Material.STONE);
        this.textureId = 45;
    }

    public int d() {
        return 4;
    }

    public int getDropType(int i, Random random, int j) {
        return Block.DISPENSER.id;
    }

    public void onPlace(World world, int i, int j, int k) {
        super.onPlace(world, i, j, k);
        this.g(world, i, j, k);
    }

    private void g(World world, int i, int j, int k) {
        if (!world.isStatic) {
            int l = world.getTypeId(i, j, k - 1);
            int i1 = world.getTypeId(i, j, k + 1);
            int j1 = world.getTypeId(i - 1, j, k);
            int k1 = world.getTypeId(i + 1, j, k);
            byte b0 = 3;

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

            world.setData(i, j, k, b0);
        }
    }

    public int a(int i) {
        return i == 1 ? this.textureId + 17 : (i == 0 ? this.textureId + 17 : (i == 3 ? this.textureId + 1 : this.textureId));
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman) {
        if (world.isStatic) {
            return true;
        } else {
            TileEntityDispenser tileentitydispenser = (TileEntityDispenser) world.getTileEntity(i, j, k);

            if (tileentitydispenser != null) {
                entityhuman.a(tileentitydispenser);
            }

            return true;
        }
    }

    // CraftBukkit - priv to public
    public void dispense(World world, int i, int j, int k, Random random) {
        int l = world.getData(i, j, k);
        byte b0 = 0;
        byte b1 = 0;

        if (l == 3) {
            b1 = 1;
        } else if (l == 2) {
            b1 = -1;
        } else if (l == 5) {
            b0 = 1;
        } else {
            b0 = -1;
        }

        TileEntityDispenser tileentitydispenser = (TileEntityDispenser) world.getTileEntity(i, j, k);

        if (tileentitydispenser != null) {
            // CraftBukkit start
            int dispenseSlot = tileentitydispenser.findDispenseSlot();
            ItemStack itemstack = null;
            if (dispenseSlot > -1) {
                itemstack = tileentitydispenser.getContents()[dispenseSlot];

                // Copy item stack, because we want it to have 1 item
                itemstack = new ItemStack(itemstack.id, 1, itemstack.getData(), itemstack.getEnchantments());
            }
            // CraftBukkit end

            double d0 = (double) i + (double) b0 * 0.6D + 0.5D;
            double d1 = (double) j + 0.5D;
            double d2 = (double) k + (double) b1 * 0.6D + 0.5D;

            if (itemstack == null) {
                world.f(1001, i, j, k, 0);
            } else {
                // CraftBukkit start
                double d3 = random.nextDouble() * 0.1D + 0.2D;
                double motX = (double) b0 * d3;
                double motY = 0.20000000298023224D;
                double motZ = (double) b1 * d3;
                motX += random.nextGaussian() * 0.007499999832361937D * 6.0D;
                motY += random.nextGaussian() * 0.007499999832361937D * 6.0D;
                motZ += random.nextGaussian() * 0.007499999832361937D * 6.0D;

                org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
                org.bukkit.inventory.ItemStack bukkitItem = new CraftItemStack(itemstack).clone();

                BlockDispenseEvent event = new BlockDispenseEvent(block, bukkitItem, new Vector(motX, motY, motZ));
                world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }

                // Actually remove the item
                tileentitydispenser.splitStack(dispenseSlot, 1);

                motX = event.getVelocity().getX();
                motY = event.getVelocity().getY();
                motZ = event.getVelocity().getZ();

                itemstack = CraftItemStack.createNMSItemStack(event.getItem());
                // CraftBukkit end

                if (itemstack.id == Item.ARROW.id) {
                    EntityArrow entityarrow = new EntityArrow(world, d0, d1, d2);

                    entityarrow.a((double) b0, 0.10000000149011612D, (double) b1, 1.1F, 6.0F);
                    entityarrow.fromPlayer = true;
                    world.addEntity(entityarrow);
                    world.f(1002, i, j, k, 0);
                } else if (itemstack.id == Item.EGG.id) {
                    EntityEgg entityegg = new EntityEgg(world, d0, d1, d2);

                    entityegg.a((double) b0, 0.10000000149011612D, (double) b1, 1.1F, 6.0F);
                    world.addEntity(entityegg);
                    world.f(1002, i, j, k, 0);
                } else if (itemstack.id == Item.SNOW_BALL.id) {
                    EntitySnowball entitysnowball = new EntitySnowball(world, d0, d1, d2);

                    entitysnowball.a((double) b0, 0.10000000149011612D, (double) b1, 1.1F, 6.0F);
                    world.addEntity(entitysnowball);
                    world.f(1002, i, j, k, 0);
                } else if (itemstack.id == Item.POTION.id && ItemPotion.c(itemstack.getData())) {
                    EntityPotion entitypotion = new EntityPotion(world, d0, d1, d2, itemstack.getData());

                    entitypotion.a((double) b0, 0.10000000149011612D, (double) b1, 1.375F, 3.0F);
                    world.addEntity(entitypotion);
                    world.f(1002, i, j, k, 0);
                } else {
                    EntityItem entityitem = new EntityItem(world, d0, d1 - 0.3D, d2, itemstack);
                    // CraftBukkit start
                    // double d3 = random.nextDouble() * 0.1D + 0.2D; // Moved up
                    entityitem.motX = motX;
                    entityitem.motY = motY;
                    entityitem.motZ = motZ;
                    // CraftBukkit end
                    world.addEntity(entityitem);
                    world.f(1000, i, j, k, 0);
                }

                world.f(2000, i, j, k, b0 + 1 + (b1 + 1) * 3);
            }
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (l > 0 && Block.byId[l].isPowerSource()) {
            boolean flag = world.isBlockIndirectlyPowered(i, j, k) || world.isBlockIndirectlyPowered(i, j + 1, k);

            if (flag) {
                world.c(i, j, k, this.id, this.d());
            }
        }
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (!world.isStatic && (world.isBlockIndirectlyPowered(i, j, k) || world.isBlockIndirectlyPowered(i, j + 1, k))) {
            this.dispense(world, i, j, k, random);
        }
    }

    public TileEntity a_() {
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

    public void remove(World world, int i, int j, int k) {
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser) world.getTileEntity(i, j, k);

        if (tileentitydispenser != null) {
            for (int l = 0; l < tileentitydispenser.getSize(); ++l) {
                ItemStack itemstack = tileentitydispenser.getItem(l);

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
                        EntityItem entityitem = new EntityItem(world, (double) ((float) i + f), (double) ((float) j + f1), (double) ((float) k + f2), new ItemStack(itemstack.id, i1, itemstack.getData(), itemstack.getEnchantments())); // CraftBukkit - make sure enchantments are copied over
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
}
