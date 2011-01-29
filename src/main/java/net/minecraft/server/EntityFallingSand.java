package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftFallingSand;
// CraftBukkit end

public class EntityFallingSand extends Entity {

    public int a;
    public int b = 0;

    public EntityFallingSand(World world) {
        super(world);

        // CraftBukkit start
        CraftServer server = ((WorldServer) this.world).getServer();
        this.bukkitEntity = new CraftFallingSand(server, this);
        // CraftBukkit end
    }

    public EntityFallingSand(World world, double d0, double d1, double d2, int i) {
        this(world); // CraftBukkit super->this so we assign the entity

        this.a = i;
        this.i = true;
        this.a(0.98F, 0.98F);
        this.height = this.width / 2.0F;
        this.a(d0, d1, d2);
        this.motX = 0.0D;
        this.motY = 0.0D;
        this.motZ = 0.0D;
        this.M = false;
        this.lastX = d0;
        this.lastY = d1;
        this.lastZ = d2;
    }

    protected void a() {}

    public boolean c_() {
        return !this.dead;
    }

    public void b_() {
        if (this.a == 0) {
            this.q();
        } else {
            this.lastX = this.locX;
            this.lastY = this.locY;
            this.lastZ = this.locZ;
            ++this.b;
            this.motY -= 0.03999999910593033D;
            this.c(this.motX, this.motY, this.motZ);
            this.motX *= 0.9800000190734863D;
            this.motY *= 0.9800000190734863D;
            this.motZ *= 0.9800000190734863D;
            int i = MathHelper.b(this.locX);
            int j = MathHelper.b(this.locY);
            int k = MathHelper.b(this.locZ);

            if (this.world.getTypeId(i, j, k) == this.a) {
                this.world.e(i, j, k, 0);
            }

            if (this.onGround) {
                this.motX *= 0.699999988079071D;
                this.motZ *= 0.699999988079071D;
                this.motY *= -0.5D;
                this.q();
                if ((!this.world.a(this.a, i, j, k, true) || !this.world.e(i, j, k, this.a)) && !this.world.isStatic) {
                    this.a(this.a, 1);
                }
            } else if (this.b > 100 && !this.world.isStatic) {
                this.a(this.a, 1);
                this.q();
            }
        }
    }

    protected void a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("Tile", (byte) this.a);
    }

    protected void b(NBTTagCompound nbttagcompound) {
        this.a = nbttagcompound.b("Tile") & 255;
    }
}
