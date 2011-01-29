package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftGiant;
// CraftBukkit stop

public class EntityGiantZombie extends EntityMonster {

    public EntityGiantZombie(World world) {
        super(world);
        this.texture = "/mob/zombie.png";
        this.bC = 0.5F;
        this.c = 50;
        this.health *= 10;
        this.height *= 6.0F;
        this.a(this.length * 6.0F, this.width * 6.0F);

        // CraftBukkit start
        CraftServer server = ((WorldServer) this.world).getServer();
        this.bukkitEntity = new CraftGiant(server, this);
        // CraftBukkit end
    }

    protected float a(int i, int j, int k) {
        return this.world.l(i, j, k) - 0.5F;
    }
}
