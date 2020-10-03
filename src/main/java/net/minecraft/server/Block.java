package net.minecraft.server;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Block extends BlockBase implements IMaterial {

    protected static final Logger LOGGER = LogManager.getLogger();
    public static final RegistryBlockID<IBlockData> REGISTRY_ID = new RegistryBlockID<>();
    private static final LoadingCache<VoxelShape, Boolean> a = CacheBuilder.newBuilder().maximumSize(512L).weakKeys().build(new CacheLoader<VoxelShape, Boolean>() {
        public Boolean load(VoxelShape voxelshape) {
            return !VoxelShapes.c(VoxelShapes.b(), voxelshape, OperatorBoolean.NOT_SAME);
        }
    });
    protected final BlockStateList<Block, IBlockData> blockStateList;
    private IBlockData blockData;
    @Nullable
    private String name;
    @Nullable
    private Item d;
    private static final ThreadLocal<Object2ByteLinkedOpenHashMap<Block.a>> e = ThreadLocal.withInitial(() -> {
        Object2ByteLinkedOpenHashMap<Block.a> object2bytelinkedopenhashmap = new Object2ByteLinkedOpenHashMap<Block.a>(2048, 0.25F) {
            protected void rehash(int i) {}
        };

        object2bytelinkedopenhashmap.defaultReturnValue((byte) 127);
        return object2bytelinkedopenhashmap;
    });

    public static int getCombinedId(@Nullable IBlockData iblockdata) {
        if (iblockdata == null) {
            return 0;
        } else {
            int i = Block.REGISTRY_ID.getId(iblockdata);

            return i == -1 ? 0 : i;
        }
    }

    public static IBlockData getByCombinedId(int i) {
        IBlockData iblockdata = (IBlockData) Block.REGISTRY_ID.fromId(i);

        return iblockdata == null ? Blocks.AIR.getBlockData() : iblockdata;
    }

    public static Block asBlock(@Nullable Item item) {
        return item instanceof ItemBlock ? ((ItemBlock) item).getBlock() : Blocks.AIR;
    }

    public static IBlockData a(IBlockData iblockdata, IBlockData iblockdata1, World world, BlockPosition blockposition) {
        VoxelShape voxelshape = VoxelShapes.b(iblockdata.getCollisionShape(world, blockposition), iblockdata1.getCollisionShape(world, blockposition), OperatorBoolean.ONLY_SECOND).a((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ());
        List<Entity> list = world.getEntities((Entity) null, voxelshape.getBoundingBox());
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();
            double d0 = VoxelShapes.a(EnumDirection.EnumAxis.Y, entity.getBoundingBox().d(0.0D, 1.0D, 0.0D), Stream.of(voxelshape), -1.0D);

            entity.enderTeleportTo(entity.locX(), entity.locY() + 1.0D + d0, entity.locZ());
        }

        return iblockdata1;
    }

    public static VoxelShape a(double d0, double d1, double d2, double d3, double d4, double d5) {
        return VoxelShapes.create(d0 / 16.0D, d1 / 16.0D, d2 / 16.0D, d3 / 16.0D, d4 / 16.0D, d5 / 16.0D);
    }

    public boolean a(Tag<Block> tag) {
        return tag.isTagged(this);
    }

    public boolean a(Block block) {
        return this == block;
    }

    public static IBlockData b(IBlockData iblockdata, GeneratorAccess generatoraccess, BlockPosition blockposition) {
        IBlockData iblockdata1 = iblockdata;
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
        EnumDirection[] aenumdirection = Block.ar;
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumDirection enumdirection = aenumdirection[j];

            blockposition_mutableblockposition.a((BaseBlockPosition) blockposition, enumdirection);
            iblockdata1 = iblockdata1.updateState(enumdirection, generatoraccess.getType(blockposition_mutableblockposition), generatoraccess, blockposition, blockposition_mutableblockposition);
        }

        return iblockdata1;
    }

    public static void a(IBlockData iblockdata, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, int i) {
        a(iblockdata, iblockdata1, generatoraccess, blockposition, i, 512);
    }

    public static void a(IBlockData iblockdata, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, int i, int j) {
        if (iblockdata1 != iblockdata) {
            if (iblockdata1.isAir()) {
                if (!generatoraccess.s_()) {
                    generatoraccess.a(blockposition, (i & 32) == 0, (Entity) null, j);
                }
            } else {
                generatoraccess.a(blockposition, iblockdata1, i & -33, j);
            }
        }

    }

    public Block(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        BlockStateList.a<Block, IBlockData> blockstatelist_a = new BlockStateList.a<>(this);

        this.a(blockstatelist_a);
        this.blockStateList = blockstatelist_a.a(Block::getBlockData, IBlockData::new);
        this.j((IBlockData) this.blockStateList.getBlockData());
    }

    public static boolean b(Block block) {
        return block instanceof BlockLeaves || block == Blocks.BARRIER || block == Blocks.CARVED_PUMPKIN || block == Blocks.JACK_O_LANTERN || block == Blocks.MELON || block == Blocks.PUMPKIN || block.a((Tag) TagsBlock.SHULKER_BOXES);
    }

    public boolean isTicking(IBlockData iblockdata) {
        return this.av;
    }

    public static boolean c(IBlockAccess iblockaccess, BlockPosition blockposition) {
        return iblockaccess.getType(blockposition).a(iblockaccess, blockposition, EnumDirection.UP, EnumBlockSupport.RIGID);
    }

    public static boolean a(IWorldReader iworldreader, BlockPosition blockposition, EnumDirection enumdirection) {
        IBlockData iblockdata = iworldreader.getType(blockposition);

        return enumdirection == EnumDirection.DOWN && iblockdata.a((Tag) TagsBlock.aC) ? false : iblockdata.a((IBlockAccess) iworldreader, blockposition, enumdirection, EnumBlockSupport.CENTER);
    }

    public static boolean a(VoxelShape voxelshape, EnumDirection enumdirection) {
        VoxelShape voxelshape1 = voxelshape.a(enumdirection);

        return a(voxelshape1);
    }

    public static boolean a(VoxelShape voxelshape) {
        return (Boolean) Block.a.getUnchecked(voxelshape);
    }

    public boolean b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return !a(iblockdata.getShape(iblockaccess, blockposition)) && iblockdata.getFluid().isEmpty();
    }

    public void postBreak(GeneratorAccess generatoraccess, BlockPosition blockposition, IBlockData iblockdata) {}

    public static List<ItemStack> a(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, @Nullable TileEntity tileentity) {
        LootTableInfo.Builder loottableinfo_builder = (new LootTableInfo.Builder(worldserver)).a(worldserver.random).set(LootContextParameters.ORIGIN, Vec3D.a((BaseBlockPosition) blockposition)).set(LootContextParameters.TOOL, ItemStack.b).setOptional(LootContextParameters.BLOCK_ENTITY, tileentity);

        return iblockdata.a(loottableinfo_builder);
    }

    public static List<ItemStack> getDrops(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, @Nullable TileEntity tileentity, @Nullable Entity entity, ItemStack itemstack) {
        LootTableInfo.Builder loottableinfo_builder = (new LootTableInfo.Builder(worldserver)).a(worldserver.random).set(LootContextParameters.ORIGIN, Vec3D.a((BaseBlockPosition) blockposition)).set(LootContextParameters.TOOL, itemstack).setOptional(LootContextParameters.THIS_ENTITY, entity).setOptional(LootContextParameters.BLOCK_ENTITY, tileentity);

        return iblockdata.a(loottableinfo_builder);
    }

    public static void c(IBlockData iblockdata, World world, BlockPosition blockposition) {
        if (world instanceof WorldServer) {
            a(iblockdata, (WorldServer) world, blockposition, (TileEntity) null).forEach((itemstack) -> {
                a(world, blockposition, itemstack);
            });
            iblockdata.dropNaturally((WorldServer) world, blockposition, ItemStack.b);
        }

    }

    public static void a(IBlockData iblockdata, GeneratorAccess generatoraccess, BlockPosition blockposition, @Nullable TileEntity tileentity) {
        if (generatoraccess instanceof WorldServer) {
            a(iblockdata, (WorldServer) generatoraccess, blockposition, tileentity).forEach((itemstack) -> {
                a((World) ((WorldServer) generatoraccess), blockposition, itemstack);
            });
            iblockdata.dropNaturally((WorldServer) generatoraccess, blockposition, ItemStack.b);
        }

    }

    public static void dropItems(IBlockData iblockdata, World world, BlockPosition blockposition, @Nullable TileEntity tileentity, Entity entity, ItemStack itemstack) {
        if (world instanceof WorldServer) {
            getDrops(iblockdata, (WorldServer) world, blockposition, tileentity, entity, itemstack).forEach((itemstack1) -> {
                a(world, blockposition, itemstack1);
            });
            iblockdata.dropNaturally((WorldServer) world, blockposition, itemstack);
        }

    }

    public static void a(World world, BlockPosition blockposition, ItemStack itemstack) {
        if (!world.isClientSide && !itemstack.isEmpty() && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
            float f = 0.5F;
            double d0 = (double) (world.random.nextFloat() * 0.5F) + 0.25D;
            double d1 = (double) (world.random.nextFloat() * 0.5F) + 0.25D;
            double d2 = (double) (world.random.nextFloat() * 0.5F) + 0.25D;
            EntityItem entityitem = new EntityItem(world, (double) blockposition.getX() + d0, (double) blockposition.getY() + d1, (double) blockposition.getZ() + d2, itemstack);

            entityitem.defaultPickupDelay();
            // CraftBukkit start
            if (world.captureDrops != null) {
                world.captureDrops.add(entityitem);
            } else {
                world.addEntity(entityitem);
            }
            // CraftBukkit end
        }
    }

    protected void dropExperience(WorldServer worldserver, BlockPosition blockposition, int i) {
        if (worldserver.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
            while (i > 0) {
                int j = EntityExperienceOrb.getOrbValue(i);

                i -= j;
                worldserver.addEntity(new EntityExperienceOrb(worldserver, (double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D, j));
            }
        }

    }

    public float getDurability() {
        return this.durability;
    }

    public void wasExploded(World world, BlockPosition blockposition, Explosion explosion) {}

    public void stepOn(World world, BlockPosition blockposition, Entity entity) {}

    @Nullable
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        return this.getBlockData();
    }

    public void a(World world, EntityHuman entityhuman, BlockPosition blockposition, IBlockData iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        entityhuman.b(StatisticList.BLOCK_MINED.b(this));
        entityhuman.applyExhaustion(0.005F);
        dropItems(iblockdata, world, blockposition, tileentity, entityhuman, itemstack);
    }

    public void postPlace(World world, BlockPosition blockposition, IBlockData iblockdata, @Nullable EntityLiving entityliving, ItemStack itemstack) {}

    public boolean ai_() {
        return !this.material.isBuildable() && !this.material.isLiquid();
    }

    public String i() {
        if (this.name == null) {
            this.name = SystemUtils.a("block", IRegistry.BLOCK.getKey(this));
        }

        return this.name;
    }

    public void fallOn(World world, BlockPosition blockposition, Entity entity, float f) {
        entity.b(f, 1.0F);
    }

    public void a(IBlockAccess iblockaccess, Entity entity) {
        entity.setMot(entity.getMot().d(1.0D, 0.0D, 1.0D));
    }

    public void a(CreativeModeTab creativemodetab, NonNullList<ItemStack> nonnulllist) {
        nonnulllist.add(new ItemStack(this));
    }

    public float getFrictionFactor() {
        return this.frictionFactor;
    }

    public float getSpeedFactor() {
        return this.speedFactor;
    }

    public float getJumpFactor() {
        return this.jumpFactor;
    }

    public void a(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman) {
        world.a(entityhuman, 2001, blockposition, getCombinedId(iblockdata));
        if (this.a((Tag) TagsBlock.GUARDED_BY_PIGLINS)) {
            PiglinAI.a(entityhuman, false);
        }

    }

    public void c(World world, BlockPosition blockposition) {}

    public boolean a(Explosion explosion) {
        return true;
    }

    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {}

    public BlockStateList<Block, IBlockData> getStates() {
        return this.blockStateList;
    }

    protected final void j(IBlockData iblockdata) {
        this.blockData = iblockdata;
    }

    public final IBlockData getBlockData() {
        return this.blockData;
    }

    public SoundEffectType getStepSound(IBlockData iblockdata) {
        return this.stepSound;
    }

    @Override
    public Item getItem() {
        if (this.d == null) {
            this.d = Item.getItemOf(this);
        }

        return this.d;
    }

    public boolean o() {
        return this.aA;
    }

    public String toString() {
        return "Block{" + IRegistry.BLOCK.getKey(this) + "}";
    }

    @Override
    protected Block p() {
        return this;
    }

    // CraftBukkit start
    public int getExpDrop(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, ItemStack itemstack) {
        return 0;
    }
    // CraftBukkit end

    public static final class a {

        private final IBlockData a;
        private final IBlockData b;
        private final EnumDirection c;

        public a(IBlockData iblockdata, IBlockData iblockdata1, EnumDirection enumdirection) {
            this.a = iblockdata;
            this.b = iblockdata1;
            this.c = enumdirection;
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            } else if (!(object instanceof Block.a)) {
                return false;
            } else {
                Block.a block_a = (Block.a) object;

                return this.a == block_a.a && this.b == block_a.b && this.c == block_a.c;
            }
        }

        public int hashCode() {
            int i = this.a.hashCode();

            i = 31 * i + this.b.hashCode();
            i = 31 * i + this.c.hashCode();
            return i;
        }
    }
}
