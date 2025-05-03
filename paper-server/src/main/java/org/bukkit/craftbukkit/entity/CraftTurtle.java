package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Turtle;

public class CraftTurtle extends CraftAnimals implements Turtle {

    public CraftTurtle(CraftServer server, net.minecraft.world.entity.animal.Turtle entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Turtle getHandle() {
        return (net.minecraft.world.entity.animal.Turtle) this.entity;
    }

    @Override
    public boolean hasEgg() {
        return this.getHandle().hasEgg();
    }

    @Override
    public boolean isLayingEgg() {
        return this.getHandle().isLayingEgg();
    }

    @Override
    public org.bukkit.Location getHome() {
        return CraftLocation.toBukkit(this.getHandle().homePos, this.getHandle().level());
    }

    @Override
    public void setHome(org.bukkit.Location location) {
        this.getHandle().homePos = CraftLocation.toBlockPosition(location);
    }

    @Override
    public boolean isGoingHome() {
        return this.getHandle().goingHome;
    }

    @Override
    public void setHasEgg(boolean hasEgg) {
        this.getHandle().setHasEgg(hasEgg);
    }
}
