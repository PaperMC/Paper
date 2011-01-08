package org.bukkit.craftbukkit;

/**
 * Indicates that an object has a method to get its CraftBukkit-equivalent
 * CraftEntity object from its Minecraft net.minecraft.server.Entity object.
 * 
 * @author sk89q
 */
public interface CraftMappable {
    /**
     * Gets the CraftEntity version.
     * 
     * @return
     */
    public CraftEntity getCraftEntity();
}
