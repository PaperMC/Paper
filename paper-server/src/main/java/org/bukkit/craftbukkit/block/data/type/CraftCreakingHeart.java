package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.CreakingHeart;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftCreakingHeart extends CraftBlockData implements CreakingHeart {

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> CREAKING_HEART_STATE = getEnum("creaking_heart_state");
    private static final net.minecraft.world.level.block.state.properties.BooleanProperty NATURAL = getBoolean("natural");

    @Override
    public State getCreakingHeartState() {
        return this.get(CraftCreakingHeart.CREAKING_HEART_STATE, CreakingHeart.State.class);
    }

    @Override
    public void setCreakingHeartState(State state) {
        this.set(CREAKING_HEART_STATE, state);
    }

    @Override
    public boolean isNatural() {
        return this.get(CraftCreakingHeart.NATURAL);
    }

    @Override
    public void setNatural(boolean natural) {
        this.set(CraftCreakingHeart.NATURAL, natural);
    }
}
