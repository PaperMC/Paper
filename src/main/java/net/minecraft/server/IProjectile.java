package net.minecraft.server;

import java.util.Iterator;
import java.util.UUID;
import javax.annotation.Nullable;

public abstract class IProjectile extends Entity {

    private UUID shooter;
    private int c;
    private boolean d;

    IProjectile(EntityTypes<? extends IProjectile> entitytypes, World world) {
        super(entitytypes, world);
    }

    public void setShooter(@Nullable Entity entity) {
        if (entity != null) {
            this.shooter = entity.getUniqueID();
            this.c = entity.getId();
        }

    }

    @Nullable
    public Entity getShooter() {
        return this.shooter != null && this.world instanceof WorldServer ? ((WorldServer) this.world).getEntity(this.shooter) : (this.c != 0 ? this.world.getEntity(this.c) : null);
    }

    @Override
    protected void saveData(NBTTagCompound nbttagcompound) {
        if (this.shooter != null) {
            nbttagcompound.a("Owner", this.shooter);
        }

        if (this.d) {
            nbttagcompound.setBoolean("LeftOwner", true);
        }

    }

    @Override
    protected void loadData(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.b("Owner")) {
            this.shooter = nbttagcompound.a("Owner");
        }

        this.d = nbttagcompound.getBoolean("LeftOwner");
    }

    @Override
    public void tick() {
        if (!this.d) {
            this.d = this.h();
        }

        super.tick();
    }

    private boolean h() {
        Entity entity = this.getShooter();

        if (entity != null) {
            Iterator iterator = this.world.getEntities(this, this.getBoundingBox().b(this.getMot()).g(1.0D), (entity1) -> {
                return !entity1.isSpectator() && entity1.isInteractable();
            }).iterator();

            while (iterator.hasNext()) {
                Entity entity1 = (Entity) iterator.next();

                if (entity1.getRootVehicle() == entity.getRootVehicle()) {
                    return false;
                }
            }
        }

        return true;
    }

    public void shoot(double d0, double d1, double d2, float f, float f1) {
        Vec3D vec3d = (new Vec3D(d0, d1, d2)).d().add(this.random.nextGaussian() * 0.007499999832361937D * (double) f1, this.random.nextGaussian() * 0.007499999832361937D * (double) f1, this.random.nextGaussian() * 0.007499999832361937D * (double) f1).a((double) f);

        this.setMot(vec3d);
        float f2 = MathHelper.sqrt(c(vec3d));

        this.yaw = (float) (MathHelper.d(vec3d.x, vec3d.z) * 57.2957763671875D);
        this.pitch = (float) (MathHelper.d(vec3d.y, (double) f2) * 57.2957763671875D);
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
    }

    public void a(Entity entity, float f, float f1, float f2, float f3, float f4) {
        float f5 = -MathHelper.sin(f1 * 0.017453292F) * MathHelper.cos(f * 0.017453292F);
        float f6 = -MathHelper.sin((f + f2) * 0.017453292F);
        float f7 = MathHelper.cos(f1 * 0.017453292F) * MathHelper.cos(f * 0.017453292F);

        this.shoot((double) f5, (double) f6, (double) f7, f3, f4);
        Vec3D vec3d = entity.getMot();

        this.setMot(this.getMot().add(vec3d.x, entity.isOnGround() ? 0.0D : vec3d.y, vec3d.z));
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        MovingObjectPosition.EnumMovingObjectType movingobjectposition_enummovingobjecttype = movingobjectposition.getType();

        if (movingobjectposition_enummovingobjecttype == MovingObjectPosition.EnumMovingObjectType.ENTITY) {
            this.a((MovingObjectPositionEntity) movingobjectposition);
        } else if (movingobjectposition_enummovingobjecttype == MovingObjectPosition.EnumMovingObjectType.BLOCK) {
            this.a((MovingObjectPositionBlock) movingobjectposition);
        }

    }

    protected void a(MovingObjectPositionEntity movingobjectpositionentity) {}

    protected void a(MovingObjectPositionBlock movingobjectpositionblock) {
        IBlockData iblockdata = this.world.getType(movingobjectpositionblock.getBlockPosition());

        iblockdata.a(this.world, iblockdata, movingobjectpositionblock, this);
    }

    protected boolean a(Entity entity) {
        if (!entity.isSpectator() && entity.isAlive() && entity.isInteractable()) {
            Entity entity1 = this.getShooter();

            return entity1 == null || this.d || !entity1.isSameVehicle(entity);
        } else {
            return false;
        }
    }

    protected void x() {
        Vec3D vec3d = this.getMot();
        float f = MathHelper.sqrt(c(vec3d));

        this.pitch = e(this.lastPitch, (float) (MathHelper.d(vec3d.y, (double) f) * 57.2957763671875D));
        this.yaw = e(this.lastYaw, (float) (MathHelper.d(vec3d.x, vec3d.z) * 57.2957763671875D));
    }

    protected static float e(float f, float f1) {
        while (f1 - f < -180.0F) {
            f -= 360.0F;
        }

        while (f1 - f >= 180.0F) {
            f += 360.0F;
        }

        return MathHelper.g(0.2F, f, f1);
    }
}
