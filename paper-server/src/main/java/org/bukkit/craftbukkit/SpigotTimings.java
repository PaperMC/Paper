package org.bukkit.craftbukkit;

import java.util.HashMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.bukkit.craftbukkit.scheduler.CraftTask;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.scheduler.BukkitTask;
import org.spigotmc.CustomTimingsHandler;

public class SpigotTimings {

    public static final CustomTimingsHandler serverTickTimer = new CustomTimingsHandler("** Full Server Tick");
    public static final CustomTimingsHandler playerListTimer = new CustomTimingsHandler("Player List");
    public static final CustomTimingsHandler commandFunctionsTimer = new CustomTimingsHandler("Command Functions");
    public static final CustomTimingsHandler connectionTimer = new CustomTimingsHandler("Connection Handler");
    public static final CustomTimingsHandler playerConnectionTimer = new CustomTimingsHandler("** PlayerConnection");
    public static final CustomTimingsHandler tickablesTimer = new CustomTimingsHandler("Tickables");
    public static final CustomTimingsHandler schedulerTimer = new CustomTimingsHandler("Scheduler");
    public static final CustomTimingsHandler timeUpdateTimer = new CustomTimingsHandler("Time Update");
    public static final CustomTimingsHandler serverCommandTimer = new CustomTimingsHandler("Server Command");
    public static final CustomTimingsHandler worldSaveTimer = new CustomTimingsHandler("World Save");

    public static final CustomTimingsHandler entityMoveTimer = new CustomTimingsHandler("** entityMove");
    public static final CustomTimingsHandler tickEntityTimer = new CustomTimingsHandler("** tickEntity");
    public static final CustomTimingsHandler activatedEntityTimer = new CustomTimingsHandler("** activatedTickEntity");
    public static final CustomTimingsHandler tickTileEntityTimer = new CustomTimingsHandler("** tickTileEntity");

    public static final CustomTimingsHandler timerEntityBaseTick = new CustomTimingsHandler("** livingEntityBaseTick");
    public static final CustomTimingsHandler timerEntityAI = new CustomTimingsHandler("** livingEntityAI");
    public static final CustomTimingsHandler timerEntityAICollision = new CustomTimingsHandler("** livingEntityAICollision");
    public static final CustomTimingsHandler timerEntityAIMove = new CustomTimingsHandler("** livingEntityAIMove");
    public static final CustomTimingsHandler timerEntityTickRest = new CustomTimingsHandler("** livingEntityTickRest");

    public static final CustomTimingsHandler processQueueTimer = new CustomTimingsHandler("processQueue");
    public static final CustomTimingsHandler schedulerSyncTimer = new CustomTimingsHandler("** Scheduler - Sync Tasks", JavaPluginLoader.pluginParentTimer);

    public static final CustomTimingsHandler playerCommandTimer = new CustomTimingsHandler("** playerCommand");

    public static final CustomTimingsHandler entityActivationCheckTimer = new CustomTimingsHandler("entityActivationCheck");
    public static final CustomTimingsHandler checkIfActiveTimer = new CustomTimingsHandler("** checkIfActive");

    public static final HashMap<String, CustomTimingsHandler> entityTypeTimingMap = new HashMap<String, CustomTimingsHandler>();
    public static final HashMap<String, CustomTimingsHandler> tileEntityTypeTimingMap = new HashMap<String, CustomTimingsHandler>();
    public static final HashMap<String, CustomTimingsHandler> pluginTaskTimingMap = new HashMap<String, CustomTimingsHandler>();

    /**
     * Gets a timer associated with a plugins tasks.
     * @param task
     * @param period
     * @return
     */
    public static CustomTimingsHandler getPluginTaskTimings(BukkitTask task, long period) {
        if (!task.isSync()) {
            return null;
        }
        String plugin;
        final CraftTask ctask = (CraftTask) task;

        if (task.getOwner() != null) {
            plugin = task.getOwner().getDescription().getFullName();
        } else {
            plugin = "Unknown";
        }
        String taskname = ctask.getTaskName();

        String name = "Task: " + plugin + " Runnable: " + taskname;
        if (period > 0) {
            name += "(interval:" + period + ")";
        } else {
            name += "(Single)";
        }
        CustomTimingsHandler result = SpigotTimings.pluginTaskTimingMap.get(name);
        if (result == null) {
            result = new CustomTimingsHandler(name, SpigotTimings.schedulerSyncTimer);
            SpigotTimings.pluginTaskTimingMap.put(name, result);
        }
        return result;
    }

    /**
     * Get a named timer for the specified entity type to track type specific timings.
     * @param entity
     * @return
     */
    public static CustomTimingsHandler getEntityTimings(Entity entity) {
        String entityType = entity.getClass().getName();
        CustomTimingsHandler result = SpigotTimings.entityTypeTimingMap.get(entityType);
        if (result == null) {
            result = new CustomTimingsHandler("** tickEntity - " + entity.getClass().getSimpleName(), SpigotTimings.activatedEntityTimer);
            SpigotTimings.entityTypeTimingMap.put(entityType, result);
        }
        return result;
    }

    /**
     * Get a named timer for the specified tile entity type to track type specific timings.
     * @param entity
     * @return
     */
    public static CustomTimingsHandler getTileEntityTimings(BlockEntity entity) {
        String entityType = entity.getClass().getName();
        CustomTimingsHandler result = SpigotTimings.tileEntityTypeTimingMap.get(entityType);
        if (result == null) {
            result = new CustomTimingsHandler("** tickTileEntity - " + entity.getClass().getSimpleName(), SpigotTimings.tickTileEntityTimer);
            SpigotTimings.tileEntityTypeTimingMap.put(entityType, result);
        }
        return result;
    }

    /**
     * Set of timers per world, to track world specific timings.
     */
    public static class WorldTimingsHandler {
        public final CustomTimingsHandler mobSpawn;
        public final CustomTimingsHandler doChunkUnload;
        public final CustomTimingsHandler doTickPending;
        public final CustomTimingsHandler doTickTiles;
        public final CustomTimingsHandler doChunkMap;
        public final CustomTimingsHandler doSounds;
        public final CustomTimingsHandler entityTick;
        public final CustomTimingsHandler tileEntityTick;
        public final CustomTimingsHandler tileEntityPending;
        public final CustomTimingsHandler tracker;
        public final CustomTimingsHandler doTick;
        public final CustomTimingsHandler tickEntities;

        public final CustomTimingsHandler syncChunkLoadTimer;
        public final CustomTimingsHandler syncChunkLoadStructuresTimer;
        public final CustomTimingsHandler syncChunkLoadEntitiesTimer;
        public final CustomTimingsHandler syncChunkLoadTileEntitiesTimer;
        public final CustomTimingsHandler syncChunkLoadTileTicksTimer;
        public final CustomTimingsHandler syncChunkLoadPostTimer;

        public WorldTimingsHandler(Level server) {
            String name = ((PrimaryLevelData) server.levelData).getLevelName() + " - ";

            this.mobSpawn = new CustomTimingsHandler("** " + name + "mobSpawn");
            this.doChunkUnload = new CustomTimingsHandler("** " + name + "doChunkUnload");
            this.doTickPending = new CustomTimingsHandler("** " + name + "doTickPending");
            this.doTickTiles = new CustomTimingsHandler("** " + name + "doTickTiles");
            this.doChunkMap = new CustomTimingsHandler("** " + name + "doChunkMap");
            this.doSounds = new CustomTimingsHandler("** " + name + "doSounds");
            this.entityTick = new CustomTimingsHandler("** " + name + "entityTick");
            this.tileEntityTick = new CustomTimingsHandler("** " + name + "tileEntityTick");
            this.tileEntityPending = new CustomTimingsHandler("** " + name + "tileEntityPending");

            this.syncChunkLoadTimer = new CustomTimingsHandler("** " + name + "syncChunkLoad");
            this.syncChunkLoadStructuresTimer = new CustomTimingsHandler("** " + name + "chunkLoad - Structures");
            this.syncChunkLoadEntitiesTimer = new CustomTimingsHandler("** " + name + "chunkLoad - Entities");
            this.syncChunkLoadTileEntitiesTimer = new CustomTimingsHandler("** " + name + "chunkLoad - TileEntities");
            this.syncChunkLoadTileTicksTimer = new CustomTimingsHandler("** " + name + "chunkLoad - TileTicks");
            this.syncChunkLoadPostTimer = new CustomTimingsHandler("** " + name + "chunkLoad - Post");


            this.tracker = new CustomTimingsHandler(name + "tracker");
            this.doTick = new CustomTimingsHandler(name + "doTick");
            this.tickEntities = new CustomTimingsHandler(name + "tickEntities");
        }
    }
}
