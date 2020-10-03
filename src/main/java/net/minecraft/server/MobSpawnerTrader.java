package net.minecraft.server;

import java.util.Iterator;
import java.util.Optional;
import java.util.Random;
import javax.annotation.Nullable;

public class MobSpawnerTrader implements MobSpawner {

    private final Random a = new Random();
    private final IWorldDataServer b;
    private int c;
    private int d;
    private int e;

    public MobSpawnerTrader(IWorldDataServer iworlddataserver) {
        this.b = iworlddataserver;
        this.c = 1200;
        this.d = iworlddataserver.v();
        this.e = iworlddataserver.w();
        if (this.d == 0 && this.e == 0) {
            this.d = 24000;
            iworlddataserver.g(this.d);
            this.e = 25;
            iworlddataserver.h(this.e);
        }

    }

    @Override
    public int a(WorldServer worldserver, boolean flag, boolean flag1) {
        if (!worldserver.getGameRules().getBoolean(GameRules.DO_TRADER_SPAWNING)) {
            return 0;
        } else if (--this.c > 0) {
            return 0;
        } else {
            this.c = 1200;
            this.d -= 1200;
            this.b.g(this.d);
            if (this.d > 0) {
                return 0;
            } else {
                this.d = 24000;
                if (!worldserver.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
                    return 0;
                } else {
                    int i = this.e;

                    this.e = MathHelper.clamp(this.e + 25, 25, 75);
                    this.b.h(this.e);
                    if (this.a.nextInt(100) > i) {
                        return 0;
                    } else if (this.a(worldserver)) {
                        this.e = 25;
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        }
    }

    private boolean a(WorldServer worldserver) {
        EntityPlayer entityplayer = worldserver.q_();

        if (entityplayer == null) {
            return true;
        } else if (this.a.nextInt(10) != 0) {
            return false;
        } else {
            BlockPosition blockposition = entityplayer.getChunkCoordinates();
            boolean flag = true;
            VillagePlace villageplace = worldserver.y();
            Optional<BlockPosition> optional = villageplace.c(VillagePlaceType.s.c(), (blockposition1) -> {
                return true;
            }, blockposition, 48, VillagePlace.Occupancy.ANY);
            BlockPosition blockposition1 = (BlockPosition) optional.orElse(blockposition);
            BlockPosition blockposition2 = this.a((IWorldReader) worldserver, blockposition1, 48);

            if (blockposition2 != null && this.a(worldserver, blockposition2)) {
                if (worldserver.i(blockposition2).equals(Optional.of(Biomes.THE_VOID))) {
                    return false;
                }

                EntityVillagerTrader entityvillagertrader = (EntityVillagerTrader) EntityTypes.WANDERING_TRADER.spawnCreature(worldserver, (NBTTagCompound) null, (IChatBaseComponent) null, (EntityHuman) null, blockposition2, EnumMobSpawn.EVENT, false, false, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.NATURAL); // CraftBukkit

                if (entityvillagertrader != null) {
                    for (int i = 0; i < 2; ++i) {
                        this.a(worldserver, entityvillagertrader, 4);
                    }

                    this.b.a(entityvillagertrader.getUniqueID());
                    entityvillagertrader.u(48000);
                    entityvillagertrader.g(blockposition1);
                    entityvillagertrader.a(blockposition1, 16);
                    return true;
                }
            }

            return false;
        }
    }

    private void a(WorldServer worldserver, EntityVillagerTrader entityvillagertrader, int i) {
        BlockPosition blockposition = this.a((IWorldReader) worldserver, entityvillagertrader.getChunkCoordinates(), i);

        if (blockposition != null) {
            EntityLlamaTrader entityllamatrader = (EntityLlamaTrader) EntityTypes.TRADER_LLAMA.spawnCreature(worldserver, (NBTTagCompound) null, (IChatBaseComponent) null, (EntityHuman) null, blockposition, EnumMobSpawn.EVENT, false, false, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.NATURAL); // CraftBukkit

            if (entityllamatrader != null) {
                entityllamatrader.setLeashHolder(entityvillagertrader, true);
            }
        }
    }

    @Nullable
    private BlockPosition a(IWorldReader iworldreader, BlockPosition blockposition, int i) {
        BlockPosition blockposition1 = null;

        for (int j = 0; j < 10; ++j) {
            int k = blockposition.getX() + this.a.nextInt(i * 2) - i;
            int l = blockposition.getZ() + this.a.nextInt(i * 2) - i;
            int i1 = iworldreader.a(HeightMap.Type.WORLD_SURFACE, k, l);
            BlockPosition blockposition2 = new BlockPosition(k, i1, l);

            if (SpawnerCreature.a(EntityPositionTypes.Surface.ON_GROUND, iworldreader, blockposition2, EntityTypes.WANDERING_TRADER)) {
                blockposition1 = blockposition2;
                break;
            }
        }

        return blockposition1;
    }

    private boolean a(IBlockAccess iblockaccess, BlockPosition blockposition) {
        Iterator iterator = BlockPosition.a(blockposition, blockposition.b(1, 2, 1)).iterator();

        BlockPosition blockposition1;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            blockposition1 = (BlockPosition) iterator.next();
        } while (iblockaccess.getType(blockposition1).getCollisionShape(iblockaccess, blockposition1).isEmpty());

        return false;
    }
}
