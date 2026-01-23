package io.papermc.paper.event.player;

import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.NullMarked;

// todo javadocs?
@NullMarked
public interface PlayerArmSwingEvent extends PlayerAnimationEvent {

    /**
     * Returns the hand of the arm swing.
     *
     * @return the hand
     */
    EquipmentSlot getHand();
}
