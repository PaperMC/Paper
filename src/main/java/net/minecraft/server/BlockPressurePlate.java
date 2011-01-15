package net.minecraft.server;

import java.util.List;
import java.util.Random;

// CraftBukkit start
import org.bukkit.block.BlockFace;import org.bukkit.craftbukkit.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockInteractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
// CraftBukkit end

public class BlockPressurePlate extends Block {

    private EnumMobType a;

    protected BlockPressurePlate(int i, int j, EnumMobType enummobtype) {
        super(i, j, Material.d);
        a = enummobtype;
        a(true);
        float f = 0.0625F;

        a(f, 0.0F, f, 1.0F - f, 0.03125F, 1.0F - f);
    }

    public int b() {
        return 20;
    }

    public AxisAlignedBB d(World world, int i, int j, int k) {
        return null;
    }

    public boolean a() {
        return false;
    }

    public boolean a(World world, int i, int j, int k) {
        return world.d(i, j - 1, k);
    }

    public void e(World world, int i, int j, int k) {}

    public void b(World world, int i, int j, int k, int l) {
        boolean flag = false;

        if (!world.d(i, j - 1, k)) {
            flag = true;
        }
        if (flag) {
            a_(world, i, j, k, world.b(i, j, k));
            world.e(i, j, k, 0);
        }
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (world.z) {
            return;
        }
        if (world.b(i, j, k) == 0) {
            return;
        } else {
            g(world, i, j, k);
            return;
        }
    }

    public void a(World world, int i, int j, int k, Entity entity) {
        if (world.z) {
            return;
        }
        if (world.b(i, j, k) == 1) {
            return;
        } else {
            // CraftBukkit start - Interact Pressure Plate
            if (entity instanceof EntityLiving) {
                CraftBlock block = (CraftBlock) ((WorldServer) world).getWorld().getBlockAt(i, j, k);
                CraftLivingEntity craftEntity = null;

                if (entity instanceof EntityPlayerMP) {
                    craftEntity = new CraftPlayer(((WorldServer) world).getServer(), (EntityPlayerMP) entity);
                } else {
                    craftEntity = new CraftLivingEntity(((WorldServer) world).getServer(), (EntityLiving) entity);
                }
                BlockInteractEvent bie = new BlockInteractEvent(Type.BLOCK_INTERACT, block, craftEntity);

                ((WorldServer) world).getServer().getPluginManager().callEvent(bie);

                if (bie.isCancelled()) {
                    return;
                }
            }
            // CraftBukkit end

            g(world, i, j, k);
            return;
        }
    }

    private void g(World world, int i, int j, int k) {
        boolean flag = world.b(i, j, k) == 1;
        boolean flag1 = false;
        float f = 0.125F;
        List list = null;

        if (a == EnumMobType.a) {
            list = world.b(((Entity) (null)), AxisAlignedBB.b((float) i + f, j, (float) k + f, (float) (i + 1) - f, (double) j + 0.25D, (float) (k + 1) - f));
        }
        if (a == EnumMobType.b) {
            list = world.a(net.minecraft.server.EntityLiving.class, AxisAlignedBB.b((float) i + f, j, (float) k + f, (float) (i + 1) - f, (double) j + 0.25D, (float) (k + 1) - f));
        }
        if (a == EnumMobType.c) {
            list = world.a(net.minecraft.server.EntityPlayer.class, AxisAlignedBB.b((float) i + f, j, (float) k + f, (float) (i + 1) - f, (double) j + 0.25D, (float) (k + 1) - f));
        }
        if (list.size() > 0) {
            flag1 = true;
        }

        // Craftbukkit start
        CraftBlock block = (CraftBlock) ((WorldServer) world).getWorld().getBlockAt(i, j, k);
        BlockRedstoneEvent bre = new BlockRedstoneEvent(block, BlockFace.SELF, flag ? 15 : 0, flag1 ? 15 : 0);
        ((WorldServer) world).getServer().getPluginManager().callEvent(bre);
        flag1 = bre.getNewCurrent() > 0;
        // Craftbukkit end

        if (flag1 && !flag) {
            world.c(i, j, k, 1);
            world.h(i, j, k, bi);
            world.h(i, j - 1, k, bi);
            world.b(i, j, k, i, j, k);
            world.a((double) i + 0.5D, (double) j + 0.10000000000000001D, (double) k + 0.5D, "random.click", 0.3F, 0.6F);
        }
        if (!flag1 && flag) {
            world.c(i, j, k, 0);
            world.h(i, j, k, bi);
            world.h(i, j - 1, k, bi);
            world.b(i, j, k, i, j, k);
            world.a((double) i + 0.5D, (double) j + 0.10000000000000001D, (double) k + 0.5D, "random.click", 0.3F, 0.5F);
        }
        if (flag1) {
            world.i(i, j, k, bi);
        }
    }

    public void b(World world, int i, int j, int k) {
        int l = world.b(i, j, k);

        if (l > 0) {
            world.h(i, j, k, bi);
            world.h(i, j - 1, k, bi);
        }
        super.b(world, i, j, k);
    }

    public void a(IBlockAccess iblockaccess, int i, int j, int k) {
        boolean flag = iblockaccess.b(i, j, k) == 1;
        float f = 0.0625F;

        if (flag) {
            a(f, 0.0F, f, 1.0F - f, 0.03125F, 1.0F - f);
        } else {
            a(f, 0.0F, f, 1.0F - f, 0.0625F, 1.0F - f);
        }
    }

    public boolean b(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return iblockaccess.b(i, j, k) > 0;
    }

    public boolean d(World world, int i, int j, int k, int l) {
        if (world.b(i, j, k) == 0) {
            return false;
        } else {
            return l == 1;
        }
    }

    public boolean c() {
        return true;
    }
}
