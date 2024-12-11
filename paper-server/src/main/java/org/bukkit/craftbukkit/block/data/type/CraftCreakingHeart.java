package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.CreakingHeart;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftCreakingHeart extends CraftBlockData implements CreakingHeart {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty ACTIVE = getBoolean("active");
    private static final net.minecraft.world.level.block.state.properties.BooleanProperty NATURAL = getBoolean("natural");

    @Override
    public boolean isActive() {
        return this.get(CraftCreakingHeart.ACTIVE);
    }

    @Override
    public void setActive(boolean active) {
        this.set(CraftCreakingHeart.ACTIVE, active);
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
