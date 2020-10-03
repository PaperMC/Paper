package net.minecraft.server;

import java.util.Random;

public abstract class BlockGrowingTop extends BlockGrowingAbstract implements IBlockFragilePlantElement {

    public static final BlockStateInteger d = BlockProperties.ak;
    private final double e;

    protected BlockGrowingTop(BlockBase.Info blockbase_info, EnumDirection enumdirection, VoxelShape voxelshape, boolean flag, double d0) {
        super(blockbase_info, enumdirection, voxelshape, flag);
        this.e = d0;
        this.j((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockGrowingTop.d, 0));
    }

    @Override
    public IBlockData a(GeneratorAccess generatoraccess) {
        return (IBlockData) this.getBlockData().set(BlockGrowingTop.d, generatoraccess.getRandom().nextInt(25));
    }

    @Override
    public boolean isTicking(IBlockData iblockdata) {
        return (Integer) iblockdata.get(BlockGrowingTop.d) < 25;
    }

    @Override
    public void tick(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        if ((Integer) iblockdata.get(BlockGrowingTop.d) < 25 && random.nextDouble() < this.e) {
            BlockPosition blockposition1 = blockposition.shift(this.a);

            if (this.h(worldserver.getType(blockposition1))) {
                worldserver.setTypeUpdate(blockposition1, (IBlockData) iblockdata.a((IBlockState) BlockGrowingTop.d));
            }
        }

    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        if (enumdirection == this.a.opposite() && !iblockdata.canPlace(generatoraccess, blockposition)) {
            generatoraccess.getBlockTickList().a(blockposition, this, 1);
        }

        if (enumdirection == this.a && (iblockdata1.a((Block) this) || iblockdata1.a(this.d()))) {
            return this.d().getBlockData();
        } else {
            if (this.b) {
                generatoraccess.getFluidTickList().a(blockposition, FluidTypes.WATER, FluidTypes.WATER.a((IWorldReader) generatoraccess));
            }

            return super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
        }
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockGrowingTop.d);
    }

    @Override
    public boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, boolean flag) {
        return this.h(iblockaccess.getType(blockposition.shift(this.a)));
    }

    @Override
    public boolean a(World world, Random random, BlockPosition blockposition, IBlockData iblockdata) {
        return true;
    }

    @Override
    public void a(WorldServer worldserver, Random random, BlockPosition blockposition, IBlockData iblockdata) {
        BlockPosition blockposition1 = blockposition.shift(this.a);
        int i = Math.min((Integer) iblockdata.get(BlockGrowingTop.d) + 1, 25);
        int j = this.a(random);

        for (int k = 0; k < j && this.h(worldserver.getType(blockposition1)); ++k) {
            worldserver.setTypeUpdate(blockposition1, (IBlockData) iblockdata.set(BlockGrowingTop.d, i));
            blockposition1 = blockposition1.shift(this.a);
            i = Math.min(i + 1, 25);
        }

    }

    protected abstract int a(Random random);

    protected abstract boolean h(IBlockData iblockdata);

    @Override
    protected BlockGrowingTop c() {
        return this;
    }
}
