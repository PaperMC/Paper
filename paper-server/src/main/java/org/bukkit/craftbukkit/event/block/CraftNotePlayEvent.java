package org.bukkit.craftbukkit.event.block;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.NotePlayEvent;

public class CraftNotePlayEvent extends CraftBlockEvent implements NotePlayEvent {

    private Instrument instrument;
    private Note note;

    private boolean cancelled;

    public CraftNotePlayEvent(final Block block, final Instrument instrument, final Note note) {
        super(block);
        this.instrument = instrument;
        this.note = note;
    }

    public CraftNotePlayEvent(final Level level, final BlockPos pos, final NoteBlockInstrument instrument, final int note) {
        this(CraftBlock.at(level, pos), Instrument.values()[instrument.ordinal()], new Note(note));
    }

    @Override
    public Instrument getInstrument() {
        return this.instrument;
    }

    @Override
    public void setInstrument(final Instrument instrument) {
        Preconditions.checkArgument(instrument != null, "instrument cannot be null");
        this.instrument = instrument;
    }

    @Override
    public Note getNote() {
        return this.note;
    }

    @Override
    public void setNote(final Note note) {
        Preconditions.checkArgument(note != null, "note cannot be null");
        this.note = note;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return NotePlayEvent.getHandlerList();
    }
}
