package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;

public class CraftChestChest extends CraftChest<ChestBlockEntity> {

    public CraftChestChest(World world, ChestBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftChestChest(CraftChestChest state, Location location) {
        super(state, location);
    }

    @Override
    public CraftChestChest copy() {
        return new CraftChestChest(this, null);
    }

    @Override
    public CraftChestChest copy(Location location) {
        return new CraftChestChest(this, location);
    }
}
