package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.util.BlockStateListPopulator;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
// CraftBukkit end

public class BlockPumpkin extends BlockDirectional {

    private boolean a;

    protected BlockPumpkin(int i, boolean flag) {
        super(i, Material.PUMPKIN);
        this.b(true);
        this.a = flag;
        this.a(CreativeModeTab.b);
    }

    public void onPlace(World world, int i, int j, int k) {
        super.onPlace(world, i, j, k);
        if (world.getTypeId(i, j - 1, k) == Block.SNOW_BLOCK.id && world.getTypeId(i, j - 2, k) == Block.SNOW_BLOCK.id) {
            if (!world.isStatic) {
                // CraftBukkit start - Use BlockStateListPopulator
                BlockStateListPopulator blockList = new BlockStateListPopulator(world.getWorld());

                blockList.setTypeId(i, j, k, 0);
                blockList.setTypeId(i, j - 1, k, 0);
                blockList.setTypeId(i, j - 2, k, 0);

                EntitySnowman entitysnowman = new EntitySnowman(world);

                entitysnowman.setPositionRotation((double) i + 0.5D, (double) j - 1.95D, (double) k + 0.5D, 0.0F, 0.0F);
                if (world.addEntity(entitysnowman, SpawnReason.BUILD_SNOWMAN)) {
                    blockList.updateList();
                }
                // CraftBukkit end
            }

            for (int l = 0; l < 120; ++l) {
                world.addParticle("snowshovel", (double) i + world.random.nextDouble(), (double) (j - 2) + world.random.nextDouble() * 2.5D, (double) k + world.random.nextDouble(), 0.0D, 0.0D, 0.0D);
            }
        } else if (world.getTypeId(i, j - 1, k) == Block.IRON_BLOCK.id && world.getTypeId(i, j - 2, k) == Block.IRON_BLOCK.id) {
            boolean flag = world.getTypeId(i - 1, j - 1, k) == Block.IRON_BLOCK.id && world.getTypeId(i + 1, j - 1, k) == Block.IRON_BLOCK.id;
            boolean flag1 = world.getTypeId(i, j - 1, k - 1) == Block.IRON_BLOCK.id && world.getTypeId(i, j - 1, k + 1) == Block.IRON_BLOCK.id;

            if (flag || flag1) {
                // CraftBukkit start - Use BlockStateListPopulator
                BlockStateListPopulator blockList = new BlockStateListPopulator(world.getWorld());

                blockList.setTypeId(i, j, k, 0);
                blockList.setTypeId(i, j - 1, k, 0);
                blockList.setTypeId(i, j - 2, k, 0);

                if (flag) {
                    blockList.setTypeId(i - 1, j - 1, k, 0);
                    blockList.setTypeId(i + 1, j - 1, k, 0);
                } else {
                    blockList.setTypeId(i, j - 1, k - 1, 0);
                    blockList.setTypeId(i, j - 1, k + 1, 0);
                }

                EntityIronGolem entityirongolem = new EntityIronGolem(world);

                entityirongolem.setPlayerCreated(true);
                entityirongolem.setPositionRotation((double) i + 0.5D, (double) j - 1.95D, (double) k + 0.5D, 0.0F, 0.0F);
                if (world.addEntity(entityirongolem, SpawnReason.BUILD_IRONGOLEM)) {
                    for (int i1 = 0; i1 < 120; ++i1) {
                        world.addParticle("snowballpoof", (double) i + world.random.nextDouble(), (double) (j - 2) + world.random.nextDouble() * 3.9D, (double) k + world.random.nextDouble(), 0.0D, 0.0D, 0.0D);
                    }

                    blockList.updateList();
                }
                // CraftBukkit end
            }
        }
    }

    public boolean canPlace(World world, int i, int j, int k) {
        int l = world.getTypeId(i, j, k);

        return (l == 0 || Block.byId[l].material.isReplaceable()) && world.w(i, j - 1, k);
    }

    public void postPlace(World world, int i, int j, int k, EntityLiving entityliving, ItemStack itemstack) {
        int l = MathHelper.floor((double) (entityliving.yaw * 4.0F / 360.0F) + 2.5D) & 3;

        world.setData(i, j, k, l, 2);
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
