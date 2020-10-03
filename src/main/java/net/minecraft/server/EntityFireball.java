package net.minecraft.server;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public abstract class EntityFireball extends IProjectile {

    public double dirX;
    public double dirY;
    public double dirZ;
    public float bukkitYield = 1; // CraftBukkit
    public boolean isIncendiary = true; // CraftBukkit

    protected EntityFireball(EntityTypes<? extends EntityFireball> entitytypes, World world) {
        super(entitytypes, world);
    }

    public EntityFireball(EntityTypes<? extends EntityFireball> entitytypes, double d0, double d1, double d2, double d3, double d4, double d5, World world) {
        this(entitytypes, world);
        this.setPositionRotation(d0, d1, d2, this.yaw, this.pitch);
        this.ae();
        // CraftBukkit start - Added setDirection method
        this.setDirection(d3, d4, d5);
    }

    public void setDirection(double d3, double d4, double d5) {
        // CraftBukkit end
        double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

        if (d6 != 0.0D) {
            this.dirX = d3 / d6 * 0.1D;
            this.dirY = d4 / d6 * 0.1D;
            this.dirZ = d5 / d6 * 0.1D;
        }

    }

    public EntityFireball(EntityTypes<? extends EntityFireball> entitytypes, EntityLiving entityliving, double d0, double d1, double d2, World world) {
        this(entitytypes, entityliving.locX(), entityliving.locY(), entityliving.locZ(), d0, d1, d2, world);
        this.setShooter(entityliving);
        this.setYawPitch(entityliving.yaw, entityliving.pitch);
    }

    @Override
    protected void initDatawatcher() {}

    @Override
    public void tick() {
        Entity entity = this.getShooter();

        if (!this.world.isClientSide && (entity != null && entity.dead || !this.world.isLoaded(this.getChunkCoordinates()))) {
            this.die();
        } else {
            super.tick();
            if (this.W_()) {
                this.setOnFire(1);
            }

            MovingObjectPosition movingobjectposition = ProjectileHelper.a((Entity) this, this::a);

            if (movingobjectposition.getType() != MovingObjectPosition.EnumMovingObjectType.MISS) {
                this.a(movingobjectposition);

                // CraftBukkit start - Fire ProjectileHitEvent
                if (this.dead) {
                    CraftEventFactory.callProjectileHitEvent(this, movingobjectposition);
                }
                // CraftBukkit end
            }

            this.checkBlockCollisions();
            Vec3D vec3d = this.getMot();
            double d0 = this.locX() + vec3d.x;
            double d1 = this.locY() + vec3d.y;
            double d2 = this.locZ() + vec3d.z;

            ProjectileHelper.a(this, 0.2F);
            float f = this.i();

            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    float f1 = 0.25F;

                    this.world.addParticle(Particles.BUBBLE, d0 - vec3d.x * 0.25D, d1 - vec3d.y * 0.25D, d2 - vec3d.z * 0.25D, vec3d.x, vec3d.y, vec3d.z);
                }

                f = 0.8F;
            }

            this.setMot(vec3d.add(this.dirX, this.dirY, this.dirZ).a((double) f));
            this.world.addParticle(this.h(), d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);
            this.setPosition(d0, d1, d2);
        }
    }

    @Override
    protected boolean a(Entity entity) {
        return super.a(entity) && !entity.noclip;
    }

    protected boolean W_() {
        return true;
    }

    protected ParticleParam h() {
        return Particles.SMOKE;
    }

    protected float i() {
        return 0.95F;
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.set("power", this.a(new double[]{this.dirX, this.dirY, this.dirZ}));
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("power", 9)) {
            NBTTagList nbttaglist = nbttagcompound.getList("power", 6);

            if (nbttaglist.size() == 3) {
                this.dirX = nbttaglist.h(0);
                this.dirY = nbttaglist.h(1);
                this.dirZ = nbttaglist.h(2);
            }
        }

    }

    @Override
    public boolean isInteractable() {
        return true;
    }

    @Override
    public float bf() {
        return 1.0F;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else {
            this.velocityChanged();
            Entity entity = damagesource.getEntity();

            if (entity != null) {
                // CraftBukkit start
                if (CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f)) {
                    return false;
                }
                // CraftBukkit end
                Vec3D vec3d = entity.getLookDirection();

                this.setMot(vec3d);
                this.dirX = vec3d.x * 0.1D;
                this.dirY = vec3d.y * 0.1D;
                this.dirZ = vec3d.z * 0.1D;
                this.setShooter(entity);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public float aQ() {
        return 1.0F;
    }

    @Override
    public Packet<?> P() {
        Entity entity = this.getShooter();
        int i = entity == null ? 0 : entity.getId();

        return new PacketPlayOutSpawnEntity(this.getId(), this.getUniqueID(), this.locX(), this.locY(), this.locZ(), this.pitch, this.yaw, this.getEntityType(), i, new Vec3D(this.dirX, this.dirY, this.dirZ));
    }
}
