package org.bukkit.event.world;

import org.bukkit.event.Listener;
import org.bukkit.plugin.AuthorNagException;

/**
 * Handles all World related events
 */
public class WorldListener implements Listener {

    /**
     * Called when a chunk is loaded
     *
     * @param event Relevant event details
     */
    public void onChunkLoad(ChunkLoadEvent event) {}

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
    public void onWorldSave(WorldSaveEvent event) {
        onWorldSave((WorldEvent) event);
        throw new AuthorNagException("onWorldSave has been replaced with a new signature, (WorldSaveEvent)");
    }

    /**
     * Called when a World is initializing
     *
     * @param event Relevant event details
     */
    public void onWorldInit(WorldInitEvent event) {
    }

    /**
     * Called when a World is loaded
     *
     * @param event Relevant event details
     */
    public void onWorldLoad(WorldLoadEvent event) {
        onWorldLoad((WorldEvent) event);
        throw new AuthorNagException("onWorldLoad has been replaced with a new signature, (WorldLoadEvent)");
    }

    /**
     * Called when a World is unloaded
     *
     * @param event Relevant event details
     */
    public void onWorldUnload(WorldUnloadEvent event) { }

    // TODO: Remove after RB
    @Deprecated public void onWorldLoad(WorldEvent event) {}
    @Deprecated public void onWorldSave(WorldEvent event) {}
}
