package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftNoteBlock extends CraftBlockData implements NoteBlock {
    private static final EnumProperty<NoteBlockInstrument> INSTRUMENT = net.minecraft.world.level.block.NoteBlock.INSTRUMENT;

    private static final IntegerProperty NOTE = net.minecraft.world.level.block.NoteBlock.NOTE;

    private static final BooleanProperty POWERED = net.minecraft.world.level.block.NoteBlock.POWERED;

    public CraftNoteBlock(BlockState state) {
        super(state);
    }

    @Override
    public Instrument getInstrument() {
        return this.get(INSTRUMENT, Instrument.class);
    }

    @Override
    public void setInstrument(final Instrument instrument) {
        Preconditions.checkArgument(instrument != null, "instrument cannot be null!");
        this.set(INSTRUMENT, instrument);
    }

    @Override
    public Note getNote() {
        return new Note(this.get(NOTE));
    }

    @Override
    public void setNote(final Note note) {
        Preconditions.checkArgument(note != null, "note cannot be null!");
        this.set(NOTE, (int) note.getId());
    }

    @Override
    public boolean isPowered() {
        return this.get(POWERED);
    }

    @Override
    public void setPowered(final boolean powered) {
        this.set(POWERED, powered);
    }
}
