package org.bukkit.craftbukkit.block.data;

import org.bukkit.block.data.Lightable;

public abstract class CraftLightable extends CraftBlockData implements Lightable {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty LIT = getBoolean("lit");

    @Override
    public boolean isLit() {
        return this.get(CraftLightable.LIT);
    }

    @Override
    public void setLit(boolean lit) {
        this.set(CraftLightable.LIT, lit);
    }
}
