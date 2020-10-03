package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import javax.annotation.Nullable;

public class BlockChest extends BlockChestAbstract<TileEntityChest> implements IBlockWaterlogged {

    public static final BlockStateDirection FACING = BlockFacingHorizontal.FACING;
    public static final BlockStateEnum<BlockPropertyChestType> c = BlockProperties.aF;
    public static final BlockStateBoolean d = BlockProperties.C;
    protected static final VoxelShape e = Block.a(1.0D, 0.0D, 0.0D, 15.0D, 14.0D, 15.0D);
    protected static final VoxelShape f = Block.a(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 16.0D);
    protected static final VoxelShape g = Block.a(0.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
    protected static final VoxelShape h = Block.a(1.0D, 0.0D, 1.0D, 16.0D, 14.0D, 15.0D);
    protected static final VoxelShape i = Block.a(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
    private static final DoubleBlockFinder.Combiner<TileEntityChest, Optional<IInventory>> j = new DoubleBlockFinder.Combiner<TileEntityChest, Optional<IInventory>>() {
        public Optional<IInventory> a(TileEntityChest tileentitychest, TileEntityChest tileentitychest1) {
            return Optional.of(new InventoryLargeChest(tileentitychest, tileentitychest1));
        }

        public Optional<IInventory> a(TileEntityChest tileentitychest) {
            return Optional.of(tileentitychest);
        }

        @Override
        public Optional<IInventory> b() {
            return Optional.empty();
        }
    };
    private static final DoubleBlockFinder.Combiner<TileEntityChest, Optional<ITileInventory>> k = new DoubleBlockFinder.Combiner<TileEntityChest, Optional<ITileInventory>>() {
        public Optional<ITileInventory> a(final TileEntityChest tileentitychest, final TileEntityChest tileentitychest1) {
            final InventoryLargeChest inventorylargechest = new InventoryLargeChest(tileentitychest, tileentitychest1);

            return Optional.of(new DoubleInventory(tileentitychest, tileentitychest1, inventorylargechest)); // CraftBukkit
        }

        public Optional<ITileInventory> a(TileEntityChest tileentitychest) {
            return Optional.of(tileentitychest);
        }

        @Override
        public Optional<ITileInventory> b() {
            return Optional.empty();
        }
    };

    // CraftBukkit start
    public static class DoubleInventory implements ITileInventory {

        private final TileEntityChest tileentitychest;
        private final TileEntityChest tileentitychest1;
        public final InventoryLargeChest inventorylargechest;

        public DoubleInventory(TileEntityChest tileentitychest, TileEntityChest tileentitychest1, InventoryLargeChest inventorylargechest) {
            this.tileentitychest = tileentitychest;
            this.tileentitychest1 = tileentitychest1;
            this.inventorylargechest = inventorylargechest;
        }

        @Nullable
        @Override
        public Container createMenu(int i, PlayerInventory playerinventory, EntityHuman entityhuman) {
            if (tileentitychest.e(entityhuman) && tileentitychest1.e(entityhuman)) {
                tileentitychest.d(playerinventory.player);
                tileentitychest1.d(playerinventory.player);
                return ContainerChest.b(i, playerinventory, inventorylargechest);
            } else {
                return null;
            }
        }

        @Override
        public IChatBaseComponent getScoreboardDisplayName() {
            return (IChatBaseComponent) (tileentitychest.hasCustomName() ? tileentitychest.getScoreboardDisplayName() : (tileentitychest1.hasCustomName() ? tileentitychest1.getScoreboardDisplayName() : new ChatMessage("container.chestDouble")));
        }
    };
    // CraftBukkit end

    protected BlockChest(BlockBase.Info blockbase_info, Supplier<TileEntityTypes<? extends TileEntityChest>> supplier) {
        super(blockbase_info, supplier);
        this.j((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockChest.FACING, EnumDirection.NORTH)).set(BlockChest.c, BlockPropertyChestType.SINGLE)).set(BlockChest.d, false));
    }

    public static DoubleBlockFinder.BlockType g(IBlockData iblockdata) {
        BlockPropertyChestType blockpropertychesttype = (BlockPropertyChestType) iblockdata.get(BlockChest.c);

        return blockpropertychesttype == BlockPropertyChestType.SINGLE ? DoubleBlockFinder.BlockType.SINGLE : (blockpropertychesttype == BlockPropertyChestType.RIGHT ? DoubleBlockFinder.BlockType.FIRST : DoubleBlockFinder.BlockType.SECOND);
    }

    @Override
    public EnumRenderType b(IBlockData iblockdata) {
        return EnumRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        if ((Boolean) iblockdata.get(BlockChest.d)) {
            generatoraccess.getFluidTickList().a(blockposition, FluidTypes.WATER, FluidTypes.WATER.a((IWorldReader) generatoraccess));
        }

        if (iblockdata1.a((Block) this) && enumdirection.n().d()) {
            BlockPropertyChestType blockpropertychesttype = (BlockPropertyChestType) iblockdata1.get(BlockChest.c);

            if (iblockdata.get(BlockChest.c) == BlockPropertyChestType.SINGLE && blockpropertychesttype != BlockPropertyChestType.SINGLE && iblockdata.get(BlockChest.FACING) == iblockdata1.get(BlockChest.FACING) && h(iblockdata1) == enumdirection.opposite()) {
                return (IBlockData) iblockdata.set(BlockChest.c, blockpropertychesttype.b());
            }
        } else if (h(iblockdata) == enumdirection) {
            return (IBlockData) iblockdata.set(BlockChest.c, BlockPropertyChestType.SINGLE);
        }

        return super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        if (iblockdata.get(BlockChest.c) == BlockPropertyChestType.SINGLE) {
            return BlockChest.i;
        } else {
            switch (h(iblockdata)) {
                case NORTH:
                default:
                    return BlockChest.e;
                case SOUTH:
                    return BlockChest.f;
                case WEST:
                    return BlockChest.g;
                case EAST:
                    return BlockChest.h;
            }
        }
    }

    public static EnumDirection h(IBlockData iblockdata) {
        EnumDirection enumdirection = (EnumDirection) iblockdata.get(BlockChest.FACING);

        return iblockdata.get(BlockChest.c) == BlockPropertyChestType.LEFT ? enumdirection.g() : enumdirection.h();
    }

    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        BlockPropertyChestType blockpropertychesttype = BlockPropertyChestType.SINGLE;
        EnumDirection enumdirection = blockactioncontext.f().opposite();
        Fluid fluid = blockactioncontext.getWorld().getFluid(blockactioncontext.getClickPosition());
        boolean flag = blockactioncontext.isSneaking();
        EnumDirection enumdirection1 = blockactioncontext.getClickedFace();

        if (enumdirection1.n().d() && flag) {
            EnumDirection enumdirection2 = this.a(blockactioncontext, enumdirection1.opposite());

            if (enumdirection2 != null && enumdirection2.n() != enumdirection1.n()) {
                enumdirection = enumdirection2;
                blockpropertychesttype = enumdirection2.h() == enumdirection1.opposite() ? BlockPropertyChestType.RIGHT : BlockPropertyChestType.LEFT;
            }
        }

        if (blockpropertychesttype == BlockPropertyChestType.SINGLE && !flag) {
            if (enumdirection == this.a(blockactioncontext, enumdirection.g())) {
                blockpropertychesttype = BlockPropertyChestType.LEFT;
            } else if (enumdirection == this.a(blockactioncontext, enumdirection.h())) {
                blockpropertychesttype = BlockPropertyChestType.RIGHT;
            }
        }

        return (IBlockData) ((IBlockData) ((IBlockData) this.getBlockData().set(BlockChest.FACING, enumdirection)).set(BlockChest.c, blockpropertychesttype)).set(BlockChest.d, fluid.getType() == FluidTypes.WATER);
    }

    @Override
    public Fluid d(IBlockData iblockdata) {
        return (Boolean) iblockdata.get(BlockChest.d) ? FluidTypes.WATER.a(false) : super.d(iblockdata);
    }

    @Nullable
    private EnumDirection a(BlockActionContext blockactioncontext, EnumDirection enumdirection) {
        IBlockData iblockdata = blockactioncontext.getWorld().getType(blockactioncontext.getClickPosition().shift(enumdirection));

        return iblockdata.a((Block) this) && iblockdata.get(BlockChest.c) == BlockPropertyChestType.SINGLE ? (EnumDirection) iblockdata.get(BlockChest.FACING) : null;
    }

    @Override
    public void postPlace(World world, BlockPosition blockposition, IBlockData iblockdata, EntityLiving entityliving, ItemStack itemstack) {
        if (itemstack.hasName()) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityChest) {
                ((TileEntityChest) tileentity).setCustomName(itemstack.getName());
            }
        }

    }

    @Override
    public void remove(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if (!iblockdata.a(iblockdata1.getBlock())) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof IInventory) {
                InventoryUtils.dropInventory(world, blockposition, (IInventory) tileentity);
                world.updateAdjacentComparators(blockposition, this);
            }

            super.remove(iblockdata, world, blockposition, iblockdata1, flag);
        }
    }

    @Override
    public EnumInteractionResult interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        if (world.isClientSide) {
            return EnumInteractionResult.SUCCESS;
        } else {
            ITileInventory itileinventory = this.getInventory(iblockdata, world, blockposition);

            if (itileinventory != null) {
                entityhuman.openContainer(itileinventory);
                entityhuman.b(this.c());
                PiglinAI.a(entityhuman, true);
            }

            return EnumInteractionResult.CONSUME;
        }
    }

    protected Statistic<MinecraftKey> c() {
        return StatisticList.CUSTOM.b(StatisticList.OPEN_CHEST);
    }

    @Nullable
    public static IInventory getInventory(BlockChest blockchest, IBlockData iblockdata, World world, BlockPosition blockposition, boolean flag) {
        return (IInventory) ((Optional) blockchest.a(iblockdata, world, blockposition, flag).apply(BlockChest.j)).orElse((Object) null);
    }

    public DoubleBlockFinder.Result<? extends TileEntityChest> a(IBlockData iblockdata, World world, BlockPosition blockposition, boolean flag) {
        BiPredicate<GeneratorAccess, BlockPosition> bipredicate; // CraftBukkit - decompile error

        if (flag) {
            bipredicate = (generatoraccess, blockposition1) -> {
                return false;
            };
        } else {
            bipredicate = BlockChest::a;
        }

        return DoubleBlockFinder.a((TileEntityTypes) this.a.get(), BlockChest::g, BlockChest::h, BlockChest.FACING, iblockdata, world, blockposition, bipredicate);
    }

    @Nullable
    @Override
    public ITileInventory getInventory(IBlockData iblockdata, World world, BlockPosition blockposition) {
        return (ITileInventory) ((Optional) this.a(iblockdata, world, blockposition, false).apply(BlockChest.k)).orElse((Object) null);
    }

    @Override
    public TileEntity createTile(IBlockAccess iblockaccess) {
        return new TileEntityChest();
    }

    public static boolean a(GeneratorAccess generatoraccess, BlockPosition blockposition) {
        return a((IBlockAccess) generatoraccess, blockposition) || b(generatoraccess, blockposition);
    }

    private static boolean a(IBlockAccess iblockaccess, BlockPosition blockposition) {
        BlockPosition blockposition1 = blockposition.up();

        return iblockaccess.getType(blockposition1).isOccluding(iblockaccess, blockposition1);
    }

    private static boolean b(GeneratorAccess generatoraccess, BlockPosition blockposition) {
        List<EntityCat> list = generatoraccess.a(EntityCat.class, new AxisAlignedBB((double) blockposition.getX(), (double) (blockposition.getY() + 1), (double) blockposition.getZ(), (double) (blockposition.getX() + 1), (double) (blockposition.getY() + 2), (double) (blockposition.getZ() + 1)));

        if (!list.isEmpty()) {
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityCat entitycat = (EntityCat) iterator.next();

                if (entitycat.isSitting()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isComplexRedstone(IBlockData iblockdata) {
        return true;
    }

    @Override
    public int a(IBlockData iblockdata, World world, BlockPosition blockposition) {
        return Container.b(getInventory(this, iblockdata, world, blockposition, false));
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        return (IBlockData) iblockdata.set(BlockChest.FACING, enumblockrotation.a((EnumDirection) iblockdata.get(BlockChest.FACING)));
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        return iblockdata.a(enumblockmirror.a((EnumDirection) iblockdata.get(BlockChest.FACING)));
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockChest.FACING, BlockChest.c, BlockChest.d);
    }

    @Override
    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        return false;
    }
}
