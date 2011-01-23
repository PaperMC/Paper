package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityNote;
import org.bukkit.block.Block;
import org.bukkit.block.NoteBlock;
import org.bukkit.craftbukkit.CraftWorld;

/**
 * Represents a note block.
 * 
 * @author sk89q
 */
public class CraftNoteBlock extends CraftBlockState implements NoteBlock {
    private final CraftWorld world;
    private final TileEntityNote note;

    public CraftNoteBlock(final Block block) {
        super(block);

        world = (CraftWorld)block.getWorld();
        note = (TileEntityNote)world.getTileEntityAt(getX(), getY(), getZ());
    }

    public byte getNote() {
        return note.e;
    }

    public void setNote(byte n) {
        note.e = n;
    }
}
