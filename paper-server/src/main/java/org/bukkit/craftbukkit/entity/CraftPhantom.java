package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Phantom;

public class CraftPhantom extends CraftFlying implements Phantom, CraftEnemy {

    public CraftPhantom(CraftServer server, net.minecraft.world.entity.monster.Phantom entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Phantom getHandle() {
        return (net.minecraft.world.entity.monster.Phantom) super.getHandle();
    }

    @Override
    public int getSize() {
        return this.getHandle().getPhantomSize();
    }

    @Override
    public void setSize(int sz) {
        this.getHandle().setPhantomSize(sz);
    }

    @Override
    public String toString() {
        return "CraftPhantom";
    }

    @Override
    public java.util.UUID getSpawningEntity() {
        return this.getHandle().spawningEntity;
    }

    @Override
    public boolean shouldBurnInDay() {
        return this.getHandle().shouldBurnInDay;
    }

    @Override
    public void setShouldBurnInDay(boolean shouldBurnInDay) {
        this.getHandle().shouldBurnInDay = shouldBurnInDay;
    }

    @Override
    public org.bukkit.Location getAnchorLocation() {
        return CraftLocation.toBukkit(this.getHandle().anchorPoint, this.getHandle().level()); // todo NPE?
    }

    @Override
    public void setAnchorLocation(org.bukkit.Location location) {
        com.google.common.base.Preconditions.checkArgument(location != null, "location cannot be null");
        this.getHandle().anchorPoint = CraftLocation.toBlockPosition(location); // todo allow null?
    }
}
