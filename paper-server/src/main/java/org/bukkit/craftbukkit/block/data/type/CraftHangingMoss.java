package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.HangingMoss;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftHangingMoss extends CraftBlockData implements HangingMoss {

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean TIP = getBoolean("tip");

    @Override
    public boolean isTip() {
        return get(TIP);
    }

    @Override
    public void setTip(boolean tip) {
        set(TIP, tip);
    }
}
