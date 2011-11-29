package net.minecraft.server;

import java.io.File;
import java.util.List;
import java.util.UUID; // CraftBukkit

public interface IDataManager {

    WorldData getWorldData();

    void checkSession();

    IChunkLoader createChunkLoader(WorldProvider worldprovider);

    void saveWorldData(WorldData worlddata, List list);

    void saveWorldData(WorldData worlddata);

    PlayerFileData getPlayerFileData();

    void e();

    File getDataFile(String s);

    UUID getUUID(); // CraftBukkit
}
