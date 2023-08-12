package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.animal.EntityBee;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Bee;

public class CraftBee extends CraftAnimals implements Bee {

    public CraftBee(CraftServer server, EntityBee entity) {
        super(server, entity);
    }

    @Override
    public EntityBee getHandle() {
        return (EntityBee) entity;
    }

    @Override
    public String toString() {
        return "CraftBee";
    }

    @Override
    public Location getHive() {
        BlockPosition hive = getHandle().getHivePos();
        return (hive == null) ? null : CraftLocation.toBukkit(hive, getWorld());
    }

    @Override
    public void setHive(Location location) {
        Preconditions.checkArgument(location == null || this.getWorld().equals(location.getWorld()), "Hive must be in same world");
        getHandle().hivePos = (location == null) ? null : CraftLocation.toBlockPosition(location);
    }

    @Override
    public Location getFlower() {
        BlockPosition flower = getHandle().getSavedFlowerPos();
        return (flower == null) ? null : CraftLocation.toBukkit(flower, getWorld());
    }

    @Override
    public void setFlower(Location location) {
        Preconditions.checkArgument(location == null || this.getWorld().equals(location.getWorld()), "Flower must be in same world");
        getHandle().setSavedFlowerPos(location == null ? null : CraftLocation.toBlockPosition(location));
    }

    @Override
    public boolean hasNectar() {
        return getHandle().hasNectar();
    }

    @Override
    public void setHasNectar(boolean nectar) {
        getHandle().setHasNectar(nectar);
    }

    @Override
    public boolean hasStung() {
        return getHandle().hasStung();
    }

    @Override
    public void setHasStung(boolean stung) {
        getHandle().setHasStung(stung);
    }

    @Override
    public int getAnger() {
        return getHandle().getRemainingPersistentAngerTime();
    }

    @Override
    public void setAnger(int anger) {
        getHandle().setRemainingPersistentAngerTime(anger);
    }

    @Override
    public int getCannotEnterHiveTicks() {
        return getHandle().stayOutOfHiveCountdown;
    }

    @Override
    public void setCannotEnterHiveTicks(int ticks) {
        getHandle().setStayOutOfHiveCountdown(ticks);
    }
}
