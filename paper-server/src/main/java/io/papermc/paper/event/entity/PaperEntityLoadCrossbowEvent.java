package io.papermc.paper.event.entity;

import net.minecraft.world.InteractionHand;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PaperEntityLoadCrossbowEvent extends CraftEntityEvent implements EntityLoadCrossbowEvent {

    private final ItemStack crossbow;
    private final EquipmentSlot hand;

    private boolean consumeItem = true;
    private boolean cancelled;

    public PaperEntityLoadCrossbowEvent(final LivingEntity entity, final ItemStack crossbow, final EquipmentSlot hand) {
        super(entity);
        this.crossbow = crossbow;
        this.hand = hand;
    }

    public PaperEntityLoadCrossbowEvent(
        final net.minecraft.world.entity.LivingEntity entity, final net.minecraft.world.item.ItemStack crossbow, final InteractionHand hand
    ) {
        this(entity.getBukkitEntity(), CraftItemStack.asCraftMirror(crossbow), CraftEquipmentSlot.getHand(hand));
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    @Override
    public ItemStack getCrossbow() {
        return this.crossbow;
    }

    @Override
    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public boolean shouldConsumeItem() {
        return this.consumeItem;
    }

    @Override
    public void setConsumeItem(final boolean consume) {
        this.consumeItem = consume;
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
        return EntityLoadCrossbowEvent.getHandlerList();
    }
}
