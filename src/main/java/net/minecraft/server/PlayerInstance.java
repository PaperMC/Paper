package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class PlayerInstance {

    private final List b;
    private final ChunkCoordIntPair location;
    private short[] dirtyBlocks;
    private int dirtyCount;
    private int f;

    final PlayerManager playerManager;

    public PlayerInstance(PlayerManager playermanager, int i, int j) {
        this.playerManager = playermanager;
        this.b = new ArrayList();
        this.dirtyBlocks = new short[64];
        this.dirtyCount = 0;
        this.location = new ChunkCoordIntPair(i, j);
        playermanager.a().chunkProviderServer.getChunkAt(i, j);
    }

    public void a(EntityPlayer entityplayer) {
        if (this.b.contains(entityplayer)) {
            throw new IllegalStateException("Failed to add player. " + entityplayer + " already is in chunk " + this.location.x + ", " + this.location.z);
        } else {
            this.b.add(entityplayer);
            entityplayer.chunkCoordIntPairQueue.add(this.location);
        }
    }

    public void b(EntityPlayer entityplayer) {
        if (this.b.contains(entityplayer)) {
            entityplayer.netServerHandler.sendPacket(new Packet51MapChunk(PlayerManager.a(this.playerManager).getChunkAt(this.location.x, this.location.z), true, 0));
            this.b.remove(entityplayer);
            entityplayer.chunkCoordIntPairQueue.remove(this.location);
            if (this.b.isEmpty()) {
                long i = (long) this.location.x + 2147483647L | (long) this.location.z + 2147483647L << 32;

                PlayerManager.b(this.playerManager).remove(i);
                if (this.dirtyCount > 0) {
                    PlayerManager.c(this.playerManager).remove(this);
                }

                this.playerManager.a().chunkProviderServer.queueUnload(this.location.x, this.location.z);
            }
        }
    }

    public void a(int i, int j, int k) {
        if (this.dirtyCount == 0) {
            PlayerManager.c(this.playerManager).add(this);
        }

        this.f |= 1 << (j >> 4);
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
        Iterator iterator = this.b.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer) iterator.next();

            if (!entityplayer.chunkCoordIntPairQueue.contains(this.location)) {
                entityplayer.netServerHandler.sendPacket(packet);
            }
        }
    }

    public void a() {
        if (this.dirtyCount != 0) {
            int i;
            int j;
            int k;

            if (this.dirtyCount == 1) {
                i = this.location.x * 16 + (this.dirtyBlocks[0] >> 12 & 15);
                j = this.dirtyBlocks[0] & 255;
                k = this.location.z * 16 + (this.dirtyBlocks[0] >> 8 & 15);
                this.sendAll(new Packet53BlockChange(i, j, k, PlayerManager.a(this.playerManager)));
                if (PlayerManager.a(this.playerManager).isTileEntity(i, j, k)) {
                    this.sendTileEntity(PlayerManager.a(this.playerManager).getTileEntity(i, j, k));
                }
            } else {
                int l;

                if (this.dirtyCount == 64) {
                    i = this.location.x * 16;
                    j = this.location.z * 16;
                    this.sendAll(new Packet51MapChunk(PlayerManager.a(this.playerManager).getChunkAt(this.location.x, this.location.z), (this.f == 0xFFFF), this.f)); // CraftBukkit - send everything (including biome) if all sections flagged

                    for (k = 0; k < 16; ++k) {
                        if ((this.f & 1 << k) != 0) {
                            l = k << 4;
                            List list = PlayerManager.a(this.playerManager).getTileEntities(i, l, j, i + 16, l + 16, j + 16);
                            Iterator iterator = list.iterator();

                            while (iterator.hasNext()) {
                                TileEntity tileentity = (TileEntity) iterator.next();

                                this.sendTileEntity(tileentity);
                            }
                        }
                    }
                } else {
                    this.sendAll(new Packet52MultiBlockChange(this.location.x, this.location.z, this.dirtyBlocks, this.dirtyCount, PlayerManager.a(this.playerManager)));

                    for (i = 0; i < this.dirtyCount; ++i) {
                        j = this.location.x * 16 + (this.dirtyBlocks[i] >> 12 & 15);
                        k = this.dirtyBlocks[i] & 255;
                        l = this.location.z * 16 + (this.dirtyBlocks[i] >> 8 & 15);
                        if (PlayerManager.a(this.playerManager).isTileEntity(j, k, l)) {
                            this.sendTileEntity(PlayerManager.a(this.playerManager).getTileEntity(j, k, l));
                        }
                    }
                }
            }

            this.dirtyCount = 0;
            this.f = 0;
        }
    }

    private void sendTileEntity(TileEntity tileentity) {
        if (tileentity != null) {
            Packet packet = tileentity.l();

            if (packet != null) {
                this.sendAll(packet);
            }
        }
    }

    static ChunkCoordIntPair a(PlayerInstance playerinstance) {
        return playerinstance.location;
    }

    static List b(PlayerInstance playerinstance) {
        return playerinstance.b;
    }
}
