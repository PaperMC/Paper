package net.minecraft.server;

import java.util.Random;

public class BlockSweetBerryBush extends BlockPlant implements IBlockFragilePlantElement {

    public static final BlockStateInteger a = BlockProperties.ag;
    private static final VoxelShape b = Block.a(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D);
    private static final VoxelShape c = Block.a(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

    public BlockSweetBerryBush(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockSweetBerryBush.a, 0));
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return (Integer) iblockdata.get(BlockSweetBerryBush.a) == 0 ? BlockSweetBerryBush.b : ((Integer) iblockdata.get(BlockSweetBerryBush.a) < 3 ? BlockSweetBerryBush.c : super.b(iblockdata, iblockaccess, blockposition, voxelshapecollision));
    }

    @Override
    public boolean isTicking(IBlockData iblockdata) {
        return (Integer) iblockdata.get(BlockSweetBerryBush.a) < 3;
    }

    @Override
    public void tick(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        int i = (Integer) iblockdata.get(BlockSweetBerryBush.a);

        if (i < 3 && random.nextInt(5) == 0 && worldserver.getLightLevel(blockposition.up(), 0) >= 9) {
            worldserver.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockSweetBerryBush.a, i + 1), 2);
        }

    }

    @Override
    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Entity entity) {
        if (entity instanceof EntityLiving && entity.getEntityType() != EntityTypes.FOX && entity.getEntityType() != EntityTypes.BEE) {
            entity.a(iblockdata, new Vec3D(0.800000011920929D, 0.75D, 0.800000011920929D));
            if (!world.isClientSide && (Integer) iblockdata.get(BlockSweetBerryBush.a) > 0 && (entity.D != entity.locX() || entity.F != entity.locZ())) {
                double d0 = Math.abs(entity.locX() - entity.D);
                double d1 = Math.abs(entity.locZ() - entity.F);

                if (d0 >= 0.003000000026077032D || d1 >= 0.003000000026077032D) {
                    entity.damageEntity(DamageSource.SWEET_BERRY_BUSH, 1.0F);
                }
            }

        }
    }

    @Override
    public EnumInteractionResult interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        int i = (Integer) iblockdata.get(BlockSweetBerryBush.a);
        boolean flag = i == 3;

        if (!flag && entityhuman.b(enumhand).getItem() == Items.BONE_MEAL) {
            return EnumInteractionResult.PASS;
        } else if (i > 1) {
            int j = 1 + world.random.nextInt(2);

            a(world, blockposition, new ItemStack(Items.SWEET_BERRIES, j + (flag ? 1 : 0)));
            world.playSound((EntityHuman) null, blockposition, SoundEffects.ITEM_SWEET_BERRIES_PICK_FROM_BUSH, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
            world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockSweetBerryBush.a, 1), 2);
            return EnumInteractionResult.a(world.isClientSide);
        } else {
            return super.interact(iblockdata, world, blockposition, entityhuman, enumhand, movingobjectpositionblock);
        }
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockSweetBerryBush.a);
    }

    @Override
    public boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, boolean flag) {
        return (Integer) iblockdata.get(BlockSweetBerryBush.a) < 3;
    }

    @Override
    public boolean a(World world, Random random, BlockPosition blockposition, IBlockData iblockdata) {
        return true;
    }

    @Override
    public void a(WorldServer worldserver, Random random, BlockPosition blockposition, IBlockData iblockdata) {
        int i = Math.min(3, (Integer) iblockdata.get(BlockSweetBerryBush.a) + 1);

        worldserver.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockSweetBerryBush.a, i), 2);
    }
}
