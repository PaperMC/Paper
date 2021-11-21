package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.monster.EntityCreeper;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreeperPowerEvent;

public class CraftCreeper extends CraftMonster implements Creeper {

    public CraftCreeper(CraftServer server, EntityCreeper entity) {
        super(server, entity);
    }

    @Override
    public boolean isPowered() {
        return getHandle().isPowered();
    }

    @Override
    public void setPowered(boolean powered) {
        CreeperPowerEvent.PowerCause cause = powered ? CreeperPowerEvent.PowerCause.SET_ON : CreeperPowerEvent.PowerCause.SET_OFF;

        // only call event when we are not in world generation
        if (getHandle().generation || !callPowerEvent(cause)) {
            getHandle().setPowered(powered);
        }
    }

    private boolean callPowerEvent(CreeperPowerEvent.PowerCause cause) {
        CreeperPowerEvent event = new CreeperPowerEvent((Creeper) getHandle().getBukkitEntity(), cause);
        server.getPluginManager().callEvent(event);
        return event.isCancelled();
    }

    @Override
    public void setMaxFuseTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks < 0");

        getHandle().maxSwell = ticks;
    }

    @Override
    public int getMaxFuseTicks() {
        return getHandle().maxSwell;
    }

    @Override
    public void setFuseTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks < 0");
        Preconditions.checkArgument(ticks <= getMaxFuseTicks(), "ticks > maxFuseTicks");

        getHandle().swell = ticks;
    }

    @Override
    public int getFuseTicks() {
        return getHandle().swell;
    }

    @Override
    public void setExplosionRadius(int radius) {
        Preconditions.checkArgument(radius >= 0, "radius < 0");

        getHandle().explosionRadius = radius;
    }

    @Override
    public int getExplosionRadius() {
        return getHandle().explosionRadius;
    }

    @Override
    public void explode() {
        getHandle().explodeCreeper();
    }

    @Override
    public void ignite() {
        getHandle().ignite();
    }

    @Override
    public EntityCreeper getHandle() {
        return (EntityCreeper) entity;
    }

    @Override
    public String toString() {
        return "CraftCreeper";
    }

    @Override
    public EntityType getType() {
        return EntityType.CREEPER;
    }
}
