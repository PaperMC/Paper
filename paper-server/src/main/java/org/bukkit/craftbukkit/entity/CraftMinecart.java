package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.vehicle.EntityMinecartAbstract;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.Minecart;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public abstract class CraftMinecart extends CraftVehicle implements Minecart {
    public CraftMinecart(CraftServer server, EntityMinecartAbstract entity) {
        super(server, entity);
    }

    @Override
    public void setDamage(double damage) {
        getHandle().setDamage((float) damage);
    }

    @Override
    public double getDamage() {
        return getHandle().getDamage();
    }

    @Override
    public double getMaxSpeed() {
        return getHandle().maxSpeed;
    }

    @Override
    public void setMaxSpeed(double speed) {
        if (speed >= 0D) {
            getHandle().maxSpeed = speed;
        }
    }

    @Override
    public boolean isSlowWhenEmpty() {
        return getHandle().slowWhenEmpty;
    }

    @Override
    public void setSlowWhenEmpty(boolean slow) {
        getHandle().slowWhenEmpty = slow;
    }

    @Override
    public Vector getFlyingVelocityMod() {
        return getHandle().getFlyingVelocityMod();
    }

    @Override
    public void setFlyingVelocityMod(Vector flying) {
        getHandle().setFlyingVelocityMod(flying);
    }

    @Override
    public Vector getDerailedVelocityMod() {
        return getHandle().getDerailedVelocityMod();
    }

    @Override
    public void setDerailedVelocityMod(Vector derailed) {
        getHandle().setDerailedVelocityMod(derailed);
    }

    @Override
    public EntityMinecartAbstract getHandle() {
        return (EntityMinecartAbstract) entity;
    }

    @Override
    public void setDisplayBlock(MaterialData material) {
        if (material != null) {
            IBlockData block = CraftMagicNumbers.getBlock(material);
            this.getHandle().setDisplayBlockState(block);
        } else {
            // Set block to air (default) and set the flag to not have a display block.
            this.getHandle().setDisplayBlockState(Blocks.AIR.defaultBlockState());
            this.getHandle().setCustomDisplay(false);
        }
    }

    @Override
    public void setDisplayBlockData(BlockData blockData) {
        if (blockData != null) {
            IBlockData block = ((CraftBlockData) blockData).getState();
            this.getHandle().setDisplayBlockState(block);
        } else {
            // Set block to air (default) and set the flag to not have a display block.
            this.getHandle().setDisplayBlockState(Blocks.AIR.defaultBlockState());
            this.getHandle().setCustomDisplay(false);
        }
    }

    @Override
    public MaterialData getDisplayBlock() {
        IBlockData blockData = getHandle().getDisplayBlockState();
        return CraftMagicNumbers.getMaterial(blockData);
    }

    @Override
    public BlockData getDisplayBlockData() {
        IBlockData blockData = getHandle().getDisplayBlockState();
        return CraftBlockData.fromData(blockData);
    }

    @Override
    public void setDisplayBlockOffset(int offset) {
        getHandle().setDisplayOffset(offset);
    }

    @Override
    public int getDisplayBlockOffset() {
        return getHandle().getDisplayOffset();
    }
}
