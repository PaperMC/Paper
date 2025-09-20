package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public enum Instrument {

    /**
     * Piano (Harp) is the standard instrument for a note block.
     */
    PIANO(0x0, Sound.BLOCK_NOTE_BLOCK_HARP),
    /**
     * Bass drum is normally played when a note block is on top of a
     * stone-like block.
     */
    BASS_DRUM(0x1, Sound.BLOCK_NOTE_BLOCK_BASEDRUM),
    /**
     * Snare drum is normally played when a note block is on top of a sandy
     * block.
     */
    SNARE_DRUM(0x2, Sound.BLOCK_NOTE_BLOCK_SNARE),
    /**
     * Sticks (Hat) are normally played when a note block is on top of a glass
     * block.
     */
    STICKS(0x3, Sound.BLOCK_NOTE_BLOCK_HAT),
    /**
     * Bass guitar is normally played when a note block is on top of a wooden
     * block.
     */
    BASS_GUITAR(0x4, Sound.BLOCK_NOTE_BLOCK_BASS),
    /**
     * Flute is normally played when a note block is on top of a clay block.
     */
    FLUTE(0x5, Sound.BLOCK_NOTE_BLOCK_FLUTE),
    /**
     * Bell is normally played when a note block is on top of a gold block.
     */
    BELL(0x6, Sound.BLOCK_NOTE_BLOCK_BELL),
    /**
     * Guitar is normally played when a note block is on top of a woolen block.
     */
    GUITAR(0x7, Sound.BLOCK_NOTE_BLOCK_GUITAR),
    /**
     * Chime is normally played when a note block is on top of a packed ice
     * block.
     */
    CHIME(0x8, Sound.BLOCK_NOTE_BLOCK_CHIME),
    /**
     * Xylophone is normally played when a note block is on top of a bone block.
     */
    XYLOPHONE(0x9, Sound.BLOCK_NOTE_BLOCK_XYLOPHONE),
    /**
     * Iron Xylophone is normally played when a note block is on top of an iron block.
     */
    IRON_XYLOPHONE(0xA, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE),
    /**
     * Cow Bell is normally played when a note block is on top of a soul sand block.
     */
    COW_BELL(0xB, Sound.BLOCK_NOTE_BLOCK_COW_BELL),
    /**
     * Didgeridoo is normally played when a note block is on top of a pumpkin block.
     */
    DIDGERIDOO(0xC, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO),
    /**
     * Bit is normally played when a note block is on top of an emerald block.
     */
    BIT(0xD, Sound.BLOCK_NOTE_BLOCK_BIT),
    /**
     * Banjo is normally played when a note block is on top of a hay block.
     */
    BANJO(0xE, Sound.BLOCK_NOTE_BLOCK_BANJO),
    /**
     * Pling is normally played when a note block is on top of a glowstone block.
     */
    PLING(0xF, Sound.BLOCK_NOTE_BLOCK_PLING),
    /**
     * Zombie is normally played when a Zombie Head is on top of the note block.
     */
    ZOMBIE(0x10, Sound.BLOCK_NOTE_BLOCK_IMITATE_ZOMBIE), // Paper
    /**
     * Skeleton is normally played when a Skeleton Head is on top of the note block.
     */
    SKELETON(0x11, Sound.BLOCK_NOTE_BLOCK_IMITATE_SKELETON), // Paper
    /**
     * Creeper is normally played when a Creeper Head is on top of the note block.
     */
    CREEPER(0x12, Sound.BLOCK_NOTE_BLOCK_IMITATE_CREEPER), // Paper
    /**
     * Dragon is normally played when a Dragon Head is on top of the note block.
     */
    DRAGON(0x13, Sound.BLOCK_NOTE_BLOCK_IMITATE_ENDER_DRAGON), // Paper
    /**
     * Wither Skeleton is normally played when a Wither Skeleton Head is on top of the note block.
     */
    WITHER_SKELETON(0x14, Sound.BLOCK_NOTE_BLOCK_IMITATE_WITHER_SKELETON), // Paper
    /**
     * Piglin is normally played when a Piglin Head is on top of the note block.
     */
    PIGLIN(0x15, Sound.BLOCK_NOTE_BLOCK_IMITATE_PIGLIN), // Paper
    /**
     * Custom Sound is normally played when a Player Head with the required data is on top of the note block.
     */
    CUSTOM_HEAD(0x16, null); // Paper

    private final byte type;
    private final Sound sound;
    private static final Map<Byte, Instrument> BY_DATA = Maps.newHashMap();

    // Paper - remove ctor (the server still uses the byte magic value)

    private Instrument(final int type, final Sound sound) {
        this.type = (byte) type;
        this.sound = sound;
    }

    /**
     * Gets the sound associated with this instrument. <br>
     * Will be null for {@link Instrument#CUSTOM_HEAD}
     *
     * @return the sound or null
     */
    @Nullable
    public Sound getSound() {
        return this.sound;
    }

    /**
     * @return The type ID of this instrument.
     */
    @org.jetbrains.annotations.ApiStatus.Internal // Paper
    public byte getType() {
        return this.type;
    }

    /**
     * Get an instrument by its type ID.
     *
     * @param type The type ID
     * @return The instrument
     */
    @org.jetbrains.annotations.ApiStatus.Internal // Paper
    @Nullable
    public static Instrument getByType(final byte type) {
        return BY_DATA.get(type);
    }

    static {
        for (Instrument instrument : Instrument.values()) {
            BY_DATA.put(instrument.getType(), instrument);
        }
    }
}
