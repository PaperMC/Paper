package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.SculkCatalystBlockEntity;
import org.bukkit.World;
import org.bukkit.block.SculkCatalyst;

public class CraftSculkCatalyst extends CraftBlockEntityState<SculkCatalystBlockEntity> implements SculkCatalyst {

    public CraftSculkCatalyst(World world, SculkCatalystBlockEntity tileEntity) {
        super(world, tileEntity);
    }
}
