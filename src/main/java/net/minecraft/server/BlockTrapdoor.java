package net.minecraft.server;

import javax.annotation.Nullable;
import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

public class BlockTrapdoor extends BlockFacingHorizontal implements IBlockWaterlogged {

    public static final BlockStateBoolean OPEN = BlockProperties.u;
    public static final BlockStateEnum<BlockPropertyHalf> HALF = BlockProperties.ab;
    public static final BlockStateBoolean c = BlockProperties.w;
    public static final BlockStateBoolean d = BlockProperties.C;
    protected static final VoxelShape e = Block.a(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);
    protected static final VoxelShape f = Block.a(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape g = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
    protected static final VoxelShape h = Block.a(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape i = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D);
    protected static final VoxelShape j = Block.a(0.0D, 13.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    protected BlockTrapdoor(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockTrapdoor.FACING, EnumDirection.NORTH)).set(BlockTrapdoor.OPEN, false)).set(BlockTrapdoor.HALF, BlockPropertyHalf.BOTTOM)).set(BlockTrapdoor.c, false)).set(BlockTrapdoor.d, false));
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        if (!(Boolean) iblockdata.get(BlockTrapdoor.OPEN)) {
            return iblockdata.get(BlockTrapdoor.HALF) == BlockPropertyHalf.TOP ? BlockTrapdoor.j : BlockTrapdoor.i;
        } else {
            switch ((EnumDirection) iblockdata.get(BlockTrapdoor.FACING)) {
                case NORTH:
                default:
                    return BlockTrapdoor.h;
                case SOUTH:
                    return BlockTrapdoor.g;
                case WEST:
                    return BlockTrapdoor.f;
                case EAST:
                    return BlockTrapdoor.e;
            }
        }
    }

    @Override
    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        switch (pathmode) {
            case LAND:
                return (Boolean) iblockdata.get(BlockTrapdoor.OPEN);
            case WATER:
                return (Boolean) iblockdata.get(BlockTrapdoor.d);
            case AIR:
                return (Boolean) iblockdata.get(BlockTrapdoor.OPEN);
            default:
                return false;
        }
    }

    @Override
    public EnumInteractionResult interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        if (this.material == Material.ORE) {
            return EnumInteractionResult.PASS;
        } else {
            iblockdata = (IBlockData) iblockdata.a((IBlockState) BlockTrapdoor.OPEN);
            world.setTypeAndData(blockposition, iblockdata, 2);
            if ((Boolean) iblockdata.get(BlockTrapdoor.d)) {
                world.getFluidTickList().a(blockposition, FluidTypes.WATER, FluidTypes.WATER.a((IWorldReader) world));
            }

            this.a(entityhuman, world, blockposition, (Boolean) iblockdata.get(BlockTrapdoor.OPEN));
            return EnumInteractionResult.a(world.isClientSide);
        }
    }

    protected void a(@Nullable EntityHuman entityhuman, World world, BlockPosition blockposition, boolean flag) {
        int i;

        if (flag) {
            i = this.material == Material.ORE ? 1037 : 1007;
            world.a(entityhuman, i, blockposition, 0);
        } else {
            i = this.material == Material.ORE ? 1036 : 1013;
            world.a(entityhuman, i, blockposition, 0);
        }

    }

    @Override
    public void doPhysics(IBlockData iblockdata, World world, BlockPosition blockposition, Block block, BlockPosition blockposition1, boolean flag) {
        if (!world.isClientSide) {
            boolean flag1 = world.isBlockIndirectlyPowered(blockposition);

            if (flag1 != (Boolean) iblockdata.get(BlockTrapdoor.c)) {
                // CraftBukkit start
                org.bukkit.World bworld = world.getWorld();
                org.bukkit.block.Block bblock = bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

                int power = bblock.getBlockPower();
                int oldPower = (Boolean) iblockdata.get(OPEN) ? 15 : 0;

                if (oldPower == 0 ^ power == 0 || block.getBlockData().isPowerSource()) {
                    BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(bblock, oldPower, power);
                    world.getServer().getPluginManager().callEvent(eventRedstone);
                    flag1 = eventRedstone.getNewCurrent() > 0;
                }
                // CraftBukkit end
                if ((Boolean) iblockdata.get(BlockTrapdoor.OPEN) != flag1) {
                    iblockdata = (IBlockData) iblockdata.set(BlockTrapdoor.OPEN, flag1);
                    this.a((EntityHuman) null, world, blockposition, flag1);
                }

                world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockTrapdoor.c, flag1), 2);
                if ((Boolean) iblockdata.get(BlockTrapdoor.d)) {
                    world.getFluidTickList().a(blockposition, FluidTypes.WATER, FluidTypes.WATER.a((IWorldReader) world));
                }
            }

        }
    }

    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        IBlockData iblockdata = this.getBlockData();
        Fluid fluid = blockactioncontext.getWorld().getFluid(blockactioncontext.getClickPosition());
        EnumDirection enumdirection = blockactioncontext.getClickedFace();

        if (!blockactioncontext.c() && enumdirection.n().d()) {
            iblockdata = (IBlockData) ((IBlockData) iblockdata.set(BlockTrapdoor.FACING, enumdirection)).set(BlockTrapdoor.HALF, blockactioncontext.getPos().y - (double) blockactioncontext.getClickPosition().getY() > 0.5D ? BlockPropertyHalf.TOP : BlockPropertyHalf.BOTTOM);
        } else {
            iblockdata = (IBlockData) ((IBlockData) iblockdata.set(BlockTrapdoor.FACING, blockactioncontext.f().opposite())).set(BlockTrapdoor.HALF, enumdirection == EnumDirection.UP ? BlockPropertyHalf.BOTTOM : BlockPropertyHalf.TOP);
        }

        if (blockactioncontext.getWorld().isBlockIndirectlyPowered(blockactioncontext.getClickPosition())) {
            iblockdata = (IBlockData) ((IBlockData) iblockdata.set(BlockTrapdoor.OPEN, true)).set(BlockTrapdoor.c, true);
        }

        return (IBlockData) iblockdata.set(BlockTrapdoor.d, fluid.getType() == FluidTypes.WATER);
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockTrapdoor.FACING, BlockTrapdoor.OPEN, BlockTrapdoor.HALF, BlockTrapdoor.c, BlockTrapdoor.d);
    }

    @Override
    public Fluid d(IBlockData iblockdata) {
        return (Boolean) iblockdata.get(BlockTrapdoor.d) ? FluidTypes.WATER.a(false) : super.d(iblockdata);
    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        if ((Boolean) iblockdata.get(BlockTrapdoor.d)) {
            generatoraccess.getFluidTickList().a(blockposition, FluidTypes.WATER, FluidTypes.WATER.a((IWorldReader) generatoraccess));
        }

        return super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
    }
}
