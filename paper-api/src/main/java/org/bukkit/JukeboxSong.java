package org.bukkit;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a song which may play in a Jukebox.
 */
@ApiStatus.Experimental
public interface JukeboxSong extends Keyed, Translatable {

    public static final JukeboxSong THIRTEEN = get("13");
    public static final JukeboxSong CAT = get("cat");
    public static final JukeboxSong BLOCKS = get("blocks");
    public static final JukeboxSong CHIRP = get("chirp");
    public static final JukeboxSong FAR = get("far");
    public static final JukeboxSong MALL = get("mall");
    public static final JukeboxSong MELLOHI = get("mellohi");
    public static final JukeboxSong STAL = get("stal");
    public static final JukeboxSong STRAD = get("strad");
    public static final JukeboxSong WARD = get("ward");
    public static final JukeboxSong ELEVEN = get("11");
    public static final JukeboxSong WAIT = get("wait");
    public static final JukeboxSong PIGSTEP = get("pigstep");
    public static final JukeboxSong OTHERSIDE = get("otherside");
    public static final JukeboxSong FIVE = get("5");
    public static final JukeboxSong RELIC = get("relic");
    public static final JukeboxSong PRECIPICE = get("precipice");
    public static final JukeboxSong CREATOR = get("creator");
    public static final JukeboxSong CREATOR_MUSIC_BOX = get("creator_music_box");

    @NotNull
    private static JukeboxSong get(@NotNull String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.JUKEBOX_SONG).getOrThrow(NamespacedKey.minecraft(key));
    }

    // Paper start - adventure
    /**
     * @deprecated this method assumes that jukebox song description will
     * always be a translatable component which is not guaranteed.
     */
    @Override
    @Deprecated(forRemoval = true)
    @org.jetbrains.annotations.NotNull String getTranslationKey();
    // Paper end - adventure
}
