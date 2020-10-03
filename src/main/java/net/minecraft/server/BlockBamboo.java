package net.minecraft.server;

import java.util.Random;
import javax.annotation.Nullable;

public class BlockBamboo extends Block implements IBlockFragilePlantElement {

    protected static final VoxelShape a = Block.a(5.0D, 0.0D, 5.0D, 11.0D, 16.0D, 11.0D);
    protected static final VoxelShape b = Block.a(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    protected static final VoxelShape c = Block.a(6.5D, 0.0D, 6.5D, 9.5D, 16.0D, 9.5D);
    public static final BlockStateInteger d = BlockProperties.ae;
    public static final BlockStateEnum<BlockPropertyBambooSize> e = BlockProperties.aN;
    public static final BlockStateInteger f = BlockProperties.aA;

    public BlockBamboo(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockBamboo.d, 0)).set(BlockBamboo.e, BlockPropertyBambooSize.NONE)).set(BlockBamboo.f, 0));
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockBamboo.d, BlockBamboo.e, BlockBamboo.f);
    }

    @Override
    public BlockBase.EnumRandomOffset ah_() {
        return BlockBase.EnumRandomOffset.XZ;
    }

    @Override
    public boolean b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return true;
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        VoxelShape voxelshape = iblockdata.get(BlockBamboo.e) == BlockPropertyBambooSize.LARGE ? BlockBamboo.b : BlockBamboo.a;
        Vec3D vec3d = iblockdata.n(iblockaccess, blockposition);

        return voxelshape.a(vec3d.x, vec3d.y, vec3d.z);
    }

    @Override
    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        return false;
    }

    @Override
    public VoxelShape c(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        Vec3D vec3d = iblockdata.n(iblockaccess, blockposition);

        return BlockBamboo.c.a(vec3d.x, vec3d.y, vec3d.z);
    }

    @Nullable
    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        Fluid fluid = blockactioncontext.getWorld().getFluid(blockactioncontext.getClickPosition());

        if (!fluid.isEmpty()) {
            return null;
        } else {
            IBlockData iblockdata = blockactioncontext.getWorld().getType(blockactioncontext.getClickPosition().down());

            if (iblockdata.a((Tag) TagsBlock.BAMBOO_PLANTABLE_ON)) {
                if (iblockdata.a(Blocks.BAMBOO_SAPLING)) {
                    return (IBlockData) this.getBlockData().set(BlockBamboo.d, 0);
                } else if (iblockdata.a(Blocks.BAMBOO)) {
                    int i = (Integer) iblockdata.get(BlockBamboo.d) > 0 ? 1 : 0;

                    return (IBlockData) this.getBlockData().set(BlockBamboo.d, i);
                } else {
                    IBlockData iblockdata1 = blockactioncontext.getWorld().getType(blockactioncontext.getClickPosition().up());

                    return !iblockdata1.a(Blocks.BAMBOO) && !iblockdata1.a(Blocks.BAMBOO_SAPLING) ? Blocks.BAMBOO_SAPLING.getBlockData() : (IBlockData) this.getBlockData().set(BlockBamboo.d, iblockdata1.get(BlockBamboo.d));
                }
            } else {
                return null;
            }
        }
    }

    @Override
    public void tickAlways(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        if (!iblockdata.canPlace(worldserver, blockposition)) {
            worldserver.b(blockposition, true);
        }

    }

    @Override
    public boolean isTicking(IBlockData iblockdata) {
        return (Integer) iblockdata.get(BlockBamboo.f) == 0;
    }

    @Override
    public void tick(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        if ((Integer) iblockdata.get(BlockBamboo.f) == 0) {
            if (random.nextInt(3) == 0 && worldserver.isEmpty(blockposition.up()) && worldserver.getLightLevel(blockposition.up(), 0) >= 9) {
                int i = this.b(worldserver, blockposition) + 1;

                if (i < 16) {
                    this.a(iblockdata, (World) worldserver, blockposition, random, i);
                }
            }

        }
    }

    @Override
    public boolean canPlace(IBlockData iblockdata, IWorldReader iworldreader, BlockPosition blockposition) {
        return iworldreader.getType(blockposition.down()).a((Tag) TagsBlock.BAMBOO_PLANTABLE_ON);
    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        if (!iblockdata.canPlace(generatoraccess, blockposition)) {
            generatoraccess.getBlockTickList().a(blockposition, this, 1);
        }

        if (enumdirection == EnumDirection.UP && iblockdata1.a(Blocks.BAMBOO) && (Integer) iblockdata1.get(BlockBamboo.d) > (Integer) iblockdata.get(BlockBamboo.d)) {
            generatoraccess.setTypeAndData(blockposition, (IBlockData) iblockdata.a((IBlockState) BlockBamboo.d), 2);
        }

        return super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
    }

    @Override
    public boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, boolean flag) {
        int i = this.a(iblockaccess, blockposition);
        int j = this.b(iblockaccess, blockposition);

        return i + j + 1 < 16 && (Integer) iblockaccess.getType(blockposition.up(i)).get(BlockBamboo.f) != 1;
    }

    @Override
    public boolean a(World world, Random random, BlockPosition blockposition, IBlockData iblockdata) {
        return true;
    }

    @Override
    public void a(WorldServer worldserver, Random random, BlockPosition blockposition, IBlockData iblockdata) {
        int i = this.a((IBlockAccess) worldserver, blockposition);
        int j = this.b(worldserver, blockposition);
        int k = i + j + 1;
        int l = 1 + random.nextInt(2);

        for (int i1 = 0; i1 < l; ++i1) {
            BlockPosition blockposition1 = blockposition.up(i);
            IBlockData iblockdata1 = worldserver.getType(blockposition1);

            if (k >= 16 || (Integer) iblockdata1.get(BlockBamboo.f) == 1 || !worldserver.isEmpty(blockposition1.up())) {
                return;
            }

            this.a(iblockdata1, (World) worldserver, blockposition1, random, k);
            ++i;
            ++k;
        }

    }

    @Override
    public float getDamage(IBlockData iblockdata, EntityHuman entityhuman, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return entityhuman.getItemInMainHand().getItem() instanceof ItemSword ? 1.0F : super.getDamage(iblockdata, entityhuman, iblockaccess, blockposition);
    }

    protected void a(IBlockData iblockdata, World world, BlockPosition blockposition, Random random, int i) {
        IBlockData iblockdata1 = world.getType(blockposition.down());
        BlockPosition blockposition1 = blockposition.down(2);
        IBlockData iblockdata2 = world.getType(blockposition1);
        BlockPropertyBambooSize blockpropertybamboosize = BlockPropertyBambooSize.NONE;

        if (i >= 1) {
            if (iblockdata1.a(Blocks.BAMBOO) && iblockdata1.get(BlockBamboo.e) != BlockPropertyBambooSize.NONE) {
                if (iblockdata1.a(Blocks.BAMBOO) && iblockdata1.get(BlockBamboo.e) != BlockPropertyBambooSize.NONE) {
                    blockpropertybamboosize = BlockPropertyBambooSize.LARGE;
                    if (iblockdata2.a(Blocks.BAMBOO)) {
                        world.setTypeAndData(blockposition.down(), (IBlockData) iblockdata1.set(BlockBamboo.e, BlockPropertyBambooSize.SMALL), 3);
                        world.setTypeAndData(blockposition1, (IBlockData) iblockdata2.set(BlockBamboo.e, BlockPropertyBambooSize.NONE), 3);
                    }
                }
            } else {
                blockpropertybamboosize = BlockPropertyBambooSize.SMALL;
            }
        }

        int j = (Integer) iblockdata.get(BlockBamboo.d) != 1 && !iblockdata2.a(Blocks.BAMBOO) ? 0 : 1;
        int k = (i < 11 || random.nextFloat() >= 0.25F) && i != 15 ? 0 : 1;

        world.setTypeAndData(blockposition.up(), (IBlockData) ((IBlockData) ((IBlockData) this.getBlockData().set(BlockBamboo.d, j)).set(BlockBamboo.e, blockpropertybamboosize)).set(BlockBamboo.f, k), 3);
    }

    protected int a(IBlockAccess iblockaccess, BlockPosition blockposition) {
        int i;

        for (i = 0; i < 16 && iblockaccess.getType(blockposition.up(i + 1)).a(Blocks.BAMBOO); ++i) {
            ;
        }

        return i;
    }

    protected int b(IBlockAccess iblockaccess, BlockPosition blockposition) {
        int i;

        for (i = 0; i < 16 && iblockaccess.getType(blockposition.down(i + 1)).a(Blocks.BAMBOO); ++i) {
            ;
        }

        return i;
    }
}
