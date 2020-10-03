package net.minecraft.server;

import javax.annotation.Nullable;

public class BlockBell extends BlockTileEntity {

    public static final BlockStateDirection a = BlockFacingHorizontal.FACING;
    public static final BlockStateEnum<BlockPropertyBellAttach> b = BlockProperties.R;
    public static final BlockStateBoolean c = BlockProperties.w;
    private static final VoxelShape d = Block.a(0.0D, 0.0D, 4.0D, 16.0D, 16.0D, 12.0D);
    private static final VoxelShape e = Block.a(4.0D, 0.0D, 0.0D, 12.0D, 16.0D, 16.0D);
    private static final VoxelShape f = Block.a(5.0D, 6.0D, 5.0D, 11.0D, 13.0D, 11.0D);
    private static final VoxelShape g = Block.a(4.0D, 4.0D, 4.0D, 12.0D, 6.0D, 12.0D);
    private static final VoxelShape h = VoxelShapes.a(BlockBell.g, BlockBell.f);
    private static final VoxelShape i = VoxelShapes.a(BlockBell.h, Block.a(7.0D, 13.0D, 0.0D, 9.0D, 15.0D, 16.0D));
    private static final VoxelShape j = VoxelShapes.a(BlockBell.h, Block.a(0.0D, 13.0D, 7.0D, 16.0D, 15.0D, 9.0D));
    private static final VoxelShape k = VoxelShapes.a(BlockBell.h, Block.a(0.0D, 13.0D, 7.0D, 13.0D, 15.0D, 9.0D));
    private static final VoxelShape o = VoxelShapes.a(BlockBell.h, Block.a(3.0D, 13.0D, 7.0D, 16.0D, 15.0D, 9.0D));
    private static final VoxelShape p = VoxelShapes.a(BlockBell.h, Block.a(7.0D, 13.0D, 0.0D, 9.0D, 15.0D, 13.0D));
    private static final VoxelShape q = VoxelShapes.a(BlockBell.h, Block.a(7.0D, 13.0D, 3.0D, 9.0D, 15.0D, 16.0D));
    private static final VoxelShape r = VoxelShapes.a(BlockBell.h, Block.a(7.0D, 13.0D, 7.0D, 9.0D, 16.0D, 9.0D));

    public BlockBell(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockBell.a, EnumDirection.NORTH)).set(BlockBell.b, BlockPropertyBellAttach.FLOOR)).set(BlockBell.c, false));
    }

    @Override
    public void doPhysics(IBlockData iblockdata, World world, BlockPosition blockposition, Block block, BlockPosition blockposition1, boolean flag) {
        boolean flag1 = world.isBlockIndirectlyPowered(blockposition);

        if (flag1 != (Boolean) iblockdata.get(BlockBell.c)) {
            if (flag1) {
                this.a(world, blockposition, (EnumDirection) null);
            }

            world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockBell.c, flag1), 3);
        }

    }

    @Override
    public void a(World world, IBlockData iblockdata, MovingObjectPositionBlock movingobjectpositionblock, IProjectile iprojectile) {
        Entity entity = iprojectile.getShooter();
        EntityHuman entityhuman = entity instanceof EntityHuman ? (EntityHuman) entity : null;

        this.a(world, iblockdata, movingobjectpositionblock, entityhuman, true);
    }

    @Override
    public EnumInteractionResult interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        return this.a(world, iblockdata, movingobjectpositionblock, entityhuman, true) ? EnumInteractionResult.a(world.isClientSide) : EnumInteractionResult.PASS;
    }

    public boolean a(World world, IBlockData iblockdata, MovingObjectPositionBlock movingobjectpositionblock, @Nullable EntityHuman entityhuman, boolean flag) {
        EnumDirection enumdirection = movingobjectpositionblock.getDirection();
        BlockPosition blockposition = movingobjectpositionblock.getBlockPosition();
        boolean flag1 = !flag || this.a(iblockdata, enumdirection, movingobjectpositionblock.getPos().y - (double) blockposition.getY());

        if (flag1) {
            boolean flag2 = this.a(world, blockposition, enumdirection);

            if (flag2 && entityhuman != null) {
                entityhuman.a(StatisticList.BELL_RING);
            }

            return true;
        } else {
            return false;
        }
    }

    private boolean a(IBlockData iblockdata, EnumDirection enumdirection, double d0) {
        if (enumdirection.n() != EnumDirection.EnumAxis.Y && d0 <= 0.8123999834060669D) {
            EnumDirection enumdirection1 = (EnumDirection) iblockdata.get(BlockBell.a);
            BlockPropertyBellAttach blockpropertybellattach = (BlockPropertyBellAttach) iblockdata.get(BlockBell.b);

            switch (blockpropertybellattach) {
                case FLOOR:
                    return enumdirection1.n() == enumdirection.n();
                case SINGLE_WALL:
                case DOUBLE_WALL:
                    return enumdirection1.n() != enumdirection.n();
                case CEILING:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public boolean a(World world, BlockPosition blockposition, @Nullable EnumDirection enumdirection) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (!world.isClientSide && tileentity instanceof TileEntityBell) {
            if (enumdirection == null) {
                enumdirection = (EnumDirection) world.getType(blockposition).get(BlockBell.a);
            }

            ((TileEntityBell) tileentity).a(enumdirection);
            world.playSound((EntityHuman) null, blockposition, SoundEffects.BLOCK_BELL_USE, SoundCategory.BLOCKS, 2.0F, 1.0F);
            return true;
        } else {
            return false;
        }
    }

    private VoxelShape h(IBlockData iblockdata) {
        EnumDirection enumdirection = (EnumDirection) iblockdata.get(BlockBell.a);
        BlockPropertyBellAttach blockpropertybellattach = (BlockPropertyBellAttach) iblockdata.get(BlockBell.b);

        return blockpropertybellattach == BlockPropertyBellAttach.FLOOR ? (enumdirection != EnumDirection.NORTH && enumdirection != EnumDirection.SOUTH ? BlockBell.e : BlockBell.d) : (blockpropertybellattach == BlockPropertyBellAttach.CEILING ? BlockBell.r : (blockpropertybellattach == BlockPropertyBellAttach.DOUBLE_WALL ? (enumdirection != EnumDirection.NORTH && enumdirection != EnumDirection.SOUTH ? BlockBell.j : BlockBell.i) : (enumdirection == EnumDirection.NORTH ? BlockBell.p : (enumdirection == EnumDirection.SOUTH ? BlockBell.q : (enumdirection == EnumDirection.EAST ? BlockBell.o : BlockBell.k)))));
    }

    @Override
    public VoxelShape c(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return this.h(iblockdata);
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return this.h(iblockdata);
    }

    @Override
    public EnumRenderType b(IBlockData iblockdata) {
        return EnumRenderType.MODEL;
    }

    @Nullable
    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        EnumDirection enumdirection = blockactioncontext.getClickedFace();
        BlockPosition blockposition = blockactioncontext.getClickPosition();
        World world = blockactioncontext.getWorld();
        EnumDirection.EnumAxis enumdirection_enumaxis = enumdirection.n();
        IBlockData iblockdata;

        if (enumdirection_enumaxis == EnumDirection.EnumAxis.Y) {
            iblockdata = (IBlockData) ((IBlockData) this.getBlockData().set(BlockBell.b, enumdirection == EnumDirection.DOWN ? BlockPropertyBellAttach.CEILING : BlockPropertyBellAttach.FLOOR)).set(BlockBell.a, blockactioncontext.f());
            if (iblockdata.canPlace(blockactioncontext.getWorld(), blockposition)) {
                return iblockdata;
            }
        } else {
            boolean flag = enumdirection_enumaxis == EnumDirection.EnumAxis.X && world.getType(blockposition.west()).d(world, blockposition.west(), EnumDirection.EAST) && world.getType(blockposition.east()).d(world, blockposition.east(), EnumDirection.WEST) || enumdirection_enumaxis == EnumDirection.EnumAxis.Z && world.getType(blockposition.north()).d(world, blockposition.north(), EnumDirection.SOUTH) && world.getType(blockposition.south()).d(world, blockposition.south(), EnumDirection.NORTH);

            iblockdata = (IBlockData) ((IBlockData) this.getBlockData().set(BlockBell.a, enumdirection.opposite())).set(BlockBell.b, flag ? BlockPropertyBellAttach.DOUBLE_WALL : BlockPropertyBellAttach.SINGLE_WALL);
            if (iblockdata.canPlace(blockactioncontext.getWorld(), blockactioncontext.getClickPosition())) {
                return iblockdata;
            }

            boolean flag1 = world.getType(blockposition.down()).d(world, blockposition.down(), EnumDirection.UP);

            iblockdata = (IBlockData) iblockdata.set(BlockBell.b, flag1 ? BlockPropertyBellAttach.FLOOR : BlockPropertyBellAttach.CEILING);
            if (iblockdata.canPlace(blockactioncontext.getWorld(), blockactioncontext.getClickPosition())) {
                return iblockdata;
            }
        }

        return null;
    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        BlockPropertyBellAttach blockpropertybellattach = (BlockPropertyBellAttach) iblockdata.get(BlockBell.b);
        EnumDirection enumdirection1 = l(iblockdata).opposite();

        if (enumdirection1 == enumdirection && !iblockdata.canPlace(generatoraccess, blockposition) && blockpropertybellattach != BlockPropertyBellAttach.DOUBLE_WALL) {
            return Blocks.AIR.getBlockData();
        } else {
            if (enumdirection.n() == ((EnumDirection) iblockdata.get(BlockBell.a)).n()) {
                if (blockpropertybellattach == BlockPropertyBellAttach.DOUBLE_WALL && !iblockdata1.d(generatoraccess, blockposition1, enumdirection)) {
                    return (IBlockData) ((IBlockData) iblockdata.set(BlockBell.b, BlockPropertyBellAttach.SINGLE_WALL)).set(BlockBell.a, enumdirection.opposite());
                }

                if (blockpropertybellattach == BlockPropertyBellAttach.SINGLE_WALL && enumdirection1.opposite() == enumdirection && iblockdata1.d(generatoraccess, blockposition1, (EnumDirection) iblockdata.get(BlockBell.a))) {
                    return (IBlockData) iblockdata.set(BlockBell.b, BlockPropertyBellAttach.DOUBLE_WALL);
                }
            }

            return super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
        }
    }

    @Override
    public boolean canPlace(IBlockData iblockdata, IWorldReader iworldreader, BlockPosition blockposition) {
        EnumDirection enumdirection = l(iblockdata).opposite();

        return enumdirection == EnumDirection.UP ? Block.a(iworldreader, blockposition.up(), EnumDirection.DOWN) : BlockAttachable.b(iworldreader, blockposition, enumdirection);
    }

    private static EnumDirection l(IBlockData iblockdata) {
        switch ((BlockPropertyBellAttach) iblockdata.get(BlockBell.b)) {
            case FLOOR:
                return EnumDirection.UP;
            case CEILING:
                return EnumDirection.DOWN;
            default:
                return ((EnumDirection) iblockdata.get(BlockBell.a)).opposite();
        }
    }

    @Override
    public EnumPistonReaction getPushReaction(IBlockData iblockdata) {
        return EnumPistonReaction.DESTROY;
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockBell.a, BlockBell.b, BlockBell.c);
    }

    @Nullable
    @Override
    public TileEntity createTile(IBlockAccess iblockaccess) {
        return new TileEntityBell();
    }

    @Override
    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        return false;
    }
}
