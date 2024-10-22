/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftHangingMoss extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.HangingMoss {

    public CraftHangingMoss() {
        super();
    }

    public CraftHangingMoss(net.minecraft.world.level.block.state.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftHangingMoss

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean TIP = getBoolean(net.minecraft.world.level.block.HangingMossBlock.class, "tip");

    @Override
    public boolean isTip() {
        return get(TIP);
    }

    @Override
    public void setTip(boolean tip) {
        set(TIP, tip);
    }
}
