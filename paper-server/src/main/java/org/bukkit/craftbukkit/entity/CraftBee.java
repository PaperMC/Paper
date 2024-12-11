package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Bee;

public class CraftBee extends CraftAnimals implements Bee {

    public CraftBee(CraftServer server, net.minecraft.world.entity.animal.Bee entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Bee getHandle() {
        return (net.minecraft.world.entity.animal.Bee) this.entity;
    }

    @Override
    public String toString() {
        return "CraftBee";
    }

    @Override
    public Location getHive() {
        BlockPos hive = this.getHandle().getHivePos();
        return (hive == null) ? null : CraftLocation.toBukkit(hive, this.getWorld());
    }

    @Override
    public void setHive(Location location) {
        Preconditions.checkArgument(location == null || this.getWorld().equals(location.getWorld()), "Hive must be in same world");
        this.getHandle().hivePos = (location == null) ? null : CraftLocation.toBlockPosition(location);
    }

    @Override
    public Location getFlower() {
        BlockPos flower = this.getHandle().getSavedFlowerPos();
        return (flower == null) ? null : CraftLocation.toBukkit(flower, this.getWorld());
    }

    @Override
    public void setFlower(Location location) {
        Preconditions.checkArgument(location == null || this.getWorld().equals(location.getWorld()), "Flower must be in same world");
        this.getHandle().setSavedFlowerPos(location == null ? null : CraftLocation.toBlockPosition(location));
    }

    @Override
    public boolean hasNectar() {
        return this.getHandle().hasNectar();
    }

    @Override
    public void setHasNectar(boolean nectar) {
        this.getHandle().setHasNectar(nectar);
    }

    @Override
    public boolean hasStung() {
        return this.getHandle().hasStung();
    }

    @Override
    public void setHasStung(boolean stung) {
        this.getHandle().setHasStung(stung);
    }

    @Override
    public int getAnger() {
        return this.getHandle().getRemainingPersistentAngerTime();
    }

    @Override
    public void setAnger(int anger) {
        this.getHandle().setRemainingPersistentAngerTime(anger);
    }

    @Override
    public int getCannotEnterHiveTicks() {
        return this.getHandle().stayOutOfHiveCountdown;
    }

    @Override
    public void setCannotEnterHiveTicks(int ticks) {
        this.getHandle().setStayOutOfHiveCountdown(ticks);
    }
}
