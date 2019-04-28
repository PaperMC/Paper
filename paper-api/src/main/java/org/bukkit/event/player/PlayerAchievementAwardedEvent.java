package org.bukkit.event.player;

import org.bukkit.Achievement;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player earns an achievement.
 * @deprecated future versions of Minecraft do not have achievements
 */
@Deprecated
public class PlayerAchievementAwardedEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Achievement achievement;
    private boolean isCancelled = false;

    public PlayerAchievementAwardedEvent(Player player, Achievement achievement) {
        super(player);
        this.achievement = achievement;
    }

    /**
     * Gets the Achievement being awarded.
     *
     * @return the achievement being awarded
     */
    public Achievement getAchievement() {
        return achievement;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
