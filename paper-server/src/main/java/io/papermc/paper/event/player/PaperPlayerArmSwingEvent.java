package io.papermc.paper.event.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
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

    public PaperPlayerArmSwingEvent(final ServerPlayer player, final InteractionHand hand) {
        this(player.getBukkitEntity(), CraftEquipmentSlot.getHand(hand));
    }

    @Override
    public EquipmentSlot getHand() {
        return this.slot;
    }
}
