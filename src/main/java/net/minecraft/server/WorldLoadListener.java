package net.minecraft.server;

import javax.annotation.Nullable;

public interface WorldLoadListener {

    void a(ChunkCoordIntPair chunkcoordintpair);

    void a(ChunkCoordIntPair chunkcoordintpair, @Nullable ChunkStatus chunkstatus);

    void b();
}
