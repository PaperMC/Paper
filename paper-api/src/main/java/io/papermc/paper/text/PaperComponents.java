package io.papermc.paper.text;

import java.io.IOException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Paper API-specific methods for working with {@link Component}s and related.
 */
@NullMarked
public final class PaperComponents {

    private PaperComponents() {
        throw new RuntimeException("PaperComponents is not to be instantiated!");
    }

    /**
     * Resolves a component with a specific command sender and subject.
     * <p>
     * Note that in Vanilla, elevated permissions are usually required to use
     * '@' selectors in various component types, but this method should not
     * check such permissions from the sender.
     * <p>
     * A {@link CommandSender} argument is required to resolve:
     * <ul>
     *     <li>{@link net.kyori.adventure.text.NBTComponent}</li>
     *     <li>{@link net.kyori.adventure.text.ScoreComponent}</li>
     *     <li>{@link net.kyori.adventure.text.SelectorComponent}</li>
     * </ul>
     * A {@link Entity} argument is optional to help resolve:
     * <ul>
     *     <li>{@link net.kyori.adventure.text.ScoreComponent}</li>
     * </ul>
     * {@link net.kyori.adventure.text.TranslatableComponent}s don't require any extra arguments.
     *
     * @param input the component to resolve
     * @param context the command sender to resolve with
     * @param scoreboardSubject the scoreboard subject to use (for use with {@link net.kyori.adventure.text.ScoreComponent}s)
     * @return the resolved component
     * @throws IOException if a syntax error tripped during resolving
     */
    public static Component resolveWithContext(final Component input, final @Nullable CommandSender context, final @Nullable Entity scoreboardSubject) throws IOException {
        return resolveWithContext(input, context, scoreboardSubject, true);
    }

    /**
     * Resolves a component with a specific command sender and subject.
     * <p>
     * Note that in Vanilla, elevated permissions are required to use
     * '@' selectors in various component types. If the boolean {@code bypassPermissions}
     * argument is {@code false}, the {@link CommandSender} argument will be used to query
     * those permissions.
     * <p>
     * A {@link CommandSender} argument is required to resolve:
     * <ul>
     *     <li>{@link net.kyori.adventure.text.NBTComponent}</li>
     *     <li>{@link net.kyori.adventure.text.ScoreComponent}</li>
     *     <li>{@link net.kyori.adventure.text.SelectorComponent}</li>
     * </ul>
     * A {@link Entity} argument is optional to help resolve:
     * <ul>
     *     <li>{@link net.kyori.adventure.text.ScoreComponent}</li>
     * </ul>
     * {@link net.kyori.adventure.text.TranslatableComponent}s don't require any extra arguments.
     *
     * @param input the component to resolve
     * @param context the command sender to resolve with
     * @param scoreboardSubject the scoreboard subject to use (for use with {@link net.kyori.adventure.text.ScoreComponent}s)
     * @param bypassPermissions true to bypass permissions checks for resolving components
     * @return the resolved component
     * @throws IOException if a syntax error tripped during resolving
     */
    @SuppressWarnings("deprecation") // using unsafe as a bridge
    public static Component resolveWithContext(final Component input, final @Nullable CommandSender context, final @Nullable Entity scoreboardSubject, final boolean bypassPermissions) throws IOException {
        return Bukkit.getUnsafe().resolveWithContext(input, context, scoreboardSubject, bypassPermissions);
    }

    /**
     * Return a component flattener that can use game data to resolve extra information about components.
     *
     * @return a component flattener
     */
    @SuppressWarnings("deprecation") // using unsafe as a bridge
    public static ComponentFlattener flattener() {
        return Bukkit.getUnsafe().componentFlattener();
    }

    /**
     * Get a serializer for {@link Component}s that will convert components to
     * a plain-text string.
     *
     * <p>Implementations may provide a serializer capable of processing any
     * information that requires access to implementation details.</p>
     *
     * @return a serializer to plain text
     * @deprecated will be removed in adventure 5.0.0, use {@link PlainTextComponentSerializer#plainText()}
     */
    @Deprecated(forRemoval = true, since = "1.18.1")
    public static PlainComponentSerializer plainSerializer() {
        return Bukkit.getUnsafe().plainComponentSerializer();
    }

    /**
     * Get a serializer for {@link Component}s that will convert components to
     * a plain-text string.
     *
     * <p>Implementations may provide a serializer capable of processing any
     * information that requires access to implementation details.</p>
     *
     * @return a serializer to plain text
     * @deprecated use {@link PlainTextComponentSerializer#plainText()}
     */
    @Deprecated(forRemoval = true, since = "1.18.2")
    public static PlainTextComponentSerializer plainTextSerializer() {
        return Bukkit.getUnsafe().plainTextSerializer();
    }

    /**
     * Get a serializer for {@link Component}s that will convert to and from the
     * standard JSON serialization format using Gson.
     *
     * <p>Implementations may provide a serializer capable of processing any
     * information that requires implementation details, such as legacy
     * (pre-1.16) hover events.</p>
     *
     * @return a json component serializer
     * @deprecated use {@link GsonComponentSerializer#gson()}
     */
    @Deprecated(forRemoval = true, since = "1.18.2")
    public static GsonComponentSerializer gsonSerializer() {
        return Bukkit.getUnsafe().gsonComponentSerializer();
    }

    /**
     * Get a serializer for {@link Component}s that will convert to and from the
     * standard JSON serialization format using Gson, downsampling any RGB colors
     * to their nearest {@link NamedTextColor} counterpart.
     *
     * <p>Implementations may provide a serializer capable of processing any
     * information that requires implementation details, such as legacy
     * (pre-1.16) hover events.</p>
     *
     * @return a json component serializer
     * @deprecated use {@link GsonComponentSerializer#colorDownsamplingGson()}
     */
    @Deprecated(forRemoval = true, since = "1.18.2")
    public static GsonComponentSerializer colorDownsamplingGsonSerializer() {
        return Bukkit.getUnsafe().colorDownsamplingGsonComponentSerializer();
    }

    /**
     * Get a serializer for {@link Component}s that will convert to and from the
     * legacy component format used by Bukkit. This serializer uses the
     * {@link LegacyComponentSerializer.Builder#useUnusualXRepeatedCharacterHexFormat()}
     * option to match upstream behavior.
     *
     * <p>This legacy serializer uses the standard section symbol to mark
     * formatting characters.</p>
     *
     * <p>Implementations may provide a serializer capable of processing any
     * information that requires access to implementation details.</p>
     *
     * @return a section serializer
     * @deprecated use {@link LegacyComponentSerializer#legacySection()}
     */
    @Deprecated(forRemoval = true, since = "1.18.2")
    public static LegacyComponentSerializer legacySectionSerializer() {
        return Bukkit.getUnsafe().legacyComponentSerializer();
    }
}
