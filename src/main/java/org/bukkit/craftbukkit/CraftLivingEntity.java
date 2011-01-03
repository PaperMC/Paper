
package org.bukkit.craftbukkit;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityEgg;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntitySnowball;

import org.bukkit.Egg;
import org.bukkit.LivingEntity;
import org.bukkit.Snowball;

public class CraftLivingEntity extends CraftEntity implements LivingEntity {
    private EntityLiving entity;

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

    public void setHandle(final EntityLiving entity) {
        super.setHandle((Entity)entity);
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "CraftLivingEntity{" + "id=" + getEntityID() + '}';
    }

    @Override
    public Egg throwEgg() {
        net.minecraft.server.World world = ((CraftWorld)getWorld()).getHandle();
        EntityEgg egg = new EntityEgg(world, entity);
        world.a(egg);
        return new CraftEgg(server, egg);
    }

    @Override
    public Snowball throwSnowball() {
        net.minecraft.server.World world = ((CraftWorld)getWorld()).getHandle();
        EntitySnowball snowball = new EntitySnowball(world, entity);
        world.a(snowball);
        return new CraftSnowball(server, snowball);
    }
}
