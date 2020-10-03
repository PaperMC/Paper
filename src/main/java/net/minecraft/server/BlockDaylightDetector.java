package net.minecraft.server;

public class BlockDaylightDetector extends BlockTileEntity {

    public static final BlockStateInteger POWER = BlockProperties.az;
    public static final BlockStateBoolean b = BlockProperties.p;
    protected static final VoxelShape c = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);

    public BlockDaylightDetector(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockDaylightDetector.POWER, 0)).set(BlockDaylightDetector.b, false));
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return BlockDaylightDetector.c;
    }

    @Override
    public boolean c_(IBlockData iblockdata) {
        return true;
    }

    @Override
    public int a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return (Integer) iblockdata.get(BlockDaylightDetector.POWER);
    }

    public static void d(IBlockData iblockdata, World world, BlockPosition blockposition) {
        if (world.getDimensionManager().hasSkyLight()) {
            int i = world.getBrightness(EnumSkyBlock.SKY, blockposition) - world.c();
            float f = world.a(1.0F);
            boolean flag = (Boolean) iblockdata.get(BlockDaylightDetector.b);

            if (flag) {
                i = 15 - i;
            } else if (i > 0) {
                float f1 = f < 3.1415927F ? 0.0F : 6.2831855F;

                f += (f1 - f) * 0.2F;
                i = Math.round((float) i * MathHelper.cos(f));
            }

            i = MathHelper.clamp(i, 0, 15);
            if ((Integer) iblockdata.get(BlockDaylightDetector.POWER) != i) {
                world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockDaylightDetector.POWER, i), 3);
            }

        }
    }

    @Override
    public EnumInteractionResult interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        if (entityhuman.eJ()) {
            if (world.isClientSide) {
                return EnumInteractionResult.SUCCESS;
            } else {
                IBlockData iblockdata1 = (IBlockData) iblockdata.a((IBlockState) BlockDaylightDetector.b);

                world.setTypeAndData(blockposition, iblockdata1, 4);
                d(iblockdata1, world, blockposition);
                return EnumInteractionResult.CONSUME;
            }
        } else {
            return super.interact(iblockdata, world, blockposition, entityhuman, enumhand, movingobjectpositionblock);
        }
    }

    @Override
    public EnumRenderType b(IBlockData iblockdata) {
        return EnumRenderType.MODEL;
    }

    @Override
    public boolean isPowerSource(IBlockData iblockdata) {
        return true;
    }

    @Override
    public TileEntity createTile(IBlockAccess iblockaccess) {
        return new TileEntityLightDetector();
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockDaylightDetector.POWER, BlockDaylightDetector.b);
    }
}
