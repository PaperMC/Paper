package io.papermc.paper.registry.data;


import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.util.Either;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.bukkit.JukeboxSong;
import org.bukkit.Sound;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;

/**
 * A data-centric version-specific registry entry for the {@link JukeboxSong} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface JukeboxSongRegistryEntry {

    Either<TypedKey<Sound>, SoundEventRegistryEntry> soundEvent();

    Component description();

    @Positive float lengthInSeconds();

    @Range(from = 0, to = 15) int comparatorOutput();

    /**
     * A mutable builder for the {@link JukeboxSongRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>
     *         {@link #soundEvent(TypedKey)} or {@link #soundEvent(Consumer)}
     *     </li>
     *     <li>{@link #description(Component)}</li>
     *     <li>{@link #lengthInSeconds(float)}</li>
     *     <li>{@link #comparatorOutput(int)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends JukeboxSongRegistryEntry, RegistryBuilder<JukeboxSong> {

        @Contract(value = "_ -> this", mutates = "this")
        Builder soundEvent(TypedKey<Sound> soundEvent);

        @Contract(value = "_ -> this", mutates = "this")
        Builder soundEvent(Consumer<? super SoundEventRegistryEntry.Builder> soundEvent);

        @Contract(value = "_ -> this", mutates = "this")
        Builder description(Component description);

        @Contract(value = "_ -> this", mutates = "this")
        Builder lengthInSeconds(@Positive float lengthInSeconds);

        @Contract(value = "_ -> this", mutates = "this")
        Builder comparatorOutput(@Range(from = 0, to = 15) int comparatorOutput);
    }
}
