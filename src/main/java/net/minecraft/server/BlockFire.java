package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

// CraftBukkit start
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
// CraftBukkit end

public class BlockFire extends BlockFireAbstract {

    public static final BlockStateInteger AGE = BlockProperties.aj;
    public static final BlockStateBoolean NORTH = BlockSprawling.a;
    public static final BlockStateBoolean EAST = BlockSprawling.b;
    public static final BlockStateBoolean SOUTH = BlockSprawling.c;
    public static final BlockStateBoolean WEST = BlockSprawling.d;
    public static final BlockStateBoolean UPPER = BlockSprawling.e;
    private static final Map<EnumDirection, BlockStateBoolean> h = (Map) BlockSprawling.g.entrySet().stream().filter((entry) -> {
        return entry.getKey() != EnumDirection.DOWN;
    }).collect(SystemUtils.a());
    private static final VoxelShape i = Block.a(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape j = Block.a(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    private static final VoxelShape k = Block.a(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape o = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    private static final VoxelShape p = Block.a(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    private final Map<IBlockData, VoxelShape> q;
    private final Object2IntMap<Block> flameChances = new Object2IntOpenHashMap();
    private final Object2IntMap<Block> burnChances = new Object2IntOpenHashMap();

    public BlockFire(BlockBase.Info blockbase_info) {
        super(blockbase_info, 1.0F);
        this.j((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockFire.AGE, 0)).set(BlockFire.NORTH, false)).set(BlockFire.EAST, false)).set(BlockFire.SOUTH, false)).set(BlockFire.WEST, false)).set(BlockFire.UPPER, false));
        this.q = ImmutableMap.copyOf((Map) this.blockStateList.a().stream().filter((iblockdata) -> {
            return (Integer) iblockdata.get(BlockFire.AGE) == 0;
        }).collect(Collectors.toMap(Function.identity(), BlockFire::h)));
    }

    private static VoxelShape h(IBlockData iblockdata) {
        VoxelShape voxelshape = VoxelShapes.a();

        if ((Boolean) iblockdata.get(BlockFire.UPPER)) {
            voxelshape = BlockFire.i;
        }

        if ((Boolean) iblockdata.get(BlockFire.NORTH)) {
            voxelshape = VoxelShapes.a(voxelshape, BlockFire.o);
        }

        if ((Boolean) iblockdata.get(BlockFire.SOUTH)) {
            voxelshape = VoxelShapes.a(voxelshape, BlockFire.p);
        }

        if ((Boolean) iblockdata.get(BlockFire.EAST)) {
            voxelshape = VoxelShapes.a(voxelshape, BlockFire.k);
        }

        if ((Boolean) iblockdata.get(BlockFire.WEST)) {
            voxelshape = VoxelShapes.a(voxelshape, BlockFire.j);
        }

        return voxelshape.isEmpty() ? BlockFire.a : voxelshape;
    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        // CraftBukkit start
        if (!this.canPlace(iblockdata, generatoraccess, blockposition)) {
            // Suppress during worldgen
            if (!(generatoraccess instanceof World)) {
                return Blocks.AIR.getBlockData();
            }
            CraftBlockState blockState = CraftBlockState.getBlockState(generatoraccess, blockposition);
            blockState.setData(Blocks.AIR.getBlockData());

            BlockFadeEvent event = new BlockFadeEvent(blockState.getBlock(), blockState);
            ((World) generatoraccess).getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                return blockState.getHandle();
            }
        }
        return this.a(generatoraccess, blockposition, (Integer) iblockdata.get(BlockFire.AGE));
        // CraftBukkit end
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return (VoxelShape) this.q.get(iblockdata.set(BlockFire.AGE, 0));
    }

    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        return this.getPlacedState(blockactioncontext.getWorld(), blockactioncontext.getClickPosition());
    }

    protected IBlockData getPlacedState(IBlockAccess iblockaccess, BlockPosition blockposition) {
        BlockPosition blockposition1 = blockposition.down();
        IBlockData iblockdata = iblockaccess.getType(blockposition1);

        if (!this.e(iblockdata) && !iblockdata.d(iblockaccess, blockposition1, EnumDirection.UP)) {
            IBlockData iblockdata1 = this.getBlockData();
            EnumDirection[] aenumdirection = EnumDirection.values();
            int i = aenumdirection.length;

            for (int j = 0; j < i; ++j) {
                EnumDirection enumdirection = aenumdirection[j];
                BlockStateBoolean blockstateboolean = (BlockStateBoolean) BlockFire.h.get(enumdirection);

                if (blockstateboolean != null) {
                    iblockdata1 = (IBlockData) iblockdata1.set(blockstateboolean, this.e(iblockaccess.getType(blockposition.shift(enumdirection))));
                }
            }

            return iblockdata1;
        } else {
            return this.getBlockData();
        }
    }

    @Override
    public boolean canPlace(IBlockData iblockdata, IWorldReader iworldreader, BlockPosition blockposition) {
        BlockPosition blockposition1 = blockposition.down();

        return iworldreader.getType(blockposition1).d(iworldreader, blockposition1, EnumDirection.UP) || this.canBurn(iworldreader, blockposition);
    }

    @Override
    public void tickAlways(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        worldserver.getBlockTickList().a(blockposition, this, a(worldserver.random));
        if (worldserver.getGameRules().getBoolean(GameRules.DO_FIRE_TICK)) {
            if (!iblockdata.canPlace(worldserver, blockposition)) {
                fireExtinguished(worldserver, blockposition); // CraftBukkit - invalid place location
            }

            IBlockData iblockdata1 = worldserver.getType(blockposition.down());
            boolean flag = iblockdata1.a(worldserver.getDimensionManager().o());
            int i = (Integer) iblockdata.get(BlockFire.AGE);

            if (!flag && worldserver.isRaining() && this.a((World) worldserver, blockposition) && random.nextFloat() < 0.2F + (float) i * 0.03F) {
                fireExtinguished(worldserver, blockposition); // CraftBukkit - extinguished by rain
            } else {
                int j = Math.min(15, i + random.nextInt(3) / 2);

                if (i != j) {
                    iblockdata = (IBlockData) iblockdata.set(BlockFire.AGE, j);
                    worldserver.setTypeAndData(blockposition, iblockdata, 4);
                }

                if (!flag) {
                    if (!this.canBurn(worldserver, blockposition)) {
                        BlockPosition blockposition1 = blockposition.down();

                        if (!worldserver.getType(blockposition1).d(worldserver, blockposition1, EnumDirection.UP) || i > 3) {
                            fireExtinguished(worldserver, blockposition); // CraftBukkit
                        }

                        return;
                    }

                    if (i == 15 && random.nextInt(4) == 0 && !this.e(worldserver.getType(blockposition.down()))) {
                        fireExtinguished(worldserver, blockposition); // CraftBukkit
                        return;
                    }
                }

                boolean flag1 = worldserver.u(blockposition);
                int k = flag1 ? -50 : 0;

                // CraftBukkit start - add source blockposition to burn calls
                this.trySpread(worldserver, blockposition.east(), 300 + k, random, i, blockposition);
                this.trySpread(worldserver, blockposition.west(), 300 + k, random, i, blockposition);
                this.trySpread(worldserver, blockposition.down(), 250 + k, random, i, blockposition);
                this.trySpread(worldserver, blockposition.up(), 250 + k, random, i, blockposition);
                this.trySpread(worldserver, blockposition.north(), 300 + k, random, i, blockposition);
                this.trySpread(worldserver, blockposition.south(), 300 + k, random, i, blockposition);
                // CraftBukkit end
                BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();

                for (int l = -1; l <= 1; ++l) {
                    for (int i1 = -1; i1 <= 1; ++i1) {
                        for (int j1 = -1; j1 <= 4; ++j1) {
                            if (l != 0 || j1 != 0 || i1 != 0) {
                                int k1 = 100;

                                if (j1 > 1) {
                                    k1 += (j1 - 1) * 100;
                                }

                                blockposition_mutableblockposition.a((BaseBlockPosition) blockposition, l, j1, i1);
                                int l1 = this.a((IWorldReader) worldserver, (BlockPosition) blockposition_mutableblockposition);

                                if (l1 > 0) {
                                    int i2 = (l1 + 40 + worldserver.getDifficulty().a() * 7) / (i + 30);

                                    if (flag1) {
                                        i2 /= 2;
                                    }

                                    if (i2 > 0 && random.nextInt(k1) <= i2 && (!worldserver.isRaining() || !this.a((World) worldserver, (BlockPosition) blockposition_mutableblockposition))) {
                                        int j2 = Math.min(15, i + random.nextInt(5) / 4);

                                        // CraftBukkit start - Call to stop spread of fire
                                        if (worldserver.getType(blockposition_mutableblockposition).getBlock() != Blocks.FIRE) {
                                            if (CraftEventFactory.callBlockIgniteEvent(worldserver, blockposition_mutableblockposition, blockposition).isCancelled()) {
                                                continue;
                                            }

                                            CraftEventFactory.handleBlockSpreadEvent(worldserver, blockposition, blockposition_mutableblockposition, this.a(worldserver, blockposition_mutableblockposition, j2), 3); // CraftBukkit
                                        }
                                        // CraftBukkit end
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    protected boolean a(World world, BlockPosition blockposition) {
        return world.isRainingAt(blockposition) || world.isRainingAt(blockposition.west()) || world.isRainingAt(blockposition.east()) || world.isRainingAt(blockposition.north()) || world.isRainingAt(blockposition.south());
    }

    private int getBurnChance(IBlockData iblockdata) {
        return iblockdata.b(BlockProperties.C) && (Boolean) iblockdata.get(BlockProperties.C) ? 0 : this.burnChances.getInt(iblockdata.getBlock());
    }

    private int getFlameChance(IBlockData iblockdata) {
        return iblockdata.b(BlockProperties.C) && (Boolean) iblockdata.get(BlockProperties.C) ? 0 : this.flameChances.getInt(iblockdata.getBlock());
    }

    private void trySpread(World world, BlockPosition blockposition, int i, Random random, int j, BlockPosition sourceposition) { // CraftBukkit add sourceposition
        int k = this.getBurnChance(world.getType(blockposition));

        if (random.nextInt(i) < k) {
            IBlockData iblockdata = world.getType(blockposition);

            // CraftBukkit start
            org.bukkit.block.Block theBlock = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
            org.bukkit.block.Block sourceBlock = world.getWorld().getBlockAt(sourceposition.getX(), sourceposition.getY(), sourceposition.getZ());

            BlockBurnEvent event = new BlockBurnEvent(theBlock, sourceBlock);
            world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }
            // CraftBukkit end

            if (random.nextInt(j + 10) < 5 && !world.isRainingAt(blockposition)) {
                int l = Math.min(j + random.nextInt(5) / 4, 15);

                world.setTypeAndData(blockposition, this.a(world, blockposition, l), 3);
            } else {
                world.a(blockposition, false);
            }

            Block block = iblockdata.getBlock();

            if (block instanceof BlockTNT) {
                BlockTNT blocktnt = (BlockTNT) block;

                BlockTNT.a(world, blockposition);
            }
        }

    }

    private IBlockData a(GeneratorAccess generatoraccess, BlockPosition blockposition, int i) {
        IBlockData iblockdata = a((IBlockAccess) generatoraccess, blockposition);

        return iblockdata.a(Blocks.FIRE) ? (IBlockData) iblockdata.set(BlockFire.AGE, i) : iblockdata;
    }

    private boolean canBurn(IBlockAccess iblockaccess, BlockPosition blockposition) {
        EnumDirection[] aenumdirection = EnumDirection.values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumDirection enumdirection = aenumdirection[j];

            if (this.e(iblockaccess.getType(blockposition.shift(enumdirection)))) {
                return true;
            }
        }

        return false;
    }

    private int a(IWorldReader iworldreader, BlockPosition blockposition) {
        if (!iworldreader.isEmpty(blockposition)) {
            return 0;
        } else {
            int i = 0;
            EnumDirection[] aenumdirection = EnumDirection.values();
            int j = aenumdirection.length;

            for (int k = 0; k < j; ++k) {
                EnumDirection enumdirection = aenumdirection[k];
                IBlockData iblockdata = iworldreader.getType(blockposition.shift(enumdirection));

                i = Math.max(this.getFlameChance(iblockdata), i);
            }

            return i;
        }
    }

    @Override
    protected boolean e(IBlockData iblockdata) {
        return this.getFlameChance(iblockdata) > 0;
    }

    @Override
    public void onPlace(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        super.onPlace(iblockdata, world, blockposition, iblockdata1, flag);
        world.getBlockTickList().a(blockposition, this, a(world.random));
    }

    private static int a(Random random) {
        return 30 + random.nextInt(10);
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockFire.AGE, BlockFire.NORTH, BlockFire.EAST, BlockFire.SOUTH, BlockFire.WEST, BlockFire.UPPER);
    }

    private void a(Block block, int i, int j) {
        this.flameChances.put(block, i);
        this.burnChances.put(block, j);
    }

    public static void c() {
        BlockFire blockfire = (BlockFire) Blocks.FIRE;

        blockfire.a(Blocks.OAK_PLANKS, 5, 20);
        blockfire.a(Blocks.SPRUCE_PLANKS, 5, 20);
        blockfire.a(Blocks.BIRCH_PLANKS, 5, 20);
        blockfire.a(Blocks.JUNGLE_PLANKS, 5, 20);
        blockfire.a(Blocks.ACACIA_PLANKS, 5, 20);
        blockfire.a(Blocks.DARK_OAK_PLANKS, 5, 20);
        blockfire.a(Blocks.OAK_SLAB, 5, 20);
        blockfire.a(Blocks.SPRUCE_SLAB, 5, 20);
        blockfire.a(Blocks.BIRCH_SLAB, 5, 20);
        blockfire.a(Blocks.JUNGLE_SLAB, 5, 20);
        blockfire.a(Blocks.ACACIA_SLAB, 5, 20);
        blockfire.a(Blocks.DARK_OAK_SLAB, 5, 20);
        blockfire.a(Blocks.OAK_FENCE_GATE, 5, 20);
        blockfire.a(Blocks.SPRUCE_FENCE_GATE, 5, 20);
        blockfire.a(Blocks.BIRCH_FENCE_GATE, 5, 20);
        blockfire.a(Blocks.JUNGLE_FENCE_GATE, 5, 20);
        blockfire.a(Blocks.DARK_OAK_FENCE_GATE, 5, 20);
        blockfire.a(Blocks.ACACIA_FENCE_GATE, 5, 20);
        blockfire.a(Blocks.OAK_FENCE, 5, 20);
        blockfire.a(Blocks.SPRUCE_FENCE, 5, 20);
        blockfire.a(Blocks.BIRCH_FENCE, 5, 20);
        blockfire.a(Blocks.JUNGLE_FENCE, 5, 20);
        blockfire.a(Blocks.DARK_OAK_FENCE, 5, 20);
        blockfire.a(Blocks.ACACIA_FENCE, 5, 20);
        blockfire.a(Blocks.OAK_STAIRS, 5, 20);
        blockfire.a(Blocks.BIRCH_STAIRS, 5, 20);
        blockfire.a(Blocks.SPRUCE_STAIRS, 5, 20);
        blockfire.a(Blocks.JUNGLE_STAIRS, 5, 20);
        blockfire.a(Blocks.ACACIA_STAIRS, 5, 20);
        blockfire.a(Blocks.DARK_OAK_STAIRS, 5, 20);
        blockfire.a(Blocks.OAK_LOG, 5, 5);
        blockfire.a(Blocks.SPRUCE_LOG, 5, 5);
        blockfire.a(Blocks.BIRCH_LOG, 5, 5);
        blockfire.a(Blocks.JUNGLE_LOG, 5, 5);
        blockfire.a(Blocks.ACACIA_LOG, 5, 5);
        blockfire.a(Blocks.DARK_OAK_LOG, 5, 5);
        blockfire.a(Blocks.STRIPPED_OAK_LOG, 5, 5);
        blockfire.a(Blocks.STRIPPED_SPRUCE_LOG, 5, 5);
        blockfire.a(Blocks.STRIPPED_BIRCH_LOG, 5, 5);
        blockfire.a(Blocks.STRIPPED_JUNGLE_LOG, 5, 5);
        blockfire.a(Blocks.STRIPPED_ACACIA_LOG, 5, 5);
        blockfire.a(Blocks.STRIPPED_DARK_OAK_LOG, 5, 5);
        blockfire.a(Blocks.STRIPPED_OAK_WOOD, 5, 5);
        blockfire.a(Blocks.STRIPPED_SPRUCE_WOOD, 5, 5);
        blockfire.a(Blocks.STRIPPED_BIRCH_WOOD, 5, 5);
        blockfire.a(Blocks.STRIPPED_JUNGLE_WOOD, 5, 5);
        blockfire.a(Blocks.STRIPPED_ACACIA_WOOD, 5, 5);
        blockfire.a(Blocks.STRIPPED_DARK_OAK_WOOD, 5, 5);
        blockfire.a(Blocks.OAK_WOOD, 5, 5);
        blockfire.a(Blocks.SPRUCE_WOOD, 5, 5);
        blockfire.a(Blocks.BIRCH_WOOD, 5, 5);
        blockfire.a(Blocks.JUNGLE_WOOD, 5, 5);
        blockfire.a(Blocks.ACACIA_WOOD, 5, 5);
        blockfire.a(Blocks.DARK_OAK_WOOD, 5, 5);
        blockfire.a(Blocks.OAK_LEAVES, 30, 60);
        blockfire.a(Blocks.SPRUCE_LEAVES, 30, 60);
        blockfire.a(Blocks.BIRCH_LEAVES, 30, 60);
        blockfire.a(Blocks.JUNGLE_LEAVES, 30, 60);
        blockfire.a(Blocks.ACACIA_LEAVES, 30, 60);
        blockfire.a(Blocks.DARK_OAK_LEAVES, 30, 60);
        blockfire.a(Blocks.BOOKSHELF, 30, 20);
        blockfire.a(Blocks.TNT, 15, 100);
        blockfire.a(Blocks.GRASS, 60, 100);
        blockfire.a(Blocks.FERN, 60, 100);
        blockfire.a(Blocks.DEAD_BUSH, 60, 100);
        blockfire.a(Blocks.SUNFLOWER, 60, 100);
        blockfire.a(Blocks.LILAC, 60, 100);
        blockfire.a(Blocks.ROSE_BUSH, 60, 100);
        blockfire.a(Blocks.PEONY, 60, 100);
        blockfire.a(Blocks.TALL_GRASS, 60, 100);
        blockfire.a(Blocks.LARGE_FERN, 60, 100);
        blockfire.a(Blocks.DANDELION, 60, 100);
        blockfire.a(Blocks.POPPY, 60, 100);
        blockfire.a(Blocks.BLUE_ORCHID, 60, 100);
        blockfire.a(Blocks.ALLIUM, 60, 100);
        blockfire.a(Blocks.AZURE_BLUET, 60, 100);
        blockfire.a(Blocks.RED_TULIP, 60, 100);
        blockfire.a(Blocks.ORANGE_TULIP, 60, 100);
        blockfire.a(Blocks.WHITE_TULIP, 60, 100);
        blockfire.a(Blocks.PINK_TULIP, 60, 100);
        blockfire.a(Blocks.OXEYE_DAISY, 60, 100);
        blockfire.a(Blocks.CORNFLOWER, 60, 100);
        blockfire.a(Blocks.LILY_OF_THE_VALLEY, 60, 100);
        blockfire.a(Blocks.WITHER_ROSE, 60, 100);
        blockfire.a(Blocks.WHITE_WOOL, 30, 60);
        blockfire.a(Blocks.ORANGE_WOOL, 30, 60);
        blockfire.a(Blocks.MAGENTA_WOOL, 30, 60);
        blockfire.a(Blocks.LIGHT_BLUE_WOOL, 30, 60);
        blockfire.a(Blocks.YELLOW_WOOL, 30, 60);
        blockfire.a(Blocks.LIME_WOOL, 30, 60);
        blockfire.a(Blocks.PINK_WOOL, 30, 60);
        blockfire.a(Blocks.GRAY_WOOL, 30, 60);
        blockfire.a(Blocks.LIGHT_GRAY_WOOL, 30, 60);
        blockfire.a(Blocks.CYAN_WOOL, 30, 60);
        blockfire.a(Blocks.PURPLE_WOOL, 30, 60);
        blockfire.a(Blocks.BLUE_WOOL, 30, 60);
        blockfire.a(Blocks.BROWN_WOOL, 30, 60);
        blockfire.a(Blocks.GREEN_WOOL, 30, 60);
        blockfire.a(Blocks.RED_WOOL, 30, 60);
        blockfire.a(Blocks.BLACK_WOOL, 30, 60);
        blockfire.a(Blocks.VINE, 15, 100);
        blockfire.a(Blocks.COAL_BLOCK, 5, 5);
        blockfire.a(Blocks.HAY_BLOCK, 60, 20);
        blockfire.a(Blocks.TARGET, 15, 20);
        blockfire.a(Blocks.WHITE_CARPET, 60, 20);
        blockfire.a(Blocks.ORANGE_CARPET, 60, 20);
        blockfire.a(Blocks.MAGENTA_CARPET, 60, 20);
        blockfire.a(Blocks.LIGHT_BLUE_CARPET, 60, 20);
        blockfire.a(Blocks.YELLOW_CARPET, 60, 20);
        blockfire.a(Blocks.LIME_CARPET, 60, 20);
        blockfire.a(Blocks.PINK_CARPET, 60, 20);
        blockfire.a(Blocks.GRAY_CARPET, 60, 20);
        blockfire.a(Blocks.LIGHT_GRAY_CARPET, 60, 20);
        blockfire.a(Blocks.CYAN_CARPET, 60, 20);
        blockfire.a(Blocks.PURPLE_CARPET, 60, 20);
        blockfire.a(Blocks.BLUE_CARPET, 60, 20);
        blockfire.a(Blocks.BROWN_CARPET, 60, 20);
        blockfire.a(Blocks.GREEN_CARPET, 60, 20);
        blockfire.a(Blocks.RED_CARPET, 60, 20);
        blockfire.a(Blocks.BLACK_CARPET, 60, 20);
        blockfire.a(Blocks.DRIED_KELP_BLOCK, 30, 60);
        blockfire.a(Blocks.BAMBOO, 60, 60);
        blockfire.a(Blocks.SCAFFOLDING, 60, 60);
        blockfire.a(Blocks.LECTERN, 30, 20);
        blockfire.a(Blocks.COMPOSTER, 5, 20);
        blockfire.a(Blocks.SWEET_BERRY_BUSH, 60, 100);
        blockfire.a(Blocks.BEEHIVE, 5, 20);
        blockfire.a(Blocks.BEE_NEST, 30, 20);
    }
}
