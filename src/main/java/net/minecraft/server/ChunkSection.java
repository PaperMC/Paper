package net.minecraft.server;

import java.util.function.Predicate;
import com.destroystokyo.paper.antixray.ChunkPacketInfo; // Paper - Anti-Xray - Add chunk packet info
import javax.annotation.Nullable;

public class ChunkSection {

    public static final DataPalette<IBlockData> GLOBAL_PALETTE = new DataPaletteGlobal<>(Block.REGISTRY_ID, Blocks.AIR.getBlockData());
    final int yPos; // Paper - private -> package-private
    short nonEmptyBlockCount; // Paper - package-private
    short tickingBlockCount; // Paper - private -> package-private
    private short e;
    final DataPaletteBlock<IBlockData> blockIds; // Paper - package-private

    final com.destroystokyo.paper.util.maplist.IBlockDataList tickingList = new com.destroystokyo.paper.util.maplist.IBlockDataList(); // Paper

    // Paper start - Anti-Xray - Add parameters
    @Deprecated public ChunkSection(int i) { this(i, null, null, true); } // Notice for updates: Please make sure this constructor isn't used anywhere
    public ChunkSection(int i, IChunkAccess chunk, World world, boolean initializeBlocks) {
        this(i, (short) 0, (short) 0, (short) 0, chunk, world, initializeBlocks);
        // Paper end
    }

    // Paper start - Anti-Xray - Add parameters
    @Deprecated public ChunkSection(int i, short short0, short short1, short short2) { this(i, short0, short1, short2, null, null, true); } // Notice for updates: Please make sure this constructor isn't used anywhere
    public ChunkSection(int i, short short0, short short1, short short2, IChunkAccess chunk, World world, boolean initializeBlocks) {
        // Paper end
        this.yPos = i;
        this.nonEmptyBlockCount = short0;
        this.tickingBlockCount = short1;
        this.e = short2;
        this.blockIds = new DataPaletteBlock<>(ChunkSection.GLOBAL_PALETTE, Block.REGISTRY_ID, GameProfileSerializer::c, GameProfileSerializer::a, Blocks.AIR.getBlockData(), world == null ? null : world.chunkPacketBlockController.getPredefinedBlockData(world, chunk, this, initializeBlocks), initializeBlocks); // Paper - Anti-Xray - Add predefined block data
    }

    public final IBlockData getType(int i, int j, int k) { // Paper
        return this.blockIds.a(j << 8 | k << 4 | i); // Paper - inline
    }

    public Fluid b(int i, int j, int k) {
        return ((IBlockData) this.blockIds.a(i, j, k)).getFluid(); // Paper - diff on change - we expect this to be effectively just getType(x, y, z).getFluid(). If this changes we need to check other patches that use IBlockData#getFluid.
    }

    public void a() {
        this.blockIds.a();
    }

    public void b() {
        this.blockIds.b();
    }

    public IBlockData setType(int i, int j, int k, IBlockData iblockdata) {
        return this.setType(i, j, k, iblockdata, true);
    }

    public IBlockData setType(int i, int j, int k, IBlockData iblockdata, boolean flag) {
        IBlockData iblockdata1;

        if (flag) {
            iblockdata1 = (IBlockData) this.blockIds.setBlock(i, j, k, iblockdata);
        } else {
            iblockdata1 = (IBlockData) this.blockIds.b(i, j, k, iblockdata);
        }

        Fluid fluid = iblockdata1.getFluid();
        Fluid fluid1 = iblockdata.getFluid();

        if (!iblockdata1.isAir()) {
            --this.nonEmptyBlockCount;
            if (iblockdata1.isTicking()) {
                --this.tickingBlockCount;
                // Paper start
                this.tickingList.remove(i, j, k);
                // Paper end
            }
        }

        if (!fluid.isEmpty()) {
            --this.e;
        }

        if (!iblockdata.isAir()) {
            ++this.nonEmptyBlockCount;
            if (iblockdata.isTicking()) {
                ++this.tickingBlockCount;
                // Paper start
                this.tickingList.add(i, j, k, iblockdata);
                // Paper end
            }
        }

        if (!fluid1.isEmpty()) {
            ++this.e;
        }

        return iblockdata1;
    }

    public boolean c() {
        return this.nonEmptyBlockCount == 0;
    }

    public static boolean a(@Nullable ChunkSection chunksection) {
        return chunksection == Chunk.a || chunksection.c();
    }

    public boolean d() {
        return this.shouldTick() || this.f();
    }

    public boolean shouldTick() {
        return this.tickingBlockCount > 0;
    }

    public boolean f() {
        return this.e > 0;
    }

    public int getYPosition() {
        return this.yPos;
    }

    public void recalcBlockCounts() {
        // Paper start
        this.tickingList.clear();
        // Paper end
        this.nonEmptyBlockCount = 0;
        this.tickingBlockCount = 0;
        this.e = 0;
        this.blockIds.forEachLocation((iblockdata, location) -> { // Paper
            Fluid fluid = iblockdata.getFluid();

            if (!iblockdata.isAir()) {
                this.nonEmptyBlockCount = (short) (this.nonEmptyBlockCount + 1);
                if (iblockdata.isTicking()) {
                    this.tickingBlockCount = (short) (this.tickingBlockCount + 1);
                    // Paper start
                    this.tickingList.add(location, iblockdata);
                    // Paper end
                }
            }

            if (!fluid.isEmpty()) {
                this.nonEmptyBlockCount = (short) (this.nonEmptyBlockCount + 1);
                if (fluid.f()) {
                    this.e = (short) (this.e + 1);
                }
            }

        });
    }

    public DataPaletteBlock<IBlockData> getBlocks() {
        return this.blockIds;
    }

    // Paper start - Anti-Xray - Add chunk packet info
    @Deprecated public final void writeChunkSection(PacketDataSerializer packetDataSerializer) { this.b(packetDataSerializer); } // OBFHELPER // Notice for updates: Please make sure this method isn't used anywhere
    @Deprecated public final void b(PacketDataSerializer packetdataserializer) { this.writeChunkSection(packetdataserializer, null); } // Notice for updates: Please make sure this method isn't used anywhere
    public final void writeChunkSection(PacketDataSerializer packetDataSerializer, ChunkPacketInfo<IBlockData> chunkPacketInfo) { this.b(packetDataSerializer, chunkPacketInfo); } // OBFHELPER
    public void b(PacketDataSerializer packetdataserializer, ChunkPacketInfo<IBlockData> chunkPacketInfo) {
        // Paper end
        packetdataserializer.writeShort(this.nonEmptyBlockCount);
        this.blockIds.writeDataPaletteBlock(packetdataserializer, chunkPacketInfo, this.yPos >> 4); // Paper - Anti-Xray - Add chunk packet info
    }

    public int j() {
        return 2 + this.blockIds.c();
    }

    public boolean a(Predicate<IBlockData> predicate) {
        return this.blockIds.contains(predicate);
    }
}
