package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Chest;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftChest extends CraftBlockData implements Chest {

    private static final net.minecraft.server.BlockStateEnum<?> TYPE = getEnum("type");

    @Override
    public org.bukkit.block.data.type.Chest.Type getType() {
        return get(TYPE, org.bukkit.block.data.type.Chest.Type.class);
    }

    @Override
    public void setType(org.bukkit.block.data.type.Chest.Type type) {
        set(TYPE, type);
    }
}
