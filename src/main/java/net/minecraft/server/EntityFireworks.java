package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.OptionalInt;
import javax.annotation.Nullable;

public class EntityFireworks extends IProjectile {

    public static final DataWatcherObject<ItemStack> FIREWORK_ITEM = DataWatcher.a(EntityFireworks.class, DataWatcherRegistry.g);
    private static final DataWatcherObject<OptionalInt> c = DataWatcher.a(EntityFireworks.class, DataWatcherRegistry.r);
    public static final DataWatcherObject<Boolean> SHOT_AT_ANGLE = DataWatcher.a(EntityFireworks.class, DataWatcherRegistry.i);
    private int ticksFlown;
    public int expectedLifespan;
    private EntityLiving ridingEntity;

    public EntityFireworks(EntityTypes<? extends EntityFireworks> entitytypes, World world) {
        super(entitytypes, world);
    }

    public EntityFireworks(World world, double d0, double d1, double d2, ItemStack itemstack) {
        super(EntityTypes.FIREWORK_ROCKET, world);
        this.ticksFlown = 0;
        this.setPosition(d0, d1, d2);
        int i = 1;

        if (!itemstack.isEmpty() && itemstack.hasTag()) {
            this.datawatcher.set(EntityFireworks.FIREWORK_ITEM, itemstack.cloneItemStack());
            i += itemstack.a("Fireworks").getByte("Flight");
        }

        this.setMot(this.random.nextGaussian() * 0.001D, 0.05D, this.random.nextGaussian() * 0.001D);
        this.expectedLifespan = 10 * i + this.random.nextInt(6) + this.random.nextInt(7);
    }

    public EntityFireworks(World world, @Nullable Entity entity, double d0, double d1, double d2, ItemStack itemstack) {
        this(world, d0, d1, d2, itemstack);
        this.setShooter(entity);
    }

    public EntityFireworks(World world, ItemStack itemstack, EntityLiving entityliving) {
        this(world, entityliving, entityliving.locX(), entityliving.locY(), entityliving.locZ(), itemstack);
        this.datawatcher.set(EntityFireworks.c, OptionalInt.of(entityliving.getId()));
        this.ridingEntity = entityliving;
    }

    public EntityFireworks(World world, ItemStack itemstack, double d0, double d1, double d2, boolean flag) {
        this(world, d0, d1, d2, itemstack);
        this.datawatcher.set(EntityFireworks.SHOT_AT_ANGLE, flag);
    }

    public EntityFireworks(World world, ItemStack itemstack, Entity entity, double d0, double d1, double d2, boolean flag) {
        this(world, itemstack, d0, d1, d2, flag);
        this.setShooter(entity);
    }

    @Override
    protected void initDatawatcher() {
        this.datawatcher.register(EntityFireworks.FIREWORK_ITEM, ItemStack.b);
        this.datawatcher.register(EntityFireworks.c, OptionalInt.empty());
        this.datawatcher.register(EntityFireworks.SHOT_AT_ANGLE, false);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3D vec3d;

        if (this.n()) {
            if (this.ridingEntity == null) {
                ((OptionalInt) this.datawatcher.get(EntityFireworks.c)).ifPresent((i) -> {
                    Entity entity = this.world.getEntity(i);

                    if (entity instanceof EntityLiving) {
                        this.ridingEntity = (EntityLiving) entity;
                    }

                });
            }

            if (this.ridingEntity != null) {
                if (this.ridingEntity.isGliding()) {
                    vec3d = this.ridingEntity.getLookDirection();
                    double d0 = 1.5D;
                    double d1 = 0.1D;
                    Vec3D vec3d1 = this.ridingEntity.getMot();

                    this.ridingEntity.setMot(vec3d1.add(vec3d.x * 0.1D + (vec3d.x * 1.5D - vec3d1.x) * 0.5D, vec3d.y * 0.1D + (vec3d.y * 1.5D - vec3d1.y) * 0.5D, vec3d.z * 0.1D + (vec3d.z * 1.5D - vec3d1.z) * 0.5D));
                }

                this.setPosition(this.ridingEntity.locX(), this.ridingEntity.locY(), this.ridingEntity.locZ());
                this.setMot(this.ridingEntity.getMot());
            }
        } else {
            if (!this.isShotAtAngle()) {
                this.setMot(this.getMot().d(1.15D, 1.0D, 1.15D).add(0.0D, 0.04D, 0.0D));
            }

            vec3d = this.getMot();
            this.move(EnumMoveType.SELF, vec3d);
            this.setMot(vec3d);
        }

        MovingObjectPosition movingobjectposition = ProjectileHelper.a((Entity) this, this::a);

        if (!this.noclip) {
            this.a(movingobjectposition);
            this.impulse = true;
        }

        this.x();
        if (this.ticksFlown == 0 && !this.isSilent()) {
            this.world.playSound((EntityHuman) null, this.locX(), this.locY(), this.locZ(), SoundEffects.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.AMBIENT, 3.0F, 1.0F);
        }

        ++this.ticksFlown;
        if (this.world.isClientSide && this.ticksFlown % 2 < 2) {
            this.world.addParticle(Particles.FIREWORK, this.locX(), this.locY() - 0.3D, this.locZ(), this.random.nextGaussian() * 0.05D, -this.getMot().y * 0.5D, this.random.nextGaussian() * 0.05D);
        }

        if (!this.world.isClientSide && this.ticksFlown > this.expectedLifespan) {
            this.explode();
        }

    }

    private void explode() {
        this.world.broadcastEntityEffect(this, (byte) 17);
        this.m();
        this.die();
    }

    @Override
    protected void a(MovingObjectPositionEntity movingobjectpositionentity) {
        super.a(movingobjectpositionentity);
        if (!this.world.isClientSide) {
            this.explode();
        }
    }

    @Override
    protected void a(MovingObjectPositionBlock movingobjectpositionblock) {
        BlockPosition blockposition = new BlockPosition(movingobjectpositionblock.getBlockPosition());

        this.world.getType(blockposition).a(this.world, blockposition, (Entity) this);
        if (!this.world.s_() && this.hasExplosions()) {
            this.explode();
        }

        super.a(movingobjectpositionblock);
    }

    private boolean hasExplosions() {
        ItemStack itemstack = (ItemStack) this.datawatcher.get(EntityFireworks.FIREWORK_ITEM);
        NBTTagCompound nbttagcompound = itemstack.isEmpty() ? null : itemstack.b("Fireworks");
        NBTTagList nbttaglist = nbttagcompound != null ? nbttagcompound.getList("Explosions", 10) : null;

        return nbttaglist != null && !nbttaglist.isEmpty();
    }

    private void m() {
        float f = 0.0F;
        ItemStack itemstack = (ItemStack) this.datawatcher.get(EntityFireworks.FIREWORK_ITEM);
        NBTTagCompound nbttagcompound = itemstack.isEmpty() ? null : itemstack.b("Fireworks");
        NBTTagList nbttaglist = nbttagcompound != null ? nbttagcompound.getList("Explosions", 10) : null;

        if (nbttaglist != null && !nbttaglist.isEmpty()) {
            f = 5.0F + (float) (nbttaglist.size() * 2);
        }

        if (f > 0.0F) {
            if (this.ridingEntity != null) {
                this.ridingEntity.damageEntity(DamageSource.a(this, this.getShooter()), 5.0F + (float) (nbttaglist.size() * 2));
            }

            double d0 = 5.0D;
            Vec3D vec3d = this.getPositionVector();
            List<EntityLiving> list = this.world.a(EntityLiving.class, this.getBoundingBox().g(5.0D));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityLiving entityliving = (EntityLiving) iterator.next();

                if (entityliving != this.ridingEntity && this.h(entityliving) <= 25.0D) {
                    boolean flag = false;

                    for (int i = 0; i < 2; ++i) {
                        Vec3D vec3d1 = new Vec3D(entityliving.locX(), entityliving.e(0.5D * (double) i), entityliving.locZ());
                        MovingObjectPositionBlock movingobjectpositionblock = this.world.rayTrace(new RayTrace(vec3d, vec3d1, RayTrace.BlockCollisionOption.COLLIDER, RayTrace.FluidCollisionOption.NONE, this));

                        if (movingobjectpositionblock.getType() == MovingObjectPosition.EnumMovingObjectType.MISS) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        float f1 = f * (float) Math.sqrt((5.0D - (double) this.g((Entity) entityliving)) / 5.0D);

                        entityliving.damageEntity(DamageSource.a(this, this.getShooter()), f1);
                    }
                }
            }
        }

    }

    private boolean n() {
        return ((OptionalInt) this.datawatcher.get(EntityFireworks.c)).isPresent();
    }

    public boolean isShotAtAngle() {
        return (Boolean) this.datawatcher.get(EntityFireworks.SHOT_AT_ANGLE);
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setInt("Life", this.ticksFlown);
        nbttagcompound.setInt("LifeTime", this.expectedLifespan);
        ItemStack itemstack = (ItemStack) this.datawatcher.get(EntityFireworks.FIREWORK_ITEM);

        if (!itemstack.isEmpty()) {
            nbttagcompound.set("FireworksItem", itemstack.save(new NBTTagCompound()));
        }

        nbttagcompound.setBoolean("ShotAtAngle", (Boolean) this.datawatcher.get(EntityFireworks.SHOT_AT_ANGLE));
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.ticksFlown = nbttagcompound.getInt("Life");
        this.expectedLifespan = nbttagcompound.getInt("LifeTime");
        ItemStack itemstack = ItemStack.a(nbttagcompound.getCompound("FireworksItem"));

        if (!itemstack.isEmpty()) {
            this.datawatcher.set(EntityFireworks.FIREWORK_ITEM, itemstack);
        }

        if (nbttagcompound.hasKey("ShotAtAngle")) {
            this.datawatcher.set(EntityFireworks.SHOT_AT_ANGLE, nbttagcompound.getBoolean("ShotAtAngle"));
        }

    }

    @Override
    public boolean bK() {
        return false;
    }

    @Override
    public Packet<?> P() {
        return new PacketPlayOutSpawnEntity(this);
    }
}
