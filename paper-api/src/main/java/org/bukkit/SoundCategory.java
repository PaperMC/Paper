package org.bukkit;

import net.kyori.adventure.sound.Sound;
import org.jspecify.annotations.NullMarked;

/**
 * An Enum of categories for sounds.
 */
@NullMarked
public enum SoundCategory implements Sound.Source.Provider {

    // Start generate - SoundCategory
    MASTER,
    MUSIC,
    RECORDS,
    WEATHER,
    BLOCKS,
    HOSTILE,
    NEUTRAL,
    PLAYERS,
    AMBIENT,
    VOICE,
    UI;
    // End generate - SoundCategory

    @Override
    public Sound.Source soundSource() {
        return switch (this) {
            case MASTER -> Sound.Source.MASTER;
            case MUSIC -> Sound.Source.MUSIC;
            case RECORDS -> Sound.Source.RECORD;
            case WEATHER -> Sound.Source.WEATHER;
            case BLOCKS -> Sound.Source.BLOCK;
            case HOSTILE -> Sound.Source.HOSTILE;
            case NEUTRAL -> Sound.Source.NEUTRAL;
            case PLAYERS -> Sound.Source.PLAYER;
            case AMBIENT -> Sound.Source.AMBIENT;
            case VOICE -> Sound.Source.VOICE;
            case UI -> Sound.Source.UI;
        };
    }
}
