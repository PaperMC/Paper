package com.destroystokyo.paper.antixray;

import net.minecraft.server.Chunk;
import net.minecraft.server.DataPalette;
import net.minecraft.server.PacketPlayOutMapChunk;

public class ChunkPacketInfo<T> {

    private final PacketPlayOutMapChunk packetPlayOutMapChunk;
    private final Chunk chunk;
    private final int chunkSectionSelector;
    private byte[] data;
    private final int[] bitsPerObject = new int[16];
    private final Object[] dataPalettes = new Object[16];
    private final int[] dataBitsIndexes = new int[16];
    private final Object[][] predefinedObjects = new Object[16][];

    public ChunkPacketInfo(PacketPlayOutMapChunk packetPlayOutMapChunk, Chunk chunk, int chunkSectionSelector) {
        this.packetPlayOutMapChunk = packetPlayOutMapChunk;
        this.chunk = chunk;
        this.chunkSectionSelector = chunkSectionSelector;
    }

    public PacketPlayOutMapChunk getPacketPlayOutMapChunk() {
        return packetPlayOutMapChunk;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public int getChunkSectionSelector() {
        return chunkSectionSelector;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getBitsPerObject(int chunkSectionIndex) {
        return bitsPerObject[chunkSectionIndex];
    }

    public void setBitsPerObject(int chunkSectionIndex, int bitsPerObject) {
        this.bitsPerObject[chunkSectionIndex] = bitsPerObject;
    }

    @SuppressWarnings("unchecked")
    public DataPalette<T> getDataPalette(int chunkSectionIndex) {
        return (DataPalette<T>) dataPalettes[chunkSectionIndex];
    }

    public void setDataPalette(int chunkSectionIndex, DataPalette<T> dataPalette) {
        dataPalettes[chunkSectionIndex] = dataPalette;
    }

    public int getDataBitsIndex(int chunkSectionIndex) {
        return dataBitsIndexes[chunkSectionIndex];
    }

    public void setDataBitsIndex(int chunkSectionIndex, int dataBitsIndex) {
        dataBitsIndexes[chunkSectionIndex] = dataBitsIndex;
    }

    @SuppressWarnings("unchecked")
    public T[] getPredefinedObjects(int chunkSectionIndex) {
        return (T[]) predefinedObjects[chunkSectionIndex];
    }

    public void setPredefinedObjects(int chunkSectionIndex, T[] predefinedObjects) {
        this.predefinedObjects[chunkSectionIndex] = predefinedObjects;
    }

    public boolean isWritten(int chunkSectionIndex) {
        return bitsPerObject[chunkSectionIndex] != 0;
    }
}
