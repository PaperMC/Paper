package org.bukkit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;

import org.junit.Test;

import com.google.common.collect.Lists;

public class NoteTest {
    @Test
    public void getToneByData() {
        for (Note.Tone tone : Note.Tone.values()) {
            assertThat(Note.Tone.getById(tone.getId()), is(tone));
        }
    }

    @Test
    public void verifySharpedData() {
        for (Note.Tone tone : Note.Tone.values()) {
            if (!tone.isSharpable()) return;

            assertFalse(tone.isSharped(tone.getId(false)));
            assertTrue(tone.isSharped(tone.getId(true)));
        }
    }

    @Test
    public void verifyUnknownToneData() {
        Collection<Byte> tones = Lists.newArrayList();
        for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++) {
            tones.add((byte) i);
        }

        for (Note.Tone tone : Note.Tone.values()) {
            if (tone.isSharpable()) tones.remove(tone.getId(true));
            tones.remove(tone.getId());
        }

        for (Byte data : tones) {
            assertThat(Note.Tone.getById(data), is(nullValue()));

            for (Note.Tone tone : Note.Tone.values()) {
                try {
                    tone.isSharped(data);

                    fail(data + " should throw IllegalArgumentException");
                } catch (IllegalArgumentException e) {
                    assertNotNull(e);
                }
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNoteBelowMin() {
        new Note((byte) -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNoteAboveMax() {
        new Note((byte) 25);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNoteOctaveBelowMax() {
        new Note((byte) -1, Note.Tone.A, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNoteOctaveAboveMax() {
        new Note((byte) 3, Note.Tone.A, true);
    }

    @Test
    public void createNoteOctaveNonSharpable() {
        Note note = new Note((byte) 0, Note.Tone.B, true);
        assertFalse(note.isSharped());
        assertThat(note.getTone(), is(Note.Tone.C));
    }

    @Test
    public void createNoteFlat() {
        Note note = Note.flat(0, Note.Tone.D);
        assertTrue(note.isSharped());
        assertThat(note.getTone(), is(Note.Tone.C));
    }

    @Test
    public void createNoteFlatNonFlattenable() {
        Note note = Note.flat(0, Note.Tone.C);
        assertFalse(note.isSharped());
        assertThat(note.getTone(), is(Note.Tone.B));
    }

    @Test
    public void testFlatWrapping() {
        Note note = Note.flat(1, Note.Tone.G);
        assertTrue(note.isSharped());
        assertThat(note.getTone(), is(Note.Tone.F));
    }

    @Test
    public void testFlatWrapping2() {
        Note note = new Note(1, Note.Tone.G, false).flattened();
        assertTrue(note.isSharped());
        assertThat(note.getTone(), is(Note.Tone.F));
    }

    @Test
    public void testSharpWrapping() {
        Note note = new Note(1, Note.Tone.F, false).sharped();
        assertTrue(note.isSharped());
        assertThat(note.getTone(), is(Note.Tone.F));
        assertEquals(note.getOctave(), 2);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSharpWrapping2() {
        new Note(2, Note.Tone.F, true).sharped();
    }

    @Test
    public void testHighest() {
        Note note = new Note(2, Note.Tone.F, true);
        assertEquals(note.getId(), (byte)24);
    }

    @Test
    public void testLowest() {
        Note note = new Note(0, Note.Tone.F, true);
        assertEquals(note.getId(), (byte)0);
    }

    @Test
    public void doo() {
        for (int i = 1; i <= 24; i++) {
            Note note = new Note((byte) i);
            int octave = note.getOctave();
            Note.Tone tone = note.getTone();
            boolean sharped = note.isSharped();

            Note newNote = new Note(octave, tone, sharped);
            assertThat(newNote, is(note));
            assertThat(newNote.getId(), is(note.getId()));
        }
    }
}
