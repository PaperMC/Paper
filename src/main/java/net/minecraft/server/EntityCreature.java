package net.minecraft.server;

import java.util.UUID;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityUnleashEvent;
// CraftBukkit end

public abstract class EntityCreature extends EntityInsentient {

    public static final UUID h = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
    public static final AttributeModifier i = (new AttributeModifier(h, "Fleeing speed bonus", 2.0D, 2)).a(false);
    public PathEntity pathEntity; // CraftBukkit - private -> public
    public Entity target; // CraftBukkit - protected -> public
    protected boolean bn;
    protected int bo;
    private ChunkCoordinates bq = new ChunkCoordinates(0, 0, 0);
    private float br = -1.0F;
    private PathfinderGoal bs = new PathfinderGoalMoveTowardsRestriction(this, 1.0D);
    private boolean bt;

    public EntityCreature(World world) {
        super(world);
    }

    protected boolean bJ() {
        return false;
    }

    protected void bl() {
        this.world.methodProfiler.a("ai");
        if (this.bo > 0 && --this.bo == 0) {
            AttributeInstance attributeinstance = this.getAttributeInstance(GenericAttributes.d);

            attributeinstance.b(i);
        }

        this.bn = this.bJ();
        float f11 = 16.0F;

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
                this.pathEntity = this.world.findPath(this, this.target, f11, true, false, false, true);
            }
        } else if (this.target.isAlive()) {
            float f1 = this.target.d((Entity) this);

            if (this.o(this.target)) {
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
        if (!this.bn && this.target != null && (this.pathEntity == null || this.random.nextInt(20) == 0)) {
            this.pathEntity = this.world.findPath(this, this.target, f11, true, false, false, true);
        } else if (!this.bn && (this.pathEntity == null && this.random.nextInt(180) == 0 || this.random.nextInt(120) == 0 || this.bo > 0) && this.aV < 100) {
            this.bK();
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

            this.bd = false;
            if (vec3d != null) {
                double d1 = vec3d.c - this.locX;
                double d2 = vec3d.e - this.locZ;
                double d3 = vec3d.d - (double) i;
                // CraftBukkit - Math -> TrigMath
                float f2 = (float) (org.bukkit.craftbukkit.TrigMath.atan2(d2, d1) * 180.0D / 3.1415927410125732D) - 90.0F;
                float f3 = MathHelper.g(f2 - this.yaw);

                this.bf = (float) this.getAttributeInstance(GenericAttributes.d).getValue();
                if (f3 > 30.0F) {
                    f3 = 30.0F;
                }

                if (f3 < -30.0F) {
                    f3 = -30.0F;
                }

                this.yaw += f3;
                if (this.bn && this.target != null) {
                    double d4 = this.target.locX - this.locX;
                    double d5 = this.target.locZ - this.locZ;
                    float f4 = this.yaw;

                    this.yaw = (float) (Math.atan2(d5, d4) * 180.0D / 3.1415927410125732D) - 90.0F;
                    f3 = (f4 - this.yaw + 90.0F) * 3.1415927F / 180.0F;
                    this.be = -MathHelper.sin(f3) * this.bf * 1.0F;
                    this.bf = MathHelper.cos(f3) * this.bf * 1.0F;
                }

                if (d3 > 0.0D) {
                    this.bd = true;
                }
            }

            if (this.target != null) {
                this.a(this.target, 30.0F, 30.0F);
            }

            if (this.positionChanged && !this.bM()) {
                this.bd = true;
            }

            if (this.random.nextFloat() < 0.8F && (flag || flag1)) {
                this.bd = true;
            }

            this.world.methodProfiler.b();
        } else {
            super.bl();
            this.pathEntity = null;
        }
    }

    protected void bK() {
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

    public boolean bM() {
        return this.pathEntity != null;
    }

    public void setPathEntity(PathEntity pathentity) {
        this.pathEntity = pathentity;
    }

    public Entity bN() {
        return this.target;
    }

    public void setTarget(Entity entity) {
        this.target = entity;
    }

    public boolean bO() {
        return this.b(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
    }

    public boolean b(int i, int j, int k) {
        return this.br == -1.0F ? true : this.bq.e(i, j, k) < this.br * this.br;
    }

    public void b(int i, int j, int k, int l) {
        this.bq.b(i, j, k);
        this.br = (float) l;
    }

    public ChunkCoordinates bP() {
        return this.bq;
    }

    public float bQ() {
        return this.br;
    }

    public void bR() {
        this.br = -1.0F;
    }

    public boolean bS() {
        return this.br != -1.0F;
    }

    protected void bF() {
        super.bF();
        if (this.bH() && this.getLeashHolder() != null && this.getLeashHolder().world == this.world) {
            Entity entity = this.getLeashHolder();

            this.b((int) entity.locX, (int) entity.locY, (int) entity.locZ, 5);
            float f = this.d(entity);

            if (this instanceof EntityTameableAnimal && ((EntityTameableAnimal) this).isSitting()) {
                if (f > 10.0F) {
                    this.world.getServer().getPluginManager().callEvent(new EntityUnleashEvent(this.getBukkitEntity(), EntityUnleashEvent.UnleashReason.DISTANCE)); // CraftBukkit
                    this.unleash(true, true);
                }

                return;
            }

            if (!this.bt) {
                this.goalSelector.a(2, this.bs);
                this.getNavigation().a(false);
                this.bt = true;
            }

            this.o(f);
            if (f > 4.0F) {
                this.getNavigation().a(entity, 1.0D);
            }

            if (f > 6.0F) {
                double d0 = (entity.locX - this.locX) / (double) f;
                double d1 = (entity.locY - this.locY) / (double) f;
                double d2 = (entity.locZ - this.locZ) / (double) f;

                this.motX += d0 * Math.abs(d0) * 0.4D;
                this.motY += d1 * Math.abs(d1) * 0.4D;
                this.motZ += d2 * Math.abs(d2) * 0.4D;
            }

            if (f > 10.0F) {
                this.world.getServer().getPluginManager().callEvent(new EntityUnleashEvent(this.getBukkitEntity(), EntityUnleashEvent.UnleashReason.DISTANCE)); // CraftBukkit
                this.unleash(true, true);
            }
        } else if (!this.bH() && this.bt) {
            this.bt = false;
            this.goalSelector.a(this.bs);
            this.getNavigation().a(true);
            this.bR();
        }
    }

    protected void o(float f) {}
}
