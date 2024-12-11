/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftNote extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.NoteBlock, org.bukkit.block.data.Powerable {

    public CraftNote() {
        super();
    }

    public CraftNote(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftNoteBlock

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> INSTRUMENT = getEnum(net.minecraft.world.level.block.NoteBlock.class, "instrument");
    private static final net.minecraft.world.level.block.state.properties.IntegerProperty NOTE = getInteger(net.minecraft.world.level.block.NoteBlock.class, "note");

    @Override
    public org.bukkit.Instrument getInstrument() {
        return this.get(CraftNote.INSTRUMENT, org.bukkit.Instrument.class);
    }

    @Override
    public void setInstrument(org.bukkit.Instrument instrument) {
        this.set(CraftNote.INSTRUMENT, instrument);
    }

    @Override
    public org.bukkit.Note getNote() {
       return new org.bukkit.Note(this.get(CraftNote.NOTE));
    }

    @Override
    public void setNote(org.bukkit.Note note) {
        this.set(CraftNote.NOTE, (int) note.getId());
    }

    // org.bukkit.craftbukkit.block.data.CraftPowerable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty POWERED = getBoolean(net.minecraft.world.level.block.NoteBlock.class, "powered");

    @Override
    public boolean isPowered() {
        return this.get(CraftNote.POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        this.set(CraftNote.POWERED, powered);
    }
}
