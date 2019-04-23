package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityBell;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class CraftBell extends CraftBlockEntityState<TileEntityBell> {

    public CraftBell(Block block) {
        super(block, TileEntityBell.class);
    }

    public CraftBell(Material material, TileEntityBell te) {
        super(material, te);
    }
}
