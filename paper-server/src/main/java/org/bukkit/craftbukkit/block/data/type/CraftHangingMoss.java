package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.HangingMoss;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftHangingMoss extends CraftBlockData implements HangingMoss {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty TIP = getBoolean("tip");

    @Override
    public boolean isTip() {
        return this.get(CraftHangingMoss.TIP);
    }

    @Override
    public void setTip(boolean tip) {
        this.set(CraftHangingMoss.TIP, tip);
    }
}
