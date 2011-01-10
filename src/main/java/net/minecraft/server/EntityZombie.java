package net.minecraft.server;


import java.util.Random;

import org.bukkit.craftbukkit.CraftLivingEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityCombustEvent;


public class EntityZombie extends EntityMobs {

    public EntityZombie(World world) {
        super(world);
        aQ = "/mob/zombie.png";
        bD = 0.5F;
        f = 5;
    }

    public void G() {
        if (l.b()) {
            float f1 = b(1.0F);

            if (f1 > 0.5F && l.h(MathHelper.b(p), MathHelper.b(q), MathHelper.b(r)) && W.nextFloat() * 30F < (f1 - 0.4F) * 2.0F) {
                //Craftbukkit start
                CraftServer server = ((WorldServer) l).getServer();
                EntityCombustEvent event = new EntityCombustEvent(Type.ENTITY_COMBUST, new CraftLivingEntity(server, (EntityLiving) this));
                server.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    Z = 300;
                }
                //Craftbukkit stop
            }
        }
        super.G();
    }

    protected String d() {
        return "mob.zombie";
    }

    protected String e() {
        return "mob.zombiehurt";
    }

    protected String f() {
        return "mob.zombiedeath";
    }

    protected int g() {
        return Item.J.aW;
    }
}

