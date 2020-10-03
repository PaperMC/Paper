package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BlockPiston extends BlockDirectional {

    public static final BlockStateBoolean EXTENDED = BlockProperties.g;
    protected static final VoxelShape c = Block.a(0.0D, 0.0D, 0.0D, 12.0D, 16.0D, 16.0D);
    protected static final VoxelShape d = Block.a(4.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape e = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 12.0D);
    protected static final VoxelShape f = Block.a(0.0D, 0.0D, 4.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape g = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
    protected static final VoxelShape h = Block.a(0.0D, 4.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private final boolean sticky;

    public BlockPiston(boolean flag, BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockPiston.FACING, EnumDirection.NORTH)).set(BlockPiston.EXTENDED, false));
        this.sticky = flag;
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        if ((Boolean) iblockdata.get(BlockPiston.EXTENDED)) {
            switch ((EnumDirection) iblockdata.get(BlockPiston.FACING)) {
                case DOWN:
                    return BlockPiston.h;
                case UP:
                default:
                    return BlockPiston.g;
                case NORTH:
                    return BlockPiston.f;
                case SOUTH:
                    return BlockPiston.e;
                case WEST:
                    return BlockPiston.d;
                case EAST:
                    return BlockPiston.c;
            }
        } else {
            return VoxelShapes.b();
        }
    }

    @Override
    public void postPlace(World world, BlockPosition blockposition, IBlockData iblockdata, EntityLiving entityliving, ItemStack itemstack) {
        if (!world.isClientSide) {
            this.a(world, blockposition, iblockdata);
        }

    }

    @Override
    public void doPhysics(IBlockData iblockdata, World world, BlockPosition blockposition, Block block, BlockPosition blockposition1, boolean flag) {
        if (!world.isClientSide) {
            this.a(world, blockposition, iblockdata);
        }

    }

    @Override
    public void onPlace(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if (!iblockdata1.a(iblockdata.getBlock())) {
            if (!world.isClientSide && world.getTileEntity(blockposition) == null) {
                this.a(world, blockposition, iblockdata);
            }

        }
    }

    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        return (IBlockData) ((IBlockData) this.getBlockData().set(BlockPiston.FACING, blockactioncontext.d().opposite())).set(BlockPiston.EXTENDED, false);
    }

    private void a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        EnumDirection enumdirection = (EnumDirection) iblockdata.get(BlockPiston.FACING);
        boolean flag = this.a(world, blockposition, enumdirection);

        if (flag && !(Boolean) iblockdata.get(BlockPiston.EXTENDED)) {
            if ((new PistonExtendsChecker(world, blockposition, enumdirection, true)).a()) {
                world.playBlockAction(blockposition, this, 0, enumdirection.c());
            }
        } else if (!flag && (Boolean) iblockdata.get(BlockPiston.EXTENDED)) {
            BlockPosition blockposition1 = blockposition.shift(enumdirection, 2);
            IBlockData iblockdata1 = world.getType(blockposition1);
            byte b0 = 1;

            if (iblockdata1.a(Blocks.MOVING_PISTON) && iblockdata1.get(BlockPiston.FACING) == enumdirection) {
                TileEntity tileentity = world.getTileEntity(blockposition1);

                if (tileentity instanceof TileEntityPiston) {
                    TileEntityPiston tileentitypiston = (TileEntityPiston) tileentity;

                    if (tileentitypiston.d() && (tileentitypiston.a(0.0F) < 0.5F || world.getTime() == tileentitypiston.m() || ((WorldServer) world).m_())) {
                        b0 = 2;
                    }
                }
            }

            world.playBlockAction(blockposition, this, b0, enumdirection.c());
        }

    }

    private boolean a(World world, BlockPosition blockposition, EnumDirection enumdirection) {
        EnumDirection[] aenumdirection = EnumDirection.values();
        int i = aenumdirection.length;

        int j;

        for (j = 0; j < i; ++j) {
            EnumDirection enumdirection1 = aenumdirection[j];

            if (enumdirection1 != enumdirection && world.isBlockFacePowered(blockposition.shift(enumdirection1), enumdirection1)) {
                return true;
            }
        }

        if (world.isBlockFacePowered(blockposition, EnumDirection.DOWN)) {
            return true;
        } else {
            BlockPosition blockposition1 = blockposition.up();
            EnumDirection[] aenumdirection1 = EnumDirection.values();

            j = aenumdirection1.length;

            for (int k = 0; k < j; ++k) {
                EnumDirection enumdirection2 = aenumdirection1[k];

                if (enumdirection2 != EnumDirection.DOWN && world.isBlockFacePowered(blockposition1.shift(enumdirection2), enumdirection2)) {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public boolean a(IBlockData iblockdata, World world, BlockPosition blockposition, int i, int j) {
        EnumDirection enumdirection = (EnumDirection) iblockdata.get(BlockPiston.FACING);

        if (!world.isClientSide) {
            boolean flag = this.a(world, blockposition, enumdirection);

            if (flag && (i == 1 || i == 2)) {
                world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockPiston.EXTENDED, true), 2);
                return false;
            }

            if (!flag && i == 0) {
                return false;
            }
        }

        if (i == 0) {
            if (!this.a(world, blockposition, enumdirection, true)) {
                return false;
            }

            world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockPiston.EXTENDED, true), 67);
            world.playSound((EntityHuman) null, blockposition, SoundEffects.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.25F + 0.6F);
        } else if (i == 1 || i == 2) {
            TileEntity tileentity = world.getTileEntity(blockposition.shift(enumdirection));

            if (tileentity instanceof TileEntityPiston) {
                ((TileEntityPiston) tileentity).l();
            }

            IBlockData iblockdata1 = (IBlockData) ((IBlockData) Blocks.MOVING_PISTON.getBlockData().set(BlockPistonMoving.a, enumdirection)).set(BlockPistonMoving.b, this.sticky ? BlockPropertyPistonType.STICKY : BlockPropertyPistonType.DEFAULT);

            world.setTypeAndData(blockposition, iblockdata1, 20);
            world.setTileEntity(blockposition, BlockPistonMoving.a((IBlockData) this.getBlockData().set(BlockPiston.FACING, EnumDirection.fromType1(j & 7)), enumdirection, false, true));
            world.update(blockposition, iblockdata1.getBlock());
            iblockdata1.a(world, blockposition, 2);
            if (this.sticky) {
                BlockPosition blockposition1 = blockposition.b(enumdirection.getAdjacentX() * 2, enumdirection.getAdjacentY() * 2, enumdirection.getAdjacentZ() * 2);
                IBlockData iblockdata2 = world.getType(blockposition1);
                boolean flag1 = false;

                if (iblockdata2.a(Blocks.MOVING_PISTON)) {
                    TileEntity tileentity1 = world.getTileEntity(blockposition1);

                    if (tileentity1 instanceof TileEntityPiston) {
                        TileEntityPiston tileentitypiston = (TileEntityPiston) tileentity1;

                        if (tileentitypiston.f() == enumdirection && tileentitypiston.d()) {
                            tileentitypiston.l();
                            flag1 = true;
                        }
                    }
                }

                if (!flag1) {
                    if (i == 1 && !iblockdata2.isAir() && a(iblockdata2, world, blockposition1, enumdirection.opposite(), false, enumdirection) && (iblockdata2.getPushReaction() == EnumPistonReaction.NORMAL || iblockdata2.a(Blocks.PISTON) || iblockdata2.a(Blocks.STICKY_PISTON))) {
                        this.a(world, blockposition, enumdirection, false);
                    } else {
                        world.a(blockposition.shift(enumdirection), false);
                    }
                }
            } else {
                world.a(blockposition.shift(enumdirection), false);
            }

            world.playSound((EntityHuman) null, blockposition, SoundEffects.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.15F + 0.6F);
        }

        return true;
    }

    public static boolean a(IBlockData iblockdata, World world, BlockPosition blockposition, EnumDirection enumdirection, boolean flag, EnumDirection enumdirection1) {
        if (blockposition.getY() >= 0 && blockposition.getY() <= world.getBuildHeight() - 1 && world.getWorldBorder().a(blockposition)) {
            if (iblockdata.isAir()) {
                return true;
            } else if (!iblockdata.a(Blocks.OBSIDIAN) && !iblockdata.a(Blocks.CRYING_OBSIDIAN) && !iblockdata.a(Blocks.RESPAWN_ANCHOR)) {
                if (enumdirection == EnumDirection.DOWN && blockposition.getY() == 0) {
                    return false;
                } else if (enumdirection == EnumDirection.UP && blockposition.getY() == world.getBuildHeight() - 1) {
                    return false;
                } else {
                    if (!iblockdata.a(Blocks.PISTON) && !iblockdata.a(Blocks.STICKY_PISTON)) {
                        if (iblockdata.h(world, blockposition) == -1.0F) {
                            return false;
                        }

                        switch (iblockdata.getPushReaction()) {
                            case BLOCK:
                                return false;
                            case DESTROY:
                                return flag;
                            case PUSH_ONLY:
                                return enumdirection == enumdirection1;
                        }
                    } else if ((Boolean) iblockdata.get(BlockPiston.EXTENDED)) {
                        return false;
                    }

                    return !iblockdata.getBlock().isTileEntity();
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean a(World world, BlockPosition blockposition, EnumDirection enumdirection, boolean flag) {
        BlockPosition blockposition1 = blockposition.shift(enumdirection);

        if (!flag && world.getType(blockposition1).a(Blocks.PISTON_HEAD)) {
            world.setTypeAndData(blockposition1, Blocks.AIR.getBlockData(), 20);
        }

        PistonExtendsChecker pistonextendschecker = new PistonExtendsChecker(world, blockposition, enumdirection, flag);

        if (!pistonextendschecker.a()) {
            return false;
        } else {
            Map<BlockPosition, IBlockData> map = Maps.newHashMap();
            List<BlockPosition> list = pistonextendschecker.getMovedBlocks();
            List<IBlockData> list1 = Lists.newArrayList();

            for (int i = 0; i < list.size(); ++i) {
                BlockPosition blockposition2 = (BlockPosition) list.get(i);
                IBlockData iblockdata = world.getType(blockposition2);

                list1.add(iblockdata);
                map.put(blockposition2, iblockdata);
            }

            List<BlockPosition> list2 = pistonextendschecker.getBrokenBlocks();
            IBlockData[] aiblockdata = new IBlockData[list.size() + list2.size()];
            EnumDirection enumdirection1 = flag ? enumdirection : enumdirection.opposite();
            int j = 0;

            BlockPosition blockposition3;
            int k;
            IBlockData iblockdata1;

            for (k = list2.size() - 1; k >= 0; --k) {
                blockposition3 = (BlockPosition) list2.get(k);
                iblockdata1 = world.getType(blockposition3);
                TileEntity tileentity = iblockdata1.getBlock().isTileEntity() ? world.getTileEntity(blockposition3) : null;

                a(iblockdata1, (GeneratorAccess) world, blockposition3, tileentity);
                world.setTypeAndData(blockposition3, Blocks.AIR.getBlockData(), 18);
                aiblockdata[j++] = iblockdata1;
            }

            for (k = list.size() - 1; k >= 0; --k) {
                blockposition3 = (BlockPosition) list.get(k);
                iblockdata1 = world.getType(blockposition3);
                blockposition3 = blockposition3.shift(enumdirection1);
                map.remove(blockposition3);
                world.setTypeAndData(blockposition3, (IBlockData) Blocks.MOVING_PISTON.getBlockData().set(BlockPiston.FACING, enumdirection), 68);
                world.setTileEntity(blockposition3, BlockPistonMoving.a((IBlockData) list1.get(k), enumdirection, flag, false));
                aiblockdata[j++] = iblockdata1;
            }

            if (flag) {
                BlockPropertyPistonType blockpropertypistontype = this.sticky ? BlockPropertyPistonType.STICKY : BlockPropertyPistonType.DEFAULT;
                IBlockData iblockdata2 = (IBlockData) ((IBlockData) Blocks.PISTON_HEAD.getBlockData().set(BlockPistonExtension.FACING, enumdirection)).set(BlockPistonExtension.TYPE, blockpropertypistontype);

                iblockdata1 = (IBlockData) ((IBlockData) Blocks.MOVING_PISTON.getBlockData().set(BlockPistonMoving.a, enumdirection)).set(BlockPistonMoving.b, this.sticky ? BlockPropertyPistonType.STICKY : BlockPropertyPistonType.DEFAULT);
                map.remove(blockposition1);
                world.setTypeAndData(blockposition1, iblockdata1, 68);
                world.setTileEntity(blockposition1, BlockPistonMoving.a(iblockdata2, enumdirection, true, true));
            }

            IBlockData iblockdata3 = Blocks.AIR.getBlockData();
            Iterator iterator = map.keySet().iterator();

            while (iterator.hasNext()) {
                BlockPosition blockposition4 = (BlockPosition) iterator.next();

                world.setTypeAndData(blockposition4, iblockdata3, 82);
            }

            iterator = map.entrySet().iterator();

            BlockPosition blockposition5;

            while (iterator.hasNext()) {
                Entry<BlockPosition, IBlockData> entry = (Entry) iterator.next();

                blockposition5 = (BlockPosition) entry.getKey();
                IBlockData iblockdata4 = (IBlockData) entry.getValue();

                iblockdata4.b(world, blockposition5, 2);
                iblockdata3.a(world, blockposition5, 2);
                iblockdata3.b(world, blockposition5, 2);
            }

            j = 0;

            int l;

            for (l = list2.size() - 1; l >= 0; --l) {
                iblockdata1 = aiblockdata[j++];
                blockposition5 = (BlockPosition) list2.get(l);
                iblockdata1.b(world, blockposition5, 2);
                world.applyPhysics(blockposition5, iblockdata1.getBlock());
            }

            for (l = list.size() - 1; l >= 0; --l) {
                world.applyPhysics((BlockPosition) list.get(l), aiblockdata[j++].getBlock());
            }

            if (flag) {
                world.applyPhysics(blockposition1, Blocks.PISTON_HEAD);
            }

            return true;
        }
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        return (IBlockData) iblockdata.set(BlockPiston.FACING, enumblockrotation.a((EnumDirection) iblockdata.get(BlockPiston.FACING)));
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        return iblockdata.a(enumblockmirror.a((EnumDirection) iblockdata.get(BlockPiston.FACING)));
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockPiston.FACING, BlockPiston.EXTENDED);
    }

    @Override
    public boolean c_(IBlockData iblockdata) {
        return (Boolean) iblockdata.get(BlockPiston.EXTENDED);
    }

    @Override
    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        return false;
    }
}
