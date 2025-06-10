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
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Allows plugins to compute tab completion results asynchronously.
 * <p>
 * If this event provides completions, then the standard synchronous process
 * will not be fired to populate the results.
 * However, the synchronous TabCompleteEvent will fire with the Async results.
 * <p>
 * Only 1 process will be allowed to provide completions, the Async Event, or the standard process.
 */
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

    /**
     * Get the sender completing this command.
     *
     * @return the {@link CommandSender} instance
     */
    public CommandSender getSender() {
        return this.sender;
    }

    /**
     * The list of completions which will be offered to the sender, in order.
     * This list is mutable and reflects what will be offered.
     * <p>
     * If this collection is not empty after the event is fired, then
     * the standard process of calling {@link Command#tabComplete(CommandSender, String, String[])}
     * or current player names will not be called.
     *
     * @return a list of offered completions
     */
    public List<String> getCompletions() {
        return this.stringCompletions;
    }

    /**
     * Set the completions offered, overriding any already set.
     * If this collection is not empty after the event is fired, then
     * the standard process of calling {@link Command#tabComplete(CommandSender, String, String[])}
     * or current player names will not be called.
     * <p>
     * The passed collection will be cloned to a new {@code List}. You must call {@link #getCompletions()} to mutate from here
     *
     * @param completions the new completions
     */
    public void setCompletions(final List<String> completions) {
        Preconditions.checkArgument(completions != null, "Completions list cannot be null");
        if (completions == this.stringCompletions) {
            return;
        }
        this.completions.clear();
        this.completions.addAll(fromStrings(completions));
    }

    /**
     * The list of {@link Completion completions} which will be offered to the sender, in order.
     * This list is mutable and reflects what will be offered.
     * <p>
     * If this collection is not empty after the event is fired, then
     * the standard process of calling {@link Command#tabComplete(CommandSender, String, String[])}
     * or current player names will not be called.
     *
     * @return a list of offered completions
     */
    public List<Completion> completions() {
        return this.completions;
    }

    /**
     * Set the {@link Completion completions} offered, overriding any already set.
     * If this collection is not empty after the event is fired, then
     * the standard process of calling {@link Command#tabComplete(CommandSender, String, String[])}
     * or current player names will not be called.
     * <p>
     * The passed collection will be cloned to a new {@code List}. You must call {@link #completions()} to mutate from here
     *
     * @param newCompletions the new completions
     */
    public void completions(final List<Completion> newCompletions) {
        Preconditions.checkArgument(newCompletions != null, "new completions cannot be null");
        this.completions.clear();
        this.completions.addAll(newCompletions);
    }

    /**
     * Return the entire buffer which formed the basis of this completion.
     *
     * @return command buffer, as entered
     */
    public String getBuffer() {
        return this.buffer;
    }

    /**
     * @return {@code true} if it is a command being tab completed, {@code false} if it is a chat message.
     */
    public boolean isCommand() {
        return this.isCommand;
    }

    /**
     * @return The position looked at by the sender, or {@code null} if none
     */
    public @Nullable Location getLocation() {
        return this.location != null ? this.location.clone() : null;
    }

    /**
     * If {@code true}, the standard process of calling {@link Command#tabComplete(CommandSender, String, String[])}
     * or current player names will not be called.
     *
     * @return Is completions considered handled. Always {@code true} if completions is not empty.
     */
    public boolean isHandled() {
        return !this.completions.isEmpty() || this.handled;
    }

    /**
     * Sets whether to consider the completion request handled.
     * If {@code true}, the standard process of calling {@link Command#tabComplete(CommandSender, String, String[])}
     * or current player names will not be called.
     *
     * @param handled if this completion should be marked as being handled
     */
    public void setHandled(final boolean handled) {
        this.handled = handled;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * {@inheritDoc}
     * <br>
     * Will provide no completions, and will not fire the synchronous process
     */
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

    /**
     * A rich tab completion, consisting of a string suggestion, and a nullable {@link Component} tooltip.
     */
    public interface Completion extends Examinable {

        /**
         * Get the suggestion string for this {@link Completion}.
         *
         * @return suggestion string
         */
        String suggestion();

        /**
         * Get the suggestion tooltip for this {@link Completion}.
         *
         * @return tooltip component
         */
        @Nullable Component tooltip();

        @Override
        default Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of(ExaminableProperty.of("suggestion", this.suggestion()), ExaminableProperty.of("tooltip", this.tooltip()));
        }

        /**
         * Create a new {@link Completion} from a suggestion string.
         *
         * @param suggestion suggestion string
         * @return new completion instance
         */
        static Completion completion(final String suggestion) {
            return new CompletionImpl(suggestion, null);
        }

        /**
         * Create a new {@link Completion} from a suggestion string and a tooltip {@link Component}.
         * <p>
         * If the provided component is {@code null}, the suggestion will not have a tooltip.
         *
         * @param suggestion suggestion string
         * @param tooltip    tooltip component, or {@code null}
         * @return new completion instance
         */
        static Completion completion(final String suggestion, final @Nullable Component tooltip) {
            return new CompletionImpl(suggestion, tooltip);
        }
    }

    @ApiStatus.Internal
    static final class CompletionImpl implements Completion {

        private final String suggestion;
        private final @Nullable Component tooltip;

        CompletionImpl(final String suggestion, final @Nullable Component tooltip) {
            this.suggestion = suggestion;
            this.tooltip = tooltip;
        }

        @Override
        public String suggestion() {
            return this.suggestion;
        }

        @Override
        public @Nullable Component tooltip() {
            return this.tooltip;
        }

        @Override
        public boolean equals(final @Nullable Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final CompletionImpl that = (CompletionImpl) o;
            return this.suggestion.equals(that.suggestion)
                && Objects.equals(this.tooltip, that.tooltip);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.suggestion, this.tooltip);
        }

        @Override
        public String toString() {
            return StringExaminer.simpleEscaping().examine(this);
        }
    }
}
