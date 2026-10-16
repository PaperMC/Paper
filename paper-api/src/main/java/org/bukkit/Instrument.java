package org.bukkit;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

public enum Instrument {

    /**
     * Piano (Harp) is the standard instrument for a note block.
     */
    PIANO(Sound.BLOCK_NOTE_BLOCK_HARP),
    /**
     * Bass drum is normally played when a note block is on top of a
     * stone-like block.
     */
    BASS_DRUM(Sound.BLOCK_NOTE_BLOCK_BASEDRUM),
    /**
     * Snare drum is normally played when a note block is on top of a sandy
     * block.
     */
    SNARE_DRUM(Sound.BLOCK_NOTE_BLOCK_SNARE),
    /**
     * Sticks (Hat) are normally played when a note block is on top of a glass
     * block.
     */
    STICKS(Sound.BLOCK_NOTE_BLOCK_HAT),
    /**
     * Bass guitar is normally played when a note block is on top of a wooden
     * block.
     */
    BASS_GUITAR(Sound.BLOCK_NOTE_BLOCK_BASS),
    /**
     * Flute is normally played when a note block is on top of a clay block.
     */
    FLUTE(Sound.BLOCK_NOTE_BLOCK_FLUTE),
    /**
     * Bell is normally played when a note block is on top of a gold block.
     */
    BELL(Sound.BLOCK_NOTE_BLOCK_BELL),
    /**
     * Guitar is normally played when a note block is on top of a woolen block.
     */
    GUITAR(Sound.BLOCK_NOTE_BLOCK_GUITAR),
    /**
     * Chime is normally played when a note block is on top of a packed ice
     * block.
     */
    CHIME(Sound.BLOCK_NOTE_BLOCK_CHIME),
    /**
     * Xylophone is normally played when a note block is on top of a bone block.
     */
    XYLOPHONE(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE),
    /**
     * Iron Xylophone is normally played when a note block is on top of an iron block.
     */
    IRON_XYLOPHONE(Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE),
    /**
     * Cow Bell is normally played when a note block is on top of a soul sand block.
     */
    COW_BELL(Sound.BLOCK_NOTE_BLOCK_COW_BELL),
    /**
     * Didgeridoo is normally played when a note block is on top of a pumpkin block.
     */
    DIDGERIDOO(Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO),
    /**
     * Bit is normally played when a note block is on top of an emerald block.
     */
    BIT(Sound.BLOCK_NOTE_BLOCK_BIT),
    /**
     * Banjo is normally played when a note block is on top of a hay block.
     */
    BANJO(Sound.BLOCK_NOTE_BLOCK_BANJO),
    /**
     * Pling is normally played when a note block is on top of a glowstone block.
     */
    PLING(Sound.BLOCK_NOTE_BLOCK_PLING),
    /**
     * Trumpet is normally played when a note block is on top of a copper block.
     */
    TRUMPET(Sound.BLOCK_NOTE_BLOCK_TRUMPET),
    /**
     * Trumpet exposed is normally played when a note block is on top of an exposed copper block.
     */
    TRUMPET_EXPOSED(Sound.BLOCK_NOTE_BLOCK_TRUMPET_EXPOSED),
    /**
     * Trumpet oxidized is normally played when a note block is on top of an oxidized copper block.
     */
    TRUMPET_OXIDIZED(Sound.BLOCK_NOTE_BLOCK_TRUMPET_OXIDIZED),
    /**
     * Trumpet weathered is normally played when a note block is on top of a weathered copper block.
     */
    TRUMPET_WEATHERED(Sound.BLOCK_NOTE_BLOCK_TRUMPET_WEATHERED),
    /**
     * Zombie is normally played when a Zombie Head is on top of the note block.
     */
    ZOMBIE(Sound.BLOCK_NOTE_BLOCK_IMITATE_ZOMBIE),
    /**
     * Skeleton is normally played when a Skeleton Head is on top of the note block.
     */
    SKELETON(Sound.BLOCK_NOTE_BLOCK_IMITATE_SKELETON),
    /**
     * Creeper is normally played when a Creeper Head is on top of the note block.
     */
    CREEPER(Sound.BLOCK_NOTE_BLOCK_IMITATE_CREEPER),
    /**
     * Dragon is normally played when a Dragon Head is on top of the note block.
     */
    DRAGON(Sound.BLOCK_NOTE_BLOCK_IMITATE_ENDER_DRAGON),
    /**
     * Wither Skeleton is normally played when a Wither Skeleton Head is on top of the note block.
     */
    WITHER_SKELETON(Sound.BLOCK_NOTE_BLOCK_IMITATE_WITHER_SKELETON),
    /**
     * Piglin is normally played when a Piglin Head is on top of the note block.
     */
    PIGLIN(Sound.BLOCK_NOTE_BLOCK_IMITATE_PIGLIN),
    /**
     * Custom Sound is normally played when a Player Head with the required data is on top of the note block.
     */
    CUSTOM_HEAD(null);

    private final Sound sound;

    Instrument(final Sound sound) {
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
     * @deprecated use {@link #ordinal()}, there's no meaning to this id
     */
    @Deprecated(since = "26.1")
    public byte getType() {
        return (byte) this.ordinal();
    }

    /**
     * Get an instrument by its type ID.
     *
     * @param type The type ID
     * @return The instrument
     * @deprecated type is just the ordinal of the enum, no meaning in the game
     */
    @Nullable
    @Deprecated(since = "26.1")
    public static Instrument getByType(final byte type) {
        return ArrayUtils.get(values(), type);
    }
}
