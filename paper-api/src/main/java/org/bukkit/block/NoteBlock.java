package org.bukkit.block;

/**
 * Represents a note.
 * 
 * @author sk89q
 */
public interface NoteBlock extends BlockState {
    /**
     * Gets the note.
     * 
     * @return
     */
    public byte getNote();
    
    /**
     * Set the note.
     * 
     * @param note
     */
    public void setNote(byte note);
    /**
     * Attempts to play the note at block<br />
     * <br />
     * If the block is no longer a note block, this will return false
     *
     * @return true if successful, otherwise false
     */
    public boolean play();
}
