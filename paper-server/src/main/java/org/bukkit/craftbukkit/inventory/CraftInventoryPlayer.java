package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.List;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerInventoryPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryPlayer extends CraftInventory implements org.bukkit.inventory.PlayerInventory, EntityEquipment {
    public CraftInventoryPlayer(net.minecraft.world.entity.player.Inventory inventory) {
        super(inventory);
    }

    @Override
    public Inventory getInventory() {
        return (Inventory) this.inventory;
    }

    @Override
    public ItemStack[] getStorageContents() {
        return this.asCraftMirror(this.getInventory().getNonEquipmentItems());
    }

    @Override
    public ItemStack getItemInMainHand() {
        return CraftItemStack.asCraftMirror(this.getInventory().getSelectedItem());
    }

    @Override
    public void setItemInMainHand(ItemStack item) {
        this.setItem(this.getHeldItemSlot(), item);
    }

    @Override
    public void setItemInMainHand(ItemStack item, boolean silent) {
        this.setItemInMainHand(item); // Silence doesn't apply to players
    }

    @Override
    public ItemStack getItemInOffHand() {
        return CraftItemStack.asCraftMirror(this.getInventory().equipment.get(net.minecraft.world.entity.EquipmentSlot.OFFHAND));
    }

    @Override
    public void setItemInOffHand(ItemStack item) {
        this.getInventory().equipment.set(net.minecraft.world.entity.EquipmentSlot.OFFHAND, CraftItemStack.asNMSCopy(item));
    }

    @Override
    public void setItemInOffHand(ItemStack item, boolean silent) {
        this.setItemInOffHand(item); // Silence doesn't apply to players
    }

    @Override
    public ItemStack getItemInHand() {
        return this.getItemInMainHand();
    }

    @Override
    public void setItemInHand(ItemStack stack) {
        this.setItemInMainHand(stack);
    }

    @Override
    public void setItem(int index, ItemStack item) {
        // Paper start - Validate setItem index
        if (index < 0 || index > 42) {
            throw new ArrayIndexOutOfBoundsException("Index must be between 0 and 42");
        }
        // Paper end - Validate setItem index
        super.setItem(index, item);
        if (this.getHolder() == null) return;

        ServerPlayer player = ((CraftPlayer) this.getHolder()).getHandle();
        if (player.connection == null) return;
        // Of course, these are not part of the player inventory "menu" because these slots are not accessible.
        // However they are technically part of the player inventory.
        // This is a poor representation by this API, but basically instead send a player inventory update packet.
        // This will allow updates to the player inventory rather than through the menu.
        // TODO: This could be something worth cleaning up in the future.
        if (index > 40) {
            player.connection.send(new ClientboundSetPlayerInventoryPacket(index, CraftItemStack.asNMSCopy(item)));
            return;
        }

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
        if (index < Inventory.getSelectionSize()) {
            index += 36;
        } else if (index > 39) {
            index += 5; // Off hand
        } else if (index > 35) {
            index = 8 - (index - 36);
        }
        player.connection.send(new ClientboundContainerSetSlotPacket(player.inventoryMenu.containerId, player.inventoryMenu.incrementStateId(), index, CraftItemStack.asNMSCopy(item)));
    }

    @Override
    public void setItem(EquipmentSlot slot, ItemStack item) {
        Preconditions.checkArgument(slot != null, "slot must not be null");

        switch (slot) {
            case HAND -> this.setItemInMainHand(item);
            case OFF_HAND, FEET, LEGS, CHEST, HEAD, BODY, SADDLE ->
                this.getInventory().equipment.set(CraftEquipmentSlot.getNMS(slot), CraftItemStack.asNMSCopy(item));
        }
    }

    @Override
    public void setItem(EquipmentSlot slot, ItemStack item, boolean silent) {
        this.setItem(slot, item); // Silence doesn't apply to players
    }

    @Override
    public ItemStack getItem(EquipmentSlot slot) {
        Preconditions.checkArgument(slot != null, "slot must not be null");

        return switch (slot) {
            case HAND -> this.getItemInMainHand();
            case OFF_HAND, FEET, LEGS, CHEST, HEAD, BODY, SADDLE -> CraftItemStack.asCraftMirror(this.getInventory().equipment.get(CraftEquipmentSlot.getNMS(slot)));
        };
    }

    @Override
    public int getHeldItemSlot() {
        return this.getInventory().getSelectedSlot();
    }

    @Override
    public void setHeldItemSlot(int slot) {
        Preconditions.checkArgument(slot >= 0 && slot < Inventory.getSelectionSize(), "Slot (%s) is not between 0 and %s inclusive", slot, Inventory.getSelectionSize() - 1);
        this.getInventory().setSelectedSlot(slot);
        ((CraftPlayer) this.getHolder()).getHandle().connection.send(new ClientboundSetHeldSlotPacket(slot));
    }

    @Override
    public ItemStack getHelmet() {
        return this.getItem(EquipmentSlot.HEAD);
    }

    @Override
    public ItemStack getChestplate() {
        return this.getItem(EquipmentSlot.CHEST);
    }

    @Override
    public ItemStack getLeggings() {
        return this.getItem(EquipmentSlot.LEGS);
    }

    @Override
    public ItemStack getBoots() {
        return this.getItem(EquipmentSlot.FEET);
    }

    @Override
    public void setHelmet(ItemStack helmet) {
        this.setItem(EquipmentSlot.HEAD, helmet);
    }

    @Override
    public void setHelmet(ItemStack helmet, boolean silent) {
        this.setHelmet(helmet); // Silence doesn't apply to players
    }

    @Override
    public void setChestplate(ItemStack chestplate) {
        this.setItem(EquipmentSlot.CHEST, chestplate);
    }

    @Override
    public void setChestplate(ItemStack chestplate, boolean silent) {
        this.setChestplate(chestplate); // Silence doesn't apply to players
    }

    @Override
    public void setLeggings(ItemStack leggings) {
        this.setItem(EquipmentSlot.LEGS, leggings);
    }

    @Override
    public void setLeggings(ItemStack leggings, boolean silent) {
        this.setLeggings(leggings); // Silence doesn't apply to players
    }

    @Override
    public void setBoots(ItemStack boots) {
        this.setItem(EquipmentSlot.FEET, boots);
    }

    @Override
    public void setBoots(ItemStack boots, boolean silent) {
        this.setBoots(boots); // Silence doesn't apply to players
    }

    @Override
    public ItemStack[] getArmorContents() {
        return this.asCraftMirror(this.getInventory().getArmorContents());
    }

    private void setSlots(ItemStack[] items, int baseSlot, int length) {
        if (items == null) {
            items = new ItemStack[length];
        }
        Preconditions.checkArgument(items.length <= length, "items.length must be <= %s", length);

        for (int i = 0; i < length; i++) {
            if (i >= items.length) {
                this.setItem(baseSlot + i, null);
            } else {
                this.setItem(baseSlot + i, items[i]);
            }
        }
    }

    @Override
    public void setStorageContents(ItemStack[] items) throws IllegalArgumentException {
        this.setSlots(items, 0, this.getInventory().getNonEquipmentItems().size());
    }

    @Override
    public void setArmorContents(ItemStack[] items) {
        this.setSlots(items, this.getInventory().getNonEquipmentItems().size(), this.getInventory().getArmorContents().size());
    }

    @Override
    public ItemStack[] getExtraContents() {
        return this.asCraftMirror(this.getInventory().getExtraContent());
    }

    @Override
    public void setExtraContents(ItemStack[] items) {
        this.setSlots(items, this.getInventory().getNonEquipmentItems().size() + this.getInventory().getArmorContents().size(), 3);
    }

    @Override
    public HumanEntity getHolder() {
        return (HumanEntity) this.inventory.getOwner();
    }

    @Override
    public float getItemInHandDropChance() {
        return this.getItemInMainHandDropChance();
    }

    @Override
    public void setItemInHandDropChance(float chance) {
        this.setItemInMainHandDropChance(chance);
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
    // Paper start
    @Override
    public float getDropChance(EquipmentSlot slot) {
        return 1;
    }

    @Override
    public void setDropChance(EquipmentSlot slot, float chance) {
        throw new UnsupportedOperationException("Cannot set drop chance for PlayerInventory");
    }
    // Paper end
}
