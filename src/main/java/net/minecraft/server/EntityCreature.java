package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.event.entity.EntityTargetEvent;
// CraftBukkit end

public abstract class EntityCreature extends EntityLiving {

    public PathEntity pathEntity; // CraftBukkit - private -> public
    public Entity target; // CraftBukkit - protected -> public
    protected boolean b = false;
    protected int c = 0;

    public EntityCreature(World world) {
        super(world);
    }

    protected boolean h() {
        return false;
    }

    protected void bn() {
        this.world.methodProfiler.a("ai");
        if (this.c > 0) {
            --this.c;
        }

        this.b = this.h();
        float f = 16.0F;

        if (this.target == null) {
            // CraftBukkit start
            Entity target = this.findTarget();
            if (target != null) {
                EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), target.getBukkitEntity(), EntityTargetEvent.TargetReason.CLOSEST_PLAYER);
                this.world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    if (event.getTarget() == null) {
                        this.target = null;
                    } else {
                        this.target = ((CraftEntity) event.getTarget()).getHandle();
                    }
                }
            }
            // CraftBukkit end

            if (this.target != null) {
                this.pathEntity = this.world.findPath(this, this.target, f, true, false, false, true);
            }
        } else if (this.target.isAlive()) {
            float f1 = this.target.d((Entity) this);

            if (this.n(this.target)) {
                this.a(this.target, f1);
            }
        } else {
            // CraftBukkit start
            EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), null, EntityTargetEvent.TargetReason.TARGET_DIED);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                if (event.getTarget() == null) {
                    this.target = null;
                } else {
                    this.target = ((CraftEntity) event.getTarget()).getHandle();
                }
            }
            // CraftBukkit end
        }

        this.world.methodProfiler.b();
        if (!this.b && this.target != null && (this.pathEntity == null || this.random.nextInt(20) == 0)) {
            this.pathEntity = this.world.findPath(this, this.target, f, true, false, false, true);
        } else if (!this.b && (this.pathEntity == null && this.random.nextInt(180) == 0 || this.random.nextInt(120) == 0 || this.c > 0) && this.bB < 100) {
            this.i();
        }

        int i = MathHelper.floor(this.boundingBox.b + 0.5D);
        boolean flag = this.H();
        boolean flag1 = this.J();

        this.pitch = 0.0F;
        if (this.pathEntity != null && this.random.nextInt(100) != 0) {
            this.world.methodProfiler.a("followpath");
            Vec3D vec3d = this.pathEntity.a((Entity) this);
            double d0 = (double) (this.width * 2.0F);

            while (vec3d != null && vec3d.d(this.locX, vec3d.d, this.locZ) < d0 * d0) {
                this.pathEntity.a();
                if (this.pathEntity.b()) {
                    vec3d = null;
                    this.pathEntity = null;
                } else {
                    vec3d = this.pathEntity.a((Entity) this);
                }
            }

            this.bF = false;
            if (vec3d != null) {
                double d1 = vec3d.c - this.locX;
                double d2 = vec3d.e - this.locZ;
                double d3 = vec3d.d - (double) i;
                // CraftBukkit - Math -> TrigMath
                float f2 = (float) (org.bukkit.craftbukkit.TrigMath.atan2(d2, d1) * 180.0D / 3.1415927410125732D) - 90.0F;
                float f3 = MathHelper.g(f2 - this.yaw);

                this.bD = this.bH;
                if (f3 > 30.0F) {
                    f3 = 30.0F;
                }

                if (f3 < -30.0F) {
                    f3 = -30.0F;
                }

                this.yaw += f3;
                if (this.b && this.target != null) {
                    double d4 = this.target.locX - this.locX;
                    double d5 = this.target.locZ - this.locZ;
                    float f4 = this.yaw;

                    this.yaw = (float) (Math.atan2(d5, d4) * 180.0D / 3.1415927410125732D) - 90.0F;
                    f3 = (f4 - this.yaw + 90.0F) * 3.1415927F / 180.0F;
                    this.bC = -MathHelper.sin(f3) * this.bC * 1.0F;
                    this.bD = MathHelper.cos(f3) * this.bC * 1.0F;
                }

                if (d3 > 0.0D) {
                    this.bF = true;
                }
            }

            if (this.target != null) {
                this.a(this.target, 30.0F, 30.0F);
            }

            if (this.positionChanged && !this.k()) {
                this.bF = true;
            }

            if (this.random.nextFloat() < 0.8F && (flag || flag1)) {
                this.bF = true;
            }

            this.world.methodProfiler.b();
        } else {
            super.bn();
            this.pathEntity = null;
        }
    }

    protected void i() {
        this.world.methodProfiler.a("stroll");
        boolean flag = false;
        int i = -1;
        int j = -1;
        int k = -1;
        float f = -99999.0F;

        for (int l = 0; l < 10; ++l) {
            int i1 = MathHelper.floor(this.locX + (double) this.random.nextInt(13) - 6.0D);
            int j1 = MathHelper.floor(this.locY + (double) this.random.nextInt(7) - 3.0D);
            int k1 = MathHelper.floor(this.locZ + (double) this.random.nextInt(13) - 6.0D);
            float f1 = this.a(i1, j1, k1);

            if (f1 > f) {
                f = f1;
                i = i1;
                j = j1;
                k = k1;
                flag = true;
            }
        }

        if (flag) {
            this.pathEntity = this.world.a(this, i, j, k, 10.0F, true, false, false, true);
        }

        this.world.methodProfiler.b();
    }

    protected void a(Entity entity, float f) {}

    public float a(int i, int j, int k) {
        return 0.0F;
    }

    protected Entity findTarget() {
        return null;
    }

    public boolean canSpawn() {
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.boundingBox.b);
        int k = MathHelper.floor(this.locZ);

        return super.canSpawn() && this.a(i, j, k) >= 0.0F;
    }

    public boolean k() {
        return this.pathEntity != null;
    }

    public void setPathEntity(PathEntity pathentity) {
        this.pathEntity = pathentity;
    }

    public Entity l() {
        return this.target;
    }

    public void setTarget(Entity entity) {
        this.target = entity;
    }

    public float bB() {
        float f = super.bB();

        if (this.c > 0 && !this.be()) {
            f *= 2.0F;
        }

        return f;
    }
}
