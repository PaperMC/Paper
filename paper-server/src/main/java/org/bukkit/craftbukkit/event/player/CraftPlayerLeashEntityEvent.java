package org.bukkit.craftbukkit.event.player;

import net.minecraft.world.InteractionHand;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class CraftPlayerLeashEntityEvent extends CraftPlayerEvent implements PlayerLeashEntityEvent {

    private final Entity leashHolder;
    private final Entity entity;
    private final EquipmentSlot hand;

    private boolean cancelled;

    public CraftPlayerLeashEntityEvent(final Entity entity, final Entity leashHolder, final Player leasher, final EquipmentSlot hand) {
        super(leasher);
        this.leashHolder = leashHolder;
        this.entity = entity;
        this.hand = hand;
    }

    public CraftPlayerLeashEntityEvent(
        final net.minecraft.world.entity.Entity entity,
        final net.minecraft.world.entity.Entity leashHolder,
        final net.minecraft.world.entity.player.Player leasher,
        final InteractionHand hand
    ) {
        this(entity.getBukkitEntity(), leashHolder.getBukkitEntity(), (Player) leasher.getBukkitEntity(), CraftEquipmentSlot.getHand(hand));
    }

    @Override
    public Entity getLeashHolder() {
        return this.leashHolder;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerLeashEntityEvent.getHandlerList();
    }
}
