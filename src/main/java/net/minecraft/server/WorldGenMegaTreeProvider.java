package net.minecraft.server;

import java.util.Random;
import javax.annotation.Nullable;

public abstract class WorldGenMegaTreeProvider extends WorldGenTreeProvider {

    public WorldGenMegaTreeProvider() {}

    @Override
    public boolean a(WorldServer worldserver, ChunkGenerator chunkgenerator, BlockPosition blockposition, IBlockData iblockdata, Random random) {
        for (int i = 0; i >= -1; --i) {
            for (int j = 0; j >= -1; --j) {
                if (a(iblockdata, worldserver, blockposition, i, j)) {
                    return this.a(worldserver, chunkgenerator, blockposition, iblockdata, random, i, j);
                }
            }
        }

        return super.a(worldserver, chunkgenerator, blockposition, iblockdata, random);
    }

    @Nullable
    protected abstract WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> a(Random random);

    public boolean a(WorldServer worldserver, ChunkGenerator chunkgenerator, BlockPosition blockposition, IBlockData iblockdata, Random random, int i, int j) {
        WorldGenFeatureConfigured<WorldGenFeatureTreeConfiguration, ?> worldgenfeatureconfigured = this.a(random);

        if (worldgenfeatureconfigured == null) {
            return false;
        } else {
            ((WorldGenFeatureTreeConfiguration) worldgenfeatureconfigured.f).b();
            setTreeType(worldgenfeatureconfigured); // CraftBukkit
            IBlockData iblockdata1 = Blocks.AIR.getBlockData();

            worldserver.setTypeAndData(blockposition.b(i, 0, j), iblockdata1, 4);
            worldserver.setTypeAndData(blockposition.b(i + 1, 0, j), iblockdata1, 4);
            worldserver.setTypeAndData(blockposition.b(i, 0, j + 1), iblockdata1, 4);
            worldserver.setTypeAndData(blockposition.b(i + 1, 0, j + 1), iblockdata1, 4);
            if (worldgenfeatureconfigured.a(worldserver, chunkgenerator, random, blockposition.b(i, 0, j))) {
                return true;
            } else {
                worldserver.setTypeAndData(blockposition.b(i, 0, j), iblockdata, 4);
                worldserver.setTypeAndData(blockposition.b(i + 1, 0, j), iblockdata, 4);
                worldserver.setTypeAndData(blockposition.b(i, 0, j + 1), iblockdata, 4);
                worldserver.setTypeAndData(blockposition.b(i + 1, 0, j + 1), iblockdata, 4);
                return false;
            }
        }
    }

    public static boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, int i, int j) {
        Block block = iblockdata.getBlock();

        return block == iblockaccess.getType(blockposition.b(i, 0, j)).getBlock() && block == iblockaccess.getType(blockposition.b(i + 1, 0, j)).getBlock() && block == iblockaccess.getType(blockposition.b(i, 0, j + 1)).getBlock() && block == iblockaccess.getType(blockposition.b(i + 1, 0, j + 1)).getBlock();
    }
}
