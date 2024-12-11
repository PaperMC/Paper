package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
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

    // Paper start
    @Override
    public java.util.UUID getSpawningEntity() {
        return getHandle().getSpawningEntity();
    }

    @Override
    public boolean shouldBurnInDay() {
        return getHandle().shouldBurnInDay();
    }

    @Override
    public void setShouldBurnInDay(boolean shouldBurnInDay) {
        getHandle().setShouldBurnInDay(shouldBurnInDay);
    }

    @Override
    public org.bukkit.Location getAnchorLocation() {
        net.minecraft.core.BlockPos pos = this.getHandle().anchorPoint;
        return io.papermc.paper.util.MCUtil.toLocation(this.getHandle().level(), pos);
    }

    @Override
    public void setAnchorLocation(org.bukkit.Location location) {
        com.google.common.base.Preconditions.checkArgument(location != null, "location cannot be null");
        this.getHandle().anchorPoint = io.papermc.paper.util.MCUtil.toBlockPosition(location);
    }
    // Paper end
}
