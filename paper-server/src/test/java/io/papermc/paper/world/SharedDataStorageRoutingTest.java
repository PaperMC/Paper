package io.papermc.paper.world;

import com.mojang.datafixers.DataFixer;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapIndex;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.SavedDataStorage;
import net.minecraft.world.scores.ScoreboardSaveData;
import org.bukkit.craftbukkit.map.CraftMapView;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Answers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Normal
class SharedDataStorageRoutingTest {

    @Test
    void scoreboardSavesToSharedServerStorage() throws ReflectiveOperationException {
        final MinecraftServer server = mock(MinecraftServer.class, Answers.CALLS_REAL_METHODS);
        final ServerScoreboard scoreboard = mock(ServerScoreboard.class);
        final SavedDataStorage sharedStorage = mock(SavedDataStorage.class);
        final SavedDataStorage overworldStorage = mock(SavedDataStorage.class);
        final ScoreboardSaveData sharedScoreboard = new ScoreboardSaveData(ScoreboardSaveData.Packed.EMPTY);
        final ScoreboardSaveData overworldScoreboard = new ScoreboardSaveData(ScoreboardSaveData.Packed.EMPTY);
        final ServerLevel overworld = mock(ServerLevel.class);

        when(sharedStorage.computeIfAbsent(ScoreboardSaveData.TYPE)).thenReturn(sharedScoreboard);
        when(overworldStorage.computeIfAbsent(ScoreboardSaveData.TYPE)).thenReturn(overworldScoreboard);
        when(overworld.getDataStorage()).thenReturn(overworldStorage);

        setField(server, MinecraftServer.class, "savedDataStorage", sharedStorage);
        setField(server, MinecraftServer.class, "scoreboard", scoreboard);
        setField(server, MinecraftServer.class, "levels", Map.of(Level.OVERWORLD, overworld));

        assertTrue(server.saveAllChunks(true, false, false));
        verify(scoreboard).storeToSaveDataIfDirty(sharedScoreboard);
        verify(scoreboard, never()).storeToSaveDataIfDirty(overworldScoreboard);
    }

    @Test
    void mapReadsUseSharedServerStorage(@TempDir final Path tempDir) throws Exception {
        final SavedDataStorage sharedStorage = createStorage(tempDir.resolve("shared"));
        final SavedDataStorage overworldStorage = createStorage(tempDir.resolve("overworld"));
        final MinecraftServer server = mock(MinecraftServer.class);
        final ServerLevel level = mock(ServerLevel.class, Answers.CALLS_REAL_METHODS);
        final MapId id = new MapId(3);
        final MapItemSavedData expected = createMapData();

        sharedStorage.set(MapItemSavedData.type(id), expected);
        when(server.getDataStorage()).thenReturn(sharedStorage);
        setField(level, ServerLevel.class, "server", server);

        assertSame(expected, level.getMapData(id));
        assertNull(overworldStorage.get(MapItemSavedData.type(id)));
    }

    @Test
    void mapWritesUseSharedServerStorage(@TempDir final Path tempDir) throws Exception {
        final SavedDataStorage sharedStorage = createStorage(tempDir.resolve("shared"));
        final SavedDataStorage overworldStorage = createStorage(tempDir.resolve("overworld"));
        final MinecraftServer server = mock(MinecraftServer.class);
        final ServerLevel level = mock(ServerLevel.class, Answers.CALLS_REAL_METHODS);
        final MapId id = new MapId(9);
        final MapItemSavedData data = createMapData();

        when(server.getDataStorage()).thenReturn(sharedStorage);
        setField(level, ServerLevel.class, "server", server);

        level.setMapData(id, data);

        assertEquals(id, data.id);
        assertSame(data, sharedStorage.get(MapItemSavedData.type(id)));
        assertNull(overworldStorage.get(MapItemSavedData.type(id)));
    }

    @Test
    void mapIdsUseSharedServerStorage(@TempDir final Path tempDir) throws Exception {
        final SavedDataStorage sharedStorage = createStorage(tempDir.resolve("shared"));
        final SavedDataStorage overworldStorage = createStorage(tempDir.resolve("overworld"));
        final MinecraftServer server = mock(MinecraftServer.class);
        final ServerLevel level = mock(ServerLevel.class, Answers.CALLS_REAL_METHODS);

        when(server.getDataStorage()).thenReturn(sharedStorage);
        setField(level, ServerLevel.class, "server", server);

        assertEquals(new MapId(0), level.getFreeMapId());
        assertNotNull(sharedStorage.get(MapIndex.TYPE));
        assertNull(overworldStorage.get(MapIndex.TYPE));
    }

    private static SavedDataStorage createStorage(final Path path) throws java.io.IOException {
        Files.createDirectories(path);
        return new SavedDataStorage(path, mock(DataFixer.class), mock(HolderLookup.Provider.class));
    }

    private static MapItemSavedData createMapData() throws ReflectiveOperationException {
        final MapItemSavedData data = mock(MapItemSavedData.class);
        setField(data, MapItemSavedData.class, "mapView", new CraftMapView(data));
        return data;
    }

    private static void setField(final Object target, final Class<?> owner, final String name, final Object value) throws ReflectiveOperationException {
        final Field field = owner.getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
