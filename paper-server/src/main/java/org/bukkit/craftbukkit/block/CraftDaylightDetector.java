package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TileEntityLightDetector;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.DaylightDetector;

public class CraftDaylightDetector extends CraftBlockEntityState<TileEntityLightDetector> implements DaylightDetector {

    public CraftDaylightDetector(final Block block) {
        super(block, TileEntityLightDetector.class);
    }

    public CraftDaylightDetector(final Material material, final TileEntityLightDetector te) {
        super(material, te);
    }
}
