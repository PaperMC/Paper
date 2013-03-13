package net.minecraft.server;

import java.util.List;
import java.util.Random;

import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

public class BlockMinecartDetector extends BlockMinecartTrackAbstract {

    public BlockMinecartDetector(int i) {
        super(i, true);
        this.b(true);
    }

    public int a(World world) {
        return 20;
    }

    public boolean isPowerSource() {
        return true;
    }

    public void a(World world, int i, int j, int k, Entity entity) {
        if (!world.isStatic) {
            int l = world.getData(i, j, k);

            if ((l & 8) == 0) {
                this.d(world, i, j, k, l);
            }
        }
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (!world.isStatic) {
            int l = world.getData(i, j, k);

            if ((l & 8) != 0) {
                this.d(world, i, j, k, l);
            }
        }
    }

    public int b(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return (iblockaccess.getData(i, j, k) & 8) != 0 ? 15 : 0;
    }

    public int c(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return (iblockaccess.getData(i, j, k) & 8) == 0 ? 0 : (l == 1 ? 15 : 0);
    }

    private void d(World world, int i, int j, int k, int l) {
        boolean flag = (l & 8) != 0;
        boolean flag1 = false;
        float f = 0.125F;
        List list = world.a(EntityMinecartAbstract.class, AxisAlignedBB.a().a((double) ((float) i + f), (double) j, (double) ((float) k + f), (double) ((float) (i + 1) - f), (double) ((float) (j + 1) - f), (double) ((float) (k + 1) - f)));

        if (!list.isEmpty()) {
            flag1 = true;
        }

        // CraftBukkit start
        if (flag != flag1) {
            org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, flag ? 15 : 0, flag1 ? 15 : 0);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            flag1 = eventRedstone.getNewCurrent() > 0;
        }
        // CraftBukkit end

        if (flag1 && !flag) {
            world.setData(i, j, k, l | 8, 3);
            world.applyPhysics(i, j, k, this.id);
            world.applyPhysics(i, j - 1, k, this.id);
            world.g(i, j, k, i, j, k);
        }

        if (!flag1 && flag) {
            world.setData(i, j, k, l & 7, 3);
            world.applyPhysics(i, j, k, this.id);
            world.applyPhysics(i, j - 1, k, this.id);
            world.g(i, j, k, i, j, k);
        }

        if (flag1) {
            world.a(i, j, k, this.id, this.a(world));
        }

        world.m(i, j, k, this.id);
    }

    public void onPlace(World world, int i, int j, int k) {
        super.onPlace(world, i, j, k);
        this.d(world, i, j, k, world.getData(i, j, k));
    }

    public boolean q_() {
        return true;
    }

    public int b_(World world, int i, int j, int k, int l) {
        if ((world.getData(i, j, k) & 8) > 0) {
            float f = 0.125F;
            List list = world.a(EntityMinecartAbstract.class, AxisAlignedBB.a().a((double) ((float) i + f), (double) j, (double) ((float) k + f), (double) ((float) (i + 1) - f), (double) ((float) (j + 1) - f), (double) ((float) (k + 1) - f)), IEntitySelector.b);

            if (list.size() > 0) {
                return Container.b((IInventory) list.get(0));
            }
        }

        return 0;
    }
}
