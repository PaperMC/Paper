package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Lectern;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftLectern extends CraftBlockData implements Lectern {

    private static final net.minecraft.server.BlockStateBoolean HAS_BOOK = getBoolean("has_book");

    @Override
    public boolean hasBook() {
        return get(HAS_BOOK);
    }
}
