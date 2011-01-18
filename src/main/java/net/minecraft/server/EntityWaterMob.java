package net.minecraft.server;

//CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftWaterMob;
//CraftBukkit stop

public class EntityWaterMob extends EntityCreature implements IAnimals {

    public EntityWaterMob(World world) {
        super(world);
        //CraftBukkit start
        CraftServer server = ((WorldServer) this.l).getServer();
        this.bukkitEntity = new CraftWaterMob(server, this);
        //CraftBukkit end
    }

    public boolean d_() {
        return true;
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    public boolean b() {
        return l.a(z);
    }

    public int c() {
        return 120;
    }
}
