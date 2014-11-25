package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityArmorStand;
import net.minecraft.server.Vector3f;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class CraftArmorStand extends CraftLivingEntity implements ArmorStand {

    private static final int HAND = 0;
    private static final int FEET = 1;
    private static final int LEGS = 2;
    private static final int CHEST = 3;
    private static final int HEAD = 4;

    public CraftArmorStand(CraftServer server, EntityArmorStand entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftArmorStand";
    }

    @Override
    public EntityType getType() {
        return EntityType.ARMOR_STAND;
    }

    @Override
    public EntityArmorStand getHandle() {
        return (EntityArmorStand) super.getHandle();
    }

    @Override
    public ItemStack getItemInHand() {
        return CraftItemStack.asBukkitCopy(getHandle().getEquipment(HAND));
    }

    @Override
    public void setItemInHand(ItemStack item) {
        getHandle().setEquipment(HAND, CraftItemStack.asNMSCopy(item));
    }

    @Override
    public ItemStack getBoots() {
        return CraftItemStack.asBukkitCopy(getHandle().getEquipment(FEET));
    }

    @Override
    public void setBoots(ItemStack item) {
        getHandle().setEquipment(FEET, CraftItemStack.asNMSCopy(item));
    }

    @Override
    public ItemStack getLeggings() {
        return CraftItemStack.asBukkitCopy(getHandle().getEquipment(LEGS));
    }

    @Override
    public void setLeggings(ItemStack item) {
        getHandle().setEquipment(LEGS, CraftItemStack.asNMSCopy(item));
    }

    @Override
    public ItemStack getChestplate() {
        return CraftItemStack.asBukkitCopy(getHandle().getEquipment(CHEST));
    }

    @Override
    public void setChestplate(ItemStack item) {
        getHandle().setEquipment(CHEST, CraftItemStack.asNMSCopy(item));
    }

    @Override
    public ItemStack getHelmet() {
        return CraftItemStack.asBukkitCopy(getHandle().getEquipment(HEAD));
    }

    @Override
    public void setHelmet(ItemStack item) {
        getHandle().setEquipment(HEAD, CraftItemStack.asNMSCopy(item));
    }

    @Override
    public EulerAngle getBodyPose() {
        return fromNMS(getHandle().bodyPose);
    }

    @Override
    public void setBodyPose(EulerAngle pose) {
        getHandle().setBodyPose(toNMS(pose));
    }

    @Override
    public EulerAngle getLeftArmPose() {
        return fromNMS(getHandle().leftArmPose);
    }

    @Override
    public void setLeftArmPose(EulerAngle pose) {
        getHandle().setLeftArmPose(toNMS(pose));
    }

    @Override
    public EulerAngle getRightArmPose() {
        return fromNMS(getHandle().rightArmPose);
    }

    @Override
    public void setRightArmPose(EulerAngle pose) {
        getHandle().setRightArmPose(toNMS(pose));
    }

    @Override
    public EulerAngle getLeftLegPose() {
        return fromNMS(getHandle().leftLegPose);
    }

    @Override
    public void setLeftLegPose(EulerAngle pose) {
        getHandle().setLeftLegPose(toNMS(pose));
    }

    @Override
    public EulerAngle getRightLegPose() {
        return fromNMS(getHandle().rightLegPose);
    }

    @Override
    public void setRightLegPose(EulerAngle pose) {
        getHandle().setRightLegPose(toNMS(pose));
    }

    @Override
    public EulerAngle getHeadPose() {
        return fromNMS(getHandle().headPose);
    }

    @Override
    public void setHeadPose(EulerAngle pose) {
        getHandle().setHeadPose(toNMS(pose));
    }

    @Override
    public boolean hasBasePlate() {
        return !getHandle().hasBasePlate();
    }

    @Override
    public void setBasePlate(boolean basePlate) {
        getHandle().setBasePlate(!basePlate);
    }

    @Override
    public boolean hasGravity() {
        return !getHandle().hasGravity();
    }

    @Override
    public void setGravity(boolean gravity) {
        getHandle().setGravity(!gravity);
    }

    @Override
    public boolean isVisible() {
        return !getHandle().isInvisible();
    }

    @Override
    public void setVisible(boolean visible) {
        getHandle().setInvisible(!visible);
    }

    @Override
    public boolean hasArms() {
        return getHandle().hasArms();
    }

    @Override
    public void setArms(boolean arms) {
        getHandle().setArms(arms);
    }

    @Override
    public boolean isSmall() {
        return getHandle().isSmall();
    }

    @Override
    public void setSmall(boolean small) {
        getHandle().setSmall(small);
    }

    private static EulerAngle fromNMS(Vector3f old) {
        return new EulerAngle(
            Math.toRadians(old.getX()),
            Math.toRadians(old.getY()),
            Math.toRadians(old.getZ())
        );
    }

    private static Vector3f toNMS(EulerAngle old) {
        return new Vector3f(
            (float) Math.toDegrees(old.getX()),
            (float) Math.toDegrees(old.getY()),
            (float) Math.toDegrees(old.getZ())
        );
    }
}
