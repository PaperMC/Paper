package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

class PlayerInstance {

    private List b;
    private int chunkX;
    private int chunkZ;
    private ChunkCoordIntPair e;
    private short[] dirtyBlocks;
    private int dirtyCount;
    private int h;
    private int i;
    private int j;
    private int k;
    private int l;
    private int m;

    final PlayerManager playerManager;

    public PlayerInstance(PlayerManager playermanager, int i, int j) {
        this.playerManager = playermanager;
        this.b = new ArrayList();
        this.dirtyBlocks = new short[10];
        this.dirtyCount = 0;
        this.chunkX = i;
        this.chunkZ = j;
        this.e = new ChunkCoordIntPair(i, j);

        // CraftBukkit
        playermanager.world.chunkProviderServer.getChunkAt(i, j);
    }

    public void a(EntityPlayer entityplayer) {
        if (this.b.contains(entityplayer)) {
            throw new IllegalStateException("Failed to add player. " + entityplayer + " already is in chunk " + this.chunkX + ", " + this.chunkZ);
        } else {
            // CraftBukkit start
            if (entityplayer.g.add(this.e)) {
                entityplayer.netServerHandler.sendPacket(new Packet50PreChunk(this.e.x, this.e.z, true));
            }
            // CraftBukkit end

            this.b.add(entityplayer);
            entityplayer.f.add(this.e);
        }
    }

    public void b(EntityPlayer entityplayer) {
        if (!this.b.contains(entityplayer)) {
            // CraftBukkit - reduce console spam under certain conditions
            // (new IllegalStateException("Failed to remove player. " + entityplayer + " isn\'t in chunk " + this.chunkX + ", " + this.chunkZ)).printStackTrace();
        } else {
            this.b.remove(entityplayer);
            if (this.b.size() == 0) {
                long i = (long) this.chunkX + 2147483647L | (long) this.chunkZ + 2147483647L << 32;

                PlayerManager.b(this.playerManager).b(i);
                if (this.dirtyCount > 0) {
                    PlayerManager.c(this.playerManager).remove(this);
                }

                // CraftBukkit
                ((WorldServer) entityplayer.world).chunkProviderServer.queueUnload(this.chunkX, this.chunkZ);
            }

            entityplayer.f.remove(this.e);
            // CraftBukkit - contains -> remove
            if (entityplayer.g.remove(this.e)) {
                entityplayer.netServerHandler.sendPacket(new Packet50PreChunk(this.chunkX, this.chunkZ, false));
            }
        }
    }

    public void a(int i, int j, int k) {
        if (this.dirtyCount == 0) {
            PlayerManager.c(this.playerManager).add(this);
            this.h = this.i = i;
            this.j = this.k = j;
            this.l = this.m = k;
        }

        if (this.h > i) {
            this.h = i;
        }

        if (this.i < i) {
            this.i = i;
        }

        if (this.j > j) {
            this.j = j;
        }

        if (this.k < j) {
            this.k = j;
        }

        if (this.l > k) {
            this.l = k;
        }

        if (this.m < k) {
            this.m = k;
        }

        if (this.dirtyCount < 10) {
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

            if (entityplayer.g.contains(this.e)) {
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
                i = this.chunkX * 16 + this.h;
                j = this.j;
                k = this.chunkZ * 16 + this.l;

                // CraftBukkit start
                this.sendAll(new Packet53BlockChange(i, j, k, this.playerManager.world));
                if (Block.isTileEntity[this.playerManager.world.getTypeId(i, j, k)]) {
                    this.sendTileEntity(this.playerManager.world.getTileEntity(i, j, k));
                }
                // CraftBukkit end
            } else {
                int l;

                if (this.dirtyCount == 10) {
                    this.j = this.j / 2 * 2;
                    this.k = (this.k / 2 + 1) * 2;
                    i = this.h + this.chunkX * 16;
                    j = this.j;
                    k = this.l + this.chunkZ * 16;
                    l = this.i - this.h + 1;
                    int i1 = this.k - this.j + 2;
                    int j1 = this.m - this.l + 1;

                    // CraftBukkit start
                    this.sendAll(new Packet51MapChunk(i, j, k, l, i1, j1, this.playerManager.world));
                    List list = this.playerManager.world.getTileEntities(i, j, k, i + l, j + i1, k + j1);
                    // CraftBukkit end

                    for (int k1 = 0; k1 < list.size(); ++k1) {
                        this.sendTileEntity((TileEntity) list.get(k1));
                    }
                } else {
                    // CraftBukkit
                    this.sendAll(new Packet52MultiBlockChange(this.chunkX, this.chunkZ, this.dirtyBlocks, this.dirtyCount, this.playerManager.world));

                    for (i = 0; i < this.dirtyCount; ++i) {
                        j = this.chunkX * 16 + (this.dirtyCount >> 12 & 15);
                        k = this.dirtyCount & 255;
                        l = this.chunkZ * 16 + (this.dirtyCount >> 8 & 15);

                        // CraftBukkit start
                        if (Block.isTileEntity[this.playerManager.world.getTypeId(j, k, l)]) {
                            // System.out.println("Sending!"); // CraftBukkit
                            this.sendTileEntity(this.playerManager.world.getTileEntity(j, k, l));
                        }
                        // CraftBukkit end
                    }
                }
            }

            this.dirtyCount = 0;
        }
    }

    private void sendTileEntity(TileEntity tileentity) {
        if (tileentity != null) {
            Packet packet = tileentity.e();

            if (packet != null) {
                this.sendAll(packet);
            }
        }
    }
}
