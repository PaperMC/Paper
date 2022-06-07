package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.SculkShrieker;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftSculkShrieker extends CraftBlockData implements SculkShrieker {

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean CAN_SUMMON = getBoolean("can_summon");
    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean SHRIEKING = getBoolean("shrieking");

    @Override
    public boolean isCanSummon() {
        return get(CAN_SUMMON);
    }

    @Override
    public void setCanSummon(boolean can_summon) {
        set(CAN_SUMMON, can_summon);
    }

    @Override
    public boolean isShrieking() {
        return get(SHRIEKING);
    }

    @Override
    public void setShrieking(boolean shrieking) {
        set(SHRIEKING, shrieking);
    }
}
