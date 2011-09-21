package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Thrown whenever a {@link Player} dies
 */
public class PlayerDeathEvent extends EntityDeathEvent {
    private int newExp = 0;

    public PlayerDeathEvent(Player player, List<ItemStack> drops, int droppedExp, int newExp) {
        super(player, drops, droppedExp);
        this.newExp = newExp;
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
     * {@link #setDropedExp(int)} for that.
     * 
     * @get exp New EXP of the respawned player
     */
    public void setNewExp(int exp) {
        this.newExp = exp;
    }
}
