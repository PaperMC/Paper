package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityNote;

import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.NoteBlock;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftNoteBlock extends CraftBlockState implements NoteBlock {
    private final CraftWorld world;
    private final TileEntityNote note;

    public CraftNoteBlock(final Block block) {
        super(block);

        world = (CraftWorld) block.getWorld();
        note = (TileEntityNote) world.getTileEntityAt(getX(), getY(), getZ());
    }

    public Note getNote() {
        return new Note(note.note);
    }

    public byte getRawNote() {
        return note.note;
    }

    public void setNote(Note n) {
        note.note = n.getId();
    }

    public void setRawNote(byte n) {
        note.note = n;
    }

    public boolean play() {
        Block block = getBlock();

        if (block.getType() == Material.NOTE_BLOCK) {
            note.play(world.getHandle(), getX(), getY(), getZ());
            return true;
        } else {
            return false;
        }
    }

    public boolean play(byte instrument, byte note) {
        Block block = getBlock();

        if (block.getType() == Material.NOTE_BLOCK) {
            world.getHandle().playNote(getX(), getY(), getZ(), block.getTypeId(), instrument, note);
            return true;
        } else {
            return false;
        }
    }

    public boolean play(Instrument instrument, Note note) {
        Block block = getBlock();

        if (block.getType() == Material.NOTE_BLOCK) {
            world.getHandle().playNote(getX(), getY(), getZ(), block.getTypeId(), instrument.getType(), note.getId());
            return true;
        } else {
            return false;
        }
    }
}
