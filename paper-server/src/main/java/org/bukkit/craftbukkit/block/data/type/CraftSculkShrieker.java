package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.SculkShrieker;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftSculkShrieker extends CraftBlockData implements SculkShrieker {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty CAN_SUMMON = getBoolean("can_summon");
    private static final net.minecraft.world.level.block.state.properties.BooleanProperty SHRIEKING = getBoolean("shrieking");

    @Override
    public boolean isCanSummon() {
        return this.get(CraftSculkShrieker.CAN_SUMMON);
    }

    @Override
    public void setCanSummon(boolean can_summon) {
        this.set(CraftSculkShrieker.CAN_SUMMON, can_summon);
    }

    @Override
    public boolean isShrieking() {
        return this.get(CraftSculkShrieker.SHRIEKING);
    }

    @Override
    public void setShrieking(boolean shrieking) {
        this.set(CraftSculkShrieker.SHRIEKING, shrieking);
    }
}
