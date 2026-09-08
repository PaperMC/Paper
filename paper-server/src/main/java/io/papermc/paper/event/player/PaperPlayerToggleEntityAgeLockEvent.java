package io.papermc.paper.event.player;

import net.minecraft.world.InteractionHand;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PaperPlayerToggleEntityAgeLockEvent extends CraftPlayerEvent implements PlayerToggleEntityAgeLockEvent {

    private final LivingEntity entity;
    private final ItemStack item;
    private final EquipmentSlot hand;
    private final boolean ageLocked;
    private boolean cancelled;

    public PaperPlayerToggleEntityAgeLockEvent(final Player player, final LivingEntity entity, final ItemStack item, final EquipmentSlot hand, final boolean ageLocked) {
        super(player);
        this.entity = entity;
        this.item = item;
        this.hand = hand;
        this.ageLocked = ageLocked;
    }

    public PaperPlayerToggleEntityAgeLockEvent(
        final net.minecraft.world.entity.player.Player player,
        final net.minecraft.world.entity.LivingEntity entity,
        final net.minecraft.world.item.ItemStack item,
        final InteractionHand hand,
        final boolean ageLocked
    ) {
        this((Player) player.getBukkitEntity(), entity.getBukkitEntity(), CraftItemStack.asCraftMirror(item), CraftEquipmentSlot.getHand(hand), ageLocked);
    }

    @Override
    public LivingEntity getEntity() {
        return this.entity;
    }

    @Override
    public ItemStack getItem() {
        return this.item.clone();
    }

    @Override
    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public boolean isAgeLocked() {
        return this.ageLocked;
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
        return PlayerToggleEntityAgeLockEvent.getHandlerList();
    }
}
