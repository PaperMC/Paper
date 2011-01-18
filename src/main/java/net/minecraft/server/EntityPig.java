package net.minecraft.server;

//CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPig;
//CraftBukkit end

public class EntityPig extends EntityAnimals {

    public EntityPig(World world) {
        super(world);
        aP = "/mob/pig.png";
        a(0.9F, 0.9F);
        //CraftBukkit start
        CraftServer server = ((WorldServer) this.l).getServer();
        this.bukkitEntity = new CraftPig(server, this);
        //CraftBukkit end
    }

    protected void a() {
        af.a(16, ((Byte.valueOf((byte) 0))));
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        nbttagcompound.a("Saddle", K());
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        a(nbttagcompound.l("Saddle"));
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

    public boolean a(EntityPlayer entityplayer) {
        if (K() && !l.z && (j == null || j == entityplayer)) {
            entityplayer.e(((Entity) (this)));
            return true;
        } else {
            return false;
        }
    }

    protected int h() {
        return Item.ao.ba;
    }

    public boolean K() {
        return (af.a(16) & 1) != 0;
    }

    public void a(boolean flag) {
        if (flag) {
            af.b(16, ((Byte.valueOf((byte) 1))));
        } else {
            af.b(16, ((Byte.valueOf((byte) 0))));
        }
    }
}
