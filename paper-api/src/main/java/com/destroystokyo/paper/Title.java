package com.destroystokyo.paper;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Represents a title to may be sent to a {@link Player}.
 *
 * <p>A title can be sent without subtitle text.</p>
 *
 * @deprecated use {@link net.kyori.adventure.title.Title}
 */
@Deprecated(since = "1.16.5")
public final class Title {

    /**
     * The default number of ticks for the title to fade in.
     */
    public static final int DEFAULT_FADE_IN = 20;
    /**
     * The default number of ticks for the title to stay.
     */
    public static final int DEFAULT_STAY = 200;
    /**
     * The default number of ticks for the title to fade out.
     */
    public static final int DEFAULT_FADE_OUT = 20;

    private final BaseComponent[] title;
    private final BaseComponent[] subtitle;
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    /**
     * Create a title with the default time values and no subtitle.
     *
     * <p>Times use default values.</p>
     *
     * @param title the main text of the title
     * @throws NullPointerException if the title is null
     */
    public Title(@NotNull BaseComponent title) {
        this(title, null);
    }

    /**
     * Create a title with the default time values and no subtitle.
     *
     * <p>Times use default values.</p>
     *
     * @param title the main text of the title
     * @throws NullPointerException if the title is null
     */
    public Title(@NotNull BaseComponent[] title) {
        this(title, null);
    }

    /**
     * Create a title with the default time values and no subtitle.
     *
     * <p>Times use default values.</p>
     *
     * @param title the main text of the title
     * @throws NullPointerException if the title is null
     */
    public Title(@NotNull String title) {
        this(title, null);
    }

    /**
     * Create a title with the default time values.
     *
     * <p>Times use default values.</p>
     *
     * @param title    the main text of the title
     * @param subtitle the secondary text of the title
     */
    public Title(@NotNull BaseComponent title, @Nullable BaseComponent subtitle) {
        this(title, subtitle, DEFAULT_FADE_IN, DEFAULT_STAY, DEFAULT_FADE_OUT);
    }

    /**
     * Create a title with the default time values.
     *
     * <p>Times use default values.</p>
     *
     * @param title    the main text of the title
     * @param subtitle the secondary text of the title
     */
    public Title(@NotNull BaseComponent[] title, @Nullable BaseComponent[] subtitle) {
        this(title, subtitle, DEFAULT_FADE_IN, DEFAULT_STAY, DEFAULT_FADE_OUT);
    }

    /**
     * Create a title with the default time values.
     *
     * <p>Times use default values.</p>
     *
     * @param title    the main text of the title
     * @param subtitle the secondary text of the title
     */
    public Title(@NotNull String title, @Nullable String subtitle) {
        this(title, subtitle, DEFAULT_FADE_IN, DEFAULT_STAY, DEFAULT_FADE_OUT);
    }

    /**
     * Creates a new title.
     *
     * @param title    the main text of the title
     * @param subtitle the secondary text of the title
     * @param fadeIn   the number of ticks for the title to fade in
     * @param stay     the number of ticks for the title to stay on screen
     * @param fadeOut  the number of ticks for the title to fade out
     * @throws IllegalArgumentException if any of the times are negative
     */
    public Title(@NotNull BaseComponent title, @Nullable BaseComponent subtitle, int fadeIn, int stay, int fadeOut) {
        this(
                new BaseComponent[]{checkNotNull(title, "title")},
                subtitle == null ? null : new BaseComponent[]{subtitle},
                fadeIn,
                stay,
                fadeOut
        );
    }

    /**
     * Creates a new title.
     *
     * @param title    the main text of the title
     * @param subtitle the secondary text of the title
     * @param fadeIn   the number of ticks for the title to fade in
     * @param stay     the number of ticks for the title to stay on screen
     * @param fadeOut  the number of ticks for the title to fade out
     * @throws IllegalArgumentException if any of the times are negative
     */
    public Title(@Nullable BaseComponent[] title, @NotNull BaseComponent[] subtitle, int fadeIn, int stay, int fadeOut) {
        checkArgument(fadeIn >= 0, "Negative fadeIn: %s", fadeIn);
        checkArgument(stay >= 0, "Negative stay: %s", stay);
        checkArgument(fadeOut >= 0, "Negative fadeOut: %s", fadeOut);
        this.title = checkNotNull(title, "title");
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    /**
     * Creates a new title.
     *
     * <p>It is recommended to use the {@link BaseComponent} constructors.</p>
     *
     * @param title    the main text of the title
     * @param subtitle the secondary text of the title
     * @param fadeIn   the number of ticks for the title to fade in
     * @param stay     the number of ticks for the title to stay on screen
     * @param fadeOut  the number of ticks for the title to fade out
     */
    public Title(@NotNull String title, @Nullable String subtitle, int fadeIn, int stay, int fadeOut) {
        this(
                TextComponent.fromLegacyText(checkNotNull(title, "title")),
                subtitle == null ? null : TextComponent.fromLegacyText(subtitle),
                fadeIn,
                stay,
                fadeOut
        );
    }

    /**
     * Gets the text of this title
     *
     * @return the text
     */
    @NotNull
    public BaseComponent[] getTitle() {
        return this.title;
    }

    /**
     * Gets the text of this title's subtitle
     *
     * @return the text
     */
    @Nullable
    public BaseComponent[] getSubtitle() {
        return this.subtitle;
    }

    /**
     * Gets the number of ticks to fade in.
     *
     * <p>The returned value is never negative.</p>
     *
     * @return the number of ticks to fade in
     */
    public int getFadeIn() {
        return this.fadeIn;
    }

    /**
     * Gets the number of ticks to stay.
     *
     * <p>The returned value is never negative.</p>
     *
     * @return the number of ticks to stay
     */
    public int getStay() {
        return this.stay;
    }

    /**
     * Gets the number of ticks to fade out.
     *
     * <p>The returned value is never negative.</p>
     *
     * @return the number of ticks to fade out
     */
    public int getFadeOut() {
        return this.fadeOut;
    }

    /**
     * Sends the title directly to a player
     *
     * @param player the receiver of the title
     */
    public void send(@NotNull Player player) {
        player.sendTitle(this);
    }

    /**
     * Sends the title directly to the defined players
     *
     * @param players the receivers of the title
     */
    public void send(@NotNull Collection<? extends Player> players) {
        for (Player player : players) {
            player.sendTitle(this);
        }
    }

    /**
     * Sends the title directly to the defined players
     *
     * @param players the receivers of the title
     */
    public void send(@NotNull Player[] players) {
        for (Player player : players) {
            player.sendTitle(this);
        }
    }

    /**
     * Sends the title directly to all online players
     */
    public void broadcast() {
        send(Bukkit.getOnlinePlayers());
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for creating titles
     */
    public static final class Builder {

        private BaseComponent[] title;
        private BaseComponent[] subtitle;
        private int fadeIn = DEFAULT_FADE_IN;
        private int stay = DEFAULT_STAY;
        private int fadeOut = DEFAULT_FADE_OUT;

        /**
         * Sets the title to the given text.
         *
         * @param title the title text
         * @return this builder instance
         * @throws NullPointerException if the title is null
         */
        @NotNull
        public Builder title(@NotNull BaseComponent title) {
            return this.title(new BaseComponent[]{checkNotNull(title, "title")});
        }

        /**
         * Sets the title to the given text.
         *
         * @param title the title text
         * @return this builder instance
         * @throws NullPointerException if the title is null
         */
        @NotNull
        public Builder title(@NotNull BaseComponent[] title) {
            this.title = checkNotNull(title, "title");
            return this;
        }

        /**
         * Sets the title to the given text.
         *
         * <p>It is recommended to use the {@link BaseComponent} methods.</p>
         *
         * @param title the title text
         * @return this builder instance
         * @throws NullPointerException if the title is null
         */
        @NotNull
        public Builder title(@NotNull String title) {
            return this.title(TextComponent.fromLegacyText(checkNotNull(title, "title")));
        }

        /**
         * Sets the subtitle to the given text.
         *
         * @param subtitle the title text
         * @return this builder instance
         */
        @NotNull
        public Builder subtitle(@Nullable BaseComponent subtitle) {
            return this.subtitle(subtitle == null ? null : new BaseComponent[]{subtitle});
        }

        /**
         * Sets the subtitle to the given text.
         *
         * @param subtitle the title text
         * @return this builder instance
         */
        @NotNull
        public Builder subtitle(@Nullable BaseComponent[] subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        /**
         * Sets the subtitle to the given text.
         *
         * <p>It is recommended to use the {@link BaseComponent} methods.</p>
         *
         * @param subtitle the title text
         * @return this builder instance
         */
        @NotNull
        public Builder subtitle(@Nullable String subtitle) {
            return this.subtitle(subtitle == null ? null : TextComponent.fromLegacyText(subtitle));
        }

        /**
         * Sets the number of ticks for the title to fade in
         *
         * @param fadeIn the number of ticks to fade in
         * @return this builder instance
         * @throws IllegalArgumentException if it is negative
         */
        @NotNull
        public Builder fadeIn(int fadeIn) {
            checkArgument(fadeIn >= 0, "Negative fadeIn: %s", fadeIn);
            this.fadeIn = fadeIn;
            return this;
        }


        /**
         * Sets the number of ticks for the title to stay.
         *
         * @param stay the number of ticks to stay
         * @return this builder instance
         * @throws IllegalArgumentException if it is negative
         */
        @NotNull
        public Builder stay(int stay) {
            checkArgument(stay >= 0, "Negative stay: %s", stay);
            this.stay = stay;
            return this;
        }

        /**
         * Sets the number of ticks for the title to fade out.
         *
         * @param fadeOut the number of ticks to fade out
         * @return this builder instance
         * @throws IllegalArgumentException if it is negative
         */
        @NotNull
        public Builder fadeOut(int fadeOut) {
            checkArgument(fadeOut >= 0, "Negative fadeOut: %s", fadeOut);
            this.fadeOut = fadeOut;
            return this;
        }

        /**
         * Create a title based on the values in the builder.
         *
         * @return a title from the values in this builder
         * @throws IllegalStateException if title isn't specified
         */
        @NotNull
        public Title build() {
            checkState(title != null, "Title not specified");
            return new Title(this.title, this.subtitle, this.fadeIn, this.stay, this.fadeOut);
        }
    }
}
