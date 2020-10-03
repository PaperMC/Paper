package net.minecraft.server;

import java.util.List;
import java.util.Random;

public class BlockGrass extends BlockDirtSnowSpreadable implements IBlockFragilePlantElement {

    public BlockGrass(BlockBase.Info blockbase_info) {
        super(blockbase_info);
    }

    @Override
    public boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, boolean flag) {
        return iblockaccess.getType(blockposition.up()).isAir();
    }

    @Override
    public boolean a(World world, Random random, BlockPosition blockposition, IBlockData iblockdata) {
        return true;
    }

    @Override
    public void a(WorldServer worldserver, Random random, BlockPosition blockposition, IBlockData iblockdata) {
        BlockPosition blockposition1 = blockposition.up();
        IBlockData iblockdata1 = Blocks.GRASS.getBlockData();
        int i = 0;

        while (i < 128) {
            BlockPosition blockposition2 = blockposition1;
            int j = 0;

            while (true) {
                if (j < i / 16) {
                    blockposition2 = blockposition2.b(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                    if (worldserver.getType(blockposition2.down()).a((Block) this) && !worldserver.getType(blockposition2).r(worldserver, blockposition2)) {
                        ++j;
                        continue;
                    }
                } else {
                    IBlockData iblockdata2 = worldserver.getType(blockposition2);

                    if (iblockdata2.a(iblockdata1.getBlock()) && random.nextInt(10) == 0) {
                        ((IBlockFragilePlantElement) iblockdata1.getBlock()).a(worldserver, random, blockposition2, iblockdata2);
                    }

                    if (iblockdata2.isAir()) {
                        label38:
                        {
                            IBlockData iblockdata3;

                            if (random.nextInt(8) == 0) {
                                List<WorldGenFeatureConfigured<?, ?>> list = worldserver.getBiome(blockposition2).e().b();

                                if (list.isEmpty()) {
                                    break label38;
                                }

                                WorldGenFeatureConfigured<?, ?> worldgenfeatureconfigured = (WorldGenFeatureConfigured) list.get(0);
                                WorldGenFlowers worldgenflowers = (WorldGenFlowers) worldgenfeatureconfigured.e;

                                iblockdata3 = worldgenflowers.b(random, blockposition2, worldgenfeatureconfigured.c());
                            } else {
                                iblockdata3 = iblockdata1;
                            }

                            if (iblockdata3.canPlace(worldserver, blockposition2)) {
                                org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockGrowEvent(worldserver, blockposition2, iblockdata3, 3); // CraftBukkit
                            }
                        }
                    }
                }

                ++i;
                break;
            }
        }

    }
}
