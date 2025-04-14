package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.JukeboxSong;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#JUKEBOX_SONG}.
 *
 * @apiNote The fields provided here are a direct representation of
 * what is available from the vanilla game source. They may be
 * changed (including removals) on any Minecraft version
 * bump, so cross-version compatibility is not provided on the
 * same level as it is on most of the other API.
 */
@SuppressWarnings({
        "unused",
        "SpellCheckingInspection"
})
@GeneratedFrom("1.21.4")
@NullMarked
@ApiStatus.Experimental
public final class JukeboxSongKeys {
    /**
     * {@code minecraft:11}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<JukeboxSong> ELEVEN = create(key("11"));

    /**
     * {@code minecraft:13}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<JukeboxSong> THIRTEEN = create(key("13"));

    /**
     * {@code minecraft:5}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<JukeboxSong> FIVE = create(key("5"));

    /**
     * {@code minecraft:blocks}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<JukeboxSong> BLOCKS = create(key("blocks"));

    /**
     * {@code minecraft:cat}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<JukeboxSong> CAT = create(key("cat"));

    /**
     * {@code minecraft:chirp}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<JukeboxSong> CHIRP = create(key("chirp"));

    /**
     * {@code minecraft:creator}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<JukeboxSong> CREATOR = create(key("creator"));

    /**
     * {@code minecraft:creator_music_box}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<JukeboxSong> CREATOR_MUSIC_BOX = create(key("creator_music_box"));

    /**
     * {@code minecraft:far}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<JukeboxSong> FAR = create(key("far"));

    /**
     * {@code minecraft:mall}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<JukeboxSong> MALL = create(key("mall"));

    /**
     * {@code minecraft:mellohi}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<JukeboxSong> MELLOHI = create(key("mellohi"));

    /**
     * {@code minecraft:otherside}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<JukeboxSong> OTHERSIDE = create(key("otherside"));

    /**
     * {@code minecraft:pigstep}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<JukeboxSong> PIGSTEP = create(key("pigstep"));

    /**
     * {@code minecraft:precipice}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<JukeboxSong> PRECIPICE = create(key("precipice"));

    /**
     * {@code minecraft:relic}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<JukeboxSong> RELIC = create(key("relic"));

    /**
     * {@code minecraft:stal}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<JukeboxSong> STAL = create(key("stal"));

    /**
     * {@code minecraft:strad}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<JukeboxSong> STRAD = create(key("strad"));

    /**
     * {@code minecraft:wait}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<JukeboxSong> WAIT = create(key("wait"));

    /**
     * {@code minecraft:ward}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<JukeboxSong> WARD = create(key("ward"));

    private JukeboxSongKeys() {
    }

    /**
     * Creates a key for {@link JukeboxSong} in the registry {@code minecraft:jukebox_song}.
     *
     * @param key the value's key in the registry
     * @return a new typed key
     */
    @ApiStatus.Experimental
    public static TypedKey<JukeboxSong> create(final Key key) {
        return TypedKey.create(RegistryKey.JUKEBOX_SONG, key);
    }
}
