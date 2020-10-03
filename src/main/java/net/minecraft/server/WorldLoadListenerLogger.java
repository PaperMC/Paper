package net.minecraft.server;

import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldLoadListenerLogger implements WorldLoadListener {

    private static final Logger LOGGER = LogManager.getLogger();
    private final int b;
    private int c;
    private long d;
    private long e = Long.MAX_VALUE;

    public WorldLoadListenerLogger(int i) {
        int j = i * 2 + 1;

        this.b = j * j;
    }

    @Override
    public void a(ChunkCoordIntPair chunkcoordintpair) {
        this.e = SystemUtils.getMonotonicMillis();
        this.d = this.e;
    }

    @Override
    public void a(ChunkCoordIntPair chunkcoordintpair, @Nullable ChunkStatus chunkstatus) {
        if (chunkstatus == ChunkStatus.FULL) {
            ++this.c;
        }

        int i = this.c();

        if (SystemUtils.getMonotonicMillis() > this.e) {
            this.e += 500L;
            WorldLoadListenerLogger.LOGGER.info((new ChatMessage("menu.preparingSpawn", new Object[]{MathHelper.clamp(i, 0, 100)})).getString());
        }

    }

    @Override
    public void b() {
        WorldLoadListenerLogger.LOGGER.info("Time elapsed: {} ms", SystemUtils.getMonotonicMillis() - this.d);
        this.e = Long.MAX_VALUE;
    }

    public int c() {
        return MathHelper.d((float) this.c * 100.0F / (float) this.b);
    }
}
