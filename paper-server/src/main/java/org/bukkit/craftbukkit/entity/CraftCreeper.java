package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreeperPowerEvent;

public class CraftCreeper extends CraftMonster implements Creeper {

    public CraftCreeper(CraftServer server, net.minecraft.world.entity.monster.Creeper entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Creeper getHandle() {
        return (net.minecraft.world.entity.monster.Creeper) this.entity;
    }

    @Override
    public boolean isPowered() {
        return this.getHandle().isPowered();
    }

    @Override
    public void setPowered(boolean powered) {
        CreeperPowerEvent.PowerCause cause = powered ? CreeperPowerEvent.PowerCause.SET_ON : CreeperPowerEvent.PowerCause.SET_OFF;

        // only call event when we are not in world generation
        if (this.getHandle().generation || !this.callPowerEvent(cause)) {
            this.getHandle().setPowered(powered);
        }
    }

    private boolean callPowerEvent(CreeperPowerEvent.PowerCause cause) {
        CreeperPowerEvent event = new CreeperPowerEvent((Creeper) this.getHandle().getBukkitEntity(), cause);
        this.server.getPluginManager().callEvent(event);
        return event.isCancelled();
    }

    @Override
    public void setMaxFuseTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks < 0");

        this.getHandle().maxSwell = ticks;
    }

    @Override
    public int getMaxFuseTicks() {
        return this.getHandle().maxSwell;
    }

    @Override
    public void setFuseTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks < 0");
        Preconditions.checkArgument(ticks <= this.getMaxFuseTicks(), "ticks > maxFuseTicks");

        this.getHandle().swell = ticks;
    }

    @Override
    public int getFuseTicks() {
        return this.getHandle().swell;
    }

    @Override
    public void setExplosionRadius(int radius) {
        Preconditions.checkArgument(radius >= 0, "radius < 0");

        this.getHandle().explosionRadius = radius;
    }

    @Override
    public int getExplosionRadius() {
        return this.getHandle().explosionRadius;
    }

    @Override
    public void explode() {
        this.getHandle().explodeCreeper();
    }

    @Override
    public void ignite(Entity entity) {
        Preconditions.checkNotNull(entity, "entity cannot be null");
        this.getHandle().entityIgniter = ((CraftEntity) entity).getHandle();
        this.getHandle().ignite();
    }

    @Override
    public void ignite() {
        this.getHandle().ignite();
    }

    @Override
    public Entity getIgniter() {
        return (this.getHandle().entityIgniter != null) ? this.getHandle().entityIgniter.getBukkitEntity() : null;
    }

    // Paper start
    @Override
    public void setIgnited(boolean ignited) {
        getHandle().setIgnited(ignited);
    }

    @Override
    public boolean isIgnited() {
        return getHandle().isIgnited();
    }
    // Paper end
}
