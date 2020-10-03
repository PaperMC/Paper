package net.minecraft.server;

import java.util.Random;

public class BlockIceFrost extends BlockIce {

    public static final BlockStateInteger a = BlockProperties.ag;

    public BlockIceFrost(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockIceFrost.a, 0));
    }

    @Override
    public void tick(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        this.tickAlways(iblockdata, worldserver, blockposition, random);
    }

    @Override
    public void tickAlways(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        if ((random.nextInt(3) == 0 || this.a(worldserver, blockposition, 4)) && worldserver.getLightLevel(blockposition) > 11 - (Integer) iblockdata.get(BlockIceFrost.a) - iblockdata.b((IBlockAccess) worldserver, blockposition) && this.e(iblockdata, (World) worldserver, blockposition)) {
            BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
            EnumDirection[] aenumdirection = EnumDirection.values();
            int i = aenumdirection.length;

            for (int j = 0; j < i; ++j) {
                EnumDirection enumdirection = aenumdirection[j];

                blockposition_mutableblockposition.a((BaseBlockPosition) blockposition, enumdirection);
                IBlockData iblockdata1 = worldserver.getType(blockposition_mutableblockposition);

                if (iblockdata1.a((Block) this) && !this.e(iblockdata1, (World) worldserver, blockposition_mutableblockposition)) {
                    worldserver.getBlockTickList().a(blockposition_mutableblockposition, this, MathHelper.nextInt(random, 20, 40));
                }
            }

        } else {
            worldserver.getBlockTickList().a(blockposition, this, MathHelper.nextInt(random, 20, 40));
        }
    }

    private boolean e(IBlockData iblockdata, World world, BlockPosition blockposition) {
        int i = (Integer) iblockdata.get(BlockIceFrost.a);

        if (i < 3) {
            world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockIceFrost.a, i + 1), 2);
            return false;
        } else {
            this.melt(iblockdata, world, blockposition);
            return true;
        }
    }

    @Override
    public void doPhysics(IBlockData iblockdata, World world, BlockPosition blockposition, Block block, BlockPosition blockposition1, boolean flag) {
        if (block == this && this.a(world, blockposition, 2)) {
            this.melt(iblockdata, world, blockposition);
        }

        super.doPhysics(iblockdata, world, blockposition, block, blockposition1, flag);
    }

    private boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, int i) {
        int j = 0;
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
        EnumDirection[] aenumdirection = EnumDirection.values();
        int k = aenumdirection.length;

        for (int l = 0; l < k; ++l) {
            EnumDirection enumdirection = aenumdirection[l];

            blockposition_mutableblockposition.a((BaseBlockPosition) blockposition, enumdirection);
            if (iblockaccess.getType(blockposition_mutableblockposition).a((Block) this)) {
                ++j;
                if (j >= i) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockIceFrost.a);
    }
}
