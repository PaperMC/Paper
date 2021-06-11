package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.CaveVinesPlant;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftCaveVinesPlant extends CraftBlockData implements CaveVinesPlant {

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean BERRIES = getBoolean("berries");

    @Override
    public boolean isBerries() {
        return get(BERRIES);
    }

    @Override
    public void setBerries(boolean berries) {
        set(BERRIES, berries);
    }
}
