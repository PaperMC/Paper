package org.bukkit.craftbukkit.entity;

import net.minecraft.core.Rotations;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ArmorStand.LockType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class CraftArmorStand extends CraftLivingEntity implements ArmorStand {

    public CraftArmorStand(CraftServer server, net.minecraft.world.entity.decoration.ArmorStand entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftArmorStand";
    }

    @Override
    public net.minecraft.world.entity.decoration.ArmorStand getHandle() {
        return (net.minecraft.world.entity.decoration.ArmorStand) super.getHandle();
    }

    @Override
    public ItemStack getItemInHand() {
        return this.getEquipment().getItemInHand();
    }

    @Override
    public void setItemInHand(ItemStack item) {
        this.getEquipment().setItemInHand(item);
    }

    @Override
    public ItemStack getBoots() {
        return this.getEquipment().getBoots();
    }

    @Override
    public void setBoots(ItemStack item) {
        this.getEquipment().setBoots(item);
    }

    @Override
    public ItemStack getLeggings() {
        return this.getEquipment().getLeggings();
    }

    @Override
    public void setLeggings(ItemStack item) {
        this.getEquipment().setLeggings(item);
    }

    @Override
    public ItemStack getChestplate() {
        return this.getEquipment().getChestplate();
    }

    @Override
    public void setChestplate(ItemStack item) {
        this.getEquipment().setChestplate(item);
    }

    @Override
    public ItemStack getHelmet() {
        return this.getEquipment().getHelmet();
    }

    @Override
    public void setHelmet(ItemStack item) {
        this.getEquipment().setHelmet(item);
    }

    @Override
    public EulerAngle getBodyPose() {
        return CraftArmorStand.fromNMS(this.getHandle().bodyPose);
    }

    @Override
    public void setBodyPose(EulerAngle pose) {
        this.getHandle().setBodyPose(CraftArmorStand.toNMS(pose));
    }

    @Override
    public EulerAngle getLeftArmPose() {
        return CraftArmorStand.fromNMS(this.getHandle().leftArmPose);
    }

    @Override
    public void setLeftArmPose(EulerAngle pose) {
        this.getHandle().setLeftArmPose(CraftArmorStand.toNMS(pose));
    }

    @Override
    public EulerAngle getRightArmPose() {
        return CraftArmorStand.fromNMS(this.getHandle().rightArmPose);
    }

    @Override
    public void setRightArmPose(EulerAngle pose) {
        this.getHandle().setRightArmPose(CraftArmorStand.toNMS(pose));
    }

    @Override
    public EulerAngle getLeftLegPose() {
        return CraftArmorStand.fromNMS(this.getHandle().leftLegPose);
    }

    @Override
    public void setLeftLegPose(EulerAngle pose) {
        this.getHandle().setLeftLegPose(CraftArmorStand.toNMS(pose));
    }

    @Override
    public EulerAngle getRightLegPose() {
        return CraftArmorStand.fromNMS(this.getHandle().rightLegPose);
    }

    @Override
    public void setRightLegPose(EulerAngle pose) {
        this.getHandle().setRightLegPose(CraftArmorStand.toNMS(pose));
    }

    @Override
    public EulerAngle getHeadPose() {
        return CraftArmorStand.fromNMS(this.getHandle().headPose);
    }

    @Override
    public void setHeadPose(EulerAngle pose) {
        this.getHandle().setHeadPose(CraftArmorStand.toNMS(pose));
    }

    @Override
    public boolean hasBasePlate() {
        return this.getHandle().showBasePlate();
    }

    @Override
    public void setBasePlate(boolean basePlate) {
        this.getHandle().setNoBasePlate(!basePlate);
    }

    @Override
    public void setGravity(boolean gravity) {
        super.setGravity(gravity);
        // Armor stands are special
        this.getHandle().noPhysics = !gravity;
    }

    @Override
    public boolean isVisible() {
        return !this.getHandle().isInvisible();
    }

    @Override
    public void setVisible(boolean visible) {
        this.getHandle().setInvisible(!visible);
    }

    @Override
    public boolean hasArms() {
        return this.getHandle().showArms();
    }

    @Override
    public void setArms(boolean arms) {
        this.getHandle().setShowArms(arms);
    }

    @Override
    public boolean isSmall() {
        return this.getHandle().isSmall();
    }

    @Override
    public void setSmall(boolean small) {
        this.getHandle().setSmall(small);
    }

    private static EulerAngle fromNMS(Rotations old) {
        return new EulerAngle(
            Math.toRadians(old.getX()),
            Math.toRadians(old.getY()),
            Math.toRadians(old.getZ())
        );
    }

    private static Rotations toNMS(EulerAngle old) {
        return new Rotations(
            (float) Math.toDegrees(old.getX()),
            (float) Math.toDegrees(old.getY()),
            (float) Math.toDegrees(old.getZ())
        );
    }

    @Override
    public boolean isMarker() {
        return this.getHandle().isMarker();
    }

    @Override
    public void setMarker(boolean marker) {
        this.getHandle().setMarker(marker);
    }

    @Override
    public void addEquipmentLock(EquipmentSlot equipmentSlot, LockType lockType) {
        this.getHandle().disabledSlots |= (1 << CraftEquipmentSlot.getNMS(equipmentSlot).getFilterBit(lockType.ordinal() * 8));
    }

    @Override
    public void removeEquipmentLock(EquipmentSlot equipmentSlot, LockType lockType) {
        this.getHandle().disabledSlots &= ~(1 << CraftEquipmentSlot.getNMS(equipmentSlot).getFilterBit(lockType.ordinal() * 8));
    }

    @Override
    public boolean hasEquipmentLock(EquipmentSlot equipmentSlot, LockType lockType) {
        return (this.getHandle().disabledSlots & (1 << CraftEquipmentSlot.getNMS(equipmentSlot).getFilterBit(lockType.ordinal() * 8))) != 0;
    }
}
