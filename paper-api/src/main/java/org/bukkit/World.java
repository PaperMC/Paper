
package org.bukkit;

/**
 * Represents a world.
 *
 * Currently there is only one world in the default Minecraft spec, but this
 * may change with the addition of a functional Nether world
 */
public interface World {
    /**
     * Gets the block at the given location
     *
     * This block will always represent the latest state
     *
     * @param x X-coordinate of the block
     * @param y Y-coordinate of the block
     * @param z Z-coordinate of the block
     * @return Block at the given location
     */
    public Block getBlockAt(int x, int y, int z);

    /**
     * Gets the highest non-air coordinate at the given (x,z) location
     * @param x X-coordinate of the blocks
     * @param z Z-coordinate of the blocks
     * @return Y-coordinate of the highest non-air block
     */
    public int getHighestBlockYAt(int x, int z);

    /**
     * Gets the chunk at the given location
     *
     * @param x X-coordinate of the chunk
     * @param z Z-coordinate of the chunk
     * @return Chunk at the given location
     */
    public Chunk getChunkAt(int x, int z);

    /**
     * Gets the chunk which contains the given block
     *
     * @param block Block to get the parent chunk from
     * @return Chunk that contains the given block
     */
    public Chunk getChunkAt(Block block);

    /**
     * Checks if the specified chunk is loaded
     *
     * @return true if the chunk is loaded, otherwise false
     */
    public boolean isChunkLoaded(Chunk chunk);
    
    /**
     * Drop an item exactly at the specified location.
     * 
     * @param loc
     * @param item
     * @return dropped item entity
     */
    public ItemDrop dropItem(Location loc, ItemStack item);
    
    /**
     * Drop an item as if it was mined (randomly placed).
     * 
     * @param loc
     * @param item
     * @return dropped item entity
     */
    public ItemDrop dropItemNaturally(Location loc, ItemStack item);
    
    /**
     * Spawns an arrow.
     * 
     * @param loc
     * @param velocity velocity vector
     * @param speed a reasonable speed is 0.6
     * @param spread a reasonable spread is 12
     * @return the arrow entity
     */
    public Arrow spawnArrow(Location loc, Vector velocity,
            float speed, float spread);
    
    /**
     * Spawns a tree at a location.
     * 
     * @param loc
     * @return whether the tree was created
     */
    public boolean generateTree(Location loc);

    /**
     * Spawns a big tree at a location.
     * 
     * @param loc
     * @return whether the tree was created
     */
    public boolean generateBigTree(Location loc);

    /**
     * Spawns a regular passenger minecart.
     * 
     * @param loc
     * @return
     */
    public Minecart spawnMinecart(Location loc);

    /**
     * Spawns a storage minecart.
     * 
     * @param loc
     * @return
     */
    public StorageMinecart spawnStorageMinecart(Location loc);

    /**
     * Spawns a powered minecart.
     * 
     * @param loc
     * @return
     */
    public PoweredMinecart spawnPoweredMinecart(Location loc);
    
    /**
     * Spawn a boat.
     * 
     * @param loc
     * @return
     */
    public Boat spawnBoat(Location loc);

    /**
     * Gets the name of this world. This is not guaranteed to be unique.
     *
     * @return Name of this world
     */
    public String getName();

    /**
     * Gets a semi-unique identifier for this world. While it is highly unlikely
     * that this may be shared with another World, it is not guaranteed to be
     * unique.
     *
     * @return Id of this world
     */
    public long getId();
}
