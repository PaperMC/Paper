package net.minecraft.server;

import java.util.Random;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockInteractEvent;

public class BlockDispenser extends BlockContainer {

    protected BlockDispenser(int i) {
        super(i, Material.STONE);
        this.textureId = 45;
    }

    public int b() {
        return 4;
    }

    public int a(int i, Random random) {
        return Block.DISPENSER.id;
    }

    public void e(World world, int i, int j, int k) {
        super.e(world, i, j, k);
        this.g(world, i, j, k);
    }

    private void g(World world, int i, int j, int k) {
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

        world.c(i, j, k, b0);
    }

    public int a(int i) {
        return i == 1 ? this.textureId + 17 : (i == 0 ? this.textureId + 17 : (i == 3 ? this.textureId + 1 : this.textureId));
    }

    public boolean a(World world, int i, int j, int k, EntityHuman entityhuman) {
        if (world.isStatic) {
            return true;
        } else {
            // CraftBukkit start - Interact Dispenser
            CraftWorld craftWorld = ((WorldServer) world).getWorld();
            CraftServer server = ((WorldServer) world).getServer();
            Type eventType = Type.BLOCK_INTERACT;
            CraftBlock block = (CraftBlock) craftWorld.getBlockAt(i, j, k);
            LivingEntity who = (entityhuman == null)?null:(LivingEntity)entityhuman.getBukkitEntity();

            BlockInteractEvent bie = new BlockInteractEvent(eventType, block, who);
            server.getPluginManager().callEvent(bie);

            if (bie.isCancelled()) {
                return true;
            }
            // CraftBukkit end
            
            TileEntityDispenser tileentitydispenser = (TileEntityDispenser) world.getTileEntity(i, j, k);

            entityhuman.a(tileentitydispenser);
            return true;
        }
    }

    // Craftbukkit - following method should be public
    public void b(World world, int i, int j, int k, Random random) {
        int l = world.getData(i, j, k);
        float f = 0.0F;
        float f1 = 0.0F;

        if (l == 3) {
            f1 = 1.0F;
        } else if (l == 2) {
            f1 = -1.0F;
        } else if (l == 5) {
            f = 1.0F;
        } else {
            f = -1.0F;
        }

        TileEntityDispenser tileentitydispenser = (TileEntityDispenser) world.getTileEntity(i, j, k);
        ItemStack itemstack = tileentitydispenser.e();
        double d0 = (double) i + (double) f * 0.5D + 0.5D;
        double d1 = (double) j + 0.5D;
        double d2 = (double) k + (double) f1 * 0.5D + 0.5D;

        if (itemstack == null) {
            world.a((double) i, (double) j, (double) k, "random.click", 1.0F, 1.2F);
        } else {
            double d3;

            if (itemstack.id == Item.ARROW.id) {
                EntityArrow entityarrow = new EntityArrow(world, d0, d1, d2);

                entityarrow.a((double) f, 0.10000000149011612D, (double) f1, 1.1F, 6.0F);
                world.a((Entity) entityarrow);
                world.a((double) i, (double) j, (double) k, "random.bow", 1.0F, 1.2F);
            } else if (itemstack.id == Item.EGG.id) {
                EntityEgg entityegg = new EntityEgg(world, d0, d1, d2);

                entityegg.a((double) f, 0.10000000149011612D, (double) f1, 1.1F, 6.0F);
                world.a((Entity) entityegg);
                world.a((double) i, (double) j, (double) k, "random.bow", 1.0F, 1.2F);
            } else if (itemstack.id == Item.SNOW_BALL.id) {
                EntitySnowball entitysnowball = new EntitySnowball(world, d0, d1, d2);

                entitysnowball.a((double) f, 0.10000000149011612D, (double) f1, 1.1F, 6.0F);
                world.a((Entity) entitysnowball);
                world.a((double) i, (double) j, (double) k, "random.bow", 1.0F, 1.2F);
            } else {
                EntityItem entityitem = new EntityItem(world, d0, d1 - 0.3D, d2, itemstack);

                d3 = random.nextDouble() * 0.1D + 0.2D;
                entityitem.motX = (double) f * d3;
                entityitem.motY = 0.20000000298023224D;
                entityitem.motZ = (double) f1 * d3;
                entityitem.motX += random.nextGaussian() * 0.007499999832361937D * 6.0D;
                entityitem.motY += random.nextGaussian() * 0.007499999832361937D * 6.0D;
                entityitem.motZ += random.nextGaussian() * 0.007499999832361937D * 6.0D;
                world.a((Entity) entityitem);
                world.a((double) i, (double) j, (double) k, "random.click", 1.0F, 1.0F);
            }

            for (int i1 = 0; i1 < 10; ++i1) {
                d3 = random.nextDouble() * 0.2D + 0.01D;
                double d4 = d0 + (double) f * 0.01D + (random.nextDouble() - 0.5D) * (double) f1 * 0.5D;
                double d5 = d1 + (random.nextDouble() - 0.5D) * 0.5D;
                double d6 = d2 + (double) f1 * 0.01D + (random.nextDouble() - 0.5D) * (double) f * 0.5D;
                double d7 = (double) f * d3 + random.nextGaussian() * 0.01D;
                double d8 = -0.03D + random.nextGaussian() * 0.01D;
                double d9 = (double) f1 * d3 + random.nextGaussian() * 0.01D;

                world.a("smoke", d4, d5, d6, d7, d8, d9);
            }
        }
    }

    public void b(World world, int i, int j, int k, int l) {
        if (l > 0 && Block.byId[l].c()) {
            boolean flag = world.p(i, j, k) || world.p(i, j + 1, k);

            if (flag) {
                world.i(i, j, k, this.id);
            }
        }
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (world.p(i, j, k) || world.p(i, j + 1, k)) {
            this.b(world, i, j, k, random);
        }
    }

    protected TileEntity a_() {
        return new TileEntityDispenser();
    }

    public void a(World world, int i, int j, int k, EntityLiving entityliving) {
        int l = MathHelper.b((double) (entityliving.yaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0) {
            world.c(i, j, k, 2);
        }

        if (l == 1) {
            world.c(i, j, k, 5);
        }

        if (l == 2) {
            world.c(i, j, k, 3);
        }

        if (l == 3) {
            world.c(i, j, k, 4);
        }
    }
}