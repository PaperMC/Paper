package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.ItemFrame;

public class CraftItemFrame extends CraftHanging implements ItemFrame {
    public CraftItemFrame(CraftServer server, net.minecraft.world.entity.decoration.ItemFrame entity) {
        super(server, entity);
    }

    @Override
    public boolean setFacingDirection(BlockFace face, boolean force) {
        HangingEntity hanging = this.getHandle();
        Direction oldDir = hanging.getDirection();
        Direction newDir = CraftBlock.blockFaceToNotch(face);

        Preconditions.checkArgument(newDir != null, "%s is not a valid facing direction", face);

        this.getHandle().setDirection(newDir);
        if (!force && !this.getHandle().generation && !hanging.survives()) {
            hanging.setDirection(oldDir);
            return false;
        }

        this.update();

        return true;
    }

    @Override
    protected void update() {
        super.update();

        // mark dirty, so that the client gets updated with item and rotation
        this.getHandle().getEntityData().markDirty(net.minecraft.world.entity.decoration.ItemFrame.DATA_ITEM);
        this.getHandle().getEntityData().markDirty(net.minecraft.world.entity.decoration.ItemFrame.DATA_ROTATION);

        // update redstone
        if (!this.getHandle().generation) {
            this.getHandle().level().updateNeighbourForOutputSignal(this.getHandle().getPos(), Blocks.AIR);
        }
    }

    @Override
    public void setItem(org.bukkit.inventory.ItemStack item) {
        this.setItem(item, true);
    }

    @Override
    public void setItem(org.bukkit.inventory.ItemStack item, boolean playSound) {
        // only updated redstone and play sound when it is not in generation
        this.getHandle().setItem(CraftItemStack.asNMSCopy(item), !this.getHandle().generation, !this.getHandle().generation && playSound);
    }

    @Override
    public org.bukkit.inventory.ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(this.getHandle().getItem());
    }

    @Override
    public float getItemDropChance() {
        return this.getHandle().dropChance;
    }

    @Override
    public void setItemDropChance(float chance) {
        Preconditions.checkArgument(0.0 <= chance && chance <= 1.0, "Chance (%s) outside range [0, 1]", chance);
        this.getHandle().dropChance = chance;
    }

    @Override
    public Rotation getRotation() {
        return this.toBukkitRotation(this.getHandle().getRotation());
    }

    Rotation toBukkitRotation(int value) {
        // Translate NMS rotation integer to Bukkit API
        switch (value) {
        case 0:
            return Rotation.NONE;
        case 1:
            return Rotation.CLOCKWISE_45;
        case 2:
            return Rotation.CLOCKWISE;
        case 3:
            return Rotation.CLOCKWISE_135;
        case 4:
            return Rotation.FLIPPED;
        case 5:
            return Rotation.FLIPPED_45;
        case 6:
            return Rotation.COUNTER_CLOCKWISE;
        case 7:
            return Rotation.COUNTER_CLOCKWISE_45;
        default:
            throw new AssertionError("Unknown rotation " + value + " for " + this.getHandle());
        }
    }

    @Override
    public void setRotation(Rotation rotation) {
        Preconditions.checkArgument(rotation != null, "Rotation cannot be null");
        this.getHandle().setRotation(CraftItemFrame.toInteger(rotation));
    }

    static int toInteger(Rotation rotation) {
        // Translate Bukkit API rotation to NMS integer
        switch (rotation) {
        case NONE:
            return 0;
        case CLOCKWISE_45:
            return 1;
        case CLOCKWISE:
            return 2;
        case CLOCKWISE_135:
            return 3;
        case FLIPPED:
            return 4;
        case FLIPPED_45:
            return 5;
        case COUNTER_CLOCKWISE:
            return 6;
        case COUNTER_CLOCKWISE_45:
            return 7;
        default:
            throw new IllegalArgumentException(rotation + " is not applicable to an ItemFrame");
        }
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
    public boolean isFixed() {
        return this.getHandle().fixed;
    }

    @Override
    public void setFixed(boolean fixed) {
        this.getHandle().fixed = fixed;
    }

    @Override
    public net.minecraft.world.entity.decoration.ItemFrame getHandle() {
        return (net.minecraft.world.entity.decoration.ItemFrame) this.entity;
    }

    @Override
    public String toString() {
        return "CraftItemFrame{item=" + this.getItem() + ", rotation=" + this.getRotation() + "}";
    }
}
