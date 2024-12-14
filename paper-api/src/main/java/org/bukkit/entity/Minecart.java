package org.bukkit.entity;

import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a minecart entity.
 *
 * @since 1.0.0 R1
 */
public interface Minecart extends Vehicle, io.papermc.paper.entity.Frictional { // Paper

    /**
     * Sets a minecart's damage.
     *
     * @param damage over 40 to "kill" a minecart
     * @since 1.6.1 R0.1
     */
    public void setDamage(double damage);

    /**
     * Gets a minecart's damage.
     *
     * @return The damage
     */
    public double getDamage();

    /**
     * Gets the maximum speed of a minecart. The speed is unrelated to the
     * velocity.
     *
     * @return The max speed
     */
    public double getMaxSpeed();

    /**
     * Sets the maximum speed of a minecart. Must be nonnegative. Default is
     * 0.4D or {@link GameRule#MINECART_MAX_SPEED}.
     *
     * @param speed The max speed
     */
    public void setMaxSpeed(double speed);

    /**
     * Returns whether this minecart will slow down faster without a passenger
     * occupying it
     *
     * @return Whether it decelerates faster
     */
    public boolean isSlowWhenEmpty();

    /**
     * Sets whether this minecart will slow down faster without a passenger
     * occupying it
     *
     * @param slow Whether it will decelerate faster
     */
    public void setSlowWhenEmpty(boolean slow);

    /**
     * Gets the flying velocity modifier. Used for minecarts that are in
     * mid-air. A flying minecart's velocity is multiplied by this factor each
     * tick.
     *
     * @return The vector factor
     */
    @NotNull
    public Vector getFlyingVelocityMod();

    /**
     * Sets the flying velocity modifier. Used for minecarts that are in
     * mid-air. A flying minecart's velocity is multiplied by this factor each
     * tick.
     *
     * @param flying velocity modifier vector
     */
    public void setFlyingVelocityMod(@NotNull Vector flying);

    /**
     * Gets the derailed velocity modifier. Used for minecarts that are on the
     * ground, but not on rails.
     * <p>
     * A derailed minecart's velocity is multiplied by this factor each tick.
     *
     * @return derailed visible speed
     */
    @NotNull
    public Vector getDerailedVelocityMod();

    /**
     * Sets the derailed velocity modifier. Used for minecarts that are on the
     * ground, but not on rails. A derailed minecart's velocity is multiplied
     * by this factor each tick.
     *
     * @param derailed visible speed
     */
    public void setDerailedVelocityMod(@NotNull Vector derailed);

    /**
     * Sets the display block for this minecart.
     * Passing a null value will set the minecart to have no display block.
     *
     * @param material the material to set as display block.
     * @deprecated use {@link #setDisplayBlockData(BlockData)}
     * @since 1.8
     */
    @Deprecated(forRemoval = true, since = "1.13")
    public void setDisplayBlock(@Nullable MaterialData material);

    /**
     * Gets the display block for this minecart.
     * This function will return the type AIR if none is set.
     *
     * @return the block displayed by this minecart.
     * @deprecated use {@link #getDisplayBlockData()}
     * @since 1.8
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "1.13")
    public MaterialData getDisplayBlock();

    /**
     * Sets the display block for this minecart.
     * Passing a null value will set the minecart to have no display block.
     *
     * @param blockData the material to set as display block.
     * @since 1.13
     */
    public void setDisplayBlockData(@Nullable BlockData blockData);

    /**
     * Gets the display block for this minecart.
     * This function will return the type AIR if none is set.
     *
     * @return the block displayed by this minecart.
     * @since 1.13
     */
    @NotNull
    public BlockData getDisplayBlockData();

    /**
     * Sets the offset of the display block.
     *
     * @param offset the block offset to set for this minecart.
     * @since 1.8
     */
    public void setDisplayBlockOffset(int offset);

    /**
     * Gets the offset of the display block.
     *
     * @return the current block offset for this minecart.
     * @since 1.8
     */
    public int getDisplayBlockOffset();

    // Paper start
    /**
     * Gets the {@link Material} that represents this Minecart type.
     *
     * @return the minecart material.
     * @since 1.16.4
     */
    @NotNull
    public Material getMinecartMaterial();
    // Paper end
}
