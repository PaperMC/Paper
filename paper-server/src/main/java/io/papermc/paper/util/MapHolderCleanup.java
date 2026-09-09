package io.papermc.paper.util;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

/**
 * Removes per-player map holder state for departed players.
 *
 * <p>Vanilla only drops {@code MapItemSavedData} holder entries while a map
 * ticks for an online player, so disconnected players are retained - along
 * with their whole {@code ServerPlayer} - until the map data unloads.
 * Disconnects are collected here and swept in one batched pass, so quit
 * storms cost a single catalogue scan instead of one per player.</p>
 */
public final class MapHolderCleanup {
    private static final int SWEEP_INTERVAL_TICKS = 100; // 5 seconds
    private static final Set<UUID> pendingDepartures = ConcurrentHashMap.newKeySet();
    private static int lastSweepTick;

    private MapHolderCleanup() {
    }

    public static void onPlayerDisconnect(final UUID playerId) {
        pendingDepartures.add(playerId);
    }

    public static void tick(final MinecraftServer server) {
        if (pendingDepartures.isEmpty()) {
            return;
        }
        int tick = server.getTickCount();
        if (tick - lastSweepTick < SWEEP_INTERVAL_TICKS) {
            return;
        }
        lastSweepTick = tick;

        Set<UUID> departed = Set.copyOf(pendingDepartures);
        pendingDepartures.removeAll(departed);

        Set<UUID> offline = new HashSet<>();
        for (UUID uuid : departed) {
            if (server.getPlayerList().getPlayer(uuid) == null) {
                offline.add(uuid);
            }
        }
        if (offline.isEmpty()) {
            return;
        }

        for (ServerLevel level : server.getAllLevels()) {
            for (SavedData data : level.getDataStorage().getLoadedData()) {
                if (data instanceof MapItemSavedData mapData) {
                    mapData.removeHolders(offline);
                }
            }
        }
    }
}
