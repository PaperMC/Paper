package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftLivingEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityCombustEvent;
// CraftBukkit end

public class EntityZombie extends EntityMobs {

    public EntityZombie(World world) {
        super(world);
        aP = "/mob/zombie.png";
        bC = 0.5F;
        c = 5;
    }

    public void o() {
        if (l.b()) {
            float f1 = b(1.0F);

            if (f1 > 0.5F && l.i(MathHelper.b(p), MathHelper.b(q), MathHelper.b(r)) && W.nextFloat() * 30F < (f1 - 0.4F) * 2.0F) {
                // CraftBukkit start
                CraftServer server = ((WorldServer) l).getServer();
                EntityCombustEvent event = new EntityCombustEvent(Type.ENTITY_COMBUST, new CraftLivingEntity(server, (EntityLiving) this));
                server.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    Z = 300;
                }
                // CraftBukkit end
            }
        }
        super.o();
    }

    protected String e() {
        return "mob.zombie";
    }

    protected String f() {
        return "mob.zombiehurt";
    }

    protected String g() {
        return "mob.zombiedeath";
    }

    protected int h() {
        return Item.J.ba;
    }
}
