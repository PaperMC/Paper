package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

public class BlockRedstoneWire extends Block {

    public static final BlockStateEnum<BlockPropertyRedstoneSide> NORTH = BlockProperties.X;
    public static final BlockStateEnum<BlockPropertyRedstoneSide> EAST = BlockProperties.W;
    public static final BlockStateEnum<BlockPropertyRedstoneSide> SOUTH = BlockProperties.Y;
    public static final BlockStateEnum<BlockPropertyRedstoneSide> WEST = BlockProperties.Z;
    public static final BlockStateInteger POWER = BlockProperties.az;
    public static final Map<EnumDirection, BlockStateEnum<BlockPropertyRedstoneSide>> f = Maps.newEnumMap(ImmutableMap.of(EnumDirection.NORTH, BlockRedstoneWire.NORTH, EnumDirection.EAST, BlockRedstoneWire.EAST, EnumDirection.SOUTH, BlockRedstoneWire.SOUTH, EnumDirection.WEST, BlockRedstoneWire.WEST));
    private static final VoxelShape g = Block.a(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D);
    private static final Map<EnumDirection, VoxelShape> h = Maps.newEnumMap(ImmutableMap.of(EnumDirection.NORTH, Block.a(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), EnumDirection.SOUTH, Block.a(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), EnumDirection.EAST, Block.a(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), EnumDirection.WEST, Block.a(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D)));
    private static final Map<EnumDirection, VoxelShape> i = Maps.newEnumMap(ImmutableMap.of(EnumDirection.NORTH, VoxelShapes.a((VoxelShape) BlockRedstoneWire.h.get(EnumDirection.NORTH), Block.a(3.0D, 0.0D, 0.0D, 13.0D, 16.0D, 1.0D)), EnumDirection.SOUTH, VoxelShapes.a((VoxelShape) BlockRedstoneWire.h.get(EnumDirection.SOUTH), Block.a(3.0D, 0.0D, 15.0D, 13.0D, 16.0D, 16.0D)), EnumDirection.EAST, VoxelShapes.a((VoxelShape) BlockRedstoneWire.h.get(EnumDirection.EAST), Block.a(15.0D, 0.0D, 3.0D, 16.0D, 16.0D, 13.0D)), EnumDirection.WEST, VoxelShapes.a((VoxelShape) BlockRedstoneWire.h.get(EnumDirection.WEST), Block.a(0.0D, 0.0D, 3.0D, 1.0D, 16.0D, 13.0D))));
    private final Map<IBlockData, VoxelShape> j = Maps.newHashMap();
    private static final Vector3fa[] k = new Vector3fa[16];
    private final IBlockData o;
    private boolean p = true;

    public BlockRedstoneWire(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockRedstoneWire.NORTH, BlockPropertyRedstoneSide.NONE)).set(BlockRedstoneWire.EAST, BlockPropertyRedstoneSide.NONE)).set(BlockRedstoneWire.SOUTH, BlockPropertyRedstoneSide.NONE)).set(BlockRedstoneWire.WEST, BlockPropertyRedstoneSide.NONE)).set(BlockRedstoneWire.POWER, 0));
        this.o = (IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.getBlockData().set(BlockRedstoneWire.NORTH, BlockPropertyRedstoneSide.SIDE)).set(BlockRedstoneWire.EAST, BlockPropertyRedstoneSide.SIDE)).set(BlockRedstoneWire.SOUTH, BlockPropertyRedstoneSide.SIDE)).set(BlockRedstoneWire.WEST, BlockPropertyRedstoneSide.SIDE);
        UnmodifiableIterator unmodifiableiterator = this.getStates().a().iterator();

        while (unmodifiableiterator.hasNext()) {
            IBlockData iblockdata = (IBlockData) unmodifiableiterator.next();

            if ((Integer) iblockdata.get(BlockRedstoneWire.POWER) == 0) {
                this.j.put(iblockdata, this.l(iblockdata));
            }
        }

    }

    private VoxelShape l(IBlockData iblockdata) {
        VoxelShape voxelshape = BlockRedstoneWire.g;
        Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumDirection enumdirection = (EnumDirection) iterator.next();
            BlockPropertyRedstoneSide blockpropertyredstoneside = (BlockPropertyRedstoneSide) iblockdata.get((IBlockState) BlockRedstoneWire.f.get(enumdirection));

            if (blockpropertyredstoneside == BlockPropertyRedstoneSide.SIDE) {
                voxelshape = VoxelShapes.a(voxelshape, (VoxelShape) BlockRedstoneWire.h.get(enumdirection));
            } else if (blockpropertyredstoneside == BlockPropertyRedstoneSide.UP) {
                voxelshape = VoxelShapes.a(voxelshape, (VoxelShape) BlockRedstoneWire.i.get(enumdirection));
            }
        }

        return voxelshape;
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return (VoxelShape) this.j.get(iblockdata.set(BlockRedstoneWire.POWER, 0));
    }

    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        return this.a((IBlockAccess) blockactioncontext.getWorld(), this.o, blockactioncontext.getClickPosition());
    }

    private IBlockData a(IBlockAccess iblockaccess, IBlockData iblockdata, BlockPosition blockposition) {
        boolean flag = n(iblockdata);

        iblockdata = this.b(iblockaccess, (IBlockData) this.getBlockData().set(BlockRedstoneWire.POWER, iblockdata.get(BlockRedstoneWire.POWER)), blockposition);
        if (flag && n(iblockdata)) {
            return iblockdata;
        } else {
            boolean flag1 = ((BlockPropertyRedstoneSide) iblockdata.get(BlockRedstoneWire.NORTH)).b();
            boolean flag2 = ((BlockPropertyRedstoneSide) iblockdata.get(BlockRedstoneWire.SOUTH)).b();
            boolean flag3 = ((BlockPropertyRedstoneSide) iblockdata.get(BlockRedstoneWire.EAST)).b();
            boolean flag4 = ((BlockPropertyRedstoneSide) iblockdata.get(BlockRedstoneWire.WEST)).b();
            boolean flag5 = !flag1 && !flag2;
            boolean flag6 = !flag3 && !flag4;

            if (!flag4 && flag5) {
                iblockdata = (IBlockData) iblockdata.set(BlockRedstoneWire.WEST, BlockPropertyRedstoneSide.SIDE);
            }

            if (!flag3 && flag5) {
                iblockdata = (IBlockData) iblockdata.set(BlockRedstoneWire.EAST, BlockPropertyRedstoneSide.SIDE);
            }

            if (!flag1 && flag6) {
                iblockdata = (IBlockData) iblockdata.set(BlockRedstoneWire.NORTH, BlockPropertyRedstoneSide.SIDE);
            }

            if (!flag2 && flag6) {
                iblockdata = (IBlockData) iblockdata.set(BlockRedstoneWire.SOUTH, BlockPropertyRedstoneSide.SIDE);
            }

            return iblockdata;
        }
    }

    private IBlockData b(IBlockAccess iblockaccess, IBlockData iblockdata, BlockPosition blockposition) {
        boolean flag = !iblockaccess.getType(blockposition.up()).isOccluding(iblockaccess, blockposition);
        Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumDirection enumdirection = (EnumDirection) iterator.next();

            if (!((BlockPropertyRedstoneSide) iblockdata.get((IBlockState) BlockRedstoneWire.f.get(enumdirection))).b()) {
                BlockPropertyRedstoneSide blockpropertyredstoneside = this.a(iblockaccess, blockposition, enumdirection, flag);

                iblockdata = (IBlockData) iblockdata.set((IBlockState) BlockRedstoneWire.f.get(enumdirection), blockpropertyredstoneside);
            }
        }

        return iblockdata;
    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        if (enumdirection == EnumDirection.DOWN) {
            return iblockdata;
        } else if (enumdirection == EnumDirection.UP) {
            return this.a((IBlockAccess) generatoraccess, iblockdata, blockposition);
        } else {
            BlockPropertyRedstoneSide blockpropertyredstoneside = this.a((IBlockAccess) generatoraccess, blockposition, enumdirection);

            return blockpropertyredstoneside.b() == ((BlockPropertyRedstoneSide) iblockdata.get((IBlockState) BlockRedstoneWire.f.get(enumdirection))).b() && !m(iblockdata) ? (IBlockData) iblockdata.set((IBlockState) BlockRedstoneWire.f.get(enumdirection), blockpropertyredstoneside) : this.a((IBlockAccess) generatoraccess, (IBlockData) ((IBlockData) this.o.set(BlockRedstoneWire.POWER, iblockdata.get(BlockRedstoneWire.POWER))).set((IBlockState) BlockRedstoneWire.f.get(enumdirection), blockpropertyredstoneside), blockposition);
        }
    }

    private static boolean m(IBlockData iblockdata) {
        return ((BlockPropertyRedstoneSide) iblockdata.get(BlockRedstoneWire.NORTH)).b() && ((BlockPropertyRedstoneSide) iblockdata.get(BlockRedstoneWire.SOUTH)).b() && ((BlockPropertyRedstoneSide) iblockdata.get(BlockRedstoneWire.EAST)).b() && ((BlockPropertyRedstoneSide) iblockdata.get(BlockRedstoneWire.WEST)).b();
    }

    private static boolean n(IBlockData iblockdata) {
        return !((BlockPropertyRedstoneSide) iblockdata.get(BlockRedstoneWire.NORTH)).b() && !((BlockPropertyRedstoneSide) iblockdata.get(BlockRedstoneWire.SOUTH)).b() && !((BlockPropertyRedstoneSide) iblockdata.get(BlockRedstoneWire.EAST)).b() && !((BlockPropertyRedstoneSide) iblockdata.get(BlockRedstoneWire.WEST)).b();
    }

    @Override
    public void a(IBlockData iblockdata, GeneratorAccess generatoraccess, BlockPosition blockposition, int i, int j) {
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
        Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumDirection enumdirection = (EnumDirection) iterator.next();
            BlockPropertyRedstoneSide blockpropertyredstoneside = (BlockPropertyRedstoneSide) iblockdata.get((IBlockState) BlockRedstoneWire.f.get(enumdirection));

            if (blockpropertyredstoneside != BlockPropertyRedstoneSide.NONE && !generatoraccess.getType(blockposition_mutableblockposition.a((BaseBlockPosition) blockposition, enumdirection)).a((Block) this)) {
                blockposition_mutableblockposition.c(EnumDirection.DOWN);
                IBlockData iblockdata1 = generatoraccess.getType(blockposition_mutableblockposition);

                if (!iblockdata1.a(Blocks.OBSERVER)) {
                    BlockPosition blockposition1 = blockposition_mutableblockposition.shift(enumdirection.opposite());
                    IBlockData iblockdata2 = iblockdata1.updateState(enumdirection.opposite(), generatoraccess.getType(blockposition1), generatoraccess, blockposition_mutableblockposition, blockposition1);

                    a(iblockdata1, iblockdata2, generatoraccess, blockposition_mutableblockposition, i, j);
                }

                blockposition_mutableblockposition.a((BaseBlockPosition) blockposition, enumdirection).c(EnumDirection.UP);
                IBlockData iblockdata3 = generatoraccess.getType(blockposition_mutableblockposition);

                if (!iblockdata3.a(Blocks.OBSERVER)) {
                    BlockPosition blockposition2 = blockposition_mutableblockposition.shift(enumdirection.opposite());
                    IBlockData iblockdata4 = iblockdata3.updateState(enumdirection.opposite(), generatoraccess.getType(blockposition2), generatoraccess, blockposition_mutableblockposition, blockposition2);

                    a(iblockdata3, iblockdata4, generatoraccess, blockposition_mutableblockposition, i, j);
                }
            }
        }

    }

    private BlockPropertyRedstoneSide a(IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return this.a(iblockaccess, blockposition, enumdirection, !iblockaccess.getType(blockposition.up()).isOccluding(iblockaccess, blockposition));
    }

    private BlockPropertyRedstoneSide a(IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection, boolean flag) {
        BlockPosition blockposition1 = blockposition.shift(enumdirection);
        IBlockData iblockdata = iblockaccess.getType(blockposition1);

        if (flag) {
            boolean flag1 = this.b(iblockaccess, blockposition1, iblockdata);

            if (flag1 && h(iblockaccess.getType(blockposition1.up()))) {
                if (iblockdata.d(iblockaccess, blockposition1, enumdirection.opposite())) {
                    return BlockPropertyRedstoneSide.UP;
                }

                return BlockPropertyRedstoneSide.SIDE;
            }
        }

        return !a(iblockdata, enumdirection) && (iblockdata.isOccluding(iblockaccess, blockposition1) || !h(iblockaccess.getType(blockposition1.down()))) ? BlockPropertyRedstoneSide.NONE : BlockPropertyRedstoneSide.SIDE;
    }

    @Override
    public boolean canPlace(IBlockData iblockdata, IWorldReader iworldreader, BlockPosition blockposition) {
        BlockPosition blockposition1 = blockposition.down();
        IBlockData iblockdata1 = iworldreader.getType(blockposition1);

        return this.b((IBlockAccess) iworldreader, blockposition1, iblockdata1);
    }

    private boolean b(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata) {
        return iblockdata.d(iblockaccess, blockposition, EnumDirection.UP) || iblockdata.a(Blocks.HOPPER);
    }

    private void a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        int i = this.a(world, blockposition);

        if ((Integer) iblockdata.get(BlockRedstoneWire.POWER) != i) {
            if (world.getType(blockposition) == iblockdata) {
                world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockRedstoneWire.POWER, i), 2);
            }

            Set<BlockPosition> set = Sets.newHashSet();

            set.add(blockposition);
            EnumDirection[] aenumdirection = EnumDirection.values();
            int j = aenumdirection.length;

            for (int k = 0; k < j; ++k) {
                EnumDirection enumdirection = aenumdirection[k];

                set.add(blockposition.shift(enumdirection));
            }

            Iterator iterator = set.iterator();

            while (iterator.hasNext()) {
                BlockPosition blockposition1 = (BlockPosition) iterator.next();

                world.applyPhysics(blockposition1, this);
            }
        }

    }

    private int a(World world, BlockPosition blockposition) {
        this.p = false;
        int i = world.s(blockposition);

        this.p = true;
        int j = 0;

        if (i < 15) {
            Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

            while (iterator.hasNext()) {
                EnumDirection enumdirection = (EnumDirection) iterator.next();
                BlockPosition blockposition1 = blockposition.shift(enumdirection);
                IBlockData iblockdata = world.getType(blockposition1);

                j = Math.max(j, this.o(iblockdata));
                BlockPosition blockposition2 = blockposition.up();

                if (iblockdata.isOccluding(world, blockposition1) && !world.getType(blockposition2).isOccluding(world, blockposition2)) {
                    j = Math.max(j, this.o(world.getType(blockposition1.up())));
                } else if (!iblockdata.isOccluding(world, blockposition1)) {
                    j = Math.max(j, this.o(world.getType(blockposition1.down())));
                }
            }
        }

        return Math.max(i, j - 1);
    }

    private int o(IBlockData iblockdata) {
        return iblockdata.a((Block) this) ? (Integer) iblockdata.get(BlockRedstoneWire.POWER) : 0;
    }

    private void b(World world, BlockPosition blockposition) {
        if (world.getType(blockposition).a((Block) this)) {
            world.applyPhysics(blockposition, this);
            EnumDirection[] aenumdirection = EnumDirection.values();
            int i = aenumdirection.length;

            for (int j = 0; j < i; ++j) {
                EnumDirection enumdirection = aenumdirection[j];

                world.applyPhysics(blockposition.shift(enumdirection), this);
            }

        }
    }

    @Override
    public void onPlace(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if (!iblockdata1.a(iblockdata.getBlock()) && !world.isClientSide) {
            this.a(world, blockposition, iblockdata);
            Iterator iterator = EnumDirection.EnumDirectionLimit.VERTICAL.iterator();

            while (iterator.hasNext()) {
                EnumDirection enumdirection = (EnumDirection) iterator.next();

                world.applyPhysics(blockposition.shift(enumdirection), this);
            }

            this.d(world, blockposition);
        }
    }

    @Override
    public void remove(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if (!flag && !iblockdata.a(iblockdata1.getBlock())) {
            super.remove(iblockdata, world, blockposition, iblockdata1, flag);
            if (!world.isClientSide) {
                EnumDirection[] aenumdirection = EnumDirection.values();
                int i = aenumdirection.length;

                for (int j = 0; j < i; ++j) {
                    EnumDirection enumdirection = aenumdirection[j];

                    world.applyPhysics(blockposition.shift(enumdirection), this);
                }

                this.a(world, blockposition, iblockdata);
                this.d(world, blockposition);
            }
        }
    }

    private void d(World world, BlockPosition blockposition) {
        Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

        EnumDirection enumdirection;

        while (iterator.hasNext()) {
            enumdirection = (EnumDirection) iterator.next();
            this.b(world, blockposition.shift(enumdirection));
        }

        iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            enumdirection = (EnumDirection) iterator.next();
            BlockPosition blockposition1 = blockposition.shift(enumdirection);

            if (world.getType(blockposition1).isOccluding(world, blockposition1)) {
                this.b(world, blockposition1.up());
            } else {
                this.b(world, blockposition1.down());
            }
        }

    }

    @Override
    public void doPhysics(IBlockData iblockdata, World world, BlockPosition blockposition, Block block, BlockPosition blockposition1, boolean flag) {
        if (!world.isClientSide) {
            if (iblockdata.canPlace(world, blockposition)) {
                this.a(world, blockposition, iblockdata);
            } else {
                c(iblockdata, world, blockposition);
                world.a(blockposition, false);
            }

        }
    }

    @Override
    public int b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return !this.p ? 0 : iblockdata.b(iblockaccess, blockposition, enumdirection);
    }

    @Override
    public int a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        if (this.p && enumdirection != EnumDirection.DOWN) {
            int i = (Integer) iblockdata.get(BlockRedstoneWire.POWER);

            return i == 0 ? 0 : (enumdirection != EnumDirection.UP && !((BlockPropertyRedstoneSide) this.a(iblockaccess, iblockdata, blockposition).get((IBlockState) BlockRedstoneWire.f.get(enumdirection.opposite()))).b() ? 0 : i);
        } else {
            return 0;
        }
    }

    protected static boolean h(IBlockData iblockdata) {
        return a(iblockdata, (EnumDirection) null);
    }

    protected static boolean a(IBlockData iblockdata, @Nullable EnumDirection enumdirection) {
        if (iblockdata.a(Blocks.REDSTONE_WIRE)) {
            return true;
        } else if (iblockdata.a(Blocks.REPEATER)) {
            EnumDirection enumdirection1 = (EnumDirection) iblockdata.get(BlockRepeater.FACING);

            return enumdirection1 == enumdirection || enumdirection1.opposite() == enumdirection;
        } else {
            return iblockdata.a(Blocks.OBSERVER) ? enumdirection == iblockdata.get(BlockObserver.FACING) : iblockdata.isPowerSource() && enumdirection != null;
        }
    }

    @Override
    public boolean isPowerSource(IBlockData iblockdata) {
        return this.p;
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        switch (enumblockrotation) {
            case CLOCKWISE_180:
                return (IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) iblockdata.set(BlockRedstoneWire.NORTH, iblockdata.get(BlockRedstoneWire.SOUTH))).set(BlockRedstoneWire.EAST, iblockdata.get(BlockRedstoneWire.WEST))).set(BlockRedstoneWire.SOUTH, iblockdata.get(BlockRedstoneWire.NORTH))).set(BlockRedstoneWire.WEST, iblockdata.get(BlockRedstoneWire.EAST));
            case COUNTERCLOCKWISE_90:
                return (IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) iblockdata.set(BlockRedstoneWire.NORTH, iblockdata.get(BlockRedstoneWire.EAST))).set(BlockRedstoneWire.EAST, iblockdata.get(BlockRedstoneWire.SOUTH))).set(BlockRedstoneWire.SOUTH, iblockdata.get(BlockRedstoneWire.WEST))).set(BlockRedstoneWire.WEST, iblockdata.get(BlockRedstoneWire.NORTH));
            case CLOCKWISE_90:
                return (IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) iblockdata.set(BlockRedstoneWire.NORTH, iblockdata.get(BlockRedstoneWire.WEST))).set(BlockRedstoneWire.EAST, iblockdata.get(BlockRedstoneWire.NORTH))).set(BlockRedstoneWire.SOUTH, iblockdata.get(BlockRedstoneWire.EAST))).set(BlockRedstoneWire.WEST, iblockdata.get(BlockRedstoneWire.SOUTH));
            default:
                return iblockdata;
        }
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        switch (enumblockmirror) {
            case LEFT_RIGHT:
                return (IBlockData) ((IBlockData) iblockdata.set(BlockRedstoneWire.NORTH, iblockdata.get(BlockRedstoneWire.SOUTH))).set(BlockRedstoneWire.SOUTH, iblockdata.get(BlockRedstoneWire.NORTH));
            case FRONT_BACK:
                return (IBlockData) ((IBlockData) iblockdata.set(BlockRedstoneWire.EAST, iblockdata.get(BlockRedstoneWire.WEST))).set(BlockRedstoneWire.WEST, iblockdata.get(BlockRedstoneWire.EAST));
            default:
                return super.a(iblockdata, enumblockmirror);
        }
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockRedstoneWire.NORTH, BlockRedstoneWire.EAST, BlockRedstoneWire.SOUTH, BlockRedstoneWire.WEST, BlockRedstoneWire.POWER);
    }

    @Override
    public EnumInteractionResult interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        if (!entityhuman.abilities.mayBuild) {
            return EnumInteractionResult.PASS;
        } else {
            if (m(iblockdata) || n(iblockdata)) {
                IBlockData iblockdata1 = m(iblockdata) ? this.getBlockData() : this.o;

                iblockdata1 = (IBlockData) iblockdata1.set(BlockRedstoneWire.POWER, iblockdata.get(BlockRedstoneWire.POWER));
                iblockdata1 = this.a((IBlockAccess) world, iblockdata1, blockposition);
                if (iblockdata1 != iblockdata) {
                    world.setTypeAndData(blockposition, iblockdata1, 3);
                    this.a(world, blockposition, iblockdata, iblockdata1);
                    return EnumInteractionResult.SUCCESS;
                }
            }

            return EnumInteractionResult.PASS;
        }
    }

    private void a(World world, BlockPosition blockposition, IBlockData iblockdata, IBlockData iblockdata1) {
        Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumDirection enumdirection = (EnumDirection) iterator.next();
            BlockPosition blockposition1 = blockposition.shift(enumdirection);

            if (((BlockPropertyRedstoneSide) iblockdata.get((IBlockState) BlockRedstoneWire.f.get(enumdirection))).b() != ((BlockPropertyRedstoneSide) iblockdata1.get((IBlockState) BlockRedstoneWire.f.get(enumdirection))).b() && world.getType(blockposition1).isOccluding(world, blockposition1)) {
                world.a(blockposition1, iblockdata1.getBlock(), enumdirection.opposite());
            }
        }

    }

    static {
        for (int i = 0; i <= 15; ++i) {
            float f = (float) i / 15.0F;
            float f1 = f * 0.6F + (f > 0.0F ? 0.4F : 0.3F);
            float f2 = MathHelper.a(f * f * 0.7F - 0.5F, 0.0F, 1.0F);
            float f3 = MathHelper.a(f * f * 0.6F - 0.7F, 0.0F, 1.0F);

            BlockRedstoneWire.k[i] = new Vector3fa(f1, f2, f3);
        }

    }
}
