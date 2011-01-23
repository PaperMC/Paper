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
}
