package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TileEntityComparator;
import org.bukkit.World;
import org.bukkit.block.Comparator;

public class CraftComparator extends CraftBlockEntityState<TileEntityComparator> implements Comparator {

    public CraftComparator(World world, TileEntityComparator tileEntity) {
        super(world, tileEntity);
    }

    protected CraftComparator(CraftComparator state) {
        super(state);
    }

    @Override
    public CraftComparator copy() {
        return new CraftComparator(this);
    }
}
