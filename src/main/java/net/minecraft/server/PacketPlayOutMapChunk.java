package net.minecraft.server;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.annotation.Nullable;

public class PacketPlayOutMapChunk implements Packet<PacketListenerPlayOut> {

    private int a;
    private int b;
    private int c;
    private NBTTagCompound d;
    @Nullable
    private int[] e;
    private byte[] f;
    private List<NBTTagCompound> g;
    private boolean h;

    public PacketPlayOutMapChunk() {}

    public PacketPlayOutMapChunk(Chunk chunk, int i) {
        ChunkCoordIntPair chunkcoordintpair = chunk.getPos();

        this.a = chunkcoordintpair.x;
        this.b = chunkcoordintpair.z;
        this.h = i == 65535;
        this.d = new NBTTagCompound();
        Iterator iterator = chunk.f().iterator();

        Entry entry;

        while (iterator.hasNext()) {
            entry = (Entry) iterator.next();
            if (((HeightMap.Type) entry.getKey()).c()) {
                this.d.set(((HeightMap.Type) entry.getKey()).b(), new NBTTagLongArray(((HeightMap) entry.getValue()).a()));
            }
        }

        if (this.h) {
            this.e = chunk.getBiomeIndex().a();
        }

        this.f = new byte[this.a(chunk, i)];
        this.c = this.a(new PacketDataSerializer(this.j()), chunk, i);
        this.g = Lists.newArrayList();
        iterator = chunk.getTileEntities().entrySet().iterator();

        while (iterator.hasNext()) {
            entry = (Entry) iterator.next();
            BlockPosition blockposition = (BlockPosition) entry.getKey();
            TileEntity tileentity = (TileEntity) entry.getValue();
            int j = blockposition.getY() >> 4;

            if (this.f() || (i & 1 << j) != 0) {
                NBTTagCompound nbttagcompound = tileentity.b();

                this.g.add(nbttagcompound);
            }
        }

    }

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.readInt();
        this.b = packetdataserializer.readInt();
        this.h = packetdataserializer.readBoolean();
        this.c = packetdataserializer.i();
        this.d = packetdataserializer.l();
        if (this.h) {
            this.e = packetdataserializer.c(BiomeStorage.a);
        }

        int i = packetdataserializer.i();

        if (i > 2097152) {
            throw new RuntimeException("Chunk Packet trying to allocate too much memory on read.");
        } else {
            this.f = new byte[i];
            packetdataserializer.readBytes(this.f);
            int j = packetdataserializer.i();

            this.g = Lists.newArrayList();

            for (int k = 0; k < j; ++k) {
                this.g.add(packetdataserializer.l());
            }

        }
    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.writeInt(this.a);
        packetdataserializer.writeInt(this.b);
        packetdataserializer.writeBoolean(this.h);
        packetdataserializer.d(this.c);
        packetdataserializer.a(this.d);
        if (this.e != null) {
            packetdataserializer.a(this.e);
        }

        packetdataserializer.d(this.f.length);
        packetdataserializer.writeBytes(this.f);
        packetdataserializer.d(this.g.size());
        Iterator iterator = this.g.iterator();

        while (iterator.hasNext()) {
            NBTTagCompound nbttagcompound = (NBTTagCompound) iterator.next();

            packetdataserializer.a(nbttagcompound);
        }

    }

    public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

    private ByteBuf j() {
        ByteBuf bytebuf = Unpooled.wrappedBuffer(this.f);

        bytebuf.writerIndex(0);
        return bytebuf;
    }

    public int a(PacketDataSerializer packetdataserializer, Chunk chunk, int i) {
        int j = 0;
        ChunkSection[] achunksection = chunk.getSections();
        int k = 0;

        for (int l = achunksection.length; k < l; ++k) {
            ChunkSection chunksection = achunksection[k];

            if (chunksection != Chunk.a && (!this.f() || !chunksection.c()) && (i & 1 << k) != 0) {
                j |= 1 << k;
                chunksection.b(packetdataserializer);
            }
        }

        return j;
    }

    protected int a(Chunk chunk, int i) {
        int j = 0;
        ChunkSection[] achunksection = chunk.getSections();
        int k = 0;

        for (int l = achunksection.length; k < l; ++k) {
            ChunkSection chunksection = achunksection[k];

            if (chunksection != Chunk.a && (!this.f() || !chunksection.c()) && (i & 1 << k) != 0) {
                j += chunksection.j();
            }
        }

        return j;
    }

    public boolean f() {
        return this.h;
    }
}
