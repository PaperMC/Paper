package net.minecraft.server;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import java.util.Random;
import javax.annotation.Nullable;
// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftBlockInventoryHolder;
import org.bukkit.craftbukkit.util.DummyGeneratorAccess;
// CraftBukkit end

public class BlockComposter extends Block implements IInventoryHolder {

    public static final BlockStateInteger a = BlockProperties.as;
    public static final Object2FloatMap<IMaterial> b = new Object2FloatOpenHashMap();
    private static final VoxelShape c = VoxelShapes.b();
    private static final VoxelShape[] d = (VoxelShape[]) SystemUtils.a((new VoxelShape[9]), (avoxelshape) -> { // CraftBukkit - decompile error
        for (int i = 0; i < 8; ++i) {
            avoxelshape[i] = VoxelShapes.a(BlockComposter.c, Block.a(2.0D, (double) Math.max(2, 1 + i * 2), 2.0D, 14.0D, 16.0D, 14.0D), OperatorBoolean.ONLY_FIRST);
        }

        avoxelshape[8] = avoxelshape[7];
    });

    public static void c() {
        BlockComposter.b.defaultReturnValue(-1.0F);
        float f = 0.3F;
        float f1 = 0.5F;
        float f2 = 0.65F;
        float f3 = 0.85F;
        float f4 = 1.0F;

        a(0.3F, Items.au);
        a(0.3F, Items.ar);
        a(0.3F, Items.as);
        a(0.3F, Items.aw);
        a(0.3F, Items.av);
        a(0.3F, Items.at);
        a(0.3F, Items.x);
        a(0.3F, Items.y);
        a(0.3F, Items.z);
        a(0.3F, Items.A);
        a(0.3F, Items.B);
        a(0.3F, Items.C);
        a(0.3F, Items.BEETROOT_SEEDS);
        a(0.3F, Items.DRIED_KELP);
        a(0.3F, Items.aL);
        a(0.3F, Items.bE);
        a(0.3F, Items.MELON_SEEDS);
        a(0.3F, Items.PUMPKIN_SEEDS);
        a(0.3F, Items.aO);
        a(0.3F, Items.SWEET_BERRIES);
        a(0.3F, Items.WHEAT_SEEDS);
        a(0.5F, Items.ma);
        a(0.5F, Items.gn);
        a(0.5F, Items.cX);
        a(0.5F, Items.bD);
        a(0.5F, Items.dR);
        a(0.5F, Items.bA);
        a(0.5F, Items.bB);
        a(0.5F, Items.bC);
        a(0.5F, Items.MELON_SLICE);
        a(0.65F, Items.aP);
        a(0.65F, Items.ed);
        a(0.65F, Items.di);
        a(0.65F, Items.dj);
        a(0.65F, Items.dQ);
        a(0.65F, Items.APPLE);
        a(0.65F, Items.BEETROOT);
        a(0.65F, Items.CARROT);
        a(0.65F, Items.COCOA_BEANS);
        a(0.65F, Items.POTATO);
        a(0.65F, Items.WHEAT);
        a(0.65F, Items.bu);
        a(0.65F, Items.bv);
        a(0.65F, Items.dM);
        a(0.65F, Items.bw);
        a(0.65F, Items.bx);
        a(0.65F, Items.NETHER_WART);
        a(0.65F, Items.by);
        a(0.65F, Items.bz);
        a(0.65F, Items.rp);
        a(0.65F, Items.bh);
        a(0.65F, Items.bi);
        a(0.65F, Items.bj);
        a(0.65F, Items.bk);
        a(0.65F, Items.bl);
        a(0.65F, Items.bm);
        a(0.65F, Items.bn);
        a(0.65F, Items.bo);
        a(0.65F, Items.bp);
        a(0.65F, Items.bq);
        a(0.65F, Items.br);
        a(0.65F, Items.bs);
        a(0.65F, Items.bt);
        a(0.65F, Items.aM);
        a(0.65F, Items.gj);
        a(0.65F, Items.gk);
        a(0.65F, Items.gl);
        a(0.65F, Items.gm);
        a(0.65F, Items.go);
        a(0.85F, Items.fL);
        a(0.85F, Items.dK);
        a(0.85F, Items.dL);
        a(0.85F, Items.hj);
        a(0.85F, Items.hk);
        a(0.85F, Items.BREAD);
        a(0.85F, Items.BAKED_POTATO);
        a(0.85F, Items.COOKIE);
        a(1.0F, Items.mN);
        a(1.0F, Items.PUMPKIN_PIE);
    }

    private static void a(float f, IMaterial imaterial) {
        BlockComposter.b.put(imaterial.getItem(), f);
    }

    public BlockComposter(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockComposter.a, 0));
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return BlockComposter.d[(Integer) iblockdata.get(BlockComposter.a)];
    }

    @Override
    public VoxelShape a_(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return BlockComposter.c;
    }

    @Override
    public VoxelShape c(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return BlockComposter.d[0];
    }

    @Override
    public void onPlace(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if ((Integer) iblockdata.get(BlockComposter.a) == 7) {
            world.getBlockTickList().a(blockposition, iblockdata.getBlock(), 20);
        }

    }

    @Override
    public EnumInteractionResult interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        int i = (Integer) iblockdata.get(BlockComposter.a);
        ItemStack itemstack = entityhuman.b(enumhand);

        if (i < 8 && BlockComposter.b.containsKey(itemstack.getItem())) {
            if (i < 7 && !world.isClientSide) {
                IBlockData iblockdata1 = b(iblockdata, (GeneratorAccess) world, blockposition, itemstack);

                world.triggerEffect(1500, blockposition, iblockdata != iblockdata1 ? 1 : 0);
                if (!entityhuman.abilities.canInstantlyBuild) {
                    itemstack.subtract(1);
                }
            }

            return EnumInteractionResult.a(world.isClientSide);
        } else if (i == 8) {
            d(iblockdata, world, blockposition, (Entity) null); // CraftBukkit - no event for players
            return EnumInteractionResult.a(world.isClientSide);
        } else {
            return EnumInteractionResult.PASS;
        }
    }

    public static IBlockData a(IBlockData iblockdata, WorldServer worldserver, ItemStack itemstack, BlockPosition blockposition, Entity entity) { // CraftBukkit
        int i = (Integer) iblockdata.get(BlockComposter.a);

        if (i < 7 && BlockComposter.b.containsKey(itemstack.getItem())) {
            // CraftBukkit start
            double rand = worldserver.getRandom().nextDouble();
            IBlockData iblockdata1 = b(iblockdata, DummyGeneratorAccess.INSTANCE, blockposition, itemstack, rand);
            if (iblockdata == iblockdata1 || org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(entity, blockposition, iblockdata1).isCancelled()) {
                return iblockdata;
            }
            iblockdata1 = b(iblockdata, (GeneratorAccess) worldserver, blockposition, itemstack, rand);
            // CraftBukkit end

            itemstack.subtract(1);
            return iblockdata1;
        } else {
            return iblockdata;
        }
    }

    // CraftBukkit start
    public static IBlockData d(IBlockData iblockdata, World world, BlockPosition blockposition, Entity entity) {
        if (entity != null) {
            IBlockData iblockdata1 = d(iblockdata, DummyGeneratorAccess.INSTANCE, blockposition);
            if (org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(entity, blockposition, iblockdata1).isCancelled()) {
                return iblockdata;
            }
        }
        // CraftBukkit end
        if (!world.isClientSide) {
            float f = 0.7F;
            double d0 = (double) (world.random.nextFloat() * 0.7F) + 0.15000000596046448D;
            double d1 = (double) (world.random.nextFloat() * 0.7F) + 0.06000000238418579D + 0.6D;
            double d2 = (double) (world.random.nextFloat() * 0.7F) + 0.15000000596046448D;
            EntityItem entityitem = new EntityItem(world, (double) blockposition.getX() + d0, (double) blockposition.getY() + d1, (double) blockposition.getZ() + d2, new ItemStack(Items.BONE_MEAL));

            entityitem.defaultPickupDelay();
            world.addEntity(entityitem);
        }

        IBlockData iblockdata1 = d(iblockdata, (GeneratorAccess) world, blockposition);

        world.playSound((EntityHuman) null, blockposition, SoundEffects.BLOCK_COMPOSTER_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
        return iblockdata1;
    }

    private static IBlockData d(IBlockData iblockdata, GeneratorAccess generatoraccess, BlockPosition blockposition) {
        IBlockData iblockdata1 = (IBlockData) iblockdata.set(BlockComposter.a, 0);

        generatoraccess.setTypeAndData(blockposition, iblockdata1, 3);
        return iblockdata1;
    }

    private static IBlockData b(IBlockData iblockdata, GeneratorAccess generatoraccess, BlockPosition blockposition, ItemStack itemstack) {
        // CraftBukkit start
        return b(iblockdata, generatoraccess, blockposition, itemstack, generatoraccess.getRandom().nextDouble());
    }

    private static IBlockData b(IBlockData iblockdata, GeneratorAccess generatoraccess, BlockPosition blockposition, ItemStack itemstack, double rand) {
        // CraftBukkit end
        int i = (Integer) iblockdata.get(BlockComposter.a);
        float f = BlockComposter.b.getFloat(itemstack.getItem());

        if ((i != 0 || f <= 0.0F) && rand >= (double) f) {
            return iblockdata;
        } else {
            int j = i + 1;
            IBlockData iblockdata1 = (IBlockData) iblockdata.set(BlockComposter.a, j);

            generatoraccess.setTypeAndData(blockposition, iblockdata1, 3);
            if (j == 7) {
                generatoraccess.getBlockTickList().a(blockposition, iblockdata.getBlock(), 20);
            }

            return iblockdata1;
        }
    }

    @Override
    public void tickAlways(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        if ((Integer) iblockdata.get(BlockComposter.a) == 7) {
            worldserver.setTypeAndData(blockposition, (IBlockData) iblockdata.a((IBlockState) BlockComposter.a), 3);
            worldserver.playSound((EntityHuman) null, blockposition, SoundEffects.BLOCK_COMPOSTER_READY, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }

    }

    @Override
    public boolean isComplexRedstone(IBlockData iblockdata) {
        return true;
    }

    @Override
    public int a(IBlockData iblockdata, World world, BlockPosition blockposition) {
        return (Integer) iblockdata.get(BlockComposter.a);
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockComposter.a);
    }

    @Override
    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        return false;
    }

    @Override
    public IWorldInventory a(IBlockData iblockdata, GeneratorAccess generatoraccess, BlockPosition blockposition) {
        int i = (Integer) iblockdata.get(BlockComposter.a);

        // CraftBukkit - empty generatoraccess, blockposition
        return (IWorldInventory) (i == 8 ? new BlockComposter.ContainerOutput(iblockdata, generatoraccess, blockposition, new ItemStack(Items.BONE_MEAL)) : (i < 7 ? new BlockComposter.ContainerInput(iblockdata, generatoraccess, blockposition) : new BlockComposter.ContainerEmpty(generatoraccess, blockposition)));
    }

    static class ContainerInput extends InventorySubcontainer implements IWorldInventory {

        private final IBlockData a;
        private final GeneratorAccess b;
        private final BlockPosition c;
        private boolean d;

        public ContainerInput(IBlockData iblockdata, GeneratorAccess generatoraccess, BlockPosition blockposition) {
            super(1);
            this.bukkitOwner = new CraftBlockInventoryHolder(generatoraccess, blockposition, this); // CraftBukkit
            this.a = iblockdata;
            this.b = generatoraccess;
            this.c = blockposition;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public int[] getSlotsForFace(EnumDirection enumdirection) {
            return enumdirection == EnumDirection.UP ? new int[]{0} : new int[0];
        }

        @Override
        public boolean canPlaceItemThroughFace(int i, ItemStack itemstack, @Nullable EnumDirection enumdirection) {
            return !this.d && enumdirection == EnumDirection.UP && BlockComposter.b.containsKey(itemstack.getItem());
        }

        @Override
        public boolean canTakeItemThroughFace(int i, ItemStack itemstack, EnumDirection enumdirection) {
            return false;
        }

        @Override
        public void update() {
            ItemStack itemstack = this.getItem(0);

            if (!itemstack.isEmpty()) {
                this.d = true;
                IBlockData iblockdata = BlockComposter.b(this.a, this.b, this.c, itemstack);

                this.b.triggerEffect(1500, this.c, iblockdata != this.a ? 1 : 0);
                this.splitWithoutUpdate(0);
            }

        }
    }

    static class ContainerOutput extends InventorySubcontainer implements IWorldInventory {

        private final IBlockData blockData;
        private final GeneratorAccess generatorAccess;
        private final BlockPosition blockPosition;
        private boolean emptied;

        public ContainerOutput(IBlockData iblockdata, GeneratorAccess generatoraccess, BlockPosition blockposition, ItemStack itemstack) {
            super(itemstack);
            this.blockData = iblockdata;
            this.generatorAccess = generatoraccess;
            this.blockPosition = blockposition;
            this.bukkitOwner = new CraftBlockInventoryHolder(generatoraccess, blockposition, this); // CraftBukkit
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public int[] getSlotsForFace(EnumDirection enumdirection) {
            return enumdirection == EnumDirection.DOWN ? new int[]{0} : new int[0];
        }

        @Override
        public boolean canPlaceItemThroughFace(int i, ItemStack itemstack, @Nullable EnumDirection enumdirection) {
            return false;
        }

        @Override
        public boolean canTakeItemThroughFace(int i, ItemStack itemstack, EnumDirection enumdirection) {
            return !this.emptied && enumdirection == EnumDirection.DOWN && itemstack.getItem() == Items.BONE_MEAL;
        }

        @Override
        public void update() {
            // CraftBukkit start - allow putting items back (eg cancelled InventoryMoveItemEvent)
            if (this.isEmpty()) {
                BlockComposter.d(this.blockData, this.generatorAccess, this.blockPosition);
                this.emptied = true;
            } else {
                this.generatorAccess.setTypeAndData(this.blockPosition, this.blockData, 3);
                this.emptied = false;
            }
            // CraftBukkit end
        }
    }

    static class ContainerEmpty extends InventorySubcontainer implements IWorldInventory {

        public ContainerEmpty(GeneratorAccess generatoraccess, BlockPosition blockposition) { // CraftBukkit
            super(0);
            this.bukkitOwner = new CraftBlockInventoryHolder(generatoraccess, blockposition, this); // CraftBukkit
        }

        @Override
        public int[] getSlotsForFace(EnumDirection enumdirection) {
            return new int[0];
        }

        @Override
        public boolean canPlaceItemThroughFace(int i, ItemStack itemstack, @Nullable EnumDirection enumdirection) {
            return false;
        }

        @Override
        public boolean canTakeItemThroughFace(int i, ItemStack itemstack, EnumDirection enumdirection) {
            return false;
        }
    }
}
