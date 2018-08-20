package com.destroystokyo.paper.antixray;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.Chunk;
import net.minecraft.server.ChunkSection;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IChunkAccess;
import net.minecraft.server.PacketPlayOutMapChunk;
import net.minecraft.server.PlayerInteractManager;
import net.minecraft.server.World;

public class ChunkPacketBlockController {

    public static final ChunkPacketBlockController NO_OPERATION_INSTANCE = new ChunkPacketBlockController();

    protected ChunkPacketBlockController() {

    }

    public IBlockData[] getPredefinedBlockData(World world, IChunkAccess chunk, ChunkSection chunkSection, boolean initializeBlocks) {
        return null;
    }

    public ChunkPacketInfo<IBlockData> getChunkPacketInfo(PacketPlayOutMapChunk packetPlayOutMapChunk, Chunk chunk, int chunkSectionSelector) {
        return null;
    }

    public void modifyBlocks(PacketPlayOutMapChunk packetPlayOutMapChunk, ChunkPacketInfo<IBlockData> chunkPacketInfo) {
        packetPlayOutMapChunk.setReady(true);
    }

    public void onBlockChange(World world, BlockPosition blockPosition, IBlockData newBlockData, IBlockData oldBlockData, int flag) {

    }

    public void onPlayerLeftClickBlock(PlayerInteractManager playerInteractManager, BlockPosition blockPosition, EnumDirection enumDirection) {

    }
}
