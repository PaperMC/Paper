package net.minecraft.server;

import java.util.Random;
import javax.annotation.Nullable;

public class BlockTurtleEgg extends Block {

    private static final VoxelShape c = Block.a(3.0D, 0.0D, 3.0D, 12.0D, 7.0D, 12.0D);
    private static final VoxelShape d = Block.a(1.0D, 0.0D, 1.0D, 15.0D, 7.0D, 15.0D);
    public static final BlockStateInteger a = BlockProperties.ap;
    public static final BlockStateInteger b = BlockProperties.ao;

    public BlockTurtleEgg(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockTurtleEgg.a, 0)).set(BlockTurtleEgg.b, 1));
    }

    @Override
    public void stepOn(World world, BlockPosition blockposition, Entity entity) {
        this.a(world, blockposition, entity, 100);
        super.stepOn(world, blockposition, entity);
    }

    @Override
    public void fallOn(World world, BlockPosition blockposition, Entity entity, float f) {
        if (!(entity instanceof EntityZombie)) {
            this.a(world, blockposition, entity, 3);
        }

        super.fallOn(world, blockposition, entity, f);
    }

    private void a(World world, BlockPosition blockposition, Entity entity, int i) {
        if (this.a(world, entity)) {
            if (!world.isClientSide && world.random.nextInt(i) == 0) {
                IBlockData iblockdata = world.getType(blockposition);

                if (iblockdata.a(Blocks.TURTLE_EGG)) {
                    this.a(world, blockposition, iblockdata);
                }
            }

        }
    }

    private void a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        world.playSound((EntityHuman) null, blockposition, SoundEffects.ENTITY_TURTLE_EGG_BREAK, SoundCategory.BLOCKS, 0.7F, 0.9F + world.random.nextFloat() * 0.2F);
        int i = (Integer) iblockdata.get(BlockTurtleEgg.b);

        if (i <= 1) {
            world.b(blockposition, false);
        } else {
            world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockTurtleEgg.b, i - 1), 2);
            world.triggerEffect(2001, blockposition, Block.getCombinedId(iblockdata));
        }

    }

    @Override
    public void tick(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        if (this.a((World) worldserver) && a((IBlockAccess) worldserver, blockposition)) {
            int i = (Integer) iblockdata.get(BlockTurtleEgg.a);

            if (i < 2) {
                worldserver.playSound((EntityHuman) null, blockposition, SoundEffects.ENTITY_TURTLE_EGG_CRACK, SoundCategory.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                worldserver.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockTurtleEgg.a, i + 1), 2);
            } else {
                worldserver.playSound((EntityHuman) null, blockposition, SoundEffects.ENTITY_TURTLE_EGG_HATCH, SoundCategory.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                worldserver.a(blockposition, false);

                for (int j = 0; j < (Integer) iblockdata.get(BlockTurtleEgg.b); ++j) {
                    worldserver.triggerEffect(2001, blockposition, Block.getCombinedId(iblockdata));
                    EntityTurtle entityturtle = (EntityTurtle) EntityTypes.TURTLE.a((World) worldserver);

                    entityturtle.setAgeRaw(-24000);
                    entityturtle.setHomePos(blockposition);
                    entityturtle.setPositionRotation((double) blockposition.getX() + 0.3D + (double) j * 0.2D, (double) blockposition.getY(), (double) blockposition.getZ() + 0.3D, 0.0F, 0.0F);
                    worldserver.addEntity(entityturtle);
                }
            }
        }

    }

    public static boolean a(IBlockAccess iblockaccess, BlockPosition blockposition) {
        return b(iblockaccess, blockposition.down());
    }

    public static boolean b(IBlockAccess iblockaccess, BlockPosition blockposition) {
        return iblockaccess.getType(blockposition).a((Tag) TagsBlock.SAND);
    }

    @Override
    public void onPlace(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if (a((IBlockAccess) world, blockposition) && !world.isClientSide) {
            world.triggerEffect(2005, blockposition, 0);
        }

    }

    private boolean a(World world) {
        float f = world.f(1.0F);

        return (double) f < 0.69D && (double) f > 0.65D ? true : world.random.nextInt(500) == 0;
    }

    @Override
    public void a(World world, EntityHuman entityhuman, BlockPosition blockposition, IBlockData iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        super.a(world, entityhuman, blockposition, iblockdata, tileentity, itemstack);
        this.a(world, blockposition, iblockdata);
    }

    @Override
    public boolean a(IBlockData iblockdata, BlockActionContext blockactioncontext) {
        return blockactioncontext.getItemStack().getItem() == this.getItem() && (Integer) iblockdata.get(BlockTurtleEgg.b) < 4 ? true : super.a(iblockdata, blockactioncontext);
    }

    @Nullable
    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        IBlockData iblockdata = blockactioncontext.getWorld().getType(blockactioncontext.getClickPosition());

        return iblockdata.a((Block) this) ? (IBlockData) iblockdata.set(BlockTurtleEgg.b, Math.min(4, (Integer) iblockdata.get(BlockTurtleEgg.b) + 1)) : super.getPlacedState(blockactioncontext);
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return (Integer) iblockdata.get(BlockTurtleEgg.b) > 1 ? BlockTurtleEgg.d : BlockTurtleEgg.c;
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockTurtleEgg.a, BlockTurtleEgg.b);
    }

    private boolean a(World world, Entity entity) {
        return !(entity instanceof EntityTurtle) && !(entity instanceof EntityBat) ? (!(entity instanceof EntityLiving) ? false : entity instanceof EntityHuman || world.getGameRules().getBoolean(GameRules.MOB_GRIEFING)) : false;
    }
}
