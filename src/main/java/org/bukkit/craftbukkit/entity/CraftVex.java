package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityVex;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Vex;

public class CraftVex extends CraftMonster implements Vex {

    public CraftVex(CraftServer server, EntityVex entity) {
        super(server, entity);
    }

    @Override
    public EntityVex getHandle() {
        return (EntityVex) super.getHandle();
    }

    // Paper start
    public Mob getSummoner() {
        EntityInsentient owner = getHandle().getOwner();
        return owner != null ? (Mob) owner.getBukkitEntity() : null;
    }

    public void setSummoner(Mob summoner) {
        getHandle().setOwner(summoner == null ? null : ((CraftMob) summoner).getHandle());
    }
    // Paper end

    @Override
    public String toString() {
        return "CraftVex";
    }

    @Override
    public EntityType getType() {
        return EntityType.VEX;
    }

    @Override
    public boolean isCharging() {
        return getHandle().isCharging();
    }

    @Override
    public void setCharging(boolean charging) {
        getHandle().setCharging(charging);
    }
}
