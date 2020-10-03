package net.minecraft.server;

import com.mojang.datafixers.DataFixer;
import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;
import javax.annotation.Nullable;

public class IChunkLoader implements AutoCloseable {

    private final IOWorker a;
    protected final DataFixer b;
    @Nullable
    private PersistentStructureLegacy c;

    public IChunkLoader(File file, DataFixer datafixer, boolean flag) {
        this.b = datafixer;
        this.a = new IOWorker(file, flag, "chunk");
    }

    public NBTTagCompound getChunkData(ResourceKey<World> resourcekey, Supplier<WorldPersistentData> supplier, NBTTagCompound nbttagcompound) {
        int i = a(nbttagcompound);
        boolean flag = true;

        if (i < 1493) {
            nbttagcompound = GameProfileSerializer.a(this.b, DataFixTypes.CHUNK, nbttagcompound, i, 1493);
            if (nbttagcompound.getCompound("Level").getBoolean("hasLegacyStructureData")) {
                if (this.c == null) {
                    this.c = PersistentStructureLegacy.a(resourcekey, (WorldPersistentData) supplier.get());
                }

                nbttagcompound = this.c.a(nbttagcompound);
            }
        }

        nbttagcompound = GameProfileSerializer.a(this.b, DataFixTypes.CHUNK, nbttagcompound, Math.max(1493, i));
        if (i < SharedConstants.getGameVersion().getWorldVersion()) {
            nbttagcompound.setInt("DataVersion", SharedConstants.getGameVersion().getWorldVersion());
        }

        return nbttagcompound;
    }

    public static int a(NBTTagCompound nbttagcompound) {
        return nbttagcompound.hasKeyOfType("DataVersion", 99) ? nbttagcompound.getInt("DataVersion") : -1;
    }

    @Nullable
    public NBTTagCompound read(ChunkCoordIntPair chunkcoordintpair) throws IOException {
        return this.a.a(chunkcoordintpair);
    }

    public void a(ChunkCoordIntPair chunkcoordintpair, NBTTagCompound nbttagcompound) {
        this.a.a(chunkcoordintpair, nbttagcompound);
        if (this.c != null) {
            this.c.a(chunkcoordintpair.pair());
        }

    }

    public void i() {
        this.a.a().join();
    }

    public void close() throws IOException {
        this.a.close();
    }
}
