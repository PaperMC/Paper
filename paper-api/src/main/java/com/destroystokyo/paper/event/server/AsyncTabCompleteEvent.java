/*
 * Copyright (c) 2017 Daniel Ennis (Aikar) MIT License
 *
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.destroystokyo.paper.event.server;

import com.google.common.base.Preconditions;
import io.papermc.paper.util.TransformingRandomAccessList;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/// Allows plugins to compute tab completion results asynchronously.
///
/// If this event provides completions, then the standard synchronous process
/// will not be fired to populate the results.
/// However, the synchronous TabCompleteEvent will fire with the Async results.
///
/// Only 1 process will be allowed to provide completions, the Async Event, or the standard process.
@NullMarked
public class AsyncTabCompleteEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final CommandSender sender;
    private final String buffer;
    private final boolean isCommand;
    private final @Nullable Location location;
    private final List<Completion> completions = new ArrayList<>();
    private final List<String> stringCompletions = new TransformingRandomAccessList<>(
        this.completions,
        Completion::suggestion,
        Completion::completion
    );
    private boolean handled;
    private boolean cancelled;

    @ApiStatus.Internal
    public AsyncTabCompleteEvent(final CommandSender sender, final String buffer, final boolean isCommand, final @Nullable Location loc) {
        super(true);
        this.sender = sender;
        this.buffer = buffer;
        this.isCommand = isCommand;
        this.location = loc;
    }

    @Deprecated
    @ApiStatus.Internal
    public AsyncTabCompleteEvent(final CommandSender sender, final List<String> completions, final String buffer, final boolean isCommand, final @Nullable Location loc) {
        super(true);
        this.sender = sender;
        this.completions.addAll(fromStrings(completions));
        this.buffer = buffer;
        this.isCommand = isCommand;
        this.location = loc;
    }

    /// Get the sender completing this command.
    ///
    /// @return the [CommandSender] instance
    public CommandSender getSender() {
        return this.sender;
    }

    /// The list of completions which will be offered to the sender, in order.
    /// This list is mutable and reflects what will be offered.
    ///
    /// If this collection is not empty after the event is fired, then
    /// the standard process of calling [Command#tabComplete(CommandSender, String, String\[\])]
    /// or current player names will not be called.
    ///
    /// @return a list of offered completions
    public List<String> getCompletions() {
        return this.stringCompletions;
    }

    /// Set the completions offered, overriding any already set.
    /// If this collection is not empty after the event is fired, then
    /// the standard process of calling [Command#tabComplete(CommandSender, String, String\[\])]
    /// or current player names will not be called.
    ///
    /// The passed collection will be cloned to a new `List`. You must call [#getCompletions()] to mutate from here
    ///
    /// @param completions the new completions
    public void setCompletions(final List<String> completions) {
        Preconditions.checkArgument(completions != null, "Completions list cannot be null");
        if (completions == this.stringCompletions) {
            return;
        }
        this.completions.clear();
        this.completions.addAll(fromStrings(completions));
    }

    /// The list of [`completions`][Completion] which will be offered to the sender, in order.
    /// This list is mutable and reflects what will be offered.
    ///
    /// If this collection is not empty after the event is fired, then
    /// the standard process of calling [Command#tabComplete(CommandSender, String, String\[\])]
    /// or current player names will not be called.
    ///
    /// @return a list of offered completions
    public List<Completion> completions() {
        return this.completions;
    }

    /// Set the [`completions`][Completion] offered, overriding any already set.
    /// If this collection is not empty after the event is fired, then
    /// the standard process of calling [Command#tabComplete(CommandSender, String, String\[\])]
    /// or current player names will not be called.
    ///
    /// The passed collection will be cloned to a new `List`. You must call [#completions()] to mutate from here
    ///
    /// @param newCompletions the new completions
    public void completions(final List<Completion> newCompletions) {
        Preconditions.checkArgument(newCompletions != null, "new completions cannot be null");
        this.completions.clear();
        this.completions.addAll(newCompletions);
    }

    /// Return the entire buffer which formed the basis of this completion.
    ///
    /// @return command buffer, as entered
    public String getBuffer() {
        return this.buffer;
    }

    /// @return `true` if it is a command being tab completed, `false` if it is a chat message.
    public boolean isCommand() {
        return this.isCommand;
    }

    /// @return The position looked at by the sender, or `null` if none
    public @Nullable Location getLocation() {
        return this.location != null ? this.location.clone() : null;
    }

    /// If `true`, the standard process of calling [Command#tabComplete(CommandSender, String, String\[\])]
    /// or current player names will not be called.
    ///
    /// @return Is completions considered handled. Always `true` if completions is not empty.
    public boolean isHandled() {
        return !this.completions.isEmpty() || this.handled;
    }

    /// Sets whether to consider the completion request handled.
    /// If `true`, the standard process of calling [Command#tabComplete(CommandSender, String, String\[\])]
    /// or current player names will not be called.
    ///
    /// @param handled if this completion should be marked as being handled
    public void setHandled(final boolean handled) {
        this.handled = handled;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /// {@inheritDoc}
    ///
    /// Will provide no completions, and will not fire the synchronous process
    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    private static List<Completion> fromStrings(final List<String> suggestions) {
        final List<Completion> list = new ArrayList<>(suggestions.size());
        for (final String suggestion : suggestions) {
            list.add(new CompletionImpl(suggestion, null));
        }
        return list;
    }

    /// A rich tab completion, consisting of a string suggestion, and a nullable [Component] tooltip.
    public interface Completion {

        /// Get the suggestion string for this [Completion].
        ///
        /// @return suggestion string
        String suggestion();

        /// Get the suggestion tooltip for this [Completion].
        ///
        /// @return tooltip component
        @Nullable Component tooltip();

        /// Create a new [Completion] from a suggestion string.
        ///
        /// @param suggestion suggestion string
        /// @return new completion instance
        static Completion completion(final String suggestion) {
            return new CompletionImpl(suggestion, null);
        }

        /// Create a new [Completion] from a suggestion string and a tooltip [Component].
        ///
        /// If the provided component is `null`, the suggestion will not have a tooltip.
        ///
        /// @param suggestion suggestion string
        /// @param tooltip    tooltip component, or `null`
        /// @return new completion instance
        static Completion completion(final String suggestion, final @Nullable Component tooltip) {
            return new CompletionImpl(suggestion, tooltip);
        }
    }

    @ApiStatus.Internal
    record CompletionImpl(String suggestion, @Nullable Component tooltip) implements Completion {
        CompletionImpl {
            Objects.requireNonNull(suggestion, "suggestion");
        }
    }
}
