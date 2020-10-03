package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public class BlockCrops extends BlockPlant implements IBlockFragilePlantElement {

    public static final BlockStateInteger AGE = BlockProperties.ai;
    private static final VoxelShape[] a = new VoxelShape[]{Block.a(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.a(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.a(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.a(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), Block.a(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.a(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), Block.a(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), Block.a(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

    protected BlockCrops(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(this.c(), 0));
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return BlockCrops.a[(Integer) iblockdata.get(this.c())];
    }

    @Override
    protected boolean c(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return iblockdata.a(Blocks.FARMLAND);
    }

    public BlockStateInteger c() {
        return BlockCrops.AGE;
    }

    public int d() {
        return 7;
    }

    protected int g(IBlockData iblockdata) {
        return (Integer) iblockdata.get(this.c());
    }

    public IBlockData setAge(int i) {
        return (IBlockData) this.getBlockData().set(this.c(), i);
    }

    public boolean isRipe(IBlockData iblockdata) {
        return (Integer) iblockdata.get(this.c()) >= this.d();
    }

    @Override
    public boolean isTicking(IBlockData iblockdata) {
        return !this.isRipe(iblockdata);
    }

    @Override
    public void tick(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        if (worldserver.getLightLevel(blockposition, 0) >= 9) {
            int i = this.g(iblockdata);

            if (i < this.d()) {
                float f = a((Block) this, (IBlockAccess) worldserver, blockposition);

                if (random.nextInt((int) (25.0F / f) + 1) == 0) {
                    CraftEventFactory.handleBlockGrowEvent(worldserver, blockposition, this.setAge(i + 1), 2); // CraftBukkit
                }
            }
        }

    }

    public void a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        int i = this.g(iblockdata) + this.a(world);
        int j = this.d();

        if (i > j) {
            i = j;
        }

        CraftEventFactory.handleBlockGrowEvent(world, blockposition, this.setAge(i), 2); // CraftBukkit
    }

    protected int a(World world) {
        return MathHelper.nextInt(world.random, 2, 5);
    }

    protected static float a(Block block, IBlockAccess iblockaccess, BlockPosition blockposition) {
        float f = 1.0F;
        BlockPosition blockposition1 = blockposition.down();

        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                float f1 = 0.0F;
                IBlockData iblockdata = iblockaccess.getType(blockposition1.b(i, 0, j));

                if (iblockdata.a(Blocks.FARMLAND)) {
                    f1 = 1.0F;
                    if ((Integer) iblockdata.get(BlockSoil.MOISTURE) > 0) {
                        f1 = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        BlockPosition blockposition2 = blockposition.north();
        BlockPosition blockposition3 = blockposition.south();
        BlockPosition blockposition4 = blockposition.west();
        BlockPosition blockposition5 = blockposition.east();
        boolean flag = block == iblockaccess.getType(blockposition4).getBlock() || block == iblockaccess.getType(blockposition5).getBlock();
        boolean flag1 = block == iblockaccess.getType(blockposition2).getBlock() || block == iblockaccess.getType(blockposition3).getBlock();

        if (flag && flag1) {
            f /= 2.0F;
        } else {
            boolean flag2 = block == iblockaccess.getType(blockposition4.north()).getBlock() || block == iblockaccess.getType(blockposition5.north()).getBlock() || block == iblockaccess.getType(blockposition5.south()).getBlock() || block == iblockaccess.getType(blockposition4.south()).getBlock();

            if (flag2) {
                f /= 2.0F;
            }
        }

        return f;
    }

    @Override
    public boolean canPlace(IBlockData iblockdata, IWorldReader iworldreader, BlockPosition blockposition) {
        return (iworldreader.getLightLevel(blockposition, 0) >= 8 || iworldreader.e(blockposition)) && super.canPlace(iblockdata, iworldreader, blockposition);
    }

    @Override
    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Entity entity) {
        if (entity instanceof EntityRavager && !CraftEventFactory.callEntityChangeBlockEvent(entity, blockposition, Blocks.AIR.getBlockData(), !world.getGameRules().getBoolean(GameRules.MOB_GRIEFING)).isCancelled()) { // CraftBukkit
            world.a(blockposition, true, entity);
        }

        super.a(iblockdata, world, blockposition, entity);
    }

    @Override
    public boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, boolean flag) {
        return !this.isRipe(iblockdata);
    }

    @Override
    public boolean a(World world, Random random, BlockPosition blockposition, IBlockData iblockdata) {
        return true;
    }

    @Override
    public void a(WorldServer worldserver, Random random, BlockPosition blockposition, IBlockData iblockdata) {
        this.a((World) worldserver, blockposition, iblockdata);
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockCrops.AGE);
    }
}
