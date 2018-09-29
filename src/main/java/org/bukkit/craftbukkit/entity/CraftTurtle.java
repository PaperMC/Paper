package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityTurtle;
import net.minecraft.server.MCUtil;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Turtle;

public class CraftTurtle extends CraftAnimals implements Turtle {

    public CraftTurtle(CraftServer server, EntityTurtle entity) {
        super(server, entity);
    }

    @Override
    public EntityTurtle getHandle() {
        return (EntityTurtle) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftTurtle";
    }

    @Override
    public EntityType getType() {
        return EntityType.TURTLE;
    }

    // Paper start
    @Override
    public Location getHome() {
        return MCUtil.toLocation(getHandle().world, getHandle().getHomePos());
    }

    @Override
    public void setHome(Location location) {
        getHandle().setHomePos(MCUtil.toBlockPosition(location));
    }

    @Override
    public boolean isGoingHome() {
        return getHandle().isGoingHome();
    }

    @Override
    public boolean isDigging() {
        return getHandle().isDigging();
    }

    @Override
    public boolean hasEgg() {
        return getHandle().hasEgg();
    }

    @Override
    public void setHasEgg(boolean hasEgg) {
        getHandle().setHasEgg(hasEgg);
    }
    // Paper end
}
