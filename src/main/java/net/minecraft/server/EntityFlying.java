package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftFlying;
// CraftBukkit end

public class EntityFlying extends EntityLiving {

    public EntityFlying(World world) {
        super(world);

        // CraftBukkit start
        CraftServer server = ((WorldServer) this.world).getServer();
        this.bukkitEntity = new CraftFlying(server, this);
        // CraftBukkit end
    }

    protected void a(float f) {}

    public void c(float f, float f1) {
        if (this.v()) {
            this.a(f, f1, 0.02F);
            this.c(this.motX, this.motY, this.motZ);
            this.motX *= 0.800000011920929D;
            this.motY *= 0.800000011920929D;
            this.motZ *= 0.800000011920929D;
        } else if (this.x()) {
            this.a(f, f1, 0.02F);
            this.c(this.motX, this.motY, this.motZ);
            this.motX *= 0.5D;
            this.motY *= 0.5D;
            this.motZ *= 0.5D;
        } else {
            float f2 = 0.91F;

            if (this.onGround) {
                f2 = 0.54600006F;
                int i = this.world.getTypeId(MathHelper.b(this.locX), MathHelper.b(this.boundingBox.b) - 1, MathHelper.b(this.locZ));

                if (i > 0) {
                    f2 = Block.byId[i].frictionFactor * 0.91F;
                }
            }

            float f3 = 0.16277136F / (f2 * f2 * f2);

            this.a(f, f1, this.onGround ? 0.1F * f3 : 0.02F);
            f2 = 0.91F;
            if (this.onGround) {
                f2 = 0.54600006F;
                int j = this.world.getTypeId(MathHelper.b(this.locX), MathHelper.b(this.boundingBox.b) - 1, MathHelper.b(this.locZ));

                if (j > 0) {
                    f2 = Block.byId[j].frictionFactor * 0.91F;
                }
            }

            this.c(this.motX, this.motY, this.motZ);
            this.motX *= (double) f2;
            this.motY *= (double) f2;
            this.motZ *= (double) f2;
        }

        this.bl = this.bm;
        double d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        float f4 = MathHelper.a(d0 * d0 + d1 * d1) * 4.0F;

        if (f4 > 1.0F) {
            f4 = 1.0F;
        }

        this.bm += (f4 - this.bm) * 0.4F;
        this.bn += this.bm;
    }

    public boolean m() {
        return false;
    }
}
