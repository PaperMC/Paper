package net.minecraft.server;

import java.util.Random;

public abstract class BlockDiodeAbstract extends BlockFacingHorizontal {

    protected static final VoxelShape b = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    public static final BlockStateBoolean c = BlockProperties.w;

    protected BlockDiodeAbstract(BlockBase.Info blockbase_info) {
        super(blockbase_info);
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return BlockDiodeAbstract.b;
    }

    @Override
    public boolean canPlace(IBlockData iblockdata, IWorldReader iworldreader, BlockPosition blockposition) {
        return c((IBlockAccess) iworldreader, blockposition.down());
    }

    @Override
    public void tickAlways(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        if (!this.a((IWorldReader) worldserver, blockposition, iblockdata)) {
            boolean flag = (Boolean) iblockdata.get(BlockDiodeAbstract.c);
            boolean flag1 = this.a((World) worldserver, blockposition, iblockdata);

            if (flag && !flag1) {
                worldserver.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockDiodeAbstract.c, false), 2);
            } else if (!flag) {
                worldserver.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockDiodeAbstract.c, true), 2);
                if (!flag1) {
                    worldserver.getBlockTickList().a(blockposition, this, this.g(iblockdata), TickListPriority.VERY_HIGH);
                }
            }

        }
    }

    @Override
    public int b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return iblockdata.b(iblockaccess, blockposition, enumdirection);
    }

    @Override
    public int a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return !(Boolean) iblockdata.get(BlockDiodeAbstract.c) ? 0 : (iblockdata.get(BlockDiodeAbstract.FACING) == enumdirection ? this.b(iblockaccess, blockposition, iblockdata) : 0);
    }

    @Override
    public void doPhysics(IBlockData iblockdata, World world, BlockPosition blockposition, Block block, BlockPosition blockposition1, boolean flag) {
        if (iblockdata.canPlace(world, blockposition)) {
            this.c(world, blockposition, iblockdata);
        } else {
            TileEntity tileentity = this.isTileEntity() ? world.getTileEntity(blockposition) : null;

            a(iblockdata, (GeneratorAccess) world, blockposition, tileentity);
            world.a(blockposition, false);
            EnumDirection[] aenumdirection = EnumDirection.values();
            int i = aenumdirection.length;

            for (int j = 0; j < i; ++j) {
                EnumDirection enumdirection = aenumdirection[j];

                world.applyPhysics(blockposition.shift(enumdirection), this);
            }

        }
    }

    protected void c(World world, BlockPosition blockposition, IBlockData iblockdata) {
        if (!this.a((IWorldReader) world, blockposition, iblockdata)) {
            boolean flag = (Boolean) iblockdata.get(BlockDiodeAbstract.c);
            boolean flag1 = this.a(world, blockposition, iblockdata);

            if (flag != flag1 && !world.getBlockTickList().b(blockposition, this)) {
                TickListPriority ticklistpriority = TickListPriority.HIGH;

                if (this.c((IBlockAccess) world, blockposition, iblockdata)) {
                    ticklistpriority = TickListPriority.EXTREMELY_HIGH;
                } else if (flag) {
                    ticklistpriority = TickListPriority.VERY_HIGH;
                }

                world.getBlockTickList().a(blockposition, this, this.g(iblockdata), ticklistpriority);
            }

        }
    }

    public boolean a(IWorldReader iworldreader, BlockPosition blockposition, IBlockData iblockdata) {
        return false;
    }

    protected boolean a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        return this.b(world, blockposition, iblockdata) > 0;
    }

    protected int b(World world, BlockPosition blockposition, IBlockData iblockdata) {
        EnumDirection enumdirection = (EnumDirection) iblockdata.get(BlockDiodeAbstract.FACING);
        BlockPosition blockposition1 = blockposition.shift(enumdirection);
        int i = world.getBlockFacePower(blockposition1, enumdirection);

        if (i >= 15) {
            return i;
        } else {
            IBlockData iblockdata1 = world.getType(blockposition1);

            return Math.max(i, iblockdata1.a(Blocks.REDSTONE_WIRE) ? (Integer) iblockdata1.get(BlockRedstoneWire.POWER) : 0);
        }
    }

    protected int b(IWorldReader iworldreader, BlockPosition blockposition, IBlockData iblockdata) {
        EnumDirection enumdirection = (EnumDirection) iblockdata.get(BlockDiodeAbstract.FACING);
        EnumDirection enumdirection1 = enumdirection.g();
        EnumDirection enumdirection2 = enumdirection.h();

        return Math.max(this.b(iworldreader, blockposition.shift(enumdirection1), enumdirection1), this.b(iworldreader, blockposition.shift(enumdirection2), enumdirection2));
    }

    protected int b(IWorldReader iworldreader, BlockPosition blockposition, EnumDirection enumdirection) {
        IBlockData iblockdata = iworldreader.getType(blockposition);

        return this.h(iblockdata) ? (iblockdata.a(Blocks.REDSTONE_BLOCK) ? 15 : (iblockdata.a(Blocks.REDSTONE_WIRE) ? (Integer) iblockdata.get(BlockRedstoneWire.POWER) : iworldreader.c(blockposition, enumdirection))) : 0;
    }

    @Override
    public boolean isPowerSource(IBlockData iblockdata) {
        return true;
    }

    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        return (IBlockData) this.getBlockData().set(BlockDiodeAbstract.FACING, blockactioncontext.f().opposite());
    }

    @Override
    public void postPlace(World world, BlockPosition blockposition, IBlockData iblockdata, EntityLiving entityliving, ItemStack itemstack) {
        if (this.a(world, blockposition, iblockdata)) {
            world.getBlockTickList().a(blockposition, this, 1);
        }

    }

    @Override
    public void onPlace(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        this.d(world, blockposition, iblockdata);
    }

    @Override
    public void remove(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if (!flag && !iblockdata.a(iblockdata1.getBlock())) {
            super.remove(iblockdata, world, blockposition, iblockdata1, flag);
            this.d(world, blockposition, iblockdata);
        }
    }

    protected void d(World world, BlockPosition blockposition, IBlockData iblockdata) {
        EnumDirection enumdirection = (EnumDirection) iblockdata.get(BlockDiodeAbstract.FACING);
        BlockPosition blockposition1 = blockposition.shift(enumdirection.opposite());

        world.a(blockposition1, (Block) this, blockposition);
        world.a(blockposition1, (Block) this, enumdirection);
    }

    protected boolean h(IBlockData iblockdata) {
        return iblockdata.isPowerSource();
    }

    protected int b(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata) {
        return 15;
    }

    public static boolean isDiode(IBlockData iblockdata) {
        return iblockdata.getBlock() instanceof BlockDiodeAbstract;
    }

    public boolean c(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata) {
        EnumDirection enumdirection = ((EnumDirection) iblockdata.get(BlockDiodeAbstract.FACING)).opposite();
        IBlockData iblockdata1 = iblockaccess.getType(blockposition.shift(enumdirection));

        return isDiode(iblockdata1) && iblockdata1.get(BlockDiodeAbstract.FACING) != enumdirection;
    }

    protected abstract int g(IBlockData iblockdata);
}
