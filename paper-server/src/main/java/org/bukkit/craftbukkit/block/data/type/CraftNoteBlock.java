package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftNoteBlock extends CraftBlockData implements NoteBlock {

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> INSTRUMENT = getEnum("instrument");
    private static final net.minecraft.world.level.block.state.properties.IntegerProperty NOTE = getInteger("note");

    @Override
    public org.bukkit.Instrument getInstrument() {
        return this.get(CraftNoteBlock.INSTRUMENT, org.bukkit.Instrument.class);
    }

    @Override
    public void setInstrument(org.bukkit.Instrument instrument) {
        this.set(CraftNoteBlock.INSTRUMENT, instrument);
    }

    @Override
    public org.bukkit.Note getNote() {
       return new org.bukkit.Note(this.get(CraftNoteBlock.NOTE));
    }

    @Override
    public void setNote(org.bukkit.Note note) {
        this.set(CraftNoteBlock.NOTE, (int) note.getId());
    }
}
