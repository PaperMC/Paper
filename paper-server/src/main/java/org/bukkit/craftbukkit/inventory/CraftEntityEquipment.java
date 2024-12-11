package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.Mob;
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
        net.minecraft.world.entity.EquipmentSlot nmsSlot = CraftEquipmentSlot.getNMS(slot);
        this.setEquipment(nmsSlot, item, silent);
    }

    @Override
    public ItemStack getItem(EquipmentSlot slot) {
        Preconditions.checkArgument(slot != null, "slot must not be null");
        net.minecraft.world.entity.EquipmentSlot nmsSlot = CraftEquipmentSlot.getNMS(slot);
        return this.getEquipment(nmsSlot);
    }

    @Override
    public ItemStack getItemInMainHand() {
        return this.getEquipment(net.minecraft.world.entity.EquipmentSlot.MAINHAND);
    }

    @Override
    public void setItemInMainHand(ItemStack item) {
        this.setItemInMainHand(item, false);
    }

    @Override
    public void setItemInMainHand(ItemStack item, boolean silent) {
        this.setEquipment(net.minecraft.world.entity.EquipmentSlot.MAINHAND, item, silent);
    }

    @Override
    public ItemStack getItemInOffHand() {
        return this.getEquipment(net.minecraft.world.entity.EquipmentSlot.OFFHAND);
    }

    @Override
    public void setItemInOffHand(ItemStack item) {
        this.setItemInOffHand(item, false);
    }

    @Override
    public void setItemInOffHand(ItemStack item, boolean silent) {
        this.setEquipment(net.minecraft.world.entity.EquipmentSlot.OFFHAND, item, silent);
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
    public ItemStack getHelmet() {
        return this.getEquipment(net.minecraft.world.entity.EquipmentSlot.HEAD);
    }

    @Override
    public void setHelmet(ItemStack helmet) {
        this.setHelmet(helmet, false);
    }

    @Override
    public void setHelmet(ItemStack helmet, boolean silent) {
        this.setEquipment(net.minecraft.world.entity.EquipmentSlot.HEAD, helmet, silent);
    }

    @Override
    public ItemStack getChestplate() {
        return this.getEquipment(net.minecraft.world.entity.EquipmentSlot.CHEST);
    }

    @Override
    public void setChestplate(ItemStack chestplate) {
        this.setChestplate(chestplate, false);
    }

    @Override
    public void setChestplate(ItemStack chestplate, boolean silent) {
        this.setEquipment(net.minecraft.world.entity.EquipmentSlot.CHEST, chestplate, silent);
    }

    @Override
    public ItemStack getLeggings() {
        return this.getEquipment(net.minecraft.world.entity.EquipmentSlot.LEGS);
    }

    @Override
    public void setLeggings(ItemStack leggings) {
        this.setLeggings(leggings, false);
    }

    @Override
    public void setLeggings(ItemStack leggings, boolean silent) {
        this.setEquipment(net.minecraft.world.entity.EquipmentSlot.LEGS, leggings, silent);
    }

    @Override
    public ItemStack getBoots() {
        return this.getEquipment(net.minecraft.world.entity.EquipmentSlot.FEET);
    }

    @Override
    public void setBoots(ItemStack boots) {
        this.setBoots(boots, false);
    }

    @Override
    public void setBoots(ItemStack boots, boolean silent) {
        this.setEquipment(net.minecraft.world.entity.EquipmentSlot.FEET, boots, silent);
    }

    @Override
    public ItemStack[] getArmorContents() {
        ItemStack[] armor = new ItemStack[]{
                this.getEquipment(net.minecraft.world.entity.EquipmentSlot.FEET),
                this.getEquipment(net.minecraft.world.entity.EquipmentSlot.LEGS),
                this.getEquipment(net.minecraft.world.entity.EquipmentSlot.CHEST),
                this.getEquipment(net.minecraft.world.entity.EquipmentSlot.HEAD),
        };
        return armor;
    }

    @Override
    public void setArmorContents(ItemStack[] items) {
        this.setEquipment(net.minecraft.world.entity.EquipmentSlot.FEET, items.length >= 1 ? items[0] : null, false);
        this.setEquipment(net.minecraft.world.entity.EquipmentSlot.LEGS, items.length >= 2 ? items[1] : null, false);
        this.setEquipment(net.minecraft.world.entity.EquipmentSlot.CHEST, items.length >= 3 ? items[2] : null, false);
        this.setEquipment(net.minecraft.world.entity.EquipmentSlot.HEAD, items.length >= 4 ? items[3] : null, false);
    }

    private ItemStack getEquipment(net.minecraft.world.entity.EquipmentSlot slot) {
        return CraftItemStack.asBukkitCopy(this.entity.getHandle().getItemBySlot(slot));
    }

    private void setEquipment(net.minecraft.world.entity.EquipmentSlot slot, ItemStack stack, boolean silent) {
        this.entity.getHandle().setItemSlot(slot, CraftItemStack.asNMSCopy(stack), silent);
    }

    @Override
    public void clear() {
        for (net.minecraft.world.entity.EquipmentSlot slot : net.minecraft.world.entity.EquipmentSlot.values()) {
            this.setEquipment(slot, null, false);
        }
    }

    @Override
    public Entity getHolder() {
        return this.entity;
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
       return this.getDropChance(net.minecraft.world.entity.EquipmentSlot.MAINHAND);
    }

    @Override
    public void setItemInMainHandDropChance(float chance) {
        this.setDropChance(net.minecraft.world.entity.EquipmentSlot.MAINHAND, chance);
    }

    @Override
    public float getItemInOffHandDropChance() {
        return this.getDropChance(net.minecraft.world.entity.EquipmentSlot.OFFHAND);
    }

    @Override
    public void setItemInOffHandDropChance(float chance) {
        this.setDropChance(net.minecraft.world.entity.EquipmentSlot.OFFHAND, chance);
    }

    @Override
    public float getHelmetDropChance() {
        return this.getDropChance(net.minecraft.world.entity.EquipmentSlot.HEAD);
    }

    @Override
    public void setHelmetDropChance(float chance) {
        this.setDropChance(net.minecraft.world.entity.EquipmentSlot.HEAD, chance);
    }

    @Override
    public float getChestplateDropChance() {
        return this.getDropChance(net.minecraft.world.entity.EquipmentSlot.CHEST);
    }

    @Override
    public void setChestplateDropChance(float chance) {
        this.setDropChance(net.minecraft.world.entity.EquipmentSlot.CHEST, chance);
    }

    @Override
    public float getLeggingsDropChance() {
        return this.getDropChance(net.minecraft.world.entity.EquipmentSlot.LEGS);
    }

    @Override
    public void setLeggingsDropChance(float chance) {
        this.setDropChance(net.minecraft.world.entity.EquipmentSlot.LEGS, chance);
    }

    @Override
    public float getBootsDropChance() {
        return this.getDropChance(net.minecraft.world.entity.EquipmentSlot.FEET);
    }

    @Override
    public void setBootsDropChance(float chance) {
        this.setDropChance(net.minecraft.world.entity.EquipmentSlot.FEET, chance);
    }

    private void setDropChance(net.minecraft.world.entity.EquipmentSlot slot, float chance) {
        Preconditions.checkArgument(this.entity.getHandle() instanceof Mob, "Cannot set drop chance for non-Mob entity");

        if (slot == net.minecraft.world.entity.EquipmentSlot.MAINHAND || slot == net.minecraft.world.entity.EquipmentSlot.OFFHAND) {
            ((Mob) this.entity.getHandle()).handDropChances[slot.getIndex()] = chance;
        } else {
            ((Mob) this.entity.getHandle()).armorDropChances[slot.getIndex()] = chance;
        }
    }

    private float getDropChance(net.minecraft.world.entity.EquipmentSlot slot) {
        if (!(this.entity.getHandle() instanceof Mob)) {
            return 1;
        }

        if (slot == net.minecraft.world.entity.EquipmentSlot.MAINHAND || slot == net.minecraft.world.entity.EquipmentSlot.OFFHAND) {
            return ((Mob) this.entity.getHandle()).handDropChances[slot.getIndex()];
        } else {
            return ((Mob) this.entity.getHandle()).armorDropChances[slot.getIndex()];
        }
    }
}
