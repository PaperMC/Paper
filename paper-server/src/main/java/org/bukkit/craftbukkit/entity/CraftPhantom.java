package org.bukkit.craftbukkit.entity;

import net.kyori.adventure.util.TriState;
import net.minecraft.Optionull;
import net.minecraft.tags.EntityTypeTags;
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
        return super.burnsInDaylight();
    }

    @Override
    public void setShouldBurnInDay(boolean shouldBurnInDay) {
        super.setBurnInDaylightOverride(shouldBurnInDay == this.getHandle().getType().is(EntityTypeTags.BURN_IN_DAYLIGHT) ? TriState.NOT_SET : TriState.FALSE); // Use NOT_SET if the default value is set with this
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
