package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityFurnaceFurnace;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class CraftFurnaceFurnace extends CraftFurnace<TileEntityFurnaceFurnace> {

    public CraftFurnaceFurnace(Block block) {
        super(block, TileEntityFurnaceFurnace.class);
    }

    public CraftFurnaceFurnace(Material material, TileEntityFurnaceFurnace te) {
        super(material, te);
    }
}
