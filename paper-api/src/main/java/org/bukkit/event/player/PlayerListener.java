
package org.bukkit.event.player;

import org.bukkit.event.Listener;

/**
 * Handles all events thrown in relation to a Player
 */
public class PlayerListener implements Listener {
    public PlayerListener() {
    }

    /**
     * Called when a player joins a server
     *
     * @param event Relevant event details
     */
    public void onPlayerJoin(PlayerJoinEvent event) {
    }

    /**
     * Called when a player leaves a server
     *
     * @param event Relevant event details
     */
    public void onPlayerQuit(PlayerQuitEvent event) {
    }

    /**
     * Called when a player gets kicked from the server
     * 
     * @param event Relevant event details
     */
    public void onPlayerKick(PlayerKickEvent event) {
    }

    /**
     * Called when a player sends a chat message
     *
     * @param event Relevant event details
     */
    public void onPlayerChat(PlayerChatEvent event) {
    }

    /**
     * Called early in the command handling process. This event is only
     * for very exceptional cases and you should not normally use it.
     *
     * @param event Relevant event details
     */
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
    }

    /**
     * Called when a player attempts to move location in a world
     *
     * @param event Relevant event details
     */
    public void onPlayerMove(PlayerMoveEvent event) {
    }

    /**
     * Called when a player attempts to teleport to a new location in a world
     *
     * @param event Relevant event details
     */
    public void onPlayerTeleport(PlayerTeleportEvent event) {
    }

    /**
     * Called when a player respawns
     * 
     * @param event Relevant event details
     */
    public void onPlayerRespawn(PlayerRespawnEvent event) {
    }

    /**
     * Called when a player interacts
     *
     * @param event Relevant event details
     */
    public void onPlayerInteract(PlayerInteractEvent event) {
    }

    /**
     * Called when a player attempts to log in to the server
     *
     * @param event Relevant event details
     */
    public void onPlayerLogin(PlayerLoginEvent event) {
    }

    /**
     * Called when a player throws an egg and it might hatch
     *
     * @param event Relevant event details
     */
    public void onPlayerEggThrow(PlayerEggThrowEvent event) {
    }

    /**
     * Called when a player plays an animation, such as an arm swing
     *
     * @param event Relevant event details
     */
    public void onPlayerAnimation(PlayerAnimationEvent event) {
    }

    /**
     * Called when a player opens an inventory
     *
     * @param event Relevant event details
     */
    public void onInventoryOpen(PlayerInventoryEvent event) {
    }

    /**
     * Called when a player changes their held item
     *
     * @param event Relevant event details
     */
    public void onItemHeldChange(PlayerItemHeldEvent event) {
    }

    /**
     * Called when a player drops an item from their inventory
     *
     * @param event Relevant event details
     */
    public void onPlayerDropItem(PlayerDropItemEvent event) {
    }

    /**
     * Called when a player picks an item up off the ground
     *
     * @param event Relevant event details
     */
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
    }

    /**
     * Called when a player toggles sneak mode
     *
     * @param event Relevant event details
     */
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
    }

    /**
     * Called when a player fills a bucket
     * 
     * @param event Relevant event details
     */
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
    }

    /**
     * Called when a player empties a bucket
     * 
     * @param event Relevant event details
     */
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
    }
}
