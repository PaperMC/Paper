package net.minecraft.server;

public class BlockFenceGate extends BlockFacingHorizontal {

    public static final BlockStateBoolean OPEN = BlockProperties.u;
    public static final BlockStateBoolean POWERED = BlockProperties.w;
    public static final BlockStateBoolean IN_WALL = BlockProperties.q;
    protected static final VoxelShape d = Block.a(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
    protected static final VoxelShape e = Block.a(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);
    protected static final VoxelShape f = Block.a(0.0D, 0.0D, 6.0D, 16.0D, 13.0D, 10.0D);
    protected static final VoxelShape g = Block.a(6.0D, 0.0D, 0.0D, 10.0D, 13.0D, 16.0D);
    protected static final VoxelShape h = Block.a(0.0D, 0.0D, 6.0D, 16.0D, 24.0D, 10.0D);
    protected static final VoxelShape i = Block.a(6.0D, 0.0D, 0.0D, 10.0D, 24.0D, 16.0D);
    protected static final VoxelShape j = VoxelShapes.a(Block.a(0.0D, 5.0D, 7.0D, 2.0D, 16.0D, 9.0D), Block.a(14.0D, 5.0D, 7.0D, 16.0D, 16.0D, 9.0D));
    protected static final VoxelShape k = VoxelShapes.a(Block.a(7.0D, 5.0D, 0.0D, 9.0D, 16.0D, 2.0D), Block.a(7.0D, 5.0D, 14.0D, 9.0D, 16.0D, 16.0D));
    protected static final VoxelShape o = VoxelShapes.a(Block.a(0.0D, 2.0D, 7.0D, 2.0D, 13.0D, 9.0D), Block.a(14.0D, 2.0D, 7.0D, 16.0D, 13.0D, 9.0D));
    protected static final VoxelShape p = VoxelShapes.a(Block.a(7.0D, 2.0D, 0.0D, 9.0D, 13.0D, 2.0D), Block.a(7.0D, 2.0D, 14.0D, 9.0D, 13.0D, 16.0D));

    public BlockFenceGate(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockFenceGate.OPEN, false)).set(BlockFenceGate.POWERED, false)).set(BlockFenceGate.IN_WALL, false));
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return (Boolean) iblockdata.get(BlockFenceGate.IN_WALL) ? (((EnumDirection) iblockdata.get(BlockFenceGate.FACING)).n() == EnumDirection.EnumAxis.X ? BlockFenceGate.g : BlockFenceGate.f) : (((EnumDirection) iblockdata.get(BlockFenceGate.FACING)).n() == EnumDirection.EnumAxis.X ? BlockFenceGate.e : BlockFenceGate.d);
    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        EnumDirection.EnumAxis enumdirection_enumaxis = enumdirection.n();

        if (((EnumDirection) iblockdata.get(BlockFenceGate.FACING)).g().n() != enumdirection_enumaxis) {
            return super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
        } else {
            boolean flag = this.h(iblockdata1) || this.h(generatoraccess.getType(blockposition.shift(enumdirection.opposite())));

            return (IBlockData) iblockdata.set(BlockFenceGate.IN_WALL, flag);
        }
    }

    @Override
    public VoxelShape c(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return (Boolean) iblockdata.get(BlockFenceGate.OPEN) ? VoxelShapes.a() : (((EnumDirection) iblockdata.get(BlockFenceGate.FACING)).n() == EnumDirection.EnumAxis.Z ? BlockFenceGate.h : BlockFenceGate.i);
    }

    @Override
    public VoxelShape d(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return (Boolean) iblockdata.get(BlockFenceGate.IN_WALL) ? (((EnumDirection) iblockdata.get(BlockFenceGate.FACING)).n() == EnumDirection.EnumAxis.X ? BlockFenceGate.p : BlockFenceGate.o) : (((EnumDirection) iblockdata.get(BlockFenceGate.FACING)).n() == EnumDirection.EnumAxis.X ? BlockFenceGate.k : BlockFenceGate.j);
    }

    @Override
    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        switch (pathmode) {
            case LAND:
                return (Boolean) iblockdata.get(BlockFenceGate.OPEN);
            case WATER:
                return false;
            case AIR:
                return (Boolean) iblockdata.get(BlockFenceGate.OPEN);
            default:
                return false;
        }
    }

    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        World world = blockactioncontext.getWorld();
        BlockPosition blockposition = blockactioncontext.getClickPosition();
        boolean flag = world.isBlockIndirectlyPowered(blockposition);
        EnumDirection enumdirection = blockactioncontext.f();
        EnumDirection.EnumAxis enumdirection_enumaxis = enumdirection.n();
        boolean flag1 = enumdirection_enumaxis == EnumDirection.EnumAxis.Z && (this.h(world.getType(blockposition.west())) || this.h(world.getType(blockposition.east()))) || enumdirection_enumaxis == EnumDirection.EnumAxis.X && (this.h(world.getType(blockposition.north())) || this.h(world.getType(blockposition.south())));

        return (IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.getBlockData().set(BlockFenceGate.FACING, enumdirection)).set(BlockFenceGate.OPEN, flag)).set(BlockFenceGate.POWERED, flag)).set(BlockFenceGate.IN_WALL, flag1);
    }

    private boolean h(IBlockData iblockdata) {
        return iblockdata.getBlock().a((Tag) TagsBlock.WALLS);
    }

    @Override
    public EnumInteractionResult interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        if ((Boolean) iblockdata.get(BlockFenceGate.OPEN)) {
            iblockdata = (IBlockData) iblockdata.set(BlockFenceGate.OPEN, false);
            world.setTypeAndData(blockposition, iblockdata, 10);
        } else {
            EnumDirection enumdirection = entityhuman.getDirection();

            if (iblockdata.get(BlockFenceGate.FACING) == enumdirection.opposite()) {
                iblockdata = (IBlockData) iblockdata.set(BlockFenceGate.FACING, enumdirection);
            }

            iblockdata = (IBlockData) iblockdata.set(BlockFenceGate.OPEN, true);
            world.setTypeAndData(blockposition, iblockdata, 10);
        }

        world.a(entityhuman, (Boolean) iblockdata.get(BlockFenceGate.OPEN) ? 1008 : 1014, blockposition, 0);
        return EnumInteractionResult.a(world.isClientSide);
    }

    @Override
    public void doPhysics(IBlockData iblockdata, World world, BlockPosition blockposition, Block block, BlockPosition blockposition1, boolean flag) {
        if (!world.isClientSide) {
            boolean flag1 = world.isBlockIndirectlyPowered(blockposition);

            if ((Boolean) iblockdata.get(BlockFenceGate.POWERED) != flag1) {
                world.setTypeAndData(blockposition, (IBlockData) ((IBlockData) iblockdata.set(BlockFenceGate.POWERED, flag1)).set(BlockFenceGate.OPEN, flag1), 2);
                if ((Boolean) iblockdata.get(BlockFenceGate.OPEN) != flag1) {
                    world.a((EntityHuman) null, flag1 ? 1008 : 1014, blockposition, 0);
                }
            }

        }
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockFenceGate.FACING, BlockFenceGate.OPEN, BlockFenceGate.POWERED, BlockFenceGate.IN_WALL);
    }

    public static boolean a(IBlockData iblockdata, EnumDirection enumdirection) {
        return ((EnumDirection) iblockdata.get(BlockFenceGate.FACING)).n() == enumdirection.g().n();
    }
}
