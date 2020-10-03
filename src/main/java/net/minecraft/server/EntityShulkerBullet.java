package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

public class EntityShulkerBullet extends IProjectile {

    private Entity target;
    @Nullable
    private EnumDirection dir;
    private int d;
    private double e;
    private double f;
    private double g;
    @Nullable
    private UUID ag;

    public EntityShulkerBullet(EntityTypes<? extends EntityShulkerBullet> entitytypes, World world) {
        super(entitytypes, world);
        this.noclip = true;
    }

    public EntityShulkerBullet(World world, EntityLiving entityliving, Entity entity, EnumDirection.EnumAxis enumdirection_enumaxis) {
        this(EntityTypes.SHULKER_BULLET, world);
        this.setShooter(entityliving);
        BlockPosition blockposition = entityliving.getChunkCoordinates();
        double d0 = (double) blockposition.getX() + 0.5D;
        double d1 = (double) blockposition.getY() + 0.5D;
        double d2 = (double) blockposition.getZ() + 0.5D;

        this.setPositionRotation(d0, d1, d2, this.yaw, this.pitch);
        this.target = entity;
        this.dir = EnumDirection.UP;
        this.a(enumdirection_enumaxis);
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Override
    protected void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        if (this.target != null) {
            nbttagcompound.a("Target", this.target.getUniqueID());
        }

        if (this.dir != null) {
            nbttagcompound.setInt("Dir", this.dir.c());
        }

        nbttagcompound.setInt("Steps", this.d);
        nbttagcompound.setDouble("TXD", this.e);
        nbttagcompound.setDouble("TYD", this.f);
        nbttagcompound.setDouble("TZD", this.g);
    }

    @Override
    protected void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.d = nbttagcompound.getInt("Steps");
        this.e = nbttagcompound.getDouble("TXD");
        this.f = nbttagcompound.getDouble("TYD");
        this.g = nbttagcompound.getDouble("TZD");
        if (nbttagcompound.hasKeyOfType("Dir", 99)) {
            this.dir = EnumDirection.fromType1(nbttagcompound.getInt("Dir"));
        }

        if (nbttagcompound.b("Target")) {
            this.ag = nbttagcompound.a("Target");
        }

    }

    @Override
    protected void initDatawatcher() {}

    private void a(@Nullable EnumDirection enumdirection) {
        this.dir = enumdirection;
    }

    private void a(@Nullable EnumDirection.EnumAxis enumdirection_enumaxis) {
        double d0 = 0.5D;
        BlockPosition blockposition;

        if (this.target == null) {
            blockposition = this.getChunkCoordinates().down();
        } else {
            d0 = (double) this.target.getHeight() * 0.5D;
            blockposition = new BlockPosition(this.target.locX(), this.target.locY() + d0, this.target.locZ());
        }

        double d1 = (double) blockposition.getX() + 0.5D;
        double d2 = (double) blockposition.getY() + d0;
        double d3 = (double) blockposition.getZ() + 0.5D;
        EnumDirection enumdirection = null;

        if (!blockposition.a((IPosition) this.getPositionVector(), 2.0D)) {
            BlockPosition blockposition1 = this.getChunkCoordinates();
            List<EnumDirection> list = Lists.newArrayList();

            if (enumdirection_enumaxis != EnumDirection.EnumAxis.X) {
                if (blockposition1.getX() < blockposition.getX() && this.world.isEmpty(blockposition1.east())) {
                    list.add(EnumDirection.EAST);
                } else if (blockposition1.getX() > blockposition.getX() && this.world.isEmpty(blockposition1.west())) {
                    list.add(EnumDirection.WEST);
                }
            }

            if (enumdirection_enumaxis != EnumDirection.EnumAxis.Y) {
                if (blockposition1.getY() < blockposition.getY() && this.world.isEmpty(blockposition1.up())) {
                    list.add(EnumDirection.UP);
                } else if (blockposition1.getY() > blockposition.getY() && this.world.isEmpty(blockposition1.down())) {
                    list.add(EnumDirection.DOWN);
                }
            }

            if (enumdirection_enumaxis != EnumDirection.EnumAxis.Z) {
                if (blockposition1.getZ() < blockposition.getZ() && this.world.isEmpty(blockposition1.south())) {
                    list.add(EnumDirection.SOUTH);
                } else if (blockposition1.getZ() > blockposition.getZ() && this.world.isEmpty(blockposition1.north())) {
                    list.add(EnumDirection.NORTH);
                }
            }

            enumdirection = EnumDirection.a(this.random);
            if (list.isEmpty()) {
                for (int i = 5; !this.world.isEmpty(blockposition1.shift(enumdirection)) && i > 0; --i) {
                    enumdirection = EnumDirection.a(this.random);
                }
            } else {
                enumdirection = (EnumDirection) list.get(this.random.nextInt(list.size()));
            }

            d1 = this.locX() + (double) enumdirection.getAdjacentX();
            d2 = this.locY() + (double) enumdirection.getAdjacentY();
            d3 = this.locZ() + (double) enumdirection.getAdjacentZ();
        }

        this.a(enumdirection);
        double d4 = d1 - this.locX();
        double d5 = d2 - this.locY();
        double d6 = d3 - this.locZ();
        double d7 = (double) MathHelper.sqrt(d4 * d4 + d5 * d5 + d6 * d6);

        if (d7 == 0.0D) {
            this.e = 0.0D;
            this.f = 0.0D;
            this.g = 0.0D;
        } else {
            this.e = d4 / d7 * 0.15D;
            this.f = d5 / d7 * 0.15D;
            this.g = d6 / d7 * 0.15D;
        }

        this.impulse = true;
        this.d = 10 + this.random.nextInt(5) * 10;
    }

    @Override
    public void checkDespawn() {
        if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.die();
        }

    }

    @Override
    public void tick() {
        super.tick();
        Vec3D vec3d;

        if (!this.world.isClientSide) {
            if (this.target == null && this.ag != null) {
                this.target = ((WorldServer) this.world).getEntity(this.ag);
                if (this.target == null) {
                    this.ag = null;
                }
            }

            if (this.target != null && this.target.isAlive() && (!(this.target instanceof EntityHuman) || !((EntityHuman) this.target).isSpectator())) {
                this.e = MathHelper.a(this.e * 1.025D, -1.0D, 1.0D);
                this.f = MathHelper.a(this.f * 1.025D, -1.0D, 1.0D);
                this.g = MathHelper.a(this.g * 1.025D, -1.0D, 1.0D);
                vec3d = this.getMot();
                this.setMot(vec3d.add((this.e - vec3d.x) * 0.2D, (this.f - vec3d.y) * 0.2D, (this.g - vec3d.z) * 0.2D));
            } else if (!this.isNoGravity()) {
                this.setMot(this.getMot().add(0.0D, -0.04D, 0.0D));
            }

            MovingObjectPosition movingobjectposition = ProjectileHelper.a((Entity) this, this::a);

            if (movingobjectposition.getType() != MovingObjectPosition.EnumMovingObjectType.MISS) {
                this.a(movingobjectposition);
            }
        }

        this.checkBlockCollisions();
        vec3d = this.getMot();
        this.setPosition(this.locX() + vec3d.x, this.locY() + vec3d.y, this.locZ() + vec3d.z);
        ProjectileHelper.a(this, 0.5F);
        if (this.world.isClientSide) {
            this.world.addParticle(Particles.END_ROD, this.locX() - vec3d.x, this.locY() - vec3d.y + 0.15D, this.locZ() - vec3d.z, 0.0D, 0.0D, 0.0D);
        } else if (this.target != null && !this.target.dead) {
            if (this.d > 0) {
                --this.d;
                if (this.d == 0) {
                    this.a(this.dir == null ? null : this.dir.n());
                }
            }

            if (this.dir != null) {
                BlockPosition blockposition = this.getChunkCoordinates();
                EnumDirection.EnumAxis enumdirection_enumaxis = this.dir.n();

                if (this.world.a(blockposition.shift(this.dir), (Entity) this)) {
                    this.a(enumdirection_enumaxis);
                } else {
                    BlockPosition blockposition1 = this.target.getChunkCoordinates();

                    if (enumdirection_enumaxis == EnumDirection.EnumAxis.X && blockposition.getX() == blockposition1.getX() || enumdirection_enumaxis == EnumDirection.EnumAxis.Z && blockposition.getZ() == blockposition1.getZ() || enumdirection_enumaxis == EnumDirection.EnumAxis.Y && blockposition.getY() == blockposition1.getY()) {
                        this.a(enumdirection_enumaxis);
                    }
                }
            }
        }

    }

    @Override
    protected boolean a(Entity entity) {
        return super.a(entity) && !entity.noclip;
    }

    @Override
    public boolean isBurning() {
        return false;
    }

    @Override
    public float aQ() {
        return 1.0F;
    }

    @Override
    protected void a(MovingObjectPositionEntity movingobjectpositionentity) {
        super.a(movingobjectpositionentity);
        Entity entity = movingobjectpositionentity.getEntity();
        Entity entity1 = this.getShooter();
        EntityLiving entityliving = entity1 instanceof EntityLiving ? (EntityLiving) entity1 : null;
        boolean flag = entity.damageEntity(DamageSource.a((Entity) this, entityliving).c(), 4.0F);

        if (flag) {
            this.a(entityliving, entity);
            if (entity instanceof EntityLiving) {
                ((EntityLiving) entity).addEffect(new MobEffect(MobEffects.LEVITATION, 200));
            }
        }

    }

    @Override
    protected void a(MovingObjectPositionBlock movingobjectpositionblock) {
        super.a(movingobjectpositionblock);
        ((WorldServer) this.world).a(Particles.EXPLOSION, this.locX(), this.locY(), this.locZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
        this.playSound(SoundEffects.ENTITY_SHULKER_BULLET_HIT, 1.0F, 1.0F);
    }

    @Override
    protected void a(MovingObjectPosition movingobjectposition) {
        super.a(movingobjectposition);
        this.die();
    }

    @Override
    public boolean isInteractable() {
        return true;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (!this.world.isClientSide) {
            this.playSound(SoundEffects.ENTITY_SHULKER_BULLET_HURT, 1.0F, 1.0F);
            ((WorldServer) this.world).a(Particles.CRIT, this.locX(), this.locY(), this.locZ(), 15, 0.2D, 0.2D, 0.2D, 0.0D);
            this.die();
        }

        return true;
    }

    @Override
    public Packet<?> P() {
        return new PacketPlayOutSpawnEntity(this);
    }
}
