package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftEndPortalFrame extends CraftBlockData implements EndPortalFrame {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty EYE = getBoolean("eye");

    @Override
    public boolean hasEye() {
        return this.get(CraftEndPortalFrame.EYE);
    }

    @Override
    public void setEye(boolean eye) {
        this.set(CraftEndPortalFrame.EYE, eye);
    }
}
