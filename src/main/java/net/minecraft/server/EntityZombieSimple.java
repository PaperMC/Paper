package net.minecraft.server;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftGiant;

public class EntityZombieSimple extends EntityMobs {

    public EntityZombieSimple(World world) {
        super(world);
        aP = "/mob/zombie.png";
        bC = 0.5F;
        c = 50;
        aZ *= 10;
        H *= 6F;
        a(I * 6F, J * 6F);
        //CraftBukkit start
        CraftServer server = ((WorldServer) this.l).getServer();
        this.bukkitEntity = new CraftGiant(server, this);
        //CraftBukkit end
    }

    protected float a(int i, int j, int k) {
        return l.l(i, j, k) - 0.5F;
    }
}
