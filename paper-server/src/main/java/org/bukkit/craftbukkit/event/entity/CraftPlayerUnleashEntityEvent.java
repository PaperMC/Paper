package org.bukkit.craftbukkit.event.entity;

import net.minecraft.world.InteractionHand;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class CraftPlayerUnleashEntityEvent extends CraftEntityUnleashEvent implements PlayerUnleashEntityEvent {

    private final Player player;
    private final EquipmentSlot hand;

    public CraftPlayerUnleashEntityEvent(final Entity entity, final Player player, final EquipmentSlot hand, final boolean dropLeash) {
        super(entity, UnleashReason.PLAYER_UNLEASH, dropLeash);
        this.player = player;
        this.hand = hand;
    }

    public CraftPlayerUnleashEntityEvent(
        final net.minecraft.world.entity.Entity entity,
        final net.minecraft.world.entity.player.Player player,
        final InteractionHand hand,
        final boolean dropLeash
    ) {
        this(
            entity.getBukkitEntity(),
            (Player) player.getBukkitEntity(),
            CraftEquipmentSlot.getHand(hand),
            dropLeash
        );
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public EquipmentSlot getHand() {
        return this.hand;
    }
}
