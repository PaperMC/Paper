package org.bukkit.event.entity;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Thrown whenever a {@link Player} dies
 */
public class PlayerDeathEvent extends EntityDeathEvent {
    private int newExp = 0;
    private String deathMessage = "";

    public PlayerDeathEvent(Player player, List<ItemStack> drops, int droppedExp, int newExp, String deathMessage) {
        super(player, drops, droppedExp);
        this.newExp = newExp;
        this.deathMessage = deathMessage;
    }

    /**
     * Set the death message that will appear to everyone on the server.
     *
     * @param deathMessage Message to appear to other players on the server.
     */
    public void setDeathMessage(String deathMessage) {
        this.deathMessage = deathMessage;
    }

    /**
     * Get the death message that will appear to everyone on the server.
     *
     * @return Message to appear to other players on the server.
     */
    public String getDeathMessage() {
        return this.deathMessage;
    }

    /**
     * Gets how much EXP the Player should have at respawn.
     * <p>
     * This does not indicate how much EXP should be dropped, please see
     * {@link #getDroppedExp()} for that.
     *
     * @return New EXP of the respawned player
     */
    public int getNewExp() {
        return newExp;
    }

    /**
     * Sets how much EXP the Player should have at respawn.
     * <p>
     * This does not indicate how much EXP should be dropped, please see
     * {@link #setDroppedExp(int)} for that.
     * 
     * @get exp New EXP of the respawned player
     */
    public void setNewExp(int exp) {
        this.newExp = exp;
    }
}
