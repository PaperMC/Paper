package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.monster.hoglin.EntityHoglin;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Hoglin;

public class CraftHoglin extends CraftAnimals implements Hoglin, CraftEnemy {

    public CraftHoglin(CraftServer server, EntityHoglin entity) {
        super(server, entity);
    }

    @Override
    public boolean isImmuneToZombification() {
        return getHandle().isImmuneToZombification();
    }

    @Override
    public void setImmuneToZombification(boolean flag) {
        getHandle().setImmuneToZombification(flag);
    }

    @Override
    public boolean isAbleToBeHunted() {
        return getHandle().cannotBeHunted;
    }

    @Override
    public void setIsAbleToBeHunted(boolean flag) {
        getHandle().cannotBeHunted = flag;
    }

    @Override
    public int getConversionTime() {
        Preconditions.checkState(isConverting(), "Entity not converting");
        return getHandle().timeInOverworld;
    }

    @Override
    public void setConversionTime(int time) {
        if (time < 0) {
            getHandle().timeInOverworld = -1;
            getHandle().setImmuneToZombification(false);
        } else {
            getHandle().timeInOverworld = time;
        }
    }

    @Override
    public boolean isConverting() {
        return getHandle().isConverting();
    }

    @Override
    public EntityHoglin getHandle() {
        return (EntityHoglin) entity;
    }

    @Override
    public String toString() {
        return "CraftHoglin";
    }
}
