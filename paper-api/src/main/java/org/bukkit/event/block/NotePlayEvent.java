package org.bukkit.event.block;

import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a note block is being played through player interaction or a
 * redstone current.
 */
public interface NotePlayEvent extends BlockEvent, Cancellable {

    /**
     * Gets the {@link Instrument} to be used.
     *
     * @return the Instrument
     */
    Instrument getInstrument();

    /**
     * Overrides the {@link Instrument} to be used.
     * <p>
     * Only works when the note block isn't under a player head.
     * For this specific case the 'note_block_sound' property of the
     * player head state takes the priority.
     *
     * @param instrument the Instrument.
     */
    void setInstrument(Instrument instrument);

    /**
     * Gets the {@link Note} to be played.
     *
     * @return the Note
     */
    Note getNote();

    /**
     * Overrides the {@link Note} to be played.
     *
     * @param note the Note.
     */
    void setNote(Note note);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
