/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftStructure extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.StructureBlock {

    public CraftStructure() {
        super();
    }

    public CraftStructure(net.minecraft.server.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftStructureBlock

    private static final net.minecraft.server.BlockStateEnum<?> MODE = getEnum(net.minecraft.server.BlockStructure.class, "mode");

    @Override
    public org.bukkit.block.data.type.StructureBlock.Mode getMode() {
        return get(MODE, org.bukkit.block.data.type.StructureBlock.Mode.class);
    }

    @Override
    public void setMode(org.bukkit.block.data.type.StructureBlock.Mode mode) {
        set(MODE, mode);
    }
}
