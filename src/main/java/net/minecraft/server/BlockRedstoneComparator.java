package net.minecraft.server;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public class BlockRedstoneComparator extends BlockDiodeAbstract implements ITileEntity {

    public static final BlockStateEnum<BlockPropertyComparatorMode> MODE = BlockProperties.aG;

    public BlockRedstoneComparator(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockRedstoneComparator.FACING, EnumDirection.NORTH)).set(BlockRedstoneComparator.c, false)).set(BlockRedstoneComparator.MODE, BlockPropertyComparatorMode.COMPARE));
    }

    @Override
    protected int g(IBlockData iblockdata) {
        return 2;
    }

    @Override
    protected int b(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata) {
        TileEntity tileentity = iblockaccess.getTileEntity(blockposition);

        return tileentity instanceof TileEntityComparator ? ((TileEntityComparator) tileentity).d() : 0;
    }

    private int e(World world, BlockPosition blockposition, IBlockData iblockdata) {
        return iblockdata.get(BlockRedstoneComparator.MODE) == BlockPropertyComparatorMode.SUBTRACT ? Math.max(this.b(world, blockposition, iblockdata) - this.b((IWorldReader) world, blockposition, iblockdata), 0) : this.b(world, blockposition, iblockdata);
    }

    @Override
    protected boolean a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        int i = this.b(world, blockposition, iblockdata);

        if (i == 0) {
            return false;
        } else {
            int j = this.b((IWorldReader) world, blockposition, iblockdata);

            return i > j ? true : i == j && iblockdata.get(BlockRedstoneComparator.MODE) == BlockPropertyComparatorMode.COMPARE;
        }
    }

    @Override
    protected int b(World world, BlockPosition blockposition, IBlockData iblockdata) {
        int i = super.b(world, blockposition, iblockdata);
        EnumDirection enumdirection = (EnumDirection) iblockdata.get(BlockRedstoneComparator.FACING);
        BlockPosition blockposition1 = blockposition.shift(enumdirection);
        IBlockData iblockdata1 = world.getType(blockposition1);

        if (iblockdata1.isComplexRedstone()) {
            i = iblockdata1.a(world, blockposition1);
        } else if (i < 15 && iblockdata1.isOccluding(world, blockposition1)) {
            blockposition1 = blockposition1.shift(enumdirection);
            iblockdata1 = world.getType(blockposition1);
            EntityItemFrame entityitemframe = this.a(world, enumdirection, blockposition1);
            int j = Math.max(entityitemframe == null ? Integer.MIN_VALUE : entityitemframe.q(), iblockdata1.isComplexRedstone() ? iblockdata1.a(world, blockposition1) : Integer.MIN_VALUE);

            if (j != Integer.MIN_VALUE) {
                i = j;
            }
        }

        return i;
    }

    @Nullable
    private EntityItemFrame a(World world, EnumDirection enumdirection, BlockPosition blockposition) {
        // CraftBukkit - decompile error
        List<EntityItemFrame> list = world.a(EntityItemFrame.class, new AxisAlignedBB((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ(), (double) (blockposition.getX() + 1), (double) (blockposition.getY() + 1), (double) (blockposition.getZ() + 1)), (java.util.function.Predicate<EntityItemFrame>) (entityitemframe) -> {
            return entityitemframe != null && entityitemframe.getDirection() == enumdirection;
        });

        return list.size() == 1 ? (EntityItemFrame) list.get(0) : null;
    }

    @Override
    public EnumInteractionResult interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        if (!entityhuman.abilities.mayBuild) {
            return EnumInteractionResult.PASS;
        } else {
            iblockdata = (IBlockData) iblockdata.a((IBlockState) BlockRedstoneComparator.MODE);
            float f = iblockdata.get(BlockRedstoneComparator.MODE) == BlockPropertyComparatorMode.SUBTRACT ? 0.55F : 0.5F;

            world.playSound(entityhuman, blockposition, SoundEffects.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, f);
            world.setTypeAndData(blockposition, iblockdata, 2);
            this.f(world, blockposition, iblockdata);
            return EnumInteractionResult.a(world.isClientSide);
        }
    }

    @Override
    protected void c(World world, BlockPosition blockposition, IBlockData iblockdata) {
        if (!world.getBlockTickList().b(blockposition, this)) {
            int i = this.e(world, blockposition, iblockdata);
            TileEntity tileentity = world.getTileEntity(blockposition);
            int j = tileentity instanceof TileEntityComparator ? ((TileEntityComparator) tileentity).d() : 0;

            if (i != j || (Boolean) iblockdata.get(BlockRedstoneComparator.c) != this.a(world, blockposition, iblockdata)) {
                TickListPriority ticklistpriority = this.c((IBlockAccess) world, blockposition, iblockdata) ? TickListPriority.HIGH : TickListPriority.NORMAL;

                world.getBlockTickList().a(blockposition, this, 2, ticklistpriority);
            }

        }
    }

    private void f(World world, BlockPosition blockposition, IBlockData iblockdata) {
        int i = this.e(world, blockposition, iblockdata);
        TileEntity tileentity = world.getTileEntity(blockposition);
        int j = 0;

        if (tileentity instanceof TileEntityComparator) {
            TileEntityComparator tileentitycomparator = (TileEntityComparator) tileentity;

            j = tileentitycomparator.d();
            tileentitycomparator.a(i);
        }

        if (j != i || iblockdata.get(BlockRedstoneComparator.MODE) == BlockPropertyComparatorMode.COMPARE) {
            boolean flag = this.a(world, blockposition, iblockdata);
            boolean flag1 = (Boolean) iblockdata.get(BlockRedstoneComparator.c);

            if (flag1 && !flag) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, blockposition, 15, 0).getNewCurrent() != 0) {
                    return;
                }
                // CraftBukkit end
                world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockRedstoneComparator.c, false), 2);
            } else if (!flag1 && flag) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, blockposition, 0, 15).getNewCurrent() != 15) {
                    return;
                }
                // CraftBukkit end
                world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockRedstoneComparator.c, true), 2);
            }

            this.d(world, blockposition, iblockdata);
        }

    }

    @Override
    public void tickAlways(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        this.f((World) worldserver, blockposition, iblockdata);
    }

    @Override
    public boolean a(IBlockData iblockdata, World world, BlockPosition blockposition, int i, int j) {
        super.a(iblockdata, world, blockposition, i, j);
        TileEntity tileentity = world.getTileEntity(blockposition);

        return tileentity != null && tileentity.setProperty(i, j);
    }

    @Override
    public TileEntity createTile(IBlockAccess iblockaccess) {
        return new TileEntityComparator();
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockRedstoneComparator.FACING, BlockRedstoneComparator.MODE, BlockRedstoneComparator.c);
    }
}
