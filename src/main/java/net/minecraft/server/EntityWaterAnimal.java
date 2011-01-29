package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftWaterMob;
// CraftBukkit stop

public class EntityWaterAnimal extends EntityCreature implements IAnimal {

    public EntityWaterAnimal(World world) {
        super(world);

        // CraftBukkit start
        CraftServer server = ((WorldServer) this.world).getServer();
        this.bukkitEntity = new CraftWaterMob(server, this);
        // CraftBukkit end
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
        return this.world.a(this.boundingBox);
    }

    public int c() {
        return 120;
    }
}
