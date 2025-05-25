package org.bukkit.craftbukkit.entity;

import net.minecraft.Optionull;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Dolphin;

public class CraftDolphin extends CraftAgeable implements Dolphin {

    public CraftDolphin(CraftServer server, net.minecraft.world.entity.animal.Dolphin entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Dolphin getHandle() {
        return (net.minecraft.world.entity.animal.Dolphin) this.entity;
    }

    @Override
    public int getMoistness() {
        return this.getHandle().getMoistnessLevel();
    }

    @Override
    public void setMoistness(int moistness) {
        this.getHandle().setMoisntessLevel(moistness);
    }

    @Override
    public void setHasFish(boolean hasFish) {
        this.getHandle().setGotFish(hasFish);
    }

    @Override
    public boolean hasFish() {
        return this.getHandle().gotFish();
    }

    @Override
    public org.bukkit.Location getTreasureLocation() {
        return Optionull.map(this.getHandle().treasurePos, pos -> CraftLocation.toBukkit(pos, this.getHandle().level()));
    }

    @Override
    public void setTreasureLocation(org.bukkit.Location location) {
        this.getHandle().treasurePos = location == null ? null : CraftLocation.toBlockPosition(location);
    }
}
