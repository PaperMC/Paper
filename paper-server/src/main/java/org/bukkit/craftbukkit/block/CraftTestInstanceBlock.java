package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.TestInstanceBlock;

public class CraftTestInstanceBlock extends CraftBlockEntityState<TestInstanceBlockEntity> implements TestInstanceBlock {

    public CraftTestInstanceBlock(World world, TestInstanceBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftTestInstanceBlock(CraftTestInstanceBlock state, Location location) {
        super(state, location);
    }

    @Override
    public CraftTestInstanceBlock copy() {
        return new CraftTestInstanceBlock(this, null);
    }

    @Override
    public CraftTestInstanceBlock copy(Location location) {
        return new CraftTestInstanceBlock(this, location);
    }
}
