package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.core.FrontAndTop;
import net.minecraft.world.level.block.CrafterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.bukkit.block.data.type.Crafter;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftCrafter extends CraftBlockData implements Crafter {
    private static final BooleanProperty CRAFTING = CrafterBlock.CRAFTING;

    private static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;

    private static final BooleanProperty TRIGGERED = CrafterBlock.TRIGGERED;

    public CraftCrafter(BlockState state) {
        super(state);
    }

    @Override
    public boolean isCrafting() {
        return this.get(CRAFTING);
    }

    @Override
    public void setCrafting(final boolean crafting) {
        this.set(CRAFTING, crafting);
    }

    @Override
    public org.bukkit.block.Orientation getOrientation() {
        return this.get(ORIENTATION, org.bukkit.block.Orientation.class);
    }

    @Override
    public void setOrientation(final org.bukkit.block.Orientation orientation) {
        Preconditions.checkArgument(orientation != null, "orientation cannot be null!");
        this.set(ORIENTATION, orientation);
    }

    @Override
    public boolean isTriggered() {
        return this.get(TRIGGERED);
    }

    @Override
    public void setTriggered(final boolean triggered) {
        this.set(TRIGGERED, triggered);
    }
}
