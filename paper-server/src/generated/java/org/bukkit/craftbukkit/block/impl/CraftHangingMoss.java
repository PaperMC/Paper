package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.annotation.GeneratedClass;
import net.minecraft.world.level.block.HangingMossBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.bukkit.block.data.type.HangingMoss;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jspecify.annotations.NullMarked;

@NullMarked
@GeneratedClass
public class CraftHangingMoss extends CraftBlockData implements HangingMoss {
    private static final BooleanProperty TIP = HangingMossBlock.TIP;

    public CraftHangingMoss(BlockState state) {
        super(state);
    }

    @Override
    public boolean isTip() {
        return this.get(TIP);
    }

    @Override
    public void setTip(final boolean tip) {
        this.set(TIP, tip);
    }
}
