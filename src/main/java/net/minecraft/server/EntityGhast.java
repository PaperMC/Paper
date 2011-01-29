package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftGhast;
// CraftBukkit end

public class EntityGhast extends EntityFlying implements IMonster {

    public int a = 0;
    public double b;
    public double c;
    public double d;
    private Entity ak = null;
    private int al = 0;
    public int e = 0;
    public int f = 0;

    public EntityGhast(World world) {
        super(world);
        this.texture = "/mob/ghast.png";
        this.a(4.0F, 4.0F);
        this.ae = true;

        // CraftBukkit start
        CraftServer server = ((WorldServer) this.world).getServer();
        this.bukkitEntity = new CraftGhast(server, this);
        // CraftBukkit end
    }

    protected void d() {
        if (this.world.k == 0) {
            this.q();
        }

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

        if (this.ak != null && this.ak.dead) {
            this.ak = null;
        }

        if (this.ak == null || this.al-- <= 0) {
            this.ak = this.world.a(this, 100.0D);
            if (this.ak != null) {
                this.al = 20;
            }
        }

        double d4 = 64.0D;

        if (this.ak != null && this.ak.b((Entity) this) < d4 * d4) {
            double d5 = this.ak.locX - this.locX;
            double d6 = this.ak.boundingBox.b + (double) (this.ak.width / 2.0F) - (this.locY + (double) (this.width / 2.0F));
            double d7 = this.ak.locZ - this.locZ;

            this.aI = this.yaw = -((float) Math.atan2(d5, d7)) * 180.0F / 3.1415927F;
            if (this.i(this.ak)) {
                if (this.f == 10) {
                    this.world.a(this, "mob.ghast.charge", this.i(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                }

                ++this.f;
                if (this.f == 20) {
                    this.world.a(this, "mob.ghast.fireball", this.i(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                    EntityFireball entityfireball = new EntityFireball(this.world, this, d5, d6, d7);
                    double d8 = 4.0D;
                    Vec3D vec3d = this.c(1.0F);

                    entityfireball.locX = this.locX + vec3d.a * d8;
                    entityfireball.locY = this.locY + (double) (this.width / 2.0F) + 0.5D;
                    entityfireball.locZ = this.locZ + vec3d.c * d8;
                    this.world.a((Entity) entityfireball);
                    this.f = -40;
                }
            } else if (this.f > 0) {
                --this.f;
            }
        } else {
            this.aI = this.yaw = -((float) Math.atan2(this.motX, this.motZ)) * 180.0F / 3.1415927F;
            if (this.f > 0) {
                --this.f;
            }
        }

        this.texture = this.f > 10 ? "/mob/ghast_fire.png" : "/mob/ghast.png";
    }

    private boolean a(double d0, double d1, double d2, double d3) {
        double d4 = (this.b - this.locX) / d3;
        double d5 = (this.c - this.locY) / d3;
        double d6 = (this.d - this.locZ) / d3;
        AxisAlignedBB axisalignedbb = this.boundingBox.b();

        for (int i = 1; (double) i < d3; ++i) {
            axisalignedbb.d(d4, d5, d6);
            if (this.world.a((Entity) this, axisalignedbb).size() > 0) {
                return false;
            }
        }

        return true;
    }

    protected String e() {
        return "mob.ghast.moan";
    }

    protected String f() {
        return "mob.ghast.scream";
    }

    protected String g() {
        return "mob.ghast.death";
    }

    protected int h() {
        return Item.SULPHUR.id;
    }

    protected float i() {
        return 10.0F;
    }

    public boolean b() {
        return this.random.nextInt(20) == 0 && super.b() && this.world.k > 0;
    }

    public int j() {
        return 1;
    }
}
