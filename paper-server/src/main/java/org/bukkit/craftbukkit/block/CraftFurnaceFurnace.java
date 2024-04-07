package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TileEntityFurnaceFurnace;
import org.bukkit.Location;
import org.bukkit.World;

public class CraftFurnaceFurnace extends CraftFurnace<TileEntityFurnaceFurnace> {

    public CraftFurnaceFurnace(World world, TileEntityFurnaceFurnace tileEntity) {
        super(world, tileEntity);
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
