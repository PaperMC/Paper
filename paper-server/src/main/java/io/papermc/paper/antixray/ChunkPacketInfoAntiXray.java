package io.papermc.paper.antixray;

import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

public final class ChunkPacketInfoAntiXray extends ChunkPacketInfo<BlockState> implements Runnable {

    private final ChunkPacketBlockControllerAntiXray chunkPacketBlockControllerAntiXray;
    private LevelChunk[] nearbyChunks;

    public ChunkPacketInfoAntiXray(ClientboundLevelChunkWithLightPacket chunkPacket, LevelChunk chunk, ChunkPacketBlockControllerAntiXray chunkPacketBlockControllerAntiXray) {
        super(chunkPacket, chunk);
        this.chunkPacketBlockControllerAntiXray = chunkPacketBlockControllerAntiXray;
    }

    public LevelChunk[] getNearbyChunks() {
        return nearbyChunks;
    }

    public void setNearbyChunks(LevelChunk... nearbyChunks) {
        this.nearbyChunks = nearbyChunks;
    }

    @Override
    public void run() {
        chunkPacketBlockControllerAntiXray.obfuscate(this);
    }
}
