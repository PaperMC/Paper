package org.bukkit;

/**
 * An Enum of categories for sounds.
 */
public enum SoundCategory implements net.kyori.adventure.sound.Sound.Source.Provider { // Paper - implement Sound.Source.Provider

    MASTER,
    MUSIC,
    RECORDS,
    WEATHER,
    BLOCKS,
    HOSTILE,
    NEUTRAL,
    PLAYERS,
    AMBIENT,
    VOICE;

    // Paper start - implement Sound.Source.Provider
    @Override
    public net.kyori.adventure.sound.Sound.@org.jetbrains.annotations.NotNull Source soundSource() {
        return switch (this) {
            case MASTER -> net.kyori.adventure.sound.Sound.Source.MASTER;
            case MUSIC -> net.kyori.adventure.sound.Sound.Source.MUSIC;
            case RECORDS -> net.kyori.adventure.sound.Sound.Source.RECORD;
            case WEATHER -> net.kyori.adventure.sound.Sound.Source.WEATHER;
            case BLOCKS -> net.kyori.adventure.sound.Sound.Source.BLOCK;
            case HOSTILE -> net.kyori.adventure.sound.Sound.Source.HOSTILE;
            case NEUTRAL -> net.kyori.adventure.sound.Sound.Source.NEUTRAL;
            case PLAYERS -> net.kyori.adventure.sound.Sound.Source.PLAYER;
            case AMBIENT -> net.kyori.adventure.sound.Sound.Source.AMBIENT;
            case VOICE -> net.kyori.adventure.sound.Sound.Source.VOICE;
        };
    }
    // Paper end
}
