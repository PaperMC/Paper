package org.bukkit;

import java.util.HashMap;
import java.util.Map;

/**
 * A note class to store a specific note.
 */
public class Note {

    /**
     * An enum holding tones.
     */
    public enum Tone {
        F((byte) -0x1, true),
        G((byte) 0x1, true),
        A((byte) 0x3, true),
        B((byte) 0x5, false),
        C((byte) 0x6, true),
        D((byte) 0x8, true),
        E((byte) 0xA, false);

        private final boolean sharpable;
        private final byte id;
        private static final Map<Byte, Note.Tone> tones = new HashMap<Byte, Note.Tone>();
        /** The number of tones including sharped tones. */
        public static final byte TONES_COUNT;

        private Tone(byte id, boolean sharpable) {
            this.id = id;
            this.sharpable = sharpable;
        }

        /**
         * Returns the not sharped id of this tone.
         *
         * @return the not sharped id of this tone.
         */
        public byte getId() {
            return getId(false);
        }

        /**
         * Returns the id of this tone. These method allows to return the
         * sharped id of the tone. If the tone couldn't be sharped it always
         * return the not sharped id of this tone.
         *
         * @param sharped
         *            Set to true to return the sharped id.
         * @return the id of this tone.
         */
        public byte getId(boolean sharped) {
            byte tempId = (byte) (sharped && sharpable ? id + 1 : id);

            while (tempId < 0) {
                tempId += TONES_COUNT;
            }
            return (byte) (tempId % TONES_COUNT);
        }

        /**
         * Returns if this tone could be sharped.
         *
         * @return if this tone could be sharped.
         */
        public boolean isSharpable() {
            return sharpable;
        }

        /**
         * Returns if this tone id is the sharped id of the tone.
         *
         * @param id
         *            the id of the tone.
         * @return if the tone id is the sharped id of the tone.
         * @throws IllegalArgumentException
         *             if neither the tone nor the semitone have the id.
         */
        public boolean isSharped(byte id) {
            if (id == getId(false)) {
                return false;
            } else if (id == getId(true)) {
                return true;
            } else {
                // The id isn't matching to the tone!
                throw new IllegalArgumentException("The id isn't matching to the tone.");
            }
        }

        /**
         * Returns the tone to id. Also returning the semitones.
         *
         * @param id
         *            the id of the tone.
         * @return the tone to id.
         */
        public static Tone getToneById(byte id) {
            return tones.get(id);
        }

        static {
            byte lowest = F.id;
            byte highest = F.id;
            for (Tone tone : Tone.values()) {
                byte id = tone.id;
                tones.put(id, tone);
                if (id < lowest) {
                    lowest = id;
                }
                if (tone.isSharpable()) {
                    id++;
                    tones.put(id, tone);
                }
                if (id > highest) {
                    highest = id;
                }
            }

            TONES_COUNT = (byte) (highest - lowest + 1);
            tones.put((byte) (TONES_COUNT - 1), F);
        }
    }

    private final byte note;

    /**
     * Creates a new note.
     *
     * @param note
     *            Internal note id. {@link #getId()} always return this value.
     *            The value has to be in the interval [0;&nbsp;24].
     */
    public Note(byte note) {
        if (note < 0 || note > 24) {
            throw new IllegalArgumentException("The note value has to be between 0 and 24.");
        }
        this.note = note;
    }

    /**
     * Creates a new note.
     *
     * @param octave
     *            The octave where the note is in. Has to be 0 - 2.
     * @param note
     *            The tone within the octave. If the octave is 2 the note has to
     *            be F#.
     * @param sharped
     *            Set it the tone is sharped (e.g. for F#).
     */
    public Note(byte octave, Tone note, boolean sharped) {
        if (sharped && !note.isSharpable()) {
            throw new IllegalArgumentException("This tone could not be sharped.");
        }
        if (octave < 0 || octave > 2 || (octave == 2 && !(note == Tone.F && sharped))) {
            throw new IllegalArgumentException("Tone and octave have to be between F#0 and F#2");
        }
        this.note = (byte) (octave * Tone.TONES_COUNT + note.getId(sharped));
    }

    /**
     * Returns the internal id of this note.
     *
     * @return the internal id of this note.
     */
    public byte getId() {
        return note;
    }

    /**
     * Returns the octave of this note.
     *
     * @return the octave of this note.
     */
    public int getOctave() {
        return note / Tone.TONES_COUNT;
    }

    private byte getToneByte() {
        return (byte) (note % Tone.TONES_COUNT);
    }

    /**
     * Returns the tone of this note.
     *
     * @return the tone of this note.
     */
    public Tone getTone() {
        return Tone.getToneById(getToneByte());
    }

    /**
     * Returns if this note is sharped.
     *
     * @return if this note is sharped.
     */
    public boolean isSharped() {
        byte note = getToneByte();
        return Tone.getToneById(note).isSharped(note);
    }

}
