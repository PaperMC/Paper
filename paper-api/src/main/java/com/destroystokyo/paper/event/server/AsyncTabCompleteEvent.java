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
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
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
public interface AsyncTabCompleteEvent extends Event, Cancellable {

    /**
     * Get the sender completing this command.
     *
     * @return the {@link CommandSender} instance
     */
    CommandSender getSender();

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
    List<String> getCompletions();

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
    void setCompletions(List<String> completions);

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
    List<Completion> completions();

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
    void completions(List<Completion> newCompletions);

    /**
     * Return the entire buffer which formed the basis of this completion.
     *
     * @return command buffer, as entered
     */
    String getBuffer();

    /**
     * @return {@code true} if it is a command being tab completed, {@code false} if it is a chat message.
     */
    boolean isCommand();

    /**
     * @return The position looked at by the sender, or {@code null} if none
     */
    @Nullable Location getLocation();

    /**
     * If {@code true}, the standard process of calling {@link Command#tabComplete(CommandSender, String, String[])}
     * or current player names will not be called.
     *
     * @return Is completions considered handled. Always {@code true} if completions is not empty.
     */
    boolean isHandled();

    /**
     * Sets whether to consider the completion request handled.
     * If {@code true}, the standard process of calling {@link Command#tabComplete(CommandSender, String, String[])}
     * or current player names will not be called.
     *
     * @param handled if this completion should be marked as being handled
     */
    void setHandled(boolean handled);

    /**
     * {@inheritDoc}
     * <br>
     * Will provide no completions, and will not fire the synchronous process
     */
    @Override
    void setCancelled(final boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * A rich tab completion, consisting of a string suggestion, and a nullable {@link Component} tooltip.
     */
    interface Completion {

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

        /**
         * Create a new {@link Completion} from a suggestion string.
         *
         * @param suggestion suggestion string
         * @return new completion instance
         */
        static Completion completion(final String suggestion) {
            return completion(suggestion, null);
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
            Preconditions.checkArgument(suggestion != null, "suggestion cannot be null");
            record CompletionImpl(String suggestion, @Nullable Component tooltip) implements Completion {
            }

            return new CompletionImpl(suggestion, tooltip);
        }
    }
}
