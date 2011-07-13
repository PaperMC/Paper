package net.minecraft.server;

import java.io.File;
import java.util.List;
import java.util.UUID; // CraftBukkit

public interface IDataManager {

    WorldData c();

    void b();

    IChunkLoader a(WorldProvider worldprovider);

    void a(WorldData worlddata, List list);

    void a(WorldData worlddata);

    PlayerFileData d();

    void e();

    File b(String s);

    UUID getUUID(); // CraftBukkit
}
