package org.bukkit.event.world;

import org.bukkit.event.Listener;

/**
 * Handles all World related events
 */
@Deprecated
public class WorldListener implements Listener {

    /**
     * Called when a chunk is loaded
     *
     * @param event Relevant event details
     */
    public void onChunkLoad(ChunkLoadEvent event) {}

    /**
     * Called when a newly created chunk has been populated.
     * <p />
     * If your intent is to populate the chunk using this event, please see {@link org.bukkit.generator.BlockPopulator}
     *
     * @param event Relevant event details
     */
    public void onChunkPopulate(ChunkPopulateEvent event) {}

    /**
     * Called when a chunk is unloaded
     *
     * @param event Relevant event details
     */
    public void onChunkUnload(ChunkUnloadEvent event) {}

    /**
     * Called when a World's spawn is changed
     *
     * @param event Relevant event details
     */
    public void onSpawnChange(SpawnChangeEvent event) {}

    /**
     * Called when the world generates a portal end point
     *
     * @param event Relevant event details
     */
    public void onPortalCreate(PortalCreateEvent event) {}

    /**
     * Called when a world is saved
     *
     * @param event Relevant event details
     */
    public void onWorldSave(WorldSaveEvent event) {}

    /**
     * Called when a World is initializing
     *
     * @param event Relevant event details
     */
    public void onWorldInit(WorldInitEvent event) {}

    /**
     * Called when a World is loaded
     *
     * @param event Relevant event details
     */
    public void onWorldLoad(WorldLoadEvent event) {}

    /**
     * Called when a World is unloaded
     *
     * @param event Relevant event details
     */
    public void onWorldUnload(WorldUnloadEvent event) {}

    /**
     * Event that is called when an organic structure attempts to grow (Sapling -> Tree), (Mushroom -> Huge Mushroom), naturally or using bonemeal.
     *
     * @param event Relevant event details
     */
    public void onStructureGrow(StructureGrowEvent event) {}
}
