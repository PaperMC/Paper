package net.minecraft.server;

import javax.annotation.Nullable;

public class BlockTallPlant extends BlockPlant {

    public static final BlockStateEnum<BlockPropertyDoubleBlockHalf> HALF = BlockProperties.aa;

    public BlockTallPlant(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockTallPlant.HALF, BlockPropertyDoubleBlockHalf.LOWER));
    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        BlockPropertyDoubleBlockHalf blockpropertydoubleblockhalf = (BlockPropertyDoubleBlockHalf) iblockdata.get(BlockTallPlant.HALF);

        return enumdirection.n() == EnumDirection.EnumAxis.Y && blockpropertydoubleblockhalf == BlockPropertyDoubleBlockHalf.LOWER == (enumdirection == EnumDirection.UP) && (!iblockdata1.a((Block) this) || iblockdata1.get(BlockTallPlant.HALF) == blockpropertydoubleblockhalf) ? Blocks.AIR.getBlockData() : (blockpropertydoubleblockhalf == BlockPropertyDoubleBlockHalf.LOWER && enumdirection == EnumDirection.DOWN && !iblockdata.canPlace(generatoraccess, blockposition) ? Blocks.AIR.getBlockData() : super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1));
    }

    @Nullable
    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        BlockPosition blockposition = blockactioncontext.getClickPosition();

        return blockposition.getY() < 255 && blockactioncontext.getWorld().getType(blockposition.up()).a(blockactioncontext) ? super.getPlacedState(blockactioncontext) : null;
    }

    @Override
    public void postPlace(World world, BlockPosition blockposition, IBlockData iblockdata, EntityLiving entityliving, ItemStack itemstack) {
        world.setTypeAndData(blockposition.up(), (IBlockData) this.getBlockData().set(BlockTallPlant.HALF, BlockPropertyDoubleBlockHalf.UPPER), 3);
    }

    @Override
    public boolean canPlace(IBlockData iblockdata, IWorldReader iworldreader, BlockPosition blockposition) {
        if (iblockdata.get(BlockTallPlant.HALF) != BlockPropertyDoubleBlockHalf.UPPER) {
            return super.canPlace(iblockdata, iworldreader, blockposition);
        } else {
            IBlockData iblockdata1 = iworldreader.getType(blockposition.down());

            return iblockdata1.a((Block) this) && iblockdata1.get(BlockTallPlant.HALF) == BlockPropertyDoubleBlockHalf.LOWER;
        }
    }

    public void a(GeneratorAccess generatoraccess, BlockPosition blockposition, int i) {
        generatoraccess.setTypeAndData(blockposition, (IBlockData) this.getBlockData().set(BlockTallPlant.HALF, BlockPropertyDoubleBlockHalf.LOWER), i);
        generatoraccess.setTypeAndData(blockposition.up(), (IBlockData) this.getBlockData().set(BlockTallPlant.HALF, BlockPropertyDoubleBlockHalf.UPPER), i);
    }

    @Override
    public void a(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman) {
        if (!world.isClientSide) {
            if (entityhuman.isCreative()) {
                b(world, blockposition, iblockdata, entityhuman);
            } else {
                dropItems(iblockdata, world, blockposition, (TileEntity) null, entityhuman, entityhuman.getItemInMainHand());
            }
        }

        super.a(world, blockposition, iblockdata, entityhuman);
    }

    @Override
    public void a(World world, EntityHuman entityhuman, BlockPosition blockposition, IBlockData iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        super.a(world, entityhuman, blockposition, Blocks.AIR.getBlockData(), tileentity, itemstack);
    }

    protected static void b(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman) {
        // CraftBukkit start
        if (org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPhysicsEvent(world, blockposition).isCancelled()) {
            return;
        }
        // CraftBukkit end
        BlockPropertyDoubleBlockHalf blockpropertydoubleblockhalf = (BlockPropertyDoubleBlockHalf) iblockdata.get(BlockTallPlant.HALF);

        if (blockpropertydoubleblockhalf == BlockPropertyDoubleBlockHalf.UPPER) {
            BlockPosition blockposition1 = blockposition.down();
            IBlockData iblockdata1 = world.getType(blockposition1);

            if (iblockdata1.getBlock() == iblockdata.getBlock() && iblockdata1.get(BlockTallPlant.HALF) == BlockPropertyDoubleBlockHalf.LOWER) {
                world.setTypeAndData(blockposition1, Blocks.AIR.getBlockData(), 35);
                world.a(entityhuman, 2001, blockposition1, Block.getCombinedId(iblockdata1));
            }
        }

    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockTallPlant.HALF);
    }

    @Override
    public BlockBase.EnumRandomOffset ah_() {
        return BlockBase.EnumRandomOffset.XZ;
    }
}
