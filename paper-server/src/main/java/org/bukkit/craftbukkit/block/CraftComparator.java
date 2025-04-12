package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Comparator;

public class CraftComparator extends CraftBlockEntityState<ComparatorBlockEntity> implements Comparator {

    public CraftComparator(World world, ComparatorBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftComparator(CraftComparator state, Location location) {
        super(state, location);
    }

    @Override
    public CraftComparator copy() {
        return new CraftComparator(this, null);
    }

    @Override
    public CraftComparator copy(Location location) {
        return new CraftComparator(this, location);
    }
}
