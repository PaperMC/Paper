
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
    public void onChunkLoaded(ChunkLoadedEvent event) {
    }

    /**
     * Called when a chunk is unloaded
     *
     * @param event Relevant event details
     */
    public void onChunkUnloaded(ChunkUnloadedEvent event) {
    }
}
