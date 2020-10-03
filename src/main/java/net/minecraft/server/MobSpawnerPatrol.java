package net.minecraft.server;

import java.util.Random;

public class MobSpawnerPatrol implements MobSpawner {

    private int a;

    public MobSpawnerPatrol() {}

    @Override
    public int a(WorldServer worldserver, boolean flag, boolean flag1) {
        if (!flag) {
            return 0;
        } else if (!worldserver.getGameRules().getBoolean(GameRules.DO_PATROL_SPAWNING)) {
            return 0;
        } else {
            Random random = worldserver.random;

            --this.a;
            if (this.a > 0) {
                return 0;
            } else {
                this.a += 12000 + random.nextInt(1200);
                long i = worldserver.getDayTime() / 24000L;

                if (i >= 5L && worldserver.isDay()) {
                    if (random.nextInt(5) != 0) {
                        return 0;
                    } else {
                        int j = worldserver.getPlayers().size();

                        if (j < 1) {
                            return 0;
                        } else {
                            EntityHuman entityhuman = (EntityHuman) worldserver.getPlayers().get(random.nextInt(j));

                            if (entityhuman.isSpectator()) {
                                return 0;
                            } else if (worldserver.a(entityhuman.getChunkCoordinates(), 2)) {
                                return 0;
                            } else {
                                int k = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                                int l = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                                BlockPosition.MutableBlockPosition blockposition_mutableblockposition = entityhuman.getChunkCoordinates().i().e(k, 0, l);

                                if (!worldserver.isAreaLoaded(blockposition_mutableblockposition.getX() - 10, blockposition_mutableblockposition.getY() - 10, blockposition_mutableblockposition.getZ() - 10, blockposition_mutableblockposition.getX() + 10, blockposition_mutableblockposition.getY() + 10, blockposition_mutableblockposition.getZ() + 10)) {
                                    return 0;
                                } else {
                                    BiomeBase biomebase = worldserver.getBiome(blockposition_mutableblockposition);
                                    BiomeBase.Geography biomebase_geography = biomebase.t();

                                    if (biomebase_geography == BiomeBase.Geography.MUSHROOM) {
                                        return 0;
                                    } else {
                                        int i1 = 0;
                                        int j1 = (int) Math.ceil((double) worldserver.getDamageScaler(blockposition_mutableblockposition).b()) + 1;

                                        for (int k1 = 0; k1 < j1; ++k1) {
                                            ++i1;
                                            blockposition_mutableblockposition.p(worldserver.getHighestBlockYAt(HeightMap.Type.MOTION_BLOCKING_NO_LEAVES, blockposition_mutableblockposition).getY());
                                            if (k1 == 0) {
                                                if (!this.a(worldserver, blockposition_mutableblockposition, random, true)) {
                                                    break;
                                                }
                                            } else {
                                                this.a(worldserver, blockposition_mutableblockposition, random, false);
                                            }

                                            blockposition_mutableblockposition.o(blockposition_mutableblockposition.getX() + random.nextInt(5) - random.nextInt(5));
                                            blockposition_mutableblockposition.q(blockposition_mutableblockposition.getZ() + random.nextInt(5) - random.nextInt(5));
                                        }

                                        return i1;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    return 0;
                }
            }
        }
    }

    private boolean a(WorldServer worldserver, BlockPosition blockposition, Random random, boolean flag) {
        IBlockData iblockdata = worldserver.getType(blockposition);

        if (!SpawnerCreature.a((IBlockAccess) worldserver, blockposition, iblockdata, iblockdata.getFluid(), EntityTypes.PILLAGER)) {
            return false;
        } else if (!EntityMonsterPatrolling.b(EntityTypes.PILLAGER, (GeneratorAccess) worldserver, EnumMobSpawn.PATROL, blockposition, random)) {
            return false;
        } else {
            EntityMonsterPatrolling entitymonsterpatrolling = (EntityMonsterPatrolling) EntityTypes.PILLAGER.a((World) worldserver);

            if (entitymonsterpatrolling != null) {
                if (flag) {
                    entitymonsterpatrolling.setPatrolLeader(true);
                    entitymonsterpatrolling.eU();
                }

                entitymonsterpatrolling.setPosition((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ());
                entitymonsterpatrolling.prepare(worldserver, worldserver.getDamageScaler(blockposition), EnumMobSpawn.PATROL, (GroupDataEntity) null, (NBTTagCompound) null);
                worldserver.addAllEntities(entitymonsterpatrolling);
                return true;
            } else {
                return false;
            }
        }
    }
}
