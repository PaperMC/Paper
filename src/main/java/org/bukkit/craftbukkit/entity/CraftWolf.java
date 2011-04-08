
package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityWolf;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Wolf;
import net.minecraft.server.PathEntity;

public class CraftWolf extends CraftAnimals implements Wolf {
    public CraftWolf(CraftServer server, EntityWolf wolf) {
        super(server, wolf);
    }

    public boolean isAngry() {
        return getHandle().isAngry();
    }

    public void setAngry(boolean angry) {
        getHandle().setAngry(angry);
    }

    public boolean isSitting() {
        return getHandle().isSitting();
    }

    public void setSitting(boolean sitting) {
        getHandle().setSitting(sitting);
    }

    public boolean isTame() {
        return getHandle().m_();
    }
    
    public void setTame(boolean tame) {
        getHandle().d(tame);
    }
    
    public String getOwner() {
        return getHandle().x();
    }
    
    public void setOwner(String player) {
        EntityWolf e = getHandle();

        if ((player != null) && (player.length() > 0)) {
            e.d(true); /* Make him tame */
            e.a((PathEntity)null); /* Clear path */
            e.a(player); /* Set owner */
        }
        else {
            e.d(false); /* Make him not tame */
            e.a(""); /* Clear owner */
        }
    }

    @Override
    public EntityWolf getHandle() {
        return (EntityWolf) entity;
    }

    @Override
    public String toString() {
        return "CraftWolf[anger=" + isAngry() + ",owner=" + getOwner() + ",tame=" + isTame() + ",sitting=" + isSitting() + "]";
    }
}
