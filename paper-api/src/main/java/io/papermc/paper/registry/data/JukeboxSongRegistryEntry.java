package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.data.sound.SoundEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.JukeboxSong;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;

/**
 * A data-centric version-specific registry entry for the {@link JukeboxSong} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface JukeboxSongRegistryEntry {

    SoundEvent soundEvent();

    Component description();

    float lengthInSeconds();

    @Range(from = 0, to = 15) int comparatorOutput();

    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends JukeboxSongRegistryEntry, RegistryBuilder<JukeboxSong> {

        @Contract(value = "_ -> this", mutates = "this")
        Builder soundEvent(SoundEvent soundEvent);

        @Contract(value = "_ -> this", mutates = "this")
        Builder description(Component description);

        @Contract(value = "_ -> this", mutates = "this")
        Builder lengthInSeconds(float lengthInSeconds);

        @Contract(value = "_ -> this", mutates = "this")
        Builder comparatorOutput(@Range(from = 0, to = 15) int comparatorOutput);
    }

}
