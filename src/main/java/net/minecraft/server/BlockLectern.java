package net.minecraft.server;

import java.util.Random;
import javax.annotation.Nullable;

public class BlockLectern extends BlockTileEntity {

    public static final BlockStateDirection a = BlockFacingHorizontal.FACING;
    public static final BlockStateBoolean b = BlockProperties.w;
    public static final BlockStateBoolean c = BlockProperties.o;
    public static final VoxelShape d = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    public static final VoxelShape e = Block.a(4.0D, 2.0D, 4.0D, 12.0D, 14.0D, 12.0D);
    public static final VoxelShape f = VoxelShapes.a(BlockLectern.d, BlockLectern.e);
    public static final VoxelShape g = Block.a(0.0D, 15.0D, 0.0D, 16.0D, 15.0D, 16.0D);
    public static final VoxelShape h = VoxelShapes.a(BlockLectern.f, BlockLectern.g);
    public static final VoxelShape i = VoxelShapes.a(Block.a(1.0D, 10.0D, 0.0D, 5.333333D, 14.0D, 16.0D), Block.a(5.333333D, 12.0D, 0.0D, 9.666667D, 16.0D, 16.0D), Block.a(9.666667D, 14.0D, 0.0D, 14.0D, 18.0D, 16.0D), BlockLectern.f);
    public static final VoxelShape j = VoxelShapes.a(Block.a(0.0D, 10.0D, 1.0D, 16.0D, 14.0D, 5.333333D), Block.a(0.0D, 12.0D, 5.333333D, 16.0D, 16.0D, 9.666667D), Block.a(0.0D, 14.0D, 9.666667D, 16.0D, 18.0D, 14.0D), BlockLectern.f);
    public static final VoxelShape k = VoxelShapes.a(Block.a(15.0D, 10.0D, 0.0D, 10.666667D, 14.0D, 16.0D), Block.a(10.666667D, 12.0D, 0.0D, 6.333333D, 16.0D, 16.0D), Block.a(6.333333D, 14.0D, 0.0D, 2.0D, 18.0D, 16.0D), BlockLectern.f);
    public static final VoxelShape o = VoxelShapes.a(Block.a(0.0D, 10.0D, 15.0D, 16.0D, 14.0D, 10.666667D), Block.a(0.0D, 12.0D, 10.666667D, 16.0D, 16.0D, 6.333333D), Block.a(0.0D, 14.0D, 6.333333D, 16.0D, 18.0D, 2.0D), BlockLectern.f);

    protected BlockLectern(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockLectern.a, EnumDirection.NORTH)).set(BlockLectern.b, false)).set(BlockLectern.c, false));
    }

    @Override
    public EnumRenderType b(IBlockData iblockdata) {
        return EnumRenderType.MODEL;
    }

    @Override
    public VoxelShape d(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return BlockLectern.f;
    }

    @Override
    public boolean c_(IBlockData iblockdata) {
        return true;
    }

    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        World world = blockactioncontext.getWorld();
        ItemStack itemstack = blockactioncontext.getItemStack();
        NBTTagCompound nbttagcompound = itemstack.getTag();
        EntityHuman entityhuman = blockactioncontext.getEntity();
        boolean flag = false;

        if (!world.isClientSide && entityhuman != null && nbttagcompound != null && entityhuman.isCreativeAndOp() && nbttagcompound.hasKey("BlockEntityTag")) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("BlockEntityTag");

            if (nbttagcompound1.hasKey("Book")) {
                flag = true;
            }
        }

        return (IBlockData) ((IBlockData) this.getBlockData().set(BlockLectern.a, blockactioncontext.f().opposite())).set(BlockLectern.c, flag);
    }

    @Override
    public VoxelShape c(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return BlockLectern.h;
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        switch ((EnumDirection) iblockdata.get(BlockLectern.a)) {
            case NORTH:
                return BlockLectern.j;
            case SOUTH:
                return BlockLectern.o;
            case EAST:
                return BlockLectern.k;
            case WEST:
                return BlockLectern.i;
            default:
                return BlockLectern.f;
        }
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        return (IBlockData) iblockdata.set(BlockLectern.a, enumblockrotation.a((EnumDirection) iblockdata.get(BlockLectern.a)));
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        return iblockdata.a(enumblockmirror.a((EnumDirection) iblockdata.get(BlockLectern.a)));
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockLectern.a, BlockLectern.b, BlockLectern.c);
    }

    @Nullable
    @Override
    public TileEntity createTile(IBlockAccess iblockaccess) {
        return new TileEntityLectern();
    }

    public static boolean a(World world, BlockPosition blockposition, IBlockData iblockdata, ItemStack itemstack) {
        if (!(Boolean) iblockdata.get(BlockLectern.c)) {
            if (!world.isClientSide) {
                b(world, blockposition, iblockdata, itemstack);
            }

            return true;
        } else {
            return false;
        }
    }

    private static void b(World world, BlockPosition blockposition, IBlockData iblockdata, ItemStack itemstack) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityLectern) {
            TileEntityLectern tileentitylectern = (TileEntityLectern) tileentity;

            tileentitylectern.setBook(itemstack.cloneAndSubtract(1));
            setHasBook(world, blockposition, iblockdata, true);
            world.playSound((EntityHuman) null, blockposition, SoundEffects.ITEM_BOOK_PUT, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }

    }

    public static void setHasBook(World world, BlockPosition blockposition, IBlockData iblockdata, boolean flag) {
        world.setTypeAndData(blockposition, (IBlockData) ((IBlockData) iblockdata.set(BlockLectern.b, false)).set(BlockLectern.c, flag), 3);
        b(world, blockposition, iblockdata);
    }

    public static void a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        b(world, blockposition, iblockdata, true);
        world.getBlockTickList().a(blockposition, iblockdata.getBlock(), 2);
        world.triggerEffect(1043, blockposition, 0);
    }

    private static void b(World world, BlockPosition blockposition, IBlockData iblockdata, boolean flag) {
        world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockLectern.b, flag), 3);
        b(world, blockposition, iblockdata);
    }

    private static void b(World world, BlockPosition blockposition, IBlockData iblockdata) {
        world.applyPhysics(blockposition.down(), iblockdata.getBlock());
    }

    @Override
    public void tickAlways(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        b(worldserver, blockposition, iblockdata, false);
    }

    @Override
    public void remove(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if (!iblockdata.a(iblockdata1.getBlock())) {
            if ((Boolean) iblockdata.get(BlockLectern.c)) {
                this.d(iblockdata, world, blockposition);
            }

            if ((Boolean) iblockdata.get(BlockLectern.b)) {
                world.applyPhysics(blockposition.down(), this);
            }

            super.remove(iblockdata, world, blockposition, iblockdata1, flag);
        }
    }

    private void d(IBlockData iblockdata, World world, BlockPosition blockposition) {
        TileEntity tileentity = world.getTileEntity(blockposition, false); // CraftBukkit - don't validate, type may be changed already

        if (tileentity instanceof TileEntityLectern) {
            TileEntityLectern tileentitylectern = (TileEntityLectern) tileentity;
            EnumDirection enumdirection = (EnumDirection) iblockdata.get(BlockLectern.a);
            ItemStack itemstack = tileentitylectern.getBook().cloneItemStack();
            if (itemstack.isEmpty()) return; // CraftBukkit - SPIGOT-5500
            float f = 0.25F * (float) enumdirection.getAdjacentX();
            float f1 = 0.25F * (float) enumdirection.getAdjacentZ();
            EntityItem entityitem = new EntityItem(world, (double) blockposition.getX() + 0.5D + (double) f, (double) (blockposition.getY() + 1), (double) blockposition.getZ() + 0.5D + (double) f1, itemstack);

            entityitem.defaultPickupDelay();
            world.addEntity(entityitem);
            tileentitylectern.clear();
        }

    }

    @Override
    public boolean isPowerSource(IBlockData iblockdata) {
        return true;
    }

    @Override
    public int a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return (Boolean) iblockdata.get(BlockLectern.b) ? 15 : 0;
    }

    @Override
    public int b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return enumdirection == EnumDirection.UP && (Boolean) iblockdata.get(BlockLectern.b) ? 15 : 0;
    }

    @Override
    public boolean isComplexRedstone(IBlockData iblockdata) {
        return true;
    }

    @Override
    public int a(IBlockData iblockdata, World world, BlockPosition blockposition) {
        if ((Boolean) iblockdata.get(BlockLectern.c)) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityLectern) {
                return ((TileEntityLectern) tileentity).j();
            }
        }

        return 0;
    }

    @Override
    public EnumInteractionResult interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        if ((Boolean) iblockdata.get(BlockLectern.c)) {
            if (!world.isClientSide) {
                this.a(world, blockposition, entityhuman);
            }

            return EnumInteractionResult.a(world.isClientSide);
        } else {
            ItemStack itemstack = entityhuman.b(enumhand);

            return !itemstack.isEmpty() && !itemstack.getItem().a((Tag) TagsItem.LECTERN_BOOKS) ? EnumInteractionResult.CONSUME : EnumInteractionResult.PASS;
        }
    }

    @Nullable
    @Override
    public ITileInventory getInventory(IBlockData iblockdata, World world, BlockPosition blockposition) {
        return !(Boolean) iblockdata.get(BlockLectern.c) ? null : super.getInventory(iblockdata, world, blockposition);
    }

    private void a(World world, BlockPosition blockposition, EntityHuman entityhuman) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityLectern) {
            entityhuman.openContainer((TileEntityLectern) tileentity);
            entityhuman.a(StatisticList.INTERACT_WITH_LECTERN);
        }

    }

    @Override
    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        return false;
    }
}
