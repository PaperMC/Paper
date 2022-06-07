package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.SculkShriekerBlockEntity;
import org.bukkit.World;
import org.bukkit.block.SculkShrieker;

public class CraftSculkShrieker extends CraftBlockEntityState<SculkShriekerBlockEntity> implements SculkShrieker {

    public CraftSculkShrieker(World world, SculkShriekerBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    @Override
    public int getWarningLevel() {
        return getSnapshot().warningLevel;
    }

    @Override
    public void setWarningLevel(int level) {
        getSnapshot().warningLevel = level;
    }
}
