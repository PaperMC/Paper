package net.minecraft.server;

import java.util.Random;

public class BlockObserver extends BlockDirectional {

    public static final BlockStateBoolean b = BlockProperties.w;

    public BlockObserver(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockObserver.FACING, EnumDirection.SOUTH)).set(BlockObserver.b, false));
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockObserver.FACING, BlockObserver.b);
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        return (IBlockData) iblockdata.set(BlockObserver.FACING, enumblockrotation.a((EnumDirection) iblockdata.get(BlockObserver.FACING)));
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        return iblockdata.a(enumblockmirror.a((EnumDirection) iblockdata.get(BlockObserver.FACING)));
    }

    @Override
    public void tickAlways(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        if ((Boolean) iblockdata.get(BlockObserver.b)) {
            worldserver.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockObserver.b, false), 2);
        } else {
            worldserver.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockObserver.b, true), 2);
            worldserver.getBlockTickList().a(blockposition, this, 2);
        }

        this.a((World) worldserver, blockposition, iblockdata);
    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        if (iblockdata.get(BlockObserver.FACING) == enumdirection && !(Boolean) iblockdata.get(BlockObserver.b)) {
            this.a(generatoraccess, blockposition);
        }

        return super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
    }

    private void a(GeneratorAccess generatoraccess, BlockPosition blockposition) {
        if (!generatoraccess.s_() && !generatoraccess.getBlockTickList().a(blockposition, this)) {
            generatoraccess.getBlockTickList().a(blockposition, this, 2);
        }

    }

    protected void a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        EnumDirection enumdirection = (EnumDirection) iblockdata.get(BlockObserver.FACING);
        BlockPosition blockposition1 = blockposition.shift(enumdirection.opposite());

        world.a(blockposition1, (Block) this, blockposition);
        world.a(blockposition1, (Block) this, enumdirection);
    }

    @Override
    public boolean isPowerSource(IBlockData iblockdata) {
        return true;
    }

    @Override
    public int b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return iblockdata.b(iblockaccess, blockposition, enumdirection);
    }

    @Override
    public int a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return (Boolean) iblockdata.get(BlockObserver.b) && iblockdata.get(BlockObserver.FACING) == enumdirection ? 15 : 0;
    }

    @Override
    public void onPlace(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if (!iblockdata.a(iblockdata1.getBlock())) {
            if (!world.s_() && (Boolean) iblockdata.get(BlockObserver.b) && !world.getBlockTickList().a(blockposition, this)) {
                IBlockData iblockdata2 = (IBlockData) iblockdata.set(BlockObserver.b, false);

                world.setTypeAndData(blockposition, iblockdata2, 18);
                this.a(world, blockposition, iblockdata2);
            }

        }
    }

    @Override
    public void remove(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if (!iblockdata.a(iblockdata1.getBlock())) {
            if (!world.isClientSide && (Boolean) iblockdata.get(BlockObserver.b) && world.getBlockTickList().a(blockposition, this)) {
                this.a(world, blockposition, (IBlockData) iblockdata.set(BlockObserver.b, false));
            }

        }
    }

    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        return (IBlockData) this.getBlockData().set(BlockObserver.FACING, blockactioncontext.d().opposite().opposite());
    }
}
