package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;

public class CraftFurnaceFurnace extends CraftFurnace<FurnaceBlockEntity> {

    public CraftFurnaceFurnace(World world, FurnaceBlockEntity blockEntity, boolean useSnapshot) {
        super(world, blockEntity, useSnapshot);
    }

    protected CraftFurnaceFurnace(CraftFurnaceFurnace state, Location location) {
        super(state, location);
    }

    @Override
    public CraftFurnaceFurnace copy() {
        return new CraftFurnaceFurnace(this, null);
    }

    @Override
    public CraftFurnaceFurnace copy(Location location) {
        return new CraftFurnaceFurnace(this, location);
    }
}
