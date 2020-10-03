package net.minecraft.server;

import java.util.Iterator;
import java.util.Random;

public class BlockCactus extends Block {

    public static final BlockStateInteger AGE = BlockProperties.aj;
    protected static final VoxelShape b = Block.a(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);
    protected static final VoxelShape c = Block.a(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

    protected BlockCactus(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockCactus.AGE, 0));
    }

    @Override
    public void tickAlways(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        if (!iblockdata.canPlace(worldserver, blockposition)) {
            worldserver.b(blockposition, true);
        }

    }

    @Override
    public void tick(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        BlockPosition blockposition1 = blockposition.up();

        if (worldserver.isEmpty(blockposition1)) {
            int i;

            for (i = 1; worldserver.getType(blockposition.down(i)).a((Block) this); ++i) {
                ;
            }

            if (i < 3) {
                int j = (Integer) iblockdata.get(BlockCactus.AGE);

                if (j == 15) {
                    worldserver.setTypeUpdate(blockposition1, this.getBlockData());
                    IBlockData iblockdata1 = (IBlockData) iblockdata.set(BlockCactus.AGE, 0);

                    worldserver.setTypeAndData(blockposition, iblockdata1, 4);
                    iblockdata1.doPhysics(worldserver, blockposition1, this, blockposition, false);
                } else {
                    worldserver.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockCactus.AGE, j + 1), 4);
                }

            }
        }
    }

    @Override
    public VoxelShape c(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return BlockCactus.b;
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return BlockCactus.c;
    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        if (!iblockdata.canPlace(generatoraccess, blockposition)) {
            generatoraccess.getBlockTickList().a(blockposition, this, 1);
        }

        return super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
    }

    @Override
    public boolean canPlace(IBlockData iblockdata, IWorldReader iworldreader, BlockPosition blockposition) {
        Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

        EnumDirection enumdirection;
        Material material;

        do {
            if (!iterator.hasNext()) {
                IBlockData iblockdata1 = iworldreader.getType(blockposition.down());

                return (iblockdata1.a(Blocks.CACTUS) || iblockdata1.a(Blocks.SAND) || iblockdata1.a(Blocks.RED_SAND)) && !iworldreader.getType(blockposition.up()).getMaterial().isLiquid();
            }

            enumdirection = (EnumDirection) iterator.next();
            IBlockData iblockdata2 = iworldreader.getType(blockposition.shift(enumdirection));

            material = iblockdata2.getMaterial();
        } while (!material.isBuildable() && !iworldreader.getFluid(blockposition.shift(enumdirection)).a((Tag) TagsFluid.LAVA));

        return false;
    }

    @Override
    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Entity entity) {
        entity.damageEntity(DamageSource.CACTUS, 1.0F);
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockCactus.AGE);
    }

    @Override
    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        return false;
    }
}
