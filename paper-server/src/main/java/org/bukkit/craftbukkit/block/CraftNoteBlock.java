package org.bukkit.craftbukkit.block;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.TileEntityNote;

import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.NoteBlock;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class CraftNoteBlock extends CraftBlockEntityState<TileEntityNote> implements NoteBlock {

    public CraftNoteBlock(final Block block) {
        super(block, TileEntityNote.class);
    }

    public CraftNoteBlock(final Material material, final TileEntityNote te) {
        super(material, te);
    }

    @Override
    public Note getNote() {
        return new Note(this.getSnapshot().note);
    }

    @Override
    public byte getRawNote() {
        return this.getSnapshot().note;
    }

    @Override
    public void setNote(Note note) {
        this.getSnapshot().note = note.getId();
    }

    @Override
    public void setRawNote(byte note) {
        this.getSnapshot().note = note;
    }

    @Override
    public boolean play() {
        Block block = getBlock();

        if (block.getType() == Material.NOTE_BLOCK) {
            TileEntityNote note = (TileEntityNote) this.getTileEntityFromWorld();
            CraftWorld world = (CraftWorld) this.getWorld();
            note.play(world.getHandle(), new BlockPosition(getX(), getY(), getZ()));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean play(byte instrument, byte note) {
        Block block = getBlock();

        if (block.getType() == Material.NOTE_BLOCK) {
            CraftWorld world = (CraftWorld) this.getWorld();
            world.getHandle().playBlockAction(new BlockPosition(getX(), getY(), getZ()), CraftMagicNumbers.getBlock(block), instrument, note);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean play(Instrument instrument, Note note) {
        Block block = getBlock();

        if (block.getType() == Material.NOTE_BLOCK) {
            CraftWorld world = (CraftWorld) this.getWorld();
            world.getHandle().playBlockAction(new BlockPosition(getX(), getY(), getZ()), CraftMagicNumbers.getBlock(block), instrument.getType(), note.getId());
            return true;
        } else {
            return false;
        }
    }
}
