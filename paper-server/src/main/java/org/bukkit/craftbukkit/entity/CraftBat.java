package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Bat;

public class CraftBat extends CraftAmbient implements Bat {
    public CraftBat(CraftServer server, net.minecraft.world.entity.ambient.Bat entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.ambient.Bat getHandle() {
        return (net.minecraft.world.entity.ambient.Bat) this.entity;
    }

    @Override
    public boolean isAwake() {
        return !this.getHandle().isResting();
    }

    @Override
    public void setAwake(boolean state) {
        this.getHandle().setResting(!state);
    }

    @Override
    public org.bukkit.Location getTargetLocation() {
        net.minecraft.core.BlockPos pos = this.getHandle().targetPosition;
        if (pos == null) {
            return null;
        }

        return CraftLocation.toBukkit(pos, this.getHandle().level());
    }

    @Override
    public void setTargetLocation(org.bukkit.Location location) {
        net.minecraft.core.BlockPos pos = null;
        if (location != null) {
            pos = CraftLocation.toBlockPosition(location);
        }

        this.getHandle().targetPosition = pos;
    }
}
