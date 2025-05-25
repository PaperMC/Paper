package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.minecart.PoweredMinecart;

public class CraftMinecartFurnace extends CraftMinecart implements PoweredMinecart {

    public CraftMinecartFurnace(CraftServer server, MinecartFurnace entity) {
        super(server, entity);
    }

    @Override
    public MinecartFurnace getHandle() {
        return (MinecartFurnace) this.entity;
    }

    @Override
    public int getFuel() {
        return this.getHandle().fuel;
    }

    @Override
    public void setFuel(int fuel) {
        Preconditions.checkArgument(fuel >= 0, "ticks cannot be negative");
        this.getHandle().fuel = fuel;
    }

    @Override
    public double getPushX() {
        return this.getHandle().push.x;
    }

    @Override
    public double getPushZ() {
        return this.getHandle().push.z;
    }

    @Override
    public void setPushX(double xPush) {
        final net.minecraft.world.phys.Vec3 push = this.getHandle().push;
        this.getHandle().push = new net.minecraft.world.phys.Vec3(xPush, push.y, push.z);
    }

    @Override
    public void setPushZ(double zPush) {
        final net.minecraft.world.phys.Vec3 push = this.getHandle().push;
        this.getHandle().push = new net.minecraft.world.phys.Vec3(push.x, push.y, zPush);
    }
}
