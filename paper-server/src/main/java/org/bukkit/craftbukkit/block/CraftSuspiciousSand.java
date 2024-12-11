package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.SuspiciousSand;

public class CraftSuspiciousSand extends CraftBrushableBlock implements SuspiciousSand {

    public CraftSuspiciousSand(World world, BrushableBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftSuspiciousSand(CraftSuspiciousSand state, Location location) {
        super(state, location);
    }

    @Override
    public CraftSuspiciousSand copy() {
        return new CraftSuspiciousSand(this, null);
    }

    @Override
    public CraftSuspiciousSand copy(Location location) {
        return new CraftSuspiciousSand(this, location);
    }
}
