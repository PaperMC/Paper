package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.animal.EntityPig;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;

public class CraftPig extends CraftAnimals implements Pig {

    public CraftPig(CraftServer server, EntityPig entity) {
        super(server, entity);
    }

    @Override
    public boolean hasSaddle() {
        return getHandle().hasSaddle();
    }

    @Override
    public void setSaddle(boolean saddled) {
        getHandle().steering.setSaddle(saddled);
    }

    @Override
    public int getBoostTicks() {
        return getHandle().steering.boosting ? getHandle().steering.boostTimeTotal : 0;
    }

    @Override
    public void setBoostTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks must be >= 0");

        getHandle().steering.setBoostTicks(ticks);
    }

    @Override
    public int getCurrentBoostTicks() {
        return getHandle().steering.boosting ? getHandle().steering.boostTime : 0;
    }

    @Override
    public void setCurrentBoostTicks(int ticks) {
        if (!getHandle().steering.boosting) {
            return;
        }

        int max = getHandle().steering.boostTimeTotal;
        Preconditions.checkArgument(ticks >= 0 && ticks <= max, "boost ticks must not exceed 0 or %d (inclusive)", max);

        this.getHandle().steering.boostTime = ticks;
    }

    @Override
    public Material getSteerMaterial() {
        return Material.CARROT_ON_A_STICK;
    }

    @Override
    public EntityPig getHandle() {
        return (EntityPig) entity;
    }

    @Override
    public String toString() {
        return "CraftPig";
    }

    @Override
    public EntityType getType() {
        return EntityType.PIG;
    }
}
