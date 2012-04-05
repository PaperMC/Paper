package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

class PlayerInstance {

    private List b;
    private int chunkX;
    private int chunkZ;
    private ChunkCoordIntPair location;
    private short[] dirtyBlocks;
    private int dirtyCount;
    private int h;

    final PlayerManager playerManager;

    public PlayerInstance(PlayerManager playermanager, int i, int j) {
        this.playerManager = playermanager;
        this.b = new ArrayList();
        this.dirtyBlocks = new short[64];
        this.dirtyCount = 0;
        this.chunkX = i;
        this.chunkZ = j;
        this.location = new ChunkCoordIntPair(i, j);
        playermanager.a().chunkProviderServer.getChunkAt(i, j);
    }

    public void a(EntityPlayer entityplayer) {
        if (this.b.contains(entityplayer)) {
            throw new IllegalStateException("Failed to add player. " + entityplayer + " already is in chunk " + this.chunkX + ", " + this.chunkZ);
        } else {
            // CraftBukkit start
            if (entityplayer.playerChunkCoordIntPairs.add(this.location)) {
                entityplayer.netServerHandler.sendPacket(new Packet50PreChunk(this.location.x, this.location.z, true));
            }
            // CraftBukkit end

            this.b.add(entityplayer);
            entityplayer.chunkCoordIntPairQueue.add(this.location);
        }
    }

    public void b(EntityPlayer entityplayer) {
        if (this.b.contains(entityplayer)) {
            this.b.remove(entityplayer);
            if (this.b.size() == 0) {
                long i = (long) this.chunkX + 2147483647L | (long) this.chunkZ + 2147483647L << 32;

                PlayerManager.a(this.playerManager).remove(i);
                if (this.dirtyCount > 0) {
                    PlayerManager.b(this.playerManager).remove(this);
                }

                this.playerManager.a().chunkProviderServer.queueUnload(this.chunkX, this.chunkZ);
            }

            entityplayer.chunkCoordIntPairQueue.remove(this.location);
            // CraftBukkit - contains -> remove -- TODO VERIFY!!!!
            if (entityplayer.playerChunkCoordIntPairs.remove(this.location)) {
                entityplayer.netServerHandler.sendPacket(new Packet50PreChunk(this.chunkX, this.chunkZ, false));
            }
        }
    }

    public void a(int i, int j, int k) {
        if (this.dirtyCount == 0) {
            PlayerManager.b(this.playerManager).add(this);
        }

        this.h |= 1 << (j >> 4);
        if (this.dirtyCount < 64) {
            short short1 = (short) (i << 12 | k << 8 | j);

            for (int l = 0; l < this.dirtyCount; ++l) {
                if (this.dirtyBlocks[l] == short1) {
                    return;
                }
            }

            this.dirtyBlocks[this.dirtyCount++] = short1;
        }
    }

    public void sendAll(Packet packet) {
        for (int i = 0; i < this.b.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) this.b.get(i);

            if (entityplayer.playerChunkCoordIntPairs.contains(this.location) && !entityplayer.chunkCoordIntPairQueue.contains(this.location)) {
                entityplayer.netServerHandler.sendPacket(packet);
            }
        }
    }

    public void a() {
        WorldServer worldserver = this.playerManager.a();

        if (this.dirtyCount != 0) {
            int i;
            int j;
            int k;

            if (this.dirtyCount == 1) {
                i = this.chunkX * 16 + (this.dirtyBlocks[0] >> 12 & 15);
                j = this.dirtyBlocks[0] & 255;
                k = this.chunkZ * 16 + (this.dirtyBlocks[0] >> 8 & 15);
                this.sendAll(new Packet53BlockChange(i, j, k, worldserver));
                if (worldserver.isTileEntity(i, j, k)) {
                    this.sendTileEntity(worldserver.getTileEntity(i, j, k));
                }
            } else {
                int l;

                if (this.dirtyCount == 64) {
                    i = this.chunkX * 16;
                    j = this.chunkZ * 16;
                    this.sendAll(new Packet51MapChunk(worldserver.getChunkAt(this.chunkX, this.chunkZ), (this.h == 0xFFFF), this.h)); // CraftBukkit - send everything (including biome) if all sections flagged

                    for (k = 0; k < 16; ++k) {
                        if ((this.h & 1 << k) != 0) {
                            l = k << 4;
                            List list = worldserver.getTileEntities(i, l, j, i + 16, l + 16, j + 16);

                            for (int i1 = 0; i1 < list.size(); ++i1) {
                                this.sendTileEntity((TileEntity) list.get(i1));
                            }
                        }
                    }
                } else {
                    this.sendAll(new Packet52MultiBlockChange(this.chunkX, this.chunkZ, this.dirtyBlocks, this.dirtyCount, worldserver));

                    for (i = 0; i < this.dirtyCount; ++i) {
                        j = this.chunkX * 16 + (this.dirtyBlocks[i] >> 12 & 15);
                        k = this.dirtyBlocks[i] & 255;
                        l = this.chunkZ * 16 + (this.dirtyBlocks[i] >> 8 & 15);
                        if (worldserver.isTileEntity(j, k, l)) {
                            this.sendTileEntity(worldserver.getTileEntity(j, k, l));
                        }
                    }
                }
            }

            this.dirtyCount = 0;
            this.h = 0;
        }
    }

    private void sendTileEntity(TileEntity tileentity) {
        if (tileentity != null) {
            Packet packet = tileentity.d();

            if (packet != null) {
                this.sendAll(packet);
            }
        }
    }
}
