package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Jukebox;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftJukebox extends CraftBlockData implements Jukebox {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty HAS_RECORD = getBoolean("has_record");

    @Override
    public boolean hasRecord() {
        return this.get(CraftJukebox.HAS_RECORD);
    }
}
