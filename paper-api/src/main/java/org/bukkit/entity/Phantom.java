package org.bukkit.entity;

/**
 * Represents a phantom.
 */
public interface Phantom extends Flying {

    /**
     * @return The size of the phantom
     */
    public int getSize();

    /**
     * @param sz The new size of the phantom.
     */
    public void setSize(int sz);
}
