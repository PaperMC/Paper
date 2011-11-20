package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.event.entity.EntityTargetEvent;
// CraftBukkit end

public class EntityGhast extends EntityFlying implements IMonster {

    public int a = 0;
    public double b;
    public double c;
    public double d;
    private Entity target = null;
    private int h = 0;
    public int e = 0;
    public int f = 0;

    public EntityGhast(World world) {
        super(world);
        this.texture = "/mob/ghast.png";
        this.b(4.0F, 4.0F);
        this.fireProof = true;
        this.az = 5;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if ("fireball".equals(damagesource.l()) && damagesource.getEntity() instanceof EntityHuman) {
            super.damageEntity(damagesource, 1000);
            ((EntityHuman) damagesource.getEntity()).a((Statistic) AchievementList.y);
            return true;
        } else {
            return super.damageEntity(damagesource, i);
        }
    }

    protected void b() {
        super.b();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
    }

    public int getMaxHealth() {
        return 10;
    }

    public void w_() {
        super.w_();
        byte b0 = this.datawatcher.getByte(16);

        this.texture = b0 == 1 ? "/mob/ghast_fire.png" : "/mob/ghast.png";
    }

    protected void m_() {
        if (!this.world.isStatic && this.world.difficulty == 0) {
            this.die();
        }

        this.ak();
        this.e = this.f;
        double d0 = this.b - this.locX;
        double d1 = this.c - this.locY;
        double d2 = this.d - this.locZ;
        double d3 = (double) MathHelper.a(d0 * d0 + d1 * d1 + d2 * d2);

        if (d3 < 1.0D || d3 > 60.0D) {
            this.b = this.locX + (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.c = this.locY + (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.d = this.locZ + (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 16.0F);
        }

        if (this.a-- <= 0) {
            this.a += this.random.nextInt(5) + 2;
            if (this.a(this.b, this.c, this.d, d3)) {
                this.motX += d0 / d3 * 0.1D;
                this.motY += d1 / d3 * 0.1D;
                this.motZ += d2 / d3 * 0.1D;
            } else {
                this.b = this.locX;
                this.c = this.locY;
                this.d = this.locZ;
            }
        }

        if (this.target != null && this.target.dead) {
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

        if (this.target == null || this.h-- <= 0) {
            // CraftBukkit start
            Entity target = this.world.b(this, 100.0D);
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
                this.h = 20;
            }
        }

        double d4 = 64.0D;

        if (this.target != null && this.target.i(this) < d4 * d4) {
            double d5 = this.target.locX - this.locX;
            double d6 = this.target.boundingBox.b + (double) (this.target.width / 2.0F) - (this.locY + (double) (this.width / 2.0F));
            double d7 = this.target.locZ - this.locZ;

            this.V = this.yaw = -((float) Math.atan2(d5, d7)) * 180.0F / 3.1415927F;
            if (this.g(this.target)) {
                if (this.f == 10) {
                    this.world.a((EntityHuman) null, 1007, (int) this.locX, (int) this.locY, (int) this.locZ, 0);
                }

                ++this.f;
                if (this.f == 20) {
                    this.world.a((EntityHuman) null, 1008, (int) this.locX, (int) this.locY, (int) this.locZ, 0);
                    EntityFireball entityfireball = new EntityFireball(this.world, this, d5, d6, d7);
                    double d8 = 4.0D;
                    Vec3D vec3d = this.d(1.0F);

                    entityfireball.locX = this.locX + vec3d.a * d8;
                    entityfireball.locY = this.locY + (double) (this.width / 2.0F) + 0.5D;
                    entityfireball.locZ = this.locZ + vec3d.c * d8;
                    this.world.addEntity(entityfireball);
                    this.f = -40;
                }
            } else if (this.f > 0) {
                --this.f;
            }
        } else {
            this.V = this.yaw = -((float) Math.atan2(this.motX, this.motZ)) * 180.0F / 3.1415927F;
            if (this.f > 0) {
                --this.f;
            }
        }

        if (!this.world.isStatic) {
            byte b0 = this.datawatcher.getByte(16);
            byte b1 = (byte) (this.f > 10 ? 1 : 0);

            if (b0 != b1) {
                this.datawatcher.watch(16, Byte.valueOf(b1));
            }
        }
    }

    private boolean a(double d0, double d1, double d2, double d3) {
        double d4 = (this.b - this.locX) / d3;
        double d5 = (this.c - this.locY) / d3;
        double d6 = (this.d - this.locZ) / d3;
        AxisAlignedBB axisalignedbb = this.boundingBox.clone();

        for (int i = 1; (double) i < d3; ++i) {
            axisalignedbb.d(d4, d5, d6);
            if (this.world.getEntities(this, axisalignedbb).size() > 0) {
                return false;
            }
        }

        return true;
    }

    protected String c_() {
        return "mob.ghast.moan";
    }

    protected String m() {
        return "mob.ghast.scream";
    }

    protected String n() {
        return "mob.ghast.death";
    }

    protected int e() {
        return Item.SULPHUR.id;
    }

    protected void a(boolean flag, int i) {
        int j = this.random.nextInt(2) + this.random.nextInt(1 + i);

        int k;

        for (k = 0; k < j; ++k) {
            this.b(Item.GHAST_TEAR.id, 1);
        }

        j = this.random.nextInt(3) + this.random.nextInt(1 + i);

        for (k = 0; k < j; ++k) {
            this.b(Item.SULPHUR.id, 1);
        }
    }

    protected float o() {
        return 10.0F;
    }

    public boolean g() {
        return this.random.nextInt(20) == 0 && super.g() && this.world.difficulty > 0;
    }

    public int p() {
        return 1;
    }
}
