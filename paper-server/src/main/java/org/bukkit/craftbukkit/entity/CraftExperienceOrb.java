package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ExperienceOrb;
import java.util.UUID;

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
        return this.getHandle().getValue();
    }

    @Override
    public void setExperience(int value) {
        this.getHandle().setValue(value);
    }

    @Override
    public int getCount() {
        return this.getHandle().count;
    }

    @Override
    public void setCount(final int count) {
        this.getHandle().count = count;
    }

    @Override
    public UUID getTriggerEntityId() {
        return this.getHandle().triggerEntityId;
    }

    @Override
    public UUID getSourceEntityId() {
        return this.getHandle().sourceEntityId;
    }

    @Override
    public SpawnReason getSpawnReason() {
        return this.getHandle().spawnReason;
    }
}
