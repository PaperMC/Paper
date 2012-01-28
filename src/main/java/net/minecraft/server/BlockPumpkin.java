package net.minecraft.server;

import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

public class BlockPumpkin extends Block {

    private boolean a;

    protected BlockPumpkin(int i, int j, boolean flag) {
        super(i, Material.PUMPKIN);
        this.textureId = j;
        this.a(true);
        this.a = flag;
    }

    public int a(int i, int j) {
        if (i == 1) {
            return this.textureId;
        } else if (i == 0) {
            return this.textureId;
        } else {
            int k = this.textureId + 1 + 16;

            if (this.a) {
                ++k;
            }

            return j == 2 && i == 2 ? k : (j == 3 && i == 5 ? k : (j == 0 && i == 3 ? k : (j == 1 && i == 4 ? k : this.textureId + 16)));
        }
    }

    public int a(int i) {
        return i == 1 ? this.textureId : (i == 0 ? this.textureId : (i == 3 ? this.textureId + 1 + 16 : this.textureId + 16));
    }

    public void onPlace(World world, int i, int j, int k) {
        super.onPlace(world, i, j, k);
        if (world.getTypeId(i, j - 1, k) == Block.SNOW_BLOCK.id && world.getTypeId(i, j - 2, k) == Block.SNOW_BLOCK.id) {
            if (!world.isStatic && world.getServer().getServer().spawnAnimals) { // CraftBukkit - make snowmen obey spawning rules
                world.setTypeId(i, j, k, 0);
                world.setTypeId(i, j - 1, k, 0);
                world.setTypeId(i, j - 2, k, 0);
                EntitySnowman entitysnowman = new EntitySnowman(world);

                entitysnowman.setPositionRotation((double) i + 0.5D, (double) j - 1.95D, (double) k + 0.5D, 0.0F, 0.0F);
                world.addEntity(entitysnowman);
            }

            for (int l = 0; l < 120; ++l) {
                world.a("snowshovel", (double) i + world.random.nextDouble(), (double) (j - 2) + world.random.nextDouble() * 2.5D, (double) k + world.random.nextDouble(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public boolean canPlace(World world, int i, int j, int k) {
        int l = world.getTypeId(i, j, k);

        return (l == 0 || Block.byId[l].material.isReplacable()) && world.e(i, j - 1, k);
    }

    public void postPlace(World world, int i, int j, int k, EntityLiving entityliving) {
        int l = MathHelper.floor((double) (entityliving.yaw * 4.0F / 360.0F) + 2.5D) & 3;

        world.setData(i, j, k, l);
    }

    // CraftBukkit start
    public void doPhysics(World world, int i, int j, int k, int l) {
        if (net.minecraft.server.Block.byId[l] != null && net.minecraft.server.Block.byId[l].isPowerSource()) {
            org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
            int power = block.getBlockPower();

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, power, power);
            world.getServer().getPluginManager().callEvent(eventRedstone);
        }
    }
    // CraftBukkit end
}
