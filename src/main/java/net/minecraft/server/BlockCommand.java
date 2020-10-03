package net.minecraft.server;

import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockCommand extends BlockTileEntity {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final BlockStateDirection a = BlockDirectional.FACING;
    public static final BlockStateBoolean b = BlockProperties.c;

    public BlockCommand(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockCommand.a, EnumDirection.NORTH)).set(BlockCommand.b, false));
    }

    @Override
    public TileEntity createTile(IBlockAccess iblockaccess) {
        TileEntityCommand tileentitycommand = new TileEntityCommand();

        tileentitycommand.b(this == Blocks.CHAIN_COMMAND_BLOCK);
        return tileentitycommand;
    }

    @Override
    public void doPhysics(IBlockData iblockdata, World world, BlockPosition blockposition, Block block, BlockPosition blockposition1, boolean flag) {
        if (!world.isClientSide) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityCommand) {
                TileEntityCommand tileentitycommand = (TileEntityCommand) tileentity;
                boolean flag1 = world.isBlockIndirectlyPowered(blockposition);
                boolean flag2 = tileentitycommand.f();

                tileentitycommand.a(flag1);
                if (!flag2 && !tileentitycommand.g() && tileentitycommand.m() != TileEntityCommand.Type.SEQUENCE) {
                    if (flag1) {
                        tileentitycommand.k();
                        world.getBlockTickList().a(blockposition, this, 1);
                    }

                }
            }
        }
    }

    @Override
    public void tickAlways(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        TileEntity tileentity = worldserver.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityCommand) {
            TileEntityCommand tileentitycommand = (TileEntityCommand) tileentity;
            CommandBlockListenerAbstract commandblocklistenerabstract = tileentitycommand.getCommandBlock();
            boolean flag = !UtilColor.b(commandblocklistenerabstract.getCommand());
            TileEntityCommand.Type tileentitycommand_type = tileentitycommand.m();
            boolean flag1 = tileentitycommand.j();

            if (tileentitycommand_type == TileEntityCommand.Type.AUTO) {
                tileentitycommand.k();
                if (flag1) {
                    this.a(iblockdata, worldserver, blockposition, commandblocklistenerabstract, flag);
                } else if (tileentitycommand.x()) {
                    commandblocklistenerabstract.a(0);
                }

                if (tileentitycommand.f() || tileentitycommand.g()) {
                    worldserver.getBlockTickList().a(blockposition, this, 1);
                }
            } else if (tileentitycommand_type == TileEntityCommand.Type.REDSTONE) {
                if (flag1) {
                    this.a(iblockdata, worldserver, blockposition, commandblocklistenerabstract, flag);
                } else if (tileentitycommand.x()) {
                    commandblocklistenerabstract.a(0);
                }
            }

            worldserver.updateAdjacentComparators(blockposition, this);
        }

    }

    private void a(IBlockData iblockdata, World world, BlockPosition blockposition, CommandBlockListenerAbstract commandblocklistenerabstract, boolean flag) {
        if (flag) {
            commandblocklistenerabstract.a(world);
        } else {
            commandblocklistenerabstract.a(0);
        }

        a(world, blockposition, (EnumDirection) iblockdata.get(BlockCommand.a));
    }

    @Override
    public EnumInteractionResult interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityCommand && entityhuman.isCreativeAndOp()) {
            entityhuman.a((TileEntityCommand) tileentity);
            return EnumInteractionResult.a(world.isClientSide);
        } else {
            return EnumInteractionResult.PASS;
        }
    }

    @Override
    public boolean isComplexRedstone(IBlockData iblockdata) {
        return true;
    }

    @Override
    public int a(IBlockData iblockdata, World world, BlockPosition blockposition) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        return tileentity instanceof TileEntityCommand ? ((TileEntityCommand) tileentity).getCommandBlock().i() : 0;
    }

    @Override
    public void postPlace(World world, BlockPosition blockposition, IBlockData iblockdata, EntityLiving entityliving, ItemStack itemstack) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityCommand) {
            TileEntityCommand tileentitycommand = (TileEntityCommand) tileentity;
            CommandBlockListenerAbstract commandblocklistenerabstract = tileentitycommand.getCommandBlock();

            if (itemstack.hasName()) {
                commandblocklistenerabstract.setName(itemstack.getName());
            }

            if (!world.isClientSide) {
                if (itemstack.b("BlockEntityTag") == null) {
                    commandblocklistenerabstract.a(world.getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK));
                    tileentitycommand.b(this == Blocks.CHAIN_COMMAND_BLOCK);
                }

                if (tileentitycommand.m() == TileEntityCommand.Type.SEQUENCE) {
                    boolean flag = world.isBlockIndirectlyPowered(blockposition);

                    tileentitycommand.a(flag);
                }
            }

        }
    }

    @Override
    public EnumRenderType b(IBlockData iblockdata) {
        return EnumRenderType.MODEL;
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        return (IBlockData) iblockdata.set(BlockCommand.a, enumblockrotation.a((EnumDirection) iblockdata.get(BlockCommand.a)));
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        return iblockdata.a(enumblockmirror.a((EnumDirection) iblockdata.get(BlockCommand.a)));
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockCommand.a, BlockCommand.b);
    }

    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        return (IBlockData) this.getBlockData().set(BlockCommand.a, blockactioncontext.d().opposite());
    }

    private static void a(World world, BlockPosition blockposition, EnumDirection enumdirection) {
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = blockposition.i();
        GameRules gamerules = world.getGameRules();

        IBlockData iblockdata;
        int i;

        for (i = gamerules.getInt(GameRules.MAX_COMMAND_CHAIN_LENGTH); i-- > 0; enumdirection = (EnumDirection) iblockdata.get(BlockCommand.a)) {
            blockposition_mutableblockposition.c(enumdirection);
            iblockdata = world.getType(blockposition_mutableblockposition);
            Block block = iblockdata.getBlock();

            if (!iblockdata.a(Blocks.CHAIN_COMMAND_BLOCK)) {
                break;
            }

            TileEntity tileentity = world.getTileEntity(blockposition_mutableblockposition);

            if (!(tileentity instanceof TileEntityCommand)) {
                break;
            }

            TileEntityCommand tileentitycommand = (TileEntityCommand) tileentity;

            if (tileentitycommand.m() != TileEntityCommand.Type.SEQUENCE) {
                break;
            }

            if (tileentitycommand.f() || tileentitycommand.g()) {
                CommandBlockListenerAbstract commandblocklistenerabstract = tileentitycommand.getCommandBlock();

                if (tileentitycommand.k()) {
                    if (!commandblocklistenerabstract.a(world)) {
                        break;
                    }

                    world.updateAdjacentComparators(blockposition_mutableblockposition, block);
                } else if (tileentitycommand.x()) {
                    commandblocklistenerabstract.a(0);
                }
            }
        }

        if (i <= 0) {
            int j = Math.max(gamerules.getInt(GameRules.MAX_COMMAND_CHAIN_LENGTH), 0);

            BlockCommand.LOGGER.warn("Command Block chain tried to execute more than {} steps!", j);
        }

    }
}
