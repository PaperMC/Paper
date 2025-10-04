package io.papermc.paper.antixray;

import io.papermc.paper.configuration.WorldConfiguration;
import io.papermc.paper.configuration.type.EngineMode;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.level.chunk.GlobalPalette;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.MissingPaletteEntryException;
import net.minecraft.world.level.chunk.Palette;
import net.minecraft.world.level.chunk.PaletteResize;
import org.bukkit.Bukkit;

public final class ChunkPacketBlockControllerAntiXray extends ChunkPacketBlockController {

    private static final Palette<BlockState> GLOBAL_BLOCKSTATE_PALETTE = new GlobalPalette<>(Block.BLOCK_STATE_REGISTRY);
    private static final LevelChunkSection EMPTY_SECTION = null;
    private final Executor executor;
    private final EngineMode engineMode;
    private final int maxBlockHeight;
    private final int updateRadius;
    private final boolean usePermission;
    private final BlockState[] presetBlockStates;
    private final BlockState[] presetBlockStatesFull;
    private final BlockState[] presetBlockStatesStone;
    private final BlockState[] presetBlockStatesDeepslate;
    private final BlockState[] presetBlockStatesNetherrack;
    private final BlockState[] presetBlockStatesEndStone;
    private final int[] presetBlockStateBitsGlobal;
    private final int[] presetBlockStateBitsStoneGlobal;
    private final int[] presetBlockStateBitsDeepslateGlobal;
    private final int[] presetBlockStateBitsNetherrackGlobal;
    private final int[] presetBlockStateBitsEndStoneGlobal;
    private final boolean[] solidGlobal = new boolean[Block.BLOCK_STATE_REGISTRY.size()];
    private final boolean[] obfuscateGlobal = new boolean[Block.BLOCK_STATE_REGISTRY.size()];
    private final LevelChunkSection[] emptyNearbyChunkSections = {EMPTY_SECTION, EMPTY_SECTION, EMPTY_SECTION, EMPTY_SECTION};
    private final int maxBlockHeightUpdatePosition;

    public ChunkPacketBlockControllerAntiXray(Level level, Executor executor) {
        this.executor = executor;
        WorldConfiguration.Anticheat.AntiXray paperWorldConfig = level.paperConfig().anticheat.antiXray;
        engineMode = paperWorldConfig.engineMode;
        maxBlockHeight = paperWorldConfig.maxBlockHeight >> 4 << 4;
        updateRadius = paperWorldConfig.updateRadius;
        usePermission = paperWorldConfig.usePermission;
        List<Block> toObfuscate;

        if (engineMode == EngineMode.HIDE) {
            toObfuscate = paperWorldConfig.hiddenBlocks;
            presetBlockStates = null;
            presetBlockStatesFull = null;
            presetBlockStatesStone = new BlockState[]{Blocks.STONE.defaultBlockState()};
            presetBlockStatesDeepslate = new BlockState[]{Blocks.DEEPSLATE.defaultBlockState()};
            presetBlockStatesNetherrack = new BlockState[]{Blocks.NETHERRACK.defaultBlockState()};
            presetBlockStatesEndStone = new BlockState[]{Blocks.END_STONE.defaultBlockState()};
            presetBlockStateBitsGlobal = null;
            presetBlockStateBitsStoneGlobal = new int[]{GLOBAL_BLOCKSTATE_PALETTE.idFor(Blocks.STONE.defaultBlockState(), PaletteResize.noResizeExpected())};
            presetBlockStateBitsDeepslateGlobal = new int[]{GLOBAL_BLOCKSTATE_PALETTE.idFor(Blocks.DEEPSLATE.defaultBlockState(), PaletteResize.noResizeExpected())};
            presetBlockStateBitsNetherrackGlobal = new int[]{GLOBAL_BLOCKSTATE_PALETTE.idFor(Blocks.NETHERRACK.defaultBlockState(), PaletteResize.noResizeExpected())};
            presetBlockStateBitsEndStoneGlobal = new int[]{GLOBAL_BLOCKSTATE_PALETTE.idFor(Blocks.END_STONE.defaultBlockState(), PaletteResize.noResizeExpected())};
        } else {
            toObfuscate = new ArrayList<>(paperWorldConfig.replacementBlocks);
            List<BlockState> presetBlockStateList = new LinkedList<>();

            for (Block block : paperWorldConfig.hiddenBlocks) {

                if (!(block instanceof EntityBlock)) {
                    toObfuscate.add(block);
                    presetBlockStateList.add(block.defaultBlockState());
                }
            }

            // The doc of the LinkedHashSet(Collection<? extends E>) constructor doesn't specify that the insertion order is the predictable iteration order of the specified Collection, although it is in the implementation
            Set<BlockState> presetBlockStateSet = new LinkedHashSet<>();
            // Therefore addAll(Collection<? extends E>) is used, which guarantees this order in the doc
            presetBlockStateSet.addAll(presetBlockStateList);
            presetBlockStates = presetBlockStateSet.isEmpty() ? new BlockState[]{Blocks.DIAMOND_ORE.defaultBlockState()} : presetBlockStateSet.toArray(new BlockState[0]);
            presetBlockStatesFull = presetBlockStateSet.isEmpty() ? new BlockState[]{Blocks.DIAMOND_ORE.defaultBlockState()} : presetBlockStateList.toArray(new BlockState[0]);
            presetBlockStatesStone = null;
            presetBlockStatesDeepslate = null;
            presetBlockStatesNetherrack = null;
            presetBlockStatesEndStone = null;
            presetBlockStateBitsGlobal = new int[presetBlockStatesFull.length];

            for (int i = 0; i < presetBlockStatesFull.length; i++) {
                presetBlockStateBitsGlobal[i] = GLOBAL_BLOCKSTATE_PALETTE.idFor(presetBlockStatesFull[i], PaletteResize.noResizeExpected());
            }

            presetBlockStateBitsStoneGlobal = null;
            presetBlockStateBitsDeepslateGlobal = null;
            presetBlockStateBitsNetherrackGlobal = null;
            presetBlockStateBitsEndStoneGlobal = null;
        }

        for (Block block : toObfuscate) {

            // Don't obfuscate air because air causes unnecessary block updates and causes block updates to fail in the void
            if (block != null && !block.defaultBlockState().isAir()) {
                // Replace all block states of a specified block
                for (BlockState blockState : block.getStateDefinition().getPossibleStates()) {
                    obfuscateGlobal[GLOBAL_BLOCKSTATE_PALETTE.idFor(blockState, PaletteResize.noResizeExpected())] = true;
                }
            }
        }

        EmptyLevelChunk emptyChunk = new EmptyLevelChunk(level, new ChunkPos(0, 0), MinecraftServer.getServer().registryAccess().lookupOrThrow(Registries.BIOME).getOrThrow(Biomes.PLAINS));
        BlockPos zeroPos = new BlockPos(0, 0, 0);

        for (int i = 0; i < solidGlobal.length; i++) {
            BlockState blockState = GLOBAL_BLOCKSTATE_PALETTE.valueFor(i);

            if (blockState != null) {
                solidGlobal[i] = blockState.isRedstoneConductor(emptyChunk, zeroPos)
                    && !blockState.is(Blocks.SPAWNER) && !blockState.is(Blocks.BARRIER) && !blockState.is(Blocks.SHULKER_BOX) && !blockState.is(Blocks.SLIME_BLOCK) && !blockState.is(Blocks.MANGROVE_ROOTS) || paperWorldConfig.lavaObscures && blockState == Blocks.LAVA.defaultBlockState();
                // Comparing blockState == Blocks.LAVA.defaultBlockState() instead of blockState.is(Blocks.LAVA) ensures that only "stationary lava" is used
                // shulker box checks TE.
            }
        }

        maxBlockHeightUpdatePosition = maxBlockHeight + updateRadius - 1;
    }

    private int getPresetBlockStatesFullLength() {
        return engineMode == EngineMode.HIDE ? 1 : presetBlockStatesFull.length;
    }

    @Override
    public BlockState[] getPresetBlockStates(Level level, ChunkPos chunkPos, int chunkSectionY) {
        // Return the block states to be added to the paletted containers so that they can be used for obfuscation
        int bottomBlockY = chunkSectionY << 4;

        if (bottomBlockY < maxBlockHeight) {
            if (engineMode == EngineMode.HIDE) {
                return switch (level.getWorld().getEnvironment()) {
                    case NETHER -> presetBlockStatesNetherrack;
                    case THE_END -> presetBlockStatesEndStone;
                    default -> bottomBlockY < 0 ? presetBlockStatesDeepslate : presetBlockStatesStone;
                };
            }

            return presetBlockStates;
        }

        return null;
    }

    @Override
    public boolean shouldModify(ServerPlayer player, LevelChunk chunk) {
        return !usePermission || !player.getBukkitEntity().hasPermission("paper.antixray.bypass");
    }

    @Override
    public ChunkPacketInfoAntiXray getChunkPacketInfo(ClientboundLevelChunkWithLightPacket chunkPacket, LevelChunk chunk) {
        // Return a new instance to collect data and objects in the right state while creating the chunk packet for thread safe access later
        return new ChunkPacketInfoAntiXray(chunkPacket, chunk, this);
    }

    @Override
    public void modifyBlocks(ClientboundLevelChunkWithLightPacket chunkPacket, ChunkPacketInfo<BlockState> chunkPacketInfo) {
        if (!(chunkPacketInfo instanceof ChunkPacketInfoAntiXray)) {
            chunkPacket.setReady(true);
            return;
        }

        if (!Bukkit.isPrimaryThread()) {
            // Plugins?
            MinecraftServer.getServer().scheduleOnMain(() -> modifyBlocks(chunkPacket, chunkPacketInfo));
            return;
        }

        LevelChunk chunk = chunkPacketInfo.getChunk();
        int x = chunk.getPos().x;
        int z = chunk.getPos().z;
        Level level = chunk.getLevel();
        ((ChunkPacketInfoAntiXray) chunkPacketInfo).setNearbyChunks(level.getChunkIfLoaded(x - 1, z), level.getChunkIfLoaded(x + 1, z), level.getChunkIfLoaded(x, z - 1), level.getChunkIfLoaded(x, z + 1));
        executor.execute((Runnable) chunkPacketInfo);
    }

    // Actually these fields should be variables inside the obfuscate method but in sync mode or with SingleThreadExecutor in async mode it's okay (even without ThreadLocal)
    // If an ExecutorService with multiple threads is used, ThreadLocal must be used here
    private final ThreadLocal<int[]> presetBlockStateBits = ThreadLocal.withInitial(() -> new int[getPresetBlockStatesFullLength()]);
    private static final ThreadLocal<boolean[]> SOLID = ThreadLocal.withInitial(() -> new boolean[Block.BLOCK_STATE_REGISTRY.size()]);
    private static final ThreadLocal<boolean[]> OBFUSCATE = ThreadLocal.withInitial(() -> new boolean[Block.BLOCK_STATE_REGISTRY.size()]);
    // These boolean arrays represent chunk layers, true means don't obfuscate, false means obfuscate
    private static final ThreadLocal<boolean[][]> CURRENT = ThreadLocal.withInitial(() -> new boolean[16][16]);
    private static final ThreadLocal<boolean[][]> NEXT = ThreadLocal.withInitial(() -> new boolean[16][16]);
    private static final ThreadLocal<boolean[][]> NEXT_NEXT = ThreadLocal.withInitial(() -> new boolean[16][16]);

    public void obfuscate(ChunkPacketInfoAntiXray chunkPacketInfoAntiXray) {
        int[] presetBlockStateBits = this.presetBlockStateBits.get();
        boolean[] solid = SOLID.get();
        boolean[] obfuscate = OBFUSCATE.get();
        boolean[][] current = CURRENT.get();
        boolean[][] next = NEXT.get();
        boolean[][] nextNext = NEXT_NEXT.get();
        // bitStorageReader, bitStorageWriter and nearbyChunkSections could also be reused (with ThreadLocal if necessary) but it's not worth it
        BitStorageReader bitStorageReader = new BitStorageReader();
        BitStorageWriter bitStorageWriter = new BitStorageWriter();
        LevelChunkSection[] nearbyChunkSections = new LevelChunkSection[4];
        LevelChunk chunk = chunkPacketInfoAntiXray.getChunk();
        Level level = chunk.getLevel();
        int maxChunkSectionIndex = Math.min((maxBlockHeight >> 4) - chunk.getMinSectionY(), chunk.getSectionsCount()) - 1;
        boolean[] solidTemp = null;
        boolean[] obfuscateTemp = null;
        bitStorageReader.setBuffer(chunkPacketInfoAntiXray.getBuffer());
        bitStorageWriter.setBuffer(chunkPacketInfoAntiXray.getBuffer());
        int numberOfBlocks = presetBlockStateBits.length;
        // Keep the lambda expressions as simple as possible. They are used very frequently.
        LayeredIntSupplier random = numberOfBlocks == 1 ? (() -> 0) : engineMode == EngineMode.OBFUSCATE_LAYER ? new LayeredIntSupplier() {
            // engine-mode: 3
            private int state;
            private int next;

            {
                while ((state = ThreadLocalRandom.current().nextInt()) == 0) ;
            }

            @Override
            public void nextLayer() {
                // https://en.wikipedia.org/wiki/Xorshift
                state ^= state << 13;
                state ^= state >>> 17;
                state ^= state << 5;
                // https://www.pcg-random.org/posts/bounded-rands.html
                next = (int) ((Integer.toUnsignedLong(state) * numberOfBlocks) >>> 32);
            }

            @Override
            public int getAsInt() {
                return next;
            }
        } : new LayeredIntSupplier() {
            // engine-mode: 2
            private int state;

            {
                while ((state = ThreadLocalRandom.current().nextInt()) == 0) ;
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
            if (chunkPacketInfoAntiXray.isWritten(chunkSectionIndex) && chunkPacketInfoAntiXray.getPresetValues(chunkSectionIndex) != null) {
                int[] presetBlockStateBitsTemp;

                if (chunkPacketInfoAntiXray.getPalette(chunkSectionIndex) instanceof GlobalPalette) {
                    if (engineMode == EngineMode.HIDE) {
                        presetBlockStateBitsTemp = switch (level.getWorld().getEnvironment()) {
                            case NETHER -> presetBlockStateBitsNetherrackGlobal;
                            case THE_END -> presetBlockStateBitsEndStoneGlobal;
                            default -> chunkSectionIndex + chunk.getMinSectionY() < 0 ? presetBlockStateBitsDeepslateGlobal : presetBlockStateBitsStoneGlobal;
                        };
                    } else {
                        presetBlockStateBitsTemp = presetBlockStateBitsGlobal;
                    }
                } else {
                    // If it's presetBlockStates, use this.presetBlockStatesFull instead
                    BlockState[] presetBlockStatesFull = chunkPacketInfoAntiXray.getPresetValues(chunkSectionIndex) == presetBlockStates ? this.presetBlockStatesFull : chunkPacketInfoAntiXray.getPresetValues(chunkSectionIndex);
                    presetBlockStateBitsTemp = presetBlockStateBits;

                    for (int i = 0; i < presetBlockStateBitsTemp.length; i++) {
                        // This is thread safe because we only request IDs that are guaranteed to be in the palette and are visible
                        // For more details see the comments in the readPalette method
                        presetBlockStateBitsTemp[i] = chunkPacketInfoAntiXray.getPalette(chunkSectionIndex).idFor(presetBlockStatesFull[i], PaletteResize.noResizeExpected());
                    }
                }

                bitStorageWriter.setIndex(chunkPacketInfoAntiXray.getIndex(chunkSectionIndex));

                // Check if the chunk section below was not obfuscated
                if (chunkSectionIndex == 0 || !chunkPacketInfoAntiXray.isWritten(chunkSectionIndex - 1) || chunkPacketInfoAntiXray.getPresetValues(chunkSectionIndex - 1) == null) {
                    // If so, initialize some stuff
                    bitStorageReader.setBits(chunkPacketInfoAntiXray.getBits(chunkSectionIndex));
                    bitStorageReader.setIndex(chunkPacketInfoAntiXray.getIndex(chunkSectionIndex));
                    solidTemp = readPalette(chunkPacketInfoAntiXray.getPalette(chunkSectionIndex), solid, solidGlobal);
                    obfuscateTemp = readPalette(chunkPacketInfoAntiXray.getPalette(chunkSectionIndex), obfuscate, obfuscateGlobal);
                    // Read the blocks of the upper layer of the chunk section below if it exists
                    LevelChunkSection belowChunkSection = null;
                    boolean skipFirstLayer = chunkSectionIndex == 0 || (belowChunkSection = chunk.getSections()[chunkSectionIndex - 1]) == EMPTY_SECTION;

                    for (int z = 0; z < 16; z++) {
                        for (int x = 0; x < 16; x++) {
                            current[z][x] = true;
                            next[z][x] = skipFirstLayer || isTransparent(belowChunkSection, x, 15, z);
                        }
                    }

                    // Abuse the obfuscateLayer method to read the blocks of the first layer of the current chunk section
                    bitStorageWriter.setBits(0);
                    obfuscateLayer(-1, bitStorageReader, bitStorageWriter, solidTemp, obfuscateTemp, presetBlockStateBitsTemp, current, next, nextNext, emptyNearbyChunkSections, random);
                }

                bitStorageWriter.setBits(chunkPacketInfoAntiXray.getBits(chunkSectionIndex));
                nearbyChunkSections[0] = chunkPacketInfoAntiXray.getNearbyChunks()[0] == null ? EMPTY_SECTION : chunkPacketInfoAntiXray.getNearbyChunks()[0].getSections()[chunkSectionIndex];
                nearbyChunkSections[1] = chunkPacketInfoAntiXray.getNearbyChunks()[1] == null ? EMPTY_SECTION : chunkPacketInfoAntiXray.getNearbyChunks()[1].getSections()[chunkSectionIndex];
                nearbyChunkSections[2] = chunkPacketInfoAntiXray.getNearbyChunks()[2] == null ? EMPTY_SECTION : chunkPacketInfoAntiXray.getNearbyChunks()[2].getSections()[chunkSectionIndex];
                nearbyChunkSections[3] = chunkPacketInfoAntiXray.getNearbyChunks()[3] == null ? EMPTY_SECTION : chunkPacketInfoAntiXray.getNearbyChunks()[3].getSections()[chunkSectionIndex];

                // Obfuscate all layers of the current chunk section except the upper one
                for (int y = 0; y < 15; y++) {
                    boolean[][] temp = current;
                    current = next;
                    next = nextNext;
                    nextNext = temp;
                    random.nextLayer();
                    obfuscateLayer(y, bitStorageReader, bitStorageWriter, solidTemp, obfuscateTemp, presetBlockStateBitsTemp, current, next, nextNext, nearbyChunkSections, random);
                }

                // Check if the chunk section above doesn't need obfuscation
                if (chunkSectionIndex == maxChunkSectionIndex || !chunkPacketInfoAntiXray.isWritten(chunkSectionIndex + 1) || chunkPacketInfoAntiXray.getPresetValues(chunkSectionIndex + 1) == null) {
                    // If so, obfuscate the upper layer of the current chunk section by reading blocks of the first layer from the chunk section above if it exists
                    LevelChunkSection aboveChunkSection;

                    if (chunkSectionIndex != chunk.getSectionsCount() - 1 && (aboveChunkSection = chunk.getSections()[chunkSectionIndex + 1]) != EMPTY_SECTION) {
                        boolean[][] temp = current;
                        current = next;
                        next = nextNext;
                        nextNext = temp;

                        for (int z = 0; z < 16; z++) {
                            for (int x = 0; x < 16; x++) {
                                if (isTransparent(aboveChunkSection, x, 0, z)) {
                                    current[z][x] = true;
                                }
                            }
                        }

                        // There is nothing to read anymore
                        bitStorageReader.setBits(0);
                        solid[0] = true;
                        random.nextLayer();
                        obfuscateLayer(15, bitStorageReader, bitStorageWriter, solid, obfuscateTemp, presetBlockStateBitsTemp, current, next, nextNext, nearbyChunkSections, random);
                    }
                } else {
                    // If not, initialize the reader and other stuff for the chunk section above to obfuscate the upper layer of the current chunk section
                    bitStorageReader.setBits(chunkPacketInfoAntiXray.getBits(chunkSectionIndex + 1));
                    bitStorageReader.setIndex(chunkPacketInfoAntiXray.getIndex(chunkSectionIndex + 1));
                    solidTemp = readPalette(chunkPacketInfoAntiXray.getPalette(chunkSectionIndex + 1), solid, solidGlobal);
                    obfuscateTemp = readPalette(chunkPacketInfoAntiXray.getPalette(chunkSectionIndex + 1), obfuscate, obfuscateGlobal);
                    boolean[][] temp = current;
                    current = next;
                    next = nextNext;
                    nextNext = temp;
                    random.nextLayer();
                    obfuscateLayer(15, bitStorageReader, bitStorageWriter, solidTemp, obfuscateTemp, presetBlockStateBitsTemp, current, next, nextNext, nearbyChunkSections, random);
                }

                bitStorageWriter.flush();
            }
        }

        chunkPacketInfoAntiXray.getChunkPacket().setReady(true);
    }

    private void obfuscateLayer(int y, BitStorageReader bitStorageReader, BitStorageWriter bitStorageWriter, boolean[] solid, boolean[] obfuscate, int[] presetBlockStateBits, boolean[][] current, boolean[][] next, boolean[][] nextNext, LevelChunkSection[] nearbyChunkSections, IntSupplier random) {
        // First block of first line
        int bits = bitStorageReader.read();

        if (nextNext[0][0] = !solid[bits]) {
            bitStorageWriter.skip();
            next[0][1] = true;
            next[1][0] = true;
        } else {
            if (current[0][0] || isTransparent(nearbyChunkSections[2], 0, y, 15) || isTransparent(nearbyChunkSections[0], 15, y, 0)) {
                bitStorageWriter.skip();
            } else {
                bitStorageWriter.write(presetBlockStateBits[random.getAsInt()]);
            }
        }

        if (!obfuscate[bits]) {
            next[0][0] = true;
        }

        // First line
        for (int x = 1; x < 15; x++) {
            bits = bitStorageReader.read();

            if (nextNext[0][x] = !solid[bits]) {
                bitStorageWriter.skip();
                next[0][x - 1] = true;
                next[0][x + 1] = true;
                next[1][x] = true;
            } else {
                if (current[0][x] || isTransparent(nearbyChunkSections[2], x, y, 15)) {
                    bitStorageWriter.skip();
                } else {
                    bitStorageWriter.write(presetBlockStateBits[random.getAsInt()]);
                }
            }

            if (!obfuscate[bits]) {
                next[0][x] = true;
            }
        }

        // Last block of first line
        bits = bitStorageReader.read();

        if (nextNext[0][15] = !solid[bits]) {
            bitStorageWriter.skip();
            next[0][14] = true;
            next[1][15] = true;
        } else {
            if (current[0][15] || isTransparent(nearbyChunkSections[2], 15, y, 15) || isTransparent(nearbyChunkSections[1], 0, y, 0)) {
                bitStorageWriter.skip();
            } else {
                bitStorageWriter.write(presetBlockStateBits[random.getAsInt()]);
            }
        }

        if (!obfuscate[bits]) {
            next[0][15] = true;
        }

        // All inner lines
        for (int z = 1; z < 15; z++) {
            // First block
            bits = bitStorageReader.read();

            if (nextNext[z][0] = !solid[bits]) {
                bitStorageWriter.skip();
                next[z][1] = true;
                next[z - 1][0] = true;
                next[z + 1][0] = true;
            } else {
                if (current[z][0] || isTransparent(nearbyChunkSections[0], 15, y, z)) {
                    bitStorageWriter.skip();
                } else {
                    bitStorageWriter.write(presetBlockStateBits[random.getAsInt()]);
                }
            }

            if (!obfuscate[bits]) {
                next[z][0] = true;
            }

            // All inner blocks
            for (int x = 1; x < 15; x++) {
                bits = bitStorageReader.read();

                if (nextNext[z][x] = !solid[bits]) {
                    bitStorageWriter.skip();
                    next[z][x - 1] = true;
                    next[z][x + 1] = true;
                    next[z - 1][x] = true;
                    next[z + 1][x] = true;
                } else {
                    if (current[z][x]) {
                        bitStorageWriter.skip();
                    } else {
                        bitStorageWriter.write(presetBlockStateBits[random.getAsInt()]);
                    }
                }

                if (!obfuscate[bits]) {
                    next[z][x] = true;
                }
            }

            // Last block
            bits = bitStorageReader.read();

            if (nextNext[z][15] = !solid[bits]) {
                bitStorageWriter.skip();
                next[z][14] = true;
                next[z - 1][15] = true;
                next[z + 1][15] = true;
            } else {
                if (current[z][15] || isTransparent(nearbyChunkSections[1], 0, y, z)) {
                    bitStorageWriter.skip();
                } else {
                    bitStorageWriter.write(presetBlockStateBits[random.getAsInt()]);
                }
            }

            if (!obfuscate[bits]) {
                next[z][15] = true;
            }
        }

        // First block of last line
        bits = bitStorageReader.read();

        if (nextNext[15][0] = !solid[bits]) {
            bitStorageWriter.skip();
            next[15][1] = true;
            next[14][0] = true;
        } else {
            if (current[15][0] || isTransparent(nearbyChunkSections[3], 0, y, 0) || isTransparent(nearbyChunkSections[0], 15, y, 15)) {
                bitStorageWriter.skip();
            } else {
                bitStorageWriter.write(presetBlockStateBits[random.getAsInt()]);
            }
        }

        if (!obfuscate[bits]) {
            next[15][0] = true;
        }

        // Last line
        for (int x = 1; x < 15; x++) {
            bits = bitStorageReader.read();

            if (nextNext[15][x] = !solid[bits]) {
                bitStorageWriter.skip();
                next[15][x - 1] = true;
                next[15][x + 1] = true;
                next[14][x] = true;
            } else {
                if (current[15][x] || isTransparent(nearbyChunkSections[3], x, y, 0)) {
                    bitStorageWriter.skip();
                } else {
                    bitStorageWriter.write(presetBlockStateBits[random.getAsInt()]);
                }
            }

            if (!obfuscate[bits]) {
                next[15][x] = true;
            }
        }

        // Last block of last line
        bits = bitStorageReader.read();

        if (nextNext[15][15] = !solid[bits]) {
            bitStorageWriter.skip();
            next[15][14] = true;
            next[14][15] = true;
        } else {
            if (current[15][15] || isTransparent(nearbyChunkSections[3], 15, y, 0) || isTransparent(nearbyChunkSections[1], 0, y, 15)) {
                bitStorageWriter.skip();
            } else {
                bitStorageWriter.write(presetBlockStateBits[random.getAsInt()]);
            }
        }

        if (!obfuscate[bits]) {
            next[15][15] = true;
        }
    }

    private boolean isTransparent(LevelChunkSection chunkSection, int x, int y, int z) {
        if (chunkSection == EMPTY_SECTION) {
            return true;
        }

        try {
            return !solidGlobal[GLOBAL_BLOCKSTATE_PALETTE.idFor(chunkSection.getBlockState(x, y, z), PaletteResize.noResizeExpected())];
        } catch (MissingPaletteEntryException e) {
            // Race condition / visibility issue / no happens-before relationship
            // We don't care and treat the block as transparent
            // Internal implementation details of PalettedContainer, LinearPalette, HashMapPalette, CrudeIncrementalIntIdentityHashBiMap, ... guarantee us that no (other) exceptions will occur
            return true;
        }
    }

    private boolean[] readPalette(Palette<BlockState> palette, boolean[] temp, boolean[] global) {
        if (palette instanceof GlobalPalette) {
            return global;
        }

        try {
            for (int i = 0; i < palette.getSize(); i++) {
                temp[i] = global[GLOBAL_BLOCKSTATE_PALETTE.idFor(palette.valueFor(i), PaletteResize.noResizeExpected())];
            }
        } catch (MissingPaletteEntryException e) {
            // Race condition / visibility issue / no happens-before relationship
            // We don't care because we at least see the state as it was when the chunk packet was created
            // Internal implementation details of PalettedContainer, LinearPalette, HashMapPalette, CrudeIncrementalIntIdentityHashBiMap, ... guarantee us that no (other) exceptions will occur until we have all the data that we need here
            // Since all palettes have a fixed initial maximum size and there is no internal restructuring and no values are removed from palettes, we are also guaranteed to see the data
        }

        return temp;
    }

    @Override
    public void onBlockChange(Level level, BlockPos blockPos, BlockState newBlockState, BlockState oldBlockState, int flags, int maxUpdateDepth) {
        if (oldBlockState != null && solidGlobal[GLOBAL_BLOCKSTATE_PALETTE.idFor(oldBlockState, PaletteResize.noResizeExpected())] && !solidGlobal[GLOBAL_BLOCKSTATE_PALETTE.idFor(newBlockState, PaletteResize.noResizeExpected())] && blockPos.getY() <= maxBlockHeightUpdatePosition) {
            updateNearbyBlocks(level, blockPos);
        }
    }

    @Override
    public void onPlayerLeftClickBlock(ServerPlayerGameMode serverPlayerGameMode, BlockPos blockPos, ServerboundPlayerActionPacket.Action action, Direction direction, int worldHeight, int sequence) {
        if (blockPos.getY() <= maxBlockHeightUpdatePosition) {
            updateNearbyBlocks(serverPlayerGameMode.level, blockPos);
        }
    }

    private void updateNearbyBlocks(Level level, BlockPos blockPos) {
        if (updateRadius >= 2) {
            BlockPos temp = blockPos.west();
            updateBlock(level, temp);
            updateBlock(level, temp.west());
            updateBlock(level, temp.below());
            updateBlock(level, temp.above());
            updateBlock(level, temp.north());
            updateBlock(level, temp.south());
            updateBlock(level, temp = blockPos.east());
            updateBlock(level, temp.east());
            updateBlock(level, temp.below());
            updateBlock(level, temp.above());
            updateBlock(level, temp.north());
            updateBlock(level, temp.south());
            updateBlock(level, temp = blockPos.below());
            updateBlock(level, temp.below());
            updateBlock(level, temp.north());
            updateBlock(level, temp.south());
            updateBlock(level, temp = blockPos.above());
            updateBlock(level, temp.above());
            updateBlock(level, temp.north());
            updateBlock(level, temp.south());
            updateBlock(level, temp = blockPos.north());
            updateBlock(level, temp.north());
            updateBlock(level, temp = blockPos.south());
            updateBlock(level, temp.south());
        } else if (updateRadius == 1) {
            updateBlock(level, blockPos.west());
            updateBlock(level, blockPos.east());
            updateBlock(level, blockPos.below());
            updateBlock(level, blockPos.above());
            updateBlock(level, blockPos.north());
            updateBlock(level, blockPos.south());
        } else {
            // Do nothing if updateRadius <= 0 (test mode)
        }
    }

    private void updateBlock(Level level, BlockPos blockPos) {
        BlockState blockState = level.getBlockStateIfLoaded(blockPos);

        if (blockState != null && obfuscateGlobal[GLOBAL_BLOCKSTATE_PALETTE.idFor(blockState, PaletteResize.noResizeExpected())]) {
            ((ServerLevel) level).getChunkSource().blockChanged(blockPos);
        }
    }

    @FunctionalInterface
    private interface LayeredIntSupplier extends IntSupplier {
        default void nextLayer() {

        }
    }
}
