package net.minecraft.server;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Entity implements INamableTileEntity, ICommandListener {

    protected static final Logger LOGGER = LogManager.getLogger();
    private static final AtomicInteger entityCount = new AtomicInteger();
    private static final List<ItemStack> c = Collections.emptyList();
    private static final AxisAlignedBB d = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    private static double e = 1.0D;
    private final EntityTypes<?> f;
    private int id;
    public boolean i;
    public final List<Entity> passengers;
    protected int j;
    @Nullable
    private Entity vehicle;
    public boolean attachedToPlayer;
    public World world;
    public double lastX;
    public double lastY;
    public double lastZ;
    private Vec3D loc;
    private BlockPosition locBlock;
    private Vec3D mot;
    public float yaw;
    public float pitch;
    public float lastYaw;
    public float lastPitch;
    private AxisAlignedBB boundingBox;
    protected boolean onGround;
    public boolean positionChanged;
    public boolean v;
    public boolean velocityChanged;
    protected Vec3D x;
    public boolean dead;
    public float z;
    public float A;
    public float B;
    public float fallDistance;
    private float am;
    private float an;
    public double D;
    public double E;
    public double F;
    public float G;
    public boolean noclip;
    public float I;
    protected final Random random;
    public int ticksLived;
    public int fireTicks;
    public boolean inWater;
    protected Object2DoubleMap<Tag<FluidType>> M;
    protected boolean N;
    @Nullable
    protected Tag<FluidType> O;
    public int noDamageTicks;
    protected boolean justCreated;
    protected final DataWatcher datawatcher;
    protected static final DataWatcherObject<Byte> S = DataWatcher.a(Entity.class, DataWatcherRegistry.a);
    private static final DataWatcherObject<Integer> AIR_TICKS = DataWatcher.a(Entity.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Optional<IChatBaseComponent>> aq = DataWatcher.a(Entity.class, DataWatcherRegistry.f);
    private static final DataWatcherObject<Boolean> ar = DataWatcher.a(Entity.class, DataWatcherRegistry.i);
    private static final DataWatcherObject<Boolean> as = DataWatcher.a(Entity.class, DataWatcherRegistry.i);
    private static final DataWatcherObject<Boolean> at = DataWatcher.a(Entity.class, DataWatcherRegistry.i);
    protected static final DataWatcherObject<EntityPose> POSE = DataWatcher.a(Entity.class, DataWatcherRegistry.s);
    public boolean inChunk;
    public int chunkX;
    public int chunkY;
    public int chunkZ;
    private boolean au;
    private Vec3D av;
    public boolean Y;
    public boolean impulse;
    public int portalCooldown;
    protected boolean inPortal;
    protected int portalTicks;
    protected BlockPosition ac;
    private boolean invulnerable;
    protected UUID uniqueID;
    protected String ae;
    public boolean glowing;
    private final Set<String> ay;
    private boolean az;
    private final double[] aA;
    private long aB;
    private EntitySize size;
    private float headHeight;

    public Entity(EntityTypes<?> entitytypes, World world) {
        this.id = Entity.entityCount.incrementAndGet();
        this.passengers = Lists.newArrayList();
        this.mot = Vec3D.ORIGIN;
        this.boundingBox = Entity.d;
        this.x = Vec3D.ORIGIN;
        this.am = 1.0F;
        this.an = 1.0F;
        this.random = new Random();
        this.fireTicks = -this.getMaxFireTicks();
        this.M = new Object2DoubleArrayMap(2);
        this.justCreated = true;
        this.uniqueID = MathHelper.a(this.random);
        this.ae = this.uniqueID.toString();
        this.ay = Sets.newHashSet();
        this.aA = new double[]{0.0D, 0.0D, 0.0D};
        this.f = entitytypes;
        this.world = world;
        this.size = entitytypes.l();
        this.loc = Vec3D.ORIGIN;
        this.locBlock = BlockPosition.ZERO;
        this.av = Vec3D.ORIGIN;
        this.setPosition(0.0D, 0.0D, 0.0D);
        this.datawatcher = new DataWatcher(this);
        this.datawatcher.register(Entity.S, (byte) 0);
        this.datawatcher.register(Entity.AIR_TICKS, this.bG());
        this.datawatcher.register(Entity.ar, false);
        this.datawatcher.register(Entity.aq, Optional.empty());
        this.datawatcher.register(Entity.as, false);
        this.datawatcher.register(Entity.at, false);
        this.datawatcher.register(Entity.POSE, EntityPose.STANDING);
        this.initDatawatcher();
        this.headHeight = this.getHeadHeight(EntityPose.STANDING, this.size);
    }

    public boolean isSpectator() {
        return false;
    }

    public final void decouple() {
        if (this.isVehicle()) {
            this.ejectPassengers();
        }

        if (this.isPassenger()) {
            this.stopRiding();
        }

    }

    public void c(double d0, double d1, double d2) {
        this.a(new Vec3D(d0, d1, d2));
    }

    public void a(Vec3D vec3d) {
        this.av = vec3d;
    }

    public EntityTypes<?> getEntityType() {
        return this.f;
    }

    public int getId() {
        return this.id;
    }

    public void e(int i) {
        this.id = i;
    }

    public Set<String> getScoreboardTags() {
        return this.ay;
    }

    public boolean addScoreboardTag(String s) {
        return this.ay.size() >= 1024 ? false : this.ay.add(s);
    }

    public boolean removeScoreboardTag(String s) {
        return this.ay.remove(s);
    }

    public void killEntity() {
        this.die();
    }

    protected abstract void initDatawatcher();

    public DataWatcher getDataWatcher() {
        return this.datawatcher;
    }

    public boolean equals(Object object) {
        return object instanceof Entity ? ((Entity) object).id == this.id : false;
    }

    public int hashCode() {
        return this.id;
    }

    public void die() {
        this.dead = true;
    }

    public void setPose(EntityPose entitypose) {
        this.datawatcher.set(Entity.POSE, entitypose);
    }

    public EntityPose getPose() {
        return (EntityPose) this.datawatcher.get(Entity.POSE);
    }

    public boolean a(Entity entity, double d0) {
        double d1 = entity.loc.x - this.loc.x;
        double d2 = entity.loc.y - this.loc.y;
        double d3 = entity.loc.z - this.loc.z;

        return d1 * d1 + d2 * d2 + d3 * d3 < d0 * d0;
    }

    protected void setYawPitch(float f, float f1) {
        this.yaw = f % 360.0F;
        this.pitch = f1 % 360.0F;
    }

    public void setPosition(double d0, double d1, double d2) {
        this.setPositionRaw(d0, d1, d2);
        this.a(this.size.a(d0, d1, d2));
    }

    protected void ae() {
        this.setPosition(this.loc.x, this.loc.y, this.loc.z);
    }

    public void tick() {
        if (!this.world.isClientSide) {
            this.setFlag(6, this.bD());
        }

        this.entityBaseTick();
    }

    public void entityBaseTick() {
        this.world.getMethodProfiler().enter("entityBaseTick");
        if (this.isPassenger() && this.getVehicle().dead) {
            this.stopRiding();
        }

        if (this.j > 0) {
            --this.j;
        }

        this.z = this.A;
        this.lastPitch = this.pitch;
        this.lastYaw = this.yaw;
        this.doPortalTick();
        if (this.aN()) {
            this.aO();
        }

        this.aJ();
        this.m();
        this.aI();
        if (this.world.isClientSide) {
            this.extinguish();
        } else if (this.fireTicks > 0) {
            if (this.isFireProof()) {
                this.setFireTicks(this.fireTicks - 4);
                if (this.fireTicks < 0) {
                    this.extinguish();
                }
            } else {
                if (this.fireTicks % 20 == 0 && !this.aP()) {
                    this.damageEntity(DamageSource.BURN, 1.0F);
                }

                this.setFireTicks(this.fireTicks - 1);
            }
        }

        if (this.aP()) {
            this.burnFromLava();
            this.fallDistance *= 0.5F;
        }

        if (this.locY() < -64.0D) {
            this.am();
        }

        if (!this.world.isClientSide) {
            this.setFlag(0, this.fireTicks > 0);
        }

        this.justCreated = false;
        this.world.getMethodProfiler().exit();
    }

    public void resetPortalCooldown() {
        this.portalCooldown = this.getDefaultPortalCooldown();
    }

    public boolean ah() {
        return this.portalCooldown > 0;
    }

    protected void E() {
        if (this.ah()) {
            --this.portalCooldown;
        }

    }

    public int ai() {
        return 0;
    }

    protected void burnFromLava() {
        if (!this.isFireProof()) {
            this.setOnFire(15);
            this.damageEntity(DamageSource.LAVA, 4.0F);
        }
    }

    public void setOnFire(int i) {
        int j = i * 20;

        if (this instanceof EntityLiving) {
            j = EnchantmentProtection.a((EntityLiving) this, j);
        }

        if (this.fireTicks < j) {
            this.setFireTicks(j);
        }

    }

    public void setFireTicks(int i) {
        this.fireTicks = i;
    }

    public int getFireTicks() {
        return this.fireTicks;
    }

    public void extinguish() {
        this.setFireTicks(0);
    }

    protected void am() {
        this.die();
    }

    public boolean e(double d0, double d1, double d2) {
        return this.b(this.getBoundingBox().d(d0, d1, d2));
    }

    private boolean b(AxisAlignedBB axisalignedbb) {
        return this.world.getCubes(this, axisalignedbb) && !this.world.containsLiquid(axisalignedbb);
    }

    public void setOnGround(boolean flag) {
        this.onGround = flag;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void move(EnumMoveType enummovetype, Vec3D vec3d) {
        if (this.noclip) {
            this.a(this.getBoundingBox().c(vec3d));
            this.recalcPosition();
        } else {
            if (enummovetype == EnumMoveType.PISTON) {
                vec3d = this.b(vec3d);
                if (vec3d.equals(Vec3D.ORIGIN)) {
                    return;
                }
            }

            this.world.getMethodProfiler().enter("move");
            if (this.x.g() > 1.0E-7D) {
                vec3d = vec3d.h(this.x);
                this.x = Vec3D.ORIGIN;
                this.setMot(Vec3D.ORIGIN);
            }

            vec3d = this.a(vec3d, enummovetype);
            Vec3D vec3d1 = this.g(vec3d);

            if (vec3d1.g() > 1.0E-7D) {
                this.a(this.getBoundingBox().c(vec3d1));
                this.recalcPosition();
            }

            this.world.getMethodProfiler().exit();
            this.world.getMethodProfiler().enter("rest");
            this.positionChanged = !MathHelper.b(vec3d.x, vec3d1.x) || !MathHelper.b(vec3d.z, vec3d1.z);
            this.v = vec3d.y != vec3d1.y;
            this.onGround = this.v && vec3d.y < 0.0D;
            BlockPosition blockposition = this.ao();
            IBlockData iblockdata = this.world.getType(blockposition);

            this.a(vec3d1.y, this.onGround, iblockdata, blockposition);
            Vec3D vec3d2 = this.getMot();

            if (vec3d.x != vec3d1.x) {
                this.setMot(0.0D, vec3d2.y, vec3d2.z);
            }

            if (vec3d.z != vec3d1.z) {
                this.setMot(vec3d2.x, vec3d2.y, 0.0D);
            }

            Block block = iblockdata.getBlock();

            if (vec3d.y != vec3d1.y) {
                block.a((IBlockAccess) this.world, this);
            }

            if (this.onGround && !this.bu()) {
                block.stepOn(this.world, blockposition, this);
            }

            if (this.playStepSound() && !this.isPassenger()) {
                double d0 = vec3d1.x;
                double d1 = vec3d1.y;
                double d2 = vec3d1.z;

                if (!block.a((Tag) TagsBlock.CLIMBABLE)) {
                    d1 = 0.0D;
                }

                this.A = (float) ((double) this.A + (double) MathHelper.sqrt(c(vec3d1)) * 0.6D);
                this.B = (float) ((double) this.B + (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 0.6D);
                if (this.B > this.am && !iblockdata.isAir()) {
                    this.am = this.as();
                    if (this.isInWater()) {
                        Entity entity = this.isVehicle() && this.getRidingPassenger() != null ? this.getRidingPassenger() : this;
                        float f = entity == this ? 0.35F : 0.4F;
                        Vec3D vec3d3 = entity.getMot();
                        float f1 = MathHelper.sqrt(vec3d3.x * vec3d3.x * 0.20000000298023224D + vec3d3.y * vec3d3.y + vec3d3.z * vec3d3.z * 0.20000000298023224D) * f;

                        if (f1 > 1.0F) {
                            f1 = 1.0F;
                        }

                        this.d(f1);
                    } else {
                        this.b(blockposition, iblockdata);
                    }
                } else if (this.B > this.an && this.ay() && iblockdata.isAir()) {
                    this.an = this.e(this.B);
                }
            }

            try {
                this.checkBlockCollisions();
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.a(throwable, "Checking entity block collision");
                CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Entity being checked for collision");

                this.appendEntityCrashDetails(crashreportsystemdetails);
                throw new ReportedException(crashreport);
            }

            float f2 = this.getBlockSpeedFactor();

            this.setMot(this.getMot().d((double) f2, 1.0D, (double) f2));
            if (this.world.c(this.getBoundingBox().shrink(0.001D)).noneMatch((iblockdata1) -> {
                return iblockdata1.a((Tag) TagsBlock.FIRE) || iblockdata1.a(Blocks.LAVA);
            }) && this.fireTicks <= 0) {
                this.setFireTicks(-this.getMaxFireTicks());
            }

            if (this.aF() && this.isBurning()) {
                this.playSound(SoundEffects.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.7F, 1.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                this.setFireTicks(-this.getMaxFireTicks());
            }

            this.world.getMethodProfiler().exit();
        }
    }

    protected BlockPosition ao() {
        int i = MathHelper.floor(this.loc.x);
        int j = MathHelper.floor(this.loc.y - 0.20000000298023224D);
        int k = MathHelper.floor(this.loc.z);
        BlockPosition blockposition = new BlockPosition(i, j, k);

        if (this.world.getType(blockposition).isAir()) {
            BlockPosition blockposition1 = blockposition.down();
            IBlockData iblockdata = this.world.getType(blockposition1);
            Block block = iblockdata.getBlock();

            if (block.a((Tag) TagsBlock.FENCES) || block.a((Tag) TagsBlock.WALLS) || block instanceof BlockFenceGate) {
                return blockposition1;
            }
        }

        return blockposition;
    }

    protected float getBlockJumpFactor() {
        float f = this.world.getType(this.getChunkCoordinates()).getBlock().getJumpFactor();
        float f1 = this.world.getType(this.ar()).getBlock().getJumpFactor();

        return (double) f == 1.0D ? f1 : f;
    }

    protected float getBlockSpeedFactor() {
        Block block = this.world.getType(this.getChunkCoordinates()).getBlock();
        float f = block.getSpeedFactor();

        return block != Blocks.WATER && block != Blocks.BUBBLE_COLUMN ? ((double) f == 1.0D ? this.world.getType(this.ar()).getBlock().getSpeedFactor() : f) : f;
    }

    protected BlockPosition ar() {
        return new BlockPosition(this.loc.x, this.getBoundingBox().minY - 0.5000001D, this.loc.z);
    }

    protected Vec3D a(Vec3D vec3d, EnumMoveType enummovetype) {
        return vec3d;
    }

    protected Vec3D b(Vec3D vec3d) {
        if (vec3d.g() <= 1.0E-7D) {
            return vec3d;
        } else {
            long i = this.world.getTime();

            if (i != this.aB) {
                Arrays.fill(this.aA, 0.0D);
                this.aB = i;
            }

            double d0;

            if (vec3d.x != 0.0D) {
                d0 = this.a(EnumDirection.EnumAxis.X, vec3d.x);
                return Math.abs(d0) <= 9.999999747378752E-6D ? Vec3D.ORIGIN : new Vec3D(d0, 0.0D, 0.0D);
            } else if (vec3d.y != 0.0D) {
                d0 = this.a(EnumDirection.EnumAxis.Y, vec3d.y);
                return Math.abs(d0) <= 9.999999747378752E-6D ? Vec3D.ORIGIN : new Vec3D(0.0D, d0, 0.0D);
            } else if (vec3d.z != 0.0D) {
                d0 = this.a(EnumDirection.EnumAxis.Z, vec3d.z);
                return Math.abs(d0) <= 9.999999747378752E-6D ? Vec3D.ORIGIN : new Vec3D(0.0D, 0.0D, d0);
            } else {
                return Vec3D.ORIGIN;
            }
        }
    }

    private double a(EnumDirection.EnumAxis enumdirection_enumaxis, double d0) {
        int i = enumdirection_enumaxis.ordinal();
        double d1 = MathHelper.a(d0 + this.aA[i], -0.51D, 0.51D);

        d0 = d1 - this.aA[i];
        this.aA[i] = d1;
        return d0;
    }

    private Vec3D g(Vec3D vec3d) {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        VoxelShapeCollision voxelshapecollision = VoxelShapeCollision.a(this);
        VoxelShape voxelshape = this.world.getWorldBorder().c();
        Stream<VoxelShape> stream = VoxelShapes.c(voxelshape, VoxelShapes.a(axisalignedbb.shrink(1.0E-7D)), OperatorBoolean.AND) ? Stream.empty() : Stream.of(voxelshape);
        Stream<VoxelShape> stream1 = this.world.c(this, axisalignedbb.b(vec3d), (entity) -> {
            return true;
        });
        StreamAccumulator<VoxelShape> streamaccumulator = new StreamAccumulator<>(Stream.concat(stream1, stream));
        Vec3D vec3d1 = vec3d.g() == 0.0D ? vec3d : a(this, vec3d, axisalignedbb, this.world, voxelshapecollision, streamaccumulator);
        boolean flag = vec3d.x != vec3d1.x;
        boolean flag1 = vec3d.y != vec3d1.y;
        boolean flag2 = vec3d.z != vec3d1.z;
        boolean flag3 = this.onGround || flag1 && vec3d.y < 0.0D;

        if (this.G > 0.0F && flag3 && (flag || flag2)) {
            Vec3D vec3d2 = a(this, new Vec3D(vec3d.x, (double) this.G, vec3d.z), axisalignedbb, this.world, voxelshapecollision, streamaccumulator);
            Vec3D vec3d3 = a(this, new Vec3D(0.0D, (double) this.G, 0.0D), axisalignedbb.b(vec3d.x, 0.0D, vec3d.z), this.world, voxelshapecollision, streamaccumulator);

            if (vec3d3.y < (double) this.G) {
                Vec3D vec3d4 = a(this, new Vec3D(vec3d.x, 0.0D, vec3d.z), axisalignedbb.c(vec3d3), this.world, voxelshapecollision, streamaccumulator).e(vec3d3);

                if (c(vec3d4) > c(vec3d2)) {
                    vec3d2 = vec3d4;
                }
            }

            if (c(vec3d2) > c(vec3d1)) {
                return vec3d2.e(a(this, new Vec3D(0.0D, -vec3d2.y + vec3d.y, 0.0D), axisalignedbb.c(vec3d2), this.world, voxelshapecollision, streamaccumulator));
            }
        }

        return vec3d1;
    }

    public static double c(Vec3D vec3d) {
        return vec3d.x * vec3d.x + vec3d.z * vec3d.z;
    }

    public static Vec3D a(@Nullable Entity entity, Vec3D vec3d, AxisAlignedBB axisalignedbb, World world, VoxelShapeCollision voxelshapecollision, StreamAccumulator<VoxelShape> streamaccumulator) {
        boolean flag = vec3d.x == 0.0D;
        boolean flag1 = vec3d.y == 0.0D;
        boolean flag2 = vec3d.z == 0.0D;

        if ((!flag || !flag1) && (!flag || !flag2) && (!flag1 || !flag2)) {
            StreamAccumulator<VoxelShape> streamaccumulator1 = new StreamAccumulator<>(Stream.concat(streamaccumulator.a(), world.b(entity, axisalignedbb.b(vec3d))));

            return a(vec3d, axisalignedbb, streamaccumulator1);
        } else {
            return a(vec3d, axisalignedbb, world, voxelshapecollision, streamaccumulator);
        }
    }

    public static Vec3D a(Vec3D vec3d, AxisAlignedBB axisalignedbb, StreamAccumulator<VoxelShape> streamaccumulator) {
        double d0 = vec3d.x;
        double d1 = vec3d.y;
        double d2 = vec3d.z;

        if (d1 != 0.0D) {
            d1 = VoxelShapes.a(EnumDirection.EnumAxis.Y, axisalignedbb, streamaccumulator.a(), d1);
            if (d1 != 0.0D) {
                axisalignedbb = axisalignedbb.d(0.0D, d1, 0.0D);
            }
        }

        boolean flag = Math.abs(d0) < Math.abs(d2);

        if (flag && d2 != 0.0D) {
            d2 = VoxelShapes.a(EnumDirection.EnumAxis.Z, axisalignedbb, streamaccumulator.a(), d2);
            if (d2 != 0.0D) {
                axisalignedbb = axisalignedbb.d(0.0D, 0.0D, d2);
            }
        }

        if (d0 != 0.0D) {
            d0 = VoxelShapes.a(EnumDirection.EnumAxis.X, axisalignedbb, streamaccumulator.a(), d0);
            if (!flag && d0 != 0.0D) {
                axisalignedbb = axisalignedbb.d(d0, 0.0D, 0.0D);
            }
        }

        if (!flag && d2 != 0.0D) {
            d2 = VoxelShapes.a(EnumDirection.EnumAxis.Z, axisalignedbb, streamaccumulator.a(), d2);
        }

        return new Vec3D(d0, d1, d2);
    }

    public static Vec3D a(Vec3D vec3d, AxisAlignedBB axisalignedbb, IWorldReader iworldreader, VoxelShapeCollision voxelshapecollision, StreamAccumulator<VoxelShape> streamaccumulator) {
        double d0 = vec3d.x;
        double d1 = vec3d.y;
        double d2 = vec3d.z;

        if (d1 != 0.0D) {
            d1 = VoxelShapes.a(EnumDirection.EnumAxis.Y, axisalignedbb, iworldreader, d1, voxelshapecollision, streamaccumulator.a());
            if (d1 != 0.0D) {
                axisalignedbb = axisalignedbb.d(0.0D, d1, 0.0D);
            }
        }

        boolean flag = Math.abs(d0) < Math.abs(d2);

        if (flag && d2 != 0.0D) {
            d2 = VoxelShapes.a(EnumDirection.EnumAxis.Z, axisalignedbb, iworldreader, d2, voxelshapecollision, streamaccumulator.a());
            if (d2 != 0.0D) {
                axisalignedbb = axisalignedbb.d(0.0D, 0.0D, d2);
            }
        }

        if (d0 != 0.0D) {
            d0 = VoxelShapes.a(EnumDirection.EnumAxis.X, axisalignedbb, iworldreader, d0, voxelshapecollision, streamaccumulator.a());
            if (!flag && d0 != 0.0D) {
                axisalignedbb = axisalignedbb.d(d0, 0.0D, 0.0D);
            }
        }

        if (!flag && d2 != 0.0D) {
            d2 = VoxelShapes.a(EnumDirection.EnumAxis.Z, axisalignedbb, iworldreader, d2, voxelshapecollision, streamaccumulator.a());
        }

        return new Vec3D(d0, d1, d2);
    }

    protected float as() {
        return (float) ((int) this.B + 1);
    }

    public void recalcPosition() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();

        this.setPositionRaw((axisalignedbb.minX + axisalignedbb.maxX) / 2.0D, axisalignedbb.minY, (axisalignedbb.minZ + axisalignedbb.maxZ) / 2.0D);
    }

    protected SoundEffect getSoundSwim() {
        return SoundEffects.ENTITY_GENERIC_SWIM;
    }

    protected SoundEffect getSoundSplash() {
        return SoundEffects.ENTITY_GENERIC_SPLASH;
    }

    protected SoundEffect getSoundSplashHighSpeed() {
        return SoundEffects.ENTITY_GENERIC_SPLASH;
    }

    protected void checkBlockCollisions() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        BlockPosition blockposition = new BlockPosition(axisalignedbb.minX + 0.001D, axisalignedbb.minY + 0.001D, axisalignedbb.minZ + 0.001D);
        BlockPosition blockposition1 = new BlockPosition(axisalignedbb.maxX - 0.001D, axisalignedbb.maxY - 0.001D, axisalignedbb.maxZ - 0.001D);
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();

        if (this.world.areChunksLoadedBetween(blockposition, blockposition1)) {
            for (int i = blockposition.getX(); i <= blockposition1.getX(); ++i) {
                for (int j = blockposition.getY(); j <= blockposition1.getY(); ++j) {
                    for (int k = blockposition.getZ(); k <= blockposition1.getZ(); ++k) {
                        blockposition_mutableblockposition.d(i, j, k);
                        IBlockData iblockdata = this.world.getType(blockposition_mutableblockposition);

                        try {
                            iblockdata.a(this.world, blockposition_mutableblockposition, this);
                            this.a(iblockdata);
                        } catch (Throwable throwable) {
                            CrashReport crashreport = CrashReport.a(throwable, "Colliding entity with block");
                            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Block being collided with");

                            CrashReportSystemDetails.a(crashreportsystemdetails, blockposition_mutableblockposition, iblockdata);
                            throw new ReportedException(crashreport);
                        }
                    }
                }
            }
        }

    }

    protected void a(IBlockData iblockdata) {}

    protected void b(BlockPosition blockposition, IBlockData iblockdata) {
        if (!iblockdata.getMaterial().isLiquid()) {
            IBlockData iblockdata1 = this.world.getType(blockposition.up());
            SoundEffectType soundeffecttype = iblockdata1.a(Blocks.SNOW) ? iblockdata1.getStepSound() : iblockdata.getStepSound();

            this.playSound(soundeffecttype.d(), soundeffecttype.a() * 0.15F, soundeffecttype.b());
        }
    }

    protected void d(float f) {
        this.playSound(this.getSoundSwim(), f, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
    }

    protected float e(float f) {
        return 0.0F;
    }

    protected boolean ay() {
        return false;
    }

    public void playSound(SoundEffect soundeffect, float f, float f1) {
        if (!this.isSilent()) {
            this.world.playSound((EntityHuman) null, this.locX(), this.locY(), this.locZ(), soundeffect, this.getSoundCategory(), f, f1);
        }

    }

    public boolean isSilent() {
        return (Boolean) this.datawatcher.get(Entity.as);
    }

    public void setSilent(boolean flag) {
        this.datawatcher.set(Entity.as, flag);
    }

    public boolean isNoGravity() {
        return (Boolean) this.datawatcher.get(Entity.at);
    }

    public void setNoGravity(boolean flag) {
        this.datawatcher.set(Entity.at, flag);
    }

    protected boolean playStepSound() {
        return true;
    }

    protected void a(double d0, boolean flag, IBlockData iblockdata, BlockPosition blockposition) {
        if (flag) {
            if (this.fallDistance > 0.0F) {
                iblockdata.getBlock().fallOn(this.world, blockposition, this, this.fallDistance);
            }

            this.fallDistance = 0.0F;
        } else if (d0 < 0.0D) {
            this.fallDistance = (float) ((double) this.fallDistance - d0);
        }

    }

    public boolean isFireProof() {
        return this.getEntityType().c();
    }

    public boolean b(float f, float f1) {
        if (this.isVehicle()) {
            Iterator iterator = this.getPassengers().iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                entity.b(f, f1);
            }
        }

        return false;
    }

    public boolean isInWater() {
        return this.inWater;
    }

    private boolean isInRain() {
        BlockPosition blockposition = this.getChunkCoordinates();

        return this.world.isRainingAt(blockposition) || this.world.isRainingAt(new BlockPosition((double) blockposition.getX(), this.getBoundingBox().maxY, (double) blockposition.getZ()));
    }

    private boolean k() {
        return this.world.getType(this.getChunkCoordinates()).a(Blocks.BUBBLE_COLUMN);
    }

    public boolean isInWaterOrRain() {
        return this.isInWater() || this.isInRain();
    }

    public boolean aF() {
        return this.isInWater() || this.isInRain() || this.k();
    }

    public boolean aG() {
        return this.isInWater() || this.k();
    }

    public boolean aH() {
        return this.N && this.isInWater();
    }

    public void aI() {
        if (this.isSwimming()) {
            this.setSwimming(this.isSprinting() && this.isInWater() && !this.isPassenger());
        } else {
            this.setSwimming(this.isSprinting() && this.aH() && !this.isPassenger());
        }

    }

    protected boolean aJ() {
        this.M.clear();
        this.aK();
        double d0 = this.world.getDimensionManager().isNether() ? 0.007D : 0.0023333333333333335D;
        boolean flag = this.a((Tag) TagsFluid.LAVA, d0);

        return this.isInWater() || flag;
    }

    void aK() {
        if (this.getVehicle() instanceof EntityBoat) {
            this.inWater = false;
        } else if (this.a((Tag) TagsFluid.WATER, 0.014D)) {
            if (!this.inWater && !this.justCreated) {
                this.aL();
            }

            this.fallDistance = 0.0F;
            this.inWater = true;
            this.extinguish();
        } else {
            this.inWater = false;
        }

    }

    private void m() {
        this.N = this.a((Tag) TagsFluid.WATER);
        this.O = null;
        double d0 = this.getHeadY() - 0.1111111119389534D;
        Entity entity = this.getVehicle();

        if (entity instanceof EntityBoat) {
            EntityBoat entityboat = (EntityBoat) entity;

            if (!entityboat.aH() && entityboat.getBoundingBox().maxY >= d0 && entityboat.getBoundingBox().minY <= d0) {
                return;
            }
        }

        BlockPosition blockposition = new BlockPosition(this.locX(), d0, this.locZ());
        Fluid fluid = this.world.getFluid(blockposition);
        Iterator iterator = TagsFluid.b().iterator();

        Tag tag;

        do {
            if (!iterator.hasNext()) {
                return;
            }

            tag = (Tag) iterator.next();
        } while (!fluid.a(tag));

        double d1 = (double) ((float) blockposition.getY() + fluid.getHeight(this.world, blockposition));

        if (d1 > d0) {
            this.O = tag;
        }

    }

    protected void aL() {
        Entity entity = this.isVehicle() && this.getRidingPassenger() != null ? this.getRidingPassenger() : this;
        float f = entity == this ? 0.2F : 0.9F;
        Vec3D vec3d = entity.getMot();
        float f1 = MathHelper.sqrt(vec3d.x * vec3d.x * 0.20000000298023224D + vec3d.y * vec3d.y + vec3d.z * vec3d.z * 0.20000000298023224D) * f;

        if (f1 > 1.0F) {
            f1 = 1.0F;
        }

        if ((double) f1 < 0.25D) {
            this.playSound(this.getSoundSplash(), f1, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
        } else {
            this.playSound(this.getSoundSplashHighSpeed(), f1, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
        }

        float f2 = (float) MathHelper.floor(this.locY());

        double d0;
        double d1;
        int i;

        for (i = 0; (float) i < 1.0F + this.size.width * 20.0F; ++i) {
            d0 = (this.random.nextDouble() * 2.0D - 1.0D) * (double) this.size.width;
            d1 = (this.random.nextDouble() * 2.0D - 1.0D) * (double) this.size.width;
            this.world.addParticle(Particles.BUBBLE, this.locX() + d0, (double) (f2 + 1.0F), this.locZ() + d1, vec3d.x, vec3d.y - this.random.nextDouble() * 0.20000000298023224D, vec3d.z);
        }

        for (i = 0; (float) i < 1.0F + this.size.width * 20.0F; ++i) {
            d0 = (this.random.nextDouble() * 2.0D - 1.0D) * (double) this.size.width;
            d1 = (this.random.nextDouble() * 2.0D - 1.0D) * (double) this.size.width;
            this.world.addParticle(Particles.SPLASH, this.locX() + d0, (double) (f2 + 1.0F), this.locZ() + d1, vec3d.x, vec3d.y, vec3d.z);
        }

    }

    protected IBlockData aM() {
        return this.world.getType(this.ao());
    }

    public boolean aN() {
        return this.isSprinting() && !this.isInWater() && !this.isSpectator() && !this.by() && !this.aP() && this.isAlive();
    }

    protected void aO() {
        int i = MathHelper.floor(this.locX());
        int j = MathHelper.floor(this.locY() - 0.20000000298023224D);
        int k = MathHelper.floor(this.locZ());
        BlockPosition blockposition = new BlockPosition(i, j, k);
        IBlockData iblockdata = this.world.getType(blockposition);

        if (iblockdata.h() != EnumRenderType.INVISIBLE) {
            Vec3D vec3d = this.getMot();

            this.world.addParticle(new ParticleParamBlock(Particles.BLOCK, iblockdata), this.locX() + (this.random.nextDouble() - 0.5D) * (double) this.size.width, this.locY() + 0.1D, this.locZ() + (this.random.nextDouble() - 0.5D) * (double) this.size.width, vec3d.x * -4.0D, 1.5D, vec3d.z * -4.0D);
        }

    }

    public boolean a(Tag<FluidType> tag) {
        return this.O == tag;
    }

    public boolean aP() {
        return !this.justCreated && this.M.getDouble(TagsFluid.LAVA) > 0.0D;
    }

    public void a(float f, Vec3D vec3d) {
        Vec3D vec3d1 = a(vec3d, f, this.yaw);

        this.setMot(this.getMot().e(vec3d1));
    }

    private static Vec3D a(Vec3D vec3d, float f, float f1) {
        double d0 = vec3d.g();

        if (d0 < 1.0E-7D) {
            return Vec3D.ORIGIN;
        } else {
            Vec3D vec3d1 = (d0 > 1.0D ? vec3d.d() : vec3d).a((double) f);
            float f2 = MathHelper.sin(f1 * 0.017453292F);
            float f3 = MathHelper.cos(f1 * 0.017453292F);

            return new Vec3D(vec3d1.x * (double) f3 - vec3d1.z * (double) f2, vec3d1.y, vec3d1.z * (double) f3 + vec3d1.x * (double) f2);
        }
    }

    public float aQ() {
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition(this.locX(), 0.0D, this.locZ());

        if (this.world.isLoaded(blockposition_mutableblockposition)) {
            blockposition_mutableblockposition.p(MathHelper.floor(this.getHeadY()));
            return this.world.y(blockposition_mutableblockposition);
        } else {
            return 0.0F;
        }
    }

    public void spawnIn(World world) {
        this.world = world;
    }

    public void setLocation(double d0, double d1, double d2, float f, float f1) {
        this.f(d0, d1, d2);
        this.yaw = f % 360.0F;
        this.pitch = MathHelper.a(f1, -90.0F, 90.0F) % 360.0F;
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
    }

    public void f(double d0, double d1, double d2) {
        double d3 = MathHelper.a(d0, -3.0E7D, 3.0E7D);
        double d4 = MathHelper.a(d2, -3.0E7D, 3.0E7D);

        this.lastX = d3;
        this.lastY = d1;
        this.lastZ = d4;
        this.setPosition(d3, d1, d4);
    }

    public void d(Vec3D vec3d) {
        this.teleportAndSync(vec3d.x, vec3d.y, vec3d.z);
    }

    public void teleportAndSync(double d0, double d1, double d2) {
        this.setPositionRotation(d0, d1, d2, this.yaw, this.pitch);
    }

    public void setPositionRotation(BlockPosition blockposition, float f, float f1) {
        this.setPositionRotation((double) blockposition.getX() + 0.5D, (double) blockposition.getY(), (double) blockposition.getZ() + 0.5D, f, f1);
    }

    public void setPositionRotation(double d0, double d1, double d2, float f, float f1) {
        this.g(d0, d1, d2);
        this.yaw = f;
        this.pitch = f1;
        this.ae();
    }

    public void g(double d0, double d1, double d2) {
        this.setPositionRaw(d0, d1, d2);
        this.lastX = d0;
        this.lastY = d1;
        this.lastZ = d2;
        this.D = d0;
        this.E = d1;
        this.F = d2;
    }

    public float g(Entity entity) {
        float f = (float) (this.locX() - entity.locX());
        float f1 = (float) (this.locY() - entity.locY());
        float f2 = (float) (this.locZ() - entity.locZ());

        return MathHelper.c(f * f + f1 * f1 + f2 * f2);
    }

    public double h(double d0, double d1, double d2) {
        double d3 = this.locX() - d0;
        double d4 = this.locY() - d1;
        double d5 = this.locZ() - d2;

        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    public double h(Entity entity) {
        return this.e(entity.getPositionVector());
    }

    public double e(Vec3D vec3d) {
        double d0 = this.locX() - vec3d.x;
        double d1 = this.locY() - vec3d.y;
        double d2 = this.locZ() - vec3d.z;

        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public void pickup(EntityHuman entityhuman) {}

    public void collide(Entity entity) {
        if (!this.isSameVehicle(entity)) {
            if (!entity.noclip && !this.noclip) {
                double d0 = entity.locX() - this.locX();
                double d1 = entity.locZ() - this.locZ();
                double d2 = MathHelper.a(d0, d1);

                if (d2 >= 0.009999999776482582D) {
                    d2 = (double) MathHelper.sqrt(d2);
                    d0 /= d2;
                    d1 /= d2;
                    double d3 = 1.0D / d2;

                    if (d3 > 1.0D) {
                        d3 = 1.0D;
                    }

                    d0 *= d3;
                    d1 *= d3;
                    d0 *= 0.05000000074505806D;
                    d1 *= 0.05000000074505806D;
                    d0 *= (double) (1.0F - this.I);
                    d1 *= (double) (1.0F - this.I);
                    if (!this.isVehicle()) {
                        this.i(-d0, 0.0D, -d1);
                    }

                    if (!entity.isVehicle()) {
                        entity.i(d0, 0.0D, d1);
                    }
                }

            }
        }
    }

    public void i(double d0, double d1, double d2) {
        this.setMot(this.getMot().add(d0, d1, d2));
        this.impulse = true;
    }

    protected void velocityChanged() {
        this.velocityChanged = true;
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else {
            this.velocityChanged();
            return false;
        }
    }

    public final Vec3D f(float f) {
        return this.c(this.g(f), this.h(f));
    }

    public float g(float f) {
        return f == 1.0F ? this.pitch : MathHelper.g(f, this.lastPitch, this.pitch);
    }

    public float h(float f) {
        return f == 1.0F ? this.yaw : MathHelper.g(f, this.lastYaw, this.yaw);
    }

    protected final Vec3D c(float f, float f1) {
        float f2 = f * 0.017453292F;
        float f3 = -f1 * 0.017453292F;
        float f4 = MathHelper.cos(f3);
        float f5 = MathHelper.sin(f3);
        float f6 = MathHelper.cos(f2);
        float f7 = MathHelper.sin(f2);

        return new Vec3D((double) (f5 * f6), (double) (-f7), (double) (f4 * f6));
    }

    public final Vec3D i(float f) {
        return this.d(this.g(f), this.h(f));
    }

    protected final Vec3D d(float f, float f1) {
        return this.c(f - 90.0F, f1);
    }

    public final Vec3D j(float f) {
        if (f == 1.0F) {
            return new Vec3D(this.locX(), this.getHeadY(), this.locZ());
        } else {
            double d0 = MathHelper.d((double) f, this.lastX, this.locX());
            double d1 = MathHelper.d((double) f, this.lastY, this.locY()) + (double) this.getHeadHeight();
            double d2 = MathHelper.d((double) f, this.lastZ, this.locZ());

            return new Vec3D(d0, d1, d2);
        }
    }

    public MovingObjectPosition a(double d0, float f, boolean flag) {
        Vec3D vec3d = this.j(f);
        Vec3D vec3d1 = this.f(f);
        Vec3D vec3d2 = vec3d.add(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0);

        return this.world.rayTrace(new RayTrace(vec3d, vec3d2, RayTrace.BlockCollisionOption.OUTLINE, flag ? RayTrace.FluidCollisionOption.ANY : RayTrace.FluidCollisionOption.NONE, this));
    }

    public boolean isInteractable() {
        return false;
    }

    public boolean isCollidable() {
        return false;
    }

    public void a(Entity entity, int i, DamageSource damagesource) {
        if (entity instanceof EntityPlayer) {
            CriterionTriggers.c.a((EntityPlayer) entity, this, damagesource);
        }

    }

    public boolean a_(NBTTagCompound nbttagcompound) {
        String s = this.getSaveID();

        if (!this.dead && s != null) {
            nbttagcompound.setString("id", s);
            this.save(nbttagcompound);
            return true;
        } else {
            return false;
        }
    }

    public boolean d(NBTTagCompound nbttagcompound) {
        return this.isPassenger() ? false : this.a_(nbttagcompound);
    }

    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        try {
            if (this.vehicle != null) {
                nbttagcompound.set("Pos", this.a(this.vehicle.locX(), this.locY(), this.vehicle.locZ()));
            } else {
                nbttagcompound.set("Pos", this.a(this.locX(), this.locY(), this.locZ()));
            }

            Vec3D vec3d = this.getMot();

            nbttagcompound.set("Motion", this.a(vec3d.x, vec3d.y, vec3d.z));
            nbttagcompound.set("Rotation", this.a(this.yaw, this.pitch));
            nbttagcompound.setFloat("FallDistance", this.fallDistance);
            nbttagcompound.setShort("Fire", (short) this.fireTicks);
            nbttagcompound.setShort("Air", (short) this.getAirTicks());
            nbttagcompound.setBoolean("OnGround", this.onGround);
            nbttagcompound.setBoolean("Invulnerable", this.invulnerable);
            nbttagcompound.setInt("PortalCooldown", this.portalCooldown);
            nbttagcompound.a("UUID", this.getUniqueID());
            IChatBaseComponent ichatbasecomponent = this.getCustomName();

            if (ichatbasecomponent != null) {
                nbttagcompound.setString("CustomName", IChatBaseComponent.ChatSerializer.a(ichatbasecomponent));
            }

            if (this.getCustomNameVisible()) {
                nbttagcompound.setBoolean("CustomNameVisible", this.getCustomNameVisible());
            }

            if (this.isSilent()) {
                nbttagcompound.setBoolean("Silent", this.isSilent());
            }

            if (this.isNoGravity()) {
                nbttagcompound.setBoolean("NoGravity", this.isNoGravity());
            }

            if (this.glowing) {
                nbttagcompound.setBoolean("Glowing", this.glowing);
            }

            NBTTagList nbttaglist;
            Iterator iterator;

            if (!this.ay.isEmpty()) {
                nbttaglist = new NBTTagList();
                iterator = this.ay.iterator();

                while (iterator.hasNext()) {
                    String s = (String) iterator.next();

                    nbttaglist.add(NBTTagString.a(s));
                }

                nbttagcompound.set("Tags", nbttaglist);
            }

            this.saveData(nbttagcompound);
            if (this.isVehicle()) {
                nbttaglist = new NBTTagList();
                iterator = this.getPassengers().iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                    if (entity.a_(nbttagcompound1)) {
                        nbttaglist.add(nbttagcompound1);
                    }
                }

                if (!nbttaglist.isEmpty()) {
                    nbttagcompound.set("Passengers", nbttaglist);
                }
            }

            return nbttagcompound;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.a(throwable, "Saving entity NBT");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Entity being saved");

            this.appendEntityCrashDetails(crashreportsystemdetails);
            throw new ReportedException(crashreport);
        }
    }

    public void load(NBTTagCompound nbttagcompound) {
        try {
            NBTTagList nbttaglist = nbttagcompound.getList("Pos", 6);
            NBTTagList nbttaglist1 = nbttagcompound.getList("Motion", 6);
            NBTTagList nbttaglist2 = nbttagcompound.getList("Rotation", 5);
            double d0 = nbttaglist1.h(0);
            double d1 = nbttaglist1.h(1);
            double d2 = nbttaglist1.h(2);

            this.setMot(Math.abs(d0) > 10.0D ? 0.0D : d0, Math.abs(d1) > 10.0D ? 0.0D : d1, Math.abs(d2) > 10.0D ? 0.0D : d2);
            this.g(nbttaglist.h(0), nbttaglist.h(1), nbttaglist.h(2));
            this.yaw = nbttaglist2.i(0);
            this.pitch = nbttaglist2.i(1);
            this.lastYaw = this.yaw;
            this.lastPitch = this.pitch;
            this.setHeadRotation(this.yaw);
            this.n(this.yaw);
            this.fallDistance = nbttagcompound.getFloat("FallDistance");
            this.fireTicks = nbttagcompound.getShort("Fire");
            this.setAirTicks(nbttagcompound.getShort("Air"));
            this.onGround = nbttagcompound.getBoolean("OnGround");
            this.invulnerable = nbttagcompound.getBoolean("Invulnerable");
            this.portalCooldown = nbttagcompound.getInt("PortalCooldown");
            if (nbttagcompound.b("UUID")) {
                this.uniqueID = nbttagcompound.a("UUID");
                this.ae = this.uniqueID.toString();
            }

            if (Double.isFinite(this.locX()) && Double.isFinite(this.locY()) && Double.isFinite(this.locZ())) {
                if (Double.isFinite((double) this.yaw) && Double.isFinite((double) this.pitch)) {
                    this.ae();
                    this.setYawPitch(this.yaw, this.pitch);
                    if (nbttagcompound.hasKeyOfType("CustomName", 8)) {
                        String s = nbttagcompound.getString("CustomName");

                        try {
                            this.setCustomName(IChatBaseComponent.ChatSerializer.a(s));
                        } catch (Exception exception) {
                            Entity.LOGGER.warn("Failed to parse entity custom name {}", s, exception);
                        }
                    }

                    this.setCustomNameVisible(nbttagcompound.getBoolean("CustomNameVisible"));
                    this.setSilent(nbttagcompound.getBoolean("Silent"));
                    this.setNoGravity(nbttagcompound.getBoolean("NoGravity"));
                    this.i(nbttagcompound.getBoolean("Glowing"));
                    if (nbttagcompound.hasKeyOfType("Tags", 9)) {
                        this.ay.clear();
                        NBTTagList nbttaglist3 = nbttagcompound.getList("Tags", 8);
                        int i = Math.min(nbttaglist3.size(), 1024);

                        for (int j = 0; j < i; ++j) {
                            this.ay.add(nbttaglist3.getString(j));
                        }
                    }

                    this.loadData(nbttagcompound);
                    if (this.aU()) {
                        this.ae();
                    }

                } else {
                    throw new IllegalStateException("Entity has invalid rotation");
                }
            } else {
                throw new IllegalStateException("Entity has invalid position");
            }
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.a(throwable, "Loading entity NBT");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Entity being loaded");

            this.appendEntityCrashDetails(crashreportsystemdetails);
            throw new ReportedException(crashreport);
        }
    }

    protected boolean aU() {
        return true;
    }

    @Nullable
    public final String getSaveID() {
        EntityTypes<?> entitytypes = this.getEntityType();
        MinecraftKey minecraftkey = EntityTypes.getName(entitytypes);

        return entitytypes.a() && minecraftkey != null ? minecraftkey.toString() : null;
    }

    protected abstract void loadData(NBTTagCompound nbttagcompound);

    protected abstract void saveData(NBTTagCompound nbttagcompound);

    protected NBTTagList a(double... adouble) {
        NBTTagList nbttaglist = new NBTTagList();
        double[] adouble1 = adouble;
        int i = adouble.length;

        for (int j = 0; j < i; ++j) {
            double d0 = adouble1[j];

            nbttaglist.add(NBTTagDouble.a(d0));
        }

        return nbttaglist;
    }

    protected NBTTagList a(float... afloat) {
        NBTTagList nbttaglist = new NBTTagList();
        float[] afloat1 = afloat;
        int i = afloat.length;

        for (int j = 0; j < i; ++j) {
            float f = afloat1[j];

            nbttaglist.add(NBTTagFloat.a(f));
        }

        return nbttaglist;
    }

    @Nullable
    public EntityItem a(IMaterial imaterial) {
        return this.a(imaterial, 0);
    }

    @Nullable
    public EntityItem a(IMaterial imaterial, int i) {
        return this.a(new ItemStack(imaterial), (float) i);
    }

    @Nullable
    public EntityItem a(ItemStack itemstack) {
        return this.a(itemstack, 0.0F);
    }

    @Nullable
    public EntityItem a(ItemStack itemstack, float f) {
        if (itemstack.isEmpty()) {
            return null;
        } else if (this.world.isClientSide) {
            return null;
        } else {
            EntityItem entityitem = new EntityItem(this.world, this.locX(), this.locY() + (double) f, this.locZ(), itemstack);

            entityitem.defaultPickupDelay();
            this.world.addEntity(entityitem);
            return entityitem;
        }
    }

    public boolean isAlive() {
        return !this.dead;
    }

    public boolean inBlock() {
        if (this.noclip) {
            return false;
        } else {
            float f = 0.1F;
            float f1 = this.size.width * 0.8F;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.g((double) f1, 0.10000000149011612D, (double) f1).d(this.locX(), this.getHeadY(), this.locZ());

            return this.world.b(this, axisalignedbb, (iblockdata, blockposition) -> {
                return iblockdata.o(this.world, blockposition);
            }).findAny().isPresent();
        }
    }

    public EnumInteractionResult a(EntityHuman entityhuman, EnumHand enumhand) {
        return EnumInteractionResult.PASS;
    }

    public boolean j(Entity entity) {
        return entity.aY() && !this.isSameVehicle(entity);
    }

    public boolean aY() {
        return false;
    }

    public void passengerTick() {
        this.setMot(Vec3D.ORIGIN);
        this.tick();
        if (this.isPassenger()) {
            this.getVehicle().k(this);
        }
    }

    public void k(Entity entity) {
        this.a(entity, Entity::setPosition);
    }

    private void a(Entity entity, Entity.a entity_a) {
        if (this.w(entity)) {
            double d0 = this.locY() + this.bb() + entity.ba();

            entity_a.accept(entity, this.locX(), d0, this.locZ());
        }
    }

    public double ba() {
        return 0.0D;
    }

    public double bb() {
        return (double) this.size.height * 0.75D;
    }

    public boolean startRiding(Entity entity) {
        return this.a(entity, false);
    }

    public boolean a(Entity entity, boolean flag) {
        for (Entity entity1 = entity; entity1.vehicle != null; entity1 = entity1.vehicle) {
            if (entity1.vehicle == this) {
                return false;
            }
        }

        if (!flag && (!this.n(entity) || !entity.q(this))) {
            return false;
        } else {
            if (this.isPassenger()) {
                this.stopRiding();
            }

            this.setPose(EntityPose.STANDING);
            this.vehicle = entity;
            this.vehicle.addPassenger(this);
            return true;
        }
    }

    protected boolean n(Entity entity) {
        return !this.isSneaking() && this.j <= 0;
    }

    protected boolean c(EntityPose entitypose) {
        return this.world.getCubes(this, this.d(entitypose).shrink(1.0E-7D));
    }

    public void ejectPassengers() {
        for (int i = this.passengers.size() - 1; i >= 0; --i) {
            ((Entity) this.passengers.get(i)).stopRiding();
        }

    }

    public void be() {
        if (this.vehicle != null) {
            Entity entity = this.vehicle;

            this.vehicle = null;
            entity.removePassenger(this);
        }

    }

    public void stopRiding() {
        this.be();
    }

    protected void addPassenger(Entity entity) {
        if (entity.getVehicle() != this) {
            throw new IllegalStateException("Use x.startRiding(y), not y.addPassenger(x)");
        } else {
            if (!this.world.isClientSide && entity instanceof EntityHuman && !(this.getRidingPassenger() instanceof EntityHuman)) {
                this.passengers.add(0, entity);
            } else {
                this.passengers.add(entity);
            }

        }
    }

    protected void removePassenger(Entity entity) {
        if (entity.getVehicle() == this) {
            throw new IllegalStateException("Use x.stopRiding(y), not y.removePassenger(x)");
        } else {
            this.passengers.remove(entity);
            entity.j = 60;
        }
    }

    protected boolean q(Entity entity) {
        return this.getPassengers().size() < 1;
    }

    public float bf() {
        return 0.0F;
    }

    public Vec3D getLookDirection() {
        return this.c(this.pitch, this.yaw);
    }

    public Vec2F bh() {
        return new Vec2F(this.pitch, this.yaw);
    }

    public void d(BlockPosition blockposition) {
        if (this.ah()) {
            this.resetPortalCooldown();
        } else {
            if (!this.world.isClientSide && !blockposition.equals(this.ac)) {
                this.ac = blockposition.immutableCopy();
            }

            this.inPortal = true;
        }
    }

    protected void doPortalTick() {
        if (this.world instanceof WorldServer) {
            int i = this.ai();
            WorldServer worldserver = (WorldServer) this.world;

            if (this.inPortal) {
                MinecraftServer minecraftserver = worldserver.getMinecraftServer();
                ResourceKey<World> resourcekey = this.world.getDimensionKey() == World.THE_NETHER ? World.OVERWORLD : World.THE_NETHER;
                WorldServer worldserver1 = minecraftserver.getWorldServer(resourcekey);

                if (worldserver1 != null && minecraftserver.getAllowNether() && !this.isPassenger() && this.portalTicks++ >= i) {
                    this.world.getMethodProfiler().enter("portal");
                    this.portalTicks = i;
                    this.resetPortalCooldown();
                    this.b(worldserver1);
                    this.world.getMethodProfiler().exit();
                }

                this.inPortal = false;
            } else {
                if (this.portalTicks > 0) {
                    this.portalTicks -= 4;
                }

                if (this.portalTicks < 0) {
                    this.portalTicks = 0;
                }
            }

            this.E();
        }
    }

    public int getDefaultPortalCooldown() {
        return 300;
    }

    public Iterable<ItemStack> bm() {
        return Entity.c;
    }

    public Iterable<ItemStack> getArmorItems() {
        return Entity.c;
    }

    public Iterable<ItemStack> bo() {
        return Iterables.concat(this.bm(), this.getArmorItems());
    }

    public void setSlot(EnumItemSlot enumitemslot, ItemStack itemstack) {}

    public boolean isBurning() {
        boolean flag = this.world != null && this.world.isClientSide;

        return !this.isFireProof() && (this.fireTicks > 0 || flag && this.getFlag(0));
    }

    public boolean isPassenger() {
        return this.getVehicle() != null;
    }

    public boolean isVehicle() {
        return !this.getPassengers().isEmpty();
    }

    public boolean bs() {
        return true;
    }

    public void setSneaking(boolean flag) {
        this.setFlag(1, flag);
    }

    public boolean isSneaking() {
        return this.getFlag(1);
    }

    public boolean bu() {
        return this.isSneaking();
    }

    public boolean bv() {
        return this.isSneaking();
    }

    public boolean bw() {
        return this.isSneaking();
    }

    public boolean bx() {
        return this.isSneaking();
    }

    public boolean by() {
        return this.getPose() == EntityPose.CROUCHING;
    }

    public boolean isSprinting() {
        return this.getFlag(3);
    }

    public void setSprinting(boolean flag) {
        this.setFlag(3, flag);
    }

    public boolean isSwimming() {
        return this.getFlag(4);
    }

    public boolean bB() {
        return this.getPose() == EntityPose.SWIMMING;
    }

    public void setSwimming(boolean flag) {
        this.setFlag(4, flag);
    }

    public boolean bD() {
        return this.glowing || this.world.isClientSide && this.getFlag(6);
    }

    public void i(boolean flag) {
        this.glowing = flag;
        if (!this.world.isClientSide) {
            this.setFlag(6, this.glowing);
        }

    }

    public boolean isInvisible() {
        return this.getFlag(5);
    }

    @Nullable
    public ScoreboardTeamBase getScoreboardTeam() {
        return this.world.getScoreboard().getPlayerTeam(this.getName());
    }

    public boolean r(Entity entity) {
        return this.a(entity.getScoreboardTeam());
    }

    public boolean a(ScoreboardTeamBase scoreboardteambase) {
        return this.getScoreboardTeam() != null ? this.getScoreboardTeam().isAlly(scoreboardteambase) : false;
    }

    public void setInvisible(boolean flag) {
        this.setFlag(5, flag);
    }

    public boolean getFlag(int i) {
        return ((Byte) this.datawatcher.get(Entity.S) & 1 << i) != 0;
    }

    public void setFlag(int i, boolean flag) {
        byte b0 = (Byte) this.datawatcher.get(Entity.S);

        if (flag) {
            this.datawatcher.set(Entity.S, (byte) (b0 | 1 << i));
        } else {
            this.datawatcher.set(Entity.S, (byte) (b0 & ~(1 << i)));
        }

    }

    public int bG() {
        return 300;
    }

    public int getAirTicks() {
        return (Integer) this.datawatcher.get(Entity.AIR_TICKS);
    }

    public void setAirTicks(int i) {
        this.datawatcher.set(Entity.AIR_TICKS, i);
    }

    public void onLightningStrike(WorldServer worldserver, EntityLightning entitylightning) {
        this.setFireTicks(this.fireTicks + 1);
        if (this.fireTicks == 0) {
            this.setOnFire(8);
        }

        this.damageEntity(DamageSource.LIGHTNING, 5.0F);
    }

    public void k(boolean flag) {
        Vec3D vec3d = this.getMot();
        double d0;

        if (flag) {
            d0 = Math.max(-0.9D, vec3d.y - 0.03D);
        } else {
            d0 = Math.min(1.8D, vec3d.y + 0.1D);
        }

        this.setMot(vec3d.x, d0, vec3d.z);
    }

    public void l(boolean flag) {
        Vec3D vec3d = this.getMot();
        double d0;

        if (flag) {
            d0 = Math.max(-0.3D, vec3d.y - 0.03D);
        } else {
            d0 = Math.min(0.7D, vec3d.y + 0.06D);
        }

        this.setMot(vec3d.x, d0, vec3d.z);
        this.fallDistance = 0.0F;
    }

    public void a(WorldServer worldserver, EntityLiving entityliving) {}

    protected void l(double d0, double d1, double d2) {
        BlockPosition blockposition = new BlockPosition(d0, d1, d2);
        Vec3D vec3d = new Vec3D(d0 - (double) blockposition.getX(), d1 - (double) blockposition.getY(), d2 - (double) blockposition.getZ());
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
        EnumDirection enumdirection = EnumDirection.UP;
        double d3 = Double.MAX_VALUE;
        EnumDirection[] aenumdirection = new EnumDirection[]{EnumDirection.NORTH, EnumDirection.SOUTH, EnumDirection.WEST, EnumDirection.EAST, EnumDirection.UP};
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumDirection enumdirection1 = aenumdirection[j];

            blockposition_mutableblockposition.a((BaseBlockPosition) blockposition, enumdirection1);
            if (!this.world.getType(blockposition_mutableblockposition).r(this.world, blockposition_mutableblockposition)) {
                double d4 = vec3d.a(enumdirection1.n());
                double d5 = enumdirection1.e() == EnumDirection.EnumAxisDirection.POSITIVE ? 1.0D - d4 : d4;

                if (d5 < d3) {
                    d3 = d5;
                    enumdirection = enumdirection1;
                }
            }
        }

        float f = this.random.nextFloat() * 0.2F + 0.1F;
        float f1 = (float) enumdirection.e().a();
        Vec3D vec3d1 = this.getMot().a(0.75D);

        if (enumdirection.n() == EnumDirection.EnumAxis.X) {
            this.setMot((double) (f1 * f), vec3d1.y, vec3d1.z);
        } else if (enumdirection.n() == EnumDirection.EnumAxis.Y) {
            this.setMot(vec3d1.x, (double) (f1 * f), vec3d1.z);
        } else if (enumdirection.n() == EnumDirection.EnumAxis.Z) {
            this.setMot(vec3d1.x, vec3d1.y, (double) (f1 * f));
        }

    }

    public void a(IBlockData iblockdata, Vec3D vec3d) {
        this.fallDistance = 0.0F;
        this.x = vec3d;
    }

    private static IChatBaseComponent b(IChatBaseComponent ichatbasecomponent) {
        IChatMutableComponent ichatmutablecomponent = ichatbasecomponent.g().setChatModifier(ichatbasecomponent.getChatModifier().setChatClickable((ChatClickable) null));
        Iterator iterator = ichatbasecomponent.getSiblings().iterator();

        while (iterator.hasNext()) {
            IChatBaseComponent ichatbasecomponent1 = (IChatBaseComponent) iterator.next();

            ichatmutablecomponent.addSibling(b(ichatbasecomponent1));
        }

        return ichatmutablecomponent;
    }

    @Override
    public IChatBaseComponent getDisplayName() {
        IChatBaseComponent ichatbasecomponent = this.getCustomName();

        return ichatbasecomponent != null ? b(ichatbasecomponent) : this.bI();
    }

    protected IChatBaseComponent bI() {
        return this.f.g();
    }

    public boolean s(Entity entity) {
        return this == entity;
    }

    public float getHeadRotation() {
        return 0.0F;
    }

    public void setHeadRotation(float f) {}

    public void n(float f) {}

    public boolean bK() {
        return true;
    }

    public boolean t(Entity entity) {
        return false;
    }

    public String toString() {
        return String.format(Locale.ROOT, "%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]", this.getClass().getSimpleName(), this.getDisplayName().getString(), this.id, this.world == null ? "~NULL~" : this.world.toString(), this.locX(), this.locY(), this.locZ());
    }

    public boolean isInvulnerable(DamageSource damagesource) {
        return this.invulnerable && damagesource != DamageSource.OUT_OF_WORLD && !damagesource.v();
    }

    public boolean isInvulnerable() {
        return this.invulnerable;
    }

    public void setInvulnerable(boolean flag) {
        this.invulnerable = flag;
    }

    public void u(Entity entity) {
        this.setPositionRotation(entity.locX(), entity.locY(), entity.locZ(), entity.yaw, entity.pitch);
    }

    public void v(Entity entity) {
        NBTTagCompound nbttagcompound = entity.save(new NBTTagCompound());

        nbttagcompound.remove("Dimension");
        this.load(nbttagcompound);
        this.portalCooldown = entity.portalCooldown;
        this.ac = entity.ac;
    }

    @Nullable
    public Entity b(WorldServer worldserver) {
        if (this.world instanceof WorldServer && !this.dead) {
            this.world.getMethodProfiler().enter("changeDimension");
            this.decouple();
            this.world.getMethodProfiler().enter("reposition");
            ShapeDetectorShape shapedetectorshape = this.a(worldserver);

            if (shapedetectorshape == null) {
                return null;
            } else {
                this.world.getMethodProfiler().exitEnter("reloading");
                Entity entity = this.getEntityType().a((World) worldserver);

                if (entity != null) {
                    entity.v(this);
                    entity.setPositionRotation(shapedetectorshape.position.x, shapedetectorshape.position.y, shapedetectorshape.position.z, shapedetectorshape.yaw, entity.pitch);
                    entity.setMot(shapedetectorshape.velocity);
                    worldserver.addEntityTeleport(entity);
                    if (worldserver.getDimensionKey() == World.THE_END) {
                        WorldServer.a(worldserver);
                    }
                }

                this.bM();
                this.world.getMethodProfiler().exit();
                ((WorldServer) this.world).resetEmptyTime();
                worldserver.resetEmptyTime();
                this.world.getMethodProfiler().exit();
                return entity;
            }
        } else {
            return null;
        }
    }

    protected void bM() {
        this.dead = true;
    }

    @Nullable
    protected ShapeDetectorShape a(WorldServer worldserver) {
        boolean flag = this.world.getDimensionKey() == World.THE_END && worldserver.getDimensionKey() == World.OVERWORLD;
        boolean flag1 = worldserver.getDimensionKey() == World.THE_END;

        if (!flag && !flag1) {
            boolean flag2 = worldserver.getDimensionKey() == World.THE_NETHER;

            if (this.world.getDimensionKey() != World.THE_NETHER && !flag2) {
                return null;
            } else {
                WorldBorder worldborder = worldserver.getWorldBorder();
                double d0 = Math.max(-2.9999872E7D, worldborder.e() + 16.0D);
                double d1 = Math.max(-2.9999872E7D, worldborder.f() + 16.0D);
                double d2 = Math.min(2.9999872E7D, worldborder.g() - 16.0D);
                double d3 = Math.min(2.9999872E7D, worldborder.h() - 16.0D);
                double d4 = DimensionManager.a(this.world.getDimensionManager(), worldserver.getDimensionManager());
                BlockPosition blockposition = new BlockPosition(MathHelper.a(this.locX() * d4, d0, d2), this.locY(), MathHelper.a(this.locZ() * d4, d1, d3));

                return (ShapeDetectorShape) this.a(worldserver, blockposition, flag2).map((blockutil_rectangle) -> {
                    IBlockData iblockdata = this.world.getType(this.ac);
                    EnumDirection.EnumAxis enumdirection_enumaxis;
                    Vec3D vec3d;

                    if (iblockdata.b(BlockProperties.E)) {
                        enumdirection_enumaxis = (EnumDirection.EnumAxis) iblockdata.get(BlockProperties.E);
                        BlockUtil.Rectangle blockutil_rectangle1 = BlockUtil.a(this.ac, enumdirection_enumaxis, 21, EnumDirection.EnumAxis.Y, 21, (blockposition1) -> {
                            return this.world.getType(blockposition1) == iblockdata;
                        });

                        vec3d = this.a(enumdirection_enumaxis, blockutil_rectangle1);
                    } else {
                        enumdirection_enumaxis = EnumDirection.EnumAxis.X;
                        vec3d = new Vec3D(0.5D, 0.0D, 0.0D);
                    }

                    return BlockPortalShape.a(worldserver, blockutil_rectangle, enumdirection_enumaxis, vec3d, this.a(this.getPose()), this.getMot(), this.yaw, this.pitch);
                }).orElse((Object) null);
            }
        } else {
            BlockPosition blockposition1;

            if (flag1) {
                blockposition1 = WorldServer.a;
            } else {
                blockposition1 = worldserver.getHighestBlockYAt(HeightMap.Type.MOTION_BLOCKING_NO_LEAVES, worldserver.getSpawn());
            }

            return new ShapeDetectorShape(new Vec3D((double) blockposition1.getX() + 0.5D, (double) blockposition1.getY(), (double) blockposition1.getZ() + 0.5D), this.getMot(), this.yaw, this.pitch);
        }
    }

    protected Vec3D a(EnumDirection.EnumAxis enumdirection_enumaxis, BlockUtil.Rectangle blockutil_rectangle) {
        return BlockPortalShape.a(blockutil_rectangle, enumdirection_enumaxis, this.getPositionVector(), this.a(this.getPose()));
    }

    protected Optional<BlockUtil.Rectangle> a(WorldServer worldserver, BlockPosition blockposition, boolean flag) {
        return worldserver.getTravelAgent().findPortal(blockposition, flag);
    }

    public boolean canPortal() {
        return true;
    }

    public float a(Explosion explosion, IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, Fluid fluid, float f) {
        return f;
    }

    public boolean a(Explosion explosion, IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, float f) {
        return true;
    }

    public int bO() {
        return 3;
    }

    public boolean isIgnoreBlockTrigger() {
        return false;
    }

    public void appendEntityCrashDetails(CrashReportSystemDetails crashreportsystemdetails) {
        crashreportsystemdetails.a("Entity Type", () -> {
            return EntityTypes.getName(this.getEntityType()) + " (" + this.getClass().getCanonicalName() + ")";
        });
        crashreportsystemdetails.a("Entity ID", (Object) this.id);
        crashreportsystemdetails.a("Entity Name", () -> {
            return this.getDisplayName().getString();
        });
        crashreportsystemdetails.a("Entity's Exact location", (Object) String.format(Locale.ROOT, "%.2f, %.2f, %.2f", this.locX(), this.locY(), this.locZ()));
        crashreportsystemdetails.a("Entity's Block location", (Object) CrashReportSystemDetails.a(MathHelper.floor(this.locX()), MathHelper.floor(this.locY()), MathHelper.floor(this.locZ())));
        Vec3D vec3d = this.getMot();

        crashreportsystemdetails.a("Entity's Momentum", (Object) String.format(Locale.ROOT, "%.2f, %.2f, %.2f", vec3d.x, vec3d.y, vec3d.z));
        crashreportsystemdetails.a("Entity's Passengers", () -> {
            return this.getPassengers().toString();
        });
        crashreportsystemdetails.a("Entity's Vehicle", () -> {
            return this.getVehicle().toString();
        });
    }

    public void a_(UUID uuid) {
        this.uniqueID = uuid;
        this.ae = this.uniqueID.toString();
    }

    public UUID getUniqueID() {
        return this.uniqueID;
    }

    public String getUniqueIDString() {
        return this.ae;
    }

    public String getName() {
        return this.ae;
    }

    public boolean bU() {
        return true;
    }

    @Override
    public IChatBaseComponent getScoreboardDisplayName() {
        return ScoreboardTeam.a(this.getScoreboardTeam(), this.getDisplayName()).format((chatmodifier) -> {
            return chatmodifier.setChatHoverable(this.ca()).setInsertion(this.getUniqueIDString());
        });
    }

    public void setCustomName(@Nullable IChatBaseComponent ichatbasecomponent) {
        this.datawatcher.set(Entity.aq, Optional.ofNullable(ichatbasecomponent));
    }

    @Nullable
    @Override
    public IChatBaseComponent getCustomName() {
        return (IChatBaseComponent) ((Optional) this.datawatcher.get(Entity.aq)).orElse((Object) null);
    }

    @Override
    public boolean hasCustomName() {
        return ((Optional) this.datawatcher.get(Entity.aq)).isPresent();
    }

    public void setCustomNameVisible(boolean flag) {
        this.datawatcher.set(Entity.ar, flag);
    }

    public boolean getCustomNameVisible() {
        return (Boolean) this.datawatcher.get(Entity.ar);
    }

    public final void enderTeleportAndLoad(double d0, double d1, double d2) {
        if (this.world instanceof WorldServer) {
            ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(new BlockPosition(d0, d1, d2));

            ((WorldServer) this.world).getChunkProvider().addTicket(TicketType.POST_TELEPORT, chunkcoordintpair, 0, this.getId());
            this.world.getChunkAt(chunkcoordintpair.x, chunkcoordintpair.z);
            this.enderTeleportTo(d0, d1, d2);
        }
    }

    public void enderTeleportTo(double d0, double d1, double d2) {
        if (this.world instanceof WorldServer) {
            WorldServer worldserver = (WorldServer) this.world;

            this.setPositionRotation(d0, d1, d2, this.yaw, this.pitch);
            this.co().forEach((entity) -> {
                worldserver.chunkCheck(entity);
                entity.az = true;
                Iterator iterator = entity.passengers.iterator();

                while (iterator.hasNext()) {
                    Entity entity1 = (Entity) iterator.next();

                    entity.a(entity1, Entity::teleportAndSync);
                }

            });
        }
    }

    public void a(DataWatcherObject<?> datawatcherobject) {
        if (Entity.POSE.equals(datawatcherobject)) {
            this.updateSize();
        }

    }

    public void updateSize() {
        EntitySize entitysize = this.size;
        EntityPose entitypose = this.getPose();
        EntitySize entitysize1 = this.a(entitypose);

        this.size = entitysize1;
        this.headHeight = this.getHeadHeight(entitypose, entitysize1);
        if (entitysize1.width < entitysize.width) {
            double d0 = (double) entitysize1.width / 2.0D;

            this.a(new AxisAlignedBB(this.locX() - d0, this.locY(), this.locZ() - d0, this.locX() + d0, this.locY() + (double) entitysize1.height, this.locZ() + d0));
        } else {
            AxisAlignedBB axisalignedbb = this.getBoundingBox();

            this.a(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double) entitysize1.width, axisalignedbb.minY + (double) entitysize1.height, axisalignedbb.minZ + (double) entitysize1.width));
            if (entitysize1.width > entitysize.width && !this.justCreated && !this.world.isClientSide) {
                float f = entitysize.width - entitysize1.width;

                this.move(EnumMoveType.SELF, new Vec3D((double) f, 0.0D, (double) f));
            }

        }
    }

    public EnumDirection getDirection() {
        return EnumDirection.fromAngle((double) this.yaw);
    }

    public EnumDirection getAdjustedDirection() {
        return this.getDirection();
    }

    protected ChatHoverable ca() {
        return new ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_ENTITY, new ChatHoverable.b(this.getEntityType(), this.getUniqueID(), this.getDisplayName()));
    }

    public boolean a(EntityPlayer entityplayer) {
        return true;
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    protected AxisAlignedBB d(EntityPose entitypose) {
        EntitySize entitysize = this.a(entitypose);
        float f = entitysize.width / 2.0F;
        Vec3D vec3d = new Vec3D(this.locX() - (double) f, this.locY(), this.locZ() - (double) f);
        Vec3D vec3d1 = new Vec3D(this.locX() + (double) f, this.locY() + (double) entitysize.height, this.locZ() + (double) f);

        return new AxisAlignedBB(vec3d, vec3d1);
    }

    public void a(AxisAlignedBB axisalignedbb) {
        this.boundingBox = axisalignedbb;
    }

    protected float getHeadHeight(EntityPose entitypose, EntitySize entitysize) {
        return entitysize.height * 0.85F;
    }

    public final float getHeadHeight() {
        return this.headHeight;
    }

    public boolean a_(int i, ItemStack itemstack) {
        return false;
    }

    @Override
    public void sendMessage(IChatBaseComponent ichatbasecomponent, UUID uuid) {}

    public World getWorld() {
        return this.world;
    }

    @Nullable
    public MinecraftServer getMinecraftServer() {
        return this.world.getMinecraftServer();
    }

    public EnumInteractionResult a(EntityHuman entityhuman, Vec3D vec3d, EnumHand enumhand) {
        return EnumInteractionResult.PASS;
    }

    public boolean ch() {
        return false;
    }

    public void a(EntityLiving entityliving, Entity entity) {
        if (entity instanceof EntityLiving) {
            EnchantmentManager.a((EntityLiving) entity, (Entity) entityliving);
        }

        EnchantmentManager.b(entityliving, entity);
    }

    public void b(EntityPlayer entityplayer) {}

    public void c(EntityPlayer entityplayer) {}

    public float a(EnumBlockRotation enumblockrotation) {
        float f = MathHelper.g(this.yaw);

        switch (enumblockrotation) {
            case CLOCKWISE_180:
                return f + 180.0F;
            case COUNTERCLOCKWISE_90:
                return f + 270.0F;
            case CLOCKWISE_90:
                return f + 90.0F;
            default:
                return f;
        }
    }

    public float a(EnumBlockMirror enumblockmirror) {
        float f = MathHelper.g(this.yaw);

        switch (enumblockmirror) {
            case LEFT_RIGHT:
                return -f;
            case FRONT_BACK:
                return 180.0F - f;
            default:
                return f;
        }
    }

    public boolean ci() {
        return false;
    }

    public boolean cj() {
        boolean flag = this.az;

        this.az = false;
        return flag;
    }

    public boolean ck() {
        boolean flag = this.au;

        this.au = false;
        return flag;
    }

    @Nullable
    public Entity getRidingPassenger() {
        return null;
    }

    public List<Entity> getPassengers() {
        return (List) (this.passengers.isEmpty() ? Collections.emptyList() : Lists.newArrayList(this.passengers));
    }

    public boolean w(Entity entity) {
        Iterator iterator = this.getPassengers().iterator();

        Entity entity1;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            entity1 = (Entity) iterator.next();
        } while (!entity1.equals(entity));

        return true;
    }

    public boolean a(Class<? extends Entity> oclass) {
        Iterator iterator = this.getPassengers().iterator();

        Entity entity;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            entity = (Entity) iterator.next();
        } while (!oclass.isAssignableFrom(entity.getClass()));

        return true;
    }

    public Collection<Entity> getAllPassengers() {
        Set<Entity> set = Sets.newHashSet();
        Iterator iterator = this.getPassengers().iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            set.add(entity);
            entity.a(false, set);
        }

        return set;
    }

    public Stream<Entity> co() {
        return Stream.concat(Stream.of(this), this.passengers.stream().flatMap(Entity::co));
    }

    public boolean hasSinglePlayerPassenger() {
        Set<Entity> set = Sets.newHashSet();

        this.a(true, set);
        return set.size() == 1;
    }

    private void a(boolean flag, Set<Entity> set) {
        Entity entity;

        for (Iterator iterator = this.getPassengers().iterator(); iterator.hasNext(); entity.a(flag, set)) {
            entity = (Entity) iterator.next();
            if (!flag || EntityPlayer.class.isAssignableFrom(entity.getClass())) {
                set.add(entity);
            }
        }

    }

    public Entity getRootVehicle() {
        Entity entity;

        for (entity = this; entity.isPassenger(); entity = entity.getVehicle()) {
            ;
        }

        return entity;
    }

    public boolean isSameVehicle(Entity entity) {
        return this.getRootVehicle() == entity.getRootVehicle();
    }

    public boolean cr() {
        Entity entity = this.getRidingPassenger();

        return entity instanceof EntityHuman ? ((EntityHuman) entity).ey() : !this.world.isClientSide;
    }

    protected static Vec3D a(double d0, double d1, float f) {
        double d2 = (d0 + d1 + 9.999999747378752E-6D) / 2.0D;
        float f1 = -MathHelper.sin(f * 0.017453292F);
        float f2 = MathHelper.cos(f * 0.017453292F);
        float f3 = Math.max(Math.abs(f1), Math.abs(f2));

        return new Vec3D((double) f1 * d2 / (double) f3, 0.0D, (double) f2 * d2 / (double) f3);
    }

    public Vec3D b(EntityLiving entityliving) {
        return new Vec3D(this.locX(), this.getBoundingBox().maxY, this.locZ());
    }

    @Nullable
    public Entity getVehicle() {
        return this.vehicle;
    }

    public EnumPistonReaction getPushReaction() {
        return EnumPistonReaction.NORMAL;
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
    }

    public int getMaxFireTicks() {
        return 1;
    }

    public CommandListenerWrapper getCommandListener() {
        return new CommandListenerWrapper(this, this.getPositionVector(), this.bh(), this.world instanceof WorldServer ? (WorldServer) this.world : null, this.y(), this.getDisplayName().getString(), this.getScoreboardDisplayName(), this.world.getMinecraftServer(), this);
    }

    protected int y() {
        return 0;
    }

    public boolean k(int i) {
        return this.y() >= i;
    }

    @Override
    public boolean shouldSendSuccess() {
        return this.world.getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK);
    }

    @Override
    public boolean shouldSendFailure() {
        return true;
    }

    @Override
    public boolean shouldBroadcastCommands() {
        return true;
    }

    public void a(ArgumentAnchor.Anchor argumentanchor_anchor, Vec3D vec3d) {
        Vec3D vec3d1 = argumentanchor_anchor.a(this);
        double d0 = vec3d.x - vec3d1.x;
        double d1 = vec3d.y - vec3d1.y;
        double d2 = vec3d.z - vec3d1.z;
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);

        this.pitch = MathHelper.g((float) (-(MathHelper.d(d1, d3) * 57.2957763671875D)));
        this.yaw = MathHelper.g((float) (MathHelper.d(d2, d0) * 57.2957763671875D) - 90.0F);
        this.setHeadRotation(this.yaw);
        this.lastPitch = this.pitch;
        this.lastYaw = this.yaw;
    }

    public boolean a(Tag<FluidType> tag, double d0) {
        AxisAlignedBB axisalignedbb = this.getBoundingBox().shrink(0.001D);
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.f(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.minY);
        int l = MathHelper.f(axisalignedbb.maxY);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.f(axisalignedbb.maxZ);

        if (!this.world.isAreaLoaded(i, k, i1, j, l, j1)) {
            return false;
        } else {
            double d1 = 0.0D;
            boolean flag = this.bU();
            boolean flag1 = false;
            Vec3D vec3d = Vec3D.ORIGIN;
            int k1 = 0;
            BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();

            for (int l1 = i; l1 < j; ++l1) {
                for (int i2 = k; i2 < l; ++i2) {
                    for (int j2 = i1; j2 < j1; ++j2) {
                        blockposition_mutableblockposition.d(l1, i2, j2);
                        Fluid fluid = this.world.getFluid(blockposition_mutableblockposition);

                        if (fluid.a(tag)) {
                            double d2 = (double) ((float) i2 + fluid.getHeight(this.world, blockposition_mutableblockposition));

                            if (d2 >= axisalignedbb.minY) {
                                flag1 = true;
                                d1 = Math.max(d2 - axisalignedbb.minY, d1);
                                if (flag) {
                                    Vec3D vec3d1 = fluid.c(this.world, blockposition_mutableblockposition);

                                    if (d1 < 0.4D) {
                                        vec3d1 = vec3d1.a(d1);
                                    }

                                    vec3d = vec3d.e(vec3d1);
                                    ++k1;
                                }
                            }
                        }
                    }
                }
            }

            if (vec3d.f() > 0.0D) {
                if (k1 > 0) {
                    vec3d = vec3d.a(1.0D / (double) k1);
                }

                if (!(this instanceof EntityHuman)) {
                    vec3d = vec3d.d();
                }

                Vec3D vec3d2 = this.getMot();

                vec3d = vec3d.a(d0 * 1.0D);
                double d3 = 0.003D;

                if (Math.abs(vec3d2.x) < 0.003D && Math.abs(vec3d2.z) < 0.003D && vec3d.f() < 0.0045000000000000005D) {
                    vec3d = vec3d.d().a(0.0045000000000000005D);
                }

                this.setMot(this.getMot().e(vec3d));
            }

            this.M.put(tag, d1);
            return flag1;
        }
    }

    public double b(Tag<FluidType> tag) {
        return this.M.getDouble(tag);
    }

    public double cw() {
        return (double) this.getHeadHeight() < 0.4D ? 0.0D : 0.4D;
    }

    public final float getWidth() {
        return this.size.width;
    }

    public final float getHeight() {
        return this.size.height;
    }

    public abstract Packet<?> P();

    public EntitySize a(EntityPose entitypose) {
        return this.f.l();
    }

    public Vec3D getPositionVector() {
        return this.loc;
    }

    public BlockPosition getChunkCoordinates() {
        return this.locBlock;
    }

    public Vec3D getMot() {
        return this.mot;
    }

    public void setMot(Vec3D vec3d) {
        this.mot = vec3d;
    }

    public void setMot(double d0, double d1, double d2) {
        this.setMot(new Vec3D(d0, d1, d2));
    }

    public final double locX() {
        return this.loc.x;
    }

    public double c(double d0) {
        return this.loc.x + (double) this.getWidth() * d0;
    }

    public double d(double d0) {
        return this.c((2.0D * this.random.nextDouble() - 1.0D) * d0);
    }

    public final double locY() {
        return this.loc.y;
    }

    public double e(double d0) {
        return this.loc.y + (double) this.getHeight() * d0;
    }

    public double cE() {
        return this.e(this.random.nextDouble());
    }

    public double getHeadY() {
        return this.loc.y + (double) this.headHeight;
    }

    public final double locZ() {
        return this.loc.z;
    }

    public double f(double d0) {
        return this.loc.z + (double) this.getWidth() * d0;
    }

    public double g(double d0) {
        return this.f((2.0D * this.random.nextDouble() - 1.0D) * d0);
    }

    public void setPositionRaw(double d0, double d1, double d2) {
        if (this.loc.x != d0 || this.loc.y != d1 || this.loc.z != d2) {
            this.loc = new Vec3D(d0, d1, d2);
            int i = MathHelper.floor(d0);
            int j = MathHelper.floor(d1);
            int k = MathHelper.floor(d2);

            if (i != this.locBlock.getX() || j != this.locBlock.getY() || k != this.locBlock.getZ()) {
                this.locBlock = new BlockPosition(i, j, k);
            }

            this.au = true;
        }

    }

    public void checkDespawn() {}

    @FunctionalInterface
    public interface a {

        void accept(Entity entity, double d0, double d1, double d2);
    }
}
