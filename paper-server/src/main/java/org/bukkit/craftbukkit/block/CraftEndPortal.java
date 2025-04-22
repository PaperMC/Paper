package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;

public class CraftEndPortal extends CraftBlockEntityState<TheEndPortalBlockEntity> {

    public CraftEndPortal(World world, TheEndPortalBlockEntity blockEntity) {
        super(world, blockEntity);
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
