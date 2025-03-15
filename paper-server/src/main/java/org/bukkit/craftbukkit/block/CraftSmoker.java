package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.SmokerBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Smoker;

public class CraftSmoker extends CraftFurnace<SmokerBlockEntity> implements Smoker {

    public CraftSmoker(World world, SmokerBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftSmoker(CraftSmoker state, Location location) {
        super(state, location);
    }

    @Override
    public CraftSmoker copy() {
        return new CraftSmoker(this, null);
    }

    @Override
    public CraftSmoker copy(Location location) {
        return new CraftSmoker(this, location);
    }
}
