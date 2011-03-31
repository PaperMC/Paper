package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.event.entity.ExplosionPrimeEvent;
// CraftBukkit end

public class EntityCreeper extends EntityMonster {

    int a;
    int b;

    public EntityCreeper(World world) {
        super(world);
        this.texture = "/mob/creeper.png";
    }

    protected void a() {
        super.a();
        this.datawatcher.a(16, Byte.valueOf((byte) -1));
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    public void f_() {
        this.b = this.a;
        if (this.world.isStatic) {
            int i = this.v();

            if (i > 0 && this.a == 0) {
                this.world.a(this, "random.fuse", 1.0F, 0.5F);
            }

            this.a += i;
            if (this.a < 0) {
                this.a = 0;
            }

            if (this.a >= 30) {
                this.a = 30;
            }
        }

        super.f_();
    }

    protected String f() {
        return "mob.creeper";
    }

    protected String g() {
        return "mob.creeperdeath";
    }

    public void a(Entity entity) {
        super.a(entity);
        if (entity instanceof EntitySkeleton) {
            this.b(Item.GOLD_RECORD.id + this.random.nextInt(2), 1);
        }
    }

    protected void a(Entity entity, float f) {
        int i = this.v();

        if ((i > 0 || f >= 3.0F) && (i <= 0 || f >= 7.0F)) {
            this.e(-1);
            --this.a;
            if (this.a < 0) {
                this.a = 0;
            }
        } else {
            if (this.a == 0) {
                this.world.a(this, "random.fuse", 1.0F, 0.5F);
            }

            this.e(1);
            ++this.a;
            if (this.a >= 30) {
                // CraftBukkit start
                CraftServer server = ((WorldServer) this.world).getServer();

                ExplosionPrimeEvent event = new ExplosionPrimeEvent(CraftEntity.getEntity(server, this), 3.0F, false);
                server.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.world.a(this, this.locX, this.locY, this.locZ, event.getRadius(), event.getFire());
                    this.D();
                } else {
                    this.a = 0;
                }
                // CraftBukkit end
            }

            this.e = true;
        }
    }

    protected int h() {
        return Item.SULPHUR.id;
    }

    private int v() {
        return this.datawatcher.a(16);
    }

    private void e(int i) {
        this.datawatcher.b(16, Byte.valueOf((byte) i));
    }
}
