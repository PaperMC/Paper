package net.minecraft.server;

import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

public class BlockLever extends BlockAttachable {

    public static final BlockStateBoolean POWERED = BlockProperties.w;
    protected static final VoxelShape b = Block.a(5.0D, 4.0D, 10.0D, 11.0D, 12.0D, 16.0D);
    protected static final VoxelShape c = Block.a(5.0D, 4.0D, 0.0D, 11.0D, 12.0D, 6.0D);
    protected static final VoxelShape d = Block.a(10.0D, 4.0D, 5.0D, 16.0D, 12.0D, 11.0D);
    protected static final VoxelShape e = Block.a(0.0D, 4.0D, 5.0D, 6.0D, 12.0D, 11.0D);
    protected static final VoxelShape f = Block.a(5.0D, 0.0D, 4.0D, 11.0D, 6.0D, 12.0D);
    protected static final VoxelShape g = Block.a(4.0D, 0.0D, 5.0D, 12.0D, 6.0D, 11.0D);
    protected static final VoxelShape h = Block.a(5.0D, 10.0D, 4.0D, 11.0D, 16.0D, 12.0D);
    protected static final VoxelShape i = Block.a(4.0D, 10.0D, 5.0D, 12.0D, 16.0D, 11.0D);

    protected BlockLever(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockLever.FACING, EnumDirection.NORTH)).set(BlockLever.POWERED, false)).set(BlockLever.FACE, BlockPropertyAttachPosition.WALL));
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        switch ((BlockPropertyAttachPosition) iblockdata.get(BlockLever.FACE)) {
            case FLOOR:
                switch (((EnumDirection) iblockdata.get(BlockLever.FACING)).n()) {
                    case X:
                        return BlockLever.g;
                    case Z:
                    default:
                        return BlockLever.f;
                }
            case WALL:
                switch ((EnumDirection) iblockdata.get(BlockLever.FACING)) {
                    case EAST:
                        return BlockLever.e;
                    case WEST:
                        return BlockLever.d;
                    case SOUTH:
                        return BlockLever.c;
                    case NORTH:
                    default:
                        return BlockLever.b;
                }
            case CEILING:
            default:
                switch (((EnumDirection) iblockdata.get(BlockLever.FACING)).n()) {
                    case X:
                        return BlockLever.i;
                    case Z:
                    default:
                        return BlockLever.h;
                }
        }
    }

    @Override
    public EnumInteractionResult interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        IBlockData iblockdata1;

        if (world.isClientSide) {
            iblockdata1 = (IBlockData) iblockdata.a((IBlockState) BlockLever.POWERED);
            if ((Boolean) iblockdata1.get(BlockLever.POWERED)) {
                a(iblockdata1, world, blockposition, 1.0F);
            }

            return EnumInteractionResult.SUCCESS;
        } else {
            // CraftBukkit start - Interact Lever
            boolean powered = iblockdata.get(BlockLever.POWERED); // Old powered state
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
            int old = (powered) ? 15 : 0;
            int current = (!powered) ? 15 : 0;

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if ((eventRedstone.getNewCurrent() > 0) != (!powered)) {
                return EnumInteractionResult.SUCCESS;
            }
            // CraftBukkit end

            iblockdata1 = this.d(iblockdata, world, blockposition);
            float f = (Boolean) iblockdata1.get(BlockLever.POWERED) ? 0.6F : 0.5F;

            world.playSound((EntityHuman) null, blockposition, SoundEffects.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, f);
            return EnumInteractionResult.CONSUME;
        }
    }

    public IBlockData d(IBlockData iblockdata, World world, BlockPosition blockposition) {
        iblockdata = (IBlockData) iblockdata.a((IBlockState) BlockLever.POWERED);
        world.setTypeAndData(blockposition, iblockdata, 3);
        this.e(iblockdata, world, blockposition);
        return iblockdata;
    }

    private static void a(IBlockData iblockdata, GeneratorAccess generatoraccess, BlockPosition blockposition, float f) {
        EnumDirection enumdirection = ((EnumDirection) iblockdata.get(BlockLever.FACING)).opposite();
        EnumDirection enumdirection1 = h(iblockdata).opposite();
        double d0 = (double) blockposition.getX() + 0.5D + 0.1D * (double) enumdirection.getAdjacentX() + 0.2D * (double) enumdirection1.getAdjacentX();
        double d1 = (double) blockposition.getY() + 0.5D + 0.1D * (double) enumdirection.getAdjacentY() + 0.2D * (double) enumdirection1.getAdjacentY();
        double d2 = (double) blockposition.getZ() + 0.5D + 0.1D * (double) enumdirection.getAdjacentZ() + 0.2D * (double) enumdirection1.getAdjacentZ();

        generatoraccess.addParticle(new ParticleParamRedstone(1.0F, 0.0F, 0.0F, f), d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public void remove(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if (!flag && !iblockdata.a(iblockdata1.getBlock())) {
            if ((Boolean) iblockdata.get(BlockLever.POWERED)) {
                this.e(iblockdata, world, blockposition);
            }

            super.remove(iblockdata, world, blockposition, iblockdata1, flag);
        }
    }

    @Override
    public int a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return (Boolean) iblockdata.get(BlockLever.POWERED) ? 15 : 0;
    }

    @Override
    public int b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return (Boolean) iblockdata.get(BlockLever.POWERED) && h(iblockdata) == enumdirection ? 15 : 0;
    }

    @Override
    public boolean isPowerSource(IBlockData iblockdata) {
        return true;
    }

    private void e(IBlockData iblockdata, World world, BlockPosition blockposition) {
        world.applyPhysics(blockposition, this);
        world.applyPhysics(blockposition.shift(h(iblockdata).opposite()), this);
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockLever.FACE, BlockLever.FACING, BlockLever.POWERED);
    }
}
