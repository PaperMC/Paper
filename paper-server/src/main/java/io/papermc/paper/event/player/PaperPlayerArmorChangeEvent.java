package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PaperPlayerArmorChangeEvent extends CraftPlayerEvent implements PlayerArmorChangeEvent {

    private final SlotType slotType;
    private final ItemStack oldItem;
    private final ItemStack newItem;

    public PaperPlayerArmorChangeEvent(final Player player, final SlotType slotType, final ItemStack oldItem, final ItemStack newItem) {
        super(player);
        this.slotType = slotType;
        this.oldItem = oldItem;
        this.newItem = newItem;
    }

    @Override
    public SlotType getSlotType() {
        return this.slotType;
    }

    @Override
    public EquipmentSlot getSlot() {
        return switch (this.slotType) {
            case HEAD -> EquipmentSlot.HEAD;
            case CHEST -> EquipmentSlot.CHEST;
            case LEGS -> EquipmentSlot.LEGS;
            case FEET -> EquipmentSlot.FEET;
        };
    }

    @Override
    public ItemStack getOldItem() {
        return this.oldItem;
    }

    @Override
    public ItemStack getNewItem() {
        return this.newItem;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerArmorChangeEvent.getHandlerList();
    }
}
