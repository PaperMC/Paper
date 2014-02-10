package org.bukkit.craftbukkit.inventory;

import org.bukkit.GameMode;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.Container;

public class CraftInventoryView extends InventoryView {
    private final Container container;
    private final CraftHumanEntity player;
    private final CraftInventory viewing;

    public CraftInventoryView(HumanEntity player, Inventory viewing, Container container) {
        // TODO: Should we make sure it really IS a CraftHumanEntity first? And a CraftInventory?
        this.player = (CraftHumanEntity) player;
        this.viewing = (CraftInventory) viewing;
        this.container = container;
    }

    @Override
    public Inventory getTopInventory() {
        return viewing;
    }

    @Override
    public Inventory getBottomInventory() {
        return player.getInventory();
    }

    @Override
    public HumanEntity getPlayer() {
        return player;
    }

    @Override
    public InventoryType getType() {
        InventoryType type = viewing.getType();
        if (type == InventoryType.CRAFTING && player.getGameMode() == GameMode.CREATIVE) {
            return InventoryType.CREATIVE;
        }
        return type;
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        net.minecraft.server.ItemStack stack = CraftItemStack.asNMSCopy(item);
        if (slot != -999) {
            container.getSlot(slot).set(stack);
        } else {
            player.getHandle().drop(stack, false);
        }
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot == -999) {
            return null;
        }
        return CraftItemStack.asCraftMirror(container.getSlot(slot).getItem());
    }

    public boolean isInTop(int rawSlot) {
        return rawSlot < viewing.getSize();
    }

    public Container getHandle() {
        return container;
    }

    public static SlotType getSlotType(InventoryView inventory, int slot) {
        SlotType type = SlotType.CONTAINER;
        if (slot >= 0 && slot < inventory.getTopInventory().getSize()) {
            switch(inventory.getType()) {
            case FURNACE:
                if (slot == 2) {
                    type = SlotType.RESULT;
                } else if(slot == 1) {
                    type = SlotType.FUEL;
                } else {
                    type = SlotType.CRAFTING;
                }
                break;
            case BREWING:
                if (slot == 3) {
                    type = SlotType.FUEL;
                } else {
                    type = SlotType.CRAFTING;
                }
                break;
            case ENCHANTING:
                type = SlotType.CRAFTING;
                break;
            case WORKBENCH:
            case CRAFTING:
                if (slot == 0) {
                    type = SlotType.RESULT;
                } else {
                    type = SlotType.CRAFTING;
                }
                break;
            case MERCHANT:
                if (slot == 2) {
                    type = SlotType.RESULT;
                } else {
                    type = SlotType.CRAFTING;
                }
                break;
            case BEACON:
                type = SlotType.CRAFTING;
                break;
            case ANVIL:
                if (slot == 2) {
                    type = SlotType.RESULT;
                } else {
                    type = SlotType.CRAFTING;
                }
                break;
            default:
                // Nothing to do, it's a CONTAINER slot
            }
        } else {
            if (slot == -999) {
                type = SlotType.OUTSIDE;
            } else if (inventory.getType() == InventoryType.CRAFTING) {
                if (slot < 9) {
                    type = SlotType.ARMOR;
                } else if (slot > 35) {
                    type = SlotType.QUICKBAR;
                }
            } else if (slot >= (inventory.countSlots() - 9)) {
                type = SlotType.QUICKBAR;
            }
        }
        return type;
    }
}
