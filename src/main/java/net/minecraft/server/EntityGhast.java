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
    }

    protected void b() {
        super.b();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
    }

    public void o_() {
        super.o_();
        byte b0 = this.datawatcher.a(16);

        this.texture = b0 == 1 ? "/mob/ghast_fire.png" : "/mob/ghast.png";
    }

    protected void c_() {
        if (!this.world.isStatic && this.world.spawnMonsters == 0) {
            this.die();
        }

        this.T();
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
            Entity target = this.world.findNearbyPlayer(this, 100.0D);
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

        if (this.target != null && this.target.g(this) < d4 * d4) {
            double d5 = this.target.locX - this.locX;
            double d6 = this.target.boundingBox.b + (double) (this.target.width / 2.0F) - (this.locY + (double) (this.width / 2.0F));
            double d7 = this.target.locZ - this.locZ;

            this.K = this.yaw = -((float) Math.atan2(d5, d7)) * 180.0F / 3.1415927F;
            if (this.e(this.target)) {
                if (this.f == 10) {
                    this.world.makeSound(this, "mob.ghast.charge", this.k(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                }

                ++this.f;
                if (this.f == 20) {
                    this.world.makeSound(this, "mob.ghast.fireball", this.k(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                    EntityFireball entityfireball = new EntityFireball(this.world, this, d5, d6, d7);
                    double d8 = 4.0D;
                    Vec3D vec3d = this.b(1.0F);

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
            this.K = this.yaw = -((float) Math.atan2(this.motX, this.motZ)) * 180.0F / 3.1415927F;
            if (this.f > 0) {
                --this.f;
            }
        }

        if (!this.world.isStatic) {
            byte b0 = this.datawatcher.a(16);
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

    protected String g() {
        return "mob.ghast.moan";
    }

    protected String h() {
        return "mob.ghast.scream";
    }

    protected String i() {
        return "mob.ghast.death";
    }

    protected int j() {
        return Item.SULPHUR.id;
    }

    protected float k() {
        return 10.0F;
    }

    public boolean d() {
        return this.random.nextInt(20) == 0 && super.d() && this.world.spawnMonsters > 0;
    }

    public int l() {
        return 1;
    }
}
