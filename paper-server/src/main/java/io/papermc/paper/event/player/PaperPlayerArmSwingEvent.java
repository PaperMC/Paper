package io.papermc.paper.event.player;

import org.bukkit.craftbukkit.event.player.CraftPlayerAnimationEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.inventory.EquipmentSlot;

public class PaperPlayerArmSwingEvent extends CraftPlayerAnimationEvent implements PlayerArmSwingEvent {

    private final EquipmentSlot slot;

    public PaperPlayerArmSwingEvent(final Player player, final EquipmentSlot slot) {
        super(player, slot == EquipmentSlot.HAND ? PlayerAnimationType.ARM_SWING : PlayerAnimationType.OFF_ARM_SWING);
        this.slot = slot;
    }

    @Override
    public EquipmentSlot getHand() {
        return this.slot;
    }
}
