/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftJukeBox extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Jukebox {

    public CraftJukeBox() {
        super();
    }

    public CraftJukeBox(net.minecraft.server.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftJukebox

    private static final net.minecraft.server.BlockStateBoolean HAS_RECORD = getBoolean(net.minecraft.server.BlockJukeBox.class, "has_record");

    @Override
    public boolean hasRecord() {
        return get(HAS_RECORD);
    }
}
