package net.minecraft.server;

public class BlockCake extends Block {

    public static final BlockStateInteger BITES = BlockProperties.al;
    protected static final VoxelShape[] b = new VoxelShape[]{Block.a(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), Block.a(3.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), Block.a(5.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), Block.a(7.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), Block.a(9.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), Block.a(11.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), Block.a(13.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D)};

    protected BlockCake(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockCake.BITES, 0));
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return BlockCake.b[(Integer) iblockdata.get(BlockCake.BITES)];
    }

    @Override
    public EnumInteractionResult interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        if (world.isClientSide) {
            ItemStack itemstack = entityhuman.b(enumhand);

            if (this.a((GeneratorAccess) world, blockposition, iblockdata, entityhuman).a()) {
                return EnumInteractionResult.SUCCESS;
            }

            if (itemstack.isEmpty()) {
                return EnumInteractionResult.CONSUME;
            }
        }

        return this.a((GeneratorAccess) world, blockposition, iblockdata, entityhuman);
    }

    private EnumInteractionResult a(GeneratorAccess generatoraccess, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman) {
        if (!entityhuman.q(false)) {
            return EnumInteractionResult.PASS;
        } else {
            entityhuman.a(StatisticList.EAT_CAKE_SLICE);
            // CraftBukkit start
            // entityhuman.getFoodData().eat(2, 0.1F);
            int oldFoodLevel = entityhuman.getFoodData().foodLevel;

            org.bukkit.event.entity.FoodLevelChangeEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callFoodLevelChangeEvent(entityhuman, 2 + oldFoodLevel);

            if (!event.isCancelled()) {
                entityhuman.getFoodData().eat(event.getFoodLevel() - oldFoodLevel, 0.1F);
            }

            ((EntityPlayer) entityhuman).getBukkitEntity().sendHealthUpdate();
            // CraftBukkit end
            int i = (Integer) iblockdata.get(BlockCake.BITES);

            if (i < 6) {
                generatoraccess.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockCake.BITES, i + 1), 3);
            } else {
                generatoraccess.a(blockposition, false);
            }

            return EnumInteractionResult.SUCCESS;
        }
    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        return enumdirection == EnumDirection.DOWN && !iblockdata.canPlace(generatoraccess, blockposition) ? Blocks.AIR.getBlockData() : super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
    }

    @Override
    public boolean canPlace(IBlockData iblockdata, IWorldReader iworldreader, BlockPosition blockposition) {
        return iworldreader.getType(blockposition.down()).getMaterial().isBuildable();
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockCake.BITES);
    }

    @Override
    public int a(IBlockData iblockdata, World world, BlockPosition blockposition) {
        return (7 - (Integer) iblockdata.get(BlockCake.BITES)) * 2;
    }

    @Override
    public boolean isComplexRedstone(IBlockData iblockdata) {
        return true;
    }

    @Override
    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        return false;
    }
}
