package org.bukkit.block;

import org.bukkit.Instrument;
import org.bukkit.Note;

/**
 * Represents a note.
 */
public interface NoteBlock extends BlockState {

    /**
     * Gets the note.
     *
     * @return The note.
     */
    public Note getNote();

    /**
     * Gets the note.
     *
     * @return The note ID.
     */
    public byte getRawNote();

    /**
     * Set the note.
     *
     * @param note The note.
     */
    public void setNote(Note note);

    /**
     * Set the note.
     *
     * @param note The note ID.
     */
    public void setRawNote(byte note);

    /**
     * Attempts to play the note at block
     * <p />
     * If the block is no longer a note block, this will return false
     *
     * @return true if successful, otherwise false
     */
    public boolean play();

    /**
     * Plays an arbitrary note with an arbitrary instrument
     *
     * @param instrument Instrument ID
     * @param note Note ID
     * @return true if successful, otherwise false
     */
    public boolean play(byte instrument, byte note);

    /**
     * Plays an arbitrary note with an arbitrary instrument
     *
     * @param instrument The instrument
     * @param note The note
     * @return true if successful, otherwise false
     * @see Instrument Note
     */
    public boolean play(Instrument instrument, Note note);
}
