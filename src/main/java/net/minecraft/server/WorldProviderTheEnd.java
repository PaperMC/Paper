package net.minecraft.server;

public class WorldProviderTheEnd extends WorldProvider {

    public WorldProviderTheEnd() {}

    public void a() {
        this.c = new WorldChunkManagerHell(BiomeBase.SKY, 0.5F, 0.0F);
        this.dimension = 1;
        this.e = true;
    }

    public IChunkProvider getChunkProvider() {
        return new ChunkProviderTheEnd(this.a, this.a.getSeed());
    }

    public float a(long i, float f) {
        return 0.0F;
    }

    public boolean c() {
        return false;
    }

    public boolean d() {
        return false;
    }

    public boolean canSpawn(int i, int j) {
        int k = this.a.b(i, j);

        return k == 0 ? false : Block.byId[k].material.isSolid();
    }

    public ChunkCoordinates e() {
        return new ChunkCoordinates(100, 50, 0);
    }

    public int getSeaLevel() {
        return 50;
    }
}
