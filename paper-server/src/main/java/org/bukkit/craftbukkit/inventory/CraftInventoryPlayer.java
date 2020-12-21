package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.PacketPlayOutHeldItemSlot;
import net.minecraft.server.PacketPlayOutSetSlot;
import net.minecraft.server.PlayerInventory;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryPlayer extends CraftInventory implements org.bukkit.inventory.PlayerInventory, EntityEquipment {
    public CraftInventoryPlayer(net.minecraft.server.PlayerInventory inventory) {
        super(inventory);
    }

    @Override
    public PlayerInventory getInventory() {
        return (PlayerInventory) inventory;
    }

    @Override
    public ItemStack[] getStorageContents() {
        return asCraftMirror(getInventory().items);
    }

    @Override
    public ItemStack getItemInMainHand() {
        return CraftItemStack.asCraftMirror(getInventory().getItemInHand());
    }

    @Override
    public void setItemInMainHand(ItemStack item) {
        setItem(getHeldItemSlot(), item);
    }

    @Override
    public void setItemInMainHand(ItemStack item, boolean silent) {
        setItemInMainHand(item); // Silence doesn't apply to players
    }

    @Override
    public ItemStack getItemInOffHand() {
        return CraftItemStack.asCraftMirror(getInventory().extraSlots.get(0));
    }

    @Override
    public void setItemInOffHand(ItemStack item) {
        ItemStack[] extra = getExtraContents();
        extra[0] = item;
        setExtraContents(extra);
    }

    @Override
    public void setItemInOffHand(ItemStack item, boolean silent) {
        setItemInOffHand(item); // Silence doesn't apply to players
    }

    @Override
    public ItemStack getItemInHand() {
        return getItemInMainHand();
    }

    @Override
    public void setItemInHand(ItemStack stack) {
        setItemInMainHand(stack);
    }

    @Override
    public void setItem(int index, ItemStack item) {
        super.setItem(index, item);
        if (this.getHolder() == null) return;
        EntityPlayer player = ((CraftPlayer) this.getHolder()).getHandle();
        if (player.playerConnection == null) return;
        // PacketPlayOutSetSlot places the items differently than setItem()
        //
        // Between, and including, index 9 (the first index outside of the hotbar) and index 35 (the last index before
        // armor slots) both PacketPlayOutSetSlot and setItem() places the items in the player's inventory the same way.
        // Index 9 starts at the upper left corner of the inventory and moves to the right as it increases. When it
        // reaches the end of the line it goes back to the left side of the new line in the inventory. Basically, it
        // follows the path your eyes would follow as you read a book.
        //
        // The player's hotbar is indexed 0-8 in setItem(). The order goes: 0-8 hotbar, 9-35 normal inventory, 36 boots,
        // 37 leggings, 38 chestplate, and 39 helmet. For indexes > 39 an ArrayIndexOutOfBoundsException will be thrown.
        //
        // PacketPlayOutSetSlot works very differently. Slots 0-8 are as follows: 0 crafting output, 1-4 crafting input,
        // 5 helmet, 6 chestplate, 7 leggings, and 8 boots. Then, 9-35 work exactly the same as setItem(). The hotbar
        // for PacketPlayOutSetSlot starts at index 36, and continues to index 44. Items placed where index is < 0 or
        // > 44 have no action. Basically, the upper part of the player's inventory (crafting area and armor slots) is
        // the first "row" of 9 slots for PacketPlayOutSetSlot. From there the rows work as normal, from left to right
        // all the way down, including the hotbar.
        //
        // With this in mind, we have to modify the index we give PacketPlayOutSetSlot to match the index we intended
        // with setItem(). First, if the index is 0-8, we need to add 36, or 4 rows worth of slots, to the index. This
        // will push the item down to the correct spot in the hotbar.
        //
        // Now when index is > 35 (if index > 39 an ArrayIndexOutOfBoundsException will be thrown, so we need not worry
        // about it) then we need to reset the index, and then count backwards  from the "top" of the inventory. That is
        // to say, we first find (index - 36), which will give us the index required for the armor slots. Now, we need
        // to reverse the order of the index from 8. That means we need 0 to correspond to 8, 1 to correspond to 7,
        // 2 to correspond to 6, and 3 to correspond to 5. We do this simply by taking the result of (index - 36) and
        // subtracting that value from 8.
        if (index < PlayerInventory.getHotbarSize()) {
            index += 36;
        } else if (index > 39) {
            index += 5; // Off hand
        } else if (index > 35) {
            index = 8 - (index - 36);
        }
        player.playerConnection.sendPacket(new PacketPlayOutSetSlot(player.defaultContainer.windowId, index, CraftItemStack.asNMSCopy(item)));
    }

    @Override
    public void setItem(EquipmentSlot slot, ItemStack item) {
        Preconditions.checkArgument(slot != null, "slot must not be null");

        switch (slot) {
            case HAND:
                this.setItemInMainHand(item);
                break;
            case OFF_HAND:
                this.setItemInOffHand(item);
                break;
            case FEET:
                this.setBoots(item);
                break;
            case LEGS:
                this.setLeggings(item);
                break;
            case CHEST:
                this.setChestplate(item);
                break;
            case HEAD:
                this.setHelmet(item);
                break;
            default:
                throw new IllegalArgumentException("Not implemented. This is a bug");
        }
    }

    @Override
    public void setItem(EquipmentSlot slot, ItemStack item, boolean silent) {
        setItem(slot, item); // Silence doesn't apply to players
    }

    @Override
    public ItemStack getItem(EquipmentSlot slot) {
        Preconditions.checkArgument(slot != null, "slot must not be null");

        switch (slot) {
            case HAND:
                return getItemInMainHand();
            case OFF_HAND:
                return getItemInOffHand();
            case FEET:
                return getBoots();
            case LEGS:
                return getLeggings();
            case CHEST:
                return getChestplate();
            case HEAD:
                return getHelmet();
            default:
                throw new IllegalArgumentException("Not implemented. This is a bug");
        }
    }

    @Override
    public int getHeldItemSlot() {
        return getInventory().itemInHandIndex;
    }

    @Override
    public void setHeldItemSlot(int slot) {
        Validate.isTrue(slot >= 0 && slot < PlayerInventory.getHotbarSize(), "Slot is not between 0 and 8 inclusive");
        this.getInventory().itemInHandIndex = slot;
        ((CraftPlayer) this.getHolder()).getHandle().playerConnection.sendPacket(new PacketPlayOutHeldItemSlot(slot));
    }

    @Override
    public ItemStack getHelmet() {
        return getItem(getSize() - 2);
    }

    @Override
    public ItemStack getChestplate() {
        return getItem(getSize() - 3);
    }

    @Override
    public ItemStack getLeggings() {
        return getItem(getSize() - 4);
    }

    @Override
    public ItemStack getBoots() {
        return getItem(getSize() - 5);
    }

    @Override
    public void setHelmet(ItemStack helmet) {
        setItem(getSize() - 2, helmet);
    }

    @Override
    public void setHelmet(ItemStack helmet, boolean silent) {
        setHelmet(helmet); // Silence doesn't apply to players
    }

    @Override
    public void setChestplate(ItemStack chestplate) {
        setItem(getSize() - 3, chestplate);
    }

    @Override
    public void setChestplate(ItemStack chestplate, boolean silent) {
        setChestplate(chestplate); // Silence doesn't apply to players
    }

    @Override
    public void setLeggings(ItemStack leggings) {
        setItem(getSize() - 4, leggings);
    }

    @Override
    public void setLeggings(ItemStack leggings, boolean silent) {
        setLeggings(leggings); // Silence doesn't apply to players
    }

    @Override
    public void setBoots(ItemStack boots) {
        setItem(getSize() - 5, boots);
    }

    @Override
    public void setBoots(ItemStack boots, boolean silent) {
        setBoots(boots); // Silence doesn't apply to players
    }

    @Override
    public ItemStack[] getArmorContents() {
        return asCraftMirror(getInventory().armor);
    }

    private void setSlots(ItemStack[] items, int baseSlot, int length) {
        if (items == null) {
            items = new ItemStack[length];
        }
        Preconditions.checkArgument(items.length <= length, "items.length must be < %s", length);

        for (int i = 0; i < length; i++) {
            if (i >= items.length) {
                setItem(baseSlot + i, null);
            } else {
                setItem(baseSlot + i, items[i]);
            }
        }
    }

    @Override
    public void setStorageContents(ItemStack[] items) throws IllegalArgumentException {
        setSlots(items, 0, getInventory().items.size());
    }

    @Override
    public void setArmorContents(ItemStack[] items) {
        setSlots(items, getInventory().items.size(), getInventory().armor.size());
    }

    @Override
    public ItemStack[] getExtraContents() {
        return asCraftMirror(getInventory().extraSlots);
    }

    @Override
    public void setExtraContents(ItemStack[] items) {
        setSlots(items, getInventory().items.size() + getInventory().armor.size(), getInventory().extraSlots.size());
    }

    @Override
    public HumanEntity getHolder() {
        return (HumanEntity) inventory.getOwner();
    }

    @Override
    public float getItemInHandDropChance() {
        return getItemInMainHandDropChance();
    }

    @Override
    public void setItemInHandDropChance(float chance) {
        setItemInMainHandDropChance(chance);
    }

    @Override
    public float getItemInMainHandDropChance() {
        return 1;
    }

    @Override
    public void setItemInMainHandDropChance(float chance) {
        throw new UnsupportedOperationException("Cannot set drop chance for PlayerInventory");
    }

    @Override
    public float getItemInOffHandDropChance() {
        return 1;
    }

    @Override
    public void setItemInOffHandDropChance(float chance) {
        throw new UnsupportedOperationException("Cannot set drop chance for PlayerInventory");
    }

    @Override
    public float getHelmetDropChance() {
        return 1;
    }

    @Override
    public void setHelmetDropChance(float chance) {
        throw new UnsupportedOperationException("Cannot set drop chance for PlayerInventory");
    }

    @Override
    public float getChestplateDropChance() {
        return 1;
    }

    @Override
    public void setChestplateDropChance(float chance) {
        throw new UnsupportedOperationException("Cannot set drop chance for PlayerInventory");
    }

    @Override
    public float getLeggingsDropChance() {
        return 1;
    }

    @Override
    public void setLeggingsDropChance(float chance) {
        throw new UnsupportedOperationException("Cannot set drop chance for PlayerInventory");
    }

    @Override
    public float getBootsDropChance() {
        return 1;
    }

    @Override
    public void setBootsDropChance(float chance) {
        throw new UnsupportedOperationException("Cannot set drop chance for PlayerInventory");
    }
}
