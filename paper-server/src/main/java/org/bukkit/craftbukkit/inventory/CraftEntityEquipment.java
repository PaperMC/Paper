package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.EntityInsentient;

import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class CraftEntityEquipment implements EntityEquipment {
    private static final int WEAPON_SLOT = 0;
    private static final int HELMET_SLOT = 4;
    private static final int CHEST_SLOT = 3;
    private static final int LEG_SLOT = 2;
    private static final int BOOT_SLOT = 1;
    private static final int INVENTORY_SLOTS = 5;

    private final CraftLivingEntity entity;

    public CraftEntityEquipment(CraftLivingEntity entity) {
        this.entity = entity;
    }

    public ItemStack getItemInHand() {
        return getEquipment(WEAPON_SLOT);
    }

    public void setItemInHand(ItemStack stack) {
        setEquipment(WEAPON_SLOT, stack);
    }

    public ItemStack getHelmet() {
        return getEquipment(HELMET_SLOT);
    }

    public void setHelmet(ItemStack helmet) {
        setEquipment(HELMET_SLOT, helmet);
    }

    public ItemStack getChestplate() {
        return getEquipment(CHEST_SLOT);
    }

    public void setChestplate(ItemStack chestplate) {
        setEquipment(CHEST_SLOT, chestplate);
    }

    public ItemStack getLeggings() {
        return getEquipment(LEG_SLOT);
    }

    public void setLeggings(ItemStack leggings) {
        setEquipment(LEG_SLOT, leggings);
    }

    public ItemStack getBoots() {
        return getEquipment(BOOT_SLOT);
    }

    public void setBoots(ItemStack boots) {
        setEquipment(BOOT_SLOT, boots);
    }

    public ItemStack[] getArmorContents() {
        ItemStack[] armor = new ItemStack[INVENTORY_SLOTS - 1];
        for(int slot = WEAPON_SLOT + 1; slot < INVENTORY_SLOTS; slot++) {
            armor[slot - 1] = getEquipment(slot);
        }
        return armor;
    }

    public void setArmorContents(ItemStack[] items) {
        for(int slot = WEAPON_SLOT + 1; slot < INVENTORY_SLOTS; slot++) {
            ItemStack equipment = items != null && slot <= items.length ? items[slot - 1] : null;
            setEquipment(slot, equipment);
        }
    }

    private ItemStack getEquipment(int slot) {
        return CraftItemStack.asBukkitCopy(entity.getHandle().getEquipment(slot));
    }

    private void setEquipment(int slot, ItemStack stack) {
        entity.getHandle().setEquipment(slot, CraftItemStack.asNMSCopy(stack));
    }

    public void clear() {
        for(int i = 0; i < INVENTORY_SLOTS; i++) {
            setEquipment(i, null);
        }
    }

    public Entity getHolder() {
        return entity;
    }

    public float getItemInHandDropChance() {
       return getDropChance(WEAPON_SLOT);
    }

    public void setItemInHandDropChance(float chance) {
        setDropChance(WEAPON_SLOT, chance);
    }

    public float getHelmetDropChance() {
        return getDropChance(HELMET_SLOT);
    }

    public void setHelmetDropChance(float chance) {
        setDropChance(HELMET_SLOT, chance);
    }

    public float getChestplateDropChance() {
        return getDropChance(CHEST_SLOT);
    }

    public void setChestplateDropChance(float chance) {
        setDropChance(CHEST_SLOT, chance);
    }

    public float getLeggingsDropChance() {
        return getDropChance(LEG_SLOT);
    }

    public void setLeggingsDropChance(float chance) {
        setDropChance(LEG_SLOT, chance);
    }

    public float getBootsDropChance() {
        return getDropChance(BOOT_SLOT);
    }

    public void setBootsDropChance(float chance) {
        setDropChance(BOOT_SLOT, chance);
    }

    private void setDropChance(int slot, float chance) {
        ((EntityInsentient) entity.getHandle()).dropChances[slot] = chance - 0.1F;
    }

    private float getDropChance(int slot) {
        return ((EntityInsentient) entity.getHandle()).dropChances[slot] + 0.1F;
    }
}
