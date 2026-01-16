package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.annotation.GeneratedClass;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jspecify.annotations.NullMarked;

@NullMarked
@GeneratedClass
public class CraftRespawnAnchor extends CraftBlockData implements RespawnAnchor {
    private static final IntegerProperty CHARGE = RespawnAnchorBlock.CHARGE;

    public CraftRespawnAnchor(BlockState state) {
        super(state);
    }

    @Override
    public int getCharges() {
        return this.get(CHARGE);
    }

    @Override
    public void setCharges(final int charges) {
        this.set(CHARGE, charges);
    }

    @Override
    public int getMaximumCharges() {
        return CHARGE.max;
    }
}
