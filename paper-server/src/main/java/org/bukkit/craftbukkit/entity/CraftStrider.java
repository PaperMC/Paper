package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.server.EntityStrider;
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
        return getHandle().isShivering();
    }

    @Override
    public void setShivering(boolean shivering) {
        this.getHandle().setShivering(shivering);
    }

    @Override
    public boolean hasSaddle() {
        return getHandle().hasSaddle();
    }

    @Override
    public void setSaddle(boolean saddled) {
        getHandle().saddleStorage.setSaddle(saddled);
    }

    @Override
    public int getBoostTicks() {
        return getHandle().saddleStorage.boosting ? getHandle().saddleStorage.boostTicks : 0;
    }

    @Override
    public void setBoostTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks must be >= 0");

        getHandle().saddleStorage.setBoostTicks(ticks);
    }

    @Override
    public int getCurrentBoostTicks() {
        return getHandle().saddleStorage.boosting ? getHandle().saddleStorage.currentBoostTicks : 0;
    }

    @Override
    public void setCurrentBoostTicks(int ticks) {
        if (!getHandle().saddleStorage.boosting) {
            return;
        }

        int max = getHandle().saddleStorage.boostTicks;
        Preconditions.checkArgument(ticks >= 0 && ticks <= max, "boost ticks must not exceed 0 or %d (inclusive)", max);

        this.getHandle().saddleStorage.currentBoostTicks = ticks;
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
