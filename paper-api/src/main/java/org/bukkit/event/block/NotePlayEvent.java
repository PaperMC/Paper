package org.bukkit.event.block;

import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a note block is being played through player interaction or a
 * redstone current.
 */
public class NotePlayEvent extends BlockEvent implements Cancellable {

    private static HandlerList handlers = new HandlerList();
    private Instrument instrument;
    private Note note;
    private boolean cancelled = false;

    public NotePlayEvent(@NotNull Block block, @NotNull Instrument instrument, @NotNull Note note) {
        super(block);
        this.instrument = instrument;
        this.note = note;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Gets the {@link Instrument} to be used.
     *
     * @return the Instrument
     */
    @NotNull
    public Instrument getInstrument() {
        return instrument;
    }

    /**
     * Gets the {@link Note} to be played.
     *
     * @return the Note
     */
    @NotNull
    public Note getNote() {
        return note;
    }

    /**
     * Overrides the {@link Instrument} to be used.
     *
     * @param instrument the Instrument. Has no effect if null.
     * @deprecated no effect on newer Minecraft versions
     */
    @Deprecated
    public void setInstrument(@NotNull Instrument instrument) {
        if (instrument != null) {
            this.instrument = instrument;
        }
    }

    /**
     * Overrides the {@link Note} to be played.
     *
     * @param note the Note. Has no effect if null.
     * @deprecated no effect on newer Minecraft versions
     */
    @Deprecated
    public void setNote(@NotNull Note note) {
        if (note != null) {
            this.note = note;
        }
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
