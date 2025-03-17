package io.papermc.paper.event.connection.configuration;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.Objects;

/**
 * Called when the player has successfully exited the configuration stage and is about to enter the
 * GAME protocol stage.
 */
@NullMarked
public class PostPlayerConnectionConfigurateEvent extends PlayerConfigurationConnectionEvent {
    private static final HandlerList handlers = new HandlerList();
    @Nullable
    private Component kickMessage;

    @ApiStatus.Internal
    public PostPlayerConnectionConfigurateEvent(PlayerConfigurationConnection connection) {
        super(false, connection);
    }

    /**
     * Rejects the player from entering the next stage.
     *
     * @param component reason
     */
    public void deny(Component component) {
        this.kickMessage = component;
    }

    /**
     * Allows the player to enter the next stage.
     */
    public void allow() {
        this.kickMessage = null;
    }

    /**
     * Gets if the player is allowed to enter the next stage.
     *
     * @return if allowed
     */
    public boolean isAllowed() {
        return this.kickMessage == null;
    }

    /**
     * Gets the kick message for this event, may be empty.
     * @return kick message
     */
    public Component getKickMessage() {
        return Objects.requireNonNullElse(this.kickMessage, Component.empty());
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
