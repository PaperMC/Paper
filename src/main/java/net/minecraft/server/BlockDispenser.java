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
        super(i, Material.d);
        bh = 45;
    }

    public int b() {
        return 4;
    }

    public int a(int i, Random random) {
        return Block.P.bi;
    }

    public void e(World world, int i, int j, int k) {
        super.e(world, i, j, k);
        g(world, i, j, k);
    }

    private void g(World world, int i, int j, int k) {
        int l = world.a(i, j, k - 1);
        int i1 = world.a(i, j, k + 1);
        int j1 = world.a(i - 1, j, k);
        int k1 = world.a(i + 1, j, k);
        byte byte0 = 3;

        if (Block.o[l] && !Block.o[i1]) {
            byte0 = 3;
        }
        if (Block.o[i1] && !Block.o[l]) {
            byte0 = 2;
        }
        if (Block.o[j1] && !Block.o[k1]) {
            byte0 = 5;
        }
        if (Block.o[k1] && !Block.o[j1]) {
            byte0 = 4;
        }
        world.c(i, j, k, ((int) (byte0)));
    }

    public int a(int i) {
        if (i == 1) {
            return bh + 17;
        }
        if (i == 0) {
            return bh + 17;
        }
        if (i == 3) {
            return bh + 1;
        } else {
            return bh;
        }
    }

    public boolean a(World world, int i, int j, int k, EntityPlayer entityplayer) {
        if (world.z) {
            return true;
        } else {
            // CraftBukkit start - Interact Dispenser
            CraftWorld craftWorld = ((WorldServer) world).getWorld();
            CraftServer server = ((WorldServer) world).getServer();
            Type eventType = Type.BLOCK_INTERACT;
            CraftBlock block = (CraftBlock) craftWorld.getBlockAt(i, j, k);
            LivingEntity who = (entityplayer == null)?null:(LivingEntity)entityplayer.getBukkitEntity();
            
            BlockInteractEvent bie = new BlockInteractEvent(eventType, block, who);
            server.getPluginManager().callEvent(bie);

            if (bie.isCancelled()) {
                return true;
            }
            // CraftBukkit end
        	
            TileEntityDispenser tileentitydispenser = (TileEntityDispenser) world.m(i, j, k);

            entityplayer.a(tileentitydispenser);
            return true;
        }
    }

    private void b(World world, int i, int j, int k, Random random) {
        int l = world.b(i, j, k);
        float f = 0.0F;
        float f1 = 0.0F;

        if (l == 3) {
            f1 = 1.0F;
        } else if (l == 2) {
            f1 = -1F;
        } else if (l == 5) {
            f = 1.0F;
        } else {
            f = -1F;
        }
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser) world.m(i, j, k);
        ItemStack itemstack = tileentitydispenser.e();
        double d = (double) i + (double) f * 0.5D + 0.5D;
        double d1 = (double) j + 0.5D;
        double d2 = (double) k + (double) f1 * 0.5D + 0.5D;

        if (itemstack == null) {
            world.a(i, j, k, "random.click", 1.0F, 1.2F);
        } else {
            if (itemstack.c == Item.j.ba) {
                EntityArrow entityarrow = new EntityArrow(world, d, d1, d2);

                entityarrow.a(f, 0.10000000149011612D, f1, 1.1F, 6F);
                world.a(((Entity) (entityarrow)));
                world.a(i, j, k, "random.bow", 1.0F, 1.2F);
            } else if (itemstack.c == Item.aN.ba) {
                EntityEgg entityegg = new EntityEgg(world, d, d1, d2);

                entityegg.a(f, 0.10000000149011612D, f1, 1.1F, 6F);
                world.a(((Entity) (entityegg)));
                world.a(i, j, k, "random.bow", 1.0F, 1.2F);
            } else if (itemstack.c == Item.aB.ba) {
                EntitySnowball entitysnowball = new EntitySnowball(world, d, d1, d2);

                entitysnowball.a(f, 0.10000000149011612D, f1, 1.1F, 6F);
                world.a(((Entity) (entitysnowball)));
                world.a(i, j, k, "random.bow", 1.0F, 1.2F);
            } else {
                EntityItem entityitem = new EntityItem(world, d, d1 - 0.29999999999999999D, d2, itemstack);
                double d3 = random.nextDouble() * 0.10000000000000001D + 0.20000000000000001D;

                entityitem.s = (double) f * d3;
                entityitem.t = 0.20000000298023224D;
                entityitem.u = (double) f1 * d3;
                entityitem.s += random.nextGaussian() * 0.0074999998323619366D * 6D;
                entityitem.t += random.nextGaussian() * 0.0074999998323619366D * 6D;
                entityitem.u += random.nextGaussian() * 0.0074999998323619366D * 6D;
                world.a(((Entity) (entityitem)));
                world.a(i, j, k, "random.click", 1.0F, 1.0F);
            }
            for (int i1 = 0; i1 < 10; i1++) {
                double d4 = random.nextDouble() * 0.20000000000000001D + 0.01D;
                double d5 = d + (double) f * 0.01D + (random.nextDouble() - 0.5D) * (double) f1 * 0.5D;
                double d6 = d1 + (random.nextDouble() - 0.5D) * 0.5D;
                double d7 = d2 + (double) f1 * 0.01D + (random.nextDouble() - 0.5D) * (double) f * 0.5D;
                double d8 = (double) f * d4 + random.nextGaussian() * 0.01D;
                double d9 = -0.029999999999999999D + random.nextGaussian() * 0.01D;
                double d10 = (double) f1 * d4 + random.nextGaussian() * 0.01D;

                world.a("smoke", d5, d6, d7, d8, d9, d10);
            }
        }
    }

    public void b(World world, int i, int j, int k, int l) {
        if (l > 0 && Block.m[l].c()) {
            boolean flag = world.p(i, j, k) || world.p(i, j + 1, k);

            if (flag) {
                world.i(i, j, k, bi);
            }
        }
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (world.p(i, j, k) || world.p(i, j + 1, k)) {
            b(world, i, j, k, random);
        }
    }

    protected TileEntity a_() {
        return ((TileEntity) (new TileEntityDispenser()));
    }

    public void a(World world, int i, int j, int k, EntityLiving entityliving) {
        int l = MathHelper.b((double) ((entityliving.v * 4F) / 360F) + 0.5D) & 3;

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
