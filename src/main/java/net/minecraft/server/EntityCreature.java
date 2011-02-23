package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.TrigMath;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
// CraftBukkit end

public class EntityCreature extends EntityLiving {

    public PathEntity a; // Craftbukkit - public
    public Entity d; // Craftbukkit - public
    protected boolean e = false;

    public EntityCreature(World world) {
        super(world);
    }

    protected void c_() {
        this.e = false;
        float f = 16.0F;

        if (this.d == null) {
            // CraftBukkit start
            Entity target = this.l();
            if (target != null) {
                EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), target.getBukkitEntity(), TargetReason.CLOSEST_PLAYER);
                CraftServer server = ((WorldServer) this.world).getServer();
                server.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    if (event.getTarget() == null) {
                        this.d = null;
                    } else {
                        this.d = ((CraftEntity) event.getTarget()).getHandle();
                    }
                }
            }
            // CraftBukkit end
            if (this.d != null) {
                this.a = this.world.a(this, this.d, f);
            }
        } else if (!this.d.J()) {
            // CraftBukkit start
            EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), null, TargetReason.TARGET_DIED);
            CraftServer server = ((WorldServer) this.world).getServer();
            server.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                if (event.getTarget() == null) {
                    this.d = null;
                } else {
                    this.d = ((CraftEntity) event.getTarget()).getHandle();
                }
            }
            // CraftBukkit end
        } else {
            float f1 = this.d.f(this);

            if (this.e(this.d)) {
                this.a(this.d, f1);
            }
        }

        if (!this.e && this.d != null && (this.a == null || this.random.nextInt(20) == 0)) {
            this.a = this.world.a(this, this.d, f);
        } else if (this.a == null && this.random.nextInt(80) == 0 || this.random.nextInt(80) == 0) {
            boolean flag = false;
            int i = -1;
            int j = -1;
            int k = -1;
            float f2 = -99999.0F;

            for (int l = 0; l < 10; ++l) {
                int i1 = MathHelper.b(this.locX + (double) this.random.nextInt(13) - 6.0D);
                int j1 = MathHelper.b(this.locY + (double) this.random.nextInt(7) - 3.0D);
                int k1 = MathHelper.b(this.locZ + (double) this.random.nextInt(13) - 6.0D);
                float f3 = this.a(i1, j1, k1);

                if (f3 > f2) {
                    f2 = f3;
                    i = i1;
                    j = j1;
                    k = k1;
                    flag = true;
                }
            }

            if (flag) {
                this.a = this.world.a(this, i, j, k, 10.0F);
            }
        }

        int l1 = MathHelper.b(this.boundingBox.b);
        boolean flag1 = this.g_();
        boolean flag2 = this.Q();

        this.pitch = 0.0F;
        if (this.a != null && this.random.nextInt(100) != 0) {
            Vec3D vec3d = this.a.a(this);
            double d0 = (double) (this.length * 2.0F);

            while (vec3d != null && vec3d.d(this.locX, vec3d.b, this.locZ) < d0 * d0) {
                this.a.a();
                if (this.a.b()) {
                    vec3d = null;
                    this.a = null;
                } else {
                    vec3d = this.a.a(this);
                }
            }

            this.ax = false;
            if (vec3d != null) {
                double d1 = vec3d.a - this.locX;
                double d2 = vec3d.c - this.locZ;
                double d3 = vec3d.b - (double) l1;
                float f4 = (float) (TrigMath.atan2(d2, d1) * 180.0D / 3.1415927410125732D) - 90.0F; // Craftbukkit
                float f5 = f4 - this.yaw;

                for (this.av = this.az; f5 < -180.0F; f5 += 360.0F) {
                    ;
                }

                while (f5 >= 180.0F) {
                    f5 -= 360.0F;
                }

                if (f5 > 30.0F) {
                    f5 = 30.0F;
                }

                if (f5 < -30.0F) {
                    f5 = -30.0F;
                }

                this.yaw += f5;
                if (this.e && this.d != null) {
                    double d4 = this.d.locX - this.locX;
                    double d5 = this.d.locZ - this.locZ;
                    float f6 = this.yaw;

                    this.yaw = (float) (Math.atan2(d5, d4) * 180.0D / 3.1415927410125732D) - 90.0F;
                    f5 = (f6 - this.yaw + 90.0F) * 3.1415927F / 180.0F;
                    this.au = -MathHelper.a(f5) * this.av * 1.0F;
                    this.av = MathHelper.b(f5) * this.av * 1.0F;
                }

                if (d3 > 0.0D) {
                    this.ax = true;
                }
            }

            if (this.d != null) {
                this.b(this.d, 30.0F);
            }

            if (this.aV) {
                this.ax = true;
            }

            if (this.random.nextFloat() < 0.8F && (flag1 || flag2)) {
                this.ax = true;
            }
        } else {
            super.c_();
            this.a = null;
        }
    }

    protected void a(Entity entity, float f) {}

    protected float a(int i, int j, int k) {
        return 0.0F;
    }

    protected Entity l() {
        return null;
    }

    public boolean b() {
        int i = MathHelper.b(this.locX);
        int j = MathHelper.b(this.boundingBox.b);
        int k = MathHelper.b(this.locZ);

        return super.b() && this.a(i, j, k) >= 0.0F;
    }
}
