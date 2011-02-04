package net.minecraft.server;

//CraftBukkit start
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.event.entity.ExplosionPrimedEvent;
// CraftBukkit end

public class EntityTNTPrimed extends Entity {

    public int a;

    public EntityTNTPrimed(World world) {
        super(world);
        this.a = 0;
        this.i = true;
        this.a(0.98F, 0.98F);
        this.height = this.width / 2.0F;
    }

    public EntityTNTPrimed(World world, double d0, double d1, double d2) {
        this(world);
        this.a(d0, d1, d2);
        float f = (float) (Math.random() * 3.1415927410125732D * 2.0D);

        this.motX = (double) (-MathHelper.a(f * 3.1415927F / 180.0F) * 0.02F);
        this.motY = 0.20000000298023224D;
        this.motZ = (double) (-MathHelper.b(f * 3.1415927F / 180.0F) * 0.02F);
        this.M = false;
        this.a = 80;
        this.lastX = d0;
        this.lastY = d1;
        this.lastZ = d2;
    }

    protected void a() {}

    public boolean c_() {
        return !this.dead;
    }

    public void b_() {
        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        this.motY -= 0.03999999910593033D;
        this.c(this.motX, this.motY, this.motZ);
        this.motX *= 0.9800000190734863D;
        this.motY *= 0.9800000190734863D;
        this.motZ *= 0.9800000190734863D;
        if (this.onGround) {
            this.motX *= 0.699999988079071D;
            this.motZ *= 0.699999988079071D;
            this.motY *= -0.5D;
        }

        if (this.a-- <= 0) {
            // Craftbukkit start
            // Need to reverse the order of the explosion and the entity death so we have a location for the event.
            this.d();
            this.q();
            // Craftbukkit end
        } else {
            this.world.a("smoke", this.locX, this.locY + 0.5D, this.locZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private void d() {
        // Craftbukkit start
        CraftServer server = ((WorldServer) this.world).getServer();
        org.bukkit.event.Event.Type eventType = ExplosionPrimedEvent.Type.EXPLOSION_PRIMED;
        ExplosionPrimedEvent event = new ExplosionPrimedEvent(eventType, CraftEntity.getEntity(server, this), 4.0F, false); 
        server.getPluginManager().callEvent(event);
        if(!event.isCancelled()) {
            this.world.a((Entity) null, this.locX, this.locY, this.locZ, event.getRadius(), event.getFire());
        }
        // Craftbukkit end
    }

    protected void a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("Fuse", (byte) this.a);
    }

    protected void b(NBTTagCompound nbttagcompound) {
        this.a = nbttagcompound.b("Fuse");
    }
}
