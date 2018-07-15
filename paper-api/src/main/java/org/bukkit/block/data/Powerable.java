package org.bukkit.block.data;

/**
 * 'powered' indicates whether this block is in the powered state or not, i.e.
 * receiving a redstone current of power &gt; 0.
 */
public interface Powerable extends BlockData {

    /**
     * Gets the value of the 'powered' property.
     *
     * @return the 'powered' value
     */
    boolean isPowered();

    /**
     * Sets the value of the 'powered' property.
     *
     * @param powered the new 'powered' value
     */
    void setPowered(boolean powered);
}
