package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EnumItemSlot;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CraftEntityEquipment implements EntityEquipment {

    private final CraftLivingEntity entity;

    public CraftEntityEquipment(CraftLivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public void setItem(EquipmentSlot slot, ItemStack item) {
        this.setItem(slot, item, false);
    }

    @Override
    public void setItem(EquipmentSlot slot, ItemStack item, boolean silent) {
        Preconditions.checkArgument(slot != null, "slot must not be null");
        EnumItemSlot nmsSlot = CraftEquipmentSlot.getNMS(slot);
        setEquipment(nmsSlot, item, silent);
    }

    @Override
    public ItemStack getItem(EquipmentSlot slot) {
        Preconditions.checkArgument(slot != null, "slot must not be null");
        EnumItemSlot nmsSlot = CraftEquipmentSlot.getNMS(slot);
        return getEquipment(nmsSlot);
    }

    @Override
    public ItemStack getItemInMainHand() {
        return getEquipment(EnumItemSlot.MAINHAND);
    }

    @Override
    public void setItemInMainHand(ItemStack item) {
        this.setItemInMainHand(item, false);
    }

    @Override
    public void setItemInMainHand(ItemStack item, boolean silent) {
        setEquipment(EnumItemSlot.MAINHAND, item, silent);
    }

    @Override
    public ItemStack getItemInOffHand() {
        return getEquipment(EnumItemSlot.OFFHAND);
    }

    @Override
    public void setItemInOffHand(ItemStack item) {
        this.setItemInOffHand(item, false);
    }

    @Override
    public void setItemInOffHand(ItemStack item, boolean silent) {
        setEquipment(EnumItemSlot.OFFHAND, item, silent);
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
    public ItemStack getHelmet() {
        return getEquipment(EnumItemSlot.HEAD);
    }

    @Override
    public void setHelmet(ItemStack helmet) {
        this.setHelmet(helmet, false);
    }

    @Override
    public void setHelmet(ItemStack helmet, boolean silent) {
        setEquipment(EnumItemSlot.HEAD, helmet, silent);
    }

    @Override
    public ItemStack getChestplate() {
        return getEquipment(EnumItemSlot.CHEST);
    }

    @Override
    public void setChestplate(ItemStack chestplate) {
        this.setChestplate(chestplate, false);
    }

    @Override
    public void setChestplate(ItemStack chestplate, boolean silent) {
        setEquipment(EnumItemSlot.CHEST, chestplate, silent);
    }

    @Override
    public ItemStack getLeggings() {
        return getEquipment(EnumItemSlot.LEGS);
    }

    @Override
    public void setLeggings(ItemStack leggings) {
        this.setLeggings(leggings, false);
    }

    @Override
    public void setLeggings(ItemStack leggings, boolean silent) {
        setEquipment(EnumItemSlot.LEGS, leggings, silent);
    }

    @Override
    public ItemStack getBoots() {
        return getEquipment(EnumItemSlot.FEET);
    }

    @Override
    public void setBoots(ItemStack boots) {
        this.setBoots(boots, false);
    }

    @Override
    public void setBoots(ItemStack boots, boolean silent) {
        setEquipment(EnumItemSlot.FEET, boots, silent);
    }

    @Override
    public ItemStack[] getArmorContents() {
        ItemStack[] armor = new ItemStack[]{
                getEquipment(EnumItemSlot.FEET),
                getEquipment(EnumItemSlot.LEGS),
                getEquipment(EnumItemSlot.CHEST),
                getEquipment(EnumItemSlot.HEAD),
        };
        return armor;
    }

    @Override
    public void setArmorContents(ItemStack[] items) {
        setEquipment(EnumItemSlot.FEET, items.length >= 1 ? items[0] : null, false);
        setEquipment(EnumItemSlot.LEGS, items.length >= 2 ? items[1] : null, false);
        setEquipment(EnumItemSlot.CHEST, items.length >= 3 ? items[2] : null, false);
        setEquipment(EnumItemSlot.HEAD, items.length >= 4 ? items[3] : null, false);
    }

    private ItemStack getEquipment(EnumItemSlot slot) {
        return CraftItemStack.asBukkitCopy(entity.getHandle().getEquipment(slot));
    }

    private void setEquipment(EnumItemSlot slot, ItemStack stack, boolean silent) {
        entity.getHandle().setSlot(slot, CraftItemStack.asNMSCopy(stack), silent);
    }

    @Override
    public void clear() {
        for (EnumItemSlot slot : EnumItemSlot.values()) {
            setEquipment(slot, null, false);
        }
    }

    @Override
    public Entity getHolder() {
        return entity;
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
       return getDropChance(EnumItemSlot.MAINHAND);
    }

    @Override
    public void setItemInMainHandDropChance(float chance) {
        setDropChance(EnumItemSlot.MAINHAND, chance);
    }

    @Override
    public float getItemInOffHandDropChance() {
        return getDropChance(EnumItemSlot.OFFHAND);
    }

    @Override
    public void setItemInOffHandDropChance(float chance) {
        setDropChance(EnumItemSlot.OFFHAND, chance);
    }

    @Override
    public float getHelmetDropChance() {
        return getDropChance(EnumItemSlot.HEAD);
    }

    @Override
    public void setHelmetDropChance(float chance) {
        setDropChance(EnumItemSlot.HEAD, chance);
    }

    @Override
    public float getChestplateDropChance() {
        return getDropChance(EnumItemSlot.CHEST);
    }

    @Override
    public void setChestplateDropChance(float chance) {
        setDropChance(EnumItemSlot.CHEST, chance);
    }

    @Override
    public float getLeggingsDropChance() {
        return getDropChance(EnumItemSlot.LEGS);
    }

    @Override
    public void setLeggingsDropChance(float chance) {
        setDropChance(EnumItemSlot.LEGS, chance);
    }

    @Override
    public float getBootsDropChance() {
        return getDropChance(EnumItemSlot.FEET);
    }

    @Override
    public void setBootsDropChance(float chance) {
        setDropChance(EnumItemSlot.FEET, chance);
    }

    private void setDropChance(EnumItemSlot slot, float chance) {
        if (slot == EnumItemSlot.MAINHAND || slot == EnumItemSlot.OFFHAND) {
            ((EntityInsentient) entity.getHandle()).dropChanceHand[slot.b()] = chance;
        } else {
            ((EntityInsentient) entity.getHandle()).dropChanceArmor[slot.b()] = chance;
        }
    }

    private float getDropChance(EnumItemSlot slot) {
        if (slot == EnumItemSlot.MAINHAND || slot == EnumItemSlot.OFFHAND) {
            return ((EntityInsentient) entity.getHandle()).dropChanceHand[slot.b()];
        } else {
            return ((EntityInsentient) entity.getHandle()).dropChanceArmor[slot.b()];
        }
    }
}
