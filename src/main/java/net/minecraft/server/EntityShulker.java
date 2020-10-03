package net.minecraft.server;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.event.entity.EntityTeleportEvent;
// CraftBukkit end

public class EntityShulker extends EntityGolem implements IMonster {

    private static final UUID bp = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F");
    private static final AttributeModifier bq = new AttributeModifier(EntityShulker.bp, "Covered armor bonus", 20.0D, AttributeModifier.Operation.ADDITION);
    protected static final DataWatcherObject<EnumDirection> b = DataWatcher.a(EntityShulker.class, DataWatcherRegistry.n);
    protected static final DataWatcherObject<Optional<BlockPosition>> c = DataWatcher.a(EntityShulker.class, DataWatcherRegistry.m);
    protected static final DataWatcherObject<Byte> d = DataWatcher.a(EntityShulker.class, DataWatcherRegistry.a);
    public static final DataWatcherObject<Byte> COLOR = DataWatcher.a(EntityShulker.class, DataWatcherRegistry.a);
    private float br;
    private float bs;
    private BlockPosition bt = null;
    private int bu;

    public EntityShulker(EntityTypes<? extends EntityShulker> entitytypes, World world) {
        super(entitytypes, world);
        this.f = 5;
    }

    @Override
    protected void initPathfinder() {
        this.goalSelector.a(1, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(4, new EntityShulker.a());
        this.goalSelector.a(7, new EntityShulker.e());
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, (new PathfinderGoalHurtByTarget(this, new Class[0])).a(new Class[0])); // CraftBukkit - decompile error
        this.targetSelector.a(2, new EntityShulker.d(this));
        this.targetSelector.a(3, new EntityShulker.c(this));
    }

    @Override
    protected boolean playStepSound() {
        return false;
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_SHULKER_AMBIENT;
    }

    @Override
    public void F() {
        if (!this.eT()) {
            super.F();
        }

    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_SHULKER_DEATH;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return this.eT() ? SoundEffects.ENTITY_SHULKER_HURT_CLOSED : SoundEffects.ENTITY_SHULKER_HURT;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityShulker.b, EnumDirection.DOWN);
        this.datawatcher.register(EntityShulker.c, Optional.empty());
        this.datawatcher.register(EntityShulker.d, (byte) 0);
        this.datawatcher.register(EntityShulker.COLOR, (byte) 16);
    }

    public static AttributeProvider.Builder m() {
        return EntityInsentient.p().a(GenericAttributes.MAX_HEALTH, 30.0D);
    }

    @Override
    protected EntityAIBodyControl r() {
        return new EntityShulker.b(this);
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.datawatcher.set(EntityShulker.b, EnumDirection.fromType1(nbttagcompound.getByte("AttachFace")));
        this.datawatcher.set(EntityShulker.d, nbttagcompound.getByte("Peek"));
        this.datawatcher.set(EntityShulker.COLOR, nbttagcompound.getByte("Color"));
        if (nbttagcompound.hasKey("APX")) {
            int i = nbttagcompound.getInt("APX");
            int j = nbttagcompound.getInt("APY");
            int k = nbttagcompound.getInt("APZ");

            this.datawatcher.set(EntityShulker.c, Optional.of(new BlockPosition(i, j, k)));
        } else {
            this.datawatcher.set(EntityShulker.c, Optional.empty());
        }

    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setByte("AttachFace", (byte) ((EnumDirection) this.datawatcher.get(EntityShulker.b)).c());
        nbttagcompound.setByte("Peek", (Byte) this.datawatcher.get(EntityShulker.d));
        nbttagcompound.setByte("Color", (Byte) this.datawatcher.get(EntityShulker.COLOR));
        BlockPosition blockposition = this.eM();

        if (blockposition != null) {
            nbttagcompound.setInt("APX", blockposition.getX());
            nbttagcompound.setInt("APY", blockposition.getY());
            nbttagcompound.setInt("APZ", blockposition.getZ());
        }

    }

    @Override
    public void tick() {
        super.tick();
        BlockPosition blockposition = (BlockPosition) ((Optional) this.datawatcher.get(EntityShulker.c)).orElse((Object) null);

        if (blockposition == null && !this.world.isClientSide) {
            blockposition = this.getChunkCoordinates();
            this.datawatcher.set(EntityShulker.c, Optional.of(blockposition));
        }

        float f;

        if (this.isPassenger()) {
            blockposition = null;
            f = this.getVehicle().yaw;
            this.yaw = f;
            this.aA = f;
            this.aB = f;
            this.bu = 0;
        } else if (!this.world.isClientSide) {
            IBlockData iblockdata = this.world.getType(blockposition);
            EnumDirection enumdirection;

            if (!iblockdata.isAir()) {
                if (iblockdata.a(Blocks.MOVING_PISTON)) {
                    enumdirection = (EnumDirection) iblockdata.get(BlockPiston.FACING);
                    if (this.world.isEmpty(blockposition.shift(enumdirection))) {
                        blockposition = blockposition.shift(enumdirection);
                        this.datawatcher.set(EntityShulker.c, Optional.of(blockposition));
                    } else {
                        this.eK();
                    }
                } else if (iblockdata.a(Blocks.PISTON_HEAD)) {
                    enumdirection = (EnumDirection) iblockdata.get(BlockPistonExtension.FACING);
                    if (this.world.isEmpty(blockposition.shift(enumdirection))) {
                        blockposition = blockposition.shift(enumdirection);
                        this.datawatcher.set(EntityShulker.c, Optional.of(blockposition));
                    } else {
                        this.eK();
                    }
                } else {
                    this.eK();
                }
            }

            enumdirection = this.eL();
            if (!this.a(blockposition, enumdirection)) {
                EnumDirection enumdirection1 = this.g(blockposition);

                if (enumdirection1 != null) {
                    this.datawatcher.set(EntityShulker.b, enumdirection1);
                } else {
                    this.eK();
                }
            }
        }

        f = (float) this.eN() * 0.01F;
        this.br = this.bs;
        if (this.bs > f) {
            this.bs = MathHelper.a(this.bs - 0.05F, f, 1.0F);
        } else if (this.bs < f) {
            this.bs = MathHelper.a(this.bs + 0.05F, 0.0F, f);
        }

        if (blockposition != null) {
            if (this.world.isClientSide) {
                if (this.bu > 0 && this.bt != null) {
                    --this.bu;
                } else {
                    this.bt = blockposition;
                }
            }

            this.g((double) blockposition.getX() + 0.5D, (double) blockposition.getY(), (double) blockposition.getZ() + 0.5D);
            double d0 = 0.5D - (double) MathHelper.sin((0.5F + this.bs) * 3.1415927F) * 0.5D;
            double d1 = 0.5D - (double) MathHelper.sin((0.5F + this.br) * 3.1415927F) * 0.5D;
            EnumDirection enumdirection2 = this.eL().opposite();

            this.a((new AxisAlignedBB(this.locX() - 0.5D, this.locY(), this.locZ() - 0.5D, this.locX() + 0.5D, this.locY() + 1.0D, this.locZ() + 0.5D)).b((double) enumdirection2.getAdjacentX() * d0, (double) enumdirection2.getAdjacentY() * d0, (double) enumdirection2.getAdjacentZ() * d0));
            double d2 = d0 - d1;

            if (d2 > 0.0D) {
                List<Entity> list = this.world.getEntities(this, this.getBoundingBox());

                if (!list.isEmpty()) {
                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        Entity entity = (Entity) iterator.next();

                        if (!(entity instanceof EntityShulker) && !entity.noclip) {
                            entity.move(EnumMoveType.SHULKER, new Vec3D(d2 * (double) enumdirection2.getAdjacentX(), d2 * (double) enumdirection2.getAdjacentY(), d2 * (double) enumdirection2.getAdjacentZ()));
                        }
                    }
                }
            }
        }

    }

    @Override
    public void move(EnumMoveType enummovetype, Vec3D vec3d) {
        if (enummovetype == EnumMoveType.SHULKER_BOX) {
            this.eK();
        } else {
            super.move(enummovetype, vec3d);
        }

    }

    @Override
    public void setPosition(double d0, double d1, double d2) {
        super.setPosition(d0, d1, d2);
        if (this.datawatcher != null && this.ticksLived != 0) {
            Optional<BlockPosition> optional = (Optional) this.datawatcher.get(EntityShulker.c);
            Optional<BlockPosition> optional1 = Optional.of(new BlockPosition(d0, d1, d2));

            if (!optional1.equals(optional)) {
                this.datawatcher.set(EntityShulker.c, optional1);
                this.datawatcher.set(EntityShulker.d, (byte) 0);
                this.impulse = true;
            }

        }
    }

    @Nullable
    protected EnumDirection g(BlockPosition blockposition) {
        EnumDirection[] aenumdirection = EnumDirection.values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumDirection enumdirection = aenumdirection[j];

            if (this.a(blockposition, enumdirection)) {
                return enumdirection;
            }
        }

        return null;
    }

    private boolean a(BlockPosition blockposition, EnumDirection enumdirection) {
        return this.world.a(blockposition.shift(enumdirection), (Entity) this, enumdirection.opposite()) && this.world.getCubes(this, ShulkerUtil.a(blockposition, enumdirection.opposite()));
    }

    protected boolean eK() {
        if (!this.isNoAI() && this.isAlive()) {
            BlockPosition blockposition = this.getChunkCoordinates();

            for (int i = 0; i < 5; ++i) {
                BlockPosition blockposition1 = blockposition.b(8 - this.random.nextInt(17), 8 - this.random.nextInt(17), 8 - this.random.nextInt(17));

                if (blockposition1.getY() > 0 && this.world.isEmpty(blockposition1) && this.world.getWorldBorder().a(blockposition1) && this.world.getCubes(this, new AxisAlignedBB(blockposition1))) {
                    EnumDirection enumdirection = this.g(blockposition1);

                    if (enumdirection != null) {
                        // CraftBukkit start
                        EntityTeleportEvent teleport = new EntityTeleportEvent(this.getBukkitEntity(), this.getBukkitEntity().getLocation(), new Location(this.world.getWorld(), blockposition1.getX(), blockposition1.getY(), blockposition1.getZ()));
                        this.world.getServer().getPluginManager().callEvent(teleport);
                        if (!teleport.isCancelled()) {
                            Location to = teleport.getTo();
                            blockposition1 = new BlockPosition(to.getX(), to.getY(), to.getZ());
                        } else {
                            return false;
                        }
                        // CraftBukkit end
                        this.datawatcher.set(EntityShulker.b, enumdirection);
                        this.playSound(SoundEffects.ENTITY_SHULKER_TELEPORT, 1.0F, 1.0F);
                        this.datawatcher.set(EntityShulker.c, Optional.of(blockposition1));
                        this.datawatcher.set(EntityShulker.d, (byte) 0);
                        this.setGoalTarget((EntityLiving) null);
                        return true;
                    }
                }
            }

            return false;
        } else {
            return true;
        }
    }

    @Override
    public void movementTick() {
        super.movementTick();
        this.setMot(Vec3D.ORIGIN);
        if (!this.isNoAI()) {
            this.aB = 0.0F;
            this.aA = 0.0F;
        }

    }

    @Override
    public void a(DataWatcherObject<?> datawatcherobject) {
        if (EntityShulker.c.equals(datawatcherobject) && this.world.isClientSide && !this.isPassenger()) {
            BlockPosition blockposition = this.eM();

            if (blockposition != null) {
                if (this.bt == null) {
                    this.bt = blockposition;
                } else {
                    this.bu = 6;
                }

                this.g((double) blockposition.getX() + 0.5D, (double) blockposition.getY(), (double) blockposition.getZ() + 0.5D);
                if (valid) ((WorldServer) world).chunkCheck(this); // CraftBukkit
            }
        }

        super.a(datawatcherobject);
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.eT()) {
            Entity entity = damagesource.j();

            if (entity instanceof EntityArrow) {
                return false;
            }
        }

        if (super.damageEntity(damagesource, f)) {
            if ((double) this.getHealth() < (double) this.getMaxHealth() * 0.5D && this.random.nextInt(4) == 0) {
                this.eK();
            }

            return true;
        } else {
            return false;
        }
    }

    private boolean eT() {
        return this.eN() == 0;
    }

    @Override
    public boolean aY() {
        return this.isAlive();
    }

    public EnumDirection eL() {
        return (EnumDirection) this.datawatcher.get(EntityShulker.b);
    }

    @Nullable
    public BlockPosition eM() {
        return (BlockPosition) ((Optional) this.datawatcher.get(EntityShulker.c)).orElse((Object) null);
    }

    public void h(@Nullable BlockPosition blockposition) {
        this.datawatcher.set(EntityShulker.c, Optional.ofNullable(blockposition));
    }

    public int eN() {
        return (Byte) this.datawatcher.get(EntityShulker.d);
    }

    public void a(int i) {
        if (!this.world.isClientSide) {
            this.getAttributeInstance(GenericAttributes.ARMOR).removeModifier(EntityShulker.bq);
            if (i == 0) {
                this.getAttributeInstance(GenericAttributes.ARMOR).addModifier(EntityShulker.bq);
                this.playSound(SoundEffects.ENTITY_SHULKER_CLOSE, 1.0F, 1.0F);
            } else {
                this.playSound(SoundEffects.ENTITY_SHULKER_OPEN, 1.0F, 1.0F);
            }
        }

        this.datawatcher.set(EntityShulker.d, (byte) i);
    }

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return 0.5F;
    }

    @Override
    public int O() {
        return 180;
    }

    @Override
    public int eo() {
        return 180;
    }

    @Override
    public void collide(Entity entity) {}

    @Override
    public float bf() {
        return 0.0F;
    }

    static class c extends PathfinderGoalNearestAttackableTarget<EntityLiving> {

        public c(EntityShulker entityshulker) {
            super(entityshulker, EntityLiving.class, 10, true, false, (entityliving) -> {
                return entityliving instanceof IMonster;
            });
        }

        @Override
        public boolean a() {
            return this.e.getScoreboardTeam() == null ? false : super.a();
        }

        @Override
        protected AxisAlignedBB a(double d0) {
            EnumDirection enumdirection = ((EntityShulker) this.e).eL();

            return enumdirection.n() == EnumDirection.EnumAxis.X ? this.e.getBoundingBox().grow(4.0D, d0, d0) : (enumdirection.n() == EnumDirection.EnumAxis.Z ? this.e.getBoundingBox().grow(d0, d0, 4.0D) : this.e.getBoundingBox().grow(d0, 4.0D, d0));
        }
    }

    class d extends PathfinderGoalNearestAttackableTarget<EntityHuman> {

        public d(EntityShulker entityshulker) {
            super(entityshulker, EntityHuman.class, true);
        }

        @Override
        public boolean a() {
            return EntityShulker.this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? false : super.a();
        }

        @Override
        protected AxisAlignedBB a(double d0) {
            EnumDirection enumdirection = ((EntityShulker) this.e).eL();

            return enumdirection.n() == EnumDirection.EnumAxis.X ? this.e.getBoundingBox().grow(4.0D, d0, d0) : (enumdirection.n() == EnumDirection.EnumAxis.Z ? this.e.getBoundingBox().grow(d0, d0, 4.0D) : this.e.getBoundingBox().grow(d0, 4.0D, d0));
        }
    }

    class a extends PathfinderGoal {

        private int b;

        public a() {
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
        }

        @Override
        public boolean a() {
            EntityLiving entityliving = EntityShulker.this.getGoalTarget();

            return entityliving != null && entityliving.isAlive() ? EntityShulker.this.world.getDifficulty() != EnumDifficulty.PEACEFUL : false;
        }

        @Override
        public void c() {
            this.b = 20;
            EntityShulker.this.a(100);
        }

        @Override
        public void d() {
            EntityShulker.this.a(0);
        }

        @Override
        public void e() {
            if (EntityShulker.this.world.getDifficulty() != EnumDifficulty.PEACEFUL) {
                --this.b;
                EntityLiving entityliving = EntityShulker.this.getGoalTarget();

                EntityShulker.this.getControllerLook().a(entityliving, 180.0F, 180.0F);
                double d0 = EntityShulker.this.h((Entity) entityliving);

                if (d0 < 400.0D) {
                    if (this.b <= 0) {
                        this.b = 20 + EntityShulker.this.random.nextInt(10) * 20 / 2;
                        EntityShulker.this.world.addEntity(new EntityShulkerBullet(EntityShulker.this.world, EntityShulker.this, entityliving, EntityShulker.this.eL().n()));
                        EntityShulker.this.playSound(SoundEffects.ENTITY_SHULKER_SHOOT, 2.0F, (EntityShulker.this.random.nextFloat() - EntityShulker.this.random.nextFloat()) * 0.2F + 1.0F);
                    }
                } else {
                    EntityShulker.this.setGoalTarget((EntityLiving) null);
                }

                super.e();
            }
        }
    }

    class e extends PathfinderGoal {

        private int b;

        private e() {}

        @Override
        public boolean a() {
            return EntityShulker.this.getGoalTarget() == null && EntityShulker.this.random.nextInt(40) == 0;
        }

        @Override
        public boolean b() {
            return EntityShulker.this.getGoalTarget() == null && this.b > 0;
        }

        @Override
        public void c() {
            this.b = 20 * (1 + EntityShulker.this.random.nextInt(3));
            EntityShulker.this.a(30);
        }

        @Override
        public void d() {
            if (EntityShulker.this.getGoalTarget() == null) {
                EntityShulker.this.a(0);
            }

        }

        @Override
        public void e() {
            --this.b;
        }
    }

    class b extends EntityAIBodyControl {

        public b(EntityInsentient entityinsentient) {
            super(entityinsentient);
        }

        @Override
        public void a() {}
    }
}
