package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TileEntityBell;
import org.bukkit.Material;
import org.bukkit.block.Bell;
import org.bukkit.block.Block;

public class CraftBell extends CraftBlockEntityState<TileEntityBell> implements Bell {

    public CraftBell(Block block) {
        super(block, TileEntityBell.class);
    }

    public CraftBell(Material material, TileEntityBell te) {
        super(material, te);
    }
}
