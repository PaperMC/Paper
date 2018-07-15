package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.StructureBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftStructureBlock extends CraftBlockData implements StructureBlock {

    private static final net.minecraft.server.BlockStateEnum<?> MODE = getEnum("mode");

    @Override
    public Mode getMode() {
        return get(MODE, Mode.class);
    }

    @Override
    public void setMode(Mode mode) {
        set(MODE, mode);
    }
}
