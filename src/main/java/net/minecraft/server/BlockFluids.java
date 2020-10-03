package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlockFluids extends Block implements IFluidSource {

    public static final BlockStateInteger LEVEL = BlockProperties.av;
    protected final FluidTypeFlowing b;
    private final List<Fluid> d;
    public static final VoxelShape c = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

    protected BlockFluids(FluidTypeFlowing fluidtypeflowing, BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.b = fluidtypeflowing;
        this.d = Lists.newArrayList();
        this.d.add(fluidtypeflowing.a(false));

        for (int i = 1; i < 8; ++i) {
            this.d.add(fluidtypeflowing.a(8 - i, false));
        }

        this.d.add(fluidtypeflowing.a(8, true));
        this.j((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockFluids.LEVEL, 0));
    }

    @Override
    public VoxelShape c(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return voxelshapecollision.a(BlockFluids.c, blockposition, true) && (Integer) iblockdata.get(BlockFluids.LEVEL) == 0 && voxelshapecollision.a(iblockaccess.getFluid(blockposition.up()), this.b) ? BlockFluids.c : VoxelShapes.a();
    }

    @Override
    public boolean isTicking(IBlockData iblockdata) {
        return iblockdata.getFluid().f();
    }

    @Override
    public void tick(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        iblockdata.getFluid().b(worldserver, blockposition, random);
    }

    @Override
    public boolean b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return false;
    }

    @Override
    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        return !this.b.a((Tag) TagsFluid.LAVA);
    }

    @Override
    public Fluid d(IBlockData iblockdata) {
        int i = (Integer) iblockdata.get(BlockFluids.LEVEL);

        return (Fluid) this.d.get(Math.min(i, 8));
    }

    @Override
    public EnumRenderType b(IBlockData iblockdata) {
        return EnumRenderType.INVISIBLE;
    }

    @Override
    public List<ItemStack> a(IBlockData iblockdata, LootTableInfo.Builder loottableinfo_builder) {
        return Collections.emptyList();
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return VoxelShapes.a();
    }

    @Override
    public void onPlace(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if (this.a(world, blockposition, iblockdata)) {
            world.getFluidTickList().a(blockposition, iblockdata.getFluid().getType(), this.b.a((IWorldReader) world));
        }

    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        if (iblockdata.getFluid().isSource() || iblockdata1.getFluid().isSource()) {
            generatoraccess.getFluidTickList().a(blockposition, iblockdata.getFluid().getType(), this.b.a((IWorldReader) generatoraccess));
        }

        return super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
    }

    @Override
    public void doPhysics(IBlockData iblockdata, World world, BlockPosition blockposition, Block block, BlockPosition blockposition1, boolean flag) {
        if (this.a(world, blockposition, iblockdata)) {
            world.getFluidTickList().a(blockposition, iblockdata.getFluid().getType(), this.b.a((IWorldReader) world));
        }

    }

    private boolean a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        if (this.b.a((Tag) TagsFluid.LAVA)) {
            boolean flag = world.getType(blockposition.down()).a(Blocks.SOUL_SOIL);
            EnumDirection[] aenumdirection = EnumDirection.values();
            int i = aenumdirection.length;

            for (int j = 0; j < i; ++j) {
                EnumDirection enumdirection = aenumdirection[j];

                if (enumdirection != EnumDirection.DOWN) {
                    BlockPosition blockposition1 = blockposition.shift(enumdirection);

                    if (world.getFluid(blockposition1).a((Tag) TagsFluid.WATER)) {
                        Block block = world.getFluid(blockposition).isSource() ? Blocks.OBSIDIAN : Blocks.COBBLESTONE;

                        // CraftBukkit start
                        if (org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(world, blockposition, block.getBlockData())) {
                            this.fizz(world, blockposition);
                        }
                        // CraftBukkit end
                        return false;
                    }

                    if (flag && world.getType(blockposition1).a(Blocks.BLUE_ICE)) {
                        // CraftBukkit start
                        if (org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(world, blockposition, Blocks.BASALT.getBlockData())) {
                            this.fizz(world, blockposition);
                        }
                        // CraftBukkit end
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void fizz(GeneratorAccess generatoraccess, BlockPosition blockposition) {
        generatoraccess.triggerEffect(1501, blockposition, 0);
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockFluids.LEVEL);
    }

    @Override
    public FluidType removeFluid(GeneratorAccess generatoraccess, BlockPosition blockposition, IBlockData iblockdata) {
        if ((Integer) iblockdata.get(BlockFluids.LEVEL) == 0) {
            generatoraccess.setTypeAndData(blockposition, Blocks.AIR.getBlockData(), 11);
            return this.b;
        } else {
            return FluidTypes.EMPTY;
        }
    }
}
