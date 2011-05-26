package net.minecraft.server;

import java.util.List;
import java.util.Random;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
// CraftBukkit end

public class BlockPressurePlate extends Block {

    private EnumMobType a;

    protected BlockPressurePlate(int i, int j, EnumMobType enummobtype, Material material) {
        super(i, j, material);
        this.a = enummobtype;
        this.a(true);
        float f = 0.0625F;

        this.a(f, 0.0F, f, 1.0F - f, 0.03125F, 1.0F - f);
    }

    public int c() {
        return 20;
    }

    public AxisAlignedBB d(World world, int i, int j, int k) {
        return null;
    }

    public boolean a() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return world.d(i, j - 1, k);
    }

    public void e(World world, int i, int j, int k) {}

    public void doPhysics(World world, int i, int j, int k, int l) {
        boolean flag = false;

        if (!world.d(i, j - 1, k)) {
            flag = true;
        }

        if (flag) {
            this.b_(world, i, j, k, world.getData(i, j, k));
            world.setTypeId(i, j, k, 0);
        }
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (!world.isStatic) {
            if (world.getData(i, j, k) != 0) {
                this.g(world, i, j, k);
            }
        }
    }

    public void a(World world, int i, int j, int k, Entity entity) {
        if (!world.isStatic) {
            if (world.getData(i, j, k) != 1) {
                this.g(world, i, j, k);
            }
        }
    }

    private void g(World world, int i, int j, int k) {
        boolean flag = world.getData(i, j, k) == 1;
        boolean flag1 = false;
        float f = 0.125F;
        List list = null;

        if (this.a == EnumMobType.EVERYTHING) {
            list = world.b((Entity) null, AxisAlignedBB.b((double) ((float) i + f), (double) j, (double) ((float) k + f), (double) ((float) (i + 1) - f), (double) j + 0.25D, (double) ((float) (k + 1) - f)));
        }

        if (this.a == EnumMobType.MOBS) {
            list = world.a(EntityLiving.class, AxisAlignedBB.b((double) ((float) i + f), (double) j, (double) ((float) k + f), (double) ((float) (i + 1) - f), (double) j + 0.25D, (double) ((float) (k + 1) - f)));
        }

        if (this.a == EnumMobType.PLAYERS) {
            list = world.a(EntityHuman.class, AxisAlignedBB.b((double) ((float) i + f), (double) j, (double) ((float) k + f), (double) ((float) (i + 1) - f), (double) j + 0.25D, (double) ((float) (k + 1) - f)));
        }

        if (list.size() > 0) {
            flag1 = true;
        }

        // CraftBukkit start - Interact Pressure Plate
        if (flag != flag1) {
            if (flag1) {
                for (Object object: list) {
                    if (object != null) {
                        Cancellable cancellable;
                        if (object instanceof EntityHuman) {
                            cancellable = CraftEventFactory.callPlayerInteractEvent((EntityHuman) object, Action.PHYSICAL, i, j, k, -1, null);
                        }
                        else if (object instanceof Entity) {
                            cancellable = new EntityInteractEvent(((Entity) object).getBukkitEntity(), ((WorldServer)world).getWorld().getBlockAt(i, j, k));
                            ((CraftServer)Bukkit.getServer()).getPluginManager().callEvent((EntityInteractEvent) cancellable);
                        }
                        else continue;
                        if (cancellable.isCancelled()) {
                            return;
                        }
                    }
                }
            }

            CraftServer server = ((WorldServer) world).getServer();
            CraftWorld craftWorld = ((WorldServer) world).getWorld();
            CraftBlock block = (CraftBlock) craftWorld.getBlockAt(i, j, k);

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, flag ? 1 : 0, flag1 ? 1 : 0);
            server.getPluginManager().callEvent(eventRedstone);

            flag1 = eventRedstone.getNewCurrent() > 0;
        }
        // CraftBukkit end

        if (flag1 && !flag) {
            world.setData(i, j, k, 1);
            world.applyPhysics(i, j, k, this.id);
            world.applyPhysics(i, j - 1, k, this.id);
            world.b(i, j, k, i, j, k);
            world.makeSound((double) i + 0.5D, (double) j + 0.1D, (double) k + 0.5D, "random.click", 0.3F, 0.6F);
        }

        if (!flag1 && flag) {
            world.setData(i, j, k, 0);
            world.applyPhysics(i, j, k, this.id);
            world.applyPhysics(i, j - 1, k, this.id);
            world.b(i, j, k, i, j, k);
            world.makeSound((double) i + 0.5D, (double) j + 0.1D, (double) k + 0.5D, "random.click", 0.3F, 0.5F);
        }

        if (flag1) {
            world.c(i, j, k, this.id, this.c());
        }
    }

    public void remove(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);

        if (l > 0) {
            world.applyPhysics(i, j, k, this.id);
            world.applyPhysics(i, j - 1, k, this.id);
        }

        super.remove(world, i, j, k);
    }

    public void a(IBlockAccess iblockaccess, int i, int j, int k) {
        boolean flag = iblockaccess.getData(i, j, k) == 1;
        float f = 0.0625F;

        if (flag) {
            this.a(f, 0.0F, f, 1.0F - f, 0.03125F, 1.0F - f);
        } else {
            this.a(f, 0.0F, f, 1.0F - f, 0.0625F, 1.0F - f);
        }
    }

    public boolean a(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return iblockaccess.getData(i, j, k) > 0;
    }

    public boolean c(World world, int i, int j, int k, int l) {
        return world.getData(i, j, k) == 0 ? false : l == 1;
    }

    public boolean isPowerSource() {
        return true;
    }
}
