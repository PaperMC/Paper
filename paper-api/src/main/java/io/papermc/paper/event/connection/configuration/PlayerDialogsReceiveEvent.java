package io.papermc.paper.event.connection.configuration;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import java.util.Optional;
import net.kyori.adventure.dialog.DialogLike;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

/**
 * This Event allows for setting a custom {@code quick_action} and {@code pause_menu_additions}
 * {@link io.papermc.paper.dialog.Dialog} for each player.
 * <p>
 * This is executed once, when the player joins.
 */
public class PlayerDialogsReceiveEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final PlayerConfigurationConnection connection;
    private @Nullable DialogLike quickAction;
    private @Nullable DialogLike pauseMenuAdditions;

    @ApiStatus.Internal
    public PlayerDialogsReceiveEvent(final PlayerConfigurationConnection connection) {
        super(!Bukkit.isPrimaryThread());
        this.connection = connection;
    }

    /**
     * Gets the players Connection who will receive these Dialogs
     *
     * @return The ConfigurationConnection
     */
    public PlayerConfigurationConnection getConnection() {
        return this.connection;
    }

    /**
     * Sets the Quick Action Dialog, standard button to open this is G
     *
     * @param quickAction The dialog, or null to unset it
     */
    public void setQuickAction(DialogLike quickAction) {
        this.quickAction = quickAction;
    }


    /**
     * Sets the Pause Menu Additions Dialog, which is viewable in the Esc menu
     *
     * @param pauseMenuAdditions The dialog, or null to unset it
     */
    public void setPauseMenuAdditions(DialogLike pauseMenuAdditions) {
        this.pauseMenuAdditions = pauseMenuAdditions;
    }

    /**
     * Gets the currently set Quick Actions Dialog
     *
     * @return the Dialog, or Optional.empty() if its not set
     */
    public Optional<DialogLike> getQuickAction() {
        return Optional.ofNullable(quickAction);
    }


    /**
     * Gets the currently set Pause Menu Additions Dialog
     *
     * @return the Dialog, or Optional.empty() if its not set
     */
    public Optional<DialogLike> getPauseMenuAdditions() {
        return Optional.ofNullable(pauseMenuAdditions);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
