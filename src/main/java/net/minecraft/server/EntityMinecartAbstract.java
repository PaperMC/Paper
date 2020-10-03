package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.datafixers.util.Pair;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public abstract class EntityMinecartAbstract extends Entity {

    private static final DataWatcherObject<Integer> b = DataWatcher.a(EntityMinecartAbstract.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Integer> c = DataWatcher.a(EntityMinecartAbstract.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Float> d = DataWatcher.a(EntityMinecartAbstract.class, DataWatcherRegistry.c);
    private static final DataWatcherObject<Integer> e = DataWatcher.a(EntityMinecartAbstract.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Integer> f = DataWatcher.a(EntityMinecartAbstract.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Boolean> g = DataWatcher.a(EntityMinecartAbstract.class, DataWatcherRegistry.i);
    private static final ImmutableMap<EntityPose, ImmutableList<Integer>> ag = ImmutableMap.of(EntityPose.STANDING, ImmutableList.of(0, 1, -1), EntityPose.CROUCHING, ImmutableList.of(0, 1, -1), EntityPose.SWIMMING, ImmutableList.of(0, 1));
    private boolean ah;
    private static final Map<BlockPropertyTrackPosition, Pair<BaseBlockPosition, BaseBlockPosition>> ai = (Map) SystemUtils.a((Object) Maps.newEnumMap(BlockPropertyTrackPosition.class), (enummap) -> {
        BaseBlockPosition baseblockposition = EnumDirection.WEST.p();
        BaseBlockPosition baseblockposition1 = EnumDirection.EAST.p();
        BaseBlockPosition baseblockposition2 = EnumDirection.NORTH.p();
        BaseBlockPosition baseblockposition3 = EnumDirection.SOUTH.p();
        BaseBlockPosition baseblockposition4 = baseblockposition.down();
        BaseBlockPosition baseblockposition5 = baseblockposition1.down();
        BaseBlockPosition baseblockposition6 = baseblockposition2.down();
        BaseBlockPosition baseblockposition7 = baseblockposition3.down();

        enummap.put(BlockPropertyTrackPosition.NORTH_SOUTH, Pair.of(baseblockposition2, baseblockposition3));
        enummap.put(BlockPropertyTrackPosition.EAST_WEST, Pair.of(baseblockposition, baseblockposition1));
        enummap.put(BlockPropertyTrackPosition.ASCENDING_EAST, Pair.of(baseblockposition4, baseblockposition1));
        enummap.put(BlockPropertyTrackPosition.ASCENDING_WEST, Pair.of(baseblockposition, baseblockposition5));
        enummap.put(BlockPropertyTrackPosition.ASCENDING_NORTH, Pair.of(baseblockposition2, baseblockposition7));
        enummap.put(BlockPropertyTrackPosition.ASCENDING_SOUTH, Pair.of(baseblockposition6, baseblockposition3));
        enummap.put(BlockPropertyTrackPosition.SOUTH_EAST, Pair.of(baseblockposition3, baseblockposition1));
        enummap.put(BlockPropertyTrackPosition.SOUTH_WEST, Pair.of(baseblockposition3, baseblockposition));
        enummap.put(BlockPropertyTrackPosition.NORTH_WEST, Pair.of(baseblockposition2, baseblockposition));
        enummap.put(BlockPropertyTrackPosition.NORTH_EAST, Pair.of(baseblockposition2, baseblockposition1));
    });
    private int aj;
    private double ak;
    private double al;
    private double am;
    private double an;
    private double ao;

    protected EntityMinecartAbstract(EntityTypes<?> entitytypes, World world) {
        super(entitytypes, world);
        this.i = true;
    }

    protected EntityMinecartAbstract(EntityTypes<?> entitytypes, World world, double d0, double d1, double d2) {
        this(entitytypes, world);
        this.setPosition(d0, d1, d2);
        this.setMot(Vec3D.ORIGIN);
        this.lastX = d0;
        this.lastY = d1;
        this.lastZ = d2;
    }

    public static EntityMinecartAbstract a(World world, double d0, double d1, double d2, EntityMinecartAbstract.EnumMinecartType entityminecartabstract_enumminecarttype) {
        return (EntityMinecartAbstract) (entityminecartabstract_enumminecarttype == EntityMinecartAbstract.EnumMinecartType.CHEST ? new EntityMinecartChest(world, d0, d1, d2) : (entityminecartabstract_enumminecarttype == EntityMinecartAbstract.EnumMinecartType.FURNACE ? new EntityMinecartFurnace(world, d0, d1, d2) : (entityminecartabstract_enumminecarttype == EntityMinecartAbstract.EnumMinecartType.TNT ? new EntityMinecartTNT(world, d0, d1, d2) : (entityminecartabstract_enumminecarttype == EntityMinecartAbstract.EnumMinecartType.SPAWNER ? new EntityMinecartMobSpawner(world, d0, d1, d2) : (entityminecartabstract_enumminecarttype == EntityMinecartAbstract.EnumMinecartType.HOPPER ? new EntityMinecartHopper(world, d0, d1, d2) : (entityminecartabstract_enumminecarttype == EntityMinecartAbstract.EnumMinecartType.COMMAND_BLOCK ? new EntityMinecartCommandBlock(world, d0, d1, d2) : new EntityMinecartRideable(world, d0, d1, d2)))))));
    }

    @Override
    protected boolean playStepSound() {
        return false;
    }

    @Override
    protected void initDatawatcher() {
        this.datawatcher.register(EntityMinecartAbstract.b, 0);
        this.datawatcher.register(EntityMinecartAbstract.c, 1);
        this.datawatcher.register(EntityMinecartAbstract.d, 0.0F);
        this.datawatcher.register(EntityMinecartAbstract.e, Block.getCombinedId(Blocks.AIR.getBlockData()));
        this.datawatcher.register(EntityMinecartAbstract.f, 6);
        this.datawatcher.register(EntityMinecartAbstract.g, false);
    }

    @Override
    public boolean j(Entity entity) {
        return EntityBoat.a((Entity) this, entity);
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    @Override
    protected Vec3D a(EnumDirection.EnumAxis enumdirection_enumaxis, BlockUtil.Rectangle blockutil_rectangle) {
        return EntityLiving.h(super.a(enumdirection_enumaxis, blockutil_rectangle));
    }

    @Override
    public double bb() {
        return 0.0D;
    }

    @Override
    public Vec3D b(EntityLiving entityliving) {
        EnumDirection enumdirection = this.getAdjustedDirection();

        if (enumdirection.n() == EnumDirection.EnumAxis.Y) {
            return super.b(entityliving);
        } else {
            int[][] aint = DismountUtil.a(enumdirection);
            BlockPosition blockposition = this.getChunkCoordinates();
            BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
            ImmutableList<EntityPose> immutablelist = entityliving.ei();
            UnmodifiableIterator unmodifiableiterator = immutablelist.iterator();

            while (unmodifiableiterator.hasNext()) {
                EntityPose entitypose = (EntityPose) unmodifiableiterator.next();
                EntitySize entitysize = entityliving.a(entitypose);
                float f = Math.min(entitysize.width, 1.0F) / 2.0F;
                UnmodifiableIterator unmodifiableiterator1 = ((ImmutableList) EntityMinecartAbstract.ag.get(entitypose)).iterator();

                while (unmodifiableiterator1.hasNext()) {
                    int i = (Integer) unmodifiableiterator1.next();
                    int[][] aint1 = aint;
                    int j = aint.length;

                    for (int k = 0; k < j; ++k) {
                        int[] aint2 = aint1[k];

                        blockposition_mutableblockposition.d(blockposition.getX() + aint2[0], blockposition.getY() + i, blockposition.getZ() + aint2[1]);
                        double d0 = this.world.a(DismountUtil.a((IBlockAccess) this.world, blockposition_mutableblockposition), () -> {
                            return DismountUtil.a((IBlockAccess) this.world, blockposition_mutableblockposition.down());
                        });

                        if (DismountUtil.a(d0)) {
                            AxisAlignedBB axisalignedbb = new AxisAlignedBB((double) (-f), 0.0D, (double) (-f), (double) f, (double) entitysize.height, (double) f);
                            Vec3D vec3d = Vec3D.a((BaseBlockPosition) blockposition_mutableblockposition, d0);

                            if (DismountUtil.a(this.world, entityliving, axisalignedbb.c(vec3d))) {
                                entityliving.setPose(entitypose);
                                return vec3d;
                            }
                        }
                    }
                }
            }

            double d1 = this.getBoundingBox().maxY;

            blockposition_mutableblockposition.c((double) blockposition.getX(), d1, (double) blockposition.getZ());
            UnmodifiableIterator unmodifiableiterator2 = immutablelist.iterator();

            while (unmodifiableiterator2.hasNext()) {
                EntityPose entitypose1 = (EntityPose) unmodifiableiterator2.next();
                double d2 = (double) entityliving.a(entitypose1).height;
                int l = MathHelper.f(d1 - (double) blockposition_mutableblockposition.getY() + d2);
                double d3 = DismountUtil.a(blockposition_mutableblockposition, l, (blockposition1) -> {
                    return this.world.getType(blockposition1).getCollisionShape(this.world, blockposition1);
                });

                if (d1 + d2 <= d3) {
                    entityliving.setPose(entitypose1);
                    break;
                }
            }

            return super.b(entityliving);
        }
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (!this.world.isClientSide && !this.dead) {
            if (this.isInvulnerable(damagesource)) {
                return false;
            } else {
                this.d(-this.n());
                this.c(10);
                this.velocityChanged();
                this.setDamage(this.getDamage() + f * 10.0F);
                boolean flag = damagesource.getEntity() instanceof EntityHuman && ((EntityHuman) damagesource.getEntity()).abilities.canInstantlyBuild;

                if (flag || this.getDamage() > 40.0F) {
                    this.ejectPassengers();
                    if (flag && !this.hasCustomName()) {
                        this.die();
                    } else {
                        this.a(damagesource);
                    }
                }

                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    protected float getBlockSpeedFactor() {
        IBlockData iblockdata = this.world.getType(this.getChunkCoordinates());

        return iblockdata.a((Tag) TagsBlock.RAILS) ? 1.0F : super.getBlockSpeedFactor();
    }

    public void a(DamageSource damagesource) {
        this.die();
        if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
            ItemStack itemstack = new ItemStack(Items.MINECART);

            if (this.hasCustomName()) {
                itemstack.a(this.getCustomName());
            }

            this.a(itemstack);
        }

    }

    @Override
    public boolean isInteractable() {
        return !this.dead;
    }

    private static Pair<BaseBlockPosition, BaseBlockPosition> a(BlockPropertyTrackPosition blockpropertytrackposition) {
        return (Pair) EntityMinecartAbstract.ai.get(blockpropertytrackposition);
    }

    @Override
    public EnumDirection getAdjustedDirection() {
        return this.ah ? this.getDirection().opposite().g() : this.getDirection().g();
    }

    @Override
    public void tick() {
        if (this.getType() > 0) {
            this.c(this.getType() - 1);
        }

        if (this.getDamage() > 0.0F) {
            this.setDamage(this.getDamage() - 1.0F);
        }

        if (this.locY() < -64.0D) {
            this.am();
        }

        this.doPortalTick();
        if (this.world.isClientSide) {
            if (this.aj > 0) {
                double d0 = this.locX() + (this.ak - this.locX()) / (double) this.aj;
                double d1 = this.locY() + (this.al - this.locY()) / (double) this.aj;
                double d2 = this.locZ() + (this.am - this.locZ()) / (double) this.aj;
                double d3 = MathHelper.g(this.an - (double) this.yaw);

                this.yaw = (float) ((double) this.yaw + d3 / (double) this.aj);
                this.pitch = (float) ((double) this.pitch + (this.ao - (double) this.pitch) / (double) this.aj);
                --this.aj;
                this.setPosition(d0, d1, d2);
                this.setYawPitch(this.yaw, this.pitch);
            } else {
                this.ae();
                this.setYawPitch(this.yaw, this.pitch);
            }

        } else {
            if (!this.isNoGravity()) {
                this.setMot(this.getMot().add(0.0D, -0.04D, 0.0D));
            }

            int i = MathHelper.floor(this.locX());
            int j = MathHelper.floor(this.locY());
            int k = MathHelper.floor(this.locZ());

            if (this.world.getType(new BlockPosition(i, j - 1, k)).a((Tag) TagsBlock.RAILS)) {
                --j;
            }

            BlockPosition blockposition = new BlockPosition(i, j, k);
            IBlockData iblockdata = this.world.getType(blockposition);

            if (BlockMinecartTrackAbstract.g(iblockdata)) {
                this.c(blockposition, iblockdata);
                if (iblockdata.a(Blocks.ACTIVATOR_RAIL)) {
                    this.a(i, j, k, (Boolean) iblockdata.get(BlockPoweredRail.POWERED));
                }
            } else {
                this.h();
            }

            this.checkBlockCollisions();
            this.pitch = 0.0F;
            double d4 = this.lastX - this.locX();
            double d5 = this.lastZ - this.locZ();

            if (d4 * d4 + d5 * d5 > 0.001D) {
                this.yaw = (float) (MathHelper.d(d5, d4) * 180.0D / 3.141592653589793D);
                if (this.ah) {
                    this.yaw += 180.0F;
                }
            }

            double d6 = (double) MathHelper.g(this.yaw - this.lastYaw);

            if (d6 < -170.0D || d6 >= 170.0D) {
                this.yaw += 180.0F;
                this.ah = !this.ah;
            }

            this.setYawPitch(this.yaw, this.pitch);
            if (this.getMinecartType() == EntityMinecartAbstract.EnumMinecartType.RIDEABLE && c(this.getMot()) > 0.01D) {
                List<Entity> list = this.world.getEntities(this, this.getBoundingBox().grow(0.20000000298023224D, 0.0D, 0.20000000298023224D), IEntitySelector.a(this));

                if (!list.isEmpty()) {
                    for (int l = 0; l < list.size(); ++l) {
                        Entity entity = (Entity) list.get(l);

                        if (!(entity instanceof EntityHuman) && !(entity instanceof EntityIronGolem) && !(entity instanceof EntityMinecartAbstract) && !this.isVehicle() && !entity.isPassenger()) {
                            entity.startRiding(this);
                        } else {
                            entity.collide(this);
                        }
                    }
                }
            } else {
                Iterator iterator = this.world.getEntities(this, this.getBoundingBox().grow(0.20000000298023224D, 0.0D, 0.20000000298023224D)).iterator();

                while (iterator.hasNext()) {
                    Entity entity1 = (Entity) iterator.next();

                    if (!this.w(entity1) && entity1.isCollidable() && entity1 instanceof EntityMinecartAbstract) {
                        entity1.collide(this);
                    }
                }
            }

            this.aJ();
            if (this.aP()) {
                this.burnFromLava();
                this.fallDistance *= 0.5F;
            }

            this.justCreated = false;
        }
    }

    protected double getMaxSpeed() {
        return 0.4D;
    }

    public void a(int i, int j, int k, boolean flag) {}

    protected void h() {
        double d0 = this.getMaxSpeed();
        Vec3D vec3d = this.getMot();

        this.setMot(MathHelper.a(vec3d.x, -d0, d0), vec3d.y, MathHelper.a(vec3d.z, -d0, d0));
        if (this.onGround) {
            this.setMot(this.getMot().a(0.5D));
        }

        this.move(EnumMoveType.SELF, this.getMot());
        if (!this.onGround) {
            this.setMot(this.getMot().a(0.95D));
        }

    }

    protected void c(BlockPosition blockposition, IBlockData iblockdata) {
        this.fallDistance = 0.0F;
        double d0 = this.locX();
        double d1 = this.locY();
        double d2 = this.locZ();
        Vec3D vec3d = this.p(d0, d1, d2);

        d1 = (double) blockposition.getY();
        boolean flag = false;
        boolean flag1 = false;
        BlockMinecartTrackAbstract blockminecarttrackabstract = (BlockMinecartTrackAbstract) iblockdata.getBlock();

        if (blockminecarttrackabstract == Blocks.POWERED_RAIL) {
            flag = (Boolean) iblockdata.get(BlockPoweredRail.POWERED);
            flag1 = !flag;
        }

        double d3 = 0.0078125D;
        Vec3D vec3d1 = this.getMot();
        BlockPropertyTrackPosition blockpropertytrackposition = (BlockPropertyTrackPosition) iblockdata.get(blockminecarttrackabstract.d());

        switch (blockpropertytrackposition) {
            case ASCENDING_EAST:
                this.setMot(vec3d1.add(-0.0078125D, 0.0D, 0.0D));
                ++d1;
                break;
            case ASCENDING_WEST:
                this.setMot(vec3d1.add(0.0078125D, 0.0D, 0.0D));
                ++d1;
                break;
            case ASCENDING_NORTH:
                this.setMot(vec3d1.add(0.0D, 0.0D, 0.0078125D));
                ++d1;
                break;
            case ASCENDING_SOUTH:
                this.setMot(vec3d1.add(0.0D, 0.0D, -0.0078125D));
                ++d1;
        }

        vec3d1 = this.getMot();
        Pair<BaseBlockPosition, BaseBlockPosition> pair = a(blockpropertytrackposition);
        BaseBlockPosition baseblockposition = (BaseBlockPosition) pair.getFirst();
        BaseBlockPosition baseblockposition1 = (BaseBlockPosition) pair.getSecond();
        double d4 = (double) (baseblockposition1.getX() - baseblockposition.getX());
        double d5 = (double) (baseblockposition1.getZ() - baseblockposition.getZ());
        double d6 = Math.sqrt(d4 * d4 + d5 * d5);
        double d7 = vec3d1.x * d4 + vec3d1.z * d5;

        if (d7 < 0.0D) {
            d4 = -d4;
            d5 = -d5;
        }

        double d8 = Math.min(2.0D, Math.sqrt(c(vec3d1)));

        vec3d1 = new Vec3D(d8 * d4 / d6, vec3d1.y, d8 * d5 / d6);
        this.setMot(vec3d1);
        Entity entity = this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);

        if (entity instanceof EntityHuman) {
            Vec3D vec3d2 = entity.getMot();
            double d9 = c(vec3d2);
            double d10 = c(this.getMot());

            if (d9 > 1.0E-4D && d10 < 0.01D) {
                this.setMot(this.getMot().add(vec3d2.x * 0.1D, 0.0D, vec3d2.z * 0.1D));
                flag1 = false;
            }
        }

        double d11;

        if (flag1) {
            d11 = Math.sqrt(c(this.getMot()));
            if (d11 < 0.03D) {
                this.setMot(Vec3D.ORIGIN);
            } else {
                this.setMot(this.getMot().d(0.5D, 0.0D, 0.5D));
            }
        }

        d11 = (double) blockposition.getX() + 0.5D + (double) baseblockposition.getX() * 0.5D;
        double d12 = (double) blockposition.getZ() + 0.5D + (double) baseblockposition.getZ() * 0.5D;
        double d13 = (double) blockposition.getX() + 0.5D + (double) baseblockposition1.getX() * 0.5D;
        double d14 = (double) blockposition.getZ() + 0.5D + (double) baseblockposition1.getZ() * 0.5D;

        d4 = d13 - d11;
        d5 = d14 - d12;
        double d15;
        double d16;
        double d17;

        if (d4 == 0.0D) {
            d15 = d2 - (double) blockposition.getZ();
        } else if (d5 == 0.0D) {
            d15 = d0 - (double) blockposition.getX();
        } else {
            d16 = d0 - d11;
            d17 = d2 - d12;
            d15 = (d16 * d4 + d17 * d5) * 2.0D;
        }

        d0 = d11 + d4 * d15;
        d2 = d12 + d5 * d15;
        this.setPosition(d0, d1, d2);
        d16 = this.isVehicle() ? 0.75D : 1.0D;
        d17 = this.getMaxSpeed();
        vec3d1 = this.getMot();
        this.move(EnumMoveType.SELF, new Vec3D(MathHelper.a(d16 * vec3d1.x, -d17, d17), 0.0D, MathHelper.a(d16 * vec3d1.z, -d17, d17)));
        if (baseblockposition.getY() != 0 && MathHelper.floor(this.locX()) - blockposition.getX() == baseblockposition.getX() && MathHelper.floor(this.locZ()) - blockposition.getZ() == baseblockposition.getZ()) {
            this.setPosition(this.locX(), this.locY() + (double) baseblockposition.getY(), this.locZ());
        } else if (baseblockposition1.getY() != 0 && MathHelper.floor(this.locX()) - blockposition.getX() == baseblockposition1.getX() && MathHelper.floor(this.locZ()) - blockposition.getZ() == baseblockposition1.getZ()) {
            this.setPosition(this.locX(), this.locY() + (double) baseblockposition1.getY(), this.locZ());
        }

        this.decelerate();
        Vec3D vec3d3 = this.p(this.locX(), this.locY(), this.locZ());
        Vec3D vec3d4;
        double d18;

        if (vec3d3 != null && vec3d != null) {
            double d19 = (vec3d.y - vec3d3.y) * 0.05D;

            vec3d4 = this.getMot();
            d18 = Math.sqrt(c(vec3d4));
            if (d18 > 0.0D) {
                this.setMot(vec3d4.d((d18 + d19) / d18, 1.0D, (d18 + d19) / d18));
            }

            this.setPosition(this.locX(), vec3d3.y, this.locZ());
        }

        int i = MathHelper.floor(this.locX());
        int j = MathHelper.floor(this.locZ());

        if (i != blockposition.getX() || j != blockposition.getZ()) {
            vec3d4 = this.getMot();
            d18 = Math.sqrt(c(vec3d4));
            this.setMot(d18 * (double) (i - blockposition.getX()), vec3d4.y, d18 * (double) (j - blockposition.getZ()));
        }

        if (flag) {
            vec3d4 = this.getMot();
            d18 = Math.sqrt(c(vec3d4));
            if (d18 > 0.01D) {
                double d20 = 0.06D;

                this.setMot(vec3d4.add(vec3d4.x / d18 * 0.06D, 0.0D, vec3d4.z / d18 * 0.06D));
            } else {
                Vec3D vec3d5 = this.getMot();
                double d21 = vec3d5.x;
                double d22 = vec3d5.z;

                if (blockpropertytrackposition == BlockPropertyTrackPosition.EAST_WEST) {
                    if (this.a(blockposition.west())) {
                        d21 = 0.02D;
                    } else if (this.a(blockposition.east())) {
                        d21 = -0.02D;
                    }
                } else {
                    if (blockpropertytrackposition != BlockPropertyTrackPosition.NORTH_SOUTH) {
                        return;
                    }

                    if (this.a(blockposition.north())) {
                        d22 = 0.02D;
                    } else if (this.a(blockposition.south())) {
                        d22 = -0.02D;
                    }
                }

                this.setMot(d21, vec3d5.y, d22);
            }
        }

    }

    private boolean a(BlockPosition blockposition) {
        return this.world.getType(blockposition).isOccluding(this.world, blockposition);
    }

    protected void decelerate() {
        double d0 = this.isVehicle() ? 0.997D : 0.96D;

        this.setMot(this.getMot().d(d0, 0.0D, d0));
    }

    @Nullable
    public Vec3D p(double d0, double d1, double d2) {
        int i = MathHelper.floor(d0);
        int j = MathHelper.floor(d1);
        int k = MathHelper.floor(d2);

        if (this.world.getType(new BlockPosition(i, j - 1, k)).a((Tag) TagsBlock.RAILS)) {
            --j;
        }

        IBlockData iblockdata = this.world.getType(new BlockPosition(i, j, k));

        if (BlockMinecartTrackAbstract.g(iblockdata)) {
            BlockPropertyTrackPosition blockpropertytrackposition = (BlockPropertyTrackPosition) iblockdata.get(((BlockMinecartTrackAbstract) iblockdata.getBlock()).d());
            Pair<BaseBlockPosition, BaseBlockPosition> pair = a(blockpropertytrackposition);
            BaseBlockPosition baseblockposition = (BaseBlockPosition) pair.getFirst();
            BaseBlockPosition baseblockposition1 = (BaseBlockPosition) pair.getSecond();
            double d3 = (double) i + 0.5D + (double) baseblockposition.getX() * 0.5D;
            double d4 = (double) j + 0.0625D + (double) baseblockposition.getY() * 0.5D;
            double d5 = (double) k + 0.5D + (double) baseblockposition.getZ() * 0.5D;
            double d6 = (double) i + 0.5D + (double) baseblockposition1.getX() * 0.5D;
            double d7 = (double) j + 0.0625D + (double) baseblockposition1.getY() * 0.5D;
            double d8 = (double) k + 0.5D + (double) baseblockposition1.getZ() * 0.5D;
            double d9 = d6 - d3;
            double d10 = (d7 - d4) * 2.0D;
            double d11 = d8 - d5;
            double d12;

            if (d9 == 0.0D) {
                d12 = d2 - (double) k;
            } else if (d11 == 0.0D) {
                d12 = d0 - (double) i;
            } else {
                double d13 = d0 - d3;
                double d14 = d2 - d5;

                d12 = (d13 * d9 + d14 * d11) * 2.0D;
            }

            d0 = d3 + d9 * d12;
            d1 = d4 + d10 * d12;
            d2 = d5 + d11 * d12;
            if (d10 < 0.0D) {
                ++d1;
            } else if (d10 > 0.0D) {
                d1 += 0.5D;
            }

            return new Vec3D(d0, d1, d2);
        } else {
            return null;
        }
    }

    @Override
    protected void loadData(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.getBoolean("CustomDisplayTile")) {
            this.setDisplayBlock(GameProfileSerializer.c(nbttagcompound.getCompound("DisplayState")));
            this.setDisplayBlockOffset(nbttagcompound.getInt("DisplayOffset"));
        }

    }

    @Override
    protected void saveData(NBTTagCompound nbttagcompound) {
        if (this.t()) {
            nbttagcompound.setBoolean("CustomDisplayTile", true);
            nbttagcompound.set("DisplayState", GameProfileSerializer.a(this.getDisplayBlock()));
            nbttagcompound.setInt("DisplayOffset", this.getDisplayBlockOffset());
        }

    }

    @Override
    public void collide(Entity entity) {
        if (!this.world.isClientSide) {
            if (!entity.noclip && !this.noclip) {
                if (!this.w(entity)) {
                    double d0 = entity.locX() - this.locX();
                    double d1 = entity.locZ() - this.locZ();
                    double d2 = d0 * d0 + d1 * d1;

                    if (d2 >= 9.999999747378752E-5D) {
                        d2 = (double) MathHelper.sqrt(d2);
                        d0 /= d2;
                        d1 /= d2;
                        double d3 = 1.0D / d2;

                        if (d3 > 1.0D) {
                            d3 = 1.0D;
                        }

                        d0 *= d3;
                        d1 *= d3;
                        d0 *= 0.10000000149011612D;
                        d1 *= 0.10000000149011612D;
                        d0 *= (double) (1.0F - this.I);
                        d1 *= (double) (1.0F - this.I);
                        d0 *= 0.5D;
                        d1 *= 0.5D;
                        if (entity instanceof EntityMinecartAbstract) {
                            double d4 = entity.locX() - this.locX();
                            double d5 = entity.locZ() - this.locZ();
                            Vec3D vec3d = (new Vec3D(d4, 0.0D, d5)).d();
                            Vec3D vec3d1 = (new Vec3D((double) MathHelper.cos(this.yaw * 0.017453292F), 0.0D, (double) MathHelper.sin(this.yaw * 0.017453292F))).d();
                            double d6 = Math.abs(vec3d.b(vec3d1));

                            if (d6 < 0.800000011920929D) {
                                return;
                            }

                            Vec3D vec3d2 = this.getMot();
                            Vec3D vec3d3 = entity.getMot();

                            if (((EntityMinecartAbstract) entity).getMinecartType() == EntityMinecartAbstract.EnumMinecartType.FURNACE && this.getMinecartType() != EntityMinecartAbstract.EnumMinecartType.FURNACE) {
                                this.setMot(vec3d2.d(0.2D, 1.0D, 0.2D));
                                this.i(vec3d3.x - d0, 0.0D, vec3d3.z - d1);
                                entity.setMot(vec3d3.d(0.95D, 1.0D, 0.95D));
                            } else if (((EntityMinecartAbstract) entity).getMinecartType() != EntityMinecartAbstract.EnumMinecartType.FURNACE && this.getMinecartType() == EntityMinecartAbstract.EnumMinecartType.FURNACE) {
                                entity.setMot(vec3d3.d(0.2D, 1.0D, 0.2D));
                                entity.i(vec3d2.x + d0, 0.0D, vec3d2.z + d1);
                                this.setMot(vec3d2.d(0.95D, 1.0D, 0.95D));
                            } else {
                                double d7 = (vec3d3.x + vec3d2.x) / 2.0D;
                                double d8 = (vec3d3.z + vec3d2.z) / 2.0D;

                                this.setMot(vec3d2.d(0.2D, 1.0D, 0.2D));
                                this.i(d7 - d0, 0.0D, d8 - d1);
                                entity.setMot(vec3d3.d(0.2D, 1.0D, 0.2D));
                                entity.i(d7 + d0, 0.0D, d8 + d1);
                            }
                        } else {
                            this.i(-d0, 0.0D, -d1);
                            entity.i(d0 / 4.0D, 0.0D, d1 / 4.0D);
                        }
                    }

                }
            }
        }
    }

    public void setDamage(float f) {
        this.datawatcher.set(EntityMinecartAbstract.d, f);
    }

    public float getDamage() {
        return (Float) this.datawatcher.get(EntityMinecartAbstract.d);
    }

    public void c(int i) {
        this.datawatcher.set(EntityMinecartAbstract.b, i);
    }

    public int getType() {
        return (Integer) this.datawatcher.get(EntityMinecartAbstract.b);
    }

    public void d(int i) {
        this.datawatcher.set(EntityMinecartAbstract.c, i);
    }

    public int n() {
        return (Integer) this.datawatcher.get(EntityMinecartAbstract.c);
    }

    public abstract EntityMinecartAbstract.EnumMinecartType getMinecartType();

    public IBlockData getDisplayBlock() {
        return !this.t() ? this.q() : Block.getByCombinedId((Integer) this.getDataWatcher().get(EntityMinecartAbstract.e));
    }

    public IBlockData q() {
        return Blocks.AIR.getBlockData();
    }

    public int getDisplayBlockOffset() {
        return !this.t() ? this.s() : (Integer) this.getDataWatcher().get(EntityMinecartAbstract.f);
    }

    public int s() {
        return 6;
    }

    public void setDisplayBlock(IBlockData iblockdata) {
        this.getDataWatcher().set(EntityMinecartAbstract.e, Block.getCombinedId(iblockdata));
        this.a(true);
    }

    public void setDisplayBlockOffset(int i) {
        this.getDataWatcher().set(EntityMinecartAbstract.f, i);
        this.a(true);
    }

    public boolean t() {
        return (Boolean) this.getDataWatcher().get(EntityMinecartAbstract.g);
    }

    public void a(boolean flag) {
        this.getDataWatcher().set(EntityMinecartAbstract.g, flag);
    }

    @Override
    public Packet<?> P() {
        return new PacketPlayOutSpawnEntity(this);
    }

    public static enum EnumMinecartType {

        RIDEABLE, CHEST, FURNACE, TNT, SPAWNER, HOPPER, COMMAND_BLOCK;

        private EnumMinecartType() {}
    }
}
