package com.destroystokyo.paper.antixray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntSupplier;

import net.minecraft.server.*;
import org.bukkit.Bukkit;
import org.bukkit.World.Environment;

import com.destroystokyo.paper.PaperWorldConfig;

public final class ChunkPacketBlockControllerAntiXray extends ChunkPacketBlockController {

    private final Executor executor;
    private final EngineMode engineMode;
    private final int maxChunkSectionIndex;
    private final int updateRadius;
    private final IBlockData[] predefinedBlockData;
    private final IBlockData[] predefinedBlockDataStone;
    private final IBlockData[] predefinedBlockDataNetherrack;
    private final IBlockData[] predefinedBlockDataEndStone;
    private final int[] predefinedBlockDataBitsGlobal;
    private final int[] predefinedBlockDataBitsStoneGlobal;
    private final int[] predefinedBlockDataBitsNetherrackGlobal;
    private final int[] predefinedBlockDataBitsEndStoneGlobal;
    private final boolean[] solidGlobal = new boolean[Block.REGISTRY_ID.size()];
    private final boolean[] obfuscateGlobal = new boolean[Block.REGISTRY_ID.size()];
    private final ChunkSection[] emptyNearbyChunkSections = {Chunk.EMPTY_CHUNK_SECTION, Chunk.EMPTY_CHUNK_SECTION, Chunk.EMPTY_CHUNK_SECTION, Chunk.EMPTY_CHUNK_SECTION};
    private final int maxBlockYUpdatePosition;

    public ChunkPacketBlockControllerAntiXray(World world, Executor executor) {
        PaperWorldConfig paperWorldConfig = world.paperConfig;
        engineMode = paperWorldConfig.engineMode;
        maxChunkSectionIndex = paperWorldConfig.maxChunkSectionIndex;
        updateRadius = paperWorldConfig.updateRadius;

        this.executor = executor;

        List<String> toObfuscate;

        if (engineMode == EngineMode.HIDE) {
            toObfuscate = paperWorldConfig.hiddenBlocks;
            predefinedBlockData = null;
            predefinedBlockDataStone = new IBlockData[] {Blocks.STONE.getBlockData()};
            predefinedBlockDataNetherrack = new IBlockData[] {Blocks.NETHERRACK.getBlockData()};
            predefinedBlockDataEndStone = new IBlockData[] {Blocks.END_STONE.getBlockData()};
            predefinedBlockDataBitsGlobal = null;
            predefinedBlockDataBitsStoneGlobal = new int[] {ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(Blocks.STONE.getBlockData())};
            predefinedBlockDataBitsNetherrackGlobal = new int[] {ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(Blocks.NETHERRACK.getBlockData())};
            predefinedBlockDataBitsEndStoneGlobal = new int[] {ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(Blocks.END_STONE.getBlockData())};
        } else {
            toObfuscate = new ArrayList<>(paperWorldConfig.replacementBlocks);
            Set<IBlockData> predefinedBlockDataSet = new HashSet<IBlockData>();

            for (String id : paperWorldConfig.hiddenBlocks) {
                Block block = IRegistry.BLOCK.getOptional(new MinecraftKey(id)).orElse(null);

                if (block != null && !block.isTileEntity()) {
                    toObfuscate.add(id);
                    predefinedBlockDataSet.add(block.getBlockData());
                }
            }

            predefinedBlockData = predefinedBlockDataSet.size() == 0 ? new IBlockData[] {Blocks.DIAMOND_ORE.getBlockData()} : predefinedBlockDataSet.toArray(new IBlockData[0]);
            predefinedBlockDataStone = null;
            predefinedBlockDataNetherrack = null;
            predefinedBlockDataEndStone = null;
            predefinedBlockDataBitsGlobal = new int[predefinedBlockData.length];

            for (int i = 0; i < predefinedBlockData.length; i++) {
                predefinedBlockDataBitsGlobal[i] = ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(predefinedBlockData[i]);
            }

            predefinedBlockDataBitsStoneGlobal = null;
            predefinedBlockDataBitsNetherrackGlobal = null;
            predefinedBlockDataBitsEndStoneGlobal = null;
        }

        for (String id : toObfuscate) {
            Block block = IRegistry.BLOCK.getOptional(new MinecraftKey(id)).orElse(null);

            // Don't obfuscate air because air causes unnecessary block updates and causes block updates to fail in the void
            if (block != null && !block.getBlockData().isAir()) {
                obfuscateGlobal[ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(block.getBlockData())] = true;
            }
        }

        ChunkEmpty emptyChunk = new ChunkEmpty(world, new ChunkCoordIntPair(0, 0));
        BlockPosition zeroPos = new BlockPosition(0, 0, 0);

        for (int i = 0; i < solidGlobal.length; i++) {
            IBlockData blockData = ChunkSection.GLOBAL_PALETTE.getObject(i);

            if (blockData != null) {
                solidGlobal[i] = blockData.isOccluding(emptyChunk, zeroPos)
                    && blockData.getBlock() != Blocks.SPAWNER && blockData.getBlock() != Blocks.BARRIER && blockData.getBlock() != Blocks.SHULKER_BOX;
                // shulker box checks TE.
            }
        }

        this.maxBlockYUpdatePosition = (maxChunkSectionIndex + 1) * 16 + updateRadius - 1;
    }

    private int getPredefinedBlockDataLength() {
        return engineMode == EngineMode.HIDE ? 1 : predefinedBlockData.length;
    }

    @Override
    public IBlockData[] getPredefinedBlockData(World world, IChunkAccess chunk, ChunkSection chunkSection, boolean initializeBlocks) {
        // Return the block data which should be added to the data palettes so that they can be used for the obfuscation
        if (chunkSection.getYPosition() >> 4 <= maxChunkSectionIndex) {
            switch (engineMode) {
                case HIDE:
                    switch (world.getWorld().getEnvironment()) {
                        case NETHER:
                            return predefinedBlockDataNetherrack;
                        case THE_END:
                            return predefinedBlockDataEndStone;
                        default:
                            return predefinedBlockDataStone;
                    }
                default:
                    return predefinedBlockData;
            }
        }

        return null;
    }

    @Override
    public ChunkPacketInfoAntiXray getChunkPacketInfo(PacketPlayOutMapChunk packetPlayOutMapChunk, Chunk chunk, int chunkSectionSelector) {
        // Return a new instance to collect data and objects in the right state while creating the chunk packet for thread safe access later
        // Note: As of 1.14 this has to be moved later due to the chunk system.
        ChunkPacketInfoAntiXray chunkPacketInfoAntiXray = new ChunkPacketInfoAntiXray(packetPlayOutMapChunk, chunk, chunkSectionSelector, this);
        return chunkPacketInfoAntiXray;
    }

    @Override
    public void modifyBlocks(PacketPlayOutMapChunk packetPlayOutMapChunk, ChunkPacketInfo<IBlockData> chunkPacketInfo) {
        if (!Bukkit.isPrimaryThread()) {
            // plugins?
            MinecraftServer.getServer().scheduleOnMain(() -> {
                this.modifyBlocks(packetPlayOutMapChunk, chunkPacketInfo);
            });
            return;
        }

        Chunk chunk = chunkPacketInfo.getChunk();
        int x = chunk.getPos().x;
        int z = chunk.getPos().z;
        WorldServer world = (WorldServer)chunk.world;
        ((ChunkPacketInfoAntiXray) chunkPacketInfo).setNearbyChunks(
            (Chunk) world.getChunkIfLoadedImmediately(x - 1, z),
            (Chunk) world.getChunkIfLoadedImmediately(x + 1, z),
            (Chunk) world.getChunkIfLoadedImmediately(x, z - 1),
            (Chunk) world.getChunkIfLoadedImmediately(x, z + 1));

        executor.execute((ChunkPacketInfoAntiXray) chunkPacketInfo);
    }

    // Actually these fields should be variables inside the obfuscate method but in sync mode or with SingleThreadExecutor in async mode it's okay (even without ThreadLocal)
    // If an ExecutorService with multiple threads is used, ThreadLocal must be used here
    private final ThreadLocal<int[]> predefinedBlockDataBits = ThreadLocal.withInitial(() -> new int[getPredefinedBlockDataLength()]);
    private static final ThreadLocal<boolean[]> solid = ThreadLocal.withInitial(() -> new boolean[Block.REGISTRY_ID.size()]);
    private static final ThreadLocal<boolean[]> obfuscate = ThreadLocal.withInitial(() -> new boolean[Block.REGISTRY_ID.size()]);
    // These boolean arrays represent chunk layers, true means don't obfuscate, false means obfuscate
    private static final ThreadLocal<boolean[][]> current = ThreadLocal.withInitial(() -> new boolean[16][16]);
    private static final ThreadLocal<boolean[][]> next = ThreadLocal.withInitial(() -> new boolean[16][16]);
    private static final ThreadLocal<boolean[][]> nextNext = ThreadLocal.withInitial(() -> new boolean[16][16]);

    public void obfuscate(ChunkPacketInfoAntiXray chunkPacketInfoAntiXray) {
        int[] predefinedBlockDataBits = this.predefinedBlockDataBits.get();
        boolean[] solid = this.solid.get();
        boolean[] obfuscate = this.obfuscate.get();
        boolean[][] current = this.current.get();
        boolean[][] next = this.next.get();
        boolean[][] nextNext = this.nextNext.get();
        // dataBitsReader, dataBitsWriter and nearbyChunkSections could also be reused (with ThreadLocal if necessary) but it's not worth it
        DataBitsReader dataBitsReader = new DataBitsReader();
        DataBitsWriter dataBitsWriter = new DataBitsWriter();
        ChunkSection[] nearbyChunkSections = new ChunkSection[4];
        boolean[] solidTemp = null;
        boolean[] obfuscateTemp = null;
        dataBitsReader.setDataBits(chunkPacketInfoAntiXray.getData());
        dataBitsWriter.setDataBits(chunkPacketInfoAntiXray.getData());
        int numberOfBlocks = predefinedBlockDataBits.length;
        // Keep the lambda expressions as simple as possible. They are used very frequently.
        IntSupplier random = numberOfBlocks == 1 ? (() -> 0) : new IntSupplier() {
            private int state;

            {
                while ((state = ThreadLocalRandom.current().nextInt()) == 0);
            }

            @Override
            public int getAsInt() {
                // https://en.wikipedia.org/wiki/Xorshift
                state ^= state << 13;
                state ^= state >>> 17;
                state ^= state << 5;
                // https://www.pcg-random.org/posts/bounded-rands.html
                return (int) ((Integer.toUnsignedLong(state) * numberOfBlocks) >>> 32);
            }
        };

        for (int chunkSectionIndex = 0; chunkSectionIndex <= maxChunkSectionIndex; chunkSectionIndex++) {
            if (chunkPacketInfoAntiXray.isWritten(chunkSectionIndex) && chunkPacketInfoAntiXray.getPredefinedObjects(chunkSectionIndex) != null) {
                int[] predefinedBlockDataBitsTemp;

                if (chunkPacketInfoAntiXray.getDataPalette(chunkSectionIndex) == ChunkSection.GLOBAL_PALETTE) {
                    predefinedBlockDataBitsTemp = engineMode == EngineMode.HIDE ? chunkPacketInfoAntiXray.getChunk().world.getWorld().getEnvironment() == Environment.NETHER ? predefinedBlockDataBitsNetherrackGlobal : chunkPacketInfoAntiXray.getChunk().world.getWorld().getEnvironment() == Environment.THE_END ? predefinedBlockDataBitsEndStoneGlobal : predefinedBlockDataBitsStoneGlobal : predefinedBlockDataBitsGlobal;
                } else {
                    predefinedBlockDataBitsTemp = predefinedBlockDataBits;

                    for (int i = 0; i < predefinedBlockDataBitsTemp.length; i++) {
                        predefinedBlockDataBitsTemp[i] = chunkPacketInfoAntiXray.getDataPalette(chunkSectionIndex).getOrCreateIdFor(chunkPacketInfoAntiXray.getPredefinedObjects(chunkSectionIndex)[i]);
                    }
                }

                dataBitsWriter.setIndex(chunkPacketInfoAntiXray.getDataBitsIndex(chunkSectionIndex));

                // Check if the chunk section below was not obfuscated
                if (chunkSectionIndex == 0 || !chunkPacketInfoAntiXray.isWritten(chunkSectionIndex - 1) || chunkPacketInfoAntiXray.getPredefinedObjects(chunkSectionIndex - 1) == null) {
                    // If so, initialize some stuff
                    dataBitsReader.setBitsPerObject(chunkPacketInfoAntiXray.getBitsPerObject(chunkSectionIndex));
                    dataBitsReader.setIndex(chunkPacketInfoAntiXray.getDataBitsIndex(chunkSectionIndex));
                    solidTemp = readDataPalette(chunkPacketInfoAntiXray.getDataPalette(chunkSectionIndex), solid, solidGlobal);
                    obfuscateTemp = readDataPalette(chunkPacketInfoAntiXray.getDataPalette(chunkSectionIndex), obfuscate, obfuscateGlobal);
                    // Read the blocks of the upper layer of the chunk section below if it exists
                    ChunkSection belowChunkSection = null;
                    boolean skipFirstLayer = chunkSectionIndex == 0 || (belowChunkSection = chunkPacketInfoAntiXray.getChunk().getSections()[chunkSectionIndex - 1]) == Chunk.EMPTY_CHUNK_SECTION;

                    for (int z = 0; z < 16; z++) {
                        for (int x = 0; x < 16; x++) {
                            current[z][x] = true;
                            next[z][x] = skipFirstLayer || !solidGlobal[ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(belowChunkSection.getType(x, 15, z))];
                        }
                    }

                    // Abuse the obfuscateLayer method to read the blocks of the first layer of the current chunk section
                    dataBitsWriter.setBitsPerObject(0);
                    obfuscateLayer(-1, dataBitsReader, dataBitsWriter, solidTemp, obfuscateTemp, predefinedBlockDataBitsTemp, current, next, nextNext, emptyNearbyChunkSections, random);
                }

                dataBitsWriter.setBitsPerObject(chunkPacketInfoAntiXray.getBitsPerObject(chunkSectionIndex));
                nearbyChunkSections[0] = chunkPacketInfoAntiXray.getNearbyChunks()[0] == null ? Chunk.EMPTY_CHUNK_SECTION : chunkPacketInfoAntiXray.getNearbyChunks()[0].getSections()[chunkSectionIndex];
                nearbyChunkSections[1] = chunkPacketInfoAntiXray.getNearbyChunks()[1] == null ? Chunk.EMPTY_CHUNK_SECTION : chunkPacketInfoAntiXray.getNearbyChunks()[1].getSections()[chunkSectionIndex];
                nearbyChunkSections[2] = chunkPacketInfoAntiXray.getNearbyChunks()[2] == null ? Chunk.EMPTY_CHUNK_SECTION : chunkPacketInfoAntiXray.getNearbyChunks()[2].getSections()[chunkSectionIndex];
                nearbyChunkSections[3] = chunkPacketInfoAntiXray.getNearbyChunks()[3] == null ? Chunk.EMPTY_CHUNK_SECTION : chunkPacketInfoAntiXray.getNearbyChunks()[3].getSections()[chunkSectionIndex];

                // Obfuscate all layers of the current chunk section except the upper one
                for (int y = 0; y < 15; y++) {
                    boolean[][] temp = current;
                    current = next;
                    next = nextNext;
                    nextNext = temp;
                    obfuscateLayer(y, dataBitsReader, dataBitsWriter, solidTemp, obfuscateTemp, predefinedBlockDataBitsTemp, current, next, nextNext, nearbyChunkSections, random);
                }

                // Check if the chunk section above doesn't need obfuscation
                if (chunkSectionIndex == maxChunkSectionIndex || !chunkPacketInfoAntiXray.isWritten(chunkSectionIndex + 1) || chunkPacketInfoAntiXray.getPredefinedObjects(chunkSectionIndex + 1) == null) {
                    // If so, obfuscate the upper layer of the current chunk section by reading blocks of the first layer from the chunk section above if it exists
                    ChunkSection aboveChunkSection;

                    if (chunkSectionIndex != 15 && (aboveChunkSection = chunkPacketInfoAntiXray.getChunk().getSections()[chunkSectionIndex + 1]) != Chunk.EMPTY_CHUNK_SECTION) {
                        boolean[][] temp = current;
                        current = next;
                        next = nextNext;
                        nextNext = temp;

                        for (int z = 0; z < 16; z++) {
                            for (int x = 0; x < 16; x++) {
                                if (!solidGlobal[ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(aboveChunkSection.getType(x, 0, z))]) {
                                    current[z][x] = true;
                                }
                            }
                        }

                        // There is nothing to read anymore
                        dataBitsReader.setBitsPerObject(0);
                        solid[0] = true;
                        obfuscateLayer(15, dataBitsReader, dataBitsWriter, solid, obfuscateTemp, predefinedBlockDataBitsTemp, current, next, nextNext, nearbyChunkSections, random);
                    }
                } else {
                    // If not, initialize the reader and other stuff for the chunk section above to obfuscate the upper layer of the current chunk section
                    dataBitsReader.setBitsPerObject(chunkPacketInfoAntiXray.getBitsPerObject(chunkSectionIndex + 1));
                    dataBitsReader.setIndex(chunkPacketInfoAntiXray.getDataBitsIndex(chunkSectionIndex + 1));
                    solidTemp = readDataPalette(chunkPacketInfoAntiXray.getDataPalette(chunkSectionIndex + 1), solid, solidGlobal);
                    obfuscateTemp = readDataPalette(chunkPacketInfoAntiXray.getDataPalette(chunkSectionIndex + 1), obfuscate, obfuscateGlobal);
                    boolean[][] temp = current;
                    current = next;
                    next = nextNext;
                    nextNext = temp;
                    obfuscateLayer(15, dataBitsReader, dataBitsWriter, solidTemp, obfuscateTemp, predefinedBlockDataBitsTemp, current, next, nextNext, nearbyChunkSections, random);
                }

                dataBitsWriter.finish();
            }
        }

        chunkPacketInfoAntiXray.getPacketPlayOutMapChunk().setReady(true);
    }

    private void obfuscateLayer(int y, DataBitsReader dataBitsReader, DataBitsWriter dataBitsWriter, boolean[] solid, boolean[] obfuscate, int[] predefinedBlockDataBits, boolean[][] current, boolean[][] next, boolean[][] nextNext, ChunkSection[] nearbyChunkSections, IntSupplier random) {
        // First block of first line
        int dataBits = dataBitsReader.read();

        if (nextNext[0][0] = !solid[dataBits]) {
            dataBitsWriter.skip();
            next[0][1] = true;
            next[1][0] = true;
        } else {
            if (nearbyChunkSections[2] == Chunk.EMPTY_CHUNK_SECTION || !solidGlobal[ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(nearbyChunkSections[2].getType(0, y, 15))] || nearbyChunkSections[0] == Chunk.EMPTY_CHUNK_SECTION || !solidGlobal[ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(nearbyChunkSections[0].getType(15, y, 0))] || current[0][0]) {
                dataBitsWriter.skip();
            } else {
                dataBitsWriter.write(predefinedBlockDataBits[random.getAsInt()]);
            }
        }

        if (!obfuscate[dataBits]) {
            next[0][0] = true;
        }

        // First line
        for (int x = 1; x < 15; x++) {
            dataBits = dataBitsReader.read();

            if (nextNext[0][x] = !solid[dataBits]) {
                dataBitsWriter.skip();
                next[0][x - 1] = true;
                next[0][x + 1] = true;
                next[1][x] = true;
            } else {
                if (nearbyChunkSections[2] == Chunk.EMPTY_CHUNK_SECTION || !solidGlobal[ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(nearbyChunkSections[2].getType(x, y, 15))] || current[0][x]) {
                    dataBitsWriter.skip();
                } else {
                    dataBitsWriter.write(predefinedBlockDataBits[random.getAsInt()]);
                }
            }

            if (!obfuscate[dataBits]) {
                next[0][x] = true;
            }
        }

        // Last block of first line
        dataBits = dataBitsReader.read();

        if (nextNext[0][15] = !solid[dataBits]) {
            dataBitsWriter.skip();
            next[0][14] = true;
            next[1][15] = true;
        } else {
            if (nearbyChunkSections[2] == Chunk.EMPTY_CHUNK_SECTION || !solidGlobal[ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(nearbyChunkSections[2].getType(15, y, 15))] || nearbyChunkSections[1] == Chunk.EMPTY_CHUNK_SECTION || !solidGlobal[ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(nearbyChunkSections[1].getType(0, y, 0))] || current[0][15]) {
                dataBitsWriter.skip();
            } else {
                dataBitsWriter.write(predefinedBlockDataBits[random.getAsInt()]);
            }
        }

        if (!obfuscate[dataBits]) {
            next[0][15] = true;
        }

        // All inner lines
        for (int z = 1; z < 15; z++) {
            // First block
            dataBits = dataBitsReader.read();

            if (nextNext[z][0] = !solid[dataBits]) {
                dataBitsWriter.skip();
                next[z][1] = true;
                next[z - 1][0] = true;
                next[z + 1][0] = true;
            } else {
                if (nearbyChunkSections[0] == Chunk.EMPTY_CHUNK_SECTION || !solidGlobal[ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(nearbyChunkSections[0].getType(15, y, z))] || current[z][0]) {
                    dataBitsWriter.skip();
                } else {
                    dataBitsWriter.write(predefinedBlockDataBits[random.getAsInt()]);
                }
            }

            if (!obfuscate[dataBits]) {
                next[z][0] = true;
            }

            // All inner blocks
            for (int x = 1; x < 15; x++) {
                dataBits = dataBitsReader.read();

                if (nextNext[z][x] = !solid[dataBits]) {
                    dataBitsWriter.skip();
                    next[z][x - 1] = true;
                    next[z][x + 1] = true;
                    next[z - 1][x] = true;
                    next[z + 1][x] = true;
                } else {
                    if (current[z][x]) {
                        dataBitsWriter.skip();
                    } else {
                        dataBitsWriter.write(predefinedBlockDataBits[random.getAsInt()]);
                    }
                }

                if (!obfuscate[dataBits]) {
                    next[z][x] = true;
                }
            }

            // Last block
            dataBits = dataBitsReader.read();

            if (nextNext[z][15] = !solid[dataBits]) {
                dataBitsWriter.skip();
                next[z][14] = true;
                next[z - 1][15] = true;
                next[z + 1][15] = true;
            } else {
                if (nearbyChunkSections[1] == Chunk.EMPTY_CHUNK_SECTION || !solidGlobal[ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(nearbyChunkSections[1].getType(0, y, z))] || current[z][15]) {
                    dataBitsWriter.skip();
                } else {
                    dataBitsWriter.write(predefinedBlockDataBits[random.getAsInt()]);
                }
            }

            if (!obfuscate[dataBits]) {
                next[z][15] = true;
            }
        }

        // First block of last line
        dataBits = dataBitsReader.read();

        if (nextNext[15][0] = !solid[dataBits]) {
            dataBitsWriter.skip();
            next[15][1] = true;
            next[14][0] = true;
        } else {
            if (nearbyChunkSections[3] == Chunk.EMPTY_CHUNK_SECTION || !solidGlobal[ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(nearbyChunkSections[3].getType(0, y, 0))] || nearbyChunkSections[0] == Chunk.EMPTY_CHUNK_SECTION || !solidGlobal[ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(nearbyChunkSections[0].getType(15, y, 15))] || current[15][0]) {
                dataBitsWriter.skip();
            } else {
                dataBitsWriter.write(predefinedBlockDataBits[random.getAsInt()]);
            }
        }

        if (!obfuscate[dataBits]) {
            next[15][0] = true;
        }

        // Last line
        for (int x = 1; x < 15; x++) {
            dataBits = dataBitsReader.read();

            if (nextNext[15][x] = !solid[dataBits]) {
                dataBitsWriter.skip();
                next[15][x - 1] = true;
                next[15][x + 1] = true;
                next[14][x] = true;
            } else {
                if (nearbyChunkSections[3] == Chunk.EMPTY_CHUNK_SECTION || !solidGlobal[ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(nearbyChunkSections[3].getType(x, y, 0))] || current[15][x]) {
                    dataBitsWriter.skip();
                } else {
                    dataBitsWriter.write(predefinedBlockDataBits[random.getAsInt()]);
                }
            }

            if (!obfuscate[dataBits]) {
                next[15][x] = true;
            }
        }

        // Last block of last line
        dataBits = dataBitsReader.read();

        if (nextNext[15][15] = !solid[dataBits]) {
            dataBitsWriter.skip();
            next[15][14] = true;
            next[14][15] = true;
        } else {
            if (nearbyChunkSections[3] == Chunk.EMPTY_CHUNK_SECTION || !solidGlobal[ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(nearbyChunkSections[3].getType(15, y, 0))] || nearbyChunkSections[1] == Chunk.EMPTY_CHUNK_SECTION || !solidGlobal[ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(nearbyChunkSections[1].getType(0, y, 15))] || current[15][15]) {
                dataBitsWriter.skip();
            } else {
                dataBitsWriter.write(predefinedBlockDataBits[random.getAsInt()]);
            }
        }

        if (!obfuscate[dataBits]) {
            next[15][15] = true;
        }
    }

    private boolean[] readDataPalette(DataPalette<IBlockData> dataPalette, boolean[] temp, boolean[] global) {
        if (dataPalette == ChunkSection.GLOBAL_PALETTE) {
            return global;
        }

        IBlockData blockData;

        for (int i = 0; (blockData = dataPalette.getObject(i)) != null; i++) {
            temp[i] = global[ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(blockData)];
        }

        return temp;
    }

    @Override
    public void onBlockChange(World world, BlockPosition blockPosition, IBlockData newBlockData, IBlockData oldBlockData, int flag) {
        if (oldBlockData != null && solidGlobal[ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(oldBlockData)] && !solidGlobal[ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(newBlockData)] && blockPosition.getY() <= maxBlockYUpdatePosition) {
            updateNearbyBlocks(world, blockPosition);
        }
    }

    @Override
    public void onPlayerLeftClickBlock(PlayerInteractManager playerInteractManager, BlockPosition blockPosition, EnumDirection enumDirection) {
        if (blockPosition.getY() <= maxBlockYUpdatePosition) {
            updateNearbyBlocks(playerInteractManager.world, blockPosition);
        }
    }

    private void updateNearbyBlocks(World world, BlockPosition blockPosition) {
        if (updateRadius >= 2) {
            BlockPosition temp = blockPosition.west();
            updateBlock(world, temp);
            updateBlock(world, temp.west());
            updateBlock(world, temp.down());
            updateBlock(world, temp.up());
            updateBlock(world, temp.north());
            updateBlock(world, temp.south());
            updateBlock(world, temp = blockPosition.east());
            updateBlock(world, temp.east());
            updateBlock(world, temp.down());
            updateBlock(world, temp.up());
            updateBlock(world, temp.north());
            updateBlock(world, temp.south());
            updateBlock(world, temp = blockPosition.down());
            updateBlock(world, temp.down());
            updateBlock(world, temp.north());
            updateBlock(world, temp.south());
            updateBlock(world, temp = blockPosition.up());
            updateBlock(world, temp.up());
            updateBlock(world, temp.north());
            updateBlock(world, temp.south());
            updateBlock(world, temp = blockPosition.north());
            updateBlock(world, temp.north());
            updateBlock(world, temp = blockPosition.south());
            updateBlock(world, temp.south());
        } else if (updateRadius == 1) {
            updateBlock(world, blockPosition.west());
            updateBlock(world, blockPosition.east());
            updateBlock(world, blockPosition.down());
            updateBlock(world, blockPosition.up());
            updateBlock(world, blockPosition.north());
            updateBlock(world, blockPosition.south());
        } else {
            // Do nothing if updateRadius <= 0 (test mode)
        }
    }

    private void updateBlock(World world, BlockPosition blockPosition) {
        IBlockData blockData = world.getTypeIfLoaded(blockPosition);

        if (blockData != null && obfuscateGlobal[ChunkSection.GLOBAL_PALETTE.getOrCreateIdFor(blockData)]) {
            // world.notify(blockPosition, blockData, blockData, 3);
            ((WorldServer)world).getChunkProvider().flagDirty(blockPosition); // We only need to re-send to client
        }
    }

    public enum EngineMode {

        HIDE(1, "hide ores"),
        OBFUSCATE(2, "obfuscate");

        private final int id;
        private final String description;

        EngineMode(int id, String description) {
            this.id = id;
            this.description = description;
        }

        public static EngineMode getById(int id) {
            for (EngineMode engineMode : values()) {
                if (engineMode.id == id) {
                    return engineMode;
                }
            }

            return null;
        }

        public int getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }
    }
}
