package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TileEntityEnderPortal;
import org.bukkit.Location;
import org.bukkit.World;

public class CraftEndPortal extends CraftBlockEntityState<TileEntityEnderPortal> {

    public CraftEndPortal(World world, TileEntityEnderPortal tileEntity) {
        super(world, tileEntity);
    }

    protected CraftEndPortal(CraftEndPortal state, Location location) {
        super(state, location);
    }

    @Override
    public CraftEndPortal copy() {
        return new CraftEndPortal(this, null);
    }

    @Override
    public CraftEndPortal copy(Location location) {
        return new CraftEndPortal(this, location);
    }
}
