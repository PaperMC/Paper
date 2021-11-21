package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.monster.EntityStrider;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Strider;

public class CraftStrider extends CraftAnimals implements Strider {

    public CraftStrider(CraftServer server, EntityStrider entity) {
        super(server, entity);
    }

    @Override
    public boolean isShivering() {
        return getHandle().isSuffocating();
    }

    @Override
    public void setShivering(boolean shivering) {
        this.getHandle().setSuffocating(shivering);
    }

    @Override
    public boolean hasSaddle() {
        return getHandle().isSaddled();
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
        return Material.WARPED_FUNGUS_ON_A_STICK;
    }

    @Override
    public EntityStrider getHandle() {
        return (EntityStrider) entity;
    }

    @Override
    public String toString() {
        return "CraftStrider";
    }

    @Override
    public EntityType getType() {
        return EntityType.STRIDER;
    }
}
