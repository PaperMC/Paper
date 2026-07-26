package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.annotation.GeneratedClass;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.StructureMode;
import org.bukkit.block.data.type.StructureBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jspecify.annotations.NullMarked;

@NullMarked
@GeneratedClass
public class CraftStructureBlock extends CraftBlockData implements StructureBlock {
    private static final EnumProperty<StructureMode> MODE = net.minecraft.world.level.block.StructureBlock.MODE;

    public CraftStructureBlock(BlockState state) {
        super(state);
    }

    @Override
    public StructureBlock.Mode getMode() {
        return this.get(MODE, StructureBlock.Mode.class);
    }

    @Override
    public void setMode(final StructureBlock.Mode mode) {
        Preconditions.checkArgument(mode != null, "mode cannot be null!");
        this.set(MODE, mode);
    }
}
