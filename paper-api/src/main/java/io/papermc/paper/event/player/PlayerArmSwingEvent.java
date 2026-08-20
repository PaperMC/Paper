package io.papermc.paper.event.player;

import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.inventory.EquipmentSlot;

// todo javadocs?
public interface PlayerArmSwingEvent extends PlayerAnimationEvent { // todo redundant with parent?

    /**
     * Returns the hand of the arm swing.
     *
     * @return the hand
     */
    EquipmentSlot getHand();
}
