package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TestBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.TestBlock;

public class CraftTestBlock extends CraftBlockEntityState<TestBlockEntity> implements TestBlock {

    public CraftTestBlock(World world, TestBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftTestBlock(CraftTestBlock state, Location location) {
        super(state, location);
    }

    @Override
    public CraftTestBlock copy() {
        return new CraftTestBlock(this, null);
    }

    @Override
    public CraftTestBlock copy(Location location) {
        return new CraftTestBlock(this, location);
    }
}
