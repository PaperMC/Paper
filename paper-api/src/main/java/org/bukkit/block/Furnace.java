package org.bukkit.block;

/**
 * Represents a furnace.
 * 
 * @author sk89q
 */
public interface Furnace extends BlockState, ContainerBlock {
    /**
     * Get burn time.
     * 
     * @return
     */
    public short getBurnTime();
    
    /**
     * Set burn time.
     * 
     * @param burnTime
     */
    public void setBurnTime(short burnTime);
    
    /**
     * Get cook time.
     * 
     * @return
     */
    public short getCookTime();
    
    /**
     * Set cook time.
     * 
     * @param cookTime
     */
    public void setCookTime(short cookTime);
}
