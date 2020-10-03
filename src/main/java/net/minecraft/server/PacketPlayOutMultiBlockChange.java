package net.minecraft.server;

import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortSet;
import java.io.IOException;
import java.util.function.BiConsumer;

public class PacketPlayOutMultiBlockChange implements Packet<PacketListenerPlayOut> {

    private SectionPosition a;
    private short[] b;
    private IBlockData[] c;
    private boolean d;

    public PacketPlayOutMultiBlockChange() {}

    public PacketPlayOutMultiBlockChange(SectionPosition sectionposition, ShortSet shortset, ChunkSection chunksection, boolean flag) {
        this.a = sectionposition;
        this.d = flag;
        this.a(shortset.size());
        int i = 0;

        for (ShortIterator shortiterator = shortset.iterator(); shortiterator.hasNext(); ++i) {
            short short0 = (Short) shortiterator.next();

            this.b[i] = short0;
            this.c[i] = chunksection.getType(SectionPosition.a(short0), SectionPosition.b(short0), SectionPosition.c(short0));
        }

    }

    private void a(int i) {
        this.b = new short[i];
        this.c = new IBlockData[i];
    }

    @Override
    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = SectionPosition.a(packetdataserializer.readLong());
        this.d = packetdataserializer.readBoolean();
        int i = packetdataserializer.i();

        this.a(i);

        for (int j = 0; j < this.b.length; ++j) {
            long k = packetdataserializer.j();

            this.b[j] = (short) ((int) (k & 4095L));
            this.c[j] = (IBlockData) Block.REGISTRY_ID.fromId((int) (k >>> 12));
        }

    }

    @Override
    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.writeLong(this.a.s());
        packetdataserializer.writeBoolean(this.d);
        packetdataserializer.d(this.b.length);

        for (int i = 0; i < this.b.length; ++i) {
            packetdataserializer.b((long) (Block.getCombinedId(this.c[i]) << 12 | this.b[i]));
        }

    }

    public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }

    public void a(BiConsumer<BlockPosition, IBlockData> biconsumer) {
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();

        for (int i = 0; i < this.b.length; ++i) {
            short short0 = this.b[i];

            blockposition_mutableblockposition.d(this.a.d(short0), this.a.e(short0), this.a.f(short0));
            biconsumer.accept(blockposition_mutableblockposition, this.c[i]);
        }

    }
}
