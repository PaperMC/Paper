package org.bukkit.craftbukkit.entity;

import net.minecraft.Optionull;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Phantom;
import java.util.UUID;

public class CraftPhantom extends CraftMob implements Phantom, CraftEnemy {

    public CraftPhantom(CraftServer server, net.minecraft.world.entity.monster.Phantom entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Phantom getHandle() {
        return (net.minecraft.world.entity.monster.Phantom) this.entity;
    }

    @Override
    public int getSize() {
        return this.getHandle().getPhantomSize();
    }

    @Override
    public void setSize(int size) {
        this.getHandle().setPhantomSize(size);
    }

    @Override
    public UUID getSpawningEntity() {
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
    public Location getAnchorLocation() {
        return Optionull.map(this.getHandle().anchorPoint, pos -> CraftLocation.toBukkit(pos, this.getHandle().level()));
    }

    @Override
    public void setAnchorLocation(Location location) {
        this.getHandle().anchorPoint = location == null ? null : CraftLocation.toBlockPosition(location);
    }
}
