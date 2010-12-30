
package org.bukkit.craftbukkit;

import net.minecraft.server.EntityLiving;
import org.bukkit.LivingEntity;

public class CraftLivingEntity extends CraftEntity implements LivingEntity {
    private final EntityLiving entity;
    
    public CraftLivingEntity(final CraftServer server, final EntityLiving entity) {
        super(server, entity);
        this.entity = entity;
    }

    public int getHealth() {
        return entity.ba;
    }

    public void setHealth(int health) {
        if ((health < 0) || (health > 20)) {
            throw new IllegalArgumentException("Health must be between 0 and 20");
        }

        entity.ba = health;
    }

    @Override
    public EntityLiving getHandle() {
        return entity;
    }

    @Override
    public String toString() {
        return "CraftLivingEntity{" + "id=" + getEntityID() + '}';
    }
}
