package net.minecraft.server;

public class BlockPoweredRail extends BlockMinecartTrackAbstract {

    public static final BlockStateEnum<BlockPropertyTrackPosition> SHAPE = BlockProperties.ad;
    public static final BlockStateBoolean POWERED = BlockProperties.w;

    protected BlockPoweredRail(BlockBase.Info blockbase_info) {
        super(true, blockbase_info);
        this.j((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.NORTH_SOUTH)).set(BlockPoweredRail.POWERED, false));
    }

    protected boolean a(World world, BlockPosition blockposition, IBlockData iblockdata, boolean flag, int i) {
        if (i >= 8) {
            return false;
        } else {
            int j = blockposition.getX();
            int k = blockposition.getY();
            int l = blockposition.getZ();
            boolean flag1 = true;
            BlockPropertyTrackPosition blockpropertytrackposition = (BlockPropertyTrackPosition) iblockdata.get(BlockPoweredRail.SHAPE);

            switch (blockpropertytrackposition) {
                case NORTH_SOUTH:
                    if (flag) {
                        ++l;
                    } else {
                        --l;
                    }
                    break;
                case EAST_WEST:
                    if (flag) {
                        --j;
                    } else {
                        ++j;
                    }
                    break;
                case ASCENDING_EAST:
                    if (flag) {
                        --j;
                    } else {
                        ++j;
                        ++k;
                        flag1 = false;
                    }

                    blockpropertytrackposition = BlockPropertyTrackPosition.EAST_WEST;
                    break;
                case ASCENDING_WEST:
                    if (flag) {
                        --j;
                        ++k;
                        flag1 = false;
                    } else {
                        ++j;
                    }

                    blockpropertytrackposition = BlockPropertyTrackPosition.EAST_WEST;
                    break;
                case ASCENDING_NORTH:
                    if (flag) {
                        ++l;
                    } else {
                        --l;
                        ++k;
                        flag1 = false;
                    }

                    blockpropertytrackposition = BlockPropertyTrackPosition.NORTH_SOUTH;
                    break;
                case ASCENDING_SOUTH:
                    if (flag) {
                        ++l;
                        ++k;
                        flag1 = false;
                    } else {
                        --l;
                    }

                    blockpropertytrackposition = BlockPropertyTrackPosition.NORTH_SOUTH;
            }

            return this.a(world, new BlockPosition(j, k, l), flag, i, blockpropertytrackposition) ? true : flag1 && this.a(world, new BlockPosition(j, k - 1, l), flag, i, blockpropertytrackposition);
        }
    }

    protected boolean a(World world, BlockPosition blockposition, boolean flag, int i, BlockPropertyTrackPosition blockpropertytrackposition) {
        IBlockData iblockdata = world.getType(blockposition);

        if (!iblockdata.a((Block) this)) {
            return false;
        } else {
            BlockPropertyTrackPosition blockpropertytrackposition1 = (BlockPropertyTrackPosition) iblockdata.get(BlockPoweredRail.SHAPE);

            return blockpropertytrackposition == BlockPropertyTrackPosition.EAST_WEST && (blockpropertytrackposition1 == BlockPropertyTrackPosition.NORTH_SOUTH || blockpropertytrackposition1 == BlockPropertyTrackPosition.ASCENDING_NORTH || blockpropertytrackposition1 == BlockPropertyTrackPosition.ASCENDING_SOUTH) ? false : (blockpropertytrackposition == BlockPropertyTrackPosition.NORTH_SOUTH && (blockpropertytrackposition1 == BlockPropertyTrackPosition.EAST_WEST || blockpropertytrackposition1 == BlockPropertyTrackPosition.ASCENDING_EAST || blockpropertytrackposition1 == BlockPropertyTrackPosition.ASCENDING_WEST) ? false : ((Boolean) iblockdata.get(BlockPoweredRail.POWERED) ? (world.isBlockIndirectlyPowered(blockposition) ? true : this.a(world, blockposition, iblockdata, flag, i + 1)) : false));
        }
    }

    @Override
    protected void a(IBlockData iblockdata, World world, BlockPosition blockposition, Block block) {
        boolean flag = (Boolean) iblockdata.get(BlockPoweredRail.POWERED);
        boolean flag1 = world.isBlockIndirectlyPowered(blockposition) || this.a(world, blockposition, iblockdata, true, 0) || this.a(world, blockposition, iblockdata, false, 0);

        if (flag1 != flag) {
            world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockPoweredRail.POWERED, flag1), 3);
            world.applyPhysics(blockposition.down(), this);
            if (((BlockPropertyTrackPosition) iblockdata.get(BlockPoweredRail.SHAPE)).c()) {
                world.applyPhysics(blockposition.up(), this);
            }
        }

    }

    @Override
    public IBlockState<BlockPropertyTrackPosition> d() {
        return BlockPoweredRail.SHAPE;
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        switch (enumblockrotation) {
            case CLOCKWISE_180:
                switch ((BlockPropertyTrackPosition) iblockdata.get(BlockPoweredRail.SHAPE)) {
                    case ASCENDING_EAST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.NORTH_WEST);
                    case SOUTH_WEST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.NORTH_EAST);
                    case NORTH_WEST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.SOUTH_EAST);
                    case NORTH_EAST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.SOUTH_WEST);
                }
            case COUNTERCLOCKWISE_90:
                switch ((BlockPropertyTrackPosition) iblockdata.get(BlockPoweredRail.SHAPE)) {
                    case NORTH_SOUTH:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.EAST_WEST);
                    case EAST_WEST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.NORTH_SOUTH);
                    case ASCENDING_EAST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.ASCENDING_NORTH);
                    case ASCENDING_WEST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.ASCENDING_SOUTH);
                    case ASCENDING_NORTH:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.ASCENDING_WEST);
                    case ASCENDING_SOUTH:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.ASCENDING_EAST);
                    case SOUTH_EAST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.NORTH_EAST);
                    case SOUTH_WEST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.SOUTH_EAST);
                    case NORTH_WEST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.SOUTH_WEST);
                    case NORTH_EAST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.NORTH_WEST);
                }
            case CLOCKWISE_90:
                switch ((BlockPropertyTrackPosition) iblockdata.get(BlockPoweredRail.SHAPE)) {
                    case NORTH_SOUTH:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.EAST_WEST);
                    case EAST_WEST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.NORTH_SOUTH);
                    case ASCENDING_EAST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.ASCENDING_SOUTH);
                    case ASCENDING_WEST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.ASCENDING_NORTH);
                    case ASCENDING_NORTH:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.ASCENDING_EAST);
                    case ASCENDING_SOUTH:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.ASCENDING_WEST);
                    case SOUTH_EAST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.SOUTH_WEST);
                    case SOUTH_WEST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.NORTH_WEST);
                    case NORTH_WEST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.NORTH_EAST);
                    case NORTH_EAST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.SOUTH_EAST);
                }
            default:
                return iblockdata;
        }
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        BlockPropertyTrackPosition blockpropertytrackposition = (BlockPropertyTrackPosition) iblockdata.get(BlockPoweredRail.SHAPE);

        switch (enumblockmirror) {
            case LEFT_RIGHT:
                switch (blockpropertytrackposition) {
                    case ASCENDING_NORTH:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.NORTH_EAST);
                    case SOUTH_WEST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.NORTH_WEST);
                    case NORTH_WEST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.SOUTH_WEST);
                    case NORTH_EAST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.SOUTH_EAST);
                    default:
                        return super.a(iblockdata, enumblockmirror);
                }
            case FRONT_BACK:
                switch (blockpropertytrackposition) {
                    case ASCENDING_EAST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;
                    case SOUTH_EAST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.SOUTH_WEST);
                    case SOUTH_WEST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.SOUTH_EAST);
                    case NORTH_WEST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.NORTH_EAST);
                    case NORTH_EAST:
                        return (IBlockData) iblockdata.set(BlockPoweredRail.SHAPE, BlockPropertyTrackPosition.NORTH_WEST);
                }
        }

        return super.a(iblockdata, enumblockmirror);
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockPoweredRail.SHAPE, BlockPoweredRail.POWERED);
    }
}
