package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.TileEntityBeehive;
import net.minecraft.world.level.block.entity.TileEntityBeehive.ReleaseStatus;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Beehive;
import org.bukkit.craftbukkit.entity.CraftBee;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Bee;

public class CraftBeehive extends CraftBlockEntityState<TileEntityBeehive> implements Beehive {

    public CraftBeehive(World world, TileEntityBeehive tileEntity) {
        super(world, tileEntity);
    }

    protected CraftBeehive(CraftBeehive state, Location location) {
        super(state, location);
    }

    @Override
    public Location getFlower() {
        BlockPosition flower = getSnapshot().savedFlowerPos;
        return (flower == null) ? null : CraftLocation.toBukkit(flower, getWorld());
    }

    @Override
    public void setFlower(Location location) {
        Preconditions.checkArgument(location == null || this.getWorld().equals(location.getWorld()), "Flower must be in same world");
        getSnapshot().savedFlowerPos = (location == null) ? null : CraftLocation.toBlockPosition(location);
    }

    @Override
    public boolean isFull() {
        return getSnapshot().isFull();
    }

    @Override
    public boolean isSedated() {
        return isPlaced() && getTileEntity().isSedated();
    }

    @Override
    public int getEntityCount() {
        return getSnapshot().getOccupantCount();
    }

    @Override
    public int getMaxEntities() {
        return getSnapshot().maxBees;
    }

    @Override
    public void setMaxEntities(int max) {
        Preconditions.checkArgument(max > 0, "Max bees must be more than 0");

        getSnapshot().maxBees = max;
    }

    @Override
    public List<Bee> releaseEntities() {
        ensureNoWorldGeneration();

        List<Bee> bees = new ArrayList<>();

        if (isPlaced()) {
            TileEntityBeehive beehive = ((TileEntityBeehive) this.getTileEntityFromWorld());
            for (Entity bee : beehive.releaseBees(this.getHandle(), ReleaseStatus.BEE_RELEASED, true)) {
                bees.add((Bee) bee.getBukkitEntity());
            }
        }

        return bees;
    }

    @Override
    public void addEntity(Bee entity) {
        Preconditions.checkArgument(entity != null, "Entity must not be null");

        getSnapshot().addOccupant(((CraftBee) entity).getHandle());
    }

    @Override
    public CraftBeehive copy() {
        return new CraftBeehive(this, null);
    }

    @Override
    public CraftBeehive copy(Location location) {
        return new CraftBeehive(this, location);
    }
}
