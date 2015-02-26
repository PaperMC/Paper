package org.bukkit.craftbukkit.entity;

import net.minecraft.server.Blocks;
import net.minecraft.server.EntityMinecartAbstract;

import net.minecraft.server.IBlockData;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.Minecart;
import org.bukkit.material.MaterialData;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public abstract class CraftMinecart extends CraftVehicle implements Minecart {
    public CraftMinecart(CraftServer server, EntityMinecartAbstract entity) {
        super(server, entity);
    }

    public void setDamage(double damage) {
        getHandle().setDamage((float) damage);
    }

    public double getDamage() {
        return getHandle().getDamage();
    }

    public double getMaxSpeed() {
        return getHandle().maxSpeed;
    }

    public void setMaxSpeed(double speed) {
        if (speed >= 0D) {
            getHandle().maxSpeed = speed;
        }
    }

    public boolean isSlowWhenEmpty() {
        return getHandle().slowWhenEmpty;
    }

    public void setSlowWhenEmpty(boolean slow) {
        getHandle().slowWhenEmpty = slow;
    }

    public Vector getFlyingVelocityMod() {
        return getHandle().getFlyingVelocityMod();
    }

    public void setFlyingVelocityMod(Vector flying) {
        getHandle().setFlyingVelocityMod(flying);
    }

    public Vector getDerailedVelocityMod() {
        return getHandle().getDerailedVelocityMod();
    }

    public void setDerailedVelocityMod(Vector derailed) {
        getHandle().setDerailedVelocityMod(derailed);
    }

    @Override
    public EntityMinecartAbstract getHandle() {
        return (EntityMinecartAbstract) entity;
    }

    @Deprecated
    public void _INVALID_setDamage(int damage) {
        setDamage(damage);
    }

    @Deprecated
    public int _INVALID_getDamage() {
        return NumberConversions.ceil(getDamage());
    }

    public void setDisplayBlock(MaterialData material) {
        if(material != null) {
            IBlockData block = CraftMagicNumbers.getBlock(material.getItemTypeId()).fromLegacyData(material.getData());
            this.getHandle().setDisplayBlock(block);
        } else {
            // Set block to air (default) and set the flag to not have a display block.
            this.getHandle().setDisplayBlock(Blocks.AIR.getBlockData());
            this.getHandle().a(false);
        }
    }

    public MaterialData getDisplayBlock() {
        IBlockData blockData = getHandle().getDisplayBlock();
        return CraftMagicNumbers.getMaterial(blockData.getBlock()).getNewData((byte) blockData.getBlock().toLegacyData(blockData));
    }

    public void setDisplayBlockOffset(int offset) {
        getHandle().SetDisplayBlockOffset(offset);
    }

    public int getDisplayBlockOffset() {
        return getHandle().getDisplayBlockOffset();
    }
}
