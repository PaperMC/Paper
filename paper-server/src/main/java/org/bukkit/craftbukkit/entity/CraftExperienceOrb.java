package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ExperienceOrb;

public class CraftExperienceOrb extends CraftEntity implements ExperienceOrb {

    public CraftExperienceOrb(CraftServer server, net.minecraft.world.entity.ExperienceOrb entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.ExperienceOrb getHandle() {
        return (net.minecraft.world.entity.ExperienceOrb) this.entity;
    }

    @Override
    public int getExperience() {
        return this.getHandle().value;
    }

    @Override
    public void setExperience(int value) {
        this.getHandle().value = value;
    }

    // Paper start - expose count
    @Override
    public int getCount() {
        return this.getHandle().count;
    }

    @Override
    public void setCount(final int count) {
        this.getHandle().count = count;
    }
    // Paper end

    // Paper start
    @Override
    public java.util.UUID getTriggerEntityId() {
        return getHandle().triggerEntityId;
    }

    @Override
    public java.util.UUID getSourceEntityId() {
        return getHandle().sourceEntityId;
    }

    @Override
    public SpawnReason getSpawnReason() {
        return getHandle().spawnReason;
    }
    // Paper end
}
