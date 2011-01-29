package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPig;
// CraftBukkit end

public class EntityPig extends EntityAnimal {

    public EntityPig(World world) {
        super(world);
        this.texture = "/mob/pig.png";
        this.a(0.9F, 0.9F);

        // CraftBukkit start
        CraftServer server = ((WorldServer) this.world).getServer();
        this.bukkitEntity = new CraftPig(server, this);
        // CraftBukkit end
    }

    protected void a() {
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        nbttagcompound.a("Saddle", this.K());
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        this.a(nbttagcompound.l("Saddle"));
    }

    protected String e() {
        return "mob.pig";
    }

    protected String f() {
        return "mob.pig";
    }

    protected String g() {
        return "mob.pigdeath";
    }

    public boolean a(EntityHuman entityhuman) {
        if (this.K() && !this.world.isStatic && (this.passenger == null || this.passenger == entityhuman)) {
            entityhuman.e(this);
            return true;
        } else {
            return false;
        }
    }

    protected int h() {
        return Item.PORK.id;
    }

    public boolean K() {
        return (this.datawatcher.a(16) & 1) != 0;
    }

    public void a(boolean flag) {
        if (flag) {
            this.datawatcher.b(16, Byte.valueOf((byte) 1));
        } else {
            this.datawatcher.b(16, Byte.valueOf((byte) 0));
        }
    }
}
