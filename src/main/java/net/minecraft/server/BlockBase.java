package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import javax.annotation.Nullable;

public abstract class BlockBase {

    protected static final EnumDirection[] ar = new EnumDirection[]{EnumDirection.WEST, EnumDirection.EAST, EnumDirection.NORTH, EnumDirection.SOUTH, EnumDirection.DOWN, EnumDirection.UP};
    protected final Material material;
    protected final boolean at;
    protected final float durability;
    protected final boolean av;
    protected final SoundEffectType stepSound;
    protected final float frictionFactor;
    protected final float speedFactor;
    protected final float jumpFactor;
    protected final boolean aA;
    protected final BlockBase.Info aB;
    @Nullable
    protected MinecraftKey aC;

    public BlockBase(BlockBase.Info blockbase_info) {
        this.material = blockbase_info.a;
        this.at = blockbase_info.c;
        this.aC = blockbase_info.m;
        this.durability = blockbase_info.f;
        this.av = blockbase_info.i;
        this.stepSound = blockbase_info.d;
        this.frictionFactor = blockbase_info.j;
        this.speedFactor = blockbase_info.k;
        this.jumpFactor = blockbase_info.l;
        this.aA = blockbase_info.v;
        this.aB = blockbase_info;
    }

    @Deprecated
    public void a(IBlockData iblockdata, GeneratorAccess generatoraccess, BlockPosition blockposition, int i, int j) {}

    @Deprecated
    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        switch (pathmode) {
            case LAND:
                return !iblockdata.r(iblockaccess, blockposition);
            case WATER:
                return iblockaccess.getFluid(blockposition).a((Tag) TagsFluid.WATER);
            case AIR:
                return !iblockdata.r(iblockaccess, blockposition);
            default:
                return false;
        }
    }

    @Deprecated
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        return iblockdata;
    }

    @Deprecated
    public void doPhysics(IBlockData iblockdata, World world, BlockPosition blockposition, Block block, BlockPosition blockposition1, boolean flag) {
        PacketDebug.a(world, blockposition);
    }

    @Deprecated
    public void onPlace(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {}

    @Deprecated
    public void remove(IBlockData iblockdata, World world, BlockPosition blockposition, IBlockData iblockdata1, boolean flag) {
        if (this.isTileEntity() && !iblockdata.a(iblockdata1.getBlock())) {
            world.removeTileEntity(blockposition);
        }

    }

    @Deprecated
    public EnumInteractionResult interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        return EnumInteractionResult.PASS;
    }

    @Deprecated
    public boolean a(IBlockData iblockdata, World world, BlockPosition blockposition, int i, int j) {
        return false;
    }

    @Deprecated
    public EnumRenderType b(IBlockData iblockdata) {
        return EnumRenderType.MODEL;
    }

    @Deprecated
    public boolean c_(IBlockData iblockdata) {
        return false;
    }

    @Deprecated
    public boolean isPowerSource(IBlockData iblockdata) {
        return false;
    }

    @Deprecated
    public EnumPistonReaction getPushReaction(IBlockData iblockdata) {
        return this.material.getPushReaction();
    }

    @Deprecated
    public Fluid d(IBlockData iblockdata) {
        return FluidTypes.EMPTY.h();
    }

    @Deprecated
    public boolean isComplexRedstone(IBlockData iblockdata) {
        return false;
    }

    public BlockBase.EnumRandomOffset ah_() {
        return BlockBase.EnumRandomOffset.NONE;
    }

    @Deprecated
    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        return iblockdata;
    }

    @Deprecated
    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        return iblockdata;
    }

    @Deprecated
    public boolean a(IBlockData iblockdata, BlockActionContext blockactioncontext) {
        return this.material.isReplaceable() && (blockactioncontext.getItemStack().isEmpty() || blockactioncontext.getItemStack().getItem() != this.getItem());
    }

    @Deprecated
    public boolean a(IBlockData iblockdata, FluidType fluidtype) {
        return this.material.isReplaceable() || !this.material.isBuildable();
    }

    @Deprecated
    public List<ItemStack> a(IBlockData iblockdata, LootTableInfo.Builder loottableinfo_builder) {
        MinecraftKey minecraftkey = this.r();

        if (minecraftkey == LootTables.a) {
            return Collections.emptyList();
        } else {
            LootTableInfo loottableinfo = loottableinfo_builder.set(LootContextParameters.BLOCK_STATE, iblockdata).build(LootContextParameterSets.BLOCK);
            WorldServer worldserver = loottableinfo.getWorld();
            LootTable loottable = worldserver.getMinecraftServer().getLootTableRegistry().getLootTable(minecraftkey);

            return loottable.populateLoot(loottableinfo);
        }
    }

    @Deprecated
    public VoxelShape d(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return iblockdata.getShape(iblockaccess, blockposition);
    }

    @Deprecated
    public VoxelShape e(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return this.c(iblockdata, iblockaccess, blockposition, VoxelShapeCollision.a());
    }

    @Deprecated
    public VoxelShape a_(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return VoxelShapes.a();
    }

    @Deprecated
    public int f(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return iblockdata.i(iblockaccess, blockposition) ? iblockaccess.J() : (iblockdata.a(iblockaccess, blockposition) ? 0 : 1);
    }

    @Nullable
    @Deprecated
    public ITileInventory getInventory(IBlockData iblockdata, World world, BlockPosition blockposition) {
        return null;
    }

    @Deprecated
    public boolean canPlace(IBlockData iblockdata, IWorldReader iworldreader, BlockPosition blockposition) {
        return true;
    }

    @Deprecated
    public int a(IBlockData iblockdata, World world, BlockPosition blockposition) {
        return 0;
    }

    @Deprecated
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return VoxelShapes.b();
    }

    @Deprecated
    public VoxelShape c(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return this.at ? iblockdata.getShape(iblockaccess, blockposition) : VoxelShapes.a();
    }

    @Deprecated
    public VoxelShape a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return this.c(iblockdata, iblockaccess, blockposition, voxelshapecollision);
    }

    @Deprecated
    public void tick(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        this.tickAlways(iblockdata, worldserver, blockposition, random);
    }

    @Deprecated
    public void tickAlways(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {}

    @Deprecated
    public float getDamage(IBlockData iblockdata, EntityHuman entityhuman, IBlockAccess iblockaccess, BlockPosition blockposition) {
        float f = iblockdata.h(iblockaccess, blockposition);

        if (f == -1.0F) {
            return 0.0F;
        } else {
            int i = entityhuman.hasBlock(iblockdata) ? 30 : 100;

            return entityhuman.c(iblockdata) / f / (float) i;
        }
    }

    @Deprecated
    public void dropNaturally(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, ItemStack itemstack) {}

    @Deprecated
    public void attack(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman) {}

    @Deprecated
    public int a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return 0;
    }

    @Deprecated
    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Entity entity) {}

    @Deprecated
    public int b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return 0;
    }

    public final boolean isTileEntity() {
        return this instanceof ITileEntity;
    }

    public final MinecraftKey r() {
        if (this.aC == null) {
            MinecraftKey minecraftkey = IRegistry.BLOCK.getKey(this.p());

            this.aC = new MinecraftKey(minecraftkey.getNamespace(), "blocks/" + minecraftkey.getKey());
        }

        return this.aC;
    }

    @Deprecated
    public void a(World world, IBlockData iblockdata, MovingObjectPositionBlock movingobjectpositionblock, IProjectile iprojectile) {}

    public abstract Item getItem();

    protected abstract Block p();

    public MaterialMapColor s() {
        return (MaterialMapColor) this.aB.b.apply(this.p().getBlockData());
    }

    public interface d<A> {

        boolean test(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, A a0);
    }

    public interface e {

        boolean test(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition);
    }

    public abstract static class BlockData extends IBlockDataHolder<Block, IBlockData> {

        private final int b;
        private final boolean e;
        private final boolean f;
        private final Material g;
        private final MaterialMapColor h;
        public final float strength;
        private final boolean j;
        private final boolean k;
        private final BlockBase.e l;
        private final BlockBase.e m;
        private final BlockBase.e n;
        private final BlockBase.e o;
        private final BlockBase.e p;
        @Nullable
        protected BlockBase.BlockData.Cache a;

        protected BlockData(Block block, ImmutableMap<IBlockState<?>, Comparable<?>> immutablemap, MapCodec<IBlockData> mapcodec) {
            super(block, immutablemap, mapcodec);
            BlockBase.Info blockbase_info = block.aB;

            this.b = blockbase_info.e.applyAsInt(this.p());
            this.e = block.c_(this.p());
            this.f = blockbase_info.o;
            this.g = blockbase_info.a;
            this.h = (MaterialMapColor) blockbase_info.b.apply(this.p());
            this.strength = blockbase_info.g;
            this.j = blockbase_info.h;
            this.k = blockbase_info.n;
            this.l = blockbase_info.q;
            this.m = blockbase_info.r;
            this.n = blockbase_info.s;
            this.o = blockbase_info.t;
            this.p = blockbase_info.u;
        }

        public void a() {
            if (!this.getBlock().o()) {
                this.a = new BlockBase.BlockData.Cache(this.p());
            }

        }

        public Block getBlock() {
            return (Block) this.c;
        }

        public Material getMaterial() {
            return this.g;
        }

        public boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, EntityTypes<?> entitytypes) {
            return this.getBlock().aB.p.test(this.p(), iblockaccess, blockposition, entitytypes);
        }

        public boolean a(IBlockAccess iblockaccess, BlockPosition blockposition) {
            return this.a != null ? this.a.g : this.getBlock().b(this.p(), iblockaccess, blockposition);
        }

        public int b(IBlockAccess iblockaccess, BlockPosition blockposition) {
            return this.a != null ? this.a.h : this.getBlock().f(this.p(), iblockaccess, blockposition);
        }

        public VoxelShape a(IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
            return this.a != null && this.a.i != null ? this.a.i[enumdirection.ordinal()] : VoxelShapes.a(this.c(iblockaccess, blockposition), enumdirection);
        }

        public VoxelShape c(IBlockAccess iblockaccess, BlockPosition blockposition) {
            return this.getBlock().d(this.p(), iblockaccess, blockposition);
        }

        public boolean d() {
            return this.a == null || this.a.c;
        }

        public boolean e() {
            return this.e;
        }

        public int f() {
            return this.b;
        }

        public boolean isAir() {
            return this.f;
        }

        public MaterialMapColor d(IBlockAccess iblockaccess, BlockPosition blockposition) {
            return this.h;
        }

        public IBlockData a(EnumBlockRotation enumblockrotation) {
            return this.getBlock().a(this.p(), enumblockrotation);
        }

        public IBlockData a(EnumBlockMirror enumblockmirror) {
            return this.getBlock().a(this.p(), enumblockmirror);
        }

        public EnumRenderType h() {
            return this.getBlock().b(this.p());
        }

        public boolean isOccluding(IBlockAccess iblockaccess, BlockPosition blockposition) {
            return this.l.test(this.p(), iblockaccess, blockposition);
        }

        public boolean isPowerSource() {
            return this.getBlock().isPowerSource(this.p());
        }

        public int b(IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
            return this.getBlock().a(this.p(), iblockaccess, blockposition, enumdirection);
        }

        public boolean isComplexRedstone() {
            return this.getBlock().isComplexRedstone(this.p());
        }

        public int a(World world, BlockPosition blockposition) {
            return this.getBlock().a(this.p(), world, blockposition);
        }

        public float h(IBlockAccess iblockaccess, BlockPosition blockposition) {
            return this.strength;
        }

        public float getDamage(EntityHuman entityhuman, IBlockAccess iblockaccess, BlockPosition blockposition) {
            return this.getBlock().getDamage(this.p(), entityhuman, iblockaccess, blockposition);
        }

        public int c(IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
            return this.getBlock().b(this.p(), iblockaccess, blockposition, enumdirection);
        }

        public EnumPistonReaction getPushReaction() {
            return this.getBlock().getPushReaction(this.p());
        }

        public boolean i(IBlockAccess iblockaccess, BlockPosition blockposition) {
            if (this.a != null) {
                return this.a.a;
            } else {
                IBlockData iblockdata = this.p();

                return iblockdata.l() ? Block.a(iblockdata.c(iblockaccess, blockposition)) : false;
            }
        }

        public boolean l() {
            return this.k;
        }

        public VoxelShape getShape(IBlockAccess iblockaccess, BlockPosition blockposition) {
            return this.a(iblockaccess, blockposition, VoxelShapeCollision.a());
        }

        public VoxelShape a(IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
            return this.getBlock().b(this.p(), iblockaccess, blockposition, voxelshapecollision);
        }

        public VoxelShape getCollisionShape(IBlockAccess iblockaccess, BlockPosition blockposition) {
            return this.a != null ? this.a.b : this.b(iblockaccess, blockposition, VoxelShapeCollision.a());
        }

        public VoxelShape b(IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
            return this.getBlock().c(this.p(), iblockaccess, blockposition, voxelshapecollision);
        }

        public VoxelShape l(IBlockAccess iblockaccess, BlockPosition blockposition) {
            return this.getBlock().e(this.p(), iblockaccess, blockposition);
        }

        public VoxelShape c(IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
            return this.getBlock().a(this.p(), iblockaccess, blockposition, voxelshapecollision);
        }

        public VoxelShape m(IBlockAccess iblockaccess, BlockPosition blockposition) {
            return this.getBlock().a_(this.p(), iblockaccess, blockposition);
        }

        public final boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, Entity entity) {
            return this.a(iblockaccess, blockposition, entity, EnumDirection.UP);
        }

        public final boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, Entity entity, EnumDirection enumdirection) {
            return Block.a(this.b(iblockaccess, blockposition, VoxelShapeCollision.a(entity)), enumdirection);
        }

        public Vec3D n(IBlockAccess iblockaccess, BlockPosition blockposition) {
            BlockBase.EnumRandomOffset blockbase_enumrandomoffset = this.getBlock().ah_();

            if (blockbase_enumrandomoffset == BlockBase.EnumRandomOffset.NONE) {
                return Vec3D.ORIGIN;
            } else {
                long i = MathHelper.c(blockposition.getX(), 0, blockposition.getZ());

                return new Vec3D(((double) ((float) (i & 15L) / 15.0F) - 0.5D) * 0.5D, blockbase_enumrandomoffset == BlockBase.EnumRandomOffset.XYZ ? ((double) ((float) (i >> 4 & 15L) / 15.0F) - 1.0D) * 0.2D : 0.0D, ((double) ((float) (i >> 8 & 15L) / 15.0F) - 0.5D) * 0.5D);
            }
        }

        public boolean a(World world, BlockPosition blockposition, int i, int j) {
            return this.getBlock().a(this.p(), world, blockposition, i, j);
        }

        public void doPhysics(World world, BlockPosition blockposition, Block block, BlockPosition blockposition1, boolean flag) {
            this.getBlock().doPhysics(this.p(), world, blockposition, block, blockposition1, flag);
        }

        public final void a(GeneratorAccess generatoraccess, BlockPosition blockposition, int i) {
            this.a(generatoraccess, blockposition, i, 512);
        }

        public final void a(GeneratorAccess generatoraccess, BlockPosition blockposition, int i, int j) {
            this.getBlock();
            BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
            EnumDirection[] aenumdirection = BlockBase.ar;
            int k = aenumdirection.length;

            for (int l = 0; l < k; ++l) {
                EnumDirection enumdirection = aenumdirection[l];

                blockposition_mutableblockposition.a((BaseBlockPosition) blockposition, enumdirection);
                IBlockData iblockdata = generatoraccess.getType(blockposition_mutableblockposition);
                IBlockData iblockdata1 = iblockdata.updateState(enumdirection.opposite(), this.p(), generatoraccess, blockposition_mutableblockposition, blockposition);

                Block.a(iblockdata, iblockdata1, generatoraccess, blockposition_mutableblockposition, i, j);
            }

        }

        public final void b(GeneratorAccess generatoraccess, BlockPosition blockposition, int i) {
            this.b(generatoraccess, blockposition, i, 512);
        }

        public void b(GeneratorAccess generatoraccess, BlockPosition blockposition, int i, int j) {
            this.getBlock().a(this.p(), generatoraccess, blockposition, i, j);
        }

        public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata, boolean flag) {
            this.getBlock().onPlace(this.p(), world, blockposition, iblockdata, flag);
        }

        public void remove(World world, BlockPosition blockposition, IBlockData iblockdata, boolean flag) {
            this.getBlock().remove(this.p(), world, blockposition, iblockdata, flag);
        }

        public void a(WorldServer worldserver, BlockPosition blockposition, Random random) {
            this.getBlock().tickAlways(this.p(), worldserver, blockposition, random);
        }

        public void b(WorldServer worldserver, BlockPosition blockposition, Random random) {
            this.getBlock().tick(this.p(), worldserver, blockposition, random);
        }

        public void a(World world, BlockPosition blockposition, Entity entity) {
            this.getBlock().a(this.p(), world, blockposition, entity);
        }

        public void dropNaturally(WorldServer worldserver, BlockPosition blockposition, ItemStack itemstack) {
            this.getBlock().dropNaturally(this.p(), worldserver, blockposition, itemstack);
        }

        public List<ItemStack> a(LootTableInfo.Builder loottableinfo_builder) {
            return this.getBlock().a(this.p(), loottableinfo_builder);
        }

        public EnumInteractionResult interact(World world, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
            return this.getBlock().interact(this.p(), world, movingobjectpositionblock.getBlockPosition(), entityhuman, enumhand, movingobjectpositionblock);
        }

        public void attack(World world, BlockPosition blockposition, EntityHuman entityhuman) {
            this.getBlock().attack(this.p(), world, blockposition, entityhuman);
        }

        public boolean o(IBlockAccess iblockaccess, BlockPosition blockposition) {
            return this.m.test(this.p(), iblockaccess, blockposition);
        }

        public IBlockData updateState(EnumDirection enumdirection, IBlockData iblockdata, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
            return this.getBlock().updateState(this.p(), enumdirection, iblockdata, generatoraccess, blockposition, blockposition1);
        }

        public boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
            return this.getBlock().a(this.p(), iblockaccess, blockposition, pathmode);
        }

        public boolean a(BlockActionContext blockactioncontext) {
            return this.getBlock().a(this.p(), blockactioncontext);
        }

        public boolean a(FluidType fluidtype) {
            return this.getBlock().a(this.p(), fluidtype);
        }

        public boolean canPlace(IWorldReader iworldreader, BlockPosition blockposition) {
            return this.getBlock().canPlace(this.p(), iworldreader, blockposition);
        }

        public boolean q(IBlockAccess iblockaccess, BlockPosition blockposition) {
            return this.o.test(this.p(), iblockaccess, blockposition);
        }

        @Nullable
        public ITileInventory b(World world, BlockPosition blockposition) {
            return this.getBlock().getInventory(this.p(), world, blockposition);
        }

        public boolean a(Tag<Block> tag) {
            return this.getBlock().a(tag);
        }

        public boolean a(Tag<Block> tag, Predicate<BlockBase.BlockData> predicate) {
            return this.getBlock().a(tag) && predicate.test(this);
        }

        public boolean a(Block block) {
            return this.getBlock().a(block);
        }

        public Fluid getFluid() {
            return this.getBlock().d(this.p());
        }

        public boolean isTicking() {
            return this.getBlock().isTicking(this.p());
        }

        public SoundEffectType getStepSound() {
            return this.getBlock().getStepSound(this.p());
        }

        public void a(World world, IBlockData iblockdata, MovingObjectPositionBlock movingobjectpositionblock, IProjectile iprojectile) {
            this.getBlock().a(world, iblockdata, movingobjectpositionblock, iprojectile);
        }

        public boolean d(IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
            return this.a(iblockaccess, blockposition, enumdirection, EnumBlockSupport.FULL);
        }

        public boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection, EnumBlockSupport enumblocksupport) {
            return this.a != null ? this.a.a(enumdirection, enumblocksupport) : enumblocksupport.a(this.p(), iblockaccess, blockposition, enumdirection);
        }

        public boolean r(IBlockAccess iblockaccess, BlockPosition blockposition) {
            return this.a != null ? this.a.d : Block.a(this.getCollisionShape(iblockaccess, blockposition));
        }

        protected abstract IBlockData p();

        public boolean isRequiresSpecialTool() {
            return this.j;
        }

        static final class Cache {

            private static final EnumDirection[] e = EnumDirection.values();
            private static final int f = EnumBlockSupport.values().length;
            protected final boolean a;
            private final boolean g;
            private final int h;
            @Nullable
            private final VoxelShape[] i;
            protected final VoxelShape b;
            protected final boolean c;
            private final boolean[] j;
            protected final boolean d;

            private Cache(IBlockData iblockdata) {
                Block block = iblockdata.getBlock();

                this.a = iblockdata.i(BlockAccessAir.INSTANCE, BlockPosition.ZERO);
                this.g = block.b(iblockdata, (IBlockAccess) BlockAccessAir.INSTANCE, BlockPosition.ZERO);
                this.h = block.f(iblockdata, BlockAccessAir.INSTANCE, BlockPosition.ZERO);
                int i;

                if (!iblockdata.l()) {
                    this.i = null;
                } else {
                    this.i = new VoxelShape[BlockBase.BlockData.Cache.e.length];
                    VoxelShape voxelshape = block.d(iblockdata, BlockAccessAir.INSTANCE, BlockPosition.ZERO);
                    EnumDirection[] aenumdirection = BlockBase.BlockData.Cache.e;

                    i = aenumdirection.length;

                    for (int j = 0; j < i; ++j) {
                        EnumDirection enumdirection = aenumdirection[j];

                        this.i[enumdirection.ordinal()] = VoxelShapes.a(voxelshape, enumdirection);
                    }
                }

                this.b = block.c(iblockdata, BlockAccessAir.INSTANCE, BlockPosition.ZERO, VoxelShapeCollision.a());
                this.c = Arrays.stream(EnumDirection.EnumAxis.values()).anyMatch((enumdirection_enumaxis) -> {
                    return this.b.b(enumdirection_enumaxis) < 0.0D || this.b.c(enumdirection_enumaxis) > 1.0D;
                });
                this.j = new boolean[BlockBase.BlockData.Cache.e.length * BlockBase.BlockData.Cache.f];
                EnumDirection[] aenumdirection1 = BlockBase.BlockData.Cache.e;
                int k = aenumdirection1.length;

                for (i = 0; i < k; ++i) {
                    EnumDirection enumdirection1 = aenumdirection1[i];
                    EnumBlockSupport[] aenumblocksupport = EnumBlockSupport.values();
                    int l = aenumblocksupport.length;

                    for (int i1 = 0; i1 < l; ++i1) {
                        EnumBlockSupport enumblocksupport = aenumblocksupport[i1];

                        this.j[b(enumdirection1, enumblocksupport)] = enumblocksupport.a(iblockdata, BlockAccessAir.INSTANCE, BlockPosition.ZERO, enumdirection1);
                    }
                }

                this.d = Block.a(iblockdata.getCollisionShape(BlockAccessAir.INSTANCE, BlockPosition.ZERO));
            }

            public boolean a(EnumDirection enumdirection, EnumBlockSupport enumblocksupport) {
                return this.j[b(enumdirection, enumblocksupport)];
            }

            private static int b(EnumDirection enumdirection, EnumBlockSupport enumblocksupport) {
                return enumdirection.ordinal() * BlockBase.BlockData.Cache.f + enumblocksupport.ordinal();
            }
        }
    }

    public static class Info {

        private Material a;
        private Function<IBlockData, MaterialMapColor> b;
        private boolean c;
        private SoundEffectType d;
        private ToIntFunction<IBlockData> e;
        private float f;
        private float g;
        private boolean h;
        private boolean i;
        private float j;
        private float k;
        private float l;
        private MinecraftKey m;
        private boolean n;
        private boolean o;
        private BlockBase.d<EntityTypes<?>> p;
        private BlockBase.e q;
        private BlockBase.e r;
        private BlockBase.e s;
        private BlockBase.e t;
        private BlockBase.e u;
        private boolean v;

        private Info(Material material, MaterialMapColor materialmapcolor) {
            this(material, (iblockdata) -> {
                return materialmapcolor;
            });
        }

        private Info(Material material, Function<IBlockData, MaterialMapColor> function) {
            this.c = true;
            this.d = SoundEffectType.e;
            this.e = (iblockdata) -> {
                return 0;
            };
            this.j = 0.6F;
            this.k = 1.0F;
            this.l = 1.0F;
            this.n = true;
            this.p = (iblockdata, iblockaccess, blockposition, entitytypes) -> {
                return iblockdata.d(iblockaccess, blockposition, EnumDirection.UP) && iblockdata.f() < 14;
            };
            this.q = (iblockdata, iblockaccess, blockposition) -> {
                return iblockdata.getMaterial().f() && iblockdata.r(iblockaccess, blockposition);
            };
            this.r = (iblockdata, iblockaccess, blockposition) -> {
                return this.a.isSolid() && iblockdata.r(iblockaccess, blockposition);
            };
            this.s = this.r;
            this.t = (iblockdata, iblockaccess, blockposition) -> {
                return false;
            };
            this.u = (iblockdata, iblockaccess, blockposition) -> {
                return false;
            };
            this.a = material;
            this.b = function;
        }

        public static BlockBase.Info a(Material material) {
            return a(material, material.h());
        }

        public static BlockBase.Info a(Material material, EnumColor enumcolor) {
            return a(material, enumcolor.f());
        }

        public static BlockBase.Info a(Material material, MaterialMapColor materialmapcolor) {
            return new BlockBase.Info(material, materialmapcolor);
        }

        public static BlockBase.Info a(Material material, Function<IBlockData, MaterialMapColor> function) {
            return new BlockBase.Info(material, function);
        }

        public static BlockBase.Info a(BlockBase blockbase) {
            BlockBase.Info blockbase_info = new BlockBase.Info(blockbase.material, blockbase.aB.b);

            blockbase_info.a = blockbase.aB.a;
            blockbase_info.g = blockbase.aB.g;
            blockbase_info.f = blockbase.aB.f;
            blockbase_info.c = blockbase.aB.c;
            blockbase_info.i = blockbase.aB.i;
            blockbase_info.e = blockbase.aB.e;
            blockbase_info.b = blockbase.aB.b;
            blockbase_info.d = blockbase.aB.d;
            blockbase_info.j = blockbase.aB.j;
            blockbase_info.k = blockbase.aB.k;
            blockbase_info.v = blockbase.aB.v;
            blockbase_info.n = blockbase.aB.n;
            blockbase_info.o = blockbase.aB.o;
            blockbase_info.h = blockbase.aB.h;
            return blockbase_info;
        }

        public BlockBase.Info a() {
            this.c = false;
            this.n = false;
            return this;
        }

        public BlockBase.Info b() {
            this.n = false;
            return this;
        }

        public BlockBase.Info a(float f) {
            this.j = f;
            return this;
        }

        public BlockBase.Info b(float f) {
            this.k = f;
            return this;
        }

        public BlockBase.Info c(float f) {
            this.l = f;
            return this;
        }

        public BlockBase.Info a(SoundEffectType soundeffecttype) {
            this.d = soundeffecttype;
            return this;
        }

        public BlockBase.Info a(ToIntFunction<IBlockData> tointfunction) {
            this.e = tointfunction;
            return this;
        }

        public BlockBase.Info a(float f, float f1) {
            this.g = f;
            this.f = Math.max(0.0F, f1);
            return this;
        }

        public BlockBase.Info c() {
            return this.d(0.0F);
        }

        public BlockBase.Info d(float f) {
            this.a(f, f);
            return this;
        }

        public BlockBase.Info d() {
            this.i = true;
            return this;
        }

        public BlockBase.Info e() {
            this.v = true;
            return this;
        }

        public BlockBase.Info f() {
            this.m = LootTables.a;
            return this;
        }

        public BlockBase.Info a(Block block) {
            this.m = block.r();
            return this;
        }

        public BlockBase.Info g() {
            this.o = true;
            return this;
        }

        public BlockBase.Info a(BlockBase.d<EntityTypes<?>> blockbase_d) {
            this.p = blockbase_d;
            return this;
        }

        public BlockBase.Info a(BlockBase.e blockbase_e) {
            this.q = blockbase_e;
            return this;
        }

        public BlockBase.Info b(BlockBase.e blockbase_e) {
            this.r = blockbase_e;
            return this;
        }

        public BlockBase.Info c(BlockBase.e blockbase_e) {
            this.s = blockbase_e;
            return this;
        }

        public BlockBase.Info d(BlockBase.e blockbase_e) {
            this.t = blockbase_e;
            return this;
        }

        public BlockBase.Info e(BlockBase.e blockbase_e) {
            this.u = blockbase_e;
            return this;
        }

        public BlockBase.Info h() {
            this.h = true;
            return this;
        }
    }

    public static enum EnumRandomOffset {

        NONE, XZ, XYZ;

        private EnumRandomOffset() {}
    }
}
