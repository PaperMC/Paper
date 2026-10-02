package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.holder.RegistryHolder;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.bukkit.JukeboxSong;
import org.bukkit.Sound;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;

/// A data-centric version-specific registry entry for the [JukeboxSong] type.
@ApiStatus.NonExtendable
public interface JukeboxSongRegistryEntry {

    /// Gets the sound event for this song.
    ///
    /// @return the sound event
    @Contract(pure = true)
    RegistryHolder<Sound, SoundEventRegistryEntry> soundEvent();

    /// Gets the description for this song.
    ///
    /// @return the description
    @Contract(pure = true)
    Component description();

    /// Gets the length in seconds for this song.
    ///
    /// @return the length in seconds
    @Contract(pure = true)
    @Positive float lengthInSeconds();

    /// Gets the comparator output for this song.
    ///
    /// @return the comparator output
    @Contract(pure = true)
    @Range(from = 0, to = 15) int comparatorOutput();

    /// A mutable builder for the [JukeboxSongRegistryEntry] plugins may change in applicable registry events.
    ///
    /// The following values are required for each builder:
    ///
    ///   - [#soundEvent(TypedKey)], [#soundEvent(Consumer)] or [#soundEvent(RegistryHolder)]
    ///   - [#description(Component)]
    ///   - [#lengthInSeconds(float)]
    ///   - [#comparatorOutput(int)]
    @ApiStatus.NonExtendable
    interface Builder extends JukeboxSongRegistryEntry, RegistryBuilder<JukeboxSong> {

        /// Sets the sound event for this song to a sound event present
        /// in the [io.papermc.paper.registry.RegistryKey#SOUND_EVENT] registry.
        ///
        /// This will override both [#soundEvent(Consumer)] and [#soundEvent(RegistryHolder)]
        ///
        /// @param soundEvent the sound event
        /// @return this builder instance
        /// @see #soundEvent(Consumer)
        @Contract(value = "_ -> this", mutates = "this")
        Builder soundEvent(TypedKey<Sound> soundEvent);

        /// Sets the sound event for this song to a new sound event.
        ///
        /// This will override both [#soundEvent(TypedKey)] and [#soundEvent(RegistryHolder)]
        ///
        /// @param soundEvent the sound event
        /// @return this builder instance
        /// @see #soundEvent(TypedKey)
        @Contract(value = "_ -> this", mutates = "this")
        Builder soundEvent(Consumer<RegistryBuilderFactory<Sound, ? extends SoundEventRegistryEntry.Builder>> soundEvent);

        /// Sets the sound event for this song.
        ///
        /// This will override both [#soundEvent(Consumer)] and [#soundEvent(TypedKey)]
        ///
        /// @param soundEvent the sound event
        /// @return this builder instance
        /// @see #soundEvent(TypedKey)
        /// @see #soundEvent(Consumer)
        @Contract(value = "_ -> this", mutates = "this")
        Builder soundEvent(RegistryHolder<Sound, SoundEventRegistryEntry> soundEvent);

        /// Sets the description for this song.
        ///
        /// @param description the description
        /// @return this builder instance
        @Contract(value = "_ -> this", mutates = "this")
        Builder description(Component description);

        /// Sets the length in seconds for this song.
        ///
        /// @param lengthInSeconds the length in seconds (positive)
        /// @return this builder instance
        @Contract(value = "_ -> this", mutates = "this")
        Builder lengthInSeconds(@Positive float lengthInSeconds);

        /// Sets the comparator output for this song.
        ///
        /// @param comparatorOutput the comparator output [0-15]
        /// @return this builder instance
        @Contract(value = "_ -> this", mutates = "this")
        Builder comparatorOutput(@Range(from = 0, to = 15) int comparatorOutput);
    }
}
