package net.minecraft.server;

public abstract class BlockMinecartTrackAbstract extends Block {

    protected static final VoxelShape a = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    protected static final VoxelShape b = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    private final boolean c;

    public static boolean a(World world, BlockPosition blockposition) {
        return g(world.getType(blockposition));
    }

    public static boolean g(IBlockData iblockdata) {
        return iblockdata.a((Tag) TagsBlock.RAILS) && iblockdata.getBlock() instanceof BlockMinecartTrackAbstract;
    }

    protected BlockMinecartTrackAbstract(boolean flag, BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.c = flag;
    }

    public boolean c() {
        return this.c;
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        BlockPropertyTrackPosition blockpropertytrackposition = iblockdata.a((Block) this) ? (BlockPropertyTrackPosition) iblockdata.get(this.d()) : null;

        return blockpropertytrackposition != null && blockpropertytrackposition.c() ? BlockMinecartTrackAbstract.b : BlockMinecartTrackAbstract.a;
    }

    @Override
    public boolean canPlace(IBlockData iblockdata, IWorldReader iworldreader, BlockPosition blockposition) {
        return c((IBlockAccess) iworldreader, blockposition.down());
    }

    @Override
    public void onPlace(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if (!iblockdata1.a(iblockdata.getBlock())) {
            this.a(iblockdata, world, blockposition, flag);
        }
    }

    protected IBlockData a(IBlockData iblockdata, World world, BlockPosition blockposition, boolean flag) {
        iblockdata = this.a(world, blockposition, iblockdata, true);
        if (this.c) {
            iblockdata.doPhysics(world, blockposition, this, blockposition, flag);
        }

        return iblockdata;
    }

    @Override
    public void doPhysics(IBlockData iblockdata, World world, BlockPosition blockposition, Block block, BlockPosition blockposition1, boolean flag) {
        if (!world.isClientSide && world.getType(blockposition).a((Block) this)) {
            BlockPropertyTrackPosition blockpropertytrackposition = (BlockPropertyTrackPosition) iblockdata.get(this.d());

            if (a(blockposition, world, blockpropertytrackposition)) {
                c(iblockdata, world, blockposition);
                world.a(blockposition, flag);
            } else {
                this.a(iblockdata, world, blockposition, block);
            }

        }
    }

    private static boolean a(BlockPosition blockposition, World world, BlockPropertyTrackPosition blockpropertytrackposition) {
        if (!c((IBlockAccess) world, blockposition.down())) {
            return true;
        } else {
            switch (blockpropertytrackposition) {
                case ASCENDING_EAST:
                    return !c((IBlockAccess) world, blockposition.east());
                case ASCENDING_WEST:
                    return !c((IBlockAccess) world, blockposition.west());
                case ASCENDING_NORTH:
                    return !c((IBlockAccess) world, blockposition.north());
                case ASCENDING_SOUTH:
                    return !c((IBlockAccess) world, blockposition.south());
                default:
                    return false;
            }
        }
    }

    protected void a(IBlockData iblockdata, World world, BlockPosition blockposition, Block block) {}

    protected IBlockData a(World world, BlockPosition blockposition, IBlockData iblockdata, boolean flag) {
        if (world.isClientSide) {
            return iblockdata;
        } else {
            BlockPropertyTrackPosition blockpropertytrackposition = (BlockPropertyTrackPosition) iblockdata.get(this.d());

            return (new MinecartTrackLogic(world, blockposition, iblockdata)).a(world.isBlockIndirectlyPowered(blockposition), flag, blockpropertytrackposition).c();
        }
    }

    @Override
    public EnumPistonReaction getPushReaction(IBlockData iblockdata) {
        return EnumPistonReaction.NORMAL;
    }

    @Override
    public void remove(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if (!flag) {
            super.remove(iblockdata, world, blockposition, iblockdata1, flag);
            if (((BlockPropertyTrackPosition) iblockdata.get(this.d())).c()) {
                world.applyPhysics(blockposition.up(), this);
            }

            if (this.c) {
                world.applyPhysics(blockposition, this);
                world.applyPhysics(blockposition.down(), this);
            }

        }
    }

    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        IBlockData iblockdata = super.getBlockData();
        EnumDirection enumdirection = blockactioncontext.f();
        boolean flag = enumdirection == EnumDirection.EAST || enumdirection == EnumDirection.WEST;

        return (IBlockData) iblockdata.set(this.d(), flag ? BlockPropertyTrackPosition.EAST_WEST : BlockPropertyTrackPosition.NORTH_SOUTH);
    }

    public abstract IBlockState<BlockPropertyTrackPosition> d();
}
