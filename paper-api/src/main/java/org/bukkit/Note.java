package org.bukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A note class to store a specific note.
 *
 * @since 1.0.0 R1
 */
public class Note {

    /**
     * An enum holding tones.
     */
    public enum Tone {
        G(0x1, true),
        A(0x3, true),
        B(0x5, false),
        C(0x6, true),
        D(0x8, true),
        E(0xA, false),
        F(0xB, true);

        private final boolean sharpable;
        private final byte id;

        private static final Map<Byte, Note.Tone> BY_DATA = Maps.newHashMap();
        /** The number of tones including sharped tones. */
        public static final byte TONES_COUNT = 12;

        private Tone(int id, boolean sharpable) {
            this.id = (byte) (id % TONES_COUNT);
            this.sharpable = sharpable;
        }

        /**
         * Returns the not sharped id of this tone.
         *
         * @return the not sharped id of this tone.
         * @apiNote Internal Use Only
         */
        @org.jetbrains.annotations.ApiStatus.Internal // Paper
        public byte getId() {
            return getId(false);
        }

        /**
         * Returns the id of this tone. These method allows to return the
         * sharped id of the tone. If the tone couldn't be sharped it always
         * return the not sharped id of this tone.
         *
         * @param sharped Set to true to return the sharped id.
         * @return the id of this tone.
         * @apiNote Internal Use Only
         */
        @org.jetbrains.annotations.ApiStatus.Internal // Paper
        public byte getId(boolean sharped) {
            byte id = (byte) (sharped && sharpable ? this.id + 1 : this.id);

            return (byte) (id % TONES_COUNT);
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
         * @param id the id of the tone.
         * @return if the tone id is the sharped id of the tone.
         * @throws IllegalArgumentException if neither the tone nor the
         *     semitone have the id.
         * @apiNote Internal Use Only
         */
        @org.jetbrains.annotations.ApiStatus.Internal // Paper
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
         * @param id the id of the tone.
         * @return the tone to id.
         * @apiNote Internal Use Only
         */
        @org.jetbrains.annotations.ApiStatus.Internal // Paper
        @Nullable
        public static Tone getById(byte id) {
            return BY_DATA.get(id);
        }

        static {
            for (Tone tone : values()) {
                int id = tone.id % TONES_COUNT;
                BY_DATA.put((byte) id, tone);

                if (tone.isSharpable()) {
                    id = (id + 1) % TONES_COUNT;
                    BY_DATA.put((byte) id, tone);
                }
            }
        }
    }

    private static final float[] pitchArray = new float[25];
    static {
        for (int i = 0; i <= 24; i++) {
            // See https://minecraft.wiki/w/Note_Block#Notes
            pitchArray[i] = (float) Math.pow(2, (i - 12) / 12f);
        }
    }

    private final byte note;

    /**
     * Creates a new note.
     *
     * @param note Internal note id. {@link #getId()} always return this
     *     value. The value has to be in the interval [0;&nbsp;24].
     */
    public Note(int note) {
        Preconditions.checkArgument(note >= 0 && note <= 24, "The note value has to be between 0 and 24.");

        this.note = (byte) note;
    }

    /**
     * Creates a new note.
     *
     * @param octave The octave where the note is in. Has to be 0 - 2.
     * @param tone The tone within the octave. If the octave is 2 the note has
     *     to be F#.
     * @param sharped Set if the tone is sharped (e.g. for F#).
     */
    public Note(int octave, @NotNull Tone tone, boolean sharped) {
        if (sharped && !tone.isSharpable()) {
            tone = Tone.values()[tone.ordinal() + 1];
            sharped = false;
        }
        if (octave < 0 || octave > 2 || (octave == 2 && !(tone == Tone.F && sharped))) {
            throw new IllegalArgumentException("Tone and octave have to be between F#0 and F#2");
        }

        this.note = (byte) (octave * Tone.TONES_COUNT + tone.getId(sharped));
    }

    /**
     * Creates a new note for a flat tone, such as A-flat.
     *
     * @param octave The octave where the note is in. Has to be 0 - 1.
     * @param tone The tone within the octave.
     * @return The new note.
     */
    @NotNull
    public static Note flat(int octave, @NotNull Tone tone) {
        Preconditions.checkArgument(octave != 2, "Octave cannot be 2 for flats");
        tone = tone == Tone.G ? Tone.F : Tone.values()[tone.ordinal() - 1];
        return new Note(octave, tone, tone.isSharpable());
    }

    /**
     * Creates a new note for a sharp tone, such as A-sharp.
     *
     * @param octave The octave where the note is in. Has to be 0 - 2.
     * @param tone The tone within the octave. If the octave is 2 the note has
     *     to be F#.
     * @return The new note.
     */
    @NotNull
    public static Note sharp(int octave, @NotNull Tone tone) {
        return new Note(octave, tone, true);
    }

    /**
     * Creates a new note for a natural tone, such as A-natural.
     *
     * @param octave The octave where the note is in. Has to be 0 - 1.
     * @param tone The tone within the octave.
     * @return The new note.
     */
    @NotNull
    public static Note natural(int octave, @NotNull Tone tone) {
        Preconditions.checkArgument(octave != 2, "Octave cannot be 2 for naturals");
        return new Note(octave, tone, false);
    }

    /**
     * @return The note a semitone above this one.
     * @since 1.1.0 R5
     */
    @NotNull
    public Note sharped() {
        Preconditions.checkArgument(note < 24, "This note cannot be sharped because it is the highest known note!");
        return new Note(note + 1);
    }

    /**
     * @return The note a semitone below this one.
     * @since 1.1.0 R5
     */
    @NotNull
    public Note flattened() {
        Preconditions.checkArgument(note > 0, "This note cannot be flattened because it is the lowest known note!");
        return new Note(note - 1);
    }

    /**
     * Returns the internal id of this note.
     *
     * @return the internal id of this note.
     * @apiNote Internal Use Only
     */
    @org.jetbrains.annotations.ApiStatus.Internal // Paper
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
    @NotNull
    public Tone getTone() {
        return Tone.getById(getToneByte());
    }

    /**
     * Returns if this note is sharped.
     *
     * @return if this note is sharped.
     */
    public boolean isSharped() {
        byte note = getToneByte();
        return Tone.getById(note).isSharped(note);
    }

    /**
     * Gets the pitch of this note. This is the value used with
     * {@link World#playSound} or the /playsound command.
     *
     * @return the pitch
     * @since 1.20.2
     */
    public float getPitch() {
        return pitchArray[this.note];
    }

    /**
     * @since 1.1.0 R2
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + note;
        return result;
    }

    /**
     * @since 1.1.0 R2
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Note other = (Note) obj;
        if (note != other.note)
            return false;
        return true;
    }

    /**
     * @since 1.1.0 R5
     */
    @Override
    public String toString() {
        return "Note{" + getTone().toString() + (isSharped() ? "#" : "") + "}";
    }
}
