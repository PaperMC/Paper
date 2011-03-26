
package org.bukkit.event.world;

import org.bukkit.event.Listener;

/**
 * Handles all World related events
 */
public class WorldListener implements Listener {
    /**
     * Called when a chunk is loaded
     *
     * @param event Relevant event details
     */
    public void onChunkLoad(ChunkLoadEvent event) {
    }

    /**
     * Called when a chunk is unloaded
     *
     * @param event Relevant event details
     */
    public void onChunkUnload(ChunkUnloadEvent event) {
    }

    /**
    * Called when a world is saved
    *
    * @param event Relevant event details
    */
    public void onWorldSave(WorldSaveEvent event) {
    }

    /**
     * Called when a World is loaded
     *
     * @param event Relevant event details
     */
    public void onWorldLoad(WorldLoadEvent event) {
    }
}
