package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.server.EntityPiglin;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Piglin;

public class CraftPiglin extends CraftMonster implements Piglin {

    public CraftPiglin(CraftServer server, EntityPiglin entity) {
        super(server, entity);
    }

    @Override
    public boolean isImmuneToZombification() {
        return getHandle().eT();
    }

    @Override
    public void setImmuneToZombification(boolean flag) {
        getHandle().t(flag);
    }

    @Override
    public boolean isAbleToHunt() {
        return getHandle().bC;
    }

    @Override
    public void setIsAbleToHunt(boolean flag) {
        getHandle().bC = flag;
    }

    @Override
    public int getConversionTime() {
        Preconditions.checkState(isConverting(), "Entity not converting");
        return getHandle().bA;
    }

    @Override
    public void setConversionTime(int time) {
        if (time < 0) {
            getHandle().bA = -1;
            getHandle().t(false);
        } else {
            getHandle().bA = time;
        }
    }

    @Override
    public boolean isConverting() {
        return getHandle().eO(); // PAIL rename isConverting()
    }

    @Override
    public boolean isBaby() {
        return getHandle().isBaby();
    }

    @Override
    public void setBaby(boolean flag) {
        getHandle().a(flag);
    }

    @Override
    public EntityPiglin getHandle() {
        return (EntityPiglin) super.getHandle();
    }

    @Override
    public EntityType getType() {
        return EntityType.PIGLIN;
    }

    @Override
    public String toString() {
        return "CraftPiglin";
    }
}
