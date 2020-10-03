package net.minecraft.server;

import com.google.common.base.MoreObjects;
import java.util.Random;
import javax.annotation.Nullable;

import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

public class BlockTripwireHook extends Block {

    public static final BlockStateDirection FACING = BlockFacingHorizontal.FACING;
    public static final BlockStateBoolean POWERED = BlockProperties.w;
    public static final BlockStateBoolean ATTACHED = BlockProperties.a;
    protected static final VoxelShape d = Block.a(5.0D, 0.0D, 10.0D, 11.0D, 10.0D, 16.0D);
    protected static final VoxelShape e = Block.a(5.0D, 0.0D, 0.0D, 11.0D, 10.0D, 6.0D);
    protected static final VoxelShape f = Block.a(10.0D, 0.0D, 5.0D, 16.0D, 10.0D, 11.0D);
    protected static final VoxelShape g = Block.a(0.0D, 0.0D, 5.0D, 6.0D, 10.0D, 11.0D);

    public BlockTripwireHook(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockTripwireHook.FACING, EnumDirection.NORTH)).set(BlockTripwireHook.POWERED, false)).set(BlockTripwireHook.ATTACHED, false));
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        switch ((EnumDirection) iblockdata.get(BlockTripwireHook.FACING)) {
            case EAST:
            default:
                return BlockTripwireHook.g;
            case WEST:
                return BlockTripwireHook.f;
            case SOUTH:
                return BlockTripwireHook.e;
            case NORTH:
                return BlockTripwireHook.d;
        }
    }

    @Override
    public boolean canPlace(IBlockData iblockdata, IWorldReader iworldreader, BlockPosition blockposition) {
        EnumDirection enumdirection = (EnumDirection) iblockdata.get(BlockTripwireHook.FACING);
        BlockPosition blockposition1 = blockposition.shift(enumdirection.opposite());
        IBlockData iblockdata1 = iworldreader.getType(blockposition1);

        return enumdirection.n().d() && iblockdata1.d(iworldreader, blockposition1, enumdirection);
    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        return enumdirection.opposite() == iblockdata.get(BlockTripwireHook.FACING) && !iblockdata.canPlace(generatoraccess, blockposition) ? Blocks.AIR.getBlockData() : super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
    }

    @Nullable
    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        IBlockData iblockdata = (IBlockData) ((IBlockData) this.getBlockData().set(BlockTripwireHook.POWERED, false)).set(BlockTripwireHook.ATTACHED, false);
        World world = blockactioncontext.getWorld();
        BlockPosition blockposition = blockactioncontext.getClickPosition();
        EnumDirection[] aenumdirection = blockactioncontext.e();
        EnumDirection[] aenumdirection1 = aenumdirection;
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumDirection enumdirection = aenumdirection1[j];

            if (enumdirection.n().d()) {
                EnumDirection enumdirection1 = enumdirection.opposite();

                iblockdata = (IBlockData) iblockdata.set(BlockTripwireHook.FACING, enumdirection1);
                if (iblockdata.canPlace(world, blockposition)) {
                    return iblockdata;
                }
            }
        }

        return null;
    }

    @Override
    public void postPlace(World world, BlockPosition blockposition, IBlockData iblockdata, EntityLiving entityliving, ItemStack itemstack) {
        this.a(world, blockposition, iblockdata, false, false, -1, (IBlockData) null);
    }

    public void a(World world, BlockPosition blockposition, IBlockData iblockdata, boolean flag, boolean flag1, int i, @Nullable IBlockData iblockdata1) {
        EnumDirection enumdirection = (EnumDirection) iblockdata.get(BlockTripwireHook.FACING);
        boolean flag2 = (Boolean) iblockdata.get(BlockTripwireHook.ATTACHED);
        boolean flag3 = (Boolean) iblockdata.get(BlockTripwireHook.POWERED);
        boolean flag4 = !flag;
        boolean flag5 = false;
        int j = 0;
        IBlockData[] aiblockdata = new IBlockData[42];

        BlockPosition blockposition1;

        for (int k = 1; k < 42; ++k) {
            blockposition1 = blockposition.shift(enumdirection, k);
            IBlockData iblockdata2 = world.getType(blockposition1);

            if (iblockdata2.a(Blocks.TRIPWIRE_HOOK)) {
                if (iblockdata2.get(BlockTripwireHook.FACING) == enumdirection.opposite()) {
                    j = k;
                }
                break;
            }

            if (!iblockdata2.a(Blocks.TRIPWIRE) && k != i) {
                aiblockdata[k] = null;
                flag4 = false;
            } else {
                if (k == i) {
                    iblockdata2 = (IBlockData) MoreObjects.firstNonNull(iblockdata1, iblockdata2);
                }

                boolean flag6 = !(Boolean) iblockdata2.get(BlockTripwire.DISARMED);
                boolean flag7 = (Boolean) iblockdata2.get(BlockTripwire.POWERED);

                flag5 |= flag6 && flag7;
                aiblockdata[k] = iblockdata2;
                if (k == i) {
                    world.getBlockTickList().a(blockposition, this, 10);
                    flag4 &= flag6;
                }
            }
        }

        flag4 &= j > 1;
        flag5 &= flag4;
        IBlockData iblockdata3 = (IBlockData) ((IBlockData) this.getBlockData().set(BlockTripwireHook.ATTACHED, flag4)).set(BlockTripwireHook.POWERED, flag5);

        if (j > 0) {
            blockposition1 = blockposition.shift(enumdirection, j);
            EnumDirection enumdirection1 = enumdirection.opposite();

            world.setTypeAndData(blockposition1, (IBlockData) iblockdata3.set(BlockTripwireHook.FACING, enumdirection1), 3);
            this.a(world, blockposition1, enumdirection1);
            this.a(world, blockposition1, flag4, flag5, flag2, flag3);
        }

        // CraftBukkit start
        org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

        BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 15, 0);
        world.getServer().getPluginManager().callEvent(eventRedstone);

        if (eventRedstone.getNewCurrent() > 0) {
            return;
        }
        // CraftBukkit end

        this.a(world, blockposition, flag4, flag5, flag2, flag3);
        if (!flag) {
            world.setTypeAndData(blockposition, (IBlockData) iblockdata3.set(BlockTripwireHook.FACING, enumdirection), 3);
            if (flag1) {
                this.a(world, blockposition, enumdirection);
            }
        }

        if (flag2 != flag4) {
            for (int l = 1; l < j; ++l) {
                BlockPosition blockposition2 = blockposition.shift(enumdirection, l);
                IBlockData iblockdata4 = aiblockdata[l];

                if (iblockdata4 != null) {
                    world.setTypeAndData(blockposition2, (IBlockData) iblockdata4.set(BlockTripwireHook.ATTACHED, flag4), 3);
                    if (!world.getType(blockposition2).isAir()) {
                        ;
                    }
                }
            }
        }

    }

    @Override
    public void tickAlways(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        this.a(worldserver, blockposition, iblockdata, false, true, -1, (IBlockData) null);
    }

    private void a(World world, BlockPosition blockposition, boolean flag, boolean flag1, boolean flag2, boolean flag3) {
        if (flag1 && !flag3) {
            world.playSound((EntityHuman) null, blockposition, SoundEffects.BLOCK_TRIPWIRE_CLICK_ON, SoundCategory.BLOCKS, 0.4F, 0.6F);
        } else if (!flag1 && flag3) {
            world.playSound((EntityHuman) null, blockposition, SoundEffects.BLOCK_TRIPWIRE_CLICK_OFF, SoundCategory.BLOCKS, 0.4F, 0.5F);
        } else if (flag && !flag2) {
            world.playSound((EntityHuman) null, blockposition, SoundEffects.BLOCK_TRIPWIRE_ATTACH, SoundCategory.BLOCKS, 0.4F, 0.7F);
        } else if (!flag && flag2) {
            world.playSound((EntityHuman) null, blockposition, SoundEffects.BLOCK_TRIPWIRE_DETACH, SoundCategory.BLOCKS, 0.4F, 1.2F / (world.random.nextFloat() * 0.2F + 0.9F));
        }

    }

    private void a(World world, BlockPosition blockposition, EnumDirection enumdirection) {
        world.applyPhysics(blockposition, this);
        world.applyPhysics(blockposition.shift(enumdirection.opposite()), this);
    }

    @Override
    public void remove(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if (!flag && !iblockdata.a(iblockdata1.getBlock())) {
            boolean flag1 = (Boolean) iblockdata.get(BlockTripwireHook.ATTACHED);
            boolean flag2 = (Boolean) iblockdata.get(BlockTripwireHook.POWERED);

            if (flag1 || flag2) {
                this.a(world, blockposition, iblockdata, true, false, -1, (IBlockData) null);
            }

            if (flag2) {
                world.applyPhysics(blockposition, this);
                world.applyPhysics(blockposition.shift(((EnumDirection) iblockdata.get(BlockTripwireHook.FACING)).opposite()), this);
            }

            super.remove(iblockdata, world, blockposition, iblockdata1, flag);
        }
    }

    @Override
    public int a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return (Boolean) iblockdata.get(BlockTripwireHook.POWERED) ? 15 : 0;
    }

    @Override
    public int b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return !(Boolean) iblockdata.get(BlockTripwireHook.POWERED) ? 0 : (iblockdata.get(BlockTripwireHook.FACING) == enumdirection ? 15 : 0);
    }

    @Override
    public boolean isPowerSource(IBlockData iblockdata) {
        return true;
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        return (IBlockData) iblockdata.set(BlockTripwireHook.FACING, enumblockrotation.a((EnumDirection) iblockdata.get(BlockTripwireHook.FACING)));
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        return iblockdata.a(enumblockmirror.a((EnumDirection) iblockdata.get(BlockTripwireHook.FACING)));
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockTripwireHook.FACING, BlockTripwireHook.POWERED, BlockTripwireHook.ATTACHED);
    }
}
