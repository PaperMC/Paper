package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.TestBlockMode;
import org.bukkit.block.data.type.TestBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftTestBlock extends CraftBlockData implements TestBlock {
    private static final EnumProperty<TestBlockMode> MODE = net.minecraft.world.level.block.TestBlock.MODE;

    public CraftTestBlock(BlockState state) {
        super(state);
    }

    @Override
    public TestBlock.Mode getMode() {
        return this.get(MODE, TestBlock.Mode.class);
    }

    @Override
    public void setMode(final TestBlock.Mode mode) {
        Preconditions.checkArgument(mode != null, "mode cannot be null!");
        this.set(MODE, mode);
    }
}
