package org.bukkit;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a song which may play in a Jukebox.
 */
@NullMarked
public interface JukeboxSong extends Keyed, Translatable {

    // Start generate - JukeboxSong
    JukeboxSong ELEVEN = get("11");

    JukeboxSong THIRTEEN = get("13");

    JukeboxSong FIVE = get("5");

    JukeboxSong BLOCKS = get("blocks");

    JukeboxSong CAT = get("cat");

    JukeboxSong CHIRP = get("chirp");

    JukeboxSong CREATOR = get("creator");

    JukeboxSong CREATOR_MUSIC_BOX = get("creator_music_box");

    JukeboxSong FAR = get("far");

    JukeboxSong LAVA_CHICKEN = get("lava_chicken");

    JukeboxSong MALL = get("mall");

    JukeboxSong MELLOHI = get("mellohi");

    JukeboxSong OTHERSIDE = get("otherside");

    JukeboxSong PIGSTEP = get("pigstep");

    JukeboxSong PRECIPICE = get("precipice");

    JukeboxSong RELIC = get("relic");

    JukeboxSong STAL = get("stal");

    JukeboxSong STRAD = get("strad");

    JukeboxSong TEARS = get("tears");

    JukeboxSong WAIT = get("wait");

    JukeboxSong WARD = get("ward");
    // End generate - JukeboxSong

    private static JukeboxSong get(String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.JUKEBOX_SONG).getOrThrow(NamespacedKey.minecraft(key));
    }

    /**
     * @deprecated this method assumes that jukebox song description will
     * always be a translatable component which is not guaranteed.
     */
    @Override
    @Deprecated(forRemoval = true)
    String getTranslationKey();
}
