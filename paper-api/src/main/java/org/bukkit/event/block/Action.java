package org.bukkit.event.block;

public enum Action {

    /**
     * Left-clicking a block
     */
    LEFT_CLICK_BLOCK,
    /**
     * Right-clicking a block
     */
    RIGHT_CLICK_BLOCK,
    /**
     * Left-clicking the air
     */
    LEFT_CLICK_AIR,
    /**
     * Right-clicking the air
     */
    RIGHT_CLICK_AIR,
    /**
     * Stepping onto or into a block (Ass-pressure)
     *
     * Examples:
     * <ul>
     * <li>Jumping on soil
     * <li>Standing on pressure plate
     * <li>Triggering redstone ore
     * <li>Triggering tripwire
     * </ul>
     */
    // Paper start
    PHYSICAL;

    /**
     * Gets whether this action is a result of a left click.
     *
     * @return Whether it's a left click
     */
    public boolean isLeftClick() {
        return this == LEFT_CLICK_AIR || this == LEFT_CLICK_BLOCK;
    }

    /**
     * Gets whether this action is a result of a right click.
     *
     * @return Whether it's a right click
     */
    public boolean isRightClick() {
        return this == RIGHT_CLICK_AIR || this == RIGHT_CLICK_BLOCK;
    }
    // Paper end
}
