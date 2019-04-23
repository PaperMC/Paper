package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityJigsaw;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jigsaw;

public class CraftJigsaw extends CraftBlockEntityState<TileEntityJigsaw> implements Jigsaw {

    public CraftJigsaw(Block block) {
        super(block, TileEntityJigsaw.class);
    }

    public CraftJigsaw(Material material, TileEntityJigsaw te) {
        super(material, te);
    }
}
