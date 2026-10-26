package io.papermc.paper.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PlayerArmSwingEvent extends PlayerAnimationEvent {

    private final EquipmentSlot equipmentSlot;

    @ApiStatus.Internal
    public PlayerArmSwingEvent(final Player player, final EquipmentSlot equipmentSlot) {
        super(player, equipmentSlot == EquipmentSlot.HAND ? PlayerAnimationType.ARM_SWING : PlayerAnimationType.OFF_ARM_SWING);
        this.equipmentSlot = equipmentSlot;
    }

    /**
     * Returns the hand of the arm swing.
     *
     * @return the hand
     */
    public EquipmentSlot getHand() {
        return this.equipmentSlot;
    }
}
