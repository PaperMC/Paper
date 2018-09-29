package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Turtle;

public class CraftTurtle extends CraftAnimals implements Turtle {

    public CraftTurtle(CraftServer server, net.minecraft.world.entity.animal.Turtle entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Turtle getHandle() {
        return (net.minecraft.world.entity.animal.Turtle) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftTurtle";
    }

    @Override
    public boolean hasEgg() {
        return this.getHandle().hasEgg();
    }

    @Override
    public boolean isLayingEgg() {
        return this.getHandle().isLayingEgg();
    }

    // Paper start
    @Override
    public org.bukkit.Location getHome() {
        return io.papermc.paper.util.MCUtil.toLocation(this.getHandle().level(), this.getHandle().getHomePos());
    }

    @Override
    public void setHome(org.bukkit.Location location) {
        this.getHandle().setHomePos(io.papermc.paper.util.MCUtil.toBlockPosition(location));
    }

    @Override
    public boolean isGoingHome() {
        return this.getHandle().isGoingHome();
    }

    @Override
    public boolean isDigging() {
        return this.getHandle().isLayingEgg();
    }

    @Override
    public void setHasEgg(boolean hasEgg) {
        this.getHandle().setHasEgg(hasEgg);
    }
    // Paper end
}
