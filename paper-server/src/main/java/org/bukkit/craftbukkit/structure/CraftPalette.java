package org.bukkit.craftbukkit.structure;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.world.level.levelgen.structure.templatesystem.DefinedStructure;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.structure.Palette;

public class CraftPalette implements Palette {

    private final DefinedStructure.a palette;
    private final IRegistryCustom registry;

    public CraftPalette(DefinedStructure.a palette, IRegistryCustom registry) {
        this.palette = palette;
        this.registry = registry;
    }

    @Override
    public List<BlockState> getBlocks() {
        List<BlockState> blocks = new ArrayList<>();
        for (DefinedStructure.BlockInfo blockInfo : palette.blocks()) {
            blocks.add(CraftBlockStates.getBlockState(registry, blockInfo.pos(), blockInfo.state(), blockInfo.nbt()));
        }
        return blocks;
    }

    @Override
    public int getBlockCount() {
        return palette.blocks().size();
    }
}
