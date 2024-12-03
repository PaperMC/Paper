package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.CreakingHeart;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftCreakingHeart extends CraftBlockData implements CreakingHeart {

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean ACTIVE = getBoolean("active");
    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean NATURAL = getBoolean("natural");

    @Override
    public boolean isActive() {
        return get(ACTIVE);
    }

    @Override
    public void setActive(boolean active) {
        set(ACTIVE, active);
    }

    @Override
    public boolean isNatural() {
        return get(NATURAL);
    }

    @Override
    public void setNatural(boolean natural) {
        set(NATURAL, natural);
    }
}
