package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.server.EntityHoglin;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hoglin;

public class CraftHoglin extends CraftAnimals implements Hoglin {

    public CraftHoglin(CraftServer server, EntityHoglin entity) {
        super(server, entity);
    }

    @Override
    public boolean isImmuneToZombification() {
        return getHandle().eW();
    }

    @Override
    public void setImmuneToZombification(boolean flag) {
        getHandle().t(flag);
    }

    @Override
    public boolean isAbleToBeHunted() {
        return getHandle().bA;
    }

    @Override
    public void setIsAbleToBeHunted(boolean flag) {
        getHandle().bA = flag;
    }

    @Override
    public int getConversionTime() {
        Preconditions.checkState(isConverting(), "Entity not converting");
        return getHandle().bz;
    }

    @Override
    public void setConversionTime(int time) {
        if (time < 0) {
            getHandle().bz = -1;
            getHandle().t(false);
        } else {
            getHandle().bz = time;
        }
    }

    @Override
    public boolean isConverting() {
        return getHandle().eO(); // PAIL rename isConverting()
    }

    @Override
    public EntityHoglin getHandle() {
        return (EntityHoglin) entity;
    }

    @Override
    public String toString() {
        return "CraftHoglin";
    }

    @Override
    public EntityType getType() {
        return EntityType.HOGLIN;
    }
}
