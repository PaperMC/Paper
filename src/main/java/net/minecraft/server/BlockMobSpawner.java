package net.minecraft.server;

public class BlockMobSpawner extends BlockTileEntity {

    protected BlockMobSpawner(BlockBase.Info blockbase_info) {
        super(blockbase_info);
    }

    @Override
    public TileEntity createTile(IBlockAccess iblockaccess) {
        return new TileEntityMobSpawner();
    }

    @Override
    public void dropNaturally(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, ItemStack itemstack) {
        super.dropNaturally(iblockdata, worldserver, blockposition, itemstack);
        /* CraftBukkit start - Delegate to getExpDrop
        int i = 15 + worldserver.random.nextInt(15) + worldserver.random.nextInt(15);

        this.dropExperience(worldserver, blockposition, i);
        */
    }

    @Override
    public int getExpDrop(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, ItemStack itemstack) {
        int i = 15 + worldserver.random.nextInt(15) + worldserver.random.nextInt(15);

        return i;
        // CraftBukkit end
    }

    @Override
    public EnumRenderType b(IBlockData iblockdata) {
        return EnumRenderType.MODEL;
    }
}
