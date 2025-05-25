package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.Minecart;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;
import java.util.Optional;

public abstract class CraftMinecart extends CraftVehicle implements Minecart {

    public CraftMinecart(CraftServer server, AbstractMinecart entity) {
        super(server, entity);
    }

    @Override
    public AbstractMinecart getHandle() {
        return (AbstractMinecart) this.entity;
    }

    @Override
    public void setDamage(double damage) {
        this.getHandle().setDamage((float) damage);
    }

    @Override
    public double getDamage() {
        return this.getHandle().getDamage();
    }

    @Override
    public double getMaxSpeed() {
        return this.getHandle().getBehavior().getMaxSpeed((ServerLevel) this.getHandle().level());
    }

    @Override
    public void setMaxSpeed(double speed) {
        if (speed >= 0D) {
            this.getHandle().maxSpeed = speed;
        }
    }

    @Override
    public boolean isSlowWhenEmpty() {
        return this.getHandle().slowWhenEmpty;
    }

    @Override
    public void setSlowWhenEmpty(boolean slow) {
        this.getHandle().slowWhenEmpty = slow;
    }

    @Override
    public Vector getFlyingVelocityMod() {
        return this.getHandle().getFlyingVelocityMod();
    }

    @Override
    public void setFlyingVelocityMod(Vector flying) {
        this.getHandle().setFlyingVelocityMod(flying);
    }

    @Override
    public Vector getDerailedVelocityMod() {
        return this.getHandle().getDerailedVelocityMod();
    }

    @Override
    public void setDerailedVelocityMod(Vector derailed) {
        this.getHandle().setDerailedVelocityMod(derailed);
    }

    // Paper start
    @Override
    public Material getMinecartMaterial() {
        return CraftMagicNumbers.getMaterial(this.getHandle().getDropItem());
    }
    // Paper end

    @Override
    public void setDisplayBlock(MaterialData material) {
        this.getHandle().setCustomDisplayBlockState(Optional.ofNullable(material).map(CraftMagicNumbers::getBlock));
    }

    @Override
    public void setDisplayBlockData(BlockData blockData) {
        this.getHandle().setCustomDisplayBlockState(Optional.ofNullable(blockData).map(data -> ((CraftBlockData) data).getState()));
    }

    @Override
    public MaterialData getDisplayBlock() {
        BlockState state = this.getHandle().getDisplayBlockState();
        return CraftMagicNumbers.getMaterial(state);
    }

    @Override
    public BlockData getDisplayBlockData() {
        BlockState state = this.getHandle().getDisplayBlockState();
        return CraftBlockData.fromData(state);
    }

    @Override
    public void setDisplayBlockOffset(int offset) {
        this.getHandle().setDisplayOffset(offset);
    }

    @Override
    public int getDisplayBlockOffset() {
        return this.getHandle().getDisplayOffset();
    }

    @org.jetbrains.annotations.NotNull
    @Override
    public net.kyori.adventure.util.TriState getFrictionState() {
        return this.getHandle().frictionState;
    }

    @Override
    public void setFrictionState(@org.jetbrains.annotations.NotNull net.kyori.adventure.util.TriState state) {
        Preconditions.checkArgument(state != null, "state may not be null");
        this.getHandle().frictionState = state;
    }
}
