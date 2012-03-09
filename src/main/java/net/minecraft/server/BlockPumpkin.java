package net.minecraft.server;

// CraftBukkit start
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
// CraftBukkit end

public class BlockPumpkin extends BlockDirectional {

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
        if (world.suppressPhysics) return; // CraftBukkit
        if (world.getTypeId(i, j - 1, k) == Block.SNOW_BLOCK.id && world.getTypeId(i, j - 2, k) == Block.SNOW_BLOCK.id) {
            if (!world.isStatic && world.getServer().getServer().spawnAnimals) { // CraftBukkit - make snowmen obey spawning rules
                world.setRawTypeId(i, j, k, 0);
                world.setRawTypeId(i, j - 1, k, 0);
                world.setRawTypeId(i, j - 2, k, 0);
                EntitySnowman entitysnowman = new EntitySnowman(world);

                entitysnowman.setPositionRotation((double) i + 0.5D, (double) j - 1.95D, (double) k + 0.5D, 0.0F, 0.0F);
                world.addEntity(entitysnowman, SpawnReason.BUILD_SNOWMAN); // CraftBukkit
                world.update(i, j, k, 0);
                world.update(i, j - 1, k, 0);
                world.update(i, j - 2, k, 0);
            }

            for (int l = 0; l < 120; ++l) {
                world.a("snowshovel", (double) i + world.random.nextDouble(), (double) (j - 2) + world.random.nextDouble() * 2.5D, (double) k + world.random.nextDouble(), 0.0D, 0.0D, 0.0D);
            }
        } else if (world.getTypeId(i, j - 1, k) == Block.IRON_BLOCK.id && world.getTypeId(i, j - 2, k) == Block.IRON_BLOCK.id) {
            boolean flag = world.getTypeId(i - 1, j - 1, k) == Block.IRON_BLOCK.id && world.getTypeId(i + 1, j - 1, k) == Block.IRON_BLOCK.id;
            boolean flag1 = world.getTypeId(i, j - 1, k - 1) == Block.IRON_BLOCK.id && world.getTypeId(i, j - 1, k + 1) == Block.IRON_BLOCK.id;

            if (flag || flag1) {
                world.setRawTypeId(i, j, k, 0);
                world.setRawTypeId(i, j - 1, k, 0);
                world.setRawTypeId(i, j - 2, k, 0);
                if (flag) {
                    world.setRawTypeId(i - 1, j - 1, k, 0);
                    world.setRawTypeId(i + 1, j - 1, k, 0);
                } else {
                    world.setRawTypeId(i, j - 1, k - 1, 0);
                    world.setRawTypeId(i, j - 1, k + 1, 0);
                }

                EntityIronGolem entityirongolem = new EntityIronGolem(world);

                entityirongolem.b(true);
                entityirongolem.setPositionRotation((double) i + 0.5D, (double) j - 1.95D, (double) k + 0.5D, 0.0F, 0.0F);
                world.addEntity(entityirongolem, SpawnReason.BUILD_IRONGOLEM);

                for (int i1 = 0; i1 < 120; ++i1) {
                    world.a("snowballpoof", (double) i + world.random.nextDouble(), (double) (j - 2) + world.random.nextDouble() * 3.9D, (double) k + world.random.nextDouble(), 0.0D, 0.0D, 0.0D);
                }

                world.update(i, j, k, 0);
                world.update(i, j - 1, k, 0);
                world.update(i, j - 2, k, 0);
                if (flag) {
                    world.update(i - 1, j - 1, k, 0);
                    world.update(i + 1, j - 1, k, 0);
                } else {
                    world.update(i, j - 1, k - 1, 0);
                    world.update(i, j - 1, k + 1, 0);
                }
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
