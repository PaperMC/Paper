package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.PluginManager;
// CraftBukkit end

public class BlockRedstoneTorch extends BlockTorch {

    private boolean isOn = false;
    private static List b = new ArrayList();

    public int a(int i, int j) {
        return i == 1 ? Block.REDSTONE_WIRE.a(i, j) : super.a(i, j);
    }

    private boolean a(World world, int i, int j, int k, boolean flag) {
        if (flag) {
            b.add(new RedstoneUpdateInfo(i, j, k, world.getTime()));
        }

        int l = 0;

        for (int i1 = 0; i1 < b.size(); ++i1) {
            RedstoneUpdateInfo redstoneupdateinfo = (RedstoneUpdateInfo) b.get(i1);

            if (redstoneupdateinfo.a == i && redstoneupdateinfo.b == j && redstoneupdateinfo.c == k) {
                ++l;
                if (l >= 8) {
                    return true;
                }
            }
        }

        return false;
    }

    protected BlockRedstoneTorch(int i, int j, boolean flag) {
        super(i, j);
        this.isOn = flag;
        this.a(true);
    }

    public int b() {
        return 2;
    }

    public void e(World world, int i, int j, int k) {
        if (world.getData(i, j, k) == 0) {
            super.e(world, i, j, k);
        }

        if (this.isOn) {
            world.applyPhysics(i, j - 1, k, this.id);
            world.applyPhysics(i, j + 1, k, this.id);
            world.applyPhysics(i - 1, j, k, this.id);
            world.applyPhysics(i + 1, j, k, this.id);
            world.applyPhysics(i, j, k - 1, this.id);
            world.applyPhysics(i, j, k + 1, this.id);
        }
    }

    public void remove(World world, int i, int j, int k) {
        if (this.isOn) {
            world.applyPhysics(i, j - 1, k, this.id);
            world.applyPhysics(i, j + 1, k, this.id);
            world.applyPhysics(i - 1, j, k, this.id);
            world.applyPhysics(i + 1, j, k, this.id);
            world.applyPhysics(i, j, k - 1, this.id);
            world.applyPhysics(i, j, k + 1, this.id);
        }
    }

    public boolean b(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        if (!this.isOn) {
            return false;
        } else {
            int i1 = iblockaccess.getData(i, j, k);

            return i1 == 5 && l == 1 ? false : (i1 == 3 && l == 3 ? false : (i1 == 4 && l == 2 ? false : (i1 == 1 && l == 5 ? false : i1 != 2 || l != 4)));
        }
    }

    private boolean g(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);

        return l == 5 && world.isBlockFaceIndirectlyPowered(i, j - 1, k, 0) ? true : (l == 3 && world.isBlockFaceIndirectlyPowered(i, j, k - 1, 2) ? true : (l == 4 && world.isBlockFaceIndirectlyPowered(i, j, k + 1, 3) ? true : (l == 1 && world.isBlockFaceIndirectlyPowered(i - 1, j, k, 4) ? true : l == 2 && world.isBlockFaceIndirectlyPowered(i + 1, j, k, 5))));
    }

    public void a(World world, int i, int j, int k, Random random) {
        boolean flag = this.g(world, i, j, k);

        while (b.size() > 0 && world.getTime() - ((RedstoneUpdateInfo) b.get(0)).d > 100L) {
            b.remove(0);
        }

        // CraftBukkit start
        CraftBlock block = (CraftBlock) ((WorldServer) world).getWorld().getBlockAt(i, j, k);
        PluginManager man = ((WorldServer) world).getServer().getPluginManager();
        int oldCurrent = this.isOn ? 15 : 0;
        BlockRedstoneEvent event = new BlockRedstoneEvent(block, oldCurrent, oldCurrent);
        // CraftBukkit end

        if (this.isOn) {
            if (flag) {
                // CraftBukkit start
                if (oldCurrent != 0) {
                    event.setNewCurrent(0);
                    man.callEvent(event);
                    if (event.getNewCurrent() != 0) {
                        return;
                    }
                }
                // CraftBukkit end

                world.setTypeIdAndData(i, j, k, Block.REDSTONE_TORCH_OFF.id, world.getData(i, j, k));
                if (this.a(world, i, j, k, true)) {
                    world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), "random.fizz", 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

                    for (int l = 0; l < 5; ++l) {
                        double d0 = (double) i + random.nextDouble() * 0.6D + 0.2D;
                        double d1 = (double) j + random.nextDouble() * 0.6D + 0.2D;
                        double d2 = (double) k + random.nextDouble() * 0.6D + 0.2D;

                        world.a("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
        } else if (!flag && !this.a(world, i, j, k, false)) {
            // CraftBukkit start
            if (oldCurrent != 15) {
                event.setNewCurrent(15);
                man.callEvent(event);
                if (event.getNewCurrent() != 15) {
                    return;
                }
            }
            // CraftBukkit end

            world.setTypeIdAndData(i, j, k, Block.REDSTONE_TORCH_ON.id, world.getData(i, j, k));
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        super.doPhysics(world, i, j, k, l);
        world.c(i, j, k, this.id, this.b());
    }

    public boolean c(World world, int i, int j, int k, int l) {
        return l == 0 ? this.b(world, i, j, k, l) : false;
    }

    public int a(int i, Random random) {
        return Block.REDSTONE_TORCH_ON.id;
    }

    public boolean isPowerSource() {
        return true;
    }
}
